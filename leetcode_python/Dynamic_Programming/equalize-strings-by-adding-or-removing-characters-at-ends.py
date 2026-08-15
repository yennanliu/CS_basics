"""

3135. Equalize Strings by Adding or Removing Characters at Ends
Medium
🔒 (premium)

You are given two strings initial and target. In one operation, you can add or remove one character only at the beginning or the end of the string initial.

Return the minimum number of operations required to transform the string initial into the string target.


Example 1:

Input: initial = "abcde", target = "cdef"
Output: 3
Explanation:
Remove 'a' and 'b' from the beginning of initial, then add 'f' to the end.

Example 2:

Input: initial = "axxy", target = "yabx"
Output: 6
Explanation:
Remove "xxy" from initial, then add "yab" — 3 removals and 3 additions.

Example 3:

Input: initial = "xyz", target = "xyz"
Output: 0
Explanation:
No operations are needed.


Constraints:

1 <= initial.length, target.length <= 1000
initial and target consist only of lowercase English letters.

"""

# V0
# IDEA : ONLY A COMMON *SUBSTRING* CAN SURVIVE — SO MAXIMISE ITS LENGTH
#
#   every operation touches an end, so whatever is kept from `initial` is a
#   contiguous block, and it must appear contiguously in `target` too. once
#   that block is chosen the cost is forced :
#
#       (len(initial) - L) removals  +  (len(target) - L) additions
#
#   so the answer is minimised by the LONGEST COMMON SUBSTRING length L,
#   which the classic O(n*m) table gives :
#       dp[i][j] = dp[i-1][j-1] + 1 when the characters match, else 0
#
#   NOTE : substring, not subsequence — the reset to 0 on a mismatch is what
#          enforces contiguity.
#
# time = O(n * m), space = O(m)
class Solution(object):
    def minOperations(self, initial, target):
        n, m = len(initial), len(target)
        prev = [0] * (m + 1)
        best = 0
        for i in range(1, n + 1):
            cur = [0] * (m + 1)
            for j in range(1, m + 1):
                if initial[i - 1] == target[j - 1]:
                    cur[j] = prev[j - 1] + 1
                    if cur[j] > best:
                        best = cur[j]
            prev = cur
        return (n - best) + (m - best)
