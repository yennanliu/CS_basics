"""

3110. Score of a String
Easy

You are given a string s. The score of a string is defined as the sum of the absolute difference between the ASCII values of adjacent characters.

Return the score of s.


Example 1:

Input: s = "hello"
Output: 13
Explanation:
The ASCII values of the characters in s are: 'h' = 104, 'e' = 101, 'l' = 108, 'l' = 108, 'o' = 111. So, the score of s would be |104 - 101| + |101 - 108| + |108 - 108| + |108 - 111| = 3 + 7 + 0 + 3 = 13.

Example 2:

Input: s = "zaz"
Output: 50
Explanation:
The ASCII values of the characters in s are: 'z' = 122, 'a' = 97, 'z' = 122. So, the score of s would be |122 - 97| + |97 - 122| = 25 + 25 = 50.


Constraints:

2 <= s.length <= 100
s consists only of lowercase English letters.

"""

# V0
# IDEA : SUM |ord(s[i]) - ord(s[i-1])| OVER THE ADJACENT PAIRS
#
#   a direct reading of the definition — zip(s, s[1:]) walks the pairs.
#
# time = O(n), space = O(1)
class Solution(object):
    def scoreOfString(self, s):
        return sum(abs(ord(a) - ord(b)) for a, b in zip(s, s[1:]))
