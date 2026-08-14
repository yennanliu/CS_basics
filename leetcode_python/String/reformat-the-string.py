"""

1417. Reformat The String
Easy

You are given an alphanumeric string s. (Alphanumeric string is a string consisting of lowercase English letters and digits).

You have to find a permutation of the string where no letter is followed by another letter and no digit is followed by another digit. That is, no two adjacent characters have the same type.

Return the reformatted string or return an empty string if it is impossible to reformat the string.


Example 1:

Input: s = "a0b1c2"
Output: "0a1b2c"
Explanation: No two adjacent characters have the same type in "0a1b2c". "a0b1c2", "0a1b2c", "0c2a1b" are also valid permutations.

Example 2:

Input: s = "leetcode"
Output: ""
Explanation: "leetcode" has only characters so we cannot separate them by digits.

Example 3:

Input: s = "1229857369"
Output: ""
Explanation: "1229857369" has only digits so we cannot separate them by characters.


Constraints:

1 <= s.length <= 500
s consists of only lowercase English letters and/or digits.

"""

# V0
# IDEA : SPLIT BY TYPE + INTERLEAVE (the longer group must start)
#
#   split s into letters and digits. an alternating arrangement exists iff
#   the two group sizes differ by at most 1, because each group's items
#   must sit in every other slot.
#   put the LONGER group first, then zip the two together -> the extra
#   character (if any) naturally lands at the end.
#
# time = O(n), space = O(n)
class Solution(object):
    def reformat(self, s):
        a = [c for c in s if c.isalpha()]
        b = [c for c in s if c.isdigit()]
        if abs(len(a) - len(b)) > 1:
            return ""
        if len(a) < len(b):
            a, b = b, a               # a is now the longer (or equal) group

        res = []
        for i in range(len(b)):
            res.append(a[i])
            res.append(b[i])
        if len(a) > len(b):
            res.append(a[-1])
        return "".join(res)
