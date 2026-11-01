package LeetCodeJava.HashTable;

// https://leetcode.com/problems/check-if-n-and-its-double-exist/description/

import java.util.*;

/**
 * 1346. Check If N and Its Double Exist
 * Easy
 *
 * Given an array arr of integers, check if there exist two indices i and j
 * such that :
 *
 * - i != j
 * - 0 <= i, j < arr.length
 * - arr[i] == 2 * arr[j]
 *
 * Example 1:
 *
 * Input: arr = [10,2,5,3]
 * Output: true
 * Explanation: For i = 0 and j = 2, arr[i] == 10 == 2 * 5 == 2 * arr[j]
 *
 * Example 2:
 *
 * Input: arr = [3,1,7,11]
 * Output: false
 * Explanation: There is no i and j that satisfy the conditions.
 *
 * Constraints:
 *
 * 2 <= arr.length <= 500
 * -10^3 <= arr[i] <= 10^3
 *
 */
public class CheckIfNAndItsDoubleExist {

    // V0
    // IDEA: HASH SET, ONE PASS
    //   for the current x its partner is 2 * x or x / 2; if the partner came
    //   earlier it is in `seen`, and if it comes later it will find x then.
    //   - [0, 0] works: the second 0 finds 2 * 0 in the set; a single 0 does not
    //   - only halve even numbers, so 5 does not match 2 (5 / 2 == 2)
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean checkIfExist(int[] arr) {
        Set<Integer> seen = new HashSet<>();
        for (int x : arr) {
            if (seen.contains(x * 2)) {
                return true;
            }
            if (x % 2 == 0 && seen.contains(x / 2)) {
                return true;
            }
            seen.add(x);
        }
        return false;
    }
}
