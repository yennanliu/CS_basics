package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/partition-string-into-minimum-beautiful-substrings/description/

import java.util.Arrays;

/**
 * 2767. Partition String Into Minimum Beautiful Substrings
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a binary string s, partition the string into one or more substrings such that each substring is beautiful.
 *
 * A string is beautiful if:
 *
 * It doesn't contain leading zeros.
 * It's the binary representation of a number that is a power of 5.
 * Return the minimum number of substrings in such partition. If it is impossible to partition the string s into beautiful substrings, return -1.
 *
 * A substring is a contiguous sequence of characters in a string.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "1011"
 * Output: 2
 * Explanation: We can paritition the given string into ["101", "1"].
 * - The string "101" does not contain leading zeros and is the binary representation of integer 51 = 5.
 * - The string "1" does not contain leading zeros and is the binary representation of integer 50 = 1.
 * It can be shown that 2 is the minimum number of beautiful substrings that s can be partitioned into.
 * Example 2:
 *
 * Input: s = "111"
 * Output: 3
 * Explanation: We can paritition the given string into ["1", "1", "1"].
 * - The string "1" does not contain leading zeros and is the binary representation of integer 50 = 1.
 * It can be shown that 3 is the minimum number of beautiful substrings that s can be partitioned into.
 * Example 3:
 *
 * Input: s = "0"
 * Output: -1
 * Explanation: We can not partition the given string into beautiful substrings.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 15
 * s[i] is either '0' or '1'.
 */
public class PartitionStringIntoMinimumBeautifulSubstrings {

    // V0
//    public int minimumBeautifulSubstrings(String s) {
//
//    }

    // V0-4
    // IDEA: DP
    // https://buildmoat.teachable.com/courses/7a7af3/lectures/64103646
    public boolean isPow5(int x) {
        while (x % 5 == 0) {
            x /= 5;
        }
        return x == 1;
    }

    public int minimumBeautifulSubstrings_0_4(String s) {
        int n = s.length();
        int[] dp = new int[n];

        for (int i = 0; i < n; i++) {
            dp[i] = -1;
        }

        for (int i = 0; i < n; i++) {
            int val = 0;

            if (s.charAt(i) != '0' && (i == 0 || dp[i - 1] != -1)) {
                for (int j = i; j < n; j++) {
                    val = val * 2 + (s.charAt(j) - '0');

                    if (isPow5(val)) {
                        if (i == 0) {
                            dp[j] = 1;
                        } else {
                            if (dp[j] == -1) {
                                dp[j] = dp[i - 1] + 1;
                            } else {
                                dp[j] = Math.min(dp[j], dp[i - 1] + 1);
                            }
                        }
                    }
                }
            }
        }

        return dp[n - 1];
    }


    // V1
    // IDEA: DP
    // https://leetcode.com/problems/partition-string-into-minimum-beautiful-substrings/solutions/3737219/javacpython-dp-by-lee215-ceow/
    public int minimumBeautifulSubstrings_1(String s) {
        int n = s.length(), dp[] = new int[n + 1];
        Arrays.fill(dp, n + 1);
        dp[0] = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '0')
                continue;
            for (int j = i, cur = 0; j < n; j++) {
                cur = cur * 2 + s.charAt(j) - '0';
                if (15625 % cur == 0)
                    dp[j + 1] = Math.min(dp[j + 1], dp[i] + 1);
            }
        }
        return dp[n] > n ? -1 : dp[n];
    }

    // V2
    // https://leetcode.com/problems/partition-string-into-minimum-beautiful-substrings/solutions/7189075/java-solution-by-dheerajpoojary-mgix/
    private int solve(String s, int start, int end, int dp[][]) {
        if (s.charAt(start) == '0')
            return Integer.MAX_VALUE;

        if (dp[start][end] != -1)
            return dp[start][end];

        String str = s.substring(start, end + 1);
        int num = Integer.parseInt(str, 2);
        if (isPowerOfFive(num))
            return 1;

        int res = Integer.MAX_VALUE;
        for (int i = start; i < end; i++) {
            int a = solve(s, start, i, dp);
            int b = solve(s, i + 1, end, dp);
            if (a != Integer.MAX_VALUE && b != Integer.MAX_VALUE) {
                res = Math.min(res, a + b);
            }
        }
        return dp[start][end] = res;
    }

    private boolean isPowerOfFive(int n) {
        while (n % 5 == 0) {
            n /= 5;
        }
        return n == 1;
    }

    public int minimumBeautifulSubstrings_2(String s) {
        int n = s.length();
        int dp[][] = new int[n + 1][n + 1];
        Arrays.stream(dp).forEach(row -> Arrays.fill(row, -1));
        int res = solve(s, 0, n - 1, dp);
        return res == Integer.MAX_VALUE ? -1 : res;
    }

    // V3
    // IDEA: DP
    // https://leetcode.com/problems/partition-string-into-minimum-beautiful-substrings/solutions/3737127/palindromic-partitioning-2-variation-c-j-ibm2/
    public boolean isFive(int num) {
        while (num > 1) {
            if (num % 5 != 0) {
                return false;
            }
            num /= 5;
        }
        return num == 1;
    }

    public boolean isSubstringBeautiful(int i, int j, String s) {
        String str = s.substring(i, j + 1);
        int k = 0;
        while (k < str.length()) {
            if (str.charAt(k) == '0') {
                k++;
                return false;
            } else {
                break;
            }
        }
        int ans = 0;
        for (int m = str.length() - 1; m >= 0; m--) {
            if (str.charAt(m) == '1') {
                ans += (1 << (str.length() - 1 - m));
            }
        }
        return isFive(ans);
    }

    public int func(int idx, String s, int[] dp) {
        if (idx == s.length())
            return 0;
        if (dp[idx] != -1)
            return dp[idx];
        int min_part = (int) 1e9;
        for (int j = idx; j < s.length(); j++) {
            if (isSubstringBeautiful(idx, j, s)) {
                int cost = 1 + func(j + 1, s, dp);
                min_part = Math.min(cost, min_part);
            }
        }
        dp[idx] = min_part;
        return min_part;
    }

    public int minimumBeautifulSubstrings_3(String s) {
        int[] dp = new int[s.length() + 1];
        Arrays.fill(dp, -1);
        int x = func(0, s, dp);
        return x == 1e9 ? -1 : x;
    }


}
