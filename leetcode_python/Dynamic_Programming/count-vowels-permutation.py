"""

1220. Count Vowels Permutation
Hard

Given an integer n, your task is to count how many strings of length n can be formed
under the following rules:

Each character is a lower case vowel ('a', 'e', 'i', 'o', 'u')
Each vowel 'a' may only be followed by an 'e'.
Each vowel 'e' may only be followed by an 'a' or an 'i'.
Each vowel 'i' may not be followed by another 'i'.
Each vowel 'o' may only be followed by an 'i' or a 'u'.
Each vowel 'u' may only be followed by an 'a'.

Since the answer may be too large, return it modulo 10^9 + 7.

Example 1:

Input: n = 1
Output: 5
Explanation: All possible strings are: "a", "e", "i" , "o" and "u".

Example 2:

Input: n = 2
Output: 10
Explanation: All possible strings are: "ae", "ea", "ei", "ia", "ie", "io", "iu", "oi",
"ou" and "ua".

Example 3:

Input: n = 5
Output: 68


Constraints:

1 <= n <= 2 * 10^4

"""

# V0
# IDEA: 1D DP (count by last character)
"""
 successors (given by the problem):
    a -> e
    e -> a, i
    i -> a, e, o, u
    o -> i, u
    u -> a

 invert them to get the PREDECESSORS of each vowel:
    a <- e, i, u
    e <- a, i
    i <- e, o
    o <- i
    u <- i, o

 DP def:
    - a, e, i, o, u = number of valid strings of current length ending with that vowel

 DP eq (one step):
    - a' = e + i + u
    - e' = a + i
    - i' = e + o
    - o' = i
    - u' = i + o
"""
# time = O(n)
# space = O(1)
class Solution(object):
    def countVowelPermutation(self, n):
        MOD = 10 ** 9 + 7
        # length 1 : one string per vowel
        a = e = i = o = u = 1
        for _ in range(n - 1):
            a, e, i, o, u = (
                (e + i + u) % MOD,
                (a + i) % MOD,
                (e + o) % MOD,
                i % MOD,
                (i + o) % MOD,
            )
        return (a + e + i + o + u) % MOD
