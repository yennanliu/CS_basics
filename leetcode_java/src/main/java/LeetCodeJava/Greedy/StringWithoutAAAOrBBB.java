package LeetCodeJava.Greedy;

// https://leetcode.com/problems/string-without-aaa-or-bbb/description/
/**
 * 984. String Without AAA or BBB
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given two integers a and b, return any string s such that:
 *
 * s has length a + b and contains exactly a 'a' letters, and exactly b 'b' letters,
 * The substring 'aaa' does not occur in s, and
 * The substring 'bbb' does not occur in s.
 *
 *
 * Example 1:
 *
 * Input: a = 1, b = 2
 * Output: "abb"
 * Explanation: "abb", "bab" and "bba" are all correct answers.
 * Example 2:
 *
 * Input: a = 4, b = 1
 * Output: "aabaa"
 *
 *
 * Constraints:
 *
 * 0 <= a, b <= 100
 * It is guaranteed such an s exists for the given a and b.
 *
 *
 *
 */
public class StringWithoutAAAOrBBB {

    // V0
//    public String strWithout3a3b(int a, int b) {
//
//    }

    // V1
    // IDEA: GREEDY
    // https://leetcode.com/problems/string-without-aaa-or-bbb/editorial/
    public String strWithout3a3b_1(int A, int B) {
        StringBuilder ans = new StringBuilder();

        while (A > 0 || B > 0) {
            boolean writeA = false;
            int L = ans.length();
            if (L >= 2 && ans.charAt(L - 1) == ans.charAt(L - 2)) {
                if (ans.charAt(L - 1) == 'b')
                    writeA = true;
            } else {
                if (A >= B)
                    writeA = true;
            }

            if (writeA) {
                A--;
                ans.append('a');
            } else {
                B--;
                ans.append('b');
            }
        }

        return ans.toString();
    }


    // V2
    // https://leetcode.com/problems/string-without-aaa-or-bbb/solutions/1175800/my-java-solution-with-the-basic-idea-as-b0g35/
    public String strWithout3a3b_2(int a, int b) {
        StringBuilder sb = new StringBuilder();
        while (a > 0 || b > 0) {
            String s = sb.toString();
            // if we have aa as the last 2 characters, then the next one is b
            if (s.endsWith("aa")) {
                sb.append("b");
                b--;
            }
            // if we have bb as the last 2 characters, then the next one is a
            else if (s.endsWith("bb")) {
                sb.append("a");
                a--;
            }
            // if a > b, append a
            else if (a > b) {
                sb.append("a");
                a--;
            }
            // if b >= a, append b
            else {
                sb.append("b");
                b--;
            }
        }
        return sb.toString();
    }


    // V3
    // https://leetcode.com/problems/string-without-aaa-or-bbb/solutions/314561/a-simple-java-recursion-solution-by-yili-7sse/
    StringBuilder sb = new StringBuilder();

    public String strWithout3a3b_3(int A, int B) {
        if (A == 0 || B == 0) {
            while (A-- > 0)
                sb.append('a');
            while (B-- > 0)
                sb.append('b');
        } else if (A == B) {
            sb.append("ab");
            strWithout3a3b_3(A - 1, B - 1);
        } else if (A > B) { // A > B > 0
            sb.append("aab");
            strWithout3a3b_3(A - 2, B - 1);
        } else { // B > A > 0
            sb.append("bba");
            strWithout3a3b_3(A - 1, B - 2);
        }
        return sb.toString();
    }





}
