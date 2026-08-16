"""

3581. Count Odd Letters from Number
Easy

You are given an integer n perform the following steps:

Convert each digit of n into its lowercase English word (e.g., 4 -> "four",
1 -> "one").
Concatenate those words in the original digit order to form a string s.

Return the number of distinct characters in s that appear an odd number of
times.


Example 1:

Input: n = 41
Output: 5
Explanation:
41 -> "fourone"
Characters with odd frequencies: 'f', 'u', 'r', 'n', 'e'. Thus, the answer
is 5.

Example 2:

Input: n = 20
Output: 5
Explanation:
20 -> "twozero"
Characters with odd frequencies: 't', 'w', 'z', 'e', 'r'. Thus, the answer
is 5.


Constraints:

1 <= n <= 10^9

"""

# V0
# IDEA : XOR PARITY MASK OVER THE 26 LETTERS
#
#   only the parity of each letter's count matters, never the count itself,
#   so a single bit per letter is enough state. toggling that bit on every
#   occurrence leaves it at 1 exactly when the letter appeared an odd number
#   of times.
#
#   this also removes the need to build the concatenated string at all —
#   digits can be consumed straight off n, and the order in which they are
#   consumed is irrelevant because xor is commutative. the answer is then
#   just the number of set bits in the mask.
#
# time = O(log n), space = O(1)
class Solution(object):
    def countOddLetters(self, n):
        words = [
            "zero", "one", "two", "three", "four",
            "five", "six", "seven", "eight", "nine",
        ]

        mask = 0
        while n:
            for c in words[n % 10]:
                mask ^= 1 << (ord(c) - ord('a'))
            n //= 10

        cnt = 0
        while mask:
            mask &= mask - 1
            cnt += 1
        return cnt
