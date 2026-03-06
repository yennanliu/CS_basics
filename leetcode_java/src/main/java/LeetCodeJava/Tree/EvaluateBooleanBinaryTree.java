package LeetCodeJava.Tree;

// https://leetcode.com/problems/evaluate-boolean-binary-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *  2331. Evaluate Boolean Binary Tree
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given the root of a full binary tree with the following properties:
 *
 * Leaf nodes have either the value 0 or 1, where 0 represents False and 1 represents True.
 * Non-leaf nodes have either the value 2 or 3, where 2 represents the boolean OR and 3 represents the boolean AND.
 * The evaluation of a node is as follows:
 *
 * If the node is a leaf node, the evaluation is the value of the node, i.e. True or False.
 * Otherwise, evaluate the node's two children and apply the boolean operation of its value with the children's evaluations.
 * Return the boolean result of evaluating the root node.
 *
 * A full binary tree is a binary tree where each node has either 0 or 2 children.
 *
 * A leaf node is a node that has zero children.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [2,1,3,null,null,0,1]
 * Output: true
 * Explanation: The above diagram illustrates the evaluation process.
 * The AND node evaluates to False AND True = False.
 * The OR node evaluates to True OR False = True.
 * The root node evaluates to True, so we return true.
 * Example 2:
 *
 * Input: root = [0]
 * Output: false
 * Explanation: The root node is a leaf node and it evaluates to false, so we return false.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 1000].
 * 0 <= Node.val <= 3
 * Every node has either 0 or 2 children.
 * Leaf nodes have a value of 0 or 1.
 * Non-leaf nodes have a value of 2 or 3.
 *
 *
 */
public class EvaluateBooleanBinaryTree {

    // V0
    // IDEA: HASHMAP + DFS
    public boolean evaluateTree(TreeNode root) {
        // edge
        Map<Integer, Boolean> leafMap = new HashMap<>();
        Map<Integer, String> nonLeafMap = new HashMap<>();

        leafMap.put(0, false);
        leafMap.put(1, true);

        nonLeafMap.put(2, "OR");
        nonLeafMap.put(3, "AND");

        if (root == null) {
            return true; // ????
        }

        return helper(root, leafMap, nonLeafMap);
    }

    private boolean helper(TreeNode root, Map<Integer, Boolean> leafMap, Map<Integer, String> nonLeafMap) {
        // edge
        if (root == null) {
            return true; // ???
        }

        // leaf node
        if (root.left == null && root.right == null) {
            return leafMap.get(root.val);
        }

        // non leaf node
        // dfs check its children nodes
        if (nonLeafMap.get(root.val).equals("AND")) {
            return helper(root.left, leafMap, nonLeafMap)
                    && helper(root.right, leafMap, nonLeafMap);
        }

        return helper(root.left, leafMap, nonLeafMap)
                || helper(root.right, leafMap, nonLeafMap);
    }


    // V0-1
    // IDEA: DFS (GEMINI)
    public boolean evaluateTree_0_1(TreeNode root) {
        // 1. Base Case: Leaf Node
        // Leaf nodes contain boolean values: 0 for False, 1 for True
        if (root.left == null && root.right == null) {
            return root.val == 1;
        }

        // 2. Recursive Case: Non-Leaf Node
        // Non-leaf nodes contain operators: 2 for OR, 3 for AND
        boolean leftVal = evaluateTree_0_1(root.left);
        boolean rightVal = evaluateTree_0_1(root.right);

        if (root.val == 2) {
            // Operator OR
            return leftVal || rightVal;
        } else {
            // Operator AND (root.val == 3)
            return leftVal && rightVal;
        }
    }


    // V0-2
    // IDEA: DFS (GPT)
    public boolean evaluateTree_0_2(TreeNode root) {
        if (root == null) {
            return false;
        }

        return helper_0_2(root);
    }

    private boolean helper_0_2(TreeNode root) {

        // leaf node
        if (root.left == null && root.right == null) {
            return root.val == 1;
        }

        // OR
        if (root.val == 2) {
            return helper_0_2(root.left) || helper_0_2(root.right);
        }

        // AND
        return helper_0_2(root.left) && helper_0_2(root.right);
    }


    // V1-1
    // IDEA: Recursion + DFS
    // https://leetcode.com/problems/evaluate-boolean-binary-tree/editorial/
    public boolean evaluateTree_1_1(TreeNode root) {
        // Handles the case for leaf nodes.
        if (root.left == null && root.right == null) {
            return root.val != 0;
        }

        // Store the evaluations for the left subtree and right subtree.
        boolean evaluateLeftSubtree = evaluateTree_1_1(root.left);
        boolean evaluateRightSubtree = evaluateTree_1_1(root.right);
        boolean evaluateRoot;
        if (root.val == 2) {
            evaluateRoot = evaluateLeftSubtree | evaluateRightSubtree;
        } else {
            evaluateRoot = evaluateLeftSubtree & evaluateRightSubtree;
        }

        return evaluateRoot;
    }


    // V1-2
    // IDEA:  Iterative approach (Depth First Search)
    // https://leetcode.com/problems/evaluate-boolean-binary-tree/editorial/
    public boolean evaluateTree_1_2(TreeNode root) {
        Stack<TreeNode> st = new Stack<>();
        st.push(root);
        HashMap<TreeNode, Boolean> evaluated = new HashMap<>();

        while (!st.isEmpty()) {
            TreeNode topNode = st.peek();

            // If the node is a leaf node, store its value in the evaluated hashmap
            // and continue
            if (topNode.left == null && topNode.right == null) {
                st.pop();
                evaluated.put(topNode, topNode.val == 1);
                continue;
            }

            // If both the children have already been evaluated, use their
            // values to evaluate the current node.
            if (evaluated.containsKey(topNode.left) &&
                    evaluated.containsKey(topNode.right)) {
                st.pop();
                if (topNode.val == 2) {
                    evaluated.put(topNode,
                            evaluated.get(topNode.left) || evaluated.get(topNode.right));
                } else {
                    evaluated.put(topNode,
                            evaluated.get(topNode.left) && evaluated.get(topNode.right));
                }
            } else {
                // If both the children are not leaf nodes, push the current
                // node along with its left and right child back into the stack.
                st.push(topNode.right);
                st.push(topNode.left);
            }
        }

        return evaluated.get(root);
    }



    // V2
    // https://leetcode.com/problems/evaluate-boolean-binary-tree/solutions/5162736/fastest-100-easy-clean-concise-dry-run-b-w0h2/
    public boolean helper(TreeNode root) {
        if (root.val == 0 || root.val == 1) {
            return root.val == 1;
        } else if (root.val == 2) {
            return helper(root.left) || helper(root.right);
        } else if (root.val == 3) {
            return helper(root.left) && helper(root.right);
        }

        return false;
    }

    public boolean evaluateTree_2(TreeNode root) {
        return helper(root);
    }


    // V3
    // https://leetcode.com/problems/evaluate-boolean-binary-tree/solutions/5162869/fully-visual-explanation-in-javacpython-cx7t8/
    public boolean evaluateTree_3(TreeNode root) {
        // Base condition, if it's a leaf node
        if (root.left == null && root.right == null) {
            if (root.val == 0)
                return false;
            return true;
        }
        // If it's not a leaf node, then go to left subtree
        boolean left = evaluateTree_3(root.left);
        // then go to right subtree
        boolean right = evaluateTree_3(root.right);
        // OR operation
        if (root.val == 2)
            return left || right;
        return left && right; // AND operation
    }




}
