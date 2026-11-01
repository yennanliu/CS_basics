package LeetCodeJava.Array;

// https://leetcode.com/problems/array-transformation/description/

import java.util.*;

/**
 * 1243. Array Transformation
 * Easy
 *
 * Given an initial array arr, every day you produce a new array using the array of the previous day.
 *
 * On the i-th day, you do the following operations on the array of day i-1 to produce the array of day i:
 *
 * 1. If an element is smaller than both its left neighbor and its right neighbor, then this element is incremented.
 * 2. If an element is bigger than both its left neighbor and its right neighbor, then this element is decremented.
 * 3. The first and last elements never change.
 *
 * After some days, the array does not change. Return that final array.
 *
 * Example 1:
 *
 * Input: arr = [6,2,3,4]
 * Output: [6,3,3,4]
 * Explanation:
 * On the first day, the array is changed from [6,2,3,4] to [6,3,3,4].
 * No more operations can be done to this array.
 *
 * Example 2:
 *
 * Input: arr = [1,6,3,4,3,5]
 * Output: [1,4,4,4,4,5]
 * Explanation:
 * On the first day, the array is changed from [1,6,3,4,3,5] to [1,5,4,3,4,5].
 * On the second day, the array is changed from [1,5,4,3,4,5] to [1,4,4,4,4,5].
 * No more operations can be done to this array.
 *
 * Constraints:
 *
 * 3 <= arr.length <= 100
 * 1 <= arr[i] <= 100
 *
 */
public class ArrayTransformation {

    // V0
    // IDEA: SIMULATION DAY BY DAY UNTIL NOTHING CHANGES
    //   every cell of one day updates SIMULTANEOUSLY, so compare against a
    //   snapshot of the previous day, not the array being modified.
    /**
     * time = O(N * M), M = max value (each day moves some value one step closer to its neighbours)
     * space = O(N)
     */
    public List<Integer> transformArray(int[] arr) {
        int[] current = arr.clone();
        boolean changed = true;

        while (changed) {
            changed = false;
            int[] previousDay = current.clone();
            // the first and last elements never change
            for (int i = 1; i < previousDay.length - 1; i++) {
                boolean isPeak = previousDay[i] > previousDay[i - 1] && previousDay[i] > previousDay[i + 1];
                boolean isValley = previousDay[i] < previousDay[i - 1] && previousDay[i] < previousDay[i + 1];
                if (isPeak) {
                    current[i]--;
                    changed = true;
                } else if (isValley) {
                    current[i]++;
                    changed = true;
                }
            }
        }

        List<Integer> result = new ArrayList<>();
        for (int value : current) {
            result.add(value);
        }
        return result;
    }
}
