"""

233. Number of Digit One
Hard

Given an integer n, count the total number of digit 1 appearing in all non-negative
integers less than or equal to n.


Example 1:

Input: n = 13
Output: 6

Example 2:

Input: n = 0
Output: 0


Constraints:

0 <= n <= 10^9

"""

# V0
# IDEA : MATH - count the 1s contributed by each digit position independently
#
#  Split n around the position with place value i (1, 10, 100, ...):
#      high = n // (i * 10),  cur = (n // i) % 10,  low = n % i
#
#  How many numbers in [0, n] have digit 1 at this position?
#    - cur == 0 : high * i
#         the position can only be 1 while high is strictly below `high`
#    - cur == 1 : high * i + low + 1
#         same as above, PLUS the partial block capped by the low digits
#    - cur >  1 : (high + 1) * i
#         the whole block with prefix == high also counts
#
# time = O(log n)
# space = O(1)
class Solution(object):
    def countDigitOne(self, n):
        if n <= 0:
            return 0

        count = 0
        i = 1  # current place value: 1, 10, 100, ...
        while i <= n:
            high = n // (i * 10)
            cur = (n // i) % 10
            low = n % i

            if cur == 0:
                count += high * i
            elif cur == 1:
                count += high * i + low + 1
            else:
                count += (high + 1) * i

            i *= 10

        return count
