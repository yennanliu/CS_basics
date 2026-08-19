package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/brace-expansion-ii/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *  1096. Brace Expansion II
 *  Hard
 *
 *  Under the grammar given below, strings can represent a set of lowercase
 *  words. Let R(expr) denote the set of words the expression represents.
 *
 *    - For every lowercase letter x, R(x) = {x}.
 *    - For expressions e1, e2, ..., ek with k >= 2,
 *      R({e1,e2,...}) = R(e1) union R(e2) union ...
 *    - For expressions e1 and e2,
 *      R(e1 + e2) = { a + b for (a, b) in R(e1) x R(e2) }.
 *
 *  Given an expression representing a set of words under the given grammar,
 *  return the sorted list of words that the expression represents.
 *
 *  Example 1:
 *    Input: expression = "{a,b}{c,{d,e}}"
 *    Output: ["ac","ad","ae","bc","bd","be"]
 *
 *  Example 2:
 *    Input: expression = "{{a,z},a{b,c},{ab,z}}"
 *    Output: ["a","ab","ac","z"]
 *    Explanation: Each distinct word is written only once in the answer.
 *
 *  Constraints:
 *    1 <= expression.length <= 60
 *    expression[i] consists of '{', '}', ',' or lowercase English letters.
 *    The given expression represents a set of words based on the grammar.
 */
public class BraceExpansionII {

    // V0
    // IDEA: STACK PARSER (union list + concatenation product per nesting level)
    //       per level we keep
    //         cur   : set of words built by CONCATENATION so far
    //         parts : list of sets already closed by a ',' (to be UNIONed)
    //       '{'    -> push (parts, cur), start a fresh level
    //       ','    -> flush cur into parts, restart cur = {""}
    //       '}'    -> union the level, pop the parent, product it into parent cur
    //       letter -> append the letter to every word in cur
    /**
     * time = O(L * W)
     * space = O(W)
     *   L = expression length, W = number of distinct words produced
     */
    public List<String> braceExpansionII(String expression) {
        List<Set<String>> parts = new ArrayList<>();
        Set<String> cur = new HashSet<>();
        cur.add("");

        Deque<List<Set<String>>> partsStack = new ArrayDeque<>();
        Deque<Set<String>> curStack = new ArrayDeque<>();

        for (char c : expression.toCharArray()) {
            if (c == '{') {
                partsStack.push(parts);
                curStack.push(cur);
                parts = new ArrayList<>();
                cur = new HashSet<>();
                cur.add("");
            } else if (c == '}') {
                parts.add(cur);
                Set<String> grp = new HashSet<>();
                for (Set<String> p : parts) {
                    grp.addAll(p);
                }
                parts = partsStack.pop();
                Set<String> parent = curStack.pop();
                // NOTE !!! close the group -> product with the parent prefix
                cur = new HashSet<>();
                for (String a : parent) {
                    for (String b : grp) {
                        cur.add(a + b);
                    }
                }
            } else if (c == ',') {
                parts.add(cur);
                cur = new HashSet<>();
                cur.add("");
            } else {
                Set<String> next = new HashSet<>();
                for (String a : cur) {
                    next.add(a + c);
                }
                cur = next;
            }
        }

        parts.add(cur);
        Set<String> res = new TreeSet<>();
        for (Set<String> p : parts) {
            res.addAll(p);
        }
        return new ArrayList<>(res);
    }
}
