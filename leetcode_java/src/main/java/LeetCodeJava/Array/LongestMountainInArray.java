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
//    public int longestMountain(int[] arr) {
//
//    }


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
