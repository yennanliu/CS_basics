package LeetCodeJava.Array;

import java.util.Map;
import java.util.TreeMap;

// https://leetcode.com/problems/lemonade-change/

/**
 *  860. Lemonade Change
 *  Easy
 *
 *  At a lemonade stand, each lemonade costs $5. Customers are standing in a queue
 *  to buy from you and order one at a time (in the order specified by bills).
 *  Each customer will only buy one lemonade and pay with either a $5, $10, or $20
 *  bill. You must provide the correct change to each customer so that the net
 *  transaction is that the customer pays $5.
 *
 *  Note that you do not have any change in hand at first.
 *
 *  Given an integer array bills where bills[i] is the bill the ith customer pays,
 *  return true if you can provide every customer with the correct change, or
 *  false otherwise.
 *
 *  Example 1:
 *  Input: bills = [5,5,5,10,20]
 *  Output: true
 *
 *  Example 2:
 *  Input: bills = [5,5,10,10,20]
 *  Output: false
 *
 *  Constraints:
 *   - 1 <= bills.length <= 10^5
 *   - bills[i] is either 5, 10, or 20.
 */
public class LemonadeChange {

    // V0
    // IDEA: greedy - only $5 and $10 bills can be used as change, so track their
    //       counts; for a $20 prefer giving 10+5 (a $5 is more flexible than $10)
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean lemonadeChange(int[] bills) {
        if (bills == null || bills.length == 0) {
            return true;
        }
        int five = 0;
        int ten = 0;
        for (int b : bills) {
            if (b == 5) {
                five++;
            } else if (b == 10) {
                if (five == 0) {
                    return false;
                }
                five--;
                ten++;
            } else { // b == 20
                if (ten > 0 && five > 0) {
                    ten--;
                    five--;
                } else if (five >= 3) {
                    five -= 3;
                } else {
                    return false;
                }
            }
        }
        return true;
    }


    // V1
    // IDEA: general change-making over a TreeMap "wallet" of the bills actually held -
    //       pay each customer's change with the LARGEST usable bill first.
    //       (generalises to any denomination set, unlike the two-counter V0)
    /**
     * time = O(n log d), d = number of distinct denominations
     * space = O(d)
     */
    public boolean lemonadeChange_1(int[] bills) {
        if (bills == null || bills.length == 0) {
            return true;
        }
        TreeMap<Integer, Integer> wallet = new TreeMap<>();
        for (int b : bills) {
            int change = b - 5;
            while (change > 0) {
                Map.Entry<Integer, Integer> e = wallet.floorEntry(change);
                if (e == null) {
                    return false;
                }
                int bill = e.getKey();
                int cnt = e.getValue();
                int use = Math.min(cnt, change / bill);
                change -= use * bill;
                if (use == cnt) {
                    wallet.remove(bill);
                } else {
                    wallet.put(bill, cnt - use);
                }
            }
            Integer had = wallet.get(b);
            wallet.put(b, had == null ? 1 : had + 1);
        }
        return true;
    }

    // V2
    // IDEA: brute force - recursively try EVERY way of making change ($20 can be
    //       10+5 or 5+5+5); kept as a readable correctness reference that proves
    //       the greedy of V0 never misses a feasible schedule. Exponential in the
    //       number of $20 bills, so small inputs only.
    /**
     * time = O(2^t), t = count of $20 bills
     * space = O(n) recursion depth
     */
    public boolean lemonadeChange_2(int[] bills) {
        if (bills == null || bills.length == 0) {
            return true;
        }
        return dfs_2(bills, 0, 0, 0);
    }

    private boolean dfs_2(int[] bills, int i, int five, int ten) {
        if (i == bills.length) {
            return true;
        }
        int b = bills[i];
        if (b == 5) {
            return dfs_2(bills, i + 1, five + 1, ten);
        }
        if (b == 10) {
            if (five == 0) {
                return false;
            }
            return dfs_2(bills, i + 1, five - 1, ten + 1);
        }
        // b == 20 -> two possible ways of giving $15 back
        if (ten > 0 && five > 0 && dfs_2(bills, i + 1, five - 1, ten - 1)) {
            return true;
        }
        if (five >= 3 && dfs_2(bills, i + 1, five - 3, ten)) {
            return true;
        }
        return false;
    }
}
