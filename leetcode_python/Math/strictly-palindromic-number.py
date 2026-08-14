"""

2396. Strictly Palindromic Number
Medium

An integer n is strictly palindromic if, for every base b between 2 and n - 2 (inclusive), the string representation of the integer n in base b is palindromic.

Given an integer n, return true if n is strictly palindromic and false otherwise.

A string is palindromic if it reads the same forward and backward.


Example 1:

Input: n = 9
Output: false
Explanation: In base 2: 9 = 1001 (base 2), which is palindromic.
In base 3: 9 = 100 (base 3), which is not palindromic.
Therefore, 9 is not strictly palindromic so we return false.
Note that in bases 4, 5, 6, and 7, n = 9 is also not palindromic.

Example 2:

Input: n = 4
Output: false
Explanation: We only consider base 2: 4 = 100 (base 2), which is not palindromic.
Therefore, we return false.


Constraints:

4 <= n <= 10^5

"""

# V0
# IDEA : MATH (base n-2 is always a counter-example, so the answer is always false)
#
#   for n >= 4 the base b = n - 2 is inside the allowed range [2, n-2].
#   in that base:   n = 1 * (n - 2) + 2   ->  digits "1 2"
#   "12" is never a palindrome (1 != 2), so no n >= 4 can be strictly
#   palindromic.
#
#   NOTE : the constraint 4 <= n guarantees b = n-2 >= 2, so the range is
#          never empty and this argument always applies -> return False.
#
# time = O(1), space = O(1)
class Solution(object):
    def isStrictlyPalindromic(self, n):
        return False
