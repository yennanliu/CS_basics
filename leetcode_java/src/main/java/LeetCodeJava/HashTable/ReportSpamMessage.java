package LeetCodeJava.HashTable;

// https://leetcode.com/problems/report-spam-message/description/

import java.util.*;

/**
 * 3295. Report Spam Message
 * Medium
 *
 * You are given an array of strings message and an array of strings bannedWords.
 *
 * An array of words is considered spam if there are at least two words in it that exactly match any word in bannedWords.
 *
 * Return true if the array message is spam, and false otherwise.
 *
 *
 * Example 1:
 *
 * Input: message = ["hello","world","leetcode"], bannedWords = ["world","hello"]
 * Output: true
 * Explanation:
 * The words "hello" and "world" from the message array both appear in the bannedWords array.
 *
 * Example 2:
 *
 * Input: message = ["hello","programming","fun"], bannedWords = ["world","programming","leetcode"]
 * Output: false
 * Explanation:
 * Only one word from the message array ("programming") appears in the bannedWords array.
 *
 *
 * Constraints:
 *
 * 1 <= message.length, bannedWords.length <= 10^5
 * 1 <= message[i].length, bannedWords[i].length <= 15
 * message[i] and bannedWords[i] consist only of lowercase English letters.
 *
 */
public class ReportSpamMessage {

    // V0
    // IDEA: HASH THE BANNED WORDS, COUNT HITS AND STOP AT TWO
    //   a set makes each membership test O(1); scanning bannedWords per word
    //   would be 10^10 comparisons.
    /**
     * time = O(N + M)
     * space = O(M)
     */
    public boolean reportSpam(String[] message, String[] bannedWords) {
        Set<String> banned = new HashSet<>();
        for (String word : bannedWords) {
            banned.add(word);
        }

        int hits = 0;
        for (String word : message) {
            if (banned.contains(word)) {
                hits++;
                // the answer only needs to know the count reached two
                if (hits == 2) {
                    return true;
                }
            }
        }
        return false;
    }
}
