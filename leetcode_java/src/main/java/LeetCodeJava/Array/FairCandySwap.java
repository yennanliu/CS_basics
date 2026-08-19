package LeetCodeJava.Array;

// https://leetcode.com/problems/fair-candy-swap/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *  888. Fair Candy Swap
 *  Easy
 *
 *  Alice and Bob have a different total number of candies. You are given two
 *  integer arrays aliceSizes and bobSizes where aliceSizes[i] is the number of
 *  candies of the ith box of candy that Alice has and bobSizes[j] is the number
 *  of candies of the jth box of candy that Bob has.
 *
 *  Since they are friends, they would like to exchange one candy box each so that
 *  after the exchange, they both have the same total amount of candy. The total
 *  amount of candy a person has is the sum of the number of candies in each box
 *  they have.
 *
 *  Return an integer array answer where answer[0] is the number of candies in the
 *  box that Alice must exchange, and answer[1] is the number of candies in the
 *  box that Bob must exchange. If there are multiple answers, you may return any
 *  one of them. It is guaranteed that at least one answer exists.
 *
 *  Example 1:
 *  Input: aliceSizes = [1,1], bobSizes = [2,2]
 *  Output: [1,2]
 *
 *  Example 2:
 *  Input: aliceSizes = [1,2,5], bobSizes = [2,4]
 *  Output: [5,4]
 *
 *  Constraints:
 *   - 1 <= aliceSizes.length, bobSizes.length <= 10^4
 *   - 1 <= aliceSizes[i], bobSizes[j] <= 10^5
 *   - Alice and Bob have a different total number of candies.
 *   - There will be at least one valid answer for the given input.
 */
public class FairCandySwap {

    // V0
    // IDEA: math + hash set.
    //   sumA - a + b == sumB - b + a  =>  b = a + (sumB - sumA) / 2
    //   so for each a in alice, check whether the matching b exists in bob's set.
    /**
     * time = O(n + m)
     * space = O(m)
     */
    public int[] fairCandySwap(int[] aliceSizes, int[] bobSizes) {
        int sumA = 0;
        int sumB = 0;
        for (int a : aliceSizes) {
            sumA += a;
        }
        for (int b : bobSizes) {
            sumB += b;
        }
        int delta = (sumB - sumA) / 2;

        Set<Integer> bobSet = new HashSet<>();
        for (int b : bobSizes) {
            bobSet.add(b);
        }
        for (int a : aliceSizes) {
            if (bobSet.contains(a + delta)) {
                return new int[]{a, a + delta};
            }
        }
        return new int[]{}; // unreachable, a valid answer is guaranteed
    }

    // V1
    // IDEA: SORT + BINARY SEARCH — same b = a + (sumB - sumA) / 2 identity, but the
    //       lookup is done on a sorted copy of bob's boxes instead of a hash set,
    //       trading O(m) memory for O(log m) probes.
    /**
     * time = O(n log n + m log m)
     * space = O(m)
     */
    public int[] fairCandySwap_1(int[] aliceSizes, int[] bobSizes) {
        int sumA = 0;
        int sumB = 0;
        for (int a : aliceSizes) {
            sumA += a;
        }
        for (int b : bobSizes) {
            sumB += b;
        }
        int delta = (sumB - sumA) / 2;

        int[] sortedB = bobSizes.clone();
        Arrays.sort(sortedB);
        for (int a : aliceSizes) {
            if (Arrays.binarySearch(sortedB, a + delta) >= 0) {
                return new int[]{a, a + delta};
            }
        }
        return new int[]{}; // unreachable, a valid answer is guaranteed
    }

    // V2
    // IDEA: brute force O(n*m) — try every (a, b) pair and test the swap directly;
    //       kept as a readable correctness reference for the math above.
    /**
     * time = O(n * m)
     * space = O(1)
     */
    public int[] fairCandySwap_2(int[] aliceSizes, int[] bobSizes) {
        int sumA = 0;
        int sumB = 0;
        for (int a : aliceSizes) {
            sumA += a;
        }
        for (int b : bobSizes) {
            sumB += b;
        }

        for (int a : aliceSizes) {
            for (int b : bobSizes) {
                if (sumA - a + b == sumB - b + a) {
                    return new int[]{a, b};
                }
            }
        }
        return new int[]{}; // unreachable, a valid answer is guaranteed
    }
}
