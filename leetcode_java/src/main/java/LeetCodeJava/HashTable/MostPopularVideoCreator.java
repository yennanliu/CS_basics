package LeetCodeJava.HashTable;

// https://leetcode.com/problems/most-popular-video-creator/description/

import java.util.*;

/**
 * 2456. Most Popular Video Creator
 * Medium
 *
 * You are given two string arrays creators and ids, and an integer array views, all of length n. The ith video on a platform was created by creators[i], has an id of ids[i], and has views[i] views.
 *
 * The popularity of a creator is the sum of the number of views on all of the creator's videos. Find the creator with the highest popularity and the id of their most viewed video.
 *
 * If multiple creators have the highest popularity, find all of them.
 * If multiple videos have the highest view count for a creator, find the lexicographically smallest id.
 *
 * Note: It is possible for different videos to have the same id, meaning that ids do not uniquely identify a video. For example, two videos with the same ID are considered as distinct videos with their own viewcount.
 *
 * Return a 2D array of strings answer where answer[i] = [creators_i, id_i] means that creators_i has the highest popularity and id_i is the id of their most popular video. The answer can be returned in any order.
 *
 *
 * Example 1:
 *
 * Input: creators = ["alice","bob","alice","chris"], ids = ["one","two","three","four"], views = [5,10,5,4]
 * Output: [["alice","one"],["bob","two"]]
 * Explanation:
 * The popularity of alice is 5 + 5 = 10.
 * The popularity of bob is 10.
 * The popularity of chris is 4.
 * alice and bob are the most popular creators.
 * For bob, the video with the highest view count is "two".
 * For alice, the videos with the highest view count are "one" and "three". Since "one" is lexicographically smaller than "three", it is included in the answer.
 *
 * Example 2:
 *
 * Input: creators = ["alice","alice","alice"], ids = ["a","b","c"], views = [1,2,2]
 * Output: [["alice","b"]]
 * Explanation:
 * The videos with id "b" and "c" have the highest view count.
 * Since "b" is lexicographically smaller than "c", it is included in the answer.
 *
 *
 * Constraints:
 *
 * n == creators.length == ids.length == views.length
 * 1 <= n <= 10^5
 * 1 <= creators[i].length, ids[i].length <= 5
 * creators[i] and ids[i] consist only of lowercase English letters.
 * 0 <= views[i] <= 10^5
 *
 */
public class MostPopularVideoCreator {

    // V0
    // IDEA: HASH MAP BOOKKEEPING PER CREATOR (total views + best video), THEN PICK MAX TOTAL
    /**
     * time = O(N)
     * space = O(N)
     */
    public List<List<String>> mostPopularCreator(String[] creators, String[] ids, int[] views) {
        // total views can reach 10^5 * 10^5, so it needs a long
        Map<String, Long> totalViews = new HashMap<>();
        // creator -> index of their best video so far
        Map<String, Integer> bestVideo = new HashMap<>();

        for (int i = 0; i < creators.length; i++) {
            String creator = creators[i];
            totalViews.put(creator, totalViews.getOrDefault(creator, 0L) + views[i]);

            if (!bestVideo.containsKey(creator)) {
                bestVideo.put(creator, i);
            } else {
                int best = bestVideo.get(creator);
                // more views wins; on a tie the lexicographically smaller id wins
                boolean moreViews = views[i] > views[best];
                boolean sameViewsSmallerId = views[i] == views[best] && ids[i].compareTo(ids[best]) < 0;
                if (moreViews || sameViewsSmallerId) {
                    bestVideo.put(creator, i);
                }
            }
        }

        long topTotal = 0;
        for (long total : totalViews.values()) {
            topTotal = Math.max(topTotal, total);
        }

        // every creator reaching the top total is part of the answer
        List<List<String>> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : totalViews.entrySet()) {
            if (entry.getValue() == topTotal) {
                String creator = entry.getKey();
                result.add(Arrays.asList(creator, ids[bestVideo.get(creator)]));
            }
        }
        return result;
    }
}
