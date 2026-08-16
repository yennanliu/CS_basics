"""

3431. Minimum Unlocked Indices to Sort Nums
Medium

You are given an array nums consisting of integers between 1 and 3, and a binary
array locked of the same size.

We consider nums sortable if it can be sorted using adjacent swaps, where a swap
between two indices i and i + 1 is allowed if nums[i] - nums[i + 1] == 1 and
locked[i] == 0.

In one operation, you can unlock any index i by setting locked[i] to 0.

Return the minimum number of operations needed to make nums sortable. If it is
not possible to make nums sortable, return -1.

Example 1:

Input: nums = [1,2,1,2,3,2], locked = [1,0,1,1,0,1]

Output: 0

Explanation:

We can sort nums using the following swaps:

swap indices 1 with 2
swap indices 4 with 5

So, there is no need to unlock any index.

Example 2:

Input: nums = [1,2,1,1,3,2,2], locked = [1,0,1,1,0,1,0]

Output: 2

Explanation:

If we unlock indices 2 and 5, we can sort nums using the following swaps:

swap indices 1 with 2
swap indices 2 with 3
swap indices 4 with 5
swap indices 5 with 6

Example 3:

Input: nums = [1,2,1,2,3,2,1], locked = [0,0,0,0,0,0,0]

Output: -1

Explanation:

Even if all indices are unlocked, it can be shown that nums is not sortable.

Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 3
locked.length == nums.length
0 <= locked[i] <= 1

"""

# V0
# IDEA : A BOUNDARY NEEDS UNLOCKING EXACTLY WHEN ITS PREFIX IS WRONG
#
#   a swap is only ever legal between values that differ by 1, so a 1 can slip
#   past a 2 and a 2 past a 3, but a 1 can *never* get past a 3 — an adjacent
#   transposition is the only way two particular elements change relative order,
#   and 3 - 1 = 2 is not an allowed difference.  so if any 3 stands to the left
#   of any 1 the array is unsortable, full stop.
#
#   otherwise every adjacent inversion that can ever appear is a (2,1) or a
#   (3,2), both legal, so the only question is *where* swaps must happen.  look
#   at the boundary between i and i+1: if the multiset of nums[0..i] already
#   equals the multiset of the sorted array's first i+1 entries, no element has
#   to cross that boundary and both sides can be sorted on their own.  if it
#   differs, something must cross, so that boundary has to be unlocked.
#
#   the condition is therefore necessary *and* sufficient, and the answer is
#   just the number of locked boundaries whose prefix counts are off.  comparing
#   the count of 1s and of 2s is enough — the 3s follow.
#
# time = O(n), space = O(1)
class Solution(object):
    def minUnlockedIndices(self, nums, locked):
        n = len(nums)
        seenThree = False
        for v in nums:
            if v == 3:
                seenThree = True
            elif v == 1 and seenThree:
                return -1

        c1 = nums.count(1)
        c2 = nums.count(2)
        p1 = p2 = 0
        ans = 0
        for i in range(n - 1):
            v = nums[i]
            if v == 1:
                p1 += 1
            elif v == 2:
                p2 += 1
            take = i + 1
            want1 = min(take, c1)
            want2 = min(take - want1, c2)
            if (p1, p2) != (want1, want2) and locked[i]:
                ans += 1
        return ans
