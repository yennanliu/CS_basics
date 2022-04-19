"""

242. Valid Anagram
Easy

Given two strings s and t, return true if t is an anagram of s, and false otherwise.

An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.

 

Example 1:

Input: s = "anagram", t = "nagaram"
Output: true
Example 2:

Input: s = "rat", t = "car"
Output: false
 

Constraints:

1 <= s.length, t.length <= 5 * 104
s and t consist of lowercase English letters.
 

Follow up: What if the inputs contain Unicode characters? How would you adapt your solution to such a case?

"""

# V0 
class Solution:
    def isAnagram(self, s, t):
        return sorted(s) == sorted(t)

# V1 
# https://blog.csdn.net/liuchonge/article/details/51913298
# http://bookshadow.com/weblog/2015/08/01/leetcode-valid-anagram/
class Solution:
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
