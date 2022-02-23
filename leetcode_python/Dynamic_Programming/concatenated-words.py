"""

472. Concatenated Words
Hard

Given an array of strings words (without duplicates), return all the concatenated words in the given list of words.

A concatenated word is defined as a string that is comprised entirely of at least two shorter words in the given array.

 

Example 1:

Input: words = ["cat","cats","catsdogcats","dog","dogcatsdog","hippopotamuses","rat","ratcatdogcat"]
Output: ["catsdogcats","dogcatsdog","ratcatdogcat"]
Explanation: "catsdogcats" can be concatenated by "cats", "dog" and "cats"; 
"dogcatsdog" can be concatenated by "dog", "cats" and "dog"; 
"ratcatdogcat" can be concatenated by "rat", "cat", "dog" and "cat".
Example 2:

Input: words = ["cat","dog","catdog"]
Output: ["catdog"]
 

Constraints:

1 <= words.length <= 104
0 <= words[i].length <= 30
words[i] consists of only lowercase English letters.
0 <= sum(words[i].length) <= 105

"""

# V0

# V0'
# IDEA : DFS (TLE)
# TODO : fix it
# class Solution(object):
#     def findAllConcatenatedWordsInADict(self, words):
#         # dfs
#         def dfs(cur, _cnt, res):
#             if len("".join(cur)) > _max:
#                 return
#             elif "".join(cur) in words and _cnt >= 2:
#                 res.append("".join(cur))
#                 _cnt = 0
#                 return
#             for w in words:
#                 cur.append(w)
#                 tmp = "".join(cur)
#                 if tmp not in res:
#                     dfs(cur, _cnt+1, res)
#                 cur.pop(-1)
#         # edge case
#         if not words or words == [""]:
#             return []
#         _max = max([ len(w) for w in words ])
#         res = []
#         cur = []
#         _cnt = 0
#         tmp = dfs(cur, _cnt, res)
#         return res

# V1
# http://bookshadow.com/weblog/2016/12/18/leetcode-concatenated-words/
# IDEA : DFS 
class Solution(object):
    def findAllConcatenatedWordsInADict(self, words):
        """
        :type words: List[str]
        :rtype: List[str]
        """
        ans = []
        self.wordSet = set(words)
        for word in words:
            self.wordSet.remove(word) # avoid the search process find itself (word) when search all word in words  
            if self.search(word):
                ans.append(word)
            self.wordSet.add(word)    # add the word back for next search with new "word"
        return ans

    def search(self, word):
        if word in self.wordSet:
            return True
        for idx in range(1, len(word)):
            if word[:idx] in self.wordSet and self.search(word[idx:]):
                return True
        return False

# V1'
# http://bookshadow.com/weblog/2016/12/18/leetcode-concatenated-words/
# IDEA : TRIE
class Solution(object):
    def findAllConcatenatedWordsInADict(self, words):
        """
        :type words: List[str]
        :rtype: List[str]
        """
        self.trie = Trie()
        ans = []
        for word in words:
            self.trie.insert(word)
        for word in words:
            if self.search(word):
                ans.append(word)
        return ans

    def search(self, word):
        node = self.trie.root
        for idx, letter in enumerate(word):
            node = node.children.get(letter)
            if node is None:
                return False
            suffix = word[idx+1:]
            if node.isWord and (self.trie.search(suffix) or self.search(suffix)):
                return True
        return False

class TrieNode:
    def __init__(self):
        self.children = dict()
        self.isWord = False

class Trie:
    def __init__(self):
        self.root = TrieNode()

    def insert(self, word):
        node = self.root
        for letter in word:
            child = node.children.get(letter)
            if child is None:
                child = TrieNode()
                node.children[letter] = child
            node = child
        node.isWord = True

    def search(self, word):
        node = self.root
        for letter in word:
            node = node.children.get(letter)
            if node is None:
                return False
        return node.isWord 

# V1'''
# IDEA : DFS
# https://leetcode.com/problems/concatenated-words/discuss/224015/Python-DFS
class Solution:
    def findAllConcatenatedWordsInADict(self, words):
        words_set = set(words)
        ans = []
        for w in words:
            
            if not w:
                continue
                
            stack = [0]
            seen = {0}
            wLen = len(w)
            
            while stack:
                i = stack.pop()
                if i == wLen or (i > 0 and w[i:] in words_set):
                    ans.append(w)
                    break
                for l in range(wLen - i + 1):
                    if w[i: i+l] in words_set and i+l not in seen and l != wLen:
                        stack.append(i + l)
                        seen.add(i + l)
        return ans

# V1'''
# IDEA : TRIE + DFS
# https://leetcode.com/problems/concatenated-words/discuss/322444/Python-solutions%3A-top-down-DP-Trie-%2B-DFS
class TrieNode():
    def __init__(self):
        self.children = {}
        self.isEnd = False

class Trie():
    def __init__(self, words):
        self.root = TrieNode()
        for w in words:
            if w:
                self.insert(w)
    
    def insert(self, word):
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.isEnd = True

class Solution:
    def findAllConcatenatedWordsInADict(self, words):
        def dfs(node, i, w, space_inserted):
            if i == len(w):
                return node.isEnd and space_inserted
            if node.isEnd:
                if dfs(trie.root, i, w, True):
                    return True
            if w[i] not in node.children:
                return False
            else:
                return dfs(node.children[w[i]], i + 1, w, space_inserted)
        
        trie = Trie(words)
        res = []
        for w in words:
            if dfs(trie.root, 0, w, False):
                res.append(w)
        return res

# V1''''
# IDEA : TRIE + DFS
# https://leetcode.com/problems/concatenated-words/discuss/118917/Python-Trie%2BDFS
class Solution(object):
    def findAllConcatenatedWordsInADict(self, words):
        """
        :type words: List[str]
        :rtype: List[str]
        """
        def check(root, word, s=0):
            if not word:
                return True
            node = root
            i = s
            while i<len(word):
                if '#' in node and check(root, word, i):
                    return True
                c = word[i]
                if c in node:
                    node = node[c]
                else:
                    return False
                i += 1
            return '#' in node
                
        root = {}
        words = sorted(words, cmp=lambda x, y:len(x)-len(y))
        
        ret = []
        for w in words:
            if not w:
                continue
            if check(root, w):
                ret.append(w)
            node = root
            for c in w:
                if c in node:
                    node = node[c]
                else:
                    node[c] = {}
                    node = node[c]
            node['#'] = 'hhh'
        
        return ret

# V1''''''
# https://www.jiuzhang.com/solution/concatenated-words/#tag-highlight-lang-python
class Solution:
    """
    @param words: List[str]
    @return: return List[str]
    """
    def wordBreak(self, word, cands):
        if not cands:
            return False
        dp = [False] * (len(word) + 1) #store whether w.substr(0, i) can be formed by existing words
        dp[0] = True #empty string is always valid
        for i in range(1, len(word) + 1):
            for j in reversed(range(0, i)):
                if not dp[j]:
                    continue
                if word[j:i] in cands:
                    dp[i] = True
                    break
        return dp[-1]
    
    def findAllConcatenatedWordsInADict(self, words):
        # write your code here
        words.sort(key=lambda x: -len(x))
        cands = set(words) # using hash for acceleration
        ans = []
        for i in range(0, len(words)):
            cands -= {words[i]}
            if self.wordBreak(words[i], cands):
                ans += words[i],
        return ans

# V1''''''''
class Solution:
    def findAllConcatenatedWordsInADict(self, words):
        words.sort(key=len)
        if not words or not words[-1]: return []
        elif not words[0]: words=words[1:] #remove ""
        wordDict, ans={}, []
        for word in words: 
            if self.valid(word, wordDict):
                ans.append(word)
            temp=wordDict
            for i in range(len(word)):
                temp=temp.setdefault(word[i], {}) #trie
            temp["#"]=word
            #print(wordDict)
        return ans
    def valid(self, word, wordDict):
        if not word: return True
        temp=wordDict
        for i in range(len(word)):
            if word[i] not in temp: return False
            temp=temp[word[i]]
            if "#" in temp and self.valid(word[i+1:], wordDict): #dfs
                return True

# V2 
# Time:  O(n * l^2)
# Space: O(n * l)
class Solution(object):
    def findAllConcatenatedWordsInADict(self, words):
        """
        :type words: List[str]
        :rtype: List[str]
        """
        lookup = set(words)
        result = []
        for word in words:
            dp = [False] * (len(word)+1)
            dp[0] = True
            for i in range(len(word)):
                if not dp[i]:
                    continue

                for j in range(i+1, len(word)+1):
                    if j - i < len(word) and word[i:j] in lookup:
                        dp[j] = True

                if dp[len(word)]:
                    result.append(word)
                    break

        return result