package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-split-of-positive-even-integers/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 2178. Maximum Split of Positive Even Integers
 * Medium
 *
 * You are given an integer finalSum. Split it into a sum of a maximum number of unique positive even
 * integers.
 *
 * For example, given finalSum = 12, the following splits are valid (unique positive even integers summing up
 * to finalSum): (12), (2 + 10), (2 + 4 + 6), and (4 + 8). Among them, (2 + 4 + 6) contains the maximum
 * number of integers. Note that finalSum cannot be split into (2 + 2 + 4 + 4) as all the numbers should be
 * unique.
 *
 * Return a list of integers that represent a valid split containing a maximum number of integers. If no
 * valid split exists for finalSum, return an empty list. You may return the integers in any order.
 *
 *
 * Example 1:
 *
 * Input: finalSum = 12
 * Output: [2,4,6]
 *
 * Example 2:
 *
 * Input: finalSum = 7
 * Output: []
 *
 * Example 3:
 *
 * Input: finalSum = 28
 * Output: [6,8,2,12]
 *
 *
 * Constraints:
 *
 * 1 <= finalSum <= 10^10
 *
 */
public class MaximumSplitOfPositiveEvenIntegers {

    // V0
    // IDEA: GREEDY - TAKE 2, 4, 6, ... WHILE THE REMAINDER COVERS THE NEXT TERM; ADD THE LEFTOVER TO THE LAST
    /**
     * time = O(sqrt(finalSum))
     * space = O(sqrt(finalSum)) for the output
     */
    public List<Long> maximumEvenSplit(long finalSum) {
        List<Long> parts = new ArrayList<>();

        // an odd sum can never be made of even numbers
        if (finalSum % 2 == 1) {
            return parts;
        }

        // any split into k distinct evens sums to at least 2 + 4 + ... + 2k, so smallest-first maximises k
        long remaining = finalSum;
        long next = 2;
        while (next <= remaining) {
            parts.add(next);
            remaining -= next;
            next += 2;
        }

        // the leftover is even and smaller than `next`; adding it to the last (largest) term keeps all unique
        if (remaining > 0) {
            int last = parts.size() - 1;
            parts.set(last, parts.get(last) + remaining);
        }
        return parts;
    }
}
