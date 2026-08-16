package LeetCodeJava.Stack;

// https://leetcode.com/problems/number-of-atoms/description/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.TreeMap;

/**
 * 726. Number of Atoms
 * Hard
 *
 * Given a string formula representing a chemical formula, return the count of each atom.
 *
 * The atomic element always starts with an uppercase character, then zero or more
 * lowercase letters, representing the name.
 *
 * One or more digits representing that element's count may follow if the count is
 * greater than 1. If the count is 1, no digits will follow.
 *
 *   - For example, "H2O" and "H2O2" are possible, but "H1O2" is impossible.
 *
 * Two formulas are concatenated together to produce another formula.
 *
 *   - For example, "H2O2He3Mg4" is also a formula.
 *
 * A formula placed in parentheses, and a count (optionally added) is also a formula.
 *
 *   - For example, "(H2O2)" and "(H2O2)3" are formulas.
 *
 * Return the count of all elements as a string in the following form: the first name
 * (in sorted order), followed by its count (if that count is more than 1), followed by
 * the second name (in sorted order), followed by its count (if that count is more than
 * 1), and so on.
 *
 * The test cases are generated so that all the values in the output fit in a 32-bit
 * integer.
 *
 *
 * Example 1:
 *
 * Input: formula = "H2O"
 * Output: "H2O"
 * Explanation: The count of elements are {'H': 2, 'O': 1}.
 *
 * Example 2:
 *
 * Input: formula = "Mg(OH)2"
 * Output: "H2MgO2"
 * Explanation: The count of elements are {'H': 2, 'Mg': 1, 'O': 2}.
 *
 * Example 3:
 *
 * Input: formula = "K4(ON(SO3)2)2"
 * Output: "K4N2O14S4"
 * Explanation: The count of elements are {'K': 4, 'N': 2, 'O': 14, 'S': 4}.
 *
 *
 * Constraints:
 *
 * 1 <= formula.length <= 1000
 * formula consists of English letters, digits, '(', and ')'.
 * formula is always valid.
 *
 */
public class NumberOfAtoms {

    // V0
    // IDEA: STACK OF COUNTERS
    /**
     *   Keep a STACK whose top is the counter for the INNERMOST group currently open.
     *
     *     - '('  -> PUSH a fresh counter
     *     - ')'  -> read the multiplier that follows, POP the top counter,
     *               and FOLD it (times the multiplier) into the new top
     *     - atom -> read `Uppercase + lowercase*` then the OPTIONAL digit run,
     *               and add it to the top counter
     *
     *   At the end the bottom counter holds the totals.
     *
     *   NOTE !!! a TreeMap keeps the atom names SORTED, which is exactly the
     *            output order the problem asks for -- no extra sort needed.
     *
     *   time  = O(n^2) worst case (n = formula.length)
     *           -- merging a popped group is O(distinct atoms)
     *   space = O(n)
     */
    public String countOfAtoms(String formula) {
        int n = formula.length();

        Deque<TreeMap<String, Integer>> stack = new ArrayDeque<>();
        stack.push(new TreeMap<>());

        int i = 0;
        while (i < n) {
            char c = formula.charAt(i);

            if (c == '(') {
                stack.push(new TreeMap<>());
                i += 1;

            } else if (c == ')') {
                i += 1;
                // digits right after ')' are the GROUP multiplier (absent -> 1)
                int start = i;
                while (i < n && Character.isDigit(formula.charAt(i))) {
                    i += 1;
                }
                int mult = i > start ? Integer.parseInt(formula.substring(start, i)) : 1;

                TreeMap<String, Integer> group = stack.pop();
                TreeMap<String, Integer> top = stack.peek();
                for (Map.Entry<String, Integer> e : group.entrySet()) {
                    top.put(e.getKey(), top.getOrDefault(e.getKey(), 0) + e.getValue() * mult);
                }

            } else {
                // atom name: ONE uppercase letter then any lowercase letters
                int start = i;
                i += 1;
                while (i < n && Character.isLowerCase(formula.charAt(i))) {
                    i += 1;
                }
                String name = formula.substring(start, i);

                // OPTIONAL count (absent -> 1)
                start = i;
                while (i < n && Character.isDigit(formula.charAt(i))) {
                    i += 1;
                }
                int cnt = i > start ? Integer.parseInt(formula.substring(start, i)) : 1;

                TreeMap<String, Integer> top = stack.peek();
                top.put(name, top.getOrDefault(name, 0) + cnt);
            }
        }

        StringBuilder out = new StringBuilder();
        for (Map.Entry<String, Integer> e : stack.peek().entrySet()) {
            out.append(e.getKey());
            if (e.getValue() > 1) {
                out.append(e.getValue());
            }
        }
        return out.toString();
    }

}
