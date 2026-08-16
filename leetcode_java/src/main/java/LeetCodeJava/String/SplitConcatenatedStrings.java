package LeetCodeJava.String;

// https://leetcode.com/problems/split-concatenated-strings/description/
/**
 * 555. Split Concatenated Strings
 * Medium
 *
 * You are given an array of strings strs. You could concatenate these strings together
 * into a loop, where for each string, you could choose to reverse it or not. Among all
 * the possible loops
 *
 * Return the lexicographically largest string after cutting the loop, which will make
 * the looped string into a regular one.
 *
 * Specifically, to find the lexicographically largest string, you need to experience
 * two phases:
 *
 * 1. Concatenate all the strings into a loop, where you can reverse some strings or not
 *    and connect them in the same order as given.
 * 2. Cut and make one breakpoint in any place of the loop, which will make the looped
 *    string into a regular one starting from the character at the cutpoint.
 *
 * And your job is to find the lexicographically largest one among all the possible
 * regular strings.
 *
 * Example 1:
 *
 * Input: strs = ["abc","xyz"]
 * Output: "zyxcba"
 * Explanation: You can get the looped string "-abcxyz-", "-abczyx-", "-cbaxyz-",
 * "-cbazyx-", where '-' represents the looped status.
 * The answer string came from the fourth looped one, where you could cut from the middle
 * character 'a' and get "zyxcba".
 *
 * Example 2:
 *
 * Input: strs = ["abc"]
 * Output: "cba"
 *
 *
 * Constraints:
 *
 * 1 <= strs.length <= 1000
 * 1 <= strs[i].length <= 1000
 * 1 <= sum(strs[i].length) <= 1000
 * strs[i] consists of lowercase English letters.
 *
 */
public class SplitConcatenatedStrings {

    // V0
    // IDEA: GREEDY
    /**
     *   KEY OBSERVATION: the cut happens INSIDE exactly ONE string strs[i].
     *   For every OTHER string, its orientation is INDEPENDENT of the cut,
     *   so we greedily keep max(s, reversed(s)) -> that is always at least as good.
     *
     *   Then for each i we try BOTH orientations of strs[i] and EVERY cut position j:
     *       cur = cand[j:] + (rest of the loop) + cand[:j]
     *   and keep the largest one.
     *
     *   time  = O(L * (L + n))  // L = total length of all strings, n = strs.length
     *   space = O(L)
     */
    public String splitLoopedString(String[] strs) {
        int n = strs.length;

        // BEST orientation of every string (used for all strings EXCEPT the cut one)
        String[] best = new String[n];
        for (int i = 0; i < n; i++) {
            String rev = new StringBuilder(strs[i]).reverse().toString();
            best[i] = strs[i].compareTo(rev) >= 0 ? strs[i] : rev;
        }

        String res = "";

        for (int i = 0; i < n; i++) {
            // everything AFTER strs[i], wrapping around to just BEFORE strs[i]
            StringBuilder mid = new StringBuilder();
            for (int t = i + 1; t < n; t++) {
                mid.append(best[t]);
            }
            for (int t = 0; t < i; t++) {
                mid.append(best[t]);
            }
            String midStr = mid.toString();

            // the CUT string itself : try BOTH orientations
            String[] cands = { strs[i], new StringBuilder(strs[i]).reverse().toString() };
            for (String cand : cands) {
                for (int j = 0; j < cand.length(); j++) {
                    String cur = cand.substring(j) + midStr + cand.substring(0, j);
                    if (cur.compareTo(res) > 0) {
                        res = cur;
                    }
                }
            }
        }

        return res;
    }

}
