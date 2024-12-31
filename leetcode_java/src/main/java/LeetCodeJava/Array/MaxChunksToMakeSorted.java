package LeetCodeJava.Array;

// https://leetcode.com/problems/max-chunks-to-make-sorted/description/
// https://leetcode.cn/problems/max-chunks-to-make-sorted/description/

import java.util.Stack;

/**
 * 769. Max Chunks To Make Sorted
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given an integer array arr of length n that represents a permutation of the integers in the range [0, n - 1].
 *
 * We split arr into some number of chunks (i.e., partitions), and individually sort each chunk. After concatenating them, the result should equal the sorted array.
 *
 * Return the largest number of chunks we can make to sort the array.
 *
 *
 *
 * Example 1:
 *
 * Input: arr = [4,3,2,1,0]
 * Output: 1
 * Explanation:
 * Splitting into two or more chunks will not return the required result.
 * For example, splitting into [4, 3], [2, 1, 0] will result in [3, 4, 0, 1, 2], which isn't sorted.
 * Example 2:
 *
 * Input: arr = [1,0,2,3,4]
 * Output: 4
 * Explanation:
 * We can split into two chunks, such as [1, 0], [2, 3, 4].
 * However, splitting into [1, 0], [2], [3], [4] is the highest number of chunks possible.
 *
 *
 * Constraints:
 *
 * n == arr.length
 * 1 <= n <= 10
 * 0 <= arr[i] < n
 * All the elements of arr are unique.
 *
 */
public class MaxChunksToMakeSorted {

    // V0
//    public int maxChunksToSorted(int[] arr) {
//
//    }

    // V1-1
    // https://leetcode.com/problems/max-chunks-to-make-sorted/editorial/
    // IDEA: PrefixMax and SuffixMin Arrays
    public int maxChunksToSorted_1_1(int[] arr) {
        int n = arr.length;
        int[] prefixMax = arr.clone();
        int[] suffixMin = arr.clone();

        // Fill the prefixMax array
        for (int i = 1; i < n; i++) {
            prefixMax[i] = Math.max(prefixMax[i - 1], prefixMax[i]);
        }

        // Fill the suffixMin array in reverse order
        for (int i = n - 2; i >= 0; i--) {
            suffixMin[i] = Math.min(suffixMin[i + 1], suffixMin[i]);
        }

        int chunks = 0;
        for (int i = 0; i < n; i++) {
            // A new chunk can be created
            if (i == 0 || suffixMin[i] > prefixMax[i - 1]) {
                chunks++;
            }
        }

        return chunks;
    }

    // V1-2
    // https://leetcode.com/problems/max-chunks-to-make-sorted/editorial/
    // IDEA: Prefix Sums
    public int maxChunksToSorted_1_2(int[] arr) {
        int n = arr.length;
        int chunks = 0, prefixSum = 0, sortedPrefixSum = 0;

        // Iterate over the array
        for (int i = 0; i < n; i++) {
            // Update prefix sum of `arr`
            prefixSum += arr[i];
            // Update prefix sum of the sorted array
            sortedPrefixSum += i;

            // If the two sums are equal, the two prefixes contain the same elements; a chunk can be formed
            if (prefixSum == sortedPrefixSum) {
                chunks++;
            }
        }
        return chunks;
    }

    // V1-3
    // https://leetcode.com/problems/max-chunks-to-make-sorted/editorial/
    // IDEA:  Monotonic Increasing Stack
    public int maxChunksToSorted(int[] arr) {
        int n = arr.length;
        // Stack to store the maximum elements of each chunk
        Stack<Integer> monotonicStack = new Stack<>();

        for (int i = 0; i < n; i++) {
            // Case 1: Current element is larger, starts a new chunk
            if (monotonicStack.isEmpty() || arr[i] > monotonicStack.peek()) {
                monotonicStack.push(arr[i]);
            } else {
                // Case 2: Merge chunks
                int maxElement = monotonicStack.peek();
                while (!monotonicStack.isEmpty() && arr[i] < monotonicStack.peek()) {
                    monotonicStack.pop();
                }
                monotonicStack.push(maxElement);
            }
        }
        return monotonicStack.size();
    }

    // V2
}
