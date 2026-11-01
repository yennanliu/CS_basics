package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/the-earliest-and-latest-rounds-where-players-compete/description/

/**
 * 1900. The Earliest and Latest Rounds Where Players Compete
 * Hard
 *
 * There is a tournament where n players are participating. The players are standing in a single
 * row and are numbered from 1 to n based on their initial standing position (player 1 is the first
 * player in the row, player 2 is the second player in the row, etc.).
 *
 * The tournament consists of multiple rounds (starting from round number 1). In each round, the ith
 * player from the front of the row competes against the ith player from the end of the row, and the
 * winner advances to the next round. When the number of players is odd for the current round, the
 * player in the middle automatically advances to the next round.
 *
 * For example, if the row consists of players 1, 2, 4, 6, 7
 * Player 1 competes against player 7.
 * Player 2 competes against player 6.
 * Player 4 automatically advances to the next round.
 *
 * After each round is over, the winners are lined back up in the row based on the original ordering
 * assigned to them initially (ascending order).
 *
 * The players numbered firstPlayer and secondPlayer are the best in the tournament. They can win
 * against any other player before they compete against each other. If any two other players compete
 * against each other, either of them might win, and thus you may choose the outcome of this round.
 *
 * Given the integers n, firstPlayer, and secondPlayer, return an integer array containing two
 * values, the earliest possible round number and the latest possible round number in which these
 * two players will compete against each other, respectively.
 *
 *
 * Example 1:
 *
 * Input: n = 11, firstPlayer = 2, secondPlayer = 4
 * Output: [3,4]
 *
 * Example 2:
 *
 * Input: n = 5, firstPlayer = 1, secondPlayer = 5
 * Output: [1,1]
 * Explanation: The players numbered 1 and 5 compete in the first round.
 * There is no way to make them compete in any other round.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 28
 * 1 <= firstPlayer < secondPlayer <= n
 *
 */
public class TheEarliestAndLatestRoundsWherePlayersCompete {

    // V0
    // IDEA: MEMOIZED DP ON (PLAYERS LEFT, POSITION OF FIRST, POSITION OF SECOND)
    //
    //   only the two heroes' POSITIONS in the current row matter, not who the others are.
    //   - f + s == n + 1 -> they face each other this round: (1, 1).
    //   - by symmetry, mirror the row so that f is the closer one to the left end.
    //   - next round has half = (n + 1) / 2 players. enumerate how many players
    //     LEFT of f survive (i in [0, f-1]) and how many BETWEEN f and s survive (j);
    //     each choice gives the heroes new positions in the smaller row.
    //   - if s is in the right half, the players between f and s's mirror s2 are free,
    //     but the ones between s2 and s are forced survivors (they meet each other's
    //     mirror partners on the right) -> add that fixed "mid" count.
    //   answer for a state = (min earliest + 1, max latest + 1) over all choices.
    /**
     * time = O(N^5) in the worst case (N^3 states x N^2 choices), N <= 28
     * space = O(N^3)
     */
    public int[] earliestAndLatest(int n, int firstPlayer, int secondPlayer) {
        // memo[n][f][s] = {earliest, latest}, null when not computed yet
        int[][][][] memo = new int[n + 1][n + 1][n + 1][];
        return solve(n, firstPlayer, secondPlayer, memo);
    }

    /** Returns {earliest, latest} round for heroes at positions f < s in a row of n players. */
    private int[] solve(int n, int f, int s, int[][][][] memo) {
        // they are each other's opponent this round
        if (f + s == n + 1) {
            return new int[] {1, 1};
        }
        // mirror the row so that f + s < n + 1 (f is nearer the left end than s is to the right)
        if (f + s > n + 1) {
            int mirroredF = n + 1 - s;
            int mirroredS = n + 1 - f;
            f = mirroredF;
            s = mirroredS;
        }
        if (memo[n][f][s] != null) {
            return memo[n][f][s];
        }

        int half = (n + 1) / 2;
        int earliest = Integer.MAX_VALUE;
        int latest = Integer.MIN_VALUE;

        if (s <= half) {
            // both heroes are in the left half: everyone in front of them can win or lose freely
            for (int leftSurvivors = 0; leftSurvivors < f; leftSurvivors++) {
                for (int middleSurvivors = 0; middleSurvivors < s - f; middleSurvivors++) {
                    int newF = leftSurvivors + 1;
                    int newS = leftSurvivors + middleSurvivors + 2;
                    int[] next = solve(half, newF, newS, memo);
                    earliest = Math.min(earliest, next[0]);
                    latest = Math.max(latest, next[1]);
                }
            }
        } else {
            // s is in the right half; s2 is the position of s's opponent on the left
            int s2 = n + 1 - s;
            // players strictly between s2 and s (excluding the pairs among themselves) survive for sure
            int forcedMiddle = (n - 2 * s2 + 1) / 2;
            for (int leftSurvivors = 0; leftSurvivors < f; leftSurvivors++) {
                for (int middleSurvivors = 0; middleSurvivors < s2 - f; middleSurvivors++) {
                    int newF = leftSurvivors + 1;
                    int newS = leftSurvivors + middleSurvivors + forcedMiddle + 2;
                    int[] next = solve(half, newF, newS, memo);
                    earliest = Math.min(earliest, next[0]);
                    latest = Math.max(latest, next[1]);
                }
            }
        }

        // this round is one more round before they meet
        memo[n][f][s] = new int[] {earliest + 1, latest + 1};
        return memo[n][f][s];
    }
}
