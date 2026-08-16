package LeetCodeJava.Stack;

// https://leetcode.com/problems/basic-calculator-iv/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 770. Basic Calculator IV
 * Hard
 *
 * Given an expression such as expression = "e + 8 - a + 5" and an evaluation map such as
 * {"e": 1} (given in terms of evalvars = ["e"] and evalints = [1]), return a list of
 * tokens representing the simplified expression, such as ["-1*a","14"]
 *
 *   - An expression alternates chunks and symbols, with a space separating each chunk
 *     and symbol.
 *   - A chunk is either an expression in parentheses, a variable, or a non-negative
 *     integer.
 *   - A variable is a string of lowercase letters (not including digits).
 *
 * Expressions are evaluated in the usual order: brackets first, then multiplication,
 * then addition and subtraction.
 *
 *   - For example, expression = "1 + 2 * 3" has an answer of ["7"].
 *
 * The format of the output is as follows:
 *
 *   - For each term of free variables with a non-zero coefficient, we write the free
 *     variables within a term in sorted order lexicographically.
 *   - Terms have degrees equal to the number of free variables being multiplied,
 *     counting multiplicity. We write the largest degree terms of our answer first,
 *     breaking ties by lexicographic order ignoring the leading coefficient of the term.
 *   - The leading coefficient of the term is placed directly to the left with an
 *     asterisk separating it from the variables (if they exist). A leading coefficient
 *     of 1 is still printed.
 *   - An example of a well-formatted answer is
 *     ["-2*a*a*a", "3*a*a*b", "3*b*b", "4*a", "5*c", "-6"].
 *   - Terms (including constant terms) with coefficient 0 are not included.
 *
 * Note: You may assume that the given expression is always valid.
 * All intermediate results will be in the range of [-2^31, 2^31 - 1].
 *
 *
 * Example 1:
 *
 * Input: expression = "e + 8 - a + 5", evalvars = ["e"], evalints = [1]
 * Output: ["-1*a","14"]
 *
 * Example 2:
 *
 * Input: expression = "e - 8 + temperature - pressure",
 *        evalvars = ["e", "temperature"], evalints = [1, 12]
 * Output: ["-1*pressure","5"]
 *
 * Example 3:
 *
 * Input: expression = "(e + 8) * (e - 8)", evalvars = [], evalints = []
 * Output: ["1*e*e","-64"]
 *
 *
 * Constraints:
 *
 * 1 <= expression.length <= 250
 * expression consists of lowercase English letters, digits, '+', '-', '*', '(', ')', ' '.
 * expression does not contain any leading or trailing spaces.
 * All the tokens in expression are separated by a single space.
 * 0 <= evalvars.length <= 100
 * 1 <= evalvars[i].length <= 20
 * evalvars[i] consists of lowercase English letters.
 * evalints.length == evalvars.length
 * -100 <= evalints[i] <= 100
 *
 */
public class BasicCalculator4 {

    // V0
    // IDEA: RECURSIVE DESCENT PARSER + POLYNOMIAL ARITHMETIC
    /**
     *   Represent a polynomial as a map:
     *       {SORTED list of variable names -> coefficient}
     *   e.g.  3*a*b - 5   ->   {["a","b"]: 3, []: -5}
     *
     *   NOTE !!! `List<String>` is used as the map KEY on purpose -- java's List has
     *            value-based equals/hashCode, so two equal variable lists collide
     *            into the same term (an array would NOT).
     *
     *   Terms with coefficient 0 are dropped EAGERLY so they never reach the output.
     *
     *   Grammar (standard precedence):
     *       expr   := term (('+' | '-') term)*
     *       term   := factor ('*' factor)*
     *       factor := '(' expr ')' | variable | integer
     *
     *   Output order: HIGHEST DEGREE first (degree = key size), ties broken by the
     *   LEXICOGRAPHIC order of the variable list.
     *
     *   time  = O(n * T^2), n = expression.length, T = number of distinct terms produced
     *   space = O(n + T)
     */

    private Map<String, Integer> env;
    private String[] tokens;
    private int pos;

    public List<String> basicCalculatorIV(String expression, String[] evalvars, int[] evalints) {
        this.env = new HashMap<>();
        for (int i = 0; i < evalvars.length; i++) {
            env.put(evalvars[i], evalints[i]);
        }

        // PAD the parens so they become standalone tokens, then split on whitespace
        this.tokens = expression.replace("(", " ( ").replace(")", " ) ").trim().split("\\s+");
        this.pos = 0;

        Map<List<String>, Integer> poly = parseExpr();

        // highest degree first, then lexicographic on the variable list
        List<List<String>> keys = new ArrayList<>(poly.keySet());
        keys.sort((a, b) -> {
            if (a.size() != b.size()) {
                return b.size() - a.size();
            }
            for (int i = 0; i < a.size(); i++) {
                int cmp = a.get(i).compareTo(b.get(i));
                if (cmp != 0) {
                    return cmp;
                }
            }
            return 0;
        });

        List<String> res = new ArrayList<>();
        for (List<String> k : keys) {
            StringBuilder sb = new StringBuilder();
            sb.append(poly.get(k));
            for (String v : k) {
                sb.append('*').append(v);
            }
            res.add(sb.toString());
        }
        return res;
    }

    private Map<List<String>, Integer> parseExpr() {
        Map<List<String>, Integer> res = parseTerm();
        while (pos < tokens.length && (tokens[pos].equals("+") || tokens[pos].equals("-"))) {
            String op = tokens[pos];
            pos += 1;
            res = polyAdd(res, parseTerm(), op.equals("+") ? 1 : -1);
        }
        return res;
    }

    private Map<List<String>, Integer> parseTerm() {
        Map<List<String>, Integer> res = parseFactor();
        while (pos < tokens.length && tokens[pos].equals("*")) {
            pos += 1;
            res = polyMul(res, parseFactor());
        }
        return res;
    }

    private Map<List<String>, Integer> parseFactor() {
        String tok = tokens[pos];
        pos += 1;

        if (tok.equals("(")) {
            Map<List<String>, Integer> res = parseExpr();
            pos += 1; // consume the matching ')'
            return res;
        }
        if (Character.isDigit(tok.charAt(0))) {
            return makeConst(Integer.parseInt(tok));
        }
        return makeAtom(tok);
    }

    private Map<List<String>, Integer> makeConst(int v) {
        Map<List<String>, Integer> res = new HashMap<>();
        if (v != 0) {
            res.put(new ArrayList<>(), v);
        }
        return res;
    }

    private Map<List<String>, Integer> makeAtom(String name) {
        // a variable with a KNOWN value collapses to a constant
        if (env.containsKey(name)) {
            return makeConst(env.get(name));
        }
        Map<List<String>, Integer> res = new HashMap<>();
        List<String> key = new ArrayList<>();
        key.add(name);
        res.put(key, 1);
        return res;
    }

    private Map<List<String>, Integer> polyAdd(Map<List<String>, Integer> p,
                                               Map<List<String>, Integer> q, int sign) {
        Map<List<String>, Integer> res = new HashMap<>(p);
        for (Map.Entry<List<String>, Integer> e : q.entrySet()) {
            int v = res.getOrDefault(e.getKey(), 0) + sign * e.getValue();
            if (v == 0) {
                res.remove(e.getKey());
            } else {
                res.put(e.getKey(), v);
            }
        }
        return res;
    }

    private Map<List<String>, Integer> polyMul(Map<List<String>, Integer> p,
                                               Map<List<String>, Integer> q) {
        Map<List<String>, Integer> res = new HashMap<>();
        for (Map.Entry<List<String>, Integer> e1 : p.entrySet()) {
            for (Map.Entry<List<String>, Integer> e2 : q.entrySet()) {
                List<String> key = new ArrayList<>(e1.getKey());
                key.addAll(e2.getKey());
                // variables kept in SORTED order so equal terms share a key
                Collections.sort(key);
                res.put(key, res.getOrDefault(key, 0) + e1.getValue() * e2.getValue());
            }
        }
        res.values().removeIf(c -> c == 0);
        return res;
    }

}
