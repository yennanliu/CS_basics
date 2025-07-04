package LeetCodeJava.Greedy;

// https://leetcode.com/problems/longest-turbulent-subarray/
/**
 * 978. Longest Turbulent Subarray
 * Medium
 * Topics
 * Companies
 * Given an integer array arr, return the length of a maximum size turbulent subarray of arr.
 *
 * A subarray is turbulent if the comparison sign flips between each adjacent pair of elements in the subarray.
 *
 * More formally, a subarray [arr[i], arr[i + 1], ..., arr[j]] of arr is said to be turbulent if and only if:
 *
 * For i <= k < j:
 * arr[k] > arr[k + 1] when k is odd, and
 * arr[k] < arr[k + 1] when k is even.
 * Or, for i <= k < j:
 * arr[k] > arr[k + 1] when k is even, and
 * arr[k] < arr[k + 1] when k is odd.
 *
 *
 * Example 1:
 *
 * Input: arr = [9,4,2,10,7,8,8,1,9]
 * Output: 5
 * Explanation: arr[1] > arr[2] < arr[3] > arr[4] < arr[5]
 * Example 2:
 *
 * Input: arr = [4,8,12,16]
 * Output: 2
 * Example 3:
 *
 * Input: arr = [100]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 4 * 104
 * 0 <= arr[i] <= 109
 *
 *
 */
public class LongestTurbulentSubarray {

  /**
   * NOTE !!!
   *
   *  🌀 Turbulent Definition:
   *
   * A subarray is turbulent if the comparison signs between adjacent elements alternate:
   *
   *  e.g.:
   *
   * 	•	arr[i-1] < arr[i] > arr[i+1]
   *    or
   * 	•	arr[i-1] > arr[i] < arr[i+1].
   */

  // V0
  // IDEA: `Turbulent` definition + Integer.compare (fixed by gpt)
  public int maxTurbulenceSize(int[] arr) {
      int n = arr.length;
      if (n == 1)
          return 1;

      /**
       * maxLen:
       *    stores the maximum turbulent subarray length found so far.
       *    Starts at 1 (any element alone is trivially turbulent).
       *
       * start: the start index of the current turbulent window.
       *
       */
      int maxLen = 1;
      int start = 0;

      /**
       * Start from the second element.
       *
       * We’re comparing arr[i - 1] and arr[i], so i starts at 1.
       *
       */
      for (int i = 1; i < n; i++) {

          /**
           * Compares adjacent elements and stores the result in cmp:
           *
           *   - Returns -1 if arr[i - 1] < arr[i]
           *
           *   - Returns 0 if arr[i - 1] == arr[i]
           *
           *   - Returns 1 if arr[i - 1] > arr[i]
           *
           *
           *  -> This is how we capture the direction of change.
           *
           */
          int cmp = Integer.compare(arr[i - 1], arr[i]);

          /**
           * If two adjacent elements are equal, the turbulence breaks.
           *
           * We reset the start of our current window to i.
           *
           */
          if (cmp == 0) {
              // Equal values: turbulence broken, reset window
              // NOTE !!! we `reset` window below
              start = i;
          }
          /**
           *  NOTE !!!
           *
           *  We check two things:
           *
           * 1) i == n - 1:
           *         if we’re at the `last` element,
           *         we can’t compare with the next one,
           *         so we treat this as the END of the window.
           *
           *
           * 2) cmp * Integer.compare(arr[i], arr[i + 1]) != -1:
           *
           *    - NOTE !!
           *      - cmp : `direction` between the i and i-1 element
           *      - Integer.compare(arr[i], arr[i + 1]): `direction` between the i and i+1 element
           *      - the reason we don't use `Integer.compare(arr[i], arr[i+1])`
           *         -> is that i could equal arr.len -1, so it use above could cause out of boundary error
           *
           *   - Checks whether the comparison direction `flips`.
           *
           *   - If cmp = 1 (descending), the next should be ascending -1 → product = -1
           *
           *   - If cmp = -1, the next should be descending 1 → product = -1
           *
           *  -  Anything else (1*1, -1*-1, or 0) → turbulence is broken
           *
           */
          else if (i == n - 1 || cmp * Integer.compare(arr[i], arr[i + 1]) != -1) {
              // Turbulence breaks at next step — record max
              /**
               *
               *  NOTE !!!  if `Turbulence` breaks
               *
               *
               *  - Update the maxLen if the current turbulent
               *    sub-array is longer.
               *
               * -  Reset start to begin a new window from i.
               *
               */
              maxLen = Math.max(maxLen, i - start + 1);
              start = i;
          }
      }

      // Return the maximum turbulent window size found.
      return maxLen;
  }

  // V0-1
  // IDEA: SLIDING WINDOW (gpt)
  public int maxTurbulenceSize_0_1(int[] arr) {
        int n = arr.length;
        if (n == 1)
            return 1;

        /**
         * maxLen:
         *    stores the maximum turbulent subarray length found so far.
         *    Starts at 1 (any element alone is trivially turbulent).
         *
         * start: the start index of the current turbulent window.
         *
         */
        int maxLen = 1;
        int start = 0;

        /**
         * Start from the second element.
         *
         * We’re comparing arr[i - 1] and arr[i], so i starts at 1.
         *
         */
        for (int i = 1; i < n; i++) {

            /**
             * Compares adjacent elements and stores the result in cmp:
             *
             *   - Returns -1 if arr[i - 1] < arr[i]
             *
             *   - Returns 0 if arr[i - 1] == arr[i]
             *
             *   - Returns 1 if arr[i - 1] > arr[i]
             *
             *
             *  -> This is how we capture the direction of change.
             *
             */
            int cmp = Integer.compare(arr[i - 1], arr[i]);

            /**
             * If two adjacent elements are equal, the turbulence breaks.
             *
             * We reset the start of our current window to i.
             *
             */
            if (cmp == 0) {
                // Equal values: turbulence broken, reset window
                // NOTE !!! we `reset` window below
                start = i;
            }
            /**
             *  NOTE !!!
             *
             *  We check two things:
             *
             * 1) i == n - 1:
             *         if we’re at the `last` element,
             *         we can’t compare with the next one,
             *         so we treat this as the END of the window.
             *
             *
             * 2) cmp * Integer.compare(arr[i], arr[i + 1]) != -1:
             *
             *    - NOTE !!
             *      - cmp : `direction` between the i and i-1 element
             *      - Integer.compare(arr[i], arr[i + 1]): `direction` between the i and i+1 element
             *      - the reason we don't use `Integer.compare(arr[i], arr[i+1])`
             *         -> is that i could equal arr.len -1, so it use above could cause out of boundary error
             *
             *   - Checks whether the comparison direction `flips`.
             *
             *   - If cmp = 1 (descending), the next should be ascending -1 → product = -1
             *
             *   - If cmp = -1, the next should be descending 1 → product = -1
             *
             *  -  Anything else (1*1, -1*-1, or 0) → turbulence is broken
             *
             */
            else if (i == n - 1 || cmp * Integer.compare(arr[i], arr[i + 1]) != -1) {
                // Turbulence breaks at next step — record max
                /**
                 *
                 *  NOTE !!!  if `Turbulence` breaks
                 *
                 *
                 *  - Update the maxLen if the current turbulent
                 *    sub-array is longer.
                 *
                 * -  Reset start to begin a new window from i.
                 *
                 */
                maxLen = Math.max(maxLen, i - start + 1);
                start = i;
            }
        }

        // Return the maximum turbulent window size found.
        return maxLen;
    }

    // V1
    // https://www.youtube.com/watch?v=V_iHUhR8Dek
    // https://github.com/neetcode-gh/leetcode/blob/main/python%2F0978-longest-turbulent-subarray.py
    // python

    // V2
    // https://leetcode.com/problems/longest-turbulent-subarray/editorial/
    // IDEA: SLIDING WINDOW
    public int maxTurbulenceSize_2(int[] A) {
        int N = A.length;
        int ans = 1;
        int anchor = 0;

        for (int i = 1; i < N; ++i) {
            int c = Integer.compare(A[i - 1], A[i]);
            if (c == 0) {
                anchor = i;
            } else if (i == N - 1 || c * Integer.compare(A[i], A[i + 1]) != -1) {
                ans = Math.max(ans, i - anchor + 1);
                anchor = i;
            }
        }

        return ans;
    }

    // V3
    // https://leetcode.com/problems/longest-turbulent-subarray/solutions/222146/pythonjavac-on-time-o1-space-simple-and-wrfud/
    public int maxTurbulenceSize_3(int[] A) {
        int best = 0, clen = 0;

        for (int i = 0; i < A.length; i++) {
            if (i >= 2 && ((A[i - 2] > A[i - 1] && A[i - 1] < A[i]) ||
                    (A[i - 2] < A[i - 1] && A[i - 1] > A[i]))) {
                // If the last three elements are turbulent, increment run length by 1.
                clen++;
            } else if (i >= 1 && A[i - 1] != A[i]) {
                // The last three elements are not turbulent, so we'll reset the run length.
                // If the previous and current elements are not equal, the new run length is 2.
                clen = 2;
            } else {
                // Otherwise, the new run length is 1.
                clen = 1;
            }
            best = Math.max(best, clen);
        }
        return best;
    }

    // V4
    // https://leetcode.com/problems/longest-turbulent-subarray/solutions/4708023/javakadanes-algorithm-easy-approach-to-l-36mv/
    public int maxTurbulenceSize_4(int[] arr) {

        if (arr.length <= 1)
            return arr.length;

        int maxOddStart = 1, maxEvenStart = 1, currOddStart = 1, currEvenStart = 1;

        for (int i = 1; i < arr.length; i++) {

            if (i % 2 == 0) {

                currOddStart = arr[i - 1] > arr[i] ? currOddStart + 1 : 1;
                currEvenStart = arr[i - 1] < arr[i] ? currEvenStart + 1 : 1;

            } else {

                currOddStart = arr[i - 1] < arr[i] ? currOddStart + 1 : 1;
                currEvenStart = arr[i - 1] > arr[i] ? currEvenStart + 1 : 1;

            }

            maxOddStart = Math.max(maxOddStart, currOddStart);
            maxEvenStart = Math.max(maxEvenStart, currEvenStart);

        }

        return Math.max(maxOddStart, maxEvenStart);

    }


}
