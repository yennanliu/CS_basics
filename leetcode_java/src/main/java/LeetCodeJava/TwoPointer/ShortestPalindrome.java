package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/shortest-palindrome/description/
/**
 *  214. Shortest Palindrome
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * You are given a string s. You can convert s to a palindrome by adding characters in front of it.
 *
 * Return the shortest palindrome you can find by performing this transformation.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "aacecaaa"
 * Output: "aaacecaaa"
 * Example 2:
 *
 * Input: s = "abcd"
 * Output: "dcbabcd"
 *
 *
 * Constraints:
 *
 * 0 <= s.length <= 5 * 104
 * s consists of lowercase English letters only.
 */
public class ShortestPalindrome {

    // V0
//    public String shortestPalindrome(String s) {
//
//    }

    // V0-1
    // IDEA: BRUTE FORCE (TLE) (gemini)
    public String shortestPalindrome_0_1(String s) {
        int n = s.length();
        if (n <= 1)
            return s;

        // 1. Find the longest prefix that is a palindrome
        // We start from the end and check [0...i]
        int end = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (isPalindrome(s, 0, i)) {
                end = i;
                break;
            }
        }

        // 2. The part that is NOT part of the prefix palindrome
        // needs to be reversed and added to the front.
        String suffix = s.substring(end + 1);
        StringBuilder sb = new StringBuilder(suffix);
        return sb.reverse().toString() + s;
    }

    private boolean isPalindrome(String x, int l, int r) {
        while (l < r) {
            if (x.charAt(l) != x.charAt(r))
                return false;
            l++;
            r--;
        }
        return true;
    }


    // V1-1
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/shortest-palindrome/editorial/
    public String shortestPalindrome_1_1(String s) {
        int length = s.length();
        String reversedString = new StringBuilder(s).reverse().toString();

        // Iterate through the string to find the longest palindromic prefix
        for (int i = 0; i < length; i++) {
            if (s.substring(0, length - i).equals(reversedString.substring(i))) {
                return new StringBuilder(reversedString.substring(0, i))
                        .append(s)
                        .toString();
            }
        }
        return "";
    }


    // V1-2
    // IDEA: 2 POINTERS
    // https://leetcode.com/problems/shortest-palindrome/editorial/
    public String shortestPalindrome_1_2(String s) {
        int length = s.length();
        if (length == 0) {
            return s;
        }

        // Find the longest palindromic prefix
        int left = 0;
        for (int right = length - 1; right >= 0; right--) {
            if (s.charAt(right) == s.charAt(left)) {
                left++;
            }
        }

        // If the whole string is a palindrome, return the original string
        if (left == length) {
            return s;
        }

        // Extract the suffix that is not part of the palindromic prefix
        String nonPalindromeSuffix = s.substring(left);
        StringBuilder reverseSuffix = new StringBuilder(
                nonPalindromeSuffix
        ).reverse();

        // Form the shortest palindrome by prepending the reversed suffix
        return reverseSuffix
                .append(shortestPalindrome_1_2(s.substring(0, left)))
                .append(nonPalindromeSuffix)
                .toString();
    }


    // V1-3
    // IDEA: KMP (Knuth-Morris-Pratt) ALGO
    // https://leetcode.com/problems/shortest-palindrome/editorial/
    public String shortestPalindrome_1_3(String s) {
        String reversedString = new StringBuilder(s).reverse().toString();
        String combinedString = s + "#" + reversedString;
        int[] prefixTable = buildPrefixTable(combinedString);

        int palindromeLength = prefixTable[combinedString.length() - 1];
        StringBuilder suffix = new StringBuilder(
                s.substring(palindromeLength)).reverse();
        return suffix.append(s).toString();
    }

    private int[] buildPrefixTable(String s) {
        int[] prefixTable = new int[s.length()];
        int length = 0;
        for (int i = 1; i < s.length(); i++) {
            while (length > 0 && s.charAt(i) != s.charAt(length)) {
                length = prefixTable[length - 1];
            }
            if (s.charAt(i) == s.charAt(length)) {
                length++;
            }
            prefixTable[i] = length;
        }
        return prefixTable;
    }


    // V1-4
    // IDEA: Rolling Hash Based Algorithm
    // https://leetcode.com/problems/shortest-palindrome/editorial/
    public String shortestPalindrome_1_4(String s) {
        long hashBase = 29;
        long modValue = (long) 1e9 + 7;
        long forwardHash = 0, reverseHash = 0, powerValue = 1;
        int palindromeEndIndex = -1;

        // Calculate rolling hashes and find the longest palindromic prefix
        for (int i = 0; i < s.length(); i++) {
            char currentChar = s.charAt(i);

            // Update forward hash
            forwardHash = (forwardHash * hashBase + (currentChar - 'a' + 1)) %
                    modValue;

            // Update reverse hash
            reverseHash = (reverseHash + (currentChar - 'a' + 1) * powerValue) %
                    modValue;
            powerValue = (powerValue * hashBase) % modValue;

            // If forward and reverse hashes match, update palindrome end index
            if (forwardHash == reverseHash) {
                palindromeEndIndex = i;
            }
        }

        // Find the remaining suffix after the longest palindromic prefix
        String suffix = s.substring(palindromeEndIndex + 1);
        // Reverse the remaining suffix
        StringBuilder reversedSuffix = new StringBuilder(suffix).reverse();

        // Prepend the reversed suffix to the original string and return the result
        return reversedSuffix.append(s).toString();
    }


    // V1-5
    // IDEA: Manacher's Algorithm
    // https://leetcode.com/problems/shortest-palindrome/editorial/

    public String shortestPalindrome_1_5(String s) {
        // Return early if the string is null or empty
        if (s == null || s.length() == 0) {
            return s;
        }

        // Preprocess the string to handle palindromes uniformly
        String modifiedString = preprocessString(s);
        int[] palindromeRadiusArray = new int[modifiedString.length()];
        int center = 0, rightBoundary = 0;
        int maxPalindromeLength = 0;

        // Iterate through each character in the modified string
        for (int i = 1; i < modifiedString.length() - 1; i++) {
            int mirrorIndex = 2 * center - i;

            // Use previously computed values to avoid redundant calculations
            if (rightBoundary > i) {
                palindromeRadiusArray[i] = Math.min(
                        rightBoundary - i,
                        palindromeRadiusArray[mirrorIndex]);
            }

            // Expand around the current center while characters match
            while (modifiedString.charAt(i + 1 + palindromeRadiusArray[i]) == modifiedString
                    .charAt(i - 1 - palindromeRadiusArray[i])) {
                palindromeRadiusArray[i]++;
            }

            // Update the center and right boundary if the palindrome extends beyond the current boundary
            if (i + palindromeRadiusArray[i] > rightBoundary) {
                center = i;
                rightBoundary = i + palindromeRadiusArray[i];
            }

            // Update the maximum length of palindrome starting at the beginning
            if (i - palindromeRadiusArray[i] == 1) {
                maxPalindromeLength = Math.max(
                        maxPalindromeLength,
                        palindromeRadiusArray[i]);
            }
        }

        // Construct the shortest palindrome by reversing the suffix and prepending it to the original string
        StringBuilder suffix = new StringBuilder(
                s.substring(maxPalindromeLength)).reverse();
        return suffix.append(s).toString();
    }

    private String preprocessString(String s) {
        // Add boundaries and separators to handle palindromes uniformly
        StringBuilder sb = new StringBuilder("^");
        for (char c : s.toCharArray()) {
            sb.append("#").append(c);
        }
        return sb.append("#$").toString();
    }


    // V2

    // V3



}
