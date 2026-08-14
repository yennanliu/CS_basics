"""

2565. Subsequence With the Minimum Score
Hard

You are given two strings s and t.

You are allowed to remove any number of characters from the string t.

The score of the string is 0 if no characters are removed from the string t, otherwise:

- Let left be the minimum index among all removed characters.
- Let right be the maximum index among all removed characters.

Then the score of the string is right - left + 1.

Return the minimum possible score to make t a subsequence of s.

A subsequence of a string is a new string that is formed from the original string by deleting some (can be none) of the characters without disturbing the relative positions of the remaining characters. (i.e., "ace" is a subsequence of "abcde" while "aec" is not).


Example 1:

Input: s = "abacaba", t = "bzaa"
Output: 1
Explanation: In this example, we remove the character "z" at index 1 (0-indexed).
The string t becomes "baa" which is a subsequence of the string "abacaba" and the score is 1 - 1 + 1 = 1.
It can be proven that 1 is the minimum score that we can achieve.

Example 2:

Input: s = "cde", t = "xyz"
Output: 3
Explanation: In this example, we remove characters "x", "y" and "z" at indices 0, 1, and 2 (0-indexed).
The string t becomes "" which is a subsequence of the string "cde" and the score is 2 - 0 + 1 = 3.
It can be proven that 3 is the minimum score that we can achieve.


Constraints:

1 <= s.length, t.length <= 10^5
s and t consist of only lowercase English letters.

"""

# V0
# IDEA : PREFIX / SUFFIX GREEDY MATCH + BINARY SEARCH ON THE WINDOW LENGTH
#
#   the score only depends on the leftmost and rightmost removed index, so
#   we may as well delete the WHOLE window t[k .. k+x-1] (deleting extra
#   characters inside the window is free). the task becomes: find the
#   smallest window length x such that some window can be cut out and the
#   remaining prefix + suffix of t is a subsequence of s.
#
#   precompute with two greedy passes:
#     f[j] = smallest index in s at which t[0..j] finishes matching
#            (inf if t[0..j] is not a subsequence of s at all)
#     g[j] = largest index in s at which t[j..] starts matching
#            (-1 if impossible)
#
#   a window [k, k+x-1] works iff  left_end < right_start, i.e.
#     (f[k-1] if k > 0 else -1)  <  (g[k+x] if k+x < n else m+1)
#   which is exactly "the prefix match and the suffix match do not overlap".
#
#   NOTE : feasibility is monotonic in x (a longer cut can always mimic a
#          shorter one), so binary search x over [0, n].
#   NOTE : sentinels matter — an empty prefix "ends" before index 0 (-1) and
#          an empty suffix "starts" after the end of s (m+1).
#   NOTE : |t| reaches 10^5, so the check is an iterative O(n) scan and the
#          whole thing stays O(n log n) with no recursion.
#
# time = O((m + n) + n log n), space = O(n)
class Solution(object):
    def minimumScore(self, s, t):
        m, n = len(s), len(t)
        INF = float('inf')
        f = [INF] * n
        g = [-1] * n

        i, j = 0, 0
        while i < m and j < n:
            if s[i] == t[j]:
                f[j] = i
                j += 1
            i += 1

        i, j = m - 1, n - 1
        while i >= 0 and j >= 0:
            if s[i] == t[j]:
                g[j] = i
                j -= 1
            i -= 1

        def can_cut(x):
            for k in range(n - x + 1):
                left = f[k - 1] if k > 0 else -1
                right = g[k + x] if k + x < n else m + 1
                if left < right:
                    return True
            return False

        lo, hi = 0, n
        while lo < hi:
            mid = (lo + hi) // 2
            if can_cut(mid):
                hi = mid
            else:
                lo = mid + 1
        return lo
