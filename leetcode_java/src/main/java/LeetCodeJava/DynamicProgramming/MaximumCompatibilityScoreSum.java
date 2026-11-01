package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-compatibility-score-sum/description/

import java.util.*;

/**
 * 1947. Maximum Compatibility Score Sum
 * Medium
 *
 * There is a survey that consists of n questions where each question's answer is either 0 (no) or 1 (yes).
 *
 * The survey was given to m students numbered from 0 to m - 1 and m mentors numbered from 0 to m - 1. The
 * answers of the students are represented by a 2D integer array students where students[i] is an integer
 * array that contains the answers of the ith student (0-indexed). The answers of the mentors are
 * represented by a 2D integer array mentors where mentors[j] is an integer array that contains the answers
 * of the jth mentor (0-indexed).
 *
 * Each student will be assigned to one mentor, and each mentor will have one student assigned to them. The
 * compatibility score of a student-mentor pair is the number of answers that are the same for both the
 * student and the mentor.
 *
 * You are tasked with finding the optimal student-mentor pairings to maximize the sum of the compatibility
 * scores.
 *
 * Given students and mentors, return the maximum compatibility score sum that can be achieved.
 *
 *
 * Example 1:
 *
 * Input: students = [[1,1,0],[1,0,1],[0,0,1]], mentors = [[1,0,0],[0,0,1],[1,1,0]]
 * Output: 8
 *
 * Example 2:
 *
 * Input: students = [[0,0],[0,0],[0,0]], mentors = [[1,1],[1,1],[1,1]]
 * Output: 0
 *
 *
 * Constraints:
 *
 * m == students.length == mentors.length
 * n == students[i].length == mentors[j].length
 * 1 <= m, n <= 8
 * students[i][k] is either 0 or 1.
 * mentors[j][k] is either 0 or 1.
 *
 */
public class MaximumCompatibilityScoreSum {

    // V0
    // IDEA: BITMASK DP OVER THE SET OF USED MENTORS (assignment problem, m <= 8)
    // score[i][j] = number of matching answers between student i and mentor j.
    // best[mask] = max total when the mentors in mask have been handed out; the number of
    //              mentors used equals the students served, so the next student is bitCount(mask).
    //   best[mask | (1 << j)] = max(..., best[mask] + score[bitCount(mask)][j]) for j not in mask
    // 2^m * m states and moves, instead of m! for plain permutation backtracking.
    /**
     * time = O(M^2 * N + 2^M * M)
     * space = O(2^M)
     */
    public int maxCompatibilitySum(int[][] students, int[][] mentors) {
        int m = students.length;
        int questions = students[0].length;

        int[][] score = new int[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < questions; k++) {
                    if (students[i][k] == mentors[j][k]) {
                        score[i][j]++;
                    }
                }
            }
        }

        // -1 = state not reached yet
        int[] best = new int[1 << m];
        Arrays.fill(best, -1);
        best[0] = 0;
        for (int mask = 0; mask < (1 << m); mask++) {
            if (best[mask] < 0) {
                continue;
            }
            int student = Integer.bitCount(mask);
            if (student == m) {
                continue;
            }
            for (int mentor = 0; mentor < m; mentor++) {
                // mentor already taken
                if ((mask & (1 << mentor)) != 0) {
                    continue;
                }
                int nextMask = mask | (1 << mentor);
                best[nextMask] = Math.max(best[nextMask], best[mask] + score[student][mentor]);
            }
        }
        return best[(1 << m) - 1];
    }
}
