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
 * <p>
 * We split arr into some number of chunks (i.e., partitions), and individually sort each chunk. After concatenating them, the result should equal the sorted array.
 * <p>
 * Return the largest number of chunks we can make to sort the array.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: arr = [4,3,2,1,0]
 * Output: 1
 * Explanation:
 * Splitting into two or more chunks will not return the required result.
 * For example, splitting into [4, 3], [2, 1, 0] will result in [3, 4, 0, 1, 2], which isn't sorted.
 * Example 2:
 * <p>
 * Input: arr = [1,0,2,3,4]
 * Output: 4
 * Explanation:
 * We can split into two chunks, such as [1, 0], [2, 3, 4].
 * However, splitting into [1, 0], [2], [3], [4] is the highest number of chunks possible.
 * <p>
 * <p>
 * Constraints:
 * <p>
 * n == arr.length
 * 1 <= n <= 10
 * 0 <= arr[i] < n
 * All the elements of arr are unique.
 */
public class MaxChunksToMakeSorted {

    /** NOTE !!!
     *
     *    All the elements of arr are `unique`.
     */


    // V0
    // IDEA: GREEDY (prefix maximum) - max so far (gpt)
    /**  NOTE !!!
     *
     *  Core idea:
     *
     *   A chunk can end at index i
     *
     *     - if the maximum value seen so far equals i
     *      (since the array is a permutation (排列) of [0..n-1]).
     *
     *
     *      -> should just track the running max.
     *
     */
    /**  Dry run:
     *
     * Let’s dry run the **greedy + prefix max**
     * solution step by step for
     * LeetCode 769 Max Chunks To
     * Make Sorted so you can see exactly how the chunks form.
     *
     * ---
     *
     * ## ✅ Example 1
     *
     * **Input:**
     *
     * ```text
     * arr = [1, 0, 2, 3, 4]
     * ```
     *
     * ### Initialization
     *
     * ```text
     * chunks = 0
     * maxSoFar = 0
     * ```
     *
     * ---
     *
     * ### Iteration
     *
     * #### i = 0
     *
     * ```text
     * arr[i] = 1
     * maxSoFar = max(0, 1) = 1
     * maxSoFar != i (1 != 0) → cannot split
     * ```
     *
     * ---
     *
     * #### i = 1
     *
     * ```text
     * arr[i] = 0
     * maxSoFar = max(1, 0) = 1
     * maxSoFar == i (1 == 1) → ✅ split here
     *
     * chunks = 1
     * ```
     *
     * 👉 Chunk: `[1, 0]` → after sorting → `[0, 1]`
     *
     * ---
     *
     * #### i = 2
     *
     * ```text
     * arr[i] = 2
     * maxSoFar = max(1, 2) = 2
     * maxSoFar == i (2 == 2) → ✅ split
     *
     * chunks = 2
     * ```
     *
     * 👉 Chunk: `[2]`
     *
     * ---
     *
     * #### i = 3
     *
     * ```text
     * arr[i] = 3
     * maxSoFar = 3
     * maxSoFar == i → ✅ split
     *
     * chunks = 3
     * ```
     *
     * 👉 Chunk: `[3]`
     *
     * ---
     *
     * #### i = 4
     *
     * ```text
     * arr[i] = 4
     * maxSoFar = 4
     * maxSoFar == i → ✅ split
     *
     * chunks = 4
     * ```
     *
     * 👉 Chunk: `[4]`
     *
     * ---
     *
     * ### ✅ Final Answer
     *
     * ```text
     * chunks = 4
     * ```
     *
     * ---
     *
     * ## ❗ Example 2 (more interesting)
     *
     * **Input:**
     *
     * ```text
     * arr = [4, 3, 2, 1, 0]
     * ```
     *
     * ---
     *
     * ### Iteration
     *
     * #### i = 0 → 3
     *
     * ```text
     * maxSoFar keeps increasing: 4
     * At i = 0 → 4 != 0
     * At i = 1 → 4 != 1
     * At i = 2 → 4 != 2
     * At i = 3 → 4 != 3
     * ```
     *
     * No splits yet ❌
     *
     * ---
     *
     * #### i = 4
     *
     * ```text
     * arr[i] = 0
     * maxSoFar = 4
     * maxSoFar == i (4 == 4) → ✅ split
     * ```
     *
     * ---
     *
     * ### ✅ Final Answer
     *
     * ```text
     * chunks = 1
     * ```
     *
     * 👉 Whole array must be one chunk
     *
     * ---
     *
     * ## 🔑 Key Insight from Dry Run
     *
     * * `maxSoFar` tells you the **largest number that must be placed**
     * * If it equals current index `i`, then:
     *
     *   * all numbers `[0..i]` are already within `[0..i]`
     *   * so sorting this segment won’t affect the rest
     *
     * ---
     *
     * ## 🧠 Visual intuition
     *
     * Think of it like:
     *
     * ```text
     * Index:      0  1  2  3  4
     * arr:        1  0  2  3  4
     * maxSoFar:   1  1  2  3  4
     *               ↑     ↑  ↑  ↑
     *            split  split...
     * ```
     *
     *
     */
    public int maxChunksToSorted(int[] arr) {
        int chunks = 0;
        int maxSoFar = 0;

        for (int i = 0; i < arr.length; i++) {
            maxSoFar = Math.max(maxSoFar, arr[i]);

            if (maxSoFar == i) {
                chunks++;
            }
        }

        return chunks;
    }



    // V0-2
    // IDEA: GREEDY (prefix maximum) - max so far (GEMINI)
    /**  Dry run:
     *
     * In **LC 769 (Max Chunks To Make Sorted)**,
     * the array is a permutation of
     * numbers from `0` to `n - 1`.
     *
     * The key observation is
     * that a chunk can be formed if the **maximum value**
     * seen so far in the current segment is equal to the **current index**.
     *
     * If the max value seen from index `0` to `i`
     * is exactly `i`, it means all the numbers
     * that *should* be in the first `i + 1` positions (0 through $i$) are present in that segment, just in a different order.
     *
     * ### 🐛 The Logic Fixes
     * 1.  **Avoid Sorting/Copying**: Using `Arrays.sort` inside a loop makes the code $O(n^2 \log n)$. This problem can be solved in $O(n)$.
     * 2.  **The Index Property**: Since the values are `[0, 1, ..., n-1]`, at any point `i`, if `max(arr[0...i]) == i`, you can successfully "cut" a chunk there.
     * 3.  **The Counter**: Start your result at `0` and increment it every time the "max == index" condition is met.
     *
     *
     * ### 🔍 Senior Engineering Review
     *
     * *   **Complexity**:
     *     *   **Time**: $O(n)$. We pass through the array exactly once.
     *     *   **Space**: $O(1)$. No extra arrays or lists needed.
     * *   **Why this works**: Because the sorted version of the array is guaranteed to be `[0, 1, 2, ..., n-1]`, index `i` *must* eventually hold value `i`. If our maximum value up to index `i` is `i`, it implies we haven't seen any numbers larger than `i`, meaning the set of numbers we have seen is exactly `{0, 1, ..., i}`.
     *
     *
     *
     * ### 📊 Dry Run: `arr = [1, 0, 2, 4, 3]`
     * 1.  **i = 0**: `maxSoFar = 1`. `maxSoFar (1) != i (0)`. (No chunk)
     * 2.  **i = 1**: `maxSoFar = max(1, 0) = 1`. `maxSoFar (1) == i (1)`. **(Chunk 1: [1, 0])**
     * 3.  **i = 2**: `maxSoFar = max(1, 2) = 2`. `maxSoFar (2) == i (2)`. **(Chunk 2: [2])**
     * 4.  **i = 3**: `maxSoFar = max(2, 4) = 4`. `maxSoFar (4) != i (3)`. (No chunk)
     * 5.  **i = 4**: `maxSoFar = max(4, 3) = 4`. `maxSoFar (4) == i (4)`. **(Chunk 3: [4, 3])**
     *
     * **Result: 3**
     *
     *
     */
    public int maxChunksToSorted_0_2(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;

        int chunks = 0;
        int maxSoFar = 0;

        for (int i = 0; i < arr.length; i++) {
            // Update the maximum value seen in the current chunk
            maxSoFar = Math.max(maxSoFar, arr[i]);

            // If the maximum value seen equals the current index,
            // all values from 0 to i are present in arr[0...i].
            if (maxSoFar == i) {
                chunks++;
            }
        }

        return chunks;
    }



    // V0-3
    // IDEA: prefix + problem understanding

    /**
     * IDEA:
     * <p>
     * since this problem wants to find the MAX value of sub array with sort op
     * and concatenate can same as the SORTED ORIGINAL ARRAY
     * <p>
     * -> so all we need to do within looping is:
     * maintain
     * 1) sorted prefixSum (sort all array and its prefix sum)
     * 2) prefixSum (current array's prefix sum)
     * <p>
     * -> so, for `sorted prefixSum`, it's sortedPrefixSum + i, ... (since the array is the permutation of element within [0, n - 1])
     * for `prefixSum`, it's prefixSum + i ,....
     */
    public int maxChunksToSorted_0_3(int[] arr) {

        int n = arr.length;
        int chunks = 0, prefixSum = 0, sortedPrefixSum = 0;

        // Iterate over the array
        for (int i = 0; i < n; i++) {
            // Update prefix sum of `arr`
            prefixSum += arr[i];
            // Update prefix sum of the sorted array
            sortedPrefixSum += i;

            // If the two sums are equal, the two prefixes contain the same elements; a
            // chunk can be formed
            if (prefixSum == sortedPrefixSum) {
                chunks++;
            }
        }
        return chunks;
    }


    // V1-1
    // https://leetcode.com/problems/max-chunks-to-make-sorted/editorial/
    // IDEA: Prefix Sums

    /**
     * IDEA:
     * <p>
     * An important observation is that a segment of the
     * array can form a valid chunk if, when sorted,
     * it matches the corresponding segment
     * in the fully sorted version of the array.
     * <p>
     * Since the numbers in arr belong to the range [0, n - 1], we can simplify the problem by using the property of sums. Specifically, for any index, it suffices to check whether the sum of the elements in arr up to that index is equal to the sum of the elements in the corresponding prefix of the sorted array.
     * <p>
     * If these sums are equal, it guarantees that the elements in the current segment of arr match the elements in the corresponding segment of the sorted array (possibly in a different order). When this condition is satisfied, we can form a new chunk — either starting from the beginning of the array or the end of the previous chunk.
     * <p>
     * For example, consider arr = [1, 2, 0, 3, 4] and the sorted version sortedArr = [0, 1, 2, 3, 4]. We find the valid segments as follows:
     * <p>
     * Segment [0, 0] is not valid, since sum = 1 and sortedSum = 0.
     * Segment [0, 1] is not valid, since sum = 1 + 2 = 3 and sortedSum = 0 + 1 = 1.
     * Segment [0, 2] is valid, since sum = 1 + 2 + 0 = 3 and sortedSum = 0 + 1 + 2 = 3.
     * Segment [3, 3] is valid, because sum = 1 + 2 + 0 + 3 = 6 and sortedSum = 0 + 1 + 2 + 3 = 6.
     * Segment [4, 4] is valid, because sum = 1 + 2 + 0 + 3 + 4 = 10 and sortedSum = 0 + 1 + 2 + 3 + 4 = 10.
     * Therefore, the answer here is 3.
     * <p>
     * Algorithm
     * - Initialize n to the size of the arr array.
     * - Initialize chunks, prefixSum, and sortedPrefixSum to 0.
     * - Iterate over arr with i from 0 to n - 1:
     * - Increment prefixSum by arr[i].
     * - Increment sortedPrefixSum by i.
     * - Check if prefixSum == sortedPrefixSum:
     * - If so, increment chunks by 1.
     * - Return chunks.
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
     * IDEA:
     * <p>
     * <p>
     * Building on the above observation, we further notice that for each number in the array, we have two options: we can either include it in the same chunk as the previous number or create a new chunk for it. However, we must consider the limitation that a new chunk at index i can only be created if all the numbers in the current and previous chunks (the "prefix" of the array) are smaller than all the numbers in the following chunks (the "suffix" of the array). This is equivalent to checking whether:
     * <p>
     * max(prefix[0:i])<min(suffix[i:n]).
     * ​
     * <p>
     * Since we aim to find the largest possible number of chunks, we will choose the second option (i.e., create a new chunk) whenever the above condition is satisfied. Therefore, the problem reduces to counting how many indices in the array satisfy this condition.
     * <p>
     * Algorithm
     * - Initialize n to the size of the arr array.
     * - Initialize prefixMax and suffixMin arrays to arr.
     * - Iterate over arr with i from 1 to n - 1:
     * - Set prefixMax[i] = max(prefixMax[i], prefixMax[i-1]).
     * - Iterate over arr with i from n - 2 to 0:
     * - Set suffixMin[i] = min(suffixMin[i], suffixMin[i+1]).
     * - Initialize chunks to 0.
     * - Iterate over arr with i from 0 to n - 1:
     * - Check if i == 0 (create a chunk for the first element) or suffixMin[i] > prefixMax[i - 1].
     * - If true, increment chunks by 1.
     * - Return chunks.
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

    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxChunksToSorted_1_3(int[] arr) {
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
