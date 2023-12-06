package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/merge-k-sorted-lists/

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
  }

public class MergeKSortedLists {

    // TODO : complete below
    // V0
    // IDEA : to array -> sort -> add to new linked list
    public ListNode mergeKLists(ListNode[] lists) {

        if (lists == null || lists.length == 0) {
            // NOTE !! need to return null below
            return null;
        }

        ListNode node = new ListNode();
        ListNode head = node;

        List<Integer> tmp = new ArrayList();
        for (ListNode _node : lists){
            // double check if correct method : _node.next != null
            // (check if node is not null)
            while (_node != null){
                tmp.add(_node.val);
                _node = _node.next;
            }
        }

        //System.out.println(tmp);
        // TODO : double check
        tmp.sort(Comparator.comparing(x -> x));
        System.out.println(tmp);

        for (Integer i : tmp){
            node.next  = new ListNode(i);
            node = node.next;
            //System.out.println(i);
        }

        return head.next;
    }

    // V1'
    // IDEA : MERGE 2 LINKED LIST ONE BY ONE
    // https://leetcode.com/problems/merge-k-sorted-lists/solutions/3285930/100-faster-c-java-python/
    public ListNode mergeKLists_2(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        return mergeKListsHelper(lists, 0, lists.length - 1);
    }

    private ListNode mergeKListsHelper(ListNode[] lists, int start, int end) {
        if (start == end) {
            return lists[start];
        }
        if (start + 1 == end) {
            return merge(lists[start], lists[end]);
        }
        int mid = start + (end - start) / 2;
        ListNode left = mergeKListsHelper(lists, start, mid);
        ListNode right = mergeKListsHelper(lists, mid + 1, end);
        return merge(left, right);
    }

    private ListNode merge(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;

        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }
            curr = curr.next;
        }

        curr.next = (l1 != null) ? l1 : l2;

        return dummy.next;
    }

    // V1''
    // IDEA : ALL METHODS COMPARISON
    // https://leetcode.com/problems/merge-k-sorted-lists/solutions/429518/java-summary-of-all-solutions-b-f-minpq-divide-and-conquer/

    // V1
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/merge-k-sorted-lists/editorial/


    // V2
    // IDEA :  Compare one by one
    // https://leetcode.com/problems/merge-k-sorted-lists/editorial/


    // V3
    // IDEA : Optimize Approach 2 by Priority Queue
    // https://leetcode.com/problems/merge-k-sorted-lists/editorial/


    // V4
    // IDEA : Merge lists one by one
    // https://leetcode.com/problems/merge-k-sorted-lists/editorial/


    // V5
    // IDEA : Merge with Divide And Conquer
    // https://leetcode.com/problems/merge-k-sorted-lists/editorial/

}
