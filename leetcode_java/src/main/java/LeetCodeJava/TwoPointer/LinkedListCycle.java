package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/linked-list-cycle/

import LeetCodeJava.DataStructure.ListNode;

import java.util.HashSet;
import java.util.Set;

public class LinkedListCycle {

    // V0
    // IDEA : HASHSET
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

    // V0'
    // IDEA : Hash table
    // https://leetcode.com/problems/linked-list-cycle/editorial/
    public boolean hasCycle_0_1(ListNode head) {
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

    // V0''
    // IDEA : 2 POINTERS
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Two_Pointers/linked-list-cycle.py#L49
    public boolean hasCycle_0_2(ListNode head) {

        if (head == null || head.next == null || head.next.next == null){
            return false;
        }

        /**
         *  NOTE !!!
         *
         *   we move fast, slow pointer first,
         *   then compare if they are the same
         *   (if same, means cyclic linkedlist)
         *
         *
         *   (explanation from gpt)
         *
         *   Impact on Detection:
         *
         *      - Before the Check: This method ensures that fast and slow pointers are always moved before checking for equality. This approach works well for detecting cycles because it ensures that both pointers have progressed further before comparison.
         *
         *      - After the Check: This method checks for equality at the very start of the loop iteration, meaning it checks if they are at the same position before they are moved. This approach is generally less common because it might prematurely check pointers that have not yet advanced enough to potentially meet.
         *
         */
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null){
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow){
                return true;
            }
            /**
             *  NOTE !!!
             *
             *   below is WRONG,
             *   we should move pointers first,
             *   then compare them
             *
             */
            // fast = fast.next.next;
            // slow = slow.next;
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
