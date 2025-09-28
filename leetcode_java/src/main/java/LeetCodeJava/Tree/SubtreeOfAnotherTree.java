package LeetCodeJava.Tree;

// https://leetcode.com/problems/subtree-of-another-tree/
/**
 *  572. Subtree of Another Tree
 * Solved
 * Easy
 * Topics
 * Companies
 * Hint
 * Given the roots of two binary trees root and subRoot, return true if there is a subtree of root with the same structure and node values of subRoot and false otherwise.
 *
 * A subtree of a binary tree tree is a tree that consists of a node in tree and all of this node's descendants. The tree tree could also be considered as a subtree of itself.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,4,5,1,2], subRoot = [4,1,2]
 * Output: true
 * Example 2:
 *
 *
 * Input: root = [3,4,5,1,2,null,null,null,null,0], subRoot = [4,1,2]
 * Output: false
 *
 *
 * Constraints:
 *
 * The number of nodes in the root tree is in the range [1, 2000].
 * The number of nodes in the subRoot tree is in the range [1, 1000].
 * -104 <= root.val <= 104
 * -104 <= subRoot.val <= 104
 *
 *
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SubtreeOfAnotherTree {

    // V0
    // IDEA: BFS + `isSameTree` (LC 100)
    /**
     *  IDEA
     *   step 1) bfs traverse all nodes in root
     *   step 2) check if `each node` (visited in step 1)) as SAME TREE as subRoot
     *           if any match, return true directly
     *   step 3) otherwise, return false, since no `match` is found
     */
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        // edge
        if (root == null && subRoot == null) {
            return true;
        }
        // BFS : use `QUEUE` (FIFO)
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode cur = q.poll();
            if (isSameTree_0(cur, subRoot)) {
                return true;
            }
            if (cur.left != null) {
                q.add(cur.left);
            }
            if (cur.right != null) {
                q.add(cur.right);
            }
        }
        return false;
    }

    // same tree : LC 100
    private boolean isSameTree_0(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        if (p.val != q.val) {
            return false;
        }
        return isSameTree_0(p.left, q.left) && isSameTree_0(p.right, q.right);
    }

    // V0-0-1
    // IDEA: BFS + `isSameTree` (LC 100)
    // IDEA 1) DFS
    public boolean isSubtree_0_0_1(TreeNode root, TreeNode subRoot) {
        // edge
        if(root == null && subRoot == null){
            return true;
        }
        if(subRoot == null){
            return true;
        }
        if(root == null){
            return false;
        }

        return isSubTreeHelper(root, subRoot);
    }

    private boolean isSubTreeHelper(TreeNode root, TreeNode subRoot){
        // bfs
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()){
            TreeNode cur = q.poll();
            if(isSameTree2(cur, subRoot)){
                return true;
            }
            if(cur.left != null){
                q.add(cur.left);
            }
            if(cur.right != null){
                q.add(cur.right);
            }
        }
        return false;
    }

    private boolean isSameTree2(TreeNode t1, TreeNode t2){
        if(t1 == null && t2 == null){
            return true;
        }
        if(t1 == null || t2 == null){
            return false;
        }
        if(t1.val != t2.val){
            return false;
        }
        return isSameTree2(t1.left, t2.left)
                && isSameTree2(t1.right, t2.right);
    }


    // V0-1
    // IDEA : DFS + DFS (modified by GPT)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Tree/subtree-of-another-tree.py#L110
    public boolean isSubtree_0_1(TreeNode s, TreeNode t) {
        if (s == null && t == null) {
            return true;
        }
        if (s == null || t == null) {
            return false;
        }
        /**
         * NOTE !!! isSameTree and isSubtree with sub left, sub right tree
         *
         * e.g.
         *   isSubtree(s.left, t)
         *   isSubtree(s.right, t)
         *
         *   -> only take sub tree on s (root), but use the same t (sub root)
         *
         *
         *  NOTE !!!
         *   -> use "OR", so any `true` status can be found and return
         */
        return isSameTree(s, t) || isSubtree_0_1(s.left, t) || isSubtree_0_1(s.right, t);
    }

    /** NOTE !!!
     *
     *  isSameTree func is comparing if s, t are the same tree (same structure, val)
     */
    private boolean isSameTree(TreeNode s, TreeNode t) {

        if (s == null && t == null) {
            return true;
        }
        if (s == null || t == null) {
            return false;
        }
        /**
         *  NOTE !!!
         *
         *   -> below is "and" logic
         */
        return s.val == t.val && isSameTree(s.left, t.left) && isSameTree(s.right, t.right);
    }

    // V0-2
    public boolean isSubtree_0_2(TreeNode root, TreeNode subRoot) {
        if (root == null && subRoot == null) {
            return true;
        }
        if (root == null || subRoot == null) {
            return true;
        }

        // bfs : QUEUE !!! (FIFO)
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode cur = q.poll();
            if (isSameTree_(cur, subRoot)) {
                return true;
            }
            if (cur.left != null) {
                q.add(cur.left);
            }
            if (cur.right != null) {
                q.add(cur.right);
            }
        }
        return false;
    }

    public boolean isSameTree_(TreeNode p, TreeNode q) {
        // edge
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        if (p.val != q.val) {
            return false;
        }

        return isSameTree_(p.left, q.left) &&
                isSameTree_(p.right, q.right);
    }

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/subtree-of-another-tree/editorial/
    public boolean isSubtree_1(TreeNode root, TreeNode subRoot) {

        // If this node is Empty, then no tree is rooted at this Node
        // Hence, "tree rooted at node" cannot be equal to "tree rooted at subRoot"
        // "tree rooted at subRoot" will always be non-empty, as per constraints
        if (root == null) {
            return false;
        }

        // Check if the "tree rooted at root" is identical to "tree roooted at subRoot"
        if (isIdentical(root, subRoot)) {
            return true;
        }

        // If not, check for "tree rooted at root.left" and "tree rooted at root.right"
        // If either of them returns true, return true
        // NOTE !!! either left or right tree equals subRoot is acceptable
        return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
    }

    /** NOTE !!! check this help func */
    private boolean isIdentical(TreeNode node1, TreeNode node2) {

        // If any of the nodes is null, then both must be null
        if (node1 == null || node2 == null) {
            return node1 == null && node2 == null;
        }

        // If both nodes are non-empty, then they are identical only if
        // 1. Their values are equal
        // 2. Their left subtrees are identical
        // 3. Their right subtrees are identical
        return node1.val == node2.val && isIdentical(node1.left, node2.left) && isIdentical(node1.right, node2.right);
    }

    // V2
    // IDEA : STRING MATCH
    // https://leetcode.com/problems/subtree-of-another-tree/editorial/
    public boolean isSubtree_2(TreeNode root, TreeNode subRoot) {
        // Serialize given Nodes
        StringBuilder rootList = new StringBuilder();
        serialize(root, rootList);
        String r = rootList.toString();

        StringBuilder subRootList = new StringBuilder();
        serialize(subRoot, subRootList);
        String s = subRootList.toString();

        // Check if s is in r
        return kmp(s, r);
    }

    // Function to serialize Tree
    private void serialize(TreeNode node, StringBuilder treeStr) {
        if (node == null) {
            treeStr.append("#");
            return;
        }

        treeStr.append("^");
        treeStr.append(node.val);
        serialize(node.left, treeStr);
        serialize(node.right, treeStr);
    }

    // Knuth-Morris-Pratt algorithm to check if `needle` is in `haystack` or not
    private boolean kmp(String needle, String haystack) {
        int m = needle.length();
        int n = haystack.length();

        if (n < m)
            return false;

        // longest proper prefix which is also suffix
        int[] lps = new int[m];
        // Length of Longest Border for prefix before it.
        int prev = 0;
        // Iterating from index-1. lps[0] will always be 0
        int i = 1;

        while (i < m) {
            if (needle.charAt(i) == needle.charAt(prev)) {
                // Length of Longest Border Increased
                prev += 1;
                lps[i] = prev;
                i += 1;
            } else {
                // Only empty border exist
                if (prev == 0) {
                    lps[i] = 0;
                    i += 1;
                } else {
                    // Try finding longest border for this i with reduced prev
                    prev = lps[prev - 1];
                }
            }
        }

        // Pointer for haystack
        int haystackPointer = 0;
        // Pointer for needle.
        // Also indicates number of characters matched in current window.
        int needlePointer = 0;

        while (haystackPointer < n) {
            if (haystack.charAt(haystackPointer) == needle.charAt(needlePointer)) {
                // Matched Increment Both
                needlePointer += 1;
                haystackPointer += 1;
                // All characters matched
                if (needlePointer == m)
                    return true;
            } else {
                if (needlePointer == 0) {
                    // Zero Matched
                    haystackPointer += 1;
                } else {
                    // Optimally shift left needlePointer. Don't change haystackPointer
                    needlePointer = lps[needlePointer - 1];
                }
            }
        }
        return false;
    }

    // V3
    // IDEA : TREE HASH
    // https://leetcode.com/problems/subtree-of-another-tree/editorial/
    // CONSTANTS
    final int MOD_1 = 1000000007;
    final int MOD_2 = 2147483647;

    // Hashing a Node
    long[] hashSubtreeAtNode(TreeNode node, boolean needToAdd) {

        if (node == null)
            return new long[] { 3, 7 };

        long[] left = hashSubtreeAtNode(node.left, needToAdd);
        long[] right = hashSubtreeAtNode(node.right, needToAdd);

        long left1 = (left[0] << 5) % MOD_1;
        long right1 = (right[0] << 1) % MOD_1;
        long left2 = (left[1] << 7) % MOD_2;
        long right2 = (right[1] << 1) % MOD_2;

        long[] hashpair = { (left1 + right1 + node.val) % MOD_1,
                (left2 + right2 + node.val) % MOD_2 };

        if (needToAdd)
            memo.add(hashpair);

        return hashpair;
    }

    // Vector to store hashed value of each node.
    List<long[]> memo = new ArrayList<>();

    public boolean isSubtree_4(TreeNode root, TreeNode subRoot) {

        // Calling and adding hash to List
        hashSubtreeAtNode(root, true);

        // Storing hashed value of subRoot for comparison
        long[] s = hashSubtreeAtNode(subRoot, false);

        // Check if hash of subRoot is present in memo
        for (long[] m : memo) {
            if (m[0] == s[0] && m[1] == s[1])
                return true;
        }

        return false;
    }

}
