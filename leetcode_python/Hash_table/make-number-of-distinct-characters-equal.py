"""

2531. Make Number of Distinct Characters Equal
Medium

You are given two 0-indexed strings word1 and word2.

A move consists of choosing two indices i and j such that 0 <= i < word1.length and 0 <= j < word2.length and swapping word1[i] with word2[j].

Return true if it is possible to get the number of distinct characters in word1 and word2 to be equal with exactly one move. Return false otherwise.


Example 1:

Input: word1 = "ac", word2 = "b"
Output: false
Explanation: Any pair of swaps would yield two distinct characters in the first string, and one in the second string.

Example 2:

Input: word1 = "abcc", word2 = "aab"
Output: true
Explanation: We swap index 2 of the first string with index 0 of the second string. The resulting strings are word1 = "abac" and word2 = "cab", which both have 3 distinct characters.

Example 3:

Input: word1 = "abcde", word2 = "fghij"
Output: true
Explanation: Both resulting strings will have 5 distinct characters, regardless of which indices we swap.


Constraints:

1 <= word1.length, word2.length <= 10^5
word1 and word2 consist of only lowercase English letters.

"""

# V0
# IDEA : COUNTING + ENUMERATE THE 26 x 26 CHARACTER PAIRS
#
#   a swap only depends on WHICH two characters are exchanged, not on which
#   positions, so there are at most 26 * 26 distinct moves to try.
#
#   swapping c1 (out of word1) with c2 (into word1):
#     - word1 loses a distinct char iff cnt1[c1] == 1 (c1 was unique there)
#     - word1 gains a distinct char iff cnt1[c2] == 0 (c2 was absent)
#     - symmetrically for word2.
#
#   NOTE : c1 == c2 is a LEGAL move (the strings are unchanged), so in that
#          case we simply check whether the counts are already equal.
#   NOTE : the "lose" test must be evaluated BEFORE the "gain" test conceptually,
#          but since c1 != c2 in that branch the two tests are independent.
#
# time = O(m + n + 26^2), space = O(26)
from collections import Counter


class Solution(object):
    def isItPossible(self, word1, word2):
        cnt1 = Counter(word1)
        cnt2 = Counter(word2)
        x, y = len(cnt1), len(cnt2)
        for c1 in list(cnt1.keys()):
            for c2 in list(cnt2.keys()):
                if c1 == c2:
                    if x == y:
                        return True
                else:
                    a = x - (1 if cnt1[c1] == 1 else 0) + (1 if cnt1[c2] == 0 else 0)
                    b = y - (1 if cnt2[c2] == 1 else 0) + (1 if cnt2[c1] == 0 else 0)
                    if a == b:
                        return True
        return False
