"""

2541. Minimum Operations to Make Array Equal II
Medium

You are given two integer arrays nums1 and nums2 of equal length n and an integer k. You can perform the following operation on nums1:

Choose two indexes i and j and increment nums1[i] by k and decrement nums1[j] by k. In other words, nums1[i] = nums1[i] + k and nums1[j] = nums1[j] - k.

nums1 is said to be equal to nums2 if for all indices i such that 0 <= i < n, nums1[i] == nums2[i].

Return the minimum number of operations required to make nums1 equal to nums2. If it is impossible to make them equal, return -1.


Example 1:

Input: nums1 = [4,3,1,4], nums2 = [1,3,7,1], k = 3
Output: 2
Explanation: In 2 operations, we can transform nums1 to nums2.
1st operation: i = 2, j = 0. After applying the operation, nums1 = [1,3,4,4].
2nd operation: i = 2, j = 3. After applying the operation, nums1 = [1,3,7,1].
One can prove that it is impossible to make arrays equal in fewer operations.

Example 2:

Input: nums1 = [3,8,5,2], nums2 = [2,4,1,6], k = 1
Output: -1
Explanation: It can be proved that it is impossible to make the two arrays equal.


Constraints:

n == nums1.length == nums2.length
2 <= n <= 10^5
0 <= nums1[i], nums2[j] <= 10^9
0 <= k <= 10^5

"""

# V0
# IDEA : GREEDY / COUNTING (every op moves exactly k from one slot to another)
#
#   one operation moves k units OUT of index j and INTO index i. so for each
#   position the required change (nums1[i] - nums2[i]) must be a multiple of k,
#   and the total "surplus" (needs to go down) must exactly match the total
#   "deficit" (needs to go up) — otherwise it is impossible.
#
#   let t = (x - y) / k for each position:
#     t > 0  -> this slot has k*t too much, it must DONATE t times
#     t < 0  -> this slot is k*|t| short, it must RECEIVE |t| times
#   each op pairs one donation with one receipt, so the answer is the number
#   of donations, valid only when donations == receipts.
#
#   NOTE : k == 0 is allowed by the constraints. then no value can ever change,
#          so the arrays must already be identical (guard the division).
#
# time = O(n), space = O(1)
class Solution(object):
    def minOperations(self, nums1, nums2, k):
        give = 0
        take = 0
        for x, y in zip(nums1, nums2):
            if x == y:
                continue
            if k == 0 or (x - y) % k != 0:
                return -1
            t = (x - y) // k
            if t > 0:
                give += t
            else:
                take += -t
        return give if give == take else -1
