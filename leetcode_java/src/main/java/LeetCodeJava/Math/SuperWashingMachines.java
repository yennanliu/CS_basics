package LeetCodeJava.Math;

// https://leetcode.com/problems/super-washing-machines/

/**
 *  517. Super Washing Machines
 *  Hard
 *
 *  You have n super washing machines on a line. Initially, each washing machine has
 *  some dresses or is empty.
 *
 *  For each move, you could choose any m (1 <= m <= n) washing machines, and pass one
 *  dress of each washing machine to one of its adjacent washing machines at the same time.
 *
 *  Given an integer array machines representing the number of dresses in each washing
 *  machine from left to right on the line, return the minimum number of moves to make all
 *  the washing machines have the same number of dresses. If it is not possible to do it,
 *  return -1.
 *
 *  Example 1:
 *    Input: machines = [1,0,5]
 *    Output: 3
 *
 *  Example 2:
 *    Input: machines = [0,3,0]
 *    Output: 2
 *
 *  Example 3:
 *    Input: machines = [0,2,0]
 *    Output: -1
 *
 *  Constraints:
 *    n == machines.length
 *    1 <= n <= 10^4
 *    0 <= machines[i] <= 10^5
 */
public class SuperWashingMachines {

    // V0
    // IDEA: greedy - the answer is bounded by (a) the max |prefix imbalance| that must cross
    //       any single gap, and (b) the max surplus a single machine must ship out (it can
    //       only give away 1 dress per move per side, so surplus needs that many moves)
    /**
     * time = O(n)
     * space = O(1)
     */
    public int findMinMoves(int[] machines) {
        int n = machines.length;
        long total = 0;
        for (int m : machines) {
            total += m;
        }
        if (total % n != 0) {
            return -1;
        }
        long target = total / n;
        long res = 0;
        long toRight = 0; // running prefix imbalance
        for (int m : machines) {
            toRight += m - target;
            res = java.lang.Math.max(res, java.lang.Math.max(java.lang.Math.abs(toRight), m - target));
        }
        return (int) res;
    }
}
