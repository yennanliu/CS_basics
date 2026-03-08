package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-swap/description/
/**
 *  670. Maximum Swap
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given an integer num. You can swap two digits at most once to get the maximum valued number.
 *
 * Return the maximum valued number you can get.
 *
 *
 *
 * Example 1:
 *
 * Input: num = 2736
 * Output: 7236
 * Explanation: Swap the number 2 and the number 7.
 * Example 2:
 *
 * Input: num = 9973
 * Output: 9973
 * Explanation: No swap.
 *
 *
 * Constraints:
 *
 * 0 <= num <= 108
 *
 *
 */
public class MaximumSwap {

    // V0
//    public int maximumSwap(int num) {
//
//    }

    // V0-1
    // IDEA: BRUTE FORCE
    public int maximumSwap(int num) {
        // edge
        if (num < 10) {
            return num;
        }

        int maxRes = num;
        String numStr = String.valueOf(num);
        for (int i = 0; i < numStr.length(); i++) {
            for (int j = i + 1; j < numStr.length(); j++) {
                /** NOTE !!! swap OP */
                String swapStr = swap(numStr, i, j);
                maxRes = Math.max(maxRes, Integer.parseInt(swapStr));
//                System.out.println(">>> i = " + i
//                        + ", j = " + j
//                        + ", swapStr = " + swapStr
//                        + ", maxRes = " + maxRes);
            }
        }

        return maxRes;
    }

    /** NOTE !!! swap help func */
    private String swap(String x, int i, int j) {
        char[] arr = x.toCharArray(); // ???
        char tmp = arr[i]; // ???
        arr[i] = arr[j];
        arr[j] = tmp;
        return String.valueOf(arr);
    }


    // V0-2
    // IDEA: BRUTE FORCE (GEMINI)
    public int maximumSwap_0_2(int num) {
        char[] digits = String.valueOf(num).toCharArray();
        int maxVal = num;

        // Try every possible swap
        for (int i = 0; i < digits.length; i++) {
            for (int j = i + 1; j < digits.length; j++) {
                // 1. Swap
                char tmp = digits[i];
                digits[i] = digits[j];
                digits[j] = tmp;

                // 2. Convert to int and compare
                int currentVal = Integer.parseInt(new String(digits));
                maxVal = Math.max(maxVal, currentVal);

                // 3. Swap BACK (Reset for next iteration)
                digits[j] = digits[i];
                digits[i] = tmp;
            }
        }

        return maxVal;
    }


    // V0-2
    // IDEA: OPTIMIZED (Greedy) (GEMINI)
    public int maximumSwap_0_3(int num) {
        char[] digits = String.valueOf(num).toCharArray();
        int[] last = new int[10];

        // Record the last occurrence of each digit
        for (int i = 0; i < digits.length; i++) {
            last[digits[i] - '0'] = i;
        }

        // Traverse from left to right
        for (int i = 0; i < digits.length; i++) {
            // Look for a larger digit (9 down to current+1) appearing later
            for (int d = 9; d > digits[i] - '0'; d--) {
                if (last[d] > i) {
                    // Swap found! This is the best we can do.
                    char tmp = digits[i];
                    digits[i] = digits[last[d]];
                    digits[last[d]] = tmp;
                    return Integer.parseInt(new String(digits));
                }
            }
        }
        return num;
    }


    // V1-1
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/maximum-swap/editorial/
    public int maximumSwap_1_1(int num) {
        String numStr = Integer.toString(num); // Convert num to string for easy manipulation
        int n = numStr.length();
        int maxNum = num; // Track the maximum number found

        // Try all possible swaps
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                char[] numeral = numStr.toCharArray(); // Convert string to char array for swapping

                // Swap digits at index i and j
                char temp = numeral[i];
                numeral[i] = numeral[j];
                numeral[j] = temp;

                int tempNum = Integer.parseInt(new String(numeral)); // Convert the char array back to integer

                maxNum = Math.max(maxNum, tempNum); // Update maxNum if the new number is larger
            }
        }

        return maxNum; // Return the largest number after all possible swaps
    }



    // V1-2
    // IDEA: Greedy Two-Pass
    // https://leetcode.com/problems/maximum-swap/editorial/
    /**  IDEA (gemini)
     *
     * This code uses a **Greedy + Preprocessing** approach. Instead of trying every possible swap (Brute Force), it pre-calculates the "best" digit to swap with for every position in the number.
     *
     * Here is the step-by-step breakdown:
     *
     * ---
     *
     * ### 1. The Strategy: "Look Ahead"
     *
     * To make a number as large as possible with one swap, you want to:
     *
     * 1. Find the **left-most** digit that can be increased.
     * 2. Swap it with the **largest possible** digit that appears to its **right**.
     *
     * ### 2. Phase 1: Pre-calculating the `maxRightIndex`
     *
     * The code creates an array `maxRightIndex` where `maxRightIndex[i]` stores the index of the largest digit from index `i` all the way to the end of the string.
     *
     * It fills this array from **right to left**:
     *
     * * **Base Case:** The last digit's "max to its right" is itself.
     * * **Recurrence:** For any index `i`, the largest digit is either the digit at `i` itself OR the largest digit found in the suffix starting at `i+1`.
     *
     * > **Example:** `num = 2736`
     * > * `maxRightIndex` would look like: `[1, 1, 3, 3]`
     * > * (At index 0, the largest value to the right is `7` at index 1).
     * >
     * >
     *
     * ### 3. Phase 2: Finding the First Swap Opportunity
     *
     * Now, the code scans the number from **left to right** (index `i = 0` to `n-1`):
     *
     * * It asks: "Is the current digit `numArr[i]` smaller than the largest digit available to its right (`numArr[maxRightIndex[i]]`)?"
     * * The **first time** this is true, we have found our optimal swap.
     *
     * ### 4. Phase 3: The Swap and Exit
     *
     * Once the condition `numArr[i] < numArr[maxRightIndex[i]]` is met:
     *
     * 1. It performs the swap.
     * 2. It immediately converts the array back to an `int` and returns.
     * 3. Because we started from the left (the highest place value), the first swap we find is guaranteed to produce the maximum result.
     *
     * ---
     *
     * ### 📊 Complexity Analysis
     *
     * | Metric | Complexity | Explanation |
     * | --- | --- | --- |
     * | **Time** | **$O(D)$** | Two linear passes ($D$ = number of digits). Much faster than $O(D^2)$ Brute Force. |
     * | **Space** | **$O(D)$** | To store the `maxRightIndex` array. |
     *
     * ---
     *
     * ### ⚠️ A Small Note on a Potential Bug
     *
     * There is a tiny logical edge case in this specific implementation:
     *
     * ```java
     * maxRightIndex[i] = (numArr[i] > numArr[maxRightIndex[i + 1]]) ? i : maxRightIndex[i + 1];
     *
     * ```
     *
     * By using `>`, if there are **multiple** occurrences of the same max digit (e.g., `199`), this code will pick the **first** `9` it saw from the right. In `maximumSwap`, you generally want to swap with the **last (right-most)** occurrence of the maximum digit to get the biggest boost.
     *
     * * **Example:** `199`. Swapping the `1` with the first `9` gives `919`. Swapping with the last `9` gives `991`.
     * * **Fix:** Changing `>` to `>=` in that ternary operator ensures you pick the right-most index for duplicate digits.
     *
     *
     */
    public int maximumSwap_1_2(int num) {
        char[] numArr = Integer.toString(num).toCharArray();
        int n = numArr.length;
        int[] maxRightIndex = new int[n];

        maxRightIndex[n - 1] = n - 1;
        for (int i = n - 2; i >= 0; --i) {
            maxRightIndex[i] = (numArr[i] > numArr[maxRightIndex[i + 1]])
                    ? i
                    : maxRightIndex[i + 1];
        }

        for (int i = 0; i < n; ++i) {
            if (numArr[i] < numArr[maxRightIndex[i]]) {
                char temp = numArr[i];
                numArr[i] = numArr[maxRightIndex[i]];
                numArr[maxRightIndex[i]] = temp;
                return Integer.parseInt(new String(numArr));
            }
        }

        return num;
    }


    // V1-3
    // IDEA: Suboptimal Greedy
    // https://leetcode.com/problems/maximum-swap/editorial/
    public int maximumSwap_1_3(int num) {
        String numStr = Integer.toString(num);
        int n = numStr.length();
        int[] lastSeen = new int[10]; // Store the last occurrence of each digit

        // Record the last occurrence of each digit
        for (int i = 0; i < n; ++i) {
            lastSeen[numStr.charAt(i) - '0'] = i;
        }

        // Traverse the string to find the first digit that can be swapped with a larger one
        for (int i = 0; i < n; ++i) {
            for (int d = 9; d > numStr.charAt(i) - '0'; --d) {
                if (lastSeen[d] > i) {
                    //Perform the swap
                    char[] arr = numStr.toCharArray();
                    char temp = arr[i];
                    arr[i] = arr[lastSeen[d]];
                    arr[lastSeen[d]] = temp;
                    numStr = new String(arr);

                    return Integer.parseInt(numStr); // Return the new number immediately after the swap
                }
            }
        }

        return num; // Return the original number if no swap can maximize it
    }


    // V1-4
    // IDEA: Space Optimized Greedy
    // https://leetcode.com/problems/maximum-swap/editorial/
    public int maximumSwap_1_4(int num) {
        char[] numStr = Integer.toString(num).toCharArray();
        int n = numStr.length;
        int maxDigitIndex = -1, swapIdx1 = -1, swapIdx2 = -1;

        // Traverse the string from right to left, tracking the max digit and
        // potential swap
        for (int i = n - 1; i >= 0; --i) {
            if (maxDigitIndex == -1 || numStr[i] > numStr[maxDigitIndex]) {
                maxDigitIndex = i; // Update the index of the max digit
            } else if (numStr[i] < numStr[maxDigitIndex]) {
                swapIdx1 = i; // Mark the smaller digit for swapping
                swapIdx2 = maxDigitIndex; // Mark the larger digit for swapping
            }
        }

        // Perform the swap if a valid swap is found
        if (swapIdx1 != -1 && swapIdx2 != -1) {
            char temp = numStr[swapIdx1];
            numStr[swapIdx1] = numStr[swapIdx2];
            numStr[swapIdx2] = temp;
        }

        return Integer.parseInt(new String(numStr)); // Return the new number or the original if no
        // swap occurred
    }


    // V2





}
