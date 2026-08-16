"""

3658. GCD of Odd and Even Sums
Easy

You are given an integer n. Your task is to compute the sum of the first n odd positive numbers and the sum of the first n even positive numbers, and then return the greatest common divisor (GCD) of the two sums.

Return an integer which is the GCD of the two sums.


Example 1:

Input: n = 4
Output: 4
Explanation:
The sum of the first 4 odd numbers is 1 + 3 + 5 + 7 = 16.
The sum of the first 4 even numbers is 2 + 4 + 6 + 8 = 20.
The GCD of 16 and 20 is 4.

Example 2:

Input: n = 5
Output: 5
Explanation:
The sum of the first 5 odd numbers is 1 + 3 + 5 + 7 + 9 = 25.
The sum of the first 5 even numbers is 2 + 4 + 6 + 8 + 10 = 30.
The GCD of 25 and 30 is 5.


Constraints:

1 <= n <= 1000

"""

# V0
# IDEA : CLOSED FORMS COLLAPSE THE GCD TO n
#
#   the first n odd numbers sum to n^2 and the first n even numbers sum to
#   n(n+1). so
#       gcd(n^2, n(n+1)) = n * gcd(n, n+1) = n * 1 = n
#   because consecutive integers are always coprime.
#
#   the loop-free answer is therefore just n.
#
# time = O(1), space = O(1)
class Solution(object):
    def gcdOfOddEvenSums(self, n):
        return n
