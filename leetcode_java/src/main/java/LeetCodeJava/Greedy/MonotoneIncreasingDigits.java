package LeetCodeJava.Greedy;

// https://leetcode.com/problems/monotone-increasing-digits/description/
/**
 *  738. Monotone Increasing Digits
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * An integer has monotone increasing digits if and only if each pair of adjacent digits x and y satisfy x <= y.
 *
 * Given an integer n, return the largest number that is less than or equal to n with monotone increasing digits.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 10
 * Output: 9
 * Example 2:
 *
 * Input: n = 1234
 * Output: 1234
 * Example 3:
 *
 * Input: n = 332
 * Output: 299
 *
 *
 * Constraints:
 *
 * 0 <= n <= 109
 *
 *
 */
public class MonotoneIncreasingDigits {

    // V0
    // IDEA: BRUTE FORCE (fixed by gemini)
    public int monotoneIncreasingDigits(int n) {
        char[] digits = String.valueOf(n).toCharArray();

        // 1. Find the first cliff (where digits start decreasing)
        // We want to mark the start of the "9s"
        int marker = digits.length;

        for (int i = digits.length - 1; i > 0; i--) {
            // If current digit is smaller than the previous one
            if (digits[i - 1] > digits[i]) {
                // Decrease the previous digit
                digits[i - 1]--;
                // Mark that every digit from index i onwards should become '9'
                marker = i;
            }
        }

        // 2. Set all digits after the modified position to 9
        for (int i = marker; i < digits.length; i++) {
            digits[i] = '9';
        }

        return Integer.parseInt(new String(digits));
    }

    // V0-1
    // IDEA: BRUTE FORCE (fixed by GPT)
    public int monotoneIncreasingDigits_0_1(int n) {

        char[] arr = String.valueOf(n).toCharArray();

        int mark = arr.length;

        // Traverse from right to left
        for (int i = arr.length - 1; i > 0; i--) {
            if (arr[i] < arr[i - 1]) {
                arr[i - 1]--; // decrease previous digit
                mark = i; // mark position
            }
        }

        // Fill everything after mark with 9
        for (int i = mark; i < arr.length; i++) {
            arr[i] = '9';
        }

        return Integer.parseInt(new String(arr));
    }



    // V1-1
    // IDEA: GREEDY
    // https://leetcode.com/problems/monotone-increasing-digits/editorial/
    public int monotoneIncreasingDigits_1_1(int N) {
        String S = String.valueOf(N);
        String ans = "";
        search: for (int i = 0; i < S.length(); ++i) {
            for (char d = '1'; d <= '9'; ++d) {
                if (S.compareTo(ans + repeat(d, S.length() - i)) < 0) {
                    ans += (char) (d - 1);
                    continue search;
                }
            }
            ans += '9';
        }
        return Integer.parseInt(ans);
    }

    public String repeat(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; ++i)
            sb.append(c);
        return sb.toString();
    }



    // V1-2
    // IDEA: Truncate After Cliff
    // https://leetcode.com/problems/monotone-increasing-digits/editorial/
    public int monotoneIncreasingDigits_1_2(int N) {
        char[] S = String.valueOf(N).toCharArray();
        int i = 1;
        while (i < S.length && S[i - 1] <= S[i])
            i++;
        while (0 < i && i < S.length && S[i - 1] > S[i])
            S[--i]--;
        for (int j = i + 1; j < S.length; ++j)
            S[j] = '9';

        return Integer.parseInt(String.valueOf(S));
    }


    // V2
    // https://leetcode.com/problems/monotone-increasing-digits/solutions/3056255/javac-0ms-awesome-approach-100-faster-by-yak8/
    public int monotoneIncreasingDigits_2(int n) {
        int position;
        int digitInTheNextPosition;
        while ((position = getThePositionNotSatisfied(n)) != -1) {
            digitInTheNextPosition = ((int) (n / Math.pow(10, position - 1))) % 10;
            n -= Math.pow(10, position - 1) * (digitInTheNextPosition + 1);
            n -= n % Math.pow(10, position);
            n += Math.pow(10, position) - 1;
        }
        return n;
    }

    public int getThePositionNotSatisfied(int n) {
        int k = 10;
        int position = 0;
        while (n > 0) {
            if (k < n % 10) {
                return position;
            } else {
                k = n % 10;
                n /= 10;
                position++;
            }
        }
        return -1;
    }


    // V3
    public int monotoneIncreasingDigits_3(int n) {
        char[] arr = String.valueOf(n).toCharArray();
        int start = arr.length;
        for (int i = arr.length - 2; i >= 0; i--) {
            if (arr[i] > arr[i + 1]) {
                start = i + 1;
                arr[i]--;
            }
        }

        for (int i = start; i < arr.length; i++) {
            arr[i] = '9';
        }

        return Integer.parseInt(String.valueOf(arr));
    }





}
