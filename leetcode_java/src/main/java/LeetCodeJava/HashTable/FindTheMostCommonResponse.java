package LeetCodeJava.HashTable;

// https://leetcode.com/problems/find-the-most-common-response/description/

import java.util.*;

/**
 * 3527. Find the Most Common Response
 * Medium
 *
 * You are given a 2D string array responses where each responses[i] is an array of
 * strings representing survey responses from the i^th day.
 *
 * Return the most common response across all days after removing duplicate
 * responses within each responses[i]. If there is a tie, return the
 * lexicographically smallest response.
 *
 * Example 1:
 *
 * Input: responses =
 * [["good","ok","good","ok"],["ok","bad","good","ok","ok"],["good"],["bad"]]
 *
 * Output: "good"
 *
 * Explanation:
 *
 * After removing duplicates within each list, responses = [["good", "ok"], ["ok",
 * "bad", "good"], ["good"], ["bad"]].
 *
 * "good" appears 3 times, "ok" appears 2 times, and "bad" appears 2 times.
 *
 * Return "good" because it has the highest frequency.
 *
 * Example 2:
 *
 * Input: responses =
 * [["good","ok","good"],["ok","bad"],["bad","notsure"],["great","good"]]
 *
 * Output: "bad"
 *
 * Explanation:
 *
 * After removing duplicates within each list we have responses = [["good", "ok"],
 * ["ok", "bad"], ["bad", "notsure"], ["great", "good"]].
 *
 * "bad", "good", and "ok" each occur 2 times.
 *
 * The output is "bad" because it is the lexicographically smallest amongst the
 * words with the highest frequency.
 *
 * Constraints:
 *
 * 1 <= responses.length <= 1000
 *
 * 1 <= responses[i].length <= 1000
 *
 * 1 <= responses[i][j].length <= 10
 *
 * responses[i][j] consists of only lowercase English letters
 *
 */
public class FindTheMostCommonResponse {

    // V0
    // IDEA: DEDUPLICATE PER DAY, THEN ARGMAX WITH A LEXICOGRAPHIC TIE-BREAK
    //   a repeated answer inside one day must not inflate its count, so each
    //   day's list is collapsed to a set first. one pass then keeps the winner,
    //   replacing it on a strictly larger count or an equal count with a
    //   lexicographically smaller string.
    /**
     * time = O(T * L)   (T = total responses, L = response length)
     * space = O(T * L)
     */
    public String findCommonResponse(List<List<String>> responses) {
        Map<String, Integer> dayCount = new HashMap<>();
        for (List<String> day : responses) {
            Set<String> uniqueToday = new HashSet<>(day);
            for (String response : uniqueToday) {
                dayCount.put(response, dayCount.getOrDefault(response, 0) + 1);
            }
        }

        String best = null;
        int bestCount = -1;
        for (Map.Entry<String, Integer> entry : dayCount.entrySet()) {
            String response = entry.getKey();
            int count = entry.getValue();
            boolean moreFrequent = count > bestCount;
            boolean tieButSmaller = count == bestCount && response.compareTo(best) < 0;
            if (moreFrequent || tieButSmaller) {
                best = response;
                bestCount = count;
            }
        }
        return best;
    }
}
