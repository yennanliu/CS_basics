package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-bags-with-full-capacity-of-rocks/description/

import java.util.Arrays;

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
