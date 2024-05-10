package LeetCodeJava.String;

// https://leetcode.com/problems/valid-palindrome/description/?envType=list&envId=xoqag3yj

/**
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
    public boolean isPalindrome(String s) {

        if (s == null || s.length() == 0){
            return true;
        }

        StringBuilder sb = new StringBuilder();
        for (int idx = 0; idx < s.length(); idx++){
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

    // V0'
    public boolean isPalindrome_0(String s) {

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
