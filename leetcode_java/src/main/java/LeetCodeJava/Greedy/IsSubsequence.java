package LeetCodeJava.Greedy;

// https://leetcode.com/problems/is-subsequence/description/
/**
 * 392. Is Subsequence
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given two strings s and t, return true if s is a subsequence of t, or false otherwise.
 *
 * A subsequence of a string is a new string that is formed from the original string by deleting some (can be none) of the characters without disturbing the relative positions of the remaining characters. (i.e., "ace" is a subsequence of "abcde" while "aec" is not).
 *
 *
 *
 * Example 1:
 *
 * Input: s = "abc", t = "ahbgdc"
 * Output: true
 * Example 2:
 *
 * Input: s = "axc", t = "ahbgdc"
 * Output: false
 *
 *
 * Constraints:
 *
 * 0 <= s.length <= 100
 * 0 <= t.length <= 104
 * s and t consist only of lowercase English letters.
 *
 *
 * Follow up: Suppose there are lots of incoming s, say s1, s2, ..., sk where k >= 109, and you want to check one by one to see if t has its subsequence. In this scenario, how would you change your code?
 *
 *
 */
public class IsSubsequence {

    // V0
    // IDEA: 2 POINTERS (gemini)
    public boolean isSubsequence(String s, String t) {
        // Edge case: Empty s is always a subsequence of any t
        if (s.isEmpty())
            return true;
        // If t is empty but s isn't, s cannot be a subsequence
        if (t.isEmpty())
            return false;

        int i = 0; // Pointer for s
        int j = 0; // Pointer for t

        /** NOTE !!!
         *
         *  the while loop condition:
         *
         *     i < s.length()
         *     &&
         *     j < t.length()
         */
        while (i < s.length() && j < t.length()) {
            // If characters match, move the pointer for s
            if (s.charAt(i) == t.charAt(j)) {
                i++;
            }
            // Always move the pointer for t
            j++;
        }

        // If i reached the end of s, all characters were found in order
        return i == s.length();
    }

    // V0-0-1
    // IDEA: 2 POINTERS
    public boolean isSubsequence_0_0_1(String s, String t) {
        // edge
        if (s.isEmpty()) {
            return true;
        }
        if (t.isEmpty()) {
            return false;
        }
        if (s.equals(t)) {
            return true;
        }

        int s_idx = 0;
        int t_idx = 0;

        while (s_idx < s.length() && t_idx < t.length()) {
//            System.out.println(">>> s.charAt(s_idx) = " + s.charAt(s_idx) +
//                    ", t.charAt(t_idx) = " + t.charAt(t_idx));

            if (s.charAt(s_idx) == t.charAt(t_idx)) {
                // NOTE !!! early quit
                if (s_idx == s.length() - 1) {
                    return true;
                }
                s_idx += 1;
            }

            t_idx += 1;
        }

        return false;
    }


    // V0-1
    // IDEA: 2 POINTERS (gpt)
    public boolean isSubsequence_0_1(String s, String t) {
        // edge cases
        if (s.isEmpty())
            return true;
        if (t.isEmpty())
            return false;

        int len_s = s.length();
        int len_t = t.length();

        int idx_t = 0;

        for (int idx_s = 0; idx_s < len_s; idx_s++) {

            // move idx_t until we find s[idx_s]
            while (idx_t < len_t && s.charAt(idx_s) != t.charAt(idx_t)) {
                idx_t++;
            }

            // ran out of t without matching s[idx_s]
            if (idx_t == len_t) {
                return false;
            }

            // matched s[idx_s], move to next position in t
            idx_t++;
        }

        return true;
    }



    // V1-1
    // https://leetcode.ca/2016-12-26-392-Is-Subsequence/
    public boolean isSubsequence_1_1(String s, String t) {
        if (s == null || s.length() == 0) {
            return true;
        }

        int i = 0;
        for (int j = 0; j < t.length(); j++) {
            if (s.charAt(i) == t.charAt(j)) {
                i++;

                if (i == s.length()) { // last char is matched
                    return true;
                }
            }
        }

        return false;
    }


    // V1-2
    // https://leetcode.ca/2016-12-26-392-Is-Subsequence/
    public boolean isSubsequence_1_2(String s, String t) {
        int m = s.length(), n = t.length();
        int i = 0, j = 0;
        while (i < m && j < n) {
            if (s.charAt(i) == t.charAt(j)) {
                ++i;
            }
            ++j;
        }
        return i == m;
    }


    // V2
    // IDEA: 2 POINTERS
    // https://leetcode.com/problems/is-subsequence/solutions/6743977/video-two-pointer-solution-by-niits-7igj/
    public boolean isSubsequence_2(String s, String t) {
        int sp = 0;
        int tp = 0;

        while (sp < s.length() && tp < t.length()) {
            if (s.charAt(sp) == t.charAt(tp)) {
                sp++;
            }
            tp++;
        }

        return sp == s.length();
    }


    // V3
    // IDEA: 2 POINTERS
    // https://leetcode.com/problems/is-subsequence/solutions/7492640/using-two-pointers-on-time-o1-space-by-s-7ckv/
    public boolean isSubsequence_3(String s, String t) {
        int j = 0;

        for (int i = 0; i < t.length() && j < s.length(); i++) {
            if (s.charAt(j) == t.charAt(i)) {
                j++;
            }
        }

        return j == s.length();
    }

    // V4
    // IDEA: DP
    // https://leetcode.com/problems/is-subsequence/solutions/4074367/9376-two-pointers-dp-by-vanamsen-ejwx/


    // V5
    // IDEA: DP
    // https://leetcode.com/problems/is-subsequence/solutions/7443839/is-subsequence-dp-with-memoization-by-sh-xm4l/
    Boolean[][] dp;

    public boolean isSubsequence_5(String s, String t) {
        dp = new Boolean[s.length() + 1][t.length() + 1];
        return solve(0, 0, s, t);
    }

    private boolean solve(int i, int j, String s, String t) {
        if (i == s.length())
            return true;
        if (j == t.length())
            return false;

        if (dp[i][j] != null)
            return dp[i][j];
        if (s.charAt(i) == t.charAt(j)) {
            dp[i][j] = solve(i + 1, j + 1, s, t);
        } else {
            dp[i][j] = solve(i, j + 1, s, t);
        }

        return dp[i][j];
    }



    // FOLLOW UP:
    // https://leetcode.ca/2016-12-26-392-Is-Subsequence
//    class Solution_followup {
//        public boolean isSubsequence(String s, String t) {
//            // step 1: save all the index for the t
//            Map<Character, List<Integer>> map = new HashMap<>();
//            for (int i = 0; i < t.length(); i++) {
//                char c = t.charAt(i);
//                if (!map.containsKey(c)) {
//                    List<Integer> pos = new ArrayList<>();
//                    pos.add(i);
//                    map.put(c, pos);
//                } else {
//                    List<Integer> pos = map.get(c);
//                    pos.add(i);
//                    // map.put(c, pos);
//                }
//            }
//
//            // step 2: for each char in s, find the first index
//            int prevIndex = -1;
//            for (int i = 0; i < s.length(); i++) {
//                char c = s.charAt(i);
//                List<Integer> pos = map.get(c);
//                if (pos == null || pos.size() == 0) {
//                    return false;
//                }
//
//                int currentIndex = getNextIndexGreaterThanTarget(pos, prevIndex);
//
//                if (currentIndex == -1) {
//                    return false;
//                }
//
//                prevIndex = currentIndex;
//            }
//
//            return true;
//        }
//
//        // find next number greater than target
//        // if not found, return -1
//        private int getNextIndexGreaterThanTarget(List<Integer> pos, int targetIndex) {
//            int lo = 0;
//            int hi = pos.size() - 1;
//
//            while (lo + 1 <= hi) {
//                int mid = lo + (hi - lo) / 2;
//                if (pos.get(mid) == targetIndex) {
//                    lo = mid + 1;
//                } else if (pos.get(mid) > targetIndex) {
//                    hi = mid;
//                } else if (pos.get(mid) < targetIndex) {
//                    lo = mid + 1;
//                }
//            }
//
//            if (pos.get(lo) > targetIndex) {
//                return pos.get(lo);
//            }
//
//            if (pos.get(hi) > targetIndex) {
//                return pos.get(hi);
//            }
//
//            return -1;
//        }
//    }




}
