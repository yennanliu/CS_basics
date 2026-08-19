package LeetCodeJava.HashTable;

// https://leetcode.com/problems/bulls-and-cows/

import java.util.*;

/**
 *  299. Bulls and Cows
 *  Medium
 *
 *  You are playing the Bulls and Cows game with your friend.
 *  You write down a secret number and ask your friend to guess it. When your
 *  friend makes a guess, you provide a hint with the following info:
 *   - The number of "bulls", which are digits in the guess that are in the
 *     correct position.
 *   - The number of "cows", which are digits in the guess that are in your
 *     secret number but are located in the wrong position.
 *
 *  Return the hint for your friend's guess, formatted as "xAyB", where x is
 *  the number of bulls and y is the number of cows.
 *
 *  Example 1:
 *  Input: secret = "1807", guess = "7810"
 *  Output: "1A3B"
 *
 *  Example 2:
 *  Input: secret = "1123", guess = "0111"
 *  Output: "1A1B"
 *
 *  Constraints:
 *   - 1 <= secret.length, guess.length <= 1000
 *   - secret.length == guess.length
 *   - secret and guess consist of digits only.
 */
public class BullsAndCows {

    // V0
    // IDEA: DIGIT COUNTING (2 arrays of size 10)
    //       bulls = same digit at same idx,
    //       bulls + cows = sum of min(cntSecret[d], cntGuess[d])
    /**
     * time = O(n)
     * space = O(10) = O(1)
     */
    public String getHint(String secret, String guess) {

        int[] cntS = new int[10];
        int[] cntG = new int[10];

        int bulls = 0;

        for (int i = 0; i < secret.length(); i++) {
            int a = secret.charAt(i) - '0';
            int b = guess.charAt(i) - '0';
            if (a == b) {
                bulls++;
            }
            cntS[a]++;
            cntG[b]++;
        }

        int matched = 0; // bulls + cows
        for (int d = 0; d < 10; d++) {
            matched += Math.min(cntS[d], cntG[d]);
        }

        int cows = matched - bulls;

        return bulls + "A" + cows + "B";
    }
}
