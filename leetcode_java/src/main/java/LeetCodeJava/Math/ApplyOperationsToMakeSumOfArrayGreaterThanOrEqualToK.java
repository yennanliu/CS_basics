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

    // V0-1
    // IDEA: BRUTE FORCE (fixed by gemini)
    /**
     * Logic:
     * 1. Try every possible "target value" x for the first element.
     * 2. To reach value x from 1, we need (x - 1) additions.
     * 3. Once we have a value x, we need enough copies to reach sum k.
     * 4. Number of copies needed = ceil(k / x).
     * 5. Number of duplication operations = ceil(k / x) - 1.
     * * Time Complexity: O(sqrt(K)) or O(K). O(K) is safe given K <= 10^5.
     * Space Complexity: O(1).
     */
    public int minOperations_0_1(int k) {
        if (k <= 1)
            return 0;

        int minOp = Integer.MAX_VALUE;

        // x is the value we increase the first element to
        // We only need to check up to k, but theoretically up to sqrt(k) is enough
        for (int x = 1; x <= k; x++) {
            int addOps = x - 1;

            // Calculate duplication operations: ceil(k / x) - 1
            // A trick for ceil(a/b) is (a + b - 1) / b
            /** NOTE !!!  trick for `ceil` calculation:
             *
             * This is a common "trick" in programming to perform **Integer Ceiling Division**.
             *
             * In integer division, most languages (like Java, C++, and Python) automatically **truncate** the result (round down). However, when calculating something like "how many elements do I need to reach a sum," you often need to **round up** (the ceiling).
             *
             * ---
             *
             * ### 1. The Goal:
             *
             * If you have a sum  and each element is , you need  elements.
             *
             * * If  (Exactly 2 elements).
             * * If  (You need **3** elements to reach 11).
             *
             * Standard integer division `11 / 5` gives you `2`. You need a way to get `3`.
             *
             * ---
             *
             * ### 2. How the Formula Works
             *
             * The formula `(a + b - 1) / b` ensures that if there is any remainder at all, it pushes the result to the next whole number.
             *
             * **Case A: Perfectly Divisible**
             * Let :
             *
             *
             *
             * *The "-1" prevents it from jumping to the next number (3) when it's already a perfect fit.*
             *
             * **Case B: Not Perfectly Divisible**
             * Let :
             *
             *
             *
             * *The "+ (x - 1)" adds just enough to the numerator so that any remainder  pushes the division to the next integer.*
             *
             * ---
             *
             * ### 3. Comparison with other methods
             *
             * While you could use floating-point math, it is generally discouraged in competitive programming due to speed and precision issues.
             *
             * | Method | Code | Pros/Cons |
             * | --- | --- | --- |
             * | **Double Cast** | `(int)Math.ceil((double)k / x)` | Slower; risk of precision errors with very large numbers. |
             * | **Manual If** | `(k / x) + (k % x == 0 ? 0 : 1)` | Correct, but more verbose. |
             * | **Integer Trick** | `(k + x - 1) / x` | **Fastest and most concise.** |
             *
             * ---
             *
             * ### Summary Table for `(k + x - 1) / x`
             *
             * |  (Target) |  (Value) | Addition  | Result |
             * | --- | --- | --- | --- |
             * | 12 | 4 | 15 |  |
             * | 13 | 4 | 16 |  |
             * | 16 | 4 | 19 |  |
             *
             *
             */
            int totalElementsNeeded = (k + x - 1) / x;
            int dupOps = totalElementsNeeded - 1;

            /** NOTE here */
            int totalOps = addOps + dupOps;

            // If the total operations start increasing, we've passed the optimal point
            if (totalOps > minOp && x > Math.sqrt(k))
                break;

            minOp = Math.min(minOp, totalOps);
        }

        return minOp;
    }


    // V0-2
    // IDEA: MATH
    // https://buildmoat.teachable.com/courses/7a7af3/lectures/64243726
    /** IDEA:
     *
     * ## 1. English Translation
     *
     * **Q2**
     *
     * * You will definitely want to complete the first operation before starting the second; this is the only way to maximize the growth of the total sum of the elements.
     * * Enumerate how many times the first operation should be performed, then determine how many times the second operation is needed to reach .
     * * Because the final answer will be , you only need to enumerate up to .
     *
     * ---
     *
     * ## 2. Traditional Chinese Transcription
     *
     * **Q2**
     *
     * * 一定會想先做完第一個操作再做第二個操作，這樣才會讓元素總和成長幅度最大
     * * 窮舉第一個操作要做幾次，再來看第二個操作需要做幾次才能
     * * 因為最後出來的答案會是 (op1 + 1) * (op2 + 1) ，只要窮舉到 (k)^(1/2) 就可以了
     *
     *
     *   -> NOTE !!!
     *     - op1: how many 1 to add  (# of op1)
     *     - op2: how many max num to add  (# of op2)
     */
    public int minOperations_0_2(int k) {
        int ans = k;

        // i: biggest element in array
        for (int i = 1; i * i <= k; i++) {
            // j:  # of element in array
            int j = (k - 1) / i + 1;
            ans = Math.min(ans, i + j - 2);
        }

        return ans;
    }


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



}
