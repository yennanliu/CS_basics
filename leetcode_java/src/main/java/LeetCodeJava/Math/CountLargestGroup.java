package LeetCodeJava.Math;

// https://leetcode.com/problems/count-largest-group/description/

/**
 * 1399. Count Largest Group
 * Easy
 *
 * You are given an integer n.
 *
 * We need to group the numbers from 1 to n according to the sum of its digits. For example, the numbers 14 and 5 belong to the same group, whereas 13 and 3 belong to different groups.
 *
 * Return the number of groups that have the largest size, i.e. the maximum number of elements.
 *
 * Example 1:
 *
 * Input: n = 13
 * Output: 4
 * Explanation: There are 9 groups in total, they are grouped according sum of its digits of numbers from 1 to 13:
 * [1,10], [2,11], [3,12], [4,13], [5], [6], [7], [8], [9].
 * There are 4 groups with largest size.
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: 2
 * Explanation: There are 2 groups [1], [2] of size 1.
 *
 * Constraints:
 *
 * 1 <= n <= 10^4
 *
 */
public class CountLargestGroup {

    // V0
    // IDEA: COUNT NUMBERS PER DIGIT-SUM BUCKET
    //   n <= 10^4, so a digit sum is at most 9 * 4 = 36 -> a small array is enough.
    //   NOTE: the answer is the NUMBER of largest groups, not their size
    /**
     * time = O(N log N)
     * space = O(1)
     */
    public int countLargestGroup(int n) {
        // groupSize[s] = how many numbers in [1, n] have digit sum s
        int[] groupSize = new int[37];
        int largest = 0;

        for (int i = 1; i <= n; i++) {
            int digitSum = 0;
            int remaining = i;
            while (remaining > 0) {
                digitSum += remaining % 10;
                remaining /= 10;
            }
            groupSize[digitSum]++;
            largest = Math.max(largest, groupSize[digitSum]);
        }

        int groupsOfLargestSize = 0;
        for (int size : groupSize) {
            if (size == largest) {
                groupsOfLargestSize++;
            }
        }
        return groupsOfLargestSize;
    }
}
