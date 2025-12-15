package LeetCodeJava.DFS;

// https://leetcode.com/problems/keys-and-rooms/description/

import java.util.*;

/**
 *  841. Keys and Rooms
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * There are n rooms labeled from 0 to n - 1 and all the rooms are locked except for room 0. Your goal is to visit all the rooms. However, you cannot enter a locked room without having its key.
 *
 * When you visit a room, you may find a set of distinct keys in it. Each key has a number on it, denoting which room it unlocks, and you can take all of them with you to unlock the other rooms.
 *
 * Given an array rooms where rooms[i] is the set of keys that you can obtain if you visited room i, return true if you can visit all the rooms, or false otherwise.
 *
 *
 *
 * Example 1:
 *
 * Input: rooms = [[1],[2],[3],[]]
 * Output: true
 * Explanation:
 * We visit room 0 and pick up key 1.
 * We then visit room 1 and pick up key 2.
 * We then visit room 2 and pick up key 3.
 * We then visit room 3.
 * Since we were able to visit every room, we return true.
 * Example 2:
 *
 * Input: rooms = [[1,3],[3,0,1],[2],[0]]
 * Output: false
 * Explanation: We can not enter room number 2 since the only key that unlocks it is in that room.
 *
 *
 * Constraints:
 *
 * n == rooms.length
 * 2 <= n <= 1000
 * 0 <= rooms[i].length <= 1000
 * 1 <= sum(rooms[i].length) <= 3000
 * 0 <= rooms[i][j] < n
 * All the values of rooms[i] are unique.
 *
 */
public class KeysAndRooms {

    // V0
//    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
//
//    }

    // V0-1
    // IDEA: BFS
    public boolean canVisitAllRooms_0_1(List<List<Integer>> rooms) {
        // edge
        if (rooms.isEmpty()) {
            return true;
        }
        if (rooms.size() == 1) {
            return true;
        }
        Set<Integer> visited = new HashSet();
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < rooms.size(); i++) {
            map.putIfAbsent(i, new ArrayList<>());
            map.get(i).addAll(rooms.get(i)); // ??
        }

        //System.out.println(">>> map = " + map);
        // q: { room_1_id, ... }
        Queue<Integer> q = new LinkedList<>();
        q.add(0);
        visited.add(0);

        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                int cur = q.poll();
                // if already visited all room
                if (visited.size() == rooms.size()) {
                    return true;
                }
                for (int next : map.get(cur)) {
                    if (!visited.contains(next)) {
                        visited.add(next);
                        q.add(next);
                    }
                }
            }
        }

        //System.out.println(">>> visited = " + visited);
        return visited.size() == rooms.size(); // ???
    }

    // V0-2
    // IDEA: DFS
    public boolean canVisitAllRooms_0_2(List<List<Integer>> rooms) {
        // edge
        if (rooms.isEmpty()) {
            return true;
        }
        if (rooms.size() == 1) {
            return true;
        }
        Set<Integer> visited = new HashSet();
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < rooms.size(); i++) {
            map.putIfAbsent(i, new ArrayList<>());
            map.get(i).addAll(rooms.get(i)); // ??
        }

        //System.out.println(">>> map = " + map);
        visited.add(0); // ?? can add before dfs ?
        dfsHelper(0, map, visited);

        //System.out.println(">>> visited = " + visited);
        return visited.size() == rooms.size(); // ???
    }

    private void dfsHelper(int room, Map<Integer, List<Integer>> map, Set<Integer> visited) {
        // add to visited
        //visited.add(room);
        // visit next
        for (int next : map.get(room)) {
            if (!visited.contains(next)) {
                visited.add(next);
                dfsHelper(next, map, visited);
            }
        }
        // return true;
    }


    // V1
    // IDEA: DFS
    // https://leetcode.com/problems/keys-and-rooms/editorial/
    public boolean canVisitAllRooms_1(List<List<Integer>> rooms) {
        boolean[] seen = new boolean[rooms.size()];
        seen[0] = true;
        Stack<Integer> stack = new Stack();
        stack.push(0);

        //At the beginning, we have a todo list "stack" of keys to use.
        //'seen' represents at some point we have entered this room.
        while (!stack.isEmpty()) { // While we have keys...
            int node = stack.pop(); // Get the next key 'node'
            for (int nei : rooms.get(node)) // For every key in room # 'node'...
                if (!seen[nei]) { // ...that hasn't been used yet
                    seen[nei] = true; // mark that we've entered the room
                    stack.push(nei); // add the key to the todo list
                }
        }

        for (boolean v : seen) // if any room hasn't been visited, return false
            if (!v)
                return false;
        return true;
    }

    // V2
    // IDEA: DFS
    // https://leetcode.ca/2018-03-20-841-Keys-and-Rooms/
    private List<List<Integer>> rooms;
    private Set<Integer> vis;

    public boolean canVisitAllRooms_2(List<List<Integer>> rooms) {
        vis = new HashSet<>();
        this.rooms = rooms;
        dfs(0);
        return vis.size() == rooms.size();
    }

    private void dfs(int u) {
        if (vis.contains(u)) {
            return;
        }
        vis.add(u);
        for (int v : rooms.get(u)) {
            dfs(v);
        }
    }


    // V3
    // IDEA: DFS
    // https://leetcode.com/problems/keys-and-rooms/solutions/5699503/simple-dfs-100-beats-java-by-eshwarapras-37vx/
    boolean[] visited;
    int count = 0;
    List<List<Integer>> list;
    int n;

    public boolean canVisitAllRooms_3(List<List<Integer>> rooms) {
        n = rooms.size();
        visited = new boolean[n];
        list = rooms;
        dfs_3(0);
        return count == n;
    }

    void dfs_3(int v) {
        if (visited[v] == true)
            return;
        visited[v] = true;
        count++;
        if (count == n)
            return;
        for (int node : list.get(v)) {
            dfs_3(node);
        }
    }


}
