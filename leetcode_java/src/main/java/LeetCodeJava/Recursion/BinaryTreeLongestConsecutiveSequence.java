package LeetCodeJava.Recursion;

// https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/description/
// https://leetcode.ca/all/298.html

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 *
 *  298. Binary Tree Longest Consecutive Sequence
 * Given a binary tree, find the length of the longest consecutive sequence path.
 *
 * The path refers to any sequence of nodes from some starting node to any node in the tree along the parent-child connections. The longest consecutive path need to be from parent to child (cannot be the reverse).
 *
 * Example 1:
 *
 * Input:
 *
 *    1
 *     \
 *      3
 *     / \
 *    2   4
 *         \
 *          5
 *
 * Output: 3
 *
 * Explanation: Longest consecutive sequence path is 3-4-5, so return 3.
 * Example 2:
 *
 * Input:
 *
 *    2
 *     \
 *      3
 *     /
 *    2
 *   /
 *  1
 *
 * Output: 2
 *
 * Explanation: Longest consecutive sequence path is 2-3, not 3-2-1, so return 2.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Facebook Google Uber
 *
 */
public class BinaryTreeLongestConsecutiveSequence {

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
    // V0
    // IDEA : DFS + BFS (fix by gpt)
    // TODO : validate it
    int longestCnt = 0;

    public int longestConsecutive(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // bfs
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();  // Poll the node from the queue
            int length = dfsCount(node);  // Use the node for dfs
            longestCnt = Math.max(longestCnt, length);

            if (node.left != null) {
                q.add(node.left);
            }
            if (node.right != null) {
                q.add(node.right);
            }
        }

        return longestCnt;
    }

    private int dfsCount(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int leftCount = 0, rightCount = 0;

        if (root.left != null && root.val + 1 == root.left.val) {
            leftCount = 1 + dfsCount(root.left);  // Count consecutive path on the left
        }

        if (root.right != null && root.val + 1 == root.right.val) {
            rightCount = 1 + dfsCount(root.right);  // Count consecutive path on the right
        }

        /**
         * NOTE !!!
         *
         *  simply compare leftCount and rightCount,
         *  no need to do below :
         *
         *
         *  return Math.max(
         *                 Math.max(this.dfsCount(root.left), this.dfsCount(root.right)),
         *                 this.dfsCount(root)
         *         );
         *
         * above code may have issue below :
         *
         * 	1.	Redundant Call: this.dfsCount(root) is included in the comparison with dfsCount(root.left) and dfsCount(root.right), leading to redundant recursion. This will likely cause an infinite loop or unnecessary repeated calculations since dfsCount(root) will keep calling itself.
         * 	2.	Logic Error: The comparison includes the root itself in a way that doesn’t make sense in the context of a consecutive sequence. You want to evaluate the left and right children, not compare the root node against itself.
         * 	3.	Performance Degradation: Since this.dfsCount(root) is unnecessarily called multiple times, it can dramatically increase the recursion depth and the overall computational complexity.
         *
         */
        // Return the max length between left and right consecutive paths
        return Math.max(leftCount, rightCount) + 1;  // Include current node
    }

    // V0-0-1
    // IDEA: 437. Path Sum III (gpt)
    // TODO: validate
    /**
     *  DEMO:
     *
     *  ✅ Yes — that’s a very sharp observation.
     * You can absolutely reuse the LC 437 (Path Sum III) idea to solve LC 298 (Longest Consecutive Sequence) in a clean, efficient way — just swap out the “path sum accumulation” logic with “check subpath consecutiveness” logic.
     *
     * ⸻
     *
     * 🧠 Why this works
     *
     * In LC 437, the trick is:
     * 	•	Maintain a cur list = path from root → current node.
     * 	•	At each node, scan backward to get all subpaths ending at this node, and aggregate their sums into a map.
     *
     * For LC 298, instead of summing, we can:
     * 	•	Maintain cur list = path from root → current node.
     * 	•	At each node, scan backward through cur to find the longest consecutive increasing subpath ending at this node.
     * 	•	Keep a global maxLen.
     *
     * This perfectly matches your earlier idea:
     *
     * for node “1”, we want to collect [1], [1,3], [1,3,2], [1,3,4], [1,3,4,5] — i.e. all subpaths ending at each node.
     *
     * ⸻
     *
     * ✅ Walkthrough on your example tree
     *
     *    1
     *     \
     *      3
     *     / \
     *    2   4
     *         \
     *          5
     *
     * 	•	Path to node 5: [1, 3, 4, 5]
     * 	•	Backward scan: 5 ← 4 ← 3 ← 1
     * 	•	Consecutive sequence length = 3 (3 → 4 → 5)
     * 	•	Path to node 2: [1, 3, 2]
     * 	•	2 ← 3 ← 1 → break at 3→2 (not consecutive)
     * 	•	max remains 3
     *
     * Final maxConsecutive = 3 ✅
     *
     * ⸻
     *
     * 📝 Key differences vs your original code
     * 	•	No need for pathMap2 to store all paths.
     * 	•	We compute on the fly, like LC 437’s prefix-sum approach.
     * 	•	Uses List<Integer> path + backtrack, but no extra map needed.
     *
     * ⸻
     *
     * ✅ Advantages
     * 	•	Reuses a well-known LC 437 DFS pattern.
     * 	•	Time complexity: O(n × h) worst-case (where h is height), but typically O(n) since consecutive sequences break early.
     * 	•	Clean, Java 8 compatible.
     *
     */
    int maxConsecutive = 0;
    public int longestConsecutive_0_0_1(TreeNode root) {
        if (root == null) {
            return 0;
        }
        dfs(root, new ArrayList<>());
        return maxConsecutive;
    }

    private void dfs(TreeNode node, List<Integer> path) {
        if (node == null) {
            return;
        }

        // add current node to path
        path.add(node.val);

        // check all sub-paths ending at this node (like LC 437 sum scan)
        int len = 1;
        int prev = node.val;
        // scan backward
        for (int i = path.size() - 2; i >= 0; i--) {
            if (path.get(i) + 1 == prev) {
                len++;
                prev = path.get(i);
            } else {
                break;  // stop if not consecutive anymore
            }
        }
        maxConsecutive = Math.max(maxConsecutive, len);

        // DFS left & right
        dfs(node.left, path);
        dfs(node.right, path);

        // backtrack
        path.remove(path.size() - 1);
    }


    // V0-1
    // IDEA: DFS + PATH SUM + CONSECUTIVE PATH CHECK (fixed by gpt)
    // LC 298 - Binary Tree Longest Consecutive Sequence
    // DFS + pathMap2 (node -> path from root to this node) + post traversal to get max consecutive path
    Map<TreeNode, List<List<Integer>>> pathMap2 = new HashMap<>();
    public int longestConsecutive_0_1(TreeNode root) {
        if (root == null) return 0;
        collectAllPaths(root, new ArrayList<>(), root);

        System.out.println(">>> pathMap2 = ");
        for (TreeNode node : pathMap2.keySet()) {
            System.out.println(node.val + " -> " + pathMap2.get(node));
        }

        int maxLen = 0;
        for (List<List<Integer>> paths : pathMap2.values()) {
            for (List<Integer> p : paths) {
                if (isConsecutive(p)) {
                    maxLen = Math.max(maxLen, p.size());
                }
            }
        }
        return maxLen;
    }

// NOTE !!! below is WRONG
//    private void getNodePath(TreeNode node, List<Integer> path) {
//        if (node == null) {
//            return;
//        }
//
//        // Add current node to path
//        path.add(node.val);
//        // ⚠️ Important: store a COPY of the current path for this node
//        pathMap2.put(node, new ArrayList<>(path));
//
//        // Recurse left and right
//        getNodePath(node.left, path);
//        getNodePath(node.right, path);
//
//        // Backtrack (undo)
//        path.remove(path.size() - 1);
//    }

    private void collectAllPaths(TreeNode node, List<Integer> path, TreeNode rootAncestor) {
        if (node == null) return;

        path.add(node.val);

        /**
         * Put a COPY of current path into the ancestor's path list
         */
        // V1
//        pathMap2.computeIfAbsent(rootAncestor, k -> new ArrayList<>())
//                .add(new ArrayList<>(path));
        // V2
        // store multiple paths for each node
        Map<TreeNode, List<List<Integer>>> pathMap2 = new HashMap<>();
        // somewhere in DFS when you want to store the current path
        pathMap2.computeIfAbsent(rootAncestor, k -> new ArrayList<>())
                .add(new ArrayList<>(path));

        collectAllPaths(node.left, path, rootAncestor);
        collectAllPaths(node.right, path, rootAncestor);

        path.remove(path.size() - 1);
    }

    private boolean isConsecutive(List<Integer> path) {
        int prev = path.get(0);
        for (int i = 1; i < path.size(); i++) {
            if (path.get(i) != prev + 1) {
                return false;
            }
            prev = path.get(i);
        }
        return true;
    }

    // V0-2
    // IDEA: DFS (fixed by gpt)
    // TODO: validate
    public int longestConsecutive_0_2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return dfs(root, root.val - 1, 0);
    }

    private int dfs(TreeNode node, int prevVal, int currLen) {
        if (node == null) {
            return currLen;
        }

        // Check consecutive condition
        if (node.val == prevVal + 1) {
            currLen = currLen + 1;
        } else {
            currLen = 1;
        }

        int leftMax = dfs(node.left, node.val, currLen);
        int rightMax = dfs(node.right, node.val, currLen);

        // Return max among current, left subtree, right subtree
        return Math.max(currLen, Math.max(leftMax, rightMax));
    }

    // V0-3
    // IDEA: DFS (gemini)
    // TODO: validate
    // Global variable to track the maximum consecutive path length found anywhere in the tree.
    private int maxLen = 0;

    public int longestConsecutive_0_3(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // Start the DFS. The return value of the helper is discarded in the main function;
        // the global maxLen is updated inside the helper.
        dfs(root);

        return maxLen;
    }

    /**
     * Finds the length of the longest consecutive sequence *ending* at the current node.
     * @param node The current node.
     * @return The length of the longest consecutive sequence ending at 'node'.
     */
    private int dfs(TreeNode node) {
        if (node == null) {
            return 0;
        }

        // Recursively find the longest consecutive path lengths ending at children.
        int leftLen = dfs(node.left);
        int rightLen = dfs(node.right);

        // Current length starts at 1 (the node itself).
        int currentLen = 1;

        // Check if the current node extends the left path.
        if (node.left != null && node.left.val == node.val + 1) {
            currentLen = Math.max(currentLen, leftLen + 1);
        }

        // Check if the current node extends the right path.
        if (node.right != null && node.right.val == node.val + 1) {
            currentLen = Math.max(currentLen, rightLen + 1);
        }

        // Update the global maximum path length.
        maxLen = Math.max(maxLen, currentLen);

        // Return the length of the path that can be EXTENDED by the parent.
        // The parent can only choose to extend the current node's path in ONE direction (upwards).
        return currentLen;
    }


    // V1
    // IDEA : TREE, RECURSION
    // https://leetcode.ca/2016-09-23-298-Binary-Tree-Longest-Consecutive-Sequence/
    private int ans;

    public int longestConsecutive_1_1(TreeNode root) {
        dfs_1(root);
        return ans;
    }

    private int dfs_1(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int l = dfs_1(root.left) + 1;
        int r = dfs_1(root.right) + 1;
        if (root.left != null && root.left.val - root.val != 1) {
            l = 1;
        }
        if (root.right != null && root.right.val - root.val != 1) {
            r = 1;
        }
        int t = Math.max(l, r);
        ans = Math.max(ans, t);
        return t;
    }

    // V2
    // IDEA : DFS
    // https://walkccc.me/LeetCode/problems/298/#__tabbed_1_2
    public int longestConsecutive_2_1(TreeNode root) {
        if (root == null)
            return 0;
        return dfs_2(root, -1, 0, 1);
    }

    private int dfs_2(TreeNode root, int target, int length, int mx) {
        if (root == null)
            return mx;
        if (root.val == target)
            mx = Math.max(mx, ++length);
        else
            length = 1;
        return Math.max(dfs_2(root.left, root.val + 1, length, mx),
                dfs_2(root.right, root.val + 1, length, mx));
    }

    // V3
    // IDEA : DFS
    // https://blog.csdn.net/qq_46105170/article/details/108481001
    private int res;

    public int longestConsecutive_3(TreeNode root) {
        dfs_3(root);
        return res;
    }

    // The dfs function starts at the root and returns the length of the longest consecutive sequence
    private int dfs_3(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // Initialize the length to 1
        int len = 1;

        // Get the longest consecutive path from left and right child nodes
        int left = dfs_3(root.left), right = dfs_3(root.right);

        // If the left child is consecutive, increase the length
        if (root.left != null && root.left.val == root.val + 1) {
            len = Math.max(len, 1 + left);
        }

        // If the right child is consecutive, increase the length
        if (root.right != null && root.right.val == root.val + 1) {
            len = Math.max(len, 1 + right);
        }

        // Update the global result
        res = Math.max(res, len);

        // Return the length of the longest consecutive path starting from the current node
        return len;
    }



}
