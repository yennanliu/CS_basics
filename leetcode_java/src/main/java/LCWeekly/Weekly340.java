package LCWeekly;

import java.util.*;

/**
 * LeetCode biweekly contest 340
 * https://leetcode.com/contest/weekly-contest-340/
 * 中文題目
 * https://leetcode.cn/contest/weekly-contest-340/
 *
 */

// 14.05 - 15.25
public class Weekly340 {

    // Q1
    // LC 2614
    // https://leetcode.com/problems/prime-in-diagonal/description/
    // 14.09 - 19 pm
    /**
     *
     *  -> Return the `largest ``prime number`
     *  that lies on
     *  `at least` `one` of the` diagonals` of nums.
     *
     *
     *  =---------------------
     *
     *   IDEA 1) ARRAY OP
     *
     *    -> collect (maintain) all diagonal elements,
     *       return the biggest one
     *
     *     time: O(N)
     *     space: O(1) // ??
     *
     *
     *  =---------------------
     *
     *   ex 1)
     *
     *   Input: nums = [
     *      [1,2,3],
     *      [5,6,7],
     *      [9,10,11]
     *  ]
     *
     *   Output: 11
     *
     *
     *   ex 2)
     *
     *   Input: nums = [
     *      [1,2,3],
     *      [5,17,7],
     *      [9,11,10]
     *   ]
     *
     *
     *    Output: 17
     *
     *
     */
    public int diagonalPrime(int[][] nums) {
        int biggestPrime = -1;
        // edge
        if(nums == null || nums.length == 0){
            return biggestPrime;
        }
        if(nums.length == 1 && nums[0].length == 1){
            return isPrime(nums[0][0]) ?  nums[0][0] : -1;
        }

        int l = nums.length;
        int w = nums[0].length;

        // V1: loop over `2 diagonal`
        // V2: loop all nums anyway, and ONLY do isPrime check
        //   if it's a `diagonal element`
        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                // NOTE !!! check if `diagonal element`
                if(x == y){
                    if(isPrime(nums[y][x])){
                        biggestPrime = Math.max(nums[y][x], biggestPrime);
                    }
                }
                // ???
                else if( y == w - x || y == w + x){
                    if (w-x >= 0){
                        if(isPrime(nums[y][x])){
                            biggestPrime = Math.max(nums[y][x], biggestPrime);
                        }
                    }
                    if(w + x < l){
                        if(isPrime(nums[y][x])){
                            biggestPrime = Math.max(nums[y][x], biggestPrime);
                        }
                    }
                }
            }
        }

        return biggestPrime > 0 ? biggestPrime : -1;
    }

    private boolean isPrime(int val){
        if(val == 1){
            return true; // ??
        }
        for(int i = 2; i < Math.sqrt(val); i++){
            if(val % i == 0){
                return true;
            }
        }
        return false;
    }




    // Q2
    // LC 2615
    // https://leetcode.com/problems/sum-of-distances/description/
    // 14.25 - 35 pm
    /**
     *  -> Return the array arr.
     *
     *   - nums:  0 based idx arr, len = nums.length
     *   - arr[i] is the sum of |i - j|,
     *       over all j,
     *        - such that nums[j] == nums[i] and j != i.
     *        - if NO such j, set arr[i] to be 0.
     *
     *
     *  ---------------
     *
     *   IDEA 1) HASHMAP:
     *     { val : [idx1, idx2, ...] }
     *
     *      |   |   |
     *      a   x   b
     *
     *      |x-a| + |b - x|
     *
     *      = (x-a) + (b-x)
     *      = b - a
     *
     *
     *      |  |    |   |
     *      a  x    b   c
     *
     *      |x-a| + |b - x| + | c - x|
     *      = (x-a) + (b-x) + (c - x)
     *      = b -a + c
     *
     *      -> so the `dist sum` is
     *       (sum all idx > x) - (sum all idx < x)
     *
     *
     *  ---------------
     *
     *
     */
    // IDEA 1) HASHMAP:
    public long[] distance(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return null;
        }
        if(nums.length == 1){
            return null; // ????
        }

        // map: { val : [idx1, idx2, ...] }
        Map<Integer, List<Integer>> map = new HashMap<>();

        for(int i = 0; i < nums.length; i++){
            int val = nums[i];
            if(!map.containsKey(val)){
                map.put(val, new ArrayList<>());
            }
            List<Integer> list = map.get(val);
            list.add(i);
            map.put(val, list);
        }

        // ???

        long[] res = new long[nums.length];

        for(int i = 0; i < nums.length; i++){
            int val = nums[i];
            res[i] = getDistSum(map.get(val), val);
           //res += getDistSum(map.get(x), x);

        }

        return res;
    }

    // TODO: improve below
    private long getDistSum(List<Integer> list, int x){
        long res = 0;
        for(int val: list){
            if(val < x){
                res -= val;
            }else{
                res += val;
            }
        }

        return res;
    }




    // Q3
    // LC 2616
    // https://leetcode.com/problems/minimize-the-maximum-difference-of-pairs/description/
    // 14.43 - 53 pm
    /**
     * ->
     *  Return the minimum maximum difference among all p pairs.
     *   We define the maximum of an empty set to be zero.
     *
     *
     *   Find p pairs of indices of nums
     *

     *    NOTE:
     *     - ensure `no` index appears
     *       more than once amongst the p pairs.
     *
     *     - difference of this pair is |nums[i] - nums[j]|,
     *      where |x| represents the absolute value of x.
     *
     *
     *    nums: 0 index based
     *    p: int
     *
     *
     *
     *
     *
     *
     * ----------------
     *
     *  IDEA 1) GREEDY
     *
     *  IDEA 2) BRUTE FORCE ???
     *
     *  IDEA 3) HASH MAP ???
     *
     *
     * ----------------
     *
     *  ex 1)
     *   Input: nums = [10,1,2,7,1,3], p = 2
     *   Output: 1
     *
     *   pq ??? (big or small pq)
     *     -> [1,1,2,3,7,10]
     *
     *   HAshmap {  1: 2, 2: 1, 3: 1, 7: 1, 10: 1 }
     *
     *
     *
     *  ex 2)
     *
     *   Input: nums = [4,2,1,2], p = 1
     *   Output: 0
     *
     *   PQ:
     *
     *     [1,2,2,4]
     *
     *
     *  ex 3)
     *
     *  Input: nums = [10,1,2,9,1,4], p = 2
     *
     *   2 pointers ???
     *
     *   [1,1,2,4,9,10]
     *
     *   -> PQ : [diff, x, y]
     *
     *   ->  [ [0,1,1], [1. 1,2]. [2,2,4], ///
     *
     */
    // IDEA 1) PQ: [ diff, idx_i, idx_j ]
    public int minimizeMax(int[] nums, int p) {
        // edge

        // small PQ: PQ: [ diff, idx_i, idx_j ]
        // sort over diff (small -> big)
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o1[0] - o2[0];
                return diff;
            }
        });


        // Sort (small -> big)
        Arrays.sort(nums); // ??? default: small -> big ???

        // ??
        // add to PQ
        for(int i = 1; i < nums.length; i++){
            int diff = Math.abs(nums[i] - nums[i-1]);
            pq.add(new Integer[]{diff, i-1, i});
        }

        int res = 0;
        // ???
        for(int i = 0; i < p; i++){
            if(!pq.isEmpty()){
                // alway pop the `min diff` from PQ
                //Integer[] item = pq.poll();
                res = Math.max(res, pq.poll()[0]); // ???
            }
        }

        return res;
    }





    // Q4
    // LC 2617
    // https://leetcode.com/problems/minimum-number-of-visited-cells-in-a-grid/
    // 15.19 - 29 pm
    /**
     *  -> Return the `minimum` number of cells
     *     you need to visit to reach the `bottom-right` cell
     *     (m - 1, n - 1).
     *       - If there is no valid path, return -1.
     *
     *
     *       - 0 idx: m x n matrix
     *
     *       - Your initial position is at the top-left cell (0, 0).
     *
     *       - you can ONLY move one of the ways as below:
     *
     *          -  if j < k <= grid[i][j] + j:
     *             - `right`
     *
     *          - if  i < k <= grid[i][j] + i
     *             - `down`
     *
     *    ------------------
     *
     *    IDEA 1) GREEDY ???
     *
     *
     *    IDEA 2) DP ????
     *
     *
     *    ------------------
     *
     *
     */
    public int minimumVisitedCells(int[][] grid) {


        return 0;
    }





}
