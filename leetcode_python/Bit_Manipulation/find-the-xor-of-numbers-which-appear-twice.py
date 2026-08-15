"""

3158. Find the XOR of Numbers Which Appear Twice
Easy

You are given an array nums, where each number in the array appears either once or twice.

Return the bitwise XOR of all the numbers that appear twice in the array, or 0 if no number appears twice.


Example 1:

Input: nums = [1,2,1,3]
Output: 1
Explanation:
The only number that appears twice in nums is 1.

Example 2:

Input: nums = [1,2,3]
Output: 0
Explanation:
No number appears twice in nums.

Example 3:

Input: nums = [1,2,2,1]
Output: 3
Explanation:
Numbers 1 and 2 appeared twice. 1 XOR 2 == 3.


Constraints:

1 <= nums.length <= 50
1 <= nums[i] <= 50
Each number in nums appears either once or twice.

"""

# V0
# IDEA : A SET REMEMBERS WHAT HAS BEEN SEEN — XOR ON THE SECOND SIGHTING
#
#   since nothing appears more than twice, the moment a value shows up again
#   it is a "twice" value, so fold it into the running XOR right then. an
#   empty result is 0, which is exactly the required fallback.
#
# time = O(n), space = O(n)
class Solution(object):
    def duplicateNumbersXOR(self, nums):
        seen = set()
        res = 0
        for x in nums:
            if x in seen:
                res ^= x
            else:
                seen.add(x)
        return res
