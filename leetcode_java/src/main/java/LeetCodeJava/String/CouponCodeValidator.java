package LeetCodeJava.String;

// https://leetcode.com/problems/coupon-code-validator/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 3606. Coupon Code Validator
 * Easy
 *
 * You are given three arrays of length n that describe the properties of n
 * coupons: code, businessLine, and isActive. The ith coupon has:
 *
 * code[i]: a string representing the coupon identifier.
 * businessLine[i]: a string denoting the business category of the coupon.
 * isActive[i]: a boolean indicating whether the coupon is currently active.
 *
 * A coupon is considered valid if all of the following conditions hold:
 *
 * 1. code[i] is non-empty and consists only of alphanumeric characters
 *    (a-z, A-Z, 0-9) and underscores (_).
 * 2. businessLine[i] is one of the following four categories: "electronics",
 *    "grocery", "pharmacy", "restaurant".
 * 3. isActive[i] is true.
 *
 * Return an array of the codes of all valid coupons, sorted first by their
 * businessLine in the order: "electronics", "grocery", "pharmacy",
 * "restaurant", and then by code in lexicographical (ascending) order within
 * each category.
 *
 *
 * Example 1:
 *
 * Input: code = ["SAVE20","","PHARMA5","SAVE@20"],
 *        businessLine = ["restaurant","grocery","pharmacy","restaurant"],
 *        isActive = [true,true,true,true]
 * Output: ["PHARMA5","SAVE20"]
 * Explanation:
 * First coupon is valid.
 * Second coupon has empty code (invalid).
 * Third coupon is valid.
 * Fourth coupon has special character @ (invalid).
 *
 * Example 2:
 *
 * Input: code = ["GROCERY15","ELECTRONICS_50","DISCOUNT10"],
 *        businessLine = ["grocery","electronics","invalid"],
 *        isActive = [false,true,true]
 * Output: ["ELECTRONICS_50"]
 * Explanation:
 * First coupon is inactive (invalid).
 * Second coupon is valid.
 * Third coupon has invalid business line (invalid).
 *
 *
 * Constraints:
 *
 * n == code.length == businessLine.length == isActive.length
 * 1 <= n <= 100
 * 0 <= code[i].length, businessLine[i].length <= 100
 * code[i] and businessLine[i] consist of printable ASCII characters.
 * isActive[i] is either true or false.
 *
 */
public class CouponCodeValidator {

    private static final Set<String> VALID_LINES =
            new HashSet<>(Arrays.asList("electronics", "grocery", "pharmacy", "restaurant"));

    // V0
    // IDEA: FILTER THE VALID COUPONS, THEN SORT BY (businessLine, code)
    //   the required category order (electronics, grocery, pharmacy, restaurant) is already
    //   alphabetical, so plain string comparison on the business line gives it.
    /**
     * time = O(N * L + N log N)
     * space = O(N)
     */
    public List<String> validateCoupons(String[] code, String[] businessLine, boolean[] isActive) {
        // each kept coupon as {businessLine, code}
        List<String[]> valid = new ArrayList<>();
        for (int i = 0; i < code.length; i++) {
            if (isActive[i] && VALID_LINES.contains(businessLine[i]) && isValidCode(code[i])) {
                valid.add(new String[] {businessLine[i], code[i]});
            }
        }

        valid.sort((a, b) -> {
            int byLine = a[0].compareTo(b[0]);
            if (byLine != 0) {
                return byLine;
            }
            return a[1].compareTo(b[1]);
        });

        List<String> result = new ArrayList<>();
        for (String[] coupon : valid) {
            result.add(coupon[1]);
        }
        return result;
    }

    /** Non-empty, and only ASCII letters, digits or '_'. */
    private boolean isValidCode(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean ok = (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_';
            if (!ok) {
                return false;
            }
        }
        return true;
    }
}
