"""

1016. Binary String With Substrings Representing 1 To N
Medium

Given a binary string s and a positive integer n, return true if the binary representation of all the integers in the range [1, n] are substrings of s, or false otherwise.

A substring is a contiguous sequence of characters within a string.


Example 1:

Input: s = "0110", n = 3
Output: true

Example 2:

Input: s = "0110", n = 4
Output: false


Constraints:

1 <= s.length <= 1000
s[i] is either '0' or '1'.
1 <= n <= 10^9

"""

# V0
# IDEA : ENUMERATE the SUBSTRINGS, not the numbers
#
#   n can be up to 10^9, so looping 1..n is impossible.
#   but s has at most 1000 chars, and a binary value <= 10^9 fits in 30 bits,
#   so s holds at most 1000 * 30 = 30000 useful substrings.
#
#   -> collect every substring that starts with '1' (no leading zeros)
#      and whose value is <= n. since all collected values live in [1, n],
#      "all of 1..n are present" is simply len(seen) == n.
#
# time = O(L * 30), L = len(s)
# space = O(L * 30)
class Solution(object):
    def queryString(self, s, n):
        L = len(s)
        seen = set()
        for i in range(L):
            if s[i] == '0':
                continue
            v = 0
            for j in range(i, L):
                v = v * 2 + (1 if s[j] == '1' else 0)
                if v > n:
                    break
                seen.add(v)
        return len(seen) == n


# V1
# IDEA : ONLY CHECK THE UPPER HALF (n//2, n]
#
#   if k <= n/2 then 2k <= n, and bin(2k) == bin(k) + "0",
#   so bin(k) is a substring of bin(2k). checking the upper half is enough.
#   (still needs the len(s) guard, since s can only contain
#    O(len(s)) distinct substrings of a given length)
# time = O(n * log n) worst case
# space = O(1)
class Solution(object):
    def queryString(self, s, n):
        # a string of length L has < L substrings of each length,
        # so it can never cover a big n
        if n > 2 * len(s) * 31:
            return False
        for i in range(n, n // 2, -1):
            if bin(i)[2:] not in s:
                return False
        return True
