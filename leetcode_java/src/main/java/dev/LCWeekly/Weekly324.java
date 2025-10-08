package dev.LCWeekly;

// https://leetcode.com/contest/weekly-contest-324/

import javax.print.DocFlavor;
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
    // LC 2508
    // 17.48 - 17.58 pm
    // https://leetcode.com/problems/add-edges-to-make-degrees-of-all-nodes-even/description/
    /**
     *
     *  -> Return `true` if it is possible to make
     *     the `degree` of each node in the graph `EVEN`, otherwise return false.
     *
     *   - The `degree` of a node is the `number of edges connected to it.`
     *   - You can add `at most two additional edges` (possibly none) to this graph
     *      so that there are NO `repeated edges and no self-loops.`
     *      
     *   - There is an undirected graph consisting of n nodes
     *     numbered from 1 to n.
     *
     *
     *   IDEA 1) GRAPH + hashMap + `degree check`
     *      -> M
     *
     *
     *
     *   ex 1)
     *
     *    n =  3
     *
     *      *  *
     *       *
     */
    public boolean isPossible(int n, List<List<Integer>> edges) {
        // edge
        if(n <= 1){
            return true;
        }
        if(n == 2){
            return false;
        }
        if(n == 3){
            return true; // ???
        }
        // build map
        // map:  { node : List[neighbor_1, neighbor_2,...] }
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(List<Integer> edge: edges){
            Integer _start = edge.get(0);
            Integer _end = edge.get(1);
            
            // start -> end
            List<Integer> list = new ArrayList<>();
            if(map.containsKey(_start)){
                list = map.get(_start);
            }
            map.put(_start, list);
            
            // end -> start
            List<Integer> list2 = new ArrayList<>();
            if(map.containsKey(_end)){
                list2 = map.get(_end);
            }
            map.put(_end, list2);
            
        }
        
        // edge: ALL nodes already have `even` degree
        if(isAllEven(map)){
            return true;
        }
        
        // check if it's possible
        // to update graph by `2 operation`
        // if yes, return true; otherwise false

        //return false;
        return updateGraph(n, map);
    }

    private boolean updateGraph(int n, Map<Integer, List<Integer>> map){

        // loop over `non even` edge
        for(Integer k: map.keySet()){
            // need to update
            List<Integer> connected = map.get(k);
            if(connected.size() % 2 != 0){
                // loop over `candidates
                for(int i = 1; i < n + 1; i++){
                    if(!connected.contains(i)){
                        // try to connect
                        connected.add(i);
                        // update the `connected node`'s connected node as well
                        //canUpdateToValid(n, )
                        List<Integer> connected2 = map.get(i);
                        connected2.add(k);
                        // update map
                        map.put(k, connected);
                        map.put(i, connected2);

                        // ??? check if `current state is ALL EVEN`
                        if(isAllEven(map)){
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean isAllEven(Map<Integer, List<Integer>> map){
        for(List<Integer> e: map.values()){
            if(e.size() % 2 != 0){
                return false;
            }
        }
        return true;
    }
    

    // Q4
    // https://leetcode.com/problems/cycle-length-queries-in-a-tree/description/
    // LC 2509
    // 8.31 - 9.00 am
    /**
     *  -> Return an array answer of length m
     *     where answer[i] is the answer to the ith query.
     *
     *  - You are given an integer n.
     *    There is a complete binary tree with 2n - 1 nodes.
     *
     *  - You are also given a 2D integer array queries of length m,
     *    where queries[i] = [ai, bi].
     *
     *  - For each query, solve the following problem:
     *      1. Add an edge between the nodes with values ai and bi.
     *      2. Find the length of the cycle in the graph.
     *      3. Remove the added edge between nodes with values ai and bi.
     *
     *
     *    IDEA 1) DFS + QUICK UNION ???
     *
     */
    public int[] cycleLengthQueries(int n, int[][] queries) {
        // edge
        //if(n == )
        // build graph

        return null;
    }

}
