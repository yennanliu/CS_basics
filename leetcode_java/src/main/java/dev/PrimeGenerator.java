package dev;

import java.util.*;

public class PrimeGenerator {

    public static void printPrimes(int N) {
        if (N <= 2) {
            System.out.println("No primes less than " + N);
            return;
        }

        List<Integer> primes = new ArrayList<>();

        for (int num = 2; num < N; num++) {
            boolean isPrime = true;

            // Only check up to sqrt(num)
            int sqrt = (int) Math.sqrt(num);
            for (int p : primes) {
                if (p > sqrt) break;
                if (num % p == 0) {
                    isPrime = false;
                    break;
                }
            }

            if (isPrime) {
                primes.add(num);
            }
        }

        System.out.println("Primes less than " + N + ": " + primes);
    }

    public static void main(String[] args) {
        printPrimes(7);    // Output: 2, 3, 5
        printPrimes(10);   // Output: 2, 3, 5, 7
        printPrimes(13);   // Output: 2, 3, 5, 7, 11
        printPrimes(50);   // Output: 2, 3, 5, 7, 11, 13, 17, 19, ...
    }
}