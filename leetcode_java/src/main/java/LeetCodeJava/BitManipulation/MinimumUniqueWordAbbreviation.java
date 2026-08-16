package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/minimum-unique-word-abbreviation/description/

import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

/**
 * 411. Minimum Unique Word Abbreviation
 * Hard
 * Lock: Prime
 *
 * A string can be abbreviated by replacing any number of non-adjacent substrings
 * with their lengths. For example, a string such as "substitution" could be
 * abbreviated as (but not limited to):
 *
 * - "s10n"        ("s ubstitutio n")
 * - "sub4u4"      ("sub stit u tion")
 * - "12"          ("substitution")
 * - "su3i1u2on"   ("su bst i t u ti on")
 * - "substitution" (no substrings replaced)
 *
 * Note that "s55n" ("s ubsti tutio n") is not a valid abbreviation of
 * "substitution" because the replaced substrings are adjacent.
 *
 * The length of an abbreviation is the number of letters that were not replaced
 * plus the number of substrings that were replaced. For example, the abbreviation
 * "s10n" has a length of 3 (2 letters + 1 substring) and "su3i1u2on" has a length
 * of 9 (6 letters + 3 substrings).
 *
 * Given a target string target and an array of strings dictionary, return an
 * abbreviation of target with the shortest possible length such that it is not an
 * abbreviation of any string in dictionary. If there are multiple shortest
 * abbreviations, return any of them.
 *
 * Example 1:
 *
 * Input: target = "apple", dictionary = ["blade"]
 * Output: "a4"
 * Explanation: The shortest abbreviation of "apple" is "5", but this is also an
 * abbreviation of "blade".
 * The next shortest abbreviations are "a4" and "4e". "4e" is an abbreviation of
 * blade while "a4" is not. Hence, return "a4".
 *
 * Example 2:
 *
 * Input: target = "apple", dictionary = ["blade","plain","amber"]
 * Output: "1p3"
 * Explanation: "5" is an abbreviation of both "apple" but also every word in the
 * dictionary.
 * "a4" is an abbreviation of "apple" but also "amber".
 * "4e" is an abbreviation of "apple" but also "blade".
 * "1p3", "2p2", and "3l1" are the next shortest abbreviations of "apple".
 * Since none of them are abbreviations of words in the dictionary, returning any
 * of them is correct.
 *
 * Constraints:
 *
 * m == target.length
 * n == dictionary.length
 * 1 <= m <= 21
 * 0 <= n <= 1000
 * 1 <= dictionary[i].length <= 100
 * log2(n) + m <= 21 if n > 0
 * target and dictionary[i] consist of lowercase English letters.
 * dictionary does not contain target.
 *
 */
public class MinimumUniqueWordAbbreviation {

    // V0
    // IDEA: BITMASK ENUMERATION
    /**
     *  Only dictionary words with the SAME LENGTH as target can ever collide.
     *  For each such word w build a `diff mask`: bit i set <=> w[i] != target[i].
     *
     *  An abbreviation is described by a mask over target's positions:
     *     bit i SET   -> position i is REPLACED (folded into a number)
     *     bit i CLEAR -> position i KEEPS its letter
     *
     *  The abbreviation still MATCHES word w iff every position where they differ
     *  got replaced, i.e.  diff & keep == 0  (keep = complement of mask).
     *
     *  -> so the abbreviation is UNIQUE iff  diff & keep != 0  for EVERY diff.
     *
     *  Enumerate all 2^m masks, keep the one with the SMALLEST abbreviation length.
     *  The constraint log2(n) + m <= 21 bounds n * 2^m by ~2 * 10^6.
     *
     *  time  = O(2^m * n)   // m = target.length, n = number of same-length dict words
     *  space = O(n)
     */
    public String minAbbreviation(String target, String[] dictionary) {
        int m = target.length();

        List<Integer> diffs = new ArrayList<>();
        for (String w : dictionary) {
            /** NOTE !!!
             *
             *  a DIFFERENT length can never be abbreviated to the same thing
             *  -> such words are irrelevant
             */
            if (w.length() != m) {
                continue;
            }
            int d = 0;
            for (int i = 0; i < m; i++) {
                if (w.charAt(i) != target.charAt(i)) {
                    d |= 1 << i;
                }
            }
            diffs.add(d);
        }

        // nothing can collide -> fold the WHOLE word
        if (diffs.isEmpty()) {
            return String.valueOf(m);
        }

        int full = (1 << m) - 1;
        int bestMask = 0;
        int bestLen = m + 1;

        for (int mask = 0; mask <= full; mask++) {
            int keep = full ^ mask;

            // every dictionary word must differ at some KEPT position
            boolean ok = true;
            for (int d : diffs) {
                if ((d & keep) == 0) {
                    ok = false;
                    break;
                }
            }
            if (!ok) {
                continue;
            }

            int length = abbrLen(mask, m);
            if (length < bestLen) {
                bestLen = length;
                bestMask = mask;
            }
        }

        return toAbbr(target, bestMask);
    }

    /** a RUN of replaced positions costs 1 (the number), a kept letter costs 1 */
    private int abbrLen(int mask, int m) {
        int length = 0;
        int i = 0;
        while (i < m) {
            if ((mask >> i & 1) == 1) {
                while (i < m && (mask >> i & 1) == 1) {
                    i += 1;
                }
                length += 1;
            } else {
                length += 1;
                i += 1;
            }
        }
        return length;
    }

    private String toAbbr(String target, int mask) {
        int m = target.length();
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < m) {
            if ((mask >> i & 1) == 1) {
                int j = i;
                while (j < m && (mask >> j & 1) == 1) {
                    j += 1;
                }
                out.append(j - i);
                i = j;
            } else {
                out.append(target.charAt(i));
                i += 1;
            }
        }
        return out.toString();
    }


    // V1
    // IDEA: ENUMERATE MASKS IN ORDER OF ABBREVIATION LENGTH (early exit)
    /**
     *  V0 scans all 2^m masks and keeps the best. Sorting the masks by their
     *  abbreviation length first means the FIRST valid one we meet is already
     *  optimal -- we can return immediately.
     *
     *  Same worst case, but on typical inputs the answer is short and the scan
     *  stops almost at once.
     *
     *  time  = O(2^m * (m + log(2^m)) + 2^m * n)
     *  space = O(2^m)
     */
    public String minAbbreviation_1(String target, String[] dictionary) {
        int m = target.length();

        List<Integer> diffs = new ArrayList<>();
        for (String w : dictionary) {
            if (w.length() != m) {
                continue;
            }
            int d = 0;
            for (int i = 0; i < m; i++) {
                if (w.charAt(i) != target.charAt(i)) {
                    d |= 1 << i;
                }
            }
            diffs.add(d);
        }
        if (diffs.isEmpty()) {
            return String.valueOf(m);
        }

        int full = (1 << m) - 1;
        Integer[] masks = new Integer[1 << m];
        for (int i = 0; i <= full; i++) {
            masks[i] = i;
        }
        Arrays.sort(masks, Comparator.comparingInt(mk -> abbrLength(mk, m)));

        for (int mask : masks) {
            int keep = full ^ mask;
            boolean ok = true;
            for (int d : diffs) {
                if ((d & keep) == 0) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                return buildAbbr(target, mask);
            }
        }
        return target;
    }

    /** abbreviation length of a mask: each replaced RUN costs 1, each kept char 1 */
    private int abbrLength(int mask, int m) {
        int len = 0;
        int i = 0;
        while (i < m) {
            if (((mask >> i) & 1) == 1) {
                while (i < m && ((mask >> i) & 1) == 1) {
                    i += 1;
                }
            } else {
                i += 1;
            }
            len += 1;
        }
        return len;
    }

    private String buildAbbr(String target, int mask) {
        int m = target.length();
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < m) {
            if (((mask >> i) & 1) == 1) {
                int j = i;
                while (j < m && ((mask >> j) & 1) == 1) {
                    j += 1;
                }
                out.append(j - i);
                i = j;
            } else {
                out.append(target.charAt(i));
                i += 1;
            }
        }
        return out.toString();
    }

    // V2
    // IDEA: DFS OVER POSITIONS WITH BRANCH-AND-BOUND
    /**
     *  Walk the target left to right deciding, at each position, whether to KEEP
     *  the letter or start/extend a replaced run -- pruning any branch whose
     *  partial length has already reached the best answer found so far.
     *
     *  Never materialises the 2^m mask space, so it wins whenever a short
     *  abbreviation exists.
     *
     *  time  = O(2^m * n) worst case, far less with the bound
     *  space = O(m)
     */
    private int bestLen2;
    private int bestMask2;

    public String minAbbreviation_2(String target, String[] dictionary) {
        int m = target.length();

        List<Integer> diffs = new ArrayList<>();
        for (String w : dictionary) {
            if (w.length() != m) {
                continue;
            }
            int d = 0;
            for (int i = 0; i < m; i++) {
                if (w.charAt(i) != target.charAt(i)) {
                    d |= 1 << i;
                }
            }
            diffs.add(d);
        }
        if (diffs.isEmpty()) {
            return String.valueOf(m);
        }

        this.bestLen2 = m + 1;
        this.bestMask2 = 0;
        dfsAbbr(0, 0, 0, false, m, diffs);
        return buildAbbr(target, bestMask2);
    }

    private void dfsAbbr(int pos, int mask, int len, boolean inRun, int m, List<Integer> diffs) {
        if (len >= bestLen2) {
            return; // BOUND: this branch can no longer win
        }
        if (pos == m) {
            int keep = ((1 << m) - 1) ^ mask;
            for (int d : diffs) {
                if ((d & keep) == 0) {
                    return; // collides with a dictionary word
                }
            }
            bestLen2 = len;
            bestMask2 = mask;
            return;
        }

        // keep target[pos] -> always costs 1
        dfsAbbr(pos + 1, mask, len + 1, false, m, diffs);
        // replace target[pos] -> costs 1 only when it STARTS a new run
        dfsAbbr(pos + 1, mask | (1 << pos), inRun ? len : len + 1, true, m, diffs);
    }

    // V3
    // IDEA: SAME ENUMERATION, LENGTH VIA A BIT TRICK (no per-mask loop)
    /**
     *  The abbreviation length equals
     *      (number of kept bits) + (number of replaced RUNS)
     *  and the number of runs is the popcount of `mask & ~(mask << 1)`, i.e. the
     *  count of positions where a run STARTS.
     *
     *  -> the length becomes two popcounts instead of an O(m) walk, dropping the
     *     whole enumeration from O(2^m * m) to O(2^m).
     *
     *  time  = O(2^m * n)
     *  space = O(n)
     */
    public String minAbbreviation_3(String target, String[] dictionary) {
        int m = target.length();

        List<Integer> diffs = new ArrayList<>();
        for (String w : dictionary) {
            if (w.length() != m) {
                continue;
            }
            int d = 0;
            for (int i = 0; i < m; i++) {
                if (w.charAt(i) != target.charAt(i)) {
                    d |= 1 << i;
                }
            }
            diffs.add(d);
        }
        if (diffs.isEmpty()) {
            return String.valueOf(m);
        }

        int full = (1 << m) - 1;
        int bestMask = 0;
        int bestLen = m + 1;

        for (int mask = 0; mask <= full; mask++) {
            int keep = full ^ mask;

            boolean ok = true;
            for (int d : diffs) {
                if ((d & keep) == 0) {
                    ok = false;
                    break;
                }
            }
            if (!ok) {
                continue;
            }

            /** NOTE !!!
             *
             *  `mask & ~(mask << 1)` isolates the FIRST bit of every replaced run,
             *  so its popcount is the number of numbers in the abbreviation
             */
            int runs = Integer.bitCount(mask & ~(mask << 1));
            int len = Integer.bitCount(keep) + runs;

            if (len < bestLen) {
                bestLen = len;
                bestMask = mask;
            }
        }

        return buildAbbr(target, bestMask);
    }

}
