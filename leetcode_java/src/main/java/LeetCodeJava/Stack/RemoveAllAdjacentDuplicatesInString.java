package LeetCodeJava.Stack;

// https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/
/**
 * 1047. Remove All Adjacent Duplicates In String
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a string s consisting of lowercase English letters. A duplicate removal consists of choosing two adjacent and equal letters and removing them.
 *
 * We repeatedly make duplicate removals on s until we no longer can.
 *
 * Return the final string after all such duplicate removals have been made. It can be proven that the answer is unique.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "abbaca"
 * Output: "ca"
 * Explanation:
 * For example, in "abbaca" we could remove "bb" since the letters are adjacent and equal, and this is the only possible move.  The result of this move is that the string is "aaca", of which only "aa" is possible, so the final string is "ca".
 * Example 2:
 *
 * Input: s = "azxxzy"
 * Output: "ay"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 105
 * s consists of lowercase English letters.
 *
 */
import java.util.Stack;

public class RemoveAllAdjacentDuplicatesInString {

    // V0
    // IDEA: STACK + isAdjDup flag (gemini)
    public String removeDuplicates(String s) {
        // edge
        Stack<String> st = new Stack<>();
        for (String x : s.split("")) {
            /** NOTE !!!
             *
             * isAdjDup flag, so we can know
             * whether should `last` top element in stack
             */
            boolean isAdjDup = false;
            /** NOTE !!!
             *
             *  we use `if` instead of `while` loop
             *  to avoid `infinite`  loop
             */
            if (!st.isEmpty() && st.peek().equals(x)) {
                isAdjDup = true;
            } else {
                st.add(x);
            }
            /** NOTE !!!
             *
             * pop the `last` duplicate from stack
             */
            if (isAdjDup && !st.isEmpty()) {
                st.pop();
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String y : st) {
            sb.append(y);
        }

        return sb.toString();
    }

    // V0-0-1
    // IDEA: STACK (gemini)
    /** NOTE !!!
     *
     *  LC 1047 is ONLY remove `Adjacent` duplicated elements,
     *  so below code works here.
     *  However, for LC 1209, we may need the other approaches.
     */
    public String removeDuplicates_0_0_1(String s) {
        if (s == null || s.length() == 0){
            return "";
        }

        Stack<Character> st = new Stack<>();

        for (char x : s.toCharArray()) {
            // If stack is NOT empty and the top matches current char
            if (!st.isEmpty() && st.peek() == x) {
                st.pop(); // Remove the existing one, and don't add the current one
            } else {
                st.push(x); // Not a duplicate, add it to stack
            }
        }

        // Build the final string from the remaining characters
        StringBuilder sb = new StringBuilder();
        for (char y : st) {
            sb.append(y);
        }

        return sb.toString();
    }




    // V0-0-0-2
    // IDEA: STACK
    /**
     * time = O(1)
     * space = O(1)
     */
    public String removeDuplicates_0_0_2(String s) {
        // edge
        if (s.isEmpty() || s.length() == 1) {
            return s;
        }

        Stack<String> st = new Stack<>();
        StringBuilder sb = new StringBuilder();

        // init
        st.add(String.valueOf(s.charAt(0)));

        // start from 1
        for (int i = 1; i < s.length(); i++) {
            boolean isDuplicates = false; // ???
            String x = String.valueOf(s.charAt(i));
            // System.out.println(">>> x = " + x + ", i = " + i + ", st = " + st);
            while (!st.isEmpty() && st.peek().equals(x)) {
                isDuplicates = true;
                st.pop();
            }
            if (!isDuplicates) {
                st.add(x);
            }
        }

        //System.out.println(">>> st = " + st);
        while (!st.isEmpty()) {
            sb.append(st.pop());
        }

        // NOTE !!! reverse sb first (CAN'T use sb.reverse().toString())
        // since `sb.reverse()` return null resp
        sb.reverse();
        //System.out.println(">>> sb.toString() = " + sb.toString());
        return sb.toString();
    }


    // V0-1
    // IDEA : STACK + STRING
    /**
     * time = O(1)
     * space = O(1)
     */
    public String removeDuplicates_0_1(String s) {

        if (s.equals(null)){
            return s;
        }

        Stack<String> stack = new Stack<>();
        char[] s_array = s.toCharArray();
        for (char x : s_array){
            String _x = String.valueOf(x);
            if (stack.isEmpty()){
                stack.push(_x);
            }else{
                if (_x.equals(stack.peek())){
                    stack.pop();
                }else{
                    stack.push(_x);
                }
            }
        }

        // NOTE! we use stringBuilder here for string reverse
        StringBuilder sb = new StringBuilder();

        while(!stack.isEmpty()){
            String cur = stack.pop();
            sb.append(cur);
        }

        /** NOTE !!! since stack pop last element, we need to reverse result string */
        return sb.reverse().toString();
    }

    // V0-2
    // IDEA: STACK (gpt)
    /**
     * time = O(1)
     * space = O(1)
     */
    public String removeDuplicates_0_2(String s) {
        // edge
        if (s.isEmpty() || s.length() == 1) {
            return s;
        }

        Stack<Character> st = new Stack<>();
        StringBuilder sb = new StringBuilder();

        for (char c : s.toCharArray()) {
            if (!st.isEmpty() && st.peek() == c) {
                st.pop(); // remove duplicate
            } else {
                st.push(c);
            }
        }

        // build result
        while (!st.isEmpty()) {
            sb.append(st.pop());
        }

        return sb.reverse().toString();
    }

    // V0-3
    // IDEA: STACK
    /**
     * time = O(1)
     * space = O(1)
     */
    public String removeDuplicates_0_3(String s) {
        // edge
        if (s.isEmpty() || s.length() == 0) {
            return s;
        }
        if (s.length() == 1) {
            return s;
        }
        if (s.length() == 2) {
            // ???
            if (s.charAt(0) == s.charAt(1)) {
                return null;
            }
            return s;
        }

        char[] charArr = s.toCharArray();
        Stack<Character> st = new Stack<>();
        char prev = '*'; // ???
        for (char ch : charArr) {
            // case 1) st is empty
            if (st.isEmpty()) {
                st.add(ch);
            } else {
                // cast 2) st is NOT empty and prev != cur
                if (!st.peek().equals(ch)) {
                    st.add(ch);
                    prev = ch;
                }
                // cast 3) st is NOT empty and prev == cur
                else {
                    st.pop();
                }
            }
        }

        //  System.out.println(">>> st = " + st);
        StringBuilder sb = new StringBuilder();
        while (!st.isEmpty()) {
            sb.append(st.pop());
        }

        /** NOTE !!! even `print` apply the reverse op
         *           , so we can't do below (or will reverse str unexpectedly)
         */
        // System.out.println(">>> (sb to string) sb.toString() =  " + sb.toString());
        // System.out.println(">>> (inverse vsb to string) sb.reverse().toString() =  " + sb.reverse().toString());

        return sb.reverse().toString();
    }

    // V1
    // https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/solutions/2800677/java-solution-using-stack-explained-using-diagrams/
    /**
     * time = O(1)
     * space = O(1)
     */
    public String removeDuplicates_2(String s) {
        Stack<Character> stack = new Stack();
        if(s.length()<=1){return s;}
        stack.add(s.charAt(0));

        for(int i=1;i<s.length();i++){

            if(!stack.isEmpty() && stack.peek().equals(s.charAt(i))){
                stack.pop();
                continue;
            }
            stack.add(s.charAt(i));
        }
        String res="";
        while(!stack.isEmpty()){
            res =stack.pop()+res;
        }
        return res;
    }

    // V2
    // https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/solutions/2799124/easy-stack-solution-with-explanation/
    /**
     * time = O(1)
     * space = O(1)
     */
    public String removeDuplicates_3(String str) {

        Stack<Character> s= new Stack<>();

        for( char c: str.toCharArray()){
            if( s.isEmpty()==false && s.peek()==c){
                s.pop();
            }
            else{
                s.add( c);
            }
        }


        StringBuilder ans= new StringBuilder();

        while( s.isEmpty()==false){
            ans.append( s.pop());
        }


        // reverse it as stack is first in last out
        return ans.reverse().toString() ;
    }




}
