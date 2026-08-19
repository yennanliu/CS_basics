package LeetCodeJava.HashTable;

// https://leetcode.com/problems/shortest-completing-word/

/**
 *  748. Shortest Completing Word
 *  Easy
 *
 *  Given a string licensePlate and an array of strings words, find the shortest
 *  completing word in words.
 *
 *  A completing word is a word that contains all the letters in licensePlate.
 *  Ignore numbers and spaces in licensePlate, and treat letters as case insensitive.
 *  If a letter appears more than once in licensePlate, then it must appear at least
 *  that many times in the word.
 *
 *  It is guaranteed an answer exists. If there are multiple shortest completing words,
 *  return the first one that occurs in words.
 *
 *  Example 1:
 *  Input: licensePlate = "1s3 PSt", words = ["step","steps","stripe","stepple"]
 *  Output: "steps"
 *
 *  Example 2:
 *  Input: licensePlate = "1s3 456", words = ["looks","pest","stew","show"]
 *  Output: "pest"
 *
 *  Constraints:
 *  1 <= licensePlate.length <= 7
 *  1 <= words.length <= 1000
 *  1 <= words[i].length <= 15
 */
public class ShortestCompletingWord {

    // V0
    // IDEA: build a 26-slot letter count for the plate, then scan words keeping the shortest match
    /**
     * time = O(n * L)   // n = # words, L = avg word length
     * space = O(1)
     */
    public String shortestCompletingWord(String licensePlate, String[] words) {
        int[] need = count(licensePlate);

        String res = null;
        for (String w : words) {
            if (res != null && w.length() >= res.length()) {
                continue;
            }
            if (covers(count(w), need)) {
                res = w;
            }
        }
        return res;
    }

    private int[] count(String s) {
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c)) {
                cnt[Character.toLowerCase(c) - 'a']++;
            }
        }
        return cnt;
    }

    private boolean covers(int[] have, int[] need) {
        for (int i = 0; i < 26; i++) {
            if (have[i] < need[i]) {
                return false;
            }
        }
        return true;
    }
}
