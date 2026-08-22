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


# V0-1
# IDEA : COLLECT THE INDICES OF THE 1s, THEN COMPARE NEIGHBOURING GAPS (2 PASS)
#
#  -> pass 1 materialises the position of every 1
#  -> pass 2 walks that list pairwise : (b - a - 1) is the number of 0s
#     between two consecutive 1s, and each must be >= k
#  -> trades O(#ones) memory for a check that carries no running state
#
# time = O(n)
# space = O(#ones)
class Solution(object):
    def kLengthApart(self, nums, k):
        ones = [i for i, x in enumerate(nums) if x == 1]
        return all(b - a - 1 >= k for a, b in zip(ones, ones[1:]))


# V0-2
# IDEA : PREFIX SUM + SLIDING WINDOW OF WIDTH k + 1
#
#   restate the condition : two 1s sit < k apart  <=>  some window of k + 1
#   consecutive cells holds 2 or more 1s. so build a prefix sum and reject as
#   soon as any such window sums to more than 1.
#   NOTE !!! clamp the width to len(nums), otherwise a short array (n <= k)
#            would be scanned by zero windows and wrongly pass.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def kLengthApart(self, nums, k):
        n = len(nums)
        pre = [0] * (n + 1)
        for i, x in enumerate(nums):
            pre[i + 1] = pre[i] + x
        w = min(k + 1, n)
        for i in range(n - w + 1):
            if pre[i + w] - pre[i] > 1:
                return False
        return True
