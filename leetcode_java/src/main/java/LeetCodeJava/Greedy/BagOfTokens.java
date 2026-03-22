package LeetCodeJava.Greedy;

// https://leetcode.com/problems/bag-of-tokens/description/

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/**
 *  948. Bag of Tokens
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You start with an initial power of power, an initial score of 0, and a bag of tokens given as an integer array tokens, where each tokens[i] denotes the value of tokeni.
 *
 * Your goal is to maximize the total score by strategically playing these tokens. In one move, you can play an unplayed token in one of the two ways (but not both for the same token):
 *
 * Face-up: If your current power is at least tokens[i], you may play tokeni, losing tokens[i] power and gaining 1 score.
 * Face-down: If your current score is at least 1, you may play tokeni, gaining tokens[i] power and losing 1 score.
 * Return the maximum possible score you can achieve after playing any number of tokens.
 *
 *
 *
 * Example 1:
 *
 * Input: tokens = [100], power = 50
 *
 * Output: 0
 *
 * Explanation: Since your score is 0 initially, you cannot play the token face-down. You also cannot play it face-up since your power (50) is less than tokens[0] (100).
 *
 * Example 2:
 *
 * Input: tokens = [200,100], power = 150
 *
 * Output: 1
 *
 * Explanation: Play token1 (100) face-up, reducing your power to 50 and increasing your score to 1.
 *
 * There is no need to play token0, since you cannot play it face-up to add to your score. The maximum score achievable is 1.
 *
 * Example 3:
 *
 * Input: tokens = [100,200,300,400], power = 200
 *
 * Output: 2
 *
 * Explanation: Play the tokens in this order to get a score of 2:
 *
 * Play token0 (100) face-up, reducing power to 100 and increasing score to 1.
 * Play token3 (400) face-down, increasing power to 500 and reducing score to 0.
 * Play token1 (200) face-up, reducing power to 300 and increasing score to 1.
 * Play token2 (300) face-up, reducing power to 0 and increasing score to 2.
 * The maximum score achievable is 2.
 *
 *
 *
 * Constraints:
 *
 * 0 <= tokens.length <= 1000
 * 0 <= tokens[i], power < 104
 *
 */
public class BagOfTokens {

    // V0
//    public int bagOfTokensScore(int[] tokens, int power) {
//
//    }

    // V1-1
    // IDEA: 2 POINTERS
    // https://leetcode.com/problems/bag-of-tokens/editorial/
    public int bagOfTokensScore_1_1(int[] tokens, int power) {
        int low = 0;
        int high = tokens.length - 1;
        int score = 0;
        Arrays.sort(tokens);

        while (low <= high) {
            // When we have enough power, play lowest token face-up
            if (power >= tokens[low]) {
                score += 1;
                power -= tokens[low];
                low += 1;
                // We don't have enough power to play a token face-up
                // If there is at least one token remaining,
                // and we have enough score, play highest token face-down
            } else if (low < high && score > 0) {
                score -= 1;
                power += tokens[high];
                high -= 1;
                // We don't have enough score, power, or tokens
                // to play face-up or down and increase our score
            } else {
                return score;
            }
        }
        return score;
    }



    // V1-2
    // IDEA: DEQUEUE
    // https://leetcode.com/problems/bag-of-tokens/editorial/
    public int bagOfTokensScore_1_2(int[] tokens, int power) {
        int score = 0;
        Arrays.sort(tokens);
        Deque<Integer> deque = new LinkedList<>();

        for (int token : tokens) {
            deque.add(token);
        }

        while (!deque.isEmpty()) {
            // When we have enough power, play token face-up
            if (power >= deque.peekFirst()) {
                power -= deque.pollFirst();
                score++;
                // We don't have enough power to play a token face-up
                // When there is at least one token remaining,
                // and we have enough score, play token face-down
            } else if (deque.size() > 1 && score > 0) {
                power += deque.pollLast();
                score--;
                // We don't have enough score, power, or tokens
                // to play face-up or down and increase our score
            } else {
                return score;
            }
        }
        return score;
    }



    // V2



    // V3



}
