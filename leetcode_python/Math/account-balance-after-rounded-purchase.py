"""

2806. Account Balance After Rounded Purchase
Easy

Initially, you have a bank account balance of 100 dollars.

You are given an integer purchaseAmount representing the amount you will spend on a purchase in dollars, in other words, its price.

When making the purchase, first the purchaseAmount is rounded to the nearest multiple of 10. Let us call this value roundedAmount. Then, roundedAmount dollars are removed from your bank account.

Return an integer denoting your final bank account balance after this purchase.

Notes:

0 is considered to be a multiple of 10 in this problem.
When rounding, 5 is rounded upward (5 is rounded to 10, 15 is rounded to 20, 25 to 30, and so on).


Example 1:

Input: purchaseAmount = 9
Output: 90
Explanation:
The nearest multiple of 10 to 9 is 10. So your account balance becomes 100 - 10 = 90.

Example 2:

Input: purchaseAmount = 15
Output: 80
Explanation:
The nearest multiple of 10 to 15 is 20. So your account balance becomes 100 - 20 = 80.

Example 3:

Input: purchaseAmount = 10
Output: 90
Explanation:
10 is a multiple of 10 itself. So your account balance becomes 100 - 10 = 90.


Constraints:

0 <= purchaseAmount <= 100

"""

# V0
# IDEA : MATH (ROUND-HALF-UP TO THE NEAREST MULTIPLE OF 10)
#
#   rounding half UP to the nearest 10 is exactly
#       rounded = ((x + 5) // 10) * 10
#   adding 5 before the floor-division pushes any remainder of 5..9 into the
#   next decade while leaving 0..4 in the current one.
#
#   NOTE : the problem's tie rule (5 goes UP) is what makes the +5 shift
#          correct. python's built-in round() would NOT work here : it does
#          banker's rounding, so round(0.5) == 0 and round(2.5) == 2.
#
#   NOTE : x <= 100, and (100 + 5) // 10 * 10 == 100, so the balance never
#          goes below 0.
#
# time = O(1), space = O(1)
class Solution(object):
    def accountBalanceAfterPurchase(self, purchaseAmount):
        rounded = ((purchaseAmount + 5) // 10) * 10
        return 100 - rounded
