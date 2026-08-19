package LeetCodeJava.Tree;

// https://leetcode.com/problems/create-sorted-array-through-instructions/

/**
 *  1649. Create Sorted Array through Instructions
 *  Hard
 *
 *  Given an integer array instructions, you are asked to create a sorted array
 *  from the elements in instructions. You start with an empty container nums.
 *  For each element from left to right in instructions, insert it into nums.
 *  The cost of each insertion is the minimum of the following:
 *
 *   - The number of elements currently in nums that are strictly less than
 *     instructions[i].
 *   - The number of elements currently in nums that are strictly greater than
 *     instructions[i].
 *
 *  For example, if inserting element 3 into nums = [1,2,3,5], the cost of
 *  insertion is min(2, 1) and nums will become [1,2,3,3,5].
 *
 *  Return the total cost to insert all elements from instructions into nums.
 *  Since the answer may be large, return it modulo 10^9 + 7.
 *
 *  Example 1:
 *    Input: instructions = [1,5,6,2]
 *    Output: 1
 *    Explanation: costs are min(0,0) + min(1,0) + min(2,0) + min(1,2) = 1
 *
 *  Example 2:
 *    Input: instructions = [1,2,3,6,5,4]
 *    Output: 3
 *
 *  Example 3:
 *    Input: instructions = [1,3,3,3,2,4,2,1,2]
 *    Output: 4
 *
 *  Constraints:
 *    1 <= instructions.length <= 10^5
 *    1 <= instructions[i] <= 10^5
 */
public class CreateSortedArrayThroughInstructions {

    // V0
    // IDEA: BINARY INDEXED TREE (Fenwick) as a live frequency histogram
    //       values are bounded by 10^5, so keep a BIT over VALUES where cell v
    //       holds "how many copies of v are already inserted".
    //       before inserting the i-th value x (i elements present):
    //         less    = query(x - 1)
    //         greater = i - query(x)     // total - #(<= x)
    //         cost    = min(less, greater)
    //       then update(x, +1).
    //       NOTE: `greater` must use query(x), NOT query(x-1) -- equal values
    //             count as neither strictly less nor strictly greater.
    /**
     * time = O(N log M)   // M = max value
     * space = O(M)
     */
    public int createSortedArray(int[] instructions) {
        final int MOD = 1000000007;
        int m = 0;
        for (int x : instructions) {
            m = Math.max(m, x);
        }
        int[] tree = new int[m + 1];

        long res = 0;
        for (int i = 0; i < instructions.length; i++) {
            int x = instructions[i];
            int less = query(tree, x - 1);
            int greater = i - query(tree, x);
            res += Math.min(less, greater);
            res %= MOD;
            update(tree, x, m);
        }
        return (int) (res % MOD);
    }

    private void update(int[] tree, int i, int m) {
        while (i <= m) {
            tree[i] += 1;
            i += i & (-i);
        }
    }

    private int query(int[] tree, int i) {
        int s = 0;
        while (i > 0) {
            s += tree[i];
            i -= i & (-i);
        }
        return s;
    }
}
