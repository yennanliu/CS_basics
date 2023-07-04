package LeetCodeJava.DFS;

// https://leetcode.com/problems/path-sum-ii/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class PathSum_ii {

    // V0
    // IDEA : DFS + backtracking
    // NOTE !!! we have res attr, so can use this.res collect result
    private List<List<Integer>> res = new ArrayList<>();

    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {

        if (root == null){
            return this.res;
        }

        List<Integer> cur = new ArrayList<>();
        getPath(root, cur, targetSum);
        return this.res;
    }

     private void getPath(TreeNode root, List<Integer> cur, int targetSum){

        // return directly if root is null (not possible to go further, so just quit directly)
        if (root == null){
            return;
        }

        // NOTE !!! we add val to cache here instead of while calling method recursively ( e.g. getPath(root.left, cur, targetSum - root.val))
        //          -> so we just need to backtrack (cancel last operation) once (e.g. cur.remove(cur.size() - 1);)
        //          -> please check V0' for example with backtrack in recursively step
        cur.add(root.val);

        if (root.left == null && root.right == null && targetSum == root.val){
            this.res.add(new ArrayList<>(cur));
        }else{
            // NOTE !!! we update targetSum here (e.g. targetSum - root.val)
            getPath(root.left, cur, targetSum - root.val);
            getPath(root.right, cur, targetSum - root.val);
        }

         // NOTE !!! we do backtrack here (cancel previous adding to cur)
         cur.remove(cur.size() - 1);
    }

    // V0'
    // IDEA : DFS + backtracking V2
    private List<List<Integer>> res2 = new ArrayList<>();

    public List<List<Integer>> pathSum_2(TreeNode root, int targetSum) {

        if (root == null){
            return this.res2;
        }

        List<Integer> cur = new ArrayList<>();
        getPath_2(root, cur, targetSum);
        return this.res2;
    }

    private void getPath_2(TreeNode root, List<Integer> cur, int targetSum){

        if (root == null){
            return;
        }

        //cur.add(root.val);

        if (root.left == null && root.right == null && targetSum == root.val){
            // if condition meet, we add last val to cur
            cur.add(root.val);
            this.res2.add(new ArrayList<>(cur));
            // and of course, we need to cancel previous adding
            cur.remove(cur.size() - 1);
        }else{
            // NOTE !!! we add val to cur before recursive call, then we cancel it
            cur.add(root.val);
            getPath(root.left, cur, targetSum - root.val);
            // cancel last adding
            cur.remove(cur.size() - 1);
            // NOTE !!! we add val to cur before recursive call, then we cancel it
            cur.add(root.val);
            getPath(root.right, cur, targetSum - root.val);
            // cancel last adding
            cur.remove(cur.size() - 1);
        }

        //cur.remove(cur.size() - 1);
    }


    // V1
    // IDEA : DFS (RECURSION)
    // https://leetcode.com/problems/path-sum-ii/editorial/
    private void recurseTree(TreeNode node, int remainingSum, List<Integer> pathNodes, List<List<Integer>> pathsList) {

        if (node == null) {
            return;
        }

        // Add the current node to the path's list
        pathNodes.add(node.val);

        // Check if the current node is a leaf and also, if it
        // equals our remaining sum. If it does, we add the path to
        // our list of paths
        if (remainingSum == node.val && node.left == null && node.right == null) {
            pathsList.add(new ArrayList<>(pathNodes));
        } else {

            // Else, we will recurse on the left and the right children
            this.recurseTree(node.left, remainingSum - node.val, pathNodes, pathsList);
            this.recurseTree(node.right, remainingSum - node.val, pathNodes, pathsList);
        }

        // We need to pop the node once we are done processing ALL of it's
        // subtrees.
        pathNodes.remove(pathNodes.size() - 1);
    }

    public List<List<Integer>> pathSum_3(TreeNode root, int sum) {
        List<List<Integer>> pathsList = new ArrayList<List<Integer>>();
        List<Integer> pathNodes = new ArrayList<Integer>();
        this.recurseTree(root, sum, pathNodes, pathsList);
        return pathsList;
    }

}
