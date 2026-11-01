package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-number-of-teams/description/

/**
 * 1395. Count Number of Teams
 * Medium
 *
 * There are n soldiers standing in a line. Each soldier is assigned a unique rating value.
 *
 * You have to form a team of 3 soldiers amongst them under the following rules:
 *
 * Choose 3 soldiers with index (i, j, k) with rating (rating[i], rating[j], rating[k]).
 * A team is valid if: (rating[i] < rating[j] < rating[k]) or
 * (rating[i] > rating[j] > rating[k]) where (0 <= i < j < k < n).
 *
 * Return the number of teams you can form given the conditions. (soldiers can be part of
 * multiple teams).
 *
 *
 * Example 1:
 *
 * Input: rating = [2,5,3,4,1]
 * Output: 3
 * Explanation: We can form three teams given the conditions. (2,3,4), (5,4,1), (5,3,1).
 *
 * Example 2:
 *
 * Input: rating = [2,1,3]
 * Output: 0
 * Explanation: We can't form any team given the conditions.
 *
 * Example 3:
 *
 * Input: rating = [1,2,3,4]
 * Output: 4
 *
 *
 * Constraints:
 *
 * n == rating.length
 * 3 <= n <= 1000
 * 1 <= rating[i] <= 10^5
 * All the integers in rating are unique.
 *
 */
public class CountNumberOfTeams {

    // V0
    // IDEA: FIX THE MIDDLE SOLDIER, COUNT SMALLER ON THE LEFT / LARGER ON THE RIGHT
    /**
     * every team is determined by its middle index j:
     *   smallerLeft  = # of i < j with rating[i] < rating[j]
     *   largerRight  = # of k > j with rating[k] > rating[j]
     *
     * increasing teams through j = smallerLeft * largerRight
     * decreasing teams through j = largerLeft * smallerRight
     *   where largerLeft = j - smallerLeft, smallerRight = (n - 1 - j) - largerRight
     *   (ratings are unique, so "not smaller" means "larger")
     */
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public int numTeams(int[] rating) {
        int n = rating.length;
        int teams = 0;

        for (int j = 0; j < n; j++) {
            int smallerLeft = 0;
            for (int i = 0; i < j; i++) {
                if (rating[i] < rating[j]) {
                    smallerLeft++;
                }
            }
            int largerRight = 0;
            for (int k = j + 1; k < n; k++) {
                if (rating[k] > rating[j]) {
                    largerRight++;
                }
            }
            int largerLeft = j - smallerLeft;
            int smallerRight = (n - 1 - j) - largerRight;

            // small < mid < big, and big > mid > small
            teams += smallerLeft * largerRight;
            teams += largerLeft * smallerRight;
        }
        return teams;
    }
}
