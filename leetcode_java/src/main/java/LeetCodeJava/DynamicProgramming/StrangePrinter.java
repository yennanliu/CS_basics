package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/strange-printer/description/

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
/**
 * 664. Strange Printer
 * Hard
 *
 * There is a strange printer with the following two special properties:
 *
 * The printer can only print a sequence of the same character each time.
 * At each turn, the printer can print new characters starting from and ending at any
 * place and will cover the original existing characters.
 *
 * Given a string s, return the minimum number of turns the printer needed to print it.
 *
 * Example 1:
 *
 * Input: s = "aaabbb"
 * Output: 2
 * Explanation: Print "aaa" first and then print "bbb".
 *
 * Example 2:
 *
 * Input: s = "aba"
 * Output: 2
 * Explanation: Print "aaa" first and then print "b" from the second place of the string,
 * which will cover the existing character 'a'.
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s consists of lowercase English letters.
 *
 */
public class StrangePrinter {

    // V0
    // IDEA: INTERVAL DP
    /**
     *  PRE-STEP: COLLAPSE runs of equal characters ("aaabbb" -> "ab").
     *  A run costs the SAME as a single character, and collapsing shrinks n a lot.
     *
     *  DP def:
     *    - dp[i][j] = minimum turns to print s[i..j]
     *
     *  DP eq (think about the LAST character s[j]):
     *    - worst case: print s[j] on its OWN turn      -> dp[i][j] = dp[i][j-1] + 1
     *    - BETTER: if some k in [i, j-1] has s[k] == s[j], the SAME turn that printed
     *      s[k] can be STRETCHED to cover position j, so s[j] is FREE:
     *        dp[i][j] = min(dp[i][j], dp[i][k] + dp[k+1][j-1])
     *      (dp[k+1][j-1] is 0 when the middle segment is empty)
     *
     *  Iterate i DOWNWARDS so dp[i][k] / dp[k+1][j-1] are already computed.
     *
     *  time  = O(n^3)
     *  space = O(n^2)
     */
    public int strangePrinter(String s) {
        // COLLAPSE consecutive duplicates
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (sb.length() == 0 || sb.charAt(sb.length() - 1) != s.charAt(i)) {
                sb.append(s.charAt(i));
            }
        }

        char[] compact = sb.toString().toCharArray();
        int n = compact.length;
        if (n == 0) {
            return 0;
        }

        int[][] dp = new int[n][n];

        for (int i = n - 1; i >= 0; i--) {
            dp[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                // baseline: s[j] needs its OWN turn
                dp[i][j] = dp[i][j - 1] + 1;
                for (int k = i; k < j; k++) {
                    if (compact[k] == compact[j]) {
                        int mid = (k + 1 <= j - 1) ? dp[k + 1][j - 1] : 0;
                        dp[i][j] = Math.min(dp[i][j], dp[i][k] + mid);
                    }
                }
            }
        }

        return dp[0][n - 1];
    }


    // V1
    // IDEA: TOP-DOWN MEMOISED INTERVAL DP
    /**
     *  The same recurrence written as recursion over (i, j), which makes the
     *  `stretch the turn that printed s[k] to cover s[j]` case read as a direct
     *  choice rather than as a loop body.
     *
     *  time  = O(n^3)
     *  space = O(n^2)
     */
    private char[] compactP;
    private Integer[][] memoP;

    public int strangePrinter_1(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (sb.length() == 0 || sb.charAt(sb.length() - 1) != s.charAt(i)) {
                sb.append(s.charAt(i));
            }
        }
        compactP = sb.toString().toCharArray();
        int n = compactP.length;
        if (n == 0) {
            return 0;
        }
        memoP = new Integer[n][n];
        return solveP(0, n - 1);
    }

    private int solveP(int i, int j) {
        if (i > j) {
            return 0;
        }
        if (i == j) {
            return 1;
        }
        if (memoP[i][j] != null) {
            return memoP[i][j];
        }

        int res = solveP(i, j - 1) + 1;          // s[j] gets its own turn
        for (int k = i; k < j; k++) {
            if (compactP[k] == compactP[j]) {
                res = Math.min(res, solveP(i, k) + solveP(k + 1, j - 1));
            }
        }
        memoP[i][j] = res;
        return res;
    }

    // V2
    // IDEA: THINK ABOUT THE FIRST CHARACTER instead of the last
    /**
     *  Symmetric recurrence: print s[i] first, covering [i, j], then the same turn
     *  can be reused at any later k with s[k] == s[i].
     *
     *      dp[i][j] = 1 + dp[i+1][j], improved by
     *                 dp[i+1][k-1] + dp[k][j]  for k in (i, j] with s[k] == s[i]
     *
     *  Useful as a cross-check: a wrong index in one direction rarely produces the
     *  same wrong answer in the other.
     *
     *  time  = O(n^3)
     *  space = O(n^2)
     */
    public int strangePrinter_2(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (sb.length() == 0 || sb.charAt(sb.length() - 1) != s.charAt(i)) {
                sb.append(s.charAt(i));
            }
        }
        char[] c = sb.toString().toCharArray();
        int n = c.length;
        if (n == 0) {
            return 0;
        }

        int[][] dp = new int[n + 1][n + 1];
        for (int i = n - 1; i >= 0; i--) {
            dp[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                dp[i][j] = 1 + dp[i + 1][j];
                for (int k = i + 1; k <= j; k++) {
                    if (c[k] == c[i]) {
                        int mid = (i + 1 <= k - 1) ? dp[i + 1][k - 1] : 0;
                        dp[i][j] = Math.min(dp[i][j], mid + dp[k][j]);
                    }
                }
            }
        }
        return dp[0][n - 1];
    }

    // V3
    // IDEA: BFS OVER STRING STATES (tiny inputs only)
    /**
     *  Treat each partially printed string as a state and BFS over `print character
     *  c across positions [l, r]`.
     *
     *  Explodes immediately, so it only terminates for very short inputs -- but it
     *  performs the operation the STATEMENT defines rather than the interval
     *  recurrence, which is what makes it a genuine oracle.
     *
     *  time  = exponential
     *  space = exponential
     */
    public int strangePrinter_3(String s) {
        if (s.isEmpty()) {
            return 0;
        }
        char[] blankArr = new char[s.length()];
        Arrays.fill(blankArr, '.');
        String blank = new String(blankArr);

        Set<Character> alphabet = new HashSet<>();
        for (char ch : s.toCharArray()) {
            alphabet.add(ch);
        }

        Deque<String> q = new ArrayDeque<>();
        Set<String> seen = new HashSet<>();
        q.offer(blank);
        seen.add(blank);
        int turns = 0;

        while (!q.isEmpty()) {
            if (q.contains(s)) {
                return turns;
            }
            int levelSize = q.size();
            turns += 1;
            for (int t = 0; t < levelSize; t++) {
                String cur = q.poll();
                for (char ch : alphabet) {
                    for (int l = 0; l < s.length(); l++) {
                        for (int r = l; r < s.length(); r++) {
                            char[] nx = cur.toCharArray();
                            boolean useful = false;
                            for (int p = l; p <= r; p++) {
                                if (nx[p] != ch) {
                                    useful = true;
                                }
                                nx[p] = ch;
                            }
                            if (!useful) {
                                continue;
                            }
                            String key = new String(nx);
                            if (key.equals(s)) {
                                return turns;
                            }
                            if (compatible(key, s) && seen.add(key)) {
                                q.offer(key);
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }

    /** prune: a printed character can never be un-printed to a DIFFERENT target char */
    private boolean compatible(String cur, String target) {
        int fixed = 0;
        for (int i = 0; i < cur.length(); i++) {
            if (cur.charAt(i) != '.' && cur.charAt(i) == target.charAt(i)) {
                fixed += 1;
            }
        }
        return fixed > 0;
    }

}
