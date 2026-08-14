"""

1447. Simplified Fractions
Medium

Given an integer n, return a list of all simplified fractions between 0 and 1 (exclusive) such that the denominator is less-than-or-equal-to n. You can return the answer in any order.


Example 1:

Input: n = 2
Output: ["1/2"]
Explanation: "1/2" is the only unique fraction with a denominator less-than-or-equal-to 2.

Example 2:

Input: n = 3
Output: ["1/2","1/3","2/3"]

Example 3:

Input: n = 4
Output: ["1/2","1/3","1/4","2/3","3/4"]
Explanation: "2/4" is not a simplified fraction because it can be simplified to "1/2".


Constraints:

1 <= n <= 100

"""

# V0
# IDEA : ENUMERATE PAIRS + GCD (a fraction is simplified iff gcd == 1)
#
#   the value must lie strictly between 0 and 1, so numerator < denominator:
#   enumerate the numerator i in [1, n) and the denominator j in (i, n].
#   keep i/j only when gcd(i, j) == 1 - otherwise the very same value has
#   already been emitted in lowest terms.
#   NOTE : n = 1 yields nothing, the loops simply never run.
#
# time = O(n^2 * log n), space = O(1) besides the output
class Solution(object):
    def simplifiedFractions(self, n):
        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a

        res = []
        for i in range(1, n):
            for j in range(i + 1, n + 1):
                if gcd(i, j) == 1:
                    res.append(str(i) + "/" + str(j))
        return res
