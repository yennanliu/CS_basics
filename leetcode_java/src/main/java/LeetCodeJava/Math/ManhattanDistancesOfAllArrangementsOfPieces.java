package LeetCodeJava.Math;

// https://leetcode.com/problems/manhattan-distances-of-all-arrangements-of-pieces/description/

/**
 * 3426. Manhattan Distances of All Arrangements of Pieces
 * Hard
 *
 * You are given three integers m, n, and k.
 *
 * There is a rectangular grid of size m × n containing k identical pieces. Return
 * the sum of Manhattan distances between every pair of pieces over all valid
 * arrangements of pieces.
 *
 * A valid arrangement is a placement of all k pieces on the grid with at most one
 * piece per cell.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * The Manhattan Distance between two cells (x_i, y_i) and (x_j, y_j) is |x_i -
 * x_j| + |y_i - y_j|.
 *
 * Example 1:
 *
 * Input: m = 2, n = 2, k = 2
 *
 * Output: 8
 *
 * Explanation:
 *
 * The valid arrangements of pieces on the board are:
 *
 * In the first 4 arrangements, the Manhattan distance between the two pieces is 1.
 * In the last 2 arrangements, the Manhattan distance between the two pieces is 2.
 *
 * Thus, the total Manhattan distance across all valid arrangements is 1 + 1 + 1 +
 * 1 + 2 + 2 = 8.
 *
 * Example 2:
 *
 * Input: m = 1, n = 4, k = 3
 *
 * Output: 20
 *
 * Explanation:
 *
 * The valid arrangements of pieces on the board are:
 *
 * The first and last arrangements have a total Manhattan distance of 1 + 1 + 2 =
 * 4.
 * The middle two arrangements have a total Manhattan distance of 1 + 2 + 3 = 6.
 *
 * The total Manhattan distance between all pairs of pieces across all arrangements
 * is 4 + 6 + 6 + 4 = 20.
 *
 * Constraints:
 *
 * 1 <= m, n <= 10^5
 * 2 <= m * n <= 10^5
 * 2 <= k <= m * n
 *
 */
public class ManhattanDistancesOfAllArrangementsOfPieces {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: SWAP THE SUMMATION ORDER -- EVERY CELL PAIR IS COUNTED THE SAME NUMBER OF TIMES
    //       instead of "for each arrangement, sum over pairs", ask "for each pair of cells, how
    //       many arrangements put pieces on both": the other k - 2 identical pieces go anywhere
    //       among the remaining m*n - 2 cells, so that count is C(mn - 2, k - 2) for EVERY pair.
    //       answer = C(mn - 2, k - 2) * S, where S = sum over unordered cell pairs of distance.
    //       S splits by coordinate:
    //         row part    = n^2 * sum_{i<j} (j - i) = n^2 * C(m + 1, 3)
    //         column part = m^2 * C(n + 1, 3)
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int distanceSum(int m, int n, int k) {
        int total = m * n;

        // factorials and inverse factorials up to m*n, for C(total - 2, k - 2)
        long[] fact = new long[total + 1];
        long[] invFact = new long[total + 1];
        fact[0] = 1;
        for (int i = 1; i <= total; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }
        invFact[total] = modPow(fact[total], MOD - 2);
        for (int i = total; i > 0; i--) {
            invFact[i - 1] = invFact[i] * i % MOD;
        }

        // sum of |x1 - x2| + |y1 - y2| over all unordered cell pairs
        long nSquared = (long) n * n % MOD;
        long mSquared = (long) m * m % MOD;
        long rowPart = nSquared * choose3(m + 1) % MOD;
        long colPart = mSquared * choose3(n + 1) % MOD;
        long pairDistanceSum = (rowPart + colPart) % MOD;

        // how many arrangements contain one fixed pair of cells
        long waysPerPair = comb(total - 2, k - 2, fact, invFact);

        return (int) (pairDistanceSum * waysPerPair % MOD);
    }

    /** C(t, 3) = t * (t - 1) * (t - 2) / 6, modulo MOD. */
    private long choose3(long t) {
        long inverseSix = modPow(6, MOD - 2);
        return t % MOD * ((t - 1) % MOD) % MOD * ((t - 2) % MOD) % MOD * inverseSix % MOD;
    }

    /** C(a, b) modulo MOD from the precomputed factorial tables; 0 when b is out of range. */
    private long comb(int a, int b, long[] fact, long[] invFact) {
        if (b < 0 || b > a) {
            return 0;
        }
        return fact[a] * invFact[b] % MOD * invFact[a - b] % MOD;
    }

    /** base^exp modulo MOD by fast exponentiation. */
    private long modPow(long base, long exp) {
        long result = 1;
        base %= MOD;
        while (exp > 0) {
            // the lowest bit of exp decides whether this power of base is used
            if ((exp & 1) == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exp >>= 1;
        }
        return result;
    }
}
