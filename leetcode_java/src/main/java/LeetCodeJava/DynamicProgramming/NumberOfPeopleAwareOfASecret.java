package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-people-aware-of-a-secret/description/

/**
 * 2327. Number of People Aware of a Secret
 * Medium
 *
 * On day 1, one person discovers a secret.
 *
 * You are given an integer delay, which means that each person will share the secret with a new person every day, starting from delay days after discovering the secret. You are also given an integer forget, which means that each person will forget the secret forget days after discovering it. A person cannot share the secret on the same day they forgot it, or on any day afterwards.
 *
 * Given an integer n, return the number of people who know the secret at the end of day n. Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 6, delay = 2, forget = 4
 * Output: 5
 * Explanation:
 * Day 1: Suppose the first person is named A. (1 person)
 * Day 2: A is the only person who knows the secret. (1 person)
 * Day 3: A shares the secret with a new person, B. (2 people)
 * Day 4: A shares the secret with a new person, C. (3 people)
 * Day 5: A forgets the secret, and B shares the secret with a new person, D. (3 people)
 * Day 6: B shares the secret with E, and C shares the secret with F. (5 people)
 *
 * Example 2:
 *
 * Input: n = 4, delay = 1, forget = 3
 * Output: 6
 * Explanation:
 * Day 1: The first person is named A. (1 person)
 * Day 2: A shares the secret with B. (2 people)
 * Day 3: A and B share the secret with 2 new people, C and D. (4 people)
 * Day 4: A, B, C, and D share the secret with 2 new people, E and F. (6 people)
 *
 *
 * Constraints:
 *
 * 2 <= n <= 1000
 * 1 <= delay < forget <= n
 *
 */
public class NumberOfPeopleAwareOfASecret {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: dp[day] = PEOPLE WHO LEARN THE SECRET ON day; A SLIDING WINDOW SUMS THE CURRENT SHARERS
    /**
     * time = O(N)
     * space = O(N)
     */
    public int peopleAwareOfSecret(int n, int delay, int forget) {
        // newPeople[day] = number of people who first learn the secret on `day`
        long[] newPeople = new long[n + 1];
        newPeople[1] = 1;

        // sharers = people who learned on a day d with day - forget < d <= day - delay
        long sharers = 0;
        for (int day = 2; day <= n; day++) {
            // people who learned `delay` days ago start sharing today
            if (day - delay >= 1) {
                sharers = (sharers + newPeople[day - delay]) % MOD;
            }
            // people who learned `forget` days ago forget today
            if (day - forget >= 1) {
                sharers = (sharers - newPeople[day - forget] + MOD) % MOD;
            }
            newPeople[day] = sharers;
        }

        // still aware at the end of day n: those who learned within the last `forget` days
        long aware = 0;
        for (int day = Math.max(1, n - forget + 1); day <= n; day++) {
            aware = (aware + newPeople[day]) % MOD;
        }
        return (int) aware;
    }
}
