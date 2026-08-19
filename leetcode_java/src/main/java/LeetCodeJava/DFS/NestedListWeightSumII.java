package LeetCodeJava.DFS;

// https://leetcode.com/problems/nested-list-weight-sum-ii/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 *  364. Nested List Weight Sum II
 *  Medium
 *
 *  You are given a nested list of integers nestedList. Each element is either an integer
 *  or a list whose elements may also be integers or other lists.
 *
 *  The depth of an integer is the number of lists that it is inside of. Let maxDepth be the
 *  maximum depth of any integer. The weight of an integer is maxDepth - (the depth of the
 *  integer) + 1.
 *
 *  Return the sum of each integer in nestedList multiplied by its weight.
 *
 *  Example 1:
 *  Input: nestedList = [[1,1],2,[1,1]]
 *  Output: 8
 *  Explanation: Four 1's with a weight of 1, one 2 with a weight of 2.
 *
 *  Example 2:
 *  Input: nestedList = [1,[4,[6]]]
 *  Output: 17
 *  Explanation: One 1 at depth 3, one 4 at depth 2, and one 6 at depth 1.
 *  1*3 + 4*2 + 6*1 = 17.
 *
 *  Constraints:
 *  1 <= nestedList.length <= 50
 *  The values of the integers in the nested list is in the range [-100, 100].
 *  The maximum depth of any integer is less than or equal to 50.
 */
public class NestedListWeightSumII {

    /**
     * This is the interface that allows for creating nested lists.
     * You should not implement it, or speculate about its implementation.
     */
    public interface NestedInteger {
        // @return true if this NestedInteger holds a single integer, rather than a nested list.
        public boolean isInteger();

        // @return the single integer that this NestedInteger holds, if it holds a single integer
        // Return null if this NestedInteger holds a nested list
        public Integer getInteger();

        // Set this NestedInteger to hold a single integer.
        public void setInteger(int value);

        // Set this NestedInteger to hold a nested list and adds a nested integer to it.
        public void add(NestedInteger ni);

        // @return the nested list that this NestedInteger holds, if it holds a nested list
        // Return empty list if this NestedInteger holds a single integer
        public List<NestedInteger> getList();
    }

    // V0
    // IDEA: DFS twice - first get maxDepth, then weight each integer by (maxDepth - depth + 1)
    /**
     * time = O(n)
     * space = O(d)   // d = max nesting depth
     */
    public int depthSumInverse(List<NestedInteger> nestedList) {
        int maxDepth = maxDepth(nestedList, 1);
        return weightedSum(nestedList, 1, maxDepth);
    }

    private int maxDepth(List<NestedInteger> list, int depth) {
        int res = depth;
        if (list == null) {
            return res;
        }
        for (NestedInteger ni : list) {
            if (!ni.isInteger()) {
                res = Math.max(res, maxDepth(ni.getList(), depth + 1));
            }
        }
        return res;
    }

    private int weightedSum(List<NestedInteger> list, int depth, int maxDepth) {
        int sum = 0;
        if (list == null) {
            return 0;
        }
        for (NestedInteger ni : list) {
            if (ni.isInteger()) {
                sum += ni.getInteger() * (maxDepth - depth + 1);
            } else {
                sum += weightedSum(ni.getList(), depth + 1, maxDepth);
            }
        }
        return sum;
    }

    // V1
    // IDEA: BFS one pass - keep a running `levelSum` prefix; adding it into `res` on every
    //       level makes shallower integers counted more times (= larger weight)
    /**
     * time = O(n)
     * space = O(n)
     */
    public int depthSumInverse_1(List<NestedInteger> nestedList) {
        if (nestedList == null || nestedList.isEmpty()) {
            return 0;
        }
        Deque<NestedInteger> queue = new ArrayDeque<>(nestedList);
        int levelSum = 0;
        int res = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                NestedInteger ni = queue.poll();
                if (ni.isInteger()) {
                    levelSum += ni.getInteger();
                } else {
                    List<NestedInteger> sub = ni.getList();
                    if (sub != null) {
                        queue.addAll(new ArrayList<>(sub));
                    }
                }
            }
            res += levelSum;
        }
        return res;
    }
}
