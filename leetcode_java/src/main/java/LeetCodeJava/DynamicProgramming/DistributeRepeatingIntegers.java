package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/distribute-repeating-integers/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1655. Distribute Repeating Integers
 * Hard
 *
 * You are given an array of n integers, nums, where there are at most 50 unique values in the array.
 * You are also given an array of m customer order quantities, quantity, where quantity[i] is the amount
 * of integers the ith customer ordered. Determine if it is possible to distribute nums such that:
 *
 * - The ith customer gets exactly quantity[i] integers,
 * - The integers the ith customer gets are all equal, and
 * - Every customer is satisfied.
 *
 * Return true if it is possible to distribute nums according to the above conditions.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4], quantity = [2]
 * Output: false
 * Explanation: The 0th customer cannot be given two different integers.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,3], quantity = [2]
 * Output: true
 * Explanation: The 0th customer is given [3,3]. The integers [1,2] are not used.
 *
 * Example 3:
 *
 * Input: nums = [1,1,2,2], quantity = [2,2]
 * Output: true
 * Explanation: The 0th customer is given [1,1], and the 1st customer is given [2,2].
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 10^5
 * 1 <= nums[i] <= 1000
 * m == quantity.length
 * 1 <= m <= 10
 * 1 <= quantity[i] <= 10^5
 * There are at most 50 unique values in nums.
 *
 */
public class DistributeRepeatingIntegers {

    // V0
    // IDEA: BITMASK DP OVER CUSTOMERS + SUBSET ENUMERATION
    //       only the multiplicities of the values matter. m <= 10 customers -> mask of served ones.
    //       need[mask] = total quantity the customers in mask want.
    //       processing one value with frequency f:
    //         canServe'[mask] = canServe[mask]
    //                         OR (some subset sub of mask with need[sub] <= f
    //                             and canServe[mask without sub])
    //       at most m values can ever be useful, so keep only the m largest frequencies.
    /**
     * time = O(M * 3^M)
     * space = O(2^M)
     */
    public boolean canDistribute(int[] nums, int[] quantity) {
        int m = quantity.length;
        int fullMask = (1 << m) - 1;

        // need[mask] = sum of quantity[j] for every customer j in mask
        int[] need = new int[1 << m];
        for (int mask = 1; mask <= fullMask; mask++) {
            int total = 0;
            for (int j = 0; j < m; j++) {
                if (((mask >> j) & 1) == 1) {
                    total += quantity[j];
                }
            }
            need[mask] = total;
        }

        // frequencies of each distinct value, largest first, at most m of them
        Map<Integer, Integer> frequency = new HashMap<>();
        for (int value : nums) {
            frequency.put(value, frequency.getOrDefault(value, 0) + 1);
        }
        List<Integer> counts = new ArrayList<>(frequency.values());
        counts.sort(Collections.reverseOrder());
        if (counts.size() > m) {
            counts = counts.subList(0, m);
        }

        // canServe[mask] = the values processed so far can satisfy exactly the customers in mask
        boolean[] canServe = new boolean[1 << m];
        canServe[0] = true;
        for (int available : counts) {
            boolean[] next = canServe.clone();
            for (int mask = 1; mask <= fullMask; mask++) {
                if (next[mask]) {
                    continue;
                }
                // try handing this value to every non-empty subset of mask;
                // (sub - 1) & mask steps to the next smaller subset of mask
                for (int sub = mask; sub > 0; sub = (sub - 1) & mask) {
                    if (need[sub] <= available && canServe[mask ^ sub]) {
                        next[mask] = true;
                        break;
                    }
                }
            }
            canServe = next;
        }
        return canServe[fullMask];
    }
}
