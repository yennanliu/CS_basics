"""

Given a set of keywords words and a string S, make all appearances of all keywords in S bold. Any letters between <b> and </b> tags become bold.

The returned string should use the least number of tags possible, and of course the tags should form a valid combination.

For example, given that words = ["ab", "bc"] and S = "aabcd", we should return "a<b>abc</b>d". Note that returning "a<b>a<b>b</b>c</b>d" would use more tags, so it is incorrect.

Note:

words has length in range [0, 50].
words[i] has length in range [1, 10].
S has length in range [0, 500].
All characters in words[i] and S are lowercase letters.


"""

# Time:  O(n * l), n is the length of S, l is the average length of words
# Space: O(t)    , t is the size of trie

# V0 

# V1 
# DEV 

# V2 
# Time:  O(n * l), n is the length of S, l is the average length of words
# Space: O(t)    , t is the size of trie
import collections
import functools
class Solution(object):
    def boldWords(self, words, S):
        """
        :type words: List[str]
        :type S: str
        :rtype: str
        """
        _trie = lambda: collections.defaultdict(_trie)
        trie = _trie()
        for i, word in enumerate(words):
            functools.reduce(dict.__getitem__, word, trie).setdefault("_end")

        lookup = [False] * len(S)
        for i in range(len(S)):
            curr = trie
            k = -1
            for j in range(i, len(S)):
                if S[j] not in curr:
                    break
                curr = curr[S[j]]
                if "_end" in curr:
                    k = j
            for j in range(i, k+1):
                lookup[j] = True

        result = []
        for i in range(len(S)):
            if lookup[i] and (i == 0 or not lookup[i-1]):
                result.append("<b>")
            result.append(S[i])
            if lookup[i] and (i == len(S)-1 or not lookup[i+1]):
                result.append("</b>")
        return "".join(result)

# Time:  O(n * d * l), l is the average length of words
# Space: O(n)
class Solution2(object):
    def boldWords(self, words, S):
        """
        :type words: List[str]
        :type S: str
        :rtype: str
        """
        lookup = [0] * len(S)
        for d in words:
            pos = S.find(d)
            while pos != -1:
                lookup[pos:pos+len(d)] = [1] * len(d)
                pos = S.find(d, pos+1)

        result = []
        for i in range(len(S)):
            if lookup[i] and (i == 0 or not lookup[i-1]):
                result.append("<b>")
            result.append(S[i])
            if lookup[i] and (i == len(S)-1 or not lookup[i+1]):
                result.append("</b>")
        return "".join(result)
        