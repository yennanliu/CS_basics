package LeetCodeJava.HashTable;

// https://leetcode.com/problems/find-players-with-zero-or-one-losses/description/

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 2225. Find Players With Zero or One Losses
 * Medium
 *
 * You are given an integer array matches where matches[i] = [winneri, loseri] indicates that the player winneri defeated player loseri in a match.
 *
 * Return a list answer of size 2 where:
 *
 * answer[0] is a list of all players that have not lost any matches.
 * answer[1] is a list of all players that have lost exactly one match.
 *
 * The values in the two lists should be returned in increasing order.
 *
 * Note:
 *
 * You should only consider the players that have played at least one match.
 * The testcases will be generated such that no two matches will have the same outcome.
 *
 *
 * Example 1:
 *
 * Input: matches = [[1,3],[2,3],[3,6],[5,6],[5,7],[4,5],[4,8],[4,9],[10,4],[10,9]]
 * Output: [[1,2,10],[4,5,7,8]]
 * Explanation:
 * Players 1, 2, and 10 have not lost any matches.
 * Players 4, 5, 7, and 8 each have lost one match.
 * Players 3, 6, and 9 each have lost two matches.
 * Thus, answer[0] = [1,2,10] and answer[1] = [4,5,7,8].
 *
 * Example 2:
 *
 * Input: matches = [[2,3],[1,3],[5,4],[6,4]]
 * Output: [[1,2,5,6],[]]
 * Explanation:
 * Players 1, 2, 5, and 6 have not lost any matches.
 * Players 3 and 4 each have lost two matches.
 * Thus, answer[0] = [1,2,5,6] and answer[1] = [].
 *
 *
 * Constraints:
 *
 * 1 <= matches.length <= 10^5
 * matches[i].length == 2
 * 1 <= winneri, loseri <= 10^5
 * winneri != loseri
 * All matches[i] are unique.
 *
 */
public class FindPlayersWithZeroOrOneLosses {

    // V0
    // IDEA: HASH TABLE OF LOSS COUNTS (TreeMap, so players come out sorted)
    //   a winner is registered with 0 losses if unseen, so players who never lost
    //   still appear (they did play). then bucket loss == 0 and loss == 1.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public List<List<Integer>> findWinners(int[][] matches) {
        Map<Integer, Integer> losses = new TreeMap<>();
        for (int[] match : matches) {
            int winner = match[0];
            int loser = match[1];
            if (!losses.containsKey(winner)) {
                losses.put(winner, 0);
            }
            losses.put(loser, losses.getOrDefault(loser, 0) + 1);
        }

        List<Integer> neverLost = new ArrayList<>();
        List<Integer> lostOnce = new ArrayList<>();
        // TreeMap iterates keys in ascending order, which the answer requires
        for (Map.Entry<Integer, Integer> entry : losses.entrySet()) {
            if (entry.getValue() == 0) {
                neverLost.add(entry.getKey());
            } else if (entry.getValue() == 1) {
                lostOnce.add(entry.getKey());
            }
        }

        List<List<Integer>> answer = new ArrayList<>();
        answer.add(neverLost);
        answer.add(lostOnce);
        return answer;
    }
}
