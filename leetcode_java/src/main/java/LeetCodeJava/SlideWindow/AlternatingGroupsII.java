package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/alternating-groups-ii/

/**
 *  3208. Alternating Groups II
 *  Medium
 *
 *  There is a circle of red and blue tiles. You are given an array of integers
 *  colors and an integer k. The color of tile i is represented by colors[i]:
 *
 *   - colors[i] == 0 means that tile i is red.
 *   - colors[i] == 1 means that tile i is blue.
 *
 *  An alternating group is every k contiguous tiles in the circle with
 *  alternating colors (each tile in the group except the first and last one has
 *  a different color from its left and right tiles).
 *
 *  Return the number of alternating groups.
 *
 *  Note that since colors represents a circle, the first and the last tiles are
 *  considered to be next to each other.
 *
 *  Example 1:
 *    Input: colors = [0,1,0,1,0], k = 3
 *    Output: 3
 *
 *  Example 2:
 *    Input: colors = [0,1,0,0,1,0,1], k = 6
 *    Output: 2
 *
 *  Example 3:
 *    Input: colors = [1,1,0,1], k = 4
 *    Output: 0
 *
 *  Constraints:
 *    3 <= colors.length <= 10^5
 *    0 <= colors[i] <= 1
 *    3 <= k <= colors.length
 */
public class AlternatingGroupsII {

    // V0
    // IDEA: TRACK THE CURRENT ALTERNATING RUN LENGTH AROUND THE CIRCLE
    //       a window of k tiles is a group exactly when every adjacent pair
    //       inside it differs. so walk the circle keeping `run` = how many
    //       tiles the current alternating stretch covers, resetting to 1
    //       whenever two neighbours match. every position where run reaches k
    //       closes one group.
    //       the wrap-around is handled by walking n + k - 1 steps (indices
    //       taken modulo n), which lets a run starting near the end finish at
    //       the front without double counting.
    /**
     * time = O(N + K)
     * space = O(1)
     */
    public int numberOfAlternatingGroups(int[] colors, int k) {
        int n = colors.length;
        int res = 0;
        int run = 1;
        for (int t = 1; t < n + k - 1; t++) {
            int i = t % n;
            int prev = (t - 1) % n;
            run = (colors[i] != colors[prev]) ? run + 1 : 1;
            if (run >= k) {
                res++;
            }
        }
        return res;
    }
}
