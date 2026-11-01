package LeetCodeJava.Array;

// https://leetcode.com/problems/find-the-first-player-to-win-k-games-in-a-row/description/

/**
 * 3175. Find The First Player to win K Games in a Row
 * Medium
 *
 * A competition consists of n players numbered from 0 to n - 1.
 *
 * You are given an integer array skills of size n and a positive integer k, where skills[i] is the skill level of player i. All integers in skills are unique.
 *
 * All players are standing in a queue in order from player 0 to player n - 1.
 *
 * The competition process is as follows:
 *
 * The first two players in the queue play a game, and the player with the higher skill level wins.
 * After the game, the winner stays at the beginning of the queue, and the loser goes to the end of it.
 *
 * The winner of the competition is the first player who wins k games in a row.
 *
 * Return the initial index of the winning player.
 *
 *
 * Example 1:
 *
 * Input: skills = [4,2,6,3,9], k = 2
 * Output: 2
 * Explanation:
 * Initially, the queue of players is [0,1,2,3,4]. The following process happens:
 * Players 0 and 1 play a game, since the skill of player 0 is higher than that of player 1, player 0 wins. The resulting queue is [0,2,3,4,1].
 * Players 0 and 2 play a game, since the skill of player 2 is higher than that of player 0, player 2 wins. The resulting queue is [2,3,4,1,0].
 * Players 2 and 3 play a game, since the skill of player 2 is higher than that of player 3, player 2 wins. The resulting queue is [2,4,1,0,3].
 * Player 2 won k = 2 games in a row, so the winner is player 2.
 *
 * Example 2:
 *
 * Input: skills = [2,5,4], k = 3
 * Output: 1
 * Explanation:
 * Initially, the queue of players is [0,1,2]. The following process happens:
 * Players 0 and 1 play a game, since the skill of player 1 is higher than that of player 0, player 1 wins. The resulting queue is [1,2,0].
 * Players 1 and 2 play a game, since the skill of player 1 is higher than that of player 2, player 1 wins. The resulting queue is [1,0,2].
 * Players 1 and 0 play a game, since the skill of player 1 is higher than that of player 0, player 1 wins. The resulting queue is [1,2,0].
 * Player 1 won k = 3 games in a row, so the winner is player 1.
 *
 *
 * Constraints:
 *
 * n == skills.length
 * 2 <= n <= 10^5
 * 1 <= k <= 10^9
 * 1 <= skills[i] <= 1e6
 * All integers in skills are unique.
 *
 */
public class FindTheFirstPlayerToWinKGamesInARow {

    // V0
    // IDEA: ONE SWEEP IS ENOUGH - AFTER IT, THE STRONGEST PLAYER NEVER LOSES
    // a single pass plays every "new challenger" game in queue order. track the
    // current champion and its streak; the first streak to reach k wins. if none
    // does, the strongest player now holds the front and wins forever - which
    // also covers k larger than n.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int findWinningPlayer(int[] skills, int k) {
        int champion = 0;
        int streak = 0;

        for (int challenger = 1; challenger < skills.length; challenger++) {
            if (skills[challenger] > skills[champion]) {
                // the challenger takes over with its first win
                champion = challenger;
                streak = 1;
            } else {
                streak++;
            }
            if (streak == k) {
                return champion;
            }
        }
        // the champion after a full sweep is the maximum skill
        return champion;
    }
}
