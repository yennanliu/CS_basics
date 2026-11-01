package LeetCodeJava.Greedy;

// https://leetcode.com/problems/sum-game/description/

/**
 * 1927. Sum Game
 * Medium
 *
 * Alice and Bob take turns playing a game, with Alice starting first.
 *
 * You are given a string num of even length consisting of digits and '?'
 * characters. On each turn, a player will do the following if there is still at
 * least one '?' in num:
 *
 * 1. Choose an index i where num[i] == '?'.
 * 2. Replace num[i] with any digit between '0' and '9'.
 *
 * The game ends when there are no more '?' characters in num.
 *
 * For Bob to win, the sum of the digits in the first half of num must be equal to
 * the sum of the digits in the second half. For Alice to win, the sums must not
 * be equal.
 *
 * For example, if the game ended with num = "243801", then Bob wins because
 * 2+4+3 = 8+0+1. If the game ended with num = "243803", then Alice wins because
 * 2+4+3 != 8+0+3.
 *
 * Assuming Alice and Bob play optimally, return true if Alice will win and false
 * if Bob will win.
 *
 *
 * Example 1:
 *
 * Input: num = "5023"
 * Output: false
 * Explanation: There are no moves to be made.
 * The sum of the first half is equal to the sum of the second half: 5 + 0 = 2 + 3.
 *
 * Example 2:
 *
 * Input: num = "25??"
 * Output: true
 * Explanation: Alice can replace one of the '?'s with '9' and it will be
 * impossible for Bob to make the sums equal.
 *
 * Example 3:
 *
 * Input: num = "?3295???"
 * Output: false
 * Explanation: It can be proven that Bob will always win.
 *
 *
 * Constraints:
 *
 * 2 <= num.length <= 10^5
 * num.length is even.
 * num consists of only digits and '?'.
 *
 */
public class SumGame {

    // V0
    // IDEA: GAME THEORY / CASE ANALYSIS (closed form, no simulation)
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean sumGame(String num) {
        int n = num.length();
        int half = n / 2;
        int leftSum = 0;
        int rightSum = 0;
        int leftQuestions = 0;
        int rightQuestions = 0;

        for (int i = 0; i < n; i++) {
            char ch = num.charAt(i);
            boolean inLeftHalf = i < half;
            if (ch == '?') {
                if (inLeftHalf) {
                    leftQuestions++;
                } else {
                    rightQuestions++;
                }
            } else {
                if (inLeftHalf) {
                    leftSum += ch - '0';
                } else {
                    rightSum += ch - '0';
                }
            }
        }

        // an odd number of '?' means Alice moves last and can always break equality
        if ((leftQuestions + rightQuestions) % 2 == 1) {
            return true;
        }

        // with an even count Bob mirrors Alice (d answered by 9 - d in the other half),
        // so each pair of surplus '?' is worth exactly 9 to the half that owns them.
        // Bob wins only when leftSum - rightSum == 9 * (rightQuestions - leftQuestions) / 2;
        // compare doubled values to stay in integers
        return 2 * (leftSum - rightSum) != 9 * (rightQuestions - leftQuestions);
    }
}
