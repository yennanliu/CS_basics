"""

87. Scramble String
Hard

We can scramble a string s to get a string t using the following algorithm:

1. If the length of the string is 1, stop.
2. If the length of the string is > 1, do the following:
   - Split the string into two non-empty substrings at a random index, i.e., if the string
     is s, divide it to x and y where s = x + y.
   - Randomly decide to swap the two substrings or to keep them in the same order.
     i.e., after this step, s may become s = x + y or s = y + x.
   - Apply step 1 recursively on each of the two substrings x and y.

Given two strings s1 and s2 of the same length, return true if s2 is a scrambled string of s1,
otherwise, return false.


Example 1:

Input: s1 = "great", s2 = "rgeat"
Output: true
Explanation: One possible scenario applied on s1 is:
"great" --> "gr/eat"        // divide at random index.
"gr/eat" --> "gr/eat"       // decision is not to swap, keep them in order.
"gr/eat" --> "g/r / e/at"   // apply the same algorithm recursively on both substrings.
"g/r / e/at" --> "r/g / e/at"
"r/g / e/at" --> "r/g / e/ a/t"
"r/g / e/ a/t" --> "r/g / e/ a/t"
The result string is "rgeat" which is s2, so we return true.

Example 2:

Input: s1 = "abcde", s2 = "caebd"
Output: false

Example 3:

Input: s1 = "a", s2 = "a"
Output: true


Constraints:

s1.length == s2.length
1 <= s1.length <= 30
s1 and s2 consist of lowercase English letters.

"""

# V0
# IDEA : MEMOIZED DFS (interval DP)
#
#  dfs(i, j, k) = can s1[i : i+k] be scrambled into s2[j : j+k] ?
#
#  for every split length h in [1, k):
#     - no swap : dfs(i, j, h)         and dfs(i+h, j+h, k-h)
#     - swap    : dfs(i, j+k-h, h)     and dfs(i+h, j,   k-h)
#
"""

DP def
    (INTERVAL DP over two strings)

    dfs(i, j, k): can s1[i : i+k] be scrambled into s2[j : j+k] ?

DP eq

     for every split length h in [1, k):

        NO SWAP: dfs(i, j, h)         and dfs(i+h, j+h, k-h)

        SWAP   : dfs(i, j+k-h, h)     and dfs(i+h, j,   k-h)

     dfs = True if either arrangement works for some h


    -> e.g. in the SWAP case s1's LEFT part must match s2's RIGHT part,
              which is why the s2 offset becomes j + k - h

     prune: if sorted(a) != sorted(b) the letter multisets differ -> False
            (this is what makes the O(n^4) practical)

     base: the two slices are already equal -> True
     ans = dfs(0, 0, n)

"""
# time = O(n^4)   # O(n^3) states x O(n) splits
# space = O(n^3)
class Solution(object):
    def isScramble(self, s1, s2):
        if len(s1) != len(s2):
            return False

        n = len(s1)
        memo = {}

        def dfs(i, j, k):
            key = (i, j, k)
            if key in memo:
                return memo[key]

            a, b = s1[i:i + k], s2[j:j + k]
            if a == b:
                memo[key] = True
                return True

            # prune: different letter multisets can never match
            if sorted(a) != sorted(b):
                memo[key] = False
                return False

            for h in range(1, k):
                # keep the two halves in order
                if dfs(i, j, h) and dfs(i + h, j + h, k - h):
                    memo[key] = True
                    return True
                # swap the two halves: s1's left part matches s2's RIGHT part
                if dfs(i, j + k - h, h) and dfs(i + h, j, k - h):
                    memo[key] = True
                    return True

            memo[key] = False
            return False

        return dfs(0, 0, n)
