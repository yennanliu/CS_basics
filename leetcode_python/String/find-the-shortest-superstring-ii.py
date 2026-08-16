"""

3571. Find the Shortest Superstring II
Easy

You are given two strings, s1 and s2. Return the shortest possible string
that contains both s1 and s2 as substrings. If there are multiple valid
answers, return any one of them.

A substring is a contiguous sequence of characters within a string.


Example 1:

Input: s1 = "aba", s2 = "bab"
Output: "abab"
Explanation:
"abab" is the shortest string that contains both "aba" and "bab" as
substrings.

Example 2:

Input: s1 = "aa", s2 = "aaa"
Output: "aaa"
Explanation:
"aa" is already contained within "aaa", so the shortest superstring is
"aaa".


Constraints:

1 <= s1.length <= 100
1 <= s2.length <= 100
s1 and s2 consist of lowercase English letters only.

"""

# V0
# IDEA : MAXIMUM PREFIX / SUFFIX OVERLAP
#
#   any string that contains both s1 and s2 pins down an occurrence of each,
#   and those two occurrences either nest (one inside the other) or stick
#   out on opposite ends. so the answer is always one of: the longer string
#   alone, s1 followed by s2, or s2 followed by s1 — and in the last two
#   cases the only freedom left is how far the two blocks are pushed into
#   each other, i.e. how much of one's suffix equals the other's prefix.
#
#   fixing m = min(len) and normalising so s1 is the shorter one, an overlap
#   of m - i characters (either orientation) yields a superstring of length
#   n + i. so sweeping i upward from 0 and returning the first orientation
#   that fits gives the minimum length directly — no need to compare the two
#   orientations, they tie at equal i and either is accepted.
#
#   the nesting case is exactly "s1 is a substring of s2", checked first.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def shortestSuperstring(self, s1, s2):
        # keep s1 as the shorter string; a superstring of the pair is
        # symmetric in the two arguments
        if len(s1) > len(s2):
            s1, s2 = s2, s1

        if s1 in s2:
            return s2

        m = len(s1)
        for i in range(m):
            # s1's suffix of length m - i is a prefix of s2
            if s2.startswith(s1[i:]):
                return s1[:i] + s2
            # s1's prefix of length m - i is a suffix of s2
            if s2.endswith(s1[:m - i]):
                return s2 + s1[m - i:]

        return s1 + s2
