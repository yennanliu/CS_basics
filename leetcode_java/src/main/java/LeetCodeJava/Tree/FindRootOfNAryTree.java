package LeetCodeJava.Tree;

// https://leetcode.com/problems/find-root-of-n-ary-tree/

import java.util.ArrayList;
import java.util.List;

/**
 *  1506. Find Root of N-Ary Tree
 *  Medium
 *
 *  You are given all the nodes of an N-ary tree as an array of Node objects,
 *  where each node has a unique value.
 *
 *  Return the root of the N-ary tree.
 *
 *  The driver code constructs the tree from a serialized input, puts every Node
 *  object into an array in an ARBITRARY order, and passes that array to findRoot.
 *
 *  Example 1:
 *    Input: tree = [1,null,3,2,4,null,5,6]
 *    Output: [1,null,3,2,4,null,5,6]
 *    Explanation: findRoot should return Node(1) whatever the array order is.
 *
 *  Example 2:
 *    Input: tree = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
 *    Output: [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
 *
 *  Constraints:
 *    The total number of nodes is between [1, 5 * 10^4].
 *    Each node has a unique value.
 *
 *  Follow up: Could you solve this problem in constant space complexity with a
 *             linear time algorithm?
 */
public class FindRootOfNAryTree {

    // N-ary tree node (problem specific shape)
    public static class Node {
        public int val;
        public List<Node> children;

        public Node() {
            this.children = new ArrayList<>();
        }

        public Node(int val) {
            this.val = val;
            this.children = new ArrayList<>();
        }

        public Node(int val, List<Node> children) {
            this.val = val;
            this.children = children;
        }
    }

    // V0
    // IDEA: XOR TRICK (O(1) extra space)
    //       every node value appears exactly ONCE as "a node in the array", and
    //       every value EXCEPT the root's also appears exactly once as
    //       "someone's child". so XOR-ing all node values together with all
    //       child values cancels every non-root value (x ^ x == 0) and leaves
    //       the root value behind.
    //       NOTE: values are unique, which is what makes the cancellation exact.
    /**
     * time = O(N)
     * space = O(1)
     */
    public Node findRoot(List<Node> tree) {
        int x = 0;
        for (Node node : tree) {
            x ^= node.val;
            for (Node child : node.children) {
                x ^= child.val;
            }
        }
        for (Node node : tree) {
            if (node.val == x) {
                return node;
            }
        }
        return null;
    }
}
