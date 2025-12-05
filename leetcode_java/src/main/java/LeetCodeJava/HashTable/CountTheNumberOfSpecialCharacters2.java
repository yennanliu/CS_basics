package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-the-number-of-special-characters-ii/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  3121. Count the Number of Special Characters II
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a string word. A letter c is called special if it appears both in lowercase and uppercase in word, and every lowercase occurrence of c appears before the first uppercase occurrence of c.
 *
 * Return the number of special letters in word.
 *
 *
 *
 * Example 1:
 *
 * Input: word = "aaAbcBC"
 *
 * Output: 3
 *
 * Explanation:
 *
 * The special characters are 'a', 'b', and 'c'.
 *
 * Example 2:
 *
 * Input: word = "abc"
 *
 * Output: 0
 *
 * Explanation:
 *
 * There are no special characters in word.
 *
 * Example 3:
 *
 * Input: word = "AbBCab"
 *
 * Output: 0
 *
 * Explanation:
 *
 * There are no special characters in word.
 *
 *
 *
 * Constraints:
 *
 * 1 <= word.length <= 2 * 105
 * word consists of only lowercase and uppercase English letters.
 *
 */
public class CountTheNumberOfSpecialCharacters2 {

    // V0
//    public int numberOfSpecialChars(String word) {
//
//    }

    // V0-1
    // IDEA: HASHMAP (fixed by gemini)
    /**
     * Counts the number of letters where ALL lowercase occurrences appear BEFORE
     * the FIRST uppercase occurrence.
     * * Time Complexity: O(N)
     * Space Complexity: O(N)
     */
    public int numberOfSpecialChars_0_1(String word) {
        if (word == null || word.isEmpty()) {
            return 0;
        }

        // Map to store indices of all occurrences.
        // Key: The canonical lowercase character ('a' for both 'a' and 'A')
        // Value: Integer[2] array: [Index of last lowercase, Index of first uppercase]
        // We use -1 for indices that haven't been found yet.
        // We can use a map to store pairs of indices: Map<Character, int[]>
        Map<Character, int[]> indexMap = new HashMap<>();

        // 1. Traverse the word and record indices (O(N))
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            char canonicalKey = Character.toLowerCase(ch);

            // Get or initialize the index array for the canonical letter
            indexMap.putIfAbsent(canonicalKey, new int[] { -1, -1 });
            int[] indices = indexMap.get(canonicalKey);

            if (ch >= 'a' && ch <= 'z') {
                // Update the last seen index for the lowercase character (index 0)
                indices[0] = i;
            } else { // Uppercase
                // Only set the first seen index for the uppercase character (index 1)
                // If it's -1, it means we haven't seen it yet.
                if (indices[1] == -1) {
                    indices[1] = i;
                }
            }
        }

        // 2. Validate and Count Special Characters (O(1) - max 26 iterations)
        int specialCount = 0;

        for (int[] indices : indexMap.values()) {
            int lastLowerIdx = indices[0];
            int firstUpperIdx = indices[1];

            // Condition 1: Must appear as both lower (lastLowerIdx != -1)
            //              AND upper (firstUpperIdx != -1).
            if (lastLowerIdx != -1 && firstUpperIdx != -1) {

                // Condition 2: ALL lowercase occurrences must appear BEFORE the first uppercase occurrence.
                if (lastLowerIdx < firstUpperIdx) {
                    specialCount++;
                }
            }
        }

        return specialCount;
    }


    // V0-2
    // IDEA: HASHMAP (fixed by gpt)
    public int numberOfSpecialChars_0_2(String word) {
        if (word == null || word.isEmpty())
            return 0;
        if (word.length() == 1)
            return 0;

        // Track the *last* lowercase and *first* uppercase index for each base letter.
        // lower[i] = last index of 'a' + i (lowercase)
        // upper[i] = first index of 'A' + i (uppercase)
        int[] lower = new int[26];
        int[] upper = new int[26];

        Arrays.fill(lower, -1); // not seen
        Arrays.fill(upper, Integer.MAX_VALUE); // not seen

        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (Character.isLowerCase(ch)) {
                int idx = ch - 'a';
                lower[idx] = i; // update last lowercase index
            } else {
                int idx = Character.toLowerCase(ch) - 'a';
                upper[idx] = Math.min(upper[idx], i); // update first uppercase index
            }
        }

        int cnt = 0;

        for (int i = 0; i < 26; i++) {
            // Must appear both lowercase and uppercase
            if (lower[i] != -1 && upper[i] != Integer.MAX_VALUE) {
                // Rule: ALL lowercase before ANY uppercase
                if (lower[i] < upper[i]) {
                    cnt++;
                }
            }
        }

        return cnt;
    }


    // V1-1
    // IDEA:
    // https://leetcode.com/problems/count-the-number-of-special-characters-ii/solutions/5052474/two-flags-by-votrubac-l0zs/
    public int numberOfSpecialChars_1_1(String word) {
        boolean[] lo = new boolean[26], up = new boolean[26];
        int res = 0;
        for (char ch : word.toCharArray())
            if (Character.isLowerCase(ch))
                lo[ch - 'a'] = !up[ch - 'a'];
            else
                up[ch - 'A'] = true;
        for (int i = 0; i < 26; ++i)
            res += lo[i] && up[i] ? 1 : 0;
        return res;
    }


    // V2
    // https://leetcode.com/problems/count-the-number-of-special-characters-ii/solutions/5052488/simple-java-solution-by-siddhant_1602-1ote/
    public int numberOfSpecialChars_2(String word) {
        int a[][] = new int[26][2];
        int b[][] = new int[26][2];
        for (int i = 0; i < 26; i++) {
            b[i][1] = -1;
        }
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (c >= 'a' && c <= 'z') {
                a[c - 'a'][0]++;
                a[c - 'a'][1] = i;
            } else {
                b[c - 'A'][0]++;
                if (b[c - 'A'][1] == -1) {
                    b[c - 'A'][1] = i;
                }
            }
        }
        int count = 0;
        for (int i = 0; i < 26; i++) {
            if (a[i][0] != 0 && b[i][0] != 0 && a[i][1] < b[i][1]) {
                count++;
            }
        }
        return count;
    }

    // V3
    // IDEA: HASHMAP
    // https://leetcode.com/problems/count-the-number-of-special-characters-ii/solutions/5052518/java-2-maps-get-last-lowercase-and-first-ikc8/
    public int numberOfSpecialChars_3(String word) {
        Map<Character, Integer> firstUpperCasePos = new HashMap<>();
        Map<Character, Integer> lastLowerCasePos = new HashMap<>();

        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (Character.isLowerCase(ch)) {
                lastLowerCasePos.put(ch, i);
            } else if (Character.isUpperCase(ch)) {
                char lowerCh = Character.toLowerCase(ch);
                if (!firstUpperCasePos.containsKey(lowerCh)) {
                    firstUpperCasePos.put(lowerCh, i);
                }
            }
        }
        int specialCharsCount = 0;
        for (char ch : lastLowerCasePos.keySet()) {
            if (firstUpperCasePos.containsKey(ch)) {
                if (lastLowerCasePos.get(ch) < firstUpperCasePos.get(ch)) {
                    specialCharsCount++;
                }
            }
        }
        return specialCharsCount;
    }


    // V4
    // https://leetcode.com/problems/count-the-number-of-special-characters-ii/solutions/6979560/simple-solution-without-using-hash-map-b-gz0m/
    public int numberOfSpecialChars_4(String word) {

        int u[][] = new int[26][2];
        int l[][] = new int[26][2];
        int ocu = 0;

        for (int i = 0; i < u.length; i++) {
            l[i][1] = -1;
            u[i][1] = -1;

        }

        for (char ch : word.toCharArray()) {
            if (Character.isLowerCase(ch)) {
                l[ch - 'a'][0]++;
                if (l[ch - 'a'][1] == -1 || l[ch - 'a'][1] > -1)
                    l[ch - 'a'][1] = ocu;
            } else {
                u[ch - 'A'][0]++;
                if (u[ch - 'A'][1] == -1)
                    u[ch - 'A'][1] = ocu;
            }

            ocu++;

        }

        // for(int i=0;i<u.length;i++)
        // {
        //     System.err.print( (char)( i+'a' ) +":");
        //     System.out.print(l[i][0]+":");
        //     System.out.print(l[i][1]+"-->");
        // }
        // System.out.println();
        // for(int i=0;i<u.length;i++)
        // {
        //     System.err.print( (char)( i+'A' ) +":");
        //     System.out.print(u[i][0]+":");
        //     System.out.print(u[i][1]+"-->");
        // }

        int c = 0;

        for (int i = 0; i < 26; i++) {
            if (u[i][0] != 0 && l[i][0] != 0 && l[i][1] < u[i][1]) {

                c++;

            }

        }

        return c;
    }



}
