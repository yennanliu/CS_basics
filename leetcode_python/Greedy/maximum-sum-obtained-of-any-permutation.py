"""

1589. Maximum Sum Obtained of Any Permutation
Medium

We have an array of integers, nums, and an array of requests where requests[i] = [starti, endi]. The ith request asks for the sum of nums[starti] + nums[starti + 1] + ... + nums[endi - 1] + nums[endi]. Both starti and endi are 0-indexed.

Return the maximum total sum of all requests among all permutations of nums.

Since the answer may be too large, return it modulo 10^9 + 7.

Example 1:

Input: nums = [1,2,3,4,5], requests = [[1,3],[0,1]]
Output: 19
Explanation: One permutation of nums is [2,1,3,4,5] with the following result:
requests[0] -> nums[1] + nums[2] + nums[3] = 1 + 3 + 4 = 8
requests[1] -> nums[0] + nums[1] = 2 + 1 = 3
Total sum: 8 + 3 = 11.
A permutation with a higher total sum is [3,5,4,2,1] with the following result:
requests[0] -> nums[1] + nums[2] + nums[3] = 5 + 4 + 2 = 11
requests[1] -> nums[0] + nums[1] = 3 + 5  = 8
Total sum: 11 + 8 = 19, which is the best that you can do.

Example 2:

Input: nums = [1,2,3,4,5,6], requests = [[0,1]]
Output: 11
Explanation: A permutation with the max total sum is [6,5,4,3,2,1] with request sums [11].

Example 3:

Input: nums = [1,2,3,4,5,10], requests = [[0,2],[1,3],[1,1]]
Output: 47
Explanation: A permutation with the max total sum is [4,10,5,3,2,1] with request sums [19,18,10].

Constraints:

n == nums.length
1 <= n <= 10^5
0 <= nums[i] <= 10^5
1 <= requests.length <= 10^5
requests[i].length == 2
0 <= starti <= endi < n

"""

# V0
# IDEA : DIFFERENCE ARRAY + GREEDY (biggest number on the hottest index)
#
#   requests only tell us HOW OFTEN each index is summed. build those
#   frequencies with a difference array in O(n + q), then, by the
#   rearrangement inequality, pair the largest value with the largest
#   frequency.
#   NOTE : take the modulo only at the very end, the products are exact
#          Python ints until then.
#
# time = O(n log n + q), space = O(n)
class Solution(object):
    def maxSumRangeQuery(self, nums, requests):
        MOD = 10 ** 9 + 7
        n = len(nums)
        diff = [0] * (n + 1)
        for a, b in requests:
            diff[a] += 1
            diff[b + 1] -= 1

        freq = []
        cur = 0
        for i in range(n):
            cur += diff[i]
            freq.append(cur)

        freq.sort(reverse=True)
        nums.sort(reverse=True)
        return sum(a * b for a, b in zip(freq, nums)) % MOD
