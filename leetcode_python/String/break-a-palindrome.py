"""

1328. Break a Palindrome
Medium

Given a palindromic string of lowercase English letters palindrome, replace exactly one character
with any lowercase English letter so that the resulting string is not a palindrome and that it is
the lexicographically smallest one possible.

Return the resulting string. If there is no way to replace a character to make it not a palindrome,
return an empty string.

A string a is lexicographically smaller than a string b (of the same length) if in the first
position where a and b differ, a has a character strictly smaller than the corresponding character
in b. For example, "abcc" is lexicographically smaller than "abcd" because the first position
they differ is at the fourth character, and 'c' is smaller than 'd'.


Example 1:

Input: palindrome = "abccba"
Output: "aaccba"
Explanation: There are many ways to make "abccba" not a palindrome, such as "zbccba", "aaccba",
and "abacba".
Of all the ways, "aaccba" is the lexicographically smallest.

Example 2:

Input: palindrome = "a"
Output: ""
Explanation: There is no way to replace a single character to make "a" not a palindrome,
so return an empty string.


Constraints:

1 <= palindrome.length <= 1000
palindrome consists of only lowercase English letters.

"""

# V0
# IDEA : GREEDY (first non-'a' in the left half -> 'a', else last char -> 'b')
#
#   to get the smallest string we want to lower the EARLIEST character we can,
#   and the smallest letter is 'a'. Scanning only the left half is enough :
#   changing a left-half character always breaks the mirror.
#
#   two corner cases :
#     - the left half is all 'a' (e.g. "aaa", "aabaa") -> no character there
#       can be lowered, so raise the LAST character to 'b' instead (cheapest
#       possible bump, and the tail position keeps the string small)
#     - length 1 -> any single replacement is still a palindrome -> ""
#
#   NOTE : the middle character of an odd-length palindrome is skipped on
#          purpose -- editing it mirrors onto itself and stays a palindrome.
#
# time = O(n), space = O(n)
class Solution(object):
    def breakPalindrome(self, palindrome):
        n = len(palindrome)
        if n < 2:
            return ""

        chars = list(palindrome)
        for i in range(n // 2):
            if chars[i] != 'a':
                chars[i] = 'a'
                return ''.join(chars)

        chars[-1] = 'b'
        return ''.join(chars)
