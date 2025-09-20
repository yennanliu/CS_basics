package LeetCodeJava.Stack;

// https://www.tutorialspoint.com/java/util/stack_pop.htm
// https://leetcode.com/problems/valid-parentheses/
// https://www.softwaretestinghelp.com/java-queue-interface/
/**
 * 20. Valid Parentheses
 * N/A
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
 *
 * An input string is valid if:
 *
 * Open brackets must be closed by the same type of brackets.
 * Open brackets must be closed in the correct order.
 * Every close bracket has a corresponding open bracket of the same type.
 *
 *
 * Example 1:
 *
 * Input: s = "()"
 *
 * Output: true
 *
 * Example 2:
 *
 * Input: s = "()[]{}"
 *
 * Output: true
 *
 * Example 3:
 *
 * Input: s = "(]"
 *
 * Output: false
 *
 * Example 4:
 *
 * Input: s = "([])"
 *
 * Output: true
 *
 * Example 5:
 *
 * Input: s = "([)]"
 *
 * Output: false
 *
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 104
 * s consists of parentheses only '()[]{}'.
 *
 * Accepted
 * 6,510,306/15.2M
 * Acceptance Rate
 *
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ValidParentheses {

    // V0
    // IDEA : STACK + DICT
    public boolean isValid(String s) {

        if (s == null || s.length() == 0){
            return true;
        }

        Map<String, String> map = new HashMap<>();
        map.put("(", ")");
        map.put("{", "}");
        map.put("[", "]");

        /** NOTE !!! use stack here, FILO */
        Stack<String> stack = new Stack<>();

        for (String x : s.split("")){
            //System.out.println("x = " + x + " stack = " + stack);
            if (map.containsKey(x)){
                stack.add(x);
            }else{
                if (stack.isEmpty()){
                    return false;
                }
                String last = stack.pop();
                if (!map.get(last).equals(x)){
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    // V0-1
    // IDEA : STACK + DICT
    public boolean isValid_0_1(String s) {

        if (s.length() % 2 != 0){
            return false;
        }

        HashMap<String, String> map = new HashMap<>();
        Stack st = new Stack();

        map.put("(", ")");
        map.put("{", "}");
        map.put("[", "]");

        for (int i = 0; i < s.length(); i++){
            String cur = String.valueOf(s.charAt(i));
            if (st.empty() && !map.containsKey(cur)){
                return false;
            }
            if (map.containsKey(cur)){
                st.push(cur);
            }
            else{
                String popElement = (String) st.pop();
                String expect = map.get(popElement);
                if (!expect.equals(cur)){
                    return false;
                }
            }
        }
        return true ? st.empty() : false;
    }

    // V0-2
    // IDEA : STACK + add "inverse" Parentheses to stack directly
    // https://www.bilibili.com/video/BV1AF411w78g/?share_source=copy_web&vd_source=771d0eba9b524b4f63f92e37bde71301
    public boolean isValid_0_2(String s) {

        if (s.length() % 2 != 0){
            return false;
        }

        Stack st = new Stack();

        for (int i = 0; i < s.length(); i++){

            String cur = String.valueOf(s.charAt(i));

            if (cur.equals("(")){
                st.push(")");
                continue;
            }
            else if (cur.equals("{")){
                st.push("}");
                continue;
            }
            else if (cur.equals("[")){
                st.push("]");
                continue;
            }
            else{
                if (st.empty()){
                    return false;
                }else{
                    String _cur = (String) st.pop();
                    if (!_cur.equals(cur)){
                        return false;
                    }
                }
            }

        }
        return true ? st.empty() : false;
    }

    // V0-3
    // IDEA: STACK
    public boolean isValid_0_3(String s) {
        // edge
        if (s.isEmpty() || s.length() == 0) {
            return false;
        }
        if (s.length() == 1) {
            return false;
        }
        Map<String, String> map = new HashMap<>();
        map.put("(", ")");
        map.put("{", "}");
        map.put("[", "]");

        // stack: FILO
        Stack<String> st = new Stack<>();

        for (String x : s.split("")) {
            //System.out.println(">>> x = " + x);
            if (st.isEmpty() && !map.containsKey(x)) {
                return false;
            }
            if (map.containsKey(x)) {
                st.add(x);
            } else {
                if (!map.get(st.peek()).equals(x)) {
                    return false;
                }
                st.pop();
            }
        }

        return st.isEmpty();
    }


    // V1
    // https://leetcode.com/problems/valid-parentheses/solutions/3398779/python-java-c-simple-solution-video-solution/
    public boolean isValid_1(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                if (stack.empty()) {
                    return false;
                }
                if (c == ')' && stack.peek() == '(') {
                    stack.pop();
                } else if (c == '}' && stack.peek() == '{') {
                    stack.pop();
                } else if (c == ']' && stack.peek() == '[') {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }
        return stack.empty();
    }

    // V2
    // https://leetcode.com/problems/valid-parentheses/solutions/3399077/easy-solutions-in-java-python-and-c-look-at-once-with-exaplanation/
    public boolean isValid_2(String s) {
        Stack<Character> stack = new Stack<>();
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(' || s.charAt(i) == '{' || s.charAt(i) == '[') {
                stack.push(s.charAt(i));
                count++;
            } else {
                if (stack.isEmpty()) return false;
                char ch = stack.pop();
                if ((s.charAt(i) == ')' && ch == '(') || (s.charAt(i) == ']' && ch == '[') || (s.charAt(i) == '}' && ch == '{')) {
                } else {
                    return false;
                }
                count--;
            }
        }
        return count == 0;
    }

    /**
     *  Follow up:
     *
     *   Return `validated string`
     *    - if input is a valid parentheses
     *    - if input is NOT a valid parentheses
     *
     *    https://buildmoat.teachable.com/courses/7a7af3/lectures/62677055
     */
    // FOLLOWUP V0
    // IDEA: STACK (teachable course)
    public String ConvertToValidBracketString (String s) {
        Stack<Character> stack = new Stack<>();
        String ans = "";
        String openBrackets = "({[", closeBrackets = ")}]";
        for(char c : s.toCharArray()) {
            if(openBrackets.contains(String.valueOf(c))) {
                stack.push(c);
                ans += c;
            } else {
                for( int i = 0 ; i < 3 ; i++) {
                    if(c == closeBrackets.charAt(i)) {
                        if(!stack.empty() && stack.peek() == openBrackets.charAt(i) ) {
                            stack.pop();
                            ans += c;
                        } else {
                            ans += openBrackets.charAt(i);
                            ans += c;
                        }
                    }
                }
            }
        }
        while (!stack.empty()) {
            for( int i=0 ; i<3;i++) {
                if(openBrackets.charAt(i) == stack.peek()) {
                    ans +=closeBrackets.charAt(i);
                }
            }
            stack.pop();
        }
        //assertThat(validateBracketsString(ans)).EqualTrue();
        return ans;
    }

    // FOLLOWUP V1
    // IDEA: STACK (GPT)
    public String ConvertToValidBracketString_1(String s) {
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

    // FOLLOWUP V2
    // IDEA: STACK (fixed by GPT)
    public String convertToValidBracketString_2(String s) {
        if (s == null || s.isEmpty()) {
            return "()";
        }

        // Bracket pairs
        Map<Character, Character> openToClose = new HashMap<>();
        openToClose.put('(', ')');
        openToClose.put('{', '}');
        openToClose.put('[', ']');

        Map<Character, Character> closeToOpen = new HashMap<>();
        closeToOpen.put(')', '(');
        closeToOpen.put('}', '{');
        closeToOpen.put(']', '[');

        Stack<Character> stack = new Stack<>();
        StringBuilder result = new StringBuilder();

        for (char c : s.toCharArray()) {
            if (openToClose.containsKey(c)) {
                // opening
                stack.push(c);
                result.append(c);
            } else if (closeToOpen.containsKey(c)) {
                // closing
                if (!stack.isEmpty() && stack.peek() == closeToOpen.get(c)) {
                    // matches
                    stack.pop();
                    result.append(c);
                } else {
                    // extra closing: insert missing opening
                    result.insert(0, closeToOpen.get(c));
                    result.append(c);
                }
            }
        }

        // close remaining openings
        while (!stack.isEmpty()) {
            result.append(openToClose.get(stack.pop()));
        }

        return result.toString();
    }


}


