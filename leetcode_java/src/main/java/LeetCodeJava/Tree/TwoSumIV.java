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
    // IDEA : BFS + HASHMAP + 2 SUM
    public boolean findTarget(TreeNode root, int k) {
        // edge
        if (root == null) {
            return false;
        }
        if (root.left == null && root.right == null) {
            return false;
        }
        // map : { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();
        // bfs
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode cur = q.poll();
            map.put(cur.val, 1); // node val should be unique
            // x + y = k
            // -> x = k - y
            // node val should be unique
            /** NOTE !!!! below condition
             *
             *  1. it's a valid BST, so  root > root.left and root < root.right
             *     -> ALL node val in BST MUST be UNIQUE
             *
             *  2. so `to avoid duplicated count`, we need to make sure
             *     we DON'T re-compute current node.
             *     e.g. k - cur.val != cur.val
             *
             *     example:
             *
             *      3
             *    2   null
             *
             *    k = 6
             *  
             *
             */
            if (map.containsKey(k - cur.val) && k - cur.val != cur.val) {
                return true;
            }
            if (cur.left != null) {
                q.add(cur.left);
            }
            if (cur.right != null) {
                q.add(cur.right);
            }
        }

        return false;
    }
    // V1
    // IDEA : SET + RECURSIVE
    // https://leetcode.com/problems/two-sum-iv-input-is-a-bst/editorial/
    public boolean findTarget_1(TreeNode root, int k) {
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
