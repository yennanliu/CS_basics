package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/smallest-sufficient-team/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1125. Smallest Sufficient Team
 * Hard
 *
 * In a project, you have a list of required skills req_skills, and a list of people.
 * The ith person people[i] contains a list of skills that the person has.
 *
 * Consider a sufficient team: a set of people such that for every required skill in req_skills,
 * there is at least one person in the team who has that skill.
 * We can represent these teams by the index of each person.
 *
 * For example, team = [0, 1, 3] represents the people with skills people[0], people[1], and people[3].
 *
 * Return any sufficient team of the smallest possible size, represented by the index of each person.
 * You may return the answer in any order.
 *
 * It is guaranteed an answer exists.
 *
 *
 * Example 1:
 *
 * Input: req_skills = ["java","nodejs","reactjs"], people = [["java"],["nodejs"],["nodejs","reactjs"]]
 * Output: [0,2]
 *
 * Example 2:
 *
 * Input: req_skills = ["algorithms","math","java","reactjs","csharp","aws"], people = [["algorithms","math","java"],["algorithms","math","reactjs"],["java","csharp","aws"],["reactjs","csharp"],["csharp","math"],["aws","java"]]
 * Output: [1,2]
 *
 *
 * Constraints:
 *
 * 1 <= req_skills.length <= 16
 * 1 <= req_skills[i].length <= 16
 * req_skills[i] consists of lowercase English letters.
 * All the strings of req_skills are unique.
 * 1 <= people.length <= 60
 * 0 <= people[i].length <= 16
 * 1 <= people[i][j].length <= 16
 * people[i][j] consists of lowercase English letters.
 * All the strings of people[i] are unique.
 * Every skill in people[i] is a skill in req_skills.
 * It is guaranteed a sufficient team exists.
 *
 */
public class SmallestSufficientTeam {

    // V0
    // IDEA: BITMASK DP OVER SKILL SETS (m <= 16 -> ONLY 2^16 STATES)
    /**
     * personMask[j]   = bitmask of the skills person j covers (bit i = req_skills[i])
     * dp[state]       = min team size whose union of skills is exactly `state`
     * lastHired/prev  = the person hired last and the state before, to rebuild the team
     *
     * dp[state | personMask[j]] = min(dp[state | personMask[j]], dp[state] + 1)
     *   for every reachable state and every person j
     *
     * base: dp[0] = 0; answer = walk lastHired / prev back from the full mask
     */
    /**
     * time = O(2^M * N)
     * space = O(2^M)
     */
    public int[] smallestSufficientTeam(String[] req_skills, List<List<String>> people) {
        int skillCount = req_skills.length;
        int peopleCount = people.size();

        // skill name -> bit position
        Map<String, Integer> skillId = new HashMap<>();
        for (int i = 0; i < skillCount; i++) {
            skillId.put(req_skills[i], i);
        }

        // each person as a bitmask: set bit `id` for every skill they have
        int[] personMask = new int[peopleCount];
        for (int j = 0; j < peopleCount; j++) {
            for (String skill : people.get(j)) {
                personMask[j] |= (1 << skillId.get(skill));
            }
        }

        // 2^skillCount states; the full mask has every one of the skillCount bits set
        int stateCount = 1 << skillCount;
        int fullMask = stateCount - 1;

        int[] dp = new int[stateCount];
        int[] lastHired = new int[stateCount];
        int[] prevState = new int[stateCount];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        // states only grow (state | mask >= state), so increasing order is a valid DP order
        for (int state = 0; state < stateCount; state++) {
            if (dp[state] == Integer.MAX_VALUE) {
                continue;
            }
            for (int j = 0; j < peopleCount; j++) {
                // union of the current team's skills with person j's skills
                int nextState = state | personMask[j];
                if (dp[state] + 1 < dp[nextState]) {
                    dp[nextState] = dp[state] + 1;
                    lastHired[nextState] = j;
                    prevState[nextState] = state;
                }
            }
        }

        // walk back from the full skill set to rebuild the team
        List<Integer> team = new ArrayList<>();
        int state = fullMask;
        while (state != 0) {
            team.add(lastHired[state]);
            state = prevState[state];
        }

        int[] result = new int[team.size()];
        for (int i = 0; i < team.size(); i++) {
            result[i] = team.get(i);
        }
        return result;
    }
}
