"""

2177. Find Three Consecutive Integers That Sum to a Given Number
Medium

Given an integer num, return three consecutive integers (as a sorted array) that sum to num. If num cannot be expressed as the sum of three consecutive integers, return an empty array.


Example 1:

Input: num = 33
Output: [10,11,12]
Explanation: 33 can be expressed as 10 + 11 + 12 = 33.
10, 11, 12 are 3 consecutive integers, so we return [10, 11, 12].

Example 2:

Input: num = 4
Output: []
Explanation: There is no way to express 4 as the sum of 3 consecutive integers.


Constraints:

0 <= num <= 10^15

"""

# V0
# IDEA : THE MIDDLE TERM IS num / 3
#
#   three consecutive integers around m are (m-1) + m + (m+1) = 3m. so the
#   only candidate is m = num / 3, and a solution exists iff num is divisible
#   by 3.
#
#   NOTE : num can be 10^15, so this must stay arithmetic — no searching.
#          python ints handle the magnitude natively.
#
# time = O(1), space = O(1)
class Solution(object):
    def sumOfThree(self, num):
        if num % 3:
            return []
        m = num // 3
        return [m - 1, m, m + 1]
