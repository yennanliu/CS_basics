"""

2743. Count Substrings Without Repeating Character
Medium

You are given a string s consisting only of lowercase English letters. We call a substring special if it contains no character which has occurred at least twice (in other words, it does not contain a repeating character). Your task is to count the number of special substrings. For example, in the string "pop", the substring "po" is a special substring, however, "pop" is not special (since 'p' has occurred twice).

Return the number of special substrings.

A substring is a contiguous sequence of characters within a string. For example, "abc" is a substring of "abcd", but "acd" is not.


Example 1:

Input: s = "abcd"
Output: 10
Explanation: Since each character occurs once, every substring is a special substring. We have 4 substrings of length one, 3 of length two, 2 of length three, and 1 substring of length four. So overall there are 4 + 3 + 2 + 1 = 10 special substrings.

Example 2:

Input: s = "ooo"
Output: 3
Explanation: Any substring with a length of at least two contains a repeating character. So we have to count the number of substrings of length one, which is 3.

Example 3:

Input: s = "abab"
Output: 7
Explanation: Special substrings are as follows (sorted by their start positions):
Special substrings of length 1: "a", "b", "a", "b"
Special substrings of length 2: "ab", "ba", "ab"
And it can be shown that there are no special substrings with a length of at least three. So the answer would be 4 + 3 = 7.


Constraints:

1 <= s.length <= 10^5
s consists of lowercase English letters

"""

# V0
# IDEA : SLIDING WINDOW (TWO POINTERS) + "COUNT SUBSTRINGS ENDING AT i"
#
#   Maintain the longest window [left, i] that has no repeated character.
#   When s[i] enters the window, jump `left` past the PREVIOUS occurrence of
#   s[i] (if that occurrence is still inside the window).
#
#   Key counting idea : every special substring is counted exactly once by
#   its RIGHT end. A substring [j, i] is special iff j >= left, so there are
#   i - left + 1 special substrings ending at i. Summing over i gives the
#   answer.
#
#   NOTE : monotonicity is what makes this valid — dropping characters from
#          the left can never CREATE a duplicate, so the maximal valid left
#          boundary never moves backwards.
#
#   NOTE : `last[c]` may point BEFORE the current left (an older occurrence
#          already evicted), hence the max() — left must never rewind.
#
#   NOTE : the answer can be ~n^2/2 = 5 * 10^9 for n = 10^5, which overflows
#          32-bit ints in other languages. Python ints are unbounded, so no
#          special handling is needed here.
#
# time = O(n), space = O(C) with C = 26
class Solution(object):
    def numberOfSpecialSubstrings(self, s):
        last = {}
        res = 0
        left = 0
        for i, c in enumerate(s):
            if c in last and last[c] >= left:
                left = last[c] + 1
            last[c] = i
            res += i - left + 1
        return res
