package LeetCodeJava.Array;

// https://leetcode.com/problems/average-salary-excluding-the-minimum-and-maximum-salary/description/

/**
 * 1491. Average Salary Excluding the Minimum and Maximum Salary
 * Easy
 *
 * You are given an array of unique integers salary where salary[i] is the salary of the
 * ith employee.
 *
 * Return the average salary of employees excluding the minimum and maximum salary. Answers
 * within 10^-5 of the actual answer will be accepted.
 *
 *
 * Example 1:
 *
 * Input: salary = [4000,3000,1000,2000]
 * Output: 2500.00000
 * Explanation: Minimum salary and maximum salary are 1000 and 4000 respectively.
 * Average salary excluding minimum and maximum salary is (2000+3000) / 2 = 2500
 *
 * Example 2:
 *
 * Input: salary = [1000,2000,3000]
 * Output: 2000.00000
 *
 *
 * Constraints:
 *
 * 3 <= salary.length <= 100
 * 1000 <= salary[i] <= 10^6
 * All the integers of salary are unique.
 *
 */
public class AverageSalaryExcludingTheMinimumAndMaximumSalary {

    // V0
    // IDEA: SUM MINUS THE EXTREMES (one pass, no sorting) — values are unique, so exactly one
    //       entry is dropped at each end; n >= 3 keeps the denominator positive
    /**
     * time = O(N)
     * space = O(1)
     */
    public double average(int[] salary) {
        long total = 0;
        int lowest = Integer.MAX_VALUE;
        int highest = Integer.MIN_VALUE;

        for (int pay : salary) {
            total += pay;
            lowest = Math.min(lowest, pay);
            highest = Math.max(highest, pay);
        }

        // force floating-point division
        return (double) (total - lowest - highest) / (salary.length - 2);
    }
}
