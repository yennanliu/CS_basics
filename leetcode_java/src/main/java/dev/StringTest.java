package dev;

public class StringTest {

    public static void main(String[] args) {
//
//        String x = "abc";
//        System.out.println(x.substring(1,3));
//

        String x_1 = "9871230";
        StringBuilder sb = new StringBuilder();
        sb.append(x_1);
        sb.append("0");
        int val = Integer.parseInt(sb.toString());
        System.out.println(val);


    }


}
