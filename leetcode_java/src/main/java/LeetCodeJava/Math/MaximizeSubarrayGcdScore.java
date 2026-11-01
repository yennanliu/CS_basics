package LeetCodeJava.Math;

// https://leetcode.com/problems/maximize-subarray-gcd-score/description/

/**
 * 3574. Maximize Subarray GCD Score
 * Hard
 *
 * You are given an array of positive integers nums and an integer k.
 *
 * You may perform at most k operations. In each operation, you can choose one element in the array and double its value. Each element can be doubled at most once.
 *
 * The score of a contiguous subarray is defined as the product of its length and the greatest common divisor (GCD) of all its elements.
 *
 * Your task is to return the maximum score that can be achieved by selecting a contiguous subarray from the modified array.
 *
 * Note:
 *
 * The greatest common divisor (GCD) of an array is the largest integer that evenly divides all the array elements.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,4], k = 1
 * Output: 8
 * Explanation:
 * Double nums[0] to 4 using one operation. The modified array becomes [4, 4].
 * The GCD of the subarray [4, 4] is 4, and the length is 2.
 * Thus, the maximum possible score is 2 * 4 = 8.
 *
 * Example 2:
 *
 * Input: nums = [3,5,7], k = 2
 * Output: 14
 * Explanation:
 * Double nums[2] to 14 using one operation. The modified array becomes [3, 5, 14].
 * The GCD of the subarray [14] is 14, and the length is 1.
 * Thus, the maximum possible score is 1 * 14 = 14.
 *
 * Example 3:
 *
 * Input: nums = [5,5,5], k = 1
 * Output: 15
 * Explanation:
 * The subarray [5, 5, 5] has a GCD of 5, and its length is 3.
 * Thus, the maximum possible score is 3 * 5 = 15.
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 1500
 * 1 <= nums[i] <= 10^9
 * 1 <= k <= n
 *
 */
public class MaximizeSubarrayGcdScore {

    // V0
    // IDEA: ENUMERATE EVERY SUBARRAY, TRACK ITS GCD AND THE ELEMENTS WITH THE FEWEST FACTORS OF 2
    //       write each element as (odd part) * 2^v. gcd of a window = gcd(odd parts) * 2^(min v).
    //       doubling raises one element's v by 1 and leaves its odd part alone, so the window's
    //       gcd can at best gain ONE factor of 2 -- and only if every element attaining the
    //       minimal v is doubled. so per window: score = length * gcd, and also
    //       length * gcd * 2 when the count of minimal-v elements is <= k.
    /**
     * time = O(N^2 * log(max))
     * space = O(N)
     */
    public long maxGCDScore(int[] nums, int k) {
        int n = nums.length;

        // twoPower[i] = how many times 2 divides nums[i]
        int[] twoPower = new int[n];
        for (int i = 0; i < n; i++) {
            twoPower[i] = Integer.numberOfTrailingZeros(nums[i]);
        }

        long best = 0;
        for (int start = 0; start < n; start++) {
            long windowGcd = 0;
            int minTwoPower = Integer.MAX_VALUE;
            int countAtMin = 0;
            for (int end = start; end < n; end++) {
                windowGcd = gcd(windowGcd, nums[end]);

                // maintain the minimal power of two and how many elements attain it
                if (twoPower[end] < minTwoPower) {
                    minTwoPower = twoPower[end];
                    countAtMin = 1;
                } else if (twoPower[end] == minTwoPower) {
                    countAtMin++;
                }

                long length = end - start + 1;
                long score = length * windowGcd;
                if (countAtMin <= k) {
                    // doubling every minimal-v element lifts the gcd by one factor of 2
                    score = score * 2;
                }
                best = Math.max(best, score);
            }
        }
        return best;
    }

    /** Euclid's algorithm. */
    private long gcd(long a, long b) {
        while (b != 0) {
            long remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
