package LeetCodeJava.Math;

// https://leetcode.com/problems/the-k-th-lexicographical-string-of-all-happy-strings-of-length-n/description/

/**
 * 1415. The k-th Lexicographical String of All Happy Strings of Length n
 * Medium
 *
 * A happy string is a string that:
 *
 * consists only of letters of the set ['a', 'b', 'c'].
 * s[i] != s[i + 1] for all values of i from 1 to s.length - 1 (string is 1-indexed).
 *
 * For example, strings "abc", "ac", "b" and "abcbabcbcb" are all happy strings and strings "aa", "baa" and "ababbc" are not happy strings.
 *
 * Given two integers n and k, consider a list of all happy strings of length n sorted in lexicographical order.
 *
 * Return the kth string of this list or return an empty string if there are less than k happy strings of length n.
 *
 *
 * Example 1:
 *
 * Input: n = 1, k = 3
 * Output: "c"
 * Explanation: The list ["a", "b", "c"] contains all happy strings of length 1. The third string is "c".
 *
 * Example 2:
 *
 * Input: n = 1, k = 4
 * Output: ""
 * Explanation: There are only 3 happy strings of length 1.
 *
 * Example 3:
 *
 * Input: n = 3, k = 9
 * Output: "cab"
 * Explanation: There are 12 different happy string of length 3 ["aba", "abc", "aca", "acb", "bab", "bac", "bca", "bcb", "cab", "cac", "cba", "cbc"]. You will find the 9th string = "cab"
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10
 * 1 <= k <= 100
 *
 */
public class TheKThLexicographicalStringOfAllHappyStringsOfLengthN {

    private static final String LETTERS = "abc";

    // V0
    // IDEA: COUNTING / DIGIT SELECTION (no need to enumerate every string)
    //   first letter has 3 options, every later letter exactly 2 (anything but the previous one),
    //   so there are 3 * 2^(n-1) happy strings and each choice owns a contiguous block.
    //   pick letters left to right: rank / blockSize picks the branch, rank % blockSize descends.
    //   the remaining letters taken in "abc" order are already lexicographic.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String getHappyString(int n, int k) {
        // strings under each first-letter branch
        int blockSize = 1 << (n - 1);
        int total = 3 * blockSize;
        if (k > total) {
            return "";
        }

        // 0-based rank of the wanted string
        int rank = k - 1;
        StringBuilder sb = new StringBuilder();
        sb.append(LETTERS.charAt(rank / blockSize));
        rank %= blockSize;

        for (int pos = 1; pos < n; pos++) {
            // each of the 2 allowed letters now owns half of the current block
            blockSize /= 2;
            char previous = sb.charAt(sb.length() - 1);

            // the allowed letters, in lexicographic order
            char[] options = new char[2];
            int idx = 0;
            for (int i = 0; i < LETTERS.length(); i++) {
                char c = LETTERS.charAt(i);
                if (c != previous) {
                    options[idx] = c;
                    idx++;
                }
            }

            sb.append(options[rank / blockSize]);
            rank %= blockSize;
        }
        return sb.toString();
    }
}
