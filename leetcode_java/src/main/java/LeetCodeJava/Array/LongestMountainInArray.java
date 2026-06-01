package LeetCodeJava.Array;

// https://leetcode.com/problems/longest-mountain-in-array/description/
/**
 *  845. Longest Mountain in Array
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You may recall that an array arr is a mountain array if and only if:
 *
 * arr.length >= 3
 * There exists some index i (0-indexed) with 0 < i < arr.length - 1 such that:
 * arr[0] < arr[1] < ... < arr[i - 1] < arr[i]
 * arr[i] > arr[i + 1] > ... > arr[arr.length - 1]
 * Given an integer array arr, return the length of the longest subarray, which is a mountain. Return 0 if there is no mountain subarray.
 *
 *
 *
 * Example 1:
 *
 * Input: arr = [2,1,4,7,3,2,5]
 * Output: 5
 * Explanation: The largest mountain is [1,4,7,3,2] which has length 5.
 * Example 2:
 *
 * Input: arr = [2,2,2]
 * Output: 0
 * Explanation: There is no mountain.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 104
 * 0 <= arr[i] <= 104
 *
 *
 * Follow up:
 *
 * Can you solve it using only one pass?
 * Can you solve it in O(1) space?
 *
 */
public class LongestMountainInArray {

    // V0
    // IDEA: Two-Pointer Expansion approach (gemini)
    // NOTE !!! this is NOT a slide window approach
    /**
     * Time: O(N²)
     * Space: O(1)
     */
    public int longestMountain(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }

        int maxLen = 0;
        int n = arr.length;

        /** NOTE !!!
         *
         *  start from i = 1,
         *  and end at n - 2
         */
        // A peak cannot exist at the first (0) or last (n-1) index
        for (int i = 1; i < n - 1; i++) {
            // Step 1: Identify a valid peak
            /** NOTE !!!
             *
             *  ONLY proceed if the [i-1, i, i+1]
             *  is a `valid` peak
             */
            if (arr[i] > arr[i - 1] && arr[i] > arr[i + 1]) {

                // Step 2: Expand left to find the starting base
                int left = i - 1;
                /** NOTE !!!
                 *
                 *  `while` loop
                 */
                while (left > 0 && arr[left] > arr[left - 1]) {
                    left--;
                }

                // Step 3: Expand right to find the ending base
                int right = i + 1;
                /** NOTE !!!
                 *
                 *  `while` loop
                 */
                while (right < n - 1 && arr[right] > arr[right + 1]) {
                    right++;
                }

                // Step 4: Calculate length and
                // track the maximum seen so far
                int currentLen = right - left + 1;
                maxLen = Math.max(maxLen, currentLen);

                /**  NOTE !!!!
                 *
                 *  via below, we make time complexity
                 *  from O(N * N) to O(N)
                 *
                 *
                 *  -> Without that line, your code would back up and
                 *     `recheck` indices it has already visited.
                 *     With it, the algorithm is forced to only move forward.
                 *
                 *
                 * ----
                 *
                 */
                // OPTIMIZATION: Skip 'i' directly to the right base
                // to avoid re-scanning the descending slope
                /** NOTE !!!
                 *
                 *  below optimization is optional,
                 *  with it, time complexity is O(N);
                 *  without it, time complexity is O(N * N);
                 */
                i = right;
            }
        }

        return maxLen;
    }


    // V0-1
    // IDEA: 2 POINTERS (gpt)
    /**
     * Time: O(N²)
     * Space: O(1)
     */
    public int longestMountain_0_1(int[] arr) {

        int ans = 0;

        for (int i = 1; i < arr.length - 1; i++) {
            ans = Math.max(ans, getMountainLen(arr, i));
        }

        return ans;
    }

    private int getMountainLen(int[] arr, int peak) {
        // edge
        if (peak == 0 || peak == arr.length - 1) {
            return 0;
        }

        /** NOTE !!!
         *
         *  the edge case:
         *  if either `left` or `right` height is bigger than peak
         *  at the beginning
         *
         *  -> NOT a valid, should return 0 directly
         */
        // peak must be local maximum
        if (arr[peak] <= arr[peak - 1]
                || arr[peak] <= arr[peak + 1]) {
            return 0;
        }

        int l = peak;
        int r = peak;

        /** NOTE !!!
         *
         *   keep moving `left` pointer,
         *   when arr[l - 1] < arr[l] and l still in boundary
         */
        while (l > 0 && arr[l - 1] < arr[l]) {
            l--;
        }

        /** NOTE !!!
         *
         *   keep moving `right` pointer,
         *   when arr[r] > arr[r + 1] and r still in boundary
         */
        while (r < arr.length - 1
                && arr[r] > arr[r + 1]) {
            r++;
        }

        /** NOTE !!!
         *
         *  how we get len
         */
        return r - l + 1;
    }


    // V0-2
    // IDEA: Two-Pointer Expansion approach (gemini)
    // (similar to finding the longest palindromic substring).
    /**
     * Time: O(N²)
     * Space: O(1)
     */
    /**  Dry run
     *
     * ===================================================================================================
     * | Index (i) | arr[i] | Peak Detected? | Left Base Expansion (left) | Right Base Expansion (right) | Len |
     * ===================================================================================================
     * |     1     |   1    | 1 > 2 (False)  | —                          | —                            |  0  |
     * |     2     |   4    | 4 > 7 (False)  | —                          | —                            |  0  |
     * |     3     |   7    | 7>4 && 7>3 (Yes)| left stops at idx 1 (val 1)| right stops at idx 5 (val 2)  |  5  |
     * ---------------------------------------------------------------------------------------------------
     * |  * i skips directly to index 5 (val 2) to continue checking for the next peak * |
     * ---------------------------------------------------------------------------------------------------
     * |     5     |   2    | 2 > 5 (False)  | —                          | —                            |  5  |
     * ===================================================================================================
     * Final Maximum Length Returned: 5 (representing the mountain subarray [1, 4, 7, 3, 2])
     *
     *
     */
    public int longestMountain_0_2(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }

        int maxLen = 0;
        int n = arr.length;

        // A peak cannot exist at the first (0) or last (n-1) index
        for (int i = 1; i < n - 1; i++) {
            // Step 1: Identify a valid peak
            if (arr[i] > arr[i - 1] && arr[i] > arr[i + 1]) {

                // Step 2: Expand left to find the starting base
                int left = i - 1;
                while (left > 0 && arr[left] > arr[left - 1]) {
                    left--;
                }

                // Step 3: Expand right to find the ending base
                int right = i + 1;
                while (right < n - 1 && arr[right] > arr[right + 1]) {
                    right++;
                }

                // Step 4: Calculate length and track the maximum seen so far
                int currentLen = right - left + 1;
                maxLen = Math.max(maxLen, currentLen);

                /**  NOTE !!!!
                 *
                 *  via below, we make time complexity
                 *  from O(N * N) to O(N)
                 *
                 *
                 *  -> Without that line, your code would back up and
                 *     `recheck` indices it has already visited.
                 *     With it, the algorithm is forced to only move forward.
                 *
                 *
                 * ----
                 *
                 */
                // OPTIMIZATION: Skip 'i' directly to the right base
                // to avoid re-scanning the descending slope
                i = right;
            }
        }

        return maxLen;
    }


    // V0-3
    // IDEA: Two-Pointer Expansion approach (GPT)
    public int longestMountain_0_3(int[] arr) {
        int n = arr.length;
        int maxLen = 0;

        int i = 1;

        while (i < n - 1) {

            // Check whether arr[i] is a peak
            if (arr[i - 1] < arr[i] && arr[i] > arr[i + 1]) {

                int left = i;
                int right = i;

                // Expand left (strictly increasing)
                while (left > 0 && arr[left - 1] < arr[left]) {
                    left--;
                }

                // Expand right (strictly decreasing)
                while (right < n - 1 && arr[right] > arr[right + 1]) {
                    right++;
                }

                maxLen = Math.max(maxLen, right - left + 1);

                // Skip the mountain we just processed
                i = right;
            }

            i++;
        }

        return maxLen;
    }


    // V1
    // IDEA: 2 POINTERS
    // https://leetcode.com/problems/longest-mountain-in-array/editorial/
    public int longestMountain_1(int[] A) {
        int N = A.length;
        int ans = 0, base = 0;
        while (base < N) {
            int end = base;
            // if base is a left-boundary
            if (end + 1 < N && A[end] < A[end + 1]) {
                // set end to the peak of this potential mountain
                while (end + 1 < N && A[end] < A[end + 1])
                    end++;

                // if end is really a peak..
                if (end + 1 < N && A[end] > A[end + 1]) {
                    // set end to the right-boundary of mountain
                    while (end + 1 < N && A[end] > A[end + 1])
                        end++;
                    // record candidate answer
                    ans = Math.max(ans, end - base + 1);
                }
            }

            base = Math.max(end, base + 1);
        }

        return ans;
    }



    // V2
    // https://leetcode.com/problems/longest-mountain-in-array/solutions/8234413/beats-100-peak-finding-two-pointers-java-pp31/
    public int longestMountain_2(int[] arr) {
        int n = arr.length;
        if (n < 3) {
            return 0;
        }
        int maxLen = 0;

        for (int i = 1; i < n - 1; i++) {
            // Identify a peak
            if (arr[i] > arr[i + 1] && arr[i] > arr[i - 1]) {
                int left = i - 1;
                int right = i + 1;

                // Expand down the left slope
                while (left > 0 && arr[left] > arr[left - 1]) {
                    left--;
                }
                // Expand down the right slope
                while (right < n - 1 && arr[right + 1] < arr[right]) {
                    right++;
                }

                maxLen = Math.max(maxLen, right - left + 1);

                // Fast-forward i to the end of the right slope
                i = right;
            }
        }
        return maxLen;
    }


    // V3
    // https://leetcode.com/problems/longest-mountain-in-array/solutions/7532696/easy-to-understand-java-solution-beats-7-jg5r/
    public int longestMountain_3(int[] arr) {
        // A mountain needs at least 3 elements
        if (arr.length < 3)
            return 0;
        int ans = 0;
        int si = 0; // si -> start index of a potential mountain
        int i = si + 1; //i -> moving pointer to scan the array
        while (i < arr.length) {
            // If the slope is flat or decreasing, a mountain cannot start here.
            // Move the start index forward and continue scanning.
            if (arr[i] <= arr[i - 1]) {
                si = i;
                i++;
                continue;
            }
            // Climb up: strictly increasing sequence until we reach the peak
            while (i < arr.length && arr[i] > arr[i - 1]) {
                i++;
            }
            // If we reached the end or encountered a flat slope,
            // then this cannot form a valid mountain
            // Reset start
            if (i == arr.length || arr[i] == arr[i - 1])
                continue;

            // Descend: strictly decreasing sequence after the peak
            while (i < arr.length && arr[i] < arr[i - 1])
                i++;

            // i is already one step past the last valid element of the mountain,
            // so the mountain length is (i - si)
            ans = Math.max(i - si, ans);

            // Reset start index to the last element of the mountain
            // This allows detection of overlapping mountains
            si = i - 1;
        }

        return ans;
    }







}
