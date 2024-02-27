package LeetCodeJava.String;

// https://leetcode.com/problems/longest-palindromic-substring/

import java.util.ArrayList;

public class LongestPalindromicSubstring {

    // V0
    // IDEA : SLIDING WINDOW : for - while
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/String/longest-palindromic-substring.py
    public String longestPalindrome(String s) {

        if (s == null || s.length() == 0) {
            return s;
        }

        ArrayList<String> tmp = new ArrayList<String>();
        String res = "";

        /** NOTE !!! for - while (sliding window) */
        for (int i = 0; i < s.length(); i++) {

            // case 1 : length is odd
//            int l = i - 1;
//            int r = i + 1;
            // NOTE !!! we set l = i, r = i when "length is odd"
            int l = i;
            int r = i;
            while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {

                // NOTE !!! we get res first, then do pointers (l, r) update
                if (r - l + 1 > res.length()) {
                    res = s.substring(l, r+1); // NOTE !!! substring with idx l, r+1
                }

                l -= 1;
                r += 1;
            }

            // case 2 : length is even
            // NOTE !!! we set l = i -1, r = i when "length is odd"
            l = i - 1;
            r = i;
            while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {

                // NOTE !!! we get res first, then do pointers (l, r) update
                if (r - l + 1 > res.length()) {
                    res = s.substring(l, r+1);  // NOTE !!! substring with idx l, r+1
                }

                l -= 1;
                r += 1;
            }
        }

        return res;
    }

    // V1
    // IDEA : BRUTE FORCE + isPalindrome check
    // https://leetcode.com/problems/longest-palindromic-substring/solutions/4212564/beats-96-49-5-different-approaches-brute-force-eac-dp-ma-recursion/
    public String longestPalindrome_2(String s) {
        if (s.length() <= 1) {
            return s;
        }

        int maxLen = 1;
        String maxStr = s.substring(0, 1);

        for (int i = 0; i < s.length(); i++) {
            for (int j = i + maxLen; j <= s.length(); j++) {
                if (j - i > maxLen && isPalindrome(s.substring(i, j))) {
                    maxLen = j - i;
                    maxStr = s.substring(i, j);
                }
            }
        }

        return maxStr;
    }

    private boolean isPalindrome(String str) {
        int left = 0;
        int right = str.length() - 1;

        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }

    // V2
    // IDEA : SLIDING WINDOW
    // https://leetcode.com/problems/longest-palindromic-substring/solutions/3401644/detailed-recursive-explaination-with-pictures-in-c-java-python-dp-two-pointers/
    int maxLen = 0;
    int lo = 0;
    public String longestPalindrome_3(String s) {
        char[] input = s.toCharArray();
        if(s.length() < 2) {
            return s;
        }

        for(int i = 0; i<input.length; i++) {
            expandPalindrome(input, i, i);
            expandPalindrome(input, i, i+1);
        }
        return s.substring(lo, lo+maxLen);
    }

    public void expandPalindrome(char[] s, int j, int k) {
        while(j >= 0 && k < s.length && s[j] == s[k]) {
            j--;
            k++;
        }
        if(maxLen < k - j - 1) {
            maxLen = k - j - 1;
            lo = j+1;
        }
    }

}
