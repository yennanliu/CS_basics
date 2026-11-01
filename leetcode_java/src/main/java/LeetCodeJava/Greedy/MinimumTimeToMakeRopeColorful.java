package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-time-to-make-rope-colorful/description/

/**
 * 1578. Minimum Time to Make Rope Colorful
 * Medium
 *
 * Alice has n balloons arranged on a rope. You are given a 0-indexed string colors
 * where colors[i] is the color of the ith balloon.
 *
 * Alice wants the rope to be colorful. She does not want two consecutive balloons
 * to be of the same color, so she asks Bob for help. Bob can remove some balloons
 * from the rope to make it colorful. You are given a 0-indexed integer array
 * neededTime where neededTime[i] is the time (in seconds) that Bob needs to remove
 * the ith balloon from the rope.
 *
 * Return the minimum time Bob needs to make the rope colorful.
 *
 *
 * Example 1:
 *
 * Input: colors = "abaac", neededTime = [1,2,3,4,5]
 * Output: 3
 * Explanation: Bob can remove the balloon at index 2. This takes 3 seconds.
 *
 * Example 2:
 *
 * Input: colors = "abc", neededTime = [1,2,3]
 * Output: 0
 * Explanation: The rope is already colorful.
 *
 * Example 3:
 *
 * Input: colors = "aabaa", neededTime = [1,2,3,4,1]
 * Output: 2
 * Explanation: Bob will remove the balloons at indices 0 and 4. Total time = 1 + 1 = 2.
 *
 *
 * Constraints:
 *
 * n == colors.length == neededTime.length
 * 1 <= n <= 10^5
 * 1 <= neededTime[i] <= 10^4
 * colors contains only lowercase English letters.
 *
 */
public class MinimumTimeToMakeRopeColorful {

    // V0
    // IDEA: GREEDY (INSIDE A BLOCK OF EQUAL COLOURS KEEP THE MOST EXPENSIVE ONE)
    //       cost of a block = sum(block) - max(block); blocks are independent.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minCost(String colors, int[] neededTime) {
        int n = colors.length();
        int totalCost = 0;
        int i = 0;

        while (i < n) {
            // [i, j) is a maximal block of the same colour
            int j = i;
            int blockSum = 0;
            int blockMax = 0;
            while (j < n && colors.charAt(j) == colors.charAt(i)) {
                blockSum += neededTime[j];
                blockMax = Math.max(blockMax, neededTime[j]);
                j++;
            }
            // remove everything in the block except its most expensive balloon
            totalCost += blockSum - blockMax;
            i = j;
        }
        return totalCost;
    }
}
