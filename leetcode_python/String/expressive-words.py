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
# IDEA: Length Encoding (gpt)
class Solution(object):
    def expressiveWords(self, s, words):
        """
        :type s: str
        :type words: List[str]
        :rtype: int
        """

        s_encode = self.encode(s)

        count = 0

        for word in words:
            w_encode = self.encode(word)

            if self.is_same(s_encode, w_encode):
                count += 1

        return count


    def encode(self, x):
        """
        Convert string into groups.

        Example:
        "heeellooo"

        =>
        [('h', 1), ('e', 3), ('l', 2), ('o', 3)]
        """

        if not x:
            return []

        res = []

        # NOTE !!! use `prev` for simpler handling
        prev = x[0]
        # NOTE !!! use `count` for simpler handling
        count = 1

        for i in range(1, len(x)):

            if x[i] == prev:
                count += 1

            else:
                res.append((prev, count))

                prev = x[i]
                count = 1


        # NOTE !!! Don't forget the last group
        # Don't forget the last group
        res.append((prev, count))

        return res


    def is_same(self, x, y):
        """
        Check whether y can be stretched to become x.
        """

        # Different number of groups
        if len(x) != len(y):
            return False

        for i in range(len(x)):

            x_char, x_cnt = x[i]
            y_char, y_cnt = y[i]

            # Characters must match
            if x_char != y_char:
                return False

            # word has more characters than s
            if y_cnt > x_cnt:
                return False

            # s has more characters than word.
            # We can only stretch if s's group has >= 3 chars.
            if x_cnt > y_cnt and x_cnt < 3:
                return False

        return True


# V0-1
# IDEA: 2 POINTERS (gemini)
class Solution(object):
    def expressiveWords(self, s, words):
        return sum(self.is_stretchy(s, w) for w in words)

    def is_stretchy(self, s, w):
        i, j = 0, 0
        n, m = len(s), len(w)

        while i < n and j < m:
            if s[i] != w[j]:
                return False

            # Measure group length in s
            len_s = 0
            char = s[i]
            while i < n and s[i] == char:
                i += 1
                len_s += 1

            # Measure group length in w
            len_w = 0
            while j < m and w[j] == char:
                j += 1
                len_w += 1

            # Check stretch rules
            if len_s < len_w or (len_s > len_w and len_s < 3):
                return False

        # Both pointers must reach the end
        return i == n and j == m


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
