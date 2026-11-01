package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-operations-to-make-a-subsequence/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1713. Minimum Operations to Make a Subsequence
 * Hard
 *
 * You are given an array target that consists of distinct integers and another integer array arr that
 * can have duplicates.
 *
 * In one operation, you can insert any integer at any position in arr. For example, if arr = [1,4,1,2],
 * you can add 3 in the middle and make it [1,4,3,1,2]. Note that you can insert the integer at the very
 * beginning or end of the array.
 *
 * Return the minimum number of operations needed to make target a subsequence of arr.
 *
 * A subsequence of an array is a new array generated from the original array by deleting some elements
 * (possibly none) without changing the remaining elements' relative order. For example, [2,7,4] is a
 * subsequence of [4,2,3,7,2,1,4] (the underlined elements), while [2,4,2] is not.
 *
 *
 * Example 1:
 *
 * Input: target = [5,1,3], arr = [9,4,2,3,4]
 * Output: 2
 * Explanation: You can add 5 and 1 in such a way that makes arr = [5,9,4,1,2,3,4], then target will be
 * a subsequence of arr.
 *
 * Example 2:
 *
 * Input: target = [6,4,8,1,3,2], arr = [4,7,6,2,3,8,6,1]
 * Output: 3
 *
 *
 * Constraints:
 *
 * 1 <= target.length, arr.length <= 10^5
 * 1 <= target[i], arr[i] <= 10^9
 * target contains no duplicates.
 *
 */
public class MinimumOperationsToMakeASubsequence {

    // V0
    // IDEA: LCS -> LIS (target IS DISTINCT, SO MAP VALUES TO INDICES) + BINARY SEARCH
    /**
     *  answer = target.length - LCS(target, arr): what the two already share in order stays,
     *  everything else in target must be inserted.
     *
     *  Plain LCS is O(M * N) = 10^10. Because target has no duplicates each value has a unique
     *  position; rewrite arr as the target positions of its values (skipping values not in
     *  target). A common subsequence is then exactly a strictly increasing subsequence of that
     *  list, so LCS == LIS, which runs in O(N log M) with the patience tails list.
     */
    /**
     * time = O(N log M)
     * space = O(M + N)
     */
    public int minOperations(int[] target, int[] arr) {
        // value -> its index in target
        Map<Integer, Integer> positionInTarget = new HashMap<>();
        for (int i = 0; i < target.length; i++) {
            positionInTarget.put(target[i], i);
        }

        // tails.get(L) = smallest tail of a strictly increasing subsequence of length L + 1
        List<Integer> tails = new ArrayList<>();
        for (int value : arr) {
            Integer position = positionInTarget.get(value);
            if (position == null) {
                // not in target: can never be part of the common subsequence
                continue;
            }
            // lower bound (first tail >= position) keeps the sequence strictly increasing
            int left = 0;
            int right = tails.size();
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails.get(mid) >= position) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            if (left == tails.size()) {
                tails.add(position);
            } else {
                tails.set(left, position);
            }
        }
        return target.length - tails.size();
    }
}
