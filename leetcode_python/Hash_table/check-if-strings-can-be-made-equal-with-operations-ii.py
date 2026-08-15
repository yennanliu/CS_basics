"""

2840. Check if Strings Can be Made Equal With Operations II
Medium

You are given two strings s1 and s2, both of length n, consisting of lowercase English letters.

You can apply the following operation on any of the two strings any number of times:

- Choose any two indices i and j such that i < j and the difference j - i is even, then swap the two characters at those indices in the string.

Return true if you can make the strings s1 and s2 equal, and false otherwise.


Example 1:

Input: s1 = "abcdba", s2 = "cabdab"
Output: true
Explanation: We can apply the following operations on s1:
- Choose the indices i = 0, j = 2. The resulting string is s1 = "cbadba".
- Choose the indices i = 2, j = 4. The resulting string is s1 = "cbbdaa".
- Choose the indices i = 1, j = 5. The resulting string is s1 = "cabdab" = s2.

Example 2:

Input: s1 = "abe", s2 = "bea"
Output: false
Explanation: It is not possible to make the two strings equal.


Constraints:

n == s1.length == s2.length
1 <= n <= 10^5
s1 and s2 consist only of lowercase English letters.

"""

# V0
# IDEA : PARITY CLASSES + COUNTING (hash table over 2 x 26 counters)
#
#   j - i even <=> i and j have the SAME parity. so characters can be shuffled
#   arbitrarily among even indices, and arbitrarily among odd indices, but can
#   never cross between the two classes.
#
#   => the two strings are convertible iff the even-index multiset of s1 equals
#      the even-index multiset of s2, and likewise for the odd indices.
#
#   one pass with a 2 x 26 counter table: +1 for s1's char, -1 for s2's char at
#   the same parity bucket; everything must cancel to zero.
#
#   NOTE : within one parity class ANY permutation is reachable (adjacent
#          same-parity indices generate the whole symmetric group), so a plain
#          count comparison is enough - positions inside a class don't matter.
#
# time = O(n + 26), space = O(26)
class Solution(object):
    def checkStrings(self, s1, s2):
        if len(s1) != len(s2):
            return False
        cnt = [[0] * 26 for _ in range(2)]
        for i in range(len(s1)):
            cnt[i & 1][ord(s1[i]) - 97] += 1
            cnt[i & 1][ord(s2[i]) - 97] -= 1
        return all(v == 0 for row in cnt for v in row)
