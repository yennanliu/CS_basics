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
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
    // IDEA: DP (GPT)
    /**  NOTE !!!
     *
     * 1. What does dp[i][j] mean?
     *
     *  -  The substring from index i to index j
     *     (inclusive) is a palindrome.
     *
     *  Example:
     *     s = "b a b a d"
     *      0 1 2 3 4
     *
     *   Example meanings:
     * 	•	dp[0][0] = true → "b"
     * 	•	dp[0][2] = true → "bab"
     * 	•	dp[1][3] = true → "aba"
     * 	•	dp[2][4] = false → "bad"
     *
     * --------------
     *
     * 2.  DP Table
     *
     *   dp[i][j]
     *
     *    Rows → start index i
     *    Columns → end index j
     *
     *    Only the upper-right triangle
     *    is used (i <= j).
     *
     *   Example:
     *
     *              j →
     *       0   1   2   3   4
     * i  ┌───────────────────
     * 0  │ T   F   T   F   F
     * 1  │     T   F   T   F
     * 2  │         T   F   F
     * 3  │             T   F
     * 4  │                 T
     *
     *
     */
    /**  NOTE !!!
     *
     *
     *  Explain Transition Formula ?
     *
     *  ```
     *  dp[i][j] = true
     *     if:
     *         s[i] == s[j] AND
     *         (j - i <= 2 OR dp[i + 1][j - 1])
     *  ```
     *
     *
     *  Great question 👍
     * This transition formula is the heart of LC 005, so let’s break it down slowly, visually, and logically.
     *
     * ⸻
     *
     * 🔑 The Transition Formula (Deep Dive)
     *
     * dp[i][j] = true
     * if:
     *     s[i] == s[j] AND
     *     (j - i <= 2 OR dp[i + 1][j - 1])
     *
     *
     * ⸻
     *
     * Step 1️⃣ What are we trying to prove?
     *
     * We want to know:
     *
     * Is the substring s[i..j] a palindrome?
     *
     * A palindrome means:
     * 	•	first and last characters are equal
     * 	•	the inside substring is also a palindrome
     *
     * ⸻
     *
     * Step 2️⃣ Condition #1 — s[i] == s[j]
     *
     * Why is this required?
     *
     * A palindrome must mirror:
     *
     * s[i] ...... s[j]
     *    ↑          ↑
     *
     * Example ❌:
     *
     * "abca"
     *  i    j
     * 'a' != 'a' → OK
     * but "bc" inside is not palindrome
     *
     * Example ❌:
     *
     * "ab"
     * 'a' != 'b' → NOT palindrome
     *
     * So:
     *
     * If s[i] != s[j] → dp[i][j] = false immediately
     *
     *
     * ⸻
     *
     * Step 3️⃣ Condition #2 — What about the middle?
     *
     * Now assume:
     *
     * s[i] == s[j]
     *
     * We still need to check the inside substring:
     *
     * s[i+1 .. j-1]
     *
     *
     * ⸻
     *
     * Step 4️⃣ The Key Optimization
     *
     * Why do we split into 2 cases?
     *
     * (j - i <= 2) OR dp[i + 1][j - 1]
     *
     * Because short substrings behave differently.
     *
     * ⸻
     *
     * Case A️⃣ j - i <= 2 (Length ≤ 3)
     *
     * Let’s calculate length:
     *
     * length = j - i + 1
     *
     * j - i	length	example
     * 0	1	“a”
     * 1	2	“aa”
     * 2	3	“aba”
     *
     * Why auto-true?
     *
     * Length 1
     *
     * "a" → palindrome
     *
     * Length 2
     *
     * "aa" → palindrome if chars equal
     *
     * Length 3
     *
     * "aba"
     *
     * Middle is one character, always palindrome.
     *
     * 👉 No need to check dp[i+1][j-1].
     *
     * That’s why:
     *
     * (j - i <= 2)
     *
     * is sufficient.
     *
     * ⸻
     *
     * Case B️⃣ j - i > 2 (Length ≥ 4)
     *
     * Example:
     *
     * "abccba"
     *  i      j
     *
     * Now the inside is:
     *
     * "bccb"
     *
     * This is not guaranteed to be a palindrome.
     *
     * So we must check:
     *
     * dp[i + 1][j - 1]
     *
     * Only if the inside is palindrome can the whole string be palindrome.
     *
     * ⸻
     *
     * Step 5️⃣ Putting It Together
     *
     * Full Logical Meaning
     *
     * s[i..j] is palindrome if:
     *     1) first and last characters match
     *     2) AND either:
     *         - it's short (≤3), OR
     *         - the inside substring is palindrome
     *
     *
     * ⸻
     *
     * Visual Example Walkthrough
     *
     * Example 1️⃣ "aba"
     *
     * i = 0, j = 2
     * s[i] == s[j] → 'a' == 'a'
     * j - i = 2 → small substring
     * → dp[0][2] = true
     *
     *
     * ⸻
     *
     * Example 2️⃣ "abba"
     *
     * i = 0, j = 3
     * s[i] == s[j] → 'a' == 'a'
     * j - i = 3 → must check dp[1][2]
     *
     * Check inside:
     *
     * dp[1][2] = "bb" → true
     *
     * So:
     *
     * dp[0][3] = true
     *
     *
     * ⸻
     *
     * Example 3️⃣ "abca"
     *
     * i = 0, j = 3
     * s[i] == s[j] → 'a' == 'a'
     * j - i = 3 → check dp[1][2]
     *
     * dp[1][2] = "bc" → false
     *
     * So:
     *
     * dp[0][3] = false
     *
     *
     * ⸻
     *
     * Why This Is DP (Not Just Brute Force)
     *
     * We reuse previously computed answers:
     *
     * dp[i+1][j-1] already known
     *
     * This avoids checking substrings repeatedly.
     *
     * ⸻
     *
     * Mental Model (Interview Gold)
     *
     * Think like this:
     *
     * “If I wrap the same character on both ends of a palindrome,
     * I still have a palindrome.”
     *
     * ⸻
     *
     * TL;DR (One-Line Intuition)
     *
     * A string is palindrome if:
     * ends match AND the middle is palindrome
     * (short strings skip the middle check)
     *
     *
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public String longestPalindrome_0_2(String s) {
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
                    /**  NOTE !!!   why `dp[i + 1][j - 1]` ?
                     *
                     *  1.  dp[i][j]: "Is the substring starting at [i,j] a palindrome?"
                     *
                     *  2. based on 1), given input s = "LEVEL"
                     *    -> if want to know if substring between
                     *        - "L" at idx = 0 and  "L" at idx = 4
                     *           is palindrome
                     *
                     *      -> we need to check substring within idx=1 and idx=3
                     *
                     *        -> e.g. dp[0+1][4-1] = dp[1][3]
                     *
                     */
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


    // V0-3
    // IDEA: DP (gemini)
    /**  NOTE !!!
     *
     *   dp[i][j]: "Is the substring starting at [i,j] a palindrome?"
     *
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public String longestPalindrome_0_3(String s) {
        // 1. Handle empty strings
        if (s == null || s.length() == 0)
            return "";

        int n = s.length();
        // 2. Initialize 2D table. Default values in Java are 'false'.
        boolean[][] dp = new boolean[n][n];
        int start = 0;
        int maxLen = 1;

        // 3. Base Case: Every individual character (length 1) is a palindrome.
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }

        // 4. Iterate through possible substring lengths starting from 2 up to n.
        for (int len = 2; len <= n; len++) {
            // 5. 'i' is the starting index of the substring.
            for (int i = 0; i <= n - len; i++) {
                // 6. 'j' is the ending index of the substring.
                int j = i + len - 1;

                // 7. Core logic: Check if outer characters match.
                if (s.charAt(i) == s.charAt(j)) {
                    // 8. If length is 2 (e.g., "aa"), it's a palindrome if s[i] == s[j].
                    if (len == 2) {
                        dp[i][j] = true;
                    }
                    // 9. If length > 2, check if the inner string is a palindrome.
                    // This looks up the result we already calculated for a smaller length.
                    /**  NOTE !!!   why `dp[i + 1][j - 1]` ?
                     *
                     *  1.  dp[i][j]: "Is the substring starting at [i,j] a palindrome?"
                     *
                     *  2. based on 1), given input s = "LEVEL"
                     *    -> if want to know if substring between
                     *        - "L" at idx = 0 and  "L" at idx = 4
                     *           is palindrome
                     *
                     *      -> we need to check substring within idx=1 and idx=3
                     *
                     *        -> e.g. dp[0+1][4-1] = dp[1][3]
                     *
                     */
                    else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                }

                // 10. If the current s[i...j] is a palindrome and longer than our previous max...
                if (dp[i][j] && len > maxLen) {
                    maxLen = len; // Update the longest length found
                    start = i; // Save the starting index for extraction later
                }
            }
        }

        // 11. Extract the final substring using the saved start and length.
        return s.substring(start, start + maxLen);
    }



    // V1
    // IDEA : BRUTE FORCE + isPalindrome check
    // https://leetcode.com/problems/longest-palindromic-substring/solutions/4212564/beats-96-49-5-different-approaches-brute-force-eac-dp-ma-recursion/
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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

    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
