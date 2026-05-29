package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-number-of-operations-to-reinitialize-a-permutation/description/
/**
 *  1806. Minimum Number of Operations to Reinitialize a Permutation
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an even integer n​​​​​​. You initially have a permutation perm of size n​​ where perm[i] == i​ (0-indexed)​​​​.
 *
 * In one operation, you will create a new array arr, and for each i:
 *
 * If i % 2 == 0, then arr[i] = perm[i / 2].
 * If i % 2 == 1, then arr[i] = perm[n / 2 + (i - 1) / 2].
 * You will then assign arr​​​​ to perm.
 *
 * Return the minimum non-zero number of operations you need to perform on perm to return the permutation to its initial value.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: 1
 * Explanation: perm = [0,1] initially.
 * After the 1st operation, perm = [0,1]
 * So it takes only 1 operation.
 * Example 2:
 *
 * Input: n = 4
 * Output: 2
 * Explanation: perm = [0,1,2,3] initially.
 * After the 1st operation, perm = [0,2,1,3]
 * After the 2nd operation, perm = [0,1,2,3]
 * So it takes only 2 operations.
 * Example 3:
 *
 * Input: n = 6
 * Output: 4
 *
 *
 * Constraints:
 *
 * 2 <= n <= 1000
 * n is even.
 *
 */
public class MinimumNumberOfOperationsToReinitializeAPermutation {

    // V0

    // V1

    // V2
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/minimum-number-of-operations-to-reinitialize-a-permutation/solutions/1131138/detailed-explanation-with-thought-proces-oh32/
    public int reinitializePermutation_2(int n) {
        int ans = 1;
        int num = 2;
        if (n == 2)
            return 1;
        while (true) {
            if (num % (n - 1) == 1)
                break;
            else {
                ans++;
                num = (num * 2) % (n - 1);
            }
        }
        return ans;

    }

    // V3
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/minimum-number-of-operations-to-reinitialize-a-permutation/solutions/1130704/brute-force-by-himanshuchhikara-ynin/
    public int reinitializePermutation_3(int n) {
        int[] perm = new int[n];
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            perm[i] = i;
            nums[i] = i;
        }
        int operation = 1;
        while (true) {
            int[] arr = performOperation(perm, n);
            if (isEqualToInitial(arr, nums))
                return operation;
            perm = arr;
            operation++;
        }

    }

    private boolean isEqualToInitial(int[] arr1, int[] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i])
                return false;
            ;
        }
        return true;
    }

    private int[] performOperation(int[] perm, int n) {
        int[] arr = new int[n];
        for (int i = 0; i < perm.length; i++) {
            if (i % 2 == 0) {
                arr[i] = perm[i / 2];
            } else {
                arr[i] = perm[n / 2 + (i - 1) / 2];
            }
        }
        return arr;
    }




    // V4
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/minimum-number-of-operations-to-reinitialize-a-permutation/solutions/1130784/java-o1-space-easy-to-understand-by-alan-007d/
    public int reinitializePermutation_4(int n) {
        int temp = n / 2, count = 1;
        while (temp != 1) {
            if (temp % 2 == 0)
                temp /= 2;
            else
                temp = n / 2 + (temp - 1) / 2;
            count++;
        }
        return count;
    }






}
