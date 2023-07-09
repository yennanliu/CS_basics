package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/design-linked-list/

import LeetCodeJava.DataStructure.ListNode;
import java.util.LinkedList;
import java.util.List;

/**
 * Your MyLinkedList object will be instantiated and called as such:
 * MyLinkedList obj = new MyLinkedList();
 * int param_1 = obj.get(index);
 * obj.addAtHead(val);
 * obj.addAtTail(val);
 * obj.addAtIndex(index,val);
 * obj.deleteAtIndex(index);
 */

public class DesignLinkedList {

    // V0
//    class MyLinkedList {
//
//        // attr
//        private int size;
//        private ListNode head;
//
//        // constructor
//        public MyLinkedList() {
//
//            this.size = 0;
//            this.head = new ListNode();
//            this.head.next = null;
//        }
//
//        public int get(int index) throws Exception {
//
//            if (index > this.size || index < 0){
//                throw new Exception("invalid index");
//            }
//            int cur = 0;
//            ListNode tmp = this.head;
//            while(cur < index){
//                cur += 1;
//                tmp = tmp.next;
//            }
//            return tmp.val;
//        }
//
//        public void addAtHead(int val) {
//
//            //ListNode dummy = new ListNode();
//            ListNode newNode = new ListNode(val);
//            //dummy.next = newNode;
//            newNode.next = this.head;
//            this.size += 1;
//            return;
//        }
//
//        public void addAtTail(int val) {
//
//            while (this.head != null){
//                this.head = this.head.next;
//            }
//            this.head.next = new ListNode(val);
//            this.size += 1;
//            return;
//        }
//
//        public void addAtIndex(int index, int val) throws Exception {
//
//            if (index > this.size || index < 0){
//                throw new Exception("invalid index");
//            }
//
//            // case 1: index = 0
//            if (index == 0){
//                this.addAtHead(val);
//            }
//
//            // case 2: index = size
//            else if (index == this.size){
//                this.addAtTail(val);
//            }
//
//            // case 3: 0 < index < size
//            int cur = 0;
//            while (cur < index - 1){
//                cur += 1;
//                this.head = this.head.next;
//            }
//            ListNode _next = this.head.next.next;
//            ListNode newNode = new ListNode(val);
//            this.head.next = newNode;
//            newNode.next = _next;
//        }
//
//        public void deleteAtIndex(int index) {
//
//            if (index > this.size){
//                return;
//            }
//
//            int cur = 0;
//            int cur2 = 0;
//
//            // case 1 : index = 0
//            if (index == 0){
//                ListNode dummy = new ListNode();
//                ListNode _next = this.head.next;
//                dummy.next = _next;
//                this.size -= 1;
//                return;
//            }
//
//            // case 2 : index = size
//            else if (index == this.size) {
//                while (cur < this.size-1){
//                    cur += 1;
//                    this.head = this.head.next;
//                }
//                this.head.next = new ListNode();
//                this.size -= 1;
//                return;
//            }
//
//            // case 3 :  0 < index < size
//            else{
//                while (cur < this.size-1){
//                    cur += 1;
//                    this.head = this.head.next;
//                }
//                ListNode _next = this.head.next.next;
//                this.head.next = _next;
//                this.size -= 1;
//                return;
//            }
//        }
//
//    }

    // V1
    // IDEA : Singly Linked List
    // https://leetcode.com/problems/design-linked-list/editorial/
    class MyLinkedList_2 {
        int size;
        ListNode head;  // sentinel node as pseudo-head
        public MyLinkedList_2() {
            size = 0;
            head = new ListNode(0);
        }

        /** Get the value of the index-th node in the linked list. If the index is invalid, return -1. */
        public int get(int index) {
            // if index is invalid
            if (index < 0 || index >= size) return -1;

            ListNode curr = head;
            // index steps needed
            // to move from sentinel node to wanted index
            for(int i = 0; i < index + 1; ++i) curr = curr.next;
            return curr.val;
        }

        /** Add a node of value val before the first element of the linked list. After the insertion, the new node will be the first node of the linked list. */
        public void addAtHead(int val) {
            addAtIndex(0, val);
        }

        /** Append a node of value val to the last element of the linked list. */
        public void addAtTail(int val) {
            addAtIndex(size, val);
        }

        /** Add a node of value val before the index-th node in the linked list. If index equals to the length of linked list, the node will be appended to the end of linked list. If index is greater than the length, the node will not be inserted. */
        public void addAtIndex(int index, int val) {
            // If index is greater than the length,
            // the node will not be inserted.
            if (index > size) return;

            // [so weird] If index is negative,
            // the node will be inserted at the head of the list.
            if (index < 0) index = 0;

            ++size;
            // find predecessor of the node to be added
            ListNode pred = head;
            for(int i = 0; i < index; ++i) pred = pred.next;

            // node to be added
            ListNode toAdd = new ListNode(val);
            // insertion itself
            toAdd.next = pred.next;
            pred.next = toAdd;
        }

        /** Delete the index-th node in the linked list, if the index is valid. */
        public void deleteAtIndex(int index) {
            // if the index is invalid, do nothing
            if (index < 0 || index >= size) return;

            size--;
            // find predecessor of the node to be deleted
            ListNode pred = head;
            for(int i = 0; i < index; ++i) pred = pred.next;

            // delete pred.next
            pred.next = pred.next.next;
        }
    }

    // V2
    // IDEA : Doubly Linked List
    // https://leetcode.com/problems/design-linked-list/editorial/
    public class ListNode {
        int val;
        ListNode next;
        ListNode prev;
        ListNode(int x) { val = x; }
    }

    class MyLinkedList_3 {
        int size;
        // sentinel nodes as pseudo-head and pseudo-tail
        ListNode head, tail;
        public MyLinkedList_3() {
            size = 0;
            head = new ListNode(0);
            tail = new ListNode(0);
            head.next = tail;
            tail.prev = head;
        }

        /** Get the value of the index-th node in the linked list. If the index is invalid, return -1. */
        public int get(int index) {
            // if index is invalid
            if (index < 0 || index >= size) return -1;

            // choose the fastest way: to move from the head
            // or to move from the tail
            ListNode curr = head;
            if (index + 1 < size - index)
                for(int i = 0; i < index + 1; ++i) curr = curr.next;
            else {
                curr = tail;
                for(int i = 0; i < size - index; ++i) curr = curr.prev;
            }

            return curr.val;
        }

        /** Add a node of value val before the first element of the linked list. After the insertion, the new node will be the first node of the linked list. */
        public void addAtHead(int val) {
            ListNode pred = head, succ = head.next;

            ++size;
            ListNode toAdd = new ListNode(val);
            toAdd.prev = pred;
            toAdd.next = succ;
            pred.next = toAdd;
            succ.prev = toAdd;
        }

        /** Append a node of value val to the last element of the linked list. */
        public void addAtTail(int val) {
            ListNode succ = tail, pred = tail.prev;

            ++size;
            ListNode toAdd = new ListNode(val);
            toAdd.prev = pred;
            toAdd.next = succ;
            pred.next = toAdd;
            succ.prev = toAdd;
        }

        /** Add a node of value val before the index-th node in the linked list. If index equals to the length of linked list, the node will be appended to the end of linked list. If index is greater than the length, the node will not be inserted. */
        public void addAtIndex(int index, int val) {
            // If index is greater than the length,
            // the node will not be inserted.
            if (index > size) return;

            // [so weird] If index is negative,
            // the node will be inserted at the head of the list.
            if (index < 0) index = 0;

            // find predecessor and successor of the node to be added
            ListNode pred, succ;
            if (index < size - index) {
                pred = head;
                for(int i = 0; i < index; ++i) pred = pred.next;
                succ = pred.next;
            }
            else {
                succ = tail;
                for (int i = 0; i < size - index; ++i) succ = succ.prev;
                pred = succ.prev;
            }

            // insertion itself
            ++size;
            ListNode toAdd = new ListNode(val);
            toAdd.prev = pred;
            toAdd.next = succ;
            pred.next = toAdd;
            succ.prev = toAdd;
        }

        /** Delete the index-th node in the linked list, if the index is valid. */
        public void deleteAtIndex(int index) {
            // if the index is invalid, do nothing
            if (index < 0 || index >= size) return;

            // find predecessor and successor of the node to be deleted
            ListNode pred, succ;
            if (index < size - index) {
                pred = head;
                for(int i = 0; i < index; ++i) pred = pred.next;
                succ = pred.next.next;
            }
            else {
                succ = tail;
                for (int i = 0; i < size - index - 1; ++i) succ = succ.prev;
                pred = succ.prev.prev;
            }

            // delete pred.next
            --size;
            pred.next = succ;
            succ.prev = pred;
        }
    }

//    public static void main(String[] args) {
//        System.out.println(123);
//    }

}
