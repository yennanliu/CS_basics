package dev;

import java.util.*;

public class LC20FollowUp {

    public static void main(String[] args) {
        LC20FollowUp sol = new LC20FollowUp();

        System.out.println(sol.makeValidParentheses("()"));      // "()"
        System.out.println(sol.makeValidParentheses("(()"));     // "(())"
        System.out.println(sol.makeValidParentheses("())("));    // "()()"
        System.out.println(sol.makeValidParentheses("))(("));    // "(())(())"
        System.out.println(sol.makeValidParentheses("{[}]"));    // "{[]}"
    }

    public String makeValidParentheses(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        // Map of matching pairs
        Map<Character, Character> pairs = new HashMap<>();
        pairs.put(')', '(');
        pairs.put('}', '{');
        pairs.put(']', '[');

        Map<Character, Character> reverse = new HashMap<>();
        reverse.put('(', ')');
        reverse.put('{', '}');
        reverse.put('[', ']');

        // Stack for open brackets
        Stack<Character> stack = new Stack<>();
        StringBuilder result = new StringBuilder();

        for (char c : s.toCharArray()) {
            if (pairs.containsValue(c)) {
                // Opening bracket
                stack.push(c);
                result.append(c);
            } else if (pairs.containsKey(c)) {
                // Closing bracket
                if (!stack.isEmpty() && stack.peek() == pairs.get(c)) {
                    // Matches the last open
                    stack.pop();
                    result.append(c);
                } else {
                    // Extra closing -> insert a matching opening before it
                    result.insert(0, pairs.get(c));
                    result.append(c);
                }
            }
        }

        // Add any missing closing brackets for leftovers in the stack
        while (!stack.isEmpty()) {
            result.append(reverse.get(stack.pop()));
        }

        return result.toString();
    }
}