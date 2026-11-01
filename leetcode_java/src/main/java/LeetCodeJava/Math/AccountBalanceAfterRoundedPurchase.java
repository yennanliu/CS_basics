package LeetCodeJava.Math;

// https://leetcode.com/problems/account-balance-after-rounded-purchase/description/

/**
 * 2806. Account Balance After Rounded Purchase
 * Easy
 *
 * Initially, you have a bank account balance of 100 dollars.
 *
 * You are given an integer purchaseAmount representing the amount you will spend on a purchase in dollars, in other words, its price.
 *
 * When making the purchase, first the purchaseAmount is rounded to the nearest multiple of 10. Let us call this value roundedAmount. Then, roundedAmount dollars are removed from your bank account.
 *
 * Return an integer denoting your final bank account balance after this purchase.
 *
 * Notes:
 *
 * 0 is considered to be a multiple of 10 in this problem.
 * When rounding, 5 is rounded upward (5 is rounded to 10, 15 is rounded to 20, 25 to 30, and so on).
 *
 *
 * Example 1:
 *
 * Input: purchaseAmount = 9
 * Output: 90
 *
 * Example 2:
 *
 * Input: purchaseAmount = 15
 * Output: 80
 *
 * Example 3:
 *
 * Input: purchaseAmount = 10
 * Output: 90
 *
 *
 * Constraints:
 *
 * 0 <= purchaseAmount <= 100
 *
 */
public class AccountBalanceAfterRoundedPurchase {

    private static final int INITIAL_BALANCE = 100;

    // V0
    // IDEA: ROUND HALF UP TO THE NEAREST 10 -- adding 5 before integer division pushes remainders 5..9 up
    /**
     * time = O(1)
     * space = O(1)
     */
    public int accountBalanceAfterPurchase(int purchaseAmount) {
        int roundedAmount = ((purchaseAmount + 5) / 10) * 10;
        return INITIAL_BALANCE - roundedAmount;
    }
}
