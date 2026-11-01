package LeetCodeJava.HashTable;

// https://leetcode.com/problems/number-of-equivalent-domino-pairs/description/

/**
 * 1128. Number of Equivalent Domino Pairs
 * Easy
 *
 * Given a list of dominoes, dominoes[i] = [a, b] is equivalent to dominoes[j] = [c, d] if and only
 * if either (a == c and b == d), or (a == d and b == c) - that is, one domino can be rotated to be
 * equal to another domino.
 *
 * Return the number of pairs (i, j) for which 0 <= i < j < dominoes.length, and dominoes[i] is
 * equivalent to dominoes[j].
 *
 *
 * Example 1:
 *
 * Input: dominoes = [[1,2],[2,1],[3,4],[5,6]]
 * Output: 1
 *
 * Example 2:
 *
 * Input: dominoes = [[1,2],[1,2],[1,1],[1,2],[2,2]]
 * Output: 3
 *
 *
 * Constraints:
 *
 * 1 <= dominoes.length <= 4 * 10^4
 * dominoes[i].length == 2
 * 1 <= dominoes[i][j] <= 9
 *
 */
public class NumberOfEquivalentDominoPairs {

    // V0
    // IDEA: COUNTING WITH A NORMALISED KEY
    //   [a, b] and [b, a] are the same domino, so encode it as smaller * 10 + bigger (10..99).
    //   while scanning, count[key] is exactly the number of earlier dominoes that pair with
    //   the current one.
    /**
     * time = O(N)
     * space = O(1) (100 buckets)
     */
    public int numEquivDominoPairs(int[][] dominoes) {
        int[] count = new int[100];
        int pairs = 0;
        for (int[] domino : dominoes) {
            int key = normalisedKey(domino);
            // pair the current domino with every equivalent one seen before it
            pairs += count[key];
            count[key]++;
        }
        return pairs;
    }

    // V1
    // IDEA: COUNT FIRST, THEN SUM C(k, 2) PER GROUP
    //   a group of k equivalent dominoes contributes k * (k - 1) / 2 pairs.
    /**
     * time = O(N)
     * space = O(1) (100 buckets)
     */
    public int numEquivDominoPairs_1(int[][] dominoes) {
        int[] count = new int[100];
        for (int[] domino : dominoes) {
            count[normalisedKey(domino)]++;
        }
        int pairs = 0;
        for (int groupSize : count) {
            pairs += groupSize * (groupSize - 1) / 2;
        }
        return pairs;
    }

    /** Same key for [a, b] and [b, a]: smaller digit first. */
    private int normalisedKey(int[] domino) {
        int smaller = Math.min(domino[0], domino[1]);
        int bigger = Math.max(domino[0], domino[1]);
        return smaller * 10 + bigger;
    }
}
