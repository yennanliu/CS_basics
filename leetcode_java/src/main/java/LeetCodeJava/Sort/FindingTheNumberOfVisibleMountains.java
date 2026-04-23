package LeetCodeJava.Sort;

// https://leetcode.com/problems/finding-the-number-of-visible-mountains/
// https://leetcode.ca/2022-08-25-2345-Finding-the-Number-of-Visible-Mountains/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  2345 - Finding the Number of Visible Mountains
 * Posted on August 25, 2022 · 3 minute read
 * Welcome to Subscribe On Youtube
 *
 * Formatted question description: https://leetcode.ca/all/2345.html
 *
 * 2345. Finding the Number of Visible Mountains
 * Description
 * You are given a 0-indexed 2D integer array peaks where peaks[i] = [xi, yi] states that mountain i has a peak at coordinates (xi, yi). A mountain can be described as a right-angled isosceles triangle, with its base along the x-axis and a right angle at its peak. More formally, the gradients of ascending and descending the mountain are 1 and -1 respectively.
 *
 * A mountain is considered visible if its peak does not lie within another mountain (including the border of other mountains).
 *
 * Return the number of visible mountains.
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: peaks = [[2,2],[6,3],[5,4]]
 * Output: 2
 * Explanation: The diagram above shows the mountains.
 * - Mountain 0 is visible since its peak does not lie within another mountain or its sides.
 * - Mountain 1 is not visible since its peak lies within the side of mountain 2.
 * - Mountain 2 is visible since its peak does not lie within another mountain or its sides.
 * There are 2 mountains that are visible.
 * Example 2:
 *
 *
 *
 * Input: peaks = [[1,3],[1,3]]
 * Output: 0
 * Explanation: The diagram above shows the mountains (they completely overlap).
 * Both mountains are not visible since their peaks lie within each other.
 *
 *
 * Constraints:
 *
 * 1 <= peaks.length <= 105
 * peaks[i].length == 2
 * 1 <= xi, yi <= 105
 *
 *
 */
public class FindingTheNumberOfVisibleMountains {

    // V0

    // V1
    // IDEA:
    // https://leetcode.ca/2022-08-25-2345-Finding-the-Number-of-Visible-Mountains/
    public int visibleMountains_1(int[][] peaks) {
        int n = peaks.length;
        int[][] arr = new int[n][2];
        Map<String, Integer> cnt = new HashMap<>();
        for (int i = 0; i < n; ++i) {
            int x = peaks[i][0], y = peaks[i][1];
            arr[i] = new int[] {x - y, x + y};
            cnt.merge((x - y) + "" + (x + y), 1, Integer::sum);
        }
        Arrays.sort(arr, (a, b) -> a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]);
        int ans = 0;
        int cur = Integer.MIN_VALUE;
        for (int[] e : arr) {
            int l = e[0], r = e[1];
            if (r <= cur) {
                continue;
            }
            cur = r;
            if (cnt.get(l + "" + r) == 1) {
                ++ans;
            }
        }
        return ans;
    }


    // V2




}
