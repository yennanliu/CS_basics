"""

1925. Count Square Sum Triples
Easy

A square triple (a,b,c) is a triple where a, b, and c are integers and a^2 + b^2 = c^2.

Given an integer n, return the number of square triples such that 1 <= a, b, c <= n.


Example 1:

Input: n = 5
Output: 2
Explanation: The square triples are (3,4,5) and (4,3,5).

Example 2:

Input: n = 10
Output: 4
Explanation: The square triples are (3,4,5), (4,3,5), (6,8,10), and (8,6,10).


Constraints:

1 <= n <= 250

"""

# V0
# IDEA : HASH SET OF SQUARES + ENUMERATE (a, b)
#
#   precompute the set { c*c : 1 <= c <= n }. then for every ordered pair
#   (a, b) with a, b < n, the triple is valid iff a*a + b*b is in that set.
#
#   the set lookup replaces the float sqrt() check, so there is no rounding
#   risk at all.
#
#   NOTE : the answer counts ORDERED pairs, so (3,4,5) and (4,3,5) both count;
#          just loop a and b independently.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def countTriples(self, n):
        squares = set(c * c for c in range(1, n + 1))
        res = 0
        for a in range(1, n):
            aa = a * a
            for b in range(1, n):
                if aa + b * b in squares:
                    res += 1
        return res
