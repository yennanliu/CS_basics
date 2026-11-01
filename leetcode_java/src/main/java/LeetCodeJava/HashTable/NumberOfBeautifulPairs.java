package LeetCodeJava.HashTable;

// https://leetcode.com/problems/number-of-beautiful-pairs/description/

/**
 * 2748. Number of Beautiful Pairs
 * Easy
 *
 * You are given a 0-indexed integer array nums. A pair of indices i, j where 0 <= i < j < nums.length is called beautiful if the first digit of nums[i] and the last digit of nums[j] are coprime.
 *
 * Return the total number of beautiful pairs in nums.
 *
 * Two integers x and y are coprime if there is no integer greater than 1 that divides both of them. In other words, x and y are coprime if gcd(x, y) == 1, where gcd(x, y) is the greatest common divisor of x and y.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,5,1,4]
 * Output: 5
 * Explanation: There are 5 beautiful pairs in nums:
 * When i = 0 and j = 1: the first digit of nums[0] is 2, and the last digit of nums[1] is 5. We can confirm that 2 and 5 are coprime, since gcd(2,5) == 1.
 * When i = 0 and j = 2: the first digit of nums[0] is 2, and the last digit of nums[2] is 1. Indeed, gcd(2,1) == 1.
 * When i = 1 and j = 2: the first digit of nums[1] is 5, and the last digit of nums[2] is 1. Indeed, gcd(5,1) == 1.
 * When i = 1 and j = 3: the first digit of nums[1] is 5, and the last digit of nums[3] is 4. Indeed, gcd(5,4) == 1.
 * When i = 2 and j = 3: the first digit of nums[2] is 1, and the last digit of nums[3] is 4. Indeed, gcd(1,4) == 1.
 * Thus, we return 5.
 *
 * Example 2:
 *
 * Input: nums = [11,21,12]
 * Output: 2
 * Explanation: There are 2 beautiful pairs:
 * When i = 0 and j = 1: the first digit of nums[0] is 1, and the last digit of nums[1] is 1. Indeed, gcd(1,1) == 1.
 * When i = 0 and j = 2: the first digit of nums[0] is 1, and the last digit of nums[2] is 2. Indeed, gcd(1,2) == 1.
 * Thus, we return 2.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 100
 * 1 <= nums[i] <= 9999
 * nums[i] % 10 != 0
 *
 */
public class NumberOfBeautifulPairs {

    // V0
    // IDEA: BUCKET COUNT OF LEADING DIGITS SEEN SO FAR (only 9 possible first digits)
    /**
     * time = O(N * 9 * log 10)
     * space = O(1)
     */
    public int countBeautifulPairs(int[] nums) {
        // leadingCount[d] = how many earlier elements start with digit d
        int[] leadingCount = new int[10];
        int pairs = 0;
        for (int num : nums) {
            // this element plays j: pair it with every earlier i whose first digit is coprime
            int lastDigit = num % 10;
            for (int d = 1; d <= 9; d++) {
                if (leadingCount[d] > 0 && gcd(d, lastDigit) == 1) {
                    pairs += leadingCount[d];
                }
            }
            // register AFTER counting so an element never pairs with itself
            leadingCount[firstDigit(num)]++;
        }
        return pairs;
    }

    /** Most significant decimal digit of a positive x. */
    private int firstDigit(int x) {
        while (x > 9) {
            x /= 10;
        }
        return x;
    }

    /** Euclid's greatest common divisor. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
