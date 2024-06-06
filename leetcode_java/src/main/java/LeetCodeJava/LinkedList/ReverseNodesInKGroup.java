package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/reverse-nodes-in-k-group/description/
// https://neetcode.io/problems/reverse-nodes-in-k-group

public class ReverseNodesInKGroup {

    // V0
    // TODO : implement

    // V1
    // IDEA : LINKED LIST OP + RECURSIVE
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0025-reverse-nodes-in-k-group.java
    public ListNode reverseKGroup_1(ListNode head, int k) {
        ListNode cur = head;
        int count = 0;
        while (cur != null && count != k) {
            cur = cur.next;
            count++;
        }
        if (count == k) {
            cur = reverseKGroup_1(cur, k);
            while (count-- > 0) { // TODO : check code meaning (if count-- is bigger than 0)
                ListNode temp = head.next;
                head.next = cur;
                cur = head;
                head = temp;
            }
            head = cur;
        }
        return head;
    }

    // V1'
    // IDEA : LINKED LIST OP + ITERATION
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0025-reverse-nodes-in-k-group.java
    public ListNode reverseKGroup_1_1(ListNode head, int k) {
        ListNode dummy = new ListNode(0, head);
        ListNode curr = head;
        ListNode prev = dummy;
        ListNode temp = null;

        int count = k;

        while (curr != null) {
            if (count > 1) {
                temp = prev.next;
                prev.next = curr.next;
                curr.next = curr.next.next;
                prev.next.next = temp;

                count--;
            } else {
                prev = curr;
                curr = curr.next;

                ListNode end = curr;

                for (int i = 0; i < k; ++i) {
                    if (end == null) {
                        return dummy.next;
                    }
                    end = end.next;
                }
                count = k;
            }
        }

        return dummy.next;
    }

}
