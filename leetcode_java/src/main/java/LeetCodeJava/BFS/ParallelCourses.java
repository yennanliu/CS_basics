package LeetCodeJava.BFS;

// https://leetcode.com/problems/parallel-courses/description/
// https://leetcode.ca/2019-01-09-1136-Parallel-Courses/

import java.util.*;

/**
 * 1136. Parallel Courses
 * Medium
 *
 * You are given an integer n, which indicates that there are n courses labeled from 1 to n.
 * You are also given an array relations where relations[i] = [prevCourse_i, nextCourse_i],
 * representing a prerequisite relationship between course prevCourse_i and course nextCourse_i:
 * course prevCourse_i has to be taken before course nextCourse_i.
 *
 * In one semester, you can take any number of courses as long as you have taken all the
 * prerequisites in the previous semester for the courses you are taking.
 *
 * Return the minimum number of semesters needed to take all courses.
 * If there is no way to take all the courses, return -1.
 *
 *
 * Example 1:
 *
 * Input: n = 3, relations = [[1,3],[2,3]]
 * Output: 2
 * Explanation: The figure above represents the given graph.
 * In the first semester, you can take courses 1 and 2.
 * In the second semester, you can take course 3.
 *
 * Example 2:
 *
 * Input: n = 3, relations = [[1,2],[2,3],[3,1]]
 * Output: -1
 * Explanation: No course can be studied because they are prerequisites of each other.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 5000
 * 1 <= relations.length <= 5000
 * relations[i].length == 2
 * 1 <= prevCourse_i, nextCourse_i <= n
 * prevCourse_i != nextCourse_i
 * All the pairs [prevCourse_i, nextCourse_i] are unique.
 *
 */
public class ParallelCourses {

    // V0
    // IDEA: BFS + TOPOLOGICAL SORT (KAHN'S ALGORITHM, LEVEL BY LEVEL)
    //
    //   A node is a course, an edge prev -> next a prerequisite. Every course with
    //   in-degree 0 can be taken now, so one BFS level == one semester.
    //   If fewer than n courses are ever taken, the rest sit on a cycle -> -1.
    /**
     * time = O(N + M)
     * space = O(N + M)
     */
    public int minimumSemesters(int n, int[][] relations) {
        // adjacency list and in-degree, courses are 1-indexed
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        int[] inDegree = new int[n + 1];
        for (int[] relation : relations) {
            graph.get(relation[0]).add(relation[1]);
            inDegree[relation[1]]++;
        }

        // semester 1: every course with no prerequisite
        Queue<Integer> queue = new LinkedList<>();
        for (int course = 1; course <= n; course++) {
            if (inDegree[course] == 0) {
                queue.offer(course);
            }
        }

        int taken = 0;
        int semesters = 0;
        while (!queue.isEmpty()) {
            semesters++;
            // NOTE: process the WHOLE level at once -- it is one semester
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                int course = queue.poll();
                taken++;
                for (int next : graph.get(course)) {
                    inDegree[next]--;
                    // all of next's prerequisites are done -> it can go next semester
                    if (inDegree[next] == 0) {
                        queue.offer(next);
                    }
                }
            }
        }

        // a cycle leaves some courses with in-degree > 0 forever
        return taken == n ? semesters : -1;
    }

    // V1
    // IDEA: DFS LONGEST PATH IN A DAG + CYCLE DETECTION (3-COLOR)
    //
    //   The answer is the number of courses on the longest prerequisite chain.
    //   longest[c] = 1 + max(longest[next]) over c's successors, memoised.
    //   A node met again while still on the recursion stack (state VISITING) is a
    //   cycle -> -1. Same bound as V0; V0 is the usual interview answer, and this
    //   one recurses up to N deep, which can be risky for N = 5000 on a small stack.
    /**
     * time = O(N + M)
     * space = O(N + M)
     */
    public int minimumSemesters_1(int n, int[][] relations) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] relation : relations) {
            graph.get(relation[0]).add(relation[1]);
        }

        int[] state = new int[n + 1];
        int[] longest = new int[n + 1];
        int answer = 0;
        for (int course = 1; course <= n; course++) {
            int chain = longestChainFrom(course, graph, state, longest);
            if (chain == -1) {
                return -1;
            }
            answer = Math.max(answer, chain);
        }
        return answer;
    }

    private static final int UNVISITED = 0;
    private static final int VISITING = 1;
    private static final int DONE = 2;

    /** Length of the longest chain starting at course, or -1 if a cycle is reachable. */
    private int longestChainFrom(int course, List<List<Integer>> graph, int[] state, int[] longest) {
        if (state[course] == VISITING) {
            // back edge: course is its own (indirect) prerequisite
            return -1;
        }
        if (state[course] == DONE) {
            return longest[course];
        }

        state[course] = VISITING;
        int best = 1;
        for (int next : graph.get(course)) {
            int chain = longestChainFrom(next, graph, state, longest);
            if (chain == -1) {
                return -1;
            }
            best = Math.max(best, chain + 1);
        }
        state[course] = DONE;
        longest[course] = best;
        return best;
    }
}
