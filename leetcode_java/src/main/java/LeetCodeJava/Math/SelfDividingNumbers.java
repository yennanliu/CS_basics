package LeetCodeJava.Math;

// https://leetcode.com/problems/self-dividing-numbers/

import java.util.ArrayList;
import java.util.List;

/**
 *  728. Self Dividing Numbers
 *  Easy
 *
 *  A self-dividing number is a number that is divisible by every digit it
 *  contains.
 *    - For example, 128 is a self-dividing number because
 *      128 % 1 == 0, 128 % 2 == 0, and 128 % 8 == 0.
 *  A self-dividing number is not allowed to contain the digit zero.
 *
 *  Given two integers left and right, return a list of all the self-dividing
 *  numbers in the range [left, right] (both inclusive).
 *
 *  Example 1:
 *    Input: left = 1, right = 22
 *    Output: [1,2,3,4,5,6,7,8,9,11,12,15,22]
 *
 *  Example 2:
 *    Input: left = 47, right = 85
 *    Output: [48,55,66,77]
 *
 *  Constraints:
 *   - 1 <= left <= right <= 10^4
 */
public class SelfDividingNumbers {

    // V0
    // IDEA: BRUTE FORCE -- for each number, walk its digits and check divisibility
    /**
     * time = O(n * log r), n = right - left + 1, r = right
     * space = O(1) (excluding output)
     */
    public List<Integer> selfDividingNumbers(int left, int right) {

        List<Integer> res = new ArrayList<>();

        for (int num = left; num <= right; num++) {
            if (isSelfDividing(num)) {
                res.add(num);
            }
        }

        return res;
    }

    private boolean isSelfDividing(int num) {
        int x = num;
        while (x > 0) {
            int digit = x % 10;
            if (digit == 0 || num % digit != 0) {
                return false;
            }
            x /= 10;
        }
        return true;
    }
}
