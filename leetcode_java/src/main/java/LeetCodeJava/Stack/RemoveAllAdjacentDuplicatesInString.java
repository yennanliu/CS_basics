package LeetCodeJava.Stack;

// https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/

import java.util.Stack;

public class RemoveAllAdjacentDuplicatesInString {

    // V0
    // IDEA : STACK + STRING
    public String removeDuplicates(String s) {

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
