package LeetCodeJava.Stack;

// https://leetcode.com/problems/number-of-atoms/description/

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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


    // V1
    // IDEA: RECURSIVE DESCENT PARSER (grammar instead of a stack)
    /**
     *  Write the formula's grammar out and let the call stack replace the explicit
     *  one:
     *      formula := ( atom | '(' formula ')' ) count?  ...
     *
     *  The recursion mirrors the nesting directly, so there is no `pop and fold`
     *  step to get wrong.
     *
     *  time  = O(n^2) worst case
     *  space = O(n) recursion depth
     */
    private int pos726;

    public String countOfAtoms_1(String formula) {
        this.pos726 = 0;
        Map<String, Integer> total = parseFormula(formula);

        StringBuilder out = new StringBuilder();
        for (Map.Entry<String, Integer> e : new TreeMap<>(total).entrySet()) {
            out.append(e.getKey());
            if (e.getValue() > 1) {
                out.append(e.getValue());
            }
        }
        return out.toString();
    }

    private Map<String, Integer> parseFormula(String s) {
        Map<String, Integer> acc = new HashMap<>();

        while (pos726 < s.length() && s.charAt(pos726) != ')') {
            Map<String, Integer> unit;

            if (s.charAt(pos726) == '(') {
                pos726 += 1;                    // consume '('
                unit = parseFormula(s);
                pos726 += 1;                    // consume ')'
            } else {
                int start = pos726++;
                while (pos726 < s.length() && Character.isLowerCase(s.charAt(pos726))) {
                    pos726 += 1;
                }
                unit = new HashMap<>();
                unit.put(s.substring(start, pos726), 1);
            }

            int mult = parseCount(s);
            for (Map.Entry<String, Integer> e : unit.entrySet()) {
                acc.merge(e.getKey(), e.getValue() * mult, Integer::sum);
            }
        }
        return acc;
    }

    private int parseCount(String s) {
        int start = pos726;
        while (pos726 < s.length() && Character.isDigit(s.charAt(pos726))) {
            pos726 += 1;
        }
        return pos726 > start ? Integer.parseInt(s.substring(start, pos726)) : 1;
    }

    // V2
    // IDEA: REGEX TOKENISER + stack of counters
    /**
     *  Let a regex split the formula into tokens (atom name, number, paren) so the
     *  scanning loop disappears entirely and only the folding logic remains.
     *
     *  The pattern documents the grammar in one line, which is the real benefit --
     *  the cost is regex overhead on every token.
     *
     *  time  = O(n^2) worst case
     *  space = O(n)
     */
    public String countOfAtoms_2(String formula) {
        Matcher m = Pattern.compile("([A-Z][a-z]*)|(\\d+)|(\\()|(\\))").matcher(formula);

        Deque<Map<String, Integer>> stack = new ArrayDeque<>();
        stack.push(new HashMap<>());

        String pendingAtom = null;   // an atom waiting to learn its count
        boolean pendingGroup = false; // a just-closed group waiting for its count
        Map<String, Integer> closed = null;

        while (m.find()) {
            String tok = m.group();

            if (tok.equals("(")) {
                flush(stack, pendingAtom, pendingGroup, closed, 1);
                pendingAtom = null;
                pendingGroup = false;
                closed = null;
                stack.push(new HashMap<>());
            } else if (tok.equals(")")) {
                flush(stack, pendingAtom, pendingGroup, closed, 1);
                pendingAtom = null;
                closed = stack.pop();
                pendingGroup = true;
            } else if (Character.isDigit(tok.charAt(0))) {
                flush(stack, pendingAtom, pendingGroup, closed, Integer.parseInt(tok));
                pendingAtom = null;
                pendingGroup = false;
                closed = null;
            } else {
                flush(stack, pendingAtom, pendingGroup, closed, 1);
                pendingGroup = false;
                closed = null;
                pendingAtom = tok;
            }
        }
        flush(stack, pendingAtom, pendingGroup, closed, 1);

        StringBuilder out = new StringBuilder();
        for (Map.Entry<String, Integer> e : new TreeMap<>(stack.peek()).entrySet()) {
            out.append(e.getKey());
            if (e.getValue() > 1) {
                out.append(e.getValue());
            }
        }
        return out.toString();
    }

    /** commit whatever is pending (an atom or a closed group) with `mult` */
    private void flush(Deque<Map<String, Integer>> stack, String pendingAtom,
                       boolean pendingGroup, Map<String, Integer> closed, int mult) {
        if (pendingAtom != null) {
            stack.peek().merge(pendingAtom, mult, Integer::sum);
        } else if (pendingGroup && closed != null) {
            for (Map.Entry<String, Integer> e : closed.entrySet()) {
                stack.peek().merge(e.getKey(), e.getValue() * mult, Integer::sum);
            }
        }
    }

    // V3
    // IDEA: SCAN RIGHT TO LEFT WITH A RUNNING MULTIPLIER STACK
    /**
     *  Walking BACKWARDS, the multiplier that applies to an atom is simply the
     *  product of every group count still open to its right.
     *
     *  So keep one running `mult` plus a stack of the values to restore -- no
     *  per-group maps at all, and each atom is credited exactly once.
     *
     *  -> O(n) instead of the O(n^2) that folding nested maps costs.
     *
     *  time  = O(n log n) (the final TreeMap ordering dominates)
     *  space = O(n)
     */
    public String countOfAtoms_3(String formula) {
        int n = formula.length();
        Map<String, Integer> total = new TreeMap<>();

        Deque<Integer> multStack = new ArrayDeque<>();
        long mult = 1;
        long pendingCount = 1; // the number most recently read (to the right)

        int i = n - 1;
        while (i >= 0) {
            char c = formula.charAt(i);

            if (Character.isDigit(c)) {
                int end = i + 1;
                while (i >= 0 && Character.isDigit(formula.charAt(i))) {
                    i -= 1;
                }
                pendingCount = Long.parseLong(formula.substring(i + 1, end));
            } else if (c == ')') {
                multStack.push((int) mult);
                mult *= pendingCount;   // this group multiplies everything inside
                pendingCount = 1;
                i -= 1;
            } else if (c == '(') {
                mult = multStack.pop();
                pendingCount = 1;
                i -= 1;
            } else {
                // an atom name ends here; walk left over its lowercase tail
                int end = i + 1;
                while (i >= 0 && Character.isLowerCase(formula.charAt(i))) {
                    i -= 1;
                }
                String name = formula.substring(i, end);
                i -= 1;
                total.merge(name, (int) (pendingCount * mult), Integer::sum);
                pendingCount = 1;
            }
        }

        StringBuilder out = new StringBuilder();
        for (Map.Entry<String, Integer> e : total.entrySet()) {
            out.append(e.getKey());
            if (e.getValue() > 1) {
                out.append(e.getValue());
            }
        }
        return out.toString();
    }

}
