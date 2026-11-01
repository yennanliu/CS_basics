package LeetCodeJava.Math;

// https://leetcode.com/problems/multiply-two-polynomials/description/

import java.math.BigInteger;

/**
 * 3549. Multiply Two Polynomials
 * Hard
 *
 * You are given two integer arrays poly1 and poly2, where the element at index i
 * in each array represents the coefficient of x^i in a polynomial.
 *
 * Let A(x) and B(x) be the polynomials represented by poly1 and poly2, respectively.
 *
 * Return an integer array result of length (poly1.length + poly2.length - 1)
 * representing the coefficients of the product polynomial R(x) = A(x) * B(x),
 * where result[i] denotes the coefficient of x^i in R(x).
 *
 *
 * Example 1:
 *
 * Input: poly1 = [3,2,5], poly2 = [1,4]
 * Output: [3,14,13,20]
 *
 * Example 2:
 *
 * Input: poly1 = [1,0,-2], poly2 = [-1]
 * Output: [-1,0,2]
 *
 * Example 3:
 *
 * Input: poly1 = [1,5,-3], poly2 = [-4,2,0]
 * Output: [-4,-18,22,-6,0]
 *
 *
 * Constraints:
 *
 * 1 <= poly1.length, poly2.length <= 5 * 10^4
 * -10^3 <= poly1[i], poly2[i] <= 10^3
 * poly1 and poly2 contain at least one non-zero coefficient.
 *
 */
public class MultiplyTwoPolynomials {

    // V0
    // IDEA: FFT CONVOLUTION
    //   the schoolbook double loop is 2.5 * 10^9 multiplications at these sizes and
    //   times out. evaluate both polynomials at the 2^k-th roots of unity with an
    //   FFT, multiply point-wise, and interpolate back with the inverse FFT.
    //   every true coefficient is an integer with |c| <= 5 * 10^10, far inside a
    //   double's 2^53 exact range, so rounding the inverse transform is exact.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long[] multiply(int[] poly1, int[] poly2) {
        int resultLength = poly1.length + poly2.length - 1;

        // FFT size: the smallest power of two that holds the whole product
        int size = 1;
        while (size < resultLength) {
            size *= 2;
        }

        double[] aReal = new double[size];
        double[] aImag = new double[size];
        double[] bReal = new double[size];
        double[] bImag = new double[size];
        for (int i = 0; i < poly1.length; i++) {
            aReal[i] = poly1[i];
        }
        for (int i = 0; i < poly2.length; i++) {
            bReal[i] = poly2[i];
        }

        // coefficients -> point values
        fft(aReal, aImag, false);
        fft(bReal, bImag, false);

        // point-wise complex product: (a + bi)(c + di) = (ac - bd) + (ad + bc)i
        for (int i = 0; i < size; i++) {
            double real = aReal[i] * bReal[i] - aImag[i] * bImag[i];
            double imag = aReal[i] * bImag[i] + aImag[i] * bReal[i];
            aReal[i] = real;
            aImag[i] = imag;
        }

        // point values -> coefficients
        fft(aReal, aImag, true);

        long[] result = new long[resultLength];
        for (int i = 0; i < resultLength; i++) {
            result[i] = Math.round(aReal[i]);
        }
        return result;
    }

    /** In-place iterative Cooley-Tukey FFT; invert = true computes the inverse (scaled by 1/n). */
    private void fft(double[] real, double[] imag, boolean invert) {
        int n = real.length;

        // bit-reversal permutation: put each element where the butterflies expect it
        int j = 0;
        for (int i = 1; i < n; i++) {
            int bit = n / 2;
            while ((j & bit) != 0) {
                j ^= bit;
                bit /= 2;
            }
            j ^= bit;
            if (i < j) {
                double tempReal = real[i];
                real[i] = real[j];
                real[j] = tempReal;
                double tempImag = imag[i];
                imag[i] = imag[j];
                imag[j] = tempImag;
            }
        }

        // combine sub-transforms of length len/2 into transforms of length len
        for (int len = 2; len <= n; len *= 2) {
            double angle = 2 * Math.PI / len * (invert ? -1 : 1);
            int halfLen = len / 2;
            for (int start = 0; start < n; start += len) {
                for (int k = 0; k < halfLen; k++) {
                    // twiddle factor w = e^(i * angle * k), computed directly for accuracy
                    double wReal = Math.cos(angle * k);
                    double wImag = Math.sin(angle * k);

                    int evenIndex = start + k;
                    int oddIndex = start + k + halfLen;
                    double oddReal = real[oddIndex] * wReal - imag[oddIndex] * wImag;
                    double oddImag = real[oddIndex] * wImag + imag[oddIndex] * wReal;

                    real[oddIndex] = real[evenIndex] - oddReal;
                    imag[oddIndex] = imag[evenIndex] - oddImag;
                    real[evenIndex] = real[evenIndex] + oddReal;
                    imag[evenIndex] = imag[evenIndex] + oddImag;
                }
            }
        }

        if (invert) {
            for (int i = 0; i < n; i++) {
                real[i] /= n;
                imag[i] /= n;
            }
        }
    }

    // V1
    // IDEA: KRONECKER SUBSTITUTION -- LET BigInteger's MULTIPLY DO THE WORK
    //   evaluate both polynomials at x = 2^40: each coefficient sits in its own 40-bit
    //   slot of one huge integer, and the integer product's slots are exactly the
    //   convolution (|product coefficient| <= 5 * 10^10 < 2^40, so no slot overflows).
    //   slots must stay non-negative, so split each polynomial into its positive and
    //   negative parts and recombine (A+ - A-)(B+ - B-) = (A+B+ + A-B-) - (A+B- + A-B+).
    //   BigInteger uses Toom-Cook for large operands, so this is sub-quadratic.
    /**
     * time = O(N^1.47)  (Toom-Cook 3 inside BigInteger.multiply)
     * space = O(N)
     */
    public long[] multiply_1(int[] poly1, int[] poly2) {
        int resultLength = poly1.length + poly2.length - 1;

        BigInteger aPositive = pack(poly1, true);
        BigInteger aNegative = pack(poly1, false);
        BigInteger bPositive = pack(poly2, true);
        BigInteger bNegative = pack(poly2, false);

        // like-sign products contribute +, opposite-sign products contribute -
        long[] positive = unpack(aPositive.multiply(bPositive).add(aNegative.multiply(bNegative)), resultLength);
        long[] negative = unpack(aPositive.multiply(bNegative).add(aNegative.multiply(bPositive)), resultLength);

        long[] result = new long[resultLength];
        for (int i = 0; i < resultLength; i++) {
            result[i] = positive[i] - negative[i];
        }
        return result;
    }

    // each slot is 5 bytes = 40 bits wide
    private static final int SLOT_BYTES = 5;

    /** Packs the positive (or the negated negative) coefficients into 5-byte slots of one integer. */
    private BigInteger pack(int[] coefficients, boolean keepPositive) {
        int slots = coefficients.length;
        // big-endian magnitude: slot i occupies the bytes counted from the END of the array
        byte[] bytes = new byte[SLOT_BYTES * slots];
        for (int i = 0; i < slots; i++) {
            int value = keepPositive ? coefficients[i] : -coefficients[i];
            if (value <= 0) {
                continue;
            }
            int lowestByte = bytes.length - 1 - SLOT_BYTES * i;
            // write value little byte first, moving towards the front of the array
            for (int b = 0; b < SLOT_BYTES; b++) {
                bytes[lowestByte - b] = (byte) (value & 0xFF);
                value >>= 8;
            }
        }
        // signum 1: treat the bytes as an unsigned magnitude
        return new BigInteger(1, bytes);
    }

    /** Reads the first count 5-byte slots of a non-negative integer, lowest slot first. */
    private long[] unpack(BigInteger packed, int count) {
        byte[] bytes = packed.toByteArray(); // big-endian, may carry a leading 0 sign byte
        long[] slots = new long[count];
        for (int i = 0; i < count; i++) {
            int lowestByte = bytes.length - 1 - SLOT_BYTES * i;
            long value = 0;
            // read the slot's bytes from its most significant byte down
            for (int b = SLOT_BYTES - 1; b >= 0; b--) {
                int index = lowestByte - b;
                int unsignedByte = index >= 0 ? (bytes[index] & 0xFF) : 0;
                value = (value << 8) | unsignedByte;
            }
            slots[i] = value;
        }
        return slots;
    }
}
