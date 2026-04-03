package LeetCodeJava.DFS;

// https://leetcode.com/problems/kill-process/
// https://leetcode.ca/2017-07-04-582-Kill-Process/

import java.util.*;

/**
 *  582 - Kill Process
 * Posted on July 4, 2017 · 3 minute read
 * Welcome to Subscribe On Youtube
 *
 * 582. Kill Process
 * Description
 * You have n processes forming a rooted tree structure. You are given two integer arrays pid and ppid, where pid[i] is the ID of the ith process and ppid[i] is the ID of the ith process's parent process.
 *
 * Each process has only one parent process but may have multiple children processes. Only one process has ppid[i] = 0, which means this process has no parent process (the root of the tree).
 *
 * When a process is killed, all of its children processes will also be killed.
 *
 * Given an integer kill representing the ID of a process you want to kill, return a list of the IDs of the processes that will be killed. You may return the answer in any order.
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: pid = [1,3,10,5], ppid = [3,0,5,3], kill = 5
 * Output: [5,10]
 * Explanation: The processes colored in red are the processes that should be killed.
 * Example 2:
 *
 * Input: pid = [1], ppid = [0], kill = 1
 * Output: [1]
 *
 *
 * Constraints:
 *
 * n == pid.length
 * n == ppid.length
 * 1 <= n <= 5 * 104
 * 1 <= pid[i] <= 5 * 104
 * 0 <= ppid[i] <= 5 * 104
 * Only one process has no parent.
 * All the values of pid are unique.
 * kill is guaranteed to be in pid.
 *
 *
 */
public class KillProcess {

    // V0
    // IDEA: DFS (fixed by gemini)
    public List<Integer> killProcess(List<Integer> pid, List<Integer> ppid, int kill) {


        // 1. Build the Adjacency List: Parent -> List of Children
        /** NOTE !!!
         *
         *  // { node: [sub_node_1, sub_node_2, ..] }
         *
         *
         *   -> Build the Adjacency List: Parent -> List of Children
         *
         */
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < ppid.size(); i++) {
            int parent = ppid.get(i);
            int child = pid.get(i);
            map.putIfAbsent(parent, new ArrayList<>());
            map.get(parent).add(child);

            /** NOTE !!!
             *
             *   NO need to add `pid` as key to map
             *
             */
        }

        List<Integer> res = new ArrayList<>();

        // 2. Start DFS from the 'kill' node
        dfsKill(map, res, kill);

        return res;
    }

    private void dfsKill(Map<Integer, List<Integer>> map, List<Integer> res, int kill) {

        // Add current process to result
        /** NOTE !!!
         *
         *  Add current process to result
         *  (so can still handle `kill is ONLY existed in pid case`)
         *
         */
        res.add(kill);

        // If this process has children, visit them
        if (map.containsKey(kill)) {
            for (int child : map.get(kill)) {
                dfsKill(map, res, child);
            }
        }
    }



    // V0-1
    // IDEA: DFS (gpt)
    // TODO: validate
    public List<Integer> killProcess_0_1(List<Integer> pid, List<Integer> ppid, int kill) {
        // Build graph: parent -> children
        Map<Integer, List<Integer>> map = new HashMap<>();

        for (int i = 0; i < pid.size(); i++) {
            int parent = ppid.get(i);
            int child = pid.get(i);

            map.computeIfAbsent(parent, k -> new ArrayList<>()).add(child);
        }

        List<Integer> res = new ArrayList<>();
        dfs(kill, map, res);
        return res;
    }

    private void dfs(int cur, Map<Integer, List<Integer>> map, List<Integer> res) {
        res.add(cur);

        if (!map.containsKey(cur)) return;

        for (int child : map.get(cur)) {
            dfs(child, map, res);
        }
    }


    // V0-2
    // IDEA: BFS (gemini)
    // TODO: validate
    public List<Integer> killProcess_0_2(List<Integer> pid, List<Integer> ppid, int kill) {
        // 1. Build Adjacency List: Parent -> List of Children
        Map<Integer, List<Integer>> adj = new HashMap<>();
        for (int i = 0; i < ppid.size(); i++) {
            int parent = ppid.get(i);
            int child = pid.get(i);
            adj.putIfAbsent(parent, new ArrayList<>());
            adj.get(parent).add(child);
        }

        // 2. BFS to find all descendants of the 'kill' node
        List<Integer> res = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();

        queue.offer(kill);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            res.add(current);

            // If this process has children, add them to the queue
            if (adj.containsKey(current)) {
                for (int child : adj.get(current)) {
                    queue.offer(child);
                }
            }
        }

        return res;
    }


    // V0-3
    // IDEA: BFS (gpt)
    public List<Integer> killProcess_0_3(List<Integer> pid, List<Integer> ppid, int kill) {

        Map<Integer, List<Integer>> map = new HashMap<>();

        for (int i = 0; i < pid.size(); i++) {
            map.computeIfAbsent(ppid.get(i), k -> new ArrayList<>()).add(pid.get(i));
        }

        List<Integer> res = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();

        queue.offer(kill);

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            res.add(curr);

            if (map.containsKey(curr)) {
                for (int child : map.get(curr)) {
                    queue.offer(child);
                }
            }
        }

        return res;
    }



    // V1
    // https://leetcode.ca/2017-07-04-582-Kill-Process/
    // IDEA: DFS
    private Map<Integer, List<Integer>> g = new HashMap<>();
    private List<Integer> ans = new ArrayList<>();

    public List<Integer> killProcess_1(List<Integer> pid, List<Integer> ppid, int kill) {
        int n = pid.size();
        for (int i = 0; i < n; ++i) {
            g.computeIfAbsent(ppid.get(i), k -> new ArrayList<>()).add(pid.get(i));
        }
        dfs(kill);
        return ans;
    }

    private void dfs(int i) {
        ans.add(i);
        for (int j : g.getOrDefault(i, Collections.emptyList())) {
            dfs(j);
        }
    }




}
