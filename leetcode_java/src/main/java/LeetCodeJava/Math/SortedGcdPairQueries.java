package LeetCodeJava.Math;

// https://leetcode.com/problems/sorted-gcd-pair-queries/description/

/**
 * 3312. Sorted GCD Pair Queries
 * Hard
 *
 * You are given an integer array nums of length n and an integer array queries.
 *
 * Let gcdPairs denote an array obtained by calculating GCD(nums[i], nums[j]) for all 0 <= i < j < n,
 * and then sorting these values in ascending order.
 *
 * For each query queries[i], you need to find the element at index queries[i] in gcdPairs.
 *
 * Return an integer array answer, where answer[i] is the value at gcdPairs[queries[i]] for each query.
 *
 * The term gcd(a, b) denotes the greatest common divisor of a and b.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,4], queries = [0,2,2]
 * Output: [1,2,2]
 * Explanation:
 * gcdPairs = [gcd(nums[0], nums[1]), gcd(nums[0], nums[2]), gcd(nums[1], nums[2])] = [1, 2, 1].
 * After sorting in ascending order, gcdPairs = [1, 1, 2].
 * So, the answer is [gcdPairs[0], gcdPairs[2], gcdPairs[2]] = [1, 2, 2].
 *
 * Example 2:
 *
 * Input: nums = [4,4,2,1], queries = [5,3,1,0]
 * Output: [4,2,1,1]
 * Explanation:
 * gcdPairs sorted in ascending order is [1, 1, 1, 2, 2, 4].
 *
 * Example 3:
 *
 * Input: nums = [2,2], queries = [0,0]
 * Output: [2,2]
 * Explanation:
 * gcdPairs = [2].
 *
 *
 * Constraints:
 *
 * 2 <= n == nums.length <= 10^5
 * 1 <= nums[i] <= 5 * 10^4
 * 1 <= queries.length <= 10^5
 * 0 <= queries[i] < n * (n - 1) / 2
 *
 */
public class SortedGcdPairQueries {

    // V0
    // IDEA: COUNT PAIRS PER GCD WITH A DIVISOR SIEVE (INCLUSION-EXCLUSION FROM LARGE d DOWN),
    //   PREFIX-SUM THE COUNTS, THEN BINARY SEARCH EACH QUERY
    //   exact[d] = C(multiplesOf[d], 2) - sum over multiples e > d of exact[e]
    /**
     * time = O(V log V + Q log V)
     * space = O(V)
     */
    public int[] gcdValues(int[] nums, long[] queries) {
        int maxValue = 0;
        for (int value : nums) {
            maxValue = Math.max(maxValue, value);
        }

        int[] frequency = new int[maxValue + 1];
        for (int value : nums) {
            frequency[value]++;
        }

        // multiplesOf[d] = how many elements are divisible by d
        long[] multiplesOf = new long[maxValue + 1];
        for (int d = 1; d <= maxValue; d++) {
            for (int multiple = d; multiple <= maxValue; multiple += d) {
                multiplesOf[d] += frequency[multiple];
            }
        }

        // exact[d] = pairs whose gcd is exactly d
        // C(multiplesOf[d], 2) counts pairs with gcd a multiple of d; subtract the larger multiples
        long[] exact = new long[maxValue + 1];
        for (int d = maxValue; d >= 1; d--) {
            long pairs = multiplesOf[d] * (multiplesOf[d] - 1) / 2;
            for (int multiple = 2 * d; multiple <= maxValue; multiple += d) {
                pairs -= exact[multiple];
            }
            exact[d] = pairs;
        }

        // pairsUpTo[d] = how many pairs have gcd <= d (non-decreasing in d)
        long[] pairsUpTo = new long[maxValue + 1];
        for (int d = 1; d <= maxValue; d++) {
            pairsUpTo[d] = pairsUpTo[d - 1] + exact[d];
        }

        int[] answer = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            answer[i] = firstGreater(pairsUpTo, queries[i]);
        }
        return answer;
    }

    /** Smallest d with pairsUpTo[d] > target: the gcd sitting at sorted index `target`. */
    private int firstGreater(long[] pairsUpTo, long target) {
        int left = 1;
        int right = pairsUpTo.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (pairsUpTo[mid] > target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}
