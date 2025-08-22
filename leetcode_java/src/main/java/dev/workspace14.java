package dev;

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
    // 7.32 - 7.42 am
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

    public Node flatten(Node head) {
        // edge
        if(head == null){
            return head;
        }
        if(head.next == null && head.child == null && head.prev == null){
            return head;
        }

        // helper func

        return  null;
    }

    private Node flattenHelper(Node head){
        // edge
        if(head == null){
            return head;
        }

        // cache next

        // check `child`
        if(head.child != null){
            Node childEnd =  flattenHelper(head.child);
        }



        // check `next`
        // check `prev`

        return head;
    }


}
