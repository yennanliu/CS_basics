package LeetCodeJava.HashTable;

// https://leetcode.com/problems/happy-number/

import java.util.HashSet;
import java.util.Set;

/**
 * 202. Happy Number
 * Easy
 *
 * Write an algorithm to determine if a number n is happy.
 *
 * A happy number is a number defined by the following process:
 *
 * Starting with any positive integer, replace the number by the sum of the squares of its digits.
 * Repeat the process until the number equals 1 (where it will stay), or it loops endlessly in a cycle which does not include 1.
 * Those numbers for which this process ends in 1 are happy.
 *
 * Return true if n is a happy number, and false if not.
 *
 * Example 1:
 *
 * Input: n = 19
 * Output: true
 * Explanation:
 * 1^2 + 9^2 = 82
 * 8^2 + 2^2 = 68
 * 6^2 + 8^2 = 100
 * 1^2 + 0^2 + 0^2 = 1
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: false
 *
 * Constraints:
 *
 * 1 <= n <= 2^31 - 1
 *
 */
public class HappyNumber {
    // V0

    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isHappy(int n) {

        Set<Integer> seen = new HashSet<>();

        while(n != 1 && !seen.contains(n)){
            seen.add(n);
            System.out.println("n = " + n);
            n = getSquareSum(n);
        }
        return n == 1;
    }

    private int getSquareSum(int input){
        int res = 0;
        while (input != 0){
            int tmp = input % 10;
            res += tmp * tmp;
            input = input / 10;
        }
        return res;
    }

}
