package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-the-lexicographically-largest-string-from-the-box-ii/description/
// https://leetcode.ca/2025-01-19-3406-Find-the-Lexicographically-Largest-String-From-the-Box-II/

/**
 * 3406. Find the Lexicographically Largest String From the Box II
 * Hard
 *
 * You are given a string word, and an integer numFriends.
 *
 * Alice is organizing a game for her numFriends friends. There are multiple rounds
 * in the game, where in each round:
 *
 * word is split into numFriends non-empty strings, such that no previous round has
 * had the exact same split.
 * All the split words are put into a box.
 *
 * Find the lexicographically largest string from the box after all the rounds are
 * finished.
 *
 * A string a is lexicographically smaller than a string b if in the first position
 * where a and b differ, string a has a letter that appears earlier in the alphabet
 * than the corresponding letter in b.
 * If the first min(a.length, b.length) characters do not differ, then the shorter
 * string is the lexicographically smaller one.
 *
 * Example 1:
 *
 * Input: word = "dbca", numFriends = 2
 *
 * Output: "dbc"
 *
 * Explanation:
 *
 * All possible splits are:
 *
 * "d" and "bca".
 * "db" and "ca".
 * "dbc" and "a".
 *
 * Example 2:
 *
 * Input: word = "gggg", numFriends = 4
 *
 * Output: "g"
 *
 * Explanation:
 *
 * The only possible split is: "g", "g", "g", and "g".
 *
 * Constraints:
 *
 * 1 <= word.length <= 2 * 10^5
 * word consists only of lowercase English letters.
 * 1 <= numFriends <= word.length
 *
 */
public class FindTheLexicographicallyLargestStringFromTheBoxII {

    // V0
    // IDEA: MAXIMUM SUFFIX (TWO-CANDIDATE SCAN) THEN TRUNCATE TO cap
    //       the box holds exactly the substrings of length <= cap = n - numFriends + 1.
    //       if word[best:] is the lexicographically largest suffix, then
    //       word[best : best + cap] is the largest substring of length <= cap.
    //       the largest suffix comes from the classic linear scan: champion start i,
    //       challenger start j, matched offset k; on a mismatch the loser and every
    //       start it "covers" are discarded.
    /**
     * time = O(N)
     * space = O(N)   for the returned substring
     */
    public String answerString(String word, int numFriends) {
        if (numFriends == 1) {
            return word;
        }
        int n = word.length();
        int cap = n - numFriends + 1;

        int champion = 0;   // start of the best suffix so far
        int challenger = 1; // start of the suffix being compared against it
        int offset = 0;     // how many chars of the two suffixes already match
        while (challenger + offset < n) {
            char a = word.charAt(champion + offset);
            char b = word.charAt(challenger + offset);
            if (a == b) {
                offset++;
            } else if (a > b) {
                // challenger loses, and so does every start inside its matched part
                challenger = challenger + offset + 1;
                offset = 0;
            } else {
                // champion loses, and so does every start inside its matched part
                champion = Math.max(champion + offset + 1, challenger);
                challenger = champion + 1;
                offset = 0;
            }
        }
        return word.substring(champion, Math.min(n, champion + cap));
    }
}
