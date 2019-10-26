# V0

# V1

# V1'
# https://www.jiuzhang.com/solution/minimum-ascii-delete-sum-for-two-strings/
# JAVA
# public class Solution {
#     /**
#      * @param s1: a string
#      * @param s2: a string
#      * @return: the lowest ASCII sum of deleted characters to make two strings equal
#      */
#     public int minimumDeleteSum(String s1, String s2) {
#         // Write your code here
#         int[][] dp = new int[s1.length()+1][s2.length()+1];
#         int sum = 0;
#         for(int i = 0; i < s1.length(); i++) sum += (int)s1.charAt(i);
#         for(int i = 0; i < s2.length(); i++) sum += (int)s2.charAt(i);
#         for(int i = 1; i <= s1.length(); i++) {
#             for(int j = 1; j <= s2.length(); j++) {
#                 if(s1.charAt(i-1) == s2.charAt(j-1)) 
#                     dp[i][j] = Math.max((int)s1.charAt(i-1) + dp[i-1][j-1], Math.max(dp[i-1][j], dp[i][j-1]));
#                 else
#                     dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
#             }
#         }
#         return sum - 2 * dp[s1.length()][s2.length()];
#     }
# }

# V2
# Time:  O(m * n)
# Space: O(n)
class Solution(object):
    def minimumDeleteSum(self, s1, s2):
        """
        :type s1: str
        :type s2: str
        :rtype: int
        """
        dp = [[0] * (len(s2)+1) for _ in range(2)]
        for j in range(len(s2)):
            dp[0][j+1] = dp[0][j] + ord(s2[j])

        for i in range(len(s1)):
            dp[(i+1)%2][0] = dp[i%2][0] + ord(s1[i])
            for j in range(len(s2)):
                if s1[i] == s2[j]:
                    dp[(i+1)%2][j+1] = dp[i%2][j]
                else:
                    dp[(i+1)%2][j+1] = min(dp[i%2][j+1] + ord(s1[i]), \
                                           dp[(i+1)%2][j] + ord(s2[j]))
        return dp[len(s1)%2][-1]

# Time:  O(m * n)
# Space: O(m * n)
class Solution2(object):
    def minimumDeleteSum(self, s1, s2):
        """
        :type s1: str
        :type s2: str
        :rtype: int
        """
        dp = [[0] * (len(s2)+1) for _ in range(len(s1)+1)]
        for i in range(len(s1)):
            dp[i+1][0] = dp[i][0] + ord(s1[i])
        for j in range(len(s2)):
            dp[0][j+1] = dp[0][j] + ord(s2[j])

        for i in range(len(s1)):
            for j in range(len(s2)):
                if s1[i] == s2[j]:
                    dp[i+1][j+1] = dp[i][j]
                else:
                    dp[i+1][j+1] = min(dp[i][j+1] + ord(s1[i]), \
                                       dp[i+1][j] + ord(s2[j]))
        return dp[-1][-1]