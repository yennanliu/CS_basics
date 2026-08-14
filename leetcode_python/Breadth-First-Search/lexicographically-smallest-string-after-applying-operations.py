"""

1625. Lexicographically Smallest String After Applying Operations
Medium

You are given a string s of even length consisting of digits from 0 to 9, and two integers a and b.

You can apply either of the following two operations any number of times and in any order on s:

- Add a to all odd indices of s (0-indexed). Digits post 9 are cycled back to 0. For example, if s = "3456" and a = 5, s becomes "3951".
- Rotate s to the right by b positions. For example, if s = "3456" and b = 1, s becomes "6345".

Return the lexicographically smallest string you can obtain by applying the above operations any number of times on s.

A string a is lexicographically smaller than a string b (of the same length) if in the first position where a and b differ, string a has a letter that appears earlier in the alphabet than the corresponding letter in b. For example, "0158" is lexicographically smaller than "0190" because the first position they differ is at the third letter, and '5' comes before '9'.


Example 1:

Input: s = "5525", a = 9, b = 2
Output: "2050"
Explanation: We can apply the following operations:
Start:  "5525"
Rotate: "2555"
Add:    "2454"
Add:    "2353"
Rotate: "5323"
Add:    "5222"
Add:    "5121"
Rotate: "2151"
Add:    "2050"
There is no way to obtain a string that is lexicographically smaller than "2050".

Example 2:

Input: s = "74", a = 5, b = 1
Output: "24"
Explanation: We can apply the following operations:
Start:  "74"
Rotate: "47"
Add:    "42"
Rotate: "24"
There is no way to obtain a string that is lexicographically smaller than "24".

Example 3:

Input: s = "0011", a = 4, b = 2
Output: "0011"
Explanation: There are no sequence of operations that will give us a lexicographically smaller string than "0011".


Constraints:

2 <= s.length <= 100
s.length is even.
s consists of digits from 0 to 9 only.
1 <= a <= 9
1 <= b <= s.length - 1

"""

# V0
# IDEA : ENUMERATE THE REACHABLE SET (rotations x independent digit shifts)
#
#   the two operations commute in effect, so any reachable string is
#     rotate the original right by some multiple of b, then
#     add i*a to the odd positions (i in 0..9, since 10a = 0 mod 10)
#
#   NOTE : if b is ODD, a rotation swaps index parities, so by rotating,
#          adding, and rotating back we can also add j*a to the EVEN
#          positions independently -> a second loop j in 0..9.
#          if b is even the even positions can never change.
#   NOTE : reachable rotations are the multiples of b mod n; walk
#          r = 0, b, 2b, ... until it cycles back.
#
# time = O(n^2 * 100) worst case, space = O(n)
class Solution(object):
    def findLexSmallestString(self, s, a, b):
        n = len(s)
        digits = [int(c) for c in s]
        even_shifts = range(10) if b % 2 == 1 else [0]

        res = None
        seen_rot = set()
        r = 0
        while r not in seen_rot:
            seen_rot.add(r)
            # rotate right by r
            rot = digits[n - r:] + digits[:n - r]
            for i in range(10):
                for j in even_shifts:
                    cand = []
                    for idx in range(n):
                        if idx % 2 == 1:
                            cand.append(str((rot[idx] + i * a) % 10))
                        else:
                            cand.append(str((rot[idx] + j * a) % 10))
                    cand = "".join(cand)
                    if res is None or cand < res:
                        res = cand
            r = (r + b) % n
        return res
