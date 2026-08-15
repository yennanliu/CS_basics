"""

2099. Find Subsequence of Length K With the Largest Sum
Easy

You are given an integer array nums and an integer k. You want to find a subsequence of nums of length k that has the largest sum.

Return any such subsequence as an integer array of length k.

A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.


Example 1:

Input: nums = [2,1,3,3], k = 2
Output: [3,3]
Explanation:
The subsequence has the largest sum of 3 + 3 = 6.

Example 2:

Input: nums = [-1,-2,3,4], k = 3
Output: [-1,3,4]
Explanation:
The subsequence has the largest sum of -1 + 3 + 4 = 6.

Example 3:

Input: nums = [3,4,3,3], k = 2
Output: [3,4]
Explanation:
The subsequence has the largest sum of 3 + 4 = 7.
Another possible subsequence is [4, 3].


Constraints:

1 <= nums.length <= 1000
-10^5 <= nums[i] <= 10^5
1 <= k <= nums.length

"""

# V0
# IDEA : PICK THE K LARGEST *INDICES*, THEN RESTORE THE ORIGINAL ORDER
#
#   the sum only depends on WHICH k values are taken, so take the k largest.
#   but the answer must stay a subsequence, so keep the indices (not the
#   values), sort those indices, and read the values back in that order.
#
#   NOTE : working with indices also handles duplicates for free — picking
#          "one of the two 3s" is unambiguous once it is index 0 vs index 2.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def maxSubsequence(self, nums, k):
        idx = sorted(range(len(nums)), key=lambda i: nums[i], reverse=True)[:k]
        idx.sort()
        return [nums[i] for i in idx]
