package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/longest-increasing-subsequence/description/
/**
 * 300. Longest Increasing Subsequence
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an integer array nums, return the length of the longest strictly increasing
 * subsequence
 * .
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [10,9,2,5,3,7,101,18]
 * Output: 4
 * Explanation: The longest increasing subsequence is [2,3,7,101], therefore the length is 4.
 * Example 2:
 *
 * Input: nums = [0,1,0,3,2,3]
 * Output: 4
 * Example 3:
 *
 * Input: nums = [7,7,7,7,7,7,7]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2500
 * -104 <= nums[i] <= 104
 *
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LongestIncreasingSubsequence {

    // V0
    // IDEA : 1D DP
    // TODO : check & implement again
    /**  NOTE !!
     *
     *  1. DP def: dp[i] = the length of LIS ending at index i
     *
     *  2. DP eq:
     *
     *     ```
     *     if (nums[i] > nums[j]) {
     *
     *         // We can append nums[i] to the subsequence ending at j.
     *         // So the new length is dp[j] + 1.
     *         // We take the max to keep the best sequence found so far for index i.
     *
     *
     *         dp[i] = Math.max(dp[i], dp[j] + 1);
     *     }
     *     ```
     */
    /**  NOTE !!!
     *
     *  no need `2D DP` for this LC, likely an overkill approach.
     *
     *  Reason:
     *
     *  - DP Definition: By using dp[i][j],
     *  you are trying to track substrings or ranges,
     *  but LIS doesn't care about ranges; it cares about
     *  the "previous smallest number" you can attach the current number to.
     */
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int lengthOfLIS(int[] nums) {

        if(nums == null || nums.length == 0){
            return 0;
        }

        int n = nums.length;
        // init dp
        // DP def: dp[i] = the length of LIS ending at index i
        int[] dp = new int[nums.length];
        /**
         *
         * NOTE !!!
         *
         * Arrays.fill(dp, 1);
         * -> sets every element in the array dp to the value 1.
         *
         */
        Arrays.fill(dp,1);
        // set res as final result (Longest Increasing Subsequence length) , keep updating it
        int res = 1;
        /**
         * NOTE !!!
         *
         *  main dp logic : dp[i]=Math.max(dp[i],dp[j]+1);
         *
         *  -> we still use "brute force", double looping over i, j
         *  However, we use dp to "memorize" the result already calculated before
         *
         *
         *  i start from 1, while j start from 0
         */
        /**
         * NOTE !!!
         *
         *  we move
         *      `i` first as `RIGHT` boundary,
         *     then move `j` as `LEFT` boundary
         *
         *  (different from `regular` double loop, here,
         *  we setup right boundary, then setup left boundary)
         *
         *  e.g.
         *
         *    |--------|
         *    j        i
         *
         */
        for(int i=1; i<n; i++){
            for(int j=0; j<i; j++){
                /**
                 * NOTE !!!
                 *
                 *  `nums[i] > nums[j]` condition  !!!
                 *
                 *  -> ONLY if `right element is bigger than left element`,
                 *     new length is calculated and DP array is updated
                 *
                 */
                if(nums[i] > nums[j]){
                    dp[i] = Math.max(dp[i],dp[j]+1);
                    res = Math.max(res, dp[i]);
                }
            }
        }
        return res;
    }

  // V0-0-1
  // IDEA: 1D DP (fixed by gemini)
  /**  NOTE !!!
   *
   *  1. use 1-D DP
   *  2. Key Insight (Important):
   *
   *     - dp[i] = best LIS ending exactly at index i
   *
   *     - Inner loop checks:
   *          - “Can I extend a smaller LIS ending at j by appending nums[i]?”
   *
   *      - maxLen tracks the global maximum across all endpoints
   *
   */
  /**  Question:
   *
   *   why do we can decide if expand `max seq len` at `j` by comparing nums[i] > nums[j]?
   *   why not compare nums[j] > nums[j-1] ?  isn't i is the right pointer ?
   *
   *
   *  Answer (gpt):
   *
   *  1. dp[i] is the max SUB SEQUENCE len (NOT sub array) ends at i
   *  2. if (dp[i] > dp[j]), we know that can `expand ` subsequence to i
   *        so dp[i] = max(dp[i], dp[j] + 1)
   *
   *
   *  This is an **excellent question** — it gets to the *core invariant* of the LIS DP.
   *   The confusion you’re feeling is very common, so let’s resolve it cleanly and rigorously.
   *
   * ---
   *
   * ## Short answer (intuition)
   *
   * We compare **`nums[j] < nums[i]`** because:
   *
   * > `dp[j]` already represents a **valid increasing subsequence ending at `j`**.
   * > To extend that subsequence, we only need to check whether `nums[i]` can come **after its last element** — which is `nums[j]`.
   *
   * We **do NOT** compare `nums[j] > nums[j-1]` because:
   *
   * * LIS is a **subsequence**, not a **subarray**  (NOTE !!!!!!!)
   * * Elements do **not have to be adjacent**
   * * `j-1` may not even be part of the subsequence ending at `j`
   *
   * ---
   *
   * ## Step 1: What `dp[j]` REALLY means (this is the key)
   *
   * In LIS DP, we define:
   *
   * ```
   *
   * (NOTE !!!!!!!)
   *
   * dp[j] = length of the longest increasing subsequence
   *         that ENDS at index j
   * ```
   *
   * That implies **two critical facts**:
   *
   * 1. The subsequence **must include `nums[j]`**
   * 2. The **last value** of that subsequence is exactly `nums[j]`
   *
   * So `nums[j]` is the **only value that matters** when deciding whether we can append something after it.
   *
   * ---
   *
   * ## Step 2: Why `nums[i] > nums[j]` is the correct check
   *
   * We are at index `i` and asking:
   *
   * > “Can I extend a subsequence that ends at `j` by appending `nums[i]`?”
   *
   * For a subsequence to remain **strictly increasing**, the rule is:
   *
   * ```
   * last_value < new_value
   * ```
   *
   * Since the subsequence ending at `j` ends with `nums[j]`, the condition is:
   *
   * ```java
   * if (nums[j] < nums[i]) {
   *     dp[i] = max(dp[i], dp[j] + 1);
   * }
   * ```
   *
   * That’s it.
   * No other comparison is relevant.
   *
   * ---
   *
   * ## Step 3: Why `nums[j] > nums[j-1]` is WRONG
   *
   * This check:
   *
   * ```java
   * nums[j] > nums[j-1]
   * ```
   *
   * only tells you something about **adjacent elements**.
   *
   * But LIS is about **subsequences**, not contiguous segments.
   *
   * ### Counterexample
   *
   * ```
   * nums = [3, 1, 2]
   * ```
   *
   * The LIS is:
   *
   * ```
   * [1, 2]
   * ```
   *
   * But:
   *
   * * `nums[1] = 1`
   * * `nums[0] = 3`
   * * `nums[1] > nums[0]` ❌ false
   *
   * Yet `1` **is still a valid start** of an LIS.
   *
   * So comparing `nums[j]` with `nums[j-1]`:
   *
   * * ❌ misses valid subsequences
   * * ❌ incorrectly assumes adjacency matters
   *
   * ---
   *
   * ## Step 4: Why `i` is the “right pointer” but not used in that way
   *
   * Yes — `i` is the **right endpoint** of the subsequence we are trying to build.
   *
   * But DP logic is:
   *
   * > “To compute `dp[i]`, look at all earlier valid endpoints `j < i`
   * > and see which ones can connect to `i`.”
   *
   * So:
   *
   * * `i` = the element you want to **append**
   * * `j` = the element that is currently the **end** of a subsequence
   *
   * That’s why the comparison is:
   *
   * ```
   * nums[j] < nums[i]
   * ```
   *
   * Not:
   *
   * ```
   * nums[j] > nums[j-1]
   * ```
   *
   * ---
   *
   * ## Step 5: Mental model (use this in interviews)
   *
   * Think of it like this:
   *
   * > Every index `j` represents a **completed LIS ending at `j`**.
   * > You are trying to **attach `nums[i]` to the end of it**.
   *
   * So the only question is:
   *
   * > “Is `nums[i]` bigger than the last element of that LIS?”
   *
   * And the last element **is `nums[j]`**.
   *
   * ---
   *
   * ## Visual summary
   *
   * ```
   * Valid LIS ending at j:
   * [ ... , nums[j] ]
   *
   * Can we extend it with nums[i]?
   *
   * YES if:
   * nums[j] < nums[i]
   * ```
   *
   * Nothing else matters.
   *
   * ---
   *
   * ## Final takeaway (very important)
   *
   * * ✅ LIS DP compares **endpoints**
   * * ❌ Never compare with `j-1` (that’s subarray logic)
   * * ✅ `dp[j]` already encodes the whole history
   * * ✅ `nums[j]` is the only value needed to extend
   *
   *
   */
  /**  DEMO:
   *
   * Below is a **clear, step-by-step visualization**
   * of that LIS DP loop so you can *see*
   * exactly what is happening.
   *
   * ---
   *
   * ## Example Input
   *
   * ```text
   * nums = [10, 9, 2, 5, 3, 7, 101, 18]
   * ```
   *
   * Initial state:
   *
   * ```text
   * dp = [1, 1, 1, 1, 1, 1, 1, 1]
   * maxLen = 1
   * ```
   *
   * > Meaning: every element by itself is an
   * increasing subsequence of length 1.
   *
   * ---
   *
   * ## Step-by-Step Visualization
   *
   * ---
   *
   * ### i = 1 → nums[i] = 9
   *
   * Compare with all `j < i`:
   *
   * | j | nums[j] | nums[j] < nums[i]? | dp[i] |
   * | - | ------- | ------------------ | ----- |
   * | 0 | 10      | ❌                  | 1     |
   *
   * Result:
   *
   * ```text
   * dp = [1, 1, 1, 1, 1, 1, 1, 1]
   * maxLen = 1
   * ```
   *
   * ---
   *
   * ### i = 2 → nums[i] = 2
   *
   * | j | nums[j] | nums[j] < nums[i]? |
   * | - | ------- | ------------------ |
   * | 0 | 10      | ❌                  |
   * | 1 | 9       | ❌                  |
   *
   * No updates.
   *
   * ```text
   * dp = [1, 1, 1, 1, 1, 1, 1, 1]
   * maxLen = 1
   * ```
   *
   * ---
   *
   * ### i = 3 → nums[i] = 5
   *
   * | j | nums[j] | nums[j] < nums[i]? | dp[i]               |
   * | - | ------- | ------------------ | ------------------- |
   * | 0 | 10      | ❌                  | 1                   |
   * | 1 | 9       | ❌                  | 1                   |
   * | 2 | 2       | ✅                  | max(1, dp[2]+1 = 2) |
   *
   * ```text
   * dp = [1, 1, 1, 2, 1, 1, 1, 1]
   * maxLen = 2
   * ```
   *
   * ---
   *
   * ### i = 4 → nums[i] = 3
   *
   * | j | nums[j] | nums[j] < nums[i]? | dp[i] |
   * | - | ------- | ------------------ | ----- |
   * | 0 | 10      | ❌                  | 1     |
   * | 1 | 9       | ❌                  | 1     |
   * | 2 | 2       | ✅                  | 2     |
   * | 3 | 5       | ❌                  | 2     |
   *
   * ```text
   * dp = [1, 1, 1, 2, 2, 1, 1, 1]
   * maxLen = 2
   * ```
   *
   * ---
   *
   * ### i = 5 → nums[i] = 7
   *
   * | j | nums[j] | nums[j] < nums[i]? | dp[i] |
   * | - | ------- | ------------------ | ----- |
   * | 0 | 10      | ❌                  | 1     |
   * | 1 | 9       | ❌                  | 1     |
   * | 2 | 2       | ✅                  | 2     |
   * | 3 | 5       | ✅                  | 3     |
   * | 4 | 3       | ✅                  | 3     |
   *
   * ```text
   * dp = [1, 1, 1, 2, 2, 3, 1, 1]
   * maxLen = 3
   * ```
   *
   * ---
   *
   * ### i = 6 → nums[i] = 101
   *
   * | j | nums[j] | nums[j] < nums[i]? | dp[i] |
   * | - | ------- | ------------------ | ----- |
   * | 0 | 10      | ✅                  | 2     |
   * | 1 | 9       | ✅                  | 2     |
   * | 2 | 2       | ✅                  | 2     |
   * | 3 | 5       | ✅                  | 3     |
   * | 4 | 3       | ✅                  | 3     |
   * | 5 | 7       | ✅                  | 4     |
   *
   * ```text
   * dp = [1, 1, 1, 2, 2, 3, 4, 1]
   * maxLen = 4
   * ```
   *
   * ---
   *
   * ### i = 7 → nums[i] = 18
   *
   * | j | nums[j] | nums[j] < nums[i]? | dp[i] |
   * | - | ------- | ------------------ | ----- |
   * | 0 | 10      | ✅                  | 2     |
   * | 1 | 9       | ✅                  | 2     |
   * | 2 | 2       | ✅                  | 2     |
   * | 3 | 5       | ✅                  | 3     |
   * | 4 | 3       | ✅                  | 3     |
   * | 5 | 7       | ✅                  | 4     |
   * | 6 | 101     | ❌                  | 4     |
   *
   * ```text
   * dp = [1, 1, 1, 2, 2, 3, 4, 4]
   * maxLen = 4
   * ```
   *
   * ---
   *
   * ## Final Result
   *
   * ```text
   * Longest Increasing Subsequence Length = 4
   * ```
   *
   * Example LIS:
   *
   * ```
   * [2, 5, 7, 101]
   * ```
   *
   *
   */
  /**
   * time = O(N^2)
   * space = O(N)
   */
  public int lengthOfLIS_0_0_1(int[] nums) {
      if (nums == null || nums.length == 0) {
          return 0;
      }

      int n = nums.length;
      // dp[i] is the length of LIS ending at index i
      int[] dp = new int[n];

      // Every single element is an increasing subsequence of length 1
      Arrays.fill(dp, 1);

      int overallMax = 1;

      for (int i = 1; i < n; i++) {
          for (int j = 0; j < i; j++) {
              // If the current element is greater than the previous element
              if (nums[i] > nums[j]) {
                  // Extend the sequence ending at j
                  dp[i] = Math.max(dp[i], dp[j] + 1);
              }
          }
          // Track the highest value found in the dp array
          overallMax = Math.max(overallMax, dp[i]);
      }

      return overallMax;
  }

  // V0-0-2
  // IDEA: 1D DP (fixed by GPT)
  /**
   * time = O(N^2)
   * space = O(N)
   */
  public int lengthOfLIS_0_0_2(int[] nums) {
      if (nums == null || nums.length == 0)
          return 0;

      int n = nums.length;
      int[] dp = new int[n];
      Arrays.fill(dp, 1); // base case

      int maxLen = 1;

      for (int i = 1; i < n; i++) {
          for (int j = 0; j < i; j++) {
              if (nums[j] < nums[i]) {
                  dp[i] = Math.max(dp[i], dp[j] + 1);
              }
          }
          maxLen = Math.max(maxLen, dp[i]);
      }

      return maxLen;
  }


  // V0-1
  // IDEA: 1D DP (fixed by gpt)
  /**
   * time = O(N^2)
   * space = O(N)
   */
  public int lengthOfLIS_0_1(int[] nums) {
      // edge case: if the input array is null or has no elements
      if (nums == null || nums.length == 0) {
          return 0;
      }

      // Initialize dp array with 1, because the minimum length of LIS for any element is 1
      int[] dp = new int[nums.length];
      Arrays.fill(dp, 1);

      // Traverse the array
      /**
       * NOTE !!!
       *
       *  1. we use double loop
       *  2. first loop (i) start from idx = `nums.length`
       *     2nd loop (j) start from idx = 0 and END at j = i
       *     -> then we KEEP comparing nums[i] and nums[j]
       *
       */
      for (int i = 1; i < nums.length; i++) {
          for (int j = 0; j < i; j++) {
              if (nums[i] > nums[j]) {
                  dp[i] = Math.max(dp[i], dp[j] + 1);
              }
          }
      }

      // The answer is the maximum value in the dp array
      // below we get the max val from dp
      int maxLength = 0;
      for (int i = 0; i < dp.length; i++) {
          maxLength = Math.max(maxLength, dp[i]);
      }

      return maxLength;
  }

  // V0-2
  // https://github.com/yennanliu/CS_basics/blob/master/doc/pic/lc/lc_300_1.png

  // V0-3
  // IDEA: BRUTE FORCE + BINARY SEARCH (GPT)
  /**
   *  Explanation:
   *
   *  - Tails List: This list will store the smallest possible tail value for
   *          increasing subsequences of different lengths.
   *
   *  - Binary Search: For each element in nums, we use binary search to find the
   *                   correct position in the tails list where the element should go.
   *
   *  - Update or Append: If the element is larger than all elements in tails,
   *                      we append it. Otherwise, we replace the element in the list at the
   *                      found position to maintain the smallest possible tail value for subsequences.
   *
   *  - Final Result: The length of the tails list at the end is the length of the longest
   *                  increasing subsequence.
   *
   * Time Complexity:
   *  Binary Search: For each element in the array, we perform binary search on the tails list,
   *  which takes O(log n) time.
   *
   * Overall Complexity:
   *   Since we perform this for all n elements in nums, the total time complexity is O(n log n).
   *   This is a much more efficient solution than the classic dynamic programming approach
   *   with O(n^2) time complexity.
   *
   *
   */
  /**
   * time = O(N log N)
   * space = O(N)
   */
  public int lengthOfLIS_0_3(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        List<Integer> tails = new ArrayList<>();

        for (int num : nums) {
            int left = 0, right = tails.size();

            // Use binary search to find the right position for num
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails.get(mid) < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            // If left is equal to the size of tails, it means the current number
            // is larger than all the numbers in tails
            if (left == tails.size()) {
                tails.add(num);
            } else {
                tails.set(left, num);
            }
        }

        return tails.size();
    }

    // V0-4
    // IDEA: BINARY SEARCH (gpt)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int lengthOfLIS_0_4(int[] nums) {
        List<Integer> sub = new ArrayList<>();
        for (int num : nums) {
            int left = 0, right = sub.size() - 1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (sub.get(mid) < num) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            // left is the insertion point
            if (left == sub.size()) {
                sub.add(num);
            } else {
                sub.set(left, num);
            }
        }
        return sub.size();
    }

    // V0-5
    // IDEA: 2D DP (gemini)
    // NOTE !!!   this is an `overkill` idea,
    //            we should use 1D DP, which is clean and simple
    //            this 2D DP code is just for an reference
    /**
     * time = O(N * M)
     * space = O(N * M)
     */
    public int lengthOfLIS_0_5(int[] nums) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        // Remove duplicates to ensure "strictly" increasing
        int uniqueCount = 0;
        for (int i = 0; i < sorted.length; i++) {
            if (i == 0 || sorted[i] != sorted[i - 1]) {
                sorted[uniqueCount++] = sorted[i];
            }
        }

        // Standard 2D LCS between original nums and sorted unique nums
        int n = nums.length;
        int m = uniqueCount;
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (nums[i - 1] == sorted[j - 1]) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[n][m];
    }


    // V1-1
    // https://neetcode.io/problems/longest-increasing-subsequence
    // IDEA: RECURSION
    /**
     * time = O(2^N)
     * space = O(N)
     */
    public int lengthOfLIS_1_1(int[] nums) {
        return dfs(nums, 0, -1);
    }

    private int dfs(int[] nums, int i, int j) {
        if (i == nums.length) {
            return 0;
        }

        int LIS = dfs(nums, i + 1, j); // not include

        if (j == -1 || nums[j] < nums[i]) {
            LIS = Math.max(LIS, 1 + dfs(nums, i + 1, i)); // include
        }

        return LIS;
    }

    // V1-2
    // https://neetcode.io/problems/longest-increasing-subsequence
    // IDEA: Dynamic Programming (Top-Down)
    private int[][] memo;

    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    private int dfs(int i, int j, int[] nums) {
        if (i == nums.length) {
            return 0;
        }
        if (memo[i][j + 1] != -1) {
            return memo[i][j + 1];
        }

        int LIS = dfs(i + 1, j, nums);

        if (j == -1 || nums[j] < nums[i]) {
            LIS = Math.max(LIS, 1 + dfs(i + 1, i, nums));
        }

        memo[i][j + 1] = LIS;
        return LIS;
    }

    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int lengthOfLIS_1_2(int[] nums) {
        int n = nums.length;
        memo = new int[n][n + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        return dfs(0, -1, nums);
    }

    // V1-3
    // https://neetcode.io/problems/longest-increasing-subsequence
    // IDEA: Dynamic Programming (Bottom-Up)
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int lengthOfLIS_1_3(int[] nums) {
        int[] LIS = new int[nums.length];
        Arrays.fill(LIS, 1);

        for (int i = nums.length - 1; i >= 0; i--) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] < nums[j]) {
                    LIS[i] = Math.max(LIS[i], 1 + LIS[j]);
                }
            }
        }
        return Arrays.stream(LIS).max().getAsInt();
    }

    // V1-4
    // https://neetcode.io/problems/longest-increasing-subsequence
    // IDEA:  Segment Tree
    public class SegmentTree {
        int n;
        int[] tree;

        public SegmentTree(int N) {
            n = N;
            while ((n & (n - 1)) != 0) {
                n++;
            }
            tree = new int[2 * n];
        }

        void update(int i, int val) {
            tree[n + i] = val;
            int j = (n + i) >> 1;
            while (j >= 1) {
                tree[j] = Math.max(tree[j << 1], tree[j << 1 | 1]);
                j >>= 1;
            }
        }

        int query(int l, int r) {
            if (l > r) {
                return 0;
            }
            int res = Integer.MIN_VALUE;
            l += n;
            r += n + 1;
            while (l < r) {
                if ((l & 1) != 0) {
                    res = Math.max(res, tree[l]);
                    l++;
                }
                if ((r & 1) != 0) {
                    r--;
                    res = Math.max(res, tree[r]);
                }
                l >>= 1;
                r >>= 1;
            }
            return res;
        }
    }

    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int lengthOfLIS_1_4(int[] nums) {
        int[] sortedArr = Arrays.stream(nums).distinct().sorted().toArray();
        int[] order = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            order[i] = Arrays.binarySearch(sortedArr, nums[i]);
        }
        int n = sortedArr.length;
        SegmentTree segTree = new SegmentTree(n);

        int LIS = 0;
        for (int num : order) {
            int curLIS = segTree.query(0, num - 1) + 1;
            segTree.update(num, curLIS);
            LIS = Math.max(LIS, curLIS);
        }
        return LIS;
    }


    // V1-5
    // https://neetcode.io/problems/longest-increasing-subsequence
    // IDEA: Binary Search
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int lengthOfLIS_1_5(int[] nums) {
        List<Integer> dp = new ArrayList<>();
        dp.add(nums[0]);

        int LIS = 1;
        for (int i = 1; i < nums.length; i++) {
            if (dp.get(dp.size() - 1) < nums[i]) {
                dp.add(nums[i]);
                LIS++;
                continue;
            }

            int idx = Collections.binarySearch(dp, nums[i]);
            if (idx < 0) idx = -idx - 1;
            dp.set(idx, nums[i]);
        }

        return LIS;
    }


    // V2
    // IDEA : DP
    // https://leetcode.com/problems/longest-increasing-subsequence/solutions/4509493/300/
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int lengthOfLIS_2(int[] nums) {
        if(nums == null || nums.length == 0)return 0;
        int n=nums.length;
        int[] dp=new int[n];
        Arrays.fill(dp,1);
        for(int i=1;i<n;i++){
            for(int j=0;j<i;j++){
                if(nums[i]>nums[j]){
                    dp[i]=Math.max(dp[i],dp[j]+1);
                }
            }
        }
        int maxi=1;
        for(int len : dp){
            maxi=Math.max(maxi,len);
        }
        return maxi;
    }

    // V3
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/longest-increasing-subsequence/solutions/4509303/beats-100-binary-search-explained-with-video-c-java-python-js/
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int lengthOfLIS_3(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;
        for (int x : nums) {
            int i = 0, j = size;
            while (i != j) {
                int m = (i + j) / 2;
                if (tails[m] < x)
                    i = m + 1;
                else
                    j = m;
            }
            tails[i] = x;
            if (i == size) ++size;
        }
        return size;
    }

    // V4
    // IDEA : DP
    // https://leetcode.com/problems/longest-increasing-subsequence/solutions/4510776/java-solution-explained-in-hindi/
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int lengthOfLIS_4(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        int maxLength = 0;
        for (int len : dp) {
            maxLength = Math.max(maxLength, len);
        }
        return maxLength;
    }



}
