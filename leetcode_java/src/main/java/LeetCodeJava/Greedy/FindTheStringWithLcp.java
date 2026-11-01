package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-the-string-with-lcp/description/

/**
 * 2573. Find the String with LCP
 * Hard
 *
 * We define the lcp matrix of any 0-indexed string word of n lowercase English letters as an n x n grid such that:
 *
 * lcp[i][j] is equal to the length of the longest common prefix between the substrings word[i,n-1] and word[j,n-1].
 *
 * Given an n x n matrix lcp, return the alphabetically smallest string word that corresponds to lcp. If there is no such string, return an empty string.
 *
 * A string a is lexicographically smaller than a string b (of the same length) if in the first position where a and b differ, string a has a letter that appears earlier in the alphabet than the corresponding letter in b. For example, "aabd" is lexicographically smaller than "aaca" because the first position they differ is at the third letter, and 'b' comes before 'c'.
 *
 *
 * Example 1:
 *
 * Input: lcp = [[4,0,2,0],[0,3,0,1],[2,0,2,0],[0,1,0,1]]
 * Output: "abab"
 * Explanation: lcp corresponds to any 4 letter string with two alternating letters. The lexicographically smallest of them is "abab".
 *
 * Example 2:
 *
 * Input: lcp = [[4,3,2,1],[3,3,2,1],[2,2,2,1],[1,1,1,1]]
 * Output: "aaaa"
 * Explanation: lcp corresponds to any 4 letter string with a single distinct letter. The lexicographically smallest of them is "aaaa".
 *
 * Example 3:
 *
 * Input: lcp = [[4,3,2,1],[3,3,2,1],[2,2,2,1],[1,1,1,3]]
 * Output: ""
 * Explanation: lcp[3][3] cannot be equal to 3 since word[3,...,3] consists of only a single letter; Thus, no answer exists.
 *
 *
 * Constraints:
 *
 * 1 <= n == lcp.length == lcp[i].length <= 1000
 * 0 <= lcp[i][j] <= n
 *
 */
public class FindTheStringWithLcp {

    // V0
    // IDEA: GREEDY CONSTRUCTION + FULL DP RE-VERIFICATION
    //       (1) lcp[i][j] > 0 means word[i] == word[j]. give the class of the leftmost
    //           unassigned index the next unused letter ('a', 'b', ...) -> smallest string.
    //           more than 26 classes -> impossible.
    //       (2) the construction only used the sign of lcp, so recompute the real lcp with
    //           real[i][j] = (s[i] == s[j]) ? real[i + 1][j + 1] + 1 : 0 and compare every cell.
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public String findTheString(int[][] lcp) {
        int n = lcp.length;
        char[] word = new char[n]; // '\0' means "no letter yet"

        // (1) greedy letter assignment, smallest letter to the leftmost class
        int start = 0;
        for (char letter = 'a'; letter <= 'z'; letter++) {
            // find the leftmost position that still has no letter
            while (start < n && word[start] != 0) {
                start++;
            }
            if (start == n) {
                break;
            }
            // every later position sharing a prefix with `start` gets the same letter
            for (int j = start; j < n; j++) {
                if (lcp[start][j] > 0) {
                    word[j] = letter;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (word[i] == 0) {
                // the matrix needs more than 26 distinct letters
                return "";
            }
        }

        // (2) recompute the true lcp row by row from the bottom; only row i + 1 is needed.
        //     index n of each row is a zero guard so the last column needs no special case.
        int[] nextRow = new int[n + 1];
        int[] currRow = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                int real = 0;
                if (word[i] == word[j]) {
                    real = nextRow[j + 1] + 1;
                }
                if (real != lcp[i][j]) {
                    return "";
                }
                currRow[j] = real;
            }
            // the row just built becomes "row i + 1" for the next iteration
            int[] swap = nextRow;
            nextRow = currRow;
            currRow = swap;
        }

        return new String(word);
    }
}
