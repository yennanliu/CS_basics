package LeetCodeJava.Array;

// https://leetcode.com/problems/richest-customer-wealth/description/
/**
 *  1672. Richest Customer Wealth
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an m x n integer grid accounts where accounts[i][j] is the amount of money the i​​​​​​​​​​​th​​​​ customer has in the j​​​​​​​​​​​th​​​​ bank. Return the wealth that the richest customer has.
 *
 * A customer's wealth is the amount of money they have in all their bank accounts. The richest customer is the customer that has the maximum wealth.
 *
 *
 *
 * Example 1:
 *
 * Input: accounts = [[1,2,3],[3,2,1]]
 * Output: 6
 * Explanation:
 * 1st customer has wealth = 1 + 2 + 3 = 6
 * 2nd customer has wealth = 3 + 2 + 1 = 6
 * Both customers are considered the richest with a wealth of 6 each, so return 6.
 * Example 2:
 *
 * Input: accounts = [[1,5],[7,3],[3,5]]
 * Output: 10
 * Explanation:
 * 1st customer has wealth = 6
 * 2nd customer has wealth = 10
 * 3rd customer has wealth = 8
 * The 2nd customer is the richest with a wealth of 10.
 * Example 3:
 *
 * Input: accounts = [[2,8,7],[7,1,3],[1,9,5]]
 * Output: 17
 *
 *
 * Constraints:
 *
 * m == accounts.length
 * n == accounts[i].length
 * 1 <= m, n <= 50
 * 1 <= accounts[i][j] <= 100
 *
 */
public class RichestCustomerWealth {

    // V0
    public int maximumWealth(int[][] accounts) {
        // edge

        int l = accounts.length;
        int w = accounts[0].length;

        int maxVal = 0;
        for (int i = 0; i < l; i++) {
            int tmp = getSum(accounts[i]);
            maxVal = Math.max(maxVal, tmp);
        }

        return maxVal;
    }

    private int getSum(int[] x) {
        int res = 0;
        for (int i : x) {
            res += i;
        }
        return res;
    }


    // V1
    // https://leetcode.com/problems/richest-customer-wealth/solutions/7227528/clean-simple-easy-explanation-beats-100-rsis1/
    public int maximumWealth_1(int[][] accounts) {
        int rich = 0;
        for (int[] i : accounts) {
            int sum = 0;
            for (int money : i) {
                sum += money;
            }
            rich = Math.max(rich, sum);
        }
        return rich;
    }

    // V2
    // https://leetcode.com/problems/richest-customer-wealth/solutions/7101899/java-c-c-javascript-c-100-easy-to-unders-rlpl/
    public int maximumWealth_2(int[][] accounts) {
        int curSum = 0;
        for (int[] customers : accounts) {
            int sum = 0;
            for (int account : customers) {
                sum += account;
            }
            if (sum > curSum) {
                curSum = sum;
            }
        }
        return curSum;
    }



    // V3


}
