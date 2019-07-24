# Time:  O(n)
# Space: O(1)

# Given an arbitrary ransom note string and another string containing letters
# from all the magazines, write a function that will return true if
# the ransom  note can be constructed from the magazines ;
# otherwise, it will return false.
#
# Each letter in the magazine string can only be used once in your ransom note.
#
# Note:
# You may assume that both strings contain only lowercase letters.
#
# canConstruct("a", "b") -> false
# canConstruct("aa", "ab") -> false
# canConstruct("aa", "aab") -> true


# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/54178342
class Solution:
    def canConstruct(self, ransomNote, magazine):
        """
        :type ransomNote: str
        :type magazine: str
        :rtype: bool
        """
        rcount = collections.Counter(ransomNote)
        mcount = collections.Counter(magazine)
        for r, c in rcount.items():
            if c > mcount[r]:
                return False
        return True

# V2 
class Solution(object):
    def canConstruct(self, ransomNote, magazine):
        """
        :type ransomNote: str
        :type magazine: str
        :rtype: bool
        """
        counts = [0] * 26
        letters = 0

        for c in ransomNote:
            if counts[ord(c) - ord('a')] == 0:
                letters += 1
            counts[ord(c) - ord('a')] += 1

        for c in magazine:
            counts[ord(c) - ord('a')] -= 1
            if counts[ord(c) - ord('a')] == 0:
                letters -= 1
                if letters == 0:
                    break

        return letters == 0


# V3 
# Time:  O(n)
# Space: O(1)
import collections
class Solution2(object):
    def canConstruct(self, ransomNote, magazine):
        """
        :type ransomNote: str
        :type magazine: str
        :rtype: bool
        """
        return not collections.Counter(ransomNote) - collections.Counter(magazine)

# V3 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def canConstruct(self, ransomNote, magazine):
        """
        :type ransomNote: str
        :type magazine: str
        :rtype: bool
        """
        counts = [0] * 26
        letters = 0

        for c in ransomNote:
            if counts[ord(c) - ord('a')] == 0:
                letters += 1
            counts[ord(c) - ord('a')] += 1

        for c in magazine:
            counts[ord(c) - ord('a')] -= 1
            if counts[ord(c) - ord('a')] == 0:
                letters -= 1
                if letters == 0:
                    break

        return letters == 0

# Time:  O(n)
# Space: O(1)
import collections
class Solution2(object):
    def canConstruct(self, ransomNote, magazine):
        """
        :type ransomNote: str
        :type magazine: str
        :rtype: bool
        """
        return not collections.Counter(ransomNote) - collections.Counter(magazine)
