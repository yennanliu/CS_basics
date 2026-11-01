package LeetCodeJava.Array;

// https://leetcode.com/problems/number-of-student-replacements/description/

/**
 * 3616. Number of Student Replacements
 * Medium
 *
 * You are given an integer array ranks where ranks[i] represents the rank of the
 * ith student arriving in order. A lower number indicates a better rank.
 *
 * Initially, the first student is selected by default.
 *
 * A replacement occurs when a student with a strictly better rank arrives and
 * replaces the current selection.
 *
 * Return the total number of replacements made.
 *
 *
 * Example 1:
 *
 * Input: ranks = [4,1,2]
 * Output: 1
 *
 * Example 2:
 *
 * Input: ranks = [2,2,3]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= ranks.length <= 10^5
 * 1 <= ranks[i] <= 10^5
 *
 */
public class NumberOfStudentReplacements {

    // V0
    // IDEA: COUNT STRICT PREFIX MINIMA
    //   the selected student is always the best rank seen so far, so a replacement
    //   happens exactly when a new strict record low arrives. ties change nothing.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int totalReplacements(int[] ranks) {
        int best = ranks[0];
        int replacements = 0;
        for (int i = 1; i < ranks.length; i++) {
            if (ranks[i] < best) {
                best = ranks[i];
                replacements++;
            }
        }
        return replacements;
    }
}
