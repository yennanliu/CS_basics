package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/maximum-points-you-can-obtain-from-cards/description/

/**
 * 1423. Maximum Points You Can Obtain from Cards
 * Medium
 *
 * There are several cards arranged in a row, and each card has an associated number
 * of points. The points are given in the integer array cardPoints.
 *
 * In one step, you can take one card from the beginning or from the end of the row.
 * You have to take exactly k cards.
 *
 * Your score is the sum of the points of the cards you have taken.
 *
 * Given the integer array cardPoints and the integer k, return the maximum score you
 * can obtain.
 *
 *
 * Example 1:
 *
 * Input: cardPoints = [1,2,3,4,5,6,1], k = 3
 * Output: 12
 * Explanation: After the first step, your score will always be 1. However, choosing
 * the rightmost card first will maximize your total score. The optimal strategy is to
 * take the three cards on the right, giving a final score of 1 + 6 + 5 = 12.
 *
 * Example 2:
 *
 * Input: cardPoints = [2,2,2], k = 2
 * Output: 4
 * Explanation: Regardless of which two cards you take, your score will always be 4.
 *
 * Example 3:
 *
 * Input: cardPoints = [9,7,7,9,7,7,9], k = 7
 * Output: 55
 * Explanation: You have to take all the cards. Your score is the sum of points of all
 * cards.
 *
 *
 * Constraints:
 *
 * 1 <= cardPoints.length <= 10^5
 * 1 <= cardPoints[i] <= 10^4
 * 1 <= k <= cardPoints.length
 *
 */
public class MaximumPointsYouCanObtainFromCards {

    // V0
    // IDEA: SLIDING WINDOW ON THE COMPLEMENT (the cards you LEAVE form one contiguous block of n - k)
    // taking the most == leaving the least:  answer = sum(all) - min window sum of length (n - k)
    // e.g. [1,2,3,4,5,6,1], k = 3 -> min block of 4 = 1+2+3+4 = 10, total = 22 -> 12
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxScore(int[] cardPoints, int k) {
        // edge case: nothing to take
        if (cardPoints == null || cardPoints.length == 0 || k <= 0) {
            return 0;
        }
        int n = cardPoints.length;

        int total = 0;
        for (int point : cardPoints) {
            total += point;
        }

        // length of the block left behind; k == n leaves nothing, so take everything
        int leftLen = n - k;
        if (leftLen <= 0) {
            return total;
        }

        // sum of the first window of length leftLen
        int windowSum = 0;
        for (int i = 0; i < leftLen; i++) {
            windowSum += cardPoints[i];
        }
        int minWindowSum = windowSum;

        // slide the window one step at a time: add the new right card, drop the old left card
        for (int i = leftLen; i < n; i++) {
            windowSum += cardPoints[i] - cardPoints[i - leftLen];
            minWindowSum = Math.min(minWindowSum, windowSum);
        }

        return total - minWindowSum;
    }

    // V1
    // IDEA: TAKE i FROM THE FRONT AND k - i FROM THE BACK (slide the split point)
    // start with all k cards from the front, then move one card at a time from the front to the back.
    /**
     * time = O(K)
     * space = O(1)
     */
    public int maxScore_1(int[] cardPoints, int k) {
        if (cardPoints == null || cardPoints.length == 0 || k <= 0) {
            return 0;
        }
        int n = cardPoints.length;

        // all k cards taken from the front
        int score = 0;
        for (int i = 0; i < k; i++) {
            score += cardPoints[i];
        }
        int best = score;

        // give back front card k-1-i, take back card n-1-i instead
        for (int i = 0; i < k; i++) {
            score -= cardPoints[k - 1 - i];
            score += cardPoints[n - 1 - i];
            best = Math.max(best, score);
        }
        return best;
    }
}
