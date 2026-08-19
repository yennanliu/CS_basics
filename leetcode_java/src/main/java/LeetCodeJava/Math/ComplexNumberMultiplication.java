package LeetCodeJava.Math;

// https://leetcode.com/problems/complex-number-multiplication/

/**
 *  537. Complex Number Multiplication
 *  Medium
 *
 *  A complex number can be represented as a string on the form "real+imaginaryi" where:
 *    - real is the real part and is an integer in the range [-100, 100].
 *    - imaginary is the imaginary part and is an integer in the range [-100, 100].
 *    - i^2 == -1.
 *
 *  Given two complex numbers num1 and num2 as strings, return a string of the complex
 *  number that represents their multiplications.
 *
 *  Example 1:
 *    Input: num1 = "1+1i", num2 = "1+1i"
 *    Output: "0+2i"
 *    Explanation: (1 + i) * (1 + i) = 1 + i^2 + 2 * i = 2i
 *
 *  Example 2:
 *    Input: num1 = "1+-1i", num2 = "1+-1i"
 *    Output: "0+-2i"
 *
 *  Constraints:
 *    num1 and num2 are valid complex numbers.
 */
public class ComplexNumberMultiplication {

    // V0
    // IDEA: parse "a+bi" (the real part never contains '+', so the first '+' is the split
    //       point), then (a+bi)(c+di) = (ac - bd) + (ad + bc)i
    /**
     * time = O(n)
     * space = O(n)
     */
    public String complexNumberMultiply(String num1, String num2) {
        int[] p = parse(num1);
        int[] q = parse(num2);
        int real = p[0] * q[0] - p[1] * q[1];
        int imag = p[0] * q[1] + p[1] * q[0];
        return real + "+" + imag + "i";
    }

    // parse "a+bi" -> {a, b}
    private int[] parse(String s) {
        int plus = s.indexOf('+');
        int real = Integer.parseInt(s.substring(0, plus));
        int imag = Integer.parseInt(s.substring(plus + 1, s.length() - 1));
        return new int[] { real, imag };
    }
}
