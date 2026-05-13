package LeetCodeJava.Design;

// https://leetcode.com/problems/design-a-leaderboard/
// https://leetcode.ca/all/1244.html

import java.util.Collections;
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


    // V0-1
    // IDEA: TREEMAP (GPT)
    /**  CORE IDEA:
     *
     * The standard solution is:
     *
     * playerId -> score
     * score -> frequency/count
     *
     * using:
     *
     * HashMap for player scores
     * TreeMap (reverse order) for score frequencies
     *
     * Fixed version with full comments:
     *
     *
     */
    class Leaderboard_0_1{

        /** NOTE !!!
         *
         *   map: { player_id: score }
         */
        // playerId -> current score
        Map<Integer, Integer> playerToScore;


        /** NOTE !!!
         *
         *   TreeMap: { score: freq }
         */
        // score -> how many players have this score
        //
        // Reverse order:
        // highest score comes first
        TreeMap<Integer, Integer> scoreCount;


        public Leaderboard_0_1() {

            playerToScore = new HashMap<>();

            /** NOTE !!!
             *
             *   `Collections.reverseOrder()`
             *
             *   -> high score comes first
             */
            scoreCount = new TreeMap<>(Collections.reverseOrder());
        }


        public void addScore(int playerId, int score) {

            // ------------------------------------------------
            // CASE 1:
            // New player
            // ------------------------------------------------
            if (!playerToScore.containsKey(playerId)) {

                playerToScore.put(playerId, score);

                scoreCount.put(
                        score,
                        scoreCount.getOrDefault(score, 0) + 1
                );

                return;
            }


            // ------------------------------------------------
            // CASE 2:
            // Existing player
            // Need to:
            // 1. remove old score frequency
            // 2. add new score frequency
            // ------------------------------------------------

            int oldScore = playerToScore.get(playerId);

            int newScore = oldScore + score;


            // Remove old score count
            int oldFreq = scoreCount.get(oldScore);

            if (oldFreq == 1) {
                scoreCount.remove(oldScore);
            } else {
                scoreCount.put(oldScore, oldFreq - 1);
            }


            // Update player's new score
            playerToScore.put(playerId, newScore);


            // Add new score count
            scoreCount.put(
                    newScore,
                    scoreCount.getOrDefault(newScore, 0) + 1
            );
        }


        public int top(int K) {

            int sum = 0;


            // Traverse scores from HIGH -> LOW
            for (int score : scoreCount.keySet()) {

                int freq = scoreCount.get(score);

                // Multiple players may share same score
                for (int i = 0; i < freq && K > 0; i++) {
                    sum += score;
                    K--;
                }


                // Already got top K players
                if (K == 0) {
                    break;
                }
            }

            return sum;
        }


        public void reset(int playerId) {

            // Player's current score
            int score = playerToScore.get(playerId);


            // Remove score frequency
            int freq = scoreCount.get(score);

            if (freq == 1) {
                scoreCount.remove(score);
            } else {
                scoreCount.put(score, freq - 1);
            }


            // Remove player completely
            playerToScore.remove(playerId);
        }

    }



    // V0-2
    // IDEA: TREE MAP (GEMINI)
    class Leaderboard_0_2 {
        // 1. Map player ID to their current total score
        private Map<Integer, Integer> playerScores;
        // 2. Sorted Map of Score -> Count of players with that score (Descending)
        private TreeMap<Integer, Integer> scoreCounts;

        public Leaderboard_0_2() {
            playerScores = new HashMap<>();
            // Use reverseOrder so the highest scores are at the top of the map
            scoreCounts = new TreeMap<>(Collections.reverseOrder());
        }

        public void addScore(int playerId, int score) {
            if (!playerScores.containsKey(playerId)) {
                // New Player
                playerScores.put(playerId, score);
                scoreCounts.put(score, scoreCounts.getOrDefault(score, 0) + 1);
            } else {
                // Existing Player: Must update both maps
                int oldScore = playerScores.get(playerId);
                removeScoreCount(oldScore);

                int newScore = oldScore + score;
                playerScores.put(playerId, newScore);
                scoreCounts.put(newScore, scoreCounts.getOrDefault(newScore, 0) + 1);
            }
        }

        public int top(int K) {
            int totalSum = 0;
            int countFound = 0;

            // Iterate through the scores from highest to lowest
            for (Map.Entry<Integer, Integer> entry : scoreCounts.entrySet()) {
                int score = entry.getKey();
                int numPlayersWithScore = entry.getValue();

                // We need to take at most 'numPlayersWithScore' or the remaining 'K'
                int playersToTake = Math.min(numPlayersWithScore, K - countFound);
                totalSum += score * playersToTake;
                countFound += playersToTake;

                if (countFound == K) break;
            }

            return totalSum;
        }

        public void reset(int playerId) {
            int oldScore = playerScores.get(playerId);
            removeScoreCount(oldScore);
            playerScores.remove(playerId);
        }

        // Helper to decrement score count and clean up if it reaches zero
        private void removeScoreCount(int score) {
            int count = scoreCounts.get(score);
            if (count == 1) {
                scoreCounts.remove(score);
            } else {
                scoreCounts.put(score, count - 1);
            }
        }
    }



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
