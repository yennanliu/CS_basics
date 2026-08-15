"""

3292. Minimum Number of Valid Strings to Form Target II
Hard

You are given an array of strings words and a string target.

A string x is called valid if x is a prefix of any string in words.

Return the minimum number of valid strings that can be concatenated to form target. If it is not possible to form target, return -1.


Example 1:

Input: words = ["abc","aaaaa","bcdef"], target = "aabcdabc"
Output: 3
Explanation:
The target string can be formed by concatenating:
Prefix of length 2 of words[1], i.e. "aa".
Prefix of length 3 of words[2], i.e. "bcd".
Prefix of length 3 of words[0], i.e. "abc".

Example 2:

Input: words = ["abababab","ab"], target = "ababaababa"
Output: 2
Explanation:
The target string can be formed by concatenating:
Prefix of length 5 of words[0], i.e. "ababa".
Prefix of length 5 of words[0], i.e. "ababa".

Example 3:

Input: words = ["abcdef"], target = "xyz"
Output: -1


Constraints:

1 <= words.length <= 100
1 <= words[i].length <= 5 * 10^4
The input is generated such that sum(words[i].length) <= 10^5.
words[i] consists only of lowercase English letters.
1 <= target.length <= 5 * 10^4
target consists only of lowercase English letters.

"""

# V0
# IDEA : Z-FUNCTION PER WORD INSTEAD OF A TRIE WALK
#
#   the reach array is the same idea as LC 3291 —
#       reach[i] = i + (longest prefix of any word matching target at i)
#   — but a trie walk costs O(word length) per position, which at 5*10^4
#   characters both ways is far too slow.
#
#   the Z-algorithm gets it in linear time per word : run Z over
#       word + '#' + target
#   and the z-value at target's position i is exactly how far word's PREFIX
#   matches there. take the maximum over words.
#
#   the rest is the Jump Game II sweep : keep the furthest covered length
#   reachable with the current piece count, and spend a new piece whenever
#   the sweep catches up with the frontier.
#
# time = O(sum of word lengths + |words| * |target|), space = O(|target|)
class Solution(object):
    def minValidStrings(self, words, target):
        n = len(target)
        reach = [0] * n

        def z_array(s):
            m = len(s)
            z = [0] * m
            l = r = 0
            for i in range(1, m):
                if i < r:
                    z[i] = min(r - i, z[i - l])
                while i + z[i] < m and s[z[i]] == s[i + z[i]]:
                    z[i] += 1
                if i + z[i] > r:
                    l, r = i, i + z[i]
            return z

        for w in words:
            comb = w + '#' + target
            z = z_array(comb)
            off = len(w) + 1
            for i in range(n):
                v = z[off + i]
                if v > reach[i]:
                    reach[i] = v
        for i in range(n):
            reach[i] += i                       # exclusive end position

        res = 0
        cur_end = 0
        farthest = 0
        i = 0
        while cur_end < n:
            while i < n and i <= cur_end:
                if reach[i] > farthest:
                    farthest = reach[i]
                i += 1
            if farthest <= cur_end:
                return -1
            cur_end = farthest
            res += 1
        return res
