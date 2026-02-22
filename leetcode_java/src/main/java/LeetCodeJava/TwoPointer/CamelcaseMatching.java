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

    // V1
    // IDEA: DP
    // https://leetcode.com/problems/camelcase-matching/solutions/270742/java-4ms-dp-solution-and-summarization-o-clpi/
    public List<Boolean> camelMatch(String[] queries, String pattern) {
        List<Boolean> resultList = new ArrayList<Boolean>();
        for (int i = 0; i < queries.length; i++) {
            if (isMatch(queries[i], pattern))
                resultList.add(true);
            else
                resultList.add(false);
        }
        return resultList;
    }

    public boolean isMatch(String query, String pattern) {
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
