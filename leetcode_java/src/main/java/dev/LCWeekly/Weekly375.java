package dev.LCWeekly;

/**
 * LeetCode weekly contest 375
 * https://leetcode.com/contest/weekly-contest-375/
 * 中文題目
 * https://leetcode.cn/contest/weekly-contest-375/
 */
public class Weekly375 {

    // Q1
    // LC 2960
    // https://leetcode.com/problems/count-tested-devices-after-test-operations/
    // 15.08 - 18 pm
    /**
     *
     *  -> Return an integer denoting (表示) the number of devices
     *     that `will be tested` after
     *     performing the `test operations` `in order`.
     *
     *           - 0 idx based arr: batteryPercentages
     *               - battery pct of device n (0 to n-1)
     *    - OP:
     *      - if batteryPercentages[i] > 0, do below:
     *           - 1. increase cnt of tested devices
     *           - 2. decrease battery pct of all devices
     *                in [i+1, n-1] range by 1,
     *                and ensure that battery pct  >= 0
     *                e.g. batteryPercentages[j] = max(0, batteryPercentages[j] - 1)
     *
     *
     *  -------------
     *
     *      IDEA 1) BRUTE FORCE
     *
     *      IDEA 2) DIFF (PREFIX SUM) ARRAY ???
     *
     *
     *  -------------
     *
     *
     *  ex 1)
     *  Input: batteryPercentages = [1,1,2,1,3]
     *  Output: 3
     *
     *  ->
     *  // batteryPercentages[j] = Math.max(0, batteryPercentages[j-1]);
     *
     *  [1,1,2,1,3] b[i] > 0, do op,  [1,0,1,0,2]
     *   x
     *
     *  [1,0,1,0,2] b[i] == 0, NOT do op
     *     x
     *
     * [1,0,1,0,2] b[i] > 0, do op, [1,0,1,0,1]
     *      x
     *
     * [1,0,1,0,2] b[i] == 0, NOT do op
     *        x
     *
     * [1,0,1,0,2] b[i] > 0, but `no remaining element to do op`
     *          x
     *
     *
     *  ex 2)
     *
     *  Input: batteryPercentages = [0,1,2]
     *  Output: 2
     *
     *
     *  ->
     *
     *  [0,1,2]
     *   x
     *
     *  [0,1,2]   ans=1, [0,1,1]
     *     x
     *
     *  [0,1,1] ans=2
     *       x
     *
     */
    public int countTestedDevices(int[] batteryPercentages) {
        int ans = 0;
        // edge
        if(batteryPercentages == null || batteryPercentages.length == 0){
            return 0;
        }
        // ???
        if(batteryPercentages.length == 1){
            if(batteryPercentages[0] > 0){
                ans += 1;
            }
            return ans;
        }

        // brute force
        for(int i = 0; i < batteryPercentages.length; i++){
            // op trigger condition
            if(batteryPercentages[i] > 0){
                // op 1
                ans += 1;
                // op 2
                for(int j = i+1; j < batteryPercentages.length; j++){
                    batteryPercentages[j] = Math.max(0, batteryPercentages[j-1]);
                }

            }
        }

        return ans;
    }


    // Q2
    // LC 2961
    // https://leetcode.com/problems/double-modular-exponentiation/description/


    // Q3
    // LC 2962
    // https://leetcode.com/problems/count-subarrays-where-max-element-appears-at-least-k-times/description/

    // Q4
    // LC 2963
    // https://leetcode.com/problems/count-the-number-of-good-partitions/description/



}
