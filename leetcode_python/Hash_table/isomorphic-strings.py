# Time:  O(n)
# Space: O(1)

# Given two strings s and t, determine if they are isomorphic.
#
# Two strings are isomorphic if the characters in s can be replaced to get t.
#
# All occurrences of a character must be replaced with another character
# while preserving the order of characters. No two characters may map to
# the same character but a character may map to itself.
#
# For example,
# Given "egg", "add", return true.
#
# Given "foo", "bar", return false.
#
# Given "paper", "title", return true.
#
# Note:
# You may assume both s and t have the same length.

# V0 

# V1 
# https://blog.csdn.net/liuxiao214/article/details/77587070
# IDEA : HASH TABLE 
class Solution(object):
    def isIsomorphic(self, s, t):
        """
        :type s: str
        :type t: str
        :rtype: bool
        """
        def sub(s,t):
            m={}
            for i in range(len(s)):
                if s[i] not in m.keys():
                    m[s[i]]=t[i]
                else:
                    if m[s[i]]!=t[i]:
                        return False
            return True
        return sub(s, t) and sub(t,s)

# V1' 
# https://blog.csdn.net/liuxiao214/article/details/77587070
# IDEA : MAPPING + ORD 
class Solution1(object):
    def isIsomorphic(self, s, t):
        """
        :type s: str
        :type t: str
        :rtype: bool
        """
        a1=[-1]*256
        a2=[-1]*256
        for i in range(len(s)):
            if a1[ord(s[i])] != a2[ord(t[i])]:
                return False
            a1[ord(s[i])]=i
            a2[ord(t[i])]=i
        return True

# V1''
# https://www.jiuzhang.com/solution/isomorphic-strings/#tag-highlight-lang-python
class Solution:
    """
    @param s: a string
    @param t: a string
    @return: true if the characters in s can be replaced to get t or false
    """
    def isIsomorphic(self, s, t):
        # write your code here
        map1 = {}
        map2 = {}
        for i in range(len(s)):
            if s[i] not in map1:
                map1[s[i]] = t[i]
            elif map1[s[i]] != t[i]:
                return False
        for i in range(len(t)):
            if t[i] not in map2:
                map2[t[i]] = s[i]
            elif map2[t[i]] != s[i]:
                return False
        return True
        
# V2 
# Time:  O(n)
# Space: O(1)
from itertools import izip  # Generator version of zip.
class Solution(object):
    def isIsomorphic(self, s, t):
        """
        :type s: str
        :type t: str
        :rtype: bool
        """
        if len(s) != len(t):
            return False

        s2t, t2s = {}, {}
        for p, w in izip(s, t):
            if w not in s2t and p not in t2s:
                s2t[w] = p
                t2s[p] = w
            elif w not in s2t or s2t[w] != p:
                # Contradict mapping.
                return False
        return True

# Time:  O(n)
# Space: O(1)
class Solution2(object):
    def isIsomorphic(self, s, t):
        if len(s) != len(t):
            return False

        return self.halfIsom(s, t) and self.halfIsom(t, s)

    def halfIsom(self, s, t):
        lookup = {}
        for i in range(len(s)):
            if s[i] not in lookup:
                lookup[s[i]] = t[i]
            elif lookup[s[i]] != t[i]:
                return False
        return True
