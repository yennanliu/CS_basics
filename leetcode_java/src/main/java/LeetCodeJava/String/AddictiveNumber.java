package LeetCodeJava.String;

// https://leetcode.com/problems/additive-number/

/**
 *  306. Additive Number
 *  Medium
 *
 *  An additive number is a string whose digits can form an additive sequence.
 *
 *  A valid additive sequence should contain at least three numbers. Except for
 *  the first two numbers, each subsequent number in the sequence must be the
 *  sum of the preceding two.
 *
 *  Given a string containing only digits, return true if it is an additive
 *  number, or false otherwise.
 *
 *  Note: numbers in the additive sequence cannot have leading zeros, so the
 *  sequences 1, 2, 03 and 1, 02, 3 are invalid.
 *
 *  Example 1:
 *    Input: num = "112358"     Output: true   (1, 1, 2, 3, 5, 8)
 *  Example 2:
 *    Input: num = "199100199"  Output: true   (1, 99, 100, 199)
 *
 *  Constraints:
 *    1 <= num.length <= 35
 *    num consists only of digits.
 */
public class AddictiveNumber {

    // V0
    // IDEA: brute force the first two numbers, then greedily extend with string addition
    //       (string addition avoids any overflow for the 35-digit limit)
    /**
     * time = O(n^3)
     * space = O(n)
     */
    public boolean isAdditiveNumber(String num) {
        if (num == null || num.length() < 3) {
            return false;
        }

        int n = num.length();
        // i = length of the 1st number, j = length of the 2nd number
        for (int i = 1; i <= n / 2; i++) {
            if (num.charAt(0) == '0' && i > 1) {
                break;
            }
            for (int j = 1; Math.max(i, j) <= n - i - j; j++) {
                if (num.charAt(i) == '0' && j > 1) {
                    break;
                }
                if (check(num.substring(0, i), num.substring(i, i + j), num.substring(i + j))) {
                    return true;
                }
            }
        }
        return false;
    }

    // does `rest` start with (a + b), and does the remainder keep the property ?
    private boolean check(String a, String b, String rest) {
        if (rest.isEmpty()) {
            return true;
        }
        String sum = add(a, b);
        if (!rest.startsWith(sum)) {
            return false;
        }
        return check(b, sum, rest.substring(sum.length()));
    }

    // decimal string addition
    private String add(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int i = a.length() - 1;
        int j = b.length() - 1;
        int carry = 0;
        while (i >= 0 || j >= 0 || carry > 0) {
            int x = (i >= 0) ? a.charAt(i--) - '0' : 0;
            int y = (j >= 0) ? b.charAt(j--) - '0' : 0;
            int s = x + y + carry;
            sb.append((char) ('0' + (s % 10)));
            carry = s / 10;
        }
        return sb.reverse().toString();
    }
}
