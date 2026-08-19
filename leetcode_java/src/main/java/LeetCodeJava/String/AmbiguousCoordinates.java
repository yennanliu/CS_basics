package LeetCodeJava.String;

// https://leetcode.com/problems/ambiguous-coordinates/

import java.util.ArrayList;
import java.util.List;

/**
 *  816. Ambiguous Coordinates
 *  Medium
 *
 *  We had some 2-dimensional coordinates, like "(1, 3)" or "(2, 0.5)". Then we
 *  removed all commas, decimal points, and spaces and ended up with the string s.
 *  For example, "(1, 3)" becomes s = "(13)" and "(2, 0.5)" becomes s = "(205)".
 *  Return a list of strings representing all possibilities for what our original
 *  coordinates could have been.
 *  The original representation never had extraneous zeroes ("00", "0.0", "1.0",
 *  "001", "00.01" are all invalid), and a decimal point never occurs without at
 *  least one digit before it (".1" is invalid).
 *
 *  Example 1:
 *    Input:  s = "(123)"
 *    Output: ["(1, 2.3)","(1, 23)","(1.2, 3)","(12, 3)"]
 *
 *  Example 2:
 *    Input:  s = "(00011)"
 *    Output: ["(0, 0.011)","(0.001, 1)"]
 *
 *  Constraints:
 *    4 <= s.length <= 12
 *    s[0] == '(' and s[s.length - 1] == ')'
 *    The rest of s are digits.
 */
public class AmbiguousCoordinates {

    // V0
    // IDEA: split the digits at every position, enumerate the legal number
    //       formats of each half, then cross-multiply the two candidate lists
    /**
     * time = O(n^3)
     * space = O(n^2)
     */
    public List<String> ambiguousCoordinates(String s) {
        List<String> res = new ArrayList<>();
        String digits = s.substring(1, s.length() - 1);

        for (int i = 1; i < digits.length(); i++) {
            List<String> lefts = getValidFormats(digits.substring(0, i));
            if (lefts.isEmpty()) {
                continue;
            }
            List<String> rights = getValidFormats(digits.substring(i));
            for (String l : lefts) {
                for (String r : rights) {
                    res.add("(" + l + ", " + r + ")");
                }
            }
        }
        return res;
    }

    /** all legal integer / decimal renderings of a digit run */
    private List<String> getValidFormats(String sub) {
        List<String> ans = new ArrayList<>();
        int n = sub.length();

        // whole integer: no leading zero unless the number is exactly "0"
        if (n == 1 || sub.charAt(0) != '0') {
            ans.add(sub);
        }

        // decimal: integer part has no leading zero (unless "0"),
        //          fractional part has no trailing zero
        for (int i = 1; i < n; i++) {
            String intPart = sub.substring(0, i);
            String fracPart = sub.substring(i);
            if (intPart.length() > 1 && intPart.charAt(0) == '0') {
                continue;
            }
            if (fracPart.charAt(fracPart.length() - 1) == '0') {
                continue;
            }
            ans.add(intPart + "." + fracPart);
        }
        return ans;
    }
}
