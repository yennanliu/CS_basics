package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/count-of-substrings-containing-every-vowel-and-k-consonants-ii/

/**
 *  3306. Count of Substrings Containing Every Vowel and K Consonants II
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
 *
 *  Example 3:
 *    Input: word = "ieaouqqieaouqq", k = 1
 *    Output: 3
 *
 *  Constraints:
 *    5 <= word.length <= 2 * 10^5
 *    word consists only of lowercase English letters.
 *    0 <= k <= word.length - 5
 */
public class CountOfSubstringsContainingEveryVowelAndKConsonantsII {

    // V0
    // IDEA: SAME "AT LEAST k MINUS AT LEAST k+1" TRICK, AT 2*10^5 SCALE
    //       the exact-consonant requirement is not monotone, but "at least k"
    //       is, so the answer is the difference of two sliding-window counts.
    //       each pass extends the right end, shrinks the left while the window
    //       still holds all five vowels and enough consonants, and adds `left`
    //       — the number of valid starts for that right end.
    //       this is LC 3305 with a larger input, so the count needs long.
    /**
     * time = O(N)
     * space = O(1)
     */
    public long countOfSubstrings(String word, int k) {
        return atLeast(word, k) - atLeast(word, k + 1);
    }

    private long atLeast(String word, int need) {
        int[] cnt = new int[5];
        int distinct = 0;
        int cons = 0;
        int left = 0;
        long res = 0L;
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
            res += left;
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
