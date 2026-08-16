"""

3639. Minimum Time to Activate String
Medium

You are given a string s of length n and an integer array order, which is a permutation of the numbers in the range [0, n - 1].

Starting from time t = 0, at each time step you replace the character at index order[t] in s with '*'.

A string is called active if the number of substrings that contain at least one '*' is at least k.

Return the minimum time t at which s becomes active. If it is impossible, return -1.

A substring is a contiguous (non-empty) sequence of characters within a string.


Example 1:

Input: s = "abc", order = [0,1,2], k = 3
Output: 0
Explanation:
At t = 0, s becomes "*bc". The substrings that contain at least one '*' are "*", "*b" and "*bc", so there are 3 of them, which is at least k = 3.
Hence, the answer is 0.

Example 2:

Input: s = "cat", order = [0,2,1], k = 6
Output: 2
Explanation:
At t = 0, s becomes "*at" with 3 substrings containing '*'.
At t = 1, s becomes "*a*" with 5 substrings containing '*'.
At t = 2, s becomes "***" with 6 substrings containing '*', which is at least k = 6.
Hence, the answer is 2.


Constraints:

1 <= n == s.length == order.length <= 10^5
0 <= order[i] <= n - 1
order is a permutation of [0, n - 1].
1 <= k <= 10^18

"""

# V0
# IDEA : BINARY SEARCH ON TIME + COMPLEMENT COUNT OF STARRED SUBSTRINGS
#
#   the number of substrings containing a '*' never decreases as time passes,
#   because stars are only ever added. that monotonicity is what makes binary
#   search on t legal.
#
#   counting directly is awkward, so count the complement: substrings with NO
#   star are exactly the substrings of the maximal star-free gaps. if the gaps
#   between (and around) the starred positions have lengths g1, g2, ..., then
#       answer(t) = n*(n+1)/2 - sum gi*(gi+1)/2
#
#   for a candidate t the starred positions are order[0..t]; sorting them is
#   avoided by marking a boolean array and sweeping once, so each check is
#   O(n) and the whole thing is O(n log n).
#
# time = O(n log n), space = O(n)
class Solution(object):
    def minTime(self, s, order, k):
        n = len(s)
        total = n * (n + 1) // 2
        if total < k:
            return -1

        starred = [False] * n

        def count_with_star(t):
            # positions order[0..t] are stars
            for i in range(t + 1):
                starred[order[i]] = True
            gaps = total
            run = 0
            for i in range(n):
                if starred[i]:
                    gaps -= run * (run + 1) // 2
                    run = 0
                else:
                    run += 1
            gaps -= run * (run + 1) // 2
            for i in range(t + 1):
                starred[order[i]] = False
            return gaps

        lo, hi = 0, n - 1
        while lo < hi:
            mid = (lo + hi) // 2
            if count_with_star(mid) >= k:
                hi = mid
            else:
                lo = mid + 1
        return lo
