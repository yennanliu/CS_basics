package LeetCodeJava.TwoPointer;

// https://leetcode.ca/2018-10-20-1055-Shortest-Way-to-Form-String/
// https://leetcode.com/problems/shortest-way-to-form-string/description/

/**
 *  1055. Shortest Way to Form String
 * Description
 * A subsequence of a string is a new string that is formed from the original string by deleting some (can be none) of the characters without disturbing the relative positions of the remaining characters. (i.e., "ace" is a subsequence of "abcde" while "aec" is not).
 *
 * Given two strings source and target, return the minimum number of subsequences of source such that their concatenation equals target. If the task is impossible, return -1.
 *
 *
 *
 * Example 1:
 *
 * Input: source = "abc", target = "abcbc"
 * Output: 2
 * Explanation: The target "abcbc" can be formed by "abc" and "bc", which are subsequences of source "abc".
 * Example 2:
 *
 * Input: source = "abc", target = "acdbc"
 * Output: -1
 * Explanation: The target string cannot be constructed from the subsequences of source string due to the character "d" in target string.
 * Example 3:
 *
 * Input: source = "xyz", target = "xzyxz"
 * Output: 3
 * Explanation: The target string can be constructed as follows "xz" + "y" + "xz".
 *
 *
 * Constraints:
 *
 * 1 <= source.length, target.length <= 1000
 * source and target consist of lowercase English letters.
 *
 * 
 */
public class ShortestWayToFormString {

    // V0
    // IDEA : 2 POINTER (greedy: consume as much of target as one pass of source can)
    /**
     *  1) Impossibility test first: if target uses a letter that source does not
     *     contain at all, no number of copies can ever cover it -> -1.
     *     (Doing this up front is what lets the main loop stay simple - it can
     *     then assume every pass consumes at least one target char, so it always
     *     terminates.)
     *
     *  2) Greedy: for each "copy" of source, sweep source left to right with `i`
     *     and advance `j` over target on every match. Taking every match as
     *     early as possible is optimal - matching a char later can only leave a
     *     shorter suffix of source for the remaining target chars, so it can
     *     never need FEWER copies.
     *
     *  Once source is exhausted, we start a new copy (cnt++) and reset i = 0.
     *
     *  time  = O(M * K), K = answer <= N, so O(M * N) worst case
     *  space = O(1)  (26 sized alphabet flag array)
     */
    public int shortestWay(String source, String target) {

        // edge
        if (source == null || source.isEmpty() || target == null || target.isEmpty()) {
            return -1;
        }

        // 1) every target char must exist somewhere in source
        boolean[] inSource = new boolean[26];
        for (int k = 0; k < source.length(); k++) {
            inSource[source.charAt(k) - 'a'] = true;
        }
        for (int k = 0; k < target.length(); k++) {
            if (!inSource[target.charAt(k) - 'a']) {
                return -1;
            }
        }

        int m = source.length();
        int n = target.length();
        int j = 0;   // pointer on target
        int cnt = 0; // number of source subsequences used

        // 2) greedy sweeps over source
        while (j < n) {
            cnt++; // start a new copy of source
            int i = 0;
            while (i < m && j < n) {
                if (source.charAt(i) == target.charAt(j)) {
                    j++;
                }
                i++;
            }
        }

        return cnt;
    }

    // V1_1
    // IDEA : 2 POINTER (gpt)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int shortestWay_1(String source, String target) {
        // Step 1: Check if every character in target exists in source
        for (char x : target.toCharArray()) {
            if (source.indexOf(x) == -1) {
                return -1;
            }
        }

        int sourceLen = source.length();
        int targetLen = target.length();
        int sourceIdx = 0;
        int targetIdx = 0;
        int res = 0;

        // Step 2: Iterate through the target string
        while (targetIdx < targetLen) {
            int currentIdx = targetIdx;

            // Step 3: Match as many characters as possible from source with target
            /** NOTE !!! below logic
             *
             *   while src idx < src len && target idx < target len.
             *   keep comparing src and target val
             */
            while (sourceIdx < sourceLen && targetIdx < targetLen) {
                if (source.charAt(sourceIdx) == target.charAt(targetIdx)) {
                    targetIdx++;
                }
                sourceIdx++;
            }

            // Step 4: If no progress was made in this pass, it means the target cannot be formed
            if (targetIdx == currentIdx) {
                return -1;
            }

            // Step 5: Reset source index and increment the subsequence count
            /** NOTE !!! reset src idx after src idx reach src len */
            sourceIdx = 0;
            res++;
        }

        return res;
    }


    // V2_1
    // https://leetcode.ca/2018-10-20-1055-Shortest-Way-to-Form-String/
    // IDEA : 2 POINTER
    /**
     * time = O(N)
     * space = O(1)
     */
    public int shortestWay_2_1(String source, String target) {
        int m = source.length(), n = target.length();
        int ans = 0, j = 0;
        while (j < n) {
            int i = 0;
            boolean ok = false;
            while (i < m && j < n) {
                if (source.charAt(i) == target.charAt(j)) {
                    ok = true;
                    ++j;
                }
                ++i;
            }
            if (!ok) {
                return -1;
            }
            ++ans;
        }
        return ans;
    }

    // V2

}
