package LeetCodeJava.Math;

// https://leetcode.com/problems/powx-n/description/

public class Pow {

    // V0
    // IDEA : MATH
    // TODO : fix below
//    public double myPow(double x, int n) {
//
//        if (x == 1.0 || x == 0.0){
//            return x;
//        }
//
//        if (n == 0){
//            return 1;
//        }
//
//        boolean negative = false;
//        if (n < 0){
//            negative = true;
//            n = n * -1;
//        }
//        double res = 1.0;
//        while (n > 0){
//            res = res * x;
//            n -= 1;
//        }
//
//        if (negative){
//            return 1 / res;
//        }
//
//        return res;
//    }

    // V1
    // https://leetcode.com/problems/powx-n/solutions/4919918/beats-100-00-of-users-with-java-simple-easy-well-explained-solution-using-loop/
    public double myPow_1(double x, int n) {
        double ans=1;
        long n_temp = n;
        if(n_temp<0){
            n_temp *= -1;
        }
        while(n_temp>0){
            if(n_temp%2==0){
                x = x*x;
                n_temp = n_temp/2;
            }else{
                ans = ans * x;
                n_temp--;
            }
        }
        if(n<0){
            return (double) 1/ans;
        }
        return ans;
    }

    // V2
    // IDEA : BIT OP
    // https://leetcode.com/problems/powx-n/
    public double myPow_2(double x, int n) {

        if(n < 0){
            n = -n;
            x = 1 / x;
        }

        double pow = 1;

        while(n != 0){
            if((n & 1) != 0){
                pow *= x;
            }

            x *= x;
            n >>>= 1;

        }

        return pow;
    }

}
