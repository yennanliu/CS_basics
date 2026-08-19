package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/winner-of-the-linked-list-game/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  3062. Winner of the Linked List Game
 *  Easy
 *  (premium / locked)
 *
 *  You are given the head of a linked list of even length containing integers.
 *  Each odd-indexed node contains an odd integer and each even-indexed node
 *  contains an even integer.
 *
 *  We call each even-indexed node and its next node a pair, e.g., the nodes with
 *  indices 0 and 1 are a pair, the nodes with indices 2 and 3 are a pair, and so on.
 *
 *  For every pair, we compare the values of the nodes in the pair:
 *    If the odd-indexed node is higher, the "Odd" team gets a point.
 *    If the even-indexed node is higher, the "Even" team gets a point.
 *
 *  Return the name of the team with the higher points, if the points are equal,
 *  return "Tie".
 *
 *  Example 1:
 *    Input: head = [2,1]
 *    Output: "Even"
 *    Explanation: only pair is (2,1), 2 > 1 so the Even team gets the point.
 *
 *  Example 2:
 *    Input: head = [2,5,4,7,20,5]
 *    Output: "Odd"
 *    Explanation: (2,5) -> Odd, (4,7) -> Odd, (20,5) -> Even. Odd 2 : 1 Even.
 *
 *  Example 3:
 *    Input: head = [4,5,2,1]
 *    Output: "Tie"
 *
 *  Constraints:
 *    The number of nodes in the list is in the range [2, 100].
 *    The number of nodes in the list is even.
 *    1 <= Node.val <= 100
 */
public class WinnerOfTheLinkedListGame {

    // V0
    // IDEA: ONE PASS, 2 NODES AT A TIME
    //       values are guaranteed distinct within a pair (one odd, one even), so
    //       every pair scores exactly one point. keep a single running balance
    //       (+1 even wins, -1 odd wins) and read off the sign at the end.
    /**
     * time = O(N)
     * space = O(1)
     */
    public String gameResult(ListNode head) {
        int diff = 0; // (# even wins) - (# odd wins)
        ListNode cur = head;
        while (cur != null && cur.next != null) {
            if (cur.val > cur.next.val) {
                diff++;
            } else {
                diff--;
            }
            cur = cur.next.next;
        }

        if (diff > 0) {
            return "Even";
        }
        if (diff < 0) {
            return "Odd";
        }
        return "Tie";
    }
}
