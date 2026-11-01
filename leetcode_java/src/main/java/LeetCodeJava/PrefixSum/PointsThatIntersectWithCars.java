package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/points-that-intersect-with-cars/description/

import java.util.*;

/**
 * 2848. Points That Intersect With Cars
 * Easy
 *
 * You are given a 0-indexed 2D integer array nums representing the coordinates of the cars parking on a number line. For any index i, nums[i] = [start_i, end_i] where start_i is the starting point of the ith car and end_i is the ending point of the ith car.
 *
 * Return the number of integer points on the line that are covered with any part of a car.
 *
 *
 * Example 1:
 *
 * Input: nums = [[3,6],[1,5],[4,7]]
 * Output: 7
 * Explanation: All the points from 1 to 7 intersect at least one car, therefore the answer would be 7.
 *
 * Example 2:
 *
 * Input: nums = [[1,3],[5,8]]
 * Output: 7
 * Explanation: Points intersecting at least one car are 1, 2, 3, 5, 6, 7, 8. There are a total of 7 points, therefore the answer would be 7.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * nums[i].length == 2
 * 1 <= start_i <= end_i <= 100
 *
 */
public class PointsThatIntersectWithCars {

    // max coordinate is 100, and the difference array writes at end + 1
    private static final int SIZE = 102;

    // V0
    // IDEA: DIFFERENCE ARRAY (line sweep over a tiny coordinate range)
    //       +1 at start, -1 at end + 1 (the interval is closed); a point is covered iff the
    //       running prefix sum there is > 0. overlaps are counted once.
    /**
     * time = O(N + M)
     * space = O(M)
     */
    public int numberOfPoints(List<List<Integer>> nums) {
        int[] diff = new int[SIZE];
        for (List<Integer> car : nums) {
            int start = car.get(0);
            int end = car.get(1);
            diff[start]++;
            diff[end + 1]--;
        }

        int covered = 0;
        int activeCars = 0;
        for (int point = 0; point < SIZE; point++) {
            activeCars += diff[point];
            if (activeCars > 0) {
                covered++;
            }
        }
        return covered;
    }

    // V1
    // IDEA: MARK EVERY COVERED POINT (brute force, fine for coordinates <= 100)
    /**
     * time = O(N * M)
     * space = O(M)
     */
    public int numberOfPoints_1(List<List<Integer>> nums) {
        boolean[] isCovered = new boolean[SIZE];
        for (List<Integer> car : nums) {
            for (int point = car.get(0); point <= car.get(1); point++) {
                isCovered[point] = true;
            }
        }

        int covered = 0;
        for (boolean flag : isCovered) {
            if (flag) {
                covered++;
            }
        }
        return covered;
    }
}
