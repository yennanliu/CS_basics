package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-number-of-ones/description/

import java.util.*;

/**
 * 1183. Maximum Number of Ones
 * Hard
 *
 * Consider a matrix M with dimensions width * height, such that every cell has value 0 or 1,
 * and any square sub-matrix of M of size sideLength * sideLength has at most maxOnes ones.
 *
 * Return the maximum possible number of ones that the matrix M can have.
 *
 *
 * Example 1:
 *
 * Input: width = 3, height = 3, sideLength = 2, maxOnes = 1
 * Output: 4
 * Explanation:
 * In a 3*3 matrix, no 2*2 sub-matrix can have more than 1 one.
 * The best solution that has 4 ones is:
 * [1,0,1]
 * [0,0,0]
 * [1,0,1]
 *
 * Example 2:
 *
 * Input: width = 3, height = 3, sideLength = 2, maxOnes = 2
 * Output: 6
 * Explanation:
 * [1,0,1]
 * [1,0,1]
 * [1,0,1]
 *
 *
 * Constraints:
 *
 * 1 <= width, height <= 100
 * 1 <= sideLength <= width, height
 * 0 <= maxOnes <= sideLength * sideLength
 *
 */
public class MaximumNumberOfOnes {

    // V0
    // IDEA: GREEDY OVER RESIDUE CLASSES (i % L, j % L): turn on the maxOnes most populated classes
    /**
     * time = O(W * H + L^2 log L)
     * space = O(L^2)
     */
    public int maximumNumberOfOnes(int width, int height, int sideLength, int maxOnes) {
        int side = sideLength;

        // every L x L window holds exactly one cell of each residue class (i % L, j % L),
        // so an optimal matrix repeats with period L: a class is either all ones or all zeros.
        // count how many real cells each class owns
        int[] classSize = new int[side * side];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                classSize[(i % side) * side + (j % side)]++;
            }
        }

        // at most maxOnes classes may be switched on: pick the biggest ones
        Arrays.sort(classSize);
        int total = 0;
        for (int k = 0; k < maxOnes; k++) {
            total += classSize[classSize.length - 1 - k];
        }
        return total;
    }
}
