package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/add-two-polynomials-represented-as-linked-lists/

/**
 *  1634. Add Two Polynomials Represented as Linked Lists
 *  Medium
 *
 *  A polynomial linked list is a special type of linked list where every node
 *  represents a term in a polynomial expression.
 *
 *  Each node has three attributes:
 *   - coefficient: an integer representing the number multiplier of the term.
 *                  The coefficient of the term 9x^4 is 9.
 *   - power: an integer representing the exponent. The power of the term 9x^4 is 4.
 *   - next: a pointer to the next node in the list, or null if it is the last node.
 *
 *  For example, the polynomial 5x^3 + 4x - 7 is represented by the polynomial
 *  linked list [[5,3],[4,1],[-7,0]].
 *
 *  The polynomial linked list must be in its standard form: the polynomial must be
 *  in strictly descending order by its power value. Also, terms with a coefficient
 *  of 0 are omitted.
 *
 *  Given two polynomial linked list heads, poly1 and poly2, add the polynomials
 *  together and return the head of the sum of the polynomials.
 *
 *  Example 1:
 *    Input: poly1 = [[1,1]], poly2 = [[1,0]]
 *    Output: [[1,1],[1,0]]
 *    Explanation: poly1 = x. poly2 = 1. The sum is x + 1.
 *
 *  Example 2:
 *    Input: poly1 = [[2,2],[4,1],[3,0]], poly2 = [[3,2],[-4,1],[-1,0]]
 *    Output: [[5,2],[2,0]]
 *    Explanation: poly1 = 2x^2 + 4x + 3. poly2 = 3x^2 - 4x - 1.
 *                 The sum is 5x^2 + 2. Notice that we omit the "0x" term.
 *
 *  Example 3:
 *    Input: poly1 = [[1,2]], poly2 = [[-1,2]]
 *    Output: []
 *    Explanation: The sum is 0. We return an empty list.
 *
 *  Constraints:
 *    0 <= n <= 10^4
 *    -10^9 <= PolyNode.coefficient <= 10^9
 *    PolyNode.coefficient != 0
 *    0 <= PolyNode.power <= 10^9
 *    PolyNode.power > PolyNode.next.power
 */
public class AddTwoPolynomialsRepresentedAsLinkedLists {

    // Definition for polynomial singly-linked list.
    public static class PolyNode {
        public int coefficient;
        public int power;
        public PolyNode next;

        public PolyNode() {
            this.coefficient = 0;
            this.power = 0;
            this.next = null;
        }

        public PolyNode(int x, int y) {
            this.coefficient = x;
            this.power = y;
            this.next = null;
        }

        public PolyNode(int x, int y, PolyNode next) {
            this.coefficient = x;
            this.power = y;
            this.next = next;
        }
    }

    // V0
    // IDEA: MERGE TWO SORTED LISTS (both strictly descending by power)
    //       - poly1.power > poly2.power -> take poly1's term
    //       - poly1.power < poly2.power -> take poly2's term
    //       - equal powers              -> emit the SUM of coefficients, advance both
    //       NOTE: a summed coefficient of 0 must be DROPPED (standard form omits
    //             zero terms) -> that is the whole trick of example 3.
    //       NOTE: build onto a dummy head so the empty-result case is free.
    /**
     * time = O(M + N)
     * space = O(1)   // ignoring the output list
     */
    public PolyNode addPoly(PolyNode poly1, PolyNode poly2) {
        PolyNode dummy = new PolyNode();
        PolyNode cur = dummy;

        while (poly1 != null && poly2 != null) {
            if (poly1.power > poly2.power) {
                cur.next = new PolyNode(poly1.coefficient, poly1.power);
                cur = cur.next;
                poly1 = poly1.next;
            } else if (poly1.power < poly2.power) {
                cur.next = new PolyNode(poly2.coefficient, poly2.power);
                cur = cur.next;
                poly2 = poly2.next;
            } else {
                // NOTE !!! coefficients can reach 2 * 10^9 -> use long, then narrow
                long c = (long) poly1.coefficient + (long) poly2.coefficient;
                if (c != 0) {
                    cur.next = new PolyNode((int) c, poly1.power);
                    cur = cur.next;
                }
                poly1 = poly1.next;
                poly2 = poly2.next;
            }
        }

        PolyNode rest = (poly1 != null) ? poly1 : poly2;
        while (rest != null) {
            cur.next = new PolyNode(rest.coefficient, rest.power);
            cur = cur.next;
            rest = rest.next;
        }

        return dummy.next;
    }
}
