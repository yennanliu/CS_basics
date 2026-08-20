package LeetCodeJava.Sort;

// https://leetcode.com/problems/count-nodes-that-are-great-enough/

import java.util.Collections;
import java.util.PriorityQueue;

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  2792. Count Nodes That Are Great Enough
 *  Hard
 *
 *  You are given a root to a binary tree and an integer k. A node of this tree is
 *  called great enough if the following hold:
 *    - Its subtree has at least k nodes.
 *    - Its value is greater than the value of at least k nodes in its subtree.
 *
 *  Return the number of nodes in this tree that are great enough.
 *
 *  The node u is in the subtree of the node v, if u == v or v is an ancestor of u.
 *
 *  Example 1:
 *    Input: root = [7,6,5,4,3,2,1], k = 2
 *    Output: 3
 *    Explanation: the nodes with values 7, 6 and 5 each have at least 2 strictly
 *                 smaller values in their subtree.
 *
 *  Example 2:
 *    Input: root = [1,2,3], k = 1
 *    Output: 0
 *
 *  Example 3:
 *    Input: root = [3,2,2], k = 2
 *    Output: 1
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 10^4].
 *    1 <= Node.val <= 10^4
 *    1 <= k <= 10
 */
public class CountNodesThatAreGreatEnough {

    private int res = 0;
    private int k;

    // V0
    // IDEA: POST-ORDER + BOUNDED MAX-HEAP OF THE k SMALLEST SUBTREE VALUES
    //       a node is "great enough" iff at least k values among its DESCENDANTS
    //       are strictly smaller than its own value (its own value never counts).
    //       to decide that we do NOT need the whole subtree - only its k smallest
    //       descendant values: if the k-th smallest is < node.val then all k of
    //       them are, and the node qualifies.
    //       so each subtree returns a MAX-heap capped at size k holding the k
    //       smallest values seen (max on top so the biggest is evicted). merge
    //       the child heaps (smaller into larger), test the node, then insert the
    //       node's own value for the parent to use. k <= 10, so each heap op is
    //       O(log k) ~ O(1).
    /**
     * time = O(n * k * log k)
     * space = O(n * k)
     */
    public int countGreatEnoughNodes(TreeNode root, int k) {
        this.res = 0;
        this.k = k;
        dfs(root);
        return this.res;
    }

    // returns a max-heap of the k smallest values in this subtree (node included)
    private PriorityQueue<Integer> dfs(TreeNode node) {
        if (node == null) {
            return new PriorityQueue<>(Collections.reverseOrder());
        }

        PriorityQueue<Integer> left = dfs(node.left);
        PriorityQueue<Integer> right = dfs(node.right);

        // merge the smaller heap into the bigger one
        PriorityQueue<Integer> cur = left;
        PriorityQueue<Integer> other = right;
        if (other.size() > cur.size()) {
            PriorityQueue<Integer> tmp = cur;
            cur = other;
            other = tmp;
        }
        for (Integer x : other) {
            push(cur, x);
        }

        // cur.peek() is the k-th smallest descendant value once cur is full
        if (cur.size() == this.k && cur.peek() < node.val) {
            this.res++;
        }

        push(cur, node.val);
        return cur;
    }

    private void push(PriorityQueue<Integer> heap, int val) {
        heap.offer(val);
        if (heap.size() > this.k) {
            heap.poll();                    // drop the biggest -> keep k smallest
        }
    }
}
