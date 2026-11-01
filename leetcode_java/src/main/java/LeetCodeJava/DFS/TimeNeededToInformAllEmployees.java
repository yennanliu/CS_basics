package LeetCodeJava.DFS;

// https://leetcode.com/problems/time-needed-to-inform-all-employees/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 1376. Time Needed to Inform All Employees
 * Medium
 *
 * A company has n employees with a unique ID for each employee from 0 to n - 1.
 * The head of the company is the one with headID.
 *
 * Each employee has one direct manager given in the manager array where manager[i]
 * is the direct manager of the i-th employee, manager[headID] = -1. Also, it is
 * guaranteed that the subordination relationships have a tree structure.
 *
 * The head of the company wants to inform all the company employees of an urgent
 * piece of news. He will inform his direct subordinates, and they will inform their
 * subordinates, and so on until all employees know about the urgent news.
 *
 * The i-th employee needs informTime[i] minutes to inform all of his direct
 * subordinates (i.e., After informTime[i] minutes, all his direct subordinates can
 * start spreading the news).
 *
 * Return the number of minutes needed to inform all the employees about the urgent news.
 *
 *
 * Example 1:
 *
 * Input: n = 1, headID = 0, manager = [-1], informTime = [0]
 * Output: 0
 * Explanation: The head of the company is the only employee in the company.
 *
 * Example 2:
 *
 * Input: n = 6, headID = 2, manager = [2,2,-1,2,2,2], informTime = [0,0,1,0,0,0]
 * Output: 1
 * Explanation: The head of the company with id = 2 is the direct manager of all the
 * employees in the company and needs 1 minute to inform them all.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 * 0 <= headID < n
 * manager.length == n
 * 0 <= manager[i] < n
 * manager[headID] == -1
 * informTime.length == n
 * 0 <= informTime[i] <= 1000
 * informTime[i] == 0 if employee i has no subordinates.
 *
 */
public class TimeNeededToInformAllEmployees {

    // V0
    // IDEA: DFS ON THE MANAGER TREE (answer = the SLOWEST root-to-leaf path)
    //       a manager tells all of their reports at once, so sibling branches run in
    //       parallel: the company is informed when the longest chain finishes, not the
    //       sum of the chains.
    //
    //          total(i) = informTime[i] + max(total(child)) over children
    //          answer   = total(headID)
    //
    //       NOTE: `manager` points UP, so it is inverted into a children list first --
    //             walking it upward per employee without memo is O(n) per node.
    //       NOTE: iterative with an explicit stack: the tree can be a 10^5-long chain,
    //             which would overflow the JVM call stack if recursed.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int numOfMinutes(int n, int headID, int[] manager, int[] informTime) {
        // edge case: the head alone needs to tell nobody
        if (n <= 1) {
            return 0;
        }

        List<List<Integer>> children = buildChildren(n, manager);

        int maxMinutes = 0;
        // each entry = {employee, minutes elapsed before THIS employee hears the news}
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{headID, 0});

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int employee = current[0];
            int heardAt = current[1];

            maxMinutes = Math.max(maxMinutes, heardAt);

            // every direct report hears the news informTime[employee] minutes later
            for (int report : children.get(employee)) {
                stack.push(new int[]{report, heardAt + informTime[employee]});
            }
        }
        return maxMinutes;
    }

    /** children.get(m) = the direct reports of manager m. */
    private List<List<Integer>> buildChildren(int n, int[] manager) {
        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            if (manager[i] != -1) {
                children.get(manager[i]).add(i);
            }
        }
        return children;
    }

    // V1
    // IDEA: WALK UP THE MANAGER CHAIN WITH MEMO (no children list needed)
    //       timeToHear[i] = minutes until employee i hears the news
    //                     = timeToHear[manager[i]] + informTime[manager[i]],  timeToHear[head] = 0
    //       answer = max over all employees of timeToHear[i].
    //       for each employee, climb until a node whose value is already known, then fill
    //       the climbed path back down. memo means every employee is resolved once, so the
    //       total work is O(n). the climb is iterative, so a long chain cannot overflow.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int numOfMinutes_1(int n, int headID, int[] manager, int[] informTime) {
        final int unknown = -1;

        int[] timeToHear = new int[n];
        for (int i = 0; i < n; i++) {
            timeToHear[i] = unknown;
        }
        timeToHear[headID] = 0;

        int maxMinutes = 0;
        List<Integer> path = new ArrayList<>();

        for (int employee = 0; employee < n; employee++) {
            // climb until we reach someone whose time is already known
            path.clear();
            int current = employee;
            while (timeToHear[current] == unknown) {
                path.add(current);
                current = manager[current];
            }

            // fill the climbed path from the top (closest to the known node) down
            for (int i = path.size() - 1; i >= 0; i--) {
                int node = path.get(i);
                int boss = manager[node];
                timeToHear[node] = timeToHear[boss] + informTime[boss];
            }

            maxMinutes = Math.max(maxMinutes, timeToHear[employee]);
        }
        return maxMinutes;
    }
}
