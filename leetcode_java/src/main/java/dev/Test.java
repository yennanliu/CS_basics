package dev;

/**
 *  Given a string `s` which represents an expression,
 *  evaluate this expression and return its value.
 * The integer division should truncate toward zero.
 * You may assume that the given expression is always valid.
 * All intermediate results will be in the range of [-2^(31), 2^(31)-1].
 * Note:
 * You are not allowed to use any built-in function
 * which evaluates strings as mathematical expressions, such as `eval()`.
 *
 * Example 1:
 * Input: s = "1*2+3*4-0"
 * Output: 14
 *
 * Example 2:
 * Input: s = " 3/2 "
 * Output: 1
 *
 * Example 3:
 * Input: s = " 3+15 / 2 "
 * Output: 10
 *
 * Example 4:
 * Input: s = " 100 / 11 /2*10 -0 -1/2+ 10 +0"
 * Output: 50
 *
 *   -> 100 / 11 /2*10 -0 -1/2+ 10 +0
 *       (9/2) * 10 - 0 + 10 = 50
 *
 * Hint:
 * 1. 1 <= s.length <= 300000
 * 2. `s` consists of integers and operators `('+', '-', '*', '/')` separated by some number of spaces.
 * 3. s represents a valid expression.
 * 4. All the integers in the expression are non-negative integers in the range [0, 2^(31) - 1].
 * 5. The answer is guaranteed to fit in a 32-bit integer.
 *
 *
 */

import java.util.Stack;

/**
 *    ex 1)
 *     Input: s = "1*2+3*4-0"
 *
 *     -> st = [2, +12, -0 ]  -> 14
 *
 *
 *   ex 2)
 *
 *    " 3/2 "
 *
 *    -> st = [1] = 1
 *
 *
 *   ex 3)
 *    " 3+15 / 2 "
 *
 *    -> st = [3, 7] -> 10
 *
 *
 *   ex 4)
 *
 *    " 100 / 11 /2*10 -0 -1/2+ 10 +0"
 *
 *    -> st = [40, -0, 0, 10, 0] -> 50
 *
 *
 *    stack
 *     time: O(n)
 *     space: O(n)
 */
public class Test {

    public static void main(String[] args) {
        //String s ="1+2+3";
        //String s ="1-2+3";
        //String s ="11-2+3";
        String s ="100-2+30";
        int res = caculate(s);
        System.out.println(res);
    }


    // "1*2+3*4-0"
     public static int caculate(String s){
         // edge
//         if(s.isEmpty() || s.length() == 0){
//             return 0;
//         }
         if (s == null || s.length() == 0)
             return 0;

         if(s.length() == 1){
             return Integer.parseInt(String.valueOf(s.charAt(0)));
         }

         //String fixEdStr = s.strip("");
         // Input: s = "1*2+3*4-0"
         String digit = "0123456789";
         String op = "*/";

         // input s = 1+2+3
         // input s = 11+2+3

         Stack<String> st = new Stack<>();
         boolean prevIsDigit = false;
         boolean isPositive = true;

         int curVal = 0;

         for(char ch: s.toCharArray()){




             String x = String.valueOf(ch);
             System.out.println("x = " + x);

             if(x.isEmpty()){
                 continue;
             }
             if(digit.contains(x)){

                 // // input s = 11+2+3

                 curVal = (curVal * 10) + Integer.parseInt(x);

                 //int toInsert = 0;
//                 if(!isPositive){
//                     System.out.println("val save to st = " + String.valueOf(-1 * Integer.parseInt(x)));
//                     st.add(String.valueOf(-1 * Integer.parseInt(x)));
//                 }else{
//                     st.add(String.valueOf( Integer.parseInt(x)));
//                 }

//                 toInsert = isPositive? Integer.parseInt(x)  : -1 * Integer.parseInt(x);
//                 st.add(String.valueOf(toInsert));

             }else{
//                 if(x.equals("+")){
//                     isPositive = true;
//                 }
//                 if(x.equals("-")){
//                     isPositive = false;
//                 }

                 int toInsert = 0;
                 toInsert = isPositive? curVal  : -1 * curVal;
                 st.add(String.valueOf(toInsert));
                 curVal = 0;

                 isPositive = (x.equals("+"));
             }
         }

         if(curVal > 0){
             st.add(String.valueOf(curVal));
         }

         int res = 0;
         for(String val:st){
             res += Integer.parseInt(val);
         }


         return res;
     }





     private String doCalculate(String s1, String s2, String op){
         if(op.equals("*")){
             return String.valueOf(Integer.parseInt(s1) * Integer.parseInt(s2));
         }
         return String.valueOf(Integer.parseInt(s1) / Integer.parseInt(s2));
     }
}
