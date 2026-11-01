package LeetCodeJava.Math;

// https://leetcode.com/problems/find-palindrome-with-fixed-length/description/

/**
 * 2217. Find Palindrome With Fixed Length
 * Medium
 *
 * Given an integer array queries and a positive integer intLength, return an array answer where answer[i] is either the queries[i]th smallest positive palindrome of length intLength or -1 if no such palindrome exists.
 *
 * A palindrome is a number that reads the same backwards and forwards. Palindromes cannot have leading zeros.
 *
 *
 * Example 1:
 *
 * Input: queries = [1,2,3,4,5,90], intLength = 3
 * Output: [101,111,121,131,141,999]
 * Explanation:
 * The first few palindromes of length 3 are:
 * 101, 111, 121, 131, 141, 151, 161, 171, 181, 191, 202, ...
 * The 90th palindrome of length 3 is 999.
 *
 * Example 2:
 *
 * Input: queries = [2,4,6], intLength = 4
 * Output: [1111,1331,1551]
 * Explanation:
 * The first six palindromes of length 4 are:
 * 1001, 1111, 1221, 1331, 1441, and 1551.
 *
 *
 * Constraints:
 *
 * 1 <= queries.length <= 5 * 10^4
 * 1 <= queries[i] <= 10^9
 * 1 <= intLength <= 15
 *
 */
public class FindPalindromeWithFixedLength {

    // V0
    // IDEA: MATH / CONSTRUCTION - A PALINDROME IS FULLY DECIDED BY ITS LEFT HALF
    //   the k-th palindrome of a given length has left half 10^(h-1) + k - 1,
    //   where h = (intLength + 1) / 2 is the length of that half.
    /**
     * time = O(q * L)
     * space = O(q)
     */
    public long[] kthPalindrome(int[] queries, int intLength) {
        int halfLength = (intLength + 1) / 2;

        // smallest left half with no leading zero: 10^(halfLength - 1)
        long smallestHalf = 1;
        for (int i = 1; i < halfLength; i++) {
            smallestHalf *= 10;
        }
        // largest left half: 10^halfLength - 1
        long largestHalf = smallestHalf * 10 - 1;

        long[] answer = new long[queries.length];
        for (int i = 0; i < queries.length; i++) {
            long half = smallestHalf + queries[i] - 1;
            if (half > largestHalf) {
                // fewer than queries[i] palindromes of this length exist
                answer[i] = -1;
                continue;
            }

            String left = String.valueOf(half);
            String right = new StringBuilder(left).reverse().toString();
            // odd length: the middle digit belongs to both halves, so do not repeat it
            if (intLength % 2 == 1) {
                right = right.substring(1);
            }
            answer[i] = Long.parseLong(left + right);
        }
        return answer;
    }
}
