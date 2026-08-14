"""

2221. Find Triangular Sum of an Array
Medium

You are given a 0-indexed integer array nums, where nums[i] is a digit between 0 and 9 (inclusive).

The triangular sum of nums is the value of the only element present in nums after the following process terminates:

1. Let nums comprise of n elements. If n == 1, end the process. Otherwise, create a new 0-indexed integer array newNums of length n - 1.
2. For each index i, where 0 <= i < n - 1, assign the value of newNums[i] as (nums[i] + nums[i+1]) % 10, where % denotes modulo operator.
3. Replace the array nums with newNums.
4. Repeat the entire process starting from step 1.

Return the triangular sum of nums.


Example 1:

Input: nums = [1,2,3,4,5]
Output: 8
Explanation:
The above diagram depicts the process from which we obtain the triangular sum of the array.

Example 2:

Input: nums = [5]
Output: 5
Explanation:
Since there is only one element in nums, the triangular sum is the value of that element itself.


Constraints:

1 <= nums.length <= 1000
0 <= nums[i] <= 9

"""

# V0
# IDEA : IN-PLACE SIMULATION (Pascal-triangle folding)
#
#   round k shrinks the array by 1; after n-1 rounds a single value is left.
#   doing it in place from the left keeps memory at O(1) :
#     for k = n-1 down to 1 :
#         for i in 0..k-1 : nums[i] = (nums[i] + nums[i+1]) % 10
#
#   NOTE : n <= 1000 so O(n^2) = 10^6 steps is fine; the closed form
#          sum(C(n-1, i) * nums[i]) mod 10 is NOT simply usable because
#          10 is not prime (Lucas does not apply directly).
#
# time = O(n^2), space = O(1)
class Solution(object):
    def triangularSum(self, nums):
        arr = list(nums)
        for k in range(len(arr) - 1, 0, -1):
            for i in range(k):
                arr[i] = (arr[i] + arr[i + 1]) % 10
        return arr[0]
