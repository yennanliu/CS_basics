package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/swap-nodes-in-pairs/

public class SwapNodesInPairs {


    // V0
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Linked_list/swap-nodes-in-pairs.py
    public ListNode swapPairs(ListNode head) {

        if (head == null || head.next == null){
            return head;
        }

        ListNode dummy  = new ListNode(0);
        dummy.next = head;
        head = dummy;

        // reverse
        while (head.next != null && head.next.next != null){

            ListNode firstNode = head.next;
            ListNode SecondNode = head.next.next;

            firstNode.next = SecondNode.next;
            SecondNode.next = firstNode;
            head.next = SecondNode;
            head = firstNode;
        }

        return dummy.next;
    }


    // V1
    // IDEA : Recursive
    // https://leetcode.com/problems/swap-nodes-in-pairs/editorial/
    public ListNode swapPairs_2(ListNode head) {

        // If the list has no node or has only one node left.
        if ((head == null) || (head.next == null)) {
            return head;
        }

        // Nodes to be swapped
        ListNode firstNode = head;
        ListNode secondNode = head.next;

        // Swapping
        firstNode.next  = swapPairs(secondNode.next);
        secondNode.next = firstNode;

        // Now the head is the second node
        return secondNode;
    }

    
    // V2
    // IDEA : Iterative
    // https://leetcode.com/problems/swap-nodes-in-pairs/editorial/
    public ListNode swapPairs_3(ListNode head) {

        // Dummy node acts as the prevNode for the head node
        // of the list and hence stores pointer to the head node.
        ListNode dummy = new ListNode(-1);
        dummy.next = head;

        ListNode prevNode = dummy;

        while ((head != null) && (head.next != null)) {

            // Nodes to be swapped
            ListNode firstNode = head;
            ListNode secondNode = head.next;

            // Swapping
            prevNode.next = secondNode;
            firstNode.next = secondNode.next;
            secondNode.next = firstNode;

            // Reinitializing the head and prevNode for next swap
            prevNode = firstNode;
            head = firstNode.next; // jump
        }

        // Return the new head node.
        return dummy.next;
    }

}
