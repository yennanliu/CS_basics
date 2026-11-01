package LeetCodeJava.Array;

// https://leetcode.com/problems/number-of-students-doing-homework-at-a-given-time/description/

/**
 * 1450. Number of Students Doing Homework at a Given Time
 * Easy
 *
 * Given two integer arrays startTime and endTime and given an integer queryTime.
 *
 * The ith student started doing their homework at the time startTime[i] and
 * finished it at time endTime[i].
 *
 * Return the number of students doing their homework at time queryTime.
 * More formally, return the number of students where queryTime lays in the
 * interval [startTime[i], endTime[i]] inclusive.
 *
 *
 * Example 1:
 *
 * Input: startTime = [1,2,3], endTime = [3,2,7], queryTime = 4
 * Output: 1
 * Explanation: The third student started at time 3 and finished at time 7 and
 * was the only student doing homework at time 4.
 *
 * Example 2:
 *
 * Input: startTime = [4], endTime = [4], queryTime = 4
 * Output: 1
 *
 *
 * Constraints:
 *
 * startTime.length == endTime.length
 * 1 <= startTime.length <= 100
 * 1 <= startTime[i] <= endTime[i] <= 1000
 * 1 <= queryTime <= 1000
 *
 */
public class NumberOfStudentsDoingHomeworkAtAGivenTime {

    // V0
    // IDEA: LINEAR SCAN — count the intervals [start, end] that cover queryTime
    /**
     * time = O(N)
     * space = O(1)
     */
    public int busyStudent(int[] startTime, int[] endTime, int queryTime) {
        int busyCount = 0;
        for (int i = 0; i < startTime.length; i++) {
            // both ends are inclusive
            if (startTime[i] <= queryTime && queryTime <= endTime[i]) {
                busyCount++;
            }
        }
        return busyCount;
    }
}
