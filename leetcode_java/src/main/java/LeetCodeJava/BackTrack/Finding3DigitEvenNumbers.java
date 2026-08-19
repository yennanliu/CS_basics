package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/finding-3-digit-even-numbers/

/**
 *  2094. Finding 3-Digit Even Numbers
 *  Easy
 *
 *  You are given an integer array digits, where each element is a digit. The
 *  array may contain duplicates.
 *
 *  You need to find all the unique integers that follow the given requirements:
 *    - The integer consists of the concatenation of three elements from digits
 *      in any arbitrary order.
 *    - The integer does not have leading zeros.
 *    - The integer is even.
 *
 *  Return a sorted array of the unique integers.
 *
 *  Example 1:
 *    Input: digits = [2,1,3,0]
 *    Output: [102,120,130,132,210,230,302,310,312,320]
 *
 *  Example 2:
 *    Input: digits = [2,2,8,8,2]
 *    Output: [222,228,282,288,822,828,882]
 *    Explanation: The same digit can be used as many times as it appears.
 *
 *  Constraints:
 *    3 <= digits.length <= 100
 *    0 <= digits[i] <= 9
 */
public class Finding3DigitEvenNumbers {

    // V0
    // IDEA: ENUMERATE ALL 450 THREE-DIGIT EVEN NUMBERS, CHECK MULTISET SUPPLY
    //       instead of permuting the input, walk the 450 even numbers in
    //       [100, 999] and ask "does `digits` hold enough of each digit?".
    //       that makes the rules trivial: starting at 100 kills leading zeros,
    //       stepping by 2 keeps them even, and ascending enumeration yields a
    //       sorted, unique list for free.
    /**
     * time = O(N + 450) -> O(N)
     * space = O(1)
     */
    public int[] findEvenNumbers(int[] digits) {
        int[] have = new int[10];
        for (int d : digits) {
            have[d]++;
        }

        int[] buf = new int[450];
        int size = 0;
        for (int x = 100; x < 1000; x += 2) {
            int[] need = new int[10];
            need[x / 100]++;
            need[(x / 10) % 10]++;
            need[x % 10]++;
            boolean ok = true;
            for (int d = 0; d < 10; d++) {
                if (need[d] > have[d]) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                buf[size++] = x;
            }
        }

        int[] res = new int[size];
        System.arraycopy(buf, 0, res, 0, size);
        return res;
    }
}
