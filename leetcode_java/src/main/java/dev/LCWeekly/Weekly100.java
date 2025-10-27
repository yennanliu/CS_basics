package dev.LCWeekly;

// https://leetcode.com/contest/biweekly-contest-100/
// https://leetcode.cn/contest/biweekly-contest-100/
public class Weekly100 {

    // Q1
    // LC 2591
    // 18.19 - 29 PM
    // https://leetcode.com/problems/distribute-money-to-maximum-children/
    /**
     *
     *  -> Return the maximum number of children
     *     who may receive exactly 8 dollars
     *     or -1 if NOT possible
     *
     *     - money: amount of money
     *     - children: # of children
     *
     *    rule:
     *     - All money must be distributed.
     *     - Everyone must receive at least 1 dollar.
     *     - Nobody receives 4 dollars.
     *
     *
     *
     *   IDEA 1) BRUTE FORCE
     *
     *
     *   ex 1) m = 20, c = 3
     *
     *   -> cnt = 2, rem = 20 - 8* 2 = 4
     *   -> cnt = 2-1 = 1
     *
     *   ex 2) m = 28, c = 3
     *
     *   -> cnt = 3, rem = 28 - 8*3 = 4
     *   ->
     *
     *
     */
//    public int distMoney(int money, int children) {
//        // edge
//        if(children == 0 || money == 0){
//            return -1;
//        }
//        int cnt = money / 8;
////        if(cnt <= 0){
////            return -1;
////        }
//        if(money == children){
//            return 0;
//        }
//        if(cnt == children){
//            return children;
//        }
//
//        //int cnt = money / 8;
//        //int ans = 0;
//        int res = money - cnt * 8;
//
//        return res == 4 ? cnt - 1: cnt;
//    }

    // FIX by gpt
    public int distMoney(int money, int children) {
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



    // Q2
    // https://leetcode.com/problems/maximize-greatness-of-an-array/

    // Q3
    // https://leetcode.com/problems/find-score-of-an-array-after-marking-all-elements/description/

    // Q4
    // https://leetcode.com/problems/minimum-time-to-repair-cars/description/

}
