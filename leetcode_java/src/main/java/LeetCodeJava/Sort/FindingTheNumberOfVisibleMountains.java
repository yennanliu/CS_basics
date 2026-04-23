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
//    public int visibleMountains(int[][] peaks) {
//
//    }

    // V0-1
    // IDEA: MATH (gpt)
    // TODO: validate
    /**  Core idea:
     *
     * 1.         *
     *   -> A mountain is `visible` **only if
     *       it `pushes the right boundary further `
     *       than anything seen so far**.
     *
     *
     *  2. ✅ Steps (correct approach (key idea)
     *
     * -> Convert each peak (x, y) into an interval:
     *
     *     left = x - y
     *     right = x + y
     *
     * Then:
     *
     *  1. Sort by:
     *    left ASC
     *    right DESC (important!)
     *
     * 2. Iterate and track the maximum right boundary seen so far
     *
     * 3. A mountain is visible if its right is strictly greater
     *    than all previous ones
     *
     * 4. Handle duplicates: identical intervals → not visible
     *
     */
    public int visibleMountains_0_1(int[][] peaks) {
        int n = peaks.length;
        int[][] intervals = new int[n][2];

        // build intervals
        for (int i = 0; i < n; i++) {
            int x = peaks[i][0];
            int y = peaks[i][1];
            intervals[i][0] = x - y; // left
            intervals[i][1] = x + y; // right
        }

        // sort: left ASC, right DESC
        Arrays.sort(intervals, (a, b) -> {
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            return Integer.compare(b[1], a[1]);
        });

        int visible = 0;
        int maxRight = Integer.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            /** NOTE !!!
             *
             *   if `100 % overlap` (e.g. duplicates)
             *   -> totally SKIP it.
             */
            // skip duplicates
            if (i > 0 &&
                    intervals[i][0] == intervals[i - 1][0] &&
                    intervals[i][1] == intervals[i - 1][1]) {
                continue;
            }

            /** NOTE !!! why `intervals[i][1] > maxRight` ??
             *
             *
             * That line is the core of the visibility
             * check—it encodes the **containment rule**
             * in a single comparison.
             *
             * ---
             *
             * ### 🔑 What `maxRight` represents
             *
             * After sorting by:
             *
             * * `left ASC`
             * * `right DESC`
             *
             * you are scanning mountains from **left to right**, and:
             *
             * > `maxRight` =
             *   the `farthest` right boundary of
             *   *any mountain you've already seen*
             *
             * ---
             *
             * ### 🧠 Why `intervals[i][1] > maxRight` works
             *
             * Let current interval be:
             *
             * ```java
             * [left_i, right_i]
             * ```
             *
             * There are only two possibilities:
             *
             * ---
             *
             * #### ✅ Case 1: `right_i > maxRight` → **VISIBLE**
             *
             * This means:
             *
             * > This mountain extends farther right than all previous ones.
             *
             * So:
             *
             * * No earlier mountain can fully cover it
             * * Therefore, it is **visible**
             *
             * ---
             *
             * #### ❌ Case 2: `right_i <= maxRight` → **NOT visible**
             *
             * This means:
             *
             * > Some previous mountain already reaches at least this far
             *
             * Because of sorting:
             *
             * * That previous mountain also has `left <= left_i`
             * * So it **completely covers** the current one
             *
             * 👉 Hence, current mountain is hidden
             *
             * ---
             *
             * ### 📌 Why sorting matters (very important)
             *
             * Sorting by:
             *
             * ```text
             * left ASC, right DESC
             * ```
             *
             * ensures:
             *
             * * Bigger mountains (wider intervals) come **first**
             * * So when a smaller one appears later, it can be detected as covered
             *
             * ---
             *
             * ### 🔍 Example
             *
             * Input:
             *
             * ```
             * [2,2] → [0,4]
             * [3,1] → [2,4]
             * [4,1] → [3,5]
             * ```
             *
             * After sorting:
             *
             * ```
             * [0,4]
             * [2,4]
             * [3,5]
             * ```
             *
             * Now iterate:
             *
             * | interval | maxRight | condition | result    |
             * | -------- | -------- | --------- | --------- |
             * | [0,4]    | -∞       | 4 > -∞    | visible ✅ |
             * | [2,4]    | 4        | 4 > 4 ❌   | hidden ❌  |
             * | [3,5]    | 4        | 5 > 4 ✅   | visible ✅ |
             *
             * ---
             *
             * ### 💡 Intuition in one sentence
             *
             * > A mountain is `visible` **only if
             *   it `pushes the right boundary further `
             *   than anything seen so far**.
             *
             * ---
             *
             * If you're wondering why we use `>` instead of `>=`,
             * that ties into **duplicate handling**—happy to break
             * that down next if you want.
             *
             *
             */
            if (intervals[i][1] > maxRight) {
                visible++;
                maxRight = intervals[i][1];
            }
        }

        return visible;
    }

    // V0-2
    // IDEA: GEMINI
    // TODO: validate
    /** NOTE !!
     *
     *
     *  Core idea:
     *
     *  Visibility Logic:
     *
     *    As you iterate,
     *    track the maxRight reached so far.
     *    If a mountain's `right boundary is <= maxRight`,
     *       -> it's hidden.
     */
    public int visibleMountains_0_2(int[][] peaks) {
        int n = peaks.length;
        // 1. Convert to intervals: [start, end]
        // We also need to handle identical mountains
        int[][] intervals = new int[n][2];
        Map<String, Integer> counts = new HashMap<>();

        for (int i = 0; i < n; i++) {
            intervals[i][0] = peaks[i][0] - peaks[i][1];
            intervals[i][1] = peaks[i][0] + peaks[i][1];
            String key = peaks[i][0] + "," + peaks[i][1];
            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }

        // 2. Sort: Start ASC, End DESC
        Arrays.sort(intervals, (a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0];
            return b[1] - a[1];
        });

        int visibleCount = 0;
        int maxRight = Integer.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            // A mountain is hidden if its right boundary is <= maxRight seen so far
            if (intervals[i][1] > maxRight) {
                maxRight = intervals[i][1];

                // Check if this mountain is hidden by its own twin (identical peak)
                // We find the original peak coords using start/end:
                // x = (start + end) / 2, y = (end - start) / 2
                int x = (intervals[i][0] + intervals[i][1]) / 2;
                int y = (intervals[i][1] - intervals[i][0]) / 2;

                if (counts.get(x + "," + y) == 1) {
                    // Only visible if there is exactly one mountain at this peak
                    visibleCount++;
                }
            }
        }

        return visibleCount;
    }



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
