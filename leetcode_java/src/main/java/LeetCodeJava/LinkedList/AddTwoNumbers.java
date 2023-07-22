package LeetCodeJava.LinkedList;

import LeetCodeJava.DataStructure.ListNode;

public class AddTwoNumbers {

    // V0
    // IDEA : STACK + number op
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        if (l1 == null && l2 == null){
            return l1;
        }

        if (l1 == null && l2 != null){
            return l2;
        }

        if (l1 != null && l2 == null){
            return l1;
        }

        ListNode dummy = new ListNode();
        ListNode node = dummy;

        int extra = 0;
        int tmp = 0;

        while (l1 != null || l2 != null){
            tmp += extra;
            extra = 0;
            if (l1 != null){
                tmp += l1.val;
                l1 = l1.next;
            }
            if (l2 != null){
                tmp += l2.val;
                l2 = l2.next;
            }
            if (tmp >= 10){
                tmp -= 10;
                extra = 1;
            }

            node.next = new ListNode(tmp);
            node = node.next;
            tmp = 0;
        }

        if (extra != 0){
            node.next = new ListNode(extra);
            node = node.next;
        }

        return dummy.next;
    }

    // V1
    // https://leetcode.com/problems/add-two-numbers/editorial/
    // IDEA : Math
    // Add Two Numbers (Java improved)
    public ListNode addTwoNumbers_2(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(0);
        ListNode curr = dummyHead;
        int carry = 0;
        while (l1 != null || l2 != null || carry != 0) {
            int x = (l1 != null) ? l1.val : 0;
            int y = (l2 != null) ? l2.val : 0;
            int sum = carry + x + y;
            carry = sum / 10;
            curr.next = new ListNode(sum % 10);
            curr = curr.next;
            if (l1 != null)
                l1 = l1.next;
            if (l2 != null)
                l2 = l2.next;
        }
        return dummyHead.next;
    }

}
