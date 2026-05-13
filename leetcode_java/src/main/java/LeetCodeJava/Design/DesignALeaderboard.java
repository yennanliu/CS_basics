package LeetCodeJava.Design;

// https://leetcode.com/problems/design-a-leaderboard/
// https://leetcode.ca/all/1244.html

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *  1244. Design A Leaderboard
 * Design a Leaderboard class, which has 3 functions:
 *
 * addScore(playerId, score): Update the leaderboard by adding score to the given player's score. If there is no player with such id in the leaderboard, add him to the leaderboard with the given score.
 * top(K): Return the score sum of the top K players.
 * reset(playerId): Reset the score of the player with the given id to 0. It is guaranteed that the player was added to the leaderboard before calling this function.
 * Initially, the leaderboard is empty.
 *
 *
 *
 * Example 1:
 *
 * Input:
 * ["Leaderboard","addScore","addScore","addScore","addScore","addScore","top","reset","reset","addScore","top"]
 * [[],[1,73],[2,56],[3,39],[4,51],[5,4],[1],[1],[2],[2,51],[3]]
 * Output:
 * [null,null,null,null,null,null,73,null,null,null,141]
 *
 * Explanation:
 * Leaderboard leaderboard = new Leaderboard ();
 * leaderboard.addScore(1,73);   // leaderboard = [[1,73]];
 * leaderboard.addScore(2,56);   // leaderboard = [[1,73],[2,56]];
 * leaderboard.addScore(3,39);   // leaderboard = [[1,73],[2,56],[3,39]];
 * leaderboard.addScore(4,51);   // leaderboard = [[1,73],[2,56],[3,39],[4,51]];
 * leaderboard.addScore(5,4);    // leaderboard = [[1,73],[2,56],[3,39],[4,51],[5,4]];
 * leaderboard.top(1);           // returns 73;
 * leaderboard.reset(1);         // leaderboard = [[2,56],[3,39],[4,51],[5,4]];
 * leaderboard.reset(2);         // leaderboard = [[3,39],[4,51],[5,4]];
 * leaderboard.addScore(2,51);   // leaderboard = [[2,51],[3,39],[4,51],[5,4]];
 * leaderboard.top(3);           // returns 141 = 51 + 51 + 39;
 *
 *
 * Constraints:
 *
 * 1 <= playerId, K <= 10000
 * It's guaranteed that K is less than or equal to the current number of players.
 * 1 <= score <= 100
 * There will be at most 1000 function calls.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Wayfair
 * Problem Solution
 * 1244-Design-A-Leaderboard
 *
 */
public class DesignALeaderboard {

    // V0
//    class Leaderboard {
//        public Leaderboard() {
//        }
//
//        public void addScore(int playerId, int score) {
//        }
//
//        public int top(int K) {
//        }
//
//        public void reset(int playerId) {
//        }
//    }



    // V1
    // https://leetcode.ca/2019-04-27-1244-Design-A-Leaderboard/
    class Leaderboard_1 {
        private Map<Integer, Integer> d = new HashMap<>();
        private TreeMap<Integer, Integer> rank = new TreeMap<>((a, b) -> b - a);

        public Leaderboard_1() {
        }

        public void addScore(int playerId, int score) {
            d.merge(playerId, score, Integer::sum);
            int newScore = d.get(playerId);
            if (newScore != score) {
                rank.merge(newScore - score, -1, Integer::sum);
            }
            rank.merge(newScore, 1, Integer::sum);
        }

        public int top(int K) {
            int ans = 0;
            for (Map.Entry<Integer, Integer> e : rank.entrySet()) {
                int score = e.getKey(), cnt = e.getValue();
                cnt = Math.min(cnt, K);
                ans += score * cnt;
                K -= cnt;
                if (K == 0) {
                    break;
                }
            }
            return ans;
        }

        public void reset(int playerId) {
            int score = d.remove(playerId);
            if (rank.merge(score, -1, Integer::sum) == 0) {
                rank.remove(score);
            }
        }
    }




    // V2




}
