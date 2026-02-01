package LeetCodeJava.DFS;

// https://leetcode.com/problems/vertical-order-traversal-of-a-binary-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 *  987. Vertical Order Traversal of a Binary Tree
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree, calculate the vertical order traversal of the binary tree.
 *
 * For each node at position (row, col), its left and right children will be at positions (row + 1, col - 1) and (row + 1, col + 1) respectively. The root of the tree is at (0, 0).
 *
 * The vertical order traversal of a binary tree is a list of top-to-bottom orderings for each column index starting from the leftmost column and ending on the rightmost column. There may be multiple nodes in the same row and same column. In such a case, sort these nodes by their values.
 *
 * Return the vertical order traversal of the binary tree.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,9,20,null,null,15,7]
 * Output: [[9],[3,15],[20],[7]]
 * Explanation:
 * Column -1: Only node 9 is in this column.
 * Column 0: Nodes 3 and 15 are in this column in that order from top to bottom.
 * Column 1: Only node 20 is in this column.
 * Column 2: Only node 7 is in this column.
 * Example 2:
 *
 *
 * Input: root = [1,2,3,4,5,6,7]
 * Output: [[4],[2],[1,5,6],[3],[7]]
 * Explanation:
 * Column -2: Only node 4 is in this column.
 * Column -1: Only node 2 is in this column.
 * Column 0: Nodes 1, 5, and 6 are in this column.
 *           1 is at the top, so it comes first.
 *           5 and 6 are at the same position (2, 0), so we order them by their value, 5 before 6.
 * Column 1: Only node 3 is in this column.
 * Column 2: Only node 7 is in this column.
 * Example 3:
 *
 *
 * Input: root = [1,2,3,4,6,5,7]
 * Output: [[4],[2],[1,5,6],[3],[7]]
 * Explanation:
 * This case is the exact same as example 2, but with nodes 5 and 6 swapped.
 * Note that the solution remains the same since 5 and 6 are in the same location and should be ordered by their values.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 1000].
 * 0 <= Node.val <= 1000
 *
 */
public class VerticalOrderTraversalOfABinaryTree {

    // V0
//    public List<List<Integer>> verticalTraversal(TreeNode root) {
//
//    }

    // V0-1
    // IDEA: DFS + col, row tracking (fixed by gpt)
    List<List<Integer>> verticalNodes = new ArrayList<>();
    /** NOTE !!!
     *
     *  nodeMap: { col: [node_val_1, node_val_2, ...] }
     */
    Map<Integer, List<int[]>> nodeMap = new HashMap<>(); // col -> list of [row, val]
    int minIdx = Integer.MAX_VALUE;
    int maxIdx = Integer.MIN_VALUE;

    public List<List<Integer>> verticalTraversal_0_1(TreeNode root) {
        if (root == null) {
            return verticalNodes;
        }

        dfs(root, 0, 0);

        // build verticalNodes from minIdx to maxIdx
        for (int i = minIdx; i <= maxIdx; i++) {
            List<int[]> list = nodeMap.get(i);
            if (list == null)
                continue;

            /** NOTE !!!!
             *
             *  we do CUSTOM sort the `map val (list)`
             *
             *  -> 1. row: small -> big
             *     2. `node val`: small -> big
             */
            // sort by row, then val
            Collections.sort(list, (a, b) -> {
                if (a[0] != b[0])
                    return a[0] - b[0]; // row ascending
                return a[1] - b[1]; // value ascending
            });

            // add to result
            List<Integer> colVals = new ArrayList<>();
            for (int[] pair : list) {
                colVals.add(pair[1]);
            }
            verticalNodes.add(colVals);
        }

        return verticalNodes;
    }

    /** NOTE !!!
     *
     *  we track BOTH col, row in helper func.
     *  -> as we need BOTH col, row info to build the result
     */
    private void dfs(TreeNode root, int row, int col) {
        if (root == null)
            return;

        minIdx = Math.min(minIdx, col);
        maxIdx = Math.max(maxIdx, col);

        List<int[]> list = nodeMap.getOrDefault(col, new ArrayList<>());
        list.add(new int[] { row, root.val });
        nodeMap.put(col, list);

        /** NOTE !!!
         *
         *  below param
         */
        dfs(root.left, row + 1, col - 1);
        dfs(root.right, row + 1, col + 1);
    }

    // V1
    // IDEA: DFS
    // https://leetcode.ca/2018-08-13-987-Vertical-Order-Traversal-of-a-Binary-Tree/
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public List<List<Integer>> verticalTraversal_1(TreeNode root) {
        List<int[]> list = new ArrayList<>();
        dfs(root, 0, 0, list);
        list.sort(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if (o1[0] != o2[0]) return Integer.compare(o1[0], o2[0]);
                if (o1[1] != o2[1]) return Integer.compare(o2[1], o1[1]);
                return Integer.compare(o1[2], o2[2]);
            }
        });
        List<List<Integer>> res = new ArrayList<>();
        int preX = 1;
        for (int[] cur : list) {
            if (preX != cur[0]) {
                res.add(new ArrayList<>());
                preX = cur[0];
            }
            res.get(res.size() - 1).add(cur[2]);
        }
        return res;
    }

    private void dfs(TreeNode root, int x, int y, List<int[]> list) {
        if (root == null) {
            return;
        }
        list.add(new int[] {x, y, root.val});
        dfs(root.left, x - 1, y - 1, list);
        dfs(root.right, x + 1, y - 1, list);
    }


    // V2
    // IDEA: DFS
    // https://leetcode.com/problems/vertical-order-traversal-of-a-binary-tree/solutions/7018292/beats-9953-easy-explanation-and-code-usi-4it9/
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public List<List<Integer>> verticalTraversal_2(TreeNode root) {
        List<int[]> nodes = new ArrayList<>();

        // Step 1: DFS to collect all (col, row, val)
        dfs_2(root, 0, 0, nodes);

        // Step 2: Sort by col, then row, then value
        nodes.sort((a, b) -> {
            if (a[0] != b[0])
                return Integer.compare(a[0], b[0]); // col
            if (a[1] != b[1])
                return Integer.compare(a[1], b[1]); // row
            return Integer.compare(a[2], b[2]); // value
        });

        List<List<Integer>> result = new ArrayList<>();
        int prevCol = Integer.MIN_VALUE;

        // Step 3: Group by column
        for (int[] node : nodes) {
            int col = node[0], val = node[2];
            if (col != prevCol) {
                result.add(new ArrayList<>());
                prevCol = col;
            }
            result.get(result.size() - 1).add(val);
        }

        return result;
    }

    // DFS helper to collect column, row, and value
    private void dfs_2(TreeNode node, int row, int col, List<int[]> nodes) {
        if (node == null)
            return;
        nodes.add(new int[] { col, row, node.val });
        dfs_2(node.left, row + 1, col - 1, nodes); // Left child
        dfs_2(node.right, row + 1, col + 1, nodes); // Right child
    }

    // V3
    // https://leetcode.com/problems/vertical-order-traversal-of-a-binary-tree/solutions/3124300/java-easy-line-by-line-explanation-level-l488/
    // We perform Level order trversal to get the output....
    // Create class tuple to store the node and coordinates.
    class Tuple {
        TreeNode node;
        int row;
        int col;

        // Constructor for tuple.
        public Tuple(TreeNode _node, int _row, int _col) {
            node = _node;
            row = _row;
            col = _col;
        }
    }

    public List<List<Integer>> verticalTraversal_3(TreeNode root) {

        // We need a treemap to store the vertical values(columns) and PriorityQueue to store the node values in increasing order.
        // (x,y,node)
        TreeMap<Integer, TreeMap<Integer, PriorityQueue<Integer>>> map = new TreeMap<>();

        // Create a queue for instering each node with respective row(x), column(y) values during iteration.
        // Initially coordinates of node are...(node,x->(0),y->(0))
        Queue<Tuple> q = new LinkedList<Tuple>();

        // Insert the tuple
        q.add(new Tuple(root, 0, 0));

        // Loop untill queue is empty.
        while (!q.isEmpty()) {

            // Pop the tuple from stack.
            Tuple tuple = q.poll();

            // Initialize the values inside the tuple.
            TreeNode node = tuple.node;
            int x = tuple.row;
            int y = tuple.col;

            // Insert the values into the treemap.

            // x - > vertical coordinate --> check example test cases.
            if (!map.containsKey(x))
                map.put(x, new TreeMap<>());

            // y - > horizontal coordinate --> check example test cases.
            if (!map.get(x).containsKey(y))
                map.get(x).put(y, new PriorityQueue<>());

            // Finally insert node value (!!!not node!!!) into map inside PriorityQueue.
            map.get(x).get(y).add(node.val);

            // Check is there exists a left or right node to the node present in the queue.
            // If present, then add it to the queue.
            if (node.left != null)
                q.add(new Tuple(node.left, x - 1, y + 1));
            if (node.right != null)
                q.add(new Tuple(node.right, x + 1, y + 1));
        }

        // Create a List Of List to store the list of node values.
        List<List<Integer>> list = new ArrayList<>();

        // Loop through the map and add the values.
        // x - > key, (y, nodes) -> values.
        for (TreeMap<Integer, PriorityQueue<Integer>> yn : map.values()) {
            // Create a sublist to store node values in each vertical.
            list.add(new ArrayList<>());

            // Now iterate in the PriorityQueue.
            for (PriorityQueue<Integer> nodes : yn.values()) {
                // Add node into the sublist from
                while (!nodes.isEmpty()) {
                    list.get(list.size() - 1).add(nodes.poll());
                }
            }
        }
        return list;
    }

    // V4
    // IDEA: BFS + TREE MAP
    // https://leetcode.com/problems/vertical-order-traversal-of-a-binary-tree/solutions/7210773/problem-no-987-vertical-order-traversal-1rm7y/
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public List<List<Integer>> verticalTraversal_4(TreeNode root) {
        // Map to store nodes by column -> row -> min-heap of node values
        // TreeMap ensures columns and rows are sorted automatically
        TreeMap<Integer, TreeMap<Integer, PriorityQueue<Integer>>> map = new TreeMap<>();

        // BFS queue to traverse the tree
        Queue<Tuple> q = new LinkedList<>();
        q.offer(new Tuple(root, 0, 0)); // start from root at row=0, col=0

        while (!q.isEmpty()) {
            Tuple tuple = q.poll();
            TreeNode node = tuple.node;
            int row = tuple.row; // row (top to bottom)
            int col = tuple.col; // column (left to right)

            // If column does not exist, create it
            map.putIfAbsent(col, new TreeMap<>());

            // If row in this column does not exist, create a min-heap
            map.get(col).putIfAbsent(row, new PriorityQueue<>());

            // Add the node value to the min-heap at (col, row)
            map.get(col).get(row).offer(node.val);

            // Add left and right children to the queue with updated coordinates
            if (node.left != null) {
                q.offer(new Tuple(node.left, row + 1, col - 1));
            }
            if (node.right != null) {
                q.offer(new Tuple(node.right, row + 1, col + 1));
            }
        }

        // Prepare the final output list
        List<List<Integer>> result = new ArrayList<>();

        // Traverse columns in order
        for (TreeMap<Integer, PriorityQueue<Integer>> rows : map.values()) {
            List<Integer> colList = new ArrayList<>();

            // Traverse rows in order (top to bottom)
            for (PriorityQueue<Integer> nodes : rows.values()) {
                // Extract nodes in ascending order if multiple nodes share the same (row, col)
                while (!nodes.isEmpty()) {
                    colList.add(nodes.poll());
                }
            }

            // Add the column list to the result
            result.add(colList);
        }

        return result;
    }



}
