"""

1933. Check if String Is Decomposable Into Value-Equal Substrings
Easy

A value-equal string is a string where all characters are the same.

For example, "1111" and "33" are value-equal strings.
In contrast, "123" is not a value-equal string.

Given a digit string s, decompose the string into some number of consecutive value-equal substrings where exactly one substring has a length of 2 and the remaining substrings have a length of 3.

Return true if you can decompose s according to the above rules. Otherwise, return false.

A substring is a contiguous sequence of characters in a string.


Example 1:

Input: s = "000111000"
Output: false
Explanation: s cannot be decomposed according to the rules because ["000", "111", "000"] does not have a substring of length 2.

Example 2:

Input: s = "00011111222"
Output: true
Explanation: s can be decomposed into ["000", "111", "11", "222"].

Example 3:

Input: s = "011100022233"
Output: false
Explanation: s cannot be decomposed according to the rules because of the first '0'.


Constraints:

1 <= s.length <= 1000
s consists of only digits '0' through '9'.

"""

# V0
# IDEA : GROUP EQUAL RUNS, THEN LOOK AT EACH RUN LENGTH MOD 3
#
#   the pieces must be value-equal, so each maximal run of one digit is cut
#   independently. a run of length L can be cut into 3's and at most one 2 :
#
#     L % 3 == 0 -> all 3's, contributes no "2"
#     L % 3 == 2 -> exactly one 2 (plus 3's), consumes THE single allowed 2
#     L % 3 == 1 -> impossible (a leftover 1 can never be paid for)
#
#   so : reject on any L % 3 == 1, count how many runs need a 2, and require
#   that count to be exactly 1.
#
# time = O(n), space = O(1)
from itertools import groupby
class Solution(object):
    def isDecomposable(self, s):
        twos = 0
        for _, grp in groupby(s):
            L = len(list(grp))
            if L % 3 == 1:
                return False
            if L % 3 == 2:
                twos += 1
                if twos > 1:
                    return False
        return twos == 1
