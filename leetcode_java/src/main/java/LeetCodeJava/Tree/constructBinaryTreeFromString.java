//package LeetCodeJava.Tree;
//
//import javax.swing.tree.TreeNode;
//import java.util.Enumeration;
//
//public class constructBinaryTreeFromString {
//}
//
//class Solution {
//    private int start = 0;
//    public TreeNode str2tree(String s) {
//        if (s == null || s.length() == 0) {
//            return null;
//        }
//
//        return str2treeHelper(s);
//    }
//
//    private TreeNode str2treeHelper(String s) {
//        if (start >= s.length()) {
//            return null;
//        }
//
//        // parse int
//        //
//        boolean neg = false;
//        if (s.charAt(start) == '-') {
//            neg = true;
//            start++;
//        }
//        int num = 0;
//        while (start < s.length() && Character.isDigit(s.charAt(start))) {
//            int digit = Character.getNumericValue(s.charAt(start));
//            num = num * 10 + digit;
//            start++;
//        }
//
//        if (neg) {
//            num = -num;
//        }
//
//        TreeNode root = new TreeNode() {
//            public TreeNode getChildAt(int childIndex) {
//                return null;
//            }
//
//            public int getChildCount() {
//                return 0;
//            }
//
//            public TreeNode getParent() {
//                return null;
//            }
//
//            public int getIndex(TreeNode node) {
//                return 0;
//            }
//
//            public boolean getAllowsChildren() {
//                return false;
//            }
//
//            public boolean isLeaf() {
//                return false;
//            }
//
//            public Enumeration children() {
//                return null;
//            }
//        };
//
//        if (start >= s.length()) {
//            return root;
//        }
//
//        // go to left child
//        //
//        if (start < s.length() && s.charAt(start) == '(') {
//            start++;
//            root.right = str2treeHelper(s);
//        }
//
//        if (start < s.length() && s.charAt(start) == ')') {
//            start++;
//            return root;
//        }
//
//        // go to the right child
//        //
//        if (start < s.length() && s.charAt(start) == '(') {
//            start++;
//            root.right = str2treeHelper(s);
//        }
//
//        if (start < s.length() && s.charAt(start) == ')') {
//            start++;
//            return root;
//        }
//
//        return root;
//    }
//}
