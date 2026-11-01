package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-adjacent-swaps-to-reach-the-kth-smallest-number/description/

/**
 * 1850. Minimum Adjacent Swaps to Reach the Kth Smallest Number
 * Medium
 *
 * You are given a string num, representing a large integer, and an integer k.
 *
 * We call some integer wonderful if it is a permutation of the digits in num and
 * is greater in value than num. There can be many wonderful integers. However,
 * we only care about the smallest-valued ones.
 *
 * For example, when num = "5489355142":
 * - The 1st smallest wonderful integer is "5489355214".
 * - The 2nd smallest wonderful integer is "5489355241".
 * - The 3rd smallest wonderful integer is "5489355412".
 * - The 4th smallest wonderful integer is "5489355421".
 *
 * Return the minimum number of adjacent digit swaps that needs to be applied to
 * num to reach the kth smallest wonderful integer.
 *
 * The tests are generated in such a way that kth smallest wonderful integer exists.
 *
 *
 * Example 1:
 *
 * Input: num = "5489355142", k = 4
 * Output: 2
 * Explanation: The 4th smallest wonderful number is "5489355421". To get this number:
 * - Swap index 7 with index 8: "5489355142" -> "5489355412"
 * - Swap index 8 with index 9: "5489355412" -> "5489355421"
 *
 * Example 2:
 *
 * Input: num = "11112", k = 4
 * Output: 4
 * Explanation: The 4th smallest wonderful number is "21111".
 *
 * Example 3:
 *
 * Input: num = "00123", k = 1
 * Output: 1
 * Explanation: The 1st smallest wonderful number is "00132". To get this number:
 * - Swap index 3 with index 4: "00123" -> "00132"
 *
 *
 * Constraints:
 *
 * 2 <= num.length <= 1000
 * 1 <= k <= 1000
 * num only consists of digits.
 *
 */
public class MinimumAdjacentSwapsToReachTheKthSmallestNumber {

    // V0
    // IDEA: NEXT PERMUTATION k TIMES, THEN GREEDY BUBBLE COUNT (pull the nearest match left)
    /**
     * time = O(K * N + N^2)
     * space = O(N)
     */
    public int getMinSwaps(String num, int k) {
        // part 1: the k-th smallest wonderful integer is next-permutation applied k times
        char[] target = num.toCharArray();
        for (int step = 0; step < k; step++) {
            nextPermutation(target);
        }

        // part 2: count adjacent swaps turning num into target
        char[] current = num.toCharArray();
        int n = current.length;
        int swaps = 0;
        for (int i = 0; i < n; i++) {
            if (current[i] == target[i]) {
                continue;
            }
            // the NEAREST matching digit is optimal: a further duplicate would cross it
            int j = i + 1;
            while (current[j] != target[i]) {
                j++;
            }
            // bubble it left one position at a time
            while (j > i) {
                swap(current, j, j - 1);
                j--;
                swaps++;
            }
        }
        return swaps;
    }

    /** Rearranges a into its next lexicographic permutation; false if it was the last. */
    private boolean nextPermutation(char[] a) {
        int n = a.length;
        // rightmost ascent a[i] < a[i + 1]
        int i = n - 2;
        while (i >= 0 && a[i] >= a[i + 1]) {
            i--;
        }
        if (i < 0) {
            return false;
        }
        // rightmost element bigger than a[i]
        int j = n - 1;
        while (a[j] <= a[i]) {
            j--;
        }
        swap(a, i, j);
        // the suffix is descending; reverse it to make it the smallest
        int left = i + 1;
        int right = n - 1;
        while (left < right) {
            swap(a, left, right);
            left++;
            right--;
        }
        return true;
    }

    /** Swaps a[i] and a[j]. */
    private void swap(char[] a, int i, int j) {
        char temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
