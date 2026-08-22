"""

1800. Maximum Ascending Subarray Sum
Easy

Given an array of positive integers nums, return the maximum possible sum of an strictly increasing subarray in nums.

A subarray is defined as a contiguous sequence of numbers in an array.

Example 1:

Input: nums = [10,20,30,5,10,50]
Output: 65
Explanation: [5,10,50] is the ascending subarray with the maximum sum of 65.

Example 2:

Input: nums = [10,20,30,40,50]
Output: 150
Explanation: [10,20,30,40,50] is the ascending subarray with the maximum sum of 150.

Example 3:

Input: nums = [12,17,15,13,10,11,12]
Output: 33
Explanation: [10,11,12] is the ascending subarray with the maximum sum of 33.

Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 100

"""

# V0
# IDEA : RUNNING SUM OF THE CURRENT ASCENDING RUN
#
#   keep the sum of the ascending run that ends at i. when nums[i] > nums[i-1]
#   the run continues, otherwise a new run starts at nums[i].
#   the answer is the largest run sum ever seen.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxAscendingSum(self, nums):
        res = cur = 0
        for i in range(len(nums)):
            if i > 0 and nums[i] > nums[i - 1]:
                cur += nums[i]
            else:
                cur = nums[i]
            res = max(res, cur)
        return res


# V0-1
# IDEA : BRUTE FORCE - EVERY START, EXTEND WHILE STRICTLY ASCENDING
#
#   read the definition literally : for each start i keep adding nums[j] while
#   the run stays strictly increasing, recording the total after every step,
#   and stop at the first non-increase. this enumerates every ascending
#   subarray rather than only the maximal ones.
#
# time = O(n^2)
# space = O(1)
class Solution(object):
    def maxAscendingSum(self, nums):
        n = len(nums)
        best = 0
        for i in range(n):
            total = nums[i]
            if total > best:
                best = total
            j = i + 1
            while j < n and nums[j] > nums[j - 1]:
                total += nums[j]
                if total > best:
                    best = total
                j += 1
        return best


# V0-2
# IDEA : PREFIX SUMS + THE LIST OF RUN BOUNDARIES
#
#   index i opens a new run exactly when nums[i] <= nums[i-1], so collect
#   those cut points (plus the two ends) and the array is described by the
#   boundary list alone. with a prefix-sum table the total of the run
#   [a, b) is the single subtraction pre[b] - pre[a], so no value is ever
#   added twice and any run's sum is an O(1) lookup afterwards.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def maxAscendingSum(self, nums):
        import itertools
        n = len(nums)
        pre = [0] + list(itertools.accumulate(nums))
        cuts = [0] + [i for i in range(1, n) if nums[i] <= nums[i - 1]] + [n]
        return max(pre[cuts[i + 1]] - pre[cuts[i]] for i in range(len(cuts) - 1))
