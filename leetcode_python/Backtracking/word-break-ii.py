"""

140. Word Break II
Hard

Given a string s and a dictionary of strings wordDict, add spaces in s to construct a sentence where each word is a valid dictionary word. Return all such possible sentences in any order.

Note that the same word in the dictionary may be reused multiple times in the segmentation.

 

Example 1:

Input: s = "catsanddog", wordDict = ["cat","cats","and","sand","dog"]
Output: ["cats and dog","cat sand dog"]
Example 2:

Input: s = "pineapplepenapple", wordDict = ["apple","pen","applepen","pine","pineapple"]
Output: ["pine apple pen apple","pineapple pen apple","pine applepen apple"]
Explanation: Note that you are allowed to reuse a dictionary word.
Example 3:

Input: s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]
Output: []
 

Constraints:

1 <= s.length <= 20
1 <= wordDict.length <= 1000
1 <= wordDict[i].length <= 10
s and wordDict[i] consist of only lowercase English letters.
All the strings of wordDict are unique.

"""

# V0
# IDEA : BACKTRCK, LC 078 Subsets
class Solution(object):
    def wordBreak(self, s, wordDict):
        def help(cur):
            """
            NOTE this !!! : 
                -> shallow copy cur[:]
            """
            if "".join(cur[:]) == s:
                res.append(" ".join(cur[:]))
                return
            if len("".join(cur[:])) > len(s):
                return
            for i in range(len(wordDict)):
                cur.append(wordDict[i])
                help(cur)
                # NOTE this
                cur.pop()

        # edge case
        if not wordDict:
            return []
        res = []
        cur = []
        cnt = 0
        help(cur)
        print ("res = " + str(res))
        return res

# V1
# IDEA : RECURSION
# https://leetcode.com/problems/word-break-ii/discuss/1426014/Python-interview-friendly-simple-recursion
class Solution:
    def wordBreak(self, s: str, wordDict: List[str]) -> List[str]:
        def recur(s, path):
            if not s:
                out.append(' '.join(path))
                return
            for i in range(1,len(s)+1):
                w,new_s = s[:i], s[i:]
                if w in wordDict:
                    recur(new_s, path + [w])
        wordDict, out = set(wordDict), []
        recur(s,[])
        return out

# V1
# IDEA : BACKTRCK
# https://leetcode.com/problems/word-break-ii/discuss/44404/Python-backtracking
class Solution:
    # @param s, a string
    # @param dict, a set of string
    # @return a list of strings
    def wordBreak(self, s, dic):
        if not dic:
            return []
        n = max(len(d) for d in dic)
        stack, parents = [0], collections.defaultdict(set)
        while stack:
            parent = stack.pop()
            for child in range(parent+1, parent+n+1):
                if s[parent:child] in dic:
                    if child not in parents:
                        stack.append(child)
                    parents[child].add(parent)
        stack, res = [[len(s)]], []
        while stack:
            r = stack.pop()
            if r[0] == 0:
                r = [s[i:j] for i, j in zip(r[:-1], r[1:])]
                res.append(' '.join(r))
            for parent in parents[r[0]]:
                stack.append([parent]+r)
        return res

# V1
# IDEA : BACKTRACK
class Solution:
    def wordBreak(self, s: str, wordDict: List[str]) -> List[str]:     
        n = len(s)
        result = []
        words = []      
        def match(index):          
            if index == n:
                result.append(" ".join(words))
                return                     
            for word in wordDict:
                isPrefix = ((index+len(word)) <= n and s[index:(index+len(word))] == word)
                if isPrefix:
                    words.append(word)
                    match(index + len(word))
                    words.pop()      
        match(0)
        return result

# V1
# IDEA : DFS
# https://leetcode.com/problems/word-break-ii/discuss/222797/Python-solution
class Solution(object):
    def wordBreak(self, s, wordDict):
        """
        :type s: str
        :type wordDict: List[str]
        :rtype: List[str]
        """
        def dfs(i):
            if i == len(s):
                return [""]
            if i in dic:
                return dic[i]
            res = []
            for j in range(i, len(s)):
                head = s[i:j+1]
                if head in wordSet:
                    tmp = dfs(j+1)
                    for string in tmp:
                        string = head +" "+string
                        res.append(string.strip())
            dic[i] = res
            return res
        
        dic = {}
        wordSet = set(wordDict)
        return dfs(0)

# V1
# IDEA : Top-Down Dynamic Programming
# https://leetcode.com/problems/word-break-ii/solution/
class Solution:
    def wordBreak(self, s: str, wordDict: List[str]) -> List[str]:
        wordSet = set(wordDict)
        # table to map a string to its corresponding words break
        # {string: [['word1', 'word2'...], ['word3', 'word4', ...]]}
        memo = defaultdict(list)

        #@lru_cache(maxsize=None)    # alternative memoization solution
        def _wordBreak_topdown(s):
            """ return list of word lists """
            if not s:
                return [[]]  # list of empty list

            if s in memo:
                # returned the cached solution directly.
                return memo[s]

            for endIndex in range(1, len(s)+1):
                word = s[:endIndex]
                if word in wordSet:
                    # move forwards to break the postfix into words
                    for subsentence in _wordBreak_topdown(s[endIndex:]):
                        memo[s].append([word] + subsentence)
            return memo[s]

        # break the input string into lists of words list
        _wordBreak_topdown(s)

        # chain up the lists of words into sentences.
        return [" ".join(words) for words in memo[s]]

# V1
# IDEA : Bottom-Up Dynamic Programming
# https://leetcode.com/problems/word-break-ii/solution/
class Solution:
    def wordBreak(self, s: str, wordDict: List[str]) -> List[str]:
        # quick check on the characters,
        #   otherwise it would exceed the time limit for certain test cases.
        if set(Counter(s).keys()) > set(Counter("".join(wordDict)).keys()):
            return []

        wordSet = set(wordDict)

        dp = [[]] * (len(s)+1)
        dp[0] = [""]

        for endIndex in range(1, len(s)+1):
            sublist = []
            # fill up the values in the dp array.
            for startIndex in range(0, endIndex):
                word = s[startIndex:endIndex]
                if word in wordSet:
                    for subsentence in dp[startIndex]:
                        sublist.append((subsentence + ' ' + word).strip())

            dp[endIndex] = sublist

        return dp[len(s)]

# V1
# https://leetcode.com/problems/word-break-ii/solution/
class Solution:
    def wordBreak(self, s: str, wordDict: List[str]) -> List[str]:
        # quick check on the characters,
        #   otherwise it would exceed the time limit for certain test cases.
        if set(Counter(s).keys()) > set(Counter("".join(wordDict)).keys()):
            return []

        wordSet = set(wordDict)

        # the dp array stores the positions to insert breaks/stops
        dp = [[]] * (len(s)+1)
        dp[0] = [[0]]

        for endIndex in range(1, len(s)+1):
            stops = []
            for startIndex in range(0, endIndex):
                word = s[startIndex:endIndex]
                if word in wordSet:
                    for subsentence in dp[startIndex]:
                        copy = subsentence.copy()
                        copy.append(endIndex)
                        stops.append(copy)
            dp[endIndex] = stops

        ret = []
        # reconstruct the words list from the positions of breaks/stops
        for stops in dp[len(s)]:
            words = []
            for i in range(len(stops)-1):
                words.append(s[stops[i]:stops[i+1]])
            ret.append(" ".join(words))
        return ret

# V1
# https://leetcode.com/problems/word-break-ii/solution/
class Solution:
    def wordBreak(self, s: str, wordDict: List[str]) -> List[str]:
        # quick check on the characters,
        #   otherwise it would exceed the time limit for certain test cases.
        if set(Counter(s).keys()) > set(Counter("".join(wordDict)).keys()):
            return []

        wordSet = set(wordDict)

        dp = [[]] * (len(s)+1)
        dp[0] = [[0]]

        for endIndex in range(1, len(s)+1):
            stops = []
            for startIndex in range(0, endIndex):
                word = s[startIndex:endIndex]
                if word in wordSet:
                    stops.append([startIndex, endIndex])
            dp[endIndex] = stops

        ret = []
        def wordDFS(sentence, dp_index):
            if dp_index == 0:
                ret.append(" ".join(sentence))
                return

            for start, end in dp[dp_index]:
                word = s[start:end]
                wordDFS([word] + sentence, start)

        wordDFS([], len(s))

        return ret

# V2