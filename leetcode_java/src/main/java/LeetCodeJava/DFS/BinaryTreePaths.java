package LeetCodeJava.DFS;

// https://leetcode.com/problems/binary-tree-paths/
/**
 * 257. Binary Tree Paths
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree, return all root-to-leaf paths in any order.
 *
 * A leaf is a node with no children.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,3,null,5]
 * Output: ["1->2->5","1->3"]
 * Example 2:
 *
 * Input: root = [1]
 * Output: ["1"]
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 100].
 * -100 <= Node.val <= 100
 *
 *
 */

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BinaryTreePaths {

    // V0
//    public List<String> binaryTreePaths(TreeNode root) {
//
//    }


    // V0-1
    // IDEA: DFS (gemini)
    public List<String> binaryTreePaths_0_1(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root == null) {
            return res;
        }

        // Start DFS from the root with its value as the initial path
        dfs(root, String.valueOf(root.val), res);
        return res;
    }

    /** NOTE !!!
     *
     *  help func return NOTHING
     */
    private void dfs(TreeNode node, String path, List<String> res) {

        /** NOTE !!!
         *
         *  we ONLY add cur path to res
         *  if it's a `leaf`
         */
        // 1. Leaf check: If we reach a leaf, save the completed path
        if (node.left == null && node.right == null) {
            res.add(path);
            return;
        }

        /** NOTE !!!
         *
         *  we ONLY do dfs call if sub tree is NOT null
         *   (left tree for example)
         *
         *  -> this make us DON'T need to handle edge cases,
         *     e.g. node.left is null, but we still try to add path to it,
         *          and validate the res... and so on
         */
        // 2. Traverse Left: If left exists, append "->val" and go down
        if (node.left != null) {
            /** NOTE !!!
             *
             *  we do `path update` within DFS call
             */
            dfs(node.left, path + "->" + node.left.val, res);
        }

        // 3. Traverse Right: If right exists, append "->val" and go down
        if (node.right != null) {
            /** NOTE !!!
             *
             *  we do `path update` within DFS call
             */
            dfs(node.right, path + "->" + node.right.val, res);
        }
    }


    // V0-2
    // IDEA: DFS (GPT)
    List<String> nodePathList = new ArrayList<>();

    public List<String> binaryTreePaths_0_2(TreeNode root) {
        if (root == null)
            return nodePathList;

        nodePathHelper(root, "");
        return nodePathList;
    }

    private void nodePathHelper(TreeNode root, String path) {
        if (root == null)
            return;

        // build path
        if (path.isEmpty()) {
            path = String.valueOf(root.val);
        } else {
            path = path + "->" + root.val;
        }

        // if leaf → add to result
        if (root.left == null && root.right == null) {
            nodePathList.add(path);
            return;
        }

        // recurse
        nodePathHelper(root.left, path);
        nodePathHelper(root.right, path);
    }


    // V0-3
    // IDEA: DFS + BACKTRACK (gemini)
    public List<String> binaryTreePaths_0_3(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root == null)
            return res;

        StringBuilder sb = new StringBuilder();
        // Add the first node value
        sb.append(root.val);

        dfs(root, sb, res);
        return res;
    }

    private void dfs(TreeNode node, StringBuilder sb, List<String> res) {
        // 1. Base Case: If we hit a leaf, add the current string to results
        if (node.left == null && node.right == null) {
            res.add(sb.toString());
            return;
        }

        // 2. Remember the length before we add children
        int len = sb.length();

        // 3. Explore Left
        if (node.left != null) {
            sb.append("->").append(node.left.val);
            dfs(node.left, sb, res);
            /** NOTE !!!
             *
             *  need backtrack, since StringBuilder is NOT a primary type.
             *  it will keep its val (path) to the next recursion run.
             *
             *  -> so we need to `undo` its last op.
             *     e.g. backtrack
             */
            // BACKTRACK: Remove the "->val" we just added
            sb.setLength(len);
        }

        // 4. Explore Right
        if (node.right != null) {
            sb.append("->").append(node.right.val);
            dfs(node.right, sb, res);
            // BACKTRACK: Remove the "->val" we just added
            sb.setLength(len);
        }
    }


    // V0-4
    // IDEA : DFS
    private List<List<Integer>> collected = new ArrayList<>();

    /**
     * time = O(N)
     * space = O(H)
     */
    public List<String> binaryTreePaths_0_4(TreeNode root) {

        if (root == null){
            return new ArrayList<>();
        }

        if (root.left == null && root.right == null){
            return new ArrayList<>(Collections.singleton(String.valueOf(root.val)));
        }

        List<Integer> cur = new ArrayList<>();
        getPath(root, cur);
        List<String> res = new ArrayList<>();

        for (List<Integer> item : collected){
            String tmp = "";
            for (int i = 0; i < item.size(); i++){
                if (i > 0){
                    tmp += ("->" + String.valueOf(item.get(i)));
                }else{
                    tmp += String.valueOf(item.get(i));
                }
            }
            res.add(tmp);
        }

        return res;
    }

    private void getPath(TreeNode root, List<Integer> cur){

        if (root == null){
            return;
        }

        cur.add(root.val);

        if (root.left == null && root.right == null){
            this.collected.add(new ArrayList<Integer>(cur));
        }else{
           getPath(root.left, cur);
           getPath(root.right, cur);
        }

        // backtrack
        cur.remove(cur.size()-1);
    }

    // V1
    // https://leetcode.com/problems/binary-tree-paths/editorial/
    // IDEA : DFS (RECURSION)
    /**
     * time = O(N)
     * space = O(H)
     */
    public void construct_paths(TreeNode root, String path, LinkedList<String> paths) {
        if (root != null) {
            path += Integer.toString(root.val);
            if ((root.left == null) && (root.right == null))  // if reach a leaf
                paths.add(path);  // update paths
            else {
                path += "->";  // extend the current path
                construct_paths(root.left, path, paths);
                construct_paths(root.right, path, paths);
            }
        }
    }

    /**
     * time = O(N)
     * space = O(H)
     */
    public List<String> binaryTreePaths_2(TreeNode root) {
        LinkedList<String> paths = new LinkedList();
        construct_paths(root, "", paths);
        return paths;
    }

    // V2
    // https://leetcode.com/problems/binary-tree-paths/editorial/
    // IDEA : ITERATIVE
    /**
     * time = O(N)
     * space = O(N)
     */
    public List<String> binaryTreePaths_3(TreeNode root) {
        LinkedList<String> paths = new LinkedList();
        if (root == null)
            return paths;

        LinkedList<TreeNode> node_stack = new LinkedList();
        LinkedList<String> path_stack = new LinkedList();
        node_stack.add(root);
        path_stack.add(Integer.toString(root.val));
        TreeNode node;
        String path;
        while ( !node_stack.isEmpty() ) {
            node = node_stack.pollLast();
            path = path_stack.pollLast();
            if ((node.left == null) && (node.right == null))
                paths.add(path);
            if (node.left != null) {
                node_stack.add(node.left);
                path_stack.add(path + "->" + Integer.toString(node.left.val));
            }
            if (node.right != null) {
                node_stack.add(node.right);
                path_stack.add(path + "->" + Integer.toString(node.right.val));
            }
        }
        return paths;
    }




}
