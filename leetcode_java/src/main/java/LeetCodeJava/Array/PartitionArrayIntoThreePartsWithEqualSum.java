package LeetCodeJava.Array;

// https://leetcode.com/problems/partition-array-into-three-parts-with-equal-sum/description/

/**
 * 1013. Partition Array Into Three Parts With Equal Sum
 * Easy
 *
 * Given an array of integers arr, return true if we can partition the array into three non-empty parts with equal sums.
 *
 * Formally, we can partition the array if we can find indexes i + 1 < j with (arr[0] + arr[1] + ... + arr[i] == arr[i + 1] + arr[i + 2] + ... + arr[j - 1] == arr[j] + arr[j + 1] + ... + arr[arr.length - 1])
 *
 * Example 1:
 *
 * Input: arr = [0,2,1,-6,6,-7,9,1,2,0,1]
 * Output: true
 * Explanation: 0 + 2 + 1 = -6 + 6 - 7 + 9 + 1 = 2 + 0 + 1
 *
 * Example 2:
 *
 * Input: arr = [0,2,1,-6,6,7,9,-1,2,0,1]
 * Output: false
 *
 * Example 3:
 *
 * Input: arr = [3,3,6,5,-2,2,5,1,-9,4]
 * Output: true
 * Explanation: 3 + 3 = 6 = 5 - 2 + 2 + 5 + 1 - 9 + 4
 *
 * Constraints:
 *
 * 3 <= arr.length <= 5 * 10^4
 * -10^4 <= arr[i] <= 10^4
 *
 */
public class PartitionArrayIntoThreePartsWithEqualSum {

    // V0
    // IDEA: PREFIX SUM + GREEDY (close a part every time the running sum hits total / 3)
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean canThreePartsEqualSum(int[] arr) {
        if (arr == null || arr.length < 3) {
            return false;
        }

        long total = 0;
        for (int value : arr) {
            total += value;
        }
        // three equal parts are impossible unless the total splits evenly
        if (total % 3 != 0) {
            return false;
        }
        long target = total / 3;

        long runningSum = 0;
        int closedParts = 0;
        for (int value : arr) {
            runningSum += value;
            // greedily close a part as early as possible; it is non-empty because
            // we only close right after adding an element
            if (runningSum == target) {
                closedParts++;
                runningSum = 0;
            }
        }
        // >= 3 (not == 3): any leftover parts sum to 0 and can be glued onto the
        // third part, e.g. [0,0,0,0]
        return closedParts >= 3;
    }
}
