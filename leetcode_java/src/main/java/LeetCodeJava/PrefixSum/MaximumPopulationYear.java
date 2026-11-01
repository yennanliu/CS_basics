package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/maximum-population-year/description/

/**
 * 1854. Maximum Population Year
 * Easy
 *
 * You are given a 2D integer array logs where each logs[i] = [birthi, deathi] indicates the birth and death years of the ith person.
 *
 * The population of some year x is the number of people alive during that year. The ith person is counted in year x's population if x is in the inclusive range [birthi, deathi - 1]. Note that the person is not counted in the year that they die.
 *
 * Return the earliest year with the maximum population.
 *
 *
 * Example 1:
 *
 * Input: logs = [[1993,1999],[2000,2010]]
 * Output: 1993
 * Explanation: The maximum population is 1, and 1993 is the earliest year with this population.
 *
 * Example 2:
 *
 * Input: logs = [[1950,1961],[1960,1971],[1970,1981]]
 * Output: 1960
 * Explanation:
 * The maximum population is 2, and it had happened in years 1960 and 1970.
 * The earlier year between them is 1960.
 *
 *
 * Constraints:
 *
 * 1 <= logs.length <= 100
 * 1950 <= birthi < deathi <= 2050
 *
 */
public class MaximumPopulationYear {

    private static final int FIRST_YEAR = 1950;
    private static final int YEAR_SLOTS = 101;   // 1950 .. 2050

    // V0
    // IDEA: DIFFERENCE ARRAY / SWEEP LINE OVER THE BOUNDED YEAR RANGE
    //       a person alive on [birth, death - 1] adds +1 at birth and -1 at death;
    //       the running prefix sum at year y is the population of year y.
    //       The strict ">" keeps the earliest year that reaches the maximum.
    /**
     * time = O(N + Y)   (Y = 101 year slots)
     * space = O(Y)
     */
    public int maximumPopulation(int[][] logs) {
        int[] delta = new int[YEAR_SLOTS + 1];
        for (int[] log : logs) {
            int birth = log[0];
            int death = log[1];
            delta[birth - FIRST_YEAR]++;
            delta[death - FIRST_YEAR]--;
        }

        int population = 0;
        int bestPopulation = 0;
        int bestOffset = 0;
        for (int offset = 0; offset < YEAR_SLOTS; offset++) {
            population += delta[offset];
            if (population > bestPopulation) {
                bestPopulation = population;
                bestOffset = offset;
            }
        }
        return FIRST_YEAR + bestOffset;
    }
}
