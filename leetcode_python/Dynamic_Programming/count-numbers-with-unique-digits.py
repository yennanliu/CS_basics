"""

357. Count Numbers with Unique Digits
Medium

Given an integer n, return the count of all numbers with unique digits, x,
where 0 <= x < 10^n.


Example 1:

Input: n = 2
Output: 91
Explanation: The answer should be the total numbers in the range of 0 <= x < 100,
excluding 11,22,33,44,55,66,77,88,99

Example 2:

Input: n = 0
Output: 1


Constraints:

0 <= n <= 8

"""


# V0
# IDEA : COUNTING DP (PER-LENGTH PERMUTATION COUNT)
#
#   dp[i] : how many numbers with EXACTLY i digits have all-distinct digits
#           -> the leading digit cannot be 0
#
#   dp[1] = 9                      # 1..9
#   dp[i] = dp[i-1] * (11 - i)     # = dp[i-1] * (10 - (i-1))
#
#   -> after fixing i-1 distinct digits, 10 - (i-1) digits are still unused,
#      and any of them may take the new position (it is not leading, so 0 is fine)
#
#   e.g. dp[2] = 9 * 9 = 81 -> 9 choices for the 1st digit, 9 for the 2nd (0-9 minus it)
#
#   ans = 1 + dp[1] + ... + dp[n]  # the leading 1 is the number 0 itself
#
# https://www.jiuzhang.com/solution/count-numbers-with-unique-digits/
# time = O(n), space = O(1)
class Solution(object):
    def countNumbersWithUniqueDigits(self, n):
        """
        :type n: int
        :rtype: int
        """
        # edge : 0 <= x < 10^0 = 1, so 0 is the only number
        if n == 0:
            return 1

        # dp[1] = 9 (1..9), and the number 0 -> 10 counted so far
        res, dp = 10, 9

        # NOTE !!! (11 - i) == 10 - (i-1) : the digits still unused at step i
        for i in range(2, n + 1):
            dp *= 11 - i
            res += dp

        return res


# V1
# IDEA : same recurrence, built for all 9 lengths up front and sliced by n
# http://bookshadow.com/weblog/2016/06/13/leetcode-count-numbers-with-unique-digits/
# time = O(1), space = O(1)
class Solution2(object):
    def countNumbersWithUniqueDigits(self, n):
        """
        :type n: int
        :rtype: int
        """
        nums = [9]
        for x in range(9, 0, -1):
            nums += nums[-1] * x,
        return sum(nums[:n]) + 1
