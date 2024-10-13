package LeetCodeJava.String;

// https://leetcode.com/problems/repeated-string-match/description/

import java.math.BigInteger;

/**
 * 686. Repeated String Match
 * Solved
 * Medium
 * Topics
 * Companies
 * Given two strings a and b, return the minimum number of times you should repeat string a so that string b is a substring of it. If it is impossible for b​​​​​​ to be a substring of a after repeating it, return -1.
 *
 * Notice: string "abc" repeated 0 times is "", repeated 1 time is "abc" and repeated 2 times is "abcabc".
 *
 *
 *
 * Example 1:
 *
 * Input: a = "abcd", b = "cdabcdab"
 * Output: 3
 * Explanation: We return 3 because by repeating a three times "abcdabcdabcd", b is a substring of it.
 * Example 2:
 *
 * Input: a = "a", b = "aa"
 * Output: 2
 *
 *
 * Constraints:
 *
 * 1 <= a.length, b.length <= 104
 * a and b consist of lowercase English letters.
 *
 */
public class RepeatedStringMatch {

    // V0
    // IDEA : BRUTE FORCE + StringBuilder (poor performance, check V0-1 for optimization)
    public int repeatedStringMatch(String a, String b) {

        // edge case
        if (a.contains(b)){
            return 1;
        }

        int cnt = 1;
        StringBuilder sb = new StringBuilder();
        sb.append(a);
        // TODO : check ??
        // 1 <= a.length, b.length <= 10^4
        while (sb.toString().length() <= 10000){
            if (sb.toString().contains(b)){
                return cnt;
            }
            sb.append(a);
            cnt += 1;
        }

        return -1;
    }

    // V0-1
    // IDEA : MATH + StringBuilder (offered by gpt)
    public int repeatedStringMatch_0_1(String a, String b) {

        // Calculate the minimum number of times to repeat 'a'
        /**
         * NOTE !!!
         *
         *   The line int repeatCount = (int) Math.ceil((double) b.length() / a.length());
         *   is used to determine the minimum number of times the string
         *   a needs to be repeated so that it could potentially contain the string b.
         *   Here’s a breakdown of why this calculation is necessary:
         *
         *
         *  Explanation of the Calculation:
         *      1.	Understanding String Lengths:
         * 	        •	The length of a indicates how many characters are in the string a.
         * 	        •	The length of b indicates how many characters are in the string b.
         *
         *      2.	Division to Find Minimum Repeats:
         * 	        •	By dividing b.length() by a.length(), we get the number of full a strings needed to cover the length of b.
         * 	        •	This division gives a floating-point result, which might not be a whole number if b is not an exact multiple of a.
         *
         *
         * 	    3.	Using Math.ceil:
         * 	        •	The Math.ceil function is used to round up to the nearest whole number. This is important because even if b needs only a fraction of another a to be fully included, we still need to count that additional occurrence.
         * 	        •	For example, if b is 7 characters long and a is 3 characters long, b.length() / a.length() would yield approximately 2.33. Using Math.ceil gives us 3, meaning we need to repeat a at least 3 times to potentially cover b.
         *
         * 	    4.	Importance in the Loop:
         * 	        •	By calculating repeatCount this way, we ensure that we do not perform unnecessary iterations. We only attempt to find b in a sufficient number of repetitions of a, making the algorithm more efficient.
         *
         *
         *
         * Summary
         *
         * This calculation is a crucial optimization in the algorithm that reduces the number of string concatenations and checks, ensuring the solution is efficient even for larger strings.
         *
         * Example:
         *
         * If we have:
         *
         * 	•	a = "abc" (length 3)
         * 	•	b = "cabcab" (length 6)
         *
         * Calculating:
         *
         * 	•	b.length() / a.length() = 6 / 3 = 2
         * 	•	Math.ceil(2) = 2 (but in this case, we need at least 3 repetitions of a to guarantee b can fit, so repeatCount might be incremented to 3 in the context of the algorithm).
         *
         * This ensures that we account for all potential overlaps in the string repetitions.
         */
        int repeatCount = (int) Math.ceil((double) b.length() / a.length());

        // Build the initial string
        StringBuilder sb = new StringBuilder(a);

        // Check for the first string length after repetitions
        for (int i = 1; i < repeatCount + 2; i++) {
            if (sb.indexOf(b) != -1) {
                return i; // Return the current repeat count
            }
            sb.append(a); // Append 'a' again
        }

        return -1; // 'b' was not found
    }


    // V1-1
    // IDEA : Ad-Hoc
    // https://leetcode.com/problems/repeated-string-match/editorial/
    public int repeatedStringMatch_1_1(String A, String B) {
        int q = 1;
        StringBuilder S = new StringBuilder(A);
        for (; S.length() < B.length(); q++) S.append(A);
        if (S.indexOf(B) >= 0) return q;
        if (S.append(A).indexOf(B) >= 0) return q+1;
        return -1;
    }

    // V1-2
    // IDEA : Rabin-Karp (Rolling Hash)
    // https://leetcode.com/problems/repeated-string-match/editorial/
    public boolean check(int index, String A, String B) {
        for (int i = 0; i < B.length(); i++) {
            if (A.charAt((i + index) % A.length()) != B.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    public int repeatedStringMatch_1_2(String A, String B) {
        int q = (B.length() - 1) / A.length() + 1;
        int p = 113, MOD = 1_000_000_007;
        int pInv = BigInteger.valueOf(p).modInverse(BigInteger.valueOf(MOD)).intValue();

        long bHash = 0, power = 1;
        for (int i = 0; i < B.length(); i++) {
            bHash += power * B.codePointAt(i);
            bHash %= MOD;
            power = (power * p) % MOD;
        }

        long aHash = 0; power = 1;
        for (int i = 0; i < B.length(); i++) {
            aHash += power * A.codePointAt(i % A.length());
            aHash %= MOD;
            power = (power * p) % MOD;
        }

        if (aHash == bHash && check(0, A, B)) return q;
        power = (power * pInv) % MOD;

        for (int i = B.length(); i < (q + 1) * A.length(); i++) {
            aHash -= A.codePointAt((i - B.length()) % A.length());
            aHash *= pInv;
            aHash += power * A.codePointAt(i % A.length());
            aHash %= MOD;
            if (aHash == bHash && check(i - B.length() + 1, A, B)) {
                return i < q * A.length() ? q : q + 1;
            }
        }
        return -1;
    }

    // V2

}
