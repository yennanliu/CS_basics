package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/rotate-list/description/
/**
 *  61. Rotate List
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the head of a linked list, rotate the list to the right by k places.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,2,3,4,5], k = 2
 * Output: [4,5,1,2,3]
 * Example 2:
 *
 *
 * Input: head = [0,1,2], k = 4
 * Output: [2,0,1]
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is in the range [0, 500].
 * -100 <= Node.val <= 100
 * 0 <= k <= 2 * 109
 */

import LeetCodeJava.DataStructure.ListNode;

import java.util.ArrayDeque;
import java.util.Deque;

public class RotateList {

    // V0
//    public ListNode rotateRight(ListNode head, int k) {
//
//    }

    // V0-1
    // IDEA: LIST NODE -> DEQUEUE -> ROTATE OP
    public ListNode rotateRight_0_1(ListNode head, int k) {
        // edge
        if (k == 0 || head == null || head.next == null) {
            return head;
        }

        Deque<Integer> dequeue = new ArrayDeque<>(); // ???
        ListNode head2 = head;

        // add to dqeueue
        while (head != null) {
            dequeue.add(head.val);
            head = head.next;

        }

        int len = dequeue.size();
//        System.out.println(">>> (before normalized) k = " + k);
//        System.out.println(">>> len = " + len);

        // edge
        if (k >= len) {
            if (len % k == 0) {
                return head2;
            }
            // `normalize` k
            k = (k % len);
        }

        //System.out.println(">>> (after normalized) k = " + k);

        // `rotate`
        int i = 0;
        while (!dequeue.isEmpty() && i < k) {
            // pop `last`
            int val = dequeue.pollLast(); // ??
            // append pop val to first idx
            dequeue.addFirst(val);
            i += 1;
        }

        ListNode dummy = new ListNode();
        ListNode res = dummy;
        System.out.println(">>> dequeue = " + dequeue);

        while (!dequeue.isEmpty()) {
            ListNode cur = new ListNode(dequeue.pollFirst());
            dummy.next = cur;
            dummy = cur;
        }

        return res.next;
    }

    //  V1
    // https://leetcode.ca/2016-01-30-61-Rotate-List/
    public ListNode rotateRight_1(ListNode head, int k) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode cur = head;
        int n = 0;
        for (; cur != null; cur = cur.next) {
            n++;
        }
        k %= n;
        if (k == 0) {
            return head;
        }
        ListNode fast = head;
        ListNode slow = head;
        while (k-- > 0) {
            fast = fast.next;
        }
        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }
        ListNode ans = slow.next;
        slow.next = null;
        fast.next = head;
        return ans;
    }

    // V2
    // https://leetcode.com/problems/rotate-list/solutions/6870528/video-find-new-head-by-niits-p22w/
    public ListNode rotateRight_2(ListNode head, int k) {
        if (head == null)
            return head;

        int length = 1;
        ListNode dummy = head;

        while (dummy.next != null) {
            dummy = dummy.next;
            length++;
        }

        int position = k % length;
        if (position == 0)
            return head;

        ListNode current = head;
        for (int i = 0; i < length - position - 1; i++) {
            current = current.next;
        }

        ListNode newHead = current.next;
        current.next = null;
        dummy.next = head;

        return newHead;
    }

    // V3
    // https://leetcode.com/problems/rotate-list/solutions/7212053/rotate-list-java-solution-reversal-appro-sz3q/
    public ListNode rotateRight_3(ListNode head, int k) {
        if (head == null || head.next == null || k == 0)
            return head;

        // Step 1: Find length
        int length = 0;
        ListNode node = head;
        while (node != null) {
            length++;
            node = node.next;
        }

        // Step 2: Adjust k
        k = k % length;
        if (k == 0)
            return head;

        // Step 3: Reverse entire list
        ListNode past = null, present = head, future = null;
        while (present != null) {
            future = present.next;
            present.next = past;
            past = present;
            present = future;
        }

        // Now 'past' is the head of reversed list
        ListNode h1 = null;
        ListNode curr = past;

        // Step 4a: Reverse first k nodes
        int count = k;
        while (curr != null && count != 0) {
            ListNode n = curr.next;
            curr.next = h1;
            h1 = curr;
            curr = n;
            count--;
        }

        // Step 4b: Reverse remaining nodes
        ListNode h2 = null;
        while (curr != null) {
            ListNode n = curr.next;
            curr.next = h2;
            h2 = curr;
            curr = n;
        }

        // Step 5: Merge h1 and h2
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        while (h1 != null) {
            tail.next = h1;
            h1 = h1.next;
            tail = tail.next;
        }
        while (h2 != null) {
            tail.next = h2;
            h2 = h2.next;
            tail = tail.next;
        }

        return dummy.next;
    }


}
