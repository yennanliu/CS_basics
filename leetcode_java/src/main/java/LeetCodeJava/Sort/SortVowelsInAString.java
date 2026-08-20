package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-vowels-in-a-string/

import java.util.Arrays;

/**
 *  2785. Sort Vowels in a String
 *  Medium
 *
 *  Given a 0-indexed string s, permute s to get a new string t such that:
 *    - All consonants remain in their original places. More formally, if there
 *      is an index i with 0 <= i < s.length such that s[i] is a consonant, then
 *      t[i] = s[i].
 *    - The vowels must be sorted in the nondecreasing order of their ASCII
 *      values.
 *
 *  Return the resulting string.
 *
 *  The vowels are 'a', 'e', 'i', 'o' and 'u', and they can appear in lowercase
 *  or uppercase. Consonants comprise all letters that are not vowels.
 *
 *  Example 1:
 *    Input: s = "lEetcOde"
 *    Output: "lEOtcede"
 *    Explanation: 'E', 'O', 'e' are the vowels; they get sorted by ASCII value
 *                 while the consonants stay put.
 *
 *  Example 2:
 *    Input: s = "lYmpH"
 *    Output: "lYmpH"
 *    Explanation: there are no vowels in s.
 *
 *  Constraints:
 *    1 <= s.length <= 10^5
 *    s consists only of letters of the English alphabet in uppercase and
 *    lowercase.
 */
public class SortVowelsInAString {

    // V0
    // IDEA: EXTRACT -> SORT -> RE-INSERT
    //       consonants are frozen, so the vowel POSITIONS are fixed too. Pull
    //       every vowel out (keeping its case), sort that list, then walk the
    //       string again dropping the sorted vowels back into the vowel slots.
    //
    //       NOTE: sorting is by ASCII value, NOT case-insensitive, so every
    //             uppercase vowel ('A'=65 .. 'U'=85) sorts BEFORE any lowercase
    //             one ('a'=97 ..) — plain char sort already gives that.
    //       NOTE: the vowel TEST is case-insensitive ("aeiouAEIOU") even though
    //             the ordering is not; keeping those two apart is the trick.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public String sortVowels(String s) {
        char[] out = s.toCharArray();

        int m = 0;
        for (char c : out) {
            if (isVowel(c)) {
                m++;
            }
        }

        char[] picked = new char[m];
        int p = 0;
        for (char c : out) {
            if (isVowel(c)) {
                picked[p++] = c;
            }
        }
        Arrays.sort(picked);

        int j = 0;
        for (int i = 0; i < out.length; i++) {
            if (isVowel(out[i])) {
                out[i] = picked[j++];
            }
        }
        return new String(out);
    }

    private boolean isVowel(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
                || c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
    }
}
