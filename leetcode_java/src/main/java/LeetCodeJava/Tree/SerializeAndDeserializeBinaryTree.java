package LeetCodeJava.Tree;

// https://leetcode.com/problems/serialize-and-deserialize-binary-tree/description/?envType=list&envId=xoqag3yj

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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
public class SerializeAndDeserializeBinaryTree {

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

    // V1
    // IDEA : BFS + QUEUE OP
    // https://leetcode.com/problems/serialize-and-deserialize-binary-tree/solutions/4705074/solution-using-level-order-traversal/?envType=list&envId=xoqag3yj
    public class Codec_1 {

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

    // V1'
    // IDEA : DFS
    // https://leetcode.com/problems/serialize-and-deserialize-binary-tree/solutions/281714/clean-java-solution/?envType=list&envId=xoqag3yj
    public class Codec_2 {
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
