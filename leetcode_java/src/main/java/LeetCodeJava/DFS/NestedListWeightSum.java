package LeetCodeJava.DFS;

// https://leetcode.com/problems/nested-list-weight-sum/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 *  339. Nested List Weight Sum
 *  Medium
 *
 *  You are given a nested list of integers nestedList. Each element is either an integer
 *  or a list whose elements may also be integers or other lists.
 *
 *  The depth of an integer is the number of lists that it is inside of. For example, the
 *  nested list [1,[2,2],[[3],2],1] has each integer's value set to its depth.
 *
 *  Return the sum of each integer in nestedList multiplied by its depth.
 *
 *  Example 1:
 *  Input: nestedList = [[1,1],2,[1,1]]
 *  Output: 10
 *  Explanation: Four 1's at depth 2, one 2 at depth 1. 1*2 + 1*2 + 2*1 + 1*2 + 1*2 = 10.
 *
 *  Example 2:
 *  Input: nestedList = [1,[4,[6]]]
 *  Output: 27
 *  Explanation: One 1 at depth 1, one 4 at depth 2, and one 6 at depth 3. 1 + 4*2 + 6*3 = 27.
 *
 *  Constraints:
 *  1 <= nestedList.length <= 50
 *  The values of the integers in the nested list is in the range [-100, 100].
 *  The maximum depth of any integer is less than or equal to 50.
 */
public class NestedListWeightSum {

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
    // IDEA: DFS - carry the current depth down and weight every integer by it
    /**
     * time = O(n)   // n = total number of integers + lists
     * space = O(d)  // d = max nesting depth (recursion stack)
     */
    public int depthSum(List<NestedInteger> nestedList) {
        return dfs(nestedList, 1);
    }

    private int dfs(List<NestedInteger> list, int depth) {
        int sum = 0;
        if (list == null) {
            return 0;
        }
        for (NestedInteger ni : list) {
            if (ni.isInteger()) {
                sum += ni.getInteger() * depth;
            } else {
                sum += dfs(ni.getList(), depth + 1);
            }
        }
        return sum;
    }

    // V1
    // IDEA: BFS - process the structure level by level, depth == level index
    /**
     * time = O(n)
     * space = O(n)
     */
    public int depthSum_1(List<NestedInteger> nestedList) {
        if (nestedList == null || nestedList.isEmpty()) {
            return 0;
        }
        int depth = 1;
        int res = 0;
        Deque<NestedInteger> queue = new ArrayDeque<>(nestedList);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                NestedInteger ni = queue.poll();
                if (ni.isInteger()) {
                    res += ni.getInteger() * depth;
                } else {
                    List<NestedInteger> sub = ni.getList();
                    if (sub != null) {
                        queue.addAll(new ArrayList<>(sub));
                    }
                }
            }
            depth++;
        }
        return res;
    }
}
