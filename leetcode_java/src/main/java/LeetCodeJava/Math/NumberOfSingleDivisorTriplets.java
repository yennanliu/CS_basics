package LeetCodeJava.Math;

// https://leetcode.com/problems/number-of-single-divisor-triplets/description/

/**
 * 2198. Number of Single Divisor Triplets
 * Medium
 *
 * You are given a 0-indexed array nums consisting of positive integers. A triplet of indices (i, j, k) is a single divisor triplet of nums if nums[i] + nums[j] + nums[k] is divisible by exactly one of nums[i], nums[j], or nums[k].
 *
 * Return the number of single divisor triplets of nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,6,7,3,2]
 * Output: 12
 * Explanation:
 * The triplet (0, 3, 4) is a single divisor triplet because nums[0] + nums[3] + nums[4] = 4 + 3 + 2 = 9 and 9 is divisible by only nums[3].
 * The triplet (0, 4, 3) is a single divisor triplet because nums[0] + nums[4] + nums[3] = 4 + 2 + 3 = 9 and 9 is divisible by only nums[4].
 * The triplet (3, 0, 4), (3, 4, 0), (4, 0, 3), and (4, 3, 0) are the same as the previous two triplets.
 * The triplet (0, 2, 3) is a single divisor triplet because nums[0] + nums[2] + nums[3] = 4 + 7 + 3 = 14 and 14 is divisible by only nums[2].
 * The triplet (0, 3, 2), (2, 0, 3), (2, 3, 0), (3, 0, 2), and (3, 2, 0) are the same as the previous triplet.
 * There are 12 single divisor triplets in total.
 *
 * Example 2:
 *
 * Input: nums = [1,2,2]
 * Output: 6
 * Explanation:
 * The triplet (0, 1, 2) is a single divisor triplet because nums[0] + nums[1] + nums[2] = 1 + 2 + 2 = 5 and 5 is divisible by only nums[0].
 * The triplet (0, 2, 1), (1, 0, 2), (1, 2, 0), (2, 0, 1), and (2, 1, 0) are the same as the previous triplet.
 * There are 6 single divisor triplets in total.
 *
 * Example 3:
 *
 * Input: nums = [1,1,1]
 * Output: 0
 * Explanation:
 * There are no single divisor triplets.
 * Note that (0, 1, 2) is not a single divisor triplet because nums[0] + nums[1] + nums[2] = 3 and 3 is divisible by nums[0], nums[1], and nums[2].
 *
 *
 * Constraints:
 *
 * 3 <= nums.length <= 10^5
 * 1 <= nums[i] <= 100
 *
 */
public class NumberOfSingleDivisorTriplets {

    // V0
    // IDEA: VALUES ARE <= 100 - ENUMERATE VALUE TRIPLES a <= b <= c, NOT INDEX TRIPLES
    //   for each value multiset whose sum is divisible by exactly one of a, b, c,
    //   add how many ORDERED index triples realise it.
    /**
     * time = O(n + 100^3)
     * space = O(100)
     */
    public long singleDivisorTriplet(int[] nums) {
        final int MAX_VALUE = 100;

        // count how many times each value appears
        long[] count = new long[MAX_VALUE + 1];
        for (int num : nums) {
            count[num]++;
        }

        long triplets = 0;
        for (int a = 1; a <= MAX_VALUE; a++) {
            if (count[a] == 0) {
                continue;
            }
            for (int b = a; b <= MAX_VALUE; b++) {
                if (count[b] == 0) {
                    continue;
                }
                for (int c = b; c <= MAX_VALUE; c++) {
                    if (count[c] == 0) {
                        continue;
                    }

                    // how many of a, b, c divide the sum
                    int sum = a + b + c;
                    int divisorCount = 0;
                    if (sum % a == 0) {
                        divisorCount++;
                    }
                    if (sum % b == 0) {
                        divisorCount++;
                    }
                    if (sum % c == 0) {
                        divisorCount++;
                    }
                    if (divisorCount != 1) {
                        continue;
                    }

                    // number of ordered index triples realising the multiset {a, b, c}
                    if (a == b && b == c) {
                        triplets += count[a] * (count[a] - 1) * (count[a] - 2);
                    } else if (a == b) {
                        triplets += 3 * count[a] * (count[a] - 1) * count[c];
                    } else if (b == c) {
                        triplets += 3 * count[b] * (count[b] - 1) * count[a];
                    } else {
                        triplets += 6 * count[a] * count[b] * count[c];
                    }
                }
            }
        }
        return triplets;
    }
}
