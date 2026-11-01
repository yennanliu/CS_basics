package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-number-of-potholes-that-can-be-fixed/description/

import java.util.*;

/**
 * 3119. Maximum Number of Potholes That Can Be Fixed
 * Medium
 *
 * 🔒 (premium)
 *
 * You are given a string road, consisting only of characters "x" and ".", where each "x" denotes a pothole and each "." denotes a smooth road, and an integer budget.
 *
 * In one repair operation, you can repair n consecutive potholes for a price of n + 1.
 *
 * Return the maximum number of potholes that can be fixed such that the sum of the price of all of the fixes doesn't go over the given budget.
 *
 *
 * Example 1:
 *
 * Input: road = "..", budget = 5
 * Output: 0
 * Explanation: There are no potholes to be fixed.
 *
 * Example 2:
 *
 * Input: road = "..xxxxx", budget = 4
 * Output: 3
 * Explanation: We fix the first three potholes (they are consecutive). The cost of this fix is 3 + 1 = 4.
 *
 * Example 3:
 *
 * Input: road = "x.....x", budget = 14
 * Output: 2
 * Explanation: We can fix both potholes. The cost of fixing the two potholes is 1 + 1 + 1 + 1 = 4.
 *
 *
 * Constraints:
 *
 * 1 <= road.length <= 10^5
 * 1 <= budget <= 10^5 + 1
 * road consists only of characters '.' and 'x'.
 *
 */
public class MaximumNumberOfPotholesThatCanBeFixed {

    // V0
    // IDEA: EACH REPAIR PAYS A FLAT +1 OVERHEAD - SO FIX THE LONGEST RUNS FIRST
    /**
     *  Fixing L consecutive potholes costs L + 1, i.e. the potholes plus one
     *  overhead per repair. To get the most potholes for the budget, pay as few
     *  overheads as possible: fix the longest runs first, and spend whatever
     *  is left (minus its overhead) on part of the next run.
     *
     *  time = O(N log N)
     *  space = O(N)
     */
    public int maxPotholes(String road, int budget) {
        // collect the lengths of the maximal runs of 'x'
        List<Integer> runs = new ArrayList<>();
        int current = 0;
        for (int i = 0; i < road.length(); i++) {
            if (road.charAt(i) == 'x') {
                current++;
            } else if (current > 0) {
                runs.add(current);
                current = 0;
            }
        }
        if (current > 0) {
            runs.add(current);
        }
        runs.sort(Collections.reverseOrder());

        int fixed = 0;
        int remaining = budget;
        for (int length : runs) {
            if (remaining <= 1) {
                break; // one unit only pays the overhead
            }
            if (length + 1 <= remaining) {
                remaining -= length + 1;
                fixed += length;
            } else {
                // fix part of this run with what is left
                fixed += remaining - 1;
                break;
            }
        }
        return fixed;
    }
}
