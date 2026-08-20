package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-pairs-of-similar-strings/description/

import java.util.*;

/**
 * 2506. Count Pairs Of Similar Strings
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed string array words.
 *
 * Two strings are similar if they consist of the same characters.
 *
 * For example, "abca" and "cba" are similar since both consist of characters 'a', 'b', and 'c'.
 * However, "abacba" and "bcfd" are not similar since they do not consist of the same characters.
 * Return the number of pairs (i, j) such that 0 <= i < j <= word.length - 1 and the two strings words[i] and words[j] are similar.
 *
 *
 *
 * Example 1:
 *
 * Input: words = ["aba","aabb","abcd","bac","aabc"]
 * Output: 2
 * Explanation: There are 2 pairs that satisfy the conditions:
 * - i = 0 and j = 1 : both words[0] and words[1] only consist of characters 'a' and 'b'.
 * - i = 3 and j = 4 : both words[3] and words[4] only consist of characters 'a', 'b', and 'c'.
 * Example 2:
 *
 * Input: words = ["aabb","ab","ba"]
 * Output: 3
 * Explanation: There are 3 pairs that satisfy the conditions:
 * - i = 0 and j = 1 : both words[0] and words[1] only consist of characters 'a' and 'b'.
 * - i = 0 and j = 2 : both words[0] and words[2] only consist of characters 'a' and 'b'.
 * - i = 1 and j = 2 : both words[1] and words[2] only consist of characters 'a' and 'b'.
 * Example 3:
 *
 * Input: words = ["nba","cba","dba"]
 * Output: 0
 * Explanation: Since there does not exist any pair that satisfies the conditions, we return 0.
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 100
 * 1 <= words[i].length <= 100
 * words[i] consist of only lowercase English letters.
 *
 *
 */
public class CountPairsOfSimilarStrings {

    // V0
    // IDEA: BIT MASK (as `char set` key) + HASH MAP + MATH
    /**
     *  "same characters" only cares about the SET of letters,
     *  so a word can be squashed into a 26 bit mask
     *  (bit i set <=> letter ('a' + i) appears).
     *
     *  -> group the words by mask, and every group of size c
     *     contributes  c * (c - 1) / 2  pairs.
     *
     *  time = O(N * L), space = O(N)   // N words, L word length
     */
    public int similarPairs(String[] words) {
        // edge
        if (words == null || words.length < 2) {
            return 0;
        }

        // {char set bit mask : cnt}
        Map<Integer, Integer> cnt = new HashMap<>();
        int res = 0;

        for (String w : words) {
            int mask = 0;
            for (char c : w.toCharArray()) {
                mask |= (1 << (c - 'a'));
            }
            /**  NOTE !!!
             *
             *   every word already seen with the SAME mask
             *   forms a new pair with the current one,
             *   so we can accumulate on the fly
             */
            int seen = cnt.getOrDefault(mask, 0);
            res += seen;
            cnt.put(mask, seen + 1);
        }

        return res;
    }

    // V0-1
    // IDEA: SET + HASHMAP + MATH (fixed by gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int similarPairs_0_1(String[] words) {
        if (words == null || words.length < 2) {
            return 0;
        }

        Map<String, Integer> map = new HashMap<>();

        for (String word : words) {
            String key = normalize(word);
            map.put(key, map.getOrDefault(key, 0) + 1);
        }

        int res = 0;
        for (int val : map.values()) {
            if (val >= 2) {
                res += val * (val - 1) / 2; // nC2
            }
        }

        return res;
    }

    private String normalize(String str) {
        char[] chars = str.toCharArray();
        Set<Character> set = new HashSet<>();
        for (char c : chars) {
            set.add(c);
        }
        List<Character> list = new ArrayList<>(set);
        Collections.sort(list); // ensure deterministic ordering
        StringBuilder sb = new StringBuilder();
        for (char c : list) {
            sb.append(c);
        }
        return sb.toString(); // this string uniquely represents the set
    }

}
