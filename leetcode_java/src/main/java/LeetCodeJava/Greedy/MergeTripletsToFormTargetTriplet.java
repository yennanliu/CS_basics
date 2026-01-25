package LeetCodeJava.Greedy;

// https://leetcode.com/problems/merge-triplets-to-form-target-triplet/description/
// https://leetcode.cn/problems/merge-triplets-to-form-target-triplet/

import java.util.*;

/**
 * 1899. Merge Triplets to Form Target Triplet
 * Medium
 * Topics
 * Companies
 * Hint
 * A triplet is an array of three integers. You are given a 2D integer array triplets, where triplets[i] = [ai, bi, ci] describes the ith triplet. You are also given an integer array target = [x, y, z] that describes the triplet you want to obtain.
 *
 * To obtain target, you may apply the following operation on triplets any number of times (possibly zero):
 *
 * Choose two indices (0-indexed) i and j (i != j) and update triplets[j] to become [max(ai, aj), max(bi, bj), max(ci, cj)].
 * For example, if triplets[i] = [2, 5, 3] and triplets[j] = [1, 7, 5], triplets[j] will be updated to [max(2, 1), max(5, 7), max(3, 5)] = [2, 7, 5].
 * Return true if it is possible to obtain the target triplet [x, y, z] as an element of triplets, or false otherwise.
 *
 *
 *
 * Example 1:
 *
 * Input: triplets = [[2,5,3],[1,8,4],[1,7,5]], target = [2,7,5]
 * Output: true
 * Explanation: Perform the following operations:
 * - Choose the first and last triplets [[2,5,3],[1,8,4],[1,7,5]]. Update the last triplet to be [max(2,1), max(5,7), max(3,5)] = [2,7,5]. triplets = [[2,5,3],[1,8,4],[2,7,5]]
 * The target triplet [2,7,5] is now an element of triplets.
 * Example 2:
 *
 * Input: triplets = [[3,4,5],[4,5,6]], target = [3,2,5]
 * Output: false
 * Explanation: It is impossible to have [3,2,5] as an element because there is no 2 in any of the triplets.
 * Example 3:
 *
 * Input: triplets = [[2,5,3],[2,3,4],[1,2,5],[5,2,3]], target = [5,5,5]
 * Output: true
 * Explanation: Perform the following operations:
 * - Choose the first and third triplets [[2,5,3],[2,3,4],[1,2,5],[5,2,3]]. Update the third triplet to be [max(2,1), max(5,2), max(3,5)] = [2,5,5]. triplets = [[2,5,3],[2,3,4],[2,5,5],[5,2,3]].
 * - Choose the third and fourth triplets [[2,5,3],[2,3,4],[2,5,5],[5,2,3]]. Update the fourth triplet to be [max(2,5), max(5,2), max(5,3)] = [5,5,5]. triplets = [[2,5,3],[2,3,4],[2,5,5],[5,5,5]].
 * The target triplet [5,5,5] is now an element of triplets.
 *
 *
 * Constraints:
 *
 * 1 <= triplets.length <= 105
 * triplets[i].length == target.length == 3
 * 1 <= ai, bi, ci, x, y, z <= 1000
 *
 */
public class MergeTripletsToFormTargetTriplet {

    // V0
    // IDEA: GREEDY + set
    // https://neetcode.io/problems/merge-triplets-to-form-target
    public boolean mergeTriplets(int[][] triplets, int[] target) {
        // edge
        if(triplets == null || triplets.length == 0){
            return false;
        }
        // step 1) remove `not qualified` element
        List<int[]> collected = new ArrayList<>();
        for(int[] t: triplets){
            /**
             *  NOTE !!! below can be optimized as
             *
             *  if( t[0] > target[0] || t[1] > target[1] || t[2] > target[2] ){
             *       continue;
             *  }
             *
             */
            boolean isValid = true;
            for(int i = 0; i < t.length; i++){
                if(t[i] > target[i]){
                    isValid = false;
                    // early break, NOTE !!! even break, it will still run the code in first `for loop`, so that's why we need the boolean
                    break;
                }
            }
            if(isValid){
                collected.add(t);
            }
        }

        // step 2) init set
        HashSet<Integer> set = new HashSet<>();
        // step 3) compare if there is a `val with idx` that as same as val in target with same idx
        for(int[] c: collected){
            for(int i = 0; i < c.length; i++){
                //System.out.println(">>> c[i]  = " + c[i] + ",  target[i] = " +  target[i]);
                if(c[i] == target[i]){

                    // NOTE !!! we collect `idx`, instead of the actual value
                    // since all we want to know is:
                    // if the `combination of candidates can form target`
                    set.add(i);
                }
            }
        }

        //System.out.println(">>> set = " + set);
        return set.size() == target.length;
    }


    // V0-0-1
    // IDEA: GREEDY + pre-check
    public boolean mergeTriplets_0_0_1(int[][] triplets, int[] target) {
        // edge
        if (triplets == null && target == null) {
            return false;
        }
        // ??
        if (triplets == null) {
            return false;
        }
        for (int[] x : triplets) {
            if (isSame(x, target)) {
                return true;
            }
        }

        // ???
        List<int[]> candidates = new ArrayList<>();
        for (int[] x : triplets) {
            boolean isValid = true;
            for (int i = 0; i < x.length; i++) {
                if (x[i] > target[i]) {
                    isValid = false;
                    break; // ???
                }
            }
            if (isValid) {
                candidates.add(x);
            }
        }

        // ???
        //System.out.println(">>> candidates = " + candidates);

        int[] tmp = new int[target.length];
        Arrays.fill(tmp, 0);
        // ???
        for (int[] x : candidates) {
            for (int i = 0; i < x.length; i++) {
                tmp[i] = Math.max(
                        tmp[i],
                        x[i]);
            }
        }

        // ???
        //System.out.println(">>> tmp = " + Arrays.toString(tmp));

        return isSame(tmp, target);
    }

    private boolean isSame(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    // V0-0-2
    // IDEA: GREEDY (gemini)
    public boolean mergeTriplets_0_0_2(int[][] triplets, int[] target) {
        // We only need to track if we've successfully reached the target
        // for each of the three positions.
        boolean[] reached = new boolean[3];

        for (int[] t : triplets) {
            // 1. FILTER: A triplet is only useful if NO element exceeds its target counterpart.
            // If any element is bigger, merging it would make the result too big.
            if (t[0] <= target[0] && t[1] <= target[1] && t[2] <= target[2]) {

                // 2. UPDATE: If it's a valid triplet, check if it helps us hit
                // any of the specific target values.
                for (int i = 0; i < 3; i++) {
                    if (t[i] == target[i]) {
                        reached[i] = true;
                    }
                }
            }

            // 3. OPTIMIZATION: If we reached all three targets, we are done!
            if (reached[0] && reached[1] && reached[2])
                return true;
        }

        return reached[0] && reached[1] && reached[2];
    }



    // V0-1
    public boolean mergeTriplets_0_1(int[][] triplets, int[] target) {
        // edge
        if(triplets == null || triplets.length == 0){
            return false;
        }

        List<int[]> candidates = new ArrayList<>();

        // filer out the `not valid arr`
        for(int[] x: triplets){
            /** NOTE !!!
             *
             * we need `canAdd` boolean
             * so can decide whether add current array to candidates or not
             */
            boolean canAdd = true;
            for(int i = 0; i < x.length; i++){
                if(x[i] > target[i]){
                    canAdd = false;
                    // early break, NOTE !!! even break, it will still run the code in first `for loop`, so that's why we need the boolean
                    break; // early break
                }
            }
            if(canAdd){
                candidates.add(x);
            }
        }

        // check if we can get the amount of elements that has same size as target
        // (hashset size)
        // NOTE : set {idx of val} ( to deal with `same val but different idx`)
        HashSet<Integer> set = new HashSet<>();

        for(int[] x: candidates){
            for(int i = 0; i < x.length; i++){
                if(x[i] == target[i]){
                    // NOTE !!! we add `idx` to set, instead of val
                    //set.add(x[i]);
                    set.add(i);
                }
            }
        }

        return set.size() == target.length;
    }

    // V1-1
    // https://neetcode.io/problems/merge-triplets-to-form-target
    // IDEA: GREEDY
    public boolean mergeTriplets_1_1(int[][] triplets, int[] target) {
        Set<Integer> good = new HashSet<>();

        for (int[] t : triplets) {
            if (t[0] > target[0] || t[1] > target[1] || t[2] > target[2]) {
                continue;
            }
            for (int i = 0; i < t.length; i++) {
                if (t[i] == target[i]) {
                    good.add(i);
                }
            }
        }
        return good.size() == 3;
    }

    // V1-2
    // https://neetcode.io/problems/merge-triplets-to-form-target
    // IDEA: GREEDY (OPTIMAL)
    public boolean mergeTriplets_1_2(int[][] triplets, int[] target) {
        boolean x = false, y = false, z = false;
        for (int[] t : triplets) {
            x |= (t[0] == target[0] && t[1] <= target[1] && t[2] <= target[2]);
            y |= (t[0] <= target[0] && t[1] == target[1] && t[2] <= target[2]);
            z |= (t[0] <= target[0] && t[1] <= target[1] && t[2] == target[2]);
            if (x && y && z) {
                return true;
            }
        }
        return false;
    }


    // V2-1
    // https://leetcode.ca/2021-07-27-1899-Merge-Triplets-to-Form-Target-Triplet/
    public boolean mergeTriplets_2_1(int[][] triplets, int[] target) {
        int[] merged = new int[3];
        for (int[] triplet : triplets) {
            if (triplet[0] <= target[0] && triplet[1] <= target[1] && triplet[2] <= target[2]) {
                merged[0] = Math.max(merged[0], triplet[0]);
                merged[1] = Math.max(merged[1], triplet[1]);
                merged[2] = Math.max(merged[2], triplet[2]);
            }
        }
        return Arrays.equals(merged, target);
    }


    // V2-2
    // https://leetcode.ca/2021-07-27-1899-Merge-Triplets-to-Form-Target-Triplet/
    public boolean mergeTriplets_2_2(int[][] triplets, int[] target) {
        int maxA = 0, maxB = 0, maxC = 0;
        for (int[] triplet : triplets) {
            int a = triplet[0], b = triplet[1], c = triplet[2];
            if (a <= target[0] && b <= target[1] && c <= target[2]) {
                maxA = Math.max(maxA, a);
                maxB = Math.max(maxB, b);
                maxC = Math.max(maxC, c);
            }
        }
        return maxA == target[0] && maxB == target[1] && maxC == target[2];
    }



}
