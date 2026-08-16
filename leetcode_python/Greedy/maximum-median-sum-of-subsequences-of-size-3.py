"""

3627. Maximum Median Sum of Subsequences of Size 3
Medium

You are given an integer array nums with a length divisible by 3.

You want to make the array empty in steps. In each step, you can select any three elements from the array, compute their median, and remove the selected elements from the array.

The median of an odd-length sequence is defined as the middle element of the sequence when it is sorted in non-decreasing order.

Return the maximum possible sum of the medians computed from the selected elements.


Example 1:

Input: nums = [2,1,3,2,1,3]
Output: 5
Explanation:
In the first step, select elements at indices 2, 4, and 5, which have a median 3. Remove these elements, and nums becomes [2,1,2].
In the second step, select elements at indices 0, 1, and 2, which have a median 2. Remove these elements, and nums becomes empty.
Hence, the sum of the medians is 3 + 2 = 5.

Example 2:

Input: nums = [1,1,10,10,10,10]
Output: 20
Explanation:
In the first step, select elements at indices 0, 2, and 3, which have a median 10.
In the second step, select elements at indices 1, 4, and 5, which have a median 10.
Hence, the sum of the medians is 10 + 10 = 20.


Constraints:

3 <= nums.length <= 5 * 10^5
nums.length % 3 == 0
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : SORT + PAIR EACH SMALL ELEMENT WITH THE TOP OF THE ARRAY
#
#   a group of three contributes its middle element, so exactly one element
#   per group is "wasted below" the median and one is "wasted above" it.
#
#   sort ascending. the cheapest way to feed each group its mandatory
#   below-median element is to use the n/3 smallest values, which are then
#   never medians. the remaining 2n/3 values are consumed from the top in
#   pairs: the largest of a pair sits above the median and the second largest
#   becomes the median. so the medians are nums[n-2], nums[n-4], nums[n-6],
#   ... taking n/3 of them.
#
# time = O(n log n), space = O(1) beyond the sort
class Solution(object):
    def maximumMedianSum(self, nums):
        nums.sort()
        n = len(nums)
        total = 0
        for i in range(1, n // 3 + 1):
            total += nums[n - 2 * i]
        return total
