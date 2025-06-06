package LeetCodeJava.Greedy;

// https://leetcode.com/problems/jump-game-vii/description/

import java.util.LinkedList;
import java.util.Queue;

/**
 * 1871. Jump Game VII
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given a 0-indexed binary string s and two integers minJump and maxJump. In the beginning, you are standing at index 0, which is equal to '0'. You can move from index i to index j if the following conditions are fulfilled:
 *
 * i + minJump <= j <= min(i + maxJump, s.length - 1), and
 * s[j] == '0'.
 * Return true if you can reach index s.length - 1 in s, or false otherwise.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "011010", minJump = 2, maxJump = 3
 * Output: true
 * Explanation:
 * In the first step, move from index 0 to index 3.
 * In the second step, move from index 3 to index 5.
 * Example 2:
 *
 * Input: s = "01101110", minJump = 2, maxJump = 3
 * Output: false
 *
 *
 * Constraints:
 *
 * 2 <= s.length <= 105
 * s[i] is either '0' or '1'.
 * s[0] == '0'
 * 1 <= minJump <= maxJump < s.length
 *
 *
 *
 */
public class JumpGame7 {

    // V0
//    public boolean canReach(String s, int minJump, int maxJump) {
//
//    }

    // V1-1
    // https://www.youtube.com/watch?v=v1HpZUnQ4Yo
    // https://github.com/neetcode-gh/leetcode/blob/main/kotlin%2F1871-jump-game-vii.kt
    // IDEA: BFS (modified by gpt)
    /**
     * Why BFS Works Here
     *
     * You're trying to reach the end by exploring multiple paths
     * where each move is constrained by jump distances and '0' positions.
     *
     * Think of this as a graph traversal:
     *
     *  - Each index in the string is a node.
     *
     *  - There is a directed edge from i to j if j is within the jump range and s[j] == '0'.
     *
     * -> BFS is optimal for shortest path search in unweighted graphs,
     *    and here you want to check reachability from node 0 to n - 1.
     *
     *
     */
    public boolean canReach_1_1(String s, int minJump, int maxJump) {
        if (s.charAt(s.length() - 1) != '0')
            return false;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        int farthest = 0;

        while (!queue.isEmpty()) {
            int i = queue.poll();
            int start = Math.max(i + minJump, farthest + 1);
            int end = Math.min(i + maxJump + 1, s.length());

            for (int j = start; j < end; j++) {
                if (s.charAt(j) == '0') {
                    if (j == s.length() - 1) {
                        return true;
                    }
                    queue.offer(j);
                }
            }

            farthest = i + maxJump;
        }

        return false;
    }

    // V1-2
    // IDEA: BFS (fixed by gpt)
    public boolean canReach_1_2(String s, int minJump, int maxJump) {
        int n = s.length();
        if (s.charAt(0) != '0' || s.charAt(n - 1) != '0')
            return false;

        Queue<Integer> queue = new LinkedList<>();
        queue.offer(0);
        int farthest = 0;

        while (!queue.isEmpty()) {
            int current = queue.poll();

            // Explore all reachable positions from current + minJump to current + maxJump
            for (int next = Math.max(current + minJump, farthest + 1); next <= Math.min(current + maxJump,
                    n - 1); next++) {
                if (s.charAt(next) == '0') {
                    if (next == n - 1)
                        return true;
                    queue.offer(next);
                }
            }

            // Update farthest to mark progress
            farthest = Math.min(n - 1, current + maxJump);
        }

        return false;
    }

    // V1-3
    // https://www.youtube.com/watch?v=v1HpZUnQ4Yo
    // https://github.com/neetcode-gh/leetcode/blob/main/kotlin%2F1871-jump-game-vii.kt
    // IDEA: DP (modified by gpt)
    public boolean canReach_1_3(String s, int minJump, int maxJump) {
        int n = s.length();
        if (s.charAt(n - 1) != '0')
            return false;

        boolean[] dp = new boolean[n];
        dp[0] = true;
        int countOfPos = 0;

        for (int i = 1; i < n; i++) {
            if (i - minJump >= 0 && dp[i - minJump]) {
                countOfPos++;
            }
            if (i - maxJump - 1 >= 0 && dp[i - maxJump - 1]) {
                countOfPos--;
            }
            dp[i] = countOfPos > 0 && s.charAt(i) == '0';
        }

        return dp[n - 1];
    }


    // V2-1
    // https://leetcode.com/problems/jump-game-vii/solutions/1236272/java-bfs-detailed-analysis-on-bfs-soluti-fnqg/
    //  (TLE)
    public boolean canReach_2_1(String s, int minJump, int maxJump) {
        if (s.charAt(s.length() - 1) != '0')
            return false;

        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[s.length()];
        queue.add(0);

        while (!queue.isEmpty()) {
            int idx = queue.remove();
            if (idx == s.length() - 1)
                return true;
            for (int i = idx + minJump; i <= idx + maxJump && i < s.length(); i++) {
                if (!visited[i] && s.charAt(i) == '0') {
                    visited[i] = true;
                    queue.add(i);
                }
            }
        }

        return false;
    }

    // V2-2
    public boolean canReach_2_2(String s, int minJump, int maxJump) {
        if (s.charAt(s.length() - 1) != '0')
            return false;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);

        // This variable tells us till which index we have processed
        int maxReach = 0;

        while (!queue.isEmpty()) {
            int idx = queue.remove();
            // If we reached the last index
            if (idx == s.length() - 1)
                return true;

            // start the loop from max of [current maximum (idx + minJump), maximum processed index (maxReach)]
            for (int j = Math.max(idx + minJump, maxReach); j <= Math.min(idx + maxJump, s.length() - 1); j++) {
                if (s.charAt(j) == '0')
                    queue.add(j);
            }

            // since we have processed till idx + maxJump so update maxReach to next index
            maxReach = Math.min(idx + maxJump + 1, s.length() - 1);
        }

        return false;
    }


    // V3
    // https://leetcode.com/problems/jump-game-vii/solutions/6455138/java-solution-by-harsh123009-drxz/
    public boolean canReach_3(String s, int minJump, int maxJump) {
        int n = s.length();
        if (s.charAt(n - 1) == '1')
            return false;
        Queue<Integer> que = new LinkedList<>();
        que.add(0);
        int far = 0;
        while (que.size() > 0) {
            int i = que.remove();
            int start = Math.max(i + minJump, far + 1);
            for (int j = start; j <= Math.min(i + maxJump, n - 1); j++) {
                if (s.charAt(j) == '0')
                    que.add(j);
                if (j == n - 1)
                    return true;
            }
            far = i + maxJump;
        }
        return false;
    }


}
