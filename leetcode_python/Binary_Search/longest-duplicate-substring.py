"""

1044. Longest Duplicate Substring
Hard

Given a string s, consider all duplicated substrings: (contiguous) substrings of s
that occur 2 or more times. The occurrences may overlap.

Return any duplicated substring that has the longest possible length.
If s does not have a duplicated substring, the answer is "".


Example 1:

Input: s = "banana"
Output: "ana"

Example 2:

Input: s = "abcd"
Output: ""


Constraints:

2 <= s.length <= 3 * 10^4
s consists of lowercase English letters.

"""

# V0
# IDEA : BINARY SEARCH ON LENGTH + RABIN-KARP (rolling hash)
#
#  monotonic property :
#    if a duplicated substring of length L exists,
#    then one of length L-1 exists too (just drop its last char)
#    -> binary search the largest feasible L
#
#  for a fixed L, sweep all n-L+1 windows with a rolling hash.
#  On a hash hit we still compare the real substrings, so collisions
#  can never produce a wrong answer.
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def longestDupSubstring(self, s):
        n = len(s)
        nums = [ord(c) - ord('a') for c in s]
        MOD = (1 << 61) - 1
        BASE = 26

        def search(L):
            """return start index of some duplicated substring of length L, else -1"""
            h = 0
            for i in range(L):
                h = (h * BASE + nums[i]) % MOD
            power = pow(BASE, L, MOD)
            seen = {h: [0]}
            for i in range(1, n - L + 1):
                h = (h * BASE - nums[i - 1] * power + nums[i + L - 1]) % MOD
                if h in seen:
                    cur = s[i:i + L]
                    for j in seen[h]:
                        if s[j:j + L] == cur:
                            return i
                    seen[h].append(i)
                else:
                    seen[h] = [i]
            return -1

        res = ""
        lo, hi = 1, n - 1
        while lo <= hi:
            mid = (lo + hi) // 2
            pos = search(mid)
            if pos != -1:
                res = s[pos:pos + mid]
                lo = mid + 1
            else:
                hi = mid - 1

        return res


# V1
# IDEA : BINARY SEARCH + SET OF SUBSTRINGS (simpler, but slicing costs O(L) each)
# time = O(n^2 log n)
#   fine for teaching / small inputs, TLE-prone on the largest cases
# space = O(n^2)
class Solution2(object):
    def longestDupSubstring(self, s):
        n = len(s)

        def check(L):
            seen = set()
            for i in range(n - L + 1):
                t = s[i:i + L]
                if t in seen:
                    return t
                seen.add(t)
            return ""

        res = ""
        lo, hi = 1, n - 1
        while lo <= hi:
            mid = (lo + hi) // 2
            t = check(mid)
            if t:
                res = t
                lo = mid + 1
            else:
                hi = mid - 1
        return res
