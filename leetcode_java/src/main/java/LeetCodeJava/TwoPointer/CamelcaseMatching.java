package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/camelcase-matching/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 1023. Camelcase Matching
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an array of strings queries and a string pattern, return a boolean array answer where answer[i] is true if queries[i] matches pattern, and false otherwise.
 *
 * A query word queries[i] matches pattern if you can insert lowercase English letters into the pattern so that it equals the query. You may insert a character at any position in pattern or you may choose not to insert any characters at all.
 *
 *
 *
 * Example 1:
 *
 * Input: queries = ["FooBar","FooBarTest","FootBall","FrameBuffer","ForceFeedBack"], pattern = "FB"
 * Output: [true,false,true,true,false]
 * Explanation: "FooBar" can be generated like this "F" + "oo" + "B" + "ar".
 * "FootBall" can be generated like this "F" + "oot" + "B" + "all".
 * "FrameBuffer" can be generated like this "F" + "rame" + "B" + "uffer".
 * Example 2:
 *
 * Input: queries = ["FooBar","FooBarTest","FootBall","FrameBuffer","ForceFeedBack"], pattern = "FoBa"
 * Output: [true,false,true,false,false]
 * Explanation: "FooBar" can be generated like this "Fo" + "o" + "Ba" + "r".
 * "FootBall" can be generated like this "Fo" + "ot" + "Ba" + "ll".
 * Example 3:
 *
 * Input: queries = ["FooBar","FooBarTest","FootBall","FrameBuffer","ForceFeedBack"], pattern = "FoBaT"
 * Output: [false,true,false,false,false]
 * Explanation: "FooBarTest" can be generated like this "Fo" + "o" + "Ba" + "r" + "T" + "est".
 *
 *
 * Constraints:
 *
 * 1 <= pattern.length, queries.length <= 100
 * 1 <= queries[i].length <= 100
 * queries[i] and pattern consist of English letters.
 *
 *
 *
 */
public class CamelcaseMatching {

    // V0
//    public List<Boolean> camelMatch(String[] queries, String pattern) {
//
//    }

    // V0-1
    // IDEA: 2 POINTERS + PATTERN MATCH (gemini)
    /**  CORE LOGIC:
     *
     * -> To solve **LC 1023 (CamelCase Matching)**,
     *
     * we need to check if a `query` string
     * can be transformed into a `pattern` string
     * by adding only **lowercase** letters.
     *
     * ### 💡 The Logic
     *
     * For a `query` to match a `pattern`:
     *
     * 1. All characters of the `pattern` must appear in the
     *    `query` in the **same order** (standard subsequence check).
     *
     *
     * 2. Any characters in the `query` that are **not**
     *     part of the `pattern` **must be lowercase**.
     *     If we encounter an extra uppercase letter in the
     *     `query` that isn't required by the `pattern`,
     *     the match is impossible.
     *
     *
     */
    /**  DEMO (dry run)
     *
     * ### 🔍 Dry Run Example
     *
     * **Pattern:** `"FB"`
     * **Query 1:** `"FooBar"`
     *
     * 1. `'F'` == `'F'`: Match! Move pattern pointer to `j=1`.
     * 2. `'o'`: Not in pattern, but it's lowercase. Continue.
     * 3. `'o'`: Not in pattern, but it's lowercase. Continue.
     * 4. `'B'` == `'B'`: Match! Move pattern pointer to `j=2`.
     * 5. `'a'`: Lowercase. Continue.
     * 6. `'r'`: Lowercase. Continue.
     * 7. End of query, `j` reached the end of pattern. **Result: True**.
     *
     * **Query 2:** `"FootBall"`
     *
     * 1. ... Matches up to `'B'`.
     * 2. Next char is `'a'` (lowercase).
     * 3. Next char is `'l'` (lowercase).
     * 4. Next char is **`'L'`** (Uppercase): This is an extra uppercase letter not in "FB". **Result: False**.
     *
     * ---
     *
     * ### 📊 Complexity
     *
     * * **Time Complexity**: , where  is the number of queries and  is the average length of a query. We look at each character of every query exactly once.
     * * **Space Complexity**:  (excluding the result list), as we only use two pointers.
     *
     */
    public List<Boolean> camelMatch_0_1(String[] queries, String pattern) {
        List<Boolean> result = new ArrayList<>();

        for (String query : queries) {
            result.add(isMatch_0_1(query, pattern));
        }

        return result;
    }

    private boolean isMatch_0_1(String query, String pattern) {
        /** NOTE !!! below
         *
         *
         *   i:  query pointer
         *   j:  pattern pointer
         *
         *
         * ------
         *
         * Exactly! You've got it.
         *
         * In your example `query = "abc"`,
         * the pointer `i` will start at **0**,
         * then move to **1**, then **2**, and finally the
         * loop ends when `i` reaches **3** (the length of the string).
         *
         * Here is the step-by-step breakdown of how the pointers behave during the loop:
         *
         * ### 📊 Pointer Trace Example
         *
         * Let's say **query** = `"aBc"` and **pattern** = `"ac"`
         *
         * | Step | `i` (Query) | `query.charAt(i)` | `j` (Pattern) | `pattern.charAt(j)` | Action |
         * | --- | --- | --- | --- | --- | --- |
         * | 1 | **0** | `'a'` | **0** | `'a'` | **Match!** Both `i` and `j` move forward. |
         * | 2 | **1** | `'B'` | **1** | *(No more chars)* | **Mismatch!** But wait, `'B'` is **Uppercase**. |
         * | 3 | **Result** |  |  |  | **FAIL**: Because an extra uppercase letter was found. |
         *
         * ---
         *
         * ### 💡 Key Pointer Rules for this Code:
         *
         * 1. **`i` (The Explorer):**
         * * Moves forward **every single step**, no matter what.
         * * It "scans" every character in the query to see if it's either a match for the pattern or a safe lowercase letter.
         *
         *
         * 2. **`j` (The Goal Tracker):**
         * * **Only moves** when it finds its matching partner in the query.
         * * If `j` reaches the end of the pattern (i.e., `j == pattern.length()`), it means we found the full subsequence.
         *
         *
         * 3. **The "Safety Check":**
         * * If `query.charAt(i)` is **not** the character `j` is looking for, the code checks: *"Is this character an Uppercase letter?"*
         * * If yes: **Instant Game Over** (`return false`).
         * * If no: **Keep going** (it's just a lowercase filler).
         */
        int i = 0; // Pointer for query
        int j = 0; // Pointer for pattern

        while (i < query.length()) {
            char qChar = query.charAt(i);

            // If characters match, move the pattern pointer
            if (j < pattern.length() && qChar == pattern.charAt(j)) {
                j++;
            }
            // If characters don't match, the extra character MUST be lowercase
            else if (Character.isUpperCase(qChar)) {
                return false;
            }

            // Always move the query pointer
            i++;
        }

        // Match is only valid if we successfully navigated through the entire pattern
        return j == pattern.length();
    }


    // V0-2
    // IDEA: 2 POINTERS (gpt)
    public List<Boolean> camelMatch_0_2(String[] queries, String pattern) {
        List<Boolean> ans = new ArrayList<>();
        for (String query : queries) {
            ans.add(isMatch_0_2(query, pattern));
        }
        return ans;
    }

    private boolean isMatch_0_2(String query, String pattern) {
        int i = 0, j = 0;
        int m = query.length(), n = pattern.length();

        while (i < m) {
            if (j < n && query.charAt(i) == pattern.charAt(j)) {
                // match current pattern char
                i++;
                j++;
            } else if (Character.isLowerCase(query.charAt(i))) {
                // skip lowercase letters
                i++;
            } else {
                // uppercase not matching pattern → fail
                return false;
            }
        }

        // success only if all pattern chars were matched
        return j == n;
    }


    // V1
    // IDEA: DP
    // https://leetcode.com/problems/camelcase-matching/solutions/270742/java-4ms-dp-solution-and-summarization-o-clpi/
    public List<Boolean> camelMatch(String[] queries, String pattern) {
        List<Boolean> resultList = new ArrayList<Boolean>();
        for (int i = 0; i < queries.length; i++) {
            if (isMatch_1(queries[i], pattern))
                resultList.add(true);
            else
                resultList.add(false);
        }
        return resultList;
    }

    public boolean isMatch_1(String query, String pattern) {
        boolean dp[][] = new boolean[query.length() + 1][pattern.length() + 1];
        dp[0][0] = true;
        for (int i = 0; i < query.length(); i++)
            if (Character.isLowerCase(query.charAt(i)))
                dp[i + 1][0] = dp[i][0];
        for (int i = 0; i < query.length(); i++) {
            for (int j = 0; j < pattern.length(); j++) {
                if (query.charAt(i) == pattern.charAt(j))
                    dp[i + 1][j + 1] = dp[i][j];
                else if (Character.isLowerCase(query.charAt(i)))
                    dp[i + 1][j + 1] = dp[i][j + 1];
            }
        }
        return dp[query.length()][pattern.length()];
    }


    // V2
    // https://leetcode.com/problems/camelcase-matching/solutions/1205803/java-simple-100-runtime-by-trevor-akshay-6jxw/
    public List<Boolean> camelMatch_2(String[] queries, String pattern) {
        List<Boolean> list = new ArrayList<>();

        for (String q : queries) {
            int index = 0;
            boolean flag = true;
            for (char c : q.toCharArray()) {
                if (index < pattern.length() && c == pattern.charAt(index)) {
                    index++;
                    continue;
                }
                if (c >= 'A' && c <= 'Z') {
                    if (index >= pattern.length() || c != pattern.charAt(index)) {
                        flag = false;
                        break;
                    }
                }
            }
            flag = flag && index == pattern.length();
            list.add(flag);
        }
        return list;
    }


    // V3




}
