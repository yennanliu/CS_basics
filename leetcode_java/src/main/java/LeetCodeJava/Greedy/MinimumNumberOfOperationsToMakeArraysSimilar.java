package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-operations-to-make-arrays-similar/description/

import java.util.*;

/**
 * 2449. Minimum Number of Operations to Make Arrays Similar
 * Hard
 *
 * You are given two positive integer arrays nums and target, of the same length.
 *
 * In one operation, you can choose any two distinct indices i and j where 0 <= i, j < nums.length and:
 *
 * set nums[i] = nums[i] + 2 and
 * set nums[j] = nums[j] - 2.
 *
 * Two arrays are considered to be similar if the frequency of each element is the same.
 *
 * Return the minimum number of operations required to make nums similar to target. The test cases are generated such that nums can always be similar to target.
 *
 *
 * Example 1:
 *
 * Input: nums = [8,12,6], target = [2,14,10]
 * Output: 2
 * Explanation: It is possible to make nums similar to target in two operations:
 * - Choose i = 0 and j = 2, nums = [10,12,4].
 * - Choose i = 1 and j = 2, nums = [10,14,2].
 * It can be shown that 2 is the minimum number of operations needed.
 *
 * Example 2:
 *
 * Input: nums = [1,2,5], target = [4,1,3]
 * Output: 1
 * Explanation: We can make nums similar to target in one operation:
 * - Choose i = 1 and j = 2, nums = [1,4,3].
 *
 * Example 3:
 *
 * Input: nums = [1,1,1,1,1], target = [1,1,1,1,1]
 * Output: 0
 * Explanation: The array nums is already similiar to target.
 *
 *
 * Constraints:
 *
 * n == nums.length == target.length
 * 1 <= n <= 10^5
 * 1 <= nums[i], target[i] <= 10^6
 * It is possible to make nums similar to target.
 *
 */
public class MinimumNumberOfOperationsToMakeArraysSimilar {

    // V0
    // IDEA: SORT + MATCH WITHIN EACH PARITY CLASS
    //       +2 / -2 keeps parity, so odds pair with odds and evens with evens; sorted
    //       in-order pairing minimises sum |a - b|. One operation removes 4 from that sum.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long makeSimilar(int[] nums, int[] target) {
        List<Integer> numsEven = new ArrayList<>();
        List<Integer> numsOdd = new ArrayList<>();
        List<Integer> targetEven = new ArrayList<>();
        List<Integer> targetOdd = new ArrayList<>();
        splitByParity(nums, numsEven, numsOdd);
        splitByParity(target, targetEven, targetOdd);

        long totalDiff = 0;
        totalDiff += pairedDifference(numsEven, targetEven);
        totalDiff += pairedDifference(numsOdd, targetOdd);
        // each operation fixes 2 on one element and 2 on another
        return totalDiff / 4;
    }

    /** Puts each value into the even or odd list. */
    private void splitByParity(int[] values, List<Integer> even, List<Integer> odd) {
        for (int value : values) {
            if (value % 2 == 0) {
                even.add(value);
            } else {
                odd.add(value);
            }
        }
    }

    /** Sorts both lists and sums |a[i] - b[i]| over the in-order pairing. */
    private long pairedDifference(List<Integer> a, List<Integer> b) {
        Collections.sort(a);
        Collections.sort(b);
        long sum = 0;
        for (int i = 0; i < a.size(); i++) {
            sum += Math.abs(a.get(i) - b.get(i));
        }
        return sum;
    }
}
