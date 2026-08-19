package LeetCodeJava.String;

// https://leetcode.com/problems/masking-personal-information/

/**
 *  831. Masking Personal Information
 *  Medium
 *
 *  You are given a personal information string s, representing either an
 *  email address or a phone number. Return the masked personal information
 *  using the below rules.
 *
 *  Email address:
 *   - An email is "name1@name2.name3" where both names contain only letters.
 *   - Lowercase all letters, then replace all letters of name1 between the
 *     first and last letter with 5 asterisks "*****".
 *
 *  Phone number:
 *   - A phone number contains 10-13 digits, and may contain the separation
 *     characters '+', '-', '(', ')', ' '.
 *   - The last 10 digits are the local number, the rest (0-3 digits) are the
 *     country code.
 *   - Mask as "***-***-XXXX" when there is no country code, otherwise
 *     "+**...**-***-***-XXXX" with one '*' per country-code digit.
 *
 *  Example 1:
 *  Input: s = "LeetCode@LeetCode.com"
 *  Output: "l*****e@leetcode.com"
 *
 *  Example 2:
 *  Input: s = "1(234)567-890"
 *  Output: "***-***-7890"
 *
 *  Constraints:
 *   - s is either a valid email or a phone number.
 */
public class MaskingPersonalInformation {

    // V0
    // IDEA: STRING SIMULATION - branch on '@' (email) vs digits (phone).
    /**
     * time = O(n)
     * space = O(n)
     */
    public String maskPII(String s) {
        if (s.indexOf('@') >= 0) {
            return maskEmail(s);
        }
        return maskPhone(s);
    }

    private String maskEmail(String s) {
        String lower = s.toLowerCase();
        int at = lower.indexOf('@');
        String name = lower.substring(0, at);
        String domain = lower.substring(at); // includes '@'
        return name.charAt(0) + "*****" + name.charAt(name.length() - 1) + domain;
    }

    private String maskPhone(String s) {
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                digits.append(c);
            }
        }
        int n = digits.length();
        String local = "***-***-" + digits.substring(n - 4);
        int countryLen = n - 10;
        if (countryLen == 0) {
            return local;
        }
        StringBuilder sb = new StringBuilder("+");
        for (int i = 0; i < countryLen; i++) {
            sb.append('*');
        }
        sb.append('-').append(local);
        return sb.toString();
    }
}
