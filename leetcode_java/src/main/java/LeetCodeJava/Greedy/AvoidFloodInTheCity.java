package LeetCodeJava.Greedy;

// https://leetcode.com/problems/avoid-flood-in-the-city/description/

import java.util.*;

/**
 * 1488. Avoid Flood in The City
 * Medium
 *
 * Your country has 10^9 lakes. Initially, all the lakes are empty, but when it rains over the
 * nth lake, the nth lake becomes full of water. If it rains over a lake that is full of water,
 * there will be a flood. Your goal is to avoid floods in any lake.
 *
 * Given an integer array rains where:
 *
 * rains[i] > 0 means there will be rains over the rains[i] lake.
 * rains[i] == 0 means there are no rains this day and you must choose one lake this day and dry it.
 *
 * Return an array ans where:
 *
 * ans.length == rains.length
 * ans[i] == -1 if rains[i] > 0.
 * ans[i] is the lake you choose to dry in the ith day if rains[i] == 0.
 *
 * If there are multiple valid answers return any of them. If it is impossible to avoid flood
 * return an empty array.
 *
 * Notice that if you chose to dry a full lake, it becomes empty, but if you chose to dry an
 * empty lake, nothing changes.
 *
 *
 * Example 1:
 *
 * Input: rains = [1,2,3,4]
 * Output: [-1,-1,-1,-1]
 * Explanation: After the first day full lakes are [1]
 * After the second day full lakes are [1,2]
 * After the third day full lakes are [1,2,3]
 * After the fourth day full lakes are [1,2,3,4]
 * There's no day to dry any lake and there is no flood in any lake.
 *
 * Example 2:
 *
 * Input: rains = [1,2,0,0,2,1]
 * Output: [-1,-1,2,1,-1,-1]
 * Explanation: After the first day full lakes are [1]
 * After the second day full lakes are [1,2]
 * After the third day, we dry lake 2. Full lakes are [1]
 * After the fourth day, we dry lake 1. There is no full lakes.
 * After the fifth day, full lakes are [2].
 * After the sixth day, full lakes are [1,2].
 * It is easy that this scenario is flood-free. [-1,-1,1,2,-1,-1] is another acceptable scenario.
 *
 * Example 3:
 *
 * Input: rains = [1,2,0,1,2]
 * Output: []
 * Explanation: After the second day, full lakes are [1,2]. We have to dry one lake in the
 * third day. After that, it will rain over lakes [1,2]. It's easy to prove that no matter
 * which lake you choose to dry in the 3rd day, the other one will flood.
 *
 *
 * Constraints:
 *
 * 1 <= rains.length <= 10^5
 * 0 <= rains[i] <= 10^9
 *
 */
public class AvoidFloodInTheCity {

    // V0
    // IDEA: GREEDY + MIN HEAP KEYED ON THE NEXT RAIN DAY (on a dry day, empty the most urgent full lake)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int[] avoidFlood(int[] rains) {
        int n = rains.length;

        // nextRain[i] = the next day lake rains[i] gets rain again (-1 = never)
        int[] nextRain = new int[n];
        Arrays.fill(nextRain, -1);
        Map<Integer, Integer> laterRainDay = new HashMap<>();
        for (int i = n - 1; i >= 0; i--) {
            int lake = rains[i];
            if (lake > 0) {
                if (laterRainDay.containsKey(lake)) {
                    nextRain[i] = laterRainDay.get(lake);
                }
                laterRainDay.put(lake, i);
            }
        }

        int[] answer = new int[n];
        Set<Integer> fullLakes = new HashSet<>();
        // {next rain day, lake}: the lake that floods soonest is on top
        PriorityQueue<int[]> urgent = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        for (int i = 0; i < n; i++) {
            int lake = rains[i];
            if (lake > 0) {
                answer[i] = -1;
                if (fullLakes.contains(lake)) {
                    // no dry day was spent on it in time: a flood is unavoidable
                    return new int[0];
                }
                fullLakes.add(lake);
                if (nextRain[i] != -1) {
                    urgent.add(new int[] {nextRain[i], lake});
                }
            } else if (!urgent.isEmpty()) {
                // drying the lake whose deadline is nearest can never be worse
                // than drying any other (exchange argument)
                int[] mostUrgent = urgent.poll();
                fullLakes.remove(mostUrgent[1]);
                answer[i] = mostUrgent[1];
            } else {
                // nothing needs drying, but a lake must be named: any lake works
                answer[i] = 1;
            }
        }
        return answer;
    }

    // V1
    // IDEA: GREEDY + TREESET OF DRY DAYS (when a full lake rains again, spend the first dry day after it filled)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int[] avoidFlood_1(int[] rains) {
        int n = rains.length;
        int[] answer = new int[n];
        // lake -> the day it last filled
        Map<Integer, Integer> filledOn = new HashMap<>();
        // dry days not yet assigned to any lake
        TreeSet<Integer> dryDays = new TreeSet<>();

        for (int i = 0; i < n; i++) {
            int lake = rains[i];
            if (lake == 0) {
                dryDays.add(i);
                // placeholder: a dry day that is never needed still has to name a lake
                answer[i] = 1;
                continue;
            }

            answer[i] = -1;
            if (filledOn.containsKey(lake)) {
                // the lake is full: it must be dried on some free day after it filled
                Integer dryDay = dryDays.higher(filledOn.get(lake));
                if (dryDay == null) {
                    return new int[0];
                }
                answer[dryDay] = lake;
                dryDays.remove(dryDay);
            }
            filledOn.put(lake, i);
        }
        return answer;
    }
}
