"""

1049. Last Stone Weight II
Solved
Medium
Topics
premium lock icon
Companies
Hint
You are given an array of integers stones where stones[i] is the weight of the ith stone.

We are playing a game with the stones. On each turn, we choose any two stones and smash them together. Suppose the stones have weights x and y with x <= y. The result of this smash is:

If x == y, both stones are destroyed, and
If x != y, the stone of weight x is destroyed, and the stone of weight y has new weight y - x.
At the end of the game, there is at most one stone left.

Return the smallest possible weight of the left stone. If there are no stones left, return 0.

 

Example 1:

Input: stones = [2,7,4,1,8,1]
Output: 1
Explanation:
We can combine 2 and 4 to get 2, so the array converts to [2,7,1,8,1] then,
we can combine 7 and 8 to get 1, so the array converts to [2,1,1,1] then,
we can combine 2 and 1 to get 1, so the array converts to [1,1,1] then,
we can combine 1 and 1 to get 0, so the array converts to [1], then that's the optimal value.
Example 2:

Input: stones = [31,26,33,21,40]
Output: 5
 

Constraints:

1 <= stones.length <= 30
1 <= stones[i] <= 100

"""

# V0
"""

DP def
    smashing pairs is the same as giving every stone a + or - sign, so the
    final weight is |sum(+group) - sum(-group)|. with S = total sum, if one
    group sums to s the answer is |S - 2s| -> we want s as close to S/2 as
    possible WITHOUT exceeding it -> a 0/1 subset-sum knapsack.

    dp[j]: True if some subset of the stones sums to exactly j

           (or: dp[j] = the best achievable sum <= j)

DP eq

     for each stone w:
         for j from S//2 DOWN to w:

             dp[j] = dp[j] or dp[j - w]


    -> e.g. NOTE !!! the j loop runs DOWNWARD - this is 0/1 knapsack, each
              stone may be used ONCE

     init: dp[0] = True
     ans = min over achievable s <= S//2 of (S - 2 * s)

"""
class Solution(object):
    def lastStoneWeightII(self, stones):
        """
        :type stones: List[int]
        :rtype: int
        """
        pass



# V1-1
# IDEA: 1D DP (0/1 knapsack) (gpt)
# https://yennj12.js.org/CS_basics/cheatsheets/dp.html#0-1-dp
# https://github.com/yennanliu/CS_basics/issues/103
"""

DP def
    smashing pairs is the same as giving every stone a + or - sign, so the
    final weight is |sum(+group) - sum(-group)|. with S = total sum, if one
    group sums to s the answer is |S - 2s| -> we want s as close to S/2 as
    possible WITHOUT exceeding it -> a 0/1 subset-sum knapsack.

    dp[j]: True if some subset of the stones sums to exactly j

           (or: dp[j] = the best achievable sum <= j)

DP eq

     for each stone w:
         for j from S//2 DOWN to w:

             dp[j] = dp[j] or dp[j - w]


    -> e.g. NOTE !!! the j loop runs DOWNWARD - this is 0/1 knapsack, each
              stone may be used ONCE

     init: dp[0] = True
     ans = min over achievable s <= S//2 of (S - 2 * s)

"""
class Solution(object):
    def lastStoneWeightII(self, stones):
        """
        :type stones: List[int]
        :rtype: int
        """

        total = sum(stones)
        sub_sum = total // 2

        # dp[j] = maximum subset sum we can achieve
        # using capacity j
        dp = [0] * (sub_sum + 1)


        """
        (0/1 knacpack)

        dp[j]: max val / cnt can have with pack capacity = j

        for weight, val in items:
            for j in range(capacity, weight - 1, - 1):
                dp[j] = max(dp[j], dp[j - weight] + val)
                # or 
                #dp[j] += dp[j - weight]
                # or
                #dp[j] = min(dp[j], dp[j - weight] + 1)
        """

        # 0/1 Knapsack
        for x in stones:
            for j in range(sub_sum, x - 1, -1):
                dp[j] = max(dp[j], dp[j - x] + x)

        # Best subset sum <= total / 2
        s = dp[sub_sum]

        """
        NOTE !!! why  `total - 2 * s` ?


        ->

        split total to (total-s), (s)
        as 2 group.

        and the diff between above 2 group is:

        (total -s) - (s) = total - 2 * s
        """
        # Difference between the two groups
        return total - 2 * s


# V1-2
# IDEA: 1D DP (0/1 knapsack) (gemini)
"""

DP def
    smashing pairs is the same as giving every stone a + or - sign, so the
    final weight is |sum(+group) - sum(-group)|. with S = total sum, if one
    group sums to s the answer is |S - 2s| -> we want s as close to S/2 as
    possible WITHOUT exceeding it -> a 0/1 subset-sum knapsack.

    dp[j]: True if some subset of the stones sums to exactly j

           (or: dp[j] = the best achievable sum <= j)

DP eq

     for each stone w:
         for j from S//2 DOWN to w:

             dp[j] = dp[j] or dp[j - w]


    -> e.g. NOTE !!! the j loop runs DOWNWARD - this is 0/1 knapsack, each
              stone may be used ONCE

     init: dp[0] = True
     ans = min over achievable s <= S//2 of (S - 2 * s)

"""
class Solution(object):
    def lastStoneWeightII(self, stones):
        """
        :type stones: List[int]
        :rtype: int
        """
        total = sum(stones)
        sub_sum = total // 2

        # dp[j] = maximum stone weight achievable with capacity j
        dp = [0] * (sub_sum + 1)

        # 0/1 Knapsack: weight = val = x
        for x in stones:
            for j in range(sub_sum, x - 1, -1):
                dp[j] = max(dp[j], dp[j - x] + x)

        # S1 = dp[sub_sum], S2 = total - dp[sub_sum]
        # Diff = S2 - S1 = total - 2 * dp[sub_sum]
        return total - 2 * dp[sub_sum]


# V2
