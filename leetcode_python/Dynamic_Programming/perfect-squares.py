"""

279. Perfect Squares
Solved
Medium
Topics
premium lock icon
Companies
Given an integer n, return the least number of perfect square numbers that sum to n.

A perfect square is an integer that is the square of an integer; in other words, it is the product of some integer with itself. For example, 1, 4, 9, and 16 are perfect squares while 3 and 11 are not.

 

Example 1:

Input: n = 12
Output: 3
Explanation: 12 = 4 + 4 + 4.
Example 2:

Input: n = 13
Output: 2
Explanation: 13 = 4 + 9.
 

Constraints:

1 <= n <= 104
"""



# V0
# IDEA : BFS
"""

DP def
    dp[i]: MINIMUM number of perfect squares summing to i

DP eq

     dp[i] = 1 + min( dp[i - s] )   over every perfect square s <= i


    -> e.g. an unbounded coin-change over the "coins" 1, 4, 9, 16, ...

     the same state graph can be walked with BFS instead: level t holds the
     values reachable with t squares, so the first time 0 is reached gives
     the minimum - identical states, different traversal order

     init: dp[0] = 0
     ans = dp[n]
           (Lagrange's four-square theorem also caps the answer at 4)

"""
# time = O(n * sqrt(n))
# space = O(n)
class Solution(object):
    def numSquares(self, n):
        if n < 2:
            return n
        lst = []
        i = 1
        while i * i <= n:
            lst.append( i * i )
            i += 1
        cnt = 0
        toCheck = {n}
        while toCheck:
            cnt += 1
            temp = set()
            for x in toCheck:
                for y in lst:
                    if x == y:
                        return cnt
                    if x < y:
                        break
                    temp.add(x-y)
            toCheck = temp
        return cnt

# V1 
# http://bookshadow.com/weblog/2015/09/09/leetcode-perfect-squares/
"""

DP def
    dp[i]: MINIMUM number of perfect squares summing to i

DP eq

     dp[i] = 1 + min( dp[i - s] )   over every perfect square s <= i


    -> e.g. an unbounded coin-change over the "coins" 1, 4, 9, 16, ...

     the same state graph can be walked with BFS instead: level t holds the
     values reachable with t squares, so the first time 0 is reached gives
     the minimum - identical states, different traversal order

     init: dp[0] = 0
     ans = dp[n]
           (Lagrange's four-square theorem also caps the answer at 4)

"""
# time out error
# time = O(n * sqrt(n))
# space = O(n)
class Solution(object):
    def numSquares(self, n):
        """
        :type n: int
        :rtype: int
        """
        dp = collections.defaultdict(int)
        y = 1
        while y * y <= n:
            dp[y * y] = 1
            y += 1
        for x in range(1, n + 1):
            y = 1
            while x + y * y <= n:
                if x + y * y not in dp or dp[x] + 1 < dp[x + y * y]:
                    dp[x + y * y] = dp[x] + 1
                y += 1
        return dp[n]

### Test case : dev

# V1'
# IDEA : DP
# http://bookshadow.com/weblog/2015/09/09/leetcode-perfect-squares/
# https://leetcode.com/problems/perfect-squares/discuss/71512/Static-DP-C%2B%2B-12-ms-Python-172-ms-Ruby-384-ms
"""

DP def
    dp[i]: MINIMUM number of perfect squares summing to i

DP eq

     dp[i] = 1 + min( dp[i - s] )   over every perfect square s <= i


    -> e.g. an unbounded coin-change over the "coins" 1, 4, 9, 16, ...

     the same state graph can be walked with BFS instead: level t holds the
     values reachable with t squares, so the first time 0 is reached gives
     the minimum - identical states, different traversal order

     init: dp[0] = 0
     ans = dp[n]
           (Lagrange's four-square theorem also caps the answer at 4)

"""
# time = O(n * sqrt(n))
# space = O(n)
class Solution(object):
    _dp = [0]
    def numSquares(self, n):
        dp = self._dp
        while len(dp) <= n:
            dp += min(dp[-i*i] for i in range(1, int(len(dp)**0.5+1))) + 1
        return dp[n]

# V1''
# IDEA : BFS
# https://leetcode.com/problems/perfect-squares/discuss/71475/Short-Python-solution-using-BFS
# IDEA : -> CHECK the LC discussion pic (BFS)
"""

DP def
    dp[i]: MINIMUM number of perfect squares summing to i

DP eq

     dp[i] = 1 + min( dp[i - s] )   over every perfect square s <= i


    -> e.g. an unbounded coin-change over the "coins" 1, 4, 9, 16, ...

     the same state graph can be walked with BFS instead: level t holds the
     values reachable with t squares, so the first time 0 is reached gives
     the minimum - identical states, different traversal order

     init: dp[0] = 0
     ans = dp[n]
           (Lagrange's four-square theorem also caps the answer at 4)

"""
# time = O(n * sqrt(n))
# space = O(n)
class Solution(object):
    def numSquares(self, n):
        if n < 2:
            return n
        lst = []
        i = 1
        while i * i <= n:
            lst.append( i * i )
            i += 1
        cnt = 0
        toCheck = {n}
        while toCheck:
            cnt += 1
            temp = set()
            for x in toCheck:
                for y in lst:
                    if x == y:
                        return cnt
                    if x < y:
                        break
                    temp.add(x-y)
            toCheck = temp
        return cnt

# V2
"""

DP def
    dp[i]: MINIMUM number of perfect squares summing to i

DP eq

     dp[i] = 1 + min( dp[i - s] )   over every perfect square s <= i


    -> e.g. an unbounded coin-change over the "coins" 1, 4, 9, 16, ...

     the same state graph can be walked with BFS instead: level t holds the
     values reachable with t squares, so the first time 0 is reached gives
     the minimum - identical states, different traversal order

     init: dp[0] = 0
     ans = dp[n]
           (Lagrange's four-square theorem also caps the answer at 4)

"""
# time = O(n * sqrt(n))
# space = O(n)
class Solution(object):
    _num = [0]
    def numSquares(self, n):
        """
        :type n: int
        :rtype: int
        """
        num = self._num
        while len(num) <= n:
            num += min(num[-i*i] for i in range(1, int(len(num)**0.5+1))) + 1
        return num[n]
