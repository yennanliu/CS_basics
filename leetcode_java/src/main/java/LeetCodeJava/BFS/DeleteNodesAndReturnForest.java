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
