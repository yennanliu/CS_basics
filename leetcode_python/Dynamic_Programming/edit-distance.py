"""

72. Edit Distance
Hard

Given two strings word1 and word2, return the minimum number of operations required to convert word1 to word2.

You have the following three operations permitted on a word:

Insert a character
Delete a character
Replace a character
 

Example 1:

Input: word1 = "horse", word2 = "ros"
Output: 3
Explanation: 
horse -> rorse (replace 'h' with 'r')
rorse -> rose (remove 'r')
rose -> ros (remove 'e')
Example 2:

Input: word1 = "intention", word2 = "execution"
Output: 5
Explanation: 
intention -> inention (remove 't')
inention -> enention (replace 'i' with 'e')
enention -> exention (replace 'n' with 'x')
exention -> exection (replace 'n' with 'c')
exection -> execution (insert 'u')
 

Constraints:

0 <= word1.length, word2.length <= 500

"""

# V0

# V1
# IDEA : DP
# https://leetcode.com/problems/edit-distance/solution/
class Solution:
    def minDistance(self, word1, word2):
        """
        :type word1: str
        :type word2: str
        :rtype: int
        """
        n = len(word1)
        m = len(word2)
        
        # if one of the strings is empty
        if n * m == 0:
            return n + m
        
        # array to store the convertion history
        d = [ [0] * (m + 1) for _ in range(n + 1)]
        
        # init boundaries
        for i in range(n + 1):
            d[i][0] = i
        for j in range(m + 1):
            d[0][j] = j
        
        # DP compute 
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                left = d[i - 1][j] + 1
                down = d[i][j - 1] + 1
                left_down = d[i - 1][j - 1] 
                if word1[i - 1] != word2[j - 1]:
                    left_down += 1
                d[i][j] = min(left, down, left_down)
        
        return d[n][m]

# V1'
# IDEA : DP
# https://www.twblogs.net/a/61b587a5f84fc55b46945724
# C++
# class Solution {
# public:
# 	vector<vector<int>> dp;
# 	int minDistance(string word1, string word2) {
# 		int len = max(word1.size(), word2.size());
# 		dp = vector<vector<int>>(len+10,vector<int>(len+10));
# 		word1.insert(word1.begin(), '#');
# 		word2.insert(word2.begin(), '@');
#
# 		for (int i = 0; i <= word1.size(); i++) { dp[i][0] = i; }
# 		for (int i = 0; i <= word2.size(); i++) { dp[0][i] = i; }
#
# 		for (int i = 1; i <= word1.size(); i++) {
# 			for (int j = 1; j <= word2.size(); j++) {
# 				dp[i][j] = min(dp[i][j - 1]+1, dp[i - 1][j]+1);
# 				if (word1[i] == word2[j]) {
# 					dp[i][j] = min(dp[i][j], dp[i - 1][j - 1]);
# 				}
# 				else {
# 					dp[i][j] = min(dp[i][j], dp[i - 1][j - 1] + 1);
# 				}
# 			}
# 		}
#
# 		return dp[word1.size()][word2.size()];
# 	}
# };


# V2