package LeetCodeJava.Stack;

// https://leetcode.com/problems/leaf-similar-trees/

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 872. Leaf-Similar Trees
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Consider all the leaves of a binary tree, from left to right order, the values of those leaves form a leaf value sequence.
 *
 *
 *
 * For example, in the given tree above, the leaf value sequence is (6, 7, 4, 9, 8).
 *
 * Two binary trees are considered leaf-similar if their leaf value sequence is the same.
 *
 * Return true if and only if the two given trees with head nodes root1 and root2 are leaf-similar.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root1 = [3,5,1,6,2,9,8,null,null,7,4], root2 = [3,5,1,6,7,4,2,null,null,null,null,null,null,9,8]
 * Output: true
 * Example 2:
 *
 *
 * Input: root1 = [1,2,3], root2 = [1,3,2]
 * Output: false
 *
 *
 * Constraints:
 *
 * The number of nodes in each tree will be in the range [1, 200].
 * Both of the given trees will have values in the range [0, 200].
 *
 *
 */

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
public class LeafSimilarTrees {

    // V0
//    public boolean leafSimilar(TreeNode root1, TreeNode root2) {
//
//    }

    // V0-1
    // IDEA: top-down DFS (gemini)
    public boolean leafSimilar_0_1(TreeNode root1, TreeNode root2) {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        // Fill both sequences
        getLeafSeq(root1, list1);
        getLeafSeq(root2, list2);

        // The most idiomatic and efficient way to compare two lists in Java
        return list1.equals(list2);
    }

    private void getLeafSeq(TreeNode root, List<Integer> list) {
        if (root == null)
            return;

        // Leaf check: Add to list only if it's a leaf
        if (root.left == null && root.right == null) {
            list.add(root.val);
            return; // Optimization: no need to check children of a leaf
        }

        getLeafSeq(root.left, list);
        getLeafSeq(root.right, list);
    }


    // V0-2
    // IDEA: top-down DFS  (GPT)
    public boolean leafSimilar_0_2(TreeNode root1, TreeNode root2) {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        getLeafSeq_0_2(root1, list1);
        getLeafSeq_0_2(root2, list2); // ✅ fixed

        return list1.equals(list2); // ✅ simpler
    }

    private void getLeafSeq_0_2(TreeNode root, List<Integer> list) {
        if (root == null)
            return;

        // if leaf → add
        if (root.left == null && root.right == null) {
            list.add(root.val);
            return; // optional but slightly cleaner
        }

        getLeafSeq_0_2(root.left, list);
        getLeafSeq_0_2(root.right, list);
    }



    // V0-5
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean leafSimilar_0_5(TreeNode root1, TreeNode root2) {

        List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();

        l1 = this.getLeaf(root1, l1);
        l2 = this.getLeaf(root2, l2);

//        System.out.println("l1 = " + l1.toString());
//        System.out.println("l2 = " + l2.toString());

        return l1.equals(l2);
    }

    private List<Integer> getLeaf(TreeNode root, List<Integer> leaf){

        if( ! (root.left == null) ){
            getLeaf(root.left, leaf);
        }

        if ( root.left == null && root.right == null ){
            leaf.add(root.val);
        }

        if( ! (root.right == null) ){
            getLeaf(root.right, leaf);
        }

        return leaf;
    }

    public class TreeNode {

        // attr
        Integer val;
        TreeNode left;
        TreeNode right;

        // constructor
        TreeNode() {
        }

        TreeNode(Integer val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // V1
    // https://leetcode.com/problems/leaf-similar-trees/solutions/2891123/recurssion-with-java-0-ms/
    /**
     * time = O(1)
     * space = O(1)
     */
    public void leaf(TreeNode node,ArrayList<Integer> al)
    {
        if(node==null)
            return;
        if(node.left==null && node.right==null){
            al.add(node.val);
            return;
        }
        leaf(node.left,al);
        leaf(node.right,al);
    }
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean leafSimilar2(TreeNode root1, TreeNode root2)
    {
        ArrayList<Integer> l1=new ArrayList<>();
        ArrayList<Integer> l2=new ArrayList<>();
        leaf(root1,l1);
        leaf(root2,l2);
        return l1.equals(l2);
    }

    // V2
    // https://leetcode.com/problems/leaf-similar-trees/solutions/2889337/java-solution-using-stack/
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean isLeaf(TreeNode node){
        return (node.left==null && node.right==null);
    }
    /**
     * time = O(1)
     * space = O(1)
     */
    public void findLeafs(TreeNode node, Stack<Integer> stack){
        if(node==null) return;
        if(isLeaf(node)) stack.push(node.val);
        findLeafs(node.left,stack);
        findLeafs(node.right,stack);
    }
    /**
     * time = O(1)
     * space = O(1)
     */
    public void removeLeafs(TreeNode node, Stack<Integer> stack){
        if(node==null) return;
        if(isLeaf(node)){
            if(stack.peek()==-1) return;
            else if(stack.isEmpty() || stack.peek()!=node.val) stack.push(-1);
            else stack.pop();
        }
        removeLeafs(node.right,stack);
        removeLeafs(node.left,stack);
    }
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean leafSimilar3(TreeNode root1, TreeNode root2) {
        Stack<Integer> stack = new Stack<Integer>();
        findLeafs(root1,stack);
        removeLeafs(root2,stack);
        return stack.isEmpty();
    }


    

}
