package dev.LCWeekly;

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

    // Q4
    // LC 2642
    // https://leetcode.com/problems/design-graph-with-shortest-path-calculator/

    

}
