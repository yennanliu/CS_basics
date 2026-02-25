package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/backspace-string-compare/
/**
 *

 844. Backspace String Compare
 Solved
 Easy
 Topics
 premium lock icon
 Companies
 Given two strings s and t, return true if they are equal when both are typed into empty text editors. '#' means a backspace character.

 Note that after backspacing an empty text, the text will continue empty.



 Example 1:

 Input: s = "ab#c", t = "ad#c"
 Output: true
 Explanation: Both s and t become "ac".
 Example 2:

 Input: s = "ab##", t = "c#d#"
 Output: true
 Explanation: Both s and t become "".
 Example 3:

 Input: s = "a#c", t = "b"
 Output: false
 Explanation: s becomes "c" while t becomes "b".


 Constraints:

 1 <= s.length, t.length <= 200
 s and t only contain lowercase letters and '#' characters.


 Follow up: Can you solve it in O(n) time and O(1) space?
 *
 *
 *
 */
import java.util.Stack;

public class BackspaceStringCompare {

    // V0
    // IDEA: STACK + SB
    public boolean backspaceCompare(String s, String t) {
        // edge

        Stack<String> st1 = new Stack<>();
        Stack<String> st2 = new Stack<>();

        for (String x : s.split("")) {
            if (x.equals("#")) {
                if (!st1.isEmpty()) {
                    st1.pop();
                }
            } else {
                st1.push(x);
            }
        }

        for (String x : t.split("")) {
            if (x.equals("#")) {
                if (!st2.isEmpty()) {
                    st2.pop();
                }
            } else {
                st2.push(x);
            }
        }

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        while (!st1.isEmpty()) {
            sb1.append(st1.pop());
        }

        while (!st2.isEmpty()) {
            sb2.append(st2.pop());
        }

//        System.out.println(">>> sb1 = " + sb1.toString());
//        System.out.println(">>> sb2 = " + sb2.toString());

        return sb1.toString().contentEquals(sb2);
    }

    // V0-1
    // IDEA : STACK + stringBuilder
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean backspaceCompare_0_1(String s, String t) {

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
    /**
     * time = O(N)
     * space = O(1)
     */
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
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean backspaceCompare_2(String S, String T) {
        return build(S).equals(build(T));
    }

    /**
     * time = O(N)
     * space = O(1)
     */
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
