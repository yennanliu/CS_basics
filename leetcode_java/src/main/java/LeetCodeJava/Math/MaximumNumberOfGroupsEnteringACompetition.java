package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-number-of-groups-entering-a-competition/description/

/**
 * 2358. Maximum Number of Groups Entering a Competition
 * Medium
 *
 * You are given a positive integer array grades which represents the grades of students in a university. You would like to enter all these students into a competition in ordered non-empty groups, such that the ordering meets the following conditions:
 *
 * The sum of the grades of students in the ith group is less than the sum of the grades of students in the (i + 1)th group, for all groups (except the last).
 * The total number of students in the ith group is less than the total number of students in the (i + 1)th group, for all groups (except the last).
 *
 * Return the maximum number of groups that can be formed.
 *
 *
 * Example 1:
 *
 * Input: grades = [10,6,12,7,3,5]
 * Output: 3
 * Explanation: The following is a possible way to form 3 groups of students:
 * - 1st group has the students with grades = [12]. Sum of grades: 12. Student count: 1
 * - 2nd group has the students with grades = [6,7]. Sum of grades: 6 + 7 = 13. Student count: 2
 * - 3rd group has the students with grades = [10,3,5]. Sum of grades: 10 + 3 + 5 = 18. Student count: 3
 * It can be shown that it is not possible to form more than 3 groups.
 *
 * Example 2:
 *
 * Input: grades = [8,8]
 * Output: 1
 * Explanation: We can only form 1 group, since forming 2 groups would lead to an equal number of students in both groups.
 *
 *
 * Constraints:
 *
 * 1 <= grades.length <= 10^5
 * 1 <= grades[i] <= 10^5
 *
 */
public class MaximumNumberOfGroupsEnteringACompetition {

    // V0
    // IDEA: MATH / GREEDY (only the group SIZES matter)
    //       sort grades ascending and hand out groups of 1, 2, 3, ... k students:
    //       sizes strictly increase, and every grade in group i is <= every grade in
    //       group i+1 which has one more student, so sums strictly increase too.
    //       leftovers join the last group. answer = largest k with k(k+1)/2 <= n.
    //       (the grades themselves never need to be sorted -- only n matters)
    /**
     * time = O(sqrt(N))
     * space = O(1)
     */
    public int maximumGroups(int[] grades) {
        int n = grades.length;
        int groups = 0;
        int studentsUsed = 0;
        // add group (groups + 1) while there are enough students for it
        while (studentsUsed + (groups + 1) <= n) {
            groups++;
            studentsUsed += groups;
        }
        return groups;
    }

    // V1
    // IDEA: BINARY SEARCH ON THE ANSWER
    //       f(k) = k(k+1)/2 is strictly increasing, so "k groups fit" is monotone;
    //       binary search the largest feasible k in [0, n].
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int maximumGroups_1(int[] grades) {
        long n = grades.length;
        long low = 0;
        long high = n;
        while (low < high) {
            // round up so that low = mid always makes progress
            long mid = low + (high - low + 1) / 2;
            if (mid * (mid + 1) / 2 <= n) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return (int) low;
    }
}
