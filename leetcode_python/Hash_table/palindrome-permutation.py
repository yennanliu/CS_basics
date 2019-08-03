"""
Given a string, determine if a permutation of the string could form a palindrome.
For example,
"code" -> False, "aab" -> True, "carerac" -> True.

Hint:
Consider the palindromes of odd vs even length. What difference do you notice?
Count the frequency of each character.
If each character occurs even number of times, then it must be a palindrome. How about character which occurs odd number of times?
http://leetcode.com/problems/palindrome-permutation/
"""

# VO

# V1 
# https://blog.csdn.net/danspace1/article/details/86552613
# IDEA :
# THE VALID STRING CAN ONLY HAVE 1 ELEMENT EXISTS FOR ODD TIMES (i.e. 1,3,5..)
# AND ALL THE REST OF THE ELEMENTS SHOULD EXIST FOR EVEN TIMES (i.e. 2,4,6..)
import collections
class Solution(object):
    def canPermutePalindrome(self, s):
        """
        :type s: str
        :rtype: bool
        """
        count = collections.Counter(s)
        chance = 1
        for char in count:
            if count[char]%2 != 0:
                chance -= 1
                if chance < 0:
                    return False
        return True

# V2 
import collections
class Solution(object):
    def canPermutePalindrome(self, s):
        """
        :type s: str
        :rtype: bool
        """
        return sum(v % 2 for v in list(collections.Counter(s).values())) < 2
