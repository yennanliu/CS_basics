# https://leetcode.com/problems/missing-number/description/
# Time:  O(n) ~ O(n^2)
# Space: O(n)
# Given a string array words, find the maximum value of
# length(word[i]) * length(word[j]) where the two words
# do not share common letters. You may assume that each
# word will contain only lower case letters. If no such
# two words exist, return 0.
#
# Example 1:
# Given ["abcw", "baz", "foo", "bar", "xtfn", "abcdef"]
# Return 16
# The two words can be "abcw", "xtfn".
#
# Example 2:
# Given ["a", "ab", "abc", "d", "cd", "bcd", "abcd"]
# Return 4
# The two words can be "ab", "cd".
#
# Example 3:
# Given ["a", "aa", "aaa", "aaaa"]
# Return 0
# No such pair of words.
#
# Follow up:
# Could you do better than O(n2), where n is the number of words?

# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80932597
# IDEA : SET 
class Solution:
    def maxProduct(self, words):
        """
        :type words: List[str]
        :rtype: int
        """
        word_dict = dict()
        for word in words:
            word_dict[word] = set(word)
        max_len = 0
        for i1, w1 in enumerate(words):
            for i2 in range(i1+1, len(words)):
                w2 = words[i2]
                if not (word_dict[w1] & word_dict[w2]):
                    max_len = max(max_len, len(w1) * len(w2))
        return max_len

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/80932597
# IDEA : BIT MANIPULATION 
class Solution(object):
    def maxProduct(self, words):
        """
        :type words: List[str]
        :rtype: int
        """
        res = 0
        d = collections.defaultdict(int)
        N = len(words)
        for i in range(N):
            w = words[i]
            for c in w:
                d[w] |= 1 << (ord(c) - ord('a'))
            for j in range(i):
                if not d[words[j]] & d[words[i]]:
                    res = max(res, len(words[j]) * len(words[i]))
        return res

# V2 
# Time:  O(n) ~ O(n^2)
# Space: O(n)
class Solution(object):
    def maxProduct(self, words):
        """
        :type words: List[str]
        :rtype: int
        """
        def counting_sort(words):
            k = 1000  # k is max length of words in the dictionary
            buckets = [[] for _ in range(k)]
            for word in words:
                buckets[len(word)].append(word)
            res = []
            for i in reversed(range(k)):
                if buckets[i]:
                    res += buckets[i]
            return res

        words = counting_sort(words)
        bits = [0] * len(words)
        for i, word in enumerate(words):
            for c in word:
                bits[i] |= (1 << (ord(c) - ord('a')))

        max_product = 0
        for i in range(len(words) - 1):
            if len(words[i]) ** 2 <= max_product:
                break
            for j in range(i + 1, len(words)):
                if len(words[i]) * len(words[j]) <= max_product:
                    break
                if not (bits[i] & bits[j]):
                    max_product = len(words[i]) * len(words[j])
        return max_product

# Time:  O(nlogn) ~ O(n^2)
# Space: O(n)
# Sorting + Pruning + Bit Manipulation
class Solution2(object):
    def maxProduct(self, words):
        """
        :type words: List[str]
        :rtype: int
        """
        words.sort(key=lambda x: len(x), reverse=True)
        bits = [0] * len(words)
        for i, word in enumerate(words):
            for c in word:
                bits[i] |= (1 << (ord(c) - ord('a')))

        max_product = 0
        for i in range(len(words) - 1):
            if len(words[i]) ** 2 <= max_product:
                break
            for j in range(i + 1, len(words)):
                if len(words[i]) * len(words[j]) <= max_product:
                    break
                if not (bits[i] & bits[j]):
                    max_product = len(words[i]) * len(words[j])
        return max_product
