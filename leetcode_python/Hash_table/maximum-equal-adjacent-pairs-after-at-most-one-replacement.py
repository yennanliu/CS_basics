"""

4066. Maximum Equal Adjacent Pairs After at Most One Replacement
Medium

You are given a 1-indexed integer array nums.

You can choose two distinct values x and y and perform the following
operation at most once:

    Replace every occurrence of x in nums with y.

Return the maximum possible number of pairs of adjacent elements that
are equal after performing the operation.


Example 1:

Input: nums = [1,2,3,2]

Output: 2

Explanation:

One optimal solution is to choose x = 3 and y = 2.
The resulting array is [1, 2, 2, 2].
There are 2 pairs of adjacent elements that are equal:
(nums[2], nums[3]) and (nums[3], nums[4]).
Therefore, the answer is 2.

Example 2:

Input: nums = [1,2,1,2,1]

Output: 4

Explanation:

One optimal solution is to choose x = 1 and y = 2.
The resulting array is [2, 2, 2, 2, 2].
There are 4 pairs of adjacent elements that are equal:
(nums[1], nums[2]), (nums[2], nums[3]), (nums[3], nums[4]), and (nums[4], nums[5]).
Therefore, the answer is 4.

Example 3:

Input: nums = [1,1,1]

Output: 2

Explanation:

One optimal solution is to perform no operation.
Thus, the resulting array is [1, 1, 1].
There are 2 pairs of adjacent elements that are equal:
(nums[1], nums[2]) and (nums[2], nums[3]).
Therefore, the answer is 2.


Constraints:

2 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
class Solution(object):
    def maxEqualAdjacentPairs(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        pass


# V0-1
# IDEA: HASH MAP + BASE LINE (gemini)
"""

1. 

CORE IDEA:


單次掃描雜湊法（Hash Map, O(N))

->

我們只需要遍歷一次陣列：

    - 如果 nums[i] == nums[i+1]：代表原本就相等，基礎計數器 base_equal += 1。

    - 如果 nums[i] != nums[i+1]：把這對數字排序為 (min(x, y), max(x, y)) 存入 Hash Map 進行計數，記錄哪一對不相等的組合出現最多次。



---


2. KEY IDEA:


    ```
    總相等對數 =  原本就相等的對數 + max(任意相異相鄰對 (x,y) 出現的最多次數)
    ```


- 原本就相等的相鄰對，替換後「必定保持相等」：
     - 若原本就有 nums[i] == nums[i+1] == x，即使我們把 $x$ 換成 $y$，它們也會同時變成 $y$，依然相等！

- 替換 $x \to y$「只會讓原本是 $(x, y)$ 或 $(y, x)$ 的相鄰對變成相等」：
    - 任何其他組合（例如 $(a, b)$）都不會因為替換 $x \to y$ 而變成相等。


"""
from collections import Counter


class Solution(object):

  def maxEqualAdjacentPairs(self, nums):
    """:type nums: List[int] :rtype: int"""
    if not nums or len(nums) <= 1:
      return 0

    base_equal = 0
    unequal_pair_cnt = Counter()
    max_newly_equal = 0

    # 1. 單次掃描所有相鄰對
    for i in range(len(nums) - 1):
      x, y = nums[i], nums[i + 1]

      if x == y:
        # 原本就相等的對，替換後必定依然相等
        base_equal += 1
      else:
        # 不相等的對：用 (min, max) 確保 (x, y) 和 (y, x) 視為同一組組合
        pair = (min(x, y), max(x, y))
        unequal_pair_cnt[pair] += 1
        max_newly_equal = max(max_newly_equal, unequal_pair_cnt[pair])

    # 2. 最終答案 = 基礎相等對數 + 透過替換獲得的最大新相等對數
    return base_equal + max_newly_equal


# V0-2
# IDEA: HASH MAP (gpt)
from collections import defaultdict


class Solution(object):
    def maxEqualAdjacentPairs(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """

        # Number of pairs that are already equal.
        baseline = 0

        # {unordered_pair: number_of_adjacent_cross_pairs}
        cross_pairs = defaultdict(int)

        for i in range(1, len(nums)):

            a = nums[i - 1]
            b = nums[i]

            if a == b:
                baseline += 1
            else:
                # (x, y) and (y, x) are the same pair
                # because replacing one value can make them equal.
                if a < b:
                    key = (a, b)
                else:
                    key = (b, a)

                cross_pairs[key] += 1

        # We can choose to do nothing.
        ans = baseline

        # Replacing one value of the pair with the other
        # turns every cross pair into an equal pair.
        for gain in cross_pairs.values():
            ans = max(ans, baseline + gain)

        return ans


# V0-3
# IDEA : HASH MAP {(x, y) : how many times x and y sit next to each other} (claude)
#
#   the brute force (try every distinct (x, y), replace, re-count) is
#   O(k^2 * n) and times out, so ask instead : what does ONE replacement
#   "x -> y" actually change ?
#
#     - a pair (x, y) or (y, x)      -> becomes (y, y)   : +1 equal pair
#     - a pair (x, x)                -> becomes (y, y)   : was equal, still equal
#     - a pair (y, y) or (a, b)      -> untouched
#     - a pair (x, a), a != y        -> becomes (y, a)   : was unequal, still unequal
#
#   so NO equal pair is ever lost, and the gain is EXACTLY the number of
#   adjacent positions holding one x and one y. the answer is therefore
#
#       (adjacent pairs already equal) + max over x != y of (adjacent {x, y} pairs)
#
#   and "at most once" is the case where that max is 0 (e.g. nums = [1,1,1]).
#
#   e.g. nums = [1,2,3,2] -> already equal = 0
#        adjacent pairs : {1,2} x1, {2,3} x2  -> best gain = 2 -> 0 + 2 = 2
#
# time = O(n), space = O(n)
from collections import defaultdict


class Solution(object):
    def maxEqualAdjacentPairs(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # edge case: a single element has no adjacent pair
        if not nums or len(nums) < 2:
            return 0

        # pairs that are equal before any operation (never lost, see IDEA)
        already_equal = 0

        # (smaller, larger) -> how many adjacent positions hold exactly these two values
        pair_count = defaultdict(int)

        for i in range(len(nums) - 1):
            left = nums[i]
            right = nums[i + 1]

            if left == right:
                already_equal += 1
            else:
                # NOTE !!! normalise the order so (2,3) and (3,2) are counted
                #          together: replacing either value with the other
                #          fixes BOTH orientations
                if left < right:
                    pair_key = (left, right)
                else:
                    pair_key = (right, left)
                pair_count[pair_key] += 1

        # the best replacement x -> y gains one equal pair per adjacent {x, y};
        # 0 when every adjacent pair is already equal (do no operation)
        best_gain = 0
        for pair_key, count in pair_count.items():
            if count > best_gain:
                best_gain = count

        return already_equal + best_gain
