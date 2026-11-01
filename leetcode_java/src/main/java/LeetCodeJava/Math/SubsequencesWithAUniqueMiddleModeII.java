package LeetCodeJava.Math;

// https://leetcode.com/problems/subsequences-with-a-unique-middle-mode-ii/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 3416. Subsequences with a Unique Middle Mode II
 * Hard
 *
 * Given an integer array nums, find the number of subsequences of size 5 of nums with a unique middle
 * mode.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * A mode of a sequence of numbers is defined as the element that appears the maximum number of times in
 * the sequence.
 *
 * A sequence of numbers contains a unique mode if it has only one mode.
 *
 * A sequence of numbers seq of size 5 contains a unique middle mode if the middle element (seq[2]) is a
 * unique mode.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,1,1,1,1]
 * Output: 6
 * Explanation:
 * [1, 1, 1, 1, 1] is the only subsequence of size 5 that can be formed, and it has a unique middle mode
 * of 1. This subsequence can be formed in 6 different ways, so the output is 6.
 *
 * Example 2:
 *
 * Input: nums = [1,2,2,3,3,4]
 * Output: 4
 * Explanation:
 * [1, 2, 2, 3, 4] and [1, 2, 3, 3, 4] each have a unique middle mode because the number at index 2 has
 * the greatest frequency in the subsequence. [1, 2, 2, 3, 3] does not have a unique middle mode because
 * 2 and 3 appear twice.
 *
 * Example 3:
 *
 * Input: nums = [0,1,2,3,4,5,6,7,8]
 * Output: 0
 * Explanation:
 * There is no subsequence of length 5 with a unique middle mode.
 *
 *
 * Constraints:
 *
 * 5 <= nums.length <= 10^5
 * -10^9 <= nums[i] <= 10^9
 *
 */
public class SubsequencesWithAUniqueMiddleModeII {

    private static final long MOD = 1_000_000_007L;

    // per-value counts and the five running totals over all distinct values y:
    //   P = sum l*r,  Q = sum l*r^2,  W = sum l^2*r,  T = sum C(r,2),  U = sum C(l,2)
    // each stays below ~4 * 10^13, so they are kept exactly in long
    private long[] leftCount;
    private long[] rightCount;
    private long sumLR;
    private long sumLRR;
    private long sumLLR;
    private long sumPairsRight;
    private long sumPairsLeft;

    // V0
    // IDEA: SAME COUNTING AS PART I (LC 3395), BUT THE PER-y CORRECTION IS A SUM OF SEPARABLE TERMS,
    //   SO FIVE RUNNING TOTALS (P, Q, W, T, U) REPLACE THE SCAN OVER y. MOVING THE MIDDLE ONE STEP
    //   CHANGES ONLY TWO VALUES' COUNTS, SO EACH TOTAL IS REPAIRED IN O(1).
    //   correction = leftX * (rightOther*P - Q + leftOther*T) + rightX * (leftOther*P - W + rightOther*U),
    //   minus the y == x share, which the totals include and the formula must not.
    //   all products are reduced mod 1e9+7 (the raw values overflow long for n = 10^5).
    /**
     * time = O(N)
     * space = O(N)
     */
    public int subsequencesWithMiddleMode(int[] nums) {
        int n = nums.length;

        // compress values to ids 0 .. distinct-1
        Map<Integer, Integer> idOf = new HashMap<>();
        int[] ids = new int[n];
        for (int i = 0; i < n; i++) {
            if (!idOf.containsKey(nums[i])) {
                idOf.put(nums[i], idOf.size());
            }
            ids[i] = idOf.get(nums[i]);
        }
        int distinct = idOf.size();

        leftCount = new long[distinct];
        rightCount = new long[distinct];
        sumLR = 0;
        sumLRR = 0;
        sumLLR = 0;
        sumPairsRight = 0;
        sumPairsLeft = 0;

        // everything starts on the right; with l = 0 only T is non-zero
        for (int id : ids) {
            rightCount[id]++;
        }
        for (int v = 0; v < distinct; v++) {
            sumPairsRight += choose2(rightCount[v]);
        }
        // index 0 is the first middle, so it leaves the right side
        shift(ids[0], 0, -1);

        long answer = 0;
        for (int i = 0; i < n; i++) {
            int x = ids[i];
            long leftSize = i;
            long rightSize = n - 1 - i;
            long leftX = leftCount[x];
            long rightX = rightCount[x];
            long leftOther = leftSize - leftX;
            long rightOther = rightSize - rightX;

            long allPicks = mul(choose2(leftSize), choose2(rightSize));
            long zeroExtra = mul(choose2(leftOther), choose2(rightOther));
            long oneExtra = (mul(mul(leftX, leftOther), choose2(rightOther))
                    + mul(mul(choose2(leftOther), rightX), rightOther)) % MOD;
            long twoOrMoreExtra = mod(allPicks - zeroExtra - oneExtra);

            long repeatedOther = 0;
            // the one-extra bucket is non-empty when either of its two products has all factors positive
            // (tested on the factors, because the raw product overflows long)
            boolean extraFromLeft = leftX > 0 && leftOther > 0 && rightOther >= 2;
            boolean extraFromRight = rightX > 0 && rightOther > 0 && leftOther >= 2;
            if (extraFromLeft || extraFromRight) {
                // extra x on the left: sum over y of (l_y * r_y * (rightOther - r_y) + leftOther * C(r_y, 2))
                long leftSide = mul(rightOther, sumLR) - sumLRR % MOD + mul(leftOther, sumPairsRight);
                // ... minus the y == x term
                leftSide -= mul(mul(leftX, rightX), rightOther - rightX) + mul(leftOther, choose2(rightX));
                // extra x on the right: the mirror image
                long rightSide = mul(leftOther, sumLR) - sumLLR % MOD + mul(rightOther, sumPairsLeft);
                rightSide -= mul(mul(rightX, leftX), leftOther - leftX) + mul(rightOther, choose2(leftX));
                repeatedOther = mod(mul(leftX, mod(leftSide)) + mul(rightX, mod(rightSide)));
            }

            answer = mod(answer + twoOrMoreExtra + oneExtra - repeatedOther);

            // slide the middle one step right: x joins the left, the next element leaves the right
            if (i + 1 < n) {
                shift(x, 1, 0);
                shift(ids[i + 1], 0, -1);
            }
        }
        return (int) answer;
    }

    /** Moves value v's counts by (deltaLeft, deltaRight), repairing all five running totals. */
    private void shift(int v, int deltaLeft, int deltaRight) {
        removeContribution(v);
        leftCount[v] += deltaLeft;
        rightCount[v] += deltaRight;
        addContribution(v, 1);
    }

    /** Subtracts value v's current terms from the running totals. */
    private void removeContribution(int v) {
        addContribution(v, -1);
    }

    /** Adds sign * (value v's current terms) to the running totals. */
    private void addContribution(int v, int sign) {
        long l = leftCount[v];
        long r = rightCount[v];
        sumLR += sign * (l * r);
        sumLRR += sign * (l * r * r);
        sumLLR += sign * (l * l * r);
        sumPairsRight += sign * choose2(r);
        sumPairsLeft += sign * choose2(l);
    }

    /** C(count, 2), exact. */
    private long choose2(long count) {
        return count * (count - 1) / 2;
    }

    /** (a mod MOD) * (b mod MOD) mod MOD, for non-negative a and b. */
    private long mul(long a, long b) {
        return (a % MOD) * (b % MOD) % MOD;
    }

    /** Normalises any long into [0, MOD). */
    private long mod(long value) {
        long reduced = value % MOD;
        if (reduced < 0) {
            reduced += MOD;
        }
        return reduced;
    }
}
