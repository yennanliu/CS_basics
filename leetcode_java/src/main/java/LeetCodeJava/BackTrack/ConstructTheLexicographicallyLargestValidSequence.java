package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/construct-the-lexicographically-largest-valid-sequence/

/**
 *  1718. Construct the Lexicographically Largest Valid Sequence
 *  Medium
 *
 *  Given an integer n, find a sequence with elements in the range [1, n] that
 *  satisfies all of the following:
 *    - The integer 1 occurs once in the sequence.
 *    - Each integer between 2 and n occurs twice in the sequence.
 *    - For every integer i between 2 and n, the distance between the two
 *      occurrences of i is exactly i.
 *
 *  The distance between a[i] and a[j] is |j - i|.
 *  Return the lexicographically largest sequence. It is guaranteed that under
 *  the given constraints there is always a solution.
 *
 *  Example 1:
 *    Input: n = 3
 *    Output: [3,1,2,3,2]
 *    Explanation: [2,3,2,1,3] is also valid, but [3,1,2,3,2] is the
 *                 lexicographically largest one.
 *
 *  Example 2:
 *    Input: n = 5
 *    Output: [5,3,1,4,3,5,2,4,2]
 *
 *  Constraints:
 *    1 <= n <= 20
 */
public class ConstructTheLexicographicallyLargestValidSequence {

    // V0
    // IDEA: BACKTRACKING, TRY BIG VALUES FIRST (first success = the answer)
    //       the sequence has length 2n - 1 (1 appears once, 2..n appear twice).
    //       fill left to right; at the leftmost EMPTY slot i try n, n-1, ..., 1
    //       in DESCENDING order. since we commit greedily to the biggest value
    //       that can still be completed, the FIRST full assignment reached is
    //       already the lexicographically largest one.
    //       placing v at i also fixes its partner:
    //         v >= 2 -> slot i + v must be free and inside the array
    //         v == 1 -> occupies only slot i
    //       NOTE: a slot may already be filled by an earlier value's partner,
    //             then just skip ahead to i + 1.
    /**
     * time = O(n!) worst case (heavily pruned, n <= 20)
     * space = O(n)
     */
    public int[] constructDistancedSequence(int n) {
        int size = 2 * n - 1;
        int[] res = new int[size];
        boolean[] used = new boolean[n + 1];
        dfs(0, size, n, res, used);
        return res;
    }

    private boolean dfs(int i, int size, int n, int[] res, boolean[] used) {
        if (i == size) {
            return true;
        }
        if (res[i] != 0) {
            return dfs(i + 1, size, n, res, used);
        }
        for (int v = n; v >= 1; v--) {
            if (used[v]) {
                continue;
            }
            if (v == 1) {
                used[1] = true;
                res[i] = 1;
                if (dfs(i + 1, size, n, res, used)) {
                    return true;
                }
                res[i] = 0;
                used[1] = false;
            } else if (i + v < size && res[i + v] == 0) {
                used[v] = true;
                res[i] = v;
                res[i + v] = v;
                if (dfs(i + 1, size, n, res, used)) {
                    return true;
                }
                res[i] = 0;
                res[i + v] = 0;
                used[v] = false;
            }
        }
        return false;
    }
}
