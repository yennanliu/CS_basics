# V0 
# IDEA : DP
# DP EQUATION : dp[i] += dp[i - coin]
class Solution(object):
    def change(self, amount, coins):
        """
        :type amount: int
        :type coins: List[int]
        :rtype: int
        """
        # beware of it : e.g.  dp[3] needs to have [0,1,2,3], so length needs to be (amount + 1) 
        dp = [0] * (amount + 1)
        dp[0] = 1
        for coin in coins:
            for i in range(1, amount + 1):
                # beware of it 
                if coin <= i:
                    # beware of it 
                    dp[i] += dp[i - coin]
        return dp[amount]

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82845212
# IDEA : DP
# DP EQUATION : dp[i] += dp[i - coin], when coin <= i 
class Solution(object):
    def change(self, amount, coins):
        """
        :type amount: int
        :type coins: List[int]
        :rtype: int
        """
        dp = [0] * (amount + 1)
        dp[0] = 1
        for coin in coins:
            for i in range(1, amount + 1):
                if coin <= i:
                    dp[i] += dp[i - coin]
        return dp[amount]

# V1'
# https://www.jiuzhang.com/solution/coin-change-ii/#tag-highlight-lang-python
class Solution:
    """
    @param amount: a total amount of money amount
    @param coins: the denomination of each coin
    @return: the number of combinations that make up the amount
    """
    def change(self, amount, coins):
        # write your code here
        dp = [0] * (amount + 1)
        dp[0] = 1
        for i in range(len(coins)):
            for j in range(coins[i], amount + 1):
                dp[j] += dp[j - coins[i]]
        return dp[amount]

# V2