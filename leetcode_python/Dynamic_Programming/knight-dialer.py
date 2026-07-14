"""

935. Knight Dialer
Medium
Topics
premium lock icon
Companies
The chess knight has a unique movement, it may move two squares vertically and one square horizontally, or two squares horizontally and one square vertically (with both forming the shape of an L). The possible movements of chess knight are shown in this diagram:

A chess knight can move as indicated in the chess diagram below:


We have a chess knight and a phone pad as shown below, the knight can only stand on a numeric cell (i.e. blue cell).


Given an integer n, return how many distinct phone numbers of length n we can dial.

You are allowed to place the knight on any numeric cell initially and then you should perform n - 1 jumps to dial a number of length n. All jumps should be valid knight jumps.

As the answer may be very large, return the answer modulo 109 + 7.

 

Example 1:

Input: n = 1
Output: 10
Explanation: We need to dial a number of length 1, so placing the knight over any numeric cell of the 10 cells is sufficient.
Example 2:

Input: n = 2
Output: 20
Explanation: All the valid number we can dial are [04, 06, 16, 18, 27, 29, 34, 38, 40, 43, 49, 60, 61, 67, 72, 76, 81, 83, 92, 94]
Example 3:

Input: n = 3131
Output: 136006598
Explanation: Please take care of the mod.
 

Constraints:

1 <= n <= 5000

"""


# V0
class Solution(object):
    def knightDialer(self, n):
        """
        :type n: int
        :rtype: int
        """
        pass


# V0-1
# IDEA: DP (GPT)
class Solution:
    def knightDialer(self, n: int) -> int:
        MOD = 10 ** 9 + 7

        moves = [
            [4, 6],        # 0
            [6, 8],        # 1
            [7, 9],        # 2
            [4, 8],        # 3
            [0, 3, 9],     # 4
            [],            # 5
            [0, 1, 7],     # 6
            [2, 6],        # 7
            [1, 3],        # 8
            [2, 4]         # 9
        ]

        dp = [1] * 10

        for _ in range(n - 1):
            nxt = [0] * 10

            for digit in range(10):
                for nei in moves[digit]:
                    nxt[nei] = (nxt[nei] + dp[digit]) % MOD

            dp = nxt

        return sum(dp) % MOD


# V0-2
# IDEA: DP (GEMINI)
class Solution(object):
    def knightDialer(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n == 1:
            return 10
            
        MOD = 10**9 + 7
        
        # Pre-defined knight moves from each digit 0-9
        moves = [
            [4, 6],    # from 0
            [6, 8],    # from 1
            [7, 9],    # from 2
            [4, 8],    # from 3
            [0, 3, 9], # from 4
            [],        # from 5 (no outgoing moves)
            [0, 1, 7], # from 6
            [2, 6],    # from 7
            [1, 3],    # from 8
            [2, 4]     # from 9
        ]
        
        # dp[i] represents the number of valid sequences ending on digit `i`.
        # Base case for n = 1: 1 way to end on each digit.
        dp = [1] * 10
        
        # We already have length 1, so we do n - 1 hops
        for _ in range(n - 1):
            next_dp = [0] * 10
            
            # For each digit, look at where it can jump to
            for current_digit in range(10):
                for next_digit in moves[current_digit]:
                    next_dp[next_digit] = (next_dp[next_digit] + dp[current_digit]) % MOD
                    
            # Move to the next sequence length
            dp = next_dp
            
        return sum(dp) % MOD



# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/83716573
# time = O(n)
# space = O(1)
class Solution:
    def knightDialer(self, N):
        """
        :type N: int
        :rtype: int
        """
        if N == 1: return 10
        x1 = x2 = x3 = x4 = x5 = x6 = x7 = x8 = x9 = x0 = 1
        MOD = 10 ** 9 + 7
        for i in range(N - 1):
            x1, x2, x3, x4, x5, x6, x7, x8, x9, x0 = (x6 + x8) % MOD,\
                (x7 + x9) % MOD, (x4 + x8) % MOD, (x3 + x9 + x0) % MOD, 0, (x1 + x7 + x0) % MOD,\
                (x2 + x6) % MOD, (x1 + x3) % MOD, (x2 + x4) % MOD, (x4 + x6) % MOD
        return (x1 + x2 + x3 + x4 + x5 + x6 + x7 + x8 + x9 + x0) % MOD
        
# V1'
# https://www.jiuzhang.com/solution/knight-dialer/#tag-highlight-lang-python
# time = O(n)
# space = O(1)
class Solution(object):
    def knightDialer(self, N):
        MOD = 10**9 + 7
        moves = [[4,6],[6,8],[7,9],[4,8],[3,9,0],[],
                     [1,7,0],[2,6],[1,3],[2,4]]
        dp = [1] * 10
        for hops in range(N-1):
            dp2 = [0] * 10
            for node, count in enumerate(dp):
                for nei in moves[node]:
                    dp2[nei] += count
                    dp2[nei] %= MOD
            dp = dp2
        return sum(dp) % MOD

# V2
# time = O(logn)
# space = O(1)
import itertools
class Solution(object):
    def knightDialer(self, N):
        """
        :type N: int
        :rtype: int
        """
        def matrix_expo(A, K):
            result = [[int(i==j) for j in range(len(A))] \
                      for i in range(len(A))]
            while K:
                if K % 2:
                    result = matrix_mult(result, A)
                A = matrix_mult(A, A)
                K /= 2
            return result

        def matrix_mult(A, B):
            ZB = zip(*B)
            return [[sum(a*b for a, b in itertools.izip(row, col)) % M \
                     for col in ZB] for row in A]
        
        M = 10**9 + 7
        T = [[0, 0, 0, 0, 1, 0, 1, 0, 0, 0],
             [0, 0, 0, 0, 0, 0, 1, 0, 1, 0],
             [0, 0, 0, 0, 0, 0, 0, 1, 0, 1],
             [0, 0, 0, 0, 1, 0, 0, 0, 1, 0],
             [1, 0, 0, 1, 0, 0, 0, 0, 0, 1],
             [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
             [1, 1, 0, 0, 0, 0, 0, 1, 0, 0],
             [0, 0, 1, 0, 0, 0, 1, 0, 0, 0],
             [0, 1, 0, 1, 0, 0, 0, 0, 0, 0],
             [0, 0, 1, 0, 1, 0, 0, 0, 0, 0]]
        return sum(map(sum, matrix_expo(T, N-1))) % M

# time = O(n)
# space = O(1)
class Solution2(object):
    def knightDialer(self, N):
        """
        :type N: int
        :rtype: int
        """
        M = 10**9 + 7
        moves = [[4, 6], [6, 8], [7, 9], [4, 8], [3, 9, 0], [],
                 [1, 7, 0], [2, 6], [1, 3], [2, 4]]

        dp = [[1 for _ in range(10)] for _ in range(2)]
        for i in range(N-1):
            dp[(i+1) % 2] = [0] * 10
            for j in range(10):
                for nei in moves[j]:
                    dp[(i+1) % 2][nei] += dp[i % 2][j]
                    dp[(i+1) % 2][nei] %= M
        return sum(dp[(N-1) % 2]) % M
