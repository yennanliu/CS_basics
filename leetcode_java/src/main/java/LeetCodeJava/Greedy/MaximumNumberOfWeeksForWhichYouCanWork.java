package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-number-of-weeks-for-which-you-can-work/description/

/**
 * 1953. Maximum Number of Weeks for Which You Can Work
 * Medium
 *
 * There are n projects numbered from 0 to n - 1. You are given an integer array milestones where each
 * milestones[i] denotes the number of milestones the ith project has.
 *
 * You can work on the projects following these two rules:
 *
 * Every week, you will finish exactly one milestone of one project. You must work every week.
 * You cannot work on two milestones from the same project for two consecutive weeks.
 *
 * Once all the milestones of all the projects are finished, or if the only milestones that you can work on
 * will cause you to violate the above rules, you will stop working. Note that you may not be able to finish
 * every project's milestones due to these constraints.
 *
 * Return the maximum number of weeks you would be able to work on the projects without violating the rules
 * mentioned above.
 *
 *
 * Example 1:
 *
 * Input: milestones = [1,2,3]
 * Output: 6
 *
 * Example 2:
 *
 * Input: milestones = [5,2,1]
 * Output: 7
 *
 *
 * Constraints:
 *
 * n == milestones.length
 * 1 <= n <= 10^5
 * 1 <= milestones[i] <= 10^9
 *
 */
public class MaximumNumberOfWeeksForWhichYouCanWork {

    // V0
    // IDEA: GREEDY - ONLY THE BIGGEST PROJECT MATTERS (TASK REARRANGEMENT BOUND)
    /**
     * time = O(N)
     * space = O(1)
     */
    public long numberOfWeeks(int[] milestones) {
        // total can reach 10^5 * 10^9, so it needs a long
        long total = 0;
        long biggest = 0;
        for (int milestone : milestones) {
            total += milestone;
            biggest = Math.max(biggest, milestone);
        }

        // every milestone of the biggest project must be separated by a milestone of another project
        long rest = total - biggest;

        // not enough fillers: the best schedule is B f B f ... B, using every filler once
        if (biggest > rest + 1) {
            return rest * 2 + 1;
        }

        // enough fillers to interleave everything
        return total;
    }
}
