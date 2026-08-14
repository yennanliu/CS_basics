"""

1234. Replace the Substring for Balanced String
Medium

You are given a string s of length n containing only four kinds of characters:
'Q', 'W', 'E', and 'R'.

A string is said to be balanced if each of its characters appears n / 4 times where n is
the length of the string.

Return the minimum length of the substring that can be replaced with any other string of
the same length to make s balanced. If s is already balanced, return 0.

Example 1:

Input: s = "QWER"
Output: 0
Explanation: s is already balanced.

Example 2:

Input: s = "QQWE"
Output: 1
Explanation: We need to replace a 'Q' to 'R', so that "RQWE" (or "QRWE") is balanced.

Example 3:

Input: s = "QQQW"
Output: 2
Explanation: We can replace the first "QQ" to "ER".


Constraints:

n == s.length
4 <= n <= 10^5
n is a multiple of 4.
s contains only 'Q', 'W', 'E', and 'R'.

"""

# V0
# IDEA : SLIDING WINDOW / TWO POINTERS (shrinkable window)
"""
 KEY INSIGHT:
   a window [j..i] can be rewritten into anything, so it is a valid answer iff
   the characters OUTSIDE the window each occur at most n/4 times
   (whatever is missing can then be filled in inside the window).

 So: count everything, then slide the right edge i forward (removing s[i] from
 the "outside" counter) and greedily shrink from the left while the window is
 still valid, recording the smallest width.
"""
# time = O(n)
# space = O(1)
from collections import Counter
class Solution(object):
    def balancedString(self, s):
        n = len(s)
        k = n // 4
        cnt = Counter(s)

        # already balanced
        if all(cnt[c] <= k for c in "QWER"):
            return 0

        res = n
        j = 0
        for i, c in enumerate(s):
            # s[i] moves INTO the window -> no longer counted as "outside"
            cnt[c] -= 1
            while j <= i and all(cnt[x] <= k for x in "QWER"):
                res = min(res, i - j + 1)
                cnt[s[j]] += 1
                j += 1
        return res
