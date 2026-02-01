package LeetCodeJava.String;

// https://leetcode.com/problems/valid-palindrome/description/?envType=list&envId=xoqag3yj

/**
 *
 * 125. Valid Palindrome
 * Solved
 * N/A
 * Topics
 * premium lock icon
 * Companies
 * A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward. Alphanumeric characters include letters and numbers.
 *
 * Given a string s, return true if it is a palindrome, or false otherwise.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "A man, a plan, a canal: Panama"
 * Output: true
 * Explanation: "amanaplanacanalpanama" is a palindrome.
 * Example 2:
 *
 * Input: s = "race a car"
 * Output: false
 * Explanation: "raceacar" is not a palindrome.
 * Example 3:
 *
 * Input: s = " "
 * Output: true
 * Explanation: s is an empty string "" after removing non-alphanumeric characters.
 * Since an empty string reads the same forward and backward, it is a palindrome.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 2 * 105
 * s consists only of printable ASCII characters.
 *
 *
 *  A phrase is a palindrome if, after converting all uppercase letters into lowercase
 *  letters and removing all non-alphanumeric characters,
 *  it reads the same forward and backward.
 *  Alphanumeric characters include letters and numbers.
 *
 *
 *  NOTE !!!
 *
 *  "and removing all non-alphanumeric characters, "
 *   -> so we need to consider both alphabet and numeric element
 */
public class ValidPalindrome {

    // V0
    // IDEA: STR OP
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isPalindrome(String s) {

        if (s == null || s.length() == 0){
            return true;
        }

        StringBuilder sb = new StringBuilder();
        for (int idx = 0; idx < s.length(); idx++){
            /**
             *  NOTE !!!
             *
             *   `Character.isLetterOrDigit` can check if
             *    a char is either `alphabet` or `digit`
             */
            if (Character.isLetterOrDigit(s.charAt(idx))){
                sb.append(String.valueOf(s.charAt(idx)).toUpperCase());
            }
        }

        // NOTE : should use equals (so compare value but not memory address)
        /**
         * The issue in your code is with the comparison of strings using ==.
         * In Java, you should use the equals method to compare strings for equality.
         * Here's the corrected code:
         */
        return sb.toString().equals(sb.reverse().toString());
    }

    // VO-1
    // IDEA: Str op + alpha, num check + stringBuilder
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isPalindrome_0_1(String s) {
        // edge
        if (s.isEmpty()) {
            return true;
        }
        if (s.length() == 1) {
            return true;
        }

        StringBuilder sb = new StringBuilder();
        String alpha = "abcdefghijklmonpqrstuvwxyz"; // ??
        String nums = "0123456789";
        for (String x : s.split("")) {
            boolean shouldAdd = alpha.contains(x.toLowerCase()) || nums.contains(x);
            if (shouldAdd) {
                if (alpha.contains(x.toLowerCase())) {
                    sb.append(x.toLowerCase());
                } else {
                    sb.append(x);
                }
            }
        }

        System.out.println(">>> str = " + sb.toString());
        return sb.toString().contentEquals(sb.reverse());
    }

    // V0-2
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isPalindrome_0_2(String s) {

        if (s == null || s.length() == 0){
            return true;
        }

        String sUpper = "";
        for (int idx = 0; idx < s.length(); idx++){
            if (Character.isLetterOrDigit(s.charAt(idx))){
                sUpper += String.valueOf(s.charAt(idx)).toUpperCase();
            }
        }

        //System.out.println("sUpper = " + sUpper);
        //String sUpper = s.toUpperCase();

        // 2 pointer
        int l = 0;
        int r = sUpper.length() - 1;
        while (r > l){
            if (sUpper.charAt(l) != sUpper.charAt(r)){
                return false;
            }
            l += 1;
            r -= 1;
        }
        return true;
    }

    // V1
    // https://leetcode.com/problems/valid-palindrome/solutions/4787889/simple-solution-using-java/?envType=list&envId=xoqag3yj
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isPalindrome_1(String s) {
        String str=s.replaceAll("[^a-zA-Z0-9]","").toLowerCase();
        StringBuilder sb=new StringBuilder(str);
        sb.reverse();
        String ans=sb.toString();
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)!=ans.charAt(i)){
                return false;
            }
        }
        return true;
    }



}
