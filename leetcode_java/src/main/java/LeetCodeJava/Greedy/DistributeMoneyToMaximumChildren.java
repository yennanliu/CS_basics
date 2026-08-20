package LeetCodeJava.Greedy;

// https://leetcode.com/problems/distribute-money-to-maximum-children/description/
/**
 * 2591. Distribute Money to Maximum Children
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an integer money denoting the amount of money (in dollars) that you have and another integer children denoting the number of children that you must distribute the money to.
 *
 * You have to distribute the money according to the following rules:
 *
 * All money must be distributed.
 * Everyone must receive at least 1 dollar.
 * Nobody receives 4 dollars.
 * Return the maximum number of children who may receive exactly 8 dollars if you distribute the money according to the aforementioned rules. If there is no way to distribute the money, return -1.
 *
 *
 *
 * Example 1:
 *
 * Input: money = 20, children = 3
 * Output: 1
 * Explanation:
 * The maximum number of children with 8 dollars will be 1. One of the ways to distribute the money is:
 * - 8 dollars to the first child.
 * - 9 dollars to the second child.
 * - 3 dollars to the third child.
 * It can be proven that no distribution exists such that number of children getting 8 dollars is greater than 1.
 * Example 2:
 *
 * Input: money = 16, children = 2
 * Output: 2
 * Explanation: Each child can be given 8 dollars.
 *
 *
 * Constraints:
 *
 * 1 <= money <= 200
 * 2 <= children <= 30
 *
 *
 *
 */
public class DistributeMoneyToMaximumChildren {

    // V0
    // IDEA: GREEDY
    /**
     *  Give everyone $1 first (mandatory), then hand out $7 more
     *  to as many children as possible (so they hold exactly $8).
     *
     *  2 corner cases break the naive greedy:
     *   1) every child got $8 but money is LEFT OVER
     *      -> the leftover must be dumped on somebody, so that child
     *         no longer holds exactly $8  => cnt - 1
     *   2) exactly ONE child is left and only $3 remain
     *      -> that child would hold 1 + 3 = $4, which is forbidden,
     *         so we must break one of the $8 children => cnt - 1
     *
     *  time = O(1), space = O(1)
     */
    public int distMoney(int money, int children) {
        // everyone needs at least $1
        if (money < children) {
            return -1;
        }

        // step 1) $1 for everybody
        money -= children;

        // step 2) $7 more -> exactly $8
        int cnt = Math.min(money / 7, children);
        money -= cnt * 7;
        children -= cnt;

        // corner 1) leftover money with nobody left to take it
        if (children == 0 && money > 0) {
            return cnt - 1;
        }

        // corner 2) last child would end up with 1 + 3 = $4
        if (children == 1 && money == 3) {
            return cnt - 1;
        }

        return cnt;
    }

    // V1
    // IDEA: BRUTE FORCE (fixed by gpt)
    public int distMoney_1(int money, int children) {
        // Each child needs at least $1
        if (money < children)
            return -1;

        // Give each child $1 first
        money -= children;

        // Now see how many can get $7 more (so total $8)
        int cnt = Math.min(money / 7, children);
        money -= cnt * 7;
        children -= cnt;

        // If all children get $8 and no money left, perfect
        if (children == 0 && money == 0)
            return cnt;

        // If all children got $8 but still have money left, one must get >$8, invalid
        if (children == 0 && money > 0)
            return cnt - 1;

        // Special case: if one child left and they would end up with $4, that breaks the rule
        if (children == 1 && money == 3)
            return cnt - 1;

        return cnt;
    }

    // V2
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/distribute-money-to-maximum-children/solutions/5332619/java-solution-beats-100-by-star-dust24-lqs0/
    public int distMoney_2(int money, int children) {

        if (money < children)
            return -1;
        if (money < 1 || money > 200)
            return -1;
        if (children < 2 || children > 30)
            return -1;

        int count = 0;
        while (money != 0 && children != 0) {
            money = money - 8;
            children--;
            if (children == 1 && money > 8) {
                count++;
                break;
            }

            if ((money != 4 || children != 1) && money >= children) {
                count++;
            } else
                break;
        }

        return count;
    }

    // V3
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/distribute-money-to-maximum-children/solutions/7303244/using-brute-force-by-kalashri_daivadnya-afbz/
    public int distMoney_3(int money, int children) {
        // Base cases
        if (money < children)
            return -1;
        if (money == 8 && children == 1)
            return 1;
        if (money < 8)
            return 0;

        int temp = money - children;
        int count = 0;

        for (int i = 0; i < children; i++) {

            if (temp == 3 && (children - i) == 1) {
                count--;
                break;
            }
            if (temp < 7)
                break;

            temp -= 7;
            count++;
        }

        if (count == children && temp > 0)
            count--;

        return count;
    }

    // V4

}
