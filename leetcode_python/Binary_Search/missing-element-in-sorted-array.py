"""

1060. Missing Element in Sorted Array
Medium

Given an integer array nums which is sorted in ascending order and all of its
elements are unique and given also an integer k, return the kth missing number
starting from the leftmost number of the array.

Example 1:

Input: nums = [4,7,9,10], k = 1

Output: 5

Explanation: The first missing number is 5.

Example 2:

Input: nums = [4,7,9,10], k = 3

Output: 8

Explanation: The missing numbers are [5,6,8,...], hence the third missing
number is 8.

Example 3:

Input: nums = [1,2,4], k = 3

Output: 6

Explanation: The missing numbers are [3,5,6,7,...], hence the third missing
number is 6.

Constraints:

1 <= nums.length <= 5 * 10^4
1 <= nums[i] <= 10^7
nums is sorted in ascending order, and all the elements are unique.
1 <= k <= 10^8

Follow up: Can you find a logarithmic time complexity (i.e., O(log(n)))
solution?

"""

# V0
# IDEA : BINARY SEARCH ON "HOW MANY NUMBERS ARE MISSING BEFORE nums[i]"
#
#   With no gaps, nums[i] would be nums[0] + i. So the count of numbers
#   missing between nums[0] and nums[i] is
#
#       missing(i) = nums[i] - nums[0] - i
#
#   missing(i) never decreases as i grows (elements are sorted and unique),
#   so we can binary search for the LAST index i with missing(i) < k.
#   The answer lies after nums[i], and k - missing(i) more numbers are still
#   needed:
#
#       answer = nums[i] + (k - missing(i))
#
#   If even the last element has missing(n-1) < k, the answer is past the end
#   of the array, and the same formula with i = n-1 covers it.
#
#   e.g. nums = [4,7,9,10], k = 3
#        missing = [0, 2, 3, 3]   -> last i with missing < 3 is i=1 (nums=7)
#        answer  = 7 + (3 - 2) = 8
#
# time = O(log n), space = O(1)
class Solution(object):
    def missingElement(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        n = len(nums)

        # the kth missing number is beyond the last element
        if self.count_missing(nums, n - 1) < k:
            return nums[n - 1] + (k - self.count_missing(nums, n - 1))

        # missing(0) == 0 < k always, so index 0 is a valid start;
        # search the last index whose missing count is still < k
        left = 0
        right = n - 1
        while left < right:
            # NOTE !!! round UP: with `left = mid` below, rounding down
            #          would loop forever once right == left + 1
            mid = left + (right - left + 1) // 2
            if self.count_missing(nums, mid) < k:
                # mid is still before the answer -> keep it as a candidate
                left = mid
            else:
                # the kth missing number is before nums[mid]
                right = mid - 1

        return nums[left] + (k - self.count_missing(nums, left))

    def count_missing(self, nums, idx):
        """How many numbers are missing between nums[0] and nums[idx]."""
        return nums[idx] - nums[0] - idx


# V1
# IDEA : LINEAR SCAN, SUBTRACT EACH GAP FROM k
#
#   Walk adjacent pairs. The gap between nums[i-1] and nums[i] holds
#   nums[i] - nums[i-1] - 1 missing numbers. If k fits inside this gap the
#   answer is nums[i-1] + k; otherwise use up the whole gap and move on.
#   If the array ends first, the answer is nums[-1] + k.
#
#   The natural first answer in an interview; the follow up asks for V0.
#
# time = O(n), space = O(1)
class Solution2(object):
    def missingElement(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        for i in range(1, len(nums)):
            gap = nums[i] - nums[i - 1] - 1
            if k <= gap:
                # the kth missing number sits inside this gap
                return nums[i - 1] + k
            k -= gap

        # every gap is used up -> count on from the last element
        return nums[-1] + k
