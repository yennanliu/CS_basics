package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/maximize-the-confusion-of-an-exam/description/

/**
 * 2024. Maximize the Confusion of an Exam
 * Medium
 *
 * A teacher is writing a test with n true/false questions, with 'T' denoting true and 'F' denoting false. He wants to confuse the students by maximizing the number of consecutive questions with the same answer (multiple trues or multiple falses in a row).
 *
 * You are given a string answerKey, where answerKey[i] is the original answer to the ith question. In addition, you are given an integer k, the maximum number of times you may perform the following operation:
 *
 * Change the answer key for any question to 'T' or 'F' (i.e., set answerKey[i] to 'T' or 'F').
 *
 * Return the maximum number of consecutive 'T's or 'F's in the answer key after performing the operation at most k times.
 *
 *
 * Example 1:
 *
 * Input: answerKey = "TTFF", k = 2
 * Output: 4
 * Explanation: We can replace both the 'F's with 'T's to make answerKey = "TTTT".
 * There are four consecutive 'T's.
 *
 * Example 2:
 *
 * Input: answerKey = "TFFT", k = 1
 * Output: 3
 * Explanation: We can replace the first 'T' with an 'F' to make answerKey = "FFFT".
 * Alternatively, we can replace the second 'T' with an 'F' to make answerKey = "TFFF".
 * In both cases, there are three consecutive 'F's.
 *
 * Example 3:
 *
 * Input: answerKey = "TTFTTFTT", k = 1
 * Output: 5
 * Explanation: We can replace the first 'F' to make answerKey = "TTTTTFTT"
 * Alternatively, we can replace the second 'F' to make answerKey = "TTFTTTTT".
 * In both cases, there are five consecutive 'T's.
 *
 *
 * Constraints:
 *
 * n == answerKey.length
 * 1 <= n <= 5 * 10^4
 * answerKey[i] is either 'T' or 'F'
 * 1 <= k <= n
 *
 */
public class MaximizeTheConfusionOfAnExam {

    // V0
    // IDEA: SLIDING WINDOW, TWICE (same as LC 424 "longest repeating char replacement")
    //       run the window once aiming for all 'T', once for all 'F'. for a fixed
    //       target a window is valid iff it holds at most k of the OTHER letter
    //       (those are the flips spent). grow right, shrink left while invalid,
    //       track the widest window.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxConsecutiveAnswers(String answerKey, int k) {
        return Math.max(longestWindow(answerKey, k, 'T'), longestWindow(answerKey, k, 'F'));
    }

    /** Longest window that becomes all `target` with at most k flips. */
    private int longestWindow(String answerKey, int k, char target) {
        int best = 0;
        int left = 0;
        int flipsNeeded = 0;
        for (int right = 0; right < answerKey.length(); right++) {
            if (answerKey.charAt(right) != target) {
                flipsNeeded++;
            }
            // too many flips -> shrink from the left
            while (flipsNeeded > k) {
                if (answerKey.charAt(left) != target) {
                    flipsNeeded--;
                }
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }
}
