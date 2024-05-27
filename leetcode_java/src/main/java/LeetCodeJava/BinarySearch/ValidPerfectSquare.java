package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/valid-perfect-square/

public class ValidPerfectSquare {

    // V0
    // IDEA : BINARY SEARCH
    public boolean isPerfectSquare(int num) {

        if (num < 2) {
            return true;
        }

        long left = 2;
        long right = num / 2; // NOTE !!!, "long right = num;" is OK as well
        long x;
        long guessSquared;

        while (left <= right) {
            x = (left + right) / 2;
            guessSquared = x * x;
            if (guessSquared == num) {
                return true;
            }
            if (guessSquared > num) {
                right = x - 1;
            } else {
                left = x + 1;
            }
        }
        return false;
    }

    // V1
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/valid-perfect-square/editorial/
    public boolean isPerfectSquare_1(int num) {
        if (num < 2) {
            return true;
        }

        long left = 2, right = num / 2, x, guessSquared;
        while (left <= right) {
            x = left + (right - left) / 2;
            guessSquared = x * x;
            if (guessSquared == num) {
                return true;
            }
            if (guessSquared > num) {
                right = x - 1;
            } else {
                left = x + 1;
            }
        }
        return false;
    }


}
