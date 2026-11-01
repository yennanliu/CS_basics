package LeetCodeJava.String;

// https://leetcode.com/problems/strings-differ-by-one-character/description/
// https://leetcode.ca/2020-03-02-1554-Strings-Differ-by-One-Character/

import java.util.*;

/**
 * 1554. Strings Differ by One Character
 * Medium
 *
 * Given a list of strings dict where all the strings are of the same length.
 *
 * Return true if there are 2 strings that only differ by 1 character in the same index, otherwise return false.
 *
 * Example 1:
 *
 * Input: dict = ["abcd","acbd", "aacd"]
 * Output: true
 * Explanation: Strings "abcd" and "aacd" differ only by one character in the index 1.
 *
 * Example 2:
 *
 * Input: dict = ["ab","cd","yz"]
 * Output: false
 *
 * Example 3:
 *
 * Input: dict = ["abcd","cccc","abyd","abab"]
 * Output: true
 *
 * Constraints:
 *
 * The number of characters in dict <= 10^5
 * dict[i].length == dict[j].length
 * dict[i] should be unique.
 * dict[i] contains only lowercase English letters.
 *
 */
public class StringsDifferByOneCharacter {

    private static final long MOD = 1_000_000_007L;
    private static final long BASE = 131L;

    // V0
    // IDEA: ROLLING HASH WITH ONE POSITION "BLANKED OUT"
    //       two words differ only at index i  <=>  hash(word) - weight(i) * word[i]
    //       is the same for both. For every word and index, bucket the key
    //       (i, maskedHash); a hit is verified char by char, so a hash collision
    //       can never produce a wrong answer.
    /**
     * time = O(N * M)   (expected; N = #words, M = word length)
     * space = O(N * M)
     */
    public boolean differByOne(String[] dict) {
        if (dict == null || dict.length < 2) {
            return false;
        }
        int wordLength = dict[0].length();

        // power[i] = BASE^i % MOD, the weight of the char that sits i places from the end
        long[] power = new long[wordLength];
        power[0] = 1;
        for (int i = 1; i < wordLength; i++) {
            power[i] = power[i - 1] * BASE % MOD;
        }

        // key = index * MOD + maskedHash  ->  indexes of the words that produced it
        Map<Long, List<Integer>> buckets = new HashMap<>();

        for (int w = 0; w < dict.length; w++) {
            String word = dict[w];

            // polynomial hash of the whole word
            long hash = 0;
            for (int i = 0; i < wordLength; i++) {
                hash = (hash * BASE + word.charAt(i)) % MOD;
            }

            for (int i = 0; i < wordLength; i++) {
                // remove word[i]'s contribution: the word with position i blanked out
                long contribution = word.charAt(i) * power[wordLength - 1 - i] % MOD;
                long maskedHash = (hash - contribution + MOD) % MOD;
                long key = (long) i * MOD + maskedHash;

                List<Integer> candidates = buckets.computeIfAbsent(key, x -> new ArrayList<>());
                // same masked hash at the same index: confirm it is a real match
                for (int other : candidates) {
                    if (differsOnlyAt(dict[other], word, i)) {
                        return true;
                    }
                }
                candidates.add(w);
            }
        }
        return false;
    }

    /** True if a and b are equal everywhere except (possibly) at index skip. */
    private boolean differsOnlyAt(String a, String b, int skip) {
        for (int i = 0; i < a.length(); i++) {
            if (i != skip && a.charAt(i) != b.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    // V1
    // IDEA: WILDCARD SET (replace position i with '*' and look the pattern up)
    //       simplest to explain; each pattern costs O(M) to build and hash,
    //       so it is O(N * M^2) -- fine when words are short.
    /**
     * time = O(N * M^2)
     * space = O(N * M^2)
     */
    public boolean differByOne_1(String[] dict) {
        if (dict == null || dict.length < 2) {
            return false;
        }
        Set<String> seenPatterns = new HashSet<>();
        for (String word : dict) {
            char[] chars = word.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char original = chars[i];
                chars[i] = '*';
                String pattern = new String(chars);
                // words are unique, so a repeated pattern comes from a different word
                if (!seenPatterns.add(pattern)) {
                    return true;
                }
                chars[i] = original;
            }
        }
        return false;
    }
}
