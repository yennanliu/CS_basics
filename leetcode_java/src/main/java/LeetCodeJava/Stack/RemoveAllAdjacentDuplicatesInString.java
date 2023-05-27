package LeetCodeJava.Stack;

// https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/

import java.util.Stack;

public class RemoveAllAdjacentDuplicatesInString {

    // V0
//    public String removeDuplicates(String s) {
//
//        if (s.equals(null) || s.length() == 0){
//            return "";
//        }
//
//        Stack<String> stack = new Stack<>();
//        for (int i = 0; i < s.length(); i++){
//            String cur = String.valueOf(s.charAt(i));
//            if(stack.size()==0){
//                stack.add(cur);
//            } else if (cur != stack.peek()) {
//                stack.push(cur);
//            } else{
//                String first = stack.peek();
//                while(first == cur){
//                    first = stack.peek();
//                    stack.pop();
//                }
//            }
//        }
//
//        String ans = "";
//        while (!stack.empty()){
//            ans += stack.pop();
//        }
//
//        return ans;
//    }

    // V1
    // https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/solutions/2800677/java-solution-using-stack-explained-using-diagrams/
    public String removeDuplicates(String s) {
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
    public String removeDuplicates2(String str) {

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
