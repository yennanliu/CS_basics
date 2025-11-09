package dev.LCWeekly;

import java.util.*;

/**
 * LeetCode biweekly contest 345
 * https://leetcode.com/contest/weekly-contest-345/
 * 中文題目
 * https://leetcode.cn/contest/weekly-contest-345/
 */
public class Weelly345 {

    // Q1
    // LC 2682
    // https://leetcode.com/problems/find-the-losers-of-the-circular-game/
    // 10.40 - 50 am
    /**
     *
     *  -> Given the number of friends, n, and an integer k,
     *   return the `array answer`,
     *   which contains the losers of the game in the ascending order.
     *
     *
     *   -> return the arr with list of losers (small -> big)
     *
     *    - n people seat in circle, from 1 to n in clockwise order.
     *    - rules of the game
     *      - 1st person gets ball, then pass the ball to one who
     *        is k step away from the cur person
     *       - pass to 2 * k step away from the cur person
     *       - pass to 3 * k step away from the cur person
     *       .....
     *
     *   - NOTE: The game is FINISHED
     *          when some friend receives the
     *          ball for the SECOND time.
     *
     *    - The losers of the game are friends
     *      who DID NOT receive the ball in the ENTIRE game.
     *
     *
     *
     *  IDEA 1) SET + HASHMAP + math ?????
     *
     *  IDEA 2)
     *
     *
     *   exp 1)
     *   Input: n = 5, k = 2
     *   Output: [4,5]
     *
     *        1
     *     5     2
     *       4  3
     *
     *   exp 2)
     *   Input: n = 4, k = 4
     *   Output: [2,3,4]
     *
     *        1
     *     4     2
     *       3
     *
     *
     */
    public int[] circularGameLosers(int n, int k) {
        // edge
        if(n == 1){
            return new int[]{0}; // ???
        }
        // ???
        if(k == 0){
            return new int[]{}; // ???
        }

        Set<Integer> set = new HashSet<>();

        boolean isVisitTwice = false;
        int cnt = 1; // ???
        int idx = 1;
        //int ken = n;
        // ???
        while(isVisitTwice){
            int val = cnt * k;
            // adjust
            val = val % n; // /?
            idx += val;
            if(set.contains(idx)){
                isVisitTwice = true;
            }
            set.add(idx);
            cnt += 1;
        }

        List<Integer> list = new ArrayList<>();
        for(int i = 1; i < n; i++){
            if(!set.contains(i)){
                list.add(i);
            }
        }

        int[] res = new int[list.size()];
        for(int i = 0; i < list.size(); i++){
            res[i] = list.get(i);
        }

        return res; // ??? should NOT visit here???
    }



    // Q2
    // LC 2683
    // https://leetcode.com/problems/neighboring-bitwise-xor/description/
    // 11.02 - 11.12 AM
    /**
     *
     *  -> Return true if such an array exists or false otherwise.
     *
     *  - for index = i, (range [0, n - 1])
     *    - if i = n - 1
     *      - derived[i] = original[i] ⊕ original[0]
     *    - otherwise
     *      - derived[i] = original[i] ⊕ original[i + 1]
     *
     *
     */
    public boolean doesValidArrayExist(int[] derived) {
        // edge
        if(derived == null || derived.length == 0){
            return true;
        }

        //  int result = a ^ b; // Binary: 0110 (which is 6 in decimal)

        // cache as original
        int[] original = derived;

        // ??? op
        int n = derived.length;
        for(int i = 0; i < n; i++){
            // check if `can form as original`
            if(n > 0){
                if(isSameBySort(original, derived)){
                    return true;
                }
            }
            // case 1) i == n - 1
            if(i == n - 1){
                derived[i] = (original[i] ^ original[0]); // ???
            }
            // case 2) otherwise
            else{
                derived[i] = (original[i] ^ original[i + 1]); // ???
            }
        }

        return false;
    }

    private boolean isSameBySort(int[] original, int[] cur){
        // sort ??
        Arrays.sort(original);
        Arrays.sort(cur);

        //????
        if(original.length != cur.length){
            return false;
        }
        for(int i = 0; i < original.length; i++){
            if(original[i] != cur[i]){
                return false;
            }
        }

       // ???
       // return original.equals(cur);
        return true;
    }




    // Q3
    // LC 2684
    // https://leetcode.com/problems/maximum-number-of-moves-in-a-grid/description/

    // Q4
    // LC 2685
    // https://leetcode.com/problems/count-the-number-of-complete-components/description/



}
