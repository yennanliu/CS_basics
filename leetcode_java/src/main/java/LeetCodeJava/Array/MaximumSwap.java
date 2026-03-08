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
