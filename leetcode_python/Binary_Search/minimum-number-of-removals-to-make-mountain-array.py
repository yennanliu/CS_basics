"""

1671. Minimum Number of Removals to Make Mountain Array
Medium

You may recall that an array arr is a mountain array if and only if:

arr.length >= 3
There exists some index i (0-indexed) with 0 < i < arr.length - 1 such that:
    arr[0] < arr[1] < ... < arr[i - 1] < arr[i]
    arr[i] > arr[i + 1] > ... > arr[arr.length - 1]

Given an integer array nums, return the minimum number of elements to remove to make nums a
mountain array.


Example 1:

Input: nums = [1,3,1]
Output: 0
Explanation: The array itself is a mountain array so we do not need to remove any elements.

Example 2:

Input: nums = [2,1,1,5,6,2,3,1]
Output: 3
Explanation: One solution is to remove the elements at indices 0, 1, and 5, making the array
nums = [1,5,6,3,1].


Constraints:

3 <= nums.length <= 1000
1 <= nums[i] <= 10^9
It is guaranteed that you can make a mountain array out of nums.

"""

# V0
# IDEA : BIDIRECTIONAL LIS + BINARY SEARCH (keep the longest mountain, drop the rest)
#
#   minimum removals = n - (longest mountain SUBSEQUENCE length).
#
#   for every candidate peak index i:
#     left[i]  = length of the longest STRICTLY increasing subsequence ending   at i
#     right[i] = length of the longest STRICTLY decreasing subsequence starting at i
#     mountain through i = left[i] + right[i] - 1   (i counted once)
#
#   right[] is just left[] computed on the reversed array.
#   each LIS is built with the classic patience/tails array + bisect_left,
#   where left[i] = insertion position + 1.
#
#   NOTE : a peak needs BOTH slopes non-empty -> only accept i with
#          left[i] > 1 and right[i] > 1, otherwise it is not a mountain.
#
# time = O(n log n), space = O(n)
from bisect import bisect_left
class Solution(object):
    def minimumMountainRemovals(self, nums):
        n = len(nums)

        def lis_ending_at(arr):
            # res[i] = length of longest strictly increasing subseq ending at arr[i]
            res = [0] * len(arr)
            tails = []
            for i, v in enumerate(arr):
                pos = bisect_left(tails, v)
                if pos == len(tails):
                    tails.append(v)
                else:
                    tails[pos] = v
                res[i] = pos + 1
            return res

        left = lis_ending_at(nums)
        right = lis_ending_at(nums[::-1])[::-1]

        best = 0
        for i in range(n):
            if left[i] > 1 and right[i] > 1:
                best = max(best, left[i] + right[i] - 1)
        return n - best
