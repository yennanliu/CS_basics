package dev;

import java.util.LinkedList;
import java.util.Queue;

public class workspace5 {

    // LC 430
    // 7.23 pm - 7.40 pm
    /*
    // Definition for a Node.
    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };
    */
    // recursive

    // attr
    Node res = new Node();
    //Node ans = new Node();

    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };
    public Node flatten(Node head) {

        Queue<Node> store = new LinkedList<>();
        /**
         *     /**
         *      * Inserts the specified element into this queue if it is possible to do
         *      * so immediately without violating capacity restrictions.
         *      * When using a capacity-restricted queue, this method is generally
         *      * preferable to {@link #add}, which can fail to insert an element only
         *      * by throwing an exception.
         */
        //store.offer(new Node());// ?

        if (head == null){
            return head;
        }

        if (head.child == null && head.next == null){
            return head;
        }

        Node res = new Node();
        Node ans = res;
        while(head.next != null){
            dfs(head);
            Node _next = head.next;
            head = _next;
        }

        return ans;
    }

    private void dfs(Node head){
        while (head.child != null){
            Node _child = head.child;
            Node _child_res = this.res.child; // ??
            this.res.child = _child;
            head = _child;
            this.res = _child_res; // ???
        }
        //return this.res;
    }

}
