"""

2417. Closest Fair Integer
Medium
(premium / locked problem)

You are given a positive integer n.

We call an integer k fair if the number of even digits in k is equal to the number of odd digits in it.

Return the smallest fair integer that is greater than or equal to n.


Example 1:

Input: n = 2
Output: 10
Explanation: The smallest fair integer that is greater than or equal to 2 is 10.
10 is fair because it has an equal number of even and odd digits (one even digit and one odd digit).

Example 2:

Input: n = 403
Output: 1001
Explanation: The smallest fair integer that is greater than or equal to 403 is 1001.
1001 is fair because it has an equal number of even and odd digits (two even digits and two odd digits).


Constraints:

1 <= n <= 10^9

"""

# V0
# IDEA : A FAIR NUMBER MUST HAVE AN EVEN DIGIT COUNT — JUMP OVER ODD LENGTHS
#
#   equal evens and odds means the digit count is even, so any n with an ODD
#   number of digits can be skipped entirely : the answer is the smallest
#   fair number one digit longer. with 2k digits that smallest value is
#       '1' + '0' * k + '1' * (k - 1)
#   (leading 1 is odd, then the k zeros supply all the evens, then k-1 more
#   ones finish the odds) — the smallest possible arrangement.
#
#   once the length is even, plain incrementing is fine : roughly a quarter
#   of the numbers of a given even length are fair, so the next one is only a
#   handful of steps away.
#
# time = O(gap * digits), space = O(digits)
class Solution(object):
    def closestFair(self, n):
        while True:
            s = str(n)
            if len(s) % 2:
                k = (len(s) + 1) // 2
                n = int('1' + '0' * k + '1' * (k - 1))
                continue
            even = sum(1 for c in s if int(c) % 2 == 0)
            if even * 2 == len(s):
                return n
            n += 1
