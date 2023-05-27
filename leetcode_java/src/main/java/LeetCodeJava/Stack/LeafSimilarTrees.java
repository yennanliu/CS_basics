package LeetCodeJava.Stack;

// https://leetcode.com/problems/leaf-similar-trees/

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
    public boolean leafSimilar(TreeNode root1, TreeNode root2) {

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
    public boolean isLeaf(TreeNode node){
        return (node.left==null && node.right==null);
    }
    public void findLeafs(TreeNode node, Stack<Integer> stack){
        if(node==null) return;
        if(isLeaf(node)) stack.push(node.val);
        findLeafs(node.left,stack);
        findLeafs(node.right,stack);
    }
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
    public boolean leafSimilar3(TreeNode root1, TreeNode root2) {
        Stack<Integer> stack = new Stack<Integer>();
        findLeafs(root1,stack);
        removeLeafs(root2,stack);
        return stack.isEmpty();
    }

}
