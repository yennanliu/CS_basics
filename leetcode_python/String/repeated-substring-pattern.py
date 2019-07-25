# Time:  O(n)
# Space: O(n)

# Given a non-empty string check if it can be constructed by taking a substring of it
# and appending multiple copies of the substring together.
# You may assume the given string consists of lowercase English letters only and its length will not exceed 10000.
#
# Example 1:
# Input: "abab"
#
# Output: True
#
# Explanation: It's the substring "ab" twice.
# Example 2:
# Input: "aba"
#
# Output: False
# Example 3:
# Input: "abcabcabcabc"
#
# Output: True
#
# Explanation: It's the substring "abc" four times. (And the substring "abcabc" twice.)

# KMP solution.

# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/54564801
# IDEA : 
# go through the sting, if can find any sub-string 
# that is part of the origin string, return Ture.
# if not, return False. 
class Solution:
    def repeatedSubstringPattern(self, s):
        """
        :type s: str
        :rtype: bool
        """
        len_s = len(s)
        for i in range(1, len_s // 2 + 1): # (sub-string)* k = string. k = 2, 3, 4, ....n. so k start from 2 
            if len_s % i == 0:             # when len(sub-string)*k = len(string)
                sub_s = s[:i]              # get sub-string 
                if sub_s * (len_s // i) == s:
                    return True
        return False

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def repeatedSubstringPattern(self, str):
        """
        :type str: str
        :rtype: bool
        """
        def getPrefix(pattern):
            prefix = [-1] * len(pattern)
            j = -1
            for i in range(1, len(pattern)):
                while j > -1 and pattern[j + 1] != pattern[i]:
                    j = prefix[j]
                if pattern[j + 1] == pattern[i]:
                    j += 1
                prefix[i] = j
            return prefix

        prefix = getPrefix(str)
        return prefix[-1] != -1 and \
               (prefix[-1] + 1) % (len(str) - prefix[-1] - 1) == 0

    def repeatedSubstringPattern2(self, str):
        """
        :type str: str
        :rtype: bool
        """
        if not str:
            return False

        ss = (str + str)[1:-1]
        return ss.find(str) != -1
