package LeetCodeJava.Math;

// https://leetcode.com/problems/greatest-common-divisor-of-strings/description/
/**
 * 1071. Greatest Common Divisor of Strings
 * Easy
 * Topics
 * Companies
 * Hint
 * For two strings s and t, we say "t divides s" if and only if s = t + t + t + ... + t + t (i.e., t is concatenated with itself one or more times).
 *
 * Given two strings str1 and str2, return the largest string x such that x divides both str1 and str2.
 *
 *
 *
 * Example 1:
 *
 * Input: str1 = "ABCABC", str2 = "ABC"
 * Output: "ABC"
 * Example 2:
 *
 * Input: str1 = "ABABAB", str2 = "ABAB"
 * Output: "AB"
 * Example 3:
 *
 * Input: str1 = "LEET", str2 = "CODE"
 * Output: ""
 *
 *
 * Constraints:
 *
 * 1 <= str1.length, str2.length <= 1000
 * str1 and str2 consist of English uppercase letters.
 *
 *
 */
public class GreatestCommonDivisorOfStrings {

    // V0
//    public String gcdOfStrings(String str1, String str2) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=i5I_wrbUdzM&pp=0gcJCdgAo7VqN5tD
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F1071-greatest-common-divisor-of-strings.java
    // Helper function to calculate gcd using Euclidean algorithm
    public int gcd_1_1(int a, int b){
        if(b == 0){
            return a;
        }
        return gcd_1_1(b, a%b);
    }

    /*
    Approach: If the string have a GCD, then str1 will be of the form m*GCD and str2 will be of the form n*GCD.
    So, str1 + str2 = m*GCD + n*GCD = (m+n)*GCD, hence str1 + str2 should be equal to str2 + str1.
    If the above condition is satisfied, then we can find the GCD of the lengths of the strings and return the substring of str1 of length GCD.
    Else, return empty string.
    */

    public String gcdOfStrings_1_1(String str1, String str2) {
        if((str1 + str2).equals(str2 + str1)){
            int len1 = str1.length();
            int len2 = str2.length();
            int gcd = gcd_1_1(len1, len2);
            return str1.substring(0, gcd);
        }
        return  "";
    }


    // V2-1
    // https://leetcode.com/problems/greatest-common-divisor-of-strings/editorial/
    // IDEA: BRUTE FORCE
    public boolean valid(String str1, String str2, int k) {
        int len1 = str1.length(), len2 = str2.length();
        if (len1 % k > 0 || len2 % k > 0) {
            return false;
        } else {
            String base = str1.substring(0, k);
            return str1.replace(base, "").isEmpty() && str2.replace(base, "").isEmpty();
        }
    }


    public String gcdOfStrings_2_1(String str1, String str2) {
        int len1 = str1.length(), len2 = str2.length();
        for (int i = Math.min(len1, len2); i >= 1; --i) {
            if (valid(str1, str2, i)) {
                return str1.substring(0, i);
            }
        }
        return "";
    }


    // V2-2
    // https://leetcode.com/problems/greatest-common-divisor-of-strings/editorial/
    // IDEA: Greatest Common Divisor
    public int gcd(int x, int y) {
        if (y == 0) {
            return x;
        } else {
            return gcd(y, x % y);
        }
    }

    public String gcdOfStrings_2_2(String str1, String str2) {
        // Check if they have non-zero GCD string.
        if (!(str1 + str2).equals(str2 + str1)) {
            return "";
        }

        // Get the GCD of the two lengths.
        int gcdLength = gcd(str1.length(), str2.length());
        return str1.substring(0, gcdLength);
    }


}
