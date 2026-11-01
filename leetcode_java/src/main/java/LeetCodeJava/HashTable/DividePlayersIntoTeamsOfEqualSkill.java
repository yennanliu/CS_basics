package LeetCodeJava.HashTable;

// https://leetcode.com/problems/divide-players-into-teams-of-equal-skill/description/

import java.util.*;

/**
 * 2491. Divide Players Into Teams of Equal Skill
 * Medium
 *
 * You are given a positive integer array skill of even length n where skill[i] denotes the skill of the ith player. Divide the players into n / 2 teams of size 2 such that the total skill of each team is equal.
 *
 * The chemistry of a team is equal to the product of the skills of the players on that team.
 *
 * Return the sum of the chemistry of all the teams, or return -1 if there is no way to divide the players into teams such that the total skill of each team is equal.
 *
 *
 * Example 1:
 *
 * Input: skill = [3,2,5,1,3,4]
 * Output: 22
 * Explanation:
 * Divide the players into the following teams: (1, 5), (2, 4), (3, 3), where each team has a total skill of 6.
 * The sum of the chemistry of all the teams is: 1 * 5 + 2 * 4 + 3 * 3 = 5 + 8 + 9 = 22.
 *
 * Example 2:
 *
 * Input: skill = [3,4]
 * Output: 12
 * Explanation:
 * The two players form a team with a total skill of 7.
 * The sum of the chemistry of all the teams is: 3 * 4 = 12.
 *
 * Example 3:
 *
 * Input: skill = [1,1,2,3]
 * Output: -1
 * Explanation:
 * There is no way to divide the players into teams such that the total skill of each team is equal.
 *
 *
 * Constraints:
 *
 * 2 <= skill.length <= 10^5
 * skill.length is even.
 * 1 <= skill[i] <= 1000
 *
 */
public class DividePlayersIntoTeamsOfEqualSkill {

    // V0
    // IDEA: SORT + PAIR THE ENDS (smallest must team with largest, target sum is forced)
    /**
     * time = O(N log N)
     * space = O(1) beyond the sort
     */
    public long dividePlayers(int[] skill) {
        int[] sorted = skill.clone();
        Arrays.sort(sorted);
        int n = sorted.length;
        int target = sorted[0] + sorted[n - 1];

        long chemistry = 0;
        for (int i = 0; i < n / 2; i++) {
            int low = sorted[i];
            int high = sorted[n - 1 - i];
            // any pair missing the forced total makes the division impossible
            if (low + high != target) {
                return -1;
            }
            chemistry += (long) low * high;
        }
        return chemistry;
    }

    // V1
    // IDEA: COUNTING (target = 2 * sum / n; each skill must find its complement in the count table)
    /**
     * time = O(N)
     * space = O(M), M = max skill (1000)
     */
    public long dividePlayers_1(int[] skill) {
        int n = skill.length;
        long total = 0;
        for (int s : skill) {
            total += s;
        }
        // n / 2 teams share the total equally
        int teams = n / 2;
        if (total % teams != 0) {
            return -1;
        }
        int target = (int) (total / teams);

        int[] count = new int[2001];
        for (int s : skill) {
            count[s]++;
        }

        long chemistry = 0;
        for (int s : skill) {
            // already used as someone's partner
            if (count[s] == 0) {
                continue;
            }
            int partner = target - s;
            count[s]--;
            if (partner < 0 || partner > 2000 || count[partner] == 0) {
                return -1;
            }
            count[partner]--;
            chemistry += (long) s * partner;
        }
        return chemistry;
    }
}
