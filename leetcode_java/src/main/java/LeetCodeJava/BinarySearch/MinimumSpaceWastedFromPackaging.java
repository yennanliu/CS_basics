package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-space-wasted-from-packaging/

import java.util.Arrays;

/**
 *  1889. Minimum Space Wasted From Packaging
 *  Hard
 *
 *  You have n packages that you are trying to place in boxes, one package in
 *  each box. There are m suppliers that each produce boxes of different sizes
 *  (with infinite supply). A package can be placed in a box if the size of the
 *  package is less than or equal to the size of the box.
 *
 *  You want to choose a single supplier and use boxes from them such that the
 *  total wasted space is minimized. For each package in a box, the space wasted
 *  is (size of the box - size of the package).
 *
 *  Return the minimum total wasted space by choosing the box supplier optimally,
 *  or -1 if it is impossible to fit all the packages inside boxes.
 *  Since the answer may be large, return it modulo 10^9 + 7.
 *
 *  Example 1:
 *
 *  Input: packages = [2,3,5], boxes = [[4,8],[2,8]]
 *  Output: 6
 *
 *  Example 2:
 *
 *  Input: packages = [2,3,5], boxes = [[1,4],[2,3],[3,4]]
 *  Output: -1
 *
 *  Example 3:
 *
 *  Input: packages = [3,5,8,10,11,12], boxes = [[12],[11,9],[10,5,14]]
 *  Output: 9
 *
 *  Constraints:
 *
 *  n == packages.length
 *  m == boxes.length
 *  1 <= n <= 10^5
 *  1 <= m <= 10^5
 *  1 <= packages[i] <= 10^5
 *  1 <= boxes[j].length <= 10^5
 *  1 <= boxes[j][k] <= 10^5
 *  sum(boxes[j].length) <= 10^5
 *  The elements in boxes[j] are distinct.
 */
public class MinimumSpaceWastedFromPackaging {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: sort packages; per supplier sort its boxes and binary search how many
    //       packages each box can cover ->
    //       waste = sum(box * covered_cnt) - sum(all packages)
    /**
     * time = O(n log n + sum(|boxes[j]|) * log n)
     * space = O(1) (excluding sorting)
     */
    public int minWastedSpace(int[] packages, int[][] boxes) {
        Arrays.sort(packages);
        int n = packages.length;
        long totalPkg = 0;
        for (int p : packages) {
            totalPkg += p;
        }
        int maxPkg = packages[n - 1];

        long best = Long.MAX_VALUE;
        for (int[] supplier : boxes) {
            Arrays.sort(supplier);
            // NOTE !!! this supplier cannot hold the biggest package -> skip
            if (supplier[supplier.length - 1] < maxPkg) {
                continue;
            }
            long boxSum = 0;
            int covered = 0; // how many packages already packed
            for (int box : supplier) {
                if (box < packages[covered]) {
                    continue;
                }
                // first idx with packages[idx] > box
                int idx = upperBound(packages, box);
                boxSum += (long) box * (idx - covered);
                covered = idx;
                if (covered == n) {
                    break;
                }
            }
            best = Math.min(best, boxSum - totalPkg);
        }
        if (best == Long.MAX_VALUE) {
            return -1;
        }
        return (int) (best % MOD);
    }

    // first index i such that nums[i] > val
    private int upperBound(int[] nums, int val) {
        int l = 0;
        int r = nums.length;
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] > val) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }
}
