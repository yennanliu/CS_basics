"""

809. Expressive Words
Solved
Medium
Topics
premium lock icon
Companies
Sometimes people repeat letters to represent extra feeling. For example:

"hello" -> "heeellooo"
"hi" -> "hiiii"
In these strings like "heeellooo", we have groups of adjacent letters that are all the same: "h", "eee", "ll", "ooo".

You are given a string s and an array of query strings words. A query word is stretchy if it can be made to be equal to s by any number of applications of the following extension operation: choose a group consisting of characters c, and add some number of characters c to the group so that the size of the group is three or more.

For example, starting with "hello", we could do an extension on the group "o" to get "hellooo", but we cannot get "helloo" since the group "oo" has a size less than three. Also, we could do another extension like "ll" -> "lllll" to get "helllllooo". If s = "helllllooo", then the query word "hello" would be stretchy because of these two extension operations: query = "hello" -> "hellooo" -> "helllllooo" = s.
Return the number of query strings that are stretchy.

 

Example 1:

Input: s = "heeellooo", words = ["hello", "hi", "helo"]
Output: 1
Explanation: 
We can extend "e" and "o" in the word "hello" to get "heeellooo".
We can't extend "helo" to get "heeellooo" because the group "ll" is not size 3 or more.
Example 2:

Input: s = "zzzzzyyyyy", words = ["zzyy","zy","zyy"]
Output: 3
 

Constraints:

1 <= s.length, words.length <= 100
1 <= words[i].length <= 100
s and words[i] consist of lowercase letters.
 


"""



# V0
class Solution(object):
    def expressiveWords(self, s, words):
        """
        :type s: str
        :type words: List[str]
        :rtype: int
        """
        pass


# V1
# IDEA: Run Length Encoding
# https://leetcode.com/problems/expressive-words/editorial/
class Solution(object):
    def expressiveWords(self, S, words):
        def RLE(S):
            return zip(*[(k, len(list(grp)))
                         for k, grp in itertools.groupby(S)])

        R, count = RLE(S)
        ans = 0
        for word in words:
            R2, count2 = RLE(word)
            if R2 != R: continue
            ans += all(c1 >= max(c2, 3) or c1 == c2
                       for c1, c2 in zip(count, count2))

        return ans


# V2
# http://bookshadow.com/weblog/2018/04/02/leetcode-expressive-words/
# time = O(n + s), n = sum of all word lengths, s = len(S)
# space = O(n + s)
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
        
# V3
# time = O(n + s), n is the sum of all word lengths, s is the length of S
# space = O(l + s), l is the max word length
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
