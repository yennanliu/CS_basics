package LeetCodeJava.Sort;

// https://leetcode.com/problems/can-make-arithmetic-progression-from-sequence/description/

import java.util.*;

/**
 * 1502. Can Make Arithmetic Progression From Sequence
 * Easy
 *
 * A sequence of numbers is called an arithmetic progression if the difference between any
 * two consecutive elements is the same.
 *
 * Given an array of numbers arr, return true if the array can be rearranged to form an
 * arithmetic progression. Otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: arr = [3,5,1]
 * Output: true
 * Explanation: We can reorder the elements as [1,3,5] or [5,3,1] with differences 2 and -2
 * respectively, between each consecutive elements.
 *
 * Example 2:
 *
 * Input: arr = [1,2,4]
 * Output: false
 * Explanation: There is no way to reorder the elements to obtain an arithmetic progression.
 *
 *
 * Constraints:
 *
 * 2 <= arr.length <= 1000
 * -10^6 <= arr[i] <= 10^6
 *
 */
public class CanMakeArithmeticProgressionFromSequence {

    // V0
    // IDEA: SORT + CHECK ADJACENT DIFF — an AP, if one exists, is the sorted order
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public boolean canMakeArithmeticProgression(int[] arr) {
        int[] sorted = arr.clone();
        Arrays.sort(sorted);

        int step = sorted[1] - sorted[0];
        for (int i = 2; i < sorted.length; i++) {
            if (sorted[i] - sorted[i - 1] != step) {
                return false;
            }
        }
        return true;
    }

    // V1
    // IDEA: MATH + SET (no sorting) — the common difference must be (max - min) / (n - 1);
    //       then every term min + i * d must be present, and (for d != 0) no duplicates
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean canMakeArithmeticProgression_1(int[] arr) {
        int n = arr.length;
        int lowest = Integer.MAX_VALUE;
        int highest = Integer.MIN_VALUE;
        for (int value : arr) {
            lowest = Math.min(lowest, value);
            highest = Math.max(highest, value);
        }

        // all values equal -> an AP with difference 0
        if (lowest == highest) {
            return true;
        }
        // the span must split evenly into n - 1 steps
        if ((highest - lowest) % (n - 1) != 0) {
            return false;
        }
        int step = (highest - lowest) / (n - 1);

        Set<Integer> seen = new HashSet<>();
        for (int value : arr) {
            seen.add(value);
        }
        // with step != 0 every term is distinct, so a duplicate breaks the progression
        if (seen.size() != n) {
            return false;
        }

        for (int i = 0; i < n; i++) {
            if (!seen.contains(lowest + i * step)) {
                return false;
            }
        }
        return true;
    }
}
