"""

1830. Minimum Number of Operations to Make String Sorted
Hard

You are given a string s (0-indexed). You are asked to perform the following operation on s until you get a sorted string:

Find the largest index i such that 1 <= i < s.length and s[i] < s[i - 1].
Find the largest index j such that i <= j < s.length and s[k] < s[i - 1] for all the possible values of k in the range [i, j] inclusive.
Swap the two characters at indices i - 1 and j.
Reverse the suffix starting at index i.

Return the number of operations needed to make the string sorted. Since the answer can be too large, return it modulo 10^9 + 7.


Example 1:

Input: s = "cba"
Output: 5
Explanation: The simulation goes as follows:
Operation 1: i=2, j=2. Swap s[1] and s[2] to get s="cab", then reverse the suffix starting at 2. Now, s="cab".
Operation 2: i=1, j=2. Swap s[0] and s[2] to get s="bac", then reverse the suffix starting at 1. Now, s="bca".
Operation 3: i=2, j=2. Swap s[1] and s[2] to get s="bac", then reverse the suffix starting at 2. Now, s="bac".
Operation 4: i=1, j=1. Swap s[0] and s[1] to get s="abc", then reverse the suffix starting at 1. Now, s="acb".
Operation 5: i=2, j=2. Swap s[1] and s[2] to get s="abc", then reverse the suffix starting at 2. Now, s="abc".

Example 2:

Input: s = "aabaa"
Output: 2
Explanation: The simulation goes as follows:
Operation 1: i=3, j=4. Swap s[2] and s[4] to get s="aaaab", then reverse the substring starting at 3. Now, s="aaaba".
Operation 2: i=4, j=4. Swap s[3] and s[4] to get s="aaaab", then reverse the substring starting at 4. Now, s="aaaab".


Constraints:

1 <= s.length <= 3000
s consists only of lowercase English letters.

"""

# V0
# IDEA : THE OPERATION IS "PREVIOUS PERMUTATION" -> ANSWER = RANK OF s
#
#   the four steps are exactly the standard prev_permutation algorithm, and the
#   sorted string is the smallest permutation of the multiset. so the number of
#   operations is simply how many permutations of s are strictly SMALLER than
#   s -- its 0-based lexicographic rank.
#
#   count them position by position: fixing s[0..i-1] and putting some char
#   c < s[i] at position i, the tail can be arranged in
#     (n-i-1)! / prod(cnt'[x]!)      (cnt' = remaining counts with c removed)
#   ways. summing over all such c collapses neatly to
#     m * (n-i-1)! / prod(cnt[x]!)   where m = #remaining chars smaller than s[i]
#   because (n-i-1)!/prod(cnt' !) = cnt[c] * (n-i-1)!/prod(cnt !).
#
#   NOTE : division under a prime modulus = multiply by the modular inverse,
#          so precompute factorials and their inverses once.
#
# time = O(n * 26), space = O(n)
from collections import Counter
MOD = 10 ** 9 + 7
_MAXN = 3005
_fact = [1] * _MAXN
for _i in range(1, _MAXN):
    _fact[_i] = _fact[_i - 1] * _i % MOD
_inv_fact = [1] * _MAXN
_inv_fact[_MAXN - 1] = pow(_fact[_MAXN - 1], MOD - 2, MOD)
for _i in range(_MAXN - 1, 0, -1):
    _inv_fact[_i - 1] = _inv_fact[_i] * _i % MOD


class Solution(object):
    def makeStringSorted(self, s):
        n = len(s)
        cnt = Counter(s)
        res = 0
        for i, c in enumerate(s):
            smaller = 0
            for ch, v in cnt.items():
                if ch < c:
                    smaller += v
            if smaller:
                t = _fact[n - i - 1] * smaller % MOD
                for v in cnt.values():
                    t = t * _inv_fact[v] % MOD
                res = (res + t) % MOD
            cnt[c] -= 1
            if cnt[c] == 0:
                del cnt[c]
        return res
