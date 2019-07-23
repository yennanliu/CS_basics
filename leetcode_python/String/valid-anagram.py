# Time:  O(n)
# Space: O(1)
#
# Given two strings s and t, write a function to
# determine if t is an anagram of s.
#
# For example,
# s = "anagram", t = "nagaram", return true.
# s = "rat", t = "car", return false.
#
# Note:
# You may assume the string contains only lowercase alphabets.
#

# V0 

# V1 
# https://blog.csdn.net/liuchonge/article/details/51913298
# http://bookshadow.com/weblog/2015/08/01/leetcode-valid-anagram/
class Solution:
    # @param {string} s
    # @param {string} t
    # @return {boolean}
    def isAnagram(self, s, t):
        return sorted(s) == sorted(t)

# V1' 
# https://blog.csdn.net/liuchonge/article/details/51913298
# http://bookshadow.com/weblog/2015/08/01/leetcode-valid-anagram/
class Solution:
    # @param {string} s
    # @param {string} t
    # @return {boolean}
    def isAnagram(self, s, t):
        from collections import Counter
        return Counter(s).items() == Counter(t).items()

# V2
class Solution:
    def isAnagram(self, s, t):
        return sorted(s) == sorted(t)

# V3 
# Time:  O(n)
# Space: O(1)
import collections
import string
class Solution(object):
    # @param {string} s
    # @param {string} t
    # @return {boolean}
    def isAnagram(self, s, t):
        if len(s) != len(t):
            return False
        count = collections.defaultdict(int)
        for c in s:
            count[c] += 1
        for c in t:
            count[c] -= 1
            if count[c] < 0:
                return False
        return True

# Time:  O(nlogn)
# Space: O(n)
class Solution2(object):
    # @param {string} s
    # @param {string} t
    # @return {boolean}
    def isAnagram(self, s, t):
        return sorted(s) == sorted(t)

# Time:  O(n)
# Space: O(n)
class Solution3(object):
    # @param {string} s
    # @param {string} t
    # @return {boolean}
    def isAnagram(self, s, t):
        return collections.Counter(s) == collections.Counter(t)
