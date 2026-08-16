"""

3398. Smallest Substring With Identical Characters I
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

1 <= n == s.length <= 1000
s consists only of '0' and '1'.
0 <= numOps <= n

"""

# V0
# IDEA : THE ANSWER IS MONOTONE — TEST EACH CANDIDATE LENGTH DIRECTLY
#
#   "can we get every identical run down to length <= m with at most numOps
#   flips?" gets easier as m grows, so the smallest feasible m is the answer and
#   we may test candidates one by one (n <= 1000 here).
#
#   for m >= 2 the runs are independent: a maximal run of L equal characters
#   needs f flips where the longest surviving piece is ceil((L - f)/(f + 1)), so
#   f >= (L - m)/(m + 1) and the cheapest is exactly floor(L / (m + 1)).  the
#   flipped characters can always be placed strictly inside the run, so they sit
#   between two opposite characters and never merge with a neighbouring run,
#   which is why the runs really do stay independent.
#
#   m == 1 is different: the whole string must alternate, and there are only two
#   such strings ("0101..." and "1010...").  their mismatch counts add up to n,
#   so the cost is min(c, n - c).
#
# time = O(n^2) worst case, space = O(1)
class Solution(object):
    def minLength(self, s, numOps):
        n = len(s)

        # cost of forcing a perfect alternation
        c = sum(1 for i, ch in enumerate(s) if (ord(ch) - 48) != (i & 1))
        if min(c, n - c) <= numOps:
            return 1

        for m in range(2, n + 1):
            need = 0
            i = 0
            while i < n:
                j = i
                while j < n and s[j] == s[i]:
                    j += 1
                need += (j - i) // (m + 1)
                if need > numOps:
                    break
                i = j
            if need <= numOps:
                return m
        return n
