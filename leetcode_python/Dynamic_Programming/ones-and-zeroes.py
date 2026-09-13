"""

474. Ones and Zeroes
Medium

You are given an array of binary strings strs and two integers m and n.

Return the size of the largest subset of strs such that there are at most m 0's and n 1's in the subset.

A set x is a subset of a set y if all elements of x are also elements of y.

 

Example 1:

Input: strs = ["10","0001","111001","1","0"], m = 5, n = 3
Output: 4
Explanation: The largest subset with at most 5 0's and 3 1's is {"10", "0001", "1", "0"}, so the answer is 4.
Other valid but smaller subsets include {"0001", "1"} and {"10", "1", "0"}.
{"111001"} is an invalid subset because it contains 4 1's, greater than the maximum of 3.
Example 2:

Input: strs = ["10","0","1"], m = 1, n = 1
Output: 2
Explanation: The largest subset is {"0", "1"}, so the answer is 2.
 

Constraints:

1 <= strs.length <= 600
1 <= strs[i].length <= 100
strs[i] consists only of digits '0' and '1'.
1 <= m, n <= 100
 

"""

# V0
class Solution(object):
    def findMaxForm(self, strs, m, n):
        """
        :type strs: List[str]
        :type m: int
        :type n: int
        :rtype: int
        """
        pass


# V0-1
# IDEA: 2D 0/1 KNAPSACK (gemini)
"""
DP def

    
    dp[i][j]: 使用最多 i 個 0 和最多 j 個 1，
              最多可以選幾個 strings



DP eq

    
    dp[i][j] = max(
            dp[i][j],
            dp[i - zeros][j - ones] + 1
        )

"""
class Solution(object):
    def findMaxForm(self, strs, m, n):
        # dp[i][j] 代表使用最多 i 個 '0' 與 j 個 '1' 所能拼出的最大字串數量
        dp = [[0] * (n + 1) for _ in range(m + 1)]

        for s in strs:
            # 統計當前字串消耗的代價
            zeros = s.count('0')
            ones = s.count('1')

            # 0/1 背包 2D 滾動陣列：兩個維度皆需倒序遍歷，防止重複選取同一個字串
            for i in range(m, zeros - 1, -1):
                for j in range(n, ones - 1, -1):
                    dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)

        return dp[m][n]


# V0-2
# IDEA: 2D 0/1 KNAPSACK (GPT)
class Solution(object):
    def findMaxForm(self, strs, m, n):

        # dp[i][j] = maximum number of strings
        # using at most i zeros and j ones
        dp = [[0] * (n + 1) for _ in range(m + 1)]

        for s in strs:
            zeros = s.count("0")
            ones = s.count("1")

            # 0/1 knapsack -> iterate backwards
            for i in range(m, zeros - 1, -1):
                for j in range(n, ones - 1, -1):

                    # choose this string
                    dp[i][j] = max(
                        dp[i][j],
                        dp[i - zeros][j - ones] + 1
                    )

        return dp[m][n]


# V1 
# http://bookshadow.com/weblog/2016/12/11/leetcode-ones-and-zeroes/
# IDEA : DP
# DP STATUS EQUATION:
# for s in strs:
#     zero, one = s.count('0'), s.count('1')
#     for x in range(m, zero - 1, -1):
#         for y in range(n, one - 1, -1):
#             dp[x][y] = max(dp[x - zero][y - one] + 1, dp[x][y])        
"""

DP def
    (0/1 KNAPSACK with TWO capacities)

    dp[x][y]: the largest subset size buildable with at most x zeros

              and at most y ones

DP eq

     for each string s with zero = s.count('0'), one = s.count('1'):

        for x from m DOWN to zero:
            for y from n DOWN to one:

                dp[x][y] = max( dp[x][y], dp[x-zero][y-one] + 1 )


    -> e.g. NOTE !!! BOTH capacity loops run DOWNWARD - that is what keeps
              each string usable at most once (0/1, not unbounded)

     init: dp = all 0
     ans = dp[m][n]

"""
# time = O(s * m * n)  # s = len(strs)
# space = O(m * n)
class Solution(object):
    def findMaxForm(self, strs, m, n):
        """
        :type strs: List[str]
        :type m: int
        :type n: int
        :rtype: int
        """
        dp = [[0] * (n + 1) for x in range(m + 1)]
        for s in strs:
            zero, one = s.count('0'), s.count('1')
            for x in range(m, zero - 1, -1):
                for y in range(n, one - 1, -1):
                    dp[x][y] = max(dp[x - zero][y - one] + 1, dp[x][y])
        return dp[m][n]
        
# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82825032
"""

DP def
    (0/1 KNAPSACK with TWO capacities)

    dp[x][y]: the largest subset size buildable with at most x zeros

              and at most y ones

DP eq

     for each string s with zero = s.count('0'), one = s.count('1'):

        for x from m DOWN to zero:
            for y from n DOWN to one:

                dp[x][y] = max( dp[x][y], dp[x-zero][y-one] + 1 )


    -> e.g. NOTE !!! BOTH capacity loops run DOWNWARD - that is what keeps
              each string usable at most once (0/1, not unbounded)

     init: dp = all 0
     ans = dp[m][n]

"""
# time = O(s * m * n)  # s = len(strs)
# space = O(m * n)
class Solution(object):
    def findMaxForm(self, strs, m, n):
        """
        :type strs: List[str]
        :type m: int
        :type n: int
        :rtype: int
        """
        # m of 0, n of 1 that can be collected as longest string 
        dp = [[0 for _ in range(n + 1)] for _ in range(m + 1)]
        for str in strs:
            zeros, ones = 0, 0
            for c in str:
                if c == "0":
                    zeros += 1
                elif c == "1":
                    ones += 1
            for i in range(m, zeros - 1, -1):
                for j in range(n, ones - 1, -1):
                    dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)
        return dp[m][n]


# V1''
# https://www.jiuzhang.com/solution/ones-and-zeroes/#tag-highlight-lang-python
# IDEA : DP
"""

DP def
    (0/1 KNAPSACK with TWO capacities)

    dp[x][y]: the largest subset size buildable with at most x zeros

              and at most y ones

DP eq

     for each string s with zero = s.count('0'), one = s.count('1'):

        for x from m DOWN to zero:
            for y from n DOWN to one:

                dp[x][y] = max( dp[x][y], dp[x-zero][y-one] + 1 )


    -> e.g. NOTE !!! BOTH capacity loops run DOWNWARD - that is what keeps
              each string usable at most once (0/1, not unbounded)

     init: dp = all 0
     ans = dp[m][n]

"""
# time = O(s * m * n)  # s = len(strs)
# space = O(m * n)
class Solution:
    """
    @param strs: an array with strings include only 0 and 1
    @param m: An integer
    @param n: An integer
    @return: find the maximum number of strings
    """
    def findMaxForm(self, strs, m, n):
        # write your code here
        dp = [[0] * (m + 1) for _ in range(n + 1)]
        for s in strs:
            zero = 0
            one = 0
            for ch in s:
                if ch == "1":
                    one += 1
                else:
                    zero += 1
            for i in range(n,one - 1,-1):
                for j in range(m,zero - 1,-1):
                    if dp[i - one][j - zero] + 1 > dp[i][j]:
                        dp[i][j] = dp[i - one][j - zero] + 1
        return dp[-1][-1]


# V2
"""

DP def
    (0/1 KNAPSACK with TWO capacities)

    dp[x][y]: the largest subset size buildable with at most x zeros

              and at most y ones

DP eq

     for each string s with zero = s.count('0'), one = s.count('1'):

        for x from m DOWN to zero:
            for y from n DOWN to one:

                dp[x][y] = max( dp[x][y], dp[x-zero][y-one] + 1 )


    -> e.g. NOTE !!! BOTH capacity loops run DOWNWARD - that is what keeps
              each string usable at most once (0/1, not unbounded)

     init: dp = all 0
     ans = dp[m][n]

"""
# time = O(s * m * n), s is the size of the array.
# space = O(m * n)
class Solution(object):
    def findMaxForm(self, strs, m, n):
        """
        :type strs: List[str]
        :type m: int
        :type n: int
        :rtype: int
        """
        dp = [[0 for _ in range(n+1)] for _ in range(m+1)]
        for s in strs:
            zero_count, one_count = 0, 0
            for c in s:
                if c == '0':
                    zero_count += 1
                elif c == '1':
                    one_count += 1

            for i in reversed(range(zero_count, m+1)):
            	for j in reversed(range(one_count, n+1)):
                    dp[i][j] = max(dp[i][j], dp[i-zero_count][j-one_count]+1)
        return dp[m][n]
