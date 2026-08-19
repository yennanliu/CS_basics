package LeetCodeJava.Stack;

// https://leetcode.com/problems/flatten-nested-list-iterator/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/**
 *  341. Flatten Nested List Iterator
 *  Medium
 *
 *  You are given a nested list of integers nestedList. Each element is either an integer
 *  or a list whose elements may also be integers or other lists. Implement an iterator
 *  to flatten it.
 *
 *  Implement the NestedIterator class:
 *   - NestedIterator(List<NestedInteger> nestedList) initializes the iterator.
 *   - int next() returns the next integer in the nested list.
 *   - boolean hasNext() returns true if there are still some integers left.
 *
 *  Example 1:
 *  Input: nestedList = [[1,1],2,[1,1]]
 *  Output: [1,1,2,1,1]
 *
 *  Example 2:
 *  Input: nestedList = [1,[4,[6]]]
 *  Output: [1,4,6]
 *
 *  Constraints:
 *  1 <= nestedList.length <= 500
 *  The values of the integers in the nested list are in the range [-10^6, 10^6].
 */
public class FlattenNestedListIterator {

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
    // IDEA: LAZY STACK — push the top level reversed, and only unwrap sub-lists when
    //       hasNext() is asked, so nothing is flattened until it is actually needed
    /**
     * NestedIterator:
     *   ctor    time = O(n)  // n = size of the top level list
     *   next    time = O(1) amortized
     *   hasNext time = O(1) amortized
     *   space   = O(n + d)   // d = max nesting depth
     */
    public static class NestedIterator implements Iterator<Integer> {

        private final Deque<NestedInteger> stack;

        public NestedIterator(List<NestedInteger> nestedList) {
            this.stack = new ArrayDeque<>();
            pushReversed(nestedList);
        }

        @Override
        public Integer next() {
            // hasNext() guarantees the top is an integer
            if (!hasNext()) {
                return null;
            }
            return stack.pop().getInteger();
        }

        @Override
        public boolean hasNext() {
            /**
             *  NOTE: keep unwrapping lists until the stack top is a plain integer
             *        (or the stack is empty)
             */
            while (!stack.isEmpty() && !stack.peek().isInteger()) {
                List<NestedInteger> sub = stack.pop().getList();
                pushReversed(sub);
            }
            return !stack.isEmpty();
        }

        private void pushReversed(List<NestedInteger> list) {
            if (list == null) {
                return;
            }
            for (int i = list.size() - 1; i >= 0; i--) {
                stack.push(list.get(i));
            }
        }
    }
}
