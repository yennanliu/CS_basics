"""

1869. Longer Contiguous Segments of Ones than Zeros
Easy

Given a binary string s, return true if the longest contiguous segment of 1's is strictly longer than the longest contiguous segment of 0's in s, or return false otherwise.

For example, in s = "110100010" the longest continuous segment of 1s has length 2, and the longest continuous segment of 0s has length 3.

Note that if there are no 0's, then the longest continuous segment of 0's is considered to have a length 0. The same applies if there is no 1's.


Example 1:

Input: s = "1101"
Output: true
Explanation:
The longest contiguous segment of 1s has length 2: "1101"
The longest contiguous segment of 0s has length 1: "1101"
The segment of 1s is longer, so return true.

Example 2:

Input: s = "111000"
Output: false
Explanation:
The longest contiguous segment of 1s has length 3: "111000"
The longest contiguous segment of 0s has length 3: "111000"
The segment of 1s is not longer, so return false.

Example 3:

Input: s = "110100010"
Output: false
Explanation:
The longest contiguous segment of 1s has length 2: "110100010"
The longest contiguous segment of 0s has length 3: "110100010"
The segment of 1s is not longer, so return false.


Constraints:

1 <= s.length <= 100
s[i] is either '0' or '1'.

"""

# V0
# IDEA : SINGLE PASS RUN LENGTH (track both runs at once)
#
#   walk the string keeping `run` = length of the current streak.
#   whenever the character changes, the streak restarts at 1.
#   after each step update best[c] where c is the current character.
#
#   NOTE : a character that never appears keeps best = 0, which matches
#          the problem's "no 0's -> longest 0 segment is 0" rule.
#
# time = O(n), space = O(1)
class Solution(object):
    def checkZeroOnes(self, s):
        best = {'0': 0, '1': 0}
        run = 0

        for i in range(len(s)):
            if i > 0 and s[i] == s[i - 1]:
                run += 1
            else:
                run = 1
            if run > best[s[i]]:
                best[s[i]] = run

        return best['1'] > best['0']


# V0-1
# IDEA : BRUTE FORCE - GROW A CANDIDATE RUN AND ASK "IS IT A SUBSTRING?"
#
#   instead of measuring the runs, we TEST them: '111' occurs in s iff s has a
#   run of 1s of length >= 3. so the longest run of c is the largest L with
#   c * L in s, and since the predicate is monotone in L (if c*L fits then
#   c*(L-1) fits) we can simply grow L by one until the test fails.
#
#   uses no counters and no grouping at all - only Python's substring search.
#
# time = O(n^2), each `in` test scans s and there can be O(n) tests
# space = O(n) for the candidate string being built
class Solution(object):
    def checkZeroOnes(self, s):
        def longest(ch):
            L = 0
            while ch * (L + 1) in s:
                L += 1
            return L

        return longest('1') > longest('0')


# V0-2
# IDEA : SPLIT ON THE OPPOSITE CHARACTER
#
#   splitting s on '0' cuts it exactly at every zero, so every piece that
#   survives is a MAXIMAL run of 1s (with empty strings wherever two zeros
#   were adjacent). the longest piece is therefore the answer for 1s, and the
#   mirrored split gives the answer for 0s:
#
#       "110100010".split('0') -> ['11', '1', '', '', '1', '']  -> 2
#       "110100010".split('1') -> ['', '', '0', '000', '0']     -> 3
#
#   NOTE : the empty pieces are why a character that never appears yields 0
#          rather than an error - split always returns at least one piece.
#
# time = O(n)
# space = O(n) for the pieces
class Solution(object):
    def checkZeroOnes(self, s):
        ones = max(len(piece) for piece in s.split('0'))
        zeros = max(len(piece) for piece in s.split('1'))
        return ones > zeros
