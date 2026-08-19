package LeetCodeJava.Array;

// https://leetcode.com/problems/3sum-with-multiplicity/

/**
 *  923. 3Sum With Multiplicity
 *  Medium
 *
 *  Given an integer array arr, and an integer target, return the number of tuples
 *  i, j, k such that i < j < k and arr[i] + arr[j] + arr[k] == target.
 *
 *  As the answer can be very large, return it modulo 10^9 + 7.
 *
 *  Example 1:
 *  Input: arr = [1,1,2,2,3,3,4,4,5,5], target = 8
 *  Output: 20
 *  Explanation:
 *   Enumerating by the values (arr[i], arr[j], arr[k]):
 *   (1, 2, 5) occurs 8 times;
 *   (1, 3, 4) occurs 8 times;
 *   (2, 2, 4) occurs 2 times;
 *   (2, 3, 3) occurs 2 times.
 *
 *  Example 2:
 *  Input: arr = [1,1,2,2,2,2], target = 5
 *  Output: 12
 *
 *  Example 3:
 *  Input: arr = [2,1,3], target = 6
 *  Output: 1
 *
 *  Constraints:
 *   - 3 <= arr.length <= 3000
 *   - 0 <= arr[i] <= 100
 *   - 0 <= target <= 300
 */
public class ThreeSumWithMultiplicity {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: values are bounded by 100, so count occurrences of each value and
    //       enumerate value triples x <= y <= z, using combinatorics for the ties.
    /**
     * time = O(n + 101^2)
     * space = O(101)
     */
    public int threeSumMulti(int[] arr, int target) {
        long[] count = new long[101];
        for (int v : arr) {
            count[v]++;
        }
        long res = 0;
        for (int x = 0; x <= 100; x++) {
            if (count[x] == 0) {
                continue;
            }
            for (int y = x; y <= 100; y++) {
                if (count[y] == 0) {
                    continue;
                }
                int z = target - x - y;
                if (z < y || z > 100 || count[z] == 0) {
                    continue;
                }
                if (x == y && y == z) {
                    // C(c, 3)
                    long c = count[x];
                    res += c * (c - 1) * (c - 2) / 6;
                } else if (x == y) {
                    // C(c_x, 2) * c_z
                    long c = count[x];
                    res += c * (c - 1) / 2 % MOD * count[z];
                } else if (y == z) {
                    // c_x * C(c_y, 2)
                    long c = count[y];
                    res += count[x] * (c * (c - 1) / 2 % MOD);
                } else {
                    res += count[x] * count[y] % MOD * count[z];
                }
                res %= MOD;
            }
        }
        return (int) res;
    }
}
