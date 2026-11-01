package LeetCodeJava.Array;

// https://leetcode.com/problems/most-visited-sector-in-a-circular-track/description/

import java.util.*;

/**
 * 1560. Most Visited Sector in a Circular Track
 * Easy
 *
 * Given an integer n and an integer array rounds. We have a circular track which consists
 * of n sectors labeled from 1 to n. A marathon will be held on this track, the marathon
 * consists of m rounds. The ith round starts at sector rounds[i - 1] and ends at sector
 * rounds[i]. For example, round 1 starts at sector rounds[0] and ends at sector rounds[1]
 *
 * Return an array of the most visited sectors sorted in ascending order.
 *
 * Notice that you circulate the track in ascending order of sector numbers in the
 * counter-clockwise direction (See the first example).
 *
 *
 * Example 1:
 *
 * Input: n = 4, rounds = [1,3,1,2]
 * Output: [1,2]
 * Explanation: 1 --> 2 --> 3 (end of round 1) --> 4 --> 1 (end of round 2) --> 2 (end of
 * round 3 and the marathon). Sectors 1 and 2 are visited twice, 3 and 4 only once.
 *
 * Example 2:
 *
 * Input: n = 2, rounds = [2,1,2,1,2,1,2,1,2]
 * Output: [2]
 *
 * Example 3:
 *
 * Input: n = 7, rounds = [1,3,5,7]
 * Output: [1,2,3,4,5,6,7]
 *
 *
 * Constraints:
 *
 * 2 <= n <= 100
 * 1 <= m <= 100
 * rounds.length == m + 1
 * 1 <= rounds[i] <= n
 * rounds[i] != rounds[i + 1] for 0 <= i < m
 *
 */
public class MostVisitedSectorInACircularTrack {

    // V0
    // IDEA: MATH (only the first and the last sector matter) — full laps visit every sector
    //       equally, so the winners are the sectors on the partial arc start -> end:
    //       start <= end: [start .. end];  start > end: [1 .. end] + [start .. n]
    /**
     * time = O(N)
     * space = O(N)
     */
    public List<Integer> mostVisited(int n, int[] rounds) {
        int start = rounds[0];
        int end = rounds[rounds.length - 1];
        List<Integer> result = new ArrayList<>();

        if (start <= end) {
            for (int sector = start; sector <= end; sector++) {
                result.add(sector);
            }
        } else {
            // the arc wraps past sector n; list the low part first to stay ascending
            for (int sector = 1; sector <= end; sector++) {
                result.add(sector);
            }
            for (int sector = start; sector <= n; sector++) {
                result.add(sector);
            }
        }
        return result;
    }
}
