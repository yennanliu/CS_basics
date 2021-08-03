"""

Given many words, words[i] has weight i.

Design a class WordFilter that supports one function, WordFilter.f(String prefix, String suffix). It will return the word with given prefix and suffix with maximum weight. If no word exists, return -1.

Examples:

Input:
WordFilter(["apple"])
WordFilter.f("a", "e") // returns 0
WordFilter.f("b", "") // returns -1
Note:

words has length in range [1, 15000].
For each test case, up to words.length queries WordFilter.f may be made.
words[i] has length in range [1, 10].
prefix, suffix have lengths in range [0, 10].
words[i] and prefix, suffix queries consist of lowercase letters only.

"""

# V0

# V1
# https://zhuanlan.zhihu.com/p/85356549
# https://www.youtube.com/watch?v=a-4WbFqalIA
# leetcode submit region begin(Prohibit modification and deletion)
class WordFilter(object):
    class Trie(object):

        def __init__(self):
            """
            Initialize your data structure here.
            """
            self.trie = {}

        def insert(self, word, i):
            """
            Inserts a word into the trie.
            :type word: str
            :rtype: dict
            """
            cur = self.trie
            for c in word:
                if c not in cur:
                    cur[c] = {}
                cur = cur[c]
                cur['i'] = i
            return cur

        def startsWith(self, prefix):
            """
            Returns if there is any word in the trie that starts with the given prefix.
            :type prefix: str
            :rtype: int
            """
            cur = self.trie
            for c in prefix:
                if c not in cur:
                    return -1
                cur = cur[c]
            return cur['i']

    def __init__(self, words):
        """
        :type words: List[str]
        """
        self.trie = WordFilter.Trie()
        for weight, word in enumerate(words):
            t = self.trie
            t.insert(',' + word, weight)
            for i in range(len(word)):
                w = word[-i:]
                t.insert(w + ',' + word, weight)
            
    def f(self, prefix, suffix):
        """
        :type prefix: str
        :type suffix: str
        :rtype: int
        """
        return self.trie.startsWith(suffix + ',' + prefix)

# Your WordFilter object will be instantiated and called as such:
# obj = WordFilter(words)
# param_1 = obj.f(prefix,suffix)
# leetcode submit region end(Prohibit modification and deletion)

# V1'
# http://bookshadow.com/weblog/2017/12/11/leetcode-prefix-and-suffix-search/
class WordFilter(object):

    def __init__(self, words):
        """
        :type words: List[str]
        """
        self.map = {}
        for idx, word in enumerate(words):
            for x in range(len(word) + 1):
                prefix = word[:x]
                for y in range(len(word) + 1):
                    suffix = word[y:]
                    self.map[prefix + '#' + suffix] = idx

    def f(self, prefix, suffix):
        """
        :type prefix: str
        :type suffix: str
        :rtype: int
        """
        return self.map.get(prefix + '#' + suffix, -1)

# V2 
# Time:  ctor:   O(w * l^2), w is the number of words, l is the word length on average
#        search: O(p + s)  , p is the length of the prefix, s is the length of the suffix,
# Space: O(t), t is the number of trie nodes
import collections
class WordFilter(object):

    def __init__(self, words):
        """
        :type words: List[str]
        """
        _trie = lambda: collections.defaultdict(_trie)
        self.__trie = _trie()

        for weight, word in enumerate(words):
            word += '#'
            for i in xrange(len(word)):
                cur = self.__trie
                cur["_weight"] = weight
                for j in xrange(i, 2*len(word)-1):
                    cur = cur[word[j%len(word)]]
                    cur["_weight"] = weight

    def f(self, prefix, suffix):
        """
        :type prefix: str
        :type suffix: str
        :rtype: int
        """
        cur = self.__trie
        for letter in suffix + '#' + prefix:
            if letter not in cur:
                return -1
            cur = cur[letter]
        return cur["_weight"]


# Time:  ctor:   O(w * l), w is the number of words, l is the word length on average
#        search: O(p + s + max(m, n)), p is the length of the prefix, s is the length of the suffix,
#                                      m is the number of the prefix match, n is the number of the suffix match
# Space: O(w * l)
class Trie(object):

    def __init__(self):
        _trie = lambda: collections.defaultdict(_trie)
        self.__trie = _trie()

    def insert(self, word, i):
        def add_word(cur, i):
            if "_words" not in cur:
                cur["_words"] = []
            cur["_words"].append(i)

        cur = self.__trie
        add_word(cur, i)
        for c in word:
            cur = cur[c]
            add_word(cur, i)

    def find(self, word):
        cur = self.__trie
        for c in word:
            if c not in cur:
                return []
            cur = cur[c]
        return cur["_words"]


class WordFilter2(object):

    def __init__(self, words):
        """
        :type words: List[str]
        """
        self.__prefix_trie = Trie()
        self.__suffix_trie = Trie()
        for i in reversed(xrange(len(words))):
            self.__prefix_trie.insert(words[i], i)
            self.__suffix_trie.insert(words[i][::-1], i)

    def f(self, prefix, suffix):
        """
        :type prefix: str
        :type suffix: str
        :rtype: int
        """
        prefix_match = self.__prefix_trie.find(prefix)
        suffix_match = self.__suffix_trie.find(suffix[::-1])
        i, j = 0, 0
        while i != len(prefix_match) and j != len(suffix_match):
            if prefix_match[i] == suffix_match[j]:
                return prefix_match[i]
            elif prefix_match[i] > suffix_match[j]:
                i += 1
            else:
                j += 1
        return -1