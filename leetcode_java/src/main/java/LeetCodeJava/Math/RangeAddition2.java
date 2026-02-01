package LeetCodeJava.Math;

// https://leetcode.com/problems/range-addition-ii/description/
/**
 *
 Code
 Testcase
 Test Result
 598. Range Addition II
 Solved
 Easy
 Topics
 Companies
 You are given an m x n matrix M initialized with all 0's and an array of operations ops, where ops[i] = [ai, bi] means M[x][y] should be incremented by one for all 0 <= x < ai and 0 <= y < bi.

 Count and return the number of maximum integers in the matrix after performing all the operations.



 Example 1:


 Input: m = 3, n = 3, ops = [[2,2],[3,3]]
 Output: 4
 Explanation: The maximum integer in M is 2, and there are four of it in M. So return 4.
 Example 2:

 Input: m = 3, n = 3, ops = [[2,2],[3,3],[3,3],[3,3],[2,2],[3,3],[3,3],[3,3],[2,2],[3,3],[3,3],[3,3]]
 Output: 4
 Example 3:

 Input: m = 3, n = 3, ops = []
 Output: 9


 Constraints:

 1 <= m, n <= 4 * 104
 0 <= ops.length <= 104
 ops[i].length == 2
 1 <= ai <= m
 1 <= bi <= n
 *
 */
public class RangeAddition2 {

    // V0
//    public int maxCount(int m, int n, int[][] ops) {
//
//    }

    // V1

    // V2

    // https://leetcode.ca/2017-07-20-598-Range-Addition-II/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxCount_2(int m, int n, int[][] ops) {
        for (int[] op : ops) {
            m = Math.min(m, op[0]);
            n = Math.min(n, op[1]);
        }
        return m * n;
    }

}
