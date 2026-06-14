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
# IDEA: BFS
from collections import deque

class Solution(object):
    def wordBreak(self, s, wordDict):
        if s == "":
            return True

        words = set(wordDict)

        """
        NOTE !!!

        q: [end_idx_1, end_idx_2, ...]


        -> 
        the queue stores `positions` (idx)
        in the string that have 
        already been `successfully` reached.


        ->
        # The queue will track the `starting index`
         positions we need to explore from


        """
        q = deque([0])

        # NOTE !!!
        # use `visited` to avoid duplicated processing
        visited = set([0])

        while q:
            idx = q.popleft()

            """
            NOTE !!!


            -> `idx == len(s)` (but NOT `idx == len(s) - 1`)

            ->
             In this algorithm, 
             idx doesn't represent the character
             you are currently standing on. 
             Instead, it represents the 
             `frontier` line between 
             characters where the previous
              word finished and the next word must start.


            -> when we success the `last` word paste,
               the idx move to the next `new frontier` place,
               which is `max(idx) + 1`, e.g. len(s)

            """
            if idx == len(s):
                return True

            for w in words:
                nxt = idx + len(w)

                if nxt <= len(s) and s[idx:nxt] == w:
                    if nxt not in visited:
                        visited.add(nxt)
                        q.append(nxt)

        return False



# V0-1
# IDEA: BFS
from collections import deque

class Solution(object):
    def wordBreak(self, s, wordDict):
        """
        :type s: str
        :type wordDict: List[str]
        :rtype: bool
        """
        # Convert wordDict to a set for O(1) instant lookups
        word_set = set(wordDict)
        
        # The queue will track the starting index positions we need to explore from
        q = deque([0])
        
        # CRITICAL FIX: Track processed indices to avoid redundant duplicate paths (prevents TLE)
        visited = set()
        
        while q:
            idx = q.popleft()
            
            # CRITICAL FIX: If we successfully reached the exact end of the string, 
            # it means the entire string has been perfectly segmented.
            if idx == len(s):
                return True
                
            # If we've already explored branches starting from this index, skip it
            if idx in visited:
                continue
            visited.add(idx)
            
            # Scan ahead from our current position to find matching words
            for w in word_set:
                # CRITICAL FIX: Slice string correctly from idx up to idx + len(w)
                if s[idx : idx + len(w)] == w:
                    # If it matches, push the new target start index onto the queue
                    q.append(idx + len(w))
                    
        return False



# V0
# IDEA: BFS
from collections import deque
class Solution(object):
    def wordBreak(self, s, wordDict):
        # Convert list to set for O(1) lookup
        wordSet = set(wordDict)

        # NOTE !!!
        # q: [idx_1, idx_2, ...]
        # Queue stores indices in the string s
        q = deque([0])

        # NOTE !!!
        # use `visited` to AVOID repeat processing
        # visited[i] means we've already processed index i
        visited = set([0])

        while q:
            start = q.popleft()

            # If we reached the end of the string, success
            if start == len(s):
                return True

            # NOTE !!!
            # -> ONLY loop from `start + 1` to `len(s) + 1`
            # Try extending from 'start' to every possible end
            for end in range(start + 1, len(s) + 1):

                # If substring is a valid word AND not visited
                if end not in visited and s[start:end] in wordSet:

                    # Mark as visited
                    visited.add(end)

                    # Add new state to queue
                    q.append(end)

        # If BFS ends without reaching len(s)
        return False

# V0-1
# IDEA: BFS
from collections import deque

class Solution(object):
    def wordBreak(self, s, wordDict):
        """
        :type s: str
        :type wordDict: List[str]
        :rtype: bool
        """
        if not s:
            return False
            
        # Convert dictionary to a set for instant O(1) loop evaluations
        word_set = set(wordDict)
        
        # Initialize the BFS queue. We start at prefix index 0 (the empty string prefix).
        q = deque([0])
        
        # CRITICAL FIX: Track visited index boundaries to avoid checking 
        # the exact same split point multiple times.
        visited = set([0])
        
        n = len(s)
        
        while q:
            # Inside, 'start_idx' represents the beginning of our next search segment
            start_idx = q.popleft()
            
            # SUCCESS CONDITION: If we have successfully traversed all indices up to n,
            # the string can be perfectly segmented.
            if start_idx == n:
                return True
                
            # Iterate through all possible ending indices for the current word chunk
            for end_idx in range(start_idx + 1, n + 1):
                
                # If we've already validated split combinations starting at 'end_idx', skip it
                if end_idx in visited:
                    continue
                    
                # CRITICAL FIX: Extract the slice starting directly from start_idx
                # without any arbitrary +1 index shifts.
                word_chunk = s[start_idx:end_idx]
                
                # If this substring chunk matches a word in our dictionary...
                if word_chunk in word_set:
                    # ...we mark this end boundary as a valid launching point for the next step,
                    q.append(end_idx)
                    # and register it to our visited history to prevent duplicate queue loops.
                    visited.add(end_idx)
                    
        return False



# V0-2
# IDEA: 1D DP
"""

## 🧠 DP Definition

```text
dp[i] = `True` if and only if the substring s[0:i] 
can be `segmented into one or more words from wordDict`
```

* `i` represents the **length of the prefix**
* `dp[0] = True` means the empty string is valid

---

## 🔁 DP Equation (Transition)

```text
dp[i] = OR over all j < i of:
        (dp[j] AND s[j:i] in wordDict)
```

---


"""
class Solution(object):
    def wordBreak(self, s, wordDict):
        wordSet = set(wordDict)

        n = len(s)

        # dp[i] = True means s[0:i] can be segmented
        dp = [False] * (n + 1)

        # empty string is always valid
        dp[0] = True

        for i in range(1, n + 1):
            for j in range(i):
                # if prefix dp[j] is valid AND s[j:i] is a word
                if dp[j] and s[j:i] in wordSet:
                    dp[i] = True
                    break

        return dp[n]


# V0-3
# IDEA: 1D DP
class Solution(object):
    def wordBreak(self, s, wordDict):
        """
        :type s: str
        :type wordDict: List[str]
        :rtype: bool
        """
        # Step 1: Convert the word list into a set. 
        # This optimizes our lookup time from O(W) down to O(1)!
        word_set = set(wordDict)
        
        n = len(s)
        
        # Step 2: Initialize our DP array of size n + 1 filled with False.
        # dp[i] represents whether a prefix of length 'i' can be successfully segmented.
        dp = [False] * (n + 1)
        
        # BASE CASE: An empty string (length 0) can always be considered "segmented".
        dp[0] = True
        
        # Step 3: Iterate through every prefix length from 1 up to the total length n.
        # Inside this loop, 'i' represents the CURRENT END boundary of our prefix.
        for i in range(1, n + 1):
            
            # Look backwards at all possible split points 'j' behind 'i'.
            for j in range(i):
                
                # YOUR LOGIC TRANSITION: 
                # If the string up to length 'j' is valid, AND the remaining chunk 
                # from index 'j' to 'i' is a word inside our dictionary...
                if dp[j] and s[j:i] in word_set:
                    
                    # ...then the prefix up to length 'i' is officially valid!
                    dp[i] = True
                    
                    # Optimization: Since we found a valid split configuration for 'i',
                    # we don't need to check other 'j' positions for this loop turn.
                    break
                    
        # Return the final evaluation for the full length of the string
        return dp[n]


# V0
# IDEA : BFS
class Solution:
    def wordBreak(self, s, wordDict):
        if not s or not wordDict:
            return
        q = collections.deque()
        q.append(0)
        visited = [False]*len(s)
        while q:
            i = q.popleft()
            if not visited[i]:
                for j in range(i+1,len(s)+1):                 
                    if s[i:j] in wordDict:                    
                        if j == len(s):
                            return True  
                        q.append(j)
                visited[i]=True
        return False

# V0'
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