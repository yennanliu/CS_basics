"""

3634. Minimum Removals to Balance Array
Medium

You are given an integer array nums and an integer k.

An array is considered balanced if the value of its maximum element is at
most k times the minimum element.

You may remove any number of elements from nums without making it empty.

Return the minimum number of elements to remove so that the remaining array
is balanced.

Note: An array of size 1 is considered balanced as its maximum and minimum
are equal, and the condition always holds true.

Example 1:

Input: nums = [2,1,5], k = 2
Output: 1
Explanation:
Remove nums[2] = 5 to get nums = [2, 1].
Now max = 2, min = 1 and max <= min * k as 2 <= 1 * 2. Thus, the answer is 1.

Example 2:

Input: nums = [1,6,2,9], k = 3
Output: 2
Explanation:
Remove nums[0] = 1 and nums[3] = 9 to get nums = [6, 2].
Now max = 6, min = 2 and max <= min * k as 6 <= 2 * 3. Thus, the answer is 2.

Example 3:

Input: nums = [4,6], k = 2
Output: 0
Explanation:
Since nums is already balanced as 6 <= 4 * 2, no elements need to be removed.

Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
1 <= k <= 10^5

"""

# V0
# IDEA : SORT + SLIDING WINDOW (KEEP THE LONGEST BALANCED WINDOW)
#
#   what is kept is a multiset, and only its min and max matter. after
#   sorting, any kept multiset is dominated by the CONTIGUOUS block of the
#   sorted array spanning the same min and max — adding the elements in
#   between changes neither bound and only reduces removals.
#
#   so the task is: find the longest sorted window [l, r] with
#   nums[r] <= k * nums[l], and remove everything else. as l grows the
#   feasible r is non-decreasing, so two pointers sweep it in linear time.
#
# time = O(n log n), space = O(1) beyond the sort
class Solution(object):
    def minRemoval(self, nums, k):
        nums.sort()
        n = len(nums)
        best = 0
        l = 0
        for r in range(n):
            while nums[r] > k * nums[l]:
                l += 1
            if r - l + 1 > best:
                best = r - l + 1
        return n - best
