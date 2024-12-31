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
    // IDEA: Prefix Sums
    /**
     *  IDEA:
     *
     *  An important observation is that a segment of the
     *  array can form a valid chunk if, when sorted,
     *  it matches the corresponding segment
     *  in the fully sorted version of the array.
     *
     * Since the numbers in arr belong to the range [0, n - 1], we can simplify the problem by using the property of sums. Specifically, for any index, it suffices to check whether the sum of the elements in arr up to that index is equal to the sum of the elements in the corresponding prefix of the sorted array.
     *
     * If these sums are equal, it guarantees that the elements in the current segment of arr match the elements in the corresponding segment of the sorted array (possibly in a different order). When this condition is satisfied, we can form a new chunk — either starting from the beginning of the array or the end of the previous chunk.
     *
     * For example, consider arr = [1, 2, 0, 3, 4] and the sorted version sortedArr = [0, 1, 2, 3, 4]. We find the valid segments as follows:
     *
     * Segment [0, 0] is not valid, since sum = 1 and sortedSum = 0.
     * Segment [0, 1] is not valid, since sum = 1 + 2 = 3 and sortedSum = 0 + 1 = 1.
     * Segment [0, 2] is valid, since sum = 1 + 2 + 0 = 3 and sortedSum = 0 + 1 + 2 = 3.
     * Segment [3, 3] is valid, because sum = 1 + 2 + 0 + 3 = 6 and sortedSum = 0 + 1 + 2 + 3 = 6.
     * Segment [4, 4] is valid, because sum = 1 + 2 + 0 + 3 + 4 = 10 and sortedSum = 0 + 1 + 2 + 3 + 4 = 10.
     * Therefore, the answer here is 3.
     *
     * Algorithm
     * - Initialize n to the size of the arr array.
     * - Initialize chunks, prefixSum, and sortedPrefixSum to 0.
     * - Iterate over arr with i from 0 to n - 1:
     *    - Increment prefixSum by arr[i].
     *    - Increment sortedPrefixSum by i.
     *    - Check if prefixSum == sortedPrefixSum:
     *        - If so, increment chunks by 1.
     * - Return chunks.
     *
     */
    public int maxChunksToSorted_1_1(int[] arr) {
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

  // V1-2
  // https://leetcode.com/problems/max-chunks-to-make-sorted/editorial/
  // IDEA: PrefixMax and SuffixMin Arrays
  /**
   *  IDEA:
   *
   *
   *  Building on the above observation, we further notice that for each number in the array, we have two options: we can either include it in the same chunk as the previous number or create a new chunk for it. However, we must consider the limitation that a new chunk at index i can only be created if all the numbers in the current and previous chunks (the "prefix" of the array) are smaller than all the numbers in the following chunks (the "suffix" of the array). This is equivalent to checking whether:
   *
   * max(prefix[0:i])<min(suffix[i:n]).
   * ​
   *
   * Since we aim to find the largest possible number of chunks, we will choose the second option (i.e., create a new chunk) whenever the above condition is satisfied. Therefore, the problem reduces to counting how many indices in the array satisfy this condition.
   *
   * Algorithm
   * - Initialize n to the size of the arr array.
   * - Initialize prefixMax and suffixMin arrays to arr.
   * - Iterate over arr with i from 1 to n - 1:
   *    - Set prefixMax[i] = max(prefixMax[i], prefixMax[i-1]).
   * - Iterate over arr with i from n - 2 to 0:
   *    - Set suffixMin[i] = min(suffixMin[i], suffixMin[i+1]).
   * - Initialize chunks to 0.
   * - Iterate over arr with i from 0 to n - 1:
   *    - Check if i == 0 (create a chunk for the first element) or suffixMin[i] > prefixMax[i - 1].
   *    - If true, increment chunks by 1.
   * - Return chunks.
   *
   *
   */
  public int maxChunksToSorted_1_2(int[] arr) {
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
