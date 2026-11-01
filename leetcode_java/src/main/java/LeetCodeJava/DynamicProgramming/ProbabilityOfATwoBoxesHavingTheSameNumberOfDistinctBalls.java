package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/probability-of-a-two-boxes-having-the-same-number-of-distinct-balls/description/

/**
 * 1467. Probability of a Two Boxes Having The Same Number of Distinct Balls
 * Hard
 *
 * Given 2n balls of k distinct colors. You will be given an integer array balls of size k where balls[i] is the number of balls of color i.
 *
 * All the balls will be shuffled uniformly at random, then we will distribute the first n balls to the first box and the remaining n balls to the other box (Please read the explanation of the second example carefully).
 *
 * Please note that the two boxes are considered different. For example, if we have two balls of colors a and b, and two boxes [] and (), then the distribution [a] (b) is considered different than the distribution [b] (a) (Please read the explanation of the first example carefully).
 *
 * Return the probability that the two boxes have the same number of distinct balls. Answers within 10^-5 of the actual value will be accepted as correct.
 *
 *
 * Example 1:
 *
 * Input: balls = [1,1]
 * Output: 1.00000
 * Explanation: Only 2 ways to divide the balls equally:
 * - A ball of color 1 to box 1 and a ball of color 2 to box 2
 * - A ball of color 2 to box 1 and a ball of color 1 to box 2
 * In both ways, the number of distinct colors in each box is equal. The probability is 2/2 = 1
 *
 * Example 2:
 *
 * Input: balls = [2,1,1]
 * Output: 0.66667
 * Explanation: We have the set of balls [1, 1, 2, 3]
 * This set of balls will be shuffled randomly and we may have one of the 12 distinct shuffles with equal probability (i.e. 1/12):
 * [1,1 / 2,3], [1,1 / 3,2], [1,2 / 1,3], [1,2 / 3,1], [1,3 / 1,2], [1,3 / 2,1], [2,1 / 1,3], [2,1 / 3,1], [2,3 / 1,1], [3,1 / 1,2], [3,1 / 2,1], [3,2 / 1,1]
 * After that, we add the first two balls to the first box and the second two balls to the second box.
 * We can see that 8 of these 12 possible random distributions have the same number of distinct colors of balls in each box.
 * Probability is 8/12 = 0.66667
 *
 * Example 3:
 *
 * Input: balls = [1,2,1,2]
 * Output: 0.60000
 * Explanation: The set of balls is [1, 2, 2, 3, 4, 4]. It is hard to display all the 180 possible random shuffles of this set but it is easy to check that 108 of them will have the same number of distinct colors in each box.
 * Probability = 108 / 180 = 0.6
 *
 *
 * Constraints:
 *
 * 1 <= balls.length <= 8
 * 1 <= balls[i] <= 6
 * sum(balls) is even.
 *
 */
public class ProbabilityOfATwoBoxesHavingTheSameNumberOfDistinctBalls {

    // V0
    // IDEA: COMBINATORICS + MEMOIZED DFS OVER COLOURS (count splits, not shuffles)
    //       a random shuffle cut in half = choosing which n of the 2n positions go to box 1, so
    //         P = (weighted # of valid colour splits) / C(2n, n)
    //       putting x balls of colour i into box 1 can happen in C(balls[i], x) ways.
    //       diff = distinct(box 1) - distinct(box 2): all of a colour -> +1, none -> -1,
    //       a partial take -> 0. A split counts iff box 1 is exactly full and diff == 0.
    /**
     * time = O(K * N * K * B)   (K colours, N balls per box, B = max balls of one colour)
     * space = O(K * N * K)
     */
    public double getProbability(int[] balls) {
        int colours = balls.length;
        int total = 0;
        for (int count : balls) {
            total += count;
        }
        int half = total / 2;

        // memo[i][slotsLeft][diff + colours]; diff ranges over [-colours, colours]
        Long[][][] memo = new Long[colours + 1][half + 1][2 * colours + 1];
        long goodSplits = countSplits(0, half, 0, balls, memo);
        return (double) goodSplits / combination(2 * half, half);
    }

    /** Weighted number of ways to place colours i.. with `slotsLeft` free slots in box 1. */
    private long countSplits(int i, int slotsLeft, int diff, int[] balls, Long[][][] memo) {
        // box 1 overflowed
        if (slotsLeft < 0) {
            return 0;
        }
        int colours = balls.length;
        // every colour placed: valid only if box 1 is exactly full and the distinct counts match
        if (i == colours) {
            return (slotsLeft == 0 && diff == 0) ? 1 : 0;
        }
        if (memo[i][slotsLeft][diff + colours] != null) {
            return memo[i][slotsLeft][diff + colours];
        }

        long ways = 0;
        for (int x = 0; x <= balls[i]; x++) {
            // how this colour changes distinct(box 1) - distinct(box 2)
            int delta;
            if (x == balls[i]) {
                delta = 1;      // the colour is only in box 1
            } else if (x == 0) {
                delta = -1;     // the colour is only in box 2
            } else {
                delta = 0;      // the colour is in both boxes
            }
            ways += combination(balls[i], x) * countSplits(i + 1, slotsLeft - x, diff + delta, balls, memo);
        }

        memo[i][slotsLeft][diff + colours] = ways;
        return ways;
    }

    /** C(a, b), computed incrementally so every intermediate value stays exact. */
    private long combination(int a, int b) {
        if (b < 0 || b > a) {
            return 0;
        }
        long result = 1;
        for (int i = 1; i <= b; i++) {
            // result * (a - b + i) is divisible by i, since it equals C(a - b + i, i) * i
            result = result * (a - b + i) / i;
        }
        return result;
    }
}
