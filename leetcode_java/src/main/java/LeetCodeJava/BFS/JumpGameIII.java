package LeetCodeJava.BFS;

// https://leetcode.com/problems/jump-game-iii/description/

import java.util.*;

/**
 * 1306. Jump Game III
 * Medium
 *
 * Given an array of non-negative integers arr, you are initially positioned at
 * start index of the array. When you are at index i, you can jump to i + arr[i]
 * or i - arr[i], check if you can reach any index with value 0.
 *
 * Notice that you can not jump outside of the array at any time.
 *
 *
 * Example 1:
 *
 * Input: arr = [4,2,3,0,3,1,2], start = 5
 * Output: true
 * Explanation:
 * All possible ways to reach at index 3 with value 0 are:
 * index 5 -> index 4 -> index 1 -> index 3
 * index 5 -> index 6 -> index 4 -> index 1 -> index 3
 *
 * Example 2:
 *
 * Input: arr = [4,2,3,0,3,1,2], start = 0
 * Output: true
 * Explanation:
 * One possible way to reach at index 3 with value 0 is:
 * index 0 -> index 4 -> index 1 -> index 3
 *
 * Example 3:
 *
 * Input: arr = [3,0,2,1,2], start = 2
 * Output: false
 * Explanation: There is no way to reach at index 1 with value 0.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 5 * 10^4
 * 0 <= arr[i] < arr.length
 * 0 <= start < arr.length
 *
 */
public class JumpGameIII {

    // V0
    // IDEA: BFS + VISITED
    //  each index is a node with edges to i + arr[i] and i - arr[i];
    //  the question is plain reachability of any index whose value is 0.
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean canReach(int[] arr, int start) {
        int n = arr.length;
        boolean[] visited = new boolean[n];
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int index = queue.poll();
            if (arr[index] == 0) {
                return true;
            }
            int[] nextIndices = {index + arr[index], index - arr[index]};
            for (int next : nextIndices) {
                if (next >= 0 && next < n && !visited[next]) {
                    visited[next] = true;
                    queue.offer(next);
                }
            }
        }
        return false;
    }

    // V1
    // IDEA: DFS (RECURSIVE) WITH A VISITED ARRAY
    //  NOTE: recursion depth can reach N, so BFS is the safer interview answer.
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean canReach_1(int[] arr, int start) {
        boolean[] visited = new boolean[arr.length];
        return dfs(arr, start, visited);
    }

    /** True if an index with value 0 is reachable from index. */
    private boolean dfs(int[] arr, int index, boolean[] visited) {
        if (index < 0 || index >= arr.length || visited[index]) {
            return false;
        }
        if (arr[index] == 0) {
            return true;
        }
        visited[index] = true;
        return dfs(arr, index + arr[index], visited) || dfs(arr, index - arr[index], visited);
    }
}
