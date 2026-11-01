package LeetCodeJava.Array;

// https://leetcode.com/problems/defuse-the-bomb/description/
/**
 * 1652. Defuse the Bomb
 * Easy
 *
 * You have a bomb to defuse, and your time is running out! Your informer will provide you with a
 * circular array code of length of n and a key k.
 *
 * To decrypt the code, you must replace every number. All the numbers are replaced simultaneously.
 *
 * - If k > 0, replace the ith number with the sum of the next k numbers.
 * - If k < 0, replace the ith number with the sum of the previous -k numbers.
 * - If k == 0, replace the ith number with 0.
 *
 * As code is circular, the next element of code[n-1] is code[0], and the previous element of
 * code[0] is code[n-1].
 *
 * Given the circular array code and an integer key k, return the decrypted code to defuse the bomb!
 *
 *
 * Example 1:
 *
 * Input: code = [5,7,1,4], k = 3
 * Output: [12,10,16,13]
 * Explanation: Each number is replaced by the sum of the next 3 numbers. The decrypted code is
 * [7+1+4, 1+4+5, 4+5+7, 5+7+1]. Notice that the numbers wrap around.
 *
 * Example 2:
 *
 * Input: code = [1,2,3,4], k = 0
 * Output: [0,0,0,0]
 * Explanation: When k is zero, the numbers are replaced by 0.
 *
 * Example 3:
 *
 * Input: code = [2,4,9,3], k = -2
 * Output: [12,5,6,13]
 * Explanation: The decrypted code is [3+9, 2+3, 4+2, 9+4]. Notice that the numbers wrap around
 * again. If k is negative, the sum is of the previous numbers.
 *
 *
 * Constraints:
 *
 * n == code.length
 * 1 <= n <= 100
 * 1 <= code[i] <= 100
 * -(n - 1) <= k <= n - 1
 *
 */
public class DefuseTheBomb {

    // V0
    // IDEA: SLIDING WINDOW OVER THE CIRCULAR ARRAY
    // every answer is the sum of a fixed-length window of |k| elements; only its offset moves:
    //   k > 0 -> window [i + 1, i + k],  k < 0 -> window [i + k, i - 1]   (indices mod n)
    // brute force (re-sum |k| neighbours per index, O(n * |k|)) is the stepping stone.
    /**
     * time = O(N)
     * space = O(N) for the output
     */
    public int[] decrypt(int[] code, int k) {
        int n = code.length;
        int[] result = new int[n];
        if (k == 0) {
            return result;
        }

        // window bounds (inclusive) for i = 0; they may exceed n, so every access is taken mod n
        int windowStart;
        int windowEnd;
        if (k > 0) {
            windowStart = 1;
            windowEnd = k;
        } else {
            windowStart = n + k;
            windowEnd = n - 1;
        }

        int windowSum = 0;
        for (int j = windowStart; j <= windowEnd; j++) {
            windowSum += code[j % n];
        }
        result[0] = windowSum;

        // slide by one: drop the element leaving on the left, add the one entering on the right
        for (int i = 1; i < n; i++) {
            windowSum -= code[(windowStart + i - 1) % n];
            windowSum += code[(windowEnd + i) % n];
            result[i] = windowSum;
        }
        return result;
    }

    // V1
    // IDEA: DOUBLE THE ARRAY + PREFIX SUM (no modulo inside the ranges)
    // code + code turns every wrap-around window into a contiguous range of the doubled array:
    //   k > 0 -> [i + 1, i + k],  k < 0 -> [i + n + k, i + n - 1]
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] decrypt_1(int[] code, int k) {
        int n = code.length;
        int[] result = new int[n];
        if (k == 0) {
            return result;
        }

        // prefix[i] = sum of the first i elements of the doubled array
        int[] prefix = new int[2 * n + 1];
        for (int i = 0; i < 2 * n; i++) {
            prefix[i + 1] = prefix[i] + code[i % n];
        }

        for (int i = 0; i < n; i++) {
            int low;
            int high;
            if (k > 0) {
                low = i + 1;
                high = i + k;
            } else {
                low = i + n + k;
                high = i + n - 1;
            }
            result[i] = prefix[high + 1] - prefix[low];
        }
        return result;
    }
}
