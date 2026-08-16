package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/freedom-trail/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 514. Freedom Trail
 * Hard
 *
 * In the video game Fallout 4, the quest "Road to Freedom" requires players to reach a
 * metal dial called the "Freedom Trail Ring" and use the dial to spell a specific keyword
 * to open the door.
 *
 * Given a string ring that represents the code engraved on the outer ring and another
 * string key that represents the keyword that needs to be spelled, return the minimum
 * number of steps to spell all the characters in the keyword.
 *
 * Initially, the first character of the ring is aligned at the "12:00" direction.
 * You should spell all the characters in key one by one by rotating ring clockwise or
 * anticlockwise to make each character of the string key aligned at the "12:00" direction
 * and then by pressing the center button.
 *
 * At the stage of rotating the ring to spell the key character key[i]:
 *
 * 1. You can rotate the ring clockwise or anticlockwise by one place, which counts as one
 *    step. The final purpose of the rotation is to align one of ring's characters at the
 *    "12:00" direction, where this character must equal key[i].
 * 2. If the character key[i] has been aligned at the "12:00" direction, press the center
 *    button to spell, which also counts as one step.
 *
 * Example 1:
 *
 * Input: ring = "godding", key = "gd"
 * Output: 4
 * Explanation:
 * For the first key character 'g', since it is already in place, we just need 1 step to
 * spell this character.
 * For the second key character 'd', we need to rotate the ring "godding" anticlockwise by
 * two steps to make it become "ddinggo".
 * Also, we need 1 more step for spelling.
 * So the final output is 4.
 *
 * Example 2:
 *
 * Input: ring = "godding", key = "godding"
 * Output: 13
 *
 *
 * Constraints:
 *
 * 1 <= ring.length, key.length <= 100
 * ring and key consist of only lower case English letters.
 * It is guaranteed that key could always be spelled by rotating ring.
 *
 */
public class FreedomTrail {

    // V0
    // IDEA: DP over the ring positions
    /**
     *  DP def:
     *    - dp[j] = min steps to spell key[0..i] with ring index j aligned at 12:00
     *
     *  DP eq:
     *    - dpNew[j] = min over k in pos[key[i-1]] of
     *                    dp[k] + min(|j - k|, n - |j - k|) + 1
     *      ( rotating EITHER way on a circle, + 1 for pressing the button )
     *
     *  NOTE !!! the state is the RING INDEX, not the character -- two occurrences of
     *           the same letter sit at different distances and must be kept apart.
     *
     *  time  = O(m * n^2)  // m = key.length, n = ring.length
     *  space = O(n)
     */
    public int findRotateSteps(String ring, String key) {
        int n = ring.length();

        // char -> all indices it shows up in ring
        Map<Character, List<Integer>> pos = new HashMap<>();
        for (int i = 0; i < n; i++) {
            pos.computeIfAbsent(ring.charAt(i), c -> new ArrayList<>()).add(i);
        }

        // init : spell key[0] starting from index 0
        Map<Integer, Integer> dp = new HashMap<>();
        for (int j : pos.get(key.charAt(0))) {
            dp.put(j, Math.min(j, n - j) + 1);
        }

        for (int i = 1; i < key.length(); i++) {
            Map<Integer, Integer> ndp = new HashMap<>();
            for (int j : pos.get(key.charAt(i))) {
                int best = Integer.MAX_VALUE;
                for (Map.Entry<Integer, Integer> e : dp.entrySet()) {
                    int d = Math.abs(j - e.getKey());
                    best = Math.min(best, e.getValue() + Math.min(d, n - d) + 1);
                }
                ndp.put(j, best);
            }
            dp = ndp;
        }

        int res = Integer.MAX_VALUE;
        for (int v : dp.values()) {
            res = Math.min(res, v);
        }
        return res;
    }

}
