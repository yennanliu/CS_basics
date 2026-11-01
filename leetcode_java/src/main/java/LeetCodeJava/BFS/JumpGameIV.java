package LeetCodeJava.BFS;

// https://leetcode.com/problems/jump-game-iv/description/

import java.util.*;

/**
 * 1345. Jump Game IV
 * Hard
 *
 * Given an array of integers arr, you are initially positioned at the first
 * index of the array.
 *
 * In one step you can jump from index i to index:
 *
 * - i + 1 where: i + 1 < arr.length.
 * - i - 1 where: i - 1 >= 0.
 * - j where: arr[i] == arr[j] and i != j.
 *
 * Return the minimum number of steps to reach the last index of the array.
 *
 * Notice that you can not jump outside of the array at any time.
 *
 *
 * Example 1:
 *
 * Input: arr = [100,-23,-23,404,100,23,23,23,3,404]
 * Output: 3
 * Explanation: You need three jumps from index 0 --> 4 --> 3 --> 9.
 * Note that index 9 is the last index of the array.
 *
 * Example 2:
 *
 * Input: arr = [7]
 * Output: 0
 * Explanation: Start index is the last index. You do not need to jump.
 *
 * Example 3:
 *
 * Input: arr = [7,6,9,6,9,6,9,7]
 * Output: 1
 * Explanation: You can jump directly from index 0 to index 7 which is
 * last index of the array.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 5 * 10^4
 * -10^8 <= arr[i] <= 10^8
 *
 */
public class JumpGameIV {

    // V0
    // IDEA: BFS (LEVEL = NUMBER OF STEPS) + DROP EACH VALUE GROUP AFTER ITS FIRST USE
    //  KEY: once the indices sharing arr[i] are expanded, remove that group.
    //  Every index in it is enqueued at the same (minimal) level, so it is never
    //  needed again -- without this, [7,7,7,...,7] costs O(N^2).
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minJumps(int[] arr) {
        int n = arr.length;
        if (n <= 1) {
            return 0;
        }

        // value -> every index holding that value
        Map<Integer, List<Integer>> indicesByValue = new HashMap<>();
        for (int i = 0; i < n; i++) {
            indicesByValue.computeIfAbsent(arr[i], key -> new ArrayList<>()).add(i);
        }

        boolean[] visited = new boolean[n];
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(0);
        visited[0] = true;

        int steps = 0;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int k = 0; k < levelSize; k++) {
                int index = queue.poll();
                if (index == n - 1) {
                    return steps;
                }

                // same-value jumps: consume the group, then drop it
                List<Integer> sameValue = indicesByValue.remove(arr[index]);
                if (sameValue != null) {
                    for (int next : sameValue) {
                        if (!visited[next]) {
                            visited[next] = true;
                            queue.offer(next);
                        }
                    }
                }

                // neighbour jumps
                int[] neighbours = {index - 1, index + 1};
                for (int next : neighbours) {
                    if (next >= 0 && next < n && !visited[next]) {
                        visited[next] = true;
                        queue.offer(next);
                    }
                }
            }
            steps++;
        }
        return -1;
    }
}
