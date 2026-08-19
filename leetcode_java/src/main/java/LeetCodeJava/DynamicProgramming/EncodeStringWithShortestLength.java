package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/encode-string-with-shortest-length/

/**
 *  471. Encode String with Shortest Length
 *  Hard
 *
 *  Given a string s, encode the string such that its encoded length is the
 *  shortest.
 *
 *  The encoding rule is k[encoded_string], where the encoded_string inside the
 *  square brackets is being repeated exactly k times. k should be a positive
 *  integer.
 *
 *  If an encoding process does not make the string shorter, then do not encode
 *  it. If there are several solutions, return any of them.
 *
 *  Example 1:
 *
 *  Input: s = "aaa"
 *  Output: "aaa"
 *  Explanation: There is no way to encode it such that it is shorter than the
 *  input string, so we do not encode it.
 *
 *  Example 2:
 *
 *  Input: s = "aaaaa"
 *  Output: "5[a]"
 *
 *  Example 3:
 *
 *  Input: s = "aaaaaaaaaa"
 *  Output: "10[a]"
 *
 *  Constraints:
 *
 *  1 <= s.length <= 150
 *  s consists of only lowercase English letters.
 */
public class EncodeStringWithShortestLength {

    // V0
    // IDEA: INTERVAL DP
    //  dp[i][j] = shortest encoding of s[i..j]
    //   1) whole-block repetition: find the smallest period p of s[i..j] via
    //      (t + t).indexOf(t, 1); if it repeats -> (len/p) + "[" + dp[i][i+p-1] + "]"
    //   2) split : dp[i][k] + dp[k+1][j]
    /**
     * time = O(n^4)  // O(n^2) states x O(n) splits, each doing O(n) string work
     * space = O(n^3) // O(n^2) states, each holding an O(n) string
     */
    public String encode(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        int n = s.length();
        String[][] dp = new String[n][n];

        for (int len = 1; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                String t = s.substring(i, j + 1);
                dp[i][j] = t;

                // 1) try encoding the whole block as k[...]
                //    NOTE: (t + t).indexOf(t, 1) gives the smallest period of t
                int pos = (t + t).indexOf(t, 1);
                if (pos < t.length()) {
                    // t is made of (len / pos) copies of t[0..pos-1]
                    String cand = (t.length() / pos) + "[" + dp[i][i + pos - 1] + "]";
                    if (cand.length() < dp[i][j].length()) {
                        dp[i][j] = cand;
                    }
                }

                // 2) try splitting into two encoded halves
                for (int k = i; k < j; k++) {
                    if (dp[i][k].length() + dp[k + 1][j].length() < dp[i][j].length()) {
                        dp[i][j] = dp[i][k] + dp[k + 1][j];
                    }
                }
            }
        }
        return dp[0][n - 1];
    }
}
