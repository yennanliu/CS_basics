package LeetCodeJava.Set;

// https://leetcode.com/problems/find-the-losers-of-the-circular-game/description/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 2682. Find the Losers of the Circular Game
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * There are n friends that are playing a game. The friends are sitting in a circle and are numbered from 1 to n in clockwise order. More formally, moving clockwise from the ith friend brings you to the (i+1)th friend for 1 <= i < n, and moving clockwise from the nth friend brings you to the 1st friend.
 *
 * The rules of the game are as follows:
 *
 * 1st friend receives the ball.
 *
 * After that, 1st friend passes it to the friend who is k steps away from them in the clockwise direction.
 * After that, the friend who receives the ball should pass it to the friend who is 2 * k steps away from them in the clockwise direction.
 * After that, the friend who receives the ball should pass it to the friend who is 3 * k steps away from them in the clockwise direction, and so on and so forth.
 * In other words, on the ith turn, the friend holding the ball should pass it to the friend who is i * k steps away from them in the clockwise direction.
 *
 * The game is finished when some friend receives the ball for the second time.
 *
 * The losers of the game are friends who did not receive the ball in the entire game.
 *
 * Given the number of friends, n, and an integer k, return the array answer, which contains the losers of the game in the ascending order.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 5, k = 2
 * Output: [4,5]
 * Explanation: The game goes as follows:
 * 1) Start at 1st friend and pass the ball to the friend who is 2 steps away from them - 3rd friend.
 * 2) 3rd friend passes the ball to the friend who is 4 steps away from them - 2nd friend.
 * 3) 2nd friend passes the ball to the friend who is 6 steps away from them  - 3rd friend.
 * 4) The game ends as 3rd friend receives the ball for the second time.
 * Example 2:
 *
 * Input: n = 4, k = 4
 * Output: [2,3,4]
 * Explanation: The game goes as follows:
 * 1) Start at the 1st friend and pass the ball to the friend who is 4 steps away from them - 1st friend.
 * 2) The game ends as 1st friend receives the ball for the second time.
 *
 *
 * Constraints:
 *
 * 1 <= k <= n <= 50
 *
 */
public class FindTheLosersOfTheCircularGame {

    // V0
//    public int[] circularGameLosers(int n, int k) {
//
//    }

    // V0-1
    // IDEA: SET + MATH (fixed by gpt)
    public int[] circularGameLosers_0_1(int n, int k) {
        boolean[] visited = new boolean[n + 1]; // 1-based indexing
        int cnt = 1;
        int idx = 1; // start from player 1

        // simulate the game
        while (!visited[idx]) {
            visited[idx] = true;
            /** NOTE !!!
             *
             *  the logic we get new adjusted idx
             */
            idx = (idx + cnt * k - 1) % n + 1; // move to next player
            cnt++;
        }

        // collect losers
        List<Integer> losers = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (!visited[i]) {
                losers.add(i);
            }
        }

        // convert to array
        int[] res = new int[losers.size()];
        for (int i = 0; i < losers.size(); i++) {
            res[i] = losers.get(i);
        }

        return res;
    }

    // V1
    // https://leetcode.com/problems/find-the-losers-of-the-circular-game/solutions/3521902/simple-clean-java-solution-by-himanshubh-sd3u/
    public int[] circularGameLosers_1(int n, int k) {
        boolean visited[] = new boolean[n];
        int v = 0, i = 0;
        while (visited[i % n] == false) {
            v++;
            visited[i % n] = true;
            i += v * k;
        }
        int[] res = new int[n - v];
        int j = 0;
        for (i = 0; i < visited.length; i++) {
            if (visited[i] == false)
                res[j++] = i + 1;
        }
        return res;
    }

    // V2
    // https://leetcode.com/problems/find-the-losers-of-the-circular-game/solutions/3528741/java-beats-99-10-lines-by-judgementdey-g5ia/
    public int[] circularGameLosers_2(int n, int k) {
        boolean[] map = new boolean[n];
        int x = 0, i;

        for (i = 1; !map[x]; i++) {
            map[x] = true;
            x = (x + i * k) % n;
        }
        int[] ans = new int[n - i + 1];
        int j = 0;

        for (i = 0; i < n; i++)
            if (!map[i])
                ans[j++] = i + 1;

        return ans;
    }



}
