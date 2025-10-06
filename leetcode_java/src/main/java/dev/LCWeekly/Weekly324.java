package dev.LCWeekly;

// https://leetcode.com/contest/weekly-contest-324/

import java.util.*;

public class Weekly324 {

    // Q1
    // https://leetcode.com/problems/count-pairs-of-similar-strings/description/
    // LC 2506
    // 16.32 - 16.42 pm
    /**
     *  Return the `number` of `pairs (i, j)`
     *  such that 0 <= i < j <= word.length - 1
     *   and the two strings words[i] and words[j] are `similar.`
     *
     *   -> get the cnt of `similar word pair` for any
     *      different word in words
     *
     *  - `similar` : 2 word that has the SAME SET of alphabet
     *
     *
     *
     *  IDEA 1) HASHMAP
     *
     *
     */
    public int similarPairs(String[] words) {
        if (words == null || words.length < 2) {
            return 0;
        }

        Map<String, Integer> map = new HashMap<>();

        for (String word : words) {
            String key = normalize(word);
            map.put(key, map.getOrDefault(key, 0) + 1);
        }

        int res = 0;
        for (int val : map.values()) {
            if (val >= 2) {
                res += val * (val - 1) / 2; // nC2
            }
        }

        return res;
    }

    private String normalize(String str) {
        char[] chars = str.toCharArray();
        Set<Character> set = new HashSet<>();
        for (char c : chars) {
            set.add(c);
        }
        List<Character> list = new ArrayList<>(set);
        Collections.sort(list); // ensure deterministic ordering
        StringBuilder sb = new StringBuilder();
        for (char c : list) {
            sb.append(c);
        }
        return sb.toString(); // this string uniquely represents the set
    }

//    public int similarPairs(String[] words) {
//        // edge
//        if(words == null || words.length == 0){
//            return 0;
//        }
//        if(words.length == 1){
//            return 1;
//        }
//        // `normalize` word
//        List<String> list = new ArrayList();
//        for(String w: words){
//            list.add(deDup(w));
//        }
//
//        System.out.println(">>> list = " + list);
//        // map : { val : cnt }
//        Map<String, Integer> map = new HashMap<>();
//        for(String x: list){
//            map.put(x, map.getOrDefault(x, 0 ) + 1);
//        }
//
//        System.out.println(">>> map = " + map);
//
//        int res = 0;
//
//        for(int val: map.values()){
//            /**
//             *  combination of `select 2 from k `
//             *  ->  (k!)*(2!) / (k-2)!
//             *
//             */
//            if(val < 2){
//                continue;
//            }
//            if(val == 2){
//                res += 1;
//            }else{
//                long cnt =  calculateFactorial(val) / ( calculateFactorial(val - 2) * 2 );
//                System.out.println(">>> (calculateFactorial) val = " + val + ", cnt = " + cnt);
//                res += (int) cnt;
//            }
//        }
//
//
//        return res;
//    }
//
//    public static long calculateFactorial(int n) {
//        if (n < 0) throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
//        long result = 1;
//        for (int i = 2; i <= n; i++) {
//            result *= i;
//        }
//        return result;
//    }
//
//    private String deDup(String str){
//        //String res = "";
//        Set<String> set = new HashSet<>();
//        for(String x: str.split("")){
//            set.add(x);
//        }
//        return set.toString(); // ???
//    }


    // Q2
    // LC 2507
    // 17.07 - 17 pm
    // https://leetcode.com/problems/smallest-value-after-replacing-with-sum-of-prime-factors/description/
    /**
     *  IDEA 1) MATH + BRUTE FORCE
     *
     *
     */
    /**
     * Calculates the smallest value by repeatedly replacing a number n with the
     * sum of its prime factors until n becomes a prime number.
     * The process stops when n == sum_of_prime_factors(n).
     */
    public int smallestValue(int n) {
        // Base case: If n is 1, return 1. (Though constraints start at n=2)
        if (n <= 1) {
            return n;
        }

        // Loop as long as the number can be reduced.
        // We break when sumOfFactors == n, which means n is prime.
        while (true) {
            // Get the list of prime factors (including duplicates)
            List<Integer> factors = getPrimeFactors(n);

            // Calculate the sum of these factors
            int sumOfFactors = getListSum(factors);

            // If the number doesn't change, we've reached a prime number (the smallest value).
            if (sumOfFactors == n) {
                break;
            }

            // Otherwise, replace n with the new sum and repeat.
            n = sumOfFactors;
        }

        return n;
    }

    /**
     * Correctly finds the prime factors of x (including duplicates).
     * This replaces the flawed divideToFactors function.
     */
    private List<Integer> getPrimeFactors(int x) {
        List<Integer> res = new ArrayList<>();
        int i = 2;

        // Loop up to the square root of the current value of x.
        while (i * i <= x) {
            while (x % i == 0) {
                res.add(i);
                x = x / i;
            }
            // Only increment i for the next potential prime factor
            i += 1;
        }

        // If x is greater than 1 after the loop, the remaining x is the largest prime factor.
        if (x > 1) {
            res.add(x);
        }

        return res;
    }

    /**
     * Calculates the sum of all elements in the list.
     * This function was already correct.
     */
    private int getListSum(List<Integer> list) {
        int res = 0;
        for (int x : list) {
            res += x;
        }
        return res;
    }

    // The unnecessary and flawed hasFactor function is removed.

//    public int smallestValue(int n) {
//        // edge
//        if(n <= 3){
//            return n;
//        }
//
//        while(hasFactor(n)){
//            // ....
//            List<Integer> list = divideToFactors(n);
//            System.out.println(">>> n = " + n + ", list = " + list);
//            n = getListSum(list);
//        }
//
//        return n;
//    }
//
//    private int getListSum(List<Integer> list){
//        int res = 0;
//        for(int x: list){
//            res += x;
//        }
//        return res;
//    }
//
//    private List<Integer> divideToFactors(int x){
//        List<Integer> res = new ArrayList<>();
//        // ????
//        int i = 2;
//        //int sqrtX = (int) Math.sqrt(x); // ??
//        while(i < x){
//            // ???
//            while(x % i == 0){
//                //return true;
//                res.add(i);
//                x = x / i; // ???
//            }
//            i += 1;
//        }
//
//        // ??
//        if(x != 1){
//            res.add(x); // ?? append the `remaining val` to list
//        }
//
//        return res;
//    }
//
//    private boolean hasFactor(int x){
//        // ???
//        int sqrtX = (int) Math.sqrt(x); // ??
//        int i = 2;
//        System.out.println(">>> (hasFactor)  x = " + x + ", sqrtX = " + sqrtX);
//        while(i <= sqrtX + 1){
//            System.out.println(">>> (hasFactor)  x = " + x + ", i = " + i);
//            if(x % i == 0){
//                return true;
//            }
//            i += 1;
//        }
//        return false;
//    }




    // Q3
    // https://leetcode.com/problems/add-edges-to-make-degrees-of-all-nodes-even/description/

    // Q4
    // https://leetcode.com/problems/cycle-length-queries-in-a-tree/description/

}
