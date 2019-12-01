# V0 

# V1 

# V1'
# https://www.jiuzhang.com/solution/new-21-game/#tag-highlight-lang-python
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