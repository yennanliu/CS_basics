package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/palindrome-permutation-ii/

import java.util.*;

/**
 *  267. Palindrome Permutation II
 *  Medium
 *
 *  Given a string s, return all the palindromic permutations (without
 *  duplicates) of it.
 *
 *  You may return the answer in any order. If s has no palindromic permutation,
 *  return an empty list.
 *
 *  Example 1:
 *   Input: s = "aabb"
 *   Output: ["abba","baab"]
 *
 *  Example 2:
 *   Input: s = "abc"
 *   Output: []
 *
 *  Constraints:
 *   1 <= s.length <= 16
 *   s consists of only lowercase English letters.
 */
public class PalindromePermutationII {

    // V0
    // IDEA: a palindrome exists only if at most 1 char has an odd count.
    //       Build "half" of the string, permute the half with duplicate pruning,
    //       then mirror it around the (optional) middle char.
    /**
     * time = O((n/2)! * n)
     * space = O(n)
     */
    public List<String> generatePalindromes(String s) {
        List<String> res = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return res;
        }

        int[] cnt = new int[128];
        for (char c : s.toCharArray()) {
            cnt[c] += 1;
        }

        String mid = "";
        List<Character> halfList = new ArrayList<>();
        for (int c = 0; c < 128; c++) {
            if (cnt[c] == 0) {
                continue;
            }
            if (cnt[c] % 2 == 1) {
                // more than one odd-count char -> no palindrome possible
                if (mid.length() > 0) {
                    return res;
                }
                mid = String.valueOf((char) c);
            }
            for (int i = 0; i < cnt[c] / 2; i++) {
                halfList.add((char) c);
            }
        }

        char[] half = new char[halfList.size()];
        for (int i = 0; i < halfList.size(); i++) {
            half[i] = halfList.get(i);
        }
        // sorted so equal chars are adjacent -> duplicate pruning works
        Arrays.sort(half);

        if (half.length == 0) {
            res.add(mid);
            return res;
        }

        boolean[] used = new boolean[half.length];
        backtrack(half, used, new StringBuilder(), mid, res);
        return res;
    }

    private void backtrack(char[] half, boolean[] used, StringBuilder cur,
                           String mid, List<String> res) {
        if (cur.length() == half.length) {
            String left = cur.toString();
            String right = new StringBuilder(left).reverse().toString();
            res.add(left + mid + right);
            return;
        }
        for (int i = 0; i < half.length; i++) {
            if (used[i]) {
                continue;
            }
            // skip duplicated branch at the same tree level
            if (i > 0 && half[i] == half[i - 1] && !used[i - 1]) {
                continue;
            }
            used[i] = true;
            cur.append(half[i]);
            backtrack(half, used, cur, mid, res);
            cur.deleteCharAt(cur.length() - 1);
            used[i] = false;
        }
    }
}
