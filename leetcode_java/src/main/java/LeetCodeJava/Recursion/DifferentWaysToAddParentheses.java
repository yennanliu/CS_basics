package LeetCodeJava.Recursion;

// https://leetcode.com/problems/different-ways-to-add-parentheses/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 241. Different Ways to Add Parentheses
 * Medium
 * Topics
 * Companies
 * Given a string expression of numbers and operators, return all possible results from computing all the different possible ways to group numbers and operators. You may return the answer in any order.
 *
 * The test cases are generated such that the output values fit in a 32-bit integer and the number of different results does not exceed 104.
 *
 *
 *
 * Example 1:
 *
 * Input: expression = "2-1-1"
 * Output: [0,2]
 * Explanation:
 * ((2-1)-1) = 0
 * (2-(1-1)) = 2
 * Example 2:
 *
 * Input: expression = "2*3-4*5"
 * Output: [-34,-14,-10,-10,10]
 * Explanation:
 * (2*(3-(4*5))) = -34
 * ((2*3)-(4*5)) = -14
 * ((2*(3-4))*5) = -10
 * (2*((3-4)*5)) = -10
 * (((2*3)-4)*5) = 10
 *
 *
 * Constraints:
 *
 * 1 <= expression.length <= 20
 * expression consists of digits and the operator '+', '-', and '*'.
 * All the integer values in the input expression are in the range [0, 99].
 * The integer values in the input expression do not have a leading '-' or '+' denoting the sign.
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 334.6K
 * Submissions
 * 465.3K
 * Acceptance Rate
 * 71.9%
 *
 *
 */
public class DifferentWaysToAddParentheses {

    // V0
//    public List<Integer> diffWaysToCompute(String expression) {
//
//    }

    // V1-1
    // https://leetcode.com/problems/different-ways-to-add-parentheses/editorial/
    // IDEA: Recursion
    /**
     * time = O(N)
     * space = O(H)
     */
    public List<Integer> diffWaysToCompute_1_1(String expression) {
        List<Integer> results = new ArrayList<>();

        // Base case: if the string is empty, return an empty list
        if (expression.length() == 0)
            return results;

        // Base case: if the string is a single character, treat it as a number and
        // return it
        if (expression.length() == 1) {
            results.add(Integer.parseInt(expression));
            return results;
        }

        // If the string has only two characters and the first character is a digit,
        // parse it as a number
        if (expression.length() == 2 && Character.isDigit(expression.charAt(0))) {
            results.add(Integer.parseInt(expression));
            return results;
        }

        // Recursive case: iterate through each character
        for (int i = 0; i < expression.length(); i++) {
            char currentChar = expression.charAt(i);

            // Skip if the current character is a digit
            if (Character.isDigit(currentChar))
                continue;

            // Split the expression into left and right parts
            List<Integer> leftResults = diffWaysToCompute_1_1(
                    expression.substring(0, i));
            List<Integer> rightResults = diffWaysToCompute_1_1(
                    expression.substring(i + 1));

            // Combine results from left and right parts
            for (int leftValue : leftResults) {
                for (int rightValue : rightResults) {
                    int computedResult = 0;

                    // Perform the operation based on the current character
                    switch (currentChar) {
                        case '+':
                            computedResult = leftValue + rightValue;
                            break;
                        case '-':
                            computedResult = leftValue - rightValue;
                            break;
                        case '*':
                            computedResult = leftValue * rightValue;
                            break;
                    }

                    results.add(computedResult);
                }
            }
        }

        return results;
    }


    // V1-2
    // https://leetcode.com/problems/different-ways-to-add-parentheses/editorial/
    // IDEA: Memoization
    /**
     * time = O(N)
     * space = O(H)
     */
    public List<Integer> diffWaysToCompute_1_2(String expression) {
        // Initialize memoization array to store results of subproblems
        List<Integer>[][] memo = new ArrayList[expression.length()][expression.length()];
        // Solve for the entire expression
        return computeResults(expression, memo, 0, expression.length() - 1);
    }

    private List<Integer> computeResults(
            String expression,
            List<Integer>[][] memo,
            int start,
            int end) {
        // If result is already memoized, return it
        if (memo[start][end] != null) {
            return memo[start][end];
        }

        List<Integer> results = new ArrayList<>();

        // Base case: single digit
        if (start == end) {
            results.add(expression.charAt(start) - '0');
            return results;
        }

        // Base case: two-digit number
        if (end - start == 1 && Character.isDigit(expression.charAt(start))) {
            int tens = expression.charAt(start) - '0';
            int ones = expression.charAt(end) - '0';
            results.add(10 * tens + ones);
            return results;
        }

        // Recursive case: split the expression at each operator
        for (int i = start; i <= end; i++) {
            char currentChar = expression.charAt(i);
            if (Character.isDigit(currentChar)) {
                continue;
            }

            List<Integer> leftResults = computeResults(
                    expression,
                    memo,
                    start,
                    i - 1);
            List<Integer> rightResults = computeResults(
                    expression,
                    memo,
                    i + 1,
                    end);

            // Combine results from left and right subexpressions
            for (int leftValue : leftResults) {
                for (int rightValue : rightResults) {
                    switch (currentChar) {
                        case '+':
                            results.add(leftValue + rightValue);
                            break;
                        case '-':
                            results.add(leftValue - rightValue);
                            break;
                        case '*':
                            results.add(leftValue * rightValue);
                            break;
                    }
                }
            }
        }

        // Memoize the result for this subproblem
        memo[start][end] = results;

        return results;
    }


    // V1-3
    // https://leetcode.com/problems/different-ways-to-add-parentheses/editorial/
    // IDEA: Tabulation

    /**
     * time = O(N)
     * space = O(H)
     */
    public List<Integer> diffWaysToCompute(String expression) {
        int n = expression.length();
        // Create a 2D array of lists to store results of subproblems
        List<Integer>[][] dp = new ArrayList[n][n];

        initializeBaseCases(expression, dp);

        // Fill the dp table for all possible subexpressions
        for (int length = 3; length <= n; length++) {
            for (int start = 0; start + length - 1 < n; start++) {
                int end = start + length - 1;
                processSubexpression(expression, dp, start, end);
            }
        }

        // Return the results for the entire expression
        return dp[0][n - 1];
    }

    private void initializeBaseCases(String expression, List<Integer>[][] dp) {
        int n = expression.length();
        // Initialize the dp array with empty lists
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = new ArrayList<>();
            }
        }

        // Handle base cases: single digits and two-digit numbers
        for (int i = 0; i < n; i++) {
            if (Character.isDigit(expression.charAt(i))) {
                // Check if it's a two-digit number
                int dig1 = expression.charAt(i) - '0';
                if (i + 1 < n && Character.isDigit(expression.charAt(i + 1))) {
                    int dig2 = expression.charAt(i + 1) - '0';
                    int number = dig1 * 10 + dig2;
                    dp[i][i + 1].add(number);
                }
                // Single digit case
                dp[i][i].add(dig1);
            }
        }
    }

    private void processSubexpression(
            String expression,
            List<Integer>[][] dp,
            int start,
            int end) {
        // Try all possible positions to split the expression
        for (int split = start; split <= end; split++) {
            if (Character.isDigit(expression.charAt(split)))
                continue;

            List<Integer> leftResults = dp[start][split - 1];
            List<Integer> rightResults = dp[split + 1][end];

            computeResults(
                    expression.charAt(split),
                    leftResults,
                    rightResults,
                    dp[start][end]);
        }
    }

    private void computeResults(
            char op,
            List<Integer> leftResults,
            List<Integer> rightResults,
            List<Integer> results) {
        // Compute results based on the operator at position 'split'
        for (int leftValue : leftResults) {
            for (int rightValue : rightResults) {
                switch (op) {
                    case '+':
                        results.add(leftValue + rightValue);
                        break;
                    case '-':
                        results.add(leftValue - rightValue);
                        break;
                    case '*':
                        results.add(leftValue * rightValue);
                        break;
                }
            }
        }
    }

    // V2
    // IDEA:  divide-and-conquer , Memoization (gpt)
    /**
     * time = O(N)
     * space = O(H)
     */
    public List<Integer> diffWaysToCompute_2(String expression) {
        // Use memoization to avoid redundant computations
        Map<String, List<Integer>> memo = new HashMap<>();
        return compute(expression, memo);
    }

    private List<Integer> compute(String expression, Map<String, List<Integer>> memo) {
        // Check if result is already computed for this expression
        if (memo.containsKey(expression)) {
            return memo.get(expression);
        }

        List<Integer> result = new ArrayList<>();

        // Split the expression by operators
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == '+' || c == '-' || c == '*') {
                // Divide: split into left and right subexpressions
                String left = expression.substring(0, i);
                String right = expression.substring(i + 1);

                // Conquer: recursively compute the results of left and right subexpressions
                List<Integer> leftResults = compute(left, memo);
                List<Integer> rightResults = compute(right, memo);

                // Combine: calculate all possible results for the current operator
                for (int l : leftResults) {
                    for (int r : rightResults) {
                        if (c == '+') {
                            result.add(l + r);
                        } else if (c == '-') {
                            result.add(l - r);
                        } else if (c == '*') {
                            result.add(l * r);
                        }
                    }
                }
            }
        }

        // Base case: if the expression is a single number, add it to the result
        if (result.isEmpty()) {
            result.add(Integer.parseInt(expression));
        }

        // Store the result in the memo map
        memo.put(expression, result);

        return result;
    }

}
