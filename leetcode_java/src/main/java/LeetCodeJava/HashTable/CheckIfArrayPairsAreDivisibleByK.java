package LeetCodeJava.HashTable;

// https://leetcode.com/problems/check-if-array-pairs-are-divisible-by-k/description/

/**
 * 1497. Check If Array Pairs Are Divisible by k
 * Medium
 *
 * Given an array of integers arr of even length n and an integer k.
 *
 * We want to divide the array into exactly n / 2 pairs such that the sum of each pair is divisible by k.
 *
 * Return true If you can find a way to do that or false otherwise.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,2,3,4,5,10,6,7,8,9], k = 5
 * Output: true
 * Explanation: Pairs are (1,9),(2,8),(3,7),(4,6) and (5,10).
 *
 * Example 2:
 *
 * Input: arr = [1,2,3,4,5,6], k = 7
 * Output: true
 * Explanation: Pairs are (1,6),(2,5) and(3,4).
 *
 * Example 3:
 *
 * Input: arr = [1,2,3,4,5,6], k = 10
 * Output: false
 * Explanation: You can try all possible pairs to see that there is no way to divide arr into 3 pairs each with sum divisible by 10.
 *
 *
 * Constraints:
 *
 * arr.length == n
 * 1 <= n <= 10^5
 * n is even.
 * -10^9 <= arr[i] <= 10^9
 * 1 <= k <= 10^5
 *
 */
public class CheckIfArrayPairsAreDivisibleByK {

    // V0
    // IDEA: COUNT REMAINDERS
    //   a + b divisible by k <=> (a % k) + (b % k) divisible by k,
    //   so remainder r must pair with remainder k - r:
    //   count[r] == count[k - r] for r in 1..k-1, and count[0] (and count[k/2]) must be even.
    /**
     * time = O(N + K)
     * space = O(K)
     */
    public boolean canArrange(int[] arr, int k) {
        int[] count = new int[k];
        for (int x : arr) {
            // Java's % keeps the sign of x, so shift negatives into [0, k)
            int remainder = ((x % k) + k) % k;
            count[remainder]++;
        }

        // remainder 0 pairs only with itself
        if (count[0] % 2 != 0) {
            return false;
        }

        for (int r = 1; r <= k / 2; r++) {
            if (r == k - r) {
                // remainder k/2 (k even) also pairs only with itself
                if (count[r] % 2 != 0) {
                    return false;
                }
            } else if (count[r] != count[k - r]) {
                return false;
            }
        }
        return true;
    }
}
