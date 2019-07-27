# Time:  O(n), n is the total sum of the lengths of words
# Space: O(t), t is the number of nodes in trie

# Given a list of strings words representing an English Dictionary,
# find the longest word in words that can be built one character at a time by other words in words.
# If there is more than one possible answer, return the longest word with the smallest lexicographical order.
#
# If there is no answer, return the empty string.
# Example 1:
# Input:
# words = ["w","wo","wor","worl", "world"]
# Output: "world"
# Explanation:
# The word "world" can be built one character at a time by "w", "wo", "wor", and "worl".
#
# Example 2:
# Input:
# words = ["a", "banana", "app", "appl", "ap", "apply", "apple"]
# Output: "apple"
# Explanation:
# Both "apply" and "apple" can be built from other words in the dictionary.
# However, "apple" is lexicographically smaller than "apply".
#
# Note:
# - All the strings in the input will only contain lowercase letters.
# - The length of words will be in the range [1, 1000].
# - The length of words[i] will be in the range [1, 30].

# V0 


# V1  
# https://blog.csdn.net/fuxuemingzhu/article/details/79123277
# IDEA : GREEDY 
class Solution(object):
    def longestWord(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        wset = set(words)
        res = ""
        for w in words:
            isIn = True
            for i in range(1, len(w)):
                if w[:i] not in wset:
                    isIn = False
                    break
            if isIn:
                if not res or len(w) > len(res):
                    res = w
                elif len(w) == len(res) and res > w:
                    res = w
        return res

# V1'  
# https://blog.csdn.net/fuxuemingzhu/article/details/79123277       
# IDEA : SORT 
class Solution(object):
    def longestWord(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        words.sort()
        res = set([''])
        longestWord = ''
        for word in words:
            if word[:-1] in res:
                res.add(word)
                if len(word) > len(longestWord):
                    longestWord = word
        return longestWord
        
# V2 
import collections
from functools import reduce
class Solution(object):
    def longestWord(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        _trie = lambda: collections.defaultdict(_trie)
        trie = _trie()
        for i, word in enumerate(words):
            reduce(dict.__getitem__, word, trie)["_end"] = i

        # DFS
        stack = list(trie.values())
        result = ""
        while stack:
            curr = stack.pop()
            if "_end" in curr:
                word = words[curr["_end"]]
                if len(word) > len(result) or (len(word) == len(result) and word < result):
                    result = word
                stack += [curr[letter] for letter in curr if letter != "_end"]
        return result
