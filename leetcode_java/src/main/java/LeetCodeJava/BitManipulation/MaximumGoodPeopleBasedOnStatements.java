package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/maximum-good-people-based-on-statements/

/**
 *  2151. Maximum Good People Based on Statements
 *  Hard
 *
 *  There are two types of persons:
 *    The good person: the person who always tells the truth.
 *    The bad person: the person who might tell the truth and might lie.
 *
 *  You are given a 0-indexed 2D integer array statements of size n x n that
 *  represents the statements made by n people about each other. More
 *  specifically, statements[i][j] could be one of the following:
 *    0 - person i says person j is a bad person.
 *    1 - person i says person j is a good person.
 *    2 - person i makes no statement about person j.
 *
 *  Additionally, no person ever makes a statement about themselves, i.e.
 *  statements[i][i] = 2 for all 0 <= i < n.
 *
 *  Return the maximum number of people who can be good based on the statements
 *  made by the n people.
 *
 *  Example 1:
 *    Input: statements = [[2,1,2],[1,2,2],[2,0,2]]
 *    Output: 2
 *    Explanation: at most 2 persons can be good (person 0 and person 1, with
 *                 person 2 being bad and lying).
 *
 *  Example 2:
 *    Input: statements = [[2,0],[0,2]]
 *    Output: 1
 *    Explanation: each accuses the other, so at most one of them is good.
 *
 *  Constraints:
 *    n == statements.length == statements[i].length
 *    2 <= n <= 15
 *    statements[i][j] is either 0, 1, or 2.
 *    statements[i][i] == 2
 */
public class MaximumGoodPeopleBasedOnStatements {

    // V0
    // IDEA: ENUMERATE EVERY GOOD/BAD ASSIGNMENT (n <= 15 -> 2^15 masks)
    //       guess a bitmask where bit i set means "person i is GOOD". the guess is
    //       consistent iff every GOOD person's statements match the guess:
    //           statements[i][j] == 1 -> bit j must be SET
    //           statements[i][j] == 0 -> bit j must be CLEAR
    //           statements[i][j] == 2 -> no constraint
    //       BAD people are unconstrained (they may say anything), so their rows are
    //       skipped entirely. among the consistent masks, keep the largest popcount.
    /**
     * time = O(2^N * N^2)
     * space = O(1)
     */
    public int maximumGood(int[][] statements) {
        int n = statements.length;
        int res = 0;
        for (int mask = 0; mask < (1 << n); mask++) {
            boolean ok = true;
            for (int i = 0; i < n && ok; i++) {
                if (((mask >> i) & 1) == 0) {
                    continue;                  // bad people can lie freely
                }
                for (int j = 0; j < n; j++) {
                    int s = statements[i][j];
                    if (s == 2) {
                        continue;
                    }
                    if (s != ((mask >> j) & 1)) {
                        ok = false;
                        break;
                    }
                }
            }
            if (ok) {
                res = Math.max(res, Integer.bitCount(mask));
            }
        }
        return res;
    }
}
