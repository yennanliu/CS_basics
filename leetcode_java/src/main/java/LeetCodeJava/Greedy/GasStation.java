package LeetCodeJava.Greedy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// https://leetcode.com/problems/gas-station/
/**
 * 134. Gas Station
 * Solved
 * Medium
 * Topics
 * Companies
 * There are n gas stations along a circular route, where the amount of gas at the ith station is gas[i].
 *
 * You have a car with an unlimited gas tank and it costs cost[i] of gas to travel from the ith station to its next (i + 1)th station. You begin the journey with an empty tank at one of the gas stations.
 *
 * Given two integer arrays gas and cost, return the starting gas station's index if you can travel around the circuit once in the clockwise direction, otherwise return -1. If there exists a solution, it is guaranteed to be unique.
 *
 *
 *
 * Example 1:
 *
 * Input: gas = [1,2,3,4,5], cost = [3,4,5,1,2]
 * Output: 3
 * Explanation:
 * Start at station 3 (index 3) and fill up with 4 unit of gas. Your tank = 0 + 4 = 4
 * Travel to station 4. Your tank = 4 - 1 + 5 = 8
 * Travel to station 0. Your tank = 8 - 2 + 1 = 7
 * Travel to station 1. Your tank = 7 - 3 + 2 = 6
 * Travel to station 2. Your tank = 6 - 4 + 3 = 5
 * Travel to station 3. The cost is 5. Your gas is just enough to travel back to station 3.
 * Therefore, return 3 as the starting index.
 * Example 2:
 *
 * Input: gas = [2,3,4], cost = [3,4,3]
 * Output: -1
 * Explanation:
 * You can't start at station 0 or 1, as there is not enough gas to travel to the next station.
 * Let's start at station 2 and fill up with 4 unit of gas. Your tank = 0 + 4 = 4
 * Travel to station 0. Your tank = 4 - 3 + 2 = 3
 * Travel to station 1. Your tank = 3 - 3 + 3 = 3
 * You cannot travel back to station 2, as it requires 4 unit of gas but you only have 3.
 * Therefore, you can't travel around the circuit once no matter where you start.
 *
 *
 * Constraints:
 *
 * n == gas.length == cost.length
 * 1 <= n <= 105
 * 0 <= gas[i], cost[i] <= 104
 *
 */
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

    // V1-1
    // https://neetcode.io/problems/gas-station
    // IDEA:  BRUTE FORCE
    public int canCompleteCircuit_1_1(int[] gas, int[] cost) {
        int n = gas.length;

        for (int i = 0; i < n; i++) {
            int tank = gas[i] - cost[i];
            if (tank < 0) continue;

            int j = (i + 1) % n;
            while (j != i) {
                tank += gas[j] - cost[j];
                if (tank < 0) break;
                j = (j + 1) % n;
            }

            if (j == i) return i;
        }
        return -1;
    }

    // V1-2
    // https://neetcode.io/problems/gas-station
    // IDEA: 2 POINTERS
    public int canCompleteCircuit_1_2(int[] gas, int[] cost) {
        int n = gas.length;
        int start = n - 1, end = 0;
        int tank = gas[start] - cost[start];
        while (start > end) {
            if (tank < 0) {
                start--;
                tank += gas[start] - cost[start];
            } else {
                tank += gas[end] - cost[end];
                end++;
            }
        }
        return tank >= 0 ? start : -1;
    }


    // V1-3
    // https://neetcode.io/problems/gas-station
    // IDEA: GREEDY
    public int canCompleteCircuit_1_3(int[] gas, int[] cost) {
        if (Arrays.stream(gas).sum() < Arrays.stream(cost).sum()) {
            return -1;
        }

        int total = 0;
        int res = 0;
        for (int i = 0; i < gas.length; i++) {
            total += (gas[i] - cost[i]);

            if (total < 0) {
                total = 0;
                res = i + 1;
            }
        }

        return res;
    }

    
    // V2
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
