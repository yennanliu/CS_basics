"""

139. Word Break
Medium

Given a string s and a dictionary of strings wordDict, return true if s can be segmented into a space-separated sequence of one or more dictionary words.

Note that the same word in the dictionary may be reused multiple times in the segmentation.

 

Example 1:

Input: s = "leetcode", wordDict = ["leet","code"]
Output: true
Explanation: Return true because "leetcode" can be segmented as "leet code".
Example 2:

Input: s = "applepenapple", wordDict = ["apple","pen"]
Output: true
Explanation: Return true because "applepenapple" can be segmented as "apple pen apple".
Note that you are allowed to reuse a dictionary word.
Example 3:

Input: s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]
Output: false
 

Constraints:

1 <= s.length <= 300
1 <= wordDict.length <= 1000
1 <= wordDict[i].length <= 20
s and wordDict[i] consist of only lowercase English letters.
All the strings of wordDict are unique.

"""

# V0
# IDEA : BFS
class Solution:
    def wordBreak(self, s, wordDict):
        if not s or not wordDict:
            return
        q = collections.deque()
        q.append(0)
        visited = [None]*len(s)
        while q:
            i = q.popleft()
            if not visited[i]:
                for j in range(i+1,len(s)+1):                 
                    if s[i:j] in wordDict:                    
                        if j == len(s):
                            return True  
                        q.append(j)
                visited[i]=True

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
# IDEA : BFS
# https://leetcode.com/problems/word-break/discuss/535831/Python-BFS
class Solution:
    def wordBreak(self, s, wordDict):
        if not s or not wordDict: return
        q = collections.deque()
        q.append(0)
        visited = [None]*len(s)
        while q:
            i = q.popleft()
            if not visited[i]:
                for j in range(i+1,len(s)+1):                 
                    if s[i:j] in wordDict:                    
                        if j == len(s):
                            return True  
                        q.append(j)
                visited[i]=True

# V1
# IDEA : Brute Force (TLE)
# https://leetcode.com/problems/word-break/solution/
class Solution:
    def wordBreak(self, s, wordDict):
        def wordBreakRecur(s: str, word_dict: Set[str], start: int):
            if start == len(s):
                return True
            for end in range(start + 1, len(s) + 1):
                if s[start:end] in word_dict and wordBreakRecur(s, word_dict, end):
                    return True
            return False

        return wordBreakRecur(s, set(wordDict), 0)

# V1
# IDEA : RECURSION WITH MEMORY
# https://leetcode.com/problems/word-break/solution/
class Solution:
    def wordBreak(self, s: str, wordDict: List[str]) -> bool:
        @lru_cache
        def wordBreakMemo(s: str, word_dict: FrozenSet[str], start: int):
            if start == len(s):
                return True
            for end in range(start + 1, len(s) + 1):
                if s[start:end] in word_dict and wordBreakMemo(s, word_dict, end):
                    return True
            return False

        return wordBreakMemo(s, frozenset(wordDict), 0)

# V1
# IDEA : BFS
# https://leetcode.com/problems/word-break/solution/
class Solution:
    def wordBreak(self, s: str, wordDict: List[str]) -> bool:
        word_set = set(wordDict)
        q = deque()
        visited = set()

        q.append(0)
        while q:
            start = q.popleft()
            if start in visited:
                continue
            for end in range(start + 1, len(s) + 1):
                if s[start:end] in word_set:
                    q.append(end)
                    if end == len(s):
                        return True
            visited.add(start)
        return False

# V1
# IDEA : DP
# https://leetcode.com/problems/word-break/solution/
class Solution:
    def wordBreak(self, s: str, wordDict: List[str]) -> bool:
        word_set = set(wordDict)
        dp = [False] * (len(s) + 1)
        dp[0] = True

        for i in range(1, len(s) + 1):
            for j in range(i):
                if dp[j] and s[j:i] in word_set:
                    dp[i] = True
                    break
        return dp[len(s)]

# V1
# IDEA : Iterative
# https://leetcode.com/problems/word-break/discuss/1659559/Python-or-Iterative
class Solution:
    def wordBreak(self, s: str, wordDict: List[str]) -> bool:
        parts = []
        wordDict = set(wordDict)
        for i in range(len(s)+1):
            if s[:i] in wordDict:
                parts.append(i)
            else:
                k = len(parts)-1
                while k >=0:
                    if s[parts[k]:i] in wordDict:
                        parts.append(i)
                        break
                    else:
                        k-=1
        if not parts:
            return False
        if parts[-1]== len(s):
            return True
        return False

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

# V1
# IDEA : Dynamic Programming bottom up
# https://leetcode.com/problems/word-break/discuss/164472/Python-solution
class Solution(object):
    def wordBreak(self, s, wordDict):
        """
        :type s: str
        :type wordDict: List[str]
        :rtype: bool
        """
        wordSet = set(wordDict)
        dp = [0]*(len(s)+1)
        dp[0] = 1
        for i in range(1, len(s)+1):
            for j in range(i):
                if dp[j] == 1 and s[j:i] in wordSet:
                    dp[i] = 1
                    break
            else:
                dp[i] = 0
        return dp[-1] == 1

# V1
# IDEA : Dynamic Programming top down (memoization)
# https://leetcode.com/problems/word-break/discuss/164472/Python-solution
class Solution(object):
    def wordBreak(self, s, wordDict):
        """
        :type s: str
        :type wordDict: List[str]
        :rtype: bool
        """
        def dfs(i):
            if i == len(s):
                return True
            if rec[i] != -1:
                return True if rec[i] == 1 else False
            for j in range(i, len(s)):
                if s[i:j+1] in wordSet:
                    rec[j+1] = 1 if dfs(j+1) else 0
                    if rec[j+1] == 1:
                        return True
            return False
        
        rec = [-1]*(len(s)+1)
        wordSet = set(wordDict)
        return dfs(0)

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