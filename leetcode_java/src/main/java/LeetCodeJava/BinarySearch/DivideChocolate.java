package LeetCodeJava.BinarySearch;

// https://leetcode.ca/all/1231.html
// https://leetcode.com/problems/divide-chocolate/description/
/**
 *  1231. Divide Chocolate
 * You have one chocolate bar that consists of some chunks. Each chunk has its own sweetness given by the array sweetness.
 *
 * You want to share the chocolate with your K friends so you start cutting the chocolate bar into K+1 pieces using K cuts, each piece consists of some consecutive chunks.
 *
 * Being generous, you will eat the piece with the minimum total sweetness and give the other pieces to your friends.
 *
 * Find the maximum total sweetness of the piece you can get by cutting the chocolate bar optimally.
 *
 *
 *
 * Example 1:
 *
 * Input: sweetness = [1,2,3,4,5,6,7,8,9], K = 5
 * Output: 6
 * Explanation: You can divide the chocolate to [1,2,3], [4,5], [6], [7], [8], [9]
 * Example 2:
 *
 * Input: sweetness = [5,6,7,8,9,1,2,3,4], K = 8
 * Output: 1
 * Explanation: There is only one way to cut the bar into 9 pieces.
 * Example 3:
 *
 * Input: sweetness = [1,2,2,1,2,2,1,2,2], K = 2
 * Output: 5
 * Explanation: You can divide the chocolate to [1,2,2], [1,2,2], [1,2,2]
 *
 *
 * Constraints:
 *
 * 0 <= K < sweetness.length <= 10^4
 * 1 <= sweetness[i] <= 10^5
 * Difficulty:
 * Hard
 * Lock:
 * Prime
 * Company:
 * Google
 *
 */
public class DivideChocolate {

    // V0
//    public int maximizeSweetness(int[] sweetness, int k) {
//
//    }


    // V0-1
    // IDEA: Binary Search + Greedy (gemini)
    // TODO: validate
    public int maximizeSweetness_0_1(int[] sweetness, int k) {
        // You are sharing with K friends, so there are K + 1 total pieces.
        int totalPeople = k + 1;

        // Lower bound: The smallest possible sweetness piece (at least 1)
        int left = 1;
        // Upper bound: Total sweetness divided by number of people
        int right = 0;
        for (int s : sweetness) right += s;
        right /= totalPeople;

        int ans = 0;
        while (left <= right) {
            int mid = left + (right - left) / 2;

            // If we can cut the chocolate into at least totalPeople pieces,
            // where each piece has at least 'mid' sweetness:
            if (canSplit(sweetness, totalPeople, mid)) {
                ans = mid;    // 'mid' is a possible answer
                left = mid + 1; // Try for a larger minimum sweetness
            } else {
                right = mid - 1; // 'mid' is too large, reduce the target
            }
        }

        return ans;
    }

    private boolean canSplit(int[] sweetness, int totalPeople, int minTarget) {
        int currentSweetness = 0;
        int pieces = 0;

        for (int s : sweetness) {
            currentSweetness += s;
            // If the current piece reaches the target, we cut it.
            if (currentSweetness >= minTarget) {
                pieces++;
                currentSweetness = 0;
            }
        }

        // Check if we managed to create enough pieces for everyone.
        return pieces >= totalPeople;
    }


    // V0-2
    // IDEA: Binary Search + Greedy (GPT)
    // TODO: validate
    public int maximizeSweetness_0_2(int[] sweetness, int k) {
        int left = 1;  // minimum possible sweetness
        int right = 0; // maximum possible sweetness (sum)

        for (int s : sweetness) {
            right += s;
        }

        // binary search on answer
        while (left < right) {
            int mid = left + (right - left + 1) / 2;

            if (canSplit_0_2(sweetness, mid, k)) {
                left = mid;       // try larger minimum
            } else {
                right = mid - 1;  // reduce target
            }
        }

        return left;
    }

    // check if we can make >= k+1 pieces with each >= target
    private boolean canSplit_0_2(int[] sweetness, int target, int k) {
        int sum = 0;
        int count = 0;

        for (int s : sweetness) {
            sum += s;
            if (sum >= target) {
                count++;
                sum = 0;
            }
        }

        return count >= k + 1;
    }



    // V1
    // IDEA: BINARY SEARCH + GREEDY
    // https://leetcode.ca/2019-04-14-1231-Divide-Chocolate/
    public int maximizeSweetness_1(int[] sweetness, int k) {
        int l = 0, r = 0;
        for (int v : sweetness) {
            r += v;
        }
        while (l < r) {
            int mid = (l + r + 1) >> 1;
            if (check(sweetness, mid, k)) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return l;
    }

    private boolean check(int[] nums, int x, int k) {
        int s = 0, cnt = 0;
        for (int v : nums) {
            s += v;
            if (s >= x) {
                s = 0;
                ++cnt;
            }
        }
        return cnt > k;
    }


    // V2


    // V3



}
