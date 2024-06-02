package LeetCodeJava.BFS;

// https://leetcode.com/problems/closest-leaf-in-a-binary-tree/description/
// https://leetcode.ca/all/742.html
// https://leetcode.ca/2017-12-11-742-Closest-Leaf-in-a-Binary-Tree/

//import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class ClosestLeafInaBinaryTree {

    // V0
    // IDEA : BFS + DFS
    // TODO : implement

    // V0''
    // IDEA : BFS + DFS (gpt)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Breadth-First-Search/closest-leaf-in-a-binary-tree.py#L251
    private TreeNode start;
    private Map<TreeNode, List<TreeNode>> graph = new HashMap<>();

    // Build graph via DFS
    private void buildGraph(TreeNode node, TreeNode parent, int k) {
        if (node == null) {
            return;
        }
        // If node.val == k, set start point
        if (node.val == k) {
            this.start = node;
        }
        if (parent != null) {
            graph.computeIfAbsent(node, x -> new ArrayList<>()).add(parent);
            graph.computeIfAbsent(parent, x -> new ArrayList<>()).add(node);
        }
        buildGraph(node.left, node, k);
        buildGraph(node.right, node, k);
    }

    // Search via BFS
    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int x) { val = x; }
    }

    public int findClosestLeaf(TreeNode root, int k) {
        this.start = null;
        this.graph = new HashMap<>();
        // DFS to create the graph
        buildGraph(root, null, k);
        Queue<TreeNode> q = new LinkedList<>();
        Set<TreeNode> visited = new HashSet<>();
        q.add(this.start);

        while (!q.isEmpty()) {
            TreeNode cur = q.poll();
            // Add cur to visited, not to visit this node again
            visited.add(cur);
            // If cur is a leaf node (no left and right children)
            if (cur.left == null && cur.right == null) {
                // Return the answer
                return cur.val;
            }
            // Go through all neighbors of current node and search again
            List<TreeNode> neighbors = graph.get(cur);
            if (neighbors != null) {
                for (TreeNode neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        q.add(neighbor);
                    }
                }
            }
        }
        return -1; // Should never reach here as there is always a leaf node in the tree
    }


    // V0''
    // IDEA : BFS (gpt)
    // TODO : validate below code
    static class TreeNode2 {
        int val;
        TreeNode2 left, right;
        TreeNode2(int x) { val = x; }
    }

    public int findClosestLeaf_0_1(TreeNode2 root, int k) {
        // Map to store the parent of each node
        Map<TreeNode2, TreeNode2> parentMap = new HashMap<>();
        TreeNode2 targetNode = findTargetNode2(root, k, parentMap);

        // Use BFS to find the nearest leaf node
        Queue<TreeNode2> queue = new LinkedList<>();
        Set<TreeNode2> visited = new HashSet<>();
        queue.add(targetNode);
        visited.add(targetNode);

        while (!queue.isEmpty()) {
            TreeNode2 current = queue.poll();

            // Check if it's a leaf node
            if (current.left == null && current.right == null) {
                return current.val;
            }

            // Add left child to the queue if it exists and not visited
            if (current.left != null && !visited.contains(current.left)) {
                visited.add(current.left);
                queue.add(current.left);
            }

            // Add right child to the queue if it exists and not visited
            if (current.right != null && !visited.contains(current.right)) {
                visited.add(current.right);
                queue.add(current.right);
            }

            // Add parent to the queue if it exists and not visited
            TreeNode2 parent = parentMap.get(current);
            if (parent != null && !visited.contains(parent)) {
                visited.add(parent);
                queue.add(parent);
            }
        }

        return -1; // Should never reach here as there is always a leaf node in the tree
    }

    private TreeNode2 findTargetNode2(TreeNode2 root, int k, Map<TreeNode2, TreeNode2> parentMap) {
        Queue<TreeNode2> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode2 current = queue.poll();
            if (current.val == k) {
                return current;
            }

            if (current.left != null) {
                parentMap.put(current.left, current);
                queue.add(current.left);
            }
            if (current.right != null) {
                parentMap.put(current.right, current);
                queue.add(current.right);
            }
        }

        return null; // Should never reach here as the target node always exists in the tree
    }


    // V1
    // IDEA : BFS + DFS
    private Map<TreeNode, List<TreeNode>> g;

    public int findClosestLeaf_1(TreeNode root, int k) {
        g = new HashMap<>();
        dfs(root, null);
        Deque<TreeNode> q = new LinkedList<>();
        for (Map.Entry<TreeNode, List<TreeNode>> entry : g.entrySet()) {
            if (entry.getKey() != null && entry.getKey().val == k) {
                q.offer(entry.getKey());
                break;
            }
        }
        Set<TreeNode> seen = new HashSet<>();
        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            seen.add(node);
            if (node != null) {
                if (node.left == null && node.right == null) {
                    return node.val;
                }
                for (TreeNode next : g.get(node)) {
                    if (!seen.contains(next)) {
                        q.offer(next);
                    }
                }
            }
        }
        return 0;
    }

    /**
     * - root: The current node in the tree being visited.
     *
     * - p: The parent node of the current node.
     *
     * - g: A Map<TreeNode, List<TreeNode>> that represents the graph.
     *      Each key is a node, and its value is a list of adjacent nodes.
     *
     *  Example :
     *
     *
     *    consider tree :
     *
     *      1
     *    / \
     *   2   3
     *
     *
     *   then the "g" will as below :
     *
     *
     *    {
     *     1 -> [2, 3],
     *     2 -> [1],
     *     3 -> [1]
     *    }
     *
     */
    private void dfs(TreeNode root, TreeNode p) {
        if (root != null) {
            g.computeIfAbsent(root, k -> new ArrayList<>()).add(p);
            g.computeIfAbsent(p, k -> new ArrayList<>()).add(root);
            dfs(root.left, root);
            dfs(root.right, root);
        }
    }

    // same as above
//    private void dfs(TreeNode root, TreeNode p) {
//        if (root != null) {
//            if (!g.containsKey(root)) {
//                g.put(root, new ArrayList<>());
//            }
//            if (!g.containsKey(p)) {
//                g.put(p, new ArrayList<>());
//            }
//            g.get(root).add(p);
//            g.get(p).add(root);
//            dfs(root.left, root);
//            dfs(root.right, root);
//        }
//    }


}
