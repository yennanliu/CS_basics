package LeetCodeJava.String;

// https://leetcode.com/problems/determine-if-string-halves-are-alike/description/

/**
 * 1704. Determine if String Halves Are Alike
 * Easy
 *
 * You are given a string s of even length. Split this string into two halves of equal lengths, and let a be the first half and b be the second half.
 *
 * Two strings are alike if they have the same number of vowels ('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'). Notice that s contains uppercase and lowercase letters.
 *
 * Return true if a and b are alike. Otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: s = "book"
 * Output: true
 * Explanation: a = "bo" and b = "ok". a has 1 vowel and b has 1 vowel. Therefore, they are alike.
 *
 * Example 2:
 *
 * Input: s = "textbook"
 * Output: false
 * Explanation: a = "text" and b = "book". a has 1 vowel whereas b has 2. Therefore, they are not alike.
 * Notice that the vowel o is counted twice.
 *
 *
 * Constraints:
 *
 * 2 <= s.length <= 1000
 * s.length is even.
 * s consists of uppercase and lowercase letters.
 *
 */
public class DetermineIfStringHalvesAreAlike {

    // V0
    // IDEA: ONE RUNNING BALANCE (+1 FOR A VOWEL IN THE FIRST HALF, -1 IN THE SECOND)
    //       index i belongs to the first half and i + half to the second;
    //       the halves are alike iff the balance ends at 0.
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean halvesAreAlike(String s) {
        int half = s.length() / 2;
        int balance = 0;
        for (int i = 0; i < half; i++) {
            if (isVowel(s.charAt(i))) {
                balance++;
            }
            if (isVowel(s.charAt(i + half))) {
                balance--;
            }
        }
        return balance == 0;
    }

    /** True for a, e, i, o, u in either case. */
    private boolean isVowel(char c) {
        return "aeiouAEIOU".indexOf(c) >= 0;
    }
}
