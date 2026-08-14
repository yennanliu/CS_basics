"""

1437. Check If All 1's Are at Least Length K Places Away
Easy

Given an binary array nums and an integer k, return true if all 1's are at
least k places away from each other, otherwise return false.


Example 1:

Input: nums = [1,0,0,0,1,0,0,1], k = 2
Output: true
Explanation: Each of the 1s are at least 2 places away from each other.

Example 2:

Input: nums = [1,0,0,1,0,1], k = 2
Output: false
Explanation: The second 1 and third 1 are only one apart from each other.


Constraints:

1 <= nums.length <= 10^5
0 <= k <= nums.length
nums[i] is 0 or 1

"""

# V0
# IDEA : ONE PASS, TRACK PREVIOUS 1 INDEX
#
#  -> keep `prev` = index of the last seen 1
#  -> when we hit a new 1 at index i, the number of 0s in between
#     is (i - prev - 1); it must be >= k
#  -> init prev = -inf so the FIRST 1 never fails
#
# time = O(n)
# space = O(1)
class Solution(object):
    def kLengthApart(self, nums, k):
        prev = float('-inf')
        for i, x in enumerate(nums):
            if x == 1:
                # NOTE !!! gap = number of 0s strictly between the two 1s
                if i - prev - 1 < k:
                    return False
                prev = i
        return True
