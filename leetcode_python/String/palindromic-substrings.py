"""

647. Palindromic Substrings
Medium

Given a string s, return the number of palindromic substrings in it.

A string is a palindrome when it reads the same backward as forward.

A substring is a contiguous sequence of characters within the string.

 

Example 1:

Input: s = "abc"
Output: 3
Explanation: Three palindromic strings: "a", "b", "c".
Example 2:

Input: s = "aaa"
Output: 6
Explanation: Six palindromic strings: "a", "a", "a", "aa", "aa", "aaa".
 

Constraints:

1 <= s.length <= 1000
s consists of lowercase English letters.

"""

# V0
# IDEA : BRUTE FORCE
class Solution(object):
    def countSubstrings(self, s):
        count = 0
        # NOTE: since i from 0 to len(s) - 1, so for j we need to "+1" then can get go throgh all elements in str
        for i in range(len(s)):
            # Note : for j we need to "+1"
            for j in range(i+1, len(s)+1):
                if s[i:j] == s[i:j][::-1]:
                    count += 1
        return count

# V0'
# IDEA : BRUTE FORCE
class Solution(object):
    def countSubstrings(self, s):
        count = 0
        for i in range(len(s)):
            for j in range(i, len(s)):
                if s[i:j + 1] == s[i:j + 1][::-1]:
                    count += 1
        return count

# V0'
# IDEA : TWO POINTERS
# https://leetcode.com/problems/palindromic-substrings/discuss/1041760/Python-Easy-Solution-Beats-85
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/String/longest-palindromic-substring.py
class Solution:
    def countSubstrings(self, s):
        ans = 0    
        for i in range(len(s)):
            # odd case
            # example : s = "abc"
            #  -> so have to set (l, r) at (2,2) (index)
            ans += self.helper(s, i, i)
            # even case
            # example : s = "abcd"
            #  -> so have to set (l, r) at (1,2) (index)
            ans += self.helper(s, i, i + 1)  
        return ans
        
    def helper(self, s, l, r):     
        ans = 0    
        while l >= 0 and r < len(s) and s[l] == s[r]:
            l -= 1
            r += 1
            ans += 1          
        return ans
    
# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79433960
# IDEA : GREEDY 
class Solution(object):
    def countSubstrings(self, s):
        """
        :type s: str
        :rtype: int
        """
        count = 0
        for i in range(len(s)):
            for j in range(i, len(s)):
                if s[i:j + 1] == s[i:j + 1][::-1]:
                    count += 1
        return count

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79433960
class Solution(object):
    def countSubstrings(self, s):
        """
        :type s: str
        :rtype: int
        """
        count = 0
        for i in range(len(s)):
            count += 1
            #length of palindromic substrings is odd 
            left = i - 1
            right = i + 1
            while left >= 0 and right < len(s) and s[left] == s[right]:
                count += 1
                left -= 1
                right += 1
            #length of palindromic substrings is even 
            left = i
            right = i + 1
            while left >= 0 and right < len(s) and s[left] == s[right]:
                count += 1
                left -= 1
                right += 1
        return count

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79433960
# IDEA : DP 
class Solution(object):
    def countSubstrings(self, s):
        """
        :type s: str
        :rtype: int
        """
        n = len(s)
        count = 0
        start, end, maxL = 0, 0, 0
        dp = [[0] * n for _ in range(n)]
        for i in range(n):
            for j in range(i):
                dp[j][i] = (s[j] == s[i]) & ((i - j < 2) | dp[j + 1][i - 1])
                if dp[j][i]:
                    count += 1
            dp[i][i] = 1
            count += 1
        return count

# V1'''
# IDEA : Check All Substrings
# https://leetcode.com/problems/palindromic-substrings/solution/
# JAVA 
# class Solution {
#     private boolean isPalindrome(String s, int start, int end) {
#         while (start < end) {
#             if (s.charAt(start) != s.charAt(end)) 
#                 return false;
#
#             ++start;
#             --end;
#         }
#
#         return true;
#     }
#
#     public int countSubstrings(String s) {
#         int ans = 0;
#
#         for (int start = 0; start < s.length(); ++start)
#             for (int end = start; end < s.length(); ++end) 
#                 ans += isPalindrome(s, start, end) ? 1 : 0;
#
#         return ans;
#     }
# }

# V1'''''
# IDEA : DP
# https://leetcode.com/problems/palindromic-substrings/solution/
# JAVA
# class Solution {
#     public int countSubstrings(String s) {
#         int n = s.length(), ans = 0;
#
#         if (n <= 0) 
#             return 0;
#
#         boolean[][] dp = new boolean[n][n];
#
#         // Base case: single letter substrings
#         for (int i = 0; i < n; ++i, ++ans) 
#             dp[i][i] = true;
#
#         // Base case: double letter substrings
#         for (int i = 0; i < n - 1; ++i) {
#             dp[i][i + 1] = (s.charAt(i) == s.charAt(i + 1));
#             ans += (dp[i][i + 1] ? 1 : 0);
#         }
#
#         // All other cases: substrings of length 3 to n
#         for (int len = 3; len <= n; ++len)
#             for (int i = 0, j = i + len - 1; j < n; ++i, ++j) {
#                 dp[i][j] = dp[i + 1][j - 1] && (s.charAt(i) == s.charAt(j));
#                 ans += (dp[i][j] ? 1 : 0);
#             }
#
#         return ans;
#     }
# }

# V1''''''
# IDEA : Expand Around Possible Centers
# https://leetcode.com/problems/palindromic-substrings/solution/
# JAVA 
# class Solution {
#     public int countSubstrings(String s) {
#         int ans = 0;
#
#         for (int i = 0; i < s.length(); ++i) {
#             // odd-length palindromes, single character center
#             ans += countPalindromesAroundCenter(s, i, i);
#
#             // even-length palindromes, consecutive characters center
#             ans += countPalindromesAroundCenter(s, i, i + 1);
#         }
#
#         return ans;
#     }
#
#     private int countPalindromesAroundCenter(String ss, int lo, int hi) {
#         int ans = 0;
#
#         while (lo >= 0 && hi < ss.length()) {
#             if (ss.charAt(lo) != ss.charAt(hi))
#                 break;      // the first and last characters don't match!
#
#             // expand around the center
#             lo--;
#             hi++;
#
#             ans++;
#         }
#
#         return ans;
#     }
# }

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def countSubstrings(self, s):
        """
        :type s: str
        :rtype: int
        """
        def manacher(s):
            s = '^#' + '#'.join(s) + '#$'
            P = [0] * len(s)
            C, R = 0, 0
            for i in range(1, len(s) - 1):
                i_mirror = 2*C-i
                if R > i:
                    P[i] = min(R-i, P[i_mirror])
                while s[i+1+P[i]] == s[i-1-P[i]]:
                    P[i] += 1
                if i+P[i] > R:
                    C, R = i, i+P[i]
            return P
        return sum((max_len+1)//2 for max_len in manacher(s))