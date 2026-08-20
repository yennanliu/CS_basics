package LeetCodeJava.Design;

// https://leetcode.com/problems/design-an-atm-machine/

/**
 *  2241. Design an ATM Machine
 *  Medium
 *
 *  There is an ATM machine that stores banknotes of 5 denominations: 20, 50, 100, 200,
 *  and 500 dollars. Initially the ATM is empty. The user can use the machine to deposit
 *  or withdraw any amount of money.
 *
 *  When withdrawing, the machine prioritizes using banknotes of LARGER values.
 *  For example, if you want to withdraw $600 and there are 3 $200 banknotes and 1 $500
 *  banknote, then the withdraw request is REJECTED, because the machine first takes the
 *  $500 banknote and is then unable to make up the remaining $100. It is not allowed to
 *  use the $200 banknotes instead of the $500 banknote.
 *
 *  Implement the ATM class:
 *
 *   - ATM() Initializes the ATM object.
 *   - void deposit(int[] banknotesCount) Deposits new banknotes in the order $20, $50,
 *     $100, $200, and $500.
 *   - int[] withdraw(int amount) Returns an array of length 5 of the number of banknotes
 *     that will be handed to the user in the order $20, $50, $100, $200, and $500, and
 *     updates the number of banknotes in the ATM after withdrawing. Returns [-1] if it
 *     is not possible (do not withdraw any banknotes in this case).
 *
 *  Example 1:
 *    Input
 *      ["ATM","deposit","withdraw","deposit","withdraw","withdraw"]
 *      [[],[[0,0,1,2,1]],[600],[[0,1,0,1,1]],[600],[550]]
 *    Output
 *      [null,null,[0,0,1,0,1],null,[-1],[0,1,0,0,1]]
 *    Explanation
 *      withdraw(600) uses 1 $100 and 1 $500; the machine holds [0,0,0,2,0] afterwards.
 *      the second withdraw(600) is rejected (a $500 leaves $100 it cannot make), and
 *      the inventory is left untouched.
 *      withdraw(550) uses 1 $50 and 1 $500.
 *
 *  Constraints:
 *    banknotesCount.length == 5
 *    0 <= banknotesCount[i] <= 10^9
 *    1 <= amount <= 10^9
 *    At most 5000 calls in total will be made to withdraw and deposit.
 *    At most 10 calls will be made to withdraw.
 */
public class DesignAnATMMachine {

    // V0
    // IDEA: FIXED LARGEST-FIRST GREEDY, COMMITTED ONLY IF IT FULLY SUCCEEDS
    //
    //   the machine's rule is NOT "find any combination" -- it is a FORCED greedy
    //   from the largest denomination down:
    //       take = min(count[i], amount / value[i])   for i from 500 down to 20
    //
    //   the key detail is the ROLLBACK: if any remainder is left over after the $20
    //   notes, the request is rejected and the inventory must be UNTOUCHED. so the
    //   plan is computed into a scratch array first and only applied once the
    //   remaining amount reaches exactly 0.
    //
    //   NOTE: deposits accumulate (up to 5000 calls x 10^9 notes), so the inventory
    //         counters must be long, not int.
    /**
     * time = O(1) per call (5 denominations)
     * space = O(1)
     */
    private static final int[] VALUES = {20, 50, 100, 200, 500};

    private final long[] count = new long[5];

    public DesignAnATMMachine() {
    }

    public void deposit(int[] banknotesCount) {
        for (int i = 0; i < 5; i++) {
            this.count[i] += banknotesCount[i];
        }
    }

    public int[] withdraw(int amount) {
        int[] take = new int[5];
        long remain = amount;
        for (int i = 4; i >= 0; i--) {           // largest denomination first
            long t = Math.min(this.count[i], remain / VALUES[i]);
            take[i] = (int) t;
            remain -= t * VALUES[i];
        }
        if (remain != 0) {
            return new int[]{-1};               // nothing is deducted
        }
        for (int i = 0; i < 5; i++) {
            this.count[i] -= take[i];
        }
        return take;
    }
}
