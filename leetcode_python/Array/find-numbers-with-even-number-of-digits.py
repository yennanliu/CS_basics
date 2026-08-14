"""

1295. Find Numbers with Even Number of Digits
Easy

Given an array nums of integers, return how many of them contain
an even number of digits.


Example 1:

Input: nums = [12,345,2,6,7896]
Output: 2
Explanation:
12 contains 2 digits (even number of digits).
345 contains 3 digits (odd number of digits).
2 contains 1 digit (odd number of digits).
6 contains 1 digit (odd number of digits).
7896 contains 4 digits (even number of digits).
Therefore only 12 and 7896 contain an even number of digits.

Example 2:

Input: nums = [555,901,482,1771]
Output: 1
Explanation:
Only 1771 contains an even number of digits.


Constraints:

1 <= nums.length <= 500
1 <= nums[i] <= 10^5

"""

# V0
# IDEA: cast to STRING and check the length parity
# time = O(n log M), M = max(nums)
# space = O(log M)
class Solution(object):
    def findNumbers(self, nums):
        res = 0
        for x in nums:
            if len(str(x)) % 2 == 0:
                res += 1
        return res


# V1
# IDEA: count digits by repeated DIVISION (no string allocation)
# time = O(n log M), M = max(nums)
# space = O(1)
class Solution(object):
    def findNumbers(self, nums):
        res = 0
        for x in nums:
            cnt = 0
            while x:
                x //= 10
                cnt += 1
            if cnt % 2 == 0:
                res += 1
        return res
