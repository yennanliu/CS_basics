package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-the-longest-valid-obstacle-course-at-each-position/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 1964. Find the Longest Valid Obstacle Course at Each Position
 * Hard
 *
 * You want to build some obstacle courses. You are given a 0-indexed integer array obstacles of length
 * n, where obstacles[i] describes the height of the ith obstacle.
 *
 * For every index i between 0 and n - 1 (inclusive), find the length of the longest obstacle course in
 * obstacles such that:
 *
 * You choose any number of obstacles between 0 and i inclusive.
 * You must include the ith obstacle in the course.
 * You must put the chosen obstacles in the same order as they appear in obstacles.
 * Every obstacle (except the first) is taller than or the same height as the obstacle immediately
 * before it.
 *
 * Return an array ans of length n, where ans[i] is the length of the longest obstacle course for index i
 * as described above.
 *
 *
 * Example 1:
 *
 * Input: obstacles = [1,2,3,2]
 * Output: [1,2,3,3]
 * Explanation: The longest valid obstacle course at each position is:
 * - i = 0: [1], [1] has length 1.
 * - i = 1: [1,2], [1,2] has length 2.
 * - i = 2: [1,2,3], [1,2,3] has length 3.
 * - i = 3: [1,2,3,2], [1,2,2] has length 3.
 *
 * Example 2:
 *
 * Input: obstacles = [2,2,1]
 * Output: [1,2,1]
 * Explanation: The longest valid obstacle course at each position is:
 * - i = 0: [2], [2] has length 1.
 * - i = 1: [2,2], [2,2] has length 2.
 * - i = 2: [2,2,1], [1] has length 1.
 *
 * Example 3:
 *
 * Input: obstacles = [3,1,5,6,4,2]
 * Output: [1,1,2,3,2,2]
 * Explanation: The longest valid obstacle course at each position is:
 * - i = 0: [3], [3] has length 1.
 * - i = 1: [3,1], [1] has length 1.
 * - i = 2: [3,1,5], [3,5] has length 2. [1,5] is also valid.
 * - i = 3: [3,1,5,6], [3,5,6] has length 3. [1,5,6] is also valid.
 * - i = 4: [3,1,5,6,4], [3,4] has length 2. [1,4] is also valid.
 * - i = 5: [3,1,5,6,4,2], [1,2] has length 2.
 *
 *
 * Constraints:
 *
 * n == obstacles.length
 * 1 <= n <= 10^5
 * 1 <= obstacles[i] <= 10^7
 *
 */
public class FindTheLongestValidObstacleCourseAtEachPosition {

    // V0
    // IDEA: NON-DECREASING LIS VIA PATIENCE TAILS + BINARY SEARCH (UPPER BOUND)
    /**
     *  The answer at i is the longest non-decreasing subsequence ending at i.
     *  tails.get(L) = smallest tail of a non-decreasing subsequence of length L + 1 so far;
     *  tails is non-decreasing, so it can be binary searched.
     *
     *  For each height: position = first tail strictly greater than it (upper bound, because
     *  equal heights extend a course rather than replace it); answer = position + 1; then the
     *  height becomes that tail. With a lower bound, [2,2,1] would give [1,1,1], not [1,2,1].
     */
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int[] longestObstacleCourseAtEachPosition(int[] obstacles) {
        int n = obstacles.length;
        int[] answer = new int[n];
        List<Integer> tails = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int height = obstacles[i];

            // upper bound: first tail > height
            int left = 0;
            int right = tails.size();
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails.get(mid) > height) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }

            answer[i] = left + 1;
            if (left == tails.size()) {
                tails.add(height);
            } else {
                tails.set(left, height);
            }
        }
        return answer;
    }
}
