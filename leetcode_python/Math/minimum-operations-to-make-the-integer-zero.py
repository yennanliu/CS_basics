"""

2749. Minimum Operations to Make the Integer Zero
Medium

You are given two integers num1 and num2.

In one operation, you can choose integer i in the range [0, 60] and subtract 2^i + num2 from num1.

Return the integer denoting the minimum number of operations needed to make num1 equal to 0.

If it is impossible to make num1 equal to 0, return -1.


Example 1:

Input: num1 = 3, num2 = -2
Output: 3
Explanation: We can make 3 equal to 0 with the following operations:
- We choose i = 2 and subtract 2^2 + (-2) from 3, 3 - (4 + (-2)) = 1.
- We choose i = 2 and subtract 2^2 + (-2) from 1, 1 - (4 + (-2)) = -1.
- We choose i = 0 and subtract 2^0 + (-2) from -1, (-1) - (1 + (-2)) = 0.
It can be proven, that 3 is the minimum number of operations that we need to perform.

Example 2:

Input: num1 = 5, num2 = 7
Output: -1
Explanation: It can be proven, that it is impossible to make 5 equal to 0 with the given operation.


Constraints:

1 <= num1 <= 10^9
-10^9 <= num2 <= 10^9

"""

# V0
# IDEA : ENUMERATE THE OPERATION COUNT k (bit manipulation / brainteaser)
#
#  the ORDER of the operations does not matter, only HOW MANY we do.
#  If we do exactly k operations picking exponents i1..ik, then
#
#       num1 - (2^i1 + ... + 2^ik) - k * num2 == 0
#   =>  2^i1 + ... + 2^ik == num1 - k * num2  =:  x
#
#  so the whole question becomes: can x be written as a sum of EXACTLY k
#  powers of two? That is possible iff
#
#       popcount(x) <= k <= x
#
#   - popcount(x) <= k : you need at least popcount(x) terms; and any set bit
#                        2^i can always be split into 2^(i-1) + 2^(i-1), which
#                        raises the term count by 1 -> every count in between
#                        is reachable.
#   - k <= x           : each term is >= 2^0 = 1, so k terms sum to >= k.
#
#   NOTE : we want the SMALLEST k, so scan k = 1, 2, 3, ... and return the
#          first hit.
#   NOTE : termination. If num2 > 0 then x shrinks and we stop as soon as
#          x < 0. If num2 <= 0 then x grows, but x stays below 2^36 for any
#          k <= 60 (num1, |num2| <= 10^9), so popcount(x) <= 36 <= k is
#          guaranteed to fire well before k = 60 -> the loop always ends.
#
# time = O(log(max(num1, num2))), space = O(1)
class Solution(object):
    def makeTheIntegerZero(self, num1, num2):
        k = 1
        while True:
            x = num1 - k * num2
            if x < 0:
                return -1
            if bin(x).count('1') <= k <= x:
                return k
            k += 1
