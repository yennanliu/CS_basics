package LeetCodeJava.Tree;

// https://leetcode.com/problems/serialize-and-deserialize-n-ary-tree/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 428. Serialize and Deserialize N-ary Tree
 * Hard
 * Lock: Prime
 *
 * Serialization is the process of converting a data structure or object into a
 * sequence of bits so that it can be stored in a file or memory buffer, or
 * transmitted across a network connection link to be reconstructed later in the
 * same or another computer environment.
 *
 * Design an algorithm to serialize and deserialize an N-ary tree. An N-ary tree is
 * a rooted tree in which each node has no more than N children. There is no
 * restriction on how your serialization/deserialization algorithm should work. You
 * just need to ensure that an N-ary tree can be serialized to a string and this
 * string can be deserialized to the original tree structure.
 *
 * For example, you may serialize the following 3-ary tree as [1 [3[5 6] 2 4]].
 * Note that this is just an example, you do not necessarily need to follow this
 * format.
 *
 * Example 1:
 *
 * Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,
 *                null,13,null,null,14]
 * Output: [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,
 *          null,null,14]
 *
 * Example 2:
 *
 * Input: root = [1,null,3,2,4,null,5,6]
 * Output: [1,null,3,2,4,null,5,6]
 *
 * Example 3:
 *
 * Input: root = []
 * Output: []
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 10^4].
 * 0 <= Node.val <= 10^4
 * The height of the n-ary tree is less than or equal to 1000
 * Do not use class member/global/static variables to store states.
 *
 */
public class SerializeAndDeserializeNaryTree {

    // Definition for a Node (offered by LC platform)
    static class Node {
        public int val;
        public List<Node> children;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

    /**
     * Your Codec object will be instantiated and called as such:
     * Codec codec = new Codec();
     * codec.deserialize(codec.serialize(root));
     */

    // V0
    // IDEA: PRE-ORDER DFS + CHILD COUNT
    /**
     *  Emit "val,childCount" per node in PRE-ORDER. The child count tells the
     *  decoder EXACTLY how many sub-trees to read next, so NO null markers and NO
     *  ambiguity - a single linear scan rebuilds the tree.
     *
     *     serialize:  1,3,3,2,5,0,6,0,2,0,4,0
     *                 ^ ^   ^ ^
     *                 | |   | node 3 has 2 children
     *                 | node 1 has 3 children
     *
     *  time  = O(n)   // serialize and deserialize both
     *  space = O(n)
     */
    class Codec {

        // Encodes a tree to a single string.
        public String serialize(Node root) {
            List<String> out = new ArrayList<>();
            serializeDfs(root, out);
            return String.join(",", out);
        }

        private void serializeDfs(Node node, List<String> out) {
            if (node == null) {
                return;
            }
            out.add(String.valueOf(node.val));
            int size = node.children == null ? 0 : node.children.size();
            out.add(String.valueOf(size));
            if (node.children != null) {
                for (Node child : node.children) {
                    serializeDfs(child, out);
                }
            }
        }

        // Decodes your encoded data to tree.
        public Node deserialize(String data) {
            if (data == null || data.isEmpty()) {
                return null;
            }
            /** NOTE !!!
             *
             *  a single-element cursor keeps the read position LOCAL
             *  -> the problem forbids member/global state
             */
            String[] tokens = data.split(",");
            int[] pos = new int[] { 0 };
            return build(tokens, pos);
        }

        private Node build(String[] tokens, int[] pos) {
            int val = Integer.parseInt(tokens[pos[0]++]);
            int size = Integer.parseInt(tokens[pos[0]++]);

            Node node = new Node(val, new ArrayList<>());
            for (int i = 0; i < size; i++) {
                node.children.add(build(tokens, pos));
            }
            return node;
        }
    }

    // V0-1
    // IDEA: BFS (LEVEL ORDER) + "#" AS THE END-OF-CHILDREN MARKER
    /**
     *  Distinct trick: NO recursion at all, so a 1000-deep tree cannot blow the
     *  call stack. Children of a node are emitted CONTIGUOUSLY and terminated by "#".
     *
     *  time  = O(n)
     *  space = O(n)
     */
    class Codec2 {

        public String serialize(Node root) {
            if (root == null) {
                return "";
            }

            // "rootVal" then, for every node in BFS order, its children ended by "#"
            List<String> out = new ArrayList<>();
            out.add(String.valueOf(root.val));

            Deque<Node> q = new ArrayDeque<>();
            q.offer(root);

            while (!q.isEmpty()) {
                Node node = q.poll();
                if (node.children != null) {
                    for (Node child : node.children) {
                        out.add(String.valueOf(child.val));
                        q.offer(child);
                    }
                }
                out.add("#"); // end of THIS node's children list
            }

            return String.join(",", out);
        }

        public Node deserialize(String data) {
            if (data == null || data.isEmpty()) {
                return null;
            }

            String[] tokens = data.split(",");
            Node root = new Node(Integer.parseInt(tokens[0]), new ArrayList<>());

            int i = 1;
            Deque<Node> q = new ArrayDeque<>();
            q.offer(root);

            while (!q.isEmpty() && i < tokens.length) {
                Node node = q.poll();
                while (!tokens[i].equals("#")) {
                    Node child = new Node(Integer.parseInt(tokens[i]), new ArrayList<>());
                    node.children.add(child);
                    q.offer(child);
                    i += 1;
                }
                i += 1; // skip the "#"
            }

            return root;
        }
    }

}
