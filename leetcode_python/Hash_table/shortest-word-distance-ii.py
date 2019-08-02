# V0 

# V1 
# http://www.voidcn.com/article/p-fhbblvrn-qp.html
class WordDistance(object):
    def __init__(self, words):
        """
        initialize your data structure here.
        :type words: List[str]
        """
        self.dic = {}
        for index, value in enumerate(words): 
            if value not in self.dic:
                self.dic[value] = [index]
            else:
                self.dic[value] += index,

    def shortest(self, word1, word2):
        """
        Adds a word into the data structure.
        :type word1: str
        :type word2: str
        :rtype: int
        """
        return min(abs(x-y) for x in self.dic[word1] for y in self.dic[word2])

# V1'
# http://www.voidcn.com/article/p-fhbblvrn-qp.html
class WordDistance(object):
    def __init__(self, words):
        """
        initialize your data structure here.
        :type words: List[str]
        """
        self.dic = {}
        self.l = len(words)
        for index, value in enumerate(words):
            self.dic[value] = self.dic.get(value, []) + [index] # dictionary.get()，可以传入一个参数。如果传入两个参数，第二个参数就是default值，如果字典中找不到的话，就返回default值。

    def shortest(self, word1, word2):
        """
        Adds a word into the data structure.
        :type word1: str
        :type word2: str
        :rtype: int
        """
        l1, l2 = self.dic[word1], self.dic[word2]
        i = j = 0
        res = self.l
        # O(m+n) time complexity
        while i < len(l1) and j < len(l2):
            res = min(res, abs(l1[i]-l2[j])) # loop all the l1 and l2 to find the shortest
            if l1[i] < l2[j]:
                i += 1
            else:
                j += 1
        return res

# V2 
# Time:  init: O(n), lookup: O(a + b), a, b is occurences of word1, word2
# Space: O(n)
import collections
class WordDistance(object):
    # initialize your data structure here.
    # @param {string[]} words
    def __init__(self, words):
        self.wordIndex = collections.defaultdict(list)
        for i in range(len(words)):
            self.wordIndex[words[i]].append(i)

    # @param {string} word1
    # @param {string} word2
    # @return {integer}
    # Adds a word into the data structure.
    def shortest(self, word1, word2):
        indexes1 = self.wordIndex[word1]
        indexes2 = self.wordIndex[word2]

        i, j, dist = 0, 0, float("inf")
        while i < len(indexes1) and j < len(indexes2):
            dist = min(dist, abs(indexes1[i] - indexes2[j]))
            if indexes1[i] < indexes2[j]:
                i += 1
            else:
                j += 1

        return dist