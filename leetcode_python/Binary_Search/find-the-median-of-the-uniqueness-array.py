"""

3134. Find the Median of the Uniqueness Array
Hard

You are given an integer array nums. The uniqueness array of nums is the sorted array that contains the number of distinct elements of all the subarrays of nums. In other words, it is a sorted array consisting of distinct(nums[i..j]), for all 0 <= i <= j < nums.length.

Here, distinct(nums[i..j]) denotes the number of distinct elements in the subarray that starts at index i and ends at index j.

Return the median of the uniqueness array of nums.

Note that the median of an array is defined as the middle element of the array when it is sorted in non-decreasing order. If there are two choices for a median, the smaller of the two values is taken.


Example 1:

Input: nums = [1,2,3]
Output: 1
Explanation:
The uniqueness array of nums is [distinct(nums[0..0]), distinct(nums[1..1]), distinct(nums[2..2]), distinct(nums[0..1]), distinct(nums[1..2]), distinct(nums[0..2])] which is equal to [1, 1, 1, 2, 2, 3]. The uniqueness array has a median of 1. Therefore, the answer is 1.

Example 2:

Input: nums = [3,4,3,4,5]
Output: 2
Explanation:
The uniqueness array of nums is [1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3]. The uniqueness array has a median of 2. Therefore, the answer is 2.

Example 3:

Input: nums = [4,3,5,4]
Output: 2
Explanation:
The uniqueness array of nums is [1, 1, 1, 1, 2, 2, 2, 3, 3, 3]. The uniqueness array has a median of 2. Therefore, the answer is 2.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : BINARY SEARCH THE MEDIAN VALUE, COUNT WITH A SLIDING WINDOW
#
#   the uniqueness array has n*(n+1)/2 entries and can never be built
#   explicitly. but its median is the smallest value k such that
#
#       #{ subarrays with at most k distinct } >= (total + 1) // 2
#
#   ("smaller of the two middles" is exactly the (total+1)//2-th smallest).
#
#   counting subarrays with at most k distinct is the classic two-pointer :
#   extend the right end, shrink the left while the window holds more than k
#   distinct values, and add (right - left + 1) — the number of subarrays
#   ending at `right` that qualify.
#
#   the count is monotone in k, so binary search over 1..n.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def medianOfUniquenessArray(self, nums):
        n = len(nums)
        total = n * (n + 1) // 2
        need = (total + 1) // 2

        def at_most(k):
            cnt = {}
            res = 0
            left = 0
            for right, v in enumerate(nums):
                cnt[v] = cnt.get(v, 0) + 1
                while len(cnt) > k:
                    u = nums[left]
                    cnt[u] -= 1
                    if cnt[u] == 0:
                        del cnt[u]
                    left += 1
                res += right - left + 1
            return res

        lo, hi = 1, n
        while lo < hi:
            mid = (lo + hi) // 2
            if at_most(mid) >= need:
                hi = mid
            else:
                lo = mid + 1
        return lo
