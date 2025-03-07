package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/decode-ways/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  91. Decode Ways
 * Solved
 * Medium
 * Topics
 * Companies
 * You have intercepted a secret message encoded as a string of numbers. The message is decoded via the following mapping:
 *
 * "1" -> 'A'
 *
 * "2" -> 'B'
 *
 * ...
 *
 * "25" -> 'Y'
 *
 * "26" -> 'Z'
 *
 * However, while decoding the message, you realize that there are many different ways you can decode the message because some codes are contained in other codes ("2" and "5" vs "25").
 *
 * For example, "11106" can be decoded into:
 *
 * "AAJF" with the grouping (1, 1, 10, 6)
 * "KJF" with the grouping (11, 10, 6)
 * The grouping (1, 11, 06) is invalid because "06" is not a valid code (only "6" is valid).
 * Note: there may be strings that are impossible to decode.
 *
 * Given a string s containing only digits, return the number of ways to decode it. If the entire string cannot be decoded in any valid way, return 0.
 *
 * The test cases are generated so that the answer fits in a 32-bit integer.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "12"
 *
 * Output: 2
 *
 * Explanation:
 *
 * "12" could be decoded as "AB" (1 2) or "L" (12).
 *
 * Example 2:
 *
 * Input: s = "226"
 *
 * Output: 3
 *
 * Explanation:
 *
 * "226" could be decoded as "BZ" (2 26), "VF" (22 6), or "BBF" (2 2 6).
 *
 * Example 3:
 *
 * Input: s = "06"
 *
 * Output: 0
 *
 * Explanation:
 *
 * "06" cannot be mapped to "F" because of the leading zero ("6" is different from "06"). In this case, the string is not a valid encoding, so return 0.
 *
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s contains only digits and may contain leading zero(s).
 *
 */
public class DecodeWays {

    // V0
    // IDEA : DP
    // TODO : implement
//    public int numDecodings(String encodedString) {
//
//    }

    // V1-1
    // https://neetcode.io/problems/decode-ways
    // IDEA: RECURSION
    private int dfs(int i, String s) {
        if (i == s.length()) return 1;
        if (s.charAt(i) == '0') return 0;

        int res = dfs(i + 1, s);
        if (i < s.length() - 1) {
            if (s.charAt(i) == '1' ||
                    (s.charAt(i) == '2' && s.charAt(i + 1) < '7')) {
                res += dfs(i + 2, s);
            }
        }
        return res;
    }

    public int numDecodings_1_1(String s) {
        return dfs(0, s);
    }

    // V1-2
    // https://neetcode.io/problems/decode-ways
    // IDEA: Dynamic Programming (Top-Down)
    public int numDecodings_1_2(String s) {
        Map<Integer, Integer> dp = new HashMap<>();
        dp.put(s.length(), 1);

        return dfs(s, 0, dp);
    }

    private int dfs(String s, int i, Map<Integer, Integer> dp) {
        if (dp.containsKey(i)) {
            return dp.get(i);
        }
        if (s.charAt(i) == '0') {
            return 0;
        }

        int res = dfs(s, i + 1, dp);
        if (i + 1 < s.length() && (s.charAt(i) == '1' ||
                s.charAt(i) == '2' && s.charAt(i + 1) < '7')) {
            res += dfs(s, i + 2, dp);
        }
        dp.put(i, res);
        return res;
    }


    // V1-3
    // https://neetcode.io/problems/decode-ways
    // IDEA: Dynamic Programming (Bottom-Up)
    public int numDecodings_1_3(String s) {
        int[] dp = new int[s.length() + 1];
        dp[s.length()] = 1;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '0') {
                dp[i] = 0;
            } else {
                dp[i] = dp[i + 1];
                if (i + 1 < s.length() && (s.charAt(i) == '1' ||
                        s.charAt(i) == '2' && s.charAt(i + 1) < '7')) {
                    dp[i] += dp[i + 2];
                }
            }
        }
        return dp[0];
    }


    // V2
    // IDEA : DP
    // https://leetcode.com/problems/decode-ways/solutions/4456554/character-state-machine-video-solution-java-c/
    public int numDecodings_2(String encodedString) {
        char s[] = encodedString.toCharArray();
        if(s[0] == '0')return 0;
        int sz = s.length;
        int noWaysAtIndx[] = new int[sz];
        noWaysAtIndx[0] = 1;
        for(int indx = 1; indx < sz; indx++){
            char currC = s[indx], prevC = s[indx-1];
            if(currC == '0' && !(prevC == '1' || prevC == '2')){
                return  0;
            }
            int onesDigit = currC - '0', tensDigit = prevC - '0';
            int number = tensDigit * 10 + onesDigit;
            if(number >= 10 && number <= 26){
                if(indx >= 2)
                    noWaysAtIndx[indx] += noWaysAtIndx[indx-2];
                else
                    noWaysAtIndx[indx] = 1;
            }
            if(number != 10 && number != 20){
                noWaysAtIndx[indx] += noWaysAtIndx[indx-1];
            }
        }
        return noWaysAtIndx[sz - 1];
    }

    // V3
    // IDEA : DP
    // https://leetcode.com/problems/decode-ways/solutions/4454539/decode-ways-java/
    public int numDecodings_3(String s) {
        int n = s.length();
        int[] dp = new int[n + 1];
        if(n == 0 || s.charAt(0) == '0') return 0;
        dp[0] = 1; dp[1] = 1;

        for(int i = 2; i <= n; i++){
            // check for single-digit
            if(s.charAt(i - 1) >= '1' && s.charAt(i - 1) <= '9') dp[i] = dp[i - 1];

            // check for two-digits
            if(s.charAt(i - 2) == '1') dp[i] += dp[i - 2];
            else if(s.charAt(i - 2) == '2' && s.charAt(i - 1) >= '0' && s.charAt(i - 1) <= '6') dp[i] += dp[i - 2];
        }
        return dp[n];
    }

    // V4-0
    // IDEA: RECURSION (TLE)
    // https://leetcode.com/problems/decode-ways/solutions/4454173/recursive-top-down-bottom-up-clean-and-c-hqge/

    // V4-1
    // IDEA: DP (TOP DOWN)
    // https://leetcode.com/problems/decode-ways/solutions/4454173/recursive-top-down-bottom-up-clean-and-c-hqge/
    public int numDecodings_4_1(String s) {
        int[] memo = new int[s.length()];
        Arrays.fill(memo, -1);
        return topDownDecode(s, 0, memo);
    }

    private int topDownDecode(String s, int index, int[] memo) {
        // Base case: if the index reaches the end of the string
        if (index == s.length()) {
            return 1; // This is a valid decoding
        }

        // Check memoization table
        if (memo[index] != -1) {
            return memo[index];
        }

        // Check for leading zero
        if (s.charAt(index) == '0') {
            return 0; // This decoding is invalid
        }

        // Decode single digit
        int ways = topDownDecode(s, index + 1, memo);

        // Decode two digits if possible
        if (index + 1 < s.length() && Integer.parseInt(s.substring(index, index + 2)) <= 26) {
            ways += topDownDecode(s, index + 2, memo);
        }

        // Memoize the result
        memo[index] = ways;

        return ways;
    }

    // V4-2
    // IDEA: DP (BOTTOM UP)
    // https://leetcode.com/problems/decode-ways/solutions/4454173/recursive-top-down-bottom-up-clean-and-c-hqge/
    public int numDecodings_4_2(String s) {
        int n = s.length();
        if (n == 0) {
            return 0;
        }

        // Initialize the DP array
        int[] dp = new int[n + 1];
        dp[n] = 1; // There is one way to decode an empty string

        // Fill in the DP array from right to left
        for (int i = n - 1; i >= 0; i--) {
            // Check for leading zero
            if (s.charAt(i) == '0') {
                dp[i] = 0;
            } else {
                // Decode single digit
                dp[i] += dp[i + 1];

                // Decode two digits if possible
                if (i + 1 < n && Integer.parseInt(s.substring(i, i + 2)) <= 26) {
                    dp[i] += dp[i + 2];
                }
            }
        }

        return dp[0];
    }

}
