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
//    public int reinitializePermutation(int n) {
//
//    }

    // V1-1
    // IDEA: Direct Simulation (Most Intuitive) (GEMINI)
    /**
     * Since the maximum value of $n$ is relatively
     * small ($n \le 1000$), we can directly simulate
     * the operation step-by-step using two arrays until the
     * array returns to its original configuration
     * (arr[i] == i for all indices).
     *
     */
    public int reinitializePermutation_1(int n) {
        // Initialize the array as specified: arr[i] = i
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }

        int operations = 0;

        // Keep simulating operations until the array matches its original state
        while (true) {
            int[] next = new int[n];

            // Apply the transformation rules given in the problem statement
            for (int i = 0; i < n; i++) {
                if (i % 2 == 0) {
                    next[i] = arr[i / 2];
                } else {
                    next[i] = arr[n / 2 + (i - 1) / 2];
                }
            }

            // Advance our operation counter
            operations++;
            arr = next; // Move to the next state

            // Check if the array has returned to the initial configuration
            if (isOriginal(arr)) {
                break;
            }
        }

        return operations;
    }

    // Helper method to verify if the array matches its original initialization (arr[i] == i)
    private boolean isOriginal(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != i) {
                return false;
            }
        }
        return true;
    }


    // V1-2
    // IDEA: MATH (GEMINI)
    /**
     * If you trace the position of a single element
     * (excluding the first and last elements, which never move),
     * you will notice that its index follows a deterministic cycle.
     *
     * Instead of moving the entire array, we can track
     * the index of just a single number
     * (e.g., the number originally at index 1).
     * When index 1 cycles all the way back to position 1,
     * the entire permutation is guaranteed to have returned to its original state.
     *
     *   - If the current index i is even: it moves to i / 2.
     *
     *   - If the current index i is odd: it moves to n / 2 + (i - 1) / 2.
     *
     */
    public int reinitializePermutation(int n) {
        // Track only the index location of the element that started at index 1
        int currentIndex = 1;
        int operations = 0;

        // Run until the element cycles completely back to index 1
        while (operations == 0 || currentIndex != 1) {
            if (currentIndex % 2 == 0) {
                // Even index rule inverted: what lands at index 'currentIndex' came from 'currentIndex / 2'
                currentIndex = currentIndex / 2;
            } else {
                // Odd index rule inverted
                currentIndex = n / 2 + (currentIndex - 1) / 2;
            }
            operations++;
        }

        return operations;
    }



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
