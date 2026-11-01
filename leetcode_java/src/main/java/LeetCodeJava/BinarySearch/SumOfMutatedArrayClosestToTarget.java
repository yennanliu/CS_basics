package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/sum-of-mutated-array-closest-to-target/description/

/**
 * 1300. Sum of Mutated Array Closest to Target
 * Medium
 *
 * Given an integer array arr and a target value target, return the integer value such that when
 * we change all the integers larger than value in the given array to be equal to value, the sum
 * of the array gets as close as possible (in absolute difference) to target.
 *
 * In case of a tie, return the minimum such integer.
 *
 * Notice that the answer is not neccesarilly a number from arr.
 *
 *
 * Example 1:
 *
 * Input: arr = [4,9,3], target = 10
 * Output: 3
 * Explanation: When using 3 arr converts to [3, 3, 3] which sums 9 and that's the optimal answer.
 *
 * Example 2:
 *
 * Input: arr = [2,3,5], target = 10
 * Output: 5
 *
 * Example 3:
 *
 * Input: arr = [60864,25176,27249,21296,20204], target = 56803
 * Output: 11361
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 10^4
 * 1 <= arr[i], target <= 10^5
 *
 */
public class SumOfMutatedArrayClosestToTarget {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER
    //   S(v) = sum(min(x, v)) is non-decreasing in v, so binary search the smallest v in
    //   [0, max(arr)] with S(v) >= target. the best value is then v or v - 1 (the last one
    //   still below target): anything further away is worse by monotonicity.
    //   ties go to the smaller value, hence the strict '<' when comparing v against v - 1.
    //   if even S(max(arr)) <= target, capping cannot help and max(arr) is the answer.
    /**
     * time = O(N log M), M = max(arr)
     * space = O(1)
     */
    public int findBestValue(int[] arr, int target) {
        int maxValue = 0;
        for (int x : arr) {
            maxValue = Math.max(maxValue, x);
        }
        // no cap reaches beyond the untouched sum
        if (mutatedSum(arr, maxValue) <= target) {
            return maxValue;
        }

        // lower bound: smallest value whose mutated sum reaches target
        int left = 0;
        int right = maxValue;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (mutatedSum(arr, mid) >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        if (left == 0) {
            return 0;
        }
        long diffAtLeft = Math.abs(mutatedSum(arr, left) - target);
        long diffBelow = Math.abs(mutatedSum(arr, left - 1) - target);
        // strict '<' keeps the smaller value on a tie
        if (diffAtLeft < diffBelow) {
            return left;
        }
        return left - 1;
    }

    /** Sum of arr after every element larger than cap is replaced by cap. */
    private long mutatedSum(int[] arr, int cap) {
        long sum = 0;
        for (int x : arr) {
            sum += Math.min(x, cap);
        }
        return sum;
    }
}
