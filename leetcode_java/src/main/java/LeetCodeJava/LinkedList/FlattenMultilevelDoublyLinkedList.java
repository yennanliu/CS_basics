package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/description/

import java.util.LinkedList;
import java.util.Queue;

/**
 * 430. Flatten a Multilevel Doubly Linked List
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given a doubly linked list, which contains nodes that have a next pointer, a previous pointer, and an additional child pointer. This child pointer may or may not point to a separate doubly linked list, also containing these special nodes. These child lists may have one or more children of their own, and so on, to produce a multilevel data structure as shown in the example below.
 * <p>
 * Given the head of the first level of the list, flatten the list so that all the nodes appear in a single-level, doubly linked list. Let curr be a node with a child list. The nodes in the child list should appear after curr and before curr.next in the flattened list.
 * <p>
 * Return the head of the flattened list. The nodes in the list must have all of their child pointers set to null.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * <p>
 * Input: head = [1,2,3,4,5,6,null,null,null,7,8,9,10,null,null,11,12]
 * Output: [1,2,3,7,8,11,12,9,10,4,5,6]
 * Explanation: The multilevel linked list in the input is shown.
 * After flattening the multilevel linked list it becomes:
 * <p>
 * Example 2:
 * <p>
 * <p>
 * Input: head = [1,2,null,3]
 * Output: [1,3,2]
 * Explanation: The multilevel linked list in the input is shown.
 * After flattening the multilevel linked list it becomes:
 * <p>
 * Example 3:
 * <p>
 * Input: head = []
 * Output: []
 * Explanation: There could be empty list in the input.
 * <p>
 * <p>
 * Constraints:
 * <p>
 * The number of Nodes will not exceed 1000.
 * 1 <= Node.val <= 105
 * <p>
 * <p>
 * How the multilevel linked list is represented in test cases:
 * <p>
 * We use the multilevel linked list from Example 1 above:
 * <p>
 * 1---2---3---4---5---6--NULL
 * |
 * 7---8---9---10--NULL
 * |
 * 11--12--NULL
 * The serialization of each level is as follows:
 * <p>
 * [1,2,3,4,5,6,null]
 * [7,8,9,10,null]
 * [11,12,null]
 * To serialize all levels together, we will add nulls in each level to signify no node connects to the upper node of the previous level. The serialization becomes:
 * <p>
 * [1,    2,    3, 4, 5, 6, null]
 * |
 * [null, null, 7,    8, 9, 10, null]
 * |
 * [            null, 11, 12, null]
 * Merging the serialization of each level and removing trailing nulls we obtain:
 * <p>
 * [1,2,3,4,5,6,null,null,null,7,8,9,10,null,null,11,12]
 */
public class FlattenMultilevelDoublyLinkedList {

    /*
    // Definition for a Node.
    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };
    */

    // V0
    // TODO : implement
//    public Node flatten(Node head) {
//
//    }

    // V1
    // https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/solutions/5667355/easy-java-solution/
    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };

    private Queue<Node> store = new LinkedList<>();

    public void helper(Node head) {
        Node temp;
        while (head != null) {
            temp = head.next;
            head.next = null;
            head.prev = null;
            store.offer(head);
            if (head.child != null)
                helper(head.child);
            head.child = null;
            head = temp;
        }
    }

    public Node flatten_1(Node head) {
        helper(head);
        if (store.peek() == null)
            return head;
        Node retval = store.poll();
        Node first = retval;
        while (store.peek() != null) {
            Node second = store.poll();
            first.next = second;
            second.prev = first;
            first = second;
        }
        first.next = null;
        return retval;
    }


    // V2
    // IDEA : LINKED LIST + CUSTOM CLASS
    // https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/solutions/4031041/single-pass-solution-using-custom-class/
    class lc430Helper {
        Node head;
        Node tail;
    }

    public Node flatten_2(Node head) {
        return util1(head).head;
    }

    public lc430Helper util1(Node head) {
        Node dummy = new Node();
        dummy.next = head;
        Node temp = dummy;
        while (temp.next != null) {
            temp = temp.next;
            if (temp.child == null) {
                continue;
            } else {
                lc430Helper bottom = util1(temp.child);
                Node temp2 = temp.next;
                temp.child = null;
                temp.next = bottom.head;
                bottom.head.prev = temp;
                bottom.tail.next = temp2;
                if (temp2 != null) {
                    temp2.prev = bottom.tail;
                }
                temp = bottom.tail;
            }
        }
        lc430Helper ans = new lc430Helper();
        dummy.next = null;
        ans.head = head;
        ans.tail = temp;
        return ans;
    }

    // V3
    // https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/solutions/5328452/easy-to-understand-best-solution/
    public Node flatten_3(Node head) {
        Node temp = head;
        while(temp != null){
            Node list1Tail = temp;
            Node list3Head = temp.next;
            // if the node has child node then append all its node in between

            if(temp.child != null){
                // we are assuming that recursion will give us flatten output, so we just need to adjust the pointers
                Node list2Head = flatten_3(temp.child);

                // find list2 tail
                Node list2Tail = list2Head;
                while(list2Tail.next != null){
                    list2Tail = list2Tail.next;
                }

                // attach the lists
                list1Tail.next = list2Head;
                list2Head.prev = list1Tail;
                list2Tail.next = list3Head;
                if(list3Head != null)
                    list3Head.prev = list2Tail;
                temp.child = null;
            }
            temp = temp.next;
        }
        return head;
    }

}
