"""

1574. Shortest Subarray to be Removed to Make Array Sorted
Medium

Given an integer array arr, remove a subarray (can be empty) from arr such that the remaining elements in arr are non-decreasing.

Return the length of the shortest subarray to remove.

A subarray is a contiguous subsequence of the array.

Example 1:

Input: arr = [1,2,3,10,4,2,3,5]
Output: 3
Explanation: The shortest subarray we can remove is [10,4,2] of length 3. The remaining elements after that will be [1,2,3,3,5] which are sorted.
Another correct solution is to remove the subarray [3,10,4].

Example 2:

Input: arr = [5,4,3,2,1]
Output: 4
Explanation: Since the array is strictly decreasing, we can only keep a single element. Therefore we need to remove a subarray of length 4, either [5,4,3,2] or [4,3,2,1].

Example 3:

Input: arr = [1,2,3]
Output: 0
Explanation: The array is already non-decreasing. We do not need to remove any elements.

Constraints:

1 <= arr.length <= 10^5
0 <= arr[i] <= 10^9

"""

# V0
# IDEA : TWO POINTERS (keep a non-decreasing prefix + a non-decreasing suffix)
#
#   the kept part is always arr[0..i] + arr[j..n-1] where both pieces are
#   already non-decreasing and arr[i] <= arr[j].
#   1) find the longest non-decreasing suffix, starting at j
#   2) grow the prefix i from 0 ; for every i push j forward while
#      arr[j] < arr[i], then the removal length is j - i - 1.
#   NOTE : i only ever moves forward, so does j -> linear scan.
#
# time = O(n), space = O(1)
class Solution(object):
    def findLengthOfShortestSubarray(self, arr):
        n = len(arr)
        j = n - 1
        while j > 0 and arr[j - 1] <= arr[j]:
            j -= 1
        if j == 0:
            return 0

        res = j  # drop arr[0 .. j-1]
        i = 0
        while True:
            while j < n and arr[j] < arr[i]:
                j += 1
            if j - i - 1 < res:
                res = j - i - 1
            if i + 1 < n and arr[i] <= arr[i + 1]:
                i += 1
            else:
                break
        return res
