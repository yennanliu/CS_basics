package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-distance-to-type-a-word-using-two-fingers/description/

import java.util.Arrays;

/**
 * 1320. Minimum Distance to Type a Word Using Two Fingers
 * Hard
 *
 * You have a keyboard layout in the X-Y plane, where each English uppercase letter is
 * located at some coordinate.
 *
 * For example, the letter 'A' is located at coordinate (0, 0), the letter 'B' is located
 * at coordinate (0, 1), the letter 'P' is located at coordinate (2, 3) and the letter 'Z'
 * is located at coordinate (4, 1).
 *
 * Given the string word, return the minimum total distance to type such string using only
 * two fingers.
 *
 * The distance between coordinates (x1, y1) and (x2, y2) is |x1 - x2| + |y1 - y2|.
 *
 * Note that the initial positions of your two fingers are considered free so do not count
 * towards your total distance, also your two fingers do not have to start at the first
 * letter or the first two letters.
 *
 *
 * Example 1:
 *
 * Input: word = "CAKE"
 * Output: 3
 * Explanation: Using two fingers, one optimal way to type "CAKE" is:
 * Finger 1 on letter 'C' -> cost = 0
 * Finger 1 on letter 'A' -> cost = Distance from letter 'C' to letter 'A' = 2
 * Finger 2 on letter 'K' -> cost = 0
 * Finger 2 on letter 'E' -> cost = Distance from letter 'K' to letter 'E' = 1
 * Total distance = 3
 *
 * Example 2:
 *
 * Input: word = "HAPPY"
 * Output: 6
 * Explanation: Using two fingers, one optimal way to type "HAPPY" is:
 * Finger 1 on letter 'H' -> cost = 0
 * Finger 1 on letter 'A' -> cost = Distance from letter 'H' to letter 'A' = 2
 * Finger 2 on letter 'P' -> cost = 0
 * Finger 2 on letter 'P' -> cost = Distance from letter 'P' to letter 'P' = 0
 * Finger 1 on letter 'Y' -> cost = Distance from letter 'A' to letter 'Y' = 4
 * Total distance = 6
 *
 *
 * Constraints:
 *
 * 2 <= word.length <= 300
 * word consists of uppercase English letters.
 *
 */
public class MinimumDistanceToTypeAWordUsingTwoFingers {

    /** "The second finger has not been placed yet": its first placement costs nothing. */
    private static final int FREE = 26;
    /** The keyboard is 6 keys wide, so letter k sits at (k / 6, k % 6). */
    private static final int KEYBOARD_WIDTH = 6;

    // V0
    // IDEA: DP OVER "WHERE THE OTHER FINGER RESTS" (THE TYPING FINGER IS IMPLICIT)
    /**
     * After typing word[i], one finger necessarily sits on word[i]; the only free
     * information is where the OTHER finger is.
     *
     * dp[j] = min cost so far, one finger on word[i], the other on letter j (FREE = unplaced)
     *
     * from word[i-1] to word[i], exactly two choices:
     *   1) the finger on word[i-1] types it:  next[j]         = dp[j] + dist(word[i-1], word[i])
     *   2) the finger on j types it, so word[i-1] becomes the resting letter:
     *                                         next[word[i-1]] = dp[j] + dist(j, word[i])
     *
     * dist(FREE, x) = 0 -- the initial placement of a finger is free.
     * base: after word[0], dp[FREE] = 0; answer = min(dp)
     */
    /**
     * time = O(26 * N)
     * space = O(26)
     */
    public int minimumDistance(String word) {
        int INF = Integer.MAX_VALUE;
        int[] dp = new int[FREE + 1];
        Arrays.fill(dp, INF);
        dp[FREE] = 0;

        for (int i = 1; i < word.length(); i++) {
            int prev = word.charAt(i - 1) - 'A';
            int cur = word.charAt(i) - 'A';
            int sameFingerStep = distance(prev, cur);

            int[] next = new int[FREE + 1];
            Arrays.fill(next, INF);
            for (int other = 0; other <= FREE; other++) {
                if (dp[other] == INF) {
                    continue;
                }
                // 1) the finger already on word[i-1] keeps typing
                next[other] = Math.min(next[other], dp[other] + sameFingerStep);
                // 2) the resting finger takes over; word[i-1] is now where the other finger rests
                next[prev] = Math.min(next[prev], dp[other] + distance(other, cur));
            }
            dp = next;
        }

        int answer = INF;
        for (int cost : dp) {
            answer = Math.min(answer, cost);
        }
        return answer;
    }

    /** Manhattan distance between two letters; 0 if either finger is still FREE. */
    private int distance(int from, int to) {
        if (from == FREE || to == FREE) {
            return 0;
        }
        int rowDiff = Math.abs(from / KEYBOARD_WIDTH - to / KEYBOARD_WIDTH);
        int colDiff = Math.abs(from % KEYBOARD_WIDTH - to % KEYBOARD_WIDTH);
        return rowDiff + colDiff;
    }
}
