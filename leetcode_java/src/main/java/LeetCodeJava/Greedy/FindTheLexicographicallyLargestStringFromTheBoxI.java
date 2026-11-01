package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-the-lexicographically-largest-string-from-the-box-i/description/

/**
 * 3403. Find the Lexicographically Largest String From the Box I
 * Medium
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
 * 1 <= word.length <= 5 * 10^3
 * word consists only of lowercase English letters.
 * 1 <= numFriends <= word.length
 *
 */
public class FindTheLexicographicallyLargestStringFromTheBoxI {

    // V0
    // IDEA: EVERY SUBSTRING OF LENGTH <= n - numFriends + 1 ENDS UP IN THE BOX
    //       a piece word[i..j] is possible iff the rest can still be cut into
    //       numFriends - 1 non-empty parts, i.e. its length <= cap = n - numFriends + 1.
    //       a longer candidate beats its own prefixes, so only the window of length
    //       cap (or up to the end) at each start matters; take the largest.
    //       numFriends == 1: the only split is the whole word.
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public String answerString(String word, int numFriends) {
        if (numFriends == 1) {
            return word;
        }
        int n = word.length();
        int cap = n - numFriends + 1;

        String best = "";
        for (int i = 0; i < n; i++) {
            String candidate = word.substring(i, Math.min(n, i + cap));
            if (candidate.compareTo(best) > 0) {
                best = candidate;
            }
        }
        return best;
    }
}
