package LeetCodeJava.HashTable;

// https://leetcode.com/problems/rearrange-characters-to-make-target-string/description/

/**
 * 2287. Rearrange Characters to Make Target String
 * Easy
 *
 * You are given two 0-indexed strings s and target. You can take some letters from s and rearrange them to form new strings.
 *
 * Return the maximum number of copies of target that can be formed by taking letters from s and rearranging them.
 *
 *
 * Example 1:
 *
 * Input: s = "ilovecodingonleetcode", target = "code"
 * Output: 2
 * Explanation:
 * For the first copy of "code", take the letters at indices 4, 5, 6, and 7.
 * For the second copy of "code", take the letters at indices 17, 18, 19, and 20.
 * The strings that are formed are "ecod" and "code" which can both be rearranged into "code".
 * We can make at most two copies of "code", so we return 2.
 *
 * Example 2:
 *
 * Input: s = "abcba", target = "abc"
 * Output: 1
 * Explanation:
 * We can make one copy of "abc" by taking the letters at indices 0, 1, and 2.
 * We can make at most one copy of "abc", so we return 1.
 * Note that while there is an extra 'a' and 'b' at indices 3 and 4, we cannot rearrange them to form "abc" a second time.
 *
 * Example 3:
 *
 * Input: s = "abbaccaddaeea", target = "aaaaa"
 * Output: 1
 * Explanation:
 * We can make one copy of "aaaaa" by taking the letters at indices 0, 3, 6, 9, and 12.
 * We can make at most one copy of "aaaaa", so we return 1.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * 1 <= target.length <= 100
 * s and target consist of lowercase English letters.
 *
 */
public class RearrangeCharactersToMakeTargetString {

    // V0
    // IDEA: CHARACTER COUNTS - THE BOTTLENECK LETTER DECIDES (MIN OF have / need)
    //   one copy of target consumes need[c] of each letter c, so the number of copies
    //   is the minimum of have[c] / need[c] over the letters of target.
    /**
     * time = O(S + T)
     * space = O(1)
     */
    public int rearrangeCharacters(String s, String target) {
        int[] have = new int[26];
        for (int i = 0; i < s.length(); i++) {
            have[s.charAt(i) - 'a']++;
        }
        int[] need = new int[26];
        for (int i = 0; i < target.length(); i++) {
            need[target.charAt(i) - 'a']++;
        }

        int copies = Integer.MAX_VALUE;
        for (int letter = 0; letter < 26; letter++) {
            if (need[letter] == 0) {
                continue;
            }
            copies = Math.min(copies, have[letter] / need[letter]);
        }
        return copies;
    }
}
