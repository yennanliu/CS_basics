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

# Time:  O(n)
# Space: O(1)


# V1  : dev 
"""

s='code' -> false 
s='careerac' -> true 
s='ffkkix' -> false 
s='ffkki' -> false 

"""
import collections
class Solution(object):
	def canPermutePalindrome(self, s):
		# if is palindrome-permutation, can only can 1 or 0  non-pair number 
		# have > 1 non-pair number  -> non palindrome-permutation
		if sum(v % 2 for v in collections.Counter(s).values()) < 2:
			return True 
		else:
			return False



# V2 
import collections


class Solution(object):
    def canPermutePalindrome(self, s):
        """
        :type s: str
        :rtype: bool
        """
        return sum(v % 2 for v in collections.Counter(s).values()) < 2




