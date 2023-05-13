package LeetCodeJava.String;

// https://leetcode.com/problems/add-binary/

public class AddBinary {

    // V0
    public String addBinary(String a, String b) {

        //System.out.println("(before) a = " + a + " b = " + b);
        String res = "";
        Integer plus = 0;
        int len = Math.max(a.length(), b.length());
        if (len > a.length()) {
            a = multiplyString("0", len - a.length()) + a;
        } else {
            b = multiplyString("0", len - b.length()) + b;
        }
        //System.out.println("(after) a = " + a + " b = " + b);

        for (int i = len - 1; i >= 0; i--) {
            int cur = Integer.parseInt(String.valueOf(a.charAt(i))) +
                    Integer.parseInt(String.valueOf(b.charAt(i))) + plus;
            if (cur > 1) {
                plus = 1;
                cur -= 2;
            } else {
                plus = 0;
            }
            res += cur;
        }

        if (plus.equals(1)) {
            res += "1";
        }

        //System.out.println("res = " + res);
        return reverseString(res);
    }

    public static String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    private static String multiplyString(String str, int multiplier) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < multiplier; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    // V1
    // https://leetcode.com/problems/add-binary/solutions/3183205/1ms-beats-100-full-explanation-append-reverse-c-java-python3/
    public String addBinary_1(String a, String b)
    {
        StringBuilder sb = new StringBuilder();
        int carry = 0;
        int i = a.length() - 1;
        int j = b.length() - 1;

        while (i >= 0 || j >= 0 || carry == 1)
        {
            if(i >= 0)
                carry += a.charAt(i--) - '0';
            if(j >= 0)
                carry += b.charAt(j--) - '0';
            sb.append(carry % 2);
            carry /= 2;
        }
        return sb.reverse().toString();
    }


}
