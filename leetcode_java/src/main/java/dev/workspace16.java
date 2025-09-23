package dev;

import LeetCodeJava.DataStructure.ListNode;

import java.util.HashSet;

public class workspace16 {

    // LC 876
    // 14.50 - 15.00 pm
    /**
     *  IDEA: LINKED LIST
     *
     */
    public ListNode middleNode(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return head;
        }

        // fast, slow pointers
        ListNode fast = head; // ??
        ListNode slow = head; // ??

        while(fast != null && fast.next != null){
            fast = fast.next.next;
            slow = slow.next;
        }

        return slow;
    }

    // LC 142
    // 14.57 - 15.10 pm
    /**
     *
     *  -> return the node where the cycle begins
     *  -> if NO cycle, return null
     *
     *  IDEA 1) LINKED LIST OP
     *
     */
    public ListNode detectCycle(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return null;
        }
        HashSet<ListNode> set = new HashSet<>();
        while(head != null){
            if(set.contains(head)){
                return head; // ???
            }
            set.add(head);
            head = head.next;
        }

        return null;
    }

    // LC 82
    // 9.33 - 9.43 am
    /**
     *  IDEA 1) LINKED LIST OP
     *
     *
     *
     *
     */
    public ListNode deleteDuplicates(ListNode head) {

        return null;
    }

}
