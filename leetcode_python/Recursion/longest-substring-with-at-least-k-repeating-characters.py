# V0 
# IDEA : RECURSION 
# -> KEEP SPLITING THE STRING, AND CHECK IF ELEMENTS IN IT EXISTS AT LEAST k TIMES
# DMEO 
# c =  b s =  ababbc
# c =  c s =  ababbc
# c =  b s =  ababb
# c =  a s =  ababb
# s =  ababb 5
# Out[50]: 5
class Solution(object):
    def longestSubstring(self, s, k):
        """
        :type s: str
        :type k: int
        :rtype: int
        """
        if len(s) < k:
            return 0
        for c in set(s):
            if s.count(c) < k:
                return max(self.longestSubstring(t, k) for t in s.split(c))
        return len(s) 

#  V0': NEED TO FIX
# import collections 
# class Solution(object):
#     def longestSubstring(self, s, k):
#         if len(s) < k:
#             return 0 
#         s_dict = collections.Counter(s)
#         count, tmp = 0, 0 
#         for i in s:
#             if s_dict[i] == 1:
#                 count = max(tmp, count)
#                 tmp = 0 
#             else:
#                 tmp += 1 
#         return max(tmp, count) if max(tmp, count) < k else 0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82889933
class Solution(object):
    def longestSubstring(self, s, k):
        """
        :type s: str
        :type k: int
        :rtype: int
        """
        if len(s) < k:
            return 0
        for c in set(s):
            if s.count(c) < k:
                return max(self.longestSubstring(t, k) for t in s.split(c))
        return len(s)

# V1'
# https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87768/4-lines-Python
from collections import Counter
class Solution:
    def longestSubstring(self, s, k):
        ctr = Counter(s)
        for c,v in ctr.items():
            if v<k:
                return max([self.longestSubstring(sub, k) for sub in s.split(c) if len(sub)>=k] or [0])
        return len(s)

# V1''
# https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87768/4-lines-Python
class Solution:
    def longestSubstring(self, s, k):
        if len(s) < k:
            return 0
        c = min(set(s), key=s.count)
        if s.count(c) >= k:
            return len(s)
        return max(self.longestSubstring(t, k) for t in s.split(c))

# V2 
# Time:  O(26 * n) = O(n)
# Space: O(26) = O(1)
class Solution(object):
    def longestSubstring(self, s, k):
        """
        :type s: str
        :type k: int
        :rtype: int
        """
        def longestSubstringHelper(s, k, start, end):
            count = [0] * 26
            for i in range(start, end):
                count[ord(s[i]) - ord('a')] += 1
            max_len = 0
            i = start
            while i < end:
                while i < end and count[ord(s[i]) - ord('a')] < k:
                    i += 1
                j = i
                while j < end and count[ord(s[j]) - ord('a')] >= k:
                    j += 1

                if i == start and j == end:
                    return end - start

                max_len = max(max_len, longestSubstringHelper(s, k, i, j))
                i = j
            return max_len

        return longestSubstringHelper(s, k, 0, len(s))
