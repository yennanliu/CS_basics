"""

2048. Next Greater Numerically Balanced Number
Medium

An integer x is numerically balanced if for every digit d in the number x, there are exactly d occurrences of that digit in x.

Given an integer n, return the smallest numerically balanced number strictly greater than n.


Example 1:

Input: n = 1
Output: 22
Explanation:
22 is numerically balanced since:
- The digit 2 occurs 2 times.
It is also the smallest numerically balanced number strictly greater than 1.

Example 2:

Input: n = 1000
Output: 1333
Explanation:
1333 is numerically balanced since:
- The digit 1 occurs 1 time.
- The digit 3 occurs 3 times.
It is also the smallest numerically balanced number strictly greater than 1000.
Note that 1333 has the same number of occurrences of the digit 1 and 3.

Example 3:

Input: n = 3000
Output: 3133
Explanation:
3133 is numerically balanced since:
- The digit 1 occurs 1 time.
- The digit 3 occurs 3 times.
It is also the smallest numerically balanced number strictly greater than 3000.


Constraints:

0 <= n <= 10^6

"""

# V0
# IDEA : BRUTE-FORCE SCAN UPWARD (the search space is provably tiny)
#
#   n <= 10^6, and 1224444 is balanced, so the answer never exceeds ~1.3 * 10^6.
#   that makes a plain "check every candidate" loop fast enough.
#
#   balanced check : count the digits and require  cnt[d] == d  for every
#   digit d that actually appears. note '0' can never appear (it would need
#   zero occurrences of itself, a contradiction) and the Counter naturally
#   rejects it since cnt['0'] >= 1 != 0.
#
# time = O(A * log A) with A the answer, space = O(1)
from collections import Counter


class Solution(object):
    def nextBeautifulNumber(self, n):
        def balanced(x):
            cnt = Counter(str(x))
            return all(int(d) == c for d, c in cnt.items())

        x = n + 1
        while not balanced(x):
            x += 1
        return x
