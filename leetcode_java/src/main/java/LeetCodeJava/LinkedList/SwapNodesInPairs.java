package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/swap-nodes-in-pairs/

public class SwapNodesInPairs {

    public ListNode swapPairs(ListNode head) {

        if (head == null){
            return head;
        }

        ListNode node = new ListNode();
        ListNode _head = node;

        // reverse


        return _head;
    }

    private void reverseNode(ListNode head){
        ListNode _prev = null;
        ListNode _next = head.next;
        ListNode cur = head;

        cur.next = _prev;
        _next.next = cur;
        


        _next.next = head;
        head.next = _prev;
        //return head;
    }

}
