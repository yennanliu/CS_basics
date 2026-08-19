package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-units-on-a-truck/

import java.util.*;

/**
 *  1710. Maximum Units on a Truck
 *  Easy
 *
 *  You are given a 2D array boxTypes, where
 *  boxTypes[i] = [numberOfBoxes_i, numberOfUnitsPerBox_i], and an integer
 *  truckSize (max number of boxes that can be put on the truck).
 *
 *  Return the maximum total number of units that can be put on the truck.
 *
 *  Example 1:
 *  Input: boxTypes = [[1,3],[2,2],[3,1]], truckSize = 4
 *  Output: 8    (1 * 3) + (2 * 2) + (1 * 1)
 *
 *  Example 2:
 *  Input: boxTypes = [[5,10],[2,5],[4,7],[3,9]], truckSize = 10
 *  Output: 91
 *
 *  Constraints:
 *   - 1 <= boxTypes.length <= 1000
 *   - 1 <= numberOfBoxes_i, numberOfUnitsPerBox_i <= 1000
 *   - 1 <= truckSize <= 10^6
 */
public class MaximumUnitsOnATruck {

    // V0
    // IDEA: GREEDY -> sort by "units per box" DESC, take as many as possible
    /**
     * time = O(n log n)
     * space = O(1) (in place sort)
     */
    public int maximumUnits(int[][] boxTypes, int truckSize) {

        if (boxTypes == null || boxTypes.length == 0 || truckSize <= 0) {
            return 0;
        }

        Arrays.sort(boxTypes, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(b[1], a[1]);
            }
        });

        int res = 0;

        for (int[] box : boxTypes) {
            if (truckSize <= 0) {
                break;
            }
            int take = Math.min(truckSize, box[0]);
            res += take * box[1];
            truckSize -= take;
        }

        return res;
    }

    // V1
    // IDEA: COUNTING SORT on units (1 <= units <= 1000) -> O(n) after bucketing
    /**
     * time = O(n + U)   # U = 1000 (max units per box)
     * space = O(U)
     */
    public int maximumUnits_1(int[][] boxTypes, int truckSize) {

        if (boxTypes == null || boxTypes.length == 0 || truckSize <= 0) {
            return 0;
        }

        int maxUnit = 1000;
        long[] cnt = new long[maxUnit + 1]; // cnt[u] = number of boxes with u units

        for (int[] box : boxTypes) {
            cnt[box[1]] += box[0];
        }

        int res = 0;

        for (int u = maxUnit; u >= 1 && truckSize > 0; u--) {
            if (cnt[u] == 0) {
                continue;
            }
            long take = Math.min((long) truckSize, cnt[u]);
            res += (int) (take * u);
            truckSize -= (int) take;
        }

        return res;
    }
}
