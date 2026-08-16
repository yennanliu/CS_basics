package LeetCodeJava.Recursion;

// https://leetcode.com/problems/special-binary-string/description/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 761. Special Binary String
 * Hard
 *
 * Special binary strings are binary strings with the following two properties:
 *
 *   - The number of 0's is equal to the number of 1's.
 *   - Every prefix of the binary string has at least as many 1's as 0's.
 *
 * You are given a special binary string s.
 *
 * A move consists of choosing two consecutive, non-empty, special substrings of s,
 * and swapping them. Two strings are consecutive if the last character of the first
 * string is exactly one index before the first character of the second string.
 *
 * Return the lexicographically largest resulting string possible after applying the
 * mentioned operations on the string.
 *
 *
 * Example 1:
 *
 * Input: s = "11011000"
 * Output: "11100100"
 * Explanation: The strings "10" [occuring at s[1]] and "1100" [at s[3]] are swapped.
 * This is the lexicographically largest string possible after some number of swaps.
 *
 * Example 2:
 *
 * Input: s = "10"
 * Output: "10"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 50
 * s[i] is either '0' or '1'.
 * s is a special binary string.
 *
 */
public class SpecialBinaryString {

    // V0
    // IDEA: RECURSION (treat the string as BALANCED PARENTHESES)
    /**
     *   Read '1' as '(' and '0' as ')': a special string is a VALID, BALANCED sequence.
     *
     *   Split s into its TOP-LEVEL balanced blocks (the counter returns to 0).
     *   Each block is  "1" + <inner special string> + "0"
     *   -> recursively make the INNER part largest,
     *   -> then sort the blocks in DESCENDING order and concatenate.
     *
     *   NOTE !!! sorting blocks is LEGAL because adjacent top-level blocks are exactly
     *            the `two consecutive special substrings` the problem lets us swap,
     *            and any permutation is reachable by adjacent swaps.
     *
     *   time  = O(n^2 log n)
     *   space = O(n^2) (recursive slices)
     */
    public String makeLargestSpecial(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        List<String> blocks = new ArrayList<>();
        int count = 0;
        int start = 0;

        for (int i = 0; i < s.length(); i++) {
            count += s.charAt(i) == '1' ? 1 : -1;

            /** NOTE !!!
             *
             *  count back to 0 -> s[start .. i] is a TOP-LEVEL block:
             *  '1' + inner + '0'
             */
            if (count == 0) {
                String inner = makeLargestSpecial(s.substring(start + 1, i));
                blocks.add("1" + inner + "0");
                start = i + 1;
            }
        }

        // lexicographically largest -> put the BIGGEST blocks first
        Collections.sort(blocks, Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (String b : blocks) {
            sb.append(b);
        }
        return sb.toString();
    }


    // V1
    // IDEA: RECURSION ON INDICES (no substring allocation)
    /**
     *  Same block decomposition as V0, but the recursion carries (start, end)
     *  instead of slicing a new String at every level.
     *
     *  V0 allocates O(n) characters per level for O(n) levels; this version
     *  allocates only the blocks it actually emits.
     *
     *  time  = O(n^2 log n)
     *  space = O(n) recursion depth
     */
    public String makeLargestSpecial_1(String s) {
        return solveRange(s, 0, s.length());
    }

    private String solveRange(String s, int from, int to) {
        if (from >= to) {
            return "";
        }

        List<String> blocks = new ArrayList<>();
        int count = 0;
        int start = from;

        for (int i = from; i < to; i++) {
            count += s.charAt(i) == '1' ? 1 : -1;
            if (count == 0) {
                blocks.add("1" + solveRange(s, start + 1, i) + "0");
                start = i + 1;
            }
        }

        blocks.sort(Collections.reverseOrder());
        return String.join("", blocks);
    }

    // V2
    // IDEA: BUILD AN EXPLICIT NESTING TREE, THEN SERIALISE
    /**
     *  Parse the string into a tree of nested blocks first, then sort each node's
     *  children (descending by their rendered form) and print.
     *
     *  Separating PARSE from ORDER makes the invariant obvious -- the answer is
     *  the same tree with every child list sorted -- and the tree can be inspected
     *  or reused.
     *
     *  time  = O(n^2 log n)
     *  space = O(n)
     */
    public String makeLargestSpecial_2(String s) {
        int[] pos = { 0 };
        List<Node761> roots = parseBlocks(s, pos, s.length());
        return renderAll(roots);
    }

    /** one node per balanced block; children are its inner blocks */
    private static class Node761 {
        List<Node761> children = new ArrayList<>();
    }

    private List<Node761> parseBlocks(String s, int[] pos, int limit) {
        List<Node761> out = new ArrayList<>();
        while (pos[0] < limit && s.charAt(pos[0]) == '1') {
            pos[0] += 1;                       // consume the '1'
            Node761 node = new Node761();
            node.children = parseBlocks(s, pos, limit);
            pos[0] += 1;                       // consume the matching '0'
            out.add(node);
        }
        return out;
    }

    private String renderAll(List<Node761> nodes) {
        List<String> parts = new ArrayList<>();
        for (Node761 n : nodes) {
            parts.add("1" + renderAll(n.children) + "0");
        }
        parts.sort(Collections.reverseOrder());
        return String.join("", parts);
    }

    // V3
    // IDEA: ITERATIVE, USING A STACK OF PARTIAL BLOCK LISTS
    /**
     *  Walk the string once with a stack whose top holds the blocks collected at
     *  the CURRENT nesting depth. A '1' pushes a new level; a '0' pops it, sorts
     *  that level and folds it into the parent.
     *
     *  No recursion at all -- the same shape you would use if the input could be
     *  deep enough to blow the call stack.
     *
     *  time  = O(n^2 log n)
     *  space = O(n)
     */
    public String makeLargestSpecial_3(String s) {
        Deque<List<String>> stack = new ArrayDeque<>();
        stack.push(new ArrayList<>());

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '1') {
                stack.push(new ArrayList<>());
            } else {
                List<String> inner = stack.pop();
                inner.sort(Collections.reverseOrder());
                stack.peek().add("1" + String.join("", inner) + "0");
            }
        }

        List<String> top = stack.pop();
        top.sort(Collections.reverseOrder());
        return String.join("", top);
    }

}
