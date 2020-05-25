// V0

// V1
// http://buttercola.blogspot.com/2019/01/leetcode-536-construct-binary-tree-from.html
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
class Solution {
    private int start = 0;
    public TreeNode str2tree(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
 
        return str2treeHelper(s);
    }
 
    private TreeNode str2treeHelper(String s) {
        if (start >= s.length()) {
            return null;
        }
 
        // parse int
        //
        boolean neg = false;
        if (s.charAt(start) == '-') {
            neg = true;
            start++;
        }
        int num = 0;
        while (start < s.length() && Character.isDigit(s.charAt(start))) {
            int digit = Character.getNumericValue(s.charAt(start));
            num = num * 10 + digit;
            start++;
        }
 
        if (neg) {
            num = -num;
        }
 
        TreeNode root = new TreeNode(num);
 
        if (start >= s.length()) {
            return root;
        }
 
        // go to left child
        //
        if (start < s.length() && s.charAt(start) == '(') {
            start++;
            root.left = str2treeHelper(s);
        }
 
        if (start < s.length() && s.charAt(start) == ')') {
            start++;
            return root;
        }
 
        // go to the right child
        //
        if (start < s.length() && s.charAt(start) == '(') {
            start++;
            root.right = str2treeHelper(s);
        }
 
        if (start < s.length() && s.charAt(start) == ')') {
            start++;
            return root;
        }
 
        return root;
    }
}

// V2 
