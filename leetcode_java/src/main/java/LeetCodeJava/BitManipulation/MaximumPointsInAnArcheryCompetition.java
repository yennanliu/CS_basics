package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/maximum-points-in-an-archery-competition/

/**
 *  2212. Maximum Points in an Archery Competition
 *  Medium
 *
 *  Alice and Bob are opponents in an archery competition. Alice first shoots
 *  numArrows arrows and then Bob shoots numArrows arrows. The target has integer
 *  scoring sections ranging from 0 to 11 inclusive. For each section with score k,
 *  say Alice and Bob shot ak and bk arrows on that section. If ak >= bk then Alice
 *  takes k points; if ak < bk then Bob takes k points. If ak == bk == 0 nobody
 *  takes k points.
 *
 *  You are given the integer numArrows and an integer array aliceArrows of size 12.
 *  Bob wants to maximize the total number of points he can obtain. Return the array
 *  bobArrows, whose values must sum to numArrows. If several answers achieve the
 *  maximum, return any of them.
 *
 *  Example 1:
 *    Input: numArrows = 9, aliceArrows = [1,1,0,1,0,0,2,1,0,1,2,0]
 *    Output: [0,0,0,0,1,1,0,0,1,2,3,1]
 *    Explanation: Bob earns 4 + 5 + 8 + 9 + 10 + 11 = 47 points.
 *
 *  Example 2:
 *    Input: numArrows = 3, aliceArrows = [0,0,1,0,0,0,0,0,0,0,0,2]
 *    Output: [0,0,0,0,0,0,0,0,1,1,1,0]
 *    Explanation: Bob earns 8 + 9 + 10 = 27 points.
 *
 *  Constraints:
 *    1 <= numArrows <= 10^5
 *    aliceArrows.length == bobArrows.length == 12
 *    0 <= aliceArrows[i] <= numArrows
 *    sum(aliceArrows[i]) == numArrows
 */
public class MaximumPointsInAnArcheryCompetition {

    // V0
    // IDEA: ONLY 12 SECTIONS -> ENUMERATE ALL 2^12 SUBSETS BOB COULD WIN
    //
    //  to win section k Bob must shoot strictly more arrows than Alice, and the
    //  cheapest way is exactly aliceArrows[k] + 1. so a candidate answer is just a
    //  SUBSET of the sections Bob claims, and both its cost and its score are fixed.
    //
    //  4096 subsets is nothing -> try them all, keep the affordable ones, track the best.
    //
    //  NOTE: leftover arrows must still be shot -> dump them into section 0 (worth
    //        nothing), keeping the sum exactly numArrows.
    /**
     * time = O(2^12 * 12)
     * space = O(1)
     */
    public int[] maximumBobPoints(int numArrows, int[] aliceArrows) {
        int bestScore = -1;
        int[] best = new int[12];

        for (int mask = 0; mask < (1 << 12); mask++) {
            int cost = 0;
            int score = 0;
            for (int k = 0; k < 12; k++) {
                if (((mask >> k) & 1) == 1) {
                    cost += aliceArrows[k] + 1;
                    score += k;
                }
            }
            if (cost > numArrows || score <= bestScore) {
                continue;
            }
            bestScore = score;
            int[] bob = new int[12];
            for (int k = 0; k < 12; k++) {
                if (((mask >> k) & 1) == 1) {
                    bob[k] = aliceArrows[k] + 1;
                }
            }
            bob[0] += numArrows - cost;      // spend the leftovers harmlessly
            best = bob;
        }
        return best;
    }
}
