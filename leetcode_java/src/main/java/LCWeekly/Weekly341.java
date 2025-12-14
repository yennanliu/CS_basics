package LCWeekly;

import java.util.*;

/**
 * LeetCode weekly contest 341
 * https://leetcode.com/contest/weekly-contest-341/
 * 中文題目
 * https://leetcode.cn/contest/weekly-contest-341/
 *
 */
public class Weekly341 {

    // Q1
    // LC 2643
    // https://leetcode.com/problems/row-with-maximum-ones/description/
    // 14.44 - 54 pm
    /**
     *  -> Return an array containing the
     *     index of the row, and the number of ones in it.
     *
     *   - m x n matrix
     *   - find the 0 indexed position
     *     contains the maximum count of ones,
     *     and the number of ones in that row.
     *
     *     -> [index, max_cnt_of_one]
     *
     *
     * ------------------------
     *
     *  IDEA 1) HASHMAP + ARRAY OP
     *
     *
     * ------------------------
     *
     *
     */
    public int[] rowAndMaximumOnes(int[][] mat) {
        // edge
        if(mat == null || mat.length == 0 || mat[0].length == 0){
            return new int[]{}; // ???
        }
      //  HashMap<Integer, Integer> map = new HashMap<>();
        //int max
        int maxCnt = 0;
        int[] res = new int[2];

        // ???
        for(int i = mat.length - 1; i > 0; i--){
            int cnt = 0;
            for(int x: mat[i]){
                if(x == 1){
                    cnt += 1;
                }
            }
            if(cnt > maxCnt){
                res = new int[]{i, cnt};
            }
//            maxCnt = Math.max(maxCnt, cnt);
//            map.put(i, cnt);
        }

        //int[] res = new int[2];
        //for(int k: ma)

        return res;
    }

    // Q2
    // https://leetcode.com/problems/find-the-maximum-divisibility-score/
    // LC 2644
    // 14.55- 15.05 pm
    /**
     *
     *  -> Return the integer divisors[i] with the maximum divisibility score.
     *    If multiple integers have the maximum score, return the smallest one.
     *
     *
     *   - The divisibility score of divisors[i] is the number of
     *     indices j such that nums[j] is divisible by divisors[i].
     *
     *  -----------------
     *
     *  IDEA 1) ARRAY OP + hashmap
     *
     *
     *  -----------------
     *
     *  ex 1)
     *   Input: nums = [2,9,15,50], divisors = [5,3,7,2]
     *   -> ans  = 2
     *
     *   -> divisors[0] = 2
     *   since divisors[0] = 5
     *   and 15, 50 from nums are divisible by  (divisors[0] = 5)
     *   -> so ans = 2
     *
     */
    public int maxDivScore(int[] nums, int[] divisors) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        // { divisible_cnt : [divisors_1, divisors_2, ....] }
        Map<Integer, List<Integer>> map = new HashMap<>();

        int maxCnt = 0;

        for(int i = 0; i < nums.length; i++){
            //int x = divisors[i];
            int cnt = getDivisibleCnt(nums, divisors[i]);
            if(!map.containsKey(cnt)){
                map.put(cnt, new ArrayList<>());
            }
            map.get(cnt).add(divisors[i]); // ???
            maxCnt = Math.max(maxCnt, cnt);
        }

        List<Integer> list = map.get(maxCnt);
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        return list.get(0); // ??
    }

    private int getDivisibleCnt(int[] nums, int x){
        int res = 0;
        for(int n: nums){
            if(n % x == 0){
                res += 1;
            }
        }
        return res;
    }

    // Q3
    // LC 2645
    // 15.30 - 40 pm
    // https://leetcode.com/problems/minimum-additions-to-make-valid-string/
    /**
     *
     *  -> return the `minimum` number of letters
     *    that must be `inserted` so that word becomes `valid`.
     *
     *    - valid" if it can be formed by
     *      concatenating the string "abc" several times.
     *
     *   - word, we can insert a, b, c ANYWHERE, ANY NUM OF TIMES
     *
     *
     *
     *
     *
     * ---------------
     *
     *  IDEA 1) GREEDY V1
     *
     *    -> 1. split by "a"
     *       2. loop over every group
     *       3. add the needed alphabet, and update the action cnt
     *
     *
     *   IDEA 1) GREEDY V2 + slide window (fixed window size)
     *
     *     -> s
     *
     *
     * ---------------
     *
     *  ex 1)
     *
     *  Input: word = "b"
     *  Output: 2
     *
     *   ->  a "b" c
     *
     *
     *   ex 2)
     *
     *   Input: word = "aaa"
     *   Output: 6
     *
     *   -> a "bc" a "bc" a "bc"
     *
     *
     *  ex 3)
     *
     *   Input: word = "abc"
     *   Output: 0
     *
     *
     *  ex 4)
     *
     *   input : bca
     *
     * ->  "a" bc a
     *     "a" bc a "bc"
     *
     *
     *  ex 5)
     *
     *   cba
     *
     *   -> "ab" c ba
     *       "ab" c "a"b"c" a
     *        "ab" c "a"b"c" a "bc"
     *
     *
     *
     */
    // ????
    public int addMinimum(String word) {
        // edge
        if(word.isEmpty()){
            return 3; // ??
        }
        if(word.length() == 1){
            return 2;
        }
        // ???
        if(word.equals("abc")){
            return 0;
        }


        int opCnt = 0;
        //int l = 0; // ??
        // ??
        boolean lastAlphabetHandled = false;

        for(int l = 0; l < word.length(); l++){
            StringBuilder sb = new StringBuilder();
            char prev = '\0';  // ???
            // ???
            for(int r = l + 1; r < word.length(); r++){
                char cur = word.charAt(r);
                // case 1)
//                if(sb.toString().equals("abc")){
//                    break;
//                }
                // case 2) the `cur` alphabet is before prev alphabet
                if(cur - prev <= 0){
                    // 3 - (0-0 + 1) = 2
                    opCnt += (3 - (r-l+1));

                    // ??? check if the `last alphabet` already made as valid string
                    if(r == word.length() - 1){
                        lastAlphabetHandled = true;
                    }
                    break;
                }
                // case 3) the `cur` alphabet is after alphabet
//                else{
//
//                }
                prev = cur;
            }

        }

        // ???
        if(!lastAlphabetHandled){
            opCnt += 2;
        }

        return opCnt;
    }

//    private boolean equalToAbc(StringBuilder sb){
//        return false;
//    }

    // Q4
    // LC 2646
    // https://leetcode.com/problems/minimize-the-total-price-of-the-trips/
    // 15.54 - 16.04 pm
    /**
     *  -> Return the minimum total price sum
     *  to perform all the given trips.
     *
     *
     *  -----------------------
     *
     *
     *   -----------------------
     *
     *
     */
    public int minimumTotalPrice(int n, int[][] edges, int[] price, int[][] trips) {

        return 0;
    }


}
