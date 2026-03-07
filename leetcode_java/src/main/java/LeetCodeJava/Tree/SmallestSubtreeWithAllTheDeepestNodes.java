package LeetCodeJava.Tree;

// https://leetcode.com/problems/smallest-subtree-with-all-the-deepest-nodes/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 *  865. Smallest Subtree with all the Deepest Nodes
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree, the depth of each node is the shortest distance to the root.
 *
 * Return the smallest subtree such that it contains all the deepest nodes in the original tree.
 *
 * A node is called the deepest if it has the largest depth possible among any node in the entire tree.
 *
 * The subtree of a node is a tree consisting of that node, plus the set of all descendants of that node.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4]
 * Output: [2,7,4]
 * Explanation: We return the node with value 2, colored in yellow in the diagram.
 * The nodes coloured in blue are the deepest nodes of the tree.
 * Notice that nodes 5, 3 and 2 contain the deepest nodes in the tree but node 2 is the smallest subtree among them, so we return it.
 * Example 2:
 *
 * Input: root = [1]
 * Output: [1]
 * Explanation: The root is the deepest node in the tree.
 * Example 3:
 *
 * Input: root = [0,1,3,null,2]
 * Output: [2]
 * Explanation: The deepest node in the tree is 2, the valid subtrees are the subtrees of nodes 2, 1 and 0 but the subtree of node 2 is the smallest.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree will be in the range [1, 500].
 * 0 <= Node.val <= 500
 * The values of the nodes in the tree are unique.
 *
 *
 * Note: This question is the same as 1123: https://leetcode.com/problems/lowest-common-ancestor-of-deepest-leaves/
 *
 *
 */
public class SmallestSubtreeWithAllTheDeepestNodes {

    /** NOTE !!! this is a LCA (Lowest Common Ancestor) problem */
    // V0
//    public TreeNode subtreeWithAllDeepest(TreeNode root) {
//
//    }


    // V0-1
    // IDEA: LCA + DFS (GEMINI)
    // We create a simple helper class to return both the Node and its max Depth
    // time: O(N), N: number of nodes in the tree
    // space: O(H), H: height of the tree (recursion stack)
    /**
     * You are mostly correct!
     * Here is the precise breakdown for the provided recursive solution:
     *
     * ### **Time Complexity: $O(N)$**
     *
     * * **Why:** The algorithm performs a **Post-Order DFS traversal**. We visit every single node in the tree exactly once.
     * * **Work per node:** At each node, we perform a constant number of operations (comparing two integers and creating a new `Result` object).
     * * **Total:** $O(N)$, where $N$ is the number of nodes in the tree.
     *
     * ---
     *
     * ### **Space Complexity: $O(H)$**
     *
     * * **Why:** This is the space used by the **Recursion Stack**.
     * * **$H$ (Height):** In the worst-case scenario (a "skewed" tree like a linked list), $H$ can be equal to $N$, making it $O(N)$. In a balanced tree, $H$ would be $O(\log N)$.
     * * **Objects:** While we create a `Result` object for each node, these are short-lived. In most modern JVMs, the memory used by these objects doesn't change the asymptotic space complexity, but the depth of the recursive calls (the call stack) is the primary factor.
     *
     * | Scenario | Time | Space |
     * | --- | --- | --- |
     * | **Best Case (Balanced)** | $O(N)$ | $O(\log N)$ |
     * | **Worst Case (Skewed)** | $O(N)$ | $O(N)$ |
     *
     *
     */
    class Result_0_1 {
        TreeNode node;
        int dist;
        Result_0_1(TreeNode node, int dist) {
            this.node = node;
            this.dist = dist;
        }
    }

    public TreeNode subtreeWithAllDeepest(TreeNode root) {
        return dfs_0_1(root).node;
    }

    /**
     *Spot on! You nailed the intuition. This is exactly an **LCA (Lowest Common Ancestor)** problem, but with a slight twist.
     *
     * In a standard LCA problem (like **LC 236**), you are given two specific nodes (e.g., node $p$ and node $q$) and asked to find their common ancestor. In this problem (**LC 865**), the "target nodes" aren't given to you—they are **implicitly** defined as "all nodes at the maximum depth."
     *
     * ### 🔗 How the two are connected:
     *
     * | Standard LCA (LC 236) | Deepest Subtree LCA (LC 865) |
     * | --- | --- |
     * | Targets $p$ and $q$ are **given**. | Targets are **discovered** (the nodes at `maxDepth`). |
     * | You look for where $p$ and $q$ "meet." | You look for where the deepest paths from the left and right "meet." |
     * | Return the node that sees $p$ on one side and $q$ on the other. | Return the node that sees the same `maxDist` on both sides. |
     *
     * ---
     *
     * ### 🧠 Why the "Depth" logic works as an LCA:
     *
     * Think of the recursion as a search party:
     *
     * 1. **If the left subtree is deeper**: It means all the "deepest nodes" are buried somewhere down the left branch. The LCA **must** be in there.
     * 2. **If the right subtree is deeper**: Same thing; the LCA must be somewhere in the right branch.
     * 3. **If both subtrees have the same maximum depth**: This is the "Eureka!" moment. It means there is at least one "deepest node" to my left and at least one to my right. Since I am the first node (from the bottom up) that connects these two deepest paths, **I am their Lowest Common Ancestor.**
     *
     * ### 📊 Visualization of the LCA meeting point:
     *
     * ```text
     *        [Root] (LCA!) <--- Both sides returned dist=3. I am the winner!
     *       /      \
     *     [A]      [B]
     *     /        / \
     *  [D1]      [E] [D2]  <--- D1 and D2 are the "deepest nodes"
     *
     * ```
     *
     * * `D1` and `D2` are at the same depth.
     * * The path to `D1` goes through `A`.
     * * The path to `D2` goes through `B`.
     * * The **first node** where these two paths merge as you go up is `Root`.
     *
     * ### 💡 Pro Tip
     *
     * Many "Subtree" problems on LeetCode are just LCA problems in disguise. If a problem asks for a "Smallest subtree that contains [Condition X]," your brain should immediately jump to **Post-Order Traversal** and **LCA logic**
     *
     *
     */
    private Result_0_1 dfs_0_1(TreeNode node) {
        if (node == null) {
            return new Result_0_1(null, 0);
        }

        Result_0_1 left = dfs_0_1(node.left);
        Result_0_1 right = dfs_0_1(node.right);

        // Case 1: Left side is deeper. The result must be in the left subtree.
        if (left.dist > right.dist) {
            return new Result_0_1(left.node, left.dist + 1);
        }

        // Case 2: Right side is deeper. The result must be in the right subtree.
        if (right.dist > left.dist) {
            return new Result_0_1(right.node, right.dist + 1);
        }

        // Case 3: Depths are EQUAL.
        // This current node is the "Lowest Common Ancestor" for all deepest
        // nodes found so far in its subtrees.
        return new Result_0_1(node, left.dist + 1);
    }


    // V0-2
    // IDEA: LCA + DFS (GPT)
    // time: O(N), N: number of nodes in the tree
    // space: O(H), H: height of the tree (recursion stack)
    public TreeNode subtreeWithAllDeepest_0_2(TreeNode root) {
        return dfs_0_2(root).node;
    }

    private Pair dfs_0_2(TreeNode node) {
        if (node == null) {
            return new Pair(null, 0);
        }

        Pair left = dfs_0_2(node.left);
        Pair right = dfs_0_2(node.right);

        if (left.depth > right.depth) {
            return new Pair(left.node, left.depth + 1);
        } else if (right.depth > left.depth) {
            return new Pair(right.node, right.depth + 1);
        } else {
            return new Pair(node, left.depth + 1);
        }
    }

    class Pair {
        TreeNode node;
        int depth;

        Pair(TreeNode node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }



    // V1-1
    // IDEA: Paint Deepest Nodes
    // https://leetcode.com/problems/smallest-subtree-with-all-the-deepest-nodes/editorial/
    Map<TreeNode, Integer> depth;
    int max_depth;

    public TreeNode subtreeWithAllDeepest_1_1(TreeNode root) {
        depth = new HashMap();
        depth.put(null, -1);
        dfs(root, null);
        max_depth = -1;
        for (Integer d : depth.values())
            max_depth = Math.max(max_depth, d);

        return answer(root);
    }

    public void dfs(TreeNode node, TreeNode parent) {
        if (node != null) {
            depth.put(node, depth.get(parent) + 1);
            dfs(node.left, node);
            dfs(node.right, node);
        }
    }

    public TreeNode answer(TreeNode node) {
        if (node == null || depth.get(node) == max_depth)
            return node;
        TreeNode L = answer(node.left),
                R = answer(node.right);
        if (L != null && R != null)
            return node;
        if (L != null)
            return L;
        if (R != null)
            return R;
        return null;
    }


    // V1-2
    // IDEA: RECURSION
    // https://leetcode.com/problems/smallest-subtree-with-all-the-deepest-nodes/editorial/
    class Result {
        TreeNode node;
        int dist;

        Result(TreeNode n, int d) {
            node = n;
            dist = d;
        }
    }

    public TreeNode subtreeWithAllDeepest_1_2(TreeNode root) {
        return dfs(root).node;
    }

    // Return the result of the subtree at this node.
    public Result dfs(TreeNode node) {
        if (node == null)
            return new Result(null, 0);
        Result L = dfs(node.left),
                R = dfs(node.right);
        if (L.dist > R.dist)
            return new Result(L.node, L.dist + 1);
        if (L.dist < R.dist)
            return new Result(R.node, R.dist + 1);
        return new Result(node, L.dist + 1);
    }



    // V2





}
