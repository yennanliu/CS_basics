# Time:  O(n)
# Space: O(c), c is unique count of pattern

# Given a pattern and a string str, find if str follows the same pattern.
#
# Examples:
#   1. pattern = "abba", str = "dog cat cat dog" should return true.
#   2. pattern = "abba", str = "dog cat cat fish" should return false.
#   3. pattern = "aaaa", str = "dog cat cat dog" should return false.
#   4. pattern = "abba", str = "dog dog dog dog" should return false.
#
# Notes:
#   1. Both pattern and str contains only lowercase alphabetical letters.
#   2. Both pattern and str do not have leading or trailing spaces.
#   3. Each word in str is separated by a single space.
#   4. Each letter in pattern must map to a word with length that is at least 1.

# V0 
class Solution(object):
    def wordPattern(self, pattern, str):
        """
        :type pattern: str
        :type str: str
        :rtype: bool
        """
        words = str.split()
        if len(pattern) != len(words):
            return False
        ptnDict, wordDict = {}, {}
        for ptn, word in zip(pattern, words):
            if ptn not in ptnDict:
                ptnDict[ptn] = word
            if word not in wordDict:
                wordDict[word] = ptn
            if wordDict[word] != ptn or ptnDict[ptn] != word:
                return False
        return True

# V1 
# http://bookshadow.com/weblog/2015/10/05/leetcode-word-pattern/
# IDEA : HASH TABLE 
# DEMO
# In [12]: pattern = "abba"
# In [13]: str = "dog cat cat dog"
# In [14]: Solution().wordPattern(pattern, str)
# {} {}
# {'a': 'dog'} {'dog': 'a'}
# {'a': 'dog', 'b': 'cat'} {'dog': 'a', 'cat': 'b'}
# {'a': 'dog', 'b': 'cat'} {'dog': 'a', 'cat': 'b'}
# Out[14]: True
class Solution(object):
    def wordPattern(self, pattern, str):
        """
        :type pattern: str
        :type str: str
        :rtype: bool
        """
        words = str.split()
        if len(pattern) != len(words):
            return False
        ptnDict, wordDict = {}, {}
        for ptn, word in zip(pattern, words):  # use "for loop on zip(pattern, words)" trick reduce original  2 loops to 1 loop
            if ptn not in ptnDict:
                ptnDict[ptn] = word
            if word not in wordDict:
                wordDict[word] = ptn
            if wordDict[word] != ptn or ptnDict[ptn] != word:
                return False
        return True

# V1'
# https://www.jiuzhang.com/solution/word-pattern/#tag-highlight-lang-python
class Solution:
    """
    @param pattern: a string,denote pattern string
    @param str: a string,denote matching string
    @return: return an boolean,denote whether the pattern string and the matching string match or not
    """
    def wordPattern(self, pattern, str):
        # write your code here
        mp1, mp2 = {}, {}
        tmp = ""
        str += ' '
        cnt, now = 0, 0
        for i in range(len(str)):
            if(str[i] == ' '):
                if((not pattern[cnt] in mp1) and (not tmp in mp2)):
                    mp1[pattern[cnt]] = now
                    mp2[tmp] = now
                    now += 1
                elif ((pattern[cnt] in mp1) and (tmp in mp2)):
                    if(mp1[pattern[cnt]] != mp2[tmp]):
                        return False
                else:
                    return False
                tmp = ""
                cnt += 1
            else:
                tmp += str[i]
        return True

# V2 
# Time:  O(n)
# Space: O(c), c is unique count of pattern
from itertools import izip  # Generator version of zip.
class Solution(object):
    def wordPattern(self, pattern, str):
        """
        :type pattern: str
        :type str: str
        :rtype: bool
        """
        if len(pattern) != self.wordCount(str):
            return False

        w2p, p2w = {}, {}
        for p, w in izip(pattern, self.wordGenerator(str)):
            if w not in w2p and p not in p2w:
                # Build mapping. Space: O(c)
                w2p[w] = p
                p2w[p] = w
            elif w not in w2p or w2p[w] != p:
                # Contradict mapping.
                return False
        return True

    def wordCount(self, str):
        cnt = 1 if str else 0
        for c in str:
            if c == ' ':
                cnt += 1
        return cnt

    # Generate a word at a time without saving all the words.
    def wordGenerator(self, str):
        w = ""
        for c in str:
            if c == ' ':
                yield w
                w = ""
            else:
                w += c
        yield w

# Time:  O(n)
# Space: O(n)
class Solution2(object):
    def wordPattern(self, pattern, str):
        """
        :type pattern: str
        :type str: str
        :rtype: bool
        """
        words = str.split()  # Space: O(n)
        if len(pattern) != len(words):
            return False

        w2p, p2w = {}, {}
        for p, w in izip(pattern, words):
            if w not in w2p and p not in p2w:
                # Build mapping. Space: O(c)
                w2p[w] = p
                p2w[p] = w
            elif w not in w2p or w2p[w] != p:
                # Contradict mapping.
                return False
        return True
