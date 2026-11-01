package LeetCodeJava.Array;

// https://leetcode.com/problems/find-the-winner-of-an-array-game/description/

/**
 * 1535. Find the Winner of an Array Game
 * Medium
 *
 * Given an integer array arr of distinct integers and an integer k.
 *
 * A game will be played between the first two elements of the array (i.e. arr[0] and
 * arr[1]). In each round of the game, we compare arr[0] with arr[1], the larger integer
 * wins and remains at position 0, and the smaller integer moves to the end of the array.
 * The game ends when an integer wins k consecutive rounds.
 *
 * Return the integer which will win the game.
 *
 * It is guaranteed that there will be a winner of the game.
 *
 *
 * Example 1:
 *
 * Input: arr = [2,1,3,5,4,6,7], k = 2
 * Output: 5
 * Explanation: 4 rounds will be played and 5 is the winner because it wins 2 consecutive
 * games.
 *
 * Example 2:
 *
 * Input: arr = [3,2,1], k = 10
 * Output: 3
 * Explanation: 3 will win the first 10 rounds consecutively.
 *
 *
 * Constraints:
 *
 * 2 <= arr.length <= 10^5
 * 1 <= arr[i] <= 10^6
 * arr contains distinct integers.
 * 1 <= k <= 10^9
 *
 */
public class FindTheWinnerOfAnArrayGame {

    // V0
    // IDEA: ONE LINEAR SWEEP (no queue simulation) — losers go to the back, so during the
    //       first pass the champion always faces the next un-played element. If nobody
    //       reaches k wins in that pass, the array maximum has beaten everyone and wins
    //       forever, so it is the answer (k can be 10^9, it never matters).
    /**
     * time = O(N)
     * space = O(1)
     */
    public int getWinner(int[] arr, int k) {
        int champion = arr[0];
        int winStreak = 0;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > champion) {
                // a new champion has just won its first round
                champion = arr[i];
                winStreak = 1;
            } else {
                winStreak++;
            }
            if (winStreak == k) {
                break;
            }
        }
        return champion;
    }
}
