package LeetCodeJava.Math;

// https://leetcode.com/problems/apply-operations-to-make-sum-of-array-greater-than-or-equal-to-k/description/
/**
 *  3091. Apply Operations to Make Sum of Array Greater Than or Equal to k
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a positive integer k. Initially, you have an array nums = [1].
 *
 * You can perform any of the following operations on the array any number of times (possibly zero):
 *
 * Choose any element in the array and increase its value by 1.
 * Duplicate any element in the array and add it to the end of the array.
 * Return the minimum number of operations required to make the sum of elements of the final array greater than or equal to k.
 *
 *
 *
 * Example 1:
 *
 * Input: k = 11
 *
 * Output: 5
 *
 * Explanation:
 *
 * We can do the following operations on the array nums = [1]:
 *
 * Increase the element by 1 three times. The resulting array is nums = [4].
 * Duplicate the element two times. The resulting array is nums = [4,4,4].
 * The sum of the final array is 4 + 4 + 4 = 12 which is greater than or equal to k = 11.
 * The total number of operations performed is 3 + 2 = 5.
 *
 * Example 2:
 *
 * Input: k = 1
 *
 * Output: 0
 *
 * Explanation:
 *
 * The sum of the original array is already greater than or equal to 1, so no operations are needed.
 *
 *
 *
 * Constraints:
 *
 * 1 <= k <= 105
 */
public class ApplyOperationsToMakeSumOfArrayGreaterThanOrEqualToK {

    // V0
//    public int minOperations(int k) {
//
//    }

    // V1
    // IDEA: MATH
    // https://leetcode.com/problems/apply-operations-to-make-sum-of-array-greater-than-or-equal-to-k/solutions/4916808/javacpython-o1-solution-by-lee215-iapp/
    /**
     *  IDEA:
     *
     *  Intuition
     * We will increment a times to a + 1,
     * and then copy b timees to sum,
     * so that (a + 1) * (b + 1) >= k.
     *
     * So the question is:
     * Find min(a + b), so that (a + 1) * (b + 1) >= k.
     *
     *
     * Intuition 2
     * For same value of a + b,
     * to make ab biggest,
     * we need to make a == b.
     * So if a == b, (b + 1) * (b + 1) >= k
     * So if a == b + 1, a * (a + 1) >= k.
     *
     *
     * Explanation
     * int a = sqrt(k).
     * if a * a == k, then return a + a - 2
     * if a * (a + 1) >= k, then return a + a - 1
     * if (a + 1) * (a + 1) >= k, then return a + a
     *
     *
     * Explanation 2
     * b = ceil(k / a)
     * with interger division:, b = (k - 1) / a + 1
     * then we return a + b - 2
     *
     *
     * Complexity
     * Time O(sqrt)
     * Space O(sqrt)
     *
     */
    public int minOperations_1(int k) {
        int a = (int) Math.sqrt(k);
        return a + (k - 1) / a - 1;
    }

    // V2
    // IDEA: MATH, GREEDY
    // https://leetcode.com/problems/apply-operations-to-make-sum-of-array-greater-than-or-equal-to-k/solutions/4916835/step-step-easy-video-solution-maths-gree-o5v2/
    public int minOperations_2(int k) {
        int ans = Integer.MAX_VALUE;
        for (int i = 1; i <= k; i++) {
            int m = (int) Math.ceil(k / (double) i);
            ans = Math.min(ans, i - 2 + m);
        }
        return ans;
    }


    // V3
    // IDEA: MATH
    // https://leetcode.com/problems/apply-operations-to-make-sum-of-array-greater-than-or-equal-to-k/solutions/4920227/python-3-2-lines-calculus-problem-ts-96-hr9y8/
    public int minOperations_3(int k) {

        int sqt = (int) Math.sqrt(k);
        return sqt - 1 + (k - 1) / sqt;
    }


    // V4


}
