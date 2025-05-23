package LeetCodeJava.Greedy;

// https://leetcode.com/problems/candy/
/**
 * 135. Candy
 * Solved
 * Hard
 * Topics
 * Companies
 * There are n children standing in a line. Each child is assigned a rating value given in the integer array ratings.
 *
 * You are giving candies to these children subjected to the following requirements:
 *
 * Each child must have at least one candy.
 * Children with a higher rating get more candies than their neighbors.
 * Return the minimum number of candies you need to have to distribute the candies to the children.
 *
 *
 *
 * Example 1:
 *
 * Input: ratings = [1,0,2]
 * Output: 5
 * Explanation: You can allocate to the first, second and third child with 2, 1, 2 candies respectively.
 * Example 2:
 *
 * Input: ratings = [1,2,2]
 * Output: 4
 * Explanation: You can allocate to the first, second and third child with 1, 2, 1 candies respectively.
 * The third child gets 1 candy because it satisfies the above two conditions.
 *
 *
 * Constraints:
 *
 * n == ratings.length
 * 1 <= n <= 2 * 104
 * 0 <= ratings[i] <= 2 * 104
 */
import java.util.Arrays;

public class Candy {

    // V0
    // IDEA : GREEDY
    // https://www.bilibili.com/video/BV1ev4y1r7wN/?share_source=copy_web&vd_source=49348a1975630e1327042905e52f488a
    //public int candy(int[] ratings) {}

    // V0-1
    // IDEA: GREEDY (loop 2 times over 2 direction) (fixed by gpt)
    // https://www.youtube.com/watch?v=1IzCRCcK17A
    public int candy_0_1(int[] ratings) {
        // edge cases
        if (ratings == null || ratings.length == 0) {
            return 0;
        }
        if (ratings.length == 1) {
            return 1;
        }

        int n = ratings.length;
        int[] cache = new int[n];
        Arrays.fill(cache, 1); // every child gets at least one candy

        // Step 1: left to right
        // (left -> right) (check left) (start_idx = 1)
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                /**
                 *  NOTE !!! below
                 */
                cache[i] = cache[i - 1] + 1;
            }
        }

        // Step 2: right to left
        // (right -> left) (check right) (start_idx = ratings.len -2)
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                /**
                 *  NOTE !!! below
                 */
                cache[i] = Math.max(cache[i], cache[i + 1] + 1);
            }
        }

        // Sum up the result
        int res = 0;
        for (int x : cache) {
            res += x;
        }

        return res;
    }


    // V1
    // https://www.youtube.com/watch?v=1IzCRCcK17A

    // V2
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/candy/editorial/
    public int candy_2(int[] ratings) {
        int[] candies = new int[ratings.length];
        Arrays.fill(candies, 1);
        boolean hasChanged = true;
        while (hasChanged) {
            hasChanged = false;
            for (int i = 0; i < ratings.length; i++) {
                if (i != ratings.length - 1 && ratings[i] > ratings[i + 1] && candies[i] <= candies[i + 1]) {
                    candies[i] = candies[i + 1] + 1;
                    hasChanged = true;
                }
                if (i > 0 && ratings[i] > ratings[i - 1] && candies[i] <= candies[i - 1]) {
                    candies[i] = candies[i - 1] + 1;
                    hasChanged = true;
                }
            }
        }
        int sum = 0;
        for (int candy : candies) {
            sum += candy;
        }
        return sum;
    }

    // V3
    // IDEA :  Using two arrays
    // https://leetcode.com/problems/candy/editorial/
    public int candy_3(int[] ratings) {
        int sum = 0;
        int[] left2right = new int[ratings.length];
        int[] right2left = new int[ratings.length];
        Arrays.fill(left2right, 1);
        Arrays.fill(right2left, 1);
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                left2right[i] = left2right[i - 1] + 1;
            }
        }
        for (int i = ratings.length - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                right2left[i] = right2left[i + 1] + 1;
            }
        }
        for (int i = 0; i < ratings.length; i++) {
            sum += Math.max(left2right[i], right2left[i]);
        }
        return sum;
    }

    // V4
    // IDEA : Using one array
    // https://leetcode.com/problems/candy/editorial/
    public int candy_4(int[] ratings) {
        int[] candies = new int[ratings.length];
        Arrays.fill(candies, 1);
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }
        int sum = candies[ratings.length - 1];
        for (int i = ratings.length - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
            sum += candies[i];
        }
        return sum;
    }

    // V5
    // IDEA : Single Pass Approach with Constant Space
    // https://leetcode.com/problems/candy/editorial/
    public int count(int n) {
        return (n * (n + 1)) / 2;
    }
    public int candy_5(int[] ratings) {
        if (ratings.length <= 1) {
            return ratings.length;
        }
        int candies = 0;
        int up = 0;
        int down = 0;
        int oldSlope = 0;
        for (int i = 1; i < ratings.length; i++) {
            int newSlope = (ratings[i] > ratings[i - 1]) ? 1
                    : (ratings[i] < ratings[i - 1] ? -1
                    : 0);

            if ((oldSlope > 0 && newSlope == 0) || (oldSlope < 0 && newSlope >= 0)) {
                candies += count(up) + count(down) + Math.max(up, down);
                up = 0;
                down = 0;
            }
            if (newSlope > 0) {
                up++;
            } else if (newSlope < 0) {
                down++;
            } else {
                candies++;
            }

            oldSlope = newSlope;
        }
        candies += count(up) + count(down) + Math.max(up, down) + 1;
        return candies;
    }

}
