package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimize-the-maximum-adjacent-element-difference/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 3357. Minimize the Maximum Adjacent Element Difference
 * Hard
 *
 * You are given an array of integers nums. Some values in nums are missing and are denoted by -1.
 *
 * You can choose a pair of positive integers (x, y) exactly once and replace each missing element with either x or y.
 *
 * You need to minimize the maximum absolute difference between adjacent elements of nums after replacements.
 *
 * Return the minimum possible difference.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,-1,10,8]
 * Output: 4
 * Explanation:
 * By choosing the pair as (6, 7), nums can be changed to [1, 2, 6, 10, 8].
 * The absolute differences between adjacent elements are:
 * |1 - 2| == 1
 * |2 - 6| == 4
 * |6 - 10| == 4
 * |10 - 8| == 2
 *
 * Example 2:
 *
 * Input: nums = [-1,-1,-1]
 * Output: 0
 * Explanation:
 * By choosing the pair as (4, 4), nums can be changed to [4, 4, 4].
 *
 * Example 3:
 *
 * Input: nums = [-1,10,-1,8]
 * Output: 1
 * Explanation:
 * By choosing the pair as (11, 9), nums can be changed to [11, 10, 9, 8].
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * nums[i] is either -1 or in the range [1, 10^9].
 *
 */
public class MinimizeTheMaximumAdjacentElementDifference {

    // V0
    // IDEA: BINARY SEARCH THE ANSWER d + GREEDY CHECK WITH TWO CANDIDATE VALUES (x, y)
    // differences between two known neighbours are fixed -> their max is a lower bound.
    // What remains are the KNOWN NEIGHBOURS of each run of -1s: whichever of x / y sits next
    // to neighbour v must land in [v - d, v + d]. Let x serve the small neighbours and y the
    // large ones, pushed toward the middle: x = minNeighbour + d, y = maxNeighbour - d. Then:
    //   - every neighbour must be within d of x or of y
    //   - a run of length 1 between a and b must be filled by ONE value close to both
    //   - a longer run between a and b may also use x on one end and y on the other,
    //     provided |x - y| <= d
    // A bigger d is always easier, so binary search the smallest feasible d.
    /**
     * time = O(N log(maxValue))
     * space = O(N)
     */
    public int minDifference(int[] nums) {
        int n = nums.length;

        // fixed-fixed adjacencies are out of our hands
        int base = 0;
        for (int i = 0; i + 1 < n; i++) {
            if (nums[i] != -1 && nums[i + 1] != -1) {
                base = Math.max(base, Math.abs(nums[i] - nums[i + 1]));
            }
        }

        // runs of -1: {left neighbour or -1, right neighbour or -1, run length}
        List<int[]> runs = new ArrayList<>();
        int minNeighbour = Integer.MAX_VALUE;
        int maxNeighbour = Integer.MIN_VALUE;
        int i = 0;
        while (i < n) {
            if (nums[i] != -1) {
                i++;
                continue;
            }
            int j = i;
            while (j < n && nums[j] == -1) {
                j++;
            }
            int leftValue = (i > 0) ? nums[i - 1] : -1;
            int rightValue = (j < n) ? nums[j] : -1;
            runs.add(new int[] {leftValue, rightValue, j - i});
            if (leftValue != -1) {
                minNeighbour = Math.min(minNeighbour, leftValue);
                maxNeighbour = Math.max(maxNeighbour, leftValue);
            }
            if (rightValue != -1) {
                minNeighbour = Math.min(minNeighbour, rightValue);
                maxNeighbour = Math.max(maxNeighbour, rightValue);
            }
            i = j;
        }

        // no missing cell, or every cell missing: only the fixed pairs matter
        if (runs.isEmpty() || minNeighbour == Integer.MAX_VALUE) {
            return base;
        }

        int left = base;
        int right = Math.max(base, maxNeighbour - minNeighbour);
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (canFill(runs, minNeighbour, maxNeighbour, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /** Whether some pair (x, y) keeps every adjacency touching a missing cell within d. */
    private boolean canFill(List<int[]> runs, long minNeighbour, long maxNeighbour, long d) {
        long x = minNeighbour + d;
        long y = maxNeighbour - d;
        for (int[] run : runs) {
            int a = run[0];
            int b = run[1];
            int length = run[2];

            // every known neighbour needs x or y within d
            if (a != -1 && !near(a, x, d) && !near(a, y, d)) {
                return false;
            }
            if (b != -1 && !near(b, x, d) && !near(b, y, d)) {
                return false;
            }

            if (a != -1 && b != -1) {
                // one value serving both ends
                boolean sameValue = (near(a, x, d) && near(b, x, d)) || (near(a, y, d) && near(b, y, d));
                if (length == 1) {
                    if (!sameValue) {
                        return false;
                    }
                } else {
                    // x at one end, y at the other, and x next to y inside the run
                    boolean mixed = ((near(a, x, d) && near(b, y, d)) || (near(a, y, d) && near(b, x, d)))
                            && Math.abs(x - y) <= d;
                    if (!sameValue && !mixed) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** |value - fill| <= d */
    private boolean near(long value, long fill, long d) {
        return Math.abs(value - fill) <= d;
    }
}
