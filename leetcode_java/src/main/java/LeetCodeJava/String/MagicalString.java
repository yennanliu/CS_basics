package LeetCodeJava.String;

// https://leetcode.com/problems/magical-string/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * 481. Magical String
 * Medium
 *
 * A magical string s consists of only '1' and '2' and obeys the following rule:
 *
 * - Concatenating the sequence of lengths of its consecutive groups of identical
 *   characters '1' and '2' generates the string s itself.
 *
 * The first few elements of s is s = "1221121221221121122......". If we group the
 * consecutive 1's and 2's in s, it will be "1 22 11 2 1 22 1 22 11 2 11 22 ......"
 * and counting the occurrences of 1's or 2's in each group yields the sequence
 * "1 2 2 1 1 2 1 2 2 1 2 2 ......".
 *
 * You can see that concatenating the occurrence sequence gives us s itself.
 *
 * Given an integer n, return the number of 1's in the first n number in the
 * magical string s.
 *
 * Example 1:
 *
 * Input: n = 6
 * Output: 3
 * Explanation: The first 6 elements of magical string s is "122112" and it
 * contains three 1's, so return 3.
 *
 * Example 2:
 *
 * Input: n = 1
 * Output: 1
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 *
 */
public class MagicalString {

    // V0
    // IDEA: SELF-REFERENTIAL SIMULATION (TWO POINTERS)
    /**
     *  The string describes ITS OWN run lengths, so we GROW it while READING it:
     *
     *    - seed with "122"  (the first two groups: "1" and "22")
     *    - a pointer i walks the string; s[i] is the LENGTH of the next group
     *    - group values ALTERNATE 1, 2, 1, 2, ... so the next value is 3 - last
     *
     *      1 2 2            i = 2, s[i] = 2, last = 2 -> append "11"
     *          ^
     *      1 2 2 1 1        i = 3, s[i] = 1, last = 1 -> append "2"
     *            ^
     *      1 2 2 1 1 2      i = 4, s[i] = 1, last = 2 -> append "1"
     *              ^
     *
     *  Stop once the string is at least n long, then count the 1's in the first n.
     *
     *  NOTE !!! the buffer may overshoot n (a group of 2 can cross the boundary),
     *           so it is sized n + 2 and only the first n entries are counted.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public int magicalString(int n) {
        // seed "122"; +2 headroom because the last group may overshoot n
        int[] s = new int[n + 2];
        s[0] = 1;
        s[1] = 2;
        s[2] = 2;

        int len = 3;
        int i = 2; // points at the digit giving the NEXT group size

        while (len < n) {
            int nxt = 3 - s[len - 1]; // groups alternate between 1 and 2
            for (int t = 0; t < s[i]; t++) {
                s[len] = nxt;
                len += 1;
            }
            i += 1;
        }

        int res = 0;
        for (int k = 0; k < n; k++) {
            if (s[k] == 1) {
                res += 1;
            }
        }
        return res;
    }


    // V1
    // IDEA: GROW AN ArrayList AND COUNT DURING GENERATION
    /**
     *  Same self-referential growth, but the count of 1s is accumulated AS the
     *  sequence is built rather than by a second pass at the end.
     *
     *  One pass instead of two, and the running count is available at any
     *  intermediate length -- useful if the caller wants several prefixes.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public int magicalString_1(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n <= 3) {
            return 1; // "122" -> exactly one 1 in any prefix of length 1..3
        }

        List<Integer> s = new ArrayList<>(Arrays.asList(1, 2, 2));
        int ones = 1;
        int i = 2;

        while (s.size() < n) {
            int nxt = 3 - s.get(s.size() - 1);
            int times = s.get(i);
            for (int t = 0; t < times && s.size() < n; t++) {
                s.add(nxt);
                if (nxt == 1) {
                    ones += 1;
                }
            }
            i += 1;
        }
        return ones;
    }

    // V2
    // IDEA: StringBuilder OF CHARACTERS ('1' / '2')
    /**
     *  Keep the sequence as text rather than as ints. `3 - digit` becomes the
     *  character flip '1' <-> '2', and the final count is a character scan.
     *
     *  Half the memory of an int[] and the buffer can be printed directly, which is
     *  the easiest way to eyeball that the sequence really describes itself.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public int magicalString_2(int n) {
        if (n <= 0) {
            return 0;
        }
        StringBuilder s = new StringBuilder("122");
        int i = 2;

        while (s.length() < n) {
            char last = s.charAt(s.length() - 1);
            char nxt = last == '1' ? '2' : '1';
            int times = s.charAt(i) - '0';
            for (int t = 0; t < times; t++) {
                s.append(nxt);
            }
            i += 1;
        }

        int ones = 0;
        for (int t = 0; t < n; t++) {
            if (s.charAt(t) == '1') {
                ones += 1;
            }
        }
        return ones;
    }

    // V3
    // IDEA: GROUP-LEVEL GENERATION (emit whole runs, never single elements)
    /**
     *  Track the sequence as a list of RUN LENGTHS plus the alternating value,
     *  emitting a whole group at a time and stopping as soon as the cumulative
     *  length reaches n.
     *
     *  The count of 1s then comes from the group bookkeeping, so the individual
     *  elements past the boundary are never materialised -- the group that would
     *  overshoot n is CLIPPED instead.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public int magicalString_3(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n <= 3) {
            return 1;
        }

        int[] s = new int[n + 2];
        s[0] = 1;
        s[1] = 2;
        s[2] = 2;
        int len = 3;
        int ones = 1;
        int i = 2;

        while (len < n) {
            int value = 3 - s[len - 1];
            int groupLen = s[i];
            // CLIP the group so we never write past n
            int emit = Math.min(groupLen, n - len);
            for (int t = 0; t < emit; t++) {
                s[len++] = value;
            }
            if (value == 1) {
                ones += emit;
            }
            i += 1;
        }
        return ones;
    }

}
