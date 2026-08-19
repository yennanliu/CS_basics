package LeetCodeJava.Sort;

// https://leetcode.com/problems/analyze-user-website-visit-pattern/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  1152. Analyze User Website Visit Pattern
 *  Medium
 *
 *  You are given two string arrays username and website and an integer array timestamp.
 *  All arrays have the same length and the tuple [username[i], website[i], timestamp[i]]
 *  means user username[i] visited website[i] at time timestamp[i].
 *
 *  A pattern is a list of three websites (not necessarily distinct).
 *  The score of a pattern is the number of users that visited all the websites in the
 *  pattern in the same order they appeared in the pattern.
 *
 *  Return the pattern with the largest score. If there is more than one pattern with the
 *  same largest score, return the lexicographically smallest such pattern.
 *
 *  Example 1:
 *  Input: username = ["joe","joe","joe","james","james","james","james","mary","mary","mary"],
 *         timestamp = [1,2,3,4,5,6,7,8,9,10],
 *         website = ["home","about","career","home","cart","maps","home","home","about","career"]
 *  Output: ["home","about","career"]
 *
 *  Example 2:
 *  Input: username = ["ua","ua","ua","ub","ub","ub"], timestamp = [1,2,3,4,5,6],
 *         website = ["a","b","a","a","b","c"]
 *  Output: ["a","b","a"]
 *
 *  Constraints:
 *  3 <= username.length <= 50
 *  1 <= username[i].length, website[i].length <= 10
 *  timestamp.length == username.length == website.length
 *  1 <= timestamp[i] <= 10^9
 *  username[i] and website[i] consist of lowercase English letters.
 *  At least one user visited at least three websites.
 */
public class AnalyzeUserWebsiteVisitPattern {

    // V0
    // IDEA: sort visits by time -> per-user visit sequence -> enumerate every 3-combination
    //       (deduped per user) and count how many users produced it
    /**
     * time = O(n log n + u * m^3)   // m = visits of a single user, worst case m = n
     * space = O(n + u * m^3)
     */
    public List<String> mostVisitedPattern(String[] username, int[] timestamp, String[] website) {

        int n = username.length;

        // sort the visit indices by timestamp (chronological order matters)
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> Integer.compare(timestamp[a], timestamp[b]));

        // user -> ordered list of visited sites
        Map<String, List<String>> visits = new HashMap<>();
        for (int k = 0; k < n; k++) {
            int i = order[k];
            List<String> sites = visits.get(username[i]);
            if (sites == null) {
                sites = new ArrayList<>();
                visits.put(username[i], sites);
            }
            sites.add(website[i]);
        }

        // pattern (joined by ',') -> number of DISTINCT users having it
        Map<String, Integer> count = new HashMap<>();
        for (List<String> sites : visits.values()) {
            int m = sites.size();
            if (m < 3) {
                continue;
            }
            Set<String> seen = new HashSet<>();
            for (int i = 0; i < m - 2; i++) {
                for (int j = i + 1; j < m - 1; j++) {
                    for (int k = j + 1; k < m; k++) {
                        String key = sites.get(i) + "," + sites.get(j) + "," + sites.get(k);
                        if (seen.add(key)) {
                            Integer c = count.get(key);
                            count.put(key, c == null ? 1 : c + 1);
                        }
                    }
                }
            }
        }

        /**
         *  NOTE: ',' is smaller than every lowercase letter, so comparing the joined
         *  strings is equivalent to comparing the 3-element lists lexicographically.
         */
        String best = null;
        int bestCnt = 0;
        for (Map.Entry<String, Integer> e : count.entrySet()) {
            if (e.getValue() > bestCnt
                    || (e.getValue() == bestCnt && best != null && e.getKey().compareTo(best) < 0)) {
                bestCnt = e.getValue();
                best = e.getKey();
            }
        }

        List<String> res = new ArrayList<>();
        if (best == null) {
            return res;
        }
        String[] parts = best.split(",");
        for (String p : parts) {
            res.add(p);
        }
        return res;
    }
}
