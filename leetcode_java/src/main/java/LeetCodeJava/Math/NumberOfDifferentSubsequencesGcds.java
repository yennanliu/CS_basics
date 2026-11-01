package LeetCodeJava.Math;

// https://leetcode.com/problems/number-of-different-subsequences-gcds/description/

/**
 * 1819. Number of Different Subsequences GCDs
 * Hard
 *
 * You are given an array nums that consists of positive integers.
 *
 * The GCD of a sequence of numbers is defined as the greatest integer that divides all the numbers in the sequence evenly.
 *
 * For example, the GCD of the sequence [4,6,16] is 2.
 *
 * A subsequence of an array is a sequence that can be formed by removing some elements (possibly none) of the array.
 *
 * For example, [2,5,10] is a subsequence of [1,2,1,2,4,1,5,10].
 *
 * Return the number of different GCDs among all non-empty subsequences of nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [6,10,3]
 * Output: 5
 * Explanation: The figure shows all the non-empty subsequences and their GCDs.
 * The different GCDs are 6, 10, 3, 2, and 1.
 *
 * Example 2:
 *
 * Input: nums = [5,15,40,5,6]
 * Output: 7
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 2 * 10^5
 *
 */
public class NumberOfDifferentSubsequencesGcds {

    // V0
    // IDEA: ENUMERATE THE CANDIDATE GCD, NOT THE SUBSEQUENCE (harmonic sieve)
    //   only max(nums) candidate gcds exist. If x is achievable at all, it is achieved by the set
    //   of ALL multiples of x in nums (more multiples only pull the gcd down toward x).
    //   so x counts iff gcd(every present multiple of x) == x; stop early once the gcd hits x.
    //   walking x and its multiples is a harmonic sum -> O(M log M).
    /**
     * time = O(M log M + N)
     * space = O(M)  M = max(nums)
     */
    public int countDifferentSubsequenceGCDs(int[] nums) {
        int maxValue = 0;
        for (int num : nums) {
            maxValue = Math.max(maxValue, num);
        }
        boolean[] present = new boolean[maxValue + 1];
        for (int num : nums) {
            present[num] = true;
        }

        int count = 0;
        for (int x = 1; x <= maxValue; x++) {
            int g = 0;
            for (int multiple = x; multiple <= maxValue; multiple += x) {
                if (!present[multiple]) {
                    continue;
                }
                g = gcd(g, multiple);
                if (g == x) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    /** Euclid's algorithm; gcd(0, b) = b. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
