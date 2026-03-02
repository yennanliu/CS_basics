package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/magnetic-force-between-two-balls/description/

import java.util.Arrays;

/**
 *  1552. Magnetic Force Between Two Balls
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * In the universe Earth C-137, Rick discovered a special form of magnetic force between two balls if they are put in his new invented basket. Rick has n empty baskets, the ith basket is at position[i], Morty has m balls and needs to distribute the balls into the baskets such that the minimum magnetic force between any two balls is maximum.
 *
 * Rick stated that magnetic force between two different balls at positions x and y is |x - y|.
 *
 * Given the integer array position and the integer m. Return the required force.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: position = [1,2,3,4,7], m = 3
 * Output: 3
 * Explanation: Distributing the 3 balls into baskets 1, 4 and 7 will make the magnetic force between ball pairs [3, 3, 6]. The minimum magnetic force is 3. We cannot achieve a larger minimum magnetic force than 3.
 * Example 2:
 *
 * Input: position = [5,4,3,2,1,1000000000], m = 2
 * Output: 999999999
 * Explanation: We can use baskets 1 and 1000000000.
 *
 *
 * Constraints:
 *
 * n == position.length
 * 2 <= n <= 105
 * 1 <= position[i] <= 109
 * All integers in position are distinct.
 * 2 <= m <= position.length
 *
 *
 */
public class MagneticForceBetweenTwoBalls {

    // V0
//    public int maxDistance(int[] position, int m) {
//
//    }

    // V1
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/magnetic-force-between-two-balls/editorial/
    // Check if we can place 'm' balls at 'position'
    // with each ball having at least 'x' gap.
    private boolean canPlaceBalls(int x, int[] position, int m) {
        // Place the first ball at the first position.
        int prevBallPos = position[0];
        int ballsPlaced = 1;

        // Iterate on each 'position' and place a ball there if we can place it.
        for (int i = 1; i < position.length && ballsPlaced < m; ++i) {
            int currPos = position[i];
            // Check if we can place the ball at the current position.
            if (currPos - prevBallPos >= x) {
                ballsPlaced += 1;
                prevBallPos = currPos;
            }
        }
        // If all 'm' balls are placed, return 'true'.
        return ballsPlaced == m;
    }

    public int maxDistance_1(int[] position, int m) {
        int answer = 0;
        int n = position.length;
        Arrays.sort(position);

        // Initial search space.
        int low = 1;
        int high = (int) Math.ceil(position[n - 1] / (m - 1.0));
        while (low <= high) {
            int mid = low + (high - low) / 2;
            // If we can place all balls having a gap at least 'mid',
            if (canPlaceBalls(mid, position, m)) {
                // then 'mid' can be our answer,
                answer = mid;
                // and discard the left half search space.
                low = mid + 1;
            } else {
                // Discard the right half search space.
                high = mid - 1;
            }
        }
        return answer;
    }



    // V2
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/magnetic-force-between-two-balls/solutions/5339212/beats-100-explained-with-video-cjavapyth-0zts/
    public int maxDistance_2(int[] position, int m) {
        Arrays.sort(position);
        int lo = 1;
        int hi = (position[position.length - 1] - position[0]) / (m - 1);
        int ans = 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (canWePlace(position, mid, m)) {
                ans = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return ans;
    }

    private boolean canWePlace(int[] arr, int dist, int cows) {
        int cntCows = 1;
        int last = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] - last >= dist) {
                cntCows++;
                last = arr[i];
            }
            if (cntCows >= cows) {
                return true;
            }
        }
        return false;
    }


    // V3
    // https://leetcode.com/problems/magnetic-force-between-two-balls/solutions/5339191/binary-search-on-answer-tutorial-magneti-zioj/
    public int maxDistance_3(int[] position, int m) {
        Arrays.sort(position);
        int lo = 1;
        int hi = (position[position.length - 1] - position[0]) / (m - 1);
        int ans = 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (canWePlace_3(position, mid, m)) {
                ans = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return ans;
    }

    private boolean canWePlace_3(int[] arr, int dist, int cows) {
        int cntCows = 1;
        int last = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] - last >= dist) {
                cntCows++;
                last = arr[i];
            }
            if (cntCows >= cows) {
                return true;
            }
        }
        return false;
    }




}
