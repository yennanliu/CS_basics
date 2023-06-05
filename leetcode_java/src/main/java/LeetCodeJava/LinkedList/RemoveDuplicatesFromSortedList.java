package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/remove-duplicates-from-sorted-list/

import java.util.HashSet;
import java.util.Set;

public class RemoveDuplicatesFromSortedList {

    // V0
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
