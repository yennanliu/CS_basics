package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-pairs-of-equal-substrings-with-minimum-difference/description/
// https://leetcode.ca/2021-05-16-1794-Count-Pairs-of-Equal-Substrings-With-Minimum-Difference/
// https://leetcode.ca/2020-10-28-1794-Count-Pairs-of-Equal-Substrings-With-Minimum-Difference/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *  1794 - Count Pairs of Equal Substrings With Minimum Difference
 * Posted on May 16, 2021 · 4 minute read
 * Welcome to Subscribe On Youtube
 *
 * Formatted question description: https://leetcode.ca/all/1794.html
 *
 * 1794. Count Pairs of Equal Substrings With Minimum Difference
 * Level
 * Medium
 *
 * Description
 * You are given two strings firstString and secondString that are 0-indexed and consist only of lowercase English letters. Count the number of index quadruples (i,j,a,b) that satisfy the following conditions:
 *
 * 0 <= i <= j < firstString.length
 * 0 <= a <= b < secondString.length
 * The substring of firstString that starts at the i-th character and ends at the j-th character (inclusive) is equal to the substring of secondString that starts at the a-th character and ends at the b-th character (inclusive).
 * j - a is the minimum possible value among all quadruples that satisfy the previous conditions.
 * Return the number of such quadruples.
 *
 * Example 1:
 *
 * Input: firstString = “abcd”, secondString = “bccda”
 *
 * Output: 1
 *
 * Explanation: The quadruple (0,0,4,4) is the only one that satisfies all the conditions and minimizes j - a.
 *
 * Example 2:
 *
 * Input: firstString = “ab”, secondString = “cd”
 *
 * Output: 0
 *
 * Explanation: There are no quadruples satisfying all the conditions.
 *
 * Constraints:
 *
 * 1 <= firstString.length, secondString.length <= 2 * 10^5
 * Both strings consist only of lowercase English letters.
 *
 *
 */
public class CountPairsOfEqualSubstringsWithMinimumDifference {

    // V0
//    public int countQuadruples(String firstString, String secondString) {
//    }


    // V1-1
    // IDEA: (GPT)
    // TODO: validate
    /** NOTE !!! CORE IDEA
     *
     * The key observation is that the optimal
     * quadruples always come from single-character substrings,
     *
     * so for each character we only need:
     *
     * its earliest occurrence in firstString
     * its latest occurrence in secondString
     *
     * Then find the minimum i - j and count how many characters achieve it.
     *
     */
    public int countQuadruples_1_1(String firstString, String secondString) {
        int[] last = new int[26];

        /** NOTE !!!
         *
         *
         *  store `last` occurrence in secondString
         *
         *
         *   if last[x] = 0
         *
         *   -> such character is NOT in secondString
         */
        // store last occurrence in secondString (+1 so 0 means "not found")
        for (int j = 0; j < secondString.length(); j++) {
            last[secondString.charAt(j) - 'a'] = j + 1;
        }

        int minDiff = Integer.MAX_VALUE;
        int count = 0;

        for (int i = 0; i < firstString.length(); i++) {
            int j = last[firstString.charAt(i) - 'a'];

            /** NOTE !!!
             *
             *  j > 0: the character is in secondString
             *
             *
             *  ----------
             *
             *   if last[x] = 0
             *
             *   -> such character is NOT in secondString
             */
            if (j > 0) {
                int diff = i - j; // j is stored as index+1

                if (diff < minDiff) {
                    minDiff = diff;
                    count = 1;
                } else if (diff == minDiff) {
                    count++;
                }
            }
        }

        return count;
    }


    // V1-2
    // IDEA: BRUTE FORCE (GPT)
    // TLE
    // TODO: VALIDATE
    public int countQuadruples_1_2(String firstString, String secondString) {

        int minDiff = Integer.MAX_VALUE;
        int count = 0;

        for (int i = 0; i < firstString.length(); i++) {
            for (int j = 0; j < secondString.length(); j++) {
                if (firstString.charAt(i) == secondString.charAt(j)) {
                    int diff = i - j;

                    if (diff < minDiff) {
                        minDiff = diff;
                        count = 1;
                    } else if (diff == minDiff) {
                        count++;
                    }
                }
            }
        }

        return count;
    }


    // V1-3
    // IDEA: (GEMINI)
    public int countQuadruples_1_3(String firstString, String secondString) {
        // Arrays to store the last seen index of each character ('a' to 'z')
        int[] lastIdxSecond = new int[26];
        Arrays.fill(lastIdxSecond, -1);

        // Step 1: Record the LAST occurrence of each character in secondString
        for (int j = 0; j < secondString.length(); j++) {
            lastIdxSecond[secondString.charAt(j) - 'a'] = j;
        }

        int minDiff = Integer.MAX_VALUE;
        int count = 0;

        // Step 2: Scan firstString to find the FIRST occurrence of each character
        // tracking array ensures we only evaluate the absolute first occurrence of each character
        boolean[] visitedFirst = new boolean[26];

        for (int i = 0; i < firstString.length(); i++) {
            char ch = firstString.charAt(i);
            int charIdx = ch - 'a';

            // If we've already processed this character in firstString, skip it
            // (because subsequent occurrences would only increase 'i', making i - j larger)
            if (visitedFirst[charIdx]) {
                continue;
            }
            visitedFirst[charIdx] = true;

            // If this character also exists in secondString, calculate the difference
            if (lastIdxSecond[charIdx] != -1) {
                int j = lastIdxSecond[charIdx];
                int currentDiff = i - j;

                // If we found a `strictly smaller` distance,
                // update `minDif`f and `reset` count
                if (currentDiff < minDiff) {
                    minDiff = currentDiff;
                    count = 1;
                }
                // If it matches our current minimum distance,
                // increment the counter
                else if (currentDiff == minDiff) {
                    count++;
                }
            }
        }

        return count;
    }



    // V2-1
    // IDEA:
    // https://leetcode.ca/2021-05-16-1794-Count-Pairs-of-Equal-Substrings-With-Minimum-Difference/
    public int countQuadruples_2_1(String firstString, String secondString) {
        Map<Character, Integer> map1 = new HashMap<Character, Integer>();
        Map<Character, Integer> map2 = new HashMap<Character, Integer>();
        int length1 = firstString.length(), length2 = secondString.length();
        for (int i = length1 - 1; i >= 0; i--) {
            char c = firstString.charAt(i);
            map1.put(c, i);
        }
        for (int i = 0; i < length2; i++) {
            char c = secondString.charAt(i);
            map2.put(c, i);
        }
        int minDifference = Integer.MAX_VALUE;
        Set<Character> keySet = map1.keySet();
        for (char c : keySet) {
            int index1 = map1.get(c);
            if (map2.containsKey(c)) {
                int index2 = map2.get(c);
                int difference = index1 - index2;
                minDifference = Math.min(minDifference, difference);
            }
        }
        if (minDifference == Integer.MAX_VALUE)
            return 0;
        int quadruples = 0;
        for (char c : keySet) {
            int index1 = map1.get(c);
            if (map2.containsKey(c)) {
                int index2 = map2.get(c);
                int difference = index1 - index2;
                if (difference == minDifference)
                    quadruples++;
            }
        }
        return quadruples;
    }


    // V2-2
    // IDEA:
    // https://leetcode.ca/2021-05-16-1794-Count-Pairs-of-Equal-Substrings-With-Minimum-Difference/
    public int countQuadruples_2_2(String firstString, String secondString) {
        int[] last = new int[26];
        for (int i = 0; i < secondString.length(); ++i) {
            last[secondString.charAt(i) - 'a'] = i + 1;
        }
        int ans = 0, mi = 1 << 30;
        for (int i = 0; i < firstString.length(); ++i) {
            int j = last[firstString.charAt(i) - 'a'];
            if (j > 0) {
                int t = i - j;
                if (mi > t) {
                    mi = t;
                    ans = 1;
                } else if (mi == t) {
                    ++ans;
                }
            }
        }
        return ans;
    }






}
