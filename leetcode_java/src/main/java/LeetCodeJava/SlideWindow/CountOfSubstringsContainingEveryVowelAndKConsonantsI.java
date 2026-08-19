package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/count-of-substrings-containing-every-vowel-and-k-consonants-i/

/**
 *  3305. Count of Substrings Containing Every Vowel and K Consonants I
 *  Medium
 *
 *  You are given a string word and a non-negative integer k.
 *
 *  Return the total number of substrings of word that contain every vowel
 *  ('a', 'e', 'i', 'o', and 'u') at least once and exactly k consonants.
 *
 *  Example 1:
 *    Input: word = "aeioqq", k = 1
 *    Output: 0
 *    Explanation: There is no substring with every vowel.
 *
 *  Example 2:
 *    Input: word = "aeiou", k = 0
 *    Output: 1
 *    Explanation: The only substring with every vowel and zero consonants is
 *                 word[0..4], which is "aeiou".
 *
 *  Example 3:
 *    Input: word = "ieaouqqieaouqq", k = 1
 *    Output: 3
 *
 *  Constraints:
 *    5 <= word.length <= 250
 *    word consists only of lowercase English letters.
 *    0 <= k <= word.length - 5
 */
public class CountOfSubstringsContainingEveryVowelAndKConsonantsI {

    // V0
    // IDEA: "EXACTLY k" = "AT LEAST k" MINUS "AT LEAST k+1"
    //       an exact-count condition is awkward for a sliding window, but an
    //       at-least condition is monotone: growing a substring never loses a
    //       vowel or a consonant. so count the substrings with all five vowels
    //       and at least k consonants, do the same for k+1, and subtract.
    //       for the at-least count, extend the right end and shrink the left
    //       while the window still qualifies; every start before that point
    //       also qualifies, contributing `left` substrings ending here.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countOfSubstrings(String word, int k) {
        return atLeast(word, k) - atLeast(word, k + 1);
    }

    private int atLeast(String word, int need) {
        int[] cnt = new int[5];
        int distinct = 0;
        int cons = 0;
        int left = 0;
        int res = 0;
        for (int right = 0; right < word.length(); right++) {
            int v = vowelIdx(word.charAt(right));
            if (v < 0) {
                cons++;
            } else {
                if (cnt[v] == 0) {
                    distinct++;
                }
                cnt[v]++;
            }
            while (distinct == 5 && cons >= need) {
                int d = vowelIdx(word.charAt(left));
                if (d < 0) {
                    cons--;
                } else {
                    cnt[d]--;
                    if (cnt[d] == 0) {
                        distinct--;
                    }
                }
                left++;
            }
            res += left; // starts 0 .. left-1 all work for this right end
        }
        return res;
    }

    private int vowelIdx(char c) {
        switch (c) {
            case 'a': return 0;
            case 'e': return 1;
            case 'i': return 2;
            case 'o': return 3;
            case 'u': return 4;
            default: return -1;
        }
    }
}
