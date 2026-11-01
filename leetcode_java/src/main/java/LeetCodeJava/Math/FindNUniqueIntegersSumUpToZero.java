package LeetCodeJava.Math;

// https://leetcode.com/problems/find-n-unique-integers-sum-up-to-zero/description/

/**
 * 1304. Find N Unique Integers Sum up to Zero
 * Easy
 *
 * Given an integer n, return any array containing n unique integers such that they add up to 0.
 *
 * Example 1:
 *
 * Input: n = 5
 * Output: [-7,-1,1,3,4]
 * Explanation: These arrays also are accepted [-5,-1,1,2,3] , [-3,-1,2,-2,4].
 *
 * Example 2:
 *
 * Input: n = 3
 * Output: [-1,0,1]
 *
 * Example 3:
 *
 * Input: n = 1
 * Output: [0]
 *
 * Constraints:
 *
 * 1 <= n <= 1000
 *
 */
public class FindNUniqueIntegersSumUpToZero {

    // V0
    // IDEA: MATH - SYMMETRIC +i / -i PAIRS, PLUS A LONE 0 WHEN N IS ODD
    //   each pair sums to 0 and all values stay distinct; if n is odd one slot
    //   is left, and 0 is the only value that keeps the total at 0 without repeating
    /**
     * time = O(N)
     * space = O(N)  (the output)
     */
    public int[] sumZero(int n) {
        int[] result = new int[n];
        int index = 0;
        for (int i = 1; i <= n / 2; i++) {
            result[index] = i;
            result[index + 1] = -i;
            index += 2;
        }
        // when n is odd the last slot keeps its default value, 0
        return result;
    }
}
