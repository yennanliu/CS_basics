package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/sum-of-two-integers/description/

public class SumOfTwoIntegers {

    // V0
    // IDEA : BIT OP
    // TODO : fix below
//    public int getSum(int a, int b) {
//
//        if (a == 0 && b == 0){
//            return 0;
//        }
//
//        if (a == 0 || b == 0){
//            if (a == 0){
//                return b;
//            }
//            return a;
//        }
//
//        // bit op
//        String aBin = Integer.toBinaryString(a);
//        String bBin = Integer.toBinaryString(b);
//
//        String res = addBinary(aBin, bBin);
//        return Integer.parseInt(res, 2);
//    }
//
//    public static String addBinary(String a, String b) {
//        StringBuilder sb = new StringBuilder();
//        int i = a.length() - 1, j = b.length() - 1, carry = 0;
//        while (i >= 0 || j >= 0 || carry > 0) {
//            int sum = carry;
//            if (i >= 0) sum += a.charAt(i--) - '0';
//            if (j >= 0) sum += b.charAt(j--) - '0';
//            sb.append(sum % 2);
//            carry = sum / 2;
//        }
//        return sb.reverse().toString();
//    }
//
////    private int add(String x, String y){
////
////         int len = Math.max(x.length(), y.length());
////         if (len > x.length()){
////             x = (multiplyStr("0", len - x.length()) + x);
////         }else{
////             y = (multiplyStr("0", len - x.length()) + y);
////         }
////
////         System.out.println("x = " + x + " y = " + y);
////
////         String res = "";
////
////         int plus = 0;
////         for (int j = x.length()-1; j >= 0; j --){
////             System.out.println("x.indexOf(j) = " + x.indexOf(j));
////             int cur = Integer.parseInt(String.valueOf(x.indexOf(j))) + Integer.parseInt(String.valueOf(y.indexOf(j))) + plus;
////             System.out.println("cur = " + cur);
////             if (cur > 1){
////                 cur -= 2;
////                 plus = 1;
////                 res += cur;
////             }else{
////                 res += cur;
////             }
////         }
////
////         if (plus != 0){
////             res = "1" + res;
////         }
////
////        // convert a Binary String to a base 10 integer in Java
////        // https://stackoverflow.com/questions/10178980/how-to-convert-a-binary-string-to-a-base-10-integer-in-java
////
////        System.out.println("res = " + res);
////        return Integer.parseInt(res, 2);
////    }
//
//    // https://stackoverflow.com/questions/2255500/can-i-multiply-strings-in-java-to-repeat-sequences
//    private static String multiplyStr(String s, int multiply){
//        StringBuilder r = new StringBuilder();
//        for (int i = 0; i < multiply; i++){
//            r.append(s);
//        }
//        return r.toString();
//    }

    // V1
    // IDEA : BIT OP
    // https://leetcode.com/problems/sum-of-two-integers/solutions/4623531/java-best-easy-solution-100-beats-0ms/
    public int getSum_1(int a, int b) {
        while(b!=0)
        {
            int carry=a&b;
            a=a ^ b;
            b=carry<<1;
        }
        return a;
    }

    // V2
    // https://leetcode.com/problems/sum-of-two-integers/solutions/84290/java-simple-easy-understand-solution-with-explanation/
    // Iterative
    public int getSum_2(int a, int b) {
        if (a == 0) return b;
        if (b == 0) return a;

        while (b != 0) {
            int carry = a & b;
            a = a ^ b;
            b = carry << 1;
        }

        return a;
    }

    // V3
    // https://leetcode.com/problems/sum-of-two-integers/solutions/84290/java-simple-easy-understand-solution-with-explanation/
    // Iterative
    public int getSubtract_3(int a, int b) {
        while (b != 0) {
            int borrow = (~a) & b;
            a = a ^ b;
            b = borrow << 1;
        }

        return a;
    }

}
