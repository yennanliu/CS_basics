package LeetCodeJava.Math;

// https://leetcode.com/problems/largest-multiple-of-three/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 1363. Largest Multiple of Three
 * Hard
 *
 * Given an array of digits digits, return the largest multiple of three that can be formed by concatenating some of the given digits in any order. If there is no answer return an empty string.
 *
 * Since the answer may not fit in an integer data type, return the answer as a string. Note that the returning answer must not contain unnecessary leading zeros.
 *
 *
 * Example 1:
 *
 * Input: digits = [8,1,9]
 * Output: "981"
 *
 * Example 2:
 *
 * Input: digits = [8,6,7,1,0]
 * Output: "8760"
 *
 * Example 3:
 *
 * Input: digits = [1]
 * Output: ""
 *
 *
 * Constraints:
 *
 * 1 <= digits.length <= 10^4
 * 0 <= digits[i] <= 9
 *
 */
public class LargestMultipleOfThree {

    private static final int UNREACHABLE = Integer.MIN_VALUE / 2;

    // V0
    // IDEA: DP (max digit count per remainder) + BACKTRACK THE CHOICES
    //   a number is a multiple of 3 iff its digit sum is, and digit order is free,
    //   so pick a multiset with sum % 3 == 0 and print it in descending order.
    //   maximize the COUNT first, then prefer larger digits.
    //   f[i][j] = max #digits picked from the first i (ascending) digits with sum % 3 == j
    //   f[i][j] = max(f[i-1][j], f[i-1][(j - x % 3 + 3) % 3] + 1),  f[0][0] = 0
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public String largestMultipleOfThree(int[] digits) {
        int[] sorted = digits.clone();
        Arrays.sort(sorted);
        int n = sorted.length;

        int[][] best = new int[n + 1][3];
        for (int[] row : best) {
            Arrays.fill(row, UNREACHABLE);
        }
        best[0][0] = 0;

        for (int i = 1; i <= n; i++) {
            int digitMod = sorted[i - 1] % 3;
            for (int j = 0; j < 3; j++) {
                int skip = best[i - 1][j];
                // taking this digit: the previous remainder must have been j - digitMod
                int take = best[i - 1][(j - digitMod + 3) % 3] + 1;
                best[i][j] = Math.max(skip, take);
            }
        }

        if (best[n][0] <= 0) {
            return "";
        }

        // walk from the largest digit down; take it whenever it lies on an optimal path,
        // which greedily puts the biggest affordable digit in the most significant place
        List<Integer> picked = new ArrayList<>();
        int remainder = 0;
        for (int i = n; i >= 1; i--) {
            int previous = (remainder - sorted[i - 1] % 3 + 3) % 3;
            if (best[i - 1][previous] + 1 == best[i][remainder]) {
                picked.add(sorted[i - 1]);
                remainder = previous;
            }
        }

        // digits are descending, so a leading 0 means every digit is 0
        if (picked.get(0) == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        for (int digit : picked) {
            sb.append(digit);
        }
        return sb.toString();
    }

    // V1
    // IDEA: GREEDY COUNTING (drop the fewest, smallest digits)
    //   r = digit sum % 3.
    //   r == 1 -> drop one smallest digit with d % 3 == 1, else two smallest with d % 3 == 2
    //   r == 2 -> drop one smallest digit with d % 3 == 2, else two smallest with d % 3 == 1
    /**
     * time = O(N)
     * space = O(1)
     */
    public String largestMultipleOfThree_1(int[] digits) {
        // count[d] = how many times digit d appears (counting sort, 10 buckets)
        int[] count = new int[10];
        int sum = 0;
        for (int digit : digits) {
            count[digit]++;
            sum += digit;
        }

        int remainder = sum % 3;
        if (remainder != 0) {
            // first try to remove a single digit with the same remainder
            if (!removeSmallest(count, remainder, 1)) {
                // otherwise two digits with the other non-zero remainder (1 + 1 = 2, 2 + 2 = 4 = 1 mod 3)
                if (!removeSmallest(count, 3 - remainder, 2)) {
                    return "";
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int digit = 9; digit >= 0; digit--) {
            for (int k = 0; k < count[digit]; k++) {
                sb.append(digit);
            }
        }
        if (sb.length() == 0) {
            return "";
        }
        // only zeros left -> the number is just "0"
        if (sb.charAt(0) == '0') {
            return "0";
        }
        return sb.toString();
    }

    /** Removes `how many` smallest digits whose value % 3 == mod; returns false (and changes nothing) if too few. */
    private boolean removeSmallest(int[] count, int mod, int howMany) {
        int available = 0;
        for (int digit = mod; digit <= 9; digit += 3) {
            available += count[digit];
        }
        if (available < howMany) {
            return false;
        }
        int left = howMany;
        for (int digit = mod; digit <= 9 && left > 0; digit += 3) {
            int removed = Math.min(left, count[digit]);
            count[digit] -= removed;
            left -= removed;
        }
        return true;
    }
}
