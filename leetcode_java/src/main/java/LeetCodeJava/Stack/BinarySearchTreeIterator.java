package LeetCodeJava.Stack;

// https://leetcode.com/problems/binary-search-tree-iterator/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 173. Binary Search Tree Iterator
 * Solved
 * Medium
 * Topics
 * Companies
 * Implement the BSTIterator class that represents an iterator over the in-order traversal of a binary search tree (BST):
 *
 * BSTIterator(TreeNode root) Initializes an object of the BSTIterator class. The root of the BST is given as part of the constructor. The pointer should be initialized to a non-existent number smaller than any element in the BST.
 * boolean hasNext() Returns true if there exists a number in the traversal to the right of the pointer, otherwise returns false.
 * int next() Moves the pointer to the right, then returns the number at the pointer.
 * Notice that by initializing the pointer to a non-existent smallest number, the first call to next() will return the smallest element in the BST.
 *
 * You may assume that next() calls will always be valid. That is, there will be at least a next number in the in-order traversal when next() is called.
 *
 *
 *
 * Example 1:
 *
 *
 * Input
 * ["BSTIterator", "next", "next", "hasNext", "next", "hasNext", "next", "hasNext", "next", "hasNext"]
 * [[[7, 3, 15, null, null, 9, 20]], [], [], [], [], [], [], [], [], []]
 * Output
 * [null, 3, 7, true, 9, true, 15, true, 20, false]
 *
 * Explanation
 * BSTIterator bSTIterator = new BSTIterator([7, 3, 15, null, null, 9, 20]);
 * bSTIterator.next();    // return 3
 * bSTIterator.next();    // return 7
 * bSTIterator.hasNext(); // return True
 * bSTIterator.next();    // return 9
 * bSTIterator.hasNext(); // return True
 * bSTIterator.next();    // return 15
 * bSTIterator.hasNext(); // return True
 * bSTIterator.next();    // return 20
 * bSTIterator.hasNext(); // return False
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 105].
 * 0 <= Node.val <= 106
 * At most 105 calls will be made to hasNext, and next.
 *
 *
 * Follow up:
 *
 * Could you implement next() and hasNext() to run in average O(1) time and use O(h) memory, where h is the height of the tree?
 *
 */
/**
 * NOTE !!!
 *
 * -> in-order traversal retrieves the keys in ascending sorted order.
 */
public class BinarySearchTreeIterator {
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
    /**
     * Your BSTIterator object will be instantiated and called as such:
     * BSTIterator obj = new BSTIterator(root);
     * int param_1 = obj.next();
     * boolean param_2 = obj.hasNext();
     */
    // V0
    // IDEA : tree traversal + list + sorting
    class BSTIterator {

        // attr
        TreeNode treeNode;
        List<Integer> cache;

        public BSTIterator(TreeNode root) {
            this.treeNode = root;
            this.cache = new ArrayList<>();
            this.getValues(root);
            // ordering (ascending order)
            this.cache.sort(Integer::compareTo); // ???
        }

        public int next() {
            int tmp = this.cache.get(0);
            this.cache.remove(0);
            return tmp;
        }

        public boolean hasNext() {
            return !this.cache.isEmpty();
        }

        private void getValues(TreeNode root){
            if (root == null){
                return; // ?
            }
            // pre-order traversal (root -> left -> right)
            this.cache.add(root.val);

            if (root.left != null){
                this.getValues(root.left);
            }
            if (root.right != null){
                this.getValues(root.right);
            }
        }
    }

    // V0-1
    // IDEA: DFS + BST property (fixed by gpt)
    class BSTIterator_0_1 {
        // attributes
        private List<Integer> list = new ArrayList<>();
        private int idx = 0;

        public BSTIterator_0_1(TreeNode root) {
            getAllNodes(root); // fill list in inorder order
            System.out.println(">>> list (inorder) = " + list);
        }

        public int next() {
            return list.get(idx++);
        }

        public boolean hasNext() {
            System.out.println(">>> (hasNext) idx = " + idx + ", size = " + list.size());
            return idx < list.size();
        }

        // inorder traversal (left → root → right)
        private void getAllNodes(TreeNode root) {
            if (root == null) {
                return;
            }
            getAllNodes(root.left);
            list.add(root.val);
            getAllNodes(root.right);
        }
    }

    // V1
    // IDEA: STACK
    // https://leetcode.com/problems/binary-search-tree-iterator/solutions/52647/nice-comparison-and-short-solution-by-st-jcmg/
    // https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/bst.md
    public class BSTIterator_1 {

        private TreeNode visit;
        private Stack<TreeNode> stack;

        public BSTIterator_1(TreeNode root) {
            visit = root;
            stack = new Stack();
        }

        public boolean hasNext() {
            return visit != null || !stack.empty();
        }

        /**
         * NOTE !!! next() method logic
         */
        public int next() {
      /**
       * NOTE !!!
       *
       *  since BST property is as below:
       *
       *  - For each node
       *     - `left sub node < than current node`
       *     - `right sub node > than current node`
       *
       *  so, within the loop over `left node`, we keep finding
       *  the `smaller` node, and find the `relative smallest` node when the while loop ended
       *  -> so the `relative smallest` node is the final element we put into stack
       *  -> so it's also the 1st element pop from stack
       *  -> which is the `next small` node
       */
      while (visit != null) {
                stack.push(visit);
                visit = visit.left;
            }
            // Stack: LIFO (last in, first out)
            /**
             * NOTE !!! we pop the `next small` node from stack
             */
            TreeNode next = stack.pop();
            /**
             * NOTE !!!  and set the `next small` node's right node as next node
             */
            visit = next.right;
            /**
             * NOTE !!! we return the `next small` node's right node val
             *          because of the requirement
             *
             *         e.g. : int next() Moves the pointer to the right, then returns the number at the pointer.
             */
            return next.val;
        }
    }

    // V2
    // https://leetcode.com/problems/binary-search-tree-iterator/solutions/52525/my-solutions-in-3-languages-with-stack-b-ktax/
    // IDEA: STACK
    public class BSTIterator_2 {
        private Stack<TreeNode> stack = new Stack<TreeNode>();

        public BSTIterator_2(TreeNode root) {
            pushAll(root);
        }

        /** @return whether we have a next smallest number */
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        /** @return the next smallest number */
        public int next() {
            TreeNode tmpNode = stack.pop();
            pushAll(tmpNode.right);
            return tmpNode.val;
        }

        private void pushAll(TreeNode node) {
            for (; node != null; stack.push(node), node = node.left);
        }
    }

  // V3
  // https://leetcode.com/problems/binary-search-tree-iterator/solutions/1965120/easy-iterative-using-stack-space-oh-inst-0ov7/
  // IDEA : STACK
  /**
   * IDEA:
   *
   * 1. Brute force way - You can do in-order traversal and
   *    put each element into an ArrayList (additional space).
   * 	Then use that to check hasNext() or next() element.
   * 	However, that approach will use extra space of O(n).
   *
   * 2. This approach:
   * 	a) Here we will use our own Stack (basically do in-order traversal Iteratively,
   * 	instead of recursively). Reason, being we have more control here and no need to use extra space of O(n) for ArrayList to store all elements first.
   * 	b) We get space of O(h) only instead of O(n). [h: is height of tree]
   *
   */
  class BSTIterator_3 {
        Stack<TreeNode> stack;
        public BSTIterator_3(TreeNode root) {
            stack = new Stack<>();
            TreeNode node = root;
            updateStack(node);                                      // update stack
        }
        public int next() {
            TreeNode toRemove = stack.pop();
            updateStack(toRemove.right);                             // before return node, first update stack further
            return toRemove.val;
        }
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        // -------------------
        public void updateStack(TreeNode node){
            while(node != null){
                stack.add(node);
                node = node.left;
            }
        }
    }


}
