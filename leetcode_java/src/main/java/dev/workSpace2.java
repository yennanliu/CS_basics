package dev;

public class workSpace2 {

    public static void main(String[] args) {

//        Integer[] my_list = new Integer[0];
//        for (int i = 0; i < my_list.length; i++){
//            System.out.println(my_list[i]);
//
//            Integer.parseInt("1");
//
//        }

//        String x = "abcd";
//        System.out.println(x.substring(1,3));
//        StringBuilder sb = new StringBuilder(x.substring(1,3));
//        System.out.println(sb.reverse());
//
//        System.out.println(x.substring(x.length()-1, x.length()));


        String a = new String("123");
        String b = new String("123");

        System.out.println(a==b); // false ( "new" will use new address in internal storage)

        String c = b;

        System.out.println(c==b); // true ( c will fetch from constant pool (常量池))

    }

}
