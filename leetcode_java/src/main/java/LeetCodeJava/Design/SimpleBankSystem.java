package LeetCodeJava.Design;

// https://leetcode.com/problems/simple-bank-system/

/**
 *  2043. Simple Bank System
 *  Medium
 *
 *  You have been tasked with writing a program for a popular bank that will automate all its
 *  incoming transactions (transfer, deposit, and withdraw). The bank has n accounts numbered
 *  from 1 to n. The initial balance of each account is stored in a 0-indexed integer array
 *  balance, with the (i + 1)th account having an initial balance of balance[i].
 *
 *  Execute all the valid transactions. A transaction is valid if:
 *
 *   - The given account number(s) are between 1 and n, and
 *   - The amount of money withdrawn or transferred is less than or equal to the balance
 *     of the account.
 *
 *  Implement the Bank class:
 *
 *   - Bank(long[] balance) Initializes the object with the 0-indexed integer array balance.
 *   - boolean transfer(int account1, int account2, long money) Transfers money dollars from
 *     the account numbered account1 to the account numbered account2. Return true if the
 *     transaction was successful, false otherwise.
 *   - boolean deposit(int account, long money) Deposit money dollars into the account
 *     numbered account. Return true if the transaction was successful, false otherwise.
 *   - boolean withdraw(int account, long money) Withdraw money dollars from the account
 *     numbered account. Return true if the transaction was successful, false otherwise.
 *
 *  Example 1:
 *
 *  Input
 *  ["Bank", "withdraw", "transfer", "deposit", "transfer", "withdraw"]
 *  [[[10, 100, 20, 50, 30]], [3, 10], [5, 1, 20], [5, 20], [3, 4, 15], [10, 50]]
 *  Output
 *  [null, true, true, true, false, false]
 *
 *  Explanation
 *  Bank bank = new Bank([10, 100, 20, 50, 30]);
 *  bank.withdraw(3, 10);    // true,  account 3 : 20 -> 10
 *  bank.transfer(5, 1, 20); // true,  account 5 : 30 -> 10, account 1 : 10 -> 30
 *  bank.deposit(5, 20);     // true,  account 5 : 10 -> 30
 *  bank.transfer(3, 4, 15); // false, account 3 only holds 10
 *  bank.withdraw(10, 50);   // false, account 10 does not exist
 *
 *  Constraints:
 *
 *   n == balance.length
 *   1 <= n, account, account1, account2 <= 10^5
 *   0 <= balance[i], money <= 10^12
 *   At most 10^4 calls will be made to each function transfer, deposit, and withdraw.
 */
public class SimpleBankSystem {

    // V0
    // IDEA: PLAIN ARRAY + TWO VALIDATION HELPERS
    //       accounts are 1-indexed in the API but 0-indexed in storage, so every entry
    //       point converts once. two checks cover every rule:
    //          valid(acc)         -> 1 <= acc <= n
    //          enough(acc, money) -> balance[acc - 1] >= money
    //       NOTE: transfer must validate BOTH accounts BEFORE moving any money, so a bad
    //             destination leaves the source untouched.
    //       NOTE: balances go up to 10^12 -> must use long, not int.
    /**
     * time = O(N) to build, O(1) per transaction
     * space = O(N)
     */
    private final long[] balance;

    public SimpleBankSystem(long[] balance) {
        this.balance = balance;
    }

    public boolean transfer(int account1, int account2, long money) {
        if (!valid(account1) || !valid(account2)) {
            return false;
        }
        if (!enough(account1, money)) {
            return false;
        }
        balance[account1 - 1] -= money;
        balance[account2 - 1] += money;
        return true;
    }

    public boolean deposit(int account, long money) {
        if (!valid(account)) {
            return false;
        }
        balance[account - 1] += money;
        return true;
    }

    public boolean withdraw(int account, long money) {
        if (!valid(account) || !enough(account, money)) {
            return false;
        }
        balance[account - 1] -= money;
        return true;
    }

    private boolean valid(int account) {
        return account >= 1 && account <= balance.length;
    }

    private boolean enough(int account, long money) {
        return balance[account - 1] >= money;
    }
}
