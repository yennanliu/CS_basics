"""

521. Longest Uncommon Subsequence I
Easy

Given two strings a and b, return the length of the longest uncommon subsequence between a and b. If no such uncommon subsequence exists, return -1.

An uncommon subsequence between two strings is a string that is a subsequence of exactly one of them.

Example 1:

Input: a = "aba", b = "cdc"
Output: 3
Explanation: One longest uncommon subsequence is "aba" because "aba" is a subsequence of "aba" but not "cdc".
Note that "cdc" is also a longest uncommon subsequence.

Example 2:

Input: a = "aaa", b = "bbb"
Output: 3
Explanation: The longest uncommon subsequences are "aaa" and "bbb".

Example 3:

Input: a = "aaa", b = "aaa"
Output: -1
Explanation: Every subsequence of string a is also a subsequence of string b. Similarly, every subsequence of string b is also a subsequence of string a. So the answer would be -1.

Constraints:

1 <= a.length, b.length <= 100
a and b consist of lower-case English letters.

"""

# Time:  O(min(a, b))
# Space: O(1)
# Given a group of two strings, you need to find the longest uncommon subsequence of this group of two strings.
# The longest uncommon subsequence is defined as the longest subsequence of one of these strings
# and this subsequence should not be any subsequence of the other strings.
#
# A subsequence is a sequence that can be derived from one sequence
# by deleting some characters without changing the order of the remaining elements.
# Trivially, any string is a subsequence of itself and an empty string is a subsequence of any string.
#
# The input will be two strings, and the output needs to be the length of the longest uncommon subsequence.
# If the longest uncommon subsequence doesn't exist, return -1.
#
# Example 1:
# Input: "aba", "cdc"
# Output: 3
# Explanation: The longest uncommon subsequence is "aba" (or "cdc"),
# because "aba" is a subsequence of "aba",
# but not a subsequence of any other strings in the group of two strings.
# Note:
#
# Both strings' lengths will not exceed 100.
# Only letters from a ~ z will appear in input strings.

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79398536
# time = O(min(len(a), len(b)))
# space = O(1)
class Solution(object):
    def findLUSlength(self, a, b):
        """
        :type a: str
        :type b: str
        :rtype: int
        """
        return max(len(a), len(b)) if a != b else -1

# V2
# time = O(min(len(a), len(b)))
# space = O(1)
class Solution(object):
    def findLUSlength(self, a, b):
        """
        :type a: str
        :type b: str
        :rtype: int
        """
        if a == b:
            return -1
        return max(len(a), len(b))
