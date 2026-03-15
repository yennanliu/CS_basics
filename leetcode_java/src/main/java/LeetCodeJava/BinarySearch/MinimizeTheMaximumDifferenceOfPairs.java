package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimize-the-maximum-difference-of-pairs/description/

import java.util.Arrays;

/**
 *  2616. Minimize the Maximum Difference of Pairs
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed integer array nums and an integer p. Find p pairs of indices of nums such that the maximum difference amongst all the pairs is minimized. Also, ensure no index appears more than once amongst the p pairs.
 *
 * Note that for a pair of elements at the index i and j, the difference of this pair is |nums[i] - nums[j]|, where |x| represents the absolute value of x.
 *
 * Return the minimum maximum difference among all p pairs. We define the maximum of an empty set to be zero.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [10,1,2,7,1,3], p = 2
 * Output: 1
 * Explanation: The first pair is formed from the indices 1 and 4, and the second pair is formed from the indices 2 and 5.
 * The maximum difference is max(|nums[1] - nums[4]|, |nums[2] - nums[5]|) = max(0, 1) = 1. Therefore, we return 1.
 * Example 2:
 *
 * Input: nums = [4,2,1,2], p = 1
 * Output: 0
 * Explanation: Let the indices 1 and 3 form a pair. The difference of that pair is |2 - 2| = 0, which is the minimum we can attain.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 0 <= nums[i] <= 109
 * 0 <= p <= (nums.length)/2
 *
 */
public class MinimizeTheMaximumDifferenceOfPairs {

    // V0
//    public int minimizeMax(int[] nums, int p) {
//
//    }


    /**  NOTE !!!
     *
     * This is a classic "Minimize the Maximum" problem.
     *
     *  Steps:
     *
     *  1. Sort the array so that the closest numbers are adjacent.
     *
     *  2. Binary Search for the "Maximum Difference" (let's call it mid).
     *
     *  3. Greedy Check: Can we find at least p pairs
     *     where each pair's difference is <= mid ?
     *
     */

    /** NOTE !!!
     *
     *  why `PQ` (priority queue) DOES NOT work for this LC problem ?
     *
     *  ----------------
     *
     *  ex 1):
     *
     *   nums = [1, 3, 4, 8, 9]
     *   p = 2
     *
     *
     *  ex 2)
     *
     *   nums = [1, 4, 7, 10, 11, 12]
     *   p = 2
     *
     *
     *  -------
     *
     *  Here is a **small counterexample (6 numbers)**
     *  where the **PQ greedy strategy fails**
     *  but **Binary Search + Greedy works**.
     *
     * ---
     *
     * # Counterexample
     *
     * ```text
     * nums = [1, 4, 7, 10, 11, 12]
     * p = 2
     * ```
     *
     * After sorting:
     *
     * ```text
     * [1, 4, 7, 10, 11, 12]
     * ```
     *
     * Adjacent diffs:
     *
     * ```text
     * (1,4)  = 3
     * (4,7)  = 3
     * (7,10) = 3
     * (10,11)= 1
     * (11,12)= 1
     * ```
     *
     * PQ order:
     *
     * ```text
     * 1, 1, 3, 3, 3
     * ```
     *
     * ---
     *
     * # PQ Greedy Execution
     *
     * ### Step 1
     *
     * Pick smallest diff:
     *
     * ```
     * (10,11) = 1
     * ```
     *
     * Used:
     *
     * ```
     * 10,11
     * ```
     *
     * Current max:
     *
     * ```
     * 1
     * ```
     *
     * ---
     *
     * ### Step 2
     *
     * Next smallest:
     *
     * ```
     * (11,12) = 1
     * ```
     *
     * But **11 already used → skip**
     *
     * Next:
     *
     * ```
     * (1,4) = 3
     * ```
     *
     * Pick it.
     *
     * Used:
     *
     * ```
     * 1,4
     * ```
     *
     * Final pairs:
     *
     * ```
     * (10,11)
     * (1,4)
     * ```
     *
     * Result:
     *
     * ```
     * max = 3
     * ```
     *
     * ---
     *
     * # But Optimal Pairing
     *
     * Correct pairing:
     *
     * ```
     * (10,11) = 1
     * (11,12) = 1 ❌ can't (overlap)
     * ```
     *
     * Better option:
     *
     * ```
     * (10,11) = 1
     * (7,10) ❌ overlap
     * ```
     *
     * But the **true optimal pairing** is:
     *
     * ```
     * (10,11) = 1
     * (11,12) = 1 ❌ invalid
     * ```
     *
     * Wait — still conflict.
     *
     * So optimal valid pairs are:
     *
     * ```
     * (10,11) = 1
     * (4,7) = 3
     * ```
     *
     * max = **3**
     *
     * But let's check another pairing:
     *
     * ```
     * (11,12) = 1
     * (7,10) = 3
     * ```
     *
     * max = **3**
     *
     * So PQ gives **3**, which happens to match.
     *
     * But modify slightly:
     *
     * ---
     *
     * # True Failure Example
     *
     * ```text
     * nums = [1, 3, 4, 6, 7, 20]
     * p = 2
     * ```
     *
     * Sorted:
     *
     * ```
     * [1,3,4,6,7,20]
     * ```
     *
     * Diffs:
     *
     * ```
     * (1,3)=2
     * (3,4)=1
     * (4,6)=2
     * (6,7)=1
     * (7,20)=13
     * ```
     *
     * PQ order:
     *
     * ```
     * 1,1,2,2,13
     * ```
     *
     * ---
     *
     * ### PQ Picks
     *
     * ```
     * (3,4) = 1
     * (6,7) = 1
     * ```
     *
     * Result:
     *
     * ```
     * max = 1
     * ```
     *
     * This is correct again.
     *
     * ---
     *
     * # The Real Issue
     *
     * The real reason **PQ is unsafe** is theoretical:
     *
     * The problem is essentially:
     *
     * ```
     * choose p disjoint edges minimizing the maximum edge weight
     * ```
     *
     * This is a **matching optimization problem**, where **local greedy selection does not guarantee optimality**.
     *
     * Binary search works because:
     *
     * ```
     * If maxDiff works → any larger diff also works
     * ```
     *
     * This **monotonic property** allows safe greedy pairing.
     *
     * ---
     *
     * # Key Insight
     *
     * The greedy pairing rule:
     *
     * ```java
     * if(nums[i] - nums[i-1] <= maxDiff){
     *     pair++
     *     i++
     * }
     * ```
     *
     * is guaranteed to produce the **maximum number of pairs for that maxDiff**.
     *
     * That property **does not exist for PQ selection**.
     *
     * ---
     *
     * ✅ **Summary**
     *
     * | Approach               | Correctness               |
     * | ---------------------- | ------------------------- |
     * | PQ smallest diff first | Sometimes correct         |
     * | Binary search + greedy | Always correct            |
     * | DP                     | Always correct but slower |
     *
     * ---
     *
     */

    // V0-1
    // IDEA: Greedy + Binary Search (GEMINI)
    public int minimizeMax_0_1(int[] nums, int p) {
        // edge
        if (p == 0){
            return 0;
        }

        /** NOTE !!!
         *
         *  we sort array first
         */
        Arrays.sort(nums);

        int n = nums.length;
        /**  NOTE !!!
         *
         * The range of possible answers is [0, max_difference]
         *
         */
        int low = 0;
        int high = nums[n - 1] - nums[0]; // NOTE !!! this

        int res = high;

        /** NOTE !!!
         *
         *  binary search ( <= )
         */
        while (low <= high) {
            int mid = low + (high - low) / 2;

            /** NOTE !!!
             *
             *  if can find `least p pairs` with diff <= mid,
             *
             *  -> we try a `smaller max diff`
             */
            // If we can find at least p pairs with diff <= mid,
            // try a smaller max difference.
            if (canFormPairs(nums, p, mid)) {
                res = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return res;
    }

    private boolean canFormPairs(int[] nums, int p, int maxDiff) {
        int count = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            // If the current pair fits the constraint, take it!
            if (nums[i + 1] - nums[i] <= maxDiff) {
                count++;
                /** NOTE !!!
                 *
                 * `Skip the next index`
                 *   -> because an element can only be in one pair
                 */
                i++;
            }
            if (count >= p)
                return true;
        }
        return count >= p;
    }


    // V0-2
    // IDEA: Greedy + Binary Search (GPT)
    public int minimizeMax_0_2(int[] nums, int p) {

        Arrays.sort(nums);

        int left = 0;
        int right = nums[nums.length - 1] - nums[0];

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (canForm(nums, p, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    private boolean canForm(int[] nums, int p, int maxDiff) {
        int count = 0;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] - nums[i - 1] <= maxDiff) {
                count++;
                i++; // skip next index (avoid reuse)
            }
        }

        return count >= p;
    }

    // V0-3
    // IDEA: DP (gpt) (OUT OF MEMORY ERROR)
    public int minimizeMax_0_3(int[] nums, int p) {
        Arrays.sort(nums);

        int n = nums.length;
        int[][] dp = new int[n + 1][p + 1];

        for (int i = 0; i <= n; i++)
            Arrays.fill(dp[i], Integer.MAX_VALUE);

        dp[0][0] = 0;
        dp[1][0] = 0;

        for (int i = 2; i <= n; i++) {
            for (int k = 0; k <= p; k++) {

                dp[i][k] = dp[i - 1][k];

                if (k > 0 && dp[i - 2][k - 1] != Integer.MAX_VALUE) {
                    int diff = nums[i - 1] - nums[i - 2];
                    dp[i][k] = Math.min(
                            dp[i][k],
                            Math.max(dp[i - 2][k - 1], diff));
                }
            }
        }

        return dp[n][p];
    }



    // V1
    // IDEA: Greedy + Binary Search
    // https://leetcode.com/problems/minimize-the-maximum-difference-of-pairs/editorial/
    // Find the number of valid pairs by greedy approach
    private int countValidPairs(int[] nums, int threshold) {
        int index = 0, count = 0;
        while (index < nums.length - 1) {
            // If a valid pair is found, skip both numbers.
            if (nums[index + 1] - nums[index] <= threshold) {
                count++;
                index++;
            }
            index++;
        }
        return count;
    }

    public int minimizeMax_1(int[] nums, int p) {
        Arrays.sort(nums);
        int n = nums.length;
        int left = 0, right = nums[n - 1] - nums[0];

        while (left < right) {
            int mid = left + (right - left) / 2;

            // If there are enough pairs, look for a smaller threshold.
            // Otherwise, look for a larger threshold.
            if (countValidPairs(nums, mid) >= p) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }


    // V2


    // V3


}
