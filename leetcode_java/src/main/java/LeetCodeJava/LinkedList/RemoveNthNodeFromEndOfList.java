package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/remove-nth-node-from-end-of-list/

import LeetCodeJava.DataStructure.ListNode;

public class RemoveNthNodeFromEndOfList {

    // V0
    // IDEA : 2 POINTERS
    // https://www.youtube.com/watch?v=oMzRxtXvDbA&list=PL-qDGN2q6cbClYCRpJ2pyrlhYK9VjZmL3&index=13
    public ListNode removeNthFromEnd(ListNode head, int n) {

        if (head == null){
            return head;
        }

        if (head.next == null && head.val == n){
            return null;
        }

        // move fast pointer only with n+1 step
        // 2 cases:
        //   - 1) node count is even
        //   - 2) node count is odd
        /** NOTE !! we init dummy pointer, and let fast, slow pointers point to it */
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        // NOTE here
        ListNode fast = dummy;
        ListNode slow = dummy;
        // NOTE !!! we let fast pointer move N+1 step first
        // so once fast pointers reach the end after fast, slow pointers move together
        // we are sure that slow pointer is at N-1 node
        // so All we need to do is :
        // point slow.next to slow.next.next
        // then we remove N node from linked list
        for (int i = 1; i <= n+1; i++){
            System.out.println("i = " + i);
            fast = fast.next;
        }

        // move fast and slow pointers on the same time
        while (fast != null){
            fast = fast.next;
            slow = slow.next;
        }

        // NOTE here
        slow.next = slow.next.next;
        // NOTE !!! we return dummy.next instead of slow
        return dummy.next;
    }

    // V1
    // IDEA :  Two pass algorithm
    // https://leetcode.com/problems/remove-nth-node-from-end-of-list/editorial/
    public ListNode removeNthFromEnd_2(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        int length  = 0;
        ListNode first = head;
        while (first != null) {
            length++;
            first = first.next;
        }
        length -= n;
        first = dummy;
        while (length > 0) {
            length--;
            first = first.next;
        }
        first.next = first.next.next;
        return dummy.next;
    }

    // V2
    // https://leetcode.com/problems/remove-nth-node-from-end-of-list/editorial/
    public ListNode removeNthFromEnd_3(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode first = dummy;
        ListNode second = dummy;
        // Advances first pointer so that the gap between first and second is n nodes apart
        for (int i = 1; i <= n + 1; i++) {
            first = first.next;
        }
        // Move first to the end, maintaining the gap
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }

}
