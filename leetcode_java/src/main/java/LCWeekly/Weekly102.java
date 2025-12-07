package LCWeekly;

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 * 本周題目
 * LeetCode biweekly contest 102
 * https://leetcode.com/contest/biweekly-contest-102/
 * 中文題目
 * https://leetcode.cn/contest/biweekly-contest-102/
 */
public class Weekly102 {

    // Q1
    // LC 2639
    // https://leetcode.com/problems/find-the-width-of-columns-of-a-grid/
    // 10.28 - 39 am
    /**
     *
     *
     *  -  The width of a column is the maximum length of its integers.
     *      - For example, if grid = [[-10], [3], [12]],
     *        the width of the only column is 3 since -10 is of length 3.
     *
     *
     */
//    public int[] findColumnWidth(int[][] grid) {
//        //edge
//        if(grid == null || grid.length == 0){
//            return null;
//        }
//        int[] res = new int[grid.length];
//       // List<int[]> list = new ArrayList<>();
//        for(int i = 0; i < grid.length; i++){
//            int[] g = grid[i];
//            int val = 0;
//            for(int x: g){
//                System.out.println(">>> i = " + i + ", g = " + g + ", x = " + x);
//                val = Math.max(val, String.valueOf(x).length());
//            }
//            res[i] = val;
//        }
//
//        return res;
//    }

    // V1
    // fix by GPT
    public int[] findColumnWidth(int[][] grid) {
        // edge case
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return new int[0];
        }

        int rows = grid.length;
        int cols = grid[0].length;
        int[] res = new int[cols];

        // for each column
        for (int j = 0; j < cols; j++) {
            int maxLen = 0;
            for (int i = 0; i < rows; i++) {
                maxLen = Math.max(maxLen, String.valueOf(grid[i][j]).length());
            }
            res[j] = maxLen;
        }

        return res;
    }


    // Q2
    // LC 2640
    // https://leetcode.com/problems/find-the-score-of-all-prefixes-of-an-array/description/
    // 10.42 - 11.00 am
    /**
     *
     *  -> return an array ans of length n where ans[i]
     *     is the score of the prefix nums[0..i].
     *
     *
     *   -  conversion array conver of an array arr as follows:
     *       - conver[i] = arr[i] + max(arr[0..i])
     *         where max(arr[0..i]) is the maximum value of
     *         arr[j] over 0 <= j <= i.
     *
     *    - We also define the score of an array arr as the sum of
     *       the values of the conversion array of arr.
     *
     *
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) PREFIX SUM ARRAY + PQ
     *
     *     -> get the `conversion array` first,
     *        then sum till i, and collect ans the final nas
     *
     *
     *
     *  ex 1) Input: nums = [2,3,7,5,10]
     *        Output: [4,10,24,36,56]
     *
     *   ->  nums = [2,3,7,5,10], PQ = [2], c_a = [2]
     *               x
     *
     *      nums = [2,3,7,5,10], PQ = [3], c_a = [4, 6]
     *                x
     *
     *      nums = [2,3,7,5,10], PQ = [7], c_a = [4, 6, 14]
     *                  x
     *
     *      nums = [2,3,7,5,10], PQ = [7], c_a = [4, 6, 14, 12]
     *                    x
     *
     *      nums = [2,3,7,5,10], PQ = [10], c_a = [4, 6, 14, 12, 20]
     *                      x
     *
     */
//    public long[] findPrefixScore(int[] nums) {
//        // edge
//        if(nums.length <= 1){
//            long[] ans = new long[nums.length];
//            if(nums.length == 1){
//                ans[0] = nums[0] * 2L;
//            }
//            return ans;
//        }
//
//        long[] converArr = new long[nums.length]; // /???
//
//        // big PQ
//        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                int diff = o2 - o1;
//                return diff;
//            }
//        });
//        for(int i = 0; i < nums.length; i++){
//            int val = nums[i];
//            // case 1) PQ is empty or PQ 's top val < cur val
//            if(pq.isEmpty() || pq.peek() < val){
//                pq.add(val);
//                val = pq.peek(); // ???
//            }
//            // case 2) PQ 's top val > cur val
//            // we keep PQ unchaned, and get the val from PQ peek
//            else{
//                val = pq.peek();
//            }
//            //converArr[i] = converArr[i] + val;
//            // ???
//            converArr[i] = nums[i] + val;
//        }
//
//        long ans[] =  new long[nums.length];
//        // acc sum
//        int accSum = 0;
//        for(int i = 0; i < converArr.length; i++){
//            accSum += converArr[i];
//            ans[i] = accSum;
//        }
//
//        System.out.println(">>> converArr =  " + Arrays.toString(converArr) +
//                ", ans = " + Arrays.toString(ans));
//
//        return ans;
//    }


    // V1
    // FIX BY gpt
    public long[] findPrefixScore(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return new long[0];
        }

        long[] converArr = new long[nums.length];
        long[] ans = new long[nums.length];

        // Keep track of running maximum
        int maxSoFar = Integer.MIN_VALUE;
        long accSum = 0L;

        for (int i = 0; i < nums.length; i++) {
            maxSoFar = Math.max(maxSoFar, nums[i]);
            converArr[i] = (long) nums[i] + maxSoFar;
            accSum += converArr[i];
            ans[i] = accSum;
        }

        // Debug print (optional)
        System.out.println(">>> converArr = " + Arrays.toString(converArr) +
                ", ans = " + Arrays.toString(ans));

        return ans;
    }


    // V2
    // (fix by gemini)
    public long[] findPrefixScore_2(int[] nums) {
        // Edge case: Handle null or empty array
        if (nums == null || nums.length == 0) {
            return new long[0];
        }

        int n = nums.length;
        long[] ans = new long[n]; // The final prefix score array

        // Variable to track the running maximum value encountered so far (max(nums[0...i]))
        long runningMax = 0;

        // --- FIX 2: Use a 'long' for the accumulator to prevent overflow ---
        long currentPrefixScoreSum = 0;

        // Single pass to calculate the runningMax, conver value, and final prefix sum
        for (int i = 0; i < n; i++) {

            // Cast the current number to long for safe calculations
            long currentNum = (long) nums[i];

            // --- FIX 1: Correctly update the running maximum ---
            // The max of the prefix [0...i] is the max of the previous max and the current number.
            runningMax = Math.max(runningMax, currentNum);

            // Calculate conver[i] = nums[i] + max(nums[0...i])
            long conver_i = currentNum + runningMax;

            // Update the accumulated score sum
            currentPrefixScoreSum += conver_i;

            // Store the result in the final array
            ans[i] = currentPrefixScoreSum;
        }

        // Remove the System.out.println() for production code submission.
        // System.out.println(">>> ans = " + Arrays.toString(ans));

        return ans;
    }



    // Q3
    // LC 2641
    // https://leetcode.com/problems/cousins-in-binary-tree-ii/
    // 6.40 - 6.50 am
    /**
     *
     *   -> Return the root of the modified tree.
     *
     *
     *   - Replace the value of each node in the tree
     *      with the `sum of all its cousins' values.`
     *
     *   - Two nodes of a binary tree are ` cousins` if
     *      they have the same depth with DIFFERENT parents.
     *
     *   - Depth of a node is the number of edges in
     *     the path from the root node to it.
     *
     *
     *
     *   IDEA 1) DFS + get patent - depth (hashmap)
     *           check if cousins exists
     *           update res
     *
     *       patent - depth (hashmap):
     *         { depth : [parent1-node1, parent2-node2, ....] }  ???
     *
     *         { depth-1 : [node1, node2,...],  depth-1 : [node4, node5,...]}  ???
     *         { parent1 : [node1, node2], parent2: [] , ....}
     *
     *
     */
//   // TreeNode resultNode;
//    // { depth-1 : [node1, node2,...],  depth-1 : [node4, node5,...]}  ???
//    Map<Integer, List<TreeNode>> depthNodeMap;
//    // { parent1 : [node1, node2], parent2: [] , ....}
//    Map<TreeNode, List<TreeNode>> parentNodeMap;
//    public TreeNode replaceValueInTree(TreeNode root) {
//        // edge
//        if(root == null){
//            return null;
//        }
//        if(root.left == null && root.right == null){
//            return null;
//        }
//
//        buildDepthNodeMap(root, 0);
//        TreeNode resultNode = buildCousinsTree(null, root, 0);
//
//        System.out.println(">>> depthNodeMap = " + depthNodeMap);
//        System.out.println(">>> resultNode = " + resultNode);
//
//        //return buildCousinsTree(null, root, 0); // ???
//        return resultNode;
//    }
//
//    private TreeNode buildCousinsTree(TreeNode parent, TreeNode root, Integer depth){
//        // ???
//        if(root == null){
//            return null;
//        }
//        //TreeNode resultNode = new TreeNode(); // ????
//        TreeNode resultNode = root; // ?????
//        resultNode.val = 0; // ??? reset node val
//
//        // get node with same depth
//        for(TreeNode node: depthNodeMap.get(depth)){
//            // check if they are `cousin`.
//            // e.g. 1) same depth 2) DIFFERENT parent
//            if(!parentNodeMap.containsKey(root)){
//                // if is cousin, acc sum to resultNode
//                resultNode.val += node.val;
//            }
//        }
//
//        resultNode.left = buildCousinsTree(resultNode, root.left, depth + 1);
//        resultNode.right = buildCousinsTree(resultNode, root.right,  depth + 1);
//
//        return resultNode; // ???
//    }
//
//    private void buildDepthNodeMap(TreeNode root, Integer depth){
//        if(root == null){
//            return;
//        }
//        // DFS: pre-order traversal ????
//        List<TreeNode> list1 = new ArrayList<>();
//        if(depthNodeMap.containsKey(depth)){
//            list1 = depthNodeMap.get(depth);
//        }
//        list1.add(root);
//        depthNodeMap.put(depth, list1);
//
//        // ???
//        buildDepthNodeMap(root.left, depth + 1);
//        buildDepthNodeMap(root.right, depth + 1);
//    }

    // V1: BFS (gpt)
    public TreeNode replaceValueInTree_1(TreeNode root) {
        if (root == null)
            return null;

        // root has no cousins
        root.val = 0;

        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            int size = q.size();

            // Collect children of this level and compute total sum of their original values
            List<TreeNode> children = new ArrayList<>();
            Map<TreeNode, Integer> parentChildrenSum = new HashMap<>();
            int nextLevelSum = 0;

            // iterate current level
            for (int i = 0; i < size; i++) {
                TreeNode parent = q.poll();
                int siblingSum = 0;

                if (parent.left != null) {
                    siblingSum += parent.left.val;
                    children.add(parent.left);
                    nextLevelSum += parent.left.val;
                }
                if (parent.right != null) {
                    siblingSum += parent.right.val;
                    children.add(parent.right);
                    nextLevelSum += parent.right.val;
                }

                parentChildrenSum.put(parent, siblingSum);
            }

            // assign new values for children (based on nextLevelSum and siblingSum for their parent)
            // note: we must use parentChildrenSum (keyed by parent) to exclude sibling contributions
            for (Map.Entry<TreeNode, Integer> entry : parentChildrenSum.entrySet()) {
                TreeNode parent = entry.getKey();
                int siblingSum = entry.getValue();
                if (parent.left != null) {
                    parent.left.val = nextLevelSum - siblingSum;
                }
                if (parent.right != null) {
                    parent.right.val = nextLevelSum - siblingSum;
                }
            }

            // push children into queue for next iteration
            for (TreeNode child : children) {
                q.offer(child);
            }
        }

        return root;
    }



    // V2
    // BFS (gemini)
    // Map to store the total sum of node values for each depth.
    private Map<Integer, Integer> depthSum = new HashMap<>();

    public TreeNode replaceValueInTree_2(TreeNode root) {
        if (root == null) {
            return null;
        }

        // --- Pass 1: Calculate the total sum for each depth using BFS ---
        calculateDepthSums(root);

        // --- Pass 2: Traverse and Replace Values using BFS ---

        // The root node (depth 0) is a special case: its value is always 0.
        root.val = 0;

        // Use a queue to store the PARENT nodes of the level being processed.
        // We will process the children of these nodes.
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        int depth = 1;

        // BFS loop continues as long as there are nodes to process
        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            // Get the total sum for the current depth being modified (depth 1, 2, 3, etc.)
            // We only need to check the depth if it exists in the map (i.e., the level is not empty).
            if (!depthSum.containsKey(depth)) {
                break;
            }
            int totalLevelSum = depthSum.get(depth);

            // Iterate through all parent nodes from the previous level
            for (int i = 0; i < levelSize; i++) {
                TreeNode parent = queue.poll();

                // 1. Calculate the sum of the siblings (children of the current parent)
                int childrenSum = 0;
                if (parent.left != null) {
                    childrenSum += parent.left.val;
                }
                if (parent.right != null) {
                    childrenSum += parent.right.val;
                }

                // 2. Apply the replacement logic to the left child
                if (parent.left != null) {
                    // New Value = (Total Sum at Depth) - (Sibling Sum)
                    // The sibling sum here is the combined value of the left and right child.
                    parent.left.val = totalLevelSum - childrenSum;

                    // Add the modified node to the queue for the next iteration (next depth's parents)
                    queue.add(parent.left);
                }

                // 3. Apply the replacement logic to the right child
                if (parent.right != null) {
                    parent.right.val = totalLevelSum - childrenSum;

                    // Add the modified node to the queue for the next iteration
                    queue.add(parent.right);
                }
            }
            depth++; // Move to the next depth
        }

        return root;
    }

    /**
     * Pass 1 Helper: Calculates the total sum of all nodes at each depth using BFS.
     */
    private void calculateDepthSums(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int depth = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            int currentLevelSum = 0;

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevelSum += node.val;

                if (node.left != null)
                    queue.add(node.left);
                if (node.right != null)
                    queue.add(node.right);
            }
            // Store the total sum for the current depth
            depthSum.put(depth, currentLevelSum);
            depth++;
        }
    }

    // V3
    // DFS (gpt)
    Map<Integer, Integer> levelSum = new HashMap<>();

    public TreeNode replaceValueInTree_3(TreeNode root) {
        if (root == null)
            return null;

        // 1️⃣ collect sums per depth
        collectLevelSum(root, 0);

        // 2️⃣ replace values based on cousin logic
        root.val = 0;
        dfsReplace(root, 0);

        return root;
    }

    private void collectLevelSum(TreeNode node, int depth) {
        if (node == null)
            return;
        levelSum.put(depth, levelSum.getOrDefault(depth, 0) + node.val);
        collectLevelSum(node.left, depth + 1);
        collectLevelSum(node.right, depth + 1);
    }

    private void dfsReplace(TreeNode node, int depth) {
        if (node == null)
            return;

        int nextDepth = depth + 1;
        int siblingSum = 0;
        if (node.left != null)
            siblingSum += node.left.val;
        if (node.right != null)
            siblingSum += node.right.val;

        if (node.left != null) {
            node.left.val = levelSum.getOrDefault(nextDepth, 0) - siblingSum;
        }
        if (node.right != null) {
            node.right.val = levelSum.getOrDefault(nextDepth, 0) - siblingSum;
        }

        dfsReplace(node.left, nextDepth);
        dfsReplace(node.right, nextDepth);
    }

    // Q4
    // LC 2642
    // https://leetcode.com/problems/design-graph-with-shortest-path-calculator/
    // 8.43 - 53 am
    /**
     *
     *   - implement the `Graph` class that
     *      -  Graph(int n, int[][] edges) initializes the object with n nodes and the given edges.
     *      -  addEdge(int[] edge):
     *          - adds an edge to the list of edges
     *      - shortestPath:
     *          -  return minimum cost of a path from node1 to node2. If no path exists, return -1
     *          -  The cost of a path is the sum of the costs of the edges in the path.
     *
     *
     * -  edges[i] = [fromi, toi, edgeCosti]
     *     -> an edge from fromi to toi with the cost edgeCosti.
     *     -> `fromi` to `toi` and cost `edgeCosti`
     *
     *
     *
     *   IDEA 1) Dijkstra algo ???
     *
     *   IDEA 2)  BFS + weight at path ????
     *
     *
     */
    // IDEA 1) Dijkstra algo ???
//    class Graph {
//
//        // attr
//        // { node: [neighbor_1, neighbor_2, ... }
//        //Map<Integer, List<Integer>> map;
//
//        // { node: [neighbor_1, cost_1], [neighbor_2, cost_2], ... } ????
//        Map<Integer, List<Integer[]>> map;
//
//        // weight ???
//        Integer[] weights;
//
//        // PQ: small PQ, record the `cheapest` cost from node
//        PriorityQueue<Integer> pq;
//
//        public Graph(int n, int[][] edges) {
//            this.map = new HashMap<>();
//            // ?? init as 0 ???
//            this.weights = new Integer[n]; // /??
//
//            this.pq = new PriorityQueue<>();
//        }
//
//        public void addEdge(int[] edge) {
//            int from = edge[0];
//            int to = edge[1];
//            int cost = edge[2];
//
//            // update map
//            List<Integer[]> list1 = new ArrayList<>();
//            if(this.map.containsKey(from)){
//                list1 = this.map.get(from);
//            }
//            Integer[] arr = new Integer[2];
//            arr[0] = to;
//            arr[1] = cost;
//            this.map.put(from, list1);
//
//            // update `weight` ???
//            this.weights[from] = cost; // ????
//
//        }
//
//        public int shortestPath(int node1, int node2) {
//            // edge
//            if(this.map.isEmpty()){
//                return -1;
//            }
//            // check if node1, node2 are connected
//            if(!isConnected(node1, node2)){
//                return -1;
//            }
//            return runDijkstra(node1, node2);
//        }
//
//        /** Dijkstra:
//         *
//         *  BFS + wights ????
//         */
//        private int runDijkstra(int node1, int node2){
//            //Queue<IN>
//            // ???
//            // PQ: { [next_node, cost] , [next_node, cost], ....  }
//            // min PQ: (sort on cost)
//            PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
//                @Override
//                public int compare(Integer[] o1, Integer[] o2) {
//                    int diff = o1[1] - o2[1];
//                    return diff;
//                }
//            });
//            // ????
//
//            while(!pq.isEmpty()){
//                Integer[] curArr = pq.poll();
//                // check neighbors
//                for(Integer[] neighbor: this.map.get(curArr[0])){
//                    //List<Integer> newList = new ArrayList<>();
//
//                }
//            }
//
//            return 0;
//        }
//
//        private boolean isConnected(int node1, int node2){
//            if(this.map.isEmpty()){
//                return false;
//            }
//            // ???
//            return !this.map.get(node1).contains(node2) &&
//                    !this.map.get(node2).contains(node1);
//        }
//
//
//    }



    // V1
    // IDEA: Dijkstra (fixed by gpt)
    class Graph_1 {

        private Map<Integer, List<int[]>> graph;

        public Graph_1(int n, int[][] edges) {
            graph = new HashMap<>();
            for (int i = 0; i < n; i++) {
                graph.put(i, new ArrayList<>());
            }

            for (int[] edge : edges) {
                addEdge(edge);
            }
        }

        public void addEdge(int[] edge) {
            int from = edge[0];
            int to = edge[1];
            int cost = edge[2];
            graph.get(from).add(new int[]{to, cost});
        }

        public int shortestPath(int node1, int node2) {
            // Standard Dijkstra’s Algorithm
            PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
            pq.offer(new int[]{node1, 0}); // {node, totalCost}

            Map<Integer, Integer> minDist = new HashMap<>();
            minDist.put(node1, 0);

            while (!pq.isEmpty()) {
                int[] cur = pq.poll();
                int node = cur[0];
                int cost = cur[1];

                // If we reached target node
                if (node == node2) return cost;

                // Skip outdated entry
                if (cost > minDist.getOrDefault(node, Integer.MAX_VALUE)) continue;

                for (int[] nei : graph.getOrDefault(node, new ArrayList<>())) {
                    int next = nei[0];
                    int nextCost = cost + nei[1];

                    // Relax edge
                    if (nextCost < minDist.getOrDefault(next, Integer.MAX_VALUE)) {
                        minDist.put(next, nextCost);
                        pq.offer(new int[]{next, nextCost});
                    }
                }
            }
            return -1; // unreachable
        }

    }

    // V2
    // IDEA: Dijkstra, fixed by gemini
    class Graph_2 {

        // Adjacency List: node -> List of {neighbor, cost} pairs
        // Using List<int[]> is cleaner than List<Integer[]> for primitive arrays.
        private final Map<Integer, List<int[]>> adj;
        private final int N; // Number of nodes (0 to N-1)

        /**
         * Constructor: Builds the initial graph from the edges array.
         */
        public Graph_2(int n, int[][] edges) {
            this.N = n;
            this.adj = new HashMap<>();

            // Initialize the map to ensure every node is present, even if isolated.
            for (int i = 0; i < n; i++) {
                adj.put(i, new ArrayList<>());
            }

            // Add initial edges
            for (int[] edge : edges) {
                addEdge(edge);
            }
        }

        /**
         * Adds a new directed edge to the graph.
         */
        public void addEdge(int[] edge) {
            int from = edge[0];
            int to = edge[1];
            int cost = edge[2];

            // --- FIX: Correctly add the neighbor and its cost to the adjacency list ---
            // We use int[] {to, cost} to represent the edge.
            this.adj.get(from).add(new int[]{to, cost});
        }

        /**
         * Finds the shortest path from node1 to node2 using Dijkstra's algorithm.
         */
        public int shortestPath(int node1, int node2) {

            // Array to store the minimum distance from node1 to every other node.
            // Initialize distances to a very large number (infinity).
            // Long is safer, but problem constraints allow Integer.MAX_VALUE
            int[] dist = new int[N];
            Arrays.fill(dist, Integer.MAX_VALUE);

            // Distance from the source node to itself is 0.
            dist[node1] = 0;

            // Min-Heap Priority Queue for Dijkstra's. Stores {cost, node}.
            // We prioritize lower cost.
            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

            // Start Dijkstra from node1 with cost 0.
            pq.offer(new int[]{0, node1}); // {cost, node}

            // --- Standard Dijkstra's Algorithm ---
            while (!pq.isEmpty()) {
                int[] current = pq.poll();
                int currentCost = current[0];
                int u = current[1];

                // If we found a shorter path to 'u' already, skip this outdated entry.
                if (currentCost > dist[u]) {
                    continue;
                }

                // Optimization: If we reached the target, return its distance.
                if (u == node2) {
                    return currentCost;
                }

                // Iterate through all neighbors (v) of the current node (u)
                for (int[] neighborEdge : adj.getOrDefault(u, Collections.emptyList())) {
                    int v = neighborEdge[0];
                    int edgeCost = neighborEdge[1];

                    // Relaxation step: new path cost = path to u + edge cost u->v
                    /**
                     *
                     * Relaxation is the process of checking whether the current
                     * known shortest path to a vertex can be improved
                     * by taking a different route through another vertex.
                     *
                     * -----
                     *
                     *
                     * Excellent — that’s one of the core ideas in Dijkstra’s algorithm:
                     * the relaxation step is what progressively updates the shortest known distance to each node.
                     *
                     * Let’s unpack that clearly. 👇
                     *
                     * ⸻
                     *
                     * 🧠 Concept: Relaxation
                     *
                     * Definition
                     *
                     * Relaxation is the process of checking whether the current known shortest path to a vertex can be improved by taking a different route through another vertex.
                     *
                     * ⸻
                     *
                     * In Dijkstra’s terms:
                     *
                     * Let’s say we are visiting node u (current node),
                     * and v is one of its neighbors with edge weight w(u, v).
                     *
                     * We currently know:
                     * 	•	dist[u]: shortest distance from start node → u
                     * 	•	dist[v]: shortest distance from start node → v (may be infinity if not reached yet)
                     *
                     * Then we check if:
                     *
                     * dist[v] > dist[u] + w(u, v)
                     *
                     * If true → we’ve found a shorter path to v through u,
                     * so we relax that edge by updating:
                     *
                     * dist[v] = dist[u] + w(u, v)
                     *
                     * and we push v back into the priority queue with the new cost.
                     *
                     * ⸻
                     *
                     * ✅ Example
                     *
                     * Imagine this weighted directed graph:
                     *
                     * 0 --(4)--> 1
                     * 0 --(3)--> 2
                     * 2 --(1)--> 1
                     * 1 --(2)--> 3
                     * 2 --(4)--> 3
                     *
                     * Start:
                     * dist[0]=0, all others = ∞
                     *
                     * 1️⃣ Pop (0,0): neighbors
                     * 	•	For (0→1,4): dist[1] = 4
                     * 	•	For (0→2,3): dist[2] = 3
                     *
                     * PQ = [(2,3), (1,4)]
                     *
                     * 2️⃣ Pop (2,3): neighbors
                     * 	•	For (2→1,1): check dist[1] > dist[2]+1 → 4 > 4 → equal → no update
                     * 	•	For (2→3,4): dist[3] > 3+4 → ∞ > 7 → ✅ relax → dist[3]=7
                     *
                     * PQ = [(1,4), (3,7)]
                     *
                     * 3️⃣ Pop (1,4): neighbor (1→3,2)
                     * 	•	Check dist[3] > 4+2 → 7 > 6 → ✅ relax → dist[3]=6
                     *
                     * PQ = [(3,6)]
                     * → shortest path to node 3 = 6
                     *
                     * ⸻
                     *
                     * 💡 Code line in your Dijkstra:
                     *
                     * int nextCost = cost + nei[1];
                     * if (nextCost < minDist.getOrDefault(next, Integer.MAX_VALUE)) {
                     *     minDist.put(next, nextCost);
                     *     pq.offer(new int[]{next, nextCost});
                     * }
                     *
                     * This is exactly the “relaxation step”:
                     * 	•	Compute the new possible distance via the current node (cost + edge weight)
                     * 	•	If it’s shorter → update the record + enqueue it for further exploration.
                     *
                     * ⸻
                     *
                     * 🔁 In summary
                     *
                     * Term	Meaning
                     * Relaxation	Updating a node’s shortest known distance when a better one is found
                     * Formula	dist[v] = min(dist[v], dist[u] + w(u, v))
                     * Purpose	To propagate the shortest path info through the graph
                     * Used in	Dijkstra, Bellman-Ford, A*, etc.
                     *
                     */
                    int newPathCost = currentCost + edgeCost;

                    // If a shorter path to 'v' is found
                    if (newPathCost < dist[v]) {
                        dist[v] = newPathCost;

                        // Add the neighbor to the PQ with the new, lower cost.
                        pq.offer(new int[]{newPathCost, v});
                    }
                }
            }

            // After the loop, dist[node2] holds the shortest path cost.
            // If it's still MAX_VALUE, node2 is unreachable.
            return dist[node2] == Integer.MAX_VALUE ? -1 : dist[node2];
        }
    }




}
