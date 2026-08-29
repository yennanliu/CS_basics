package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/prime-number-of-set-bits-in-binary-representation/

import java.util.HashSet;
import java.util.Set;

/**
 *  762. Prime Number of Set Bits in Binary Representation
 *  Easy
 *
 *  Given two integers left and right, return the count of numbers in the
 *  inclusive range [left, right] having a prime number of set bits in their
 *  binary representation.
 *
 *  Recall that the number of set bits an integer has is the number of 1's
 *  present when written in binary.
 *
 *  Example 1:
 *  Input: left = 6, right = 10
 *  Output: 4
 *  Explanation:
 *  6  -> 110 (2 set bits, 2 is prime)
 *  7  -> 111 (3 set bits, 3 is prime)
 *  8  -> 1000 (1 set bit, 1 is not prime)
 *  9  -> 1001 (2 set bits, 2 is prime)
 *  10 -> 1010 (2 set bits, 2 is prime)
 *  4 numbers have a prime number of set bits.
 *
 *  Example 2:
 *  Input: left = 10, right = 15
 *  Output: 5
 *
 *  Constraints:
 *  1 <= left <= right <= 10^6
 *  0 <= right - left <= 10^4
 */
public class PrimeNumberOfSetBitsInBinaryRepresentation {

    // V0
    // IDEA: right <= 10^6 < 2^20, so the popcount is at most 20 - just test the
    //       popcount against the small prime set {2,3,5,7,11,13,17,19}
    /**
     * time = O(n) with n = right - left + 1
     * space = O(1)
     */
    public int countPrimeSetBits(int left, int right) {
        Set<Integer> primes = new HashSet<>();
        int[] ps = { 2, 3, 5, 7, 11, 13, 17, 19 };
        for (int p : ps) {
            primes.add(p);
        }
        int res = 0;
        for (int i = left; i <= right; i++) {
            if (primes.contains(Integer.bitCount(i))) {
                res++;
            }
        }
        return res;
    }

    // V1
    // IDEA: same idea but the prime set is packed into a bitmask constant
    //       (bits 2,3,5,7,11,13,17,19 set => 665772)
    /**
     * time = O(n)
     * space = O(1)
     */
    public int countPrimeSetBits_1(int left, int right) {
        int primeMask = 665772; // (1<<2)|(1<<3)|(1<<5)|(1<<7)|(1<<11)|(1<<13)|(1<<17)|(1<<19)
        int res = 0;
        for (int i = left; i <= right; i++) {
            if (((primeMask >> Integer.bitCount(i)) & 1) == 1) {
                res++;
            }
        }
        return res;
    }

    // V2
    // IDEA: no lookup table at all - count the set bits with Brian Kernighan's
    //       trick (v &= v - 1 clears the lowest set bit), then decide primality
    //       of that count by trial division
    /**
     * time = O(n * (popcount + sqrt(popcount)))
     * space = O(1)
     */
    public int countPrimeSetBits_2(int left, int right) {
        int res = 0;
        for (int i = left; i <= right; i++) {
            int bits = 0;
            int v = i;
            while (v != 0) {
                v &= (v - 1);
                bits++;
            }
            if (isPrime_2(bits)) {
                res++;
            }
        }
        return res;
    }

    private boolean isPrime_2(int x) {
        if (x < 2) {
            return false;
        }
        for (int d = 2; (long) d * d <= x; d++) {
            if (x % d == 0) {
                return false;
            }
        }
        return true;
    }
}
