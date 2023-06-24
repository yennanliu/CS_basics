package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/valid-perfect-square/

public class ValidPerfectSquare {

    // V0
    // IDEA : BINARY SEARCH
//    public static boolean isPerfectSquare(int num) {
//
//        if (num == 1){
//            return true;
//        }
//
//        int left = 2;
//        int right = num / 2;
//        int tmp = 0;
//
//        while (right >= left){
//            //int mid = (left + right) / 2;
//            //int mid = left + ((right - left) / 2);
//            int mid = left + (right - left) / 2;
//            tmp = mid * mid;
//            System.out.println("left = " + left + " right = " + right + " mid = " + mid + " tmp = " + tmp);
//
//            if (tmp == num){
//                return true;
//            }
//
//            if (tmp > num){
//                right = mid - 1;
//            }else{
//                left = mid + 1;
//            }
//        }
//        return false;
//    }

    // V1
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/valid-perfect-square/editorial/
    public boolean isPerfectSquare(int num) {
        if (num < 2) {
            return true;
        }

        long left = 2, right = num / 2, x, guessSquared;
        while (left <= right) {
            x = left + (right - left) / 2;
            guessSquared = x * x;
            if (guessSquared == num) {
                return true;
            }
            if (guessSquared > num) {
                right = x - 1;
            } else {
                left = x + 1;
            }
        }
        return false;
    }


}
