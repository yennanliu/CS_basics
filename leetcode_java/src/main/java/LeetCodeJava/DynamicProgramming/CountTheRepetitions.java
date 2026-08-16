package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-the-repetitions/description/

import java.util.Arrays;
/**
 * 466. Count The Repetitions
 * Hard
 *
 * We define str = [s, n] as the string str which consists of the string s
 * concatenated n times.
 *
 * - For example, str == ["abc", 3] == "abcabcabc".
 *
 * We define that string s1 can be obtained from string s2 if we can remove some
 * characters from s2 such that it becomes s1.
 *
 * - For example, s1 = "abc" can be obtained from s2 = "abdbec" based on our
 *   definition by removing the bolded underlined characters.
 *
 * You are given two strings s1 and s2 and two integers n1 and n2. You have the two
 * strings str1 = [s1, n1] and str2 = [s2, n2].
 *
 * Return the maximum integer m such that str = [str2, m] can be obtained from str1.
 *
 * Example 1:
 *
 * Input: s1 = "acb", n1 = 4, s2 = "ab", n2 = 2
 * Output: 2
 *
 * Example 2:
 *
 * Input: s1 = "acb", n1 = 1, s2 = "acb", n2 = 1
 * Output: 1
 *
 * Constraints:
 *
 * 1 <= s1.length, s2.length <= 100
 * s1 and s2 consist of lowercase English letters.
 * 1 <= n1, n2 <= 10^6
 *
 */
public class CountTheRepetitions {

    // V0
    // IDEA: PRECOMPUTE ONE PASS OF s1, THEN ITERATE
    /**
     *  Matching str1 greedily against repeated copies of s2 is DETERMINISTIC: the
     *  ONLY state we carry from one copy of s1 to the next is `which index of s2
     *  are we currently trying to match` (a value in [0, s2.length)).
     *
     *  So precompute, for EVERY start index i in s2:
     *      cntOf[i] = how many FULL copies of s2 get matched while scanning one s1
     *      nextOf[i] = the index in s2 we end up at
     *
     *  Then walk n1 copies of s1, accumulating cnt and following the next index.
     *  Finally, [str2, m] fits m = totalS2Matched / n2 times.
     *
     *  time  = O(s1.length * s2.length + n1)
     *  space = O(s2.length)
     */
    public int getMaxRepetitions(String s1, int n1, String s2, int n2) {
        int n = s2.length();

        int[] cntOf = new int[n];
        int[] nextOf = new int[n];

        // one pass of s1 starting at s2 index i
        for (int i = 0; i < n; i++) {
            int cnt = 0;
            int j = i;
            for (int t = 0; t < s1.length(); t++) {
                if (s1.charAt(t) == s2.charAt(j)) {
                    j += 1;
                    if (j == n) { // a FULL s2 has been matched
                        cnt += 1;
                        j = 0;
                    }
                }
            }
            cntOf[i] = cnt;
            nextOf[i] = j;
        }

        long total = 0;
        int j = 0;
        for (int t = 0; t < n1; t++) {
            total += cntOf[j];
            j = nextOf[j];
        }

        return (int) (total / n2);
    }

    // V0-1
    // IDEA: SAME PRECOMPUTE + CYCLE DETECTION (O(s2.length) instead of O(n1) steps)
    /**
     *  The mapping j -> nextOf[j] is a FUNCTION on a set of at most s2.length states,
     *  so after at most s2.length copies of s1 we MUST revisit a state -> a CYCLE.
     *  Jump over the whole cycle with ARITHMETIC instead of looping n1 times.
     *
     *  Useful when n1 is far larger than 10^6.
     *
     *  time  = O(s1.length * s2.length)
     *  space = O(s2.length)
     */
    public int getMaxRepetitions_0_1(String s1, int n1, String s2, int n2) {
        int n = s2.length();

        int[] cntOf = new int[n];
        int[] nextOf = new int[n];
        for (int i = 0; i < n; i++) {
            int cnt = 0;
            int j = i;
            for (int t = 0; t < s1.length(); t++) {
                if (s1.charAt(t) == s2.charAt(j)) {
                    j += 1;
                    if (j == n) {
                        cnt += 1;
                        j = 0;
                    }
                }
            }
            cntOf[i] = cnt;
            nextOf[i] = j;
        }

        // s2 index -> {copies of s1 used, s2 matched so far}
        int[] seenUsed = new int[n];
        long[] seenTotal = new long[n];
        java.util.Arrays.fill(seenUsed, -1);

        long total = 0;
        int j = 0;
        int used = 0;
        boolean jumped = false; // a cycle can only be cashed in ONCE

        while (used < n1) {
            if (!jumped && seenUsed[j] >= 0) {
                int cycleLen = used - seenUsed[j];
                long cycleGain = total - seenTotal[j];
                long loops = (long) (n1 - used) / cycleLen;
                total += loops * cycleGain;
                used += loops * cycleLen;
                jumped = true;
                continue; // the leftover copies are walked one by one below
            }
            if (!jumped) {
                seenUsed[j] = used;
                seenTotal[j] = total;
            }
            total += cntOf[j];
            j = nextOf[j];
            used += 1;
        }

        return (int) (total / n2);
    }


    // V1
    // IDEA: BRUTE FORCE -- scan the whole expanded str1
    /**
     *  Walk n1 copies of s1 character by character, advancing a pointer into s2 and
     *  counting completed copies.
     *
     *  O(n1 * len(s1)), which is 10^8 at the limits, but it has no precomputation
     *  and no cycle reasoning -- the oracle for the two clever versions.
     *
     *  time  = O(n1 * len(s1))
     *  space = O(1)
     */
    public int getMaxRepetitions_1(String s1, int n1, String s2, int n2) {
        int j = 0;
        long matched = 0;

        for (int rep = 0; rep < n1; rep++) {
            for (int i = 0; i < s1.length(); i++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    j += 1;
                    if (j == s2.length()) {
                        matched += 1;
                        j = 0;
                    }
                }
            }
        }
        return (int) (matched / n2);
    }

    // V2
    // IDEA: PRECOMPUTE PER STARTING INDEX, then BINARY LIFTING over the copies
    /**
     *  The `apply one copy of s1` map is a function on at most len(s2) states, so
     *  it can be COMPOSED with itself log(n1) times (binary lifting) instead of
     *  applied n1 times.
     *
     *  -> O(len(s2) * log(n1)) to jump through all n1 copies, which stays fast even
     *     if n1 grew far past 10^6.
     *
     *  time  = O(len(s1) * len(s2) + len(s2) * log(n1))
     *  space = O(len(s2) * log(n1))
     */
    public int getMaxRepetitions_2(String s1, int n1, String s2, int n2) {
        int m = s2.length();
        int[] baseNext = new int[m];
        long[] baseCnt = new long[m];

        for (int i = 0; i < m; i++) {
            int j = i;
            long cnt = 0;
            for (int t = 0; t < s1.length(); t++) {
                if (s1.charAt(t) == s2.charAt(j)) {
                    j += 1;
                    if (j == m) {
                        cnt += 1;
                        j = 0;
                    }
                }
            }
            baseNext[i] = j;
            baseCnt[i] = cnt;
        }

        int levels = 1;
        while ((1L << levels) <= n1) {
            levels += 1;
        }
        int[][] nextAt = new int[levels][m];
        long[][] cntAt = new long[levels][m];
        nextAt[0] = baseNext;
        cntAt[0] = baseCnt;

        for (int L = 1; L < levels; L++) {
            for (int i = 0; i < m; i++) {
                int mid = nextAt[L - 1][i];
                nextAt[L][i] = nextAt[L - 1][mid];
                cntAt[L][i] = cntAt[L - 1][i] + cntAt[L - 1][mid];
            }
        }

        long total = 0;
        int state = 0;
        int remain = n1;
        for (int L = levels - 1; L >= 0; L--) {
            if (((remain >> L) & 1) == 1) {
                total += cntAt[L][state];
                state = nextAt[L][state];
            }
        }
        return (int) (total / n2);
    }

    // V3
    // IDEA: DETECT THE CYCLE BY (s2 index) REVISIT, with an explicit history table
    /**
     *  Record, for every s2 index, the first copy of s1 at which it was seen. The
     *  second sighting closes a cycle whose length and gain are the differences --
     *  then multiply out the whole-cycle part and walk only the remainder.
     *
     *  Same idea as V0-1 but the history is a flat array indexed by state rather
     *  than a growing map, so the cycle is found in O(1) per copy.
     *
     *  time  = O(len(s1) * len(s2))
     *  space = O(len(s2))
     */
    public int getMaxRepetitions_3(String s1, int n1, String s2, int n2) {
        int m = s2.length();
        int[] nextOf = new int[m];
        long[] cntOf = new long[m];
        for (int i = 0; i < m; i++) {
            int j = i;
            long cnt = 0;
            for (int t = 0; t < s1.length(); t++) {
                if (s1.charAt(t) == s2.charAt(j)) {
                    j += 1;
                    if (j == m) {
                        cnt += 1;
                        j = 0;
                    }
                }
            }
            nextOf[i] = j;
            cntOf[i] = cnt;
        }

        int[] seenAt = new int[m];
        long[] seenTotal = new long[m];
        Arrays.fill(seenAt, -1);

        long total = 0;
        int state = 0;
        int used = 0;

        while (used < n1) {
            if (seenAt[state] >= 0) {
                int cycleLen = used - seenAt[state];
                long cycleGain = total - seenTotal[state];
                long loops = (long) (n1 - used) / cycleLen;
                total += loops * cycleGain;
                used += (int) (loops * cycleLen);
                Arrays.fill(seenAt, -1);   // cash the cycle in once, then walk out
                continue;
            }
            seenAt[state] = used;
            seenTotal[state] = total;
            total += cntOf[state];
            state = nextOf[state];
            used += 1;
        }
        return (int) (total / n2);
    }

}
