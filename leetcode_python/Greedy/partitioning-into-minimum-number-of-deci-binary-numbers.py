"""

1689. Partitioning Into Minimum Number Of Deci-Binary Numbers
Medium

A decimal number is called deci-binary if each of its digits is either 0 or 1 without any leading
zeros. For example, 101 and 1100 are deci-binary, while 112 and 3001 are not.

Given a string n that represents a positive decimal integer, return the minimum number of positive
deci-binary numbers needed so that they sum up to n.


Example 1:

Input: n = "32"
Output: 3
Explanation: 10 + 11 + 11 = 32

Example 2:

Input: n = "82734"
Output: 8

Example 3:

Input: n = "27346209830709182346"
Output: 9


Constraints:

1 <= n.length <= 10^5
n consists of only digits.
n does not contain any leading zeros and represents a positive integer.

"""

# V0
# IDEA : GREEDY / MAX DIGIT (each deci-binary number contributes at most 1 per column)
#
#   the summands never carry (each digit column receives only 0s and 1s, and the
#   column total is exactly the digit of n, at most 9). so a column with digit d
#   needs at least d summands -> the answer is at least max(digits).
#
#   that bound is achievable: with t = max(digits) numbers, put a 1 in the k-th
#   number's column c for k < digit[c]; every column then sums to its digit.
#
# time = O(len(n)), space = O(1)
class Solution(object):
    def minPartitions(self, n):
        best = 0
        for ch in n:
            d = ord(ch) - ord("0")
            if d > best:
                best = d
        return best
