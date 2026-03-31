package LCWeekly;

import java.util.*;

/**
 *  - EN:
 *  https://leetcode.com/contest/biweekly-contest-103/
 *
 *
 *  - ZH:
 *  https://leetcode.cn/contest/biweekly-contest-103/
 *
 *
 */
// 14.58 - 16:28 (pm)
public class Weekly103 {


    // LC 2656
    // OK
    // https://leetcode.com/problems/maximum-sum-with-exactly-k-elements/description/
    /**
     *  -> Return the `maximum` score
     *  you can achieve after performing
     *  the operation exactly `k times.`
     *
     *
     *   - 0 index nums
     *   - int k
     *
     *   - op:
     *     1. select m from nums
     *     2. remove m from nums
     *     3. add element = m + 1 to nums
     *     4. increase score by m
     *
     *
     *  -------------
     *
     *  IDEA 1) GREEDY
     *
     *  IDEA 2) PQ
     *
     *
     *
     *  -------------
     *
     *
     */
    // IDEA 1) GREEDY
    public int maximizeSum(int[] nums, int k) {
        // edge
        if(k == 0 || nums.length == 0){
            return 0;
        }

        // big PQ (big -> small)
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });
        //List<Integer> list = new ArrayList<>();
        for(int x: nums){
            pq.add(x);
        }

        int score = 0;
        for(int i = 0; i < k; i++){
            if(!pq.isEmpty()){
                //int m = list.get(list.size() - 1);
                int val = pq.poll();
                int tmp = val + 1;
                score += tmp;
                pq.add(tmp);
            }
        }

        return score;
    }



    // LC 2657
    // https://leetcode.com/problems/find-the-prefix-common-array-of-two-arrays/
    /**
     *
     *  -> Return the prefix common array of A and B.
     *
     *
     *  - 0 index permutation: A, B with len = n
     *  - prefix common array of A, B
     *     - C[i] is equal to the count of numbers
     *       that are present at or before
     *       the index i in both A and B
     *
     *
     *
     *  permutations - 排列
     *
     *  NOTE:
     *
     *   - A sequence of n integers is
     *     called a `permutation`
     *      -> if it contains all integers
     *       from 1 to n `exactly once.`
     *
     *
     *
     *
     *  -----------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) SET ??
     *
     *
     *  -----------
     *
     *  ex 1)
     *
     *  Input: A = [1,3,2,4], B = [3,1,2,4]
     *   Output: [0,2,3,4]
     *
     *   array with 0=not existed, 1 = existd ???
     *
     *
     *   A = [1,3,2,4],  arr=[1,0,0,0]
     *        i
     *
     *   B = [3,1,2,4]    arr = [0,0,1,0]
     *        i
     *
     *
     *
     *
     *   A = [1,3,2,4],   [1,3]   arr=[1,0,1,0]
     *          i
     *
     *  B = [3,1,2,4]     [3,1], arr = [1,0,1,0]
     *         j
     *
     *
     */
    //   IDEA 1) BRUTE FORCE + set ???
    // a better way ???
    public int[] findThePrefixCommonArray(int[] A, int[] B) {
        // edge
        if(A == null || B == null){
            return null; // ???
        }

        int size = A.length;
        Set<Integer> setA = new HashSet<>();
        Set<Integer> setB = new HashSet<>();

        int[] res = new int[size];

        for(int i = 0; i < size; i++){
            setA.add(A[i]);
            setB.add(B[i]);
            res[i] = countHelper(setA, setB);
        }

        return res;
    }

    // /??
    private int countHelper(Set<Integer> setA, Set<Integer> setB){
        int cnt = 0;
        for(int x: setA){
            if(setB.contains(x)){
                cnt += 1;
            }
        }
        return cnt;
    }






    // LC 2658
    // https://leetcode.com/problems/maximum-number-of-fish-in-a-grid/description/
    /**
     *  -> Return the `maximum` number of `fish` the fisher can
     *   catch
     *    - if he chooses his starting cell optimally,
     *       - or 0 if no water cell exists.
     *
     *
     *   - 2D matrix, m x n
     *   - (r,c)
     *      - land: grid[r][c] = 0
     *      - water: grid[r][c] > 0
     *           - fish cnt: grid[r][c]
     *
     *   - fisher can start from ANY `water cell`
     *   - can do below op
     *      - catch all fish at cell (r,c)
     *      or
     *      - move to any adjacent water cell.
     *
     *  ----------------
     *
     *   IDEA 1) DFS
     *
     *   IDEA 2) BFS
     *      - get `island` size, and maintain the max one
     *
     *
     *  ----------------
     *
     */
    //IDEA 1) DFS ???
    public int findMaxFish(int[][] grid) {
        // edge
        if(grid == null || grid.length == 0 || grid[0].length == 0){
            return 0;
        }

        int l = grid.length;
        int w = grid[0].length;

        int maxFish = 0;

        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                // if `water`
               // int tmpCnt = 0;
                if(grid[y][x] > 0){
                    maxFish = Math.max(
                            maxFish,
                            getFishCnt(grid, x, y)
                    );
                }
            }
        }

        return maxFish;
    }

    private int getFishCnt(int[][] grid, int x, int y){
        // edge

        int l = grid.length;
        int w = grid[0].length;

        // NOTE !!! validate here
        if(x < 0 || x >= w || y < 0 || y >= l){
            if(grid[y][x] == 0){
                return 0; // ???
            }
        }

        // NOTE !!!
        // mark as visited
        int val = grid[y][x];
        grid[y][x] = 0;


        //tmpCnt += grid[y][x]; // ???
       // getFishCnt(grid, x-1, 0, tmpCnt);
        // ???
        return val +
                getFishCnt(grid, x-1, y) +
                getFishCnt(grid, x+1, y) +
                getFishCnt(grid, x, y + 1) +
                getFishCnt(grid, x, y - 1);
    }



    // LC 2659
    // https://leetcode.com/problems/make-array-empty/
    /**
     *
     *  -> Return an integer denoting
     *  the number of operations
     *  it takes to make nums empty.
     *
     *   can do below op till the nums is EMPTY,
     *   and return the `op  cnt`
     *
     *   OP:
     *      - If the `firs`t` element has the `smallest` value, remove it
     *       - Otherwise, put the first element at the end of the array.
     *
     *
     *  -----------------
     *
     *   IDEA 1) PQ + LIST OP ????
     *
     *
     *   -----------------
     *
     *
     */
    public long countOperationsToEmptyArray(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0L; // ??
        }

        List<Integer> list = new ArrayList<>();
        for(int x: nums){
            list.add(x);
        }

        // small PQ: (small -> big)
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        long opCnt = 0L;

        // /??
        while (!pq.isEmpty()){

            int smallest = pq.peek();
            opCnt += 1; // /??

            if(smallest == list.get(0)){
                list.remove(0); // ??
                pq.poll();
            }else{
                int val = list.get(0);
                list.remove(0); // ??
                list.add(val); // ???
            }
        }

        return opCnt;
    }







}
