package LeetCodeJava.Math;

// https://leetcode.com/problems/powx-n/description/
/**
 * 50. Pow(x, n)
 * Solved
 * Medium
 * Topics
 * Companies
 * Implement pow(x, n), which calculates x raised to the power n (i.e., xn).
 *
 *
 *
 * Example 1:
 *
 * Input: x = 2.00000, n = 10
 * Output: 1024.00000
 * Example 2:
 *
 * Input: x = 2.10000, n = 3
 * Output: 9.26100
 * Example 3:
 *
 * Input: x = 2.00000, n = -2
 * Output: 0.25000
 * Explanation: 2-2 = 1/22 = 1/4 = 0.25
 *
 *
 * Constraints:
 *
 * -100.0 < x < 100.0
 * -231 <= n <= 231-1
 * n is an integer.
 * Either x is not zero or n > 0.
 * -104 <= xn <= 104
 *
 */
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

    // V1-1
    // https://neetcode.io/problems/pow-x-n
    // IDEA: BRUTE FORCE
    public double myPow_1_1(double x, int n) {
        if (x == 0) {
            return 0;
        }
        if (n == 0) {
            return 1;
        }

        double res = 1;
        for (int i = 0; i < Math.abs(n); i++) {
            res *= x;
        }
        return n >= 0 ? res : 1 / res;
    }

    // V1-2
    // https://neetcode.io/problems/pow-x-n
    // IDEA: Binary Exponentiation (Recursive)
    public double myPow_1_2(double x, int n) {
        if (x == 0) {
            return 0;
        }
        if (n == 0) {
            return 1;
        }

        double res = helper(x, Math.abs((long) n));
        return (n >= 0) ? res : 1 / res;
    }

    private double helper(double x, long n) {
        if (n == 0) {
            return 1;
        }
        double half = helper(x, n / 2);
        return (n % 2 == 0) ? half * half : x * half * half;
    }


    // V1-3
    // https://neetcode.io/problems/pow-x-n
    // IDEA: Binary Exponentiation (Iterative)
    public double myPow_1_3(double x, int n) {
        if (x == 0) return 0;
        if (n == 0) return 1;

        double res = 1;
        long power = Math.abs((long)n);

        while (power > 0) {
            if ((power & 1) == 1) {
                res *= x;
            }
            x *= x;
            power >>= 1;
        }

        return n >= 0 ? res : 1 / res;
    }

    // V2
    // IDEA: GREEDY (fix by gpt)
    public double myPow_2(double x, int n) {
        if (n == 0)
            return 1.0; // ✅ Fix incorrect return value

        long N = n; // ✅ Convert to long to avoid overflow
        boolean isNegativePower = N < 0;
        if (isNegativePower) {
            N = -N;
            x = 1 / x; // ✅ Invert `x` for negative exponent
        }

        double res = 1.0;
        while (N > 0) {
            if (N % 2 == 1) {
                res *= x; // Multiply when exponent is odd
            }
            x *= x; // Square the base
            N /= 2; // Halve the exponent
        }
        return res;
    }


    // V3
    // https://leetcode.com/problems/powx-n/solutions/4919918/beats-100-00-of-users-with-java-simple-easy-well-explained-solution-using-loop/
    public double myPow_3(double x, int n) {
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

    // V4
    // IDEA : BIT OP
    // https://leetcode.com/problems/powx-n/
    public double myPow_4(double x, int n) {

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
