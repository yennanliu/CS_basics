package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/the-score-of-students-solving-math-expression/description/

import java.util.*;

/**
 * 2019. The Score of Students Solving Math Expression
 * Hard
 *
 * You are given a string s that contains digits 0-9, addition symbols '+', and
 * multiplication symbols '*' only, representing a valid math expression of single digit
 * numbers (e.g., 3+5*2). This expression was given to n elementary school students. The
 * students were instructed to get the answer of the expression by following this order
 * of operations:
 *
 * Compute multiplication, reading from left to right; Then,
 * Compute addition, reading from left to right.
 *
 * You are given an integer array answers of length n, which are the submitted answers of
 * the students in no particular order. You are asked to grade the answers, by following
 * these rules:
 *
 * If an answer equals the correct answer of the expression, this student will be
 * rewarded 5 points;
 * Otherwise, if the answer could be interpreted as if the student applied the operators
 * in the wrong order but had correct arithmetic, this student will be rewarded 2 points;
 * Otherwise, this student will be rewarded 0 points.
 *
 * Return the sum of the points of the students.
 *
 *
 * Example 1:
 *
 * Input: s = "7+3*1*2", answers = [20,13,42]
 * Output: 7
 * Explanation: The correct answer of the expression is 13, therefore one student is
 * rewarded 5 points: [20,13,42]
 * A student might have applied the operators in this order: ((7+3)*1)*2 = 20. Therefore
 * one student is rewarded 2 points: [20,13,42]
 * The points for the students are: [2,5,0]. The sum of the points is 2+5+0=7.
 *
 * Example 2:
 *
 * Input: s = "3+5*2", answers = [13,0,10,13,13,16,16]
 * Output: 19
 * Explanation: The correct answer of the expression is 13, therefore three students are
 * rewarded 5 points each: [13,0,10,13,13,16,16]
 * A student might have applied the operators in this order: ((3+5)*2 = 16. Therefore two
 * students are rewarded 2 points: [13,0,10,13,13,16,16]
 * The points for the students are: [5,0,0,5,5,2,2]. The sum of the points is
 * 5+0+0+5+5+2+2=19.
 *
 * Example 3:
 *
 * Input: s = "6+0*1", answers = [12,9,6,4,8,6]
 * Output: 10
 * Explanation: The correct answer of the expression is 6.
 * If a student had incorrectly done (6+0)*1, the answer would also be 6.
 * By the rules of grading, the students will still be rewarded 5 points (as they got the
 * correct answer), not 2 points.
 * The points for the students are: [0,0,5,0,0,5]. The sum of the points is 10.
 *
 *
 * Constraints:
 *
 * 3 <= s.length <= 31
 * s represents a valid expression that contains only digits 0-9, '+', and '*' only.
 * All the integer operands in the expression are in the inclusive range [0, 9].
 * 1 <= The count of all operators ('+' and '*') in the math expression <= 15
 * Test data are generated such that the correct answer of the expression is in the range
 * of [0, 1000].
 * answers.length == n
 * 1 <= n <= 10^4
 * 0 <= answers[i] <= 1000
 *
 */
public class TheScoreOfStudentsSolvingMathExpression {

    /** Answers are at most 1000, and a value above that can never shrink back down. */
    private static final int LIMIT = 1000;

    // V0
    // IDEA: INTERVAL DP OVER EVERY PARENTHESIZATION + CORRECT EVALUATION WITH A STACK
    /**
     *  DP def:
     *     - possible[i][j][v] = true if operands i..j can evaluate to v under SOME
     *                           parenthesization (v <= LIMIT)
     *
     *  DP eq: split on every operator k between operand i and operand j
     *     possible[i][j][combine(a, ops[k], b)] = true
     *         for every a in possible[i][k], b in possible[k+1][j]
     *
     *  Init: possible[i][i][nums[i]] = true.
     *  Prune: drop values > LIMIT (no operator here can decrease a value).
     *
     *  Grading: 5 for the true value, else 2 if possible[0][n-1][answer], else 0.
     *
     *  time = O(N^3 * V^2), N = operands (<= 16), V = LIMIT + 1
     *  space = O(N^2 * V)
     */
    public int scoreOfStudents(String s, int[] answers) {
        // operands sit at even indices, operators at odd indices
        int n = (s.length() + 1) / 2;
        int[] nums = new int[n];
        char[] ops = new char[n - 1];
        for (int i = 0; i < n; i++) {
            nums[i] = s.charAt(2 * i) - '0';
        }
        for (int i = 0; i < n - 1; i++) {
            ops[i] = s.charAt(2 * i + 1);
        }

        int correct = evaluateWithPrecedence(nums, ops);

        boolean[][][] possible = new boolean[n][n][LIMIT + 1];
        for (int i = 0; i < n; i++) {
            possible[i][i][nums[i]] = true;
        }

        // grow intervals from short to long so both halves are always ready
        for (int length = 2; length <= n; length++) {
            for (int i = 0; i + length - 1 < n; i++) {
                int j = i + length - 1;
                // the LAST operator applied is ops[k], joining [i..k] and [k+1..j]
                for (int k = i; k < j; k++) {
                    for (int a = 0; a <= LIMIT; a++) {
                        if (!possible[i][k][a]) {
                            continue;
                        }
                        for (int b = 0; b <= LIMIT; b++) {
                            if (!possible[k + 1][j][b]) {
                                continue;
                            }
                            int value = (ops[k] == '*') ? a * b : a + b;
                            if (value <= LIMIT) {
                                possible[i][j][value] = true;
                            }
                        }
                    }
                }
            }
        }

        int total = 0;
        for (int answer : answers) {
            if (answer == correct) {
                total += 5;
            } else if (answer <= LIMIT && possible[0][n - 1][answer]) {
                total += 2;
            }
        }
        return total;
    }

    /** Multiplication first, then addition: keep a running list of additive terms. */
    private int evaluateWithPrecedence(int[] nums, char[] ops) {
        Deque<Integer> terms = new ArrayDeque<>();
        terms.push(nums[0]);
        for (int i = 0; i < ops.length; i++) {
            if (ops[i] == '*') {
                // '*' binds tighter: fold into the current term
                terms.push(terms.pop() * nums[i + 1]);
            } else {
                // '+' starts a new term
                terms.push(nums[i + 1]);
            }
        }
        int sum = 0;
        for (int term : terms) {
            sum += term;
        }
        return sum;
    }
}
