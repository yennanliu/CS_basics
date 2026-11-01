package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/find-all-k-distant-indices-in-an-array/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 2200. Find All K-Distant Indices in an Array
 * Easy
 *
 * You are given a 0-indexed integer array nums and two integers key and k. A k-distant index is an index i of nums for which there exists at least one index j such that |i - j| <= k and nums[j] == key.
 *
 * Return a list of all k-distant indices sorted in increasing order.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,4,9,1,3,9,5], key = 9, k = 1
 * Output: [1,2,3,4,5,6]
 * Explanation: Here, nums[2] == key and nums[5] == key.
 * - For index 0, |0 - 2| > k and |0 - 5| > k, so there is no j where |0 - j| <= k and nums[j] == key. Thus, 0 is not a k-distant index.
 * - For index 1, |1 - 2| <= k and nums[2] == key, so 1 is a k-distant index.
 * - For index 2, |2 - 2| <= k and nums[2] == key, so 2 is a k-distant index.
 * - For index 3, |3 - 2| <= k and nums[2] == key, so 3 is a k-distant index.
 * - For index 4, |4 - 5| <= k and nums[5] == key, so 4 is a k-distant index.
 * - For index 5, |5 - 5| <= k and nums[5] == key, so 5 is a k-distant index.
 * - For index 6, |6 - 5| <= k and nums[5] == key, so 6 is a k-distant index.
 * Thus, we return [1,2,3,4,5,6] which is sorted in increasing order.
 *
 * Example 2:
 *
 * Input: nums = [2,2,2,2,2], key = 2, k = 2
 * Output: [0,1,2,3,4]
 * Explanation: For all indices i in nums, there exists some index j such that |i - j| <= k and nums[j] == key, so every index is a k-distant index.
 * Hence, we return [0,1,2,3,4].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= 1000
 * key is an integer from the array nums.
 * 1 <= k <= nums.length
 *
 */
public class FindAllKDistantIndicesInAnArray {

    // V0
    // IDEA: MARK THE INTERVAL [j - k, j + k] AROUND EVERY OCCURRENCE OF key
    //  the windows overlap, so mark a boolean array instead of collecting indices directly;
    //  reading it back in order keeps the output sorted and duplicate-free.
    /**
     * time = O(N * K)
     * space = O(N)
     */
    public List<Integer> findKDistantIndices(int[] nums, int key, int k) {
        int n = nums.length;
        boolean[] marked = new boolean[n];
        for (int j = 0; j < n; j++) {
            if (nums[j] == key) {
                int from = Math.max(0, j - k);
                int to = Math.min(n - 1, j + k);
                for (int i = from; i <= to; i++) {
                    marked[i] = true;
                }
            }
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (marked[i]) {
                result.add(i);
            }
        }
        return result;
    }

    // V1
    // IDEA: ONE PASS WITH A "NEXT UNREPORTED INDEX" POINTER
    //  for each occurrence j of key, report the indices from max(nextIndex, j - k) to
    //  min(n - 1, j + k), then move nextIndex past them. every index is reported at most once,
    //  so the whole pass is linear no matter how much the windows overlap.
    /**
     * time = O(N)
     * space = O(1)   (besides the output)
     */
    public List<Integer> findKDistantIndices_1(int[] nums, int key, int k) {
        int n = nums.length;
        List<Integer> result = new ArrayList<>();
        // smallest index not yet added to result
        int nextIndex = 0;
        for (int j = 0; j < n; j++) {
            if (nums[j] != key) {
                continue;
            }
            int from = Math.max(nextIndex, j - k);
            int to = Math.min(n - 1, j + k);
            for (int i = from; i <= to; i++) {
                result.add(i);
            }
            nextIndex = Math.max(nextIndex, to + 1);
        }
        return result;
    }
}
