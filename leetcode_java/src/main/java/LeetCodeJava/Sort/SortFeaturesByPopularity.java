package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-features-by-popularity/

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  1772. Sort Features by Popularity
 *  Medium
 *
 *  You are given a string array features where features[i] is a single word that
 *  represents the name of a feature of the latest product you are working on.
 *  You have made a survey where users have reported which features they like.
 *  You are given a string array responses, where each responses[i] is a string
 *  containing space-separated words.
 *
 *  The popularity of a feature is the number of responses[i] that contain the
 *  feature. You want to sort the features in non-increasing order by their
 *  popularity. If two features have the same popularity, order them by their
 *  original index in features. Notice that one response could contain the same
 *  feature multiple times; this feature is only counted once in its popularity.
 *
 *  Return the features in sorted order.
 *
 *  Example 1:
 *    Input: features = ["cooler","lock","touch"],
 *           responses = ["i like cooler cooler","lock touch cool","locker like touch"]
 *    Output: ["touch","cooler","lock"]
 *
 *  Example 2:
 *    Input: features = ["a","aa","b","c"],
 *           responses = ["a","a aa","a a a a a","b a"]
 *    Output: ["a","aa","b","c"]
 *
 *  Constraints:
 *    1 <= features.length <= 10^4
 *    1 <= features[i].length <= 10
 *    features contains no duplicates.
 *    1 <= responses.length <= 10^2
 *    1 <= responses[i].length <= 10^3
 *    responses[i] contains no two consecutive spaces, no leading/trailing spaces.
 */
public class SortFeaturesByPopularity {

    // V0
    // IDEA: DE-DUPED COUNTING PER RESPONSE + STABLE SORT
    //       popularity(f) = number of responses whose word SET contains f, so
    //       each response is de-duplicated (HashSet) before counting.
    //       then sort features by popularity DESC only. Arrays.sort on an object
    //       array is TimSort, which is STABLE, so equal-popularity features keep
    //       their original relative order — exactly the required tie-break, no
    //       index tiebreaker needed.
    /**
     * time = O(L + n log n)   // L = total length of responses, n = features.length
     * space = O(L + n)
     */
    public String[] sortFeatures(String[] features, String[] responses) {
        Map<String, Integer> cnt = new HashMap<>();
        for (String r : responses) {
            Set<String> seen = new HashSet<>(Arrays.asList(r.split(" ")));
            for (String w : seen) {
                cnt.put(w, cnt.getOrDefault(w, 0) + 1);
            }
        }

        String[] res = features.clone();
        Arrays.sort(res, (a, b) -> {
            int ca = cnt.getOrDefault(a, 0);
            int cb = cnt.getOrDefault(b, 0);
            return Integer.compare(cb, ca);   // popularity DESC; ties -> stable
        });
        return res;
    }
}
