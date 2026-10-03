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
# IDEA : HASH MAP {(x, y) : how many times x and y sit next to each other}
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
