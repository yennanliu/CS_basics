package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-number-of-ways-to-place-houses/description/

/**
 * 2320. Count Number of Ways to Place Houses
 * Medium
 *
 * There is a street with n * 2 plots, where there are n plots on each side of the street. The plots on each side are numbered from 1 to n. On each plot, a house can be placed.
 *
 * Return the number of ways houses can be placed such that no two houses are adjacent to each other on the same side of the street. Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * Note that if a house is placed on the ith plot on one side of the street, a house can also be placed on the ith plot on the other side of the street.
 *
 *
 * Example 1:
 *
 * Input: n = 1
 * Output: 4
 * Explanation:
 * Possible arrangements:
 * 1. All plots are empty.
 * 2. A house is placed on one side of the street.
 * 3. A house is placed on the other side of the street.
 * 4. Two houses are placed, one on each side of the street.
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: 9
 * Explanation: The 9 possible arrangements are shown in the diagram above.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^4
 *
 */
public class CountNumberOfWaysToPlaceHouses {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: THE TWO SIDES ARE INDEPENDENT — ONE SIDE IS "NO TWO ADJACENT" (FIBONACCI), ANSWER = WAYS^2
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countHousePlacements(int n) {
        // for plots 1..i on one side:
        //   withHouse = arrangements whose plot i has a house
        //   empty     = arrangements whose plot i is empty
        long withHouse = 1;
        long empty = 1;
        for (int i = 2; i <= n; i++) {
            // a house at i needs plot i-1 empty; an empty plot follows anything
            long newWithHouse = empty;
            long newEmpty = (withHouse + empty) % MOD;
            withHouse = newWithHouse;
            empty = newEmpty;
        }
        long oneSide = (withHouse + empty) % MOD;
        return (int) (oneSide * oneSide % MOD);
    }
}
