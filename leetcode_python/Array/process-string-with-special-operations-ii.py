"""

3614. Process String with Special Operations II
Hard

You are given a string s consisting of lowercase English letters and the
special characters: '*', '#', and '%'.

You are also given an integer k.

Build a new string result by processing s according to the following rules from
left to right:

If the letter is a lowercase English letter append it to result.
A '*' removes the last character from result, if it exists.
A '#' duplicates the current result and appends it to itself.
A '%' reverses the current result.

Return the kth character of the final string result. If k is out of the bounds
of result, return '.'.


Example 1:

Input: s = "a#b%*", k = 1
Output: "a"
Explanation:

i    s[i]   Operation                     Current result
0    'a'    Append 'a'                    "a"
1    '#'    Duplicate result              "aa"
2    'b'    Append 'b'                    "aab"
3    '%'    Reverse result                "baa"
4    '*'    Remove the last character     "ba"

The final result is "ba". The character at index k = 1 is 'a'.

Example 2:

Input: s = "cd%#*#", k = 3
Output: "d"
Explanation:

i    s[i]   Operation                     Current result
0    'c'    Append 'c'                    "c"
1    'd'    Append 'd'                    "cd"
2    '%'    Reverse result                "dc"
3    '#'    Duplicate result              "dcdc"
4    '*'    Remove the last character     "dcd"
5    '#'    Duplicate result              "dcddcd"

The final result is "dcddcd". The character at index k = 3 is 'd'.

Example 3:

Input: s = "z*#", k = 0
Output: "."
Explanation:

i    s[i]   Operation                     Current result
0    'z'    Append 'z'                    "z"
1    '*'    Remove the last character     ""
2    '#'    Duplicate the string          ""

The final result is "". Since index k = 0 is out of bounds, the output is '.'.


Constraints:

1 <= s.length <= 10^5
s consists of only lowercase English letters and special characters '*', '#',
and '%'.
0 <= k <= 10^15
The length of result after processing s will not exceed 10^15.

"""

# V0
# IDEA : LENGTH SWEEP FORWARD, THEN WALK THE INDEX BACKWARDS
#
#   the string itself can reach 10^15 characters, so it can never be built.
#   but the *length* after each operation is cheap to track forward, and once
#   the lengths are known every operation can be inverted on a single index.
#
#   run forward once to get the final length m; if k >= m the answer is '.'.
#   then walk the operations in reverse, carrying (k, m) = "the character we
#   want sits at index k of the string of length m that exists at this point".
#   each op is undone locally:
#
#     '#' : the string was the first half doubled, so the previous length is
#           m/2 and an index in the second half maps back to k - m/2 in the
#           first half — the two halves are identical, so either origin is
#           equally valid.
#     '%' : reversal is its own inverse on positions, k -> m - 1 - k.
#     '*' : the previous string was one longer, and our index survived the
#           deletion untouched (only the tail was dropped).
#     letter : the previous string was one shorter; if k lands exactly on the
#           newly appended slot, that letter is the answer, otherwise the
#           index is unaffected.
#
#   the invariant k < m holds at every step, which is also why undoing '*'
#   never needs a special case for a deletion on an empty string: such a step
#   leaves m = 0, and no valid index can be pointing into it.
#
# time = O(n), space = O(1)
class Solution(object):
    def processStr(self, s, k):
        m = 0
        for c in s:
            if c == "*":
                if m > 0:
                    m -= 1
            elif c == "#":
                m *= 2
            elif c != "%":
                m += 1

        if k >= m:
            return "."

        for i in range(len(s) - 1, -1, -1):
            c = s[i]
            if c == "*":
                m += 1
            elif c == "#":
                m //= 2
                if k >= m:
                    k -= m
            elif c == "%":
                k = m - 1 - k
            else:
                m -= 1
                if k == m:
                    return c
        return "."
