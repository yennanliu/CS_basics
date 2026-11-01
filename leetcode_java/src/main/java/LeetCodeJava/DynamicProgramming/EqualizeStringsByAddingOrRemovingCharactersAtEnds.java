package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/equalize-strings-by-adding-or-removing-characters-at-ends/description/
// https://leetcode.ca/2024-05-20-3135-Equalize-Strings-by-Adding-or-Removing-Characters-at-Ends/

/**
 * 3135. Equalize Strings by Adding or Removing Characters at Ends
 * Medium
 *
 * You are given two strings initial and target. In one operation, you can add or remove one character only at the beginning or the end of the string initial.
 *
 * Return the minimum number of operations required to transform the string initial into the string target.
 *
 *
 * Example 1:
 *
 * Input: initial = "abcde", target = "cdef"
 * Output: 3
 * Explanation:
 * Remove 'a' and 'b' from the beginning of initial, then add 'f' to the end.
 *
 * Example 2:
 *
 * Input: initial = "axxy", target = "yabx"
 * Output: 6
 * Explanation:
 * Remove "xxy" from initial, then add "yab" — 3 removals and 3 additions.
 *
 * Example 3:
 *
 * Input: initial = "xyz", target = "xyz"
 * Output: 0
 * Explanation:
 * No operations are needed.
 *
 *
 * Constraints:
 *
 * 1 <= initial.length, target.length <= 1000
 * initial and target consist only of lowercase English letters.
 *
 */
public class EqualizeStringsByAddingOrRemovingCharactersAtEnds {

    // V0
    // IDEA: LONGEST COMMON SUBSTRING (2D DP, ROLLING ROW)
    //   Every operation touches an end, so what survives from initial is a contiguous block
    //   that must also appear contiguously in target. Keeping a block of length L costs
    //   (len(initial) - L) removals + (len(target) - L) additions, so maximise L.
    //   common[i][j] = length of the common substring ending at initial[i-1] and target[j-1]
    //               = common[i-1][j-1] + 1 if the characters match, else 0 (reset = contiguity).
    /**
     * time = O(N * M)
     * space = O(M)
     */
    public int minOperations(String initial, String target) {
        int n = initial.length();
        int m = target.length();
        int[] prevRow = new int[m + 1];
        int longest = 0;
        for (int i = 1; i <= n; i++) {
            int[] curRow = new int[m + 1];
            for (int j = 1; j <= m; j++) {
                if (initial.charAt(i - 1) == target.charAt(j - 1)) {
                    curRow[j] = prevRow[j - 1] + 1;
                    longest = Math.max(longest, curRow[j]);
                }
            }
            prevRow = curRow;
        }
        return (n - longest) + (m - longest);
    }
}
