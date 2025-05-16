package LeetCodeJava.Greedy;

// https://leetcode.com/problems/dota2-senate/description/
// https://leetcode.cn/problems/dota2-senate/description/

import java.util.LinkedList;
import java.util.Queue;

/**
 * 649. Dota2 Senate
 * Medium
 * Topics
 * Companies
 * In the world of Dota2, there are two parties: the Radiant and the Dire.
 *
 * The Dota2 senate consists of senators coming from two parties. Now the Senate wants to decide on a change in the Dota2 game. The voting for this change is a round-based procedure. In each round, each senator can exercise one of the two rights:
 *
 * Ban one senator's right: A senator can make another senator lose all his rights in this and all the following rounds.
 * Announce the victory: If this senator found the senators who still have rights to vote are all from the same party, he can announce the victory and decide on the change in the game.
 * Given a string senate representing each senator's party belonging. The character 'R' and 'D' represent the Radiant party and the Dire party. Then if there are n senators, the size of the given string will be n.
 *
 * The round-based procedure starts from the first senator to the last senator in the given order. This procedure will last until the end of voting. All the senators who have lost their rights will be skipped during the procedure.
 *
 * Suppose every senator is smart enough and will play the best strategy for his own party. Predict which party will finally announce the victory and change the Dota2 game. The output should be "Radiant" or "Dire".
 *
 *
 *
 * Example 1:
 *
 * Input: senate = "RD"
 * Output: "Radiant"
 * Explanation:
 * The first senator comes from Radiant and he can just ban the next senator's right in round 1.
 * And the second senator can't exercise any rights anymore since his right has been banned.
 * And in round 2, the first senator can just announce the victory since he is the only guy in the senate who can vote.
 * Example 2:
 *
 * Input: senate = "RDD"
 * Output: "Dire"
 * Explanation:
 * The first senator comes from Radiant and he can just ban the next senator's right in round 1.
 * And the second senator can't exercise any rights anymore since his right has been banned.
 * And the third senator comes from Dire and he can ban the first senator's right in round 1.
 * And in round 2, the third senator can just announce the victory since he is the only guy in the senate who can vote.
 *
 *
 * Constraints:
 *
 * n == senate.length
 * 1 <= n <= 104
 * senate[i] is either 'R' or 'D'.
 *
 *
 *
 */
public class Dota2Senate {

    // V0
//    public String predictPartyVictory(String senate) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=zZA5KskfMuQ
    // https://github.com/neetcode-gh/leetcode/blob/main/kotlin%2F0649-dota2-senate.kt
    // Kotlin
//    fun predictPartyVictory(senate: String): String {
//        val rQ = LinkedList<Int>()
//        val dQ = LinkedList<Int>()
//        val n = senate.length
//
//        for (i in senate.indices) {
//            if (senate[i] == 'R') rQ.addLast(i)
//            else dQ.addLast(i)
//        }
//
//        while (rQ.isNotEmpty() && dQ.isNotEmpty()) {
//            val r = rQ.removeFirst()
//            val d = dQ.removeFirst()
//
//            if (r < d) rQ.addLast(r + n)
//            else dQ.addLast(d + n)
//        }
//
//        return if (rQ.isEmpty()) "Dire" else "Radiant"
//    }


    // V2
    // https://leetcode.com/problems/dota2-senate/solutions/3483399/simple-diagram-explanation-by-l30xl1u-fsf7/

    // V3
    // https://leetcode.com/problems/dota2-senate/solutions/6619315/solve-by-queue-which-is-super-easy-to-un-x7kx/
    public String predictPartyVictory_3(String senate) {
        Queue<Integer> radiant = new LinkedList<>(); // Queue for Radiant senators
        Queue<Integer> dire = new LinkedList<>(); // Queue for Dire senators

        int n = senate.length();

        for (int i = 0; i < n; i++) {
            if (senate.charAt(i) == 'R') {
                radiant.offer(i); // Add Radiant senator's index to the queue
            } else {
                dire.offer(i); // Add Dire senator's index to the queue
            }
        }

        while (!radiant.isEmpty() && !dire.isEmpty()) {
            int rIndex = radiant.poll(); // Get the next Radiant senator
            int dIndex = dire.poll(); // Get the next Dire senator

            if (rIndex < dIndex) {
                // Radiant senator bans Dire senator, and returns next round
                radiant.offer(rIndex + n);
            } else {
                // Dire senator bans Radiant senator, and returns next round
                dire.offer(dIndex + n);
            }
        }

        // The winning party is the one with remaining senators
        return radiant.isEmpty() ? "Dire" : "Radiant";
    }


}
