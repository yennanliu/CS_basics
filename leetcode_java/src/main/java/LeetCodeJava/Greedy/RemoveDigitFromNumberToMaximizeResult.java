package LeetCodeJava.Greedy;

// https://leetcode.com/problems/remove-digit-from-number-to-maximize-result/description/

/**
 * 2259. Remove Digit From Number to Maximize Result
 * Easy
 *
 * You are given a string number representing a positive integer and a character digit.
 *
 * Return the resulting string after removing exactly one occurrence of digit from number such that the
 * value of the resulting string in decimal form is maximized. The test cases are generated such that digit
 * occurs at least once in number.
 *
 *
 * Example 1:
 *
 * Input: number = "123", digit = "3"
 * Output: "12"
 *
 * Example 2:
 *
 * Input: number = "1231", digit = "1"
 * Output: "231"
 *
 * Example 3:
 *
 * Input: number = "551", digit = "5"
 * Output: "51"
 *
 *
 * Constraints:
 *
 * 2 <= number.length <= 100
 * number consists of digits from '1' to '9'.
 * digit is a digit from '1' to '9'.
 * digit occurs at least once in number.
 *
 */
public class RemoveDigitFromNumberToMaximizeResult {

    // V0
    // IDEA: GREEDY - REMOVE THE FIRST OCCURRENCE FOLLOWED BY A BIGGER DIGIT, ELSE THE LAST OCCURRENCE
    /**
     * time = O(N)
     * space = O(N)
     */
    public String removeDigit(String number, char digit) {
        int n = number.length();
        int removeAt = -1;

        // every candidate has length n - 1, so the bigger result is the lexicographically bigger string
        for (int i = 0; i < n; i++) {
            if (number.charAt(i) != digit) {
                continue;
            }
            removeAt = i;
            // deleting here pulls a bigger digit into position i: nothing later can beat that
            if (i + 1 < n && number.charAt(i + 1) > digit) {
                break;
            }
        }
        // otherwise removeAt is the last occurrence, which disturbs the smallest suffix
        return number.substring(0, removeAt) + number.substring(removeAt + 1);
    }
}
