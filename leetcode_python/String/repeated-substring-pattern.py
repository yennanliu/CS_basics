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


# V1  : dev 

""" 
class Solution:
    def repeatedSubstringPattern(self, s):
        if len(s) <= 1:
            return False
        if len(s) == 2:
            if s[0] == s[1]:
                return True
            else:
                return False
        for i,j in enumerate(s):
            # at least a substring with "2 elements"
            # i.e.  'ab' in 'abababa' ; instead of 'a'
            sub_str = s[:i+2]
            #print ('sub_str :', sub_str)
            #print ('s_remain : ' ,s[i+2:][-(i+2):])
            #print ('---------')
            if sub_str == s[i+2:][-(i+2):]:
                return True
        else:
            return False

"""


# V2 
class Solution(object):
    def repeatedSubstringPattern(self, str):
        """
        :type str: str
        :rtype: bool
        """
        def getPrefix(pattern):
            prefix = [-1] * len(pattern)
            j = -1
            for i in xrange(1, len(pattern)):
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
        print ss
        return ss.find(str) != -1


