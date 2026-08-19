package LeetCodeJava.Array;

// https://leetcode.com/problems/can-place-flowers/

/**
 *  605. Can Place Flowers
 *  Easy
 *
 *  You have a long flowerbed in which some of the plots are planted, and some are not.
 *  However, flowers cannot be planted in adjacent plots.
 *
 *  Given an integer array flowerbed containing 0's and 1's, where 0 means empty and
 *  1 means not empty, and an integer n, return true if n new flowers can be planted
 *  in the flowerbed without violating the no-adjacent-flowers rule.
 *
 *  Example 1:
 *  Input: flowerbed = [1,0,0,0,1], n = 1
 *  Output: true
 *
 *  Example 2:
 *  Input: flowerbed = [1,0,0,0,1], n = 2
 *  Output: false
 *
 *  Constraints:
 *  1 <= flowerbed.length <= 2 * 10^4
 *  flowerbed[i] is 0 or 1.
 *  There are no two adjacent flowers in flowerbed.
 *  0 <= n <= flowerbed.length
 */
public class CanPlaceFlowers {

    // V0
    // IDEA: GREEDY - plant as early as possible whenever the plot and both
    //       neighbours (out-of-range counts as empty) are free
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        if (n <= 0) {
            return true;
        }
        if (flowerbed == null || flowerbed.length == 0) {
            return false;
        }
        int len = flowerbed.length;
        int cnt = 0;
        for (int i = 0; i < len; i++) {
            if (flowerbed[i] != 0) {
                continue;
            }
            boolean leftEmpty = (i == 0) || (flowerbed[i - 1] == 0);
            boolean rightEmpty = (i == len - 1) || (flowerbed[i + 1] == 0);
            if (leftEmpty && rightEmpty) {
                flowerbed[i] = 1; // plant here
                cnt++;
                if (cnt >= n) {
                    return true;
                }
            }
        }
        return cnt >= n;
    }
}
