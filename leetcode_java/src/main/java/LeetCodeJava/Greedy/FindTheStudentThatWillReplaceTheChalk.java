package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-the-student-that-will-replace-the-chalk/description/

/**
 * 1894. Find the Student that Will Replace the Chalk
 * Medium
 *
 * There are n students in a class numbered from 0 to n - 1. The teacher will give
 * each student a problem starting with the student number 0, then the student
 * number 1, and so on until the teacher reaches the student number n - 1. After
 * that, the teacher will restart the process, starting with the student number 0
 * again.
 *
 * You are given a 0-indexed integer array chalk and an integer k. There are
 * initially k pieces of chalk. When the student number i is given a problem to
 * solve, they will use chalk[i] pieces of chalk to solve that problem. However,
 * if the current number of chalk pieces is strictly less than chalk[i], then the
 * student number i will be asked to replace the chalk.
 *
 * Return the index of the student that will replace the chalk pieces.
 *
 *
 * Example 1:
 *
 * Input: chalk = [5,1,5], k = 22
 * Output: 0
 * Explanation: After two full rounds k = 0, so student number 0 does not have
 * enough chalk and will have to replace it.
 *
 * Example 2:
 *
 * Input: chalk = [3,4,1,2], k = 25
 * Output: 1
 * Explanation: After two full rounds k = 5; student 0 uses 3 so k = 2, and
 * student number 1 does not have enough chalk, so they will have to replace it.
 *
 *
 * Constraints:
 *
 * chalk.length == n
 * 1 <= n <= 10^5
 * 1 <= chalk[i] <= 10^5
 * 1 <= k <= 10^9
 *
 */
public class FindTheStudentThatWillReplaceTheChalk {

    // V0
    // IDEA: MODULO THE FULL ROUND, THEN ONE LINEAR SCAN
    /**
     * time = O(N)
     * space = O(1)
     */
    public int chalkReplacer(int[] chalk, int k) {
        // a full round uses sum(chalk), which can reach 10^10 -> long
        long roundCost = 0;
        for (int c : chalk) {
            roundCost += c;
        }

        // only the chalk left at the start of the last, partial round matters
        long remaining = k % roundCost;

        for (int i = 0; i < chalk.length; i++) {
            if (remaining < chalk[i]) {
                return i;
            }
            remaining -= chalk[i];
        }
        // unreachable: remaining < roundCost guarantees someone runs out in the scan
        return 0;
    }
}
