"""

96. Unique Binary Search Trees
Medium

Given an integer n, return the number of structurally unique BST's (binary search trees) which has exactly n nodes of unique values from 1 to n.

 

Example 1:


Input: n = 3
Output: 5
Example 2:

Input: n = 1
Output: 1
 

Constraints:

1 <= n <= 19

"""

# V0 


# V1
# IDEA : DP
# https://leetcode.com/problems/unique-binary-search-trees/solution/
class Solution:
    def numTrees(self, n):
        """
        :type n: int
        :rtype: int
        """
        G = [0]*(n+1)
        G[0], G[1] = 1, 1

        for i in range(2, n+1):
            for j in range(1, i+1):
                G[i] += G[j-1] * G[i-j]

        return G[n]

# V1'
# IDEA : MATH
# https://leetcode.com/problems/unique-binary-search-trees/solution/
class Solution(object):
    def numTrees(self, n):
        """
        :type n: int
        :rtype: int
        """
        C = 1
        for i in range(0, n):
            C = C * 2*(2*i+1)/(i+2)
        return int(C)

# V1'
# IDEA : recursion (TLE)
# https://leetcode.com/problems/unique-binary-search-trees/discuss/164915/Python-solution
class Solution:
    def numTrees(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n == 0:
            return 1
        res = 0
        for i in range(1, n+1):
            res += self.numTrees(i-1)*self.numTrees(n-i)
        return res

# V1'''
# IDEA : DP
# https://leetcode.com/problems/unique-binary-search-trees/discuss/164915/Python-solution
class Solution:
    def numTrees(self, n):
        """
        :type n: int
        :rtype: int
        """
        arr = [0]*(n+1)
        arr[0] = 1
        for i in range(1, n+1):
            for j in range(1, i+1):
                arr[i] += arr[j-1] * arr[i-j]
        return arr[-1]

# V1'''''
# https://blog.csdn.net/fuxuemingzhu/article/details/79367789
class Solution(object):
    def __init__(self):
        self.dp = dict()
    
    def numTrees(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n in self.dp:
            return self.dp[n]
        if n == 0 or n == 1:
            return 1
        ans = 0
        for i in range(1, n + 1):
            ans += self.numTrees(i - 1) * self.numTrees(n - i)
        self.dp[n] = ans
        return ans

# V1'''''''
class Solution(object):
    def numTrees(self, n):
        """
        :type n: int
        :rtype: int
        """
        dp = [1, 1]
        for i in range(2, n + 1):
            count = 0
            for j in range(i):
                count += dp[j] * dp[i - j - 1]
            dp.append(count)
        return dp.pop()

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def numTrees(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n == 0:
            return 1

        def combination(n, k):
            count = 1
            # C(n, k) = (n) / 1 * (n - 1) / 2 ... * (n - k + 1) / k
            for i in range(1, k + 1):
                count = count * (n - i + 1) / i
            return count

        return combination(2 * n, n) - combination(2 * n, n - 1)

# Time:  O(n^2)
# Space: O(n)
# DP solution.
class Solution2(object):
    # @return an integer
    def numTrees(self, n):
        counts = [1, 1]
        for i in range(2, n + 1):
            count = 0
            for j in range(i):
                count += counts[j] * counts[i - j - 1]
            counts.append(count)
        return counts[-1]