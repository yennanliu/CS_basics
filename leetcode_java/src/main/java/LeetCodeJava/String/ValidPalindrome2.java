package LeetCodeJava.String;

// https://leetcode.com/problems/valid-palindrome-ii/description/
/**
 * 680. Valid Palindrome II
 * Solved
 * Easy
 * Topics
 * Companies
 * Given a string s, return true if the s can be palindrome after deleting at most one character from it.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "aba"
 * Output: true
 * Example 2:
 *
 * Input: s = "abca"
 * Output: true
 * Explanation: You could delete the character 'c'.
 * Example 3:
 *
 * Input: s = "abc"
 * Output: false
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 105
 * s consists of lowercase English letters.
 *
 */
public class ValidPalindrome2 {

    // V0
//    public boolean validPalindrome(String s) {
//
//    }

    // V0-1
    // IDEA: SLIDE WINDOW (fixed by gpt)
    public boolean validPalindrome_0_1(String s) {
        if (s == null || s.length() <= 1) {
            return true;
        }

        int l = 0;
        int r = s.length() - 1;

        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) {
                // At mismatch -> try skipping either l or r
                return checkIsPalindrome(s, l + 1, r) || checkIsPalindrome(s, l, r - 1);
            }
            l++;
            r--;
        }

        return true;
    }

    private boolean checkIsPalindrome(String s, int l, int r) {
        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }

    // V0-2
    // IDEA: 2 POINTERS (TLE)
    public boolean validPalindrome_0_2(String s) {
        // edge
        if (s == null || s.length() == 0) {
            return true;
        }
        for (int i = 0; i < s.length(); i++) {
            if (isPalindrome(s, i)) {
                return true;
            }
        }

        return false;
    }

    public boolean isPalindrome(String input, int i) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < input.split("").length; j++) {
            if (j != i) {
                sb.append(input.charAt(j));
            }
        }
        String x = sb.toString();
        //System.out.println(">>> i = " + i + ", input = " + input + ", x = " + x);

        int l = 0;
        int r = x.length() - 1;
        boolean res = true;
        while (r > l) {
            if (x.charAt(l) != x.charAt(r)) {
                return false;
            }
            r -= 1;
            l += 1;
        }

        return res;
    }

    // V1
    // IDEA: 2 POINTERS
    // https://leetcode.com/problems/valid-palindrome-ii/solutions/6445414/beats-super-easy-beginners-java-c-c-pyth-2cal/
    public boolean validPalindrome_1(String s) {
        int left = 0, right = s.length() - 1;

        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return isPalindrome_1(s, left + 1, right) || isPalindrome_1(s, left, right - 1);
            }
            left++;
            right--;
        }
        return true;
    }

    private boolean isPalindrome_1(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right))
                return false;
            left++;
            right--;
        }
        return true;
    }

    // V2-1
    // IDEA: 2 POINTERS
    // https://leetcode.com/problems/valid-palindrome-ii/solutions/6049559/video-solution-short-and-simple-by-piotr-axj4/
    public boolean validPalindrome_2(String s) {
        int left = 0;
        int right = s.length() - 1;

        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return isPalindrome_2(s, left + 1, right) ||
                        isPalindrome_2(s, left, right - 1);
            }
            left++;
            right--;
        }
        return true;
    }

    private boolean isPalindrome_2(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    // V2-2
    // IDEA: HALF STRING
    // https://leetcode.com/problems/valid-palindrome-ii/solutions/6049559/video-solution-short-and-simple-by-piotr-axj4/

   // V3
   public boolean validPalindrome_3(String s) {
       int i = 0;
       int j = s.length() - 1;

       while (i <= j) {
           if (s.charAt(i) == s.charAt(j)) {
               i++;
               j--;
           } else
               return isPalindrome_3(s, i + 1, j) || isPalindrome_3(s, i, j - 1);
       }
       return true;
   }

    public boolean isPalindrome_3(String s, int i, int j) {
        while (i <= j) {
            if (s.charAt(i) == s.charAt(j)) {
                i++;
                j--;
            } else
                return false;
        }
        return true;
    }

}
