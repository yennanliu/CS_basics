package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-number-of-groups-with-increasing-length/description/

import java.util.*;

/**
 * 2790. Maximum Number of Groups With Increasing Length
 * Hard
 *
 * You are given a 0-indexed array usageLimits of length n.
 *
 * Your task is to create groups using numbers from 0 to n - 1, ensuring that each number, i, is used no more than usageLimits[i] times in total across all groups. You must also satisfy the following conditions:
 *
 * Each group must consist of distinct numbers, meaning that no duplicate numbers are allowed within a single group.
 * Each group (except the first one) must have a length strictly greater than the previous group.
 *
 * Return an integer denoting the maximum number of groups you can create while satisfying these conditions.
 *
 *
 * Example 1:
 *
 * Input: usageLimits = [1,2,5]
 * Output: 3
 * Explanation: In this example, we can use 0 at most once, 1 at most twice, and 2 at most five times.
 * One way of creating the maximum number of groups while satisfying the conditions is:
 * Group 1 contains the number [2].
 * Group 2 contains the numbers [1,2].
 * Group 3 contains the numbers [0,1,2].
 * It can be shown that the maximum number of groups is 3.
 * So, the output is 3.
 *
 * Example 2:
 *
 * Input: usageLimits = [2,1,2]
 * Output: 2
 * Explanation: In this example, we can use 0 at most twice, 1 at most once, and 2 at most twice.
 * One way of creating the maximum number of groups while satisfying the conditions is:
 * Group 1 contains the number [0].
 * Group 2 contains the numbers [1,2].
 * It can be shown that the maximum number of groups is 2.
 * So, the output is 2.
 *
 * Example 3:
 *
 * Input: usageLimits = [1,1]
 * Output: 1
 * Explanation: In this example, we can use both 0 and 1 at most once.
 * One way of creating the maximum number of groups while satisfying the conditions is:
 * Group 1 contains the number [0].
 * It can be shown that the maximum number of groups is 1.
 * So, the output is 1.
 *
 *
 * Constraints:
 *
 * 1 <= usageLimits.length <= 10^5
 * 1 <= usageLimits[i] <= 10^9
 *
 */
public class MaximumNumberOfGroupsWithIncreasingLength {

    // V0
    // IDEA: SORT + GREEDY PREFIX BUDGET
    /**
     *  The group sizes can always be taken as 1, 2, ..., k, so k groups need
     *  1 + 2 + ... + k slots. With the limits sorted ascending, k groups are
     *  feasible iff every prefix of limits can pay for the matching prefix of
     *  group sizes. The scan checks that incrementally: `pool` is the leftover
     *  capacity seen so far, and whenever it can pay for a group of size
     *  k + 1 we open that group.
     *
     *  NOTE: limits reach 1e9 and n reaches 1e5, so the pool needs a long.
     *
     *  time = O(N log N)
     *  space = O(N)
     */
    public int maxIncreasingGroups(List<Integer> usageLimits) {
        // sort a copy so the caller's list is left untouched
        List<Integer> limits = new ArrayList<>(usageLimits);
        Collections.sort(limits);

        int groups = 0;
        long pool = 0; // spare capacity of the prefix scanned so far

        for (int limit : limits) {
            pool += limit;
            // the next group would have size groups + 1
            if (pool >= groups + 1) {
                groups += 1;
                pool -= groups;
            }
        }
        return groups;
    }
}
