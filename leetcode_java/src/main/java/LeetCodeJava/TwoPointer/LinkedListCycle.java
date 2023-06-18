package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/linked-list-cycle/

import LeetCodeJava.DataStructure.ListNode;

import java.util.HashSet;
import java.util.Set;

public class LinkedListCycle {

    // V0
    public boolean hasCycle(ListNode head) {

        if (head == null){
            return false;
        }

        HashSet<ListNode> set = new HashSet<>();

        while (head != null){
            ListNode cur = head;
            if (set.contains(cur)){
                return true;
            }
            set.add(cur);
            ListNode _next = head.next;
            head = _next;
        }

        return false;
    }

    // V1
    // IDEA : Hash table
    // https://leetcode.com/problems/linked-list-cycle/editorial/
    public boolean hasCycle_2(ListNode head) {
        Set<ListNode> nodesSeen = new HashSet<>();
        while (head != null) {
            if (nodesSeen.contains(head)) {
                return true;
            }
            nodesSeen.add(head);
            head = head.next;
        }
        return false;
    }

    // V2
    // IDEA :  Floyd's Cycle Finding Algorithm (fast, slow pointer)
    // https://leetcode.com/problems/linked-list-cycle/editorial/
    public boolean hasCycle_3(ListNode head) {
        if (head == null) {
            return false;
        }

        ListNode slow = head;
        ListNode fast = head.next;
        while (slow != fast) {
            if (fast == null || fast.next == null) {
                return false;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return true;
    }

}
