package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/flip-string-to-monotone-increasing/description/
/**
 * 926. Flip String to Monotone Increasing
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * A binary string is monotone increasing if it consists of some number of 0's (possibly none), followed by some number of 1's (also possibly none).
 *
 * You are given a binary string s. You can flip s[i] changing it from 0 to 1 or from 1 to 0.
 *
 * Return the minimum number of flips to make s monotone increasing.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "00110"
 * Output: 1
 * Explanation: We flip the last digit to get 00111.
 * Example 2:
 *
 * Input: s = "010110"
 * Output: 2
 * Explanation: We flip to get 011111, or alternatively 000111.
 * Example 3:
 *
 * Input: s = "00011000"
 * Output: 2
 * Explanation: We flip to get 00000000.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 105
 * s[i] is either '0' or '1'.
 *
 *
 *
 */
public class FlipStringToMonotoneIncreasing {

    // V0
//    public int minFlipsMonoIncr(String s) {
//
//    }


    // V1-1
    // IDEA: DP (One-Pass Dynamic Programming) (GEMINI)
    public int minFlipsMonoIncr_1_1(String s) {
        int flips = 0;
        int onesCount = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '1') {
                // We DON'T `flip` 1s immediately. We just `COUNT` them
                // as "potential" `future` costs if we find a 0.
                onesCount++;
            } else {
                // We found a '0'. To keep it `monotone`, we have `two` `choices`:
                // ->
                // 1. Flip this current '0' to a '1' (cost: flips + 1)
                // 2. Keep this '0', which means all previous '1's MUST be '0's
                //    (cost: onesCount)
                /**  NOTE !!!
                 *
                 *  `filps + 1`, and `onesCount`
                 *   are the `cost of 2 choices,`
                 *   and we want to get the smaller one (e.g. min)
                 */
                flips = Math.min(flips + 1, onesCount);
            }
        }

        return flips;
    }



    // V1-2
    // IDEA: DP (gpt)
    /**
     *
     * The real structure of the problem is:
     *
     *    -> We want the string to look like: 000...111
     *       So at every split point, we decide:
     *
     *    - left side → all 0
     *    - right side → all 1
     *
     */
    public int minFlipsMonoIncr_1_2(String s) {
        int flips = 0; // min flips so far
        int ones = 0; // number of '1's seen so far

        for (char c : s.toCharArray()) {
            if (c == '1') {
                ones++;
            } else {
                // c == '0'
                flips = Math.min(flips + 1, ones);
            }
        }

        return flips;
    }



    // V2



    // V3
    // https://leetcode.com/problems/flip-string-to-monotone-increasing/solutions/3061841/simple-explanation-with-images-on-time-o-uxb3/
    /**
     * Time Complexity: O(n) where n is the length of string s
     * - We iterate through the string twice, each taking O(n) time
     *
     * Space Complexity: O(1)
     * - We only use a constant amount of extra space regardless of input size
     * - No additional data structures that scale with input
     */
    public int minFlipsMonoIncr_3(String s) {
        int zeroes = 0;
        int ones = 0;

        // First pass: count total zeroes
        for (char ch : s.toCharArray()) {
            if (ch == '0') {
                zeroes++;
            }
        }

        int output = zeroes; // Initialize with flipping all zeroes to ones

        // Second pass: find minimum flips needed
        for (char ch : s.toCharArray()) {
            if (ch == '0') {
                zeroes--; // One less zero to flip if we include this position in prefix
            } else if (ch == '1') {
                ones++; // Need to flip this one to zero if in prefix
            }
            output = Math.min(output, zeroes + ones);
        }

        return output;
    }


    // V4
    // https://leetcode.com/problems/flip-string-to-monotone-increasing/solutions/3061487/java-solution-with-explanation-by-sartha-gmt0/
    public int minFlipsMonoIncr_4(String s) {
        // Initialize variables to store the minimum number of flips and the number of flips currently needed
        int ans = 0, noOfFlip = 0;

        // Iterate through each character in the input string
        for (int i = 0; i < s.length(); i++) {
            // If the current character is a '0', update the minimum number of flips and add 1 to the current number of flips
            if (s.charAt(i) == '0')
                ans = Math.min(noOfFlip, ans + 1);
                // If the current character is a '1', increment the number of flips
            else
                noOfFlip++;
        }
        // Return the minimum number of flips
        return ans;
    }






}
