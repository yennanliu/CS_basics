package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/distribute-elements-into-two-arrays-ii/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 3072. Distribute Elements Into Two Arrays II
 * Hard
 *
 * You are given a 1-indexed array of integers nums of length n.
 *
 * We define a function greaterCount such that greaterCount(arr, val) returns the number of elements in arr that are strictly greater than val.
 *
 * You need to distribute all the elements of nums between two arrays arr1 and arr2 using n operations. In the first operation, append nums[1] to arr1. In the second operation, append nums[2] to arr2. Afterwards, in the ith operation:
 *
 * If greaterCount(arr1, nums[i]) > greaterCount(arr2, nums[i]), append nums[i] to arr1.
 * If greaterCount(arr1, nums[i]) < greaterCount(arr2, nums[i]), append nums[i] to arr2.
 * If greaterCount(arr1, nums[i]) == greaterCount(arr2, nums[i]), append nums[i] to the array with a lesser number of elements.
 * If there is still a tie, append nums[i] to arr1.
 *
 * The array result is formed by concatenating the arrays arr1 and arr2. For example, if arr1 == [1,2,3] and arr2 == [4,5,6], then result = [1,2,3,4,5,6].
 *
 * Return the integer array result.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,1,3,3]
 * Output: [2,3,1,3]
 * Explanation: After the first 2 operations, arr1 = [2] and arr2 = [1].
 * In the 3rd operation, the number of elements greater than 3 is zero in both arrays. Also, the lengths are equal, hence, append nums[3] to arr1.
 * In the 4th operation, the number of elements greater than 3 is zero in both arrays. As the length of arr2 is lesser, hence, append nums[4] to arr2.
 * After 4 operations, arr1 = [2,3] and arr2 = [1,3].
 * Hence, the array result formed by concatenation is [2,3,1,3].
 *
 * Example 2:
 *
 * Input: nums = [5,14,3,1,2]
 * Output: [5,3,1,2,14]
 * Explanation: After the first 2 operations, arr1 = [5] and arr2 = [14].
 * In the 3rd operation, the number of elements greater than 3 is one in both arrays. Also, the lengths are equal, hence, append nums[3] to arr1.
 * In the 4th operation, the number of elements greater than 1 is greater in arr1 than arr2 (2 > 1). Hence, append nums[4] to arr1.
 * In the 5th operation, the number of elements greater than 2 is greater in arr1 than arr2 (2 > 1). Hence, append nums[5] to arr1.
 * After 5 operations, arr1 = [5,3,1,2] and arr2 = [14].
 * Hence, the array result formed by concatenation is [5,3,1,2,14].
 *
 * Example 3:
 *
 * Input: nums = [3,3,3,3]
 * Output: [3,3,3,3]
 * Explanation: At the end of 4 operations, arr1 = [3,3] and arr2 = [3,3].
 * Hence, the array result formed by concatenation is [3,3,3,3].
 *
 *
 * Constraints:
 *
 * 3 <= n <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class DistributeElementsIntoTwoArrays2 {

    // V0
    // IDEA: TWO FENWICK TREES OVER COORDINATE-COMPRESSED VALUES
    // each step needs greaterCount(arr, v) = size(arr) - (# entries <= v). A linear scan per
    // step is O(n^2); a Fenwick tree of counts per array answers it in O(log n). Values reach
    // 1e9 but there are only n of them, so compress them to ranks 1..n first.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int[] resultArray(int[] nums) {
        int n = nums.length;

        // coordinate compression: value -> rank in 1..distinct
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        Map<Integer, Integer> rankOf = new HashMap<>();
        int distinct = 0;
        for (int value : sorted) {
            if (!rankOf.containsKey(value)) {
                distinct++;
                rankOf.put(value, distinct);
            }
        }

        int[] tree1 = new int[distinct + 1];
        int[] tree2 = new int[distinct + 1];
        int[] arr1 = new int[n];
        int[] arr2 = new int[n];
        int size1 = 0;
        int size2 = 0;

        arr1[size1++] = nums[0];
        add(tree1, rankOf.get(nums[0]));
        arr2[size2++] = nums[1];
        add(tree2, rankOf.get(nums[1]));

        for (int i = 2; i < n; i++) {
            int rank = rankOf.get(nums[i]);
            int greater1 = size1 - countAtMost(tree1, rank);
            int greater2 = size2 - countAtMost(tree2, rank);
            // more greater elements wins; on a tie the shorter array; on a full tie arr1
            boolean goesToFirst;
            if (greater1 != greater2) {
                goesToFirst = greater1 > greater2;
            } else {
                goesToFirst = size1 <= size2;
            }
            if (goesToFirst) {
                arr1[size1++] = nums[i];
                add(tree1, rank);
            } else {
                arr2[size2++] = nums[i];
                add(tree2, rank);
            }
        }

        // result = arr1 followed by arr2
        int[] result = new int[n];
        for (int i = 0; i < size1; i++) {
            result[i] = arr1[i];
        }
        for (int i = 0; i < size2; i++) {
            result[size1 + i] = arr2[i];
        }
        return result;
    }

    /** Fenwick: record one more entry at `rank`. i & -i is the lowest set bit of i. */
    private void add(int[] tree, int rank) {
        for (int i = rank; i < tree.length; i += i & -i) {
            tree[i]++;
        }
    }

    /** Fenwick: number of entries with rank <= `rank`. */
    private int countAtMost(int[] tree, int rank) {
        int count = 0;
        for (int i = rank; i > 0; i -= i & -i) {
            count += tree[i];
        }
        return count;
    }
}
