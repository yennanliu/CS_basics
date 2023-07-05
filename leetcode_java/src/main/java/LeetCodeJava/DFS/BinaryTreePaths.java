package LeetCodeJava.DFS;

// https://leetcode.com/problems/binary-tree-paths/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BinaryTreePaths {

    private List<List<Integer>> collected = new ArrayList<>();

    public List<String> binaryTreePaths(TreeNode root) {

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
            //System.out.println("item = " + item.toString());
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

        //System.out.println("collected = " + collected.toString());
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

    public List<String> binaryTreePaths_2(TreeNode root) {
        LinkedList<String> paths = new LinkedList();
        construct_paths(root, "", paths);
        return paths;
    }

    // V2
    // https://leetcode.com/problems/binary-tree-paths/editorial/
    // IDEA : ITERATIVE
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
