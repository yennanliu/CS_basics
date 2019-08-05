"""
Given a string which consists of lowercase or uppercase letters, find the length of the longest palindromes that can be built with those letters.

This is case sensitive, for example "Aa" is not considered a palindrome here.

Note:
Assume the length of given string will not exceed 1,010.

Example:

Input:
"abccccdd"

Output:
7

Explanation:
One longest palindrome that can be built is "dccaccd", whose length is 7.

"""
# Time:  O(n)
# Space: O(1)

# Given a string which consists of lowercase or uppercase letters,
# find the length of the longest palindromes that can be built with those letters.
#
# This is case sensitive, for example "Aa" is not considered a palindrome here.
#
# Note:
# Assume the length of given string will not exceed 1,010.
#
# Example:
#
# Input:
# "abccccdd"
#
# Output:
# 7
#
# Explanation:
# One longest palindrome that can be built is "dccaccd", whose length is 7.

# V0 


# V1 
# http://bookshadow.com/weblog/2016/10/02/leetcode-longest-palindrome/
# IDEA : collections.Counter
import collections 
class Solution(object):
    def longestPalindrome(self, s):
        """
        :type s: str
        :rtype: int
        """
        ans = odd = 0
        cnt = collections.Counter(s)
        for c in cnt:
            ans += cnt[c]
            if cnt[c] % 2 == 1:
                ans -= 1        # for cases : "ccc", will only take substring "cc" to form the palindrome string.  
                odd += 1        # to cacluate how many odd elements in the string, and ans = ans +1 if there are odd elements 
        return ans + (odd > 0)  # for cases : "cccaadd", "cc" can be palindrome, while c is odd, and can palindrome string allow 1 odd element.  if true, (odd > 0) == 1 ; else : (odd > 0) == 0  

# V2 
import collections
class Solution(object):
    def longestPalindrome(self, s):
        """
        :type s: str
        :rtype: int
        """
        odds = 0
        for k, v in collections.Counter(s).items():
            odds += v & 1
        return len(s) - odds + int(odds > 0)

    def longestPalindrome2(self, s):
        """
        :type s: str
        :rtype: int
        """
        odd = sum([x & 1 for x in list(collections.Counter(s).values())])
        return len(s) - odd + int(odd > 0)
