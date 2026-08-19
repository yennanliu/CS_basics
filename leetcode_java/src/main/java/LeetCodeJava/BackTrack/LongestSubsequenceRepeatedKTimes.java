package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/longest-subsequence-repeated-k-times/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 *  2014. Longest Subsequence Repeated k Times
 *  Hard
 *
 *  You are given a string s of length n, and an integer k. You are tasked to
 *  find the longest subsequence repeated k times in string s.
 *
 *  A subsequence seq is repeated k times in s if seq * k is a subsequence of s,
 *  where seq * k is seq concatenated k times.
 *  For example, "bba" is repeated 2 times in "bababcba", because "bbabba" is a
 *  subsequence of "bababcba".
 *
 *  Return the longest subsequence repeated k times in s. If multiple such
 *  subsequences exist, return the lexicographically largest one. If there is no
 *  such subsequence, return an empty string.
 *
 *  Example 1:
 *    Input: s = "letsleetcode", k = 2
 *    Output: "let"
 *    Explanation: "let" and "ete" are both repeated 2 times; "let" is the
 *                 lexicographically largest one.
 *
 *  Example 2:
 *    Input: s = "bb", k = 2
 *    Output: "b"
 *
 *  Example 3:
 *    Input: s = "ab", k = 2
 *    Output: ""
 *
 *  Constraints:
 *    n == s.length
 *    2 <= k <= 2000
 *    2 <= n < min(2001, k * 8)
 *    s consists of lowercase English letters.
 */
public class LongestSubsequenceRepeatedKTimes {

    // V0
    // IDEA: BFS OVER CANDIDATES (grow level by level, prune with a greedy check)
    //       n < k * 8  =>  the answer is at most 7 characters long, and only
    //       letters occurring at least k times can appear in it at all.
    //       BFS from "" and extend every surviving candidate by each usable
    //       letter; a candidate survives iff cand * k is a subsequence of s
    //       (greedy scan). any prefix of a valid answer is itself valid, so
    //       pruning is safe.
    //       NOTE !!! visiting the letters in ASCENDING order with a FIFO queue
    //                makes each BFS level come out in ascending lexicographic
    //                order, so the LAST accepted candidate is the longest &
    //                lexicographically largest one.
    /**
     * time = O(26^L * n) with L <= 7, but the k-repetition prune keeps it tiny
     * space = O(number of surviving candidates)
     */
    public String longestSubsequenceRepeatedK(String s, int k) {
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) {
            cnt[c - 'a']++;
        }
        List<Character> letters = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            if (cnt[i] >= k) {
                letters.add((char) ('a' + i));
            }
        }

        String res = "";
        Deque<String> q = new ArrayDeque<>();
        q.add("");
        while (!q.isEmpty()) {
            String cur = q.poll();
            for (Character c : letters) {
                String nxt = cur + c;
                if (repeatedK(s, nxt, k)) {
                    res = nxt;
                    q.add(nxt);
                }
            }
        }
        return res;
    }

    // is t * k a subsequence of s ?
    private boolean repeatedK(String s, String t, int k) {
        int need = k;
        int i = 0;
        for (int p = 0; p < s.length(); p++) {
            if (s.charAt(p) == t.charAt(i)) {
                i++;
                if (i == t.length()) {
                    i = 0;
                    need--;
                    if (need == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
