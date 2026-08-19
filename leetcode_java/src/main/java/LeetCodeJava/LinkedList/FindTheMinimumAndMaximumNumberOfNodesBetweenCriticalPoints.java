package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/find-the-minimum-and-maximum-number-of-nodes-between-critical-points/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  2058. Find the Minimum and Maximum Number of Nodes Between Critical Points
 *  Medium
 *
 *  A critical point in a linked list is defined as either a local maxima or a
 *  local minima.
 *
 *  A node is a local maxima if the current node has a value strictly greater than
 *  the previous node and the next node.
 *
 *  A node is a local minima if the current node has a value strictly smaller than
 *  the previous node and the next node.
 *
 *  Note that a node can only be a local maxima/minima if there exists both a
 *  previous node and a next node.
 *
 *  Given a linked list head, return an array of length 2 containing
 *  [minDistance, maxDistance] where minDistance is the minimum distance between any
 *  two distinct critical points and maxDistance is the maximum distance between any
 *  two distinct critical points. If there are fewer than two critical points,
 *  return [-1, -1].
 *
 *  Example 1:
 *    Input: head = [3,1]
 *    Output: [-1,-1]
 *    Explanation: There are no critical points in [3,1].
 *
 *  Example 2:
 *    Input: head = [5,3,1,2,5,1,2]
 *    Output: [1,3]
 *    Explanation: critical points sit at (1-based) positions 3, 5 and 6.
 *                 minDistance = 6 - 5 = 1, maxDistance = 6 - 3 = 3.
 *
 *  Example 3:
 *    Input: head = [1,3,2,2,3,2,2,2,7]
 *    Output: [3,3]
 *
 *  Constraints:
 *    The number of nodes in the list is in the range [2, 10^5].
 *    1 <= Node.val <= 10^5
 */
public class FindTheMinimumAndMaximumNumberOfNodesBetweenCriticalPoints {

    // V0
    // IDEA: SINGLE PASS, REMEMBER ONLY THE `first` AND `prev` CRITICAL INDEX
    //       slide a 3-node window (a, b, c); b is critical iff
    //       (b < a && b < c) || (b > a && b > c)  -> strict on BOTH sides.
    //
    //       maxDistance is ALWAYS (last critical) - (first critical), so only the
    //       FIRST index needs keeping.
    //       minDistance can only come from two ADJACENT critical points, so only the
    //       PREVIOUS index needs keeping.
    //
    //       NOTE !!! fewer than 2 critical points -> [-1, -1], which is exactly the
    //                case where `first == prev` still holds at the end.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int[] nodesBetweenCriticalPoints(ListNode head) {
        int first = -1;
        int prev = -1;
        int minDist = Integer.MAX_VALUE;
        int i = 0;

        ListNode cur = head;
        while (cur != null && cur.next != null && cur.next.next != null) {
            int a = cur.val;
            int b = cur.next.val;
            int c = cur.next.next.val;

            if ((b < a && b < c) || (b > a && b > c)) {
                if (prev == -1) {
                    first = i;
                    prev = i;
                } else {
                    minDist = Math.min(minDist, i - prev);
                    prev = i;
                }
            }

            i++;
            cur = cur.next;
        }

        if (first == prev) {
            // 0 or 1 critical point
            return new int[]{-1, -1};
        }
        return new int[]{minDist, prev - first};
    }
}
