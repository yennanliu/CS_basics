import java.util.ArrayList;
import java.util.List;

public class PrimeGenerator2 {

    private static boolean isPrime(int x, List<Integer> previousPrimes) {
        if (x <= 1) return false;
        if (x == 2) return true;

        int sqrt = (int) Math.sqrt(x);
        for (int p : previousPrimes) {
            if (p > sqrt) break;
            if (x % p == 0) return false;
        }
        return true;
    }

    public static List<Integer> generatePrimes(int N) {
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i < N; i++) {
            if (isPrime(i, primes)) {
                primes.add(i);
            }
        }
        return primes;
    }

    public static void main(String[] args){

        System.out.println(generatePrimes(7));    // Output: 2, 3, 5
        System.out.println(generatePrimes(10));  // Output: 2, 3, 5, 7
        System.out.println(generatePrimes(13));   // Output: 2, 3, 5, 7, 11
        System.out.println(generatePrimes(50));// Output: 2, 3, 5, 7, 11, 13, 17, 19, ...
    }

}
