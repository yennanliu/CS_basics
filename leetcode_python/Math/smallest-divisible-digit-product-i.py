"""

3345. Smallest Divisible Digit Product I
Easy

You are given two integers n and t. Return the smallest number greater than or equal to n such that the product of its digits is divisible by t.


Example 1:

Input: n = 10, t = 2
Output: 10
Explanation:
The digit product of 10 is 0, which is divisible by 2, making it the smallest number greater than or equal to 10 that satisfies the condition.

Example 2:

Input: n = 15, t = 3
Output: 16
Explanation:
The digit product of 16 is 6, which is divisible by 3, making it the smallest number greater than or equal to 15 that satisfies the condition.


Constraints:

1 <= n <= 100
1 <= t <= 10

"""

# V0
# IDEA : n AND t ARE TINY — WALK UPWARDS UNTIL THE PRODUCT DIVIDES
#
#   any number containing a 0 digit has a digit product of 0, which every t
#   divides, so the search terminates almost immediately — no candidate is
#   ever far away.
#
# time = O(1) in practice, space = O(1)
class Solution(object):
    def smallestNumber(self, n, t):
        x = n
        while True:
            prod = 1
            for ch in str(x):
                prod *= int(ch)
            if prod % t == 0:
                return x
            x += 1
