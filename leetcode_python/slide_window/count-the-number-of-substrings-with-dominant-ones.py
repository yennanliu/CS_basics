"""

3234. Count the Number of Substrings With Dominant Ones
Medium

You are given a binary string s.

Return the number of substrings with dominant ones.

A string has dominant ones if the number of ones in the string is greater than or equal to the square of the number of zeros in the string.


Example 1:

Input: s = "00011"
Output: 5
Explanation:
The substrings with dominant ones are shown in the table below.

Example 2:

Input: s = "101101"
Output: 16
Explanation:
The substrings with non-dominant ones are shown in the table below.


Constraints:

1 <= s.length <= 4 * 10^4
s consists only of characters '0' and '1'.

"""

# V0
# IDEA : THE ZERO COUNT IS BOUNDED BY sqrt(n) — ENUMERATE IT
#
#   a substring with z zeros needs at least z^2 ones, so its length is at
#   least z^2 + z. with n <= 4*10^4 that caps z at about 200 — far fewer
#   possibilities than the O(n^2) substrings.
#
#   so for each start index, walk the possible zero counts z. the substrings
#   holding exactly z zeros end in a contiguous window : from the z-th zero
#   at or after the start, up to just before the (z+1)-th. intersecting that
#   window with the length requirement
#       end >= start + z^2 + z - 1
#   gives the count for this (start, z) in O(1).
#
# time = O(n * sqrt(n)), space = O(n)
import bisect


class Solution(object):
    def numberOfSubstrings(self, s):
        n = len(s)
        zeros = [i for i, ch in enumerate(s) if ch == '0']
        res = 0

        for start in range(n):
            k = bisect.bisect_left(zeros, start)        # first zero at or after start
            z = 0
            while True:
                if z * z + z > n:                       # even the whole string is short
                    break
                if z == 0:
                    lo = start
                    hi = (zeros[k] - 1) if k < len(zeros) else n - 1
                else:
                    if k + z - 1 >= len(zeros):
                        break                           # not that many zeros left
                    lo = zeros[k + z - 1]
                    hi = (zeros[k + z] - 1) if k + z < len(zeros) else n - 1
                need = start + z * z + z - 1            # minimum end index
                lo = max(lo, need)
                if hi >= lo:
                    res += hi - lo + 1
                z += 1
        return res
