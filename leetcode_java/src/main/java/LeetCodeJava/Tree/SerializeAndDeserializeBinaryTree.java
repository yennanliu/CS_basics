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
    // IDEA : DFS (NOTE: `BFS` approach is also OK)
    public class Codec{
        /**
         * time = O(N)
         * space = O(H)
         */
        public String serialize(TreeNode root) {

            /** NOTE !!!
             *
             *     if root == null, return "#"
             *
             *     -> so once we face "#", we know we can STOP
             *        further traverse on current node
             */
            if (root == null){
                return "#";
            }

            /** NOTE !!!
             *
             *  1)
             *   return result via pre-order, split with ","
             *
             *  2)
             *    NO need to pass `root` to serialize func.
             *    e.g. root.val
             *
             */
            return root.val + "," + serialize(root.left) + "," + serialize(root.right);
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public TreeNode deserialize(String data) {

            /** NOTE !!!
             *
             *   1) init queue and append serialize output
             *   2) even use queue, but helper func still using DFS
             *
             *   3) we use `helper` (dfs func) to implement deserialize
             */
            Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(",")));
            return helper(queue);
        }

        /** NOTE !!!!
         *
         * `Queue<String> queue` is the parameter for helper func
         */
        /**
         *  Example 1) V1 (gpt)
         *
         *  Absolutely! Let’s **visualize the `helper` function** in your `deserialize` method to see how it reconstructs the binary tree from the serialized string. It uses **pre-order traversal**, meaning it reads:
         * **root → left → right**.
         *
         * ---
         *
         * ### Example to Visualize
         *
         * Let’s take a sample tree:
         *
         * ```
         *       1
         *      / \
         *     2   3
         *        / \
         *       4   5
         * ```
         *
         * The **serialized string** (via pre-order traversal) would be:
         *
         * ```
         * "1,2,#,#,3,4,#,#,5,#,#"
         * ```
         *
         * Split by comma → `["1", "2", "#", "#", "3", "4", "#", "#", "5", "#", "#"]`
         *
         * ---
         *
         * ### Visualization of `helper(queue)` Execution
         *
         * We’ll walk through what `helper(queue)` is doing step-by-step:
         *
         * ---
         *
         * #### **Step 1**
         * - Queue: `["1", "2", "#", "#", "3", "4", "#", "#", "5", "#", "#"]`
         * - `s = "1"` → new `TreeNode(1)`
         * - Recurse left → `helper(queue)` for left child
         *
         * ---
         *
         * #### **Step 2**
         * - Queue: `["2", "#", "#", "3", "4", "#", "#", "5", "#", "#"]`
         * - `s = "2"` → new `TreeNode(2)`
         * - Recurse left → `helper(queue)`
         *
         * ---
         *
         * #### **Step 3**
         * - Queue: `["#", "#", "3", "4", "#", "#", "5", "#", "#"]`
         * - `s = "#"` → left child is `null`
         *
         * ---
         *
         * #### **Step 4**
         * - Queue: `["#", "3", "4", "#", "#", "5", "#", "#"]`
         * - `s = "#"` → right child is `null`
         *
         * Now, TreeNode(2) is complete → `TreeNode(2, null, null)`
         *
         * Back to TreeNode(1), and recurse right → `helper(queue)`
         *
         * ---
         *
         * #### **Step 5**
         * - Queue: `["3", "4", "#", "#", "5", "#", "#"]`
         * - `s = "3"` → new `TreeNode(3)`
         * - Recurse left → `helper(queue)`
         *
         * ---
         *
         * #### **Step 6**
         * - Queue: `["4", "#", "#", "5", "#", "#"]`
         * - `s = "4"` → new `TreeNode(4)`
         * - Recurse left → `s = "#"`, return `null`
         * - Recurse right → `s = "#"`, return `null`
         * - Complete TreeNode(4)
         *
         * Back to TreeNode(3), now recurse right → `helper(queue)`
         *
         * ---
         *
         * #### **Step 7**
         * - Queue: `["5", "#", "#"]`
         * - `s = "5"` → new `TreeNode(5)`
         * - Recurse left → `s = "#"`, return `null`
         * - Recurse right → `s = "#"`, return `null`
         *
         * ---
         *
         * #### **Final Tree Structure Built:**
         *
         * ```
         *       1
         *      / \
         *     2   3
         *        / \
         *       4   5
         * ```
         *
         * ---
         *
         * ### Summary of `helper(queue)`:
         * - Each call **creates a node or returns null**, based on the next string in the queue.
         * - Uses recursion to rebuild the **left and right subtree** in pre-order.
         * - It's elegant because it consumes the queue one element at a time and builds the tree recursively with minimal state.
         *
         *
         */
        /**
         *  Example 1) V2 - with diagram (gpt)
         *
         *  Awesome! Let's **visualize the tree reconstruction** process from this serialized string:
         *
         * ```
         * "1,2,#,#,3,4,#,#,5,#,#"
         * ```
         *
         * This corresponds to the tree:
         *
         * ```
         *       1
         *      / \
         *     2   3
         *        / \
         *       4   5
         * ```
         *
         * Now let's walk through the recursive calls to `helper(queue)` using a **tree diagram with steps**.
         *
         * ---
         *
         * ### Step-by-Step Tree Building
         *
         * ---
         *
         * **Start** with this queue:
         * ```
         * ["1", "2", "#", "#", "3", "4", "#", "#", "5", "#", "#"]
         * ```
         *
         * #### 1. `s = "1"` → Create `TreeNode(1)`
         * ```
         * Tree so far:
         *    1
         *   / \
         *  ?   ?
         * ```
         * → Call `helper()` for left
         *
         * ---
         *
         * #### 2. `s = "2"` → Create `TreeNode(2)`
         * ```
         * Tree so far:
         *    1
         *   / \
         *  2   ?
         * ```
         * → Call `helper()` for left of 2
         *
         * ---
         *
         * #### 3. `s = "#"` → Return `null`
         * → Left of 2 is null
         *
         * #### 4. `s = "#"` → Return `null`
         * → Right of 2 is null
         *
         * ```
         * Tree so far:
         *    1
         *   / \
         *  2   ?
         *     / \
         *  null null
         * ```
         *
         * → Back to node 1, now call `helper()` for right
         *
         * ---
         *
         * #### 5. `s = "3"` → Create `TreeNode(3)`
         * ```
         * Tree so far:
         *    1
         *   / \
         *  2   3
         * ```
         * → Call `helper()` for left of 3
         *
         * ---
         *
         * #### 6. `s = "4"` → Create `TreeNode(4)`
         * ```
         * Tree so far:
         *    1
         *   / \
         *  2   3
         *     /
         *    4
         * ```
         * → Call `helper()` for left of 4
         *
         * ---
         *
         * #### 7. `s = "#"` → Return `null`
         * → Left of 4 is null
         *
         * #### 8. `s = "#"` → Return `null`
         * → Right of 4 is null
         *
         * ```
         * Tree so far:
         *    1
         *   / \
         *  2   3
         *     / \
         *    4   ?
         *   / \
         *  n   n
         * ```
         *
         * → Back to 3, call `helper()` for right
         *
         * ---
         *
         * #### 9. `s = "5"` → Create `TreeNode(5)`
         * ```
         * Tree so far:
         *    1
         *   / \
         *  2   3
         *     / \
         *    4   5
         * ```
         * → Call `helper()` for left of 5
         *
         * ---
         *
         * #### 10. `s = "#"` → Return `null`
         * → Left of 5 is null
         *
         * #### 11. `s = "#"` → Return `null`
         * → Right of 5 is null
         *
         * ---
         *
         * ### ✅ Final Tree:
         * ```
         *         1
         *        / \
         *       2   3
         *          / \
         *         4   5
         * ```
         *
         * ---
         *
         * ### Recap
         *
         * Each recursive `helper(queue)` call:
         * - Takes the next string from the queue.
         * - If it's a value, creates a node and recurses left/right.
         * - If it's `"#"`, returns `null`.
         *
         */
        private TreeNode helper(Queue<String> queue) {

            // get val from queue first
            String s = queue.poll();

            /**
             *  NOTE !!!
             *
             *   the edge case:  `s.equals("#")`
             *   since in serialize, we set val as "#" if node is null
             */
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
            /**
             *  NOTE !!!
             *
             *   since we already `pop` element from queue,
             *   so it's OK to pass `updated queue` as parameter
             *   to below recursive call for root.left, root.right
             */
            root.left = helper(queue);
            root.right = helper(queue);
            /** NOTE !!! don't forget to return final deserialize result  */
            return root;
        }
    }

    // V0-0-1
    // IDEA: BFS (GPT)
    public class Codec_0_0_1 {

        // Encodes a tree to a single string.
        /**
         * time = O(N)
         * space = O(H)
         */
        public String serialize(TreeNode root) {
            if (root == null)
                return "#,"; // use "#" to mark nulls

            StringBuilder sb = new StringBuilder();
            Queue<TreeNode> q = new LinkedList<>();
            q.add(root);

            while (!q.isEmpty()) {
                TreeNode node = q.poll();
                if (node == null) {
                    sb.append("#,");
                    continue;
                }

                sb.append(node.val).append(",");
                q.add(node.left);
                q.add(node.right);
            }

            return sb.toString();
        }

        // Decodes your encoded data to tree.
        /**
         * time = O(N)
         * space = O(H)
         */
        public TreeNode deserialize(String data) {
            if (data.equals("#,"))
                return null;

            String[] parts = data.split(",");
            Queue<TreeNode> q = new LinkedList<>();
            TreeNode root = new TreeNode(Integer.parseInt(parts[0]));
            q.add(root);

            int i = 1;
            while (!q.isEmpty() && i < parts.length) {
                TreeNode parent = q.poll();

                // left child
                if (!parts[i].equals("#")) {
                    parent.left = new TreeNode(Integer.parseInt(parts[i]));
                    q.add(parent.left);
                }
                i++;

                // right child
                if (i < parts.length && !parts[i].equals("#")) {
                    parent.right = new TreeNode(Integer.parseInt(parts[i]));
                    q.add(parent.right);
                }
                i++;
            }

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
        /**
         * time = O(N)
         * space = O(H)
         */
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
        /**
         * time = O(N)
         * space = O(H)
         */
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
        /**
         * time = O(N)
         * space = O(H)
         */
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
        /**
         * time = O(N)
         * space = O(H)
         */
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
        /**
         * time = O(N)
         * space = O(H)
         */
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
        /**
         * time = O(N)
         * space = O(H)
         */
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
        /**
         * time = O(N)
         * space = O(H)
         */
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
        /**
         * time = O(N)
         * space = O(H)
         */
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

        /**
         * time = O(N)
         * space = O(H)
         */
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

        /**
         * time = O(N)
         * space = O(H)
         */
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
        /**
         * time = O(N)
         * space = O(H)
         */
        public String serialize(TreeNode root) {
            if (root == null) return "#";
            return root.val + "," + serialize(root.left) + "," + serialize(root.right);
        }

        /**
         * time = O(N)
         * space = O(H)
         */
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
