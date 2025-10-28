package dev.LCWeekly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Leetcode weekly contest 114
 * https://leetcode.com/contest/biweekly-contest-114/
 * 力扣 第 114 场双周赛(中文版)
 * https://leetcode.cn/contest/biweekly-contest-114/
 */
public class Weekly114 {

    // Q1
    // LC 2869: OK
    // https://leetcode.com/problems/minimum-operations-to-collect-elements/description/
    // 23.06 - 23.16 pm
    /**
     *   -> Return the `min` number of
     *   op needed to collect elements 1, 2, ..., k.
     *
     *
     *   - array nums of positive integers and an integer k.
     *   - op: remove the last element of the array and add it to your collection.
     *
     *
     *   IDEA 1) BRUTE FORCE
     *
     *
     */
    public int minOperations(List<Integer> nums, int k) {
        // edge
        if (nums == null || nums.isEmpty()) {
            return 0;
        }
        // ??
        if (k <= 0) {
            return 0;
        }
        if (k == nums.size()) {
            return k;
        }

        List<Integer> list = new ArrayList<>();
        HashSet<Integer> set = new HashSet<>();
        // ???
        for (int i = nums.size() - 1; i >= 0; i--) {
            if (set.size() == k) {
                return list.size();
            }
            int val = nums.get(i);
            System.out.println(">>> i = " + i + ", val = " + val);
            if (val <= k) {
                set.add(val);
            }
            list.add(val);
        }

        return list.size();
    }




    // Q2
    // LC 2870
    // 23.19 - 23.29 pm
    // https://leetcode.com/problems/minimum-number-of-operations-to-make-array-empty/
    public int minOperations(int[] nums) {

        return 0;
    }



    // Q3
    // LC 2871
    // https://leetcode.com/problems/split-array-into-maximum-number-of-subarrays/

    // Q4
    // LC 2872
    // https://leetcode.com/problems/maximum-number-of-k-divisible-components/description/

}
