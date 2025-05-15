package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/remove-duplicates-from-sorted-list/
/**
 * 83. Remove Duplicates from Sorted List
 * Solved
 * Easy
 * Topics
 * Companies
 * Given the head of a sorted linked list, delete all duplicates such that each element appears only once. Return the linked list sorted as well.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,1,2]
 * Output: [1,2]
 * Example 2:
 *
 *
 * Input: head = [1,1,2,3,3]
 * Output: [1,2,3]
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is in the range [0, 300].
 * -100 <= Node.val <= 100
 * The list is guaranteed to be sorted in ascending order.
 *
 */
import LeetCodeJava.DataStructure.ListNode;

import java.util.HashSet;
import java.util.Set;

public class RemoveDuplicatesFromSortedList {

    // V0
    // IDEA: HASHSET + LINKED LIST OP
    public ListNode deleteDuplicates(ListNode head) {

        ListNode ans = new ListNode();
        ListNode node = ans;
        Set<Integer> set = new HashSet<>();

        while (head != null){
            Integer cur = head.val;
            if (!set.contains(cur)){
                set.add(cur);
                node.next = new ListNode(cur);
                node = node.next;
            }
            head = head.next;
        }

        return ans.next;
    }

    // V0-1
    // IDEA: FAST, SLOW POINTER, LC 26
    public ListNode deleteDuplicates_0_1(ListNode head) {
        // edge
        if(head == null){
            return head;
        }

        ListNode fast = head;
        ListNode slow = head;

        while(fast != null){

            // NOTE !!! below
            if(fast.val != slow.val){

                slow.next = fast;
                slow = slow.next;
            }

            fast = fast.next;
        }

        // NOTE !!! below
        slow.next = null;

        return head; // ???
    }


    //  V1
    // https://leetcode.com/problems/remove-duplicates-from-sorted-list/solutions/3257316/c-python-c-java-easiest-solution-o-n-time/
    public ListNode deleteDuplicates_2(ListNode head) {
        ListNode temp = head;
        while (temp != null && temp.next != null)
        {
            if (temp.next.val==temp.val)
            {
                temp.next=temp.next.next;
                continue;
            }
            temp=temp.next;
        }
        return head;
    }

}
