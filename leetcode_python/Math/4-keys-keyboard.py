# [Leetcode] 651. 4 Keys Keyboard

# Imagine you have a special keyboard with the following keys:

# Key 1: (A): Print one 'A' on screen.

# Key 2: (Ctrl-A): Select the whole screen.

# Key 3: (Ctrl-C): Copy selection to buffer.

# Key 4: (Ctrl-V): Print buffer on screen appending it after what has already been printed.

# Now, you can only press the keyboard for N times (with the above four keys), find out the maximum numbers of 'A' you can print on screen.

# Example 1:

# Input: N = 3
# Output: 3
# Explanation: 
# We can at most get 3 A's on screen by pressing following key sequence:
# A, A, A
# Example 2:

# Input: N = 7
# Output: 9
# Explanation: 
# We can at most get 9 A's on screen by pressing following key sequence:
# A, A, A, Ctrl A, Ctrl C, Ctrl V, Ctrl V
# Note:

# 1 <= N <= 50
# Answers will be in the range of 32-bit signed integer.


# V1 : dev 
import collections


# V2 
# http://bookshadow.com/weblog/2017/07/30/leetcode-4-keys-keyboard/
class Solution(object):
    def maxA(self, N):
        """
        :type N: int
        :rtype: int
        """
        dp = collections.defaultdict(lambda : collections.defaultdict(int))
        dp[0][0] = 0 #step, buffer
        for z in range(N):
            for y in dp[z]:
                #Key 1: (A):
                dp[z + 1][y] = max(dp[z + 1][y], dp[z][y] + 1)
                #Key 4: (Ctrl-V):
                dp[z + 1][y] = max(dp[z + 1][y], dp[z][y] + y)
                #Key 2: (Ctrl-A): + Key 3: (Ctrl-C):
                dp[z + 2][dp[z][y]] = max(dp[z + 2][dp[z][y]], dp[z][y])
        return max(dp[N].values())

# V3 
# Time:  O(1)
# Space: O(1)
class Solution(object):
    def maxA(self, N):
        """
        :type N: int
        :rtype: int
        """
        if N < 7:
            return N
        if N == 10:
            return 20  # the following rule doesn't hold when N = 10

        n = N // 5 + 1  # n3 + n4 increases one every 5 keys
        # (1) n     =     n3 +     n4
        # (2) N + 1 = 4 * n3 + 5 * n4
        #     5 x (1) - (2) => 5*n - N - 1 = n3
        n3 = 5*n - N - 1
        n4 = n - n3
        return 3**n3 * 4**n4

# V3' 
# Time:  O(n)
# Space: O(1)
class Solution2(object):
    def maxA(self, N):
        """
        :type N: int
        :rtype: int
        """
        if N < 7:
            return N
        dp = list(range(N+1))
        for i in range(7, N+1):
            dp[i % 6] = max(dp[(i-4) % 6]*3, dp[(i-5) % 6]*4)
        return dp[N % 6]
