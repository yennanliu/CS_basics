package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/description/
/**
 * 1031. Maximum Sum of Two Non-Overlapping Subarrays
 * Medium
 * Topics
 * Companies
 * Hint
 * Given an integer array nums and two integers firstLen and secondLen, return the maximum sum of elements in two non-overlapping subarrays with lengths firstLen and secondLen.
 *
 * The array with length firstLen could occur before or after the array with length secondLen, but they have to be non-overlapping.
 *
 * A subarray is a contiguous part of an array.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [0,6,5,2,2,5,1,9,4], firstLen = 1, secondLen = 2
 * Output: 20
 * Explanation: One choice of subarrays is [9] with length 1, and [6,5] with length 2.
 * Example 2:
 *
 * Input: nums = [3,8,1,3,2,1,8,9,0], firstLen = 3, secondLen = 2
 * Output: 29
 * Explanation: One choice of subarrays is [3,8,1] with length 3, and [8,9] with length 2.
 * Example 3:
 *
 * Input: nums = [2,1,5,6,0,9,5,0,3,8], firstLen = 4, secondLen = 3
 * Output: 31
 * Explanation: One choice of subarrays is [5,6,0,9] with length 4, and [0,3,8] with length 3.
 *
 *
 * Constraints:
 *
 * 1 <= firstLen, secondLen <= 1000
 * 2 <= firstLen + secondLen <= 1000
 * firstLen + secondLen <= nums.length <= 1000
 * 0 <= nums[i] <= 1000
 *
 */
public class MaximumSumOfTwoNonOverlappingSubarrays {

  // V0
  //    public int maxSumTwoNoOverlap(int[] nums, int firstLen, int secondLen) {
  //
  //    }

  // V0-1
  // IDEA: PREFIX SUM
  // time: O(N), space: O(N)
  /**
   *  Does This Cover All Configurations?
   *
   * Yes, it does. By handling the two cases separately, the algorithm ensures:
   * 	•	All valid configurations are checked:
   * 	•	firstLen subarray is before secondLen.
   * 	•	secondLen subarray is before firstLen.
   * 	•	There’s no need to explicitly consider cases where:
   * 	•	secondLen subarray comes before firstLen in the “case 1” loop because “case 2” handles that.
   * 	•	Similarly, the converse is true for “case 2”.
   *
   */
  public int maxSumTwoNoOverlap_0_1(int[] nums, int firstLen, int secondLen) {
        int n = nums.length;
        int[] s = new int[n + 1];
        for (int i = 0; i < n; ++i) {
            s[i + 1] = s[i] + nums[i];
        }
        int ans = 0;

    /**
     * where end is exclusive and start is inclusive.
     * 	2.	Two Cases:
     * 	•	Case 1: Calculate the maximum sum when a subarray of length firstLen
     * 	            comes before a subarray of length secondLen.
     *
     * 	•	Case 2: Calculate the maximum sum when a subarray of length secondLen
     * 	            comes before a subarray of length firstLen.
     *
     *
     */

    // case 1) check `firstLen`, then `secondLen`
    for (int i = firstLen, t = 0; i + secondLen - 1 < n; ++i) {
      /**
       *  - Logic:
       *
       * 	1.	Start at index i = firstLen because the first subarray
       *       	must be at least firstLen long.
       *
       * 	2.	Use t to track the maximum sum of a subarray of length firstLen
       *    	ending at or before index i.
       *
       * 	3.	Add the sum of a subarray of length secondLen starting at index i
       *    	and update ans with the combined sum.
       *
       *
       *    NOTE !!!
       *
       *
       *  - In this case:
       *    - The loop ensures the first subarray (firstLen) is placed before the second subarray (secondLen).
       * 	- The variable t keeps track of the maximum sum of the firstLen subarray that ends before the second subarray starts at index i.
       *  - Important Note:
       *    - The secondLen subarray must always start after the firstLen subarray ends because the iteration proceeds sequentially.
       */
      t = Math.max(t, s[i] - s[i - firstLen]); // Maximum sum of `firstLen` ending at or before `i`
            ans = Math.max(ans, t + s[i + secondLen] - s[i]); // Combine with `secondLen` starting at `i`
        }
    // case 2) check `secondLen`, then `firstLen`

    /**
     *  - Logic
     *
     *      * 	1.	Start at index i = secondLen because the first subarray must be at least secondLen long.
     *      * 	2.	Use t to track the maximum sum of a subarray of length secondLen ending at or before index i.
     *      * 	3.	Add the sum of a subarray of length firstLen starting at index i and update ans with the combined sum.
     *
     *  - Goal: Calculate the maximum sum where a subarray of length secondLen comes before a subarray of length firstLen.
     *
     *  NOTE !!!
     *
     * - In this case:
     *      - The loop ensures the first subarray (secondLen) is placed before the second subarray (firstLen).
     *      - The variable t keeps track of the maximum sum of the secondLen subarray that ends before the first subarray starts at index i.
     * 	- Important Note:
     *      - The firstLen subarray must always start after the secondLen subarray ends because the iteration proceeds sequentially.
     *
     */
    for (int i = secondLen, t = 0; i + firstLen - 1 < n; ++i) {
            t = Math.max(t, s[i] - s[i - secondLen]);
            ans = Math.max(ans, t + s[i + firstLen] - s[i]);
        }
        return ans;
    }


    // V1
    // https://leetcode.ca/2018-09-26-1031-Maximum-Sum-of-Two-Non-Overlapping-Subarrays/
    // IDEA: PREFIX SUM
    // time: O(N), space: O(N)
    public int maxSumTwoNoOverlap_1(int[] nums, int firstLen, int secondLen) {
        int n = nums.length;
        int[] s = new int[n + 1];
        for (int i = 0; i < n; ++i) {
            s[i + 1] = s[i] + nums[i];
        }
        int ans = 0;
        // case 1)  check `firstLen`, then `secondLen`
        for (int i = firstLen, t = 0; i + secondLen - 1 < n; ++i) {
            t = Math.max(t, s[i] - s[i - firstLen]);
            ans = Math.max(ans, t + s[i + secondLen] - s[i]);
        }
        // case 2)  check  `secondLen`, then `firstLen`
        for (int i = secondLen, t = 0; i + firstLen - 1 < n; ++i) {
            t = Math.max(t, s[i] - s[i - secondLen]);
            ans = Math.max(ans, t + s[i + firstLen] - s[i]);
        }
        return ans;
    }

    // V1-2
    // IDEA: PREFIX SUM
    // https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/solutions/1489581/java-easy-to-understand-prefix-sum-by-rm-5d2z/
    // time: O(N), space: O(N)
    public int maxSumTwoNoOverlap_1_2(int[] A, int L, int M) {
        int sums[] = new int[A.length + 1];

        for (int i = 1; i <= A.length; i++)
            sums[i] = A[i - 1] + sums[i - 1];

        int maxLval = 0;
        int ans = 0;
        for (int i = L; i <= A.length - M; i++) {
            maxLval = Math.max(maxLval, sums[i] - sums[i - L]);
            ans = Math.max(ans, sums[i + M] - sums[i] + maxLval);
        }
        int maxRval = 0;
        for (int i = M; i <= A.length - L; i++) {
            maxRval = Math.max(maxRval, sums[i] - sums[i - M]);
            ans = Math.max(ans, sums[i + L] - sums[i] + maxRval);
        }
        return ans;
    }

    // V2
    // IDEA: PREFIX SUM
    // https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/solutions/5171249/java-clean-solution-using-prefix-sum-det-lxok/
    // time: O(N), space: O(N)
    // Drive Function
    public int maxSumTwoNoOverlap_2(int[] nums, int firstLen, int secondLen) {
        int[] prefix = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        return Math.max(solveHelper(prefix, firstLen, secondLen),
                solveHelper(prefix, secondLen, firstLen));
    }

    private int solveHelper(int[] prefix, int len1, int len2) {
        int res = 0;
        int maxLength = Integer.MIN_VALUE;
        for (int i = len1 + len2; i < prefix.length; i++) {
            maxLength = Math.max(maxLength,
                    prefix[i - len2] - prefix[i - len2 - len1]);
            res = Math.max(res, maxLength + prefix[i] - prefix[i - len2]);
        }
        return res;
    }

    // V3
    // https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/solutions/913234/on-thought-process-java-by-133c7-wn3a/
    // time: O(N), space: O(N)
    public int maxSumTwoNoOverlap_3(int[] A, int L, int M) {
        int n = A.length;
        if (n == 0) {
            return 0;
        }
        if (L + M > n) {
            throw new IllegalArgumentException();
        }

        int[] Ls = new int[n];
        int[] Ms = new int[n];

        int lSum = 0;
        int mSum = 0;

        for (int i = 0; i <= n - L; i++) {
            if (i == 0) {
                for (int j = 0; j < L; j++) {
                    lSum += A[j];
                }
            } else {
                lSum = lSum - A[i - 1] + A[i + L - 1];
            }

            Ls[i] = lSum;
        }

        for (int i = 0; i <= n - M; i++) {
            if (i == 0) {
                for (int j = 0; j < M; j++) {
                    mSum += A[j];
                }
            } else {
                mSum = mSum - A[i - 1] + A[i + M - 1];
            }

            Ms[i] = mSum;
        }

        int[] lmax = new int[n + 1];
        int[] mmax = new int[n + 1];

        for (int i = n - 1; i >= 0; i--) {
            lmax[i] = Math.max(lmax[i + 1], Ls[i]);
            mmax[i] = Math.max(mmax[i + 1], Ms[i]);
        }

        int ans = 0;
        for (int i = 0; i <= n - L - 1; i++) {
            ans = Math.max(ans, Ls[i] + mmax[i + L]);
        }
        for (int i = 0; i <= n - M - 1; i++) {
            ans = Math.max(ans, Ms[i] + lmax[i + M]);
        }

        return ans;
    }

}
