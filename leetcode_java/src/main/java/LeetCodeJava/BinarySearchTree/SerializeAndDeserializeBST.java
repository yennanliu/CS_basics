package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/serialize-and-deserialize-bst/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 449. Serialize and Deserialize BST
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Serialization is converting a data structure or object into a sequence of bits so that it can be stored in a file or memory buffer, or transmitted across a network connection link to be reconstructed later in the same or another computer environment.
 *
 * Design an algorithm to serialize and deserialize a binary search tree. There is no restriction on how your serialization/deserialization algorithm should work. You need to ensure that a binary search tree can be serialized to a string, and this string can be deserialized to the original tree structure.
 *
 * The encoded string should be as compact as possible.
 *
 *
 *
 * Example 1:
 *
 * Input: root = [2,1,3]
 * Output: [2,1,3]
 * Example 2:
 *
 * Input: root = []
 * Output: []
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 104].
 * 0 <= Node.val <= 104
 * The input tree is guaranteed to be a binary search tree.
 *
 *
 */
public class SerializeAndDeserializeBST {

    // V0
    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     *     int val;
     *     TreeNode left;
     *     TreeNode right;
     *     TreeNode(int x) { val = x; }
     * }
     */
//    public class Codec {
//
//        // Encodes a tree to a single string.
//        public String serialize(TreeNode root) {
//
//        }
//
//        // Decodes your encoded data to tree.
//        public TreeNode deserialize(String data) {
//
//        }
//    }

    // Your Codec object will be instantiated and called as such:
    // Codec ser = new Codec();
    // Codec deser = new Codec();
    // String tree = ser.serialize(root);
    // TreeNode ans = deser.deserialize(tree);
    // return ans;


    // V1

    // V2-1
    // https://leetcode.com/problems/serialize-and-deserialize-bst/solutions/3312687/easy-java-solution-2-approaches-beats-10-8goc/
    class Codec_2_1 {

        public String serialize(TreeNode root) {
            StringBuilder res = new StringBuilder();

            helpserialize(root, res);

            return res.toString();
        }

        private void helpserialize(TreeNode root, StringBuilder res) {
            if (root == null) {
                res.append("x,");
                return;
            }

            res.append(root.val);
            res.append(',');

            helpserialize(root.left, res);
            helpserialize(root.right, res);
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            Deque<String> q = new LinkedList<>();

            q.addAll(Arrays.asList(data.split(",")));

            return helpdeserialize(q);
        }

        private TreeNode helpdeserialize(Deque<String> q) {
            String res = q.remove();

            if (res.equals("x")) {
                return null;
            }

            TreeNode root = new TreeNode(Integer.parseInt(res));

            root.left = helpdeserialize(q);
            root.right = helpdeserialize(q);

            return root;
        }
    }

    // V2-2
    // https://leetcode.com/problems/serialize-and-deserialize-bst/solutions/3312687/easy-java-solution-2-approaches-beats-10-8goc/
    public class Codec_2_2 {

        //static TreeNode res;
        TreeNode res;

        public String serialize(TreeNode root) {
            res = root;
            return "";
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            return res;
        }
    }


    // V3
    // https://leetcode.com/problems/serialize-and-deserialize-bst/solutions/2411860/two-approaches-20ms-easy-0ms-object-orie-t02l/
    public class Codec_3 { // 20ms solution
        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            if (root == null)
                return "#";
            return root.val + " " + serialize(root.left) + " " + serialize(root.right);
        }

        // Decodes your encoded data to tree.
        String[] arr;
        int index;

        public TreeNode deserialize(String data) {
            arr = data.split(" ");
            index = 0;
            return go();
        }

        private TreeNode go() {
            int index = this.index++;
            if (arr[index].equals("#"))
                return null;
            TreeNode root = new TreeNode(Integer.valueOf(arr[index]));
            root.left = go();
            root.right = go();
            return root;
        }
    }


    // V4
    // https://leetcode.com/problems/serialize-and-deserialize-bst/solutions/1365738/simple-straightforward-java-solution-by-cihaj/
    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     *     int val;
     *     TreeNode left;
     *     TreeNode right;
     *     TreeNode(int x) { val = x; }
     * }
     */
    public class Codec_4 {
        // since this is a binary search tree, we'll just output values in BFS
        // order (when serializind), then add all values to the tree one by one (when deserializing)
        // serialize will take O(n)
        // deserialize will take O(n*h), where h is the height of the tree (n in the
        // worst case, and log(n) if the tree is balanced)

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            if (root == null) {
                return null;
            }

            StringBuilder sb = new StringBuilder();
            Queue<TreeNode> q = new LinkedList<>();
            q.add(root);
            while (!q.isEmpty()) {
                TreeNode n = q.remove();
                sb.append(n.val).append(',');
                if (n.left != null)
                    q.add(n.left);
                if (n.right != null)
                    q.add(n.right);
            }
            sb.setLength(sb.length() - 1); // remove last ','
            return sb.toString();
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            if (data == null) {
                return null;
            }

            String[] vals = data.split(",");
            TreeNode root = new TreeNode(Integer.parseInt(vals[0]));
            for (int i = 1; i < vals.length; i++) {
                add(root, Integer.parseInt(vals[i]));
            }
            return root;
        }

        private static void add(TreeNode root, int val) {
            TreeNode cur = root;
            TreeNode parent = null;
            while (cur != null) {
                parent = cur;
                cur = (val <= cur.val) ? cur.left : cur.right;
            }
            if (val <= parent.val) {
                parent.left = new TreeNode(val);
            } else {
                parent.right = new TreeNode(val);
            }
        }

    }




}
