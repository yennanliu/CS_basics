package LeetCodeJava.String;

// https://leetcode.com/problems/minimum-time-difference/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  539. Minimum Time Difference
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given a list of 24-hour clock time points in "HH:MM" format, return the minimum minutes difference between any two time-points in the list.
 *
 *
 * Example 1:
 *
 * Input: timePoints = ["23:59","00:00"]
 * Output: 1
 * Example 2:
 *
 * Input: timePoints = ["00:00","23:59","00:00"]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 2 <= timePoints.length <= 2 * 104
 * timePoints[i] is in the format "HH:MM".
 *
 */
public class MinimumTimeDifference {

    // V0
//    public int findMinDifference(List<String> timePoints) {
//
//    }


    // V0-1
    // IDEA: sort (str -> int) (GPT)
    public int findMinDifference_0_1(List<String> timePoints) {
        // edge
        if (timePoints.size() <= 1) {
            return 0;
        }

        List<Integer> list = new ArrayList<>();

        for (String x : timePoints) {
            list.add(getTimeStamp(x));
        }

        Collections.sort(list);

        // circular diff between last and first
        int minGap = 24 * 60 - (list.get(list.size() - 1) - list.get(0));

        for (int i = 1; i < list.size(); i++) {
            minGap = Math.min(
                    minGap,
                    list.get(i) - list.get(i - 1));
        }

        return minGap;
    }

    private int getTimeStamp(String str) {
        String[] arr = str.split(":");

        int hour = Integer.parseInt(arr[0]);
        int minute = Integer.parseInt(arr[1]);

        return hour * 60 + minute;
    }


    // V0-2
    // IDEA: sort (str -> int) (GEMINI)
    public int findMinDifference_0_2(List<String> timePoints) {
        // 1. Pigeonhole Principle: If more than 1440 points, a duplicate is guaranteed
        if (timePoints.size() > 1440)
            return 0;

        List<Integer> minutes = new ArrayList<>();
        for (String time : timePoints) {
            minutes.add(getTimeStamp_0_2(time));
        }

        // 2. Sort the timestamps
        Collections.sort(minutes);

        // 3. Initialize minGap with the circular "wraparound" case:
        // Difference between the last time and the first time of the next day
        int n = minutes.size();
        int first = minutes.get(0);
        int last = minutes.get(n - 1);
        int minGap = (1440 - last) + first;

        // 4. Compare adjacent time points
        for (int i = 1; i < n; i++) {
            minGap = Math.min(minGap, minutes.get(i) - minutes.get(i - 1));
            // Optimization: if minGap is 0, we can't get any lower
            if (minGap == 0)
                return 0;
        }

        return minGap;
    }

    private int getTimeStamp_0_2(String str) {
        // Split "HH:mm" into hours and minutes
        String[] parts = str.split(":");
        int h = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        return h * 60 + m;
    }




    // V1-1
    // IDEA: SORT
    // https://leetcode.com/problems/minimum-time-difference/editorial/
    public int findMinDifference_1_1(List<String> timePoints) {
        // convert input to minutes
        int[] minutes = new int[timePoints.size()];
        for (int i = 0; i < timePoints.size(); i++) {
            String time = timePoints.get(i);
            int h = Integer.parseInt(time.substring(0, 2));
            int m = Integer.parseInt(time.substring(3));
            minutes[i] = h * 60 + m;
        }

        // sort times in ascending order
        Arrays.sort(minutes);

        // find minimum difference across adjacent elements
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < minutes.length - 1; i++) {
            ans = Math.min(ans, minutes[i + 1] - minutes[i]);
        }

        // consider difference between last and first element
        return Math.min(
                ans,
                24 * 60 - minutes[minutes.length - 1] + minutes[0]);
    }



    // V1-2
    // IDEA: Bucket Sort
    // https://leetcode.com/problems/minimum-time-difference/editorial/
    public int findMinDifference_1_2(List<String> timePoints) {
        // create buckets array for the times converted to minutes
        boolean[] minutes = new boolean[24 * 60];
        for (String time : timePoints) {
            int min = Integer.parseInt(time.substring(0, 2)) * 60 +
                    Integer.parseInt(time.substring(3));
            if (minutes[min])
                return 0;
            minutes[min] = true;
        }
        int prevIndex = Integer.MAX_VALUE;
        int firstIndex = Integer.MAX_VALUE;
        int lastIndex = Integer.MAX_VALUE;
        int ans = Integer.MAX_VALUE;

        // find differences between adjacent elements in sorted array
        for (int i = 0; i < 24 * 60; i++) {
            if (minutes[i]) {
                if (prevIndex != Integer.MAX_VALUE) {
                    ans = Math.min(ans, i - prevIndex);
                }
                prevIndex = i;
                if (firstIndex == Integer.MAX_VALUE) {
                    firstIndex = i;
                }
                lastIndex = i;
            }
        }

        return Math.min(ans, 24 * 60 - lastIndex + firstIndex);
    }



    // V2


    // V3




}
