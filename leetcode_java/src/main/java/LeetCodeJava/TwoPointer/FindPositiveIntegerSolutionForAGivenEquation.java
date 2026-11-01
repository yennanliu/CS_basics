package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/find-positive-integer-solution-for-a-given-equation/description/

import java.util.*;

/**
 * 1237. Find Positive Integer Solution for a Given Equation
 * Medium
 *
 * Given a callable function f(x, y) with a hidden formula and a value z, reverse engineer the
 * formula and return all positive integer pairs x and y where f(x,y) == z. You may return the
 * pairs in any order.
 *
 * While the exact formula is hidden, the function is monotonically increasing, i.e.:
 *
 * f(x, y) < f(x + 1, y)
 * f(x, y) < f(x, y + 1)
 *
 * The function interface is defined like this:
 *
 * interface CustomFunction {
 * public:
 *   // Returns some positive integer f(x, y) for two positive integers x and y based on a formula.
 *   int f(int x, int y);
 * };
 *
 * We will judge your solution as follows:
 *
 * The judge has a list of 9 hidden implementations of CustomFunction, along with a way to
 * generate an answer key of all valid pairs for a specific z.
 * The judge will receive two inputs: a function_id (to determine which implementation to test
 * your code with), and the target z.
 * The judge will call your findSolution and compare your results with the answer key.
 * If your results match the answer key, your solution will be Accepted.
 *
 *
 * Example 1:
 *
 * Input: function_id = 1, z = 5
 * Output: [[1,4],[2,3],[3,2],[4,1]]
 * Explanation: The hidden formula for function_id = 1 is f(x, y) = x + y.
 * The following positive integer values of x and y make f(x, y) equal to 5:
 * x=1, y=4 -> f(1, 4) = 1 + 4 = 5.
 * x=2, y=3 -> f(2, 3) = 2 + 3 = 5.
 * x=3, y=2 -> f(3, 2) = 3 + 2 = 5.
 * x=4, y=1 -> f(4, 1) = 4 + 1 = 5.
 *
 * Example 2:
 *
 * Input: function_id = 2, z = 5
 * Output: [[1,5],[5,1]]
 * Explanation: The hidden formula for function_id = 2 is f(x, y) = x * y.
 * The following positive integer values of x and y make f(x, y) equal to 5:
 * x=1, y=5 -> f(1, 5) = 1 * 5 = 5.
 * x=5, y=1 -> f(5, 1) = 5 * 1 = 5.
 *
 *
 * Constraints:
 *
 * 1 <= function_id <= 9
 * 1 <= z <= 100
 * It is guaranteed that the solutions of f(x, y) == z will be in the range 1 <= x, y <= 1000.
 * It is also guaranteed that f(x, y) will fit in 32 bit signed integer if 1 <= x, y <= 1000.
 *
 */
public class FindPositiveIntegerSolutionForAGivenEquation {

    // dummy API for passing java syntax check
    // offered by LC platform when submission
    public interface CustomFunction {
        int f(int x, int y);
    }

    private static final int MAX_VALUE = 1000;

    // V0
    // IDEA: TWO POINTERS FROM THE CORNER OF A SORTED MATRIX
    //   picture f as a 1000 x 1000 matrix increasing along both axes; start at x = 1, y = 1000:
    //     f(x, y) < z  -> every smaller y is smaller still, so this x is done: x++
    //     f(x, y) > z  -> every bigger x is bigger still, so this y is done: y--
    //     f(x, y) == z -> record it; each x has at most one matching y, so move both
    /**
     * time = O(N), N = 1000
     * space = O(1) (excluding the output)
     */
    public List<List<Integer>> findSolution(CustomFunction customfunction, int z) {
        List<List<Integer>> result = new ArrayList<>();
        int x = 1;
        int y = MAX_VALUE;
        while (x <= MAX_VALUE && y >= 1) {
            int value = customfunction.f(x, y);
            if (value < z) {
                x++;
            } else if (value > z) {
                y--;
            } else {
                result.add(Arrays.asList(x, y));
                x++;
                y--;
            }
        }
        return result;
    }

    // V1
    // IDEA: ENUMERATE x, BINARY SEARCH THE SMALLEST y WITH f(x, y) >= z
    /**
     * time = O(N log N), N = 1000
     * space = O(1) (excluding the output)
     */
    public List<List<Integer>> findSolution_1(CustomFunction customfunction, int z) {
        List<List<Integer>> result = new ArrayList<>();
        for (int x = 1; x <= MAX_VALUE; x++) {
            int left = 1;
            int right = MAX_VALUE;
            // lower bound: first y where f(x, y) >= z
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (customfunction.f(x, mid) >= z) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            if (customfunction.f(x, left) == z) {
                result.add(Arrays.asList(x, left));
            }
        }
        return result;
    }
}
