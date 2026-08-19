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
}
