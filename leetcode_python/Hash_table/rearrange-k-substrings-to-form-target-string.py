"""

3365. Rearrange K Substrings to Form Target String
Medium

You are given two strings s and t, both of which are anagrams of each other, and an integer k.

Your task is to determine whether it is possible to split the string s into k equal-sized substrings, rearrange the substrings, and concatenate them in any order to create a new string equal to t.

Return true if this is possible, otherwise, return false.


Example 1:

Input: s = "abcd", t = "cdab", k = 2
Output: true
Explanation:
Split s into 2 substrings of length 2: ["ab", "cd"].
Rearranging these substrings as ["cd", "ab"], and connecting them results in "cdab", which matches t.

Example 2:

Input: s = "aabbcc", t = "bbaacc", k = 3
Output: true
Explanation:
Split s into 3 substrings of length 2: ["aa", "bb", "cc"].
Rearranging these substrings as ["bb", "aa", "cc"], and connecting them results in "bbaacc", which matches t.

Example 3:

Input: s = "aabbcc", t = "bbaacc", k = 2
Output: false
Explanation:
Split s into 2 substrings of length 3: ["aab", "bcc"].
These substrings cannot be rearranged to form t = "bbaacc", so the output is false.


Constraints:

1 <= s.length == t.length <= 2 * 10^5
1 <= k <= s.length
s.length is divisible by k.
s and t consist only of lowercase English letters.

"""

# V0
# IDEA : THE BLOCKS ARE ATOMIC — SO COMPARE THE TWO MULTISETS OF BLOCKS
#
#   the split points are fixed (k equal pieces) and the pieces are never
#   altered, only reordered. so t is reachable exactly when cutting t at the
#   same offsets yields the SAME MULTISET of pieces as cutting s.
#
#   counting the blocks with a Counter compares the multisets directly.
#
# time = O(n), space = O(n)
from collections import Counter


class Solution(object):
    def isPossibleToRearrange(self, s, t, k):
        n = len(s)
        size = n // k
        a = Counter(s[i:i + size] for i in range(0, n, size))
        b = Counter(t[i:i + size] for i in range(0, n, size))
        return a == b
