"""

3399. Smallest Substring With Identical Characters II
Hard

You are given a binary string s of length n and an integer numOps.

You are allowed to perform the following operation on s at most numOps times:

Select any index i (where 0 <= i < n) and flip s[i]. If s[i] == '1', change s[i]
to '0' and vice versa.

You need to minimize the length of the longest substring of s such that all the
characters in the substring are identical.

Return the minimum length after the operations.

Example 1:

Input: s = "000001", numOps = 1

Output: 2

Explanation:

By changing s[2] to '1', s becomes "001001". The longest substrings with
identical characters are s[0..1] and s[3..4].

Example 2:

Input: s = "0000", numOps = 2

Output: 1

Explanation:

By changing s[0] and s[2] to '1', s becomes "1010".

Example 3:

Input: s = "0101", numOps = 0

Output: 1

Constraints:

1 <= n == s.length <= 10^5
s consists only of '0' and '1'.
0 <= numOps <= n

"""

# V0
# IDEA : BINARY SEARCH THE ANSWER, GREEDY FEASIBILITY CHECK
#
#   feasibility is monotone in the target length m — a plan that keeps every run
#   at most m long also keeps it at most m + 1 long — so binary search over
#   m in [1, n] and return the smallest feasible one.  that is the only change
#   from part I; with n up to 1e5 the linear scan of candidates is too slow.
#
#   checking a candidate m >= 2: split s into maximal runs.  breaking a run of
#   length L into pieces of length <= m needs f flips, and with f well-placed
#   flips the longest piece is ceil((L - f)/(f + 1)); solving for f gives the
#   minimum floor(L / (m + 1)).  the flips go strictly inside the run so each
#   flipped character is boxed in by two opposite characters and cannot glue
#   itself to the neighbouring run — the runs stay independent and the costs
#   simply add.
#
#   m == 1 has to be handled apart, since a flipped character would then be a
#   run of length 1 next to other length-1 runs: the string must alternate
#   outright, and only two alternating strings exist.  their mismatch counts sum
#   to n, so the cost is min(c, n - c).
#
# time = O(n log n), space = O(1)
class Solution(object):
    def minLength(self, s, numOps):
        n = len(s)

        c = sum(1 for i, ch in enumerate(s) if (ord(ch) - 48) != (i & 1))
        if min(c, n - c) <= numOps:
            return 1

        # runs are the same for every candidate, so measure them once
        runs = []
        i = 0
        while i < n:
            j = i
            while j < n and s[j] == s[i]:
                j += 1
            runs.append(j - i)
            i = j

        def ok(m):
            need = 0
            d = m + 1
            for L in runs:
                need += L // d
                if need > numOps:
                    return False
            return True

        lo, hi = 2, n
        while lo < hi:
            mid = (lo + hi) // 2
            if ok(mid):
                hi = mid
            else:
                lo = mid + 1
        return lo
