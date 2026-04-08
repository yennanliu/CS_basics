package LeetCodeJava.BFS;

// https://leetcode.com/problems/delete-nodes-and-return-forest/description/
/**
 * 1110. Delete Nodes And Return Forest
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree, each node in the tree has a distinct value.
 *
 * After deleting all nodes with a value in to_delete, we are left with a forest (a disjoint union of trees).
 *
 * Return the roots of the trees in the remaining forest. You may return the result in any order.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,3,4,5,6,7], to_delete = [3,5]
 * Output: [[1,2,null,4],[6],[7]]
 * Example 2:
 *
 * Input: root = [1,2,4,null,3], to_delete = [3]
 * Output: [[1,2,4]]
 *
 *
 * Constraints:
 *
 * The number of nodes in the given tree is at most 1000.
 * Each node has a distinct value between 1 and 1000.
 * to_delete.length <= 1000
 * to_delete contains distinct values between 1 and 1000.
 *
 *
 */
import LeetCodeJava.DataStructure.TreeNode;
import java.util.*;


public class DeleteNodesAndReturnForest {

    // V0
//    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
//
//    }


    // V0-0-1
    // IDEA: POST order DFS (gpt)
    /** NOTE !!! compare V0-0-1 and V0-0-2 */
    List<TreeNode> res = new ArrayList<>();

    public List<TreeNode> delNodes_0_0_1(TreeNode root, int[] to_delete) {

        Set<Integer> set = new HashSet<>();
        for (int x : to_delete) {
            set.add(x);
        }

        /** NOTE !!! compare V0-0-1 and V0-0-2 */
        root = dfsDel(root, set);

        if (root != null) {
            res.add(root);
        }

        return res;
    }

    // NOTE !!!  `TreeNode` is the return type of help func
    private TreeNode dfsDel(TreeNode root, Set<Integer> set) {

        if (root == null) {
            return null;
        }

        root.left = dfsDel(root.left, set);
        root.right = dfsDel(root.right, set);

        if (set.contains(root.val)) {

            if (root.left != null) {
                res.add(root.left);
            }

            if (root.right != null) {
                res.add(root.right);
            }

            return null;
        }

        return root;
    }


    // V0-0-2
    // IDEA: Post order DFS (gemini)
    List<TreeNode> res_0_0_2 = new ArrayList<>();
    Set<Integer> deleteSet = new HashSet<>();

    public List<TreeNode> delNodes_0_0_2(TreeNode root, int[] to_delete) {
        res_0_0_2.clear(); // Reset for clean execution
        deleteSet.clear();
        for (int val : to_delete){
            deleteSet.add(val);
        }

        // 1. We call the helper.
        // 2. We pass "true" for isRoot because the original root
        //    is a root if it's not deleted.
        dfs(root, true);
        return res_0_0_2;
    }

    /** NOTE !!!
     *
     *   key param: `isRoot`
     *
     *    -> If it's a root and NOT deleted, add it to forest
     *
     */
    private TreeNode dfs(TreeNode node, boolean isRoot) {
        if (node == null)
            return null;

        /** NOTE !!!
         *
         *  how we determine `toDelete` val
         */
        // Check if current node needs to be deleted
        boolean toDelete = deleteSet.contains(node.val);

        // If it's a root and NOT deleted, add it to forest
        if (isRoot && !toDelete) {
            res_0_0_2.add(node);
        }

        // Post-Order: Process children.
        // If current node is deleted, its children become potential roots.
        /** NOTE !!!
         *
         *  we pass toDelete as `isRoot` param
         *  in recursion call (DFS)
         *
         *
         *  -> so if cur node is `to delete`,
         *     -> its sub node (left, right) becomes `root` potentially (isRoot=true)
         */
        node.left = dfs(node.left, toDelete);
        node.right = dfs(node.right, toDelete);

        // Return null to parent if this node is deleted, else return the node
        return toDelete ? null : node;
    }


    // V0-0-5
    // IDEA : BFS
    // TODO : implement by my way
    /**
     * time = O(N)
     * space = O(N)
     */
    public List<TreeNode> delNodes_0_0_5(TreeNode root, int[] to_delete) {
        if (root == null) {
            return new ArrayList<>();
        }

        Set<Integer> toDeleteSet = new HashSet<>();
        for (int val : to_delete) {
            toDeleteSet.add(val);
        }

        List<TreeNode> res = new ArrayList<>();

        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);

        // bfs
        while (!q.isEmpty()) {
            TreeNode currentNode = q.poll();

            /** NOTE !!!
             *
             *  if root.left is not null,
             *  we do below op
             *   1) append root.left to queue
             *   2) check if root.left is in to-delete, if true, then
             *      SET root.left as null
             *
             *   -> NOTE !!!
             *     we ONLY do 2) when root.left is NOT null
             *     since if root.left is null, then it makes no sense
             *     to compare it with to_delete
             */
            if (currentNode.left != null) {
                q.add(currentNode.left);
                // Disconnect the left child if it needs to be deleted
                if (toDeleteSet.contains(currentNode.left.val)) {
                    currentNode.left = null;
                }
            }

            /**
             *  same as above, we check and do op on root.right as well
             */
            if (currentNode.right != null) {
                q.add(currentNode.right);
                // Disconnect the right child if it needs to be deleted
                if (toDeleteSet.contains(currentNode.right.val)) {
                    currentNode.right = null;
                }
            }

            /**
             * NOTE !!!
             *
             *   if current node (e.g. root) need to be deleted
             *   -> again, check if its child node (root.left, root.right) is null
             *      if they are not null, append them to res
             *
             *     example :
             *
             *      Input: root = [1,2,3,4,5,6,7], to_delete = [3,5]
             *      Output: [[1,2,null,4],[6],[7]]
             *
             *      -> we need to append [6], [7] to result as well
             */
            // If the current node needs to be deleted, add its non-null children to the forest
            if (toDeleteSet.contains(currentNode.val)) {
                if (currentNode.left != null) {
                    res.add(currentNode.left);
                }
                if (currentNode.right != null) {
                    res.add(currentNode.right);
                }
            }
        }

        /**
         *  NOTE !!!
         *
         *   consider edge case : if the original root is NOT in the to-delete list
         *   -> need to append the root as well
         */
        // Ensure the root is added to the forest if it is not to be deleted
        if (!toDeleteSet.contains(root.val)) {
            res.add(root);
        }

        /**
         *  NOTE !!!
         *
         *  note that the return type of this problem is
         *  List<TreeNode>, meaning that
         *  we ONLY need to do "remove op", and append "root head"
         *  to result, that's it. we DON'T need to append every node
         *  to result, only the "root head"
         */
        return res;
    }


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
     *   3. Why Nodes Aren't Missed:
     * 	    - Root Node: If the root is not deleted, it is added to the forest. If it is deleted, any non-null children of the root will be added to the forest as new tree roots during the BFS traversal.
     *      - Other Nodes: During the BFS traversal, every node is processed. If a node is not in the toDeleteSet, it remains connected to its parent. If the node is in the toDeleteSet, its children (if any) are added to the forest.
     *
     */
    /**
     * time = O(N)
     * space = O(N)
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
     * 	    •	The root node is special because it's the starting point of the tree. If the root node is not in the toDeleteSet, it means the entire original tree (minus any deleted nodes) should be part of the resulting forest. Therefore, adding the root node to the forest outside the loop is correct.
     *
     *  2.	Processing All Other Nodes:
     * 	    •	Inside the while loop, the BFS traversal ensures that every node in the tree is visited.
     * 	    •	For each node that needs to be deleted (i.e., if it's in the toDeleteSet), its non-null children are added to the forest if they exist. This ensures that any subtree roots formed by deleting nodes are included in the forest.
     * 	    •	For nodes that are not in the toDeleteSet, they remain part of the tree rooted at the original root, and since the root is checked outside the loop, the entire connected component of nodes is correctly handled.
     *
     * 	3.	Why Nodes Aren't Missed:
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
    /**
     * time = O(N)
     * space = O(N)
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
