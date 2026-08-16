"""

3677. Count Binary Palindromic Numbers
Hard

You are given a non-negative integer n.

A non-negative integer is called binary-palindromic if its binary
representation (written without leading zeros) reads the same forward and
backward.

Return the number of integers k such that 0 <= k <= n and the binary
representation of k is a palindrome.

Note: The number 0 is considered binary-palindromic, and its representation
is "0".


Example 1:

Input: n = 9
Output: 6
Explanation:
The integers k in the range [0, 9] whose binary representations are
palindromes are:
0 -> "0"
1 -> "1"
3 -> "11"
5 -> "101"
7 -> "111"
9 -> "1001"
All other values in [0, 9] have non-palindromic binary forms. Therefore,
the count is 6.

Example 2:

Input: n = 0
Output: 1
Explanation:
Since "0" is a palindrome, the count is 1.


Constraints:

0 <= n <= 10^15

"""

# V0
# IDEA : A PALINDROME IS DETERMINED BY ITS FIRST HALF -- COUNT HALVES
#
#   fix a bit length L. a length-L binary palindrome is completely
#   determined by its top h = ceil(L/2) bits, and conversely every h-bit
#   string starting with 1 mirrors into exactly one valid palindrome. so
#   there are 2^(h-1) palindromes of length L, and the map
#   "first half -> palindrome" is strictly INCREASING, which is the property
#   that makes the boundary case countable instead of searchable.
#
#   so: every length shorter than n's contributes its full 2^(h-1); for
#   n's own length we take the top h bits of n as a number f, and the
#   palindromes of that length that are <= n are exactly those built from
#   halves 2^(h-1) .. f-1 (all strictly below n's prefix, hence below n)
#   plus possibly the one built from f itself, which needs a direct
#   comparison because it shares n's prefix.
#
#   0 has no leading-1 form and is handled as a separate +1.
#
# time = O(log^2 n), space = O(log n)
class Solution(object):
    def countBinaryPalindromes(self, n):
        if n == 0:
            return 1

        s = bin(n)[2:]
        L = len(s)

        ans = 1                       # k = 0
        for length in range(1, L):
            h = (length + 1) // 2
            ans += 1 << (h - 1)       # free choice of the h - 1 bits below the leading 1

        h = (L + 1) // 2
        half = s[:h]
        f = int(half, 2)
        ans += f - (1 << (h - 1))     # every smaller half gives a smaller palindrome

        pal = half + half[::-1][L % 2:]   # mirror; drop the doubled middle bit if L is odd
        if int(pal, 2) <= n:
            ans += 1
        return ans
