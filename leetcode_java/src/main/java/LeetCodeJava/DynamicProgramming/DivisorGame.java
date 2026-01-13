package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/divisor-game/description/
/**
 * 1025. Divisor Game
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Alice and Bob take turns playing a game, with Alice starting first.
 *
 * Initially, there is a number n on the chalkboard. On each player's turn, that player makes a move consisting of:
 *
 * Choosing any integer x with 0 < x < n and n % x == 0.
 * Replacing the number n on the chalkboard with n - x.
 * Also, if a player cannot make a move, they lose the game.
 *
 * Return true if and only if Alice wins the game, assuming both players play optimally.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: true
 * Explanation: Alice chooses 1, and Bob has no more moves.
 * Example 2:
 *
 * Input: n = 3
 * Output: false
 * Explanation: Alice chooses 1, Bob chooses 1, and Alice has no more moves.
 *
 */
public class DivisorGame {

    // V0
//    public boolean divisorGame(int n) {
//
//    }

    // V1
    // https://leetcode.com/problems/divisor-game/solutions/274606/javacpython-return-n-2-0-by-lee215-y2v5/
    public boolean divisorGame_1(int n) {
        return n % 2 == 0;
    }

    // V2-1
    // https://leetcode.com/problems/divisor-game/solutions/979796/java-all-approach-easy-recursive-memoiza-icg2/
    public boolean divisorGame_2_1(int N) {
        return N % 2 == 0;
    }

    // V2-2
    // https://leetcode.com/problems/divisor-game/solutions/979796/java-all-approach-easy-recursive-memoiza-icg2/
    public boolean divisorGame_2_2(int N) {

        if (N == 1)
            return false;
        if (N == 2)
            return true;

        for (int i = 1; i <= N; i++) {
            if (N % i == 0)
                return !(divisorGame_2_2(N - i));
        }
        return false;
    }

    // V2-3
    // https://leetcode.com/problems/divisor-game/solutions/979796/java-all-approach-easy-recursive-memoiza-icg2/
    public boolean divisorGame_2_3(int N) {

        boolean[] cache = new boolean[N + 1];
        if (N == 1) {
            cache[1] = false;
            return false;
        }

        if (N == 2) {
            cache[2] = true;
            return true;
        }

        for (int i = 1; i * i <= N; i++) {
            if (N % i == 0) {
                cache[i] = !(divisorGame_2_3(N - i));
                return cache[i];
            }
        }
        return false;
    }



    // V3


}
