# V0 

# V1 

# V1'
# https://www.jiuzhang.com/solution/new-21-game/#tag-highlight-lang-python
"""

DP def
    dp[i]: PROBABILITY of the running total ever being exactly i

           -> Alice draws while her total is < K, each draw uniform in [1, W]

DP eq

     dp[i] = ( dp[i-1] + dp[i-2] + ... + dp[i-W] ) / W

             counting only the terms with index < K
             (a total >= K stops drawing, so it can never be a predecessor)


    -> e.g. that sum is a SLIDING WINDOW - keep `Wsum`, add dp[i] when
              i < K, and subtract dp[i-W] when it leaves the window
              -> O(N) instead of O(N * W)

     edge: K == 0 or N >= K + W - 1  ->  probability 1.0

     init: dp[0] = 1.0, Wsum = 1.0
     ans = sum(dp[K..N])

"""
# time = O(N)
# space = O(N)
class Solution:
    """
    @param N: int
    @param K: int
    @param W: int
    @return: the probability
    """
    def new21Game(self, N, K, W):
        # Write your code here.
        if K == 0 or N >= K + W: 
            return 1.0
        dp = [1.0] + [0.0] * N
        Wsum = 1.0
        for i in range(1, N + 1):
            dp[i] = Wsum / W
            if i < K: 
                Wsum += dp[i]
            if i - W >= 0: 
                Wsum -= dp[i - W]
        return sum(dp[K:])

# V2 