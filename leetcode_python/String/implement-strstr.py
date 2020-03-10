# Time:  O(n + k)
# Space: O(k)
#
# Implement strStr().
#
# Returns a pointer to the first occurrence of needle in haystack,
#  or null if needle is not part of haystack.
#
# V0 
class Solution(object):
    def strStr(self, haystack, needle):
        if needle in haystack:
            return haystack.index(needle)
        else:
            return -1 

# V0'
class Solution(object):
    def strStr(self, haystack, needle):
        length = len(needle)
        for i in range(len(haystack) - len(needle) + 1):
            if haystack[i:i+len(needle)] == needle:
                return i
        return -1

# V1
# https://blog.csdn.net/coder_orz/article/details/51708389
class Solution(object):
    def strStr(self, haystack, needle):
        """
        :type haystack: str
        :type needle: str
        :rtype: int
        """
        return haystack.find(needle)

# V1'
# https://blog.csdn.net/coder_orz/article/details/51708389
class Solution(object):
    def strStr(self, haystack, needle):
        """
        :type haystack: str
        :type needle: str
        :rtype: int
        """
        if not needle:
            return 0
        for i in xrange(len(haystack) - len(needle) + 1):
            if haystack[i] == needle[0]:
                j = 1
                while j < len(needle) and haystack[i+j] == needle[j]:
                    j += 1
                if j == len(needle):
                    return i
        return -1

# V1''
# https://blog.csdn.net/coder_orz/article/details/51708389
class Solution(object):
    def strStr(self, haystack, needle):
        """
        :type haystack: str
        :type needle: str
        :rtype: int
        """
        for i in xrange(len(haystack) - len(needle) + 1):
            if haystack[i:i+len(needle)] == needle:
                return i
        return -1

# V1'''
# https://blog.csdn.net/coder_orz/article/details/51708389
class Solution(object):
    def strStr(self, haystack, needle):
        """
        :type haystack: str
        :type needle: str
        :rtype: int
        """
        if not needle:
            return 0
        #generate next array, need O(n) time
        i, j, m, n = -1, 0, len(haystack), len(needle)
        next = [-1] * n
        while j < n - 1:  
            #needle[k] stands for prefix, neelde[j] stands for postfix
            if i == -1 or needle[i] == needle[j]:   
                i, j = i + 1, j + 1
                next[j] = i
            else:
                i = next[i]
        #check through the haystack using next, need O(m) time
        i = j = 0
        while i < m and j < n:
            if j == -1 or haystack[i] == needle[j]:
                i, j = i + 1, j + 1
            else:
                j = next[j]
        if j == n:
            return i - j
        return -1

# V1'''''
# https://blog.csdn.net/fuxuemingzhu/article/details/79254558
class Solution(object):
    def strStr(self, haystack, needle):
        """
        :type haystack: str
        :type needle: str
        :rtype: int
        """
        return haystack.find(needle)

# V1''''''
class Solution:
    def strStr(self, haystack, needle):
        """
        :type haystack: str
        :type needle: str
        :rtype: int
        """
        M, N = len(haystack), len(needle)
        for i in range(M - N + 1):
            if haystack[i : i + N] == needle:
                return i
        return -1

# V1'''''''
# Wiki of KMP algorithm:
# http://en.wikipedia.org/wiki/Knuth-Morris-Pratt_algorithm
class Solution(object):
    def strStr(self, haystack, needle):
        """
        :type haystack: str
        :type needle: str
        :rtype: int
        """
        if not needle:
            return 0

        return self.KMP(haystack, needle)

    def KMP(self, text, pattern):
        prefix = self.getPrefix(pattern)
        j = -1
        for i in range(len(text)):
            while j > -1 and pattern[j + 1] != text[i]:
                j = prefix[j]
            if pattern[j + 1] == text[i]:
                j += 1
            if j == len(pattern) - 1:
                return i - j
        return -1

    def getPrefix(self, pattern):
        prefix = [-1] * len(pattern)
        j = -1
        for i in range(1, len(pattern)):
            while j > -1 and pattern[j + 1] != pattern[i]:
                j = prefix[j]
            if pattern[j + 1] == pattern[i]:
                j += 1
            prefix[i] = j
        return prefix

# V2
# Time:  O(n * k)
# Space: O(k)
class Solution2(object):
    def strStr(self, haystack, needle):
        """
        :type haystack: str
        :type needle: str
        :rtype: int
        """
        for i in range(len(haystack) - len(needle) + 1):
            if haystack[i : i + len(needle)] == needle:
                return i
        return -1
