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

    // V0-1
    // IDEA: DFS + `Parent Map` + BFS (fixed by gpt)
    /**  IDEA:
     *
     * Why this works ?
     *
     *  Tree -> Graph -> BFS (visiting)
     *
     * 	•	From target you need to explore `all directions` reachable in k steps:
     * 	   ` left, right, and up (to parent). `
     * 	    Converting the tree to an `undirected graph` (children + parent edges)
     * 	    and then running BFS from target to depth k returns the desired nodes.
     *
     * 	•	visited ensures we don’t revisit nodes (which would otherwise make the BFS
     *    	incorrect / infinite once parent edges are present).
     *
     */
    List<Integer> res = new ArrayList<>();
    /**
     * parentMap stores `parent` pointers for every node (node -> parent).
     */
    Map<TreeNode, TreeNode> parentMap = new HashMap<>();

    public List<Integer> distanceK_0_1(TreeNode root, TreeNode target, int k) {
        if (root == null)
            return res;

        // 1️⃣ Build parent map for all nodes
        buildParentMap(root, null);

        /**
         * 	•	We do a breadth-first search (BFS) starting at the target node.
         * 	•	queue holds the frontier for BFS.
         * 	•	visited prevents revisiting nodes
         *   	(prevents cycles when we traverse parent links too).
         * 	•	Enqueue target and mark visited immediately so we don’t re-add it.
         * 	•	dist tracks current BFS depth (distance from target).
         */
        // 2️⃣ BFS starting from target, stop at distance k
        Queue<TreeNode> queue = new LinkedList<>();
        Set<TreeNode> visited = new HashSet<>();
        queue.offer(target);
        visited.add(target);
        int dist = 0;

        /**
         * 	-	Each loop iteration processes one BFS “level”
         * 	    (all nodes at the same distance from target).
         *
         * 	-	If current distance dist equals k, the nodes currently
         * 	    in queue are exactly the nodes at distance k.
         *
         *  - We collect their values and break out — no need to explore further.
         */
        while (!queue.isEmpty()) {
            int size = queue.size();
            if (dist == k) {
                // collect all nodes currently in the queue
                for (TreeNode node : queue) {
                    res.add(node.val);
                }
                break;
            }
            /**
             *  NOTE !!!
             *
             *   for each node, we visit `cur.left, cur.right, and its parent` via BFS
             *
             *
             * 	- Process the size nodes of the current level:
             * 	   - For each cur, try to move to `cur.left, cur.right, and its parent.`
             * 	   - visited.add(node) returns true only if node was not already present in visited.
             * 	     That both checks and marks in one call, so if (node != null && visited.add(node))
             * 	     queue.offer(node); is a compact idiom to avoid duplicates.
             *
             * 	-	After processing the whole level, increment dist and continue to next level.
             * 	- 	If BFS finishes without dist == k (e.g. k larger than tree diameter), res remains empty.
             * 	- 	Finally return res.
             */
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.poll();
                // explore neighbors: left, right, parent
                if (cur.left != null && visited.add(cur.left)) {
                    queue.offer(cur.left);
                }
                if (cur.right != null && visited.add(cur.right)) {
                    queue.offer(cur.right);
                }
                TreeNode parent = parentMap.get(cur);
                if (parent != null && visited.add(parent)) {
                    queue.offer(parent);
                }
            }
            dist++;
        }

        return res;
    }

    /**  NOTE !!! help func
     *
     * 	•	We need to be able to move `upwards` from any node (to parent).
     * 	A binary tree node only knows left/right children, so we precompute parents by a DFS.
     *
     *
     * 	•	Simple DFS that records parent of each node (parentMap.put(node, parent)).
     * 	•	For root we pass parent = null.
     * 	•	After this every node maps to its parent (or null for root).
     */
    private void buildParentMap(TreeNode node, TreeNode parent) {
        if (node == null)
            return;
        parentMap.put(node, parent);
        buildParentMap(node.left, node);
        buildParentMap(node.right, node);
    }

    // VO-2
    // IDEA: PURE DFS (gpt)
    List<Integer> res_0_2 = new ArrayList<>();

    public List<Integer> distanceK_0_2(TreeNode root, TreeNode target, int k) {
        dfs(root, target, k);
        return res_0_2;
    }

    // dfs() returns the distance from current node to target, or -1 if target is not in this subtree
    private int dfs(TreeNode node, TreeNode target, int k) {
        if (node == null)
            return -1;

        // 🎯 Base case: found target
        if (node == target) {
            // collect all nodes downward at distance k from target
            collectDownward(node, 0, k);
            return 0; // distance from target to itself
        }

        // 🔹 Search left subtree
        int leftDist = dfs(node.left, target, k);
        if (leftDist != -1) {
            int distFromNode = leftDist + 1;
            if (distFromNode == k) {
                res_0_2.add(node.val);
            } else {
                // find nodes in right subtree that are k - distFromNode - 0 away
                collectDownward(node.right, distFromNode + 1, k);
            }
            return distFromNode;
        }

        // 🔸 Search right subtree
        int rightDist = dfs(node.right, target, k);
        if (rightDist != -1) {
            int distFromNode = rightDist + 1;
            if (distFromNode == k) {
                res_0_2.add(node.val);
            } else {
                // find nodes in left subtree that are k - distFromNode away
                collectDownward(node.left, distFromNode + 1, k);
            }
            return distFromNode;
        }

        // target not found in either subtree
        return -1;
    }

    // helper: collect all nodes downward at distance k
    private void collectDownward(TreeNode node, int dist, int k) {
        if (node == null)
            return;
        if (dist == k) {
            res_0_2.add(node.val);
            return;
        }
        collectDownward(node.left, dist + 1, k);
        collectDownward(node.right, dist + 1, k);
    }

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
