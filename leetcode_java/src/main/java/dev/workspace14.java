package dev;

import java.util.Stack;

public class workspace14 {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    // LC 297
    // 5.55 - 6.10 pm
    /**
     *
     *
     *
     */
    public class Codec {

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            return null;
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {

            return null;
        }
    }

    // LC 430
    // 13.44 - 13.54 pm
    /**
     *
     * IDEA 1) LINKED LIST OP + recursion
     *
     *   1. check `child`
     *      -> connect next to child
     *      -> connect child.prev to node
     *      -> set child.child as null
     *
     *   2. check `next`
     *
     *     -> connect node.next to next
     *     -> connect next to node
     *
     */
    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };

    // IDEA 2) `Stack`
    public Node flatten(Node head) {
        // edge
        if(head == null){
            return head;
        }
        if(head.next == null && head.child == null && head.prev == null){
            return head;
        }

        Node res = null;

        Stack<Node> st = new Stack<>();

        st.add(head);

        while(!st.isEmpty()){

            Node _node = st.pop();

            Node _child = null;
            Node _next = null;
//
//            while(_node.child != null){
//                st.add(_child);
//            }
//
//            _node

        }


        return res.next;
    }


    // IDEA 1) DFS
//    public Node flatten(Node head) {
//        // edge
//        if(head == null){
//            return head;
//        }
//        if(head.next == null && head.child == null && head.prev == null){
//            return head;
//        }
//
//        // helper func ???
//        return flattenHelper(head);
//
//        //return  null;
//    }
//
//    private Node flattenHelper(Node head){
//        // edge
//        if(head == null){
//            return head;
//        }
//
//        // cache next
//        Node _next = head.next;
//        Node _head = head;
//
//        Node _child = head.child;
//
//        // check `child`
//        while(_child != null){
//            head.next = _child;
//            head = _child;
//            _child = _child.next;
//
//            // what if child as `child` ???
//            //_child =  flattenHelper(head.child);
//        }
//
//        // point _child.child to null
//        _child.child = null;
//
//        // point `child` end to next
//        _child.next = _next;
//
//        return head;
//    }


}
