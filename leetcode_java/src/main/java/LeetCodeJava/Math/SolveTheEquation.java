package LeetCodeJava.Math;

// https://leetcode.com/problems/solve-the-equation/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 640. Solve the Equation
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Solve a given equation and return the value of 'x' in the form of a string "x=#value". The equation contains only '+', '-' operation, the variable 'x' and its coefficient. You should return "No solution" if there is no solution for the equation, or "Infinite solutions" if there are infinite solutions for the equation.
 *
 * If there is exactly one solution for the equation, we ensure that the value of 'x' is an integer.
 *
 *
 *
 * Example 1:
 *
 * Input: equation = "x+5-3+x=6+x-2"
 * Output: "x=2"
 * Example 2:
 *
 * Input: equation = "x=x"
 * Output: "Infinite solutions"
 * Example 3:
 *
 * Input: equation = "2x=x"
 * Output: "x=0"
 *
 *
 * Constraints:
 *
 * 3 <= equation.length <= 1000
 * equation has exactly one '='.
 * equation consists of integers with an absolute value in the range [0, 100] without any leading zeros, and the variable 'x'.
 * The input is generated that if there is a single solution, it will be an integer.
 *
 *
 */
public class SolveTheEquation {

    // V0
//    public String solveEquation(String equation) {
//
//    }

    // V1-1
    // IDEA: Partioning Coefficients

    // https://leetcode.com/problems/solve-the-equation/editorial/
    /**
     * time = O(N)
     * space = O(1)
     */
    public String coeff(String x) {
        if (x.length() > 1 && x.charAt(x.length() - 2) >= '0' && x.charAt(x.length() - 2) <= '9')
            return x.replace("x", "");
        return x.replace("x", "1");
    }


    /**
     * time = O(N)
     * space = O(1)
     */
    public String solveEquation_1_1(String equation) {
        String[] lr = equation.split("=");
        int lhs = 0, rhs = 0;
        for (String x : breakIt(lr[0])) {
            if (x.indexOf("x") >= 0) {
                lhs += Integer.parseInt(coeff(x));
            } else
                rhs -= Integer.parseInt(x);
        }
        for (String x : breakIt(lr[1])) {
            if (x.indexOf("x") >= 0)
                lhs -= Integer.parseInt(coeff(x));
            else
                rhs += Integer.parseInt(x);
        }
        if (lhs == 0) {
            if (rhs == 0)
                return "Infinite solutions";
            else
                return "No solution";
        }
        return "x=" + rhs / lhs;
    }


    /**
     * time = O(N)
     * space = O(1)
     */
    public List<String> breakIt(String s) {
        List<String> res = new ArrayList<>();
        String r = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '+' || s.charAt(i) == '-') {
                if (r.length() > 0)
                    res.add(r);
                r = "" + s.charAt(i);
            } else
                r += s.charAt(i);
        }
        res.add(r);
        return res;
    }

    // V1-2
    // IDEA: Using regex for spliting

    // https://leetcode.com/problems/solve-the-equation/editorial/
    /**
     * time = O(N)
     * space = O(1)
     */
    public String coeff_1_2(String x) {
        if (x.length() > 1 && x.charAt(x.length() - 2) >= '0' && x.charAt(x.length() - 2) <= '9')
            return x.replace("x", "");
        return x.replace("x", "1");
    }


    /**
     * time = O(N)
     * space = O(1)
     */
    public String solveEquation_1_2(String equation) {
        String[] lr = equation.split("=");
        int lhs = 0, rhs = 0;
        for (String x : lr[0].split("(?=\\+)|(?=-)")) {
            if (x.indexOf("x") >= 0) {

                lhs += Integer.parseInt(coeff_1_2(x));
            } else
                rhs -= Integer.parseInt(x);
        }
        for (String x : lr[1].split("(?=\\+)|(?=-)")) {
            if (x.indexOf("x") >= 0)
                lhs -= Integer.parseInt(coeff_1_2(x));
            else
                rhs += Integer.parseInt(x);
        }
        if (lhs == 0) {
            if (rhs == 0)
                return "Infinite solutions";
            else
                return "No solution";
        } else
            return "x=" + rhs / lhs;
    }

    // V2

}
