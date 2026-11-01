package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-pair-removal-to-sort-array-ii/description/

import java.util.TreeSet;

/**
 * 3510. Minimum Pair Removal to Sort Array II
 * Hard
 *
 * Given an array nums, you can perform the following operation any number of
 * times:
 *
 * Select the adjacent pair with the minimum sum in nums. If multiple such pairs
 * exist, choose the leftmost one.
 *
 * Replace the pair with their sum.
 *
 * Return the minimum number of operations needed to make the array non-decreasing.
 *
 * An array is said to be non-decreasing if each element is greater than or equal
 * to its previous element (if it exists).
 *
 * Example 1:
 *
 * Input: nums = [5,2,3,1]
 *
 * Output: 2
 *
 * Explanation:
 *
 * The pair (3,1) has the minimum sum of 4. After replacement, nums = [5,2,4].
 *
 * The pair (2,4) has the minimum sum of 6. After replacement, nums = [5,6].
 *
 * The array nums became non-decreasing in two operations.
 *
 * Example 2:
 *
 * Input: nums = [1,2,2]
 *
 * Output: 0
 *
 * Explanation:
 *
 * The array nums is already sorted.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 *
 * -10^9 <= nums[i] <= 10^9
 *
 */
public class MinimumPairRemovalToSortArrayII {

    // V0
    // IDEA: DOUBLY LINKED LIST + ORDERED SET OF (pair sum, left index) + COUNT OF DESCENTS
    //   the process is forced, so the work is running it fast:
    //   - an ordered set gives the leftmost minimum-sum pair in O(log n)
    //     (survivors keep increasing original indexes, so a smaller left index is "more left")
    //   - `descents` counts adjacent pairs with left > right; the array is sorted iff it is 0,
    //     and a merge only changes the three pairs around the merged cell
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minimumPairRemoval(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return 0;
        }

        // values can reach 10^5 * 10^9 after merging, so use long
        long[] value = new long[n];
        int[] prev = new int[n];
        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            value[i] = nums[i];
            prev[i] = i - 1;
            next[i] = (i + 1 < n) ? i + 1 : -1;
        }

        // each entry is {pairSum, leftIndex}; ties broken by the left index
        TreeSet<long[]> pairs = new TreeSet<>((a, b) -> {
            if (a[0] != b[0]) {
                return Long.compare(a[0], b[0]);
            }
            return Long.compare(a[1], b[1]);
        });

        int descents = 0;
        for (int i = 0; i + 1 < n; i++) {
            pairs.add(new long[] {value[i] + value[i + 1], i});
            if (value[i] > value[i + 1]) {
                descents++;
            }
        }

        int operations = 0;
        while (descents > 0) {
            // merge the leftmost minimum-sum pair (left, right)
            long[] smallest = pairs.pollFirst();
            int left = (int) smallest[1];
            int right = next[left];
            int before = prev[left];
            int after = next[right];

            // drop the old pairs touching this one, and the descents they carried
            if (value[left] > value[right]) {
                descents--;
            }
            if (before != -1) {
                pairs.remove(new long[] {value[before] + value[left], before});
                if (value[before] > value[left]) {
                    descents--;
                }
            }
            if (after != -1) {
                pairs.remove(new long[] {value[right] + value[after], right});
                if (value[right] > value[after]) {
                    descents--;
                }
            }

            // merge: left absorbs right, and right is unlinked
            value[left] += value[right];
            next[left] = after;
            if (after != -1) {
                prev[after] = left;
            }
            operations++;

            // add the new pairs around the merged cell
            if (before != -1) {
                pairs.add(new long[] {value[before] + value[left], before});
                if (value[before] > value[left]) {
                    descents++;
                }
            }
            if (after != -1) {
                pairs.add(new long[] {value[left] + value[after], left});
                if (value[left] > value[after]) {
                    descents++;
                }
            }
        }
        return operations;
    }
}
