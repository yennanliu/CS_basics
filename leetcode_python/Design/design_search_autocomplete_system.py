# 642 design_search_autocomplete_system.py
# Design a search autocomplete system for a search engine. Users may input a sentence (at least one word and end with a special character ‘#’). For each character they type except ‘#’, you need to return the top 3 historical hot sentences that have prefix the same as the part of sentence already typed. Here are the specific rules:
#
# The hot degree for a sentence is defined as the number of times a user typed the exactly same sentence before.
# The returned top 3 hot sentences should be sorted by hot degree (The first is the hottest one). If several sentences have the same degree of hot, you need to use ASCII-code order (smaller one appears first).
# If less than 3 hot sentences exist, then just return as many as you can.
# When the input is a special character, it means the sentence ends, and in this case, you need to return an empty list.
# Your job is to implement the following functions:
#
# The constructor function:
#
# AutocompleteSystem(String[] sentences, int[] times): This is the constructor. The input is historical data. Sentences is a string array consists of previously typed sentences. Times is the corresponding times a sentence has been typed. Your system should record these historical data.
#
# Now, the user wants to input a new sentence. The following function will provide the next character the user types:
#
# List input(char c): The input c is the next character typed by the user. The character will only be lower-case letters (‘a’ to ‘z’), blank space (’ ‘) or a special character (’#’). Also, the previously typed sentence should be recorded in your system. The output will be the top 3 historical hot sentences that have prefix the same as the part of sentence already typed.
#
# Example:
# Operation: AutocompleteSystem([“i love you”, “island”,“ironman”, “i love leetcode”], [5,3,2,2])
# The system have already tracked down the following sentences and their corresponding times:
# “i love you” : 5 times
# “island” : 3 times
# “ironman” : 2 times
# “i love leetcode” : 2 times
# Now, the user begins another search:
#
# Operation: input(‘i’)
# Output: [“i love you”, “island”,“i love leetcode”]
# Explanation:
# There are four sentences that have prefix “i”. Among them, “ironman” and “i love leetcode” have same hot degree. Since ’ ’ has ASCII code 32 and ‘r’ has ASCII code 114, “i love leetcode” should be in front of “ironman”. Also we only need to output top 3 hot sentences, so “ironman” will be ignored.
#
# Operation: input(’ ')
# Output: [“i love you”,“i love leetcode”]
# Explanation:
# There are only two sentences that have prefix "i ".
#
# Operation: input(‘a’)
# Output: []
# Explanation:
# There are no sentences that have prefix “i a”.
#
# Operation: input(’#’)
# Output: []
# Explanation:
# The user finished the input, the sentence “i a” should be saved as a historical sentence in system. And the following input will be counted as a new search.
#
# Note:
# The input sentence will always start with a letter and end with ‘#’, and only one blank space will exist between two words.
# The number of complete sentences that to be searched won’t exceed 100. The length of each sentence including those in the historical data won’t exceed 100.
# Please use double-quote instead of single-quote when you write test cases even for a character input.
# Please remember to RESET your class variables declared in class AutocompleteSystem, as static/class variables are persisted across multiple test cases. Please see here for more details.

# V0

# V1 
# IDEA : DICT TRIE
# http://bookshadow.com/weblog/2017/07/16/leetcode-design-search-autocomplete-system/
class TrieNode:
    def __init__(self):
        self.children = dict()
        self.sentences = set()

class AutocompleteSystem(object):

    def __init__(self, sentences, times):
        """
        :type sentences: List[str]
        :type times: List[int]
        """
        self.buffer = ''
        self.stimes = collections.defaultdict(int)
        self.trie = TrieNode()
        for s, t in zip(sentences, times):
            self.stimes[s] = t
            self.addSentence(s)
        self.tnode = self.trie

    def input(self, c):
        """
        :type c: str
        :rtype: List[str]
        """
        ans = []
        if c != '#':
            self.buffer += c
            if self.tnode: self.tnode = self.tnode.children.get(c)
            if self.tnode: ans = sorted(self.tnode.sentences, key=lambda x: (-self.stimes[x], x))[:3]
        else:
            self.stimes[self.buffer] += 1
            self.addSentence(self.buffer)
            self.buffer = ''
            self.tnode = self.trie
        return ans

    def addSentence(self, sentence):
        node = self.trie
        for letter in sentence:
            child = node.children.get(letter)
            if child is None:
                child = TrieNode()
                node.children[letter] = child
            node = child
            child.sentences.add(sentence)
# Your AutocompleteSystem object will be instantiated and called as such:
# obj = AutocompleteSystem(sentences, times)
# param_1 = obj.input(c)

### Test case : dev 

# V1'
# https://blog.csdn.net/Sengo_GWU/article/details/82948834
class TrieNode():
    def __init__(self):
        self.children = collections.defaultdict(TrieNode)
        self.isEnd = False
        self.rank = 0
        self.data = ''
        
class AutocompleteSystem(object):
 
    def __init__(self, sentences, times):
        self.root = TrieNode()
        for i, sentence in enumerate(sentences):
            self.addRecord(sentence, times[i])
        self.keyWords = ''
    
    def addRecord(self, sentence, time):
        node = self.root
        for c in sentence:
            node = node.children[c]
        node.isEnd = True
        node.rank -= time
        node.data = sentence
 
    def input(self, c):
        res = []
        if c != '#':
            self.keyWords += c
            res = self.search(self.keyWords)
        else:
            self.addRecord(self.keyWords, 1)
            self.keyWords = ''
        return res
    
    def search(self, sentence):
        node = self.root
        for c in sentence:
            node = node.children.get(c)
            if not node:
                return []
        res = self.dfs(node)
        return [data for rank, data in sorted(res)[:3]]
        
    def dfs(self, node): 
        res = []
        if node:
            if node.isEnd:
                res.append([node.rank, node.data])
            for child in node.children.values():
                res.extend(self.dfs(child))
         return res 

# V1''
# https://zhuanlan.zhihu.com/p/99499171
class AutocompleteSystem:    
    def insert(self, w: str, t=1):
        cur = self.root
        for c in w:
            if c not in cur:
                cur[c] = {}
            cur = cur[c]
            if 'hot' not in cur:
                cur['hot'] = {}
            if w not in cur['hot']:
                cur['hot'][w] = 0
            cur['hot'][w] += t  
            
    def __init__(self, sentences: List[str], times: List[int]):
        self.root = {}
        self.w = ''
        self.cur = self.root
        for i, s in enumerate(sentences):
            self.insert(s, times[i])
        
    def input(self, c: str) -> List[str]:
        if c == '#':
            self.insert(self.w)
            self.w = ''
            self.cur = self.root
            return []
        self.w += c
        if self.cur == None or c not in self.cur:
            self.cur = None
            return []
        self.cur = self.cur[c]
        return [k for k, v in sorted(self.cur['hot'].items(), key=lambda x: (-x[1], x[0]), reverse=False)][0:3]
            
# V2 
