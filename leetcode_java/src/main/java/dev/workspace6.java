package dev;

import java.util.Stack;

public class workspace6 {
    public static void main(String[] args) {

        // inverse order
        int[] array_1 = new int[3];
        array_1[0] = 1;
        array_1[1] = 2;
        array_1[2] = 3;

//        for (int i = array_1.length - 1; i >= 0; i--) {
//            System.out.println(array_1[i]);
//        }

        // s = "3[a]2[bc]", return "aaabcbc".
        /**
         *
         *      * s = "3[a]2[bc]", return "aaabcbc".
         *      * s = "3[a2[c]]", return "accaccacc".
         *      * s = "2[abc]3[cd]ef", return "abcabccdcdcdef".
         *
         */
        String x = "3[a]2[bc]"; //"3[a2[c]]"; //"2[abc]3[cd]ef"; //"3[a2[c]]"; //"3[a]2[bc]"; //"2[abc]3[cd]ef"; //"3[a2[c]]"; //"3[a]2[bc]";
        String y = decodeString(x);
       // System.out.println(">>> y = " + y);
    }

    public static String decodeString(String s) {
        if (s.length() == 0) {
            return null;
        }
        // init
        Stack<String> stack = new Stack<>(); // ??
        StringBuilder sb = new StringBuilder();
        String A_TO_Z = "abcdefghijklmnopqrstuvwxyz";
        for (String x : s.split("")) {
            System.out.println(">>> x = " + x);
            String tmp = "";
            StringBuilder tmpSb = new StringBuilder();
            if (!x.equals("]")) {
                if (!x.equals("[")) {
                    stack.add(x);
                }
            } else {
                // pop all elements from stack, multiply, and add to res
                //String tmp = "";
                while (!stack.isEmpty()) {
                    String cur = stack.pop(); // ??
                    if (A_TO_Z.contains(cur)) {
                        tmp = cur + tmp;
                        //tmpSb.append(tmpSb);
                    } else {
                        tmp = getMultiplyStr(tmp, Integer.parseInt(cur));
                    }
                }

            }

            sb.append(tmp);
        }

        //String tmpRes = sb.toString();

        StringBuilder tmpSb = new StringBuilder();

        // add remaining stack element to result
        while(!stack.isEmpty()){
            tmpSb.append(stack.pop());
            //tmpRes =  tmpRes + stack.pop();
        }

        sb.append(tmpSb.reverse());

        return sb.toString();
    }

    private static String getMultiplyStr(String cur, Integer multiply) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < multiply; x++) {
            sb.append(cur);
        }
        return sb.toString();
    }

}
