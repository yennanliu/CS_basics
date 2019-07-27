# V0 

# V1 
# http://bookshadow.com/weblog/2018/04/02/leetcode-expressive-words/
class Solution(object):
    def expressiveWords(self, S, words):
        """
        :type S: str
        :type words: List[str]
        :rtype: int
        """
        cs = self.countLetters(S)
        ans = 0
        for word in words:
            ws = self.countLetters(word)
            ans += self.checkWords(cs, ws)
        return ans
    def countLetters(self, S):
        cnt, lc = 0, ''
        ans = []
        for c in S:
            if c == lc:
                cnt += 1
            else:
                if cnt: ans.append((lc, cnt))
                cnt, lc = 1, c
        if cnt: ans.append((lc, cnt))
        return ans
    def checkWords(self, cs, ws):
        if len(cs) != len(ws): return 0
        for c, w in zip(cs, ws):
            if c[0] != w[0]: return 0
            if c[1] < w[1]: return 0
            if c[1] != w[1] and c[1] < 3: return 0
        return 1
        
# V2 
# Time:  O(n + s), n is the sum of all word lengths, s is the length of S
# Space: O(l + s), l is the max word length
import itertools
class Solution(object):
    def expressiveWords(self, S, words):
        """
        :type S: str
        :type words: List[str]
        :rtype: int
        """
        # Run length encoding
        def RLE(S):
            return itertools.izip(*[(k, len(list(grp)))
                                  for k, grp in itertools.groupby(S)])

        R, count = RLE(S)
        result = 0
        for word in words:
            R2, count2 = RLE(word)
            if R2 != R:
                continue
            result += all(c1 >= max(c2, 3) or c1 == c2
                          for c1, c2 in itertools.izip(count, count2))
        return result
