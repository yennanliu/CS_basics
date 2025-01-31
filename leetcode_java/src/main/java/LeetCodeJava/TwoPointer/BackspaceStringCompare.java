package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/backspace-string-compare/

import java.util.Stack;

public class BackspaceStringCompare {

    // V0
    // IDEA : STACK + stringBuilder
    public boolean backspaceCompare(String s, String t) {

        if (s == null && t == null){
            return true;
        }

        if (s == null || t == null){
            return false;
        }

        String s_ = backSpaceStr(s);
        String t_ = backSpaceStr(t);
        return s_.equals(t_);
    }

    private String backSpaceStr(String input){
        Stack<String> stack = new Stack<>();
        for(String x: input.split("")){
            if (!x.equals("#")){
                stack.add(x);
            }else{
                if(!stack.isEmpty()){
                    stack.pop();
                }
            }
        }
        // stack to str
        StringBuilder sb = new StringBuilder();
        while(!stack.isEmpty()){
            sb.append(stack.pop());
        }

        return sb.toString();
    }


    // V1
    public boolean backspaceCompare_1(String s, String t) {

        if (s == null && t == null){
            return true;
        }

        // TODO : check why can't have this condition
//        if ( (s == null || t != null) || (s != null && t == null) ){
//            return false;
//        }

        String _s = backSpaceOp(s);
        String _t = backSpaceOp(t);

//
//        System.out.println("_s = " + _s);
//        System.out.println("_t = " + _t);

        if (_s == null && _t == null){
            return true;
        }

        return _s.equals(_t);
    }


    private static String backSpaceOp(String s){

        if (s == null){
            return "";
        }

        Stack<String> stack = new Stack<>();

        for (int i = 0; i < s.length(); i++){
            String cur = String.valueOf(s.charAt(i));
            System.out.println("cur = " + cur + ", stack = " + stack.toString() + " is # ?" + cur.equals("#"));
            if (!cur.equals("#")){
                stack.push(cur);
            }else{
                if (!stack.empty()){
                    stack.pop();
                }
            }
        }

        String res = "";
        while (!stack.isEmpty()){
            String _cur = stack.pop();
            res += _cur;
        }

        return res;
    }

    // V2
    // IDEA : BUILD STRING
    // https://leetcode.com/problems/backspace-string-compare/editorial/
    public boolean backspaceCompare_2(String S, String T) {
        return build(S).equals(build(T));
    }

    public String build(String S) {
        Stack<Character> ans = new Stack();
        for (char c: S.toCharArray()) {
            if (c != '#')
                ans.push(c);
            else if (!ans.empty())
                ans.pop();
        }
        return String.valueOf(ans);
    }
    
}
