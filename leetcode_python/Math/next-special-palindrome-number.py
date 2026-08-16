"""

3646. Next Special Palindrome Number
Hard

You are given an integer n.

A number is called special if:

It is a palindrome.
Every digit d that appears in the number occurs exactly d times.

Return the smallest special number strictly greater than n.


Example 1:

Input: n = 2
Output: 22
Explanation:
22 is a palindrome, and it contains only the digit 2, which occurs exactly 2 times. So 22 is special, and it is the smallest special number greater than 2.

Example 2:

Input: n = 33
Output: 212
Explanation:
212 is a palindrome. The digit 2 occurs exactly 2 times and the digit 1 occurs exactly 1 time, so 212 is special. There is no special number in the range (33, 212).


Constraints:

0 <= n < 10^15

"""

# V0
# IDEA : ENUMERATE EVERY SPECIAL NUMBER ONCE, THEN BINARY SEARCH
#
#   a special number is completely described by the SET of digits it uses,
#   because digit d must appear exactly d times. so the length is just the
#   sum of the chosen set, and 0 can never appear (it would need 0 copies).
#
#   the palindrome constraint prunes hard: in a palindrome at most one digit
#   may have an odd multiplicity (the one sitting in the middle), so at most
#   one odd digit may be chosen. everything else is mirrored, meaning the
#   number is determined by an arbitrary arrangement of the "half" multiset
#   (d//2 copies of each chosen d) plus the optional middle digit.
#
#   n < 10^15 so the answer has at most 16 digits; the set {2,6,8} already
#   yields 16-digit specials above 10^15, so capping the length at 16 keeps
#   the enumeration complete while leaving only a few tens of thousands of
#   candidates.
#
# time = O(C log C) where C ~ 1e5 candidates, space = O(C)
from itertools import permutations
import bisect


def _build_specials(max_len=16):
    out = set()
    for mask in range(1, 1 << 9):
        chosen = [d for d in range(1, 10) if mask >> (d - 1) & 1]
        length = sum(chosen)
        if length > max_len:
            continue
        odds = [d for d in chosen if d % 2]
        if len(odds) > 1:          # a palindrome allows one odd count at most
            continue
        half = []
        for d in chosen:
            half.extend([d] * (d // 2))
        mid = str(odds[0]) if odds else ''
        for perm in set(permutations(half)):
            left = ''.join(map(str, perm))
            out.add(int(left + mid + left[::-1]))
    return sorted(out)


_SPECIALS = None


class Solution(object):
    def specialPalindrome(self, n):
        global _SPECIALS
        if _SPECIALS is None:
            _SPECIALS = _build_specials()
        i = bisect.bisect_right(_SPECIALS, n)
        return _SPECIALS[i]
