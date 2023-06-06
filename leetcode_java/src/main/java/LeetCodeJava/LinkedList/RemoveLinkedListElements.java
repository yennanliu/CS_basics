package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/remove-linked-list-elements/

import LeetCodeJava.DataStructure.ListNode;

public class RemoveLinkedListElements {

    // V0
    public ListNode removeElements(ListNode head, int val) {

        if (head == null){
            return null;
        }

        if (String.valueOf(val).equals(null)){
            return head;
        }

        ListNode ans = new ListNode();
        ListNode res = ans;

        while(head != null){
            if (head.val != val){
                ans.next = new ListNode(head.val);
                ans = ans.next;
            }

            head = head.next;
        }

        return res.next;
    }

    // V1
    // https://leetcode.com/problems/remove-linked-list-elements/editorial/
    public ListNode removeElements_2(ListNode head, int val) {
        ListNode sentinel = new ListNode(0);
        sentinel.next = head;

        ListNode prev = sentinel, curr = head;
        while (curr != null) {
            if (curr.val == val) prev.next = curr.next;
            else prev = curr;
            curr = curr.next;
        }
        return sentinel.next;
    }

}
