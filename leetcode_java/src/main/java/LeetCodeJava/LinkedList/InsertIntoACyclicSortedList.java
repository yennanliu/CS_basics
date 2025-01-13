package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/insert-into-a-sorted-circular-linked-list/description/
// https://leetcode.ca/all/708.html

import dev.workspace6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 708. Insert into a Cyclic Sorted List
 * Given a node from a cyclic linked list which is sorted in ascending order, write a function to insert a value into the list such that it remains a cyclic sorted list. The given node can be a reference to any single node in the list, and may not be necessarily the smallest value in the cyclic list.
 *
 * If there are multiple suitable places for insertion, you may choose any place to insert the new value. After the insertion, the cyclic list should remain sorted.
 *
 * If the list is empty (i.e., given node is null), you should create a new single cyclic list and return the reference to that single node. Otherwise, you should return the original given node.
 *
 * The following example may help you understand the problem better:
 *
 *
 *
 *
 *
 * In the figure above, there is a cyclic sorted list of three elements. You are given a reference to the node with value 3, and we need to insert 2 into the list.
 *
 *
 *
 *
 * The new node should insert between node 1 and node 3. After the insertion, the list should look like this, and we should still return node 3.
 *
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Bloomberg Facebook Google Microsoft Quip (Salesforce)
 *
 */
public class InsertIntoACyclicSortedList {

    class Node {
        public int val;
        public Node next;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _next) {
            val = _val;
            next = _next;
        }
    };

  // V0
  // TODO: fix below
  //    public Node insert(Node head, int insertVal) {
  //
  //        // edge
  //        if(head == null){
  //            return new Node(insertVal);
  //        }
  //
  //        // get all elements
  //        List<Integer> list = new ArrayList<>();
  //        Node head2 = head;
  //        while(head2 != null){
  //            list.add(head2.val);
  //            head2 = head2.next;
  //        }
  //
  //        // sort (assume the ordering is `same` as the order we tranverse linked list)
  //        Collections.sort(list);
  //
  //        // edge case 2) if new element < all elements in list or > all elements list
  //        if (insertVal < list.get(0)){
  //            Node res = new Node(insertVal);
  //            res.next = head;
  //            return res;
  //        }
  //
  //        if (insertVal > list.get(list.size()-1)){
  //            while(head != null){
  //                head = head.next;
  //            }
  //            head.next = new Node(insertVal);
  //            return head; // ?? or need to define a `res` object ?
  //        }
  //
  //        Node res = head;
  //        int idx = 0;
  //        while(head != null){
  //            int x = list.get(idx);
  //            if (head.next.val != x){
  //                Node _new = new Node(x);
  //                Node _next = head.next;
  //                Node _cur = head;
  //                head.next = _new;
  //                _new.next = _next;
  //                //head = head.next;
  //                break;
  //            }
  //            head = head.next;
  //            idx += 1;
  //        }
  //
  //        return res;
  //    }

  // V1
  // https://leetcode.ca/2017-11-07-708-Insert-into-a-Sorted-Circular-Linked-List/
  // IDEA: LINKED LIST
  /**
   *  Key Notes About the Code
   *
   * 	•	Cyclic Nature Handling:
   * 	    •	The loop ensures traversal through all nodes by checking curr != head. If it reaches back to the head, it stops, ensuring we don’t loop indefinitely.
   * 	    •	Robust Conditions:
   *
   * 	•	The combination of conditions handles:
   * 	    1.	Insertion in the middle of the sorted list.
   * 	    2.	Insertion at the end-to-start transition point.
   * 	    3.	Insertion when all nodes have the same value (handled implicitly since the loop eventually exits, and the new node is inserted anywhere).
   *
   */
  public Node insert_1(Node head, int insertVal) {
        Node node = new Node(insertVal);
        if (head == null) {
            node.next = node;
            return node;
        }
        Node prev = head, curr = head.next;
        while (curr != head) {
      /**
       * NOTE !!!
       *
       * below conditions:
       *
       *    - Case 1) prev.val <= insertVal && insertVal <= curr.val
       *       -> The value fits between two consecutive nodes in ascending order.
       *    or
       *    - Case 2) prev.val > curr.val && (insertVal >= prev.val || insertVal <= curr.val))
       *      ->
       *         - The prev.val > curr.val condition identifies the “end-to-start”
       *           transition point in the cyclic list
       *           (i.e., the largest node is followed by the smallest node).
       *
       * 	     - If insertVal is greater than or equal to the largest value (prev.val)
       * 	       or less than or equal to the smallest value (curr.val),
       * 	       it should be inserted here.
       *
       */
      if ((prev.val <= insertVal && insertVal <= curr.val)
          || (prev.val > curr.val && (insertVal >= prev.val || insertVal <= curr.val))) {
                break;
            }
            prev = curr;
            curr = curr.next;
        }


        // NOTE !!! below OP is out of while loop
        // NOTE !!! node is the new node with insertVal val ( Node node = new Node(insertVal);)
        prev.next = node;
        node.next = curr;

        return head;
    }

    // V2-1
    // IDEA: LINKED LIST (gpt)
    // TODO: validate
    public Node insert_2_1(Node head, int insertVal) {
        // Edge case: If the list is empty, create a new node pointing to itself
        if (head == null) {
            Node newNode = new Node(insertVal);
            newNode.next = newNode;
            return newNode;
        }

        Node prev = head;
        Node curr = head.next;
        boolean inserted = false;

        do {
            // Case 1: Normal insertion in sorted order
            if (prev.val <= insertVal && insertVal <= curr.val) {
                prev.next = new Node(insertVal, curr);
                inserted = true;
                break;
            }

            // Case 2: Insert at the end of the list where the largest value is followed by the smallest value
            if (prev.val > curr.val) {
                if (insertVal >= prev.val || insertVal <= curr.val) {
                    prev.next = new Node(insertVal, curr);
                    inserted = true;
                    break;
                }
            }

            // Move to the next pair of nodes
            prev = curr;
            curr = curr.next;

        } while (prev != head);

        // Case 3: If no suitable place was found (e.g., all values are the same in the list)
        if (!inserted) {
            prev.next = new Node(insertVal, curr);
        }

        return head;
    }

    // V2-1
    // IDEA: LINKED LIST (while loop)(gpt)
    // TODO: validate
    public Node insert_2_2(Node head, int insertVal) {
        // Edge case: If the list is empty, create a new node pointing to itself
        if (head == null) {
            Node newNode = new Node(insertVal);
            newNode.next = newNode;
            return newNode;
        }

        Node prev = head;
        Node curr = head.next;
        boolean inserted = false;

        while (true) {
            // Case 1: Normal insertion in sorted order
            if (prev.val <= insertVal && insertVal <= curr.val) {
                prev.next = new Node(insertVal, curr);
                inserted = true;
                break;
            }

            // Case 2: Insert at the end of the list where the largest value is followed by the smallest value
            if (prev.val > curr.val) {
                if (insertVal >= prev.val || insertVal <= curr.val) {
                    prev.next = new Node(insertVal, curr);
                    inserted = true;
                    break;
                }
            }

            // Move to the next pair of nodes
            prev = curr;
            curr = curr.next;

            // If we've completed a full cycle and haven't inserted yet
            if (prev == head) {
                break;
            }
        }

        // Case 3: If no suitable place was found (e.g., all values are the same in the list)
        if (!inserted) {
            prev.next = new Node(insertVal, curr);
        }

        return head;
    }

}
