package LeetCodeJava.Greedy;

// https://leetcode.com/problems/previous-permutation-with-one-swap/description/

/**
 * 1053. Previous Permutation With One Swap
 * Medium
 *
 * Given an array of positive integers arr (not necessarily distinct), return the
 * lexicographically largest permutation that is smaller than arr, that can be made
 * with exactly one swap. If it cannot be done, then return the same array.
 *
 * Note that a swap exchanges the positions of two numbers arr[i] and arr[j]
 *
 *
 * Example 1:
 *
 * Input: arr = [3,2,1]
 * Output: [3,1,2]
 * Explanation: Swapping 2 and 1.
 *
 * Example 2:
 *
 * Input: arr = [1,1,5]
 * Output: [1,1,5]
 * Explanation: This is already the smallest permutation.
 *
 * Example 3:
 *
 * Input: arr = [1,9,4,6,7]
 * Output: [1,7,4,6,9]
 * Explanation: Swapping 9 and 7.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 10^4
 * 1 <= arr[i] <= 10^4
 *
 */
public class PreviousPermutationWithOneSwap {

    // V0
    // IDEA: GREEDY (mirror of "next permutation"): rightmost descent, swap with the largest smaller value
    /**
     * time = O(N)
     * space = O(1)
     */
    public int[] prevPermOpt1(int[] arr) {
        int n = arr.length;

        // step 1: rightmost i with arr[i] > arr[i + 1].
        // the suffix after i is non-decreasing (already smallest), so i is the
        // rightmost position we can shrink, and shrinking the rightmost loses the least
        int i = n - 2;
        while (i >= 0 && arr[i] <= arr[i + 1]) {
            i--;
        }
        if (i < 0) {
            // already the smallest permutation
            return arr;
        }

        // step 2: the largest value in the suffix strictly smaller than arr[i]
        // (the suffix is non-decreasing, so scan from the right)
        int j = n - 1;
        while (arr[j] >= arr[i]) {
            j--;
        }

        // step 3: if that value repeats, take its leftmost copy
        // e.g. [3,1,1,3] -> swap with index 1 gives [1,3,1,3], index 2 gives [1,1,3,3]
        while (j - 1 > i && arr[j - 1] == arr[j]) {
            j--;
        }

        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return arr;
    }
}
