package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/letter-case-permutation/

import java.util.ArrayList;
import java.util.List;

/**
 *  784. Letter Case Permutation
 *  Medium
 *
 *  Given a string s, you can transform every letter individually to be lowercase
 *  or uppercase to create another string.
 *
 *  Return a list of all possible strings we could create.
 *  You can return the output in any order.
 *
 *  Example 1:
 *
 *  Input: s = "a1b2"
 *  Output: ["a1b2","a1B2","A1b2","A1B2"]
 *
 *  Example 2:
 *
 *  Input: s = "3z4"
 *  Output: ["3z4","3Z4"]
 *
 *  Constraints:
 *
 *  1 <= s.length <= 12
 *  s consists of lowercase English letters, uppercase English letters, and digits.
 */
public class LetterCasePermutation {

    // V0
    // IDEA: backtracking, at each index branch on lower/upper if it is a letter
    /**
     * time = O(n * 2^n)
     * space = O(n) (excluding output)
     */
    public List<String> letterCasePermutation(String s) {
        List<String> res = new ArrayList<>();
        if (s == null) {
            return res;
        }
        helper(s.toCharArray(), 0, new StringBuilder(), res);
        return res;
    }

    private void helper(char[] arr, int idx, StringBuilder cur, List<String> res) {
        if (idx == arr.length) {
            res.add(cur.toString());
            return;
        }
        char c = arr[idx];
        if (Character.isLetter(c)) {
            cur.append(Character.toLowerCase(c));
            helper(arr, idx + 1, cur, res);
            cur.deleteCharAt(cur.length() - 1);

            cur.append(Character.toUpperCase(c));
            helper(arr, idx + 1, cur, res);
            cur.deleteCharAt(cur.length() - 1);
        } else {
            cur.append(c);
            helper(arr, idx + 1, cur, res);
            cur.deleteCharAt(cur.length() - 1);
        }
    }

    // V1
    // IDEA: iterative BFS-like expansion, duplicate the current result set per letter
    /**
     * time = O(n * 2^n)
     * space = O(n * 2^n)
     */
    public List<String> letterCasePermutation_1(String s) {
        List<String> res = new ArrayList<>();
        res.add("");
        for (char c : s.toCharArray()) {
            List<String> next = new ArrayList<>();
            if (Character.isLetter(c)) {
                for (String prefix : res) {
                    next.add(prefix + Character.toLowerCase(c));
                    next.add(prefix + Character.toUpperCase(c));
                }
            } else {
                for (String prefix : res) {
                    next.add(prefix + c);
                }
            }
            res = next;
        }
        return res;
    }
}
