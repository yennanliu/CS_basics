"""

1259. Handshakes That Don't Cross
Hard

You are given an even number of people numPeople that stand around a circle
and each person shakes hands with someone else so that there are
numPeople / 2 handshakes total.

Return the number of ways these handshakes could occur such that none of the
handshakes cross.

Since the answer could be very large, return it modulo 10^9 + 7.


Example 1:

Input: numPeople = 4
Output: 2
Explanation: There are two ways to do it, the first way is [(1,2),(3,4)] and the second one is [(2,3),(4,1)].

Example 2:

Input: numPeople = 6
Output: 5


Constraints:

2 <= numPeople <= 1000
numPeople is even.

"""

# V0
# IDEA : DP (Catalan number recurrence)
#
#  dp[i] = number of non crossing handshakes for i people (i is even)
#
#  person 1 shakes hands with someone, splitting the rest of the circle
#  into a left part (l people) and a right part (i - l - 2 people),
#  both of which must have an EVEN size (otherwise the chord crosses):
#
#    dp[i] = sum( dp[l] * dp[i - l - 2] ) for l = 0, 2, 4, ... , i - 2
#
# time = O(n^2)
# space = O(n)
class Solution(object):
    def numberOfWays(self, numPeople):
        MOD = 10 ** 9 + 7
        dp = [0] * (numPeople + 1)
        dp[0] = 1
        for i in range(2, numPeople + 1, 2):
            total = 0
            for l in range(0, i, 2):
                total += dp[l] * dp[i - l - 2]
            dp[i] = total % MOD
        return dp[numPeople]
