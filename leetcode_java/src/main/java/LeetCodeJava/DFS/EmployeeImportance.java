package LeetCodeJava.DFS;

// https://leetcode.com/problems/employee-importance/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  690. Employee Importance
 *  Medium
 *
 *  You have a data structure of employee information, including the employee's unique ID,
 *  importance value, and direct subordinates' IDs.
 *
 *  You are given an array of employees `employees` where:
 *   - employees[i].id is the ID of the ith employee.
 *   - employees[i].importance is the importance value of the ith employee.
 *   - employees[i].subordinates is a list of the IDs of the direct subordinates of the
 *     ith employee.
 *
 *  Given an integer id that represents an employee's ID, return the total importance value
 *  of this employee and all their direct and indirect subordinates.
 *
 *  Example 1:
 *  Input: employees = [[1,5,[2,3]],[2,3,[]],[3,3,[]]], id = 1
 *  Output: 11
 *  Explanation: Employee 1 has an importance value of 5 and has two direct subordinates:
 *  employee 2 and employee 3. They both have an importance value of 3. So the total
 *  importance value of employee 1 is 5 + 3 + 3 = 11.
 *
 *  Example 2:
 *  Input: employees = [[1,2,[5]],[5,-3,[]]], id = 5
 *  Output: -3
 *
 *  Constraints:
 *  1 <= employees.length <= 2000
 *  1 <= employees[i].id <= 2000
 *  All employees[i].id are unique.
 *  -100 <= employees[i].importance <= 100
 *  One employee has at most one direct leader and may have several subordinates.
 *  The IDs in employees[i].subordinates are valid IDs.
 */
public class EmployeeImportance {

    /**
     * Definition for Employee (given by LeetCode).
     */
    public static class Employee {
        public int id;
        public int importance;
        public List<Integer> subordinates;
    }

    // V0
    // IDEA: build id -> Employee map, then DFS down the subordinate tree summing importance
    /**
     * time = O(n)
     * space = O(n)
     */
    public int getImportance(List<Employee> employees, int id) {
        Map<Integer, Employee> map = new HashMap<>();
        for (Employee e : employees) {
            map.put(e.id, e);
        }
        return dfs(map, id);
    }

    private int dfs(Map<Integer, Employee> map, int id) {
        Employee cur = map.get(id);
        if (cur == null) {
            return 0;
        }
        int total = cur.importance;
        if (cur.subordinates != null) {
            for (Integer sub : cur.subordinates) {
                total += dfs(map, sub);
            }
        }
        return total;
    }

    // V1
    // IDEA: same map, but iterative BFS (avoids deep recursion)
    /**
     * time = O(n)
     * space = O(n)
     */
    public int getImportance_1(List<Employee> employees, int id) {
        Map<Integer, Employee> map = new HashMap<>();
        for (Employee e : employees) {
            map.put(e.id, e);
        }
        int total = 0;
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(id);
        while (!queue.isEmpty()) {
            Employee cur = map.get(queue.poll());
            if (cur == null) {
                continue;
            }
            total += cur.importance;
            if (cur.subordinates != null) {
                for (Integer sub : cur.subordinates) {
                    queue.offer(sub);
                }
            }
        }
        return total;
    }
}
