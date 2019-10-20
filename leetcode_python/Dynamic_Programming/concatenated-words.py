# V0

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

# V1''
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
