package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/missing-element-in-sorted-array/description/
// https://leetcode.ca/all/1060.html

import java.util.HashSet;
import java.util.Set;

/**
 * 1060. Missing Element in Sorted Array
 * Given a sorted array A of unique numbers, find the K-th missing number starting from the leftmost number of the array.
 *
 *
 *
 * Example 1:
 *
 * Input: A = [4,7,9,10], K = 1
 * Output: 5
 * Explanation:
 * The first missing number is 5.
 * Example 2:
 *
 * Input: A = [4,7,9,10], K = 3
 * Output: 8
 * Explanation:
 * The missing numbers are [5,6,8,...], hence the third missing number is 8.
 * Example 3:
 *
 * Input: A = [1,2,4], K = 3
 * Output: 6
 * Explanation:
 * The missing numbers are [3,5,6,7,...], hence the third missing number is 6.
 *
 *
 * Note:
 *
 * 1 <= A.length <= 50000
 * 1 <= A[i] <= 1e7
 * 1 <= K <= 1e8
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 */
public class MissingElementInSortedArray {

    // V0
//    public int missingElement(int[] nums, int k) {
//
//    }

    // V0-1
    // IDEA: BRUTE FORCE (gpt)
    public int missingElement_0_1(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return -1;  // no numbers given
        }

        Set<Integer> set = new HashSet<>();
        for (int x : nums) {
            set.add(x);
        }

        int cnt = 0;
        int val = nums[0];
        while (true) {
            if (!set.contains(val)) {
                cnt++;
                if (cnt == k) {
                    return val;
                }
            }
            val++;
        }
    }

    // V0-2
    // IDEA: BINARY SEARCH (gpt)
    public int missingElement_0_2(int[] nums, int k) {
        int n = nums.length;

        // if kth missing number is beyond the last element
        if (k > missing_0_3(nums, n - 1)) {
            return nums[n - 1] + (k - missing_0_3(nums, n - 1));
        }

        int left = 0, right = n - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (missing_0_3(nums, mid) < k) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        // kth missing is between nums[left-1] and nums[left]
        return nums[left - 1] + (k - missing_0_3(nums, left - 1));
    }

    // helper function
    private int missing_0_3(int[] nums, int i) {
        return nums[i] - nums[0] - i;
    }

    // V0-3
    // IDEA: ARRAY OP (fixed by gpt)
    // TODO: validate
    public int missingElement_0_3(int[] nums, int k) {
        for (int i = 1; i < nums.length; i++) {
            int missingBetween = nums[i] - nums[i - 1] - 1;

            if (missingBetween >= k) {
                return nums[i - 1] + k;
            }

            k -= missingBetween;
        }

        // If k-th missing is beyond the last number
        return nums[nums.length - 1] + k;
    }

    // V1
    //https://leetcode.ca/2018-10-25-1060-Missing-Element-in-Sorted-Array/
    public int missingElement_1(int[] nums, int k) {
        int n = nums.length;
        if (k > missing(nums, n - 1)) {
            return nums[n - 1] + k - missing(nums, n - 1);
        }
        int l = 0, r = n - 1;
        while (l < r) {
            int mid = (l + r) >> 1;
            if (missing(nums, mid) >= k) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return nums[l - 1] + k - missing(nums, l - 1);
    }

    private int missing(int[] nums, int i) {
        return nums[i] - nums[0] - i;
    }

    // V2

}
