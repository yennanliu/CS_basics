package LeetCodeJava.Greedy;

// https://leetcode.com/problems/gas-station/

import java.util.ArrayList;
import java.util.List;

public class GasStation {

    // VO
    // IDEA: GREEDY
    // https://www.bilibili.com/video/BV1jA411r7WX/?share_source=copy_web&vd_source=49348a1975630e1327042905e52f488a
    /**
     *  total_gain = gain1 + gain2 + ....
     *
     *  if total_gain > 0, then gain1 + gain2 + ... >= 0
     *
     *  then gain1 >= - (gain2 + gain3 + ...)
     *
     *  -> means car start from such idx (gain1) is ENOUGH to visit all stops
     *
     */
    public int canCompleteCircuit(int[] gas, int[] cost) {

        if (gas == null || cost == null || gas.length == 0 || cost.length == 0){
            return 0;
        }

        int remain = 0;
        int total = 0;
        int start = 0;

        for (int i = 0; i < gas.length; i++){
            remain += (gas[i] - cost[i]);
            // keep maintaining total, since NEED to know whether this start point can finish visit all stops
            // example : we start again on idx = 3, still need to know if car can go idx = 3, 4 ... N .. 0 and back to 3
            total += (gas[i] - cost[i]);

            if (remain < 0){
                remain = 0;
                start = i+1;
            }
        }

       return total < 0 ? -1 : start;
    }

//    public int canCompleteCircuit(int[] gas, int[] cost) {
//
//        if (gas == null || cost == null || gas.length == 0 || cost.length == 0){
//            return 0;
//        }
//
//        List<Integer> diffList = new ArrayList();
//
//        diffList.add(gas[1] - cost[cost.length-1]);
//
//        for (int i = 1; i < gas.length; i++){
//            int diff = gas[i] - cost[i-1];
//            diffList.add(diff);
//        }
//
//        // reset start point if current sum < diff
//        for (int i = 0; i < diffList.size(); i++){
//            if (diffList.get(i) > 0){
//                return i;
//            }
//        }
//        return -1;
//    }

    // V1
    // IDEA : ONE PASS
    // https://leetcode.com/problems/gas-station/editorial/
    public int canCompleteCircuit_2(int[] gas, int[] cost) {
        int currGain = 0, totalGain = 0, answer = 0;

        for (int i = 0; i < gas.length; ++i) {
            // gain[i] = gas[i] - cost[i]
            totalGain += gas[i] - cost[i];
            currGain += gas[i] - cost[i];

            // If we meet a "valley", start over from the next station
            // with 0 initial gas.
            if (currGain < 0) {
                answer = i + 1;
                currGain = 0;
            }
        }

        return totalGain >= 0 ? answer : -1;
    }

}
