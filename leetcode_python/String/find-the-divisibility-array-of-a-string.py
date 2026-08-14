"""

2575. Find the Divisibility Array of a String
Medium

You are given a 0-indexed string word of length n consisting of digits, and a positive integer m.

The divisibility array div of word is an integer array of length n such that:

div[i] = 1 if the numeric value of word[0,...,i] is divisible by m, or
div[i] = 0 otherwise.

Return the divisibility array of word.


Example 1:

Input: word = "998244353", m = 3
Output: [1,1,0,0,0,1,1,0,0]
Explanation: There are only 4 prefixes that are divisible by 3: "9", "99", "998244", and "9982443".

Example 2:

Input: word = "1010", m = 10
Output: [0,1,0,1]
Explanation: There are only 2 prefixes that are divisible by 10: "10", and "1010".


Constraints:

1 <= n <= 10^5
word.length == n
word consists of digits from 0 to 9
1 <= m <= 10^9

"""

# V0
# IDEA : ROLLING REMAINDER (Horner's rule under a modulus)
#
#   The prefix value obeys  val(i) = val(i-1) * 10 + digit(i).
#   Building val(i) literally would produce a 10^5-digit integer, so instead
#   we keep only its remainder mod m. Modular arithmetic is compatible with
#   both operations used:
#
#       val(i) % m  ==  ( (val(i-1) % m) * 10 + digit(i) ) % m
#
#   so the running remainder x is all the state we ever need. Emit 1 whenever
#   x hits 0.
#
#   NOTE : x stays < m <= 10^9, so the intermediate x * 10 + d stays under
#          10^10 - fine for Python, but this is exactly where Java/C++ need a
#          64-bit (long) accumulator instead of int.
#
#   NOTE : this must be an iterative single pass - n can reach 10^5, so any
#          recursive formulation would blow the stack.
#
# time = O(n), space = O(1) beyond the output
class Solution(object):
    def divisibilityArray(self, word, m):
        res = []
        x = 0
        for c in word:
            x = (x * 10 + (ord(c) - ord('0'))) % m
            res.append(1 if x == 0 else 0)
        return res
