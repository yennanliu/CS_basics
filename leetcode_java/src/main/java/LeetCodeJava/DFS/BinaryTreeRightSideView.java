package LeetCodeJava.DFS;

// https://leetcode.com/problems/binary-tree-right-side-view/
/**
 * 199. Binary Tree Right Side View
 * Solved
 * Medium
 * Topics
 * Companies
 * Given the root of a binary tree, imagine yourself standing on the right side of it, return the values of the nodes you can see ordered from top to bottom.
 *
 *
 *
 * Example 1:
 *
 * Input: root = [1,2,3,null,5,null,4]
 *
 * Output: [1,3,4]
 *
 * Explanation:
 *
 *
 *
 * Example 2:
 *
 * Input: root = [1,2,3,4,null,null,null,5]
 *
 * Output: [1,3,4,5]
 *
 * Explanation:
 *
 *
 *
 * Example 3:
 *
 * Input: root = [1,null,3]
 *
 * Output: [1,3]
 *
 * Example 4:
 *
 * Input: root = []
 *
 * Output: []
 *
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 100].
 * -100 <= Node.val <= 100
 *
 */
import LeetCodeJava.DataStructure.TreeNode;
import java.util.*;

public class BinaryTreeRightSideView {


    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     *     int val;
     *     TreeNode left;
     *     TreeNode right;
     *     TreeNode() {}
     *     TreeNode(int val) { this.val = val; }
     *     TreeNode(int val, TreeNode left, TreeNode right) {
     *         this.val = val;
     *         this.left = left;
     *         this.right = right;
     *     }
     * }
     */
    // VO
    // IDEA : BFS + custom class
    public class NodeLayer {
        TreeNode node;
        int layer;

        public NodeLayer(TreeNode node, int layer) {
            this.node = node;
            this.layer = layer;
        }
    }

    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        // edge
        if (root == null) {
            return res;
        }
        if (root.left == null && root.right == null) {
            res.add(root.val);
            return res;
        }

        List<List<Integer>> cache = new ArrayList<>();
        cache.add(new ArrayList<>());

        // bfs
        Queue<NodeLayer> q = new LinkedList<>();
        NodeLayer nodeLayer = new NodeLayer(root, 0);
        q.add(nodeLayer);
        while (!q.isEmpty()) {
            NodeLayer curNodeLayer = q.poll();
            TreeNode node = curNodeLayer.node;
            int layer = curNodeLayer.layer;

            if (cache.size() <= layer) {
                cache.add(new ArrayList<>());
            }

           //System.out.println(">>> cache size = " + cache.size() + ", layer = " + layer);
            cache.get(layer).add(node.val);

            if (node.left != null) {
                q.add(new NodeLayer(node.left, layer + 1));
            }
            if (node.right != null) {
                q.add(new NodeLayer(node.right, layer + 1));
            }
        }

        //System.out.println(">>> cache = " + cache);
        for (List<Integer> x : cache) {
            res.add(x.get(x.size() - 1));
        }

        return res;
    }

    // V0-1
    // IDEA: BFS + `right node collect`
    public List<Integer> rightSideView_0_1(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode rightSide = null;
            int qLen = q.size();

            /**
             *  NOTE !!!
             *
             *   1) via for loop, we can get `most right node` (since the order is root -> left -> right)
             *   2) via `TreeNode rightSide = null;`, we can get the `most right node` object
             *      - rightSide could be `right sub tree` or `left sub tree`
             *
             *      e.g.
             *      
             *         1
             *       2   3
             *
             *
             *       1
             *     2   3
             *   4
             *
             */
            for (int i = 0; i < qLen; i++) {
                TreeNode node = q.poll();
                if (node != null) {
                    rightSide = node;
                    q.offer(node.left);
                    q.offer(node.right);
                }
            }
            if (rightSide != null) {
                res.add(rightSide.val);
            }
        }
        return res;
    }

    // V0-2
    // IDEA : BFS + LC 102
    public List<Integer> rightSideView_0_2(TreeNode root) {

        List<Integer> res = new ArrayList<>();

        if (root == null){
            return res;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        int layer = 0;
        queue.add(root);

        List<List<Integer>> cache = new ArrayList<>();

        while (!queue.isEmpty()){

            /** NOTE !!! we add new null array before for loop */
            cache.add(new ArrayList<>());

            /** NOTE !!! we get queue size before for loop */
            int q_size = queue.size();
            for (int i = 0; i < q_size; i++){

                TreeNode cur = queue.remove();
                cache.get(layer).add(cur.val);

                // NOTE !!! cur.left != null
                if (cur.left != null){
                    queue.add(cur.left);
                }

                // NOTE !!! cur.left != null
                if (cur.right != null){
                    queue.add(cur.right);
                }
            }

            layer += 1;
        }

//        System.out.println(">>>");
//        cache.stream().forEach(System.out::println);
//        System.out.println(">>>");

        for (List<Integer> x : cache){
            int toAdd = x.get(x.size()-1);
            res.add(toAdd);
        }

        return res;
    }


    // V1-1
    // https://neetcode.io/problems/binary-tree-right-side-view
    // IDEA: BFS
    public List<Integer> rightSideView_1_1(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode rightSide = null;
            int qLen = q.size();

            for (int i = 0; i < qLen; i++) {
                TreeNode node = q.poll();
                if (node != null) {
                    rightSide = node;
                    q.offer(node.left);
                    q.offer(node.right);
                }
            }
            if (rightSide != null) {
                res.add(rightSide.val);
            }
        }
        return res;
    }

    // 1-2
    // https://neetcode.io/problems/binary-tree-right-side-view
    // IDEA: DFS
    List<Integer> res = new ArrayList<>();

    public List<Integer> rightSideView_1_2(TreeNode root) {
        dfs(root, 0);
        return res;
    }

    private void dfs(TreeNode node, int depth) {
        if (node == null) {
            return;
        }

        if (res.size() == depth) {
            res.add(node.val);
        }

        dfs(node.right, depth + 1);
        dfs(node.left, depth + 1);
    }

    // V2
    // IDEA: BFS: Two Queues
    // https://leetcode.com/problems/binary-tree-right-side-view/editorial/
    public List<Integer> rightSideView_2(TreeNode root) {
        if (root == null) return new ArrayList<Integer>();

        ArrayDeque<TreeNode> nextLevel = new ArrayDeque() {{ offer(root); }};
        ArrayDeque<TreeNode> currLevel = new ArrayDeque();
        List<Integer> rightside = new ArrayList();

        TreeNode node = null;
        while (!nextLevel.isEmpty()) {
            // prepare for the next level
            currLevel = nextLevel;
            nextLevel = new ArrayDeque<>();

            while (! currLevel.isEmpty()) {
                node = currLevel.poll();

                // add child nodes of the current level
                // in the queue for the next level
                if (node.left != null)
                    nextLevel.offer(node.left);
                if (node.right != null)
                    nextLevel.offer(node.right);
            }

            // The current level is finished.
            // Its last element is the rightmost one.
            if (currLevel.isEmpty())
                rightside.add(node.val);
        }
        return rightside;
    }

    // V3
    // IDEA:  BFS: One Queue + Sentinel
    // https://leetcode.com/problems/binary-tree-right-side-view/editorial/
    public List<Integer> rightSideView_3(TreeNode root) {
        if (root == null) return new ArrayList<Integer>();

        Queue<TreeNode> queue = new LinkedList(){{ offer(root); offer(null); }};
        TreeNode prev, curr = root;
        List<Integer> rightside = new ArrayList();

        while (!queue.isEmpty()) {
            prev = curr;
            curr = queue.poll();

            while (curr != null) {
                // add child nodes in the queue
                if (curr.left != null) {
                    queue.offer(curr.left);
                }
                if (curr.right != null) {
                    queue.offer(curr.right);
                }

                prev = curr;
                curr = queue.poll();
            }

            // the current level is finished
            // and prev is its rightmost element
            rightside.add(prev.val);
            // add a sentinel to mark the end
            // of the next level
            if (!queue.isEmpty())
                queue.offer(null);
        }
        return rightside;
    }

    // V4
    // IDEA: BFS: One Queue + Level Size Measurements
    // https://leetcode.com/problems/binary-tree-right-side-view/editorial/
    public List<Integer> rightSideView_4(TreeNode root) {
        if (root == null) return new ArrayList<Integer>();

        ArrayDeque<TreeNode> queue = new ArrayDeque(){{ offer(root); }};
        List<Integer> rightside = new ArrayList();

        while (!queue.isEmpty()) {
            int levelLength = queue.size();

            for(int i = 0; i < levelLength; ++i) {
                TreeNode node = queue.poll();
                // if it's the rightmost element
                if (i == levelLength - 1) {
                    rightside.add(node.val);
                }

                // add child nodes in the queue
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return rightside;
    }
    
    // V5
    // IDEA: Recursive DFS
    // https://leetcode.com/problems/binary-tree-right-side-view/editorial/
    List<Integer> rightside = new ArrayList();
    public void helper(TreeNode node, int level) {
        if (level == rightside.size())
            rightside.add(node.val);

        if (node.right != null)
            helper(node.right, level + 1);
        if (node.left != null)
            helper(node.left, level + 1);
    }

    public List<Integer> rightSideView_5(TreeNode root) {
        if (root == null) return rightside;

        helper(root, 0);
        return rightside;
    }

}
