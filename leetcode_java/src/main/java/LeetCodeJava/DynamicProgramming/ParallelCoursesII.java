package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/parallel-courses-ii/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 1494. Parallel Courses II
 * Hard
 *
 * You are given an integer n, which indicates that there are n courses labeled from 1 to n. You are also given an array relations where relations[i] = [prevCourse_i, nextCourse_i], representing a prerequisite relationship between course prevCourse_i and course nextCourse_i: course prevCourse_i has to be taken before course nextCourse_i. Also, you are given the integer k.
 *
 * In one semester, you can take at most k courses as long as you have taken all the prerequisites in the previous semesters for the courses you are taking.
 *
 * Return the minimum number of semesters needed to take all courses. The testcases will be generated such that it is possible to take every course.
 *
 *
 * Example 1:
 *
 * Input: n = 4, relations = [[2,1],[3,1],[1,4]], k = 2
 * Output: 3
 * Explanation: The figure above represents the given graph.
 * In the first semester, you can take courses 2 and 3.
 * In the second semester, you can take course 1.
 * In the third semester, you can take course 4.
 *
 * Example 2:
 *
 * Input: n = 5, relations = [[2,1],[3,1],[4,1],[1,5]], k = 2
 * Output: 4
 * Explanation: The figure above represents the given graph.
 * In the first semester, you can only take courses 2 and 3 since you cannot take more than two per semester.
 * In the second semester, you can take course 4.
 * In the third semester, you can take course 1.
 * In the fourth semester, you can take course 5.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 15
 * 1 <= k <= n
 * 0 <= relations.length <= n * (n-1) / 2
 * relations[i].length == 2
 * 1 <= prevCourse_i, nextCourse_i <= n
 * prevCourse_i != nextCourse_i
 * All the pairs [prevCourse_i, nextCourse_i] are unique.
 * The given graph is a directed acyclic graph.
 *
 */
public class ParallelCoursesII {

    // V0
    // IDEA: BITMASK + BFS OVER SUBSETS (n <= 15 -> state = set of finished courses)
    //       prereqMask[i] = bitmask of course i's prerequisites.
    //       BFS one semester per level, so the first time the full mask is reached is optimal.
    //       available = courses not taken whose prerequisites are all taken.
    //       if at most k are available, take them all (never hurts);
    //       otherwise try every k-sized subset of them. Greedily picking "the most unlocking"
    //       k courses is WRONG, which is why the subsets are enumerated.
    /**
     * time = O(3^N) worst case
     * space = O(2^N)
     */
    public int minNumberOfSemesters(int n, int[][] relations, int k) {
        // courses are 1-based in the input; shift to bits 0..n-1
        int[] prereqMask = new int[n];
        for (int[] relation : relations) {
            int before = relation[0] - 1;
            int after = relation[1] - 1;
            prereqMask[after] |= 1 << before;
        }
        int fullMask = (1 << n) - 1;

        Deque<Integer> queue = new ArrayDeque<>();
        boolean[] seen = new boolean[1 << n];
        queue.offer(0);
        seen[0] = true;
        int semesters = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int q = 0; q < levelSize; q++) {
                int taken = queue.poll();
                if (taken == fullMask) {
                    return semesters;
                }

                // courses not yet taken whose prerequisites are all inside `taken`
                int available = 0;
                for (int course = 0; course < n; course++) {
                    boolean isTaken = ((taken >> course) & 1) == 1;
                    boolean ready = (taken & prereqMask[course]) == prereqMask[course];
                    if (!isTaken && ready) {
                        available |= 1 << course;
                    }
                }

                if (Integer.bitCount(available) <= k) {
                    pushIfNew(taken | available, queue, seen);
                } else {
                    // enumerate every non-empty subset of `available`:
                    // (subset - 1) & available is the next smaller subset of it
                    for (int subset = available; subset > 0; subset = (subset - 1) & available) {
                        if (Integer.bitCount(subset) == k) {
                            pushIfNew(taken | subset, queue, seen);
                        }
                    }
                }
            }
            semesters++;
        }
        return semesters;
    }

    /** Queues a state the BFS has not reached before. */
    private void pushIfNew(int state, Deque<Integer> queue, boolean[] seen) {
        if (!seen[state]) {
            seen[state] = true;
            queue.offer(state);
        }
    }
}
