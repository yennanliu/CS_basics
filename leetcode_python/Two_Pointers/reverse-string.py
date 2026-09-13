"""

344. Reverse String
Easy

Write a function that reverses a string. The input string is given as an array of characters s.

You must do this by modifying the input array in-place with O(1) extra memory.

Example 1:

Input: s = ["h","e","l","l","o"]
Output: ["o","l","l","e","h"]

Example 2:

Input: s = ["H","a","n","n","a","h"]
Output: ["h","a","n","n","a","H"]

Constraints:

1 <= s.length <= 10^5
s[i] is a printable ascii character.

"""

# Time:  O(n)
# Space: O(n)

# Write a function that takes a string as input and
# returns the string reversed.
#
# Example:
# Given s = "hello", return "olleh".


# V0

# V1
# time = O(n)
# space = O(n)
class Solution2(object):
    def reverseString(self, s):
        return s[::-1]



# V2
# time = O(n)
# space = O(n)
class Solution(object):
    def reverseString(self, s):
        """
        :type s: str
        :rtype: str
        """
        string = list(s)
        i, j = 0, len(string) - 1
        while i < j:
            string[i], string[j] = string[j], string[i]
            i += 1
            j -= 1
        return "".join(string)


# V3
# time = O(n)
# space = O(n)
class Solution2(object):
    def reverseString(self, s):
        """
        :type s: str
        :rtype: str
        """
        return s[::-1]



