"""

2380. Time Needed to Rearrange a Binary String
Medium

You are given a binary string s. In one second, all occurrences of "01" are simultaneously replaced with "10". This process repeats until no occurrences of "01" exist.

Return the number of seconds needed to complete this process.


Example 1:

Input: s = "0110101"
Output: 4
Explanation:
After one second, s becomes "1011010".
After another second, s becomes "1101100".
After the third second, s becomes "1110100".
After the fourth second, s becomes "1111000".
No occurrence of "01" exists, and the process needed 4 seconds to complete,
so we return 4.

Example 2:

Input: s = "11100"
Output: 0
Explanation:
No occurrence of "01" exists in s, and the processes needed 0 seconds to complete,
so we return 0.


Constraints:

1 <= s.length <= 1000
s[i] is either '0' or '1'.


"""

# V0
# IDEA : TRACK WHEN EACH '1' FINISHES CROSSING THE ZEROS TO ITS LEFT
#
#   a '1' with z zeros in front of it needs at least z seconds to walk past
#   them (it advances one position per second). but it also cannot overtake
#   the '1' before it, so it finishes at least one second after that one.
#
#   sweeping left to right with `zeros` = zeros seen so far :
#       res = max(res + 1, zeros)      on every '1' that has zeros ahead of it
#   the `res + 1` term is the queueing constraint, `zeros` the distance one.
#
#   a '1' with no zeros before it is already in place and costs nothing.
#
# time = O(n), space = O(1)
class Solution(object):
    def secondsToRemoveOccurrences(self, s):
        res = 0
        zeros = 0
        for c in s:
            if c == '0':
                zeros += 1
            elif zeros > 0:
                res = max(res + 1, zeros)
        return res
