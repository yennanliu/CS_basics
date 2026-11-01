package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/count-triplets-that-can-form-two-arrays-of-equal-xor/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 1442. Count Triplets That Can Form Two Arrays of Equal XOR
 * Medium
 *
 * Given an array of integers arr.
 *
 * We want to select three indices i, j and k where (0 <= i < j <= k < arr.length).
 *
 * Let's define a and b as follows:
 *
 * a = arr[i] ^ arr[i + 1] ^ ... ^ arr[j - 1]
 * b = arr[j] ^ arr[j + 1] ^ ... ^ arr[k]
 *
 * Note that ^ denotes the bitwise-xor operation.
 *
 * Return the number of triplets (i, j and k) Where a == b.
 *
 *
 * Example 1:
 *
 * Input: arr = [2,3,1,6,7]
 * Output: 4
 * Explanation: The triplets are (0,1,2), (0,2,2), (2,3,4) and (2,4,4)
 *
 * Example 2:
 *
 * Input: arr = [1,1,1,1,1]
 * Output: 10
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 300
 * 1 <= arr[i] <= 10^8
 *
 */
public class CountTripletsThatCanFormTwoArraysOfEqualXor {

    // V0
    // IDEA: XOR OF THE WHOLE RANGE
    //   a == b  <=>  a ^ b == 0  <=>  arr[i] ^ ... ^ arr[k] == 0.
    //   j drops out of the condition: once [i, k] xors to 0, ANY j in (i, k] works -> k - i triplets.
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public int countTriplets(int[] arr) {
        int n = arr.length;
        int count = 0;
        for (int i = 0; i < n; i++) {
            int rangeXor = arr[i];
            for (int k = i + 1; k < n; k++) {
                rangeXor ^= arr[k];
                if (rangeXor == 0) {
                    count += k - i;
                }
            }
        }
        return count;
    }

    // V1
    // IDEA: HASH MAP ON PREFIX XOR
    //   prefix[x] = arr[0] ^ ... ^ arr[x-1]; xor(i..k) == 0 <=> prefix[i] == prefix[k+1].
    //   for right end r = k + 1, each earlier i with the same prefix adds (r - i - 1) triplets,
    //   so per prefix value keep the COUNT of indices and the SUM of indices:
    //   contribution = count * (r - 1) - indexSum.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int countTriplets_1(int[] arr) {
        Map<Integer, Integer> count = new HashMap<>();
        Map<Integer, Integer> indexSum = new HashMap<>();
        // prefix xor 0 occurs at index 0
        count.put(0, 1);
        indexSum.put(0, 0);

        int triplets = 0;
        int prefix = 0;
        for (int r = 1; r <= arr.length; r++) {
            prefix ^= arr[r - 1];
            int seen = count.getOrDefault(prefix, 0);
            int seenIndexSum = indexSum.getOrDefault(prefix, 0);
            triplets += seen * (r - 1) - seenIndexSum;

            count.put(prefix, seen + 1);
            indexSum.put(prefix, seenIndexSum + r);
        }
        return triplets;
    }
}
