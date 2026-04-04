package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/integer-break/description/
/**
 * 343. Integer Break
 * Medium
 * Topics
 * Companies
 * Hint
 * Given an integer n, break it into the sum of k positive integers, where k >= 2, and maximize the product of those integers.
 *
 * Return the maximum product you can get.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: 1
 * Explanation: 2 = 1 + 1, 1 × 1 = 1.
 * Example 2:
 *
 * Input: n = 10
 * Output: 36
 * Explanation: 10 = 3 + 3 + 4, 3 × 3 × 4 = 36.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 58
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 393.9K
 * Submissions
 *
 *
 *
 */
public class IntegerBreak {

  // V0
  // IDEA: 1D DP (fixed by gemini)
  /**  CORE IDEA:
   *
   * ---
   *
   * ### ⚙️ How the Logic Works (The "Break" vs. "No Break" Choice)
   *
   * The most important part of this definition is understanding
   * why we use `Math.max(j * (i - j), j * dp[i - j])`.
   *
   * When we split the number $i$ at a point $j$,
   * we are left with two parts: **$j$** and **$(i - j)$**.
   * We then have to decide what to do with the second part $(i - j)$:
   *
   * 1.  **Option A: `j * (i - j)` (No further break)**
   *     * We simply multiply $j$ by the remainder $(i - j)$.
   *     * *Example*: If $i=4$ and $j=2$, we do $2 \times 2 = 4$.
   * 2.  **Option B: `j * dp[i - j]` (Break further)**
   *     * We multiply $j$ by the **best possible break** of the remainder, which we already calculated and stored in `dp[i - j]`.
   *     * *Example*: If $i=5$ and $j=2$, we do $2 \times dp[3]$. Since $dp[3] = 2$ (from $1 \times 2$), this gives us $2 \times 2 = 4$.
   *
   * ---
   *
   * ### 📊 DP State Table for $n = 6$
   * Let's trace the definition to see how the array fills up:
   *
   * | $i$ | `dp[i]` Calculation | Final `dp[i]` | Explanation |
   * | :--- | :--- | :--- | :--- |
   * | **1** | — | **0** | (Can't break 1 into two positive integers) |
   * | **2** | $1 \times 1$ | **1** | Only one way: $1+1$ |
   * | **3** | $\max(1 \times 2, 1 \times dp[2])$ | **2** | $1+2$ is better than $1+1+1$ |
   * | **4** | $\max(2 \times 2, 2 \times dp[2])$ | **4** | $2+2$ is optimal |
   * | **5** | $\max(2 \times 3, 2 \times dp[3])$ | **6** | $2+3$ is optimal |
   * | **6** | $\max(3 \times 3, 3 \times dp[3])$ | **9** | $3+3$ is optimal |
   *
   *
   */
  public int integerBreak(int n) {
      // 1. Base cases for small n (must break into at least 2 numbers)
      if (n == 2)
          return 1; // 1*1
      if (n == 3)
          return 2; // 2*1

      /** NOTE !!!
       *
       *  V1
       *
       *
       * dp[i] =
       *    the maximum product we can get by breaking
       *    integer i into at least two positive integers
       *
       *  -----------
       *
       *  V2
       *
       *  dp[i] =
       *     represents the maximum product you can get by breaking
       *     the integer i into at least two positive integers.
       *
       */
      int[] dp = new int[n + 1];

      // 2. Initialize DP
      dp[1] = 1;
      dp[2] = 1;

      // 3. Fill the DP table
      for (int i = 3; i <= n; i++) {

          /** NOTE !!!   Try splitting i into j and (i-j)
           *
           *  e.g.
           *     i -> (j) + (i - j)
           *
           */
          // Try splitting i into j and (i-j)
          for (int j = 1; j < i; j++) {
              /** NOTE !!!   For the part (i-j), we can either:
               *
               * // a) `Not` break it further: j * (i - j)
               *
               * // b) Use its already calculated max break: j * dp[i - j]
               *
               */
              int currentOption = Math.max(j * (i - j), j * dp[i - j]);
              dp[i] = Math.max(dp[i], currentOption);
          }
      }

      return dp[n];
  }


  // V0-0-1
  // IDEA: 1D DP (fixed by GPT)
  /** Example dry run:
   *
   * Nice — let’s build the **DP table step by step for
   * `n = 10`**. This is where it really clicks.
   *
   * ---
   *
   * ## 🧠 Reminder
   *
   * ```
   * dp[i] = max product by breaking i into ≥ 2 numbers
   * ```
   *
   * Transition:
   *
   * ```
   * dp[i] = max( j * (i - j),  j * dp[i - j] )
   * ```
   *
   * ---
   *
   * ## 🧩 Start
   *
   * ```
   * dp[1] = 1
   * ```
   *
   * ---
   *
   * ## 🔢 Build Table
   *
   * ### i = 2
   *
   * * j = 1 → max(1×1, 1×dp[1]=1) = 1
   *   ✅ dp[2] = 1
   *
   * ---
   *
   * ### i = 3
   *
   * * j = 1 → max(1×2, 1×dp[2]=1) = 2
   * * j = 2 → max(2×1, 2×dp[1]=2) = 2
   *   ✅ dp[3] = 2
   *
   * ---
   *
   * ### i = 4
   *
   * * j = 1 → max(1×3, 1×2) = 3
   * * j = 2 → max(2×2, 2×1) = 4 ✅
   * * j = 3 → max(3×1, 3×1) = 3
   *
   * 👉 **dp[4] = 4**
   *
   * ---
   *
   * ### i = 5
   *
   * * j = 1 → 4
   * * j = 2 → 6 ✅
   * * j = 3 → 6
   * * j = 4 → 4
   *
   * 👉 **dp[5] = 6**
   *
   * ---
   *
   * ### i = 6
   *
   * * j = 1 → 6
   * * j = 2 → 8
   * * j = 3 → 9 ✅
   * * j = 4 → 8
   * * j = 5 → 6
   *
   * 👉 **dp[6] = 9**
   *
   * ---
   *
   * ### i = 7
   *
   * * j = 1 → 9
   * * j = 2 → 12 ✅
   * * j = 3 → 12
   * * j = 4 → 12
   * * j = 5 → 10
   * * j = 6 → 9
   *
   * 👉 **dp[7] = 12**
   *
   * ---
   *
   * ### i = 8
   *
   * * j = 1 → 12
   * * j = 2 → 18 ✅
   * * j = 3 → 18
   * * j = 4 → 16
   * * j = 5 → 15
   * * j = 6 → 12
   * * j = 7 → 7
   *
   * 👉 **dp[8] = 18**
   *
   * ---
   *
   * ### i = 9
   *
   * * j = 1 → 18
   * * j = 2 → 24
   * * j = 3 → 27 ✅
   * * j = 4 → 24
   * * j = 5 → 30 ❗ wait → check carefully
   *
   *   * 5×dp[4] = 5×4 = 20 (not 30)
   * * j = 6 → 18
   * * j = 7 → 14
   * * j = 8 → 8
   *
   * 👉 **dp[9] = 27**
   *
   * ---
   *
   * ### i = 10
   *
   * * j = 1 → 27
   * * j = 2 → 36 ✅
   * * j = 3 → 36
   * * j = 4 → 32
   * * j = 5 → 30
   * * j = 6 → 27
   * * j = 7 → 21
   * * j = 8 → 16
   * * j = 9 → 9
   *
   * 👉 **dp[10] = 36**
   *
   * ---
   *
   * ## 📊 Final DP Table
   *
   * ```
   * i:      1  2  3  4  5  6  7  8  9  10
   * dp[i]:  1  1  2  4  6  9 12 18 27 36
   * ```
   *
   * ---
   *
   * ## 🔥 Pattern You Should Notice
   *
   * * Starts forming powers of **3**
   * * Optimal splits:
   *
   *   * 10 → 3 + 3 + 4 → 3×3×4 = 36
   *   * 9 → 3 + 3 + 3 → 27
   *
   * ---
   *
   * ## 🧠 Mental Shortcut
   *
   * DP is doing this:
   *
   * > “Try all splits” → eventually discovers
   * > **breaking into 3s is best**
   *
   */
  public int integerBreak_0_0_1(int n) {

      /** NOTE !!!
       *
       * dp[i] =
       *    the maximum product we can get by breaking
       *    integer i into at least two positive integers
       */
      // dp[i] = max product for integer i
      int[] dp = new int[n + 1];

      dp[1] = 1;

      for (int i = 2; i <= n; i++) {
          for (int j = 1; j < i; j++) {
              dp[i] = Math.max(
                      dp[i],
                      Math.max(j * (i - j), j * dp[i - j]));
          }
      }

      return dp[n];
  }


  // V0-1
  // IDEA: GREEDY (fixed by gpt)
  /**
   *  Mathematical observation:
   *
   * 	•	The best strategy is to use as many 3s as possible.
   *
   * 	•	But if the remainder is 1,
   *     	replace 3 + 1 with 2 + 2
   *      	(since 3×1 = 3, but 2×2 = 4 is better).
   *
   */
  public int integerBreak_0_1(int n) {
        if (n == 2)
            return 1;
        if (n == 3)
            return 2;

        int product = 1;

        // Use as many 3s as possible
        while (n > 4) {
            product *= 3;
            n -= 3;
        }

        // Remaining n will be 2, 3, or 4 -> multiply at the end
        product *= n;

        return product;
    }

    // V1
    // https://www.youtube.com/watch?v=in6QbUPMJ3I
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0343-integer-break.java
    /**
     * time = O(N)
     * space = O(N)
     */
    public int integerBreak_1(int n) {
        if (n < 4) return n - 1;

        int res = 1;

        while (n > 4) {
            n -= 3;
            res *= 3;
        }

        res *= n;
        return res;
    }

    // V2-1
    // https://leetcode.com/problems/integer-break/solutions/4135963/faster-lesser4-methodssimple-mathdynamic-lc7i/
    // IDEA: 1D DP (BOTTOM UP)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int integerBreak_2_1(int n) {
        if (n <= 1) {
            return 0;
        }
        int[] dp = new int[n + 1];
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                dp[i] = Math.max(dp[i], Math.max(j * (i - j), j * dp[i - j]));
            }
        }
        return dp[n];
    }

    // V2-2
    // https://leetcode.com/problems/integer-break/solutions/4135963/faster-lesser4-methodssimple-mathdynamic-lc7i/
    // IDEA: Dynamic Programming (Top-Down with Memoization):
    /**
     * time = O(N)
     * space = O(N)
     */
    public int integerBreak_2_2(int n) {
        if (n <= 1) {
            return 0;
        }
        int[] memo = new int[n + 1];
        return maxProduct(n, memo);
    }

    private int maxProduct(int n, int[] memo) {
        if (n == 2) {
            return 1;
        }
        if (memo[n] != 0) {
            return memo[n];
        }
        int max = 0;
        for (int i = 1; i < n; i++) {
            max = Math.max(max, Math.max(i * (n - i), i * maxProduct(n - i, memo)));
        }
        memo[n] = max;
        return max;
    }


    // V2-3
    // https://leetcode.com/problems/integer-break/solutions/4135963/faster-lesser4-methodssimple-mathdynamic-lc7i/
    // IDEA: GREEDY
    /**
     * time = O(N)
     * space = O(N)
     */
    public int integerBreak_2_3(int n) {
        if (n <= 1) {
            return 0;
        }
        if (n == 2) {
            return 1;
        }
        if (n == 3) {
            return 2;
        }
        int result = 1;
        while (n > 4) {
            result *= 3;
            n -= 3;
        }
        result *= n;
        return result;
    }

    // V2-4
    // https://leetcode.com/problems/integer-break/solutions/4135963/faster-lesser4-methodssimple-mathdynamic-lc7i/
    // IDEA: MATH
    /**
     * time = O(N)
     * space = O(N)
     */
    public int integerBreak_2_4(int n) {
        if (n <= 1) {
            return 0;
        }
        if (n == 2) {
            return 1;
        }
        if (n == 3) {
            return 2;
        }
        int quotient = n / 3;
        int remainder = n % 3;
        if (remainder == 0) {
            return (int) Math.pow(3, quotient);
        } else if (remainder == 1) {
            return (int) (Math.pow(3, quotient - 1) * 4);
        } else {
            return (int) (Math.pow(3, quotient) * 2);
        }
    }


    // V3
    // https://leetcode.com/problems/integer-break/solutions/80785/ologn-time-solution-with-explanation-by-6deq2/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int integerBreak_3(int n) {
        if (n == 2)
            return 1;
        else if (n == 3)
            return 2;
        else if (n % 3 == 0)
            return (int) Math.pow(3, n / 3);
        else if (n % 3 == 1)
            return 2 * 2 * (int) Math.pow(3, (n - 4) / 3);
        else
            return 2 * (int) Math.pow(3, n / 3);
    }

    // V4
    // https://leetcode.com/problems/integer-break/solutions/6210269/video-give-me-10-minutes-how-we-think-ab-publ/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int integerBreak_4(int n) {
        if (n == 2) {
            return 1;
        }
        if (n == 3) {
            return 2;
        }

        // Try to divide n into as many threes as possible
        int threes = n / 3;
        int remainder = n % 3;

        if (remainder == 1) {
            threes -= 1; // remove 3 * 1
            remainder = 4; // create 2 * 2
        } else if (remainder == 0) {
            remainder = 1; // when remainder is 0, set 1 which doesn't affect your answer.
        }

        return (int) (Math.pow(3, threes) * remainder);
    }




}
