# https://leetcode.ca/all/276.html


"""

276. Paint Fence
There is a fence with n posts, each post can be painted with one of the k colors.

You have to paint all the posts such that no more than two adjacent fence posts have the same color.

Return the total number of ways you can paint the fence.

Note:
n and k are non-negative integers.

Example:

Input: n = 3, k = 2
Output: 6
Explanation: Take c1 as color 1, c2 as color 2. All possible ways are:

            post1  post2  post3
 -----      -----  -----  -----
   1         c1     c1     c2
   2         c1     c2     c1
   3         c1     c2     c2
   4         c2     c1     c1 
   5         c2     c1     c2
   6         c2     c2     c1
Difficulty:
Easy
Lock:
Prime
Company:
Google
Problem Solution
276-Paint-Fence



"""

# V0
"""
DP def:



   dp[i][0] = the number of ways to paint fence posts 0...i
            where post i has the `same` color as post i-1.


   dp[i][1] = the number of ways to paint fence posts 0...i 
           where post i has a `different` color from post i-1.


DP eq:

    
    case 1) Last two posts have the same color
        
        dp[i][0] = dp[i - 1][1]


    case 2) Last two posts have different colors

         dp[i][1] = (dp[i - 1][0] + dp[i - 1][1]) * (k - 1)

"""
class Solution:
    def numWays(self, n: int, k: int) -> int:
        # `dp[i][0]` same color
        # `dp[i][1]` different color

        """
        NOTE !!!

        example dp:

        ```
        >>> n = 5

        >>> dp = [[0] * 2 for _ in range(n)]
        >>> dp
        [[0, 0], [0, 0], [0, 0], [0, 0], [0, 0]]
        >>>


        >>> dp[-1]
        [0, 0]
        ```


        """
        dp = [[0] * 2 for _ in range(n)]
        dp[0][1] = k
        for i in range(1, n):
            dp[i][0] = dp[i - 1][1] # same as previous, or else 3-posts the same color
            dp[i][1] = (dp[i - 1][0] + dp[i - 1][1]) * (k - 1) # not the same as previous
        return sum(dp[-1])

# V-1
# IDEA: DP (GPT)
# TODO: validate
class Solution:
    def numWays(self, n: int, k: int) -> int:
        if n == 1:
            return k

        # same = last two posts have the same color
        # diff = last two posts have different colors
        same = k
        diff = k * (k - 1)

        for _ in range(3, n + 1):
            same, diff = diff, (same + diff) * (k - 1)

        return same + diff


# V-2
# IDEA: DP (GPT)
# TODO: validate
class Solution(object):
    def numWays(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: int
        """
        # Edge cases
        if n == 0:
            return 0
        if n == 1:
            return k
        if n == 2:
            return k * k
            
        # Base cases for n = 2
        # same: Ways to paint the first 2 posts the SAME color (k choices for post 1, 1 for post 2)
        same = k
        # diff: Ways to paint the first 2 posts DIFFERENT colors (k choices for post 1, k-1 for post 2)
        diff = k * (k - 1)
        
        # Calculate iteratively for 3rd post up to the n-th post
        for _ in range(3, n + 1):
            prev_diff = diff
            
            # To paint it the same color, the previous two MUST have been different
            diff = (same + prev_diff) * (k - 1)
            same = prev_diff
            
        return same + diff


# V2-1
# https://leetcode.ca/2016-09-01-276-Paint-Fence/
class Solution:
    def numWays(self, n: int, k: int) -> int:
        # `dp[i][0]` same color
        # `dp[i][1]` different color
        dp = [[0] * 2 for _ in range(n)]
        dp[0][1] = k
        for i in range(1, n):
            dp[i][0] = dp[i - 1][1] # same as previous, or else 3-posts the same color
            dp[i][1] = (dp[i - 1][0] + dp[i - 1][1]) * (k - 1) # not the same as previous
        return sum(dp[-1])



# V2-2
# https://leetcode.ca/2016-09-01-276-Paint-Fence/
class Solution(object):
  def numWays(self, n, k):
    """
    :type n: int
    :type k: int
    :rtype: int
    """
    if n > 2 and k == 1:
      return 0
    if n < 2:
      return n * k
    pre = k * k
    ppre = k
    for i in range(2, n):
      tmp = pre
      pre = (k - 1) * (pre + ppre)
      ppre = tmp
    return pre




# V3
# http://www.voidcn.com/article/p-cqzythcg-qp.html
# time = O(n)
# space = O(1)
class Solution(object):
    def numWays(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: int
        """
        if n == 0: return 0
        if n == 1: return k

        # for the first 2 posts
        sameColor = k
        diffColor = k * (k - 1)

        for i in range(2, n):
            temp = diffColor
            diffColor = (sameColor + diffColor) * (k - 1)
            sameColor = temp

        return sameColor + diffColor


# V4
# time = O(n)
# space = O(1)
# V2
# DP solution with rolling window.
class Solution(object):
    def numWays(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: int
        """
        if n == 0:
            return 0
        elif n == 1:
            return k
        ways = [0] * 3
        ways[0] = k
        ways[1] = (k - 1) * ways[0] + k
        for i in range(2, n):
            ways[i % 3] = (k - 1) * (ways[(i - 1) % 3] + ways[(i - 2) % 3])
        return ways[(n - 1) % 3]

# V5
# time = O(n)
# space = O(n)
# DP solution.
class Solution2(object):
    def numWays(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: int
        """
        if n == 0:
            return 0
        elif n == 1:
            return k
        ways = [0] * n
        ways[0] = k
        ways[1] = (k - 1) * ways[0] + k
        for i in range(2, n):
            ways[i] = (k - 1) * (ways[i - 1] + ways[i - 2])
        return ways[n - 1]
