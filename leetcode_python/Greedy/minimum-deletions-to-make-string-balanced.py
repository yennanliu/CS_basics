"""

1653. Minimum Deletions to Make String Balanced
Medium

You are given a string s consisting only of characters 'a' and 'b'.

You can delete any number of characters in s to make s balanced. s is balanced if there is no pair of indices (i,j) such that i < j and s[i] = 'b' and s[j]= 'a'.

Return the minimum number of deletions needed to make s balanced.


Example 1:

Input: s = "aababbab"
Output: 2
Explanation: You can either:
Delete the characters at 0-indexed positions 2 and 6 ("aababbab" -> "aaabbb"), or
Delete the characters at 0-indexed positions 3 and 6 ("aababbab" -> "aabbbb").

Example 2:

Input: s = "bbaaaaabb"
Output: 2
Explanation: The only solution is to delete the first two characters.


Constraints:

1 <= s.length <= 10^5
s[i] is 'a' or 'b'.

"""

# V0
# IDEA : RUNNING DP -- keep the best cost for the prefix seen so far
#
#   a balanced string is a block of 'a's followed by a block of 'b's.
#   scan left to right holding
#     b     = number of 'b's seen so far
#     best  = min deletions to balance the prefix
#
#   on 'b' : keeping it is always free (it can join the trailing b-block)
#            -> best unchanged, b += 1
#   on 'a' : two choices
#            - delete this 'a'          -> best + 1
#            - keep it, which forces every earlier 'b' to go -> b
#            take the min.
#
#   NOTE : this is the classic "prefix cost" trick; no O(n) suffix arrays
#          are needed.
#
# time = O(n), space = O(1)
class Solution(object):
    def minimumDeletions(self, s):
        b = 0
        best = 0
        for c in s:
            if c == 'b':
                b += 1
            else:
                best = min(best + 1, b)
        return best
