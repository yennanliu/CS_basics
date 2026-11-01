package LeetCodeJava.Math;

// https://leetcode.com/problems/two-letter-card-game/description/

/**
 * 3664. Two-Letter Card Game
 * Medium
 *
 * You are given a deck of cards represented by a string array cards, and each
 * card displays two lowercase letters.
 *
 * You are also given a letter x. You play a game with the following rules:
 *
 * Start with 0 points.
 * On each turn, you must find two compatible cards from the deck that both
 * contain the letter x in any position.
 * Remove the pair of cards and earn 1 point.
 * The game ends when you can no longer find a pair of compatible cards.
 *
 * Return the maximum number of points you can gain with optimal play.
 *
 * Two cards are compatible if the strings differ in exactly 1 position.
 *
 *
 * Example 1:
 *
 * Input: cards = ["aa","ab","ba","ac"], x = "a"
 * Output: 2
 * Explanation:
 * On the first turn, select and remove cards "ab" and "ac", which are
 * compatible because they differ at only index 1.
 * On the second turn, select and remove cards "aa" and "ba", which are
 * compatible because they differ at only index 0.
 * Because there are no more compatible pairs, the total score is 2.
 *
 * Example 2:
 *
 * Input: cards = ["aa","ab","ba"], x = "a"
 * Output: 1
 * Explanation:
 * On the first turn, select and remove cards "aa" and "ba".
 * Because there are no more compatible pairs, the total score is 1.
 *
 * Example 3:
 *
 * Input: cards = ["aa","ab","ba","ac"], x = "b"
 * Output: 0
 * Explanation:
 * The only cards that contain the character 'b' are "ab" and "ba". However,
 * they differ in both indices, so they are not compatible. Thus, the output is
 * 0.
 *
 *
 * Constraints:
 *
 * 2 <= cards.length <= 10^5
 * cards[i].length == 2
 * Each cards[i] is composed of only lowercase English letters between 'a' and 'j'.
 * x is a lowercase English letter between 'a' and 'j'.
 *
 */
public class TwoLetterCardGame {

    // V0
    // IDEA: MAXIMUM MATCHING IN TWO COMPLETE MULTIPARTITE GRAPHS THAT SHARE THE "xx" CARDS
    //       only cards containing x matter, in three shapes: "xc", "cx" and "xx".
    //         "xc" vs "xd" -> compatible iff c != d      "cx" vs "dx" -> compatible iff c != d
    //         "xc" vs "dx" -> never (both positions differ)
    //         "xx" vs "xc" / "cx" -> always             "xx" vs "xx" -> never (identical)
    //       so each side ("xc" grouped by c, "cx" grouped by c) is a complete multipartite graph,
    //       whose maximum matching is min(total / 2, total - largestGroup).
    //       the "xx" cards act as one extra group on whichever side uses them; try every split
    //       t (to the left side) / both - t (to the right side) and keep the best.
    /**
     * time = O(N + Z)    (Z = number of "xx" cards)
     * space = O(1)
     */
    public int score(String[] cards, char x) {
        int[] leadingX = new int[26];  // cards "x c", counted by c
        int[] trailingX = new int[26]; // cards "c x", counted by c
        int doubleX = 0;               // cards "xx"
        for (String card : cards) {
            char first = card.charAt(0);
            char second = card.charAt(1);
            if (first == x && second == x) {
                doubleX++;
            } else if (first == x) {
                leadingX[second - 'a']++;
            } else if (second == x) {
                trailingX[first - 'a']++;
            }
        }

        int leftTotal = 0;
        int leftLargest = 0;
        int rightTotal = 0;
        int rightLargest = 0;
        for (int c = 0; c < 26; c++) {
            leftTotal += leadingX[c];
            leftLargest = Math.max(leftLargest, leadingX[c]);
            rightTotal += trailingX[c];
            rightLargest = Math.max(rightLargest, trailingX[c]);
        }

        int best = 0;
        for (int toLeft = 0; toLeft <= doubleX; toLeft++) {
            int toRight = doubleX - toLeft;
            // the "xx" cards join a side as one more group of size toLeft / toRight
            int leftPairs = maxMatching(leftTotal + toLeft, Math.max(leftLargest, toLeft));
            int rightPairs = maxMatching(rightTotal + toRight, Math.max(rightLargest, toRight));
            best = Math.max(best, leftPairs + rightPairs);
        }
        return best;
    }

    /** Maximum matching of a complete multipartite graph with the given total and largest part. */
    private int maxMatching(int total, int largestGroup) {
        // either everything pairs up, or one oversized group can only pair with the rest
        return Math.min(total / 2, total - largestGroup);
    }
}
