# https://leetcode.ca/2017-12-27-758-Bold-Words-in-String/

"""

758. Bold Words in String
Given a set of keywords words and a string S, make all appearances of all keywords in S bold. Any letters between <b> and </b> tags become bold.

The returned string should use the least number of tags possible, and of course the tags should form a valid combination.

For example, given that words = ["ab", "bc"] and S = "aabcd", we should return "a<b>abc</b>d". Note that returning "a<b>a<b>b</b>c</b>d" would use more tags, so it is incorrect.

Note:

words has length in range [0, 50].
words[i] has length in range [1, 10].
S has length in range [0, 500].
All characters in words[i] and S are lowercase letters.
Difficulty:
Easy
Lock:
Prime
Company:
Google
Problem Solution
758-Bold-Words-in-String
All Problems:
Link to All Problems
"""

# V0
# time = O(1)  # unimplemented stub
# space = O(1)
class Solution(object):
    def boldWords(self, words, S):
        pass


# V1
# IDEA: TRIE
# https://leetcode.ca/2017-12-27-758-Bold-Words-in-String/
class Trie:
    def __init__(self):
        self.children = [None] * 128
        self.is_end = False

    def insert(self, word):
        node = self
        for c in word:
            idx = ord(c)
            if node.children[idx] is None:
                node.children[idx] = Trie()
            node = node.children[idx]
        node.is_end = True


# time = O(n * l), n is the length of S, l is the average length of words
# space = O(t), t is the size of trie
class Solution:
    def boldWords(self, words: List[str], s: str) -> str:
        trie = Trie()
        for w in words:
            trie.insert(w)
        n = len(s)
        pairs = []
        for i in range(n):
            node = trie
            for j in range(i, n):
                idx = ord(s[j])
                if node.children[idx] is None:
                    break
                node = node.children[idx]
                if node.is_end:
                    pairs.append([i, j])
        if not pairs:
            return s
        st, ed = pairs[0]
        t = []
        for a, b in pairs[1:]:
            if ed + 1 < a:
                t.append([st, ed])
                st, ed = a, b
            else:
                ed = max(ed, b)
        t.append([st, ed])

        ans = []
        i = j = 0
        while i < n:
            if j == len(t):
                ans.append(s[i:])
                break
            st, ed = t[j]
            if i < st:
                ans.append(s[i:st])
            ans.append('<b>')
            ans.append(s[st : ed + 1])
            ans.append('</b>')
            j += 1
            i = ed + 1

        return ''.join(ans)


# V2
# time = O(n * l), n is the length of S, l is the average length of words
# space = O(t), t is the size of trie
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

# time = O(n * d * l)  # l is the average length of words
# space = O(n)
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
        