"""

906. Super Palindromes
Hard

Let's say a positive integer is a super-palindrome if it is a palindrome,
and it is also the square of a palindrome.

Given two positive integers left and right represented as strings, return the number
of super-palindromes integers in the inclusive range [left, right].


Example 1:

Input: left = "4", right = "1000"
Output: 4
Explanation: 4, 9, 121, and 484 are superpalindromes.
Note that 676 is not a superpalindrome: 26 * 26 = 676, but 26 is not a palindrome.

Example 2:

Input: left = "1", right = "2"
Output: 1


Constraints:

1 <= left.length, right.length <= 18
left and right consist of only digits.
left and right cannot have leading zeros.
left and right represent integers in the range [1, 10^18 - 1].
left is less than or equal to right.

"""

# V0
# IDEA : ENUMERATE THE PALINDROMIC ROOT (build palindromes, don't test them)
"""
 A super-palindrome x satisfies x = p * p where BOTH x and p are palindromes.
 Since x < 10^18, we know p < 10^9. Testing every p up to 10^9 is way too slow,
 so instead we GENERATE palindromic p directly:

    take a seed i (1 .. 10^5) and mirror it
        even length : str(i) + reverse(str(i))       e.g. 12 -> 1221
        odd  length : str(i) + reverse(str(i)[:-1])  e.g. 12 -> 121

 That yields every palindrome below 10^10, which more than covers p < 10^9.
 For each such p we only need to check that p * p lands in [left, right]
 and is itself a palindrome.
"""
# time = O(M^(1/4) * log(M)), M = 10^18 -> ~10^5 roots, each checked in O(18)
# space = O(1)
class Solution(object):
    def superpalindromesInRange(self, left, right):
        lo, hi = int(left), int(right)

        def is_palindrome(x):
            t = str(x)
            return t == t[::-1]

        res = 0
        # seeds up to 10^5 generate every palindrome with <= 10 digits
        for i in range(1, 100000):
            s = str(i)
            for root in (s + s[::-1], s + s[:-1][::-1]):
                p = int(root)
                x = p * p
                if lo <= x <= hi and is_palindrome(x):
                    res += 1

        return res
