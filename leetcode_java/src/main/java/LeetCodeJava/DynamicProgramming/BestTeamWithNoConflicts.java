package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/best-team-with-no-conflicts/description/

import java.util.Arrays;

/**
 * 1626. Best Team With No Conflicts
 * Medium
 *
 * You are the manager of a basketball team. For the upcoming tournament, you want to choose the team with the highest overall score. The score of the team is the sum of scores of all the players in the team.
 *
 * However, the basketball team is not allowed to have conflicts. A conflict exists if a younger player has a strictly higher score than an older player. A conflict does not occur between players of the same age.
 *
 * Given two lists, scores and ages, where each scores[i] and ages[i] represents the score and age of the ith player, respectively, return the highest overall score of all possible basketball teams.
 *
 *
 * Example 1:
 *
 * Input: scores = [1,3,5,10,15], ages = [1,2,3,4,5]
 * Output: 34
 * Explanation: You can choose all the players.
 *
 * Example 2:
 *
 * Input: scores = [4,5,6,5], ages = [2,1,2,1]
 * Output: 16
 * Explanation: It is best to choose the last 3 players. Notice that you are allowed to choose multiple people of the same age.
 *
 * Example 3:
 *
 * Input: scores = [1,2,3,5], ages = [8,9,10,1]
 * Output: 6
 * Explanation: It is best to choose the first 3 players.
 *
 *
 * Constraints:
 *
 * 1 <= scores.length, ages.length <= 1000
 * scores.length == ages.length
 * 1 <= scores[i] <= 10^6
 * 1 <= ages[i] <= 1000
 *
 */
public class BestTeamWithNoConflicts {

    // V0
    // IDEA: SORT BY (SCORE, AGE) + LIS-STYLE DP
    //       after sorting by score (ties by age), a team is conflict-free iff the ages are
    //       non-decreasing in that order -> "max-sum non-decreasing subsequence of ages".
    //       dp[i] = best team score whose last (highest-score) player is i
    //             = score[i] + max dp[j] over j < i with age[j] <= age[i]
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int bestTeamScore(int[] scores, int[] ages) {
        int n = scores.length;

        // players[i] = {score, age}
        int[][] players = new int[n][2];
        for (int i = 0; i < n; i++) {
            players[i][0] = scores[i];
            players[i][1] = ages[i];
        }
        Arrays.sort(players, (a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(a[1], b[1]);
        });

        int[] dp = new int[n];
        int answer = 0;
        for (int i = 0; i < n; i++) {
            int bestBefore = 0;
            for (int j = 0; j < i; j++) {
                // j has a score <= i's, so j must not be older than i
                if (players[j][1] <= players[i][1]) {
                    bestBefore = Math.max(bestBefore, dp[j]);
                }
            }
            dp[i] = bestBefore + players[i][0];
            answer = Math.max(answer, dp[i]);
        }
        return answer;
    }
}
