package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-the-closest-palindrome/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 564. Find the Closest Palindrome
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a string n representing an integer, return the closest integer (not including itself), which is a palindrome. If there is a tie, return the smaller one.
 *
 * The closest is defined as the absolute difference minimized between two integers.
 *
 *
 *
 * Example 1:
 *
 * Input: n = "123"
 * Output: "121"
 * Example 2:
 *
 * Input: n = "1"
 * Output: "0"
 * Explanation: 0 and 2 are the closest palindromes but we return the smallest which is 0.
 *
 *
 * Constraints:
 *
 * 1 <= n.length <= 18
 * n consists of only digits.
 * n does not have leading zeros.
 * n is representing an integer in the range [1, 1018 - 1].
 *
 */
public class FindTheClosestPalindrome {

    // V0
//    public String nearestPalindromic(String n) {
//
//    }

    // V1-1
    // https://leetcode.com/problems/find-the-closest-palindrome/editorial/
    // IDEA:  Find Previous and Next Palindromes
    /**
     * time = O(log N)
     * space = O(log N)
     */
    public String nearestPalindromic_1_1(String n) {
        int len = n.length();
        int i = len % 2 == 0 ? len / 2 - 1 : len / 2;
        long firstHalf = Long.parseLong(n.substring(0, i + 1));
        /*
        Generate possible palindromic candidates:
        1. Create a palindrome by mirroring the first half.
        2. Create a palindrome by mirroring the first half incremented by 1.
        3. Create a palindrome by mirroring the first half decremented by 1.
        4. Handle edge cases by considering palindromes of the form 999...
           and 100...001 (smallest and largest n-digit palindromes).
        */
        List<Long> possibilities = new ArrayList<>();

        possibilities.add(halfToPalindrome(firstHalf, len % 2 == 0));
        possibilities.add(halfToPalindrome(firstHalf + 1, len % 2 == 0));
        possibilities.add(halfToPalindrome(firstHalf - 1, len % 2 == 0));
        possibilities.add((long) Math.pow(10, len - 1) - 1);
        possibilities.add((long) Math.pow(10, len) + 1);

        // Find the palindrome with minimum difference, and minimum value.
        long diff = Long.MAX_VALUE, res = 0, nl = Long.parseLong(n);
        for (long cand : possibilities) {
            if (cand == nl)
                continue;
            if (Math.abs(cand - nl) < diff) {
                diff = Math.abs(cand - nl);
                res = cand;
            } else if (Math.abs(cand - nl) == diff) {
                res = Math.min(res, cand);
            }
        }

        return String.valueOf(res);
    }

    /**
     * time = O(log N)
     * space = O(1)
     */
    private long halfToPalindrome(long left, boolean even) {
        // Convert the given half to palindrome.
        long res = left;
        if (!even)
            left = left / 10;
        while (left > 0) {
            res = res * 10 + (left % 10);
            left /= 10;
        }
        return res;
    }


    // V1-2
    // https://leetcode.com/problems/find-the-closest-palindrome/editorial/
    // IDEA: Binary Search
    // Convert to palindrome keeping first half constant.
    /**
     * time = O(log N)
     * space = O(log N)
     */
    private long convert(long num) {
        String s = Long.toString(num);
        int n = s.length();
        int l = (n - 1) / 2, r = n / 2;
        char[] sArray = s.toCharArray();
        while (l >= 0) {
            sArray[r++] = sArray[l--];
        }
        return Long.parseLong(new String(sArray));
    }

    // Find the previous palindrome, just smaller than n.
    /**
     * time = O(log N * log N)
     * space = O(1)
     */
    private long previousPalindrome(long num) {
        long left = 0, right = num;
        long ans = Long.MIN_VALUE;
        while (left <= right) {
            long mid = (right - left) / 2 + left;
            long palin = convert(mid);
            if (palin < num) {
                ans = palin;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return ans;
    }

    // Find the next palindrome, just greater than n.
    /**
     * time = O(log N * log N)
     * space = O(1)
     */
    private long nextPalindrome(long num) {
        long left = num, right = (long) 1e18;
        long ans = Long.MIN_VALUE;
        while (left <= right) {
            long mid = (right - left) / 2 + left;
            long palin = convert(mid);
            if (palin > num) {
                ans = palin;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }

    /**
     * time = O(log N * log N)
     * space = O(1)
     */
    public String nearestPalindromic_1_2(String n) {
        long num = Long.parseLong(n);
        long a = previousPalindrome(num);
        long b = nextPalindrome(num);
        if (Math.abs(a - num) <= Math.abs(b - num)) {
            return Long.toString(a);
        }
        return Long.toString(b);
    }


    // V2

}
