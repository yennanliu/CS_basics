"""

3639. Minimum Time to Activate String
Medium

You are given a string s of length n and an integer array order, where order
is a permutation of the numbers in the range [0, n - 1].

Starting from time t = 0, replace the character at index order[t] in s with
'*' at each time step.

A substring is valid if it contains at least one '*'.

A string is active if the total number of valid substrings is greater than
or equal to k.

Return the minimum time t at which the string s becomes active. If it is
impossible, return -1.

Example 1:

Input: s = "abc", order = [1,0,2], k = 2
Output: 0
Explanation:
t | order[t] | Modified s | Valid Substrings | Count | Active
(Count >= k)
0 | 1 | "a*c" | "*", "a*", "*c", "a*c" | 4 | Yes
The string s becomes active at t = 0. Thus, the answer is 0.

Example 2:

Input: s = "cat", order = [0,2,1], k = 6
Output: 2
Explanation:
t | order[t] | Modified s | Valid Substrings | Count | Active
(Count >= k)
0 | 0 | "*at" | "*", "*a", "*at" | 3 | No
1 | 2 | "*a*" | "*", "*a", "*a*", "a*", "*" | 5 | No
2 | 1 | "***" | All substrings (contain '*') | 6 | Yes
The string s becomes active at t = 2. Thus, the answer is 2.

Example 3:

Input: s = "xy", order = [0,1], k = 4
Output: -1
Explanation:
Even after all replacements, it is impossible to obtain k = 4 valid
substrings. Thus, the answer is -1.

Constraints:

1 <= n == s.length <= 10^5
order.length == n
0 <= order[i] <= n - 1
s consists of lowercase English letters.
order is a permutation of integers from 0 to n - 1.
1 <= k <= 10^9

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
