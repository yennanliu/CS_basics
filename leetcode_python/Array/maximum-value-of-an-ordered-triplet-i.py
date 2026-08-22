"""

2873. Maximum Value of an Ordered Triplet I
Easy

You are given a 0-indexed integer array nums.

Return the maximum value over all triplets of indices (i, j, k) such that i < j < k. If all such triplets have a negative value, return 0.

The value of a triplet of indices (i, j, k) is equal to (nums[i] - nums[j]) * nums[k].


Example 1:

Input: nums = [12,6,1,2,7]
Output: 77
Explanation: The value of the triplet (0, 2, 4) is (nums[0] - nums[2]) * nums[4] = 77.
It can be shown that there are no ordered triplets of indices with a value greater than 77.

Example 2:

Input: nums = [1,10,3,4,19]
Output: 133
Explanation: The value of the triplet (1, 2, 4) is (nums[1] - nums[2]) * nums[4] = 133.
It can be shown that there are no ordered triplets of indices with a value greater than 133.

Example 3:

Input: nums = [1,2,3]
Output: 0
Explanation: The only ordered triplet of indices (0, 1, 2) has a negative value of (nums[0] - nums[1]) * nums[2] = -3. Hence, the answer would be 0.


Constraints:

3 <= nums.length <= 100
1 <= nums[i] <= 10^6

"""

# V0
# IDEA : ONE PASS, KEEP PREFIX MAX + BEST PREFIX DIFFERENCE
#
#   Walk k from left to right and keep two running quantities over the part of
#   the array strictly BEFORE k:
#     mx      = max(nums[i])                 for i < k
#     mx_diff = max(nums[i] - nums[j])       for i < j < k
#   Then the best triplet ending at k is mx_diff * nums[k].
#
#   NOTE : the update order inside the loop matters, and it is exactly the
#          reverse of the index order (k, then j, then i):
#            1) answer uses mx_diff built from indices < k
#            2) mx_diff then absorbs the current x as a candidate j
#            3) mx then absorbs the current x as a candidate i
#          Doing it in any other order would let one index be reused.
#
#   NOTE : all nums[i] >= 1, so a negative mx_diff can never help. Seeding
#          ans / mx / mx_diff at 0 both clamps the answer at 0 (as the problem
#          asks) and keeps the first two iterations from producing a triplet.
#
# time = O(n), space = O(1)
class Solution(object):
    def maximumTripletValue(self, nums):
        ans = 0
        mx = 0          # max nums[i] seen so far
        mx_diff = 0     # max (nums[i] - nums[j]) with i < j seen so far
        for x in nums:
            ans = max(ans, mx_diff * x)
            mx_diff = max(mx_diff, mx - x)
            mx = max(mx, x)
        return ans


# V0-1
# IDEA : BRUTE FORCE — TRY EVERY (i, j, k)
#
#   n <= 100, so the 161700 triples of the worst case are free. this is the
#   definition typed out, and the version the O(n) tricks get checked against.
#
# time = O(n^3), space = O(1)
class Solution(object):
    def maximumTripletValue(self, nums):
        n = len(nums)
        ans = 0
        for i in range(n):
            for j in range(i + 1, n):
                for k in range(j + 1, n):
                    cur = (nums[i] - nums[j]) * nums[k]
                    if cur > ans:
                        ans = cur
        return ans


# V0-2
# IDEA : FIX THE MIDDLE INDEX — PREFIX MAX ON THE LEFT, SUFFIX MAX ON THE RIGHT
#
#   for a fixed j the value (nums[i] - nums[j]) * nums[k] is maximised by
#   taking the largest nums[i] to its left and the largest nums[k] to its
#   right, independently — nums[k] >= 1 so a bigger nums[k] never hurts once
#   the difference is positive, and a negative difference is discarded by the
#   0 clamp anyway.
#
#   so precompute
#       pmax[j] = max(nums[0..j-1])
#       smax[j] = max(nums[j+1..n-1])
#   and scan j once. tabulating both directions instead of folding them into
#   the same pass makes the "left / middle / right" decomposition explicit.
#
# time = O(n), space = O(n)
class Solution(object):
    def maximumTripletValue(self, nums):
        n = len(nums)
        pmax = [0] * n            # max strictly to the left of j
        for j in range(1, n):
            pmax[j] = max(pmax[j - 1], nums[j - 1])
        smax = [0] * n            # max strictly to the right of j
        for j in range(n - 2, -1, -1):
            smax[j] = max(smax[j + 1], nums[j + 1])

        ans = 0
        for j in range(1, n - 1):
            cur = (pmax[j] - nums[j]) * smax[j]
            if cur > ans:
                ans = cur
        return ans
