package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together/

/**
 *  1151. Minimum Swaps to Group All 1's Together
 *  Medium
 *
 *  Given a binary array data, return the minimum number of swaps required to group
 *  all 1's present in the array together in any place in the array.
 *
 *  Example 1:
 *  Input: data = [1,0,1,0,1]
 *  Output: 1
 *  Explanation: There are 3 ways to group all 1's together:
 *  [1,1,1,0,0] using 1 swap.
 *  [0,1,1,1,0] using 2 swaps.
 *  [0,0,1,1,1] using 1 swap.
 *  The minimum is 1.
 *
 *  Example 2:
 *  Input: data = [0,0,0,1,0]
 *  Output: 0
 *
 *  Example 3:
 *  Input: data = [1,0,1,0,1,0,0,1,1,0,1]
 *  Output: 3
 *
 *  Constraints:
 *   1 <= data.length <= 10^5
 *   data[i] is either 0 or 1.
 */
public class MinimumSwapsToGroupAll1STogether {

    // V0
    // IDEA: fixed-size sliding window (size = total ones); answer = ones - max ones in window
    /**
     * time = O(n)
     * space = O(1)
     */
    public int minSwaps(int[] data) {
        int ones = 0;
        for (int x : data) {
            ones += x;
        }
        if (ones <= 1) {
            return 0;
        }
        int cntOne = 0;
        int maxOne = 0;
        int left = 0;
        for (int right = 0; right < data.length; right++) {
            cntOne += data[right];
            // keep window length <= ones
            if (right - left + 1 > ones) {
                cntOne -= data[left];
                left++;
            }
            maxOne = Math.max(maxOne, cntOne);
        }
        return ones - maxOne;
    }
}
