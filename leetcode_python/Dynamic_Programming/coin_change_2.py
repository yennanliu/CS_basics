"""

518. Coin Change 2
Medium

You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money.

Return the number of combinations that make up that amount. If that amount of money cannot be made up by any combination of the coins, return 0.

You may assume that you have an infinite number of each kind of coin.

The answer is guaranteed to fit into a signed 32-bit integer.

 

Example 1:

Input: amount = 5, coins = [1,2,5]
Output: 4
Explanation: there are four ways to make up the amount:
5=5
5=2+2+1
5=2+1+1+1
5=1+1+1+1+1
Example 2:

Input: amount = 3, coins = [2]
Output: 0
Explanation: the amount of 3 cannot be made up just with coins of 2.
Example 3:

Input: amount = 10, coins = [10]
Output: 1
 

Constraints:

1 <= coins.length <= 300
1 <= coins[i] <= 5000
All the values of coins are unique.
0 <= amount <= 5000

"""

# V0
# IDEA: 1D DP
"""
NOTE !!!


DP def:

    - dp[i] = number of ways 
              to make amount `i`


DP eq:

    - for each c,
        dp[i] += dp[i - c]


  
   -> e.g.:
        
        ways(amount=i)
        =
        existing ways
        +
        ways(amount=i-c) with coin c appended


"""
class Solution(object):
    def change(self, amount, coins):

        """
        # NOTE !!!
        # size = amount + 1


        -> why ?

        -> Because the index of dp represents the amount itself.


        -> example:

        If amount = 5, you need entries for:

        amount: 0 1 2 3 4 5
        index : 0 1 2 3 4 5


        -> so the size is `6`, not `5`


        """
        dp = [0] * (amount + 1)

        dp[0] = 1

        """
        NOTE !!!


        https://yennj12.js.org/CS_basics/cheatsheets/dp.html#critical-pattern-loop-order-in-unbounded-knapsack-combinations-vs-permutations


        Combinations (組合) VS Permutations (排列) in DP

        -> the `loop ordering`

        -> LC 518, is `combination`(組合)

        -> so what we need at outer loop is

            ```
            for coin in coins
            ```


        ->

         The core distinction explained:

          ┌───────────────────────┬───────────────┬────────────────────────────────────────────────────────────────────────┐
          │      Outer loop       │   What it     │                                  Why                                   │
          │                       │    counts     │                                                                        │
          ├───────────────────────┼───────────────┼────────────────────────────────────────────────────────────────────────┤
          │ for coin in coins     │ Combinations  │ Coin-1 pass happens globally before coin-2 is introduced — [2,1] can   │
          │                       │               │ never appear as a separate path                                        │
          ├───────────────────────┼───────────────┼────────────────────────────────────────────────────────────────────────┤
          │ for i in range(1,     │ Permutations  │ For each amount, asks "what was the last coin placed?" — every         │
          │ amount+1)             │               │ ordering is a distinct path                                            │
          └───────────────────────┴───────────────┴────────────────────────────────────────────────────────────────────────┘

          

        """
        for c in coins:
            for i in range(c, amount + 1):
                dp[i] += dp[i - c]

        """
        # NOTE !!!

        # use `amount` idx
        # since 


            - dp[i] = number of ways 
              to make amount `i`




        -> example:

        If amount = 5, you need entries for:

        amount: 0 1 2 3 4 5
        index : 0 1 2 3 4 5


        -> the idx of `amount=5` element
           is 5 !!! but NOT 6 

        """
        return dp[amount]



# V0-1
# IDEA: 1D DP
class Solution(object):
    def change(self, amount, coins):
        """
        :type amount: int
        :type coins: List[int]
        :rtype: int
        """
        # dp[i] stores the total number 
        # of combinations to make change for amount 'i'
        dp = [0] * (amount + 1)
        
        # CRITICAL FIX: Base Case. 
        # There is exactly 1 way to make an amount of 0 (using no coins).
        dp[0] = 1
        
        # CRITICAL FIX: Loop over coins on the OUTSIDE.
        # This forces the algorithm to process one coin entirely before moving to the next,
        # which completely prevents duplicate permutations (like counting [1,2] and [2,1] twice).
        for coin in coins:
            # We can only make change for amounts greater than or equal to the current coin value
            for i in range(coin, amount + 1):
                # The ways to form amount 'i' is its current ways plus the ways 
                # to form the remaining amount after subtracting this coin.
                dp[i] += dp[i - coin]
                
        # CRITICAL FIX: Return the final element at index 'amount'
        return dp[amount]



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
# IDEA : DP
# https://leetcode.com/problems/coin-change-2/solution/
class Solution:
    def change(self, amount: int, coins: List[int]) -> int:
        dp = [0] * (amount + 1)
        dp[0] = 1
        
        for coin in coins:
            for x in range(coin, amount + 1):
                dp[x] += dp[x - coin]
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