package LeetCodeJava.Tree;

// https://leetcode.com/problems/diameter-of-n-ary-tree/
// https://leetcode.ca/all/1522.html#google_vignette

import java.util.ArrayList;
import java.util.List;

/**
 * 1522. Diameter of N-Ary Tree
 * Given a root of an N-ary tree, you need to compute the length of the diameter of the tree.
 *
 * The diameter of an N-ary tree is the length of the longest path between any two nodes in the tree. This path may or may not pass through the root.
 *
 * (Nary-Tree input serialization is represented in their level order traversal, each group of children is separated by the null value.)
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: root = [1,null,3,2,4,null,5,6]
 * Output: 3
 * Explanation: Diameter is shown in red color.
 * Example 2:
 *
 *
 *
 * Input: root = [1,null,2,null,3,4,null,5,null,6]
 * Output: 4
 * Example 3:
 *
 *
 *
 * Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
 * Output: 7
 *
 *
 * Constraints:
 *
 * The depth of the n-ary tree is less than or equal to 1000.
 * The total number of nodes is between [0, 10^4].
 * Difficulty:
 *
 *
 */
public class DiameterOfNAryTree {

    class Node {
        public int val;
        public List<Node> children;


        public Node() {
            children = new ArrayList<Node>();
        }

        public Node(int _val) {
            val = _val;
            children = new ArrayList<Node>();
        }

        public Node(int _val,ArrayList<Node> _children) {
            val = _val;
            children = _children;
        }
    };

    // V0

    // V1
    // https://leetcode.ca/2020-01-30-1522-Diameter-of-N-Ary-Tree/
    private int ans;

    public int diameter(Node root) {
        ans = 0;
        dfs(root);
        return ans;
    }

    private int dfs(Node root) {
        if (root == null) {
            return 0;
        }
        int m1 = 0, m2 = 0;
        for (Node child : root.children) {
            int t = dfs(child);
            if (t > m1) {
                m2 = m1;
                m1 = t;
            } else if (t > m2) {
                m2 = t;
            }
        }
        ans = Math.max(ans, m1 + m2);
        return 1 + m1;
    }

    // V2

}
