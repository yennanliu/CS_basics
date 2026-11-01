package LeetCodeJava.Math;

// https://leetcode.com/problems/most-frequent-prime/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 3044. Most Frequent Prime
 * Medium
 *
 * You are given a m x n 0-indexed 2D matrix mat. From every cell, you can create numbers in the following way:
 *
 * There could be at most 8 paths from the cells namely: east, south-east, south, south-west, west,
 * north-west, north, and north-east.
 * Select a path from them and append digits in this path to the number being formed by traveling in this direction.
 * Note that numbers are generated at every step, for example, if the digits along the path are 1, 9, 1,
 * then there will be three numbers generated along the way: 1, 19, 191.
 *
 * Return the most frequent prime number greater than 10 out of all the numbers created by traversing
 * the matrix or -1 if no such prime number exists. If there are multiple prime numbers with the highest
 * frequency, then return the largest of them.
 *
 * Note: It is invalid to change the direction during the search.
 *
 *
 * Example 1:
 *
 * Input: mat = [[1,1],[9,9],[1,1]]
 * Output: 19
 * Explanation:
 * From cell (0,0) there are 3 possible directions and the numbers greater than 10 which can be created
 * in those directions are:
 * East: [11], South-East: [19], South: [19,191].
 * Numbers greater than 10 created are [11, 19, 19, 191].
 * Similarly, for other cells the numbers created are all present in the array [11, 19, 19, 191].
 * As 19 is the most frequent prime, we return 19.
 *
 * Example 2:
 *
 * Input: mat = [[7]]
 * Output: -1
 * Explanation: The only number which can be formed is 7. It is a prime smaller than 10, so we return -1.
 *
 * Example 3:
 *
 * Input: mat = [[9,7,8],[4,6,5],[2,8,6]]
 * Output: 97
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 1 <= m, n <= 6
 * 1 <= mat[i][j] <= 9
 *
 */
public class MostFrequentPrime {

    // V0
    // IDEA: ENUMERATE EVERY RAY (CELL x 8 DIRECTIONS) + TRIAL-DIVISION PRIMALITY + HASHMAP COUNT
    //   the grid is at most 6x6, so there are < 2000 candidate numbers, each below 10^6.
    /**
     * time = O(M * N * max(M, N) * sqrt(V))
     * space = O(P)
     */
    public int mostFrequentPrime(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;
        int[][] directions = {
                {0, 1}, {1, 1}, {1, 0}, {1, -1},
                {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}
        };

        // prime value -> how many times it was generated
        Map<Integer, Integer> primeCount = new HashMap<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int[] dir : directions) {
                    int value = 0;
                    int r = i;
                    int c = j;
                    // walk the ray, appending one digit per step; every prefix is a generated number
                    while (r >= 0 && r < rows && c >= 0 && c < cols) {
                        value = value * 10 + mat[r][c];
                        if (value > 10 && isPrime(value)) {
                            primeCount.put(value, primeCount.getOrDefault(value, 0) + 1);
                        }
                        r += dir[0];
                        c += dir[1];
                    }
                }
            }
        }

        // pick the highest frequency; ties go to the larger prime
        int best = -1;
        int bestFrequency = 0;
        for (Map.Entry<Integer, Integer> entry : primeCount.entrySet()) {
            int prime = entry.getKey();
            int frequency = entry.getValue();
            if (frequency > bestFrequency || (frequency == bestFrequency && prime > best)) {
                best = prime;
                bestFrequency = frequency;
            }
        }
        return best;
    }

    /** Trial division up to sqrt(x). */
    private boolean isPrime(int x) {
        if (x < 2) {
            return false;
        }
        if (x % 2 == 0) {
            return x == 2;
        }
        for (int d = 3; d * d <= x; d += 2) {
            if (x % d == 0) {
                return false;
            }
        }
        return true;
    }
}
