"""

There is a fence with n posts, each post can be painted with one of the k colors. You have to paint all the posts such that no more than two adjacent fence posts have the same color. Return the total number of ways you can paint the fence.

Java Solution
The key to solve this problem is finding this relation.

f(n) = (k-1)(f(n-1)+f(n-2))

Assuming there are 3 posts,
if the first one and the second one has the same color, 
then the third one has k-1 options. The first and second together has k options.
If the first and the second do not have same color, the total is k * (k-1), 
then the third one has k options.
Therefore, f(3) = (k-1)*k + k*(k-1)*k = (k-1)(k+k*k)

"""

# # V1  : dev 
# # https://www.programcreek.com/2014/05/leetcode-pain-fence-java/

# class Solution(object):
#     def numWays(self, n, k):
#         for i in range(2,n):
#             dp[3] = (k-1)*(dp(1) + dp(2))
#             dp[1] = dp[2]
#             dp[2] = dp[3]

#         return dp[3]


# V2 
# http://www.voidcn.com/article/p-cqzythcg-qp.html
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


# V3 
# Time:  O(n)
# Space: O(1)
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

# V4 
# Time:  O(n)
# Space: O(n)
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
