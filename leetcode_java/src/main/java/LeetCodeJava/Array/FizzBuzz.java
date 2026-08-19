package LeetCodeJava.Array;

// https://leetcode.com/problems/fizz-buzz/

import java.util.ArrayList;
import java.util.List;

/**
 *  412. Fizz Buzz
 *  Easy
 *
 *  Given an integer n, return a string array answer (1-indexed) where:
 *
 *   answer[i] == "FizzBuzz" if i is divisible by 3 and 5.
 *   answer[i] == "Fizz" if i is divisible by 3.
 *   answer[i] == "Buzz" if i is divisible by 5.
 *   answer[i] == i (as a string) if none of the above conditions are true.
 *
 *  Example 1:
 *   Input: n = 3
 *   Output: ["1","2","Fizz"]
 *
 *  Example 2:
 *   Input: n = 15
 *   Output: ["1","2","Fizz","4","Buzz","Fizz","7","8","Fizz","Buzz",
 *            "11","Fizz","13","14","FizzBuzz"]
 *
 *  Constraints:
 *   1 <= n <= 10^4
 */
public class FizzBuzz {

    // V0
    // IDEA: SIMPLE MODULO CHECK (check 15 first)
    /**
     * time = O(n)
     * space = O(1) (excluding output)
     */
    public List<String> fizzBuzz(int n) {
        List<String> res = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            if (i % 15 == 0) {
                res.add("FizzBuzz");
            } else if (i % 3 == 0) {
                res.add("Fizz");
            } else if (i % 5 == 0) {
                res.add("Buzz");
            } else {
                res.add(String.valueOf(i));
            }
        }

        return res;
    }

    // V1
    // IDEA: NO MODULO — keep two rolling counters and reset them when they reach
    //       3 / 5, so the loop never performs a division.
    /**
     * time = O(n)
     * space = O(1) (excluding output)
     */
    public List<String> fizzBuzz_1(int n) {
        List<String> res = new ArrayList<>();
        int fizz = 0;
        int buzz = 0;

        for (int i = 1; i <= n; i++) {
            fizz++;
            buzz++;
            if (fizz == 3 && buzz == 5) {
                res.add("FizzBuzz");
                fizz = 0;
                buzz = 0;
            } else if (fizz == 3) {
                res.add("Fizz");
                fizz = 0;
            } else if (buzz == 5) {
                res.add("Buzz");
                buzz = 0;
            } else {
                res.add(String.valueOf(i));
            }
        }

        return res;
    }

    // V2
    // IDEA: LOOKUP TABLE — the answer pattern repeats with period 15, so index a
    //       precomputed cycle by i % 15 and fall back to the number itself.
    /**
     * time = O(n)
     * space = O(1) (a fixed 15-entry table, excluding output)
     */
    public List<String> fizzBuzz_2(int n) {
        String[] cycle = {"FizzBuzz", "", "", "Fizz", "", "Buzz", "Fizz", "",
                "", "Fizz", "Buzz", "", "Fizz", "", ""};
        List<String> res = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            String s = cycle[i % 15];
            res.add(s.isEmpty() ? String.valueOf(i) : s);
        }

        return res;
    }
}
