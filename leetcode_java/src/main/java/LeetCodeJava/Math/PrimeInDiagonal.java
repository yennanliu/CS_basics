package LeetCodeJava.Math;

// https://leetcode.com/problems/prime-in-diagonal/description/
/**
 *  2614. Prime In Diagonal
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed two-dimensional integer array nums.
 *
 * Return the largest prime number that lies on at least one of the diagonals of nums. In case, no prime is present on any of the diagonals, return 0.
 *
 * Note that:
 *
 * An integer is prime if it is greater than 1 and has no positive integer divisors other than 1 and itself.
 * An integer val is on one of the diagonals of nums if there exists an integer i for which nums[i][i] = val or an i for which nums[i][nums.length - i - 1] = val.
 *
 *
 * In the above diagram, one diagonal is [1,5,9] and another diagonal is [3,5,7].
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [[1,2,3],[5,6,7],[9,10,11]]
 * Output: 11
 * Explanation: The numbers 1, 3, 6, 9, and 11 are the only numbers present on at least one of the diagonals. Since 11 is the largest prime, we return 11.
 * Example 2:
 *
 * Input: nums = [[1,2,3],[5,17,7],[9,11,10]]
 * Output: 17
 * Explanation: The numbers 1, 3, 9, 10, and 17 are all present on at least one of the diagonals. 17 is the largest prime, so we return 17.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 300
 * nums.length == numsi.length
 * 1 <= nums[i][j] <= 4*106
 *
 *
 */
public class PrimeInDiagonal {

    // V0
//    public int diagonalPrime(int[][] nums) {
//
//    }


    // V0-1
    // IDEA: MATH (gemini)
    public int diagonalPrime_0_1(int[][] nums) {
        int n = nums.length;
        int maxPrime = 0;

        for (int i = 0; i < n; i++) {
            // 1. Primary Diagonal: (i, i)
            int val1 = nums[i][i];
            if (val1 > maxPrime && isPrime(val1)) {
                maxPrime = val1;
            }

            // 2. Secondary Diagonal: (i, n - 1 - i)
            int val2 = nums[i][n - 1 - i];
            if (val2 > maxPrime && isPrime(val2)) {
                maxPrime = val2;
            }
        }

        return maxPrime;
    }

    private boolean isPrime(int n) {
        if (n < 2)
            return false;
        // Check up to the square root
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0)
                return false;
        }
        return true;
    }


    // V1
    // https://leetcode.com/problems/prime-in-diagonal/solutions/3395721/simple-java-solution-by-siddhant_1602-ey8o/
    public int diagonalPrime_1(int[][] nums) {
        int maxi = 0;
        for (int i = 0; i < nums.length; i++) {
            if (prime(nums[i][i])) {
                maxi = Math.max(maxi, nums[i][i]);
            }
            if (prime(nums[nums.length - i - 1][i])) {
                maxi = Math.max(maxi, nums[nums.length - i - 1][i]);
            }
        }
        return maxi;
    }

    public boolean prime(int n) {
        if (n < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }


    // V2
    // https://leetcode.com/problems/prime-in-diagonal/solutions/3395792/short-clean-java-solution-by-himanshubho-nrjt/
    public int diagonalPrime_2(int[][] nums) {
        int max = 0;
        for (int i = 0; i < nums.length; i++) {
            if (isprime(nums[i][i]))
                max = Math.max(max, nums[i][i]);
            if (isprime(nums[nums.length - i - 1][i]))
                max = Math.max(max, nums[nums.length - i - 1][i]);
        }
        return max;
    }

    boolean isprime(int n) {
        if (n < 2)
            return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0)
                return false;
        }
        return true;
    }


    // V3




}
