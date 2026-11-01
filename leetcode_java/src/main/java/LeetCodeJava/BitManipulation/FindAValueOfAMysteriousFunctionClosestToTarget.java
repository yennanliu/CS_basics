package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/find-a-value-of-a-mysterious-function-closest-to-target/description/

import java.util.HashSet;
import java.util.Set;

/**
 * 1521. Find a Value of a Mysterious Function Closest to Target
 * Hard
 *
 * Winston was given a mysterious function func. He has an integer array arr and an integer target and he wants to find the values l and r that make the value |func(arr, l, r) - target| minimum possible.
 *
 * func(arr, l, r) is the bitwise AND of arr[l], arr[l+1], ..., arr[r].
 *
 * Return the minimum possible value of |func(arr, l, r) - target|.
 *
 * Notice that func should be called with the values l and r where 0 <= l, r < arr.length.
 *
 *
 * Example 1:
 *
 * Input: arr = [9,12,3,7,15], target = 5
 * Output: 2
 * Explanation: Calling func with all the pairs of [l,r] = [[0,0],[1,1],[2,2],[3,3],[4,4],[0,1],[1,2],[2,3],[3,4],[0,2],[1,3],[2,4],[0,3],[1,4],[0,4]], Winston got the following results [9,12,3,7,15,8,0,3,7,0,0,3,0,0,0]. The value closest to 5 is 7 and 3, thus the minimum difference is 2.
 *
 * Example 2:
 *
 * Input: arr = [1000000,1000000,1000000], target = 1
 * Output: 999999
 * Explanation: Winston called the func with all possible values of [l,r] and he always got 1000000, thus the min difference is 999999.
 *
 * Example 3:
 *
 * Input: arr = [1,2,4,8,16], target = 0
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 10^5
 * 1 <= arr[i] <= 10^6
 * 0 <= target <= 10^7
 *
 */
public class FindAValueOfAMysteriousFunctionClosestToTarget {

    // V0
    // IDEA: SET OF DISTINCT AND-VALUES ENDING AT EACH INDEX
    //       fix the right end r. as the left end moves left, arr[l] & .. & arr[r]
    //       only ever clears bits, so it changes at most ~20 times
    //       (arr[i] <= 10^6 < 2^20) -> at most ~20 distinct values per r.
    //       roll that small set forward:
    //         ands(r) = { x & arr[r] : x in ands(r-1) } plus { arr[r] }
    //       and check every member against target.
    //       (brute force over all pairs is O(N^2) and times out.)
    /**
     * time = O(N log M)   where M = max(arr)
     * space = O(log M)
     */
    public int closestToTarget(int[] arr, int target) {
        int best = Math.abs(arr[0] - target);
        Set<Integer> endingHere = new HashSet<>();

        for (int value : arr) {
            // every AND of a subarray ending at this index
            Set<Integer> next = new HashSet<>();
            next.add(value);
            for (int previous : endingHere) {
                next.add(previous & value);
            }
            endingHere = next;

            for (int candidate : endingHere) {
                best = Math.min(best, Math.abs(candidate - target));
            }
            if (best == 0) {
                return 0;
            }
        }
        return best;
    }
}
