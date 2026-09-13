"""

820. Short Encoding of Words
Medium

A valid encoding of an array of words is any reference string s and array of indices indices such that:

words.length == indices.length
The reference string s ends with the '#' character.
For each index indices[i], the substring of s starting from indices[i] and up to (but not including) the next '#' character is equal to words[i].

Given an array of words, return the length of the shortest reference string s possible of any valid encoding of words.

Example 1:

Input: words = ["time", "me", "bell"]
Output: 10
Explanation: A valid encoding would be s = "time#bell#" and indices = [0, 2, 5].
words[0] = "time", the substring of s starting from indices[0] = 0 to the next '#' is underlined in "time#bell#"
words[1] = "me", the substring of s starting from indices[1] = 2 to the next '#' is underlined in "time#bell#"
words[2] = "bell", the substring of s starting from indices[2] = 5 to the next '#' is underlined in "time#bell#"

Example 2:

Input: words = ["t"]
Output: 2
Explanation: A valid encoding would be s = "t#" and indices = [0].

Constraints:

1 <= words.length <= 2000
1 <= words[i].length <= 7
words[i] consists of only lowercase letters.

"""

# V0 

# V1
# http://bookshadow.com/weblog/2018/04/22/leetcode-short-encoding-of-words/
# IDEA : SORT
# time = O(n log n), n is the total sum of the lengths of words
# space = O(n)
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
# time = O(n), n is the total sum of the lengths of words
# space = O(t), t is the number of nodes in trie
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
# time = O(n), n is the total sum of the lengths of words
# space = O(t), t is the number of nodes in trie
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