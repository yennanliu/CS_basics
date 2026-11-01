package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/maximum-white-tiles-covered-by-a-carpet/description/

import java.util.Arrays;

/**
 * 2271. Maximum White Tiles Covered by a Carpet
 * Medium
 *
 * You are given a 2D integer array tiles where tiles[i] = [li, ri] represents that every tile j in the range li <= j <= ri is colored white.
 *
 * You are also given an integer carpetLen, the length of a single carpet that can be placed anywhere.
 *
 * Return the maximum number of white tiles that can be covered by the carpet.
 *
 *
 * Example 1:
 *
 * Input: tiles = [[1,5],[10,11],[12,18],[20,25],[30,32]], carpetLen = 10
 * Output: 9
 * Explanation: Place the carpet starting on tile 10.
 * It covers 9 white tiles, so we return 9.
 * Note that there may be other places where the carpet covers 9 white tiles.
 * It can be shown that the carpet cannot cover more than 9 white tiles.
 *
 * Example 2:
 *
 * Input: tiles = [[10,11],[1,1]], carpetLen = 2
 * Output: 2
 * Explanation: Place the carpet starting on tile 10.
 * It covers 2 white tiles, so we return 2.
 *
 *
 * Constraints:
 *
 * 1 <= tiles.length <= 5 * 10^4
 * tiles[i].length == 2
 * 1 <= li <= ri <= 10^9
 * 1 <= carpetLen <= 10^9
 * The tiles are non-overlapping.
 *
 */
public class MaximumWhiteTilesCoveredByACarpet {

    // V0
    // IDEA: SORT + SLIDING WINDOW (an optimal carpet starts on some tile's left end)
    //       sliding a carpet right never gains until its left edge reaches the
    //       start of an interval, so only starts = tiles[i][0] are tried.
    //       window [i, j) holds the intervals FULLY inside the carpet; the first
    //       interval that sticks out (tiles[j]) may still be partly covered.
    //       both pointers only move forward -> O(n) after the sort.
    /**
     * time = O(N log N)
     * space = O(1)
     */
    public int maximumWhiteTiles(int[][] tiles, int carpetLen) {
        // sort intervals by their left end
        Arrays.sort(tiles, (a, b) -> Integer.compare(a[0], b[0]));
        int n = tiles.length;

        int best = 0;
        // white cells of the intervals tiles[i .. j-1], all fully under the carpet
        int fullyCovered = 0;
        int j = 0;

        for (int i = 0; i < n; i++) {
            int start = tiles[i][0];
            // the carpet covers [start, start + carpetLen - 1]
            long carpetEnd = (long) start + carpetLen - 1;

            // extend the window while interval j fits entirely under the carpet
            while (j < n && tiles[j][1] <= carpetEnd) {
                fullyCovered += tiles[j][1] - tiles[j][0] + 1;
                j++;
            }

            int covered = fullyCovered;
            // interval j sticks out on the right, but its left part may be covered
            if (j < n && tiles[j][0] <= carpetEnd) {
                covered += (int) (carpetEnd - tiles[j][0] + 1);
            }
            best = Math.max(best, covered);

            // the next start lies past interval i, so drop interval i from the window
            if (j > i) {
                // interval i was fully covered, so it is inside the window
                fullyCovered -= tiles[i][1] - tiles[i][0] + 1;
            } else {
                // interval i is longer than the carpet and never entered the window;
                // the window stays empty and restarts at the next interval
                j = i + 1;
            }
        }
        return best;
    }

    // V1
    // IDEA: SORT + PREFIX SUM + BINARY SEARCH FOR THE LAST TOUCHED INTERVAL
    //       same candidate starts as V0, but each start is computed independently:
    //       binary search the last interval whose left end is under the carpet,
    //       take the prefix sum of lengths up to it, then cut off the overhang.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maximumWhiteTiles_1(int[][] tiles, int carpetLen) {
        Arrays.sort(tiles, (a, b) -> Integer.compare(a[0], b[0]));
        int n = tiles.length;

        // prefix[i] = total white cells of tiles[0 .. i-1]
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + (tiles[i][1] - tiles[i][0] + 1);
        }

        long best = 0;
        for (int i = 0; i < n; i++) {
            long carpetEnd = (long) tiles[i][0] + carpetLen - 1;

            // last index whose left end is <= carpetEnd
            int last = lastStartAtOrBefore(tiles, carpetEnd);

            long covered = prefix[last + 1] - prefix[i];
            // the last touched interval may hang over the carpet's right edge
            if (tiles[last][1] > carpetEnd) {
                covered -= tiles[last][1] - carpetEnd;
            }
            best = Math.max(best, covered);
        }
        return (int) best;
    }

    /** Largest index whose interval starts at or before limit (tiles sorted by start). */
    private int lastStartAtOrBefore(int[][] tiles, long limit) {
        int left = 0;
        int right = tiles.length - 1;
        int answer = 0;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (tiles[mid][0] <= limit) {
                answer = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return answer;
    }
}
