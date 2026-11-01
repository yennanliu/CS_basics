package LeetCodeJava.Math;

// https://leetcode.com/problems/find-array-given-subset-sums/description/

import java.util.*;

/**
 * 1982. Find Array Given Subset Sums
 * Hard
 *
 * You are given an integer n representing the length of an unknown array that you are trying to recover. You are also given an array sums containing the values of all 2^n subset sums of the unknown array (in no particular order).
 *
 * Return the array ans of length n representing the unknown array. If multiple answers exist, return any of them.
 *
 * An array sub is a subset of an array arr if sub can be obtained from arr by deleting some (possibly zero or all) elements of arr. The sum of the elements in sub is one possible subset sum of arr. The sum of an empty array is considered to be 0.
 *
 * Note: Test cases are generated such that there will always be at least one correct answer.
 *
 *
 * Example 1:
 *
 * Input: n = 3, sums = [-3,-2,-1,0,0,1,2,3]
 * Output: [1,2,-3]
 * Explanation: [1,2,-3] is able to achieve the given subset sums:
 * - []: sum is 0
 * - [1]: sum is 1
 * - [2]: sum is 2
 * - [1,2]: sum is 3
 * - [-3]: sum is -3
 * - [1,-3]: sum is -2
 * - [2,-3]: sum is -1
 * - [1,2,-3]: sum is 0
 * Note that any permutation of [1,2,-3] and also any permutation of [-1,-2,3] will also be accepted.
 *
 * Example 2:
 *
 * Input: n = 2, sums = [0,0,0,0]
 * Output: [0,0]
 * Explanation: The only correct answer is [0,0].
 *
 * Example 3:
 *
 * Input: n = 4, sums = [0,0,5,5,4,-1,4,9,9,-1,4,3,4,8,3,8]
 * Output: [0,-1,4,5]
 * Explanation: [0,-1,4,5] is able to achieve the given subset sums.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 15
 * sums.length == 2^n
 * -10^4 <= sums[i] <= 10^4
 *
 */
public class FindArrayGivenSubsetSums {

    // V0
    // IDEA: DIVIDE AND CONQUER ON THE SORTED MULTISET (peel one element at a time)
    //  sums of k elements split into A (without x) and B = A + x; d = |x| = largest - second largest.
    //  pair each smallest unused s with s + d; the half that still holds 0 (the empty subset) survives:
    //  if the shifted half holds 0, x = -d and we keep it, otherwise x = +d and we keep the low half.
    /**
     * time = O(2^N * N)
     * space = O(2^N)
     */
    public int[] recoverArray(int n, int[] sums) {
        int[] sorted = sums.clone();
        Arrays.sort(sorted);
        List<Integer> current = new ArrayList<>();
        for (int value : sorted) {
            current.add(value);
        }

        int[] answer = new int[n];
        for (int step = 0; step < n; step++) {
            int size = current.size();
            // the top sum with and without the peeled element differ by |element|
            int diff = current.get(size - 1) - current.get(size - 2);

            // remaining multiplicity of each sum
            Map<Integer, Integer> count = new HashMap<>();
            for (int value : current) {
                count.put(value, count.getOrDefault(value, 0) + 1);
            }

            List<Integer> lowHalf = new ArrayList<>();
            List<Integer> highHalf = new ArrayList<>();
            boolean zeroInHighHalf = false;

            // ascending scan: the smallest unused s must be a "without" sum, matched to s + diff
            for (int value : current) {
                if (count.get(value) == 0) {
                    continue; // already consumed as some (t + diff)
                }
                count.put(value, count.get(value) - 1);
                count.put(value + diff, count.get(value + diff) - 1);
                lowHalf.add(value);
                highHalf.add(value + diff);
                if (value + diff == 0) {
                    zeroInHighHalf = true;
                }
            }

            if (zeroInHighHalf) {
                // the leftover elements' sums are the shifted half -> the peeled element was -diff
                answer[step] = -diff;
                current = highHalf;
            } else {
                answer[step] = diff;
                current = lowHalf;
            }
        }
        return answer;
    }
}
