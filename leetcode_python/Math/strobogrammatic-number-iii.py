"""

248. Strobogrammatic Number III
Hard
(premium)

Given two strings low and high that represent two integers low and high where low <= high,
return the number of strobogrammatic numbers in the range [low, high].

A strobogrammatic number is a number that looks the same when rotated 180 degrees
(looked at upside down).


Example 1:

Input: low = "50", high = "100"
Output: 3

Example 2:

Input: low = "0", high = "0"
Output: 1


Constraints:

1 <= low.length, high.length <= 15
low and high consist of only digits.
low <= high
low and high do not contain any leading zeros except for zero itself.

"""

# V0
# IDEA : RECURSIVE CONSTRUCTION (build outward from the middle), then filter by range
#
#  A strobogrammatic number is built by wrapping a strobogrammatic core with one of the
#  self-rotating pairs: (0,0) (1,1) (6,9) (8,8) (9,6).
#
#  Base cases:
#    - length 0 -> [""]        (even-length core)
#    - length 1 -> ["0","1","8"]  (odd-length core, only self-rotating single digits)
#
#  KEY DETAIL: the '0','0' pair is forbidden at the OUTERMOST layer (u == n), because
#  that would produce a leading zero.
#
# time  = O(5^(L/2) * L), L = len(high)
# space = O(5^(L/2) * L)
class Solution(object):
    def strobogrammaticInRange(self, low, high):
        pairs = [("0", "0"), ("1", "1"), ("6", "9"), ("8", "8"), ("9", "6")]

        def build(u, n):
            """all strobogrammatic strings of length u, sitting inside a number of length n"""
            if u == 0:
                return [""]
            if u == 1:
                return ["0", "1", "8"]

            res = []
            for inner in build(u - 2, n):
                for a, b in pairs:
                    # outermost layer -> no leading zero
                    if a == "0" and u == n:
                        continue
                    res.append(a + inner + b)
            return res

        lo, hi = int(low), int(high)
        ans = 0
        # only lengths between len(low) and len(high) can possibly land in range
        for n in range(len(low), len(high) + 1):
            for cand in build(n, n):
                if lo <= int(cand) <= hi:
                    ans += 1
        return ans
