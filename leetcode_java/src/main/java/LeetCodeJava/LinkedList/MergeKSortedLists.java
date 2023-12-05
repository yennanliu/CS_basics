package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/merge-k-sorted-lists/

import java.util.ArrayList;
import java.util.Arrays;
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

        if (lists == null || lists.length == 0){
            return new ListNode();
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

        System.out.println(tmp);
        //tmp.forEach(x -> {System.out.println(x);});
        //Arrays.sort(tmp);
        //ArrayList.sort(tmp);
        // TODO : double check
        tmp.sort(Comparator.comparing(x -> x));
        System.out.println(tmp);

        for (Integer i : tmp){
            node.val = i;
            node = node.next;
        }

        return head;
    }

}
