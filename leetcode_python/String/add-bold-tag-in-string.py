# V0 

# V1 
# http://bookshadow.com/weblog/2017/06/11/leetcode-add-bold-tag-in-string/
class Solution(object):
    def addBoldTag(self, s, dict):
        """
        :type s: str
        :type dict: List[str]
        :rtype: str
        """
        start = end = -1
        ans = ''
        for x, c in enumerate(s):
            nend = 0
            for d in dict:
                if s[x:].startswith(d):
                    nend = max(nend, len(d))
            if nend:
                if start == -1: start = x
                end = max(end, nend + x)
                continue
            if x >= end:
                ans += '<b>' + s[start:end] + '</b>'
                start = end = -1
            if start == -1: ans += c
        if start > -1: ans += '<b>' + s[start:end] + '</b>'
        return ans
        
# V2 
# Time:  O(n * d * l), l is the average string length
# Space: O(n)
import collections
import functools
# 59ms
class Solution(object):
    def addBoldTag(self, s, dict):
        """
        :type s: str
        :type dict: List[str]
        :rtype: str
        """
        lookup = [0] * len(s)
        for d in dict:
            pos = s.find(d)
            while pos != -1:
                lookup[pos:pos+len(d)] = [1] * len(d)
                pos = s.find(d, pos + 1)

        result = []
        for i in range(len(s)):
            if lookup[i] and (i == 0 or not lookup[i-1]):
                result.append("<b>")
            result.append(s[i])
            if lookup[i] and (i == len(s)-1 or not lookup[i+1]):
                result.append("</b>")
        return "".join(result)


# Time:  O(n * l), l is the average string length
# Space: O(t)    , t is the size of trie
# trie solution, 439ms
class Solution2(object):
    def addBoldTag(self, s, words):
        """
        :type s: str
        :type words: List[str]
        :rtype: str
        """
        _trie = lambda: collections.defaultdict(_trie)
        trie = _trie()
        for i, word in enumerate(words):
            functools.reduce(dict.__getitem__, word, trie).setdefault("_end")

        lookup = [False] * len(s)
        for i in range(len(s)):
            curr = trie
            k = -1
            for j in range(i, len(s)):
                if s[j] not in curr:
                    break
                curr = curr[s[j]]
                if "_end" in curr:
                    k = j
            for j in range(i, k+1):
                lookup[j] = True

        result = []
        for i in range(len(s)):
            if lookup[i] and (i == 0 or not lookup[i-1]):
                result.append("<b>")
            result.append(s[i])
            if lookup[i] and (i == len(s)-1 or not lookup[i+1]):
                result.append("</b>")
        return "".join(result)
