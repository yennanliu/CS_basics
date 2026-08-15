"""

2843. Count Symmetric Integers
Easy

You are given two positive integers low and high.

An integer x consisting of 2 * n digits is symmetric if the sum of the first n digits of x is equal to the sum of the last n digits of x. Numbers with an odd number of digits are never symmetric.

Return the number of symmetric integers in the range [low, high].


Example 1:

Input: low = 1, high = 100
Output: 9
Explanation: There are 9 symmetric integers between 1 and 100: 11, 22, 33, 44, 55, 66, 77, 88, and 99.

Example 2:

Input: low = 1200, high = 1230
Output: 4
Explanation: There are 4 symmetric integers between 1200 and 1230: 1203, 1212, 1221, and 1230.


Constraints:

1 <= low <= high <= 10^4

"""

# V0
# IDEA : BRUTE FORCE ENUMERATION over [low, high]
#
#   high <= 10^4, so at most ~10000 candidates - just test each one.
#
#   for a number x: render it as a string; an odd digit count can never be
#   symmetric, otherwise compare the digit sum of the first half against the
#   digit sum of the second half.
#
#   NOTE : "symmetric" is about DIGIT SUMS of the two halves, not about the
#          number being a palindrome - 1203 is symmetric (1+2 == 0+3) yet it
#          is not a palindrome.
#   NOTE : with high <= 10^4 only 2-digit and 4-digit numbers can qualify
#          (10000 itself has 5 digits).
#
# time = O((high - low) * log(high)), space = O(log(high))
class Solution(object):
    def countSymmetricIntegers(self, low, high):
        ans = 0
        for x in range(low, high + 1):
            d = str(x)
            n = len(d)
            if n & 1:
                continue
            half = n // 2
            left = sum(int(c) for c in d[:half])
            right = sum(int(c) for c in d[half:])
            if left == right:
                ans += 1
        return ans
