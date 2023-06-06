package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/intersection-of-two-linked-lists/

import LeetCodeJava.DataStructure.ListNode;

import java.util.HashSet;
import java.util.Set;

public class IntersectionOfTwoLinkedLists {

    // V0

    // V1
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/intersection-of-two-linked-lists/editorial/
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        while (headA != null) {
            ListNode pB = headB;
            while (pB != null) {
                if (headA == pB) return headA;
                pB = pB.next;
            }
            headA = headA.next;
        }
        return null;
    }

    // V2
    // IDEA : HASH TABLE
    // https://leetcode.com/problems/intersection-of-two-linked-lists/editorial/
    public ListNode getIntersectionNode_2(ListNode headA, ListNode headB) {
        Set<ListNode> nodesInB = new HashSet<ListNode>();

        while (headB != null) {
            nodesInB.add(headB);
            headB = headB.next;
        }

        while (headA != null) {
            // if we find the node pointed to by headA,
            // in our set containing nodes of B, then return the node
            if (nodesInB.contains(headA)) {
                return headA;
            }
            headA = headA.next;
        }

        return null;
    }

    // V3
    // IDEA : TWO POINTERS
    // https://leetcode.com/problems/intersection-of-two-linked-lists/editorial/
    public ListNode getIntersectionNode_3(ListNode headA, ListNode headB) {
        ListNode pA = headA;
        ListNode pB = headB;
        while (pA != pB) {
            pA = pA == null ? headB : pA.next;
            pB = pB == null ? headA : pB.next;
        }
        return pA;
        // Note: In the case lists do not intersect, the pointers for A and B
        // will still line up in the 2nd iteration, just that here won't be
        // a common node down the list and both will reach their respective ends
        // at the same time. So pA will be NULL in that case.
    }

}
