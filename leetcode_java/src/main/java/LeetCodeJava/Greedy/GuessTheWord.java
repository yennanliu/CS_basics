package LeetCodeJava.Greedy;

// https://leetcode.com/problems/guess-the-word/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 843. Guess the Word
 * Hard
 *
 * You are given an array of unique strings words where words[i] is six letters long.
 * One word of words was chosen as a secret word.
 *
 * You are also given the helper object Master. You may call Master.guess(word) where
 * word is a six-letter-long string, and it must be from words.
 * Master.guess(word) returns:
 *
 * -1 if word is not from words, or
 * an integer representing the number of exact matches (value and position) of your
 * guess to the secret word.
 *
 * There is a parameter allowedGuesses for each test case where allowedGuesses is the
 * maximum number of times you can call Master.guess(word).
 *
 * For each test case, you should call Master.guess with the secret word without
 * exceeding the maximum number of allowed guesses.
 *
 *
 * Example 1:
 *
 * Input: secret = "acckzz", words = ["acckzz","ccbazz","eiowzz","abcczz"],
 *        allowedGuesses = 10
 * Output: You guessed the secret word correctly.
 * Explanation:
 * master.guess("aaaaaa") returns -1, because "aaaaaa" is not in words.
 * master.guess("acckzz") returns 6, because "acckzz" is secret and has all 6 matches.
 * master.guess("ccbazz") returns 3, because "ccbazz" has 3 matches.
 * master.guess("eiowzz") returns 2, because "eiowzz" has 2 matches.
 * master.guess("abcczz") returns 4, because "abcczz" has 4 matches.
 * We made 5 calls to master.guess, and one of them was the secret,
 * so we pass the test case.
 *
 * Example 2:
 *
 * Input: secret = "hamada", words = ["hamada","khaled"], allowedGuesses = 10
 * Output: You guessed the secret word correctly.
 * Explanation: Since there are two words, you can guess both.
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 100
 * words[i].length == 6
 * words[i] consist of lowercase English letters.
 * All the strings of words are unique.
 * secret exists in words.
 * 10 <= allowedGuesses <= 30
 *
 */
public class GuessTheWord {

    // dummy API for passing java syntax check
    // offered by LC platform when submission
    interface Master {
        int guess(String word);
    }

    // V0
    // IDEA: GREEDY MINIMAX (`guess the word that shrinks the worst case most`)
    /**
     *   After each guess we learn the match count and can DROP every candidate
     *   that is inconsistent with it. Guessing a RANDOM word is risky: most pairs
     *   of random 6-letter words share 0 characters, so a 0 answer barely prunes.
     *
     *   Instead, before every guess we score each candidate w by
     *
     *       worst(w) = max over m in 0..5 of  #{c in candidates : match(w, c) == m}
     *
     *   i.e. the size of the LARGEST bucket we could be left with, and guess the
     *   word MINIMISING it. This keeps the candidate set shrinking fast enough to
     *   finish within 10 guesses.
     *
     *   NOTE !!! bucket 6 means `we already won`, so it is deliberately EXCLUDED
     *            from the worst-case max.
     *
     *   time  = O(g * n^2 * L)   // g <= 10 guesses, n = words.length, L = 6
     *   space = O(n)
     */
    public void findSecretWord(String[] words, Master master) {
        List<String> candidates = new ArrayList<>(Arrays.asList(words));

        for (int round = 0; round < 10; round++) {

            // pick the MINIMAX guess among the remaining candidates
            String best = candidates.get(0);
            int bestWorst = Integer.MAX_VALUE;

            for (String w : candidates) {
                int[] buckets = new int[7];
                for (String c : candidates) {
                    buckets[match(w, c)] += 1;
                }
                int worst = 0;
                for (int m = 0; m < 6; m++) {
                    worst = Math.max(worst, buckets[m]);
                }
                if (worst < bestWorst) {
                    bestWorst = worst;
                    best = w;
                }
            }

            int m = master.guess(best);
            if (m == 6) {
                return;
            }

            // keep ONLY the words consistent with the feedback
            List<String> next = new ArrayList<>();
            for (String c : candidates) {
                if (match(best, c) == m) {
                    next.add(c);
                }
            }
            candidates = next;
        }
    }

    /** number of positions where a and b hold the same character */
    private int match(String a, String b) {
        int cnt = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(i)) {
                cnt += 1;
            }
        }
        return cnt;
    }

}
