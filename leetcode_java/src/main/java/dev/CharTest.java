package dev;

public class CharTest {
  public static void main(String[] args) {

    // TEST 1
    // LC 127. Word Ladder
    // for (char c = 'a'; c <= 'z'; c++) {
//    for (char c = 'a'; c <= 'z'; c++) {
//      System.out.println(c);
//      // output as below:
//      /** a b c d e f g h i j k l m n o p q r s t u v w x y z */
//    }

    String x = "abcxyz";
    for ( Character c: x.toCharArray()){
      /**
       *  c = a, a - ? = 0
       *  c = b, a - ? = -1
       *  c = c, a - ? = -2
       *  c = x, a - ? = -23
       *  c = y, a - ? = -24
       *  c = z, a - ? = -25
       */
      System.out.println(" c = " + c +
              ", a - ? = " + ('a' - c)
      );
    }


  }
}
