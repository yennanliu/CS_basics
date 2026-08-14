"""

1588. Sum of All Odd Length Subarrays
Easy

Given an array of positive integers arr, return the sum of all possible odd-length subarrays of arr.

A subarray is a contiguous subsequence of the array.

Example 1:

Input: arr = [1,4,2,5,3]
Output: 58
Explanation: The odd-length subarrays of arr and their sums are:
[1] = 1
[4] = 4
[2] = 2
[5] = 5
[3] = 3
[1,4,2] = 7
[4,2,5] = 11
[2,5,3] = 10
[1,4,2,5,3] = 15
If we add all these together we get 1 + 4 + 2 + 5 + 3 + 7 + 11 + 10 + 15 = 58

Example 2:

Input: arr = [1,2]
Output: 3
Explanation: There are only 2 subarrays of odd length, [1] and [2]. Their sum is 3.

Example 3:

Input: arr = [10,11,12]
Output: 66

Constraints:

1 <= arr.length <= 100
1 <= arr[i] <= 1000

Follow up:

Could you solve this problem in O(n) time complexity?

"""

# V0
# IDEA : CONTRIBUTION COUNTING (how many odd subarrays cover index i ?)
#
#   index i belongs to (i + 1) * (n - i) subarrays in total, and exactly
#   half of them (rounded up) have odd length :
#     odd(i) = ((i + 1) * (n - i) + 1) // 2
#   so the answer is sum(arr[i] * odd(i)) -- one pass, no subarray is
#   ever built.
#
# time = O(n), space = O(1)
class Solution(object):
    def sumOddLengthSubarrays(self, arr):
        n = len(arr)
        res = 0
        for i, v in enumerate(arr):
            res += v * (((i + 1) * (n - i) + 1) // 2)
        return res
