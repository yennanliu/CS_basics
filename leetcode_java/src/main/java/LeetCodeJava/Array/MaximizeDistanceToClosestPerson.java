package LeetCodeJava.Array;

// https://leetcode.com/problems/maximize-distance-to-closest-person/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 849. Maximize Distance to Closest Person
 * Medium
 * Topics
 * Companies
 * You are given an array representing a row of seats where seats[i] = 1 represents a person sitting in the ith seat, and seats[i] = 0 represents that the ith seat is empty (0-indexed).
 *
 * There is at least one empty seat, and at least one person sitting.
 *
 * Alex wants to sit in the seat such that the distance between him and the closest person to him is maximized.
 *
 * Return that maximum distance to the closest person.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: seats = [1,0,0,0,1,0,1]
 * Output: 2
 * Explanation:
 * If Alex sits in the second open seat (i.e. seats[2]), then the closest person has distance 2.
 * If Alex sits in any other open seat, the closest person has distance 1.
 * Thus, the maximum distance to the closest person is 2.
 * Example 2:
 *
 * Input: seats = [1,0,0,0]
 * Output: 3
 * Explanation:
 * If Alex sits in the last seat (i.e. seats[3]), the closest person is 3 seats away.
 * This is the maximum distance possible, so the answer is 3.
 * Example 3:
 *
 * Input: seats = [0,1]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 2 <= seats.length <= 2 * 104
 * seats[i] is 0 or 1.
 * At least one seat is empty.
 * At least one seat is occupied.
 *
 */
public class MaximizeDistanceToClosestPerson {

    // V0
    // TODO : fix below
//    public int maxDistToClosest(int[] seats) {
//
//        List<Integer> distances = new ArrayList<>();
//        int lastIdx = -1;
//        for(int i = seats.length - 1; i >= 0; i--){
//            if (seats[i] == 1){
//                if (lastIdx != -1){
//                    int diff = Math.abs(i - lastIdx);
//                    distances.add(diff);
//                }
//                lastIdx = i;
//            }
//        }
//
//        System.out.println(">>> (before sort) distances = " + distances);
//        distances.sort(Integer::compareTo);
//        System.out.println(">>> (after sort) distances = " + distances);
//
//        // edge case : if only one "1"
//        if (distances.isEmpty()){
//            return seats.length-1;
//        }
//        // return the max dist
//        return distances.get(distances.size()-1) / 2; // ??
//    }

    // V1-1
    // IDEA : Next Array
    // https://leetcode.com/problems/maximize-distance-to-closest-person/editorial/
    public int maxDistToClosest_1_1(int[] seats) {
        int N = seats.length;
        int[] left = new int[N], right = new int[N];
        Arrays.fill(left, N);
        Arrays.fill(right, N);

        for (int i = 0; i < N; ++i) {
            if (seats[i] == 1)
                left[i] = 0;
            else if (i > 0)
                left[i] = left[i - 1] + 1;
        }

        for (int i = N - 1; i >= 0; --i) {
            if (seats[i] == 1)
                right[i] = 0;
            else if (i < N - 1)
                right[i] = right[i + 1] + 1;
        }

        int ans = 0;
        for (int i = 0; i < N; ++i)
            if (seats[i] == 0)
                ans = Math.max(ans, Math.min(left[i], right[i]));
        return ans;
    }


    // V1-2
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/maximize-distance-to-closest-person/editorial/
    public int maxDistToClosest_1_2(int[] seats) {
        int N = seats.length;
        int prev = -1, future = 0;
        int ans = 0;

        for (int i = 0; i < N; ++i) {
            if (seats[i] == 1) {
                prev = i;
            } else {
                while (future < N && seats[future] == 0 || future < i)
                    future++;

                int left = prev == -1 ? N : i - prev;
                int right = future == N ? N : future - i;
                ans = Math.max(ans, Math.min(left, right));
            }
        }

        return ans;
    }

    // V2
}
