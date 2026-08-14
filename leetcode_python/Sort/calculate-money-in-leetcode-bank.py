"""

1716. Calculate Money in Leetcode Bank
Easy

Hercy wants to save money for his first car. He puts money in the Leetcode bank every day.

He starts by putting in $1 on Monday, the first day. Every day from Tuesday to Sunday, he will put in $1 more than the day before. On every subsequent Monday, he will put in $1 more than the previous Monday.

Given n, return the total amount of money he will have in the Leetcode bank at the end of the nth day.


Example 1:

Input: n = 4
Output: 10
Explanation: After the 4th day, the total is 1 + 2 + 3 + 4 = 10.

Example 2:

Input: n = 10
Output: 37
Explanation: After the 10th day, the total is (1 + 2 + 3 + 4 + 5 + 6 + 7) + (2 + 3 + 4) = 37. Notice that on the 2nd Monday, Hercy only puts in $2.

Example 3:

Input: n = 20
Output: 96
Explanation: After the 20th day, the total is (1 + 2 + 3 + 4 + 5 + 6 + 7) + (2 + 3 + 4 + 5 + 6 + 7 + 8) + (3 + 4 + 5 + 6 + 7 + 8) = 96.


Constraints:

1 <= n <= 1000

"""

# V0
# IDEA : MATH (arithmetic series over whole weeks + the leftover days)
#
#   week k (1-indexed) deposits  k, k+1, ..., k+6  -> sum = 7k + 21.
#
#   let k = n // 7 full weeks, b = n % 7 leftover days.
#
#   full weeks   : sum over k = 1..k of (7k + 21)
#                = 7 * k(k+1)/2 + 21k
#   leftover days: week k+1 starts at (k+1), so the b days are
#                  (k+1), (k+2), ..., (k+b)  -> b*(k+1) + b(b-1)/2
#
#   NOTE : closed form, so no loop is needed even though n <= 1000 is tiny.
#
# time = O(1), space = O(1)
class Solution(object):
    def totalMoney(self, n):
        k, b = divmod(n, 7)

        full = 7 * k * (k + 1) // 2 + 21 * k
        rest = b * (k + 1) + b * (b - 1) // 2

        return full + rest
