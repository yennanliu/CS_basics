"""

567. Permutation in String
Medium

Share
Given two strings s1 and s2, return true if s2 contains a permutation of s1, or false otherwise.

In other words, return true if one of s1's permutations is the substring of s2.

 
Example 1:

Input: s1 = "ab", s2 = "eidbaooo"
Output: true
Explanation: s2 contains one permutation of s1 ("ba").
Example 2:

Input: s1 = "ab", s2 = "eidboaoo"
Output: false
 

Constraints:

1 <= s1.length, s2.length <= 104
s1 and s2 consist of lowercase English letters.

"""

# V0
# IDEA : collections + sliding window
from collections import Counter
class Solution(object):
    def checkInclusion(self, s1, s2):
        if len(s1) > len(s2):
            return False   
        l = 0
        tmp = ""
        _s1 = Counter(s1)
        _s2 = Counter()     
        for i, item in enumerate(s2):
            ### NOTE : we need to append new element first, then compare
            _s2[item] += 1
            tmp = s2[l:i+1]
            if _s2 == _s1 and len(tmp) > 0:
                return True
            if len(tmp) >= len(s1):
                _s2[tmp[0]] -= 1
                if _s2[tmp[0]] == 0:
                    del _s2[tmp[0]]
                l += 1
        return False

# V0'
# IDEA : collections + sliding window
class Solution(object):
    def checkInclusion(self, s1, s2):
        if len(s1) > len(s2):
            return False
        _c1 = Counter(s1)
        _c2 = Counter([])
        l = r = 0
        while r < len(s2):
            while r - l + 1 <= len(s1):
                ### NOTE : we need to append new element, then compare
                _c2[s2[r]] += 1
                #print ("l = " + str(l) + " r = " + str(r) + " s2[l:r+1] = " + str(s2[l:r+1]) + " _c2 = " + str(_c2))
                if _c1 == _c2:
                    return True
                r += 1
            _c2[s2[l]] -= 1
            if _c2[s2[l]] == 0:
                _c2.pop(s2[l])
            l += 1
        return False

# V0
# IDEA : collections + sliding window
import collections
class Solution(object):
    def checkInclusion(self, s1, s2):
        l1, l2 = len(s1), len(s2)
        c1 = collections.Counter(s1)
        c2 = collections.Counter()
        p = q = 0
        while q < l2:
            c2[s2[q]] += 1
            if c1 == c2:
                return True
            q += 1
            if q - p + 1 > l1:
                c2[s2[p]] -= 1
                if c2[s2[p]] == 0:
                    del c2[s2[p]]
                p += 1
        return False

# V0' NEED TO FIX 
# import collections
# class Solution(object):
#     def checkInclusion(self, s1, s2):
#         """
#         :type s1: str
#         :type s2: str
#         :rtype: bool
#         """
#         l1, l2 = len(s1), len(s2)
#         c1 = collections.Counter(s1)
#         c2 = collections.Counter()
#         l, r = 0, l+len(s1)
#         for i in range(len(s2) - len(s1)):
#             c2[s2[i]] += 1 
#             if c1 == c2:
#                 return True
#             if r - l + 1 > l1:
#                 c2[s2[l]] -= 1
#                 if c2[s2[l]] == 0:
#                     del c2[s2[l]]
#             l += 1
#             r += 1 
#         return False

# V1
# http://bookshadow.com/weblog/2017/04/30/leetcode-permutation-in-string/
import collections
class Solution(object):
    def checkInclusion(self, s1, s2):
        """
        :type s1: str
        :type s2: str
        :rtype: bool
        """
        l1, l2 = len(s1), len(s2)
        c1 = collections.Counter(s1)
        c2 = collections.Counter()
        p = q = 0
        while q < l2:
            c2[s2[q]] += 1
            if c1 == c2:
                return True
            q += 1
            if q - p + 1 > l1:
                c2[s2[p]] -= 1
                if c2[s2[p]] == 0:
                    del c2[s2[p]]
                p += 1
        return False

# V1'
# http://bookshadow.com/weblog/2017/04/30/leetcode-permutation-in-string/
import collections
class Solution(object):
    def checkInclusion(self, s1, s2):
        """
        :type s1: str
        :type s2: str
        :rtype: bool
        """
        l1, l2 = len(s1), len(s2)
        c1 = collections.Counter(s1)
        c2 = collections.Counter()
        cnt = 0
        p = q = 0
        while q < l2:
            c2[s2[q]] += 1
            if c1[s2[q]] == c2[s2[q]]:
                cnt += 1
            if cnt == len(c1):
                return True
            q += 1
            if q - p + 1 > l1:
                if c1[s2[p]] == c2[s2[p]]:
                    cnt -= 1
                c2[s2[p]] -= 1
                if c2[s2[p]] == 0:
                    del c2[s2[p]]
                p += 1
        return False

# V1'
# https://www.jiuzhang.com/solution/permutation-in-string/#tag-highlight-lang-python
import collections
class Solution:
    def checkInclusion(self, s1, s2):
        """
        :type s1: str
        :type s2: str
        :rtype: bool
        """
        l1 = len(s1)
        need = collections.Counter(s1)
        missing = l1
        for i,c in enumerate(s2):
            if c in need: 
                if need[c] > 0: missing -= 1    
                need[c] -= 1                    
            if i>=l1 and s2[i-l1] in need:      
                need[s2[i-l1]] += 1            
                if need[s2[i-l1]]>0: missing += 1  
            if missing == 0:
                return True
        return False

# V2
# Time:  O(n)
# Space: O(1)
import collections
class Solution(object):
    def checkInclusion(self, s1, s2):
        """
        :type s1: str
        :type s2: str
        :rtype: bool
        """
        counts = collections.Counter(s1)
        l = len(s1)
        for i in range(len(s2)):
            if counts[s2[i]] > 0:
                l -= 1
            counts[s2[i]] -= 1
            if l == 0:
                return True
            start = i + 1 - len(s1)
            if start >= 0:
                counts[s2[start]] += 1
                if counts[s2[start]] > 0:
                    l += 1