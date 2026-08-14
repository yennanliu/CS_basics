"""

902. Numbers At Most N Given Digit Set
Hard

Given an array of digits which is sorted in non-decreasing order.
You can write numbers using each digits[i] as many times as we want.
For example, if digits = ['1','3','5'], we may write numbers such as '13', '551',
and '1351315'.

Return the number of positive integers that can be generated that are less than or
equal to a given integer n.


Example 1:

Input: digits = ["1","3","5","7"], n = 100
Output: 20
Explanation:
The 20 numbers that can be written are:
1, 3, 5, 7, 11, 13, 15, 17, 31, 33, 35, 37, 51, 53, 55, 57, 71, 73, 75, 77.

Example 2:

Input: digits = ["1","4","9"], n = 1000000000
Output: 29523
Explanation:
We can write 3 one digit numbers, 9 two digit numbers, 27 three digit numbers,
81 four digit numbers, 243 five digit numbers, 729 six digit numbers,
2187 seven digit numbers, 6561 eight digit numbers, and 19683 nine digit numbers.
In total, this is 29523 integers that can be written using the digits array.

Example 3:

Input: digits = ["7"], n = 8
Output: 1


Constraints:

1 <= digits.length <= 9
digits[i].length == 1
digits[i] is a digit from '1' to '9'.
All the values in digits are unique.
digits is sorted in non-decreasing order.
1 <= n <= 10^9

"""

# V0
# IDEA : COMBINATORIAL COUNTING (digit by digit)
"""
 Let s = str(n), m = len(s), d = len(digits).

 1) every number with STRICTLY fewer digits than n is valid
       -> d^1 + d^2 + ... + d^(m-1)

 2) numbers with exactly m digits: walk the prefix of s.
    At position i, if we place a digit strictly smaller than s[i]
    (and the prefix so far equals s[:i]), the remaining m-1-i positions
    are free -> d^(m-1-i) choices.
    To keep walking we must be able to place s[i] itself; if s[i] is not
    in digits we can stop.

 3) if we survived the whole walk, n itself is constructible -> +1
"""
# time = O(m * d), m = number of digits of n
# space = O(1)
class Solution(object):
    def atMostNGivenDigitSet(self, digits, n):
        s = str(n)
        m = len(s)
        d = len(digits)
        digit_set = set(digits)

        res = 0

        # 1) all numbers strictly shorter than n
        for length in range(1, m):
            res += d ** length

        # 2) numbers of the same length, matching a prefix of n
        prefix_ok = True
        for i in range(m):
            cur = s[i]
            smaller = sum(1 for x in digits if x < cur)
            res += smaller * (d ** (m - 1 - i))
            if cur not in digit_set:
                # cannot keep the prefix equal to s[:i+1] -> stop
                prefix_ok = False
                break

        # 3) n itself
        if prefix_ok:
            res += 1

        return res
