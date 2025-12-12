package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-alternating-subarray/description/
/**
 * 2765. Longest Alternating Subarray
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed integer array nums. A subarray s of length m is called alternating if:
 *
 * m is greater than 1.
 * s1 = s0 + 1.
 * The 0-indexed subarray s looks like [s0, s1, s0, s1,...,s(m-1) % 2]. In other words, s1 - s0 = 1, s2 - s1 = -1, s3 - s2 = 1, s4 - s3 = -1, and so on up to s[m - 1] - s[m - 2] = (-1)m.
 * Return the maximum length of all alternating subarrays present in nums or -1 if no such subarray exists.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,4,3,4]
 *
 * Output: 4
 *
 * Explanation:
 *
 * The alternating subarrays are [2, 3], [3,4], [3,4,3], and [3,4,3,4]. The longest of these is [3,4,3,4], which is of length 4.
 *
 * Example 2:
 *
 * Input: nums = [4,5,6]
 *
 * Output: 2
 *
 * Explanation:
 *
 * [4,5] and [5,6] are the only two alternating subarrays. They are both of length 2.
 *
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 100
 * 1 <= nums[i] <= 104
 *
 */
public class LongestAlternatingSubarray {

    // V0
//    public int alternatingSubarray(int[] nums) {
//
//    }

    // V0-1
    // IDEA: BRUTE FORCE (double loop) (fixed by gpt)
    public int alternatingSubarray_0_1(int[] nums) {
        int n = nums.length;
        int res = -1;

        for (int i = 0; i < n - 1; i++) {
            if (nums[i + 1] - nums[i] != 1)
                continue;

            int expected = -1; // after +1, we expect -1

            /**  NOTE !!!
             *
             *  Question:
             *
             *  For below code, why do we do j=i+2 ?
             *  but NOT j=i+1 ? i thought we move idx 1 unit per iteration ?
             *
             *  Answer (gpt):
             *
             *
             *  -> We start j = i + 2 because the subarray [i, i+1]
             *     is `already` `validated`,
             *     and the INNER loop is only responsible
             *     for `extending it beyond length 2.`
             *
             *   If we started with j = i + 1, we would:
             *
             *    - re-check work we already did, and
             *    - break the alternation logic (expected = -1) immediately.
             *
             *
             *  ----
             *
             *   Step by step demo:
             *   
             * ## Step-by-Step Breakdown
             *
             * ### 1️⃣ What the outer loop guarantees
             *
             * ```java
             * if (nums[i + 1] - nums[i] != 1) continue;
             * ```
             *
             * At this point, we already know:
             *
             * ```
             * nums[i+1] - nums[i] == +1
             * ```
             *
             * So the subarray:
             *
             * ```
             * [i, i+1]
             * ```
             *
             * ✔ is **valid**
             * ✔ has length **2**
             * ✔ establishes the starting pattern
             *
             * That’s why we immediately do:
             *
             * ```java
             * res = Math.max(res, 2);
             * ```
             *
             * ---
             *
             * ### 2️⃣ What `expected = -1` means
             *
             * ```java
             * int expected = -1;
             * ```
             *
             * This means:
             *
             * > “The **next** difference must be `-1`.”
             *
             * The next difference is:
             *
             * ```
             * nums[j] - nums[j - 1]
             * ```
             *
             * So the **first index** where that makes sense is:
             *
             * ```
             * j = i + 2
             * ```
             *
             * ---
             *
             * ### 3️⃣ Why `j = i + 1` is WRONG
             *
             * If we started with:
             *
             * ```java
             * for (int j = i + 1; j < n; j++)
             * ```
             *
             * Then on the **first iteration**:
             *
             * ```java
             * diff = nums[i+1] - nums[i]
             * ```
             *
             * But we already know:
             *
             * ```
             * diff == +1
             * ```
             *
             * Yet:
             *
             * ```java
             * expected == -1
             * ```
             *
             * So this would immediately break:
             *
             * ```java
             * if (diff != expected) break;  // breaks immediately ❌
             * ```
             *
             * 👉 You’d never extend beyond length 2.
             *
             * ---
             *
             * ### 4️⃣ Conceptual View (Very Important)
             *
             * Think in **differences**, not indices:
             *
             * | Segment                 | Meaning          |
             * | ----------------------- | ---------------- |
             * | `nums[i] → nums[i+1]`   | establishes `+1` |
             * | `nums[i+1] → nums[i+2]` | must be `-1`     |
             * | `nums[i+2] → nums[i+3]` | must be `+1`     |
             * | …                       | alternating      |
             *
             * So:
             *
             * * First diff is **checked outside**
             * * Remaining diffs are checked **inside**
             *
             * That’s why the inner loop starts at **`i + 2`**
             *
             * ---
             *
             * ## Visual Timeline
             *
             * Indices:
             *
             * ```
             * i     i+1     i+2     i+3
             * |-----|-------|-------|
             *    +1      -1      +1
             * ```
             *
             * Code responsibility:
             *
             * ```
             * Outer loop → checks +1
             * Inner loop → checks -1, +1, -1...
             * ```
             *
             * ---
             *
             * ## Key Insight (This Is the “Aha”)
             *
             * > We move the index by **1 per iteration**,
             * > but we **start at `i + 2`** because:
             * >
             * > * `[i, i+1]` is the **base case**
             * > * the loop is about **extension**, not validation
             *
             */
            for (int j = i + 2; j < n; j++) {
                int diff = nums[j] - nums[j - 1];

                if (diff != expected)
                    break;

                expected *= -1;
                res = Math.max(res, j - i + 1);
            }

            res = Math.max(res, 2); // at least [i, i+1] is valid
        }

        return res;
    }

    // V0-2
    // IDEA: SLIDE WINDOW (gemini)
    /**
     * Fix for LC 2765: Longest Alternating Subarray.
     * Handles the case where the expected output for 'no valid subarray' is -1.
     * Time Complexity: O(N)
     */
    public int alternatingSubarray_0_2(int[] nums) {
        if (nums == null || nums.length < 2) {
            // If the platform expects -1 when no valid subarray exists,
            // we must initialize the result to -1.
            return -1;
        }

        // Initialize to -1, as per the expected output for "not found" in the failing test case.
        int maxLength = -1;

        // 'start' tracks the index where the current valid alternating subarray began.
        int start = -1;

        for (int i = 1; i < nums.length; i++) {

            int diff = nums[i] - nums[i - 1];

            if (start == -1) {
                // Case 1: Attempt to START a new window (must start with +1)
                if (diff == 1) {
                    start = i - 1;
                    maxLength = Math.max(maxLength, 2);
                }

            } else {
                // Case 2: Continue or reset the existing window

                int currentWindowLength = i - start;
                int requiredDiff = (currentWindowLength % 2 == 0) ? -1 : 1;

                if (diff == requiredDiff) {
                    // Valid continuation: Expand the window
                    maxLength = Math.max(maxLength, i - start + 1);
                } else {
                    // Sequence broken: Reset the window

                    if (diff == 1) {
                        // Current pair starts a NEW valid sequence (+1).
                        start = i - 1;
                        maxLength = Math.max(maxLength, 2);
                    } else {
                        // Pattern broken AND current pair doesn't start a new sequence.
                        start = -1;
                    }
                }
            }
        }

        return maxLength;
    }


    // V1-1
    // IDEA: BRUTE FORCE
    public int alternatingSubarray_1_1(int[] A) {
        int n = A.length, res = 0, j = 0;
        for (int i = 0; i < n; ++i)
            for (j = i + 1; j < n && A[j] == A[i] + (j - i) % 2; ++j)
                res = Math.max(res, j - i + 1);
        return res > 1 ? res : -1;
    }


    // V1-2
    // IDEA: 2 POINTERS
    // https://leetcode.com/problems/longest-alternating-subarray/solutions/3737191/javacpython-two-pointers-and-dp-solution-4y71/
    public int alternatingSubarray_1_2(int[] A) {
        int n = A.length, res = 0, j = 0;
        for (int i = 0; i < n; i = Math.max(i + 1, j - 1))
            for (j = i + 1; j < n && A[j] == A[i] + (j - i) % 2; ++j)
                res = Math.max(res, j - i + 1);
        return res > 1 ? res : -1;
    }


    // V1-3
    // IDEA: DP
    // https://leetcode.com/problems/longest-alternating-subarray/solutions/3737191/javacpython-two-pointers-and-dp-solution-4y71/
    public int alternatingSubarray_1_3(int[] A) {
        int n = A.length, res = -1, dp = -1;
        for (int i = 1; i < n; ++i, res = Math.max(res, dp))
            if (dp > 0 && A[i] == A[i - 2])
                dp++;
            else
                dp = A[i] == A[i - 1] + 1 ? 2 : -1;
        return res;
    }

    // V2
    // https://leetcode.com/problems/longest-alternating-subarray/solutions/7243610/easy-two-pointer-approach-by-lokeshthaku-au5a/
    // IDEA: 2 POINTERS
    public int alternatingSubarray_2(int[] nums) {
        int ans = -1, i = 0, j = 1, check = 1;
        while (j < nums.length) {
            if (nums[j] - nums[j - 1] == check) {
                j++;
                check *= -1;
            } else {

                if (j - i + 1 > 2) {
                    ans = Math.max(j - i, ans);
                    i = j - 1;
                } else {
                    i = j;
                    j++;
                }
                check = 1;
            }
        }
        if (j - i + 1 > 2) {
            ans = Math.max(j - i, ans);
        }
        return ans;
    }


    // V3
    // IDEA: direction and counter
    // https://leetcode.com/problems/longest-alternating-subarray/solutions/3737352/java-on-solution-without-2-pointers-with-fr4h/
    public int alternatingSubarray_3(int[] nums) {
        int ans = -1;
        int n = nums.length;
        int curr = 1;
        int dir = 1;
        for (int i = 1; i < n; i++) {
            if (nums[i] - nums[i - 1] == dir) {
                curr++;
                dir = -dir;
                ans = Math.max(ans, curr);
            } else {
                if (nums[i] - nums[i - 1] == 1) {
                    curr = 2;
                    dir = -1;
                } else {
                    curr = 1;
                    dir = 1;
                }
            }
        }
        return ans;
    }



}
