"""

761. Special Binary String
Hard

Special binary strings are binary strings with the following two properties:

  - The number of 0's is equal to the number of 1's.
  - Every prefix of the binary string has at least as many 1's as 0's.

You are given a special binary string s.

A move consists of choosing two consecutive, non-empty, special substrings of s,
and swapping them. Two strings are consecutive if the last character of the first
string is exactly one index before the first character of the second string.

Return the lexicographically largest resulting string possible after applying the
mentioned operations on the string.


Example 1:

Input: s = "11011000"
Output: "11100100"
Explanation: The strings "10" [occuring at s[1]] and "1100" [at s[3]] are swapped.
This is the lexicographically largest string possible after some number of swaps.

Example 2:

Input: s = "10"
Output: "10"


Constraints:

1 <= s.length <= 50
s[i] is either '0' or '1'.
s is a special binary string.

"""

# V0
# IDEA : RECURSION (treat the string as balanced parentheses)
#
#   Read '1' as '(' and '0' as ')': a special string is a valid, balanced sequence.
#   Split s into its top-level balanced blocks (counter returns to 0). Each block is
#   "1" + <inner special string> + "0" — recursively make the inner part largest,
#   then sort the blocks in DESCENDING order and concatenate.
#
#   Sorting blocks is legal because adjacent top-level blocks are exactly the
#   "two consecutive special substrings" the problem lets us swap, and any
#   permutation is reachable by adjacent swaps.
#
# time = O(n^2 log n)
# space = O(n^2) (recursive slices)
class Solution(object):
    def makeLargestSpecial(self, s):
        if not s:
            return ""

        blocks = []
        count = 0
        start = 0

        for i, ch in enumerate(s):
            count += 1 if ch == "1" else -1
            if count == 0:
                # s[start:i+1] is a top-level block: '1' + inner + '0'
                inner = self.makeLargestSpecial(s[start + 1:i])
                blocks.append("1" + inner + "0")
                start = i + 1

        # lexicographically largest -> put the biggest blocks first
        blocks.sort(reverse=True)
        return "".join(blocks)
