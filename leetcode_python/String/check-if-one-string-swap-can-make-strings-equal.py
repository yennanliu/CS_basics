"""

1790. Check if One String Swap Can Make Strings Equal
Easy

You are given two strings s1 and s2 of equal length. A string swap is an operation where you choose two indices in a string (not necessarily different) and swap the characters at these indices.

Return true if it is possible to make both strings equal by performing at most one string swap on exactly one of the strings. Otherwise, return false.

Example 1:

Input: s1 = "bank", s2 = "kanb"
Output: true
Explanation: For example, swap the first character with the last character of s2 to make "bank".

Example 2:

Input: s1 = "attack", s2 = "defend"
Output: false
Explanation: It is impossible to make them equal with one string swap.

Example 3:

Input: s1 = "kelb", s2 = "kelb"
Output: true
Explanation: The two strings are already equal, so no string swap operation is required.

Constraints:

1 <= s1.length, s2.length <= 100
s1.length == s2.length
s1 and s2 consist of only lowercase English letters.

"""

# V0
# IDEA : COLLECT THE MISMATCHING POSITIONS (there must be exactly 0 or 2)
#
#   one swap can repair at most two positions, and it repairs exactly two only
#   when they are each other's mirror : s1[i] == s2[j] and s1[j] == s2[i].
#   so:
#     - 0 mismatches -> already equal, True (the swap is optional / a no-op)
#     - 2 mismatches -> True iff the crossed characters match
#     - anything else -> False
#
# time = O(n), space = O(1)
class Solution(object):
    def areAlmostEqual(self, s1, s2):
        diff = []
        for i in range(len(s1)):
            if s1[i] != s2[i]:
                diff.append(i)
                if len(diff) > 2:
                    return False
        if not diff:
            return True
        if len(diff) != 2:
            return False
        i, j = diff
        return s1[i] == s2[j] and s1[j] == s2[i]
