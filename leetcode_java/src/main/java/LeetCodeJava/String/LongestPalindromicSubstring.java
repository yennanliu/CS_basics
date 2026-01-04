package LeetCodeJava.String;

// https://leetcode.com/problems/longest-palindromic-substring/
/**
 *  5. Longest Palindromic Substring
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Given a string s, return the longest
 * palindromic
 *
 * substring
 *  in s.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "babad"
 * Output: "bab"
 * Explanation: "aba" is also a valid answer.
 * Example 2:
 *
 * Input: s = "cbbd"
 * Output: "bb"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s consist of only digits and English letters.
 *
 */

import java.util.ArrayList;

public class LongestPalindromicSubstring {

    // V0
    // IDEA : SLIDING WINDOW : for - while
    // Time complexity : O(n^2), Space complexity: O(n)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/String/longest-palindromic-substring.py
    public String longestPalindrome(String s) {

        if (s == null || s.length() == 0) {
            return s;
        }

        ArrayList<String> tmp = new ArrayList<String>();
        String res = "";

        /** NOTE !!! for - while (sliding window) */
        for (int i = 0; i < s.length(); i++) {

            // case 1 : length is odd
//            int l = i - 1;
//            int r = i + 1;
            // NOTE !!! we set l = i, r = i when "length is odd"
            int l = i;
            int r = i;
            while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {

                // NOTE !!! we get res first, then do pointers (l, r) update
                if (r - l + 1 > res.length()) {
                    res = s.substring(l, r+1); // NOTE !!! substring with idx l, r+1
                }

                l -= 1;
                r += 1;
            }

            // case 2 : length is even
            // NOTE !!! we set l = i -1, r = i when "length is odd"
            l = i - 1;
            r = i;
            while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {

                // NOTE !!! we get res first, then do pointers (l, r) update
                if (r - l + 1 > res.length()) {
                    res = s.substring(l, r+1);  // NOTE !!! substring with idx l, r+1
                }

                l -= 1;
                r += 1;
            }
        }

        return res;
    }

    // V0-0-1
    // IDEA: SLIDE WINDOW (fixed by gpt)
    public String longestPalindrome_0_0_1(String s) {
        if (s == null || s.length() == 0)
            return "";
        if (s.length() == 1)
            return s;

        String best = "";

        for (int i = 0; i < s.length(); i++) {
            // odd center at (i, i)
            int[] odd = expand(s, i, i);
            if (odd[1] - odd[0] + 1 > best.length()) {
                best = s.substring(odd[0], odd[1] + 1);
            }

            // even center at (i, i+1)
            int[] even = expand(s, i, i + 1);
            if (even[1] - even[0] + 1 > best.length()) {
                best = s.substring(even[0], even[1] + 1);
            }
        }

        return best;
    }

    private int[] expand(String s, int l, int r) {
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
            l--;
            r++;
        }
        // l and r have moved one step too far; bring them back
        return new int[] { l + 1, r - 1 };
    }

    // V0-1
    // IDEA: 2 POINTERS + Palindrome check (fixed by gpt)
    public String longestPalindrome_0_1(String s) {

        // edge case: empty string or null input
        if (s == null || s.length() == 0) {
            return null;
        }
        if (s.length() == 1) {
            return s;
        }

        String res = String.valueOf(s.charAt(0)); // Initialize with the first character

        // 2 pointers to expand around each character
        for (int i = 0; i < s.length(); i++) {
            // Get the longest odd-length palindrome centered at i
            String oddSubStr = getOddPalindromic(s, i);
            // Get the longest even-length palindrome centered at i
            String evenSubStr = getEvenPalindromic(s, i);

            // Update the result with the longer palindrome
            if (oddSubStr.length() > res.length()) {
                res = oddSubStr;
            }
            if (evenSubStr.length() > res.length()) {
                res = evenSubStr;
            }
        }

        return res;
    }

    // Function to find the longest odd-length palindrome centered at `start`
    public String getOddPalindromic(String s, int start) {
        int left = start;
        int right = start;

        // Expand around the center for odd-length palindrome
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }

        // Return the palindrome substring found
        return s.substring(left + 1, right);
    }

    // Function to find the longest even-length palindrome centered between
    // `start-1` and `start`
    public String getEvenPalindromic(String s, int start) {
        int left = start - 1;
        int right = start;

        // Expand around the center for even-length palindrome
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }

        // Return the palindrome substring found
        return s.substring(left + 1, right);
    }

    // V0-2
    // IDEA: DP (gemini)
    public String longestPalindrome_0_2(String s) {
        if (s == null || s.length() == 0)
            return "";

        int n = s.length();
        // dp[i][j] indicates if s[i...j] is a palindrome
        boolean[][] dp = new boolean[n][n];
        int start = 0;
        int maxLen = 1;

        // All substrings of length 1 are palindromes
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }

        // Check for lengths from 2 up to n
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1; // Ending index

                if (s.charAt(i) == s.charAt(j)) {
                    // Case 1: Substring length is 2 (e.g., "aa")
                    if (len == 2) {
                        dp[i][j] = true;
                    }
                    // Case 2: Substring length > 2, check if inner part is a palindrome
                    else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                }

                // If s[i...j] is a palindrome, update max substring info
                if (dp[i][j] && len > maxLen) {
                    maxLen = len;
                    start = i;
                }
            }
        }

        return s.substring(start, start + maxLen);
    }

    // V0-3
    // IDEA: DP (GPT)
    public String longestPalindrome_0_3(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }

        int n = s.length();
        boolean[][] dp = new boolean[n][n];

        int start = 0;
        int maxLen = 1;

        // length 1 substrings
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }

        // fill dp table
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    if (j - i <= 2 || dp[i + 1][j - 1]) {
                        dp[i][j] = true;

                        int len = j - i + 1;
                        if (len > maxLen) {
                            maxLen = len;
                            start = i;
                        }
                    }
                }
            }
        }

        return s.substring(start, start + maxLen);
    }



    // V1
    // IDEA : BRUTE FORCE + isPalindrome check
    // https://leetcode.com/problems/longest-palindromic-substring/solutions/4212564/beats-96-49-5-different-approaches-brute-force-eac-dp-ma-recursion/
    public String longestPalindrome_1(String s) {
        if (s.length() <= 1) {
            return s;
        }

        int maxLen = 1;
        String maxStr = s.substring(0, 1);

        for (int i = 0; i < s.length(); i++) {
            for (int j = i + maxLen; j <= s.length(); j++) {
                if (j - i > maxLen && isPalindrome(s.substring(i, j))) {
                    maxLen = j - i;
                    maxStr = s.substring(i, j);
                }
            }
        }

        return maxStr;
    }

    private boolean isPalindrome(String str) {
        int left = 0;
        int right = str.length() - 1;

        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }

    // V2
    // IDEA : SLIDING WINDOW
    // https://leetcode.com/problems/longest-palindromic-substring/solutions/3401644/detailed-recursive-explaination-with-pictures-in-c-java-python-dp-two-pointers/
    int maxLen = 0;
    int lo = 0;
    public String longestPalindrome_2(String s) {
        char[] input = s.toCharArray();
        if(s.length() < 2) {
            return s;
        }

        for(int i = 0; i<input.length; i++) {
            expandPalindrome(input, i, i);
            expandPalindrome(input, i, i+1);
        }
        return s.substring(lo, lo+maxLen);
    }

    public void expandPalindrome(char[] s, int j, int k) {
        while(j >= 0 && k < s.length && s[j] == s[k]) {
            j--;
            k++;
        }
        if(maxLen < k - j - 1) {
            maxLen = k - j - 1;
            lo = j+1;
        }
    }

    // V3
    // IDEA: DP
    // https://leetcode.com/problems/longest-palindromic-substring/solutions/4212564/beats-9649-5-different-approaches-brute-u46gf/
    public String longestPalindrome_3(String s) {
        if (s.length() <= 1) {
            return s;
        }

        int maxLen = 1;
        int start = 0;
        int end = 0;
        boolean[][] dp = new boolean[s.length()][s.length()];

        for (int i = 0; i < s.length(); ++i) {
            dp[i][i] = true;
            for (int j = 0; j < i; ++j) {
                if (s.charAt(j) == s.charAt(i) && (i - j <= 2 || dp[j + 1][i - 1])) {
                    dp[j][i] = true;
                    if (i - j + 1 > maxLen) {
                        maxLen = i - j + 1;
                        start = j;
                        end = i;
                    }
                }
            }
        }

        return s.substring(start, end + 1);
    }




}
