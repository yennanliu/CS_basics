"""

1137. N-th Tribonacci Number
Solved
Easy
Topics
premium lock icon
Companies
Hint
The Tribonacci sequence Tn is defined as follows: 

T0 = 0, T1 = 1, T2 = 1, and Tn+3 = Tn + Tn+1 + Tn+2 for n >= 0.

Given n, return the value of Tn.

 

Example 1:

Input: n = 4
Output: 4
Explanation:
T_3 = 0 + 1 + 1 = 2
T_4 = 1 + 1 + 2 = 4
Example 2:

Input: n = 25
Output: 1389537
 

Constraints:

0 <= n <= 37
The answer is guaranteed to fit within a 32-bit integer, ie. answer <= 2^31 - 1.


"""


# V0
class Solution(object):
    def tribonacci(self, n):
        """
        :type n: int
        :rtype: int
        """
        pass


# V1
# IDEA: 1D DP
class Solution(object):
    def tribonacci(self, n):
        """
        :type n: int
        :rtype: int
        """
        # edge
        if n == 0:
          return 0
        
        if n == 1 or n == 2:
          return 1

        dp = [0] * (n + 1)

        dp[0] = 0
        dp[1] = 1
        dp[2] = 1

        for i in range(3, n+1):
          # Tn+3 = Tn + Tn+1 + Tn+2
          dp[i] = (dp[i-3] + dp[i-2] + dp[i-1])


        return dp[n]

# V2
# IDEA: 1D DP (O(1) space)
class Solution(object):
    def tribonacci(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n == 0:
            return 0
        if n == 1 or n == 2:
            return 1

        T0 = 0
        T1 = 1
        T2 = 1

        for i in range(3, n + 1):
            val = T0 + T1 + T2
            T0 = T1  # Fixed: used digit '0' instead of letter 'O'
            T1 = T2
            T2 = val

        return T2
