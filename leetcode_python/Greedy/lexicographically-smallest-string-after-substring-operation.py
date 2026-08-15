"""

2734. Lexicographically Smallest String After Substring Operation
Medium

Given a string s consisting of lowercase English letters. Perform the following operation:

Select any non-empty substring then replace every letter of the substring with the preceding letter of the English alphabet. For example, 'b' is converted to 'a', and 'a' is converted to 'z'.

Return the lexicographically smallest string after performing the operation.


Example 1:

Input: s = "cbabc"
Output: "baabc"
Explanation:
Perform the operation on the substring starting at index 0, and ending at index 1 inclusive.

Example 2:

Input: s = "aa"
Output: "az"
Explanation:
Perform the operation on the last letter.

Example 3:

Input: s = "acbbc"
Output: "abaab"
Explanation:
Perform the operation on the substring starting at index 1, and ending at index 4 inclusive.

Example 4:

Input: s = "leetcode"
Output: "kddsbncd"
Explanation:
Perform the operation on the entire string.


Constraints:

1 <= s.length <= 3 * 10^5
s consists of lowercase English letters

"""

# V0
# IDEA : GREEDY - DECREMENT THE FIRST NON-'a' RUN
#
#   exactly ONE operation is performed (the substring is non-empty), so we are
#   picking one interval [i, j) to decrement.
#
#   decrementing helps on any letter except 'a' ('a' wraps up to 'z', which is
#   strictly worse). Lexicographic order is decided by the EARLIEST changed
#   position, so:
#     - skip the leading 'a's (touching one of them would raise it to 'z')
#     - from the first non-'a' at index i, decrement greedily as far as we can
#       and stop right before the next 'a' — extending past that 'a' would turn
#       it into 'z' and blow up the string at that position.
#
#   NOTE : the all-'a' case is forced — we MUST operate on something, so pick
#          the single LAST character and pay 'a' -> 'z' at the latest possible
#          position, i.e. "aaa" -> "aaz".
#
# time = O(n), space = O(n)
class Solution(object):
    def smallestString(self, s):
        n = len(s)
        i = 0
        while i < n and s[i] == 'a':
            i += 1
        if i == n:
            # all 'a' : must operate, sacrifice the last char
            return s[:n - 1] + 'z'
        j = i
        while j < n and s[j] != 'a':
            j += 1
        shifted = "".join(chr(ord(c) - 1) for c in s[i:j])
        return s[:i] + shifted + s[j:]
