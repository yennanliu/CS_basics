package LeetCodeJava.Tree;

// https://leetcode.com/problems/check-if-two-expression-trees-are-equivalent/

/**
 *  1612. Check If Two Expression Trees are Equivalent
 *  Medium
 *
 *  A binary expression tree is a kind of binary tree used to represent arithmetic
 *  expressions. Each node of a binary expression tree has either zero or two
 *  children. Leaf nodes (nodes with 0 children) correspond to operands
 *  (variables), and internal nodes (nodes with two children) correspond to the
 *  operators. In this problem, we only consider the '+' operator (i.e. addition).
 *
 *  You are given the roots of two binary expression trees, root1 and root2.
 *  Return true if the two binary expression trees are equivalent.
 *  Otherwise, return false.
 *
 *  Two binary expression trees are equivalent if they evaluate to the same value
 *  regardless of what the variables are set to.
 *
 *  Example 1:
 *    Input: root1 = [x], root2 = [x]
 *    Output: true
 *
 *  Example 2:
 *    Input: root1 = [+,a,+,null,null,b,c], root2 = [+,+,a,b,c]
 *    Output: true
 *    Explanation: a + (b + c) == (b + c) + a
 *
 *  Example 3:
 *    Input: root1 = [+,a,+,null,null,b,c], root2 = [+,+,a,b,d]
 *    Output: false
 *    Explanation: a + (b + c) != (b + d) + a
 *
 *  Constraints:
 *    The number of nodes in both trees are equal, odd and, in the range [1, 4999].
 *    Node.val is '+' or a lower-case English letter.
 *    It's guaranteed that the tree given is a valid binary expression tree.
 *
 *  Follow up: What will you change in your solution if the tree also supports
 *             the '-' operator (i.e. subtraction)?
 */
public class CheckIfTwoExpressionTreesAreEquivalent {

    // expression tree node (problem specific shape: char val)
    public static class Node {
        public char val;
        public Node left;
        public Node right;

        public Node() {
            this.val = ' ';
        }

        public Node(char val) {
            this.val = val;
        }

        public Node(char val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // V0
    // IDEA: COUNT LEAVES ('+' is commutative + associative, so only the
    //       MULTISET of operands matters)
    //       any tree evaluates to sum over variables of count(var) * value(var),
    //       so two trees are equivalent iff every variable appears the same
    //       number of times. trick: walk tree1 with +1 per leaf and tree2 with
    //       -1 per leaf into the SAME counter -> equivalent iff all entries 0.
    //       follow-up with '-': keep the counter but flip the sign when
    //       descending into the RIGHT child of a '-' node.
    /**
     * time = O(N1 + N2)
     * space = O(26) counters + O(H) recursion stack
     */
    public boolean checkEquivalence(Node root1, Node root2) {
        int[] cnt = new int[26];
        walk(root1, 1, cnt);
        walk(root2, -1, cnt);
        for (int c : cnt) {
            if (c != 0) {
                return false;
            }
        }
        return true;
    }

    private void walk(Node node, int sign, int[] cnt) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            cnt[node.val - 'a'] += sign;
            return;
        }
        walk(node.left, sign, cnt);
        walk(node.right, sign, cnt);
    }
}
