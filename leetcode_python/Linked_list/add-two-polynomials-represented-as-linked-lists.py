"""

1634. Add Two Polynomials Represented as Linked Lists
Medium

A polynomial linked list is a special type of linked list where every node represents a term in a polynomial expression.

Each node has three attributes:

- coefficient: an integer representing the number multiplier of the term. The coefficient of the term 9x^4 is 9.
- power: an integer representing the exponent. The power of the term 9x^4 is 4.
- next: a pointer to the next node in the list, or null if it is the last node of the list.

For example, the polynomial 5x^3 + 4x - 7 is represented by the polynomial linked list [[5,3],[4,1],[-7,0]].

The polynomial linked list must be in its standard form: the polynomial must be in strictly descending order by its power value. Also, terms with a coefficient of 0 are omitted.

Given two polynomial linked list heads, poly1 and poly2, add the polynomials together and return the head of the sum of the polynomials.

PolyNode format:

The input/output format is as a list of n nodes, where each node is represented as its [coefficient, power]. For example, the polynomial 5x^3 + 4x - 7 would be represented as: [[5,3],[4,1],[-7,0]].


Example 1:

Input: poly1 = [[1,1]], poly2 = [[1,0]]
Output: [[1,1],[1,0]]
Explanation: poly1 = x. poly2 = 1. The sum is x + 1.

Example 2:

Input: poly1 = [[2,2],[4,1],[3,0]], poly2 = [[3,2],[-4,1],[-1,0]]
Output: [[5,2],[2,0]]
Explanation: poly1 = 2x^2 + 4x + 3. poly2 = 3x^2 - 4x - 1. The sum is 5x^2 + 2. Notice that we omit the "0x" term.

Example 3:

Input: poly1 = [[1,2]], poly2 = [[-1,2]]
Output: []
Explanation: The sum is 0. We return an empty list.


Constraints:

0 <= n <= 10^4
-10^9 <= PolyNode.coefficient <= 10^9
PolyNode.coefficient != 0
0 <= PolyNode.power <= 10^9
PolyNode.power > PolyNode.next.power

"""

# V0
# IDEA : MERGE TWO SORTED LISTS (both are strictly descending by power)
#
#   classic two-pointer merge, like merging two sorted lists:
#     - poly1.power > poly2.power -> take poly1's term
#     - poly1.power < poly2.power -> take poly2's term
#     - equal powers              -> emit the SUM of the coefficients,
#                                    and advance both
#
#   NOTE : a summed coefficient of 0 must be dropped (standard form omits
#          zero terms) -- that is the whole trick of example 3.
#   NOTE : build onto a dummy head so the empty-result case is free.
#
# time = O(m + n), space = O(1) extra (besides the output list)

# Definition for polynomial singly-linked list.
# class PolyNode(object):
#     def __init__(self, x=0, y=0, next=None):
#         self.coefficient = x
#         self.power = y
#         self.next = next

class Solution(object):
    def addPoly(self, poly1, poly2):
        dummy = PolyNode()
        cur = dummy
        while poly1 and poly2:
            if poly1.power > poly2.power:
                cur.next = PolyNode(poly1.coefficient, poly1.power)
                cur = cur.next
                poly1 = poly1.next
            elif poly1.power < poly2.power:
                cur.next = PolyNode(poly2.coefficient, poly2.power)
                cur = cur.next
                poly2 = poly2.next
            else:
                c = poly1.coefficient + poly2.coefficient
                if c != 0:
                    cur.next = PolyNode(c, poly1.power)
                    cur = cur.next
                poly1 = poly1.next
                poly2 = poly2.next

        rest = poly1 or poly2
        while rest:
            cur.next = PolyNode(rest.coefficient, rest.power)
            cur = cur.next
            rest = rest.next

        return dummy.next
