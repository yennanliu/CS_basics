package LeetCodeJava.Graph;

// https://leetcode.com/problems/gcd-sort-of-an-array/description/

import java.util.Arrays;

/**
 * 1998. GCD Sort of an Array
 * Hard
 *
 * You are given an integer array nums, and you can perform the following operation any number of
 * times on nums:
 *
 * Swap the positions of two elements nums[i] and nums[j] if gcd(nums[i], nums[j]) > 1 where
 * gcd(nums[i], nums[j]) is the greatest common divisor of nums[i] and nums[j].
 *
 * Return true if it is possible to sort nums in non-decreasing order using the above swap method,
 * or false otherwise.
 *
 *
 * Example 1:
 *
 * Input: nums = [7,21,3]
 * Output: true
 * Explanation: We can sort [7,21,3] by performing the following operations:
 * - Swap 7 and 21 because gcd(7,21) = 7. nums = [21,7,3]
 * - Swap 21 and 3 because gcd(21,3) = 3. nums = [3,7,21]
 *
 * Example 2:
 *
 * Input: nums = [5,2,6,2]
 * Output: false
 * Explanation: It is impossible to sort the array because 5 cannot be swapped with any other element.
 *
 * Example 3:
 *
 * Input: nums = [10,5,9,3,15]
 * Output: true
 * We can sort [10,5,9,3,15] by performing the following operations:
 * - Swap 10 and 15 because gcd(10,15) = 5. nums = [15,5,9,3,10]
 * - Swap 15 and 3 because gcd(15,3) = 3. nums = [3,5,9,15,10]
 * - Swap 10 and 15 because gcd(10,15) = 5. nums = [3,5,9,10,15]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 3 * 10^4
 * 2 <= nums[i] <= 10^5
 *
 */
public class GcdSortOfAnArray {

    // V0
    // IDEA: SMALLEST-PRIME-FACTOR SIEVE + UNION FIND ON VALUES
    // two values can swap iff they share a prime factor, and swaps compose, so inside a connected
    // component any permutation is reachable. Build components without pairwise gcd: union each value
    // with each of its prime factors (from an spf sieve). Then compare nums with its sorted copy: a
    // mismatched position is fixable only if both values sit in the same component.
    /**
     * time = O(M log log M + N log M * alpha(M)), M = max(nums)
     * space = O(M)
     */
    public boolean gcdSort(int[] nums) {
        int maxValue = 0;
        for (int value : nums) {
            maxValue = Math.max(maxValue, value);
        }

        // smallestPrime[x] = smallest prime factor of x
        int[] smallestPrime = new int[maxValue + 1];
        for (int x = 0; x <= maxValue; x++) {
            smallestPrime[x] = x;
        }
        for (int i = 2; (long) i * i <= maxValue; i++) {
            if (smallestPrime[i] != i) {
                continue; // i is not prime
            }
            for (int multiple = i * i; multiple <= maxValue; multiple += i) {
                if (smallestPrime[multiple] == multiple) {
                    smallestPrime[multiple] = i;
                }
            }
        }

        // union-find over VALUES 0..maxValue (primes and the numbers themselves are both nodes)
        int[] parent = new int[maxValue + 1];
        for (int x = 0; x <= maxValue; x++) {
            parent[x] = x;
        }
        for (int value : nums) {
            int remaining = value;
            while (remaining > 1) {
                int prime = smallestPrime[remaining];
                union(parent, value, prime);
                // strip every copy of this prime before moving to the next one
                while (remaining % prime == 0) {
                    remaining /= prime;
                }
            }
        }

        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != sorted[i] && find(parent, nums[i]) != find(parent, sorted[i])) {
                return false;
            }
        }
        return true;
    }

    /** Root of x with iterative path compression (values reach 10^5). */
    private int find(int[] parent, int x) {
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    private void union(int[] parent, int a, int b) {
        int rootA = find(parent, a);
        int rootB = find(parent, b);
        if (rootA != rootB) {
            parent[rootB] = rootA;
        }
    }
}
