package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/heaters/

import java.util.Arrays;

/**
 *  475. Heaters
 *  Medium
 *
 *  Winter is coming! During the contest, your first job is to design a standard
 *  heater with a fixed warm radius to warm all the houses.
 *
 *  Every house can be warmed, as long as the house is within the heater's warm
 *  radius range.
 *
 *  Given the positions of houses and heaters on a horizontal line, return the
 *  minimum radius standard of heaters so that those heaters could cover all houses.
 *
 *  Notice that all the heaters follow your radius standard, and the warm radius
 *  will the same.
 *
 *  Example 1:
 *
 *  Input: houses = [1,2,3], heaters = [2]
 *  Output: 1
 *
 *  Example 2:
 *
 *  Input: houses = [1,2,3,4], heaters = [1,4]
 *  Output: 1
 *
 *  Example 3:
 *
 *  Input: houses = [1,5], heaters = [2]
 *  Output: 3
 *
 *  Constraints:
 *
 *  1 <= houses.length, heaters.length <= 3 * 10^4
 *  1 <= houses[i], heaters[i] <= 10^9
 */
public class Heaters {

    // V0
    // IDEA: sort heaters, for each house binary search its closest heater on the
    //       left and on the right, answer = max over houses of that min distance
    /**
     * time = O(m log m + n log n)
     * space = O(1)
     */
    public int findRadius(int[] houses, int[] heaters) {
        Arrays.sort(heaters);
        int n = heaters.length;
        int res = 0;
        for (int house : houses) {
            // first idx with heaters[idx] >= house
            int l = 0;
            int r = n;
            while (l < r) {
                int mid = l + (r - l) / 2;
                if (heaters[mid] >= house) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            long best = Long.MAX_VALUE;
            if (l < n) {
                best = Math.min(best, (long) heaters[l] - house);
            }
            if (l > 0) {
                best = Math.min(best, (long) house - heaters[l - 1]);
            }
            res = Math.max(res, (int) best);
        }
        return res;
    }

    // V1
    // IDEA: sort both, two pointer sweep (advance heater ptr while the next
    //       heater is not worse for the current house)
    /**
     * time = O(m log m + n log n)
     * space = O(1)
     */
    public int findRadius_1(int[] houses, int[] heaters) {
        Arrays.sort(houses);
        Arrays.sort(heaters);
        int i = 0;
        int res = 0;
        for (int house : houses) {
            while (i + 1 < heaters.length
                    && Math.abs(heaters[i + 1] - house) <= Math.abs(heaters[i] - house)) {
                i++;
            }
            res = Math.max(res, Math.abs(heaters[i] - house));
        }
        return res;
    }
}
