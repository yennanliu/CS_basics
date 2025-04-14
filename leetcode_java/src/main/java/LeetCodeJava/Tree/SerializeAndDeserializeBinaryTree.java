package LeetCodeJava.Tree;

// https://leetcode.com/problems/serialize-and-deserialize-binary-tree/description/?envType=list&envId=xoqag3yj
// https://support.leetcode.com/hc/en-us/articles/32442719377939-How-to-create-test-cases-on-LeetCode#h_01J5EGREAW3NAEJ14XC07GRW1A
/**
 * 297. Serialize and Deserialize Binary Tree
 * Solved
 * Hard
 * Topics
 * Companies
 * Serialization is the process of converting a data structure or object into a sequence of bits so that it can be stored in a file or memory buffer, or transmitted across a network connection link to be reconstructed later in the same or another computer environment.
 *
 * Design an algorithm to serialize and deserialize a binary tree. There is no restriction on how your serialization/deserialization algorithm should work. You just need to ensure that a binary tree can be serialized to a string and this string can be deserialized to the original tree structure.
 *
 * Clarification: The input/output format is the same as how LeetCode serializes a binary tree. You do not necessarily need to follow this format, so please be creative and come up with different approaches yourself.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,3,null,null,4,5]
 * Output: [1,2,3,null,null,4,5]
 * Example 2:
 *
 * Input: root = []
 * Output: []
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 104].
 * -1000 <= Node.val <= 1000
 *
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class SerializeAndDeserializeBinaryTree {

    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     * int val;
     * TreeNode left;
     * TreeNode right;
     * TreeNode(int x) { val = x; }
     * }
     */
    // Your Codec object will be instantiated and called as such:
    // Codec ser = new Codec();
    // Codec deser = new Codec();
    // TreeNode ans = deser.deserialize(ser.serialize(root));

    // VO
    // IDEA : DFS
    public class Codec{
        public String serialize(TreeNode root) {

            /** NOTE !!!
             *
             *     if root == null, return "#"
             */
            if (root == null){
                return "#";
            }

            /** NOTE !!! return result via pre-order, split with "," */
            return root.val + "," + serialize(root.left) + "," + serialize(root.right);
        }

        public TreeNode deserialize(String data) {

            /** NOTE !!!
             *
             *   1) init queue and append serialize output
             *   2) even use queue, but helper func still using DFS
             */
            Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(",")));
            return helper(queue);
        }

        private TreeNode helper(Queue<String> queue) {

            // get val from queue first
            String s = queue.poll();

            if (s.equals("#")){
                return null;
            }
            /** NOTE !!! init current node  */
            TreeNode root = new TreeNode(Integer.valueOf(s));
            /** NOTE !!!
             *
             *    since serialize is "pre-order",
             *    deserialize we use "pre-order" as well
             *    e.g. root -> left sub tree -> right sub tree
             *    -> so we get sub tree via below :
             *
             *       root.left = helper(queue);
             *       root.right = helper(queue);
             *
             */
            root.left = helper(queue);
            root.right = helper(queue);
            /** NOTE !!! don't forget to return final deserialize result  */
            return root;
        }
    }

    // V0-1
    // IDEA: DFS (GPT)

    // class TreeNode {
    //     int val;
    //     TreeNode left, right;
    //     TreeNode(int val) {
    //         this.val = val;
    //     }
    // }
    public class Codec_0_1 {

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            if (root == null)
                return ""; // Edge case: empty tree

            StringBuilder sb = new StringBuilder();
            serializeHelper(root, sb);
            return sb.toString();
        }

        private void serializeHelper(TreeNode node, StringBuilder sb) {
            if (node == null) {
                sb.append("null,"); // Mark null nodes
                return;
            }

            /**
             *  NOTE !!!
             *
             *  below we first append node val and ',' to stringBuilder
             */
            sb.append(node.val).append(","); // Add root value
            /**
             *  NOTE !!!
             *
             *  then we go node.left, node.right via recursive call
             */
            serializeHelper(node.left, sb); // Serialize left subtree
            serializeHelper(node.right, sb); // Serialize right subtree
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            if (data.isEmpty())
                return null; // Edge case: empty string means no tree

            Queue<String> nodes = new LinkedList<>();
            /**
             *  NOTE !!!
             *  via queue, then we can easily `pop` current element,
             *  and build new node with it, isstead of dealing with string operation
             */
            // below are equals
            //nodes.addAll(List.of(data.split(",")));
            for(String d: data.split(",")){
                nodes.add(d);
            }
            return deserializeHelper(nodes);
        }

        private TreeNode deserializeHelper(Queue<String> nodes) {
            if (nodes.isEmpty())
                return null;

            /**
             *  NOTE !!!
             *  we `pop` current element,
             */
            String val = nodes.poll();
            if (val.equals("null"))
                return null; // If "null", return null node

            /**
             *  NOTE !!!
             *  we build root node via current element,
             *  then build its sub left, right node via
             *  recursively call
             */
            TreeNode root = new TreeNode(Integer.parseInt(val));
            root.left = deserializeHelper(nodes); // Reconstruct left subtree
            root.right = deserializeHelper(nodes); // Reconstruct right subtree

            return root;
        }

    }

    // V0-2
    // IDEA: DFS (fixed by gpt)
    public class Codec_0_2 {

        // Encodes a tree to a single string using pre-order DFS.
        public String serialize(TreeNode root) {
            StringBuilder sb = new StringBuilder();
            serializeHelper(root, sb);
            return sb.toString();
        }

        private void serializeHelper(TreeNode node, StringBuilder sb) {
            if (node == null) {
                sb.append("null,");
                return;
            }
            sb.append(node.val).append(",");
            serializeHelper(node.left, sb);
            serializeHelper(node.right, sb);
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            if (data == null || data.isEmpty())
                return null;
            String[] nodes = data.split(",");
            Queue<String> queue = new LinkedList<>(Arrays.asList(nodes));
            return deserializeHelper(queue);
        }

        private TreeNode deserializeHelper(Queue<String> queue) {
            if (queue.isEmpty())
                return null;

            String val = queue.poll();
            if (val.equals("null"))
                return null;

            TreeNode node = new TreeNode(Integer.parseInt(val));
            node.left = deserializeHelper(queue);
            node.right = deserializeHelper(queue);
            return node;
        }
    }

    
    // V1-1
    // https://neetcode.io/problems/serialize-and-deserialize-binary-tree
    // https://youtu.be/u4JAi2JJhI8?si=_bd33xap_9uRTI9E
    // IDEA: DFS
    public class Codec_1_1 {

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            List<String> res = new ArrayList<>();
            dfsSerialize(root, res);
            return String.join(",", res);
        }

        private void dfsSerialize(TreeNode node, List<String> res) {
            if (node == null) {
                res.add("N");
                return;
            }
            res.add(String.valueOf(node.val));
            dfsSerialize(node.left, res);
            dfsSerialize(node.right, res);
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            String[] vals = data.split(",");
            int[] i = {0};
            return dfsDeserialize(vals, i);
        }

        private TreeNode dfsDeserialize(String[] vals, int[] i) {
            if (vals[i[0]].equals("N")) {
                i[0]++;
                return null;
            }
            /**
             *  NOTE !!!
             *
             *  1)  since we know val array and idx,
             *      so it's NO NEEDED use a for loop accessing element.
             *      Instead, we can simply use, and update idx,
             *      then we can get new val when build tree
             *
             *  e.g.
             *   TreeNode node = new TreeNode(Integer.parseInt(vals[i[0]]));
             *   i[0] ++
             *
             *
             *  2) return type is `TreeNode` for this build tree helper func
             *     and we need to point sub left, right tree to current node
             *     e.g.
             *       - node.left = dfsDeserialize(vals, i);
             *       - node.right = dfsDeserialize(vals, i);
             *
             * 3) Don't forget to retunn built tree as final result
             *
             *    e.g.
             *        return node
             */
            TreeNode node = new TreeNode(Integer.parseInt(vals[i[0]]));
            i[0]++;
            node.left = dfsDeserialize(vals, i);
            node.right = dfsDeserialize(vals, i);
            return node;
        }
    }

    // V1-2
    // https://neetcode.io/problems/serialize-and-deserialize-binary-tree
    // https://youtu.be/u4JAi2JJhI8?si=_bd33xap_9uRTI9E
    // IDEA: BFS
    public class Codec_1_2 {

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            if (root == null) return "N";
            StringBuilder res = new StringBuilder();
            Queue<TreeNode> queue = new LinkedList<>();
            queue.add(root);

            while (!queue.isEmpty()) {
                TreeNode node = queue.poll();
                if (node == null) {
                    res.append("N,");
                } else {
                    res.append(node.val).append(",");
                    queue.add(node.left);
                    queue.add(node.right);
                }
            }
            return res.toString();
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            String[] vals = data.split(",");
            if (vals[0].equals("N")) return null;
            TreeNode root = new TreeNode(Integer.parseInt(vals[0]));
            Queue<TreeNode> queue = new LinkedList<>();
            queue.add(root);
            int index = 1;

            while (!queue.isEmpty()) {
                TreeNode node = queue.poll();
                if (!vals[index].equals("N")) {
                    node.left = new TreeNode(Integer.parseInt(vals[index]));
                    queue.add(node.left);
                }
                index++;
                if (!vals[index].equals("N")) {
                    node.right = new TreeNode(Integer.parseInt(vals[index]));
                    queue.add(node.right);
                }
                index++;
            }
            return root;
        }
    }

    // V2
    // IDEA : BFS + QUEUE OP
    // https://leetcode.com/problems/serialize-and-deserialize-binary-tree/solutions/4705074/solution-using-level-order-traversal/?envType=list&envId=xoqag3yj
    public class Codec_2 {

        public String serialize(TreeNode root) {

            if (root == null)
                return "";

            Queue<TreeNode> q = new LinkedList<>();
            q.offer(root);
            StringBuilder sb = new StringBuilder();

            while (!q.isEmpty()) {
                TreeNode node = q.poll();

                if (node == null) {
                    sb.append("#").append(" ");
                    continue;
                }

                sb.append(node.val).append(" ");
                q.add(node.left);
                q.add(node.right);
            }

            return sb.toString().trim();
        }

        public TreeNode deserialize(String data) {
            if (data.equals(""))
                return null;

            String[] nodes = data.split(" ");
            TreeNode root = new TreeNode(Integer.parseInt(nodes[0]));

            Queue<TreeNode> q = new LinkedList<>();
            q.offer(root);

            int len = nodes.length;
            for (int i = 1; i < len; i++) {
                TreeNode parent = q.poll();

                /** NOTE !!! if node[i] is NOT null ( nodes[i] != "#" ) */
                if (! nodes[i].equals("#")) {
                    TreeNode leftNode = new TreeNode(Integer.parseInt(nodes[i]));
                    parent.left = leftNode;
                    q.add(leftNode);
                }

                /** NOTE !!! if node[++i] is NOT null ( node[++i] != "#" ) */
                if (! nodes[++i].equals("#")) {
                    TreeNode rightNode = new TreeNode(Integer.parseInt(nodes[i]));
                    parent.right = rightNode;
                    q.add(rightNode);
                }
            }

            return root;
        }
    }

    // V3
    // IDEA : DFS
    // https://leetcode.com/problems/serialize-and-deserialize-binary-tree/solutions/281714/clean-java-solution/?envType=list&envId=xoqag3yj
    public class Codec_3 {
        public String serialize(TreeNode root) {
            if (root == null) return "#";
            return root.val + "," + serialize(root.left) + "," + serialize(root.right);
        }

        public TreeNode deserialize(String data) {
            Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(",")));
            return helper(queue);
        }

        private TreeNode helper(Queue<String> queue) {
            String s = queue.poll();
            if (s.equals("#")) return null;
            TreeNode root = new TreeNode(Integer.valueOf(s));
            root.left = helper(queue);
            root.right = helper(queue);
            return root;
        }
    }

}
