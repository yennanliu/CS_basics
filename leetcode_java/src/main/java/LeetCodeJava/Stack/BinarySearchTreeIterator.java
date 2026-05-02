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
    // IDEA: STACK + BST (`eager` traversal) (GPT)
    class BSTIterator {
        List<Integer> vals;
        int idx;

        public BSTIterator(TreeNode root) {
            this.vals = new ArrayList<>();
            helper(root);
            this.idx = 0;
        }

        public int next() {
            return this.vals.get(this.idx++);
        }

        public boolean hasNext() {
            return this.idx < this.vals.size();
        }

        // In-order traversal (sorted order for BST)
        /**
         *         // BST:
         *         // `IN ORDER` traverse
         *         // -> ascending order (small -> big)
         *
         */
        private void helper(TreeNode root) {
            if (root == null)
                return;

            helper(root.left);
            vals.add(root.val);
            helper(root.right);
        }
    }


    // V0-0-0-1
    // IDEA: STACK + BST (`lazy` traversal) (GPT)
    // NOTE: in this version, we `DON'T need to get ALL BST nodes `
    // at first place, which is more doable if big dataset
    /**  Dry run:
     *
     * # 🧠 What “lazy traversal” means here
     *
     * Instead of:
     *
     * * Precomputing **all nodes** (your list version, O(n) space)
     *
     * We:
     *
     * * Only prepare **just enough nodes** to answer the next query
     * * Expand the traversal **on demand**
     *
     * So:
     *
     * * `next()` does a *small amount of work each time*
     * * Total work is still O(n), but spread across calls
     *
     * ---
     *
     * # 🔑 Core idea
     *
     * In-order traversal = **left → node → right**
     *
     * To simulate that lazily:
     *
     * 1. Always go as far **left as possible**
     * 2. Use a **stack** to remember the path
     * 3. When visiting a node:
     *
     *    * Return it
     *    * Then explore its **right subtree**
     *
     * ---
     *
     * # 🔄 Key invariant (super important)
     *
     * > The stack always contains the path to the **next smallest node**,
     * with the top being the next answer.
     *
     * ---
     *
     * # 🌳 Dry run example
     *
     * Consider this BST:
     *
     * ```
     *         7
     *        / \
     *       3   15
     *          /  \
     *         9   20
     * ```
     *
     * ---
     *
     * ## Step 1: Initialization
     *
     * ```java
     * BSTIterator(root)
     * → pushLeft(7)
     * ```
     *
     * ### pushLeft(7):
     *
     * * push 7
     * * push 3
     * * stop (3.left == null)
     *
     * **Stack (top → bottom):**
     *
     * ```
     * 3
     * 7
     * ```
     *
     * ---
     *
     * ## Step 2: next()
     *
     * ```java
     * pop → 3
     * ```
     *
     * * 3 has no right child
     *
     * **Return:** `3`
     *
     * **Stack now:**
     *
     * ```
     * 7
     * ```
     *
     * ---
     *
     * ## Step 3: next()
     *
     * ```java
     * pop → 7
     * ```
     *
     * * 7 has right child → 15
     *   → call `pushLeft(15)`
     *
     * ### pushLeft(15):
     *
     * * push 15
     * * push 9
     * * stop
     *
     * **Stack:**
     *
     * ```
     * 9
     * 15
     * ```
     *
     * **Return:** `7`
     *
     * ---
     *
     * ## Step 4: next()
     *
     * ```java
     * pop → 9
     * ```
     *
     * * no right child
     *
     * **Stack:**
     *
     * ```
     * 15
     * ```
     *
     * **Return:** `9`
     *
     * ---
     *
     * ## Step 5: next()
     *
     * ```java
     * pop → 15
     * ```
     *
     * * has right → 20
     *   → pushLeft(20)
     *
     * **Stack:**
     *
     * ```
     * 20
     * ```
     *
     * **Return:** `15`
     *
     * ---
     *
     * ## Step 6: next()
     *
     * ```java
     * pop → 20
     * ```
     *
     * * no right
     *
     * **Stack:**
     * (empty)
     *
     * **Return:** `20`
     *
     * ---
     *
     * ## Final output order:
     *
     * ```
     * 3 → 7 → 9 → 15 → 20
     * ```
     *
     * ✔ Sorted (correct for BST)
     *
     * ---
     *
     * # ⚖️ Why this works
     *
     * ### pushLeft(node)
     *
     * This ensures:
     *
     * * The **smallest available node** is always on top
     *
     * ### next()
     *
     * * Pops the current smallest
     * * Then prepares the next candidates from the right subtree
     *
     * ---
     *
     * # ⏱ Complexity
     *
     * | Operation   | Time           | Space |
     * | ----------- | -------------- | ----- |
     * | `next()`    | amortized O(1) | O(h)  |
     * | `hasNext()` | O(1)           | O(h)  |
     *
     * * `h` = tree height
     * * Worst case (skewed tree): O(n)
     * * Balanced tree: O(log n)
     *
     * ---
     *
     * # ⚠️ Common pitfalls
     *
     * * Forgetting `pushLeft(node.right)` → skips nodes
     * * Trying to store values instead of nodes → loses structure
     * * Not understanding stack invariant → leads to bugs
     *
     * ---
     *
     * # 🧩 Intuition in one sentence
     *
     * > “Always keep the next smallest node on top of the stack,
     * and when you consume one, prepare the next by
     * exploring its right subtree.”
     *
     *
     */
    class BSTIterator_0_0_0_1 {
        private Stack<TreeNode> stack = new Stack<>();

        public BSTIterator_0_0_0_1(TreeNode root) {
            pushLeft(root);
        }

        private void pushLeft(TreeNode node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        public int next() {
            // Stack: FILO
            // NOTE !!! we pop cur element from stack first
            TreeNode node = stack.pop();
            if (node.right != null) {
                /** NOTE !!!
                 *
                 *  (if root.right != null)
                 *
                 *  we call the `pushLeft()`
                 *  with node.right
                 *  in `next()`
                 *
                 */
                pushLeft(node.right);
            }
            return node.val;
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }
    }


    // V0-0-x
    // IDEA : tree traversal + list + sorting
    class BSTIterator_0_0_x {

        // attr
        TreeNode treeNode;
        List<Integer> cache;

        /**
         * time = O(1)
         * space = O(N)
         */
        public BSTIterator_0_0_x(TreeNode root) {
            this.treeNode = root;
            this.cache = new ArrayList<>();
            this.getValues(root);
            // ordering (ascending order)
            this.cache.sort(Integer::compareTo); // ???
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int next() {
            int tmp = this.cache.get(0);
            this.cache.remove(0);
            return tmp;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
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

    // V0-0-1
    // IDEA: DFS + INORDER TRAVERSE + INDEX
    /**
     *  1. collect nodes as list via inorder traverse
     *  2. maintain an idx, so we know next(), and hasNext()
     *  3. do op per input accordingly
     */
    class BSTIterator_0_0_1 {

        // attr
        List<TreeNode> list;
        TreeNode root;
        int idx;

        /**
         * time = O(1)
         * space = O(N)
         */
        public BSTIterator_0_0_1(TreeNode root) {
            this.root = root;
            this.list = new ArrayList<>();
            this.idx = 0;

            buildInorder(root);
        }

        // helper func: inorder: left -> root -> right
        private void buildInorder(TreeNode root) {
            if (root == null) {
                return;
            }
            buildInorder(root.left);
            list.add(root);
            buildInorder(root.right);
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int next() {
            // edge
            if (this.idx >= this.list.size()) {
                System.out.println("out of boundary");
                return -1;
            }
            TreeNode cur = this.list.get(this.idx);
            this.idx += 1;
            return cur.val;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean hasNext() {
            return this.idx < this.list.size();
        }

    }

    // V0-1
    // IDEA: DFS + BST property (fixed by gpt)
    /**
     *  NOTE !!!
     *
     *   for BST, in-order traverse is
     *   small -> big array of all its node value
     *
     *   since for BST,
     *   left < root < right
     *
     */
    class BSTIterator_0_1 {
        // attributes
        private List<Integer> list = new ArrayList<>();
        private int idx = 0;

        /**
         * time = O(1)
         * space = O(N)
         */
        public BSTIterator_0_1(TreeNode root) {
            getAllNodes(root); // fill list in inorder order
            System.out.println(">>> list (inorder) = " + list);
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int next() {
            return list.get(idx++);
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean hasNext() {
            System.out.println(">>> (hasNext) idx = " + idx + ", size = " + list.size());
            return idx < list.size();
        }

        // inorder traversal (left → root → right)
        private void getAllNodes(TreeNode root) {
            if (root == null) {
                return;
            }
            /**
             *  NOTE !!!
             *
             *   we use `below INORDER traverse`
             *   to collect the node val from small -> big
             *
             *   so we DON'T need to sort collected val again
             */
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

        /**
         * time = O(1)
         * space = O(N)
         */
        public BSTIterator_1(TreeNode root) {
            visit = root;
            stack = new Stack();
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean hasNext() {
            return visit != null || !stack.empty();
        }

        /**
         * NOTE !!! next() method logic
         */
        /**
         * time = O(1)
         * space = O(1)
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

        /**
         * time = O(1)
         * space = O(N)
         */
        public BSTIterator_2(TreeNode root) {
            pushAll(root);
        }

        /** @return whether we have a next smallest number */
        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        /** @return the next smallest number */
        /**
         * time = O(1)
         * space = O(1)
         */
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
        /**
         * time = O(1)
         * space = O(N)
         */
        public BSTIterator_3(TreeNode root) {
            stack = new Stack<>();
            TreeNode node = root;
            updateStack(node);                                      // update stack
        }
        /**
         * time = O(1)
         * space = O(1)
         */
        public int next() {
            TreeNode toRemove = stack.pop();
            updateStack(toRemove.right);                             // before return node, first update stack further
            return toRemove.val;
        }
        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        // -------------------
        /**
         * time = O(1)
         * space = O(1)
         */
        public void updateStack(TreeNode node){
            while(node != null){
                stack.add(node);
                node = node.left;
            }
        }
    }






}
