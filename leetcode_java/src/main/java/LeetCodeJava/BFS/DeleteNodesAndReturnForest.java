package LeetCodeJava.BFS;

// https://leetcode.com/problems/delete-nodes-and-return-forest/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class DeleteNodesAndReturnForest {

    // V0
    // TODO : implement below
//    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
//
//        return null;
//    }


    // V1
    // IDEA : BFS
    // https://leetcode.com/problems/delete-nodes-and-return-forest/solutions/5489867/explanations-no-one-will-give-you-2-detailed-approaches-extremely-simple-and-effective/
    /**
     *  NOTE !!!
     *
     *   1. return type is List<TreeNode>,
     *      so we just need to return "parent node" of each node after delete operation
     *
     *   2. main point :
     *       - handle to-delete node and its sub tree
     *       - collect node when node is/isn't in to-delete list
     *
     *   3. Why Nodes Aren’t Missed:
     * 	    - Root Node: If the root is not deleted, it is added to the forest. If it is deleted, any non-null children of the root will be added to the forest as new tree roots during the BFS traversal.
     *      - Other Nodes: During the BFS traversal, every node is processed. If a node is not in the toDeleteSet, it remains connected to its parent. If the node is in the toDeleteSet, its children (if any) are added to the forest.
     *
     */
    public List<TreeNode> delNodes_1(TreeNode root, int[] to_delete) {
        List<TreeNode> result = new ArrayList<>();
        if (root == null) return result;

        Set<Integer> toDeleteSet = new HashSet<>();
        for (int val : to_delete) {
            toDeleteSet.add(val);
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode curNode = queue.poll();

            if (curNode.left != null) {
                queue.offer(curNode.left);
                // NOTE !!! if toDeleteSet has sub left tree val
                // make sub left tree as null
                if (toDeleteSet.contains(curNode.left.val)) {
                    curNode.left = null;
                }
            }

            if (curNode.right != null) {
                queue.offer(curNode.right);
                // NOTE !!! if toDeleteSet has sub right tree val
                // make sub right tree as null
                if (toDeleteSet.contains(curNode.right.val)) {
                    curNode.right = null;
                }
            }

            // NOTE !!! then handle to delete element
            if (toDeleteSet.contains(curNode.val)) {
                if (curNode.left != null) {
                    result.add(curNode.left);
                }
                if (curNode.right != null) {
                    result.add(curNode.right);
                }
            } else if (result.isEmpty()) {
                result.add(curNode);
            }
        }

        return result;
    }

    // V2
    // IDEA : BFS Forest Formation
    // https://leetcode.com/problems/delete-nodes-and-return-forest/editorial/
    /**
     *   NOTE !!! (by GPT)
     *
     *    why we don't miss any nodes that are not in the toDeleteSet
     *    when forest.add(root); is outside of wheile loop
     *
     * 	1.	Handling the Root Node:
     * 	    •	The root node is special because it’s the starting point of the tree. If the root node is not in the toDeleteSet, it means the entire original tree (minus any deleted nodes) should be part of the resulting forest. Therefore, adding the root node to the forest outside the loop is correct.
     *
     *  2.	Processing All Other Nodes:
     * 	    •	Inside the while loop, the BFS traversal ensures that every node in the tree is visited.
     * 	    •	For each node that needs to be deleted (i.e., if it’s in the toDeleteSet), its non-null children are added to the forest if they exist. This ensures that any subtree roots formed by deleting nodes are included in the forest.
     * 	    •	For nodes that are not in the toDeleteSet, they remain part of the tree rooted at the original root, and since the root is checked outside the loop, the entire connected component of nodes is correctly handled.
     *
     * 	3.	Why Nodes Aren’t Missed:
     * 	    •	Root Node: If the root is not deleted, it is added to the forest. If it is deleted, any non-null children of the root will be added to the forest as new tree roots during the BFS traversal.
     * 	    •	Other Nodes: During the BFS traversal, every node is processed. If a node is not in the toDeleteSet, it remains connected to its parent. If the node is in the toDeleteSet, its children (if any) are added to the forest.
     *
     * Conclusion:
     *
     *  - The check outside the loop for the root node ensures that the
     *    root (and any connected nodes not in the toDeleteSet)
     *    is correctly added to the forest.
     *    Since BFS covers all nodes, no nodes are missed.
     *
     */
    public List<TreeNode> delNodes_2(TreeNode root, int[] to_delete) {
        if (root == null) {
            return new ArrayList<>();
        }

        Set<Integer> toDeleteSet = new HashSet<>();
        for (int val : to_delete) {
            toDeleteSet.add(val);
        }

        List<TreeNode> forest = new ArrayList<>();

        Queue<TreeNode> nodesQueue = new LinkedList<>();
        nodesQueue.add(root);

        while (!nodesQueue.isEmpty()) {
            TreeNode currentNode = nodesQueue.poll();

            if (currentNode.left != null) {
                nodesQueue.add(currentNode.left);
                // Disconnect the left child if it needs to be deleted
                if (toDeleteSet.contains(currentNode.left.val)) {
                    currentNode.left = null;
                }
            }

            if (currentNode.right != null) {
                nodesQueue.add(currentNode.right);
                // Disconnect the right child if it needs to be deleted
                if (toDeleteSet.contains(currentNode.right.val)) {
                    currentNode.right = null;
                }
            }

            // If the current node needs to be deleted, add its non-null children to the forest
            if (toDeleteSet.contains(currentNode.val)) {
                if (currentNode.left != null) {
                    forest.add(currentNode.left);
                }
                if (currentNode.right != null) {
                    forest.add(currentNode.right);
                }
            }
        }

        // Ensure the root is added to the forest if it is not to be deleted
        if (!toDeleteSet.contains(root.val)) {
            forest.add(root);
        }

        return forest;
    }

    // V2_1
    // IDEA :  Recursion (Postorder Traversal)
    // https://leetcode.com/problems/delete-nodes-and-return-forest/editorial/
    public List<TreeNode> delNodes_2_1(TreeNode root, int[] to_delete) {
        Set<Integer> toDeleteSet = new HashSet<>();
        for (int val : to_delete) {
            toDeleteSet.add(val);
        }
        List<TreeNode> forest = new ArrayList<>();

        root = processNode(root, toDeleteSet, forest);

        // If the root is not deleted, add it to the forest
        if (root != null) {
            forest.add(root);
        }

        return forest;
    }

    private TreeNode processNode(
            TreeNode node,
            Set<Integer> toDeleteSet,
            List<TreeNode> forest
    ) {
        if (node == null) {
            return null;
        }

        node.left = processNode(node.left, toDeleteSet, forest);
        node.right = processNode(node.right, toDeleteSet, forest);

        // Node Evaluation: Check if the current node needs to be deleted
        if (toDeleteSet.contains(node.val)) {
            // If the node has left or right children, add them to the forest
            if (node.left != null) {
                forest.add(node.left);
            }
            if (node.right != null) {
                forest.add(node.right);
            }
            // Return null to its parent to delete the current node
            return null;
        }

        return node;
    }

    // V2

}
