package LeetCodeJava.String;

// https://leetcode.com/problems/strong-password-checker/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 420. Strong Password Checker
 * Hard
 *
 * A password is considered strong if the below conditions are all met:
 *
 * - It has at least 6 characters and at most 20 characters.
 * - It contains at least one lowercase letter, at least one uppercase letter, and
 *   at least one digit.
 * - It does not contain three repeating characters in a row (i.e., "Baaabb0" is
 *   weak, but "Baabaa0" is strong).
 *
 * Given a string password, return the minimum number of steps required to make
 * password strong. if password is already strong, return 0.
 *
 * In one step, you can:
 *
 * - Insert one character to password,
 * - Delete one character from password, or
 * - Replace one character of password with another character.
 *
 * Example 1:
 *
 * Input: password = "a"
 * Output: 5
 *
 * Example 2:
 *
 * Input: password = "aA1"
 * Output: 3
 *
 * Example 3:
 *
 * Input: password = "1337C0d3"
 * Output: 0
 *
 * Constraints:
 *
 * 1 <= password.length <= 50
 * password consists of letters, digits, dot '.' or exclamation mark '!'.
 *
 */
public class StrongPasswordChecker {

    // V0
    // IDEA: GREEDY (3 cases on length)
    /**
     *  A run of L equal chars needs L / 3 REPLACEMENTS to be broken up.
     *
     *  case 1) len < 6   : we must INSERT (6 - len) chars; each insertion can also
     *                      supply a missing char type AND break a run
     *                      -> max(6 - len, missingTypes)
     *
     *  case 2) 6 <= len <= 20 : only REPLACEMENTS; each replacement can also supply a
     *                      missing type -> max(sum(L / 3), missingTypes)
     *
     *  case 3) len > 20  : we MUST delete (len - 20) chars. Spend them where they
     *                      save the MOST replacements:
     *                        - run with L % 3 == 0 : 1 deletion saves 1 replacement
     *                        - run with L % 3 == 1 : 2 deletions save 1 replacement
     *                        - any other run       : 3 deletions save 1 replacement
     *                      -> delete + max(remainingReplacements, missingTypes)
     *
     *  NOTE !!! the 3 deletion passes must run in THAT order -- spending a deletion on
     *           a `% 3 == 0` run is strictly the best value, so it goes first.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public int strongPasswordChecker(String password) {
        int n = password.length();

        int hasLower = 0;
        int hasUpper = 0;
        int hasDigit = 0;
        for (int i = 0; i < n; i++) {
            char ch = password.charAt(i);
            if (Character.isLowerCase(ch)) {
                hasLower = 1;
            } else if (Character.isUpperCase(ch)) {
                hasUpper = 1;
            } else if (Character.isDigit(ch)) {
                hasDigit = 1;
            }
        }
        int missing = 3 - (hasLower + hasUpper + hasDigit);

        // lengths of the runs of >= 3 identical chars
        List<Integer> runs = new ArrayList<>();
        int i = 0;
        while (i < n) {
            int j = i;
            while (j < n && password.charAt(j) == password.charAt(i)) {
                j += 1;
            }
            if (j - i >= 3) {
                runs.add(j - i);
            }
            i = j;
        }

        if (n < 6) {
            return Math.max(6 - n, missing);
        }

        if (n <= 20) {
            int replace = 0;
            for (int r : runs) {
                replace += r / 3;
            }
            return Math.max(replace, missing);
        }

        // n > 20
        int delete = n - 20;
        int left = delete;

        // 1 deletion kills one replacement on a run with length % 3 == 0
        for (int idx = 0; idx < runs.size() && left > 0; idx++) {
            if (runs.get(idx) % 3 == 0) {
                runs.set(idx, runs.get(idx) - 1);
                left -= 1;
            }
        }

        // 2 deletions kill one replacement on a run with length % 3 == 1
        for (int idx = 0; idx < runs.size() && left > 1; idx++) {
            if (runs.get(idx) >= 4 && runs.get(idx) % 3 == 1) {
                runs.set(idx, runs.get(idx) - 2);
                left -= 2;
            }
        }

        // 3 deletions kill one replacement on any run that is still >= 3
        for (int idx = 0; idx < runs.size() && left > 0; idx++) {
            if (runs.get(idx) >= 3) {
                int d = Math.min(left, runs.get(idx) - 2);
                runs.set(idx, runs.get(idx) - d);
                left -= d;
            }
        }

        int replace = 0;
        for (int r : runs) {
            replace += r / 3;
        }
        return delete + Math.max(replace, missing);
    }

}
