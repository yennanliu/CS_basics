package LeetCodeJava.Greedy;

// https://leetcode.com/problems/string-without-aaa-or-bbb/description/

import java.util.PriorityQueue;

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

    // V0-1
    // IDEA: GREEDY (gemini)
    public String strWithout3a3b_0_1(int a, int b) {
        StringBuilder res = new StringBuilder();
        int continueA = 0;
        int continueB = 0;

        while (a > 0 || b > 0) {
            // Decision logic: Should we write 'a'?
            // 1. We MUST write 'a' if we have 2 'b's in a row.
            // 2. We CAN write 'a' if we have more 'a's than 'b's AND we don't have 2 'a's in a row.
            boolean writeA = false;
            if (continueB == 2) {
                writeA = true;
            } else if (continueA == 2) {
                writeA = false;
            } else {
                // Greedy: write the one we have more of
                writeA = a >= b;
            }

            if (writeA) {
                res.append("a");
                a--;
                continueA++;
                continueB = 0; // Reset the other counter
            } else {
                res.append("b");
                b--;
                continueB++;
                continueA = 0; // Reset the other counter
            }
        }
        return res.toString();
    }

    // V0-2
    // IDEA: GREEDY (GPT)
    public String strWithout3a3b_0_2(int a, int b) {
        StringBuilder res = new StringBuilder();

        int continueA = 0;
        int continueB = 0;

        while (a > 0 || b > 0) {
            if ((a >= b && continueA < 2) || continueB == 2) {
                res.append("a");
                a--;
                continueA++;
                continueB = 0; // reset B counter
            } else if ((b > a && continueB < 2) || continueA == 2) {
                res.append("b");
                b--;
                continueB++;
                continueA = 0; // reset A counter
            }
        }

        return res.toString();
    }

    // Correct multipleStr2 helper (if you really need it)
    private String multipleStr2(String x, int times) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < times; i++) {
            res.append(x);
        }
        return res.toString();
    }


    // V0-3
    // IDEA: PQ (gemini)
    public String strWithout3a3b_0_3(int a, int b) {
        // Max heap: store [char, count], sort by count descending
        PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> y[1] - x[1]);

        if (a > 0)
            pq.offer(new int[] { 'a', a });
        if (b > 0)
            pq.offer(new int[] { 'b', b });

        StringBuilder res = new StringBuilder();

        while (!pq.isEmpty()) {
            int[] first = pq.poll();

            // Determine how many times to append (max 2)
            int use = Math.min(2, first[1]);
            // But if last two chars are same as first[0], only append 1
            int n = res.length();
            if (n >= 2 && res.charAt(n - 1) == first[0] && res.charAt(n - 2) == first[0]) {
                use = 0;
            }

            if (use == 0) {
                if (pq.isEmpty())
                    break; // no other char to use, done
                int[] second = pq.poll();
                res.append((char) second[0]);
                second[1]--;
                if (second[1] > 0)
                    pq.offer(second);
                pq.offer(first); // push first back
            } else {
                for (int i = 0; i < use; i++)
                    res.append((char) first[0]);
                first[1] -= use;
                if (first[1] > 0)
                    pq.offer(first);
            }
        }

        return res.toString();
    }


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
