package LeetCodeJava.Design;

// https://leetcode.com/problems/tweet-counts-per-frequency/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *  1348. Tweet Counts Per Frequency
 *  Medium
 *
 *  A social media company is trying to monitor activity on their site by analyzing the number
 *  of tweets that occur in select periods of time. These periods can be partitioned into
 *  smaller time chunks based on a certain frequency (every minute, hour, or day).
 *
 *  For example, the period [10, 10000] (in seconds) would be partitioned into the following
 *  time chunks with these frequencies:
 *
 *   Every minute (60-second chunks): [10,69], [70,129], [130,189], ..., [9970,10000]
 *   Every hour (3600-second chunks): [10,3609], [3610,7209], [7210,10000]
 *   Every day (86400-second chunks): [10,10000]
 *
 *  Notice that the last chunk may be shorter than the specified frequency's chunk size and
 *  will always end with the end time of the period (10000 in the above example).
 *
 *  Implement the TweetCounts class:
 *
 *   - TweetCounts() Initializes the TweetCounts object.
 *   - void recordTweet(String tweetName, int time) Stores the tweetName at the recorded time
 *     (in seconds).
 *   - List<Integer> getTweetCountsPerFrequency(String freq, String tweetName, int startTime,
 *     int endTime) Returns a list of integers representing the number of tweets with
 *     tweetName in each time chunk for the given period of time [startTime, endTime]
 *     (in seconds) and frequency freq. freq is one of "minute", "hour", or "day".
 *
 *  Example 1:
 *
 *  Input
 *  ["TweetCounts","recordTweet","recordTweet","recordTweet",
 *   "getTweetCountsPerFrequency","getTweetCountsPerFrequency","recordTweet",
 *   "getTweetCountsPerFrequency"]
 *  [[],["tweet3",0],["tweet3",60],["tweet3",10],["minute","tweet3",0,59],
 *   ["minute","tweet3",0,60],["tweet3",120],["hour","tweet3",0,210]]
 *  Output
 *  [null,null,null,null,[2],[2,1],null,[4]]
 *
 *  Constraints:
 *
 *   0 <= time, startTime, endTime <= 10^9
 *   0 <= endTime - startTime <= 10^4
 *   There will be at most 10^4 calls in total to recordTweet and
 *   getTweetCountsPerFrequency.
 */
public class TweetCountsPerFrequency {

    // V0
    // IDEA: PER-NAME TreeMap<time, count> + SUBMAP RANGE SCAN PER CHUNK
    //       keep, for every tweetName, a sorted multiset of its timestamps
    //       (TreeMap time -> how many tweets landed on that exact second).
    //       counting a chunk is then a subMap over [lo, hi) -> one tree walk.
    //
    //       chunks are laid out FROM startTime, each `size` seconds wide, and the last one
    //       is truncated at endTime.
    //       NOTE: the period is INCLUSIVE on both ends, so a chunk's right edge is
    //             min(t + size, endTime + 1) -- the +1 makes endTime itself count.
    //       NOTE: endTime - startTime <= 10^4 caps the number of chunks per query, so the
    //             per-query cost stays small even for "minute".
    /**
     * time = O(log N) per recordTweet, O(C * log N + K) per query
     *        (C = number of chunks, K = timestamps actually visited)
     * space = O(N)
     */
    private final Map<String, TreeMap<Integer, Integer>> tweets;

    public TweetCountsPerFrequency() {
        this.tweets = new HashMap<>();
    }

    public void recordTweet(String tweetName, int time) {
        TreeMap<Integer, Integer> times = tweets.get(tweetName);
        if (times == null) {
            times = new TreeMap<>();
            tweets.put(tweetName, times);
        }
        Integer cnt = times.get(time);
        times.put(time, cnt == null ? 1 : cnt + 1);
    }

    public List<Integer> getTweetCountsPerFrequency(String freq, String tweetName,
                                                   int startTime, int endTime) {
        int size;
        if ("minute".equals(freq)) {
            size = 60;
        } else if ("hour".equals(freq)) {
            size = 3600;
        } else { // "day"
            size = 86400;
        }

        List<Integer> res = new ArrayList<>();
        TreeMap<Integer, Integer> times = tweets.get(tweetName);

        // chunk starts: startTime, startTime + size, ... up to (and including) endTime
        for (long lo = startTime; lo <= endTime; lo += size) {
            if (times == null) {
                res.add(0);
                continue;
            }
            long hi = Math.min(lo + size, (long) endTime + 1); // exclusive right edge
            SortedMap<Integer, Integer> window = times.subMap((int) lo, (int) hi);
            int cnt = 0;
            for (Integer c : window.values()) {
                cnt += c;
            }
            res.add(cnt);
        }
        return res;
    }
}
