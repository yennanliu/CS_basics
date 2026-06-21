"""

583. Delete Operation for Two Strings
Medium
Topics
premium lock icon
Companies
Given two strings word1 and word2, return the minimum number of steps required to make word1 and word2 the same.

In one step, you can delete exactly one character in either string.

 

Example 1:

Input: word1 = "sea", word2 = "eat"
Output: 2
Explanation: You need one step to make "sea" to "ea" and another step to make "eat" to "ea".
Example 2:

Input: word1 = "leetcode", word2 = "etco"
Output: 4
 

Constraints:

1 <= word1.length, word2.length <= 500
word1 and word2 consist of only lowercase English letters.


"""

# V0
# IDEA: DP
"""
NOTE !!!


DP def:

dp[i][j] = min deletions needed to make
           word1[:i], word2[:j] equal


DP eq:


dp[i][j] =
     if word1[i - 1] == word2[j - 1]:
         dp[i][j] = dp[i-1][j-1]
     else:
         1 + min(
           dp[i-1][j],   # delete word1[i-1]
           dp[i][j-1]    # delete word2[j-1]
        )


"""
class Solution(object):
    def minDistance(self, word1, word2):
        n1 = len(word1)
        n2 = len(word2)

        # dp[i][j] =
        # minimum deletions needed to make
        # word1[:i] and word2[:j] equal
        dp = [[0] * (n2 + 1) for _ in range(n1 + 1)]

        # Base Case 1:
        # word2 is empty.
        # Need to delete `all` characters from `word1[:i].`
        for i in range(n1 + 1):
            dp[i][0] = i

        # Base Case 2:
        # word1 is empty.
        # Need to delete `all` characters from `word2[:j].`
        for j in range(n2 + 1):
            dp[0][j] = j

        # Fill DP table
        for i in range(1, n1 + 1):
            for j in range(1, n2 + 1):

                # case 1) word1[i - 1] == word2[j - 1]
                # Current characters match
                if word1[i - 1] == word2[j - 1]:
                    dp[i][j] = dp[i - 1][j - 1]

                # case 2) word1[i - 1] != word2[j - 1]
                # Characters differ
                else:
                    dp[i][j] = 1 + min(
                        dp[i - 1][j],   # delete from word1
                        dp[i][j - 1]    # delete from word2
                    )

        return dp[n1][n2]



# V1
# IDEA: 2D DP
class Solution(object):
    def minDistance(self, word1, word2):
        """
        :type word1: str
        :type word2: str
        :rtype: int
        """
        n1 = len(word1)
        n2 = len(word2)
        
        # Step 1: Create a DP matrix where dp[i][j] stores the LCS length 
        # of word1[0...i-1] and word2[0...j-1]
        dp = [[0] * (n2 + 1) for _ in range(n1 + 1)]
        
        # Step 2: Build up the grid solutions bottom-up
        for i in range(1, n1 + 1):
            for j in range(1, n2 + 1):
                # If characters match, extend the shared subsequence length diagonally
                if word1[i - 1] == word2[j - 1]:
                    dp[i][j] = dp[i - 1][j - 1] + 1
                else:
                    # If they don't match, carry over the best path from left or top
                    dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
                    
        # Grab the maximum common character length found
        longest_common_subsequence = dp[n1][n2]
        
        # Step 3: Delete all unshared elements remaining across both strings
        return n1 + n2 - (2 * longest_common_subsequence)



# V1
# http://bookshadow.com/weblog/2017/05/15/leetcode-delete-operation-for-two-strings/
# IDEA : DP + Longest Common Subsequence
class Solution(object):
    def minDistance(self, word1, word2):
        """
        :type word1: str
        :type word2: str
        :rtype: int
        """
        return len(word1) + len(word2) - 2 * self.lcs(word1, word2)

    def lcs(self, word1, word2):
        len1, len2 = len(word1), len(word2)
        dp = [[0] * (len2 + 1) for x in range(len1 + 1)]
        for x in range(len1):
            for y in range(len2):
                dp[x + 1][y + 1] = max(dp[x][y + 1], dp[x + 1][y])
                if word1[x] == word2[y]:
                    dp[x + 1][y + 1] = dp[x][y] + 1
        return dp[len1][len2]

# V1'
# http://bookshadow.com/weblog/2017/05/15/leetcode-delete-operation-for-two-strings/
# IDEA : DP
# DP EQUATION : 
# dp[x][y] = x + y     if x == 0 or y == 0
# dp[x][y] = dp[x - 1][y - 1]     if word1[x] == word2[y]
# dp[x][y] = min(dp[x - 1][y], dp[x][y - 1]) + 1     otherwise
class Solution(object):
    def minDistance(self, word1, word2):
        """
        :type word1: str
        :type word2: str
        :rtype: int
        """
        len1, len2 = len(word1), len(word2)
        dp = [[0] * (len2 + 1) for x in range(len1 + 1)]
        for x in range(len1 + 1):
            for y in range(len2 + 1):
                if x == 0 or y == 0:
                    dp[x][y] = x + y
                elif word1[x - 1] == word2[y - 1]:
                    dp[x][y] = dp[x - 1][y - 1]
                else:
                    dp[x][y] = min(dp[x - 1][y], dp[x][y - 1]) + 1
        return dp[len1][len2]


# V1'
# https://www.jiuzhang.com/solution/delete-operation-for-two-strings/#tag-highlight-lang-python
class Solution:
    """
    @param word1: a string
    @param word2: a string
    @return: return a integer
    """
    def minDistance(self, word1, word2):
        # write your code here
        m, n = len(word1), len(word2)
        dp = [[0] * (n + 1) for i in range(m + 1)]
        for i in range(m):
            for j in range(n):
                dp[i + 1][j + 1] = max(dp[i][j + 1], dp[i + 1][j], dp[i][j] + (word1[i] == word2[j]))
        return m + n - 2 * dp[m][n]


# V2
# Time:  O(m * n)
# Space: O(n)
class Solution(object):
    def minDistance(self, word1, word2):
        """
        :type word1: str
        :type word2: str
        :rtype: int
        """
        m, n = len(word1), len(word2)
        dp = [[0] * (n+1) for _ in range(2)]
        for i in range(m):
            for j in range(n):
                dp[(i+1)%2][j+1] = max(dp[i%2][j+1], \
                                       dp[(i+1)%2][j], \
                                       dp[i%2][j] + (word1[i] == word2[j]))
        return m + n - 2*dp[m%2][n]