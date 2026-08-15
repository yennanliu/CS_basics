"""

3106. Lexicographically Smallest String After Operations With Constraint
Medium

You are given a string s and an integer k.

Define a function distance(s1, s2) between two strings s1 and s2 of the same length n as:

The sum of the minimum distance between s1[i] and s2[i] when the characters from 'a' to 'z' are placed in a cyclic order, for all i in the range [0, n - 1].

For example, distance("ab", "cd") == 4, and distance("a", "z") == 1.

You can change any letter of s to any other lowercase English letter, any number of times.

Return a string denoting the lexicographically smallest string t you can get after some changes, such that distance(s, t) <= k.


Example 1:

Input: s = "zbbz", k = 3
Output: "aaaz"
Explanation:
Change s to "aaaz". The distance between "zbbz" and "aaaz" is equal to k = 3.

Example 2:

Input: s = "xaxcd", k = 4
Output: "aawcd"
Explanation:
The distance between "xaxcd" and "aawcd" is equal to k = 4.

Example 3:

Input: s = "lol", k = 0
Output: "lol"
Explanation:
It's impossible to change any character as k = 0.


Constraints:

1 <= s.length <= 100
0 <= k <= 2000
s consists only of lowercase English letters.

"""

# V0
# IDEA : GREEDY LEFT TO RIGHT — SPEND THE BUDGET ON THE EARLIEST POSITIONS
#
#   an earlier position outranks every later one lexicographically, so at
#   each index push the letter as low as the remaining budget allows, then
#   move on with what is left. no lookahead can beat that.
#
#   the cyclic distance between two letters is
#       d = |a - b|,  then  min(d, 26 - d)
#   so reaching 'a' from c costs min(c, 26 - c).
#
#   if 'a' is affordable, take it; otherwise walk up from 'a' and take the
#   first letter within budget — and if even the original letter's own cost
#   (0) is all we can afford, the letter simply stays.
#
# time = O(26 * n), space = O(n)
class Solution(object):
    def getSmallestString(self, s, k):

        def dist(a, b):
            d = abs(a - b)
            return min(d, 26 - d)

        out = []
        for ch in s:
            c = ord(ch) - 97
            for target in range(26):
                cost = dist(c, target)
                if cost <= k:
                    out.append(chr(97 + target))
                    k -= cost
                    break
        return ''.join(out)
