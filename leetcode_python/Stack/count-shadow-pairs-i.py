"""

4054. Count Shadow Pairs I
Medium

You are given an integer array nums of length n.

A pair of indices (i, j) is called a shadow pair if all of the following
conditions are satisfied:

0 <= i < j < n

nums[i] < nums[j]

There does not exist an index k such that i < k < j and
nums[k] < nums[i] < nums[j].

Return the total number of shadow pairs.


Example 1:

Input: nums = [3,1,4,1,5]

Output: 3

Explanation:

(1, 2), (1, 4) and (3, 4) are shadow pairs.
for (1, 4) : nums[2] = 4 and nums[3] = 1 are not smaller than nums[1] = 1.

Example 2:

Input: nums = [6,7,6,6,7]

Output: 4

Explanation:

(0, 1), (0, 4), (2, 4) and (3, 4) are shadow pairs.
for (2, 4) : nums[3] = 6 is not smaller than nums[2] = 6.

Example 3:

Input: nums = [1,2,3,4]

Output: 6

Explanation:

all 6 pairs qualify - nothing sitting between i and j is ever smaller
than nums[i].


Constraints:

3 <= n == nums.length <= 10^5

1 <= nums[i] <= 10^9

"""


# V0
class Solution(object):
    def shadowPairs(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        pass



# V0-1
# IDEA : MONOTONIC STACK (NEXT STRICTLY SMALLER) + THE EQUAL-VALUE CHAIN
#
#   the killing condition only ever mentions nums[i], so for a FIXED i the
#   pair (i, j) dies the moment some k in (i, j) has nums[k] < nums[i].
#
#   so let nxt[i] = the first index > i whose value is STRICTLY smaller
#   than nums[i] (n if there is none). then :
#
#     - every j in (i, nxt[i]) has nums[j] >= nums[i], so nothing between
#       i and j can kill the pair -> it only needs nums[j] > nums[i]
#     - j = nxt[i] itself can never pair, since nums[j] < nums[i]
#     - no j past nxt[i] can pair, since k = nxt[i] now sits strictly inside
#
#   which turns the count into arithmetic :
#
#     pairs(i) = (nxt[i] - i - 1) - #{ j in (i, nxt[i]) : nums[j] == nums[i] }
#
#   and the EQUAL ones chain : if the next index ne with nums[ne] == nums[i]
#   lands before nxt[i], then nxt[ne] == nxt[i] (same value, and nothing
#   smaller in between), so eq[i] = 1 + eq[ne].
#
# NOTE !!! the stack pops on `>=`, which is what makes nxt[i] the first
#          STRICTLY smaller one - popping on `>` would stop at an equal
#          value and shrink every window.
#
#   e.g. nums = [6,7,6,6,7], i = 0 : nxt[0] = 5 -> 4 candidates, of which
#        indices 2 and 3 are equal to 6 -> 4 - 2 = 2 pairs : (0,1) and (0,4)
#
# time = O(n), space = O(n)
class Solution(object):
    def shadowPairs(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # edge
        if not nums or len(nums) <= 1:
            return 0

        n = len(nums)

        nxt = [n] * n       # first idx > i whose value is strictly smaller
        eq = [0] * n        # how many nums[j] == nums[i] sit inside (i, nxt[i])
        last_same = {}      # value -> the smallest idx > i seen so far

        stack = []          # indices, values strictly increasing towards the top

        res = 0

        # right -> left, so the stack already holds everything after i
        for i in range(n - 1, -1, -1):
            while stack and nums[stack[-1]] >= nums[i]:
                stack.pop()

            nxt[i] = stack[-1] if stack else n

            ne = last_same.get(nums[i], n)
            eq[i] = 1 + eq[ne] if ne < nxt[i] else 0

            res += (nxt[i] - i - 1) - eq[i]

            stack.append(i)
            last_same[nums[i]] = i

        return res


# V0-3
# IDEA: STACK (gpt)
class Solution(object):
    def shadowPairs(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums or len(nums) <= 1:
            return 0

        stack = []
        cnt = 0
        total = 0

        for x in nums:

            # Values > x can no longer be valid candidates.
            while stack and stack[-1][0] > x:
                total -= stack[-1][1]
                stack.pop()

            # No candidate
            if not stack:
                stack.append([x, 1])
                total += 1
                continue

            # Same value
            if stack[-1][0] == x:
                value, freq = stack.pop()

                # Equal values cannot form a pair because
                # nums[i] < nums[j] must be strict.
                cnt += total - freq

                freq += 1
                stack.append([value, freq])
                total += 1

            # x is greater than the top value
            else:
                # All remaining candidates are smaller than x.
                cnt += total

                stack.append([x, 1])
                total += 1

        return cnt


# V0-4
# IDEA: BRUTE FORCE (gemini) (TLE)
class Solution(object):

  def shadowPairs(self, nums):
    """
        :type nums: List[int]
        :rtype: int
        """
    if not nums or len(nums) <= 1:
      return 0

    n = len(nums)
    if n == 2:
      return 1 if nums[1] > nums[0] else 0

    cnt = 0

    # 雙迴圈走訪所有合法區間對 (l, r)，維持 O(N^2) 內可控範圍
    for l in range(n - 1):
      l_val = nums[l]
      for r in range(l + 1, n):
        r_val = nums[r]

        # 基礎條件：右端點大於左端點
        if r_val > l_val:
          if r - l == 1:
            cnt += 1
          else:
            # 檢查夾在中間的區間元素 nums[l+1 : r] 是否滿足條件
            sub_arr = nums[l + 1 : r]
            # 假設 shadow pair 的條件為中間所有元素皆 >= l_val (可依實際題意調整)
            if all(x >= l_val for x in sub_arr):
              cnt += 1

    return cnt


# V0-5
# IDEA : BRUTE FORCE, BUT BREAK AT THE FIRST SMALLER ELEMENT (the contest draft)
#
#   walk r forward from l and stop the instant nums[r] < nums[l], because from
#   then on k = r sits strictly inside every remaining window.
#
# NOTE !!! the break happens AFTER the count, not before - index r is allowed
#          to BE the smaller element, only a k strictly inside (l, r) kills the
#          pair. breaking first drops nothing here (nums[r] < nums[l] fails the
#          nums[l] < nums[r] test anyway) but the order is the easy thing to
#          get wrong when the condition is "<=".
#
#   justified over V0 : none for submitting - a strictly increasing array never
#   breaks, so this is O(n^2) and TLEs at n = 10^5. it is kept as the obvious
#   reading of the statement, and as the oracle V0 is randomly tested against.
#
# time = O(n^2), space = O(1)
class Solution2(object):
    def shadowPairs(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # edge
        if not nums or len(nums) <= 1:
            return 0

        n = len(nums)
        res = 0

        for l in range(n - 1):
            for r in range(l + 1, n):
                if nums[r] > nums[l]:
                    res += 1
                if nums[r] < nums[l]:
                    break

        return res
