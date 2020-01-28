# V0 
# IDEA : DP
class Solution(object):
    def wordBreak(self, s, wordDict):
        """
        :type s: str
        :type wordDict: List[str]
        :rtype: bool
        """
        dp = [False] * (len(s) + 1)
        dp[0] = True
        for i in range(1, len(s) + 1):
            for k in range(i):
                if dp[k] and s[k:i] in wordDict:
                    dp[i] = True
        return dp.pop()

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79368360
class Solution(object):
    def wordBreak(self, s, wordDict):
        """
        :type s: str
        :type wordDict: List[str]
        :rtype: bool
        """
        print(s)
        print(wordDict)
        dp = [False] * (len(s) + 1)
        dp[0] = True
        print(dp)
        for i in range(1, len(s) + 1):
            for k in range(i):
                # if dp[k] : if dp[k] == True
                if dp[k] and s[k:i] in wordDict:
                    dp[i] = True
                    print(dp)
        return dp.pop()

# V1'
# https://www.jiuzhang.com/solution/word-break/#tag-highlight-lang-python
class Solution:
    # @param s: A string s
    # @param dict: A dictionary of words dict
    def wordBreak(self, s, dict):
        if len(dict) == 0:
            return len(s) == 0
            
        n = len(s)
        f = [False] * (n + 1)
        f[0] = True     
        maxLength = max([len(w) for w in dict])
        for i in range(1, n + 1):
            for j in range(1, min(i, maxLength) + 1):
                if not f[i - j]:
                    continue
                if s[i - j:i] in dict:
                    f[i] = True
                    break        
        return f[n]

# V2 
# Time:  O(n * l^2)
# Space: O(n)
class Solution(object):
    def wordBreak(self, s, wordDict):
        """
        :type s: str
        :type wordDict: Set[str]
        :rtype: bool
        """
        n = len(s)

        max_len = 0
        for string in wordDict:
            max_len = max(max_len, len(string))

        can_break = [False for _ in range(n + 1)]
        can_break[0] = True
        for i in range(1, n + 1):
            for l in range(1, min(i, max_len) + 1):
                if can_break[i-l] and s[i-l:i] in wordDict:
                    can_break[i] = True
                    break

        return can_break[-1]
