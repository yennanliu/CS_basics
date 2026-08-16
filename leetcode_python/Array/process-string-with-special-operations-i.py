"""

3612. Process String with Special Operations I
Medium

You are given a string s consisting of lowercase English letters and the special
characters: *, #, and %.

Build a new string result by processing s according to the following rules from
left to right:

If the letter is a lowercase English letter append it to result.
A '*' removes the last character from result, if it exists.
A '#' duplicates the current result and appends it to itself.
A '%' reverses the current result.

Return the final string result after processing all characters in s.


Example 1:

Input: s = "a#b%*"
Output: "ba"
Explanation:

i   s[i]   Operation                   Current result
0   'a'    Append 'a'                  "a"
1   '#'    Duplicate result            "aa"
2   'b'    Append 'b'                  "aab"
3   '%'    Reverse result              "baa"
4   '*'    Remove the last character   "ba"

Thus, the final result is "ba".

Example 2:

Input: s = "z*#"
Output: ""
Explanation:

i   s[i]   Operation                   Current result
0   'z'    Append 'z'                  "z"
1   '*'    Remove the last character   ""
2   '#'    Duplicate the string        ""

Thus, the final result is "".


Constraints:

1 <= s.length <= 20
s consists of only lowercase English letters and special characters *, #, and %.
"""

# V0
# IDEA : DIRECT SIMULATION ON A CHARACTER LIST
#
#   nothing here needs to be outsmarted: the operations are applied strictly
#   left to right and each one is a total function of the current result, so
#   replaying them literally is already the definition of the answer.
#
#   the only thing worth checking is that the size stays sane. '#' is the sole
#   growth operator and it doubles, so with n <= 20 the result can never exceed
#   2^20 characters — small enough that the naive doubling, reversal and pop are
#   all affordable. (the sequel, LC 3614, raises the bound and forces the
#   backwards index-chasing approach instead.)
#
#   the '*' rule is guarded rather than exceptional: popping an empty result is
#   defined as a no-op, so an unmatched '*' is silently absorbed.
#
# time = O(2^n), space = O(2^n)
class Solution(object):
    def processStr(self, s):
        res = []
        for c in s:
            if c == '*':
                if res:
                    res.pop()
            elif c == '#':
                res.extend(res)
            elif c == '%':
                res.reverse()
            else:
                res.append(c)
        return ''.join(res)
