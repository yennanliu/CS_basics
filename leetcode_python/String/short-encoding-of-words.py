# V0 

# V1 
# http://bookshadow.com/weblog/2018/04/22/leetcode-short-encoding-of-words/
# IDEA : SORT 
class Solution(object):
    def minimumLengthEncoding(self, words):
        """
        :type words: List[str]
        :rtype: int
        """
        words = sorted(word[::-1] for word in set(words))
        ans = 0
        last = ''
        for word in words + ['']:
            if not word.startswith(last):
                ans += len(last) + 1
            last = word
        return ans

# V1'  
# http://bookshadow.com/weblog/2018/04/22/leetcode-short-encoding-of-words/
# IDEA :DICT + TREE 
class TrieNode:
    def __init__(self):
        self.children = dict()

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

    def levelOrderTraverse(self):
        ans = level = 0
        q = [self.root]
        while q:
            q0 = []
            level += 1
            for node in q:
                if not node.children:
                    ans += level
                else:
                    q0 += list(node.children.values())
            q = q0
        return ans

class Solution(object):
    def minimumLengthEncoding(self, words):
        """
        :type words: List[str]
        :rtype: int
        """
        words = set(words)
        trie = Trie()
        for word in words:
            trie.insert(word[::-1])
        return trie.levelOrderTraverse()
        
# V2 
# Time:  O(n), n is the total sum of the lengths of words
# Space: O(t), t is the number of nodes in trie
import collections
import functools
class Solution(object):
    def minimumLengthEncoding(self, words):
        """
        :type words: List[str]
        :rtype: int
        """
        words = list(set(words))
        _trie = lambda: collections.defaultdict(_trie)
        trie = _trie()

        nodes = [functools.reduce(dict.__getitem__, word[::-1], trie)
                 for word in words]

        return sum(len(word) + 1
                   for i, word in enumerate(words)
                   if len(nodes[i]) == 0)