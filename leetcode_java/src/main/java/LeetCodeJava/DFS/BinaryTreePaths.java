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

    private void dfs(TreeNode node, String path, List<String> res) {
        // 1. Leaf check: If we reach a leaf, save the completed path
        if (node.left == null && node.right == null) {
            res.add(path);
            return;
        }

        // 2. Traverse Left: If left exists, append "->val" and go down
        if (node.left != null) {
            dfs(node.left, path + "->" + node.left.val, res);
        }

        // 3. Traverse Right: If right exists, append "->val" and go down
        if (node.right != null) {
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
