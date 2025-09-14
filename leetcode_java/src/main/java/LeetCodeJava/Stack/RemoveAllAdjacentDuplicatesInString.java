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
    // IDEA: STACK
    public String removeDuplicates(String s) {
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

    // V1
    // https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/solutions/2800677/java-solution-using-stack-explained-using-diagrams/
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
