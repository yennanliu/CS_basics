package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/longest-well-performing-interval/description/

import java.util.*;

/**
 *  1124. Longest Well-Performing Interval
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * We are given hours, a list of the number of hours worked per day for a given employee.
 *
 * A day is considered to be a tiring day if and only if the number of hours worked is (strictly) greater than 8.
 *
 * A well-performing interval is an interval of days for which the number of tiring days is strictly larger than the number of non-tiring days.
 *
 * Return the length of the longest well-performing interval.
 *
 *
 *
 * Example 1:
 *
 * Input: hours = [9,9,6,0,6,6,9]
 * Output: 3
 * Explanation: The longest well-performing interval is [9,9,6].
 * Example 2:
 *
 * Input: hours = [6,6,6]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= hours.length <= 104
 * 0 <= hours[i] <= 16
 *
 */
public class LongestWellPerformingInterval {

    // V0
//    public int longestWPI(int[] hours) {
//
//    }


    // V0-1
    // IDEA: TREEMAP + SORT (GPT)
    public List<List<Long>> splitPainting_0_1(int[][] segments) {

        // position -> color delta
        TreeMap<Integer, Long> map = new TreeMap<>();

        for (int[] seg : segments) {
            int start = seg[0];
            int end = seg[1];
            int color = seg[2];

            map.put(start, map.getOrDefault(start, 0L) + color);
            map.put(end, map.getOrDefault(end, 0L) - color);
        }

        List<List<Long>> res = new ArrayList<>();

        long colorSum = 0;
        Integer prev = null;

        for (Map.Entry<Integer, Long> entry : map.entrySet()) {

            int curr = entry.getKey();

            // interval [prev, curr)
            if (prev != null && colorSum > 0) {
                res.add(Arrays.asList(
                        (long) prev,
                        (long) curr,
                        colorSum));
            }

            // apply delta at current position
            colorSum += entry.getValue();

            prev = curr;
        }

        return res;
    }


    // V0-2
    // IDEA: TREEMAP + SORT (GEMINI)
    public List<List<Long>> splitPainting_0_2(int[][] segments) {
        // 1. Use a TreeMap to store color changes at each coordinate.
        // TreeMap keeps coordinates sorted and handles multiple segments at the same point.
        Map<Integer, Long> diff = new TreeMap<>();

        for (int[] seg : segments) {
            int start = seg[0];
            int end = seg[1];
            int color = seg[2];

            diff.put(start, diff.getOrDefault(start, 0L) + color);
            diff.put(end, diff.getOrDefault(end, 0L) - color);
        }

        List<List<Long>> res = new ArrayList<>();
        long runningColorSum = 0;
        Integer prev = null;

        // 2. Iterate through the sorted boundary points
        for (int curr : diff.keySet()) {
            if (prev != null && runningColorSum > 0) {
                // Create an interval from the previous boundary to the current one
                res.add(Arrays.asList((long) prev, (long) curr, runningColorSum));
            }

            // Update the cumulative color sum for the next interval
            runningColorSum += diff.get(curr);
            prev = curr;
        }

        return res;
    }



    // V1-1
    // IDEA: PREFIX SUM + HASHMAP (gpt)
    public int longestWPI_1_1(int[] hours) {
        int n = hours.length;
        // Convert hours to 1 (tiring) or -1 (non-tiring)
        int[] work = new int[n];
        for (int i = 0; i < n; i++) {
            work[i] = hours[i] > 8 ? 1 : -1;
        }

        /** NOTE !!
         *
         *  - map stores the `first occurrence` of each `prefix sum.`
         *     - Key → prefix sum
         *     - Value → index where this sum first occurred
         *
         *     -> map: { prefix_sum: idx_first_occur }
         *
         *  - prefix tracks the running sum of the transformed array.
         *
         *  - maxLen keeps the length of the longest well-performing interval found so far.
         *
         */
        Map<Integer, Integer> map = new HashMap<>();
        int prefix = 0;
        int maxLen = 0;

        /** NOTE !!!
         *
         * Loop through each day.
         * Update prefix with the current day’s value (+1 or -1).
         * This is the cumulative sum up to index i.
         */
        for (int i = 0; i < n; i++) {
            prefix += work[i];

            // Case 1: the prefix sum itself > 0
            /** NOTE !!!  case 1)
             *
             * - If the total sum from day 0 to i is positive,
             *    then the interval [0..i] is well-performing.
             *
             * - Its length is i + 1 (0-based index).
             *
             * - Update maxLen because this
             *   might be the longest interval.
             *
             */
            if (prefix > 0) {
                maxLen = i + 1;
            }
            /** NOTE !!!  case 2)
             *
             *
             *  - If prefix <= 0, we can’t take [0..i].
             *
             *  -> (NOTE !!!)
             *     But there may exist a `subarray` starting
             *     later that is well-performing:
             *
             * Reasoning:
             *
             *   - Let j be the first index where prefix[j] = prefix[i] - 1.
             *   - Then prefix[i] - prefix[j] = 1 → sum of [j+1..i] is positive.
             *   - Length = i - j.
             *
             * - map.get(prefix - 1) gives the earliest index j where this happened.
             * - Update maxLen if this subarray is longer than the previous best.
             *
             */
            else {
                // Case 2: check if (prefix - 1) was seen before
                if (map.containsKey(prefix - 1)) {
                    maxLen = Math.max(maxLen, i - map.get(prefix - 1));
                }
            }

            /**
             * Only store the first occurrence of each prefix sum
             */
            map.putIfAbsent(prefix, i);
        }

        return maxLen;
    }



    // V1-2
    // IDEA: PREFIX SUM + HASHMAP (gemini)
    public int longestWPI_1_2(int[] hours) {
        int res = 0;
        int score = 0;
        // Map stores {score : earliest_index_found}
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < hours.length; i++) {
            // Step 1: Transform to +1 / -1
            score += (hours[i] > 8) ? 1 : -1;

            // Step 2: Check if current prefix is already valid
            if (score > 0) {
                res = i + 1;
            } else {
                // Step 3: If score <= 0, we need to find a previous score
                // that is exactly score - 1.
                // If we find it, the subarray between that index and i has sum 1.
                map.putIfAbsent(score, i);
                if (map.containsKey(score - 1)) {
                    res = Math.max(res, i - map.get(score - 1));
                }
            }
        }

        return res;
    }



    // V2
    // IDEA: PREFIX SUM
    // https://leetcode.com/problems/longest-well-performing-interval/solutions/5870075/simple-explanation-prefix-sum-by-davide_-krwj/
    public int longestWPI_2(int[] hours) {
        int[] transformed = new int[hours.length];
        for (int i = 0; i < hours.length; i++) {
            transformed[i] = (hours[i] > 8) ? 1 : -1;
        }

        int prefixSum = 0;
        int longestInterval = 0;
        Map<Integer, Integer> prefixSumMap = new HashMap<>();

        for (int i = 0; i < transformed.length; i++) {
            // Update the running prefix sum
            prefixSum += transformed[i];

            // If the prefix sum is positive, we have a well-performing interval from the start to i
            if (prefixSum > 0)
                longestInterval = i + 1;
            else
                // Check if there is an earlier prefix sum that would give a well-performing interval
                if (prefixSumMap.containsKey(prefixSum - 1))
                    longestInterval = Math.max(longestInterval, i - prefixSumMap.get(prefixSum - 1));

            // Record the first occurrence of each prefix sum
            if (!prefixSumMap.containsKey(prefixSum))
                prefixSumMap.put(prefixSum, i);
        }

        return longestInterval;
    }


    // V3
    // IDEA: HashMap
    // https://leetcode.com/problems/longest-well-performing-interval/solutions/336639/java-hashmap-solution-with-code-comments-9cse/
    public int longestWPI_3(int[] hours) {
        if (hours == null || hours.length == 0) {
            return 0;
        }
        int longestLength = 0;
        int score = 0;
        int n = hours.length;
        // A map to store the first index of each unique negative score.
        // Map keys: negative cumulative scores.
        // Map values: indexes of the negative scores.
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < n; i++) {
            // Track running sum of tiring events.
            // Each event is either +1 or -1.
            score += (hours[i] > 8) ? 1 : -1;
            if (score > 0) {
                // Cumulative score since i==0 is positive.
                // This means the entire length to i is the new best interval.
                int intervalLength = i + 1;
                longestLength = intervalLength;
            } else {
                // Record first instance of of this score deficit (-1, -2, etc.).
                seen.putIfAbsent(score, i);
                // Think of a road climbing and falling along rolling hills.
                //
                // If a sea level road crosses a 5 ft deep valley in the desert,
                // it will see an elevation of -1 ft once at the start of the
                // valley on the way down, and once at the end of the
                // valley on the way back up.
                //
                // For the road to get back up to -1 ft, it had to have a section
                // of decently positive elevation gain. From -1 ft to -1 ft, the
                // road would have a net elevation gain of 0 ft.
                //
                // This problem is looking for a 1 ft or greater net elevation gain
                // over a span. If the road saw -2 ft at some point, the net gain
                // back to -1 ft would be 1 ft which matches the problem's criteria.
                //
                // The first observation of a unique negative score is kept because
                // it would represent the largest span matching "score + 1". In the
                // road analogy, if the road crossed two identical valleys, a net
                // positive span would occur twice, though the first occurrence of
                // -2 ft would result in the longest net positive span.
                if (seen.containsKey(score - 1)) {
                    int indexOfFirstScoreMinus1 = seen.get(score - 1);
                    // The length is the difference between the two indicies.
                    int intervalLength = i - indexOfFirstScoreMinus1;
                    longestLength = Math.max(longestLength, intervalLength);
                }
            }
        }
        return longestLength;
    }


    // V4
    // IDEA: HASHMAP + PREFIX SUM
    // https://leetcode.com/problems/longest-well-performing-interval/solutions/1327424/java-hashmap-prefix-sum-simple-no-fancy-a16hd/
    public int longestWPI_4(int[] hours) {
        int n = hours.length;
        int[] pre = new int[n + 1];
        int[] interval = new int[n];
        for (int i = 0; i < n; ++i) {
            if (hours[i] > 8) {
                interval[i] = 1;
            } else {
                interval[i] = -1;
            }
        }
        pre[0] = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, 0);
        int max_len = 0;
        for (int i = 1; i <= n; ++i) {
            pre[i] = pre[i - 1] + interval[i - 1];
            if (pre[i] > 0) {
                max_len = Math.max(max_len, i);
            } else {
                int look_up = pre[i] - 1;
                if (map.containsKey(look_up)) {
                    max_len = Math.max(i - map.get(look_up), max_len);
                }
            }
            if (!map.containsKey(pre[i])) {
                map.put(pre[i], i);
            }
        }
        return max_len;

    }




    // V5





}
