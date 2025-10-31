package LeetCodeJava.Tree;

// https://leetcode.com/problems/two-sum-iv-input-is-a-bst/
/**
 * 653. Two Sum IV - Input is a BST
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary search tree and an integer k, return true if there exist two elements in the BST such that their sum is equal to k, or false otherwise.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [5,3,6,2,4,null,7], k = 9
 * Output: true
 * Example 2:
 *
 *
 * Input: root = [5,3,6,2,4,null,7], k = 28
 * Output: false
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 104].
 * -104 <= Node.val <= 104
 * root is guaranteed to be a valid binary search tree.
 * -105 <= k <= 105
 *
 *
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class TwoSumIV {

    // V0
//    public boolean findTarget(TreeNode root, int k) {
//
//        if (root == null) {
//            return false;
//        }
//
//        if (root.equals(null)){
//            return false;
//        }
//
//        if (root.left == null && root.right == null) {
//            return false;
//        }
//
//        //HashMap<Integer, Integer> dict = new HashMap<>();
//        HashSet<Integer> set = new HashSet<>();
//        Queue<TreeNode> queue = new LinkedList<>();
//        queue.add(root);
//
//        while (!queue.isEmpty()) {
//
//            while (!queue.isEmpty()) {
//
//                System.out.println("set = " + set.toString());
//
//                TreeNode cur = queue.poll();
//                if (set.contains(k - cur.val)) {
//                    return true;
//                }
//
//                set.add(cur.val);
//
//                if (cur.left != null) {
//                    queue.add(cur.left);
//                }
//
//                if (cur.right != null) {
//                    queue.add(cur.right);
//                }
//            }
//        }
//
//        return false;
//    }

    // V1
    // IDEA : SET + RECURSIVE
    // https://leetcode.com/problems/two-sum-iv-input-is-a-bst/editorial/
    public boolean findTarget(TreeNode root, int k) {
        Set< Integer > set = new HashSet();
        return find(root, k, set);
    }
    public boolean find(TreeNode root, int k, Set < Integer > set) {
        if (root == null)
            return false;
        if (set.contains(k - root.val))
            return true;
        set.add(root.val);
        return find(root.left, k, set) || find(root.right, k, set);
    }

    // V2
    // IDEA : BFS + HASHSET
    // https://leetcode.com/problems/two-sum-iv-input-is-a-bst/editorial/
    public boolean findTarget_2(TreeNode root, int k) {
        Set < Integer > set = new HashSet();
        Queue < TreeNode > queue = new LinkedList();
        queue.add(root);
        while (!queue.isEmpty()) {
            if (queue.peek() != null) {
                TreeNode node = queue.remove();
                if (set.contains(k - node.val))
                    return true;
                set.add(node.val);
                queue.add(node.right);
                queue.add(node.left);
            } else
                queue.remove();
        }
        return false;
    }

    // V3
    // IDEA : BST
    // https://leetcode.com/problems/two-sum-iv-input-is-a-bst/editorial/
    public boolean findTarget_3(TreeNode root, int k) {
        List < Integer > list = new ArrayList();
        inorder(root, list);
        int l = 0, r = list.size() - 1;
        while (l < r) {
            int sum = list.get(l) + list.get(r);
            if (sum == k)
                return true;
            if (sum < k)
                l++;
            else
                r--;
        }
        return false;
    }
    public void inorder(TreeNode root, List < Integer > list) {
        if (root == null)
            return;
        inorder(root.left, list);
        list.add(root.val);
        inorder(root.right, list);
    }



}
