package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/verbal-arithmetic-puzzle/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  1307. Verbal Arithmetic Puzzle
 *  Hard
 *
 *  Given an equation, represented by words on the left side and the result on the
 *  right side.
 *
 *  You need to check if the equation is solvable under the following rules:
 *    - Each character is decoded as one digit (0 - 9).
 *    - No two characters can map to the same digit.
 *    - Each words[i] and result are decoded as one number without leading zeros.
 *    - Sum of numbers on the left side (words) will equal to the number on the right
 *      side (result).
 *
 *  Return true if the equation is solvable, otherwise return false.
 *
 *  Example 1:
 *    Input: words = ["SEND","MORE"], result = "MONEY"
 *    Output: true
 *    Explanation: 'S'->9, 'E'->5, 'N'->6, 'D'->7, 'M'->1, 'O'->0, 'R'->8, 'Y'->2
 *                 so that 9567 + 1085 = 10652
 *
 *  Example 2:
 *    Input: words = ["LEET","CODE"], result = "POINT"
 *    Output: false
 *
 *  Constraints:
 *    2 <= words.length <= 5
 *    1 <= words[i].length, result.length <= 7
 *    words[i], result contain only uppercase English letters.
 *    The number of different characters used in the expression is at most 10.
 */
public class VerbalArithmeticPuzzle {

    private int[] coefOf;      // coefficient per letter index
    private boolean[] isLead;  // letter index may not be 0
    private int lettersCnt;
    private long[] posSuf;
    private long[] negSuf;
    private boolean[] usedDigit = new boolean[10];

    // V0
    // IDEA: COEFFICIENT BACKTRACKING
    //       Turn the whole equation into a single linear form:
    //           sum(coef[ch] * digit[ch]) == 0
    //       where a letter in words[i] contributes +10^p and a letter in result
    //       contributes -10^p. Then backtrack over the (at most 10) distinct letters,
    //       assigning distinct digits.
    //
    //       Pruning : keep suffix bounds of the most positive / most negative
    //       contribution still reachable; if even those cannot bring the running
    //       total back to 0, cut the branch.
    //
    //       Letters with the biggest |coefficient| are assigned first so the bound
    //       bites as early as possible.
    /**
     * time = O(10!) worst case, heavily pruned in practice
     * space = O(1)   // at most 10 distinct letters
     */
    public boolean isSolvable(String[] words, String result) {
        // a word longer than the result can never sum to the result
        for (String w : words) {
            if (w.length() > result.length()) {
                return false;
            }
        }

        Map<Character, Integer> coef = new HashMap<>();
        Set<Character> leading = new HashSet<>();
        for (String w : words) {
            for (int i = 0; i < w.length(); i++) {
                char ch = w.charAt(i);
                int p = pow10(w.length() - 1 - i);
                coef.put(ch, (coef.containsKey(ch) ? coef.get(ch) : 0) + p);
            }
            if (w.length() > 1) {
                leading.add(w.charAt(0));
            }
        }
        for (int i = 0; i < result.length(); i++) {
            char ch = result.charAt(i);
            int p = pow10(result.length() - 1 - i);
            coef.put(ch, (coef.containsKey(ch) ? coef.get(ch) : 0) - p);
        }
        if (result.length() > 1) {
            leading.add(result.charAt(0));
        }

        // biggest |coefficient| first -> the bound prunes earlier
        List<Character> letters = new ArrayList<>(coef.keySet());
        final Map<Character, Integer> cf = coef;
        letters.sort(new Comparator<Character>() {
            @Override
            public int compare(Character a, Character b) {
                return Math.abs(cf.get(b)) - Math.abs(cf.get(a));
            }
        });
        int n = letters.size();
        if (n > 10) {
            return false;
        }

        this.lettersCnt = n;
        this.coefOf = new int[n];
        this.isLead = new boolean[n];
        for (int i = 0; i < n; i++) {
            coefOf[i] = coef.get(letters.get(i));
            isLead[i] = leading.contains(letters.get(i));
        }

        // suffix bounds : max / min contribution still reachable from index i on
        this.posSuf = new long[n + 1];
        this.negSuf = new long[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            int c = coefOf[i];
            posSuf[i] = posSuf[i + 1] + (c > 0 ? 9L * c : 0);
            negSuf[i] = negSuf[i + 1] + (c < 0 ? 9L * c : 0);
        }

        Arrays.fill(usedDigit, false);
        return backtrack(0, 0L);
    }

    private boolean backtrack(int i, long total) {
        if (i == lettersCnt) {
            return total == 0;
        }
        // even the most positive / most negative completion cannot reach 0
        if (total + posSuf[i] < 0 || total + negSuf[i] > 0) {
            return false;
        }
        int c = coefOf[i];
        int start = isLead[i] ? 1 : 0;
        for (int d = start; d < 10; d++) {
            if (usedDigit[d]) {
                continue;
            }
            usedDigit[d] = true;
            if (backtrack(i + 1, total + (long) c * d)) {
                return true;
            }
            usedDigit[d] = false;
        }
        return false;
    }

    private int pow10(int e) {
        int r = 1;
        for (int i = 0; i < e; i++) {
            r *= 10;
        }
        return r;
    }
}
