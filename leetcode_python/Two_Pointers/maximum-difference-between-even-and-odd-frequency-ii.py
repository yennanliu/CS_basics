"""

3445. Maximum Difference Between Even and Odd Frequency II
Hard

You are given a string s and an integer k. Your task is to find the maximum
difference between the frequency of two characters, freq[a] - freq[b], in a
substring subs of s, such that:

subs has a size of at least k.
Character a has an odd frequency in subs.
Character b has a non-zero even frequency in subs.

Return the maximum difference.

Note that subs can contain more than 2 distinct characters.

Example 1:

Input: s = "12233", k = 4

Output: -1

Explanation:

For the substring "12233", the frequency of '1' is 1 and the frequency of '3' is
2. The difference is 1 - 2 = -1.

Example 2:

Input: s = "1122211", k = 3

Output: 1

Explanation:

For the substring "11222", the frequency of '2' is 3 and the frequency of '1' is
2. The difference is 3 - 2 = 1.

Example 3:

Input: s = "110", k = 3

Output: -1

Constraints:

3 <= s.length <= 3 * 10^4
s consists only of digits '0' to '4'.
The input is generated that at least one substring has a character with an even
frequency and a character with an odd frequency.
1 <= k <= s.length

"""

# V0
# IDEA : PREFIX COUNTS + PARITY BUCKETS, WITH A SECOND POINTER FOR "NON-ZERO"
#
#   there are only 5 digits, so fix an ordered pair (a, b) and solve 20 small
#   problems.  with A and B the prefix counts, a substring (l, r] contributes
#     (A[r] - B[r]) - (A[l] - B[l]),
#   so for each r we want the *smallest* A[l] - B[l] over the legal l.  the
#   three conditions turn into:
#     - length >= k          ->  l <= r - k
#     - freq(a) odd          ->  parity(A[l]) != parity(A[r])
#     - freq(b) even, non-0  ->  parity(B[l]) == parity(B[r]) and B[l] < B[r]
#
#   the two parities give four buckets, each holding a running minimum, so the
#   first two conditions are handled by releasing index l into its bucket once
#   l <= r - k.
#
#   the "non-zero" condition is the subtle one, and it is what makes a second
#   pointer necessary: B is non-decreasing, so {l : B[l] < B[r]} is a prefix,
#   and it only ever grows as r grows.  hence one monotone pointer that releases
#   l while *both* l <= r - k and B[l] < B[r] hold is exactly right — it never
#   has to take an index back.
#
# time = O(20 * n), space = O(n)
class Solution(object):
    def maxDifference(self, S, k):
        n = len(S)
        D = 5
        pref = [[0] * (n + 1) for _ in range(D)]
        for i, c in enumerate(S):
            d = ord(c) - 48
            for t in range(D):
                pref[t][i + 1] = pref[t][i] + (1 if t == d else 0)

        INF = float('inf')
        ans = -INF
        for a in range(D):
            A = pref[a]
            for b in range(D):
                if a == b:
                    continue
                B = pref[b]
                best = [[INF, INF], [INF, INF]]
                q = 0
                for r in range(k, n + 1):
                    bound = r - k
                    br = B[r]
                    while q <= bound and B[q] < br:
                        v = A[q] - B[q]
                        pa = A[q] & 1
                        pb = B[q] & 1
                        if v < best[pa][pb]:
                            best[pa][pb] = v
                        q += 1
                    m = best[1 - (A[r] & 1)][br & 1]
                    if m < INF:
                        cand = (A[r] - br) - m
                        if cand > ans:
                            ans = cand
        return ans
