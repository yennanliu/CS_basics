package dev;

import LeetCodeJava.LinkedList.FlattenMultilevelDoublyLinkedList;

import java.util.Stack;

public class workspace15 {

    // LC 430
    // 4.28 - 4.38 pm
    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };

    /**
     *  IDEA 1) LINKED LIST OP
     *
     *   step 1) check if `child exists`
     *   step 2) re-point
     *       next to child
     *       child prev to cur
     *   step 3) if a `nested child`, repeat above step
     *   step 4) re-point `next` to next
     *
     */
    // IDEA 2) ITERATIVE + LINKED LIST
    public Node flatten(Node head) {
        if (head == null){
            return null;
        }

        Stack<Node> st = new Stack<>();
        // `null` pointer
        Node _prev = new Node(); // ???

        st.add(head); // ???

        // stack: FILO, so we can add `next` first, then child
        // and `pop` `child` then `next`
        // e.g. process child -> next
        while(!st.isEmpty()){

            // step 1) pop `last` node
            Node cur = st.pop();
            // step 2) point cur prev to _prev ????
            cur.prev = _prev;

            // step 3) point _prev next to cur
            _prev.next = cur;
//            // step 4) point _prev child to null
//            _prev.child = null; // ???

            // step 5) move _prev to cur
            _prev = cur;

            if(cur.next != null){
                st.add(cur.next);
            }

            // ???? (dealing with `nested` child structure)
            if(cur.child != null){
                st.add(cur.child);
            }
        }

        // remove dummy ????
        _prev.next.prev = null; // /????

        return _prev.next; // ???
    }



    // IDEA 1) RECURSIVE + LINKED LIST
    public Node flatten_2(Node head) {
        // edge
        if(head == null){
            return head;
        }
        if(head.child == null){
            return head;
        }

        Node res = head; // ????
        // cache cur node ???
        Node _cur = head;

        // cache `next` ???
        Node _next = head.next;

        // child
        while (head.child != null){
            Node _cuild_end = flatten(head.child); // ????
            res.next = _cuild_end; // ???
            res = _cuild_end;
        }

        // next
        res.next = _next;


        return res;
    }

    // ???
    private Node childHelper(Node head){
        return null;
    }


}
