package dev.LCWeekly;

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


    // Q4
    // LC 2642
    // https://leetcode.com/problems/design-graph-with-shortest-path-calculator/





}
