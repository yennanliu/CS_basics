"""

2839. Check if Strings Can be Made Equal With Operations I
Easy

You are given two strings s1 and s2, both of length 4, consisting of lowercase English letters.

You can apply the following operation on any of the two strings any number of times:

- Choose any two indices i and j such that j - i = 2, then swap the two characters at those indices in the string.

Return true if you can make the strings s1 and s2 equal, and false otherwise.


Example 1:

Input: s1 = "abcd", s2 = "cdab"
Output: true
Explanation: We can do the following operations on s1:
- Choose the indices i = 0, j = 2. The resulting string is s1 = "cbad".
- Choose the indices i = 1, j = 3. The resulting string is s1 = "cdab" = s2.

Example 2:

Input: s1 = "abcd", s2 = "dacb"
Output: false
Explanation: It is not possible to make the two strings equal.


Constraints:

s1.length == s2.length == 4
s1 and s2 consist only of lowercase English letters.

"""

# V0
# IDEA : PARITY CLASSES + COUNTING
#
#   a swap only ever links indices 2 apart, so a character can never move
#   between an even index and an odd index. the string therefore splits into
#   two independent groups: {0, 2} and {1, 3}.
#
#   inside a group any permutation is reachable (with length 4 each group has
#   just 2 slots, one swap flips them), so s1 and s2 match iff each group holds
#   the same MULTISET of characters.
#
#   NOTE : compare multisets, not sorted-vs-position - the order inside a group
#          is free, the group membership is not.
#
# time = O(1) (length is fixed at 4), space = O(1)
class Solution(object):
    def canBeEqual(self, s1, s2):
        return (sorted(s1[0::2]) == sorted(s2[0::2])
                and sorted(s1[1::2]) == sorted(s2[1::2]))
