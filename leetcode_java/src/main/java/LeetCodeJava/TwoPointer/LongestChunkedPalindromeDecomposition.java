package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/longest-chunked-palindrome-decomposition/description/
/**
 *  1147. Longest Chunked Palindrome Decomposition
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a string text. You should split it to k substrings (subtext1, subtext2, ..., subtextk) such that:
 *
 * subtexti is a non-empty string.
 * The concatenation of all the substrings is equal to text (i.e., subtext1 + subtext2 + ... + subtextk == text).
 * subtexti == subtextk - i + 1 for all valid values of i (i.e., 1 <= i <= k).
 * Return the largest possible value of k.
 *
 *
 *
 * Example 1:
 *
 * Input: text = "ghiabcdefhelloadamhelloabcdefghi"
 * Output: 7
 * Explanation: We can split the string on "(ghi)(abcdef)(hello)(adam)(hello)(abcdef)(ghi)".
 * Example 2:
 *
 * Input: text = "merchant"
 * Output: 1
 * Explanation: We can split the string on "(merchant)".
 * Example 3:
 *
 * Input: text = "antaprezatepzapreanta"
 * Output: 11
 * Explanation: We can split the string on "(a)(nt)(a)(pre)(za)(tep)(za)(pre)(a)(nt)(a)".
 *
 *
 * Constraints:
 *
 * 1 <= text.length <= 1000
 * text consists only of lowercase English characters.
 *
 *
 *
 */
public class LongestChunkedPalindromeDecomposition {

    // V0
//    public int longestDecomposition(String text) {
//
//    }


    // V1


    // V2
    // https://leetcode.com/problems/longest-chunked-palindrome-decomposition/solutions/350762/java-0ms-concise-beats-100-both-time-and-pl1v/
    public int longestDecomposition_2(String text) {
        int n = text.length();
        for (int i = 0; i < n / 2; i++)
            if (text.substring(0, i + 1).equals(text.substring(n - 1 - i, n)))
                return 2 + longestDecomposition_2(text.substring(i + 1, n - 1 - i));
        return (n == 0) ? 0 : 1;
    }


    // V3
    // https://leetcode.com/problems/longest-chunked-palindrome-decomposition/solutions/6253836/most-easiest-approach-without-dp-and-rol-dl6y/
    public int longestDecomposition_3(String text) {
        int n = text.length();
        int k = 0, totalLength = 0;
        int str1Start = 0, str1End = 0;
        int str2Start = n - 1, str2End = n;
        while (str1End < str2Start) {
            if (text.substring(str1Start, str1End + 1).equals(text.substring(str2Start, str2End))) {
                totalLength += (str2End - str2Start) * 2;
                k++;
                str1Start = str1End + 1;
                str2End = str2Start;
            }
            str1End++;
            str2Start--;
        }
        if (totalLength < n)
            return (k * 2) + 1;
        return k * 2;
    }



    // V4
    // https://leetcode.com/problems/longest-chunked-palindrome-decomposition/solutions/7101896/beginner-friendly-solution-by-amyy_1256-p2en/
    public int longestDecomposition_4(String text) {

        int left = 0;
        int right = text.length() - 1;
        int cnt = 0;

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        while (left <= right) {
            sb1.append(text.charAt(left));
            sb2.append(text.charAt(right));

            if ((sb1.toString()).equals(sb2.reverse().toString())) {
                if (left == right)
                    cnt += 1;
                else
                    cnt += 2;

                sb1.setLength(0);
                sb2.setLength(0);
            } else {
                sb2.reverse();
            }
            left++;
            right--;
        }

        if (sb1.length() > 0)
            cnt += 1;
        return cnt;
    }





}
