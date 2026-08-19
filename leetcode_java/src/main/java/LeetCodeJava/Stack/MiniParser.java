package LeetCodeJava.Stack;

// https://leetcode.com/problems/mini-parser/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 *  385. Mini Parser
 *  Medium
 *
 *  Given a string s represents the serialization of a nested list, implement a parser
 *  to deserialize it and return the deserialized NestedInteger.
 *
 *  Each element is either an integer or a list whose elements may also be integers
 *  or other lists.
 *
 *  Example 1:
 *  Input: s = "324"
 *  Output: 324
 *  Explanation: You should return a NestedInteger object which contains a single integer 324.
 *
 *  Example 2:
 *  Input: s = "[123,[456,[789]]]"
 *  Output: [123,[456,[789]]]
 *
 *  Constraints:
 *  1 <= s.length <= 5 * 10^4
 *  s consists of digits, square brackets "[]", negative sign '-', and commas ','.
 *  s is the serialization of valid NestedInteger.
 *  All the values in the input are in the range [-10^6, 10^6].
 */
public class MiniParser {

    /**
     * This is the interface that allows for creating nested lists.
     * On LeetCode it is provided by the judge — reproduced here so the file compiles.
     */
    public static class NestedInteger {

        private Integer value;
        private final List<NestedInteger> list;

        /** Initializes an empty nested list. */
        public NestedInteger() {
            this.value = null;
            this.list = new ArrayList<>();
        }

        /** Initializes a single integer equal to value. */
        public NestedInteger(int value) {
            this.value = value;
            this.list = new ArrayList<>();
        }

        /** @return true if this NestedInteger holds a single integer, rather than a nested list. */
        public boolean isInteger() {
            return this.value != null;
        }

        /** @return the single integer this holds, or null if it holds a nested list. */
        public Integer getInteger() {
            return this.value;
        }

        /** Set this NestedInteger to hold a single integer. */
        public void setInteger(int value) {
            this.value = value;
        }

        /** Set this NestedInteger to hold a nested list and add a nested integer to it. */
        public void add(NestedInteger ni) {
            this.value = null;
            this.list.add(ni);
        }

        /** @return the nested list this holds, or an empty list if it holds a single integer. */
        public List<NestedInteger> getList() {
            return this.list;
        }
    }

    // V0
    // IDEA: STACK — '[' opens a new NestedInteger, ']' closes the current one into its parent,
    //       digits (with optional '-') are parsed as a number and added to the current one
    /**
     * time = O(n)
     * space = O(d)   // d = max nesting depth
     */
    public NestedInteger deserialize(String s) {

        if (s == null || s.isEmpty()) {
            return new NestedInteger();
        }

        // a bare number, no brackets at all
        if (s.charAt(0) != '[') {
            return new NestedInteger(Integer.parseInt(s));
        }

        Deque<NestedInteger> stack = new ArrayDeque<>();
        NestedInteger cur = null;
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (c == '[') {
                if (cur != null) {
                    stack.push(cur);
                }
                cur = new NestedInteger();
                i++;
            } else if (c == ']') {
                if (!stack.isEmpty()) {
                    NestedInteger parent = stack.pop();
                    parent.add(cur);
                    cur = parent;
                }
                i++;
            } else if (c == ',') {
                i++;
            } else {
                // read a (possibly negative) integer
                int j = i;
                if (s.charAt(j) == '-') {
                    j++;
                }
                while (j < s.length() && Character.isDigit(s.charAt(j))) {
                    j++;
                }
                cur.add(new NestedInteger(Integer.parseInt(s.substring(i, j))));
                i = j;
            }
        }
        return cur;
    }
}
