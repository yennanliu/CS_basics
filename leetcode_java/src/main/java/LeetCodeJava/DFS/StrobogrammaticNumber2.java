package LeetCodeJava.DFS;

// https://leetcode.com/problems/strobogrammatic-number-ii/description/
// https://leetcode.ca/all/247.html

import java.util.*;

/**
 * 247. Strobogrammatic Number II
 * A strobogrammatic number is a number that looks the same when rotated 180 degrees (looked at upside down).
 *
 * Find all strobogrammatic numbers that are of length = n.
 *
 * Example:
 *
 * Input:  n = 2
 * Output: ["11","69","88","96"]
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Cisco Facebook Google
 * Problem Solution
 * 247-Strobogrammatic-Number-II
 *
 */
public class StrobogrammaticNumber2 {

    // V0
    // IDEA: DFS, BUILD FROM THE MIDDLE OUTWARDS (length grows by 2 each level)
    /**
     *  A strobogrammatic number is fixed by the 5 rotation pairs
     *
     *      0<->0, 1<->1, 8<->8, 6<->9, 9<->6
     *
     *  so a valid length-k string is always ONE such pair wrapped around a
     *  valid length-(k-2) string. That gives the recursion
     *
     *      build(k) = { p[0] + inner + p[1] | inner in build(k-2), p in PAIRS }
     *
     *  with base cases build(0) = {""} and build(1) = {"0","1","8"}
     *  (only those 3 digits map to themselves, so they are the legal middles).
     *
     *  NOTE !!! the "0" pair is only allowed on the INNER levels: at the
     *  outermost level (k == n) it would make a leading zero. That is exactly
     *  why `n` is carried along, so the recursion can tell "am I the outer
     *  layer ?" apart from "am I an inner layer ?".
     *
     *  time = O(n * 5^(n/2))
     *  space = O(n * 5^(n/2)) for the output
     */
    public List<String> findStrobogrammatic(int n) {
        return strobogrammaticHelper(n, n);
    }

    private List<String> strobogrammaticHelper(int k, int n) {
        // base case : even length -> one empty core
        if (k == 0) {
            return new ArrayList<>(Arrays.asList(""));
        }
        // base case : odd length -> the self-symmetric digits are the middles
        if (k == 1) {
            return new ArrayList<>(Arrays.asList("0", "1", "8"));
        }

        List<String> inner = strobogrammaticHelper(k - 2, n);
        List<String> res = new ArrayList<>();

        for (String x : inner) {
            // "0" wrap is illegal on the outermost layer (no leading zero)
            if (k != n) {
                res.add("0" + x + "0");
            }
            res.add("1" + x + "1");
            res.add("6" + x + "9");
            res.add("8" + x + "8");
            res.add("9" + x + "6");
        }

        return res;
    }

    // V1-1
    // https://leetcode.ca/2016-08-03-247-Strobogrammatic-Number-II/
    List<String> singleDigitList = new ArrayList<>(Arrays.asList("0", "1", "8")); // not char[], because List can direct return as result
    char[][] digitPair = { {'1', '1'}, {'8', '8'}, {'6', '9'}, {'9', '6'} }; // except '0', a special case

    /**
     * time = O(5^(N/2))
     * space = O(N)
     */
    public List<String> findStrobogrammatic_1_1(int n) {
        return dfs(n, n);
    }

    /**
     * time = O(5^(N/2))
     * space = O(N)
     */
    public List<String> dfs(int k, int n) {
        if (k <= 0) {
            return new ArrayList<String>(Arrays.asList(""));
        }
        if (k == 1) {
            return singleDigitList;
        }

        List<String> subList = dfs(k - 2, n);
        List<String> result = new ArrayList<>();

        for (String str : subList) {
            if (k != n) { // @note: cannot start with 0
                result.add("0" + str + "0");
            }
            for (char[] aDigitPair : digitPair) {
                result.add(aDigitPair[0] + str + aDigitPair[1]);
            }
        }

        return result;
    }

    // V1-2
    // https://leetcode.ca/2016-08-03-247-Strobogrammatic-Number-II/
    private static final int[][] PAIRS = { {1, 1}, {8, 8}, {6, 9}, {9, 6}};
    private int n;

    /**
     * time = O(5^(N/2))
     * space = O(N)
     */
    public List<String> findStrobogrammatic_1_2(int n) {
        this.n = n;
        return dfs(n);
    }

    private List<String> dfs(int u) {
        if (u == 0) {
            return Collections.singletonList("");
        }
        if (u == 1) {
            return Arrays.asList("0", "1", "8");
        }
        List<String> ans = new ArrayList<>();
        for (String v : dfs(u - 2)) {
            for ( int[] p : PAIRS) {
                ans.add(p[0] + v + p[1]);
            }
            if (u != n) {
                ans.add(0 + v + 0);
            }
        }
        return ans;
    }

    // V2-1
    // IDEA : DFS (gpt)
    List<String> res = new ArrayList<>();
    /**
     * time = O(5^(N/2))
     * space = O(N)
     */
    public List<String> findStrobogrammatic_2_1(int n) {
        // Use valid strobogrammatic pairs only
        char[][] pairs = new char[][] {
                {'0', '0'}, {'1', '1'}, {'8', '8'},
                {'6', '9'}, {'9', '6'}
        };

        findNumbers(n, new char[n], 0, n - 1, pairs);
        return res;
    }

    private void findNumbers(int n, char[] current, int left, int right, char[][] pairs) {
        if (left > right) {
            // Base case: Valid strobogrammatic number
            res.add(new String(current));
            return;
        }

        for (char[] pair : pairs) {
            // Avoid leading zero unless the number is of length 1
            if (left == 0 && pair[0] == '0' && n > 1) {
                continue;
            }

            /** NOTE !!!
             *
             *  when left == right we are writing the SINGLE middle cell, so the
             *  two halves of the pair have to be the same digit. '6'/'9' would
             *  overwrite each other and yield an invalid middle (and a dup).
             */
            if (left == right && pair[0] != pair[1]) {
                continue;
            }

            // Place the pair
            current[left] = pair[0];
            current[right] = pair[1];

            // Recursive call
            findNumbers(n, current, left + 1, right - 1, pairs);
        }
    }

}
