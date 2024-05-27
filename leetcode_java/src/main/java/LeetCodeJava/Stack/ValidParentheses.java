package LeetCodeJava.Stack;

// https://www.tutorialspoint.com/java/util/stack_pop.htm
// https://leetcode.com/problems/valid-parentheses/
// https://www.softwaretestinghelp.com/java-queue-interface/

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

    // V0'
    // IDEA : STACK + DICT
    public boolean isValid_0(String s) {

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

    // V0'
    // IDEA : STACK + add "inverse" Parentheses to stack directly
    // https://www.bilibili.com/video/BV1AF411w78g/?share_source=copy_web&vd_source=771d0eba9b524b4f63f92e37bde71301
    public boolean isValid_(String s) {

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

}


