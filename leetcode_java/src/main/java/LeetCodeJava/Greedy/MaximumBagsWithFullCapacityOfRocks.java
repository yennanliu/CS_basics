package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-bags-with-full-capacity-of-rocks/description/

import java.util.*;

/**
 *  2279. Maximum Bags With Full Capacity of Rocks
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You have n bags numbered from 0 to n - 1. You are given two 0-indexed integer arrays capacity and rocks. The ith bag can hold a maximum of capacity[i] rocks and currently contains rocks[i] rocks. You are also given an integer additionalRocks, the number of additional rocks you can place in any of the bags.
 *
 * Return the maximum number of bags that could have full capacity after placing the additional rocks in some bags.
 *
 *
 *
 * Example 1:
 *
 * Input: capacity = [2,3,4,5], rocks = [1,2,4,4], additionalRocks = 2
 * Output: 3
 * Explanation:
 * Place 1 rock in bag 0 and 1 rock in bag 1.
 * The number of rocks in each bag are now [2,3,4,4].
 * Bags 0, 1, and 2 have full capacity.
 * There are 3 bags at full capacity, so we return 3.
 * It can be shown that it is not possible to have more than 3 bags at full capacity.
 * Note that there may be other ways of placing the rocks that result in an answer of 3.
 * Example 2:
 *
 * Input: capacity = [10,2,2], rocks = [2,2,0], additionalRocks = 100
 * Output: 3
 * Explanation:
 * Place 8 rocks in bag 0 and 2 rocks in bag 2.
 * The number of rocks in each bag are now [10,2,2].
 * Bags 0, 1, and 2 have full capacity.
 * There are 3 bags at full capacity, so we return 3.
 * It can be shown that it is not possible to have more than 3 bags at full capacity.
 * Note that we did not use all of the additional rocks.
 *
 *
 * Constraints:
 *
 * n == capacity.length == rocks.length
 * 1 <= n <= 5 * 104
 * 1 <= capacity[i] <= 109
 * 0 <= rocks[i] <= capacity[i]
 * 1 <= additionalRocks <= 109
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 109,003/160.7K
 * Acceptance Rate
 * 67.8%
 * Topics
 * icon
 * Companies
 * Hint 1
 * Hint 2
 * Similar Questions
 * Discussion (54)
 */
public class MaximumBagsWithFullCapacityOfRocks {

    // V0
//    public int maximumBags(int[] capacity, int[] rocks, int additionalRocks) {
//
//    }

    // V0-1
    // IDEA: SORT + [idx, diff] List
    public int maximumBags_0_1(int[] capacity, int[] rocks, int additionalRocks) {
        // edge
        if(capacity == null || rocks == null){
            return 0;
        }
        if(capacity.length == 1 || rocks.length == 1){
            if(capacity[0] == rocks[0]){
                return 1;
            }
            return rocks[0] + additionalRocks >= capacity[0] ? 1 : 0;
        }

        // list: [ [idx, diff] ]
        List<Integer[]> list = new ArrayList<>();
        for(int i = 0; i < capacity.length; i++){
            int cap = capacity[i];
            int rock = rocks[i];
            list.add(new Integer[]{i, cap - rock});
        }

        // sort (small -> big) on diff
        Collections.sort(list, new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o1[1] - o2[1];
                return diff;
            }
        });

        int cnt = 0;

        // ??
        for(Integer[] cur: list){
            // NOTE !!! since we sort on diff (samll -> big)
            // then thr `diff == 0` case should occur first
            // -> it's Ok to early exit when `additionalRocks == 0`
            // without missing any `already full capacity case with an index`
            if(additionalRocks == 0){
                return cnt;
                //break;
            }
            int diff = cur[1];
            // case 1) already `full capacity`
            if(diff == 0){
                cnt += 1;
            }else{
                if(additionalRocks >= diff){
                    additionalRocks -= diff;
                    cnt += 1;
                }else{
                    additionalRocks = 0; // ???
                }
            }
        }

        return cnt;
    }

    // V0-2
    // IDEA: DIFF ARRAY (fixed by gpt)
    public int maximumBags_0_2(int[] capacity, int[] rocks, int additionalRocks) {
        // edge
        if (capacity == null || rocks == null) {
            return 0;
        }

        int n = capacity.length;
        int[] diff = new int[n];

        // diff[i] = how many more rocks needed to fill this bag
        for (int i = 0; i < n; i++) {
            diff[i] = capacity[i] - rocks[i];
        }

        // sort diff ascending
        Arrays.sort(diff);

        int cnt = 0;

        for (int need : diff) {
            if (need == 0) {
                cnt++;       // already full
            } else if (additionalRocks >= need) {
                additionalRocks -= need;
                cnt++;
            } else {
                break;       // cannot fill further bags
            }
        }

        return cnt;
    }

    // V0-3
    // IDEA: SORT + DIFF ARRAY (gemini)
    public int maximumBags_0_3(int[] capacity, int[] rocks, int additionalRocks) {
        if (capacity == null || rocks == null || capacity.length == 0) {
            return 0;
        }

        int n = capacity.length;
        // Array to store the number of rocks needed for each bag: capacity[i] - rocks[i].
        int[] neededRocks = new int[n];

        // 1. Calculate the required rocks for each bag.
        for (int i = 0; i < n; i++) {
            neededRocks[i] = capacity[i] - rocks[i];
        }

        // 2. Apply the Greedy Strategy: Sort the neededRocks array in ascending order.
        // This ensures we always fill the bags that cost the fewest additional rocks first.
        Arrays.sort(neededRocks);

        int fullBagsCount = 0;

        // 3. Iterate through the sorted differences and fill the bags.
        for (int needed : neededRocks) {
            // Case 1: The bag is already full (needed == 0).
            if (needed == 0) {
                fullBagsCount++;
                continue;
            }

            // Case 2: The bag requires rocks, and we have enough.
            if (additionalRocks >= needed) {
                additionalRocks -= needed;
                fullBagsCount++;
            } else {
                // If we encounter a bag we cannot fill, because the array is sorted,
                // we cannot fill any subsequent (more costly) bags either.
                break;
            }
        }

        return fullBagsCount;
    }

    // V1-1
    // IDEA: SORT + GREEDY
    // https://leetcode.ca/2022-06-20-2279-Maximum-Bags-With-Full-Capacity-of-Rocks/
    public int maximumBags_1_1(int[] capacity, int[] rocks, int additionalRocks) {
        int len = capacity.length;
        for (int i = 0; i < len; i++) {
            capacity[i] -= rocks[i];
        }
        Arrays.sort(capacity);
        int total = 0;
        for (int i = 0; i < len && additionalRocks > 0; i++) {
            if (capacity[i] <= additionalRocks) {
                additionalRocks -= capacity[i];
                total++;
            }
        }
        return total;
    }


    // V1-2
    // IDEA: SORT
    // https://leetcode.ca/2022-06-20-2279-Maximum-Bags-With-Full-Capacity-of-Rocks/
    public int maximumBags_1_2(int[] capacity, int[] rocks, int additionalRocks) {
        int n = capacity.length;
        int[] d = new int[n];
        for (int i = 0; i < n; ++i) {
            d[i] = capacity[i] - rocks[i];
        }
        Arrays.sort(d);
        int ans = 0;
        for (int v : d) {
            if (v <= additionalRocks) {
                ++ans;
                additionalRocks -= v;
            } else {
                break;
            }
        }
        return ans;
    }

    // V2
    // https://leetcode.com/problems/maximum-bags-with-full-capacity-of-rocks/solutions/2061949/greedy-intuition-o1-space-cjava-by-xxvvp-kb8k/
    public int maximumBags_2(int[] C, int[] R, int add) {
        int n = R.length, cnt=0;
        for(int i = 0;i < n; i++) R[i] = C[i] - R[i];
        Arrays.sort(R);
        for(int i = 0;i < n && (R[i] - add) <= 0;i++) {
            cnt++;
            add -= R[i];
        }
        return cnt;
    }

    // V3
    // IDEA: GREEDY + SORT
    // https://leetcode.com/problems/maximum-bags-with-full-capacity-of-rocks/solutions/7297729/easy-java-solution-beginner-friendly-sol-g0lm/
    public int maximumBags_3(int[] capacity, int[] rocks, int additionalRocks) {
        int n = capacity.length;
        int bags = 0;
        int[] required = new int[n];

        for( int i = 0; i < n; i++ ) {
            required[i] = capacity[i] - rocks[i];
        }
        Arrays.sort(required);

        for( int i = 0; i < n; i++ ) {
            if( additionalRocks >= required[i]) {
                additionalRocks -= required[i];
                bags += 1;
            }
            else if( required[i] == 0 ) {
                bags += 1;
            }
        }

        return bags;
    }



}
