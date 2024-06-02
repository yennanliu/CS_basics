package LeetCodeJava.Tree;

// https://leetcode.com/problems/convert-bst-to-greater-tree/submissions/1274802003/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ConvertBstToGreaterTree {

    // V0
    // IDEA : DFS or BFS
    // TODO : implement
//    public TreeNode convertBST(TreeNode root) {
//
//    }

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/convert-bst-to-greater-tree/solutions/2823307/java-recursive-solution/
    static Set<Integer> set;
    static int sum;
    public TreeNode convertBST_1(TreeNode root) {
        set = new TreeSet<>();
        sum = 0;
        traverseBST(root);
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer i : set) {
            map.put(i, sum);
            sum -= i;
        }
        traverseBSTAndPutNewValues(root, map);
        return root;
    }

    private void traverseBSTAndPutNewValues(TreeNode node, Map<Integer, Integer> map) {
        if (node == null) {
            return;
        }
        if (map.containsKey(node.val)) {
            node.val = map.get(node.val);
        }
        traverseBSTAndPutNewValues(node.left, map);
        traverseBSTAndPutNewValues(node.right, map);
    }

    private void traverseBST(TreeNode node) {
        if (node == null) {
            return;
        }

        if (set.add(node.val)) {
            sum += node.val;
        }
        traverseBST(node.left);
        traverseBST(node.right);
    }

    // V2
    // https://leetcode.com/problems/convert-bst-to-greater-tree/solutions/4932808/java-dept-first-search-beats-100-0ms/
    int store=0;
    public void helper(TreeNode root,boolean flag){
        if(root==null){
            return ;
        }
        helper(root.right,false);
        if(!flag){
            int last=root.val;
            root.val+=store;
            store+=last;
            flag=true;
        }
        helper(root.left,false);

    }
    public TreeNode convertBST_2(TreeNode root) {
        helper(root,false);
        return root;
    }


}
