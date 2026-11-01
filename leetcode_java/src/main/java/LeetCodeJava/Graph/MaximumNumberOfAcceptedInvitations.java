package LeetCodeJava.Graph;

// https://leetcode.com/problems/maximum-number-of-accepted-invitations/description/

import java.util.*;

/**
 * 1820. Maximum Number of Accepted Invitations
 * Medium
 *
 * There are m boys and n girls in a class attending an upcoming party.
 *
 * You are given an m x n integer matrix grid, where grid[i][j] equals 0 or 1. If grid[i][j] == 1, then that
 * means the ith boy can invite the jth girl to the party. A boy can invite at most one girl, and a girl can
 * accept at most one invitation from a boy.
 *
 * Return the maximum possible number of accepted invitations.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,1,1],
 *                [1,0,1],
 *                [0,0,1]]
 * Output: 3
 *
 * Example 2:
 *
 * Input: grid = [[1,0,1,0],
 *                [1,0,0,0],
 *                [0,0,1,0],
 *                [1,1,1,0]]
 * Output: 3
 *
 *
 * Constraints:
 *
 * grid.length == m
 * grid[i].length == n
 * 1 <= m, n <= 200
 * grid[i][j] is either 0 or 1.
 *
 */
public class MaximumNumberOfAcceptedInvitations {

    // V0
    // IDEA: MAXIMUM BIPARTITE MATCHING (Kuhn's augmenting path)
    // boys on the left, girls on the right, an edge where grid[i][j] == 1; we want the
    // largest matching. For each boy, look for an augmenting path: take a girl he can invite
    // who is free, or whose current partner can be re-seated elsewhere (recursive try).
    // Every successful search grows the matching by exactly one.
    // NOTE: `visited` is reset per boy -- it guards one search, not the whole run.
    /**
     * time = O(M * M * N)
     * space = O(M + N)
     */
    public int maximumInvitations(int[][] grid) {
        int boys = grid.length;
        int girls = grid[0].length;
        // partnerOf[j] = boy currently matched with girl j, -1 = free
        int[] partnerOf = new int[girls];
        Arrays.fill(partnerOf, -1);

        int matched = 0;
        for (int boy = 0; boy < boys; boy++) {
            boolean[] visited = new boolean[girls];
            if (tryMatch(grid, boy, visited, partnerOf)) {
                matched++;
            }
        }
        return matched;
    }

    /** Tries to give `boy` a girl, re-seating earlier boys along an augmenting path. */
    private boolean tryMatch(int[][] grid, int boy, boolean[] visited, int[] partnerOf) {
        for (int girl = 0; girl < partnerOf.length; girl++) {
            if (grid[boy][girl] == 0 || visited[girl]) {
                continue;
            }
            visited[girl] = true;
            // girl is free, or her partner can move to another girl
            if (partnerOf[girl] == -1 || tryMatch(grid, partnerOf[girl], visited, partnerOf)) {
                partnerOf[girl] = boy;
                return true;
            }
        }
        return false;
    }
}
