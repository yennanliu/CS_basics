"""

2027. Minimum Moves to Convert String
Easy

You are given a string s consisting of n characters which are either 'X' or 'O'.

A move is defined as selecting three consecutive characters of s and converting them to 'O'. Note that if a move is applied to the character 'O', it will stay the same.

Return the minimum number of moves required so that all the characters of s are converted to 'O'.


Example 1:

Input: s = "XXX"
Output: 1
Explanation: XXX -> OOO
We select all the 3 characters and convert them in one move.

Example 2:

Input: s = "XXOX"
Output: 2
Explanation: XXOX -> OOOX -> OOOO
We select the first 3 characters in the first move, and convert them to 'O'.
Then we select the last 3 characters and convert them so that the final string contains all 'O's.

Example 3:

Input: s = "OOOO"
Output: 0
Explanation: There are no 'X's in s to convert.


Constraints:

3 <= s.length <= 1000
s[i] is either 'X' or 'O'.

"""

# V0
# IDEA : GREEDY LEFT-TO-RIGHT SCAN
#
#   the leftmost remaining 'X' must be covered by some move, and the move
#   that covers the most ground is the one STARTING at that 'X'. so : jump
#   to the first 'X', spend one move, skip 3 characters, repeat.
#
#   NOTE : jumping i += 3 is safe even near the end — a window that runs off
#          the string still clears the trailing 'X's in one move.
#
# time = O(n), space = O(1)
class Solution(object):
    def minimumMoves(self, s):
        res = 0
        i = 0
        n = len(s)
        while i < n:
            if s[i] == 'X':
                res += 1
                i += 3
            else:
                i += 1
        return res
