package LeetCodeJava.Tree;

// https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 * 863. All Nodes Distance K in Binary Tree
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree, the value of a target node target, and an integer k, return an array of the values of all nodes that have a distance k from the target node.
 *
 * You can return the answer in any order.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], target = 5, k = 2
 * Output: [7,4,1]
 * Explanation: The nodes that are a distance 2 from the target node (with value 5) have values 7, 4, and 1.
 * Example 2:
 *
 * Input: root = [1], target = 1, k = 3
 * Output: []
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 500].
 * 0 <= Node.val <= 500
 * All the values Node.val are unique.
 * target is the value of one of the nodes in the tree.
 * 0 <= k <= 1000
 *
 *
 */
public class AllNodesDistanceKInBinaryTree {

    // V0
//    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
//
//    }

    // V1
    // https://leetcode.ca/2018-04-11-863-All-Nodes-Distance-K-in-Binary-Tree/
    // IDEA: DFS + HASHMAP
    private Map<TreeNode, TreeNode> p;
    private Set<Integer> vis;
    private List<Integer> ans;

    public List<Integer> distanceK_1(TreeNode root, TreeNode target, int k) {
        p = new HashMap<>();
        vis = new HashSet<>();
        ans = new ArrayList<>();
        parents(root, null);
        dfs(target, k);
        return ans;
    }

    private void parents(TreeNode root, TreeNode prev) {
        if (root == null) {
            return;
        }
        p.put(root, prev);
        parents(root.left, root);
        parents(root.right, root);
    }

    private void dfs(TreeNode root, int k) {
        if (root == null || vis.contains(root.val)) {
            return;
        }
        vis.add(root.val);
        if (k == 0) {
            ans.add(root.val);
            return;
        }
        dfs(root.left, k - 1);
        dfs(root.right, k - 1);
        dfs(p.get(root), k - 1);
    }


    // V2
    // IDEA: BFS
    // https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/solutions/3747835/two-bfs-c-java-python-beginner-friendly-jwora/
    public List<Integer> distanceK_2(TreeNode root, TreeNode target, int k) {
        List<Integer> ans = new ArrayList<>();
        Map<Integer, TreeNode> parent = new HashMap<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode top = queue.poll();

                if (top.left != null) {
                    parent.put(top.left.val, top);
                    queue.offer(top.left);
                }

                if (top.right != null) {
                    parent.put(top.right.val, top);
                    queue.offer(top.right);
                }
            }
        }

        Map<Integer, Integer> visited = new HashMap<>();
        queue.offer(target);
        while (k > 0 && !queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                TreeNode top = queue.poll();

                visited.put(top.val, 1);

                if (top.left != null && !visited.containsKey(top.left.val)) {
                    queue.offer(top.left);
                }

                if (top.right != null && !visited.containsKey(top.right.val)) {
                    queue.offer(top.right);
                }

                if (parent.containsKey(top.val) && !visited.containsKey(parent.get(top.val).val)) {
                    queue.offer(parent.get(top.val));
                }
            }

            k--;
        }

        while (!queue.isEmpty()) {
            ans.add(queue.poll().val);
        }
        return ans;
    }


    // V3
    // IDEA: BFS
    // https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/solutions/7205194/the-best-detailed-explanation-let-you-ln-i610/
    public List<Integer> distanceK_3(TreeNode root, TreeNode target, int k) {

        HashMap<TreeNode, TreeNode> map = new HashMap<>();
        getParent(root, map);

        Queue<TreeNode> q = new LinkedList<>();
        q.add(target);
        int lvl = 0;
        HashSet<TreeNode> set = new HashSet<>();
        set.add(target);
        while (!q.isEmpty()) {

            int size = q.size();
            if (lvl == k)
                break;
            for (int i = 0; i < size; i++) {
                TreeNode curr = q.remove();

                if (curr.left != null && !set.contains(curr.left)) {
                    q.add(curr.left);
                    set.add(curr.left);
                }

                if (curr.right != null && !set.contains(curr.right)) {
                    q.add(curr.right);
                    set.add(curr.right);
                }

                TreeNode parent = map.get(curr);
                if (!set.contains(parent) && parent != null) {
                    q.add(parent);
                    set.add(parent);
                }
            }
            lvl++;
        }

        List<Integer> list = new ArrayList<>();
        while (!q.isEmpty()) {
            TreeNode curr = q.remove();
            list.add(curr.val);
        }
        return list;
    }

    public void getParent(TreeNode root, HashMap<TreeNode, TreeNode> map) {

        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);

        while (!q.isEmpty()) {
            int size = q.size();

            for (int i = 0; i < size; i++) {
                TreeNode curr = q.remove();

                if (curr.left != null) {
                    q.add(curr.left);
                    map.put(curr.left, curr);
                }

                if (curr.right != null) {
                    q.add(curr.right);
                    map.put(curr.right, curr);
                }
            }
        }
    }


}
