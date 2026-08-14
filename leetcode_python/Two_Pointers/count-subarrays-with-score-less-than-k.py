"""

2302. Count Subarrays With Score Less Than K
Hard

The score of an array is defined as the product of its sum and its length.

For example, the score of [1, 2, 3, 4, 5] is (1 + 2 + 3 + 4 + 5) * 5 = 75.

Given a positive integer array nums and an integer k, return the number of non-empty subarrays of nums whose score is strictly less than k.

A subarray is a contiguous sequence of elements within an array.


Example 1:

Input: nums = [2,1,4,3,5], k = 10
Output: 6
Explanation:
The 6 subarrays having scores less than 10 are:
- [2] with score 2 * 1 = 2.
- [1] with score 1 * 1 = 1.
- [4] with score 4 * 1 = 4.
- [3] with score 3 * 1 = 3.
- [5] with score 5 * 1 = 5.
- [2,1] with score (2 + 1) * 2 = 6.
Note that subarrays such as [1,4] and [4,3,5] are not considered because their scores are 10 and 36 respectively, while we need scores strictly less than 10.

Example 2:

Input: nums = [1,1,1], k = 5
Output: 5
Explanation:
Every subarray except [1,1,1] has a score less than 5.
[1,1,1] has a score (1 + 1 + 1) * 3 = 9, which is greater than 5.
Thus, there are 5 subarrays having scores less than 5.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5
1 <= k <= 10^15

"""

# V0
# IDEA : SLIDING WINDOW (score is monotonic when the window grows)
#
#   all nums[i] >= 1, so extending a window to the right strictly
#   increases both its sum and its length -> its score only goes up.
#   that monotonicity makes a 2-pointer window valid:
#     - move right end i, add nums[i] to the running sum
#     - while sum * (i - j + 1) >= k, shrink from the left (j += 1)
#     - every window ending at i that starts at j..i is now valid
#       -> add (i - j + 1) to the answer
#
#   NOTE : count subarrays "ending at i" (not "starting at j"), otherwise
#          the same subarray gets counted more than once.
#
# time = O(n), space = O(1)
class Solution(object):
    def countSubarrays(self, nums, k):
        res = 0
        cur = 0
        j = 0
        for i in range(len(nums)):
            cur += nums[i]
            while cur * (i - j + 1) >= k:
                cur -= nums[j]
                j += 1
            res += i - j + 1
        return res
