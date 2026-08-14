"""

2156. Find Substring With Given Hash Value
Medium

The hash of a 0-indexed string s of length k, given integers p and m, is computed using the following function:

hash(s, p, m) = (val(s[0]) * p^0 + val(s[1]) * p^1 + ... + val(s[k-1]) * p^(k-1)) mod m.

Where val(s[i]) represents the index of s[i] in the alphabet from val('a') = 1 to val('z') = 26.

You are given a string s and the integers power, modulo, k, and hashValue. Return sub, the first substring of s of length k such that hash(sub, power, modulo) == hashValue.

The test cases will be generated such that an answer always exists.

A substring is a contiguous non-empty sequence of characters within a string.


Example 1:

Input: s = "leetcode", power = 7, modulo = 20, k = 2, hashValue = 0
Output: "ee"
Explanation: The hash of "ee" can be computed to be hash("ee", 7, 20) = (5 * 1 + 5 * 7) mod 20 = 40 mod 20 = 0.
"ee" is the first substring of length 2 with hashValue 0. Hence, we return "ee".

Example 2:

Input: s = "fbxzaad", power = 31, modulo = 100, k = 3, hashValue = 32
Output: "fbx"
Explanation: The hash of "fbx" can be computed to be hash("fbx", 31, 100) = (6 * 1 + 2 * 31 + 24 * 961) mod 100 = 23132 mod 100 = 32.
"fbx" is the first substring of length 3 with hashValue 32. Hence, we return "fbx".
Note that "bxz" also has a hash of 32 but it is a later substring.


Constraints:

1 <= k <= s.length <= 2 * 10^4
1 <= power, modulo <= 10^9
0 <= hashValue < modulo
s consists of lowercase English letters only.
The test cases are generated such that an answer always exists.

"""

# V0
# IDEA : ROLLING HASH — SLIDE RIGHT-TO-LEFT TO AVOID A MODULAR INVERSE
#
#   the weight of a character grows with its position INSIDE the window, so
#   sliding LEFT-to-right would require dividing by `power` (a modular
#   inverse, which may not exist when gcd(power, modulo) != 1).
#
#   going RIGHT-to-left instead, moving the window from [i+1, i+k] to
#   [i, i+k-1] is a clean multiply-and-add :
#       h = h * power - val(s[i+k]) * power^k + val(s[i])
#   all mod `modulo`.
#
#   scanning backwards means the LAST match found is the earliest one, so
#   just keep overwriting the answer.
#
# time = O(n), space = O(1)
class Solution(object):
    def subStrHash(self, s, power, modulo, k, hashValue):
        n = len(s)

        def val(c):
            return ord(c) - ord('a') + 1

        top = pow(power, k, modulo)          # power^k mod modulo

        # hash of the last window s[n-k : n], built with Horner's rule from
        # the RIGHT so the leftmost character ends up with weight power^0
        h = 0
        for i in range(n - 1, n - k - 1, -1):
            h = (h * power + val(s[i])) % modulo

        start = n - k if h == hashValue else -1
        for i in range(n - k - 1, -1, -1):
            h = (h * power - val(s[i + k]) * top + val(s[i])) % modulo
            if h == hashValue:
                start = i
        return s[start:start + k]
