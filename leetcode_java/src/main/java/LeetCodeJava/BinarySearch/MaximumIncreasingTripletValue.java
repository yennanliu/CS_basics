package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/maximum-increasing-triplet-value/description/

import java.util.Arrays;
import java.util.TreeSet;

/**
 * 3073. Maximum Increasing Triplet Value
 * Medium
 *
 * 🔒 (premium)
 *
 * Given an array nums, return the maximum value of a triplet (i, j, k) such that i < j < k and nums[i] < nums[j] < nums[k].
 *
 * The value of a triplet (i, j, k) is nums[i] - nums[j] + nums[k].
 *
 *
 * Example 1:
 *
 * Input: nums = [5,6,9]
 * Output: 8
 * Explanation: We only have one choice for an increasing triplet and that is choosing all three elements. The value of this triplet would be 5 - 6 + 9 = 8.
 *
 * Example 2:
 *
 * Input: nums = [1,5,3,6]
 * Output: 4
 * Explanation: There are only two increasing triplets:
 * (0, 1, 3): The value of this triplet is nums[0] - nums[1] + nums[3] = 1 - 5 + 6 = 2.
 * (0, 2, 3): The value of this triplet is nums[0] - nums[2] + nums[3] = 1 - 3 + 6 = 4.
 * Thus the answer would be 4.
 *
 *
 * Constraints:
 *
 * 3 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * The input is generated such that at least one triplet meets the given condition.
 *
 */
public class MaximumIncreasingTripletValue {

    // V0
    // IDEA: FIX THE MIDDLE j; BEST LEFT FROM AN ORDERED SET, BEST RIGHT FROM A SUFFIX MAX
    // value = nums[i] - nums[j] + nums[k], and the choices of i and k are independent:
    //   left  : the LARGEST nums[i] < nums[j] with i < j  -> TreeSet.lower() over the prefix
    //   right : the LARGEST nums[k] with k > j            -> suffix maximum; valid iff > nums[j]
    // (if the largest value to the right is not > nums[j], nothing to the right is.)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maximumTripletValue(int[] nums) {
        int n = nums.length;

        // suffixMax[t] = max of nums[t..n-1]
        int[] suffixMax = new int[n + 1];
        for (int t = n - 1; t >= 0; t--) {
            suffixMax[t] = Math.max(suffixMax[t + 1], nums[t]);
        }

        TreeSet<Integer> seen = new TreeSet<>();
        int best = Integer.MIN_VALUE;
        for (int j = 0; j < n; j++) {
            Integer left = seen.lower(nums[j]);
            int right = suffixMax[j + 1];
            if (left != null && right > nums[j]) {
                best = Math.max(best, left - nums[j] + right);
            }
            seen.add(nums[j]);
        }
        return best;
    }

    // V1
    // IDEA: SAME SPLIT, LEFT SIDE VIA A PREFIX-MAX FENWICK TREE OVER COMPRESSED VALUES
    // (the Python V0's shape: "max value with rank < rank(nums[j]) seen so far")
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maximumTripletValue_1(int[] nums) {
        int n = nums.length;

        // coordinate compression: sorted distinct values, rank = position + 1
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int size = 0;
        for (int i = 0; i < n; i++) {
            if (i == 0 || sorted[i] != sorted[i - 1]) {
                sorted[size++] = sorted[i];
            }
        }
        int[] distinctSorted = Arrays.copyOf(sorted, size);
        // tree[i] holds a max over a rank range; 0 means "nothing yet" (all nums >= 1)
        int[] tree = new int[size + 1];

        int[] suffixMax = new int[n + 1];
        for (int t = n - 1; t >= 0; t--) {
            suffixMax[t] = Math.max(suffixMax[t + 1], nums[t]);
        }

        int best = Integer.MIN_VALUE;
        for (int j = 0; j < n; j++) {
            // 1-based rank of nums[j]
            int rank = Arrays.binarySearch(distinctSorted, nums[j]) + 1;
            int left = prefixMax(tree, rank - 1);
            int right = suffixMax[j + 1];
            if (left > 0 && right > nums[j]) {
                best = Math.max(best, left - nums[j] + right);
            }
            updateMax(tree, rank, nums[j]);
        }
        return best;
    }

    /** Fenwick: raise the max at `rank`. i & -i is the lowest set bit of i. */
    private void updateMax(int[] tree, int rank, int value) {
        for (int i = rank; i < tree.length; i += i & -i) {
            tree[i] = Math.max(tree[i], value);
        }
    }

    /** Fenwick: max over ranks 1..rank, 0 when empty. */
    private int prefixMax(int[] tree, int rank) {
        int best = 0;
        for (int i = rank; i > 0; i -= i & -i) {
            best = Math.max(best, tree[i]);
        }
        return best;
    }
}
