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
# IDEA: 2D DP
class Solution(object):
    def minDistance(self, word1, word2):
        n1 = len(word1)
        n2 = len(word2)

        # dp[i][j] = min edits to convert word1[0:i] -> word2[0:j]
        dp = [[0] * (n2 + 1) for _ in range(n1 + 1)]

        # base case 1:
        # convert word1 prefix -> empty string = delete all chars
        for i in range(n1 + 1):
            dp[i][0] = i

        # base case 2:
        # convert empty string -> word2 prefix = insert all chars
        for j in range(n2 + 1):
            dp[0][j] = j

        # fill DP table
        for i in range(1, n1 + 1):
            for j in range(1, n2 + 1):

                # if last characters match, no operation needed
                if word1[i - 1] == word2[j - 1]:
                    dp[i][j] = dp[i - 1][j - 1]

                """
                NOTE !!!

                why dp[i][j - 1] for `insert` ?


                -> consider below:

                word1 = "ab"
                word2 = "abc"


                we want to convert `ab` to `abc`

                -> so what we need is the `ab` state,
                   -> which is dp[i][j-1]

                   -> e.g. we `shrink` 1 index on w2 right pointer


                """
                else:
                    dp[i][j] = min(
                        dp[i - 1][j],      # delete from word1
                        dp[i][j - 1],      # insert into word1
                        dp[i - 1][j - 1]   # replace
                    ) + 1

        return dp[n1][n2]



# V0-1
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
        
        # CRITICAL FIX: Allocate a 2D table of size (n1 + 1) x (n2 + 1)
        dp = [[0] * (n2 + 1) for _ in range(n1 + 1)]
        
        # INITIALIZE BASE CASES:
        # Filling row 0 (transforming empty string to word2 prefixes)
        for j in range(n2 + 1):
            dp[0][j] = j
            
        # Filling column 0 (transforming word1 prefixes to empty string)
        for i in range(n1 + 1):
            dp[i][0] = i
            
        # BUILD THE DP TABLE
        for i in range(1, n1 + 1):
            for j in range(1, n2 + 1):
                # CRITICAL FIX: Offset by -1 to read the actual string characters
                if word1[i-1] == word2[j-1]:
                    # Match found! No operation needed. Carry over the diagonal score.
                    dp[i][j] = dp[i-1][j-1]
                else:
                    # No match! Find the cheapest operation and add 1
                    delete_op  = dp[i-1][j]
                    insert_op  = dp[i][j-1]
                    replace_op = dp[i-1][j-1]
                    
                    dp[i][j] = 1 + min(delete_op, insert_op, replace_op)
                    
        # The bottom-right corner contains our absolute minimum operations answer
        return dp[n1][n2]




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