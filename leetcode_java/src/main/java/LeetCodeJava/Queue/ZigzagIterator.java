package LeetCodeJava.Queue;

// https://leetcode.com/problems/zigzag-iterator/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *  281. Zigzag Iterator
 *  Medium
 *
 *  Given two vectors of integers v1 and v2, implement an iterator to return
 *  their elements alternately.
 *
 *  Implement the ZigzagIterator class:
 *
 *   - ZigzagIterator(List<int> v1, List<int> v2) initializes the object with
 *     the two vectors.
 *   - boolean hasNext() returns true if the iterator still has elements.
 *   - int next() returns the current element and moves the iterator forward.
 *
 *
 *  Example 1:
 *
 *  Input: v1 = [1,2], v2 = [3,4,5,6]
 *  Output: [1,3,2,4,5,6]
 *
 *  Example 2:
 *
 *  Input: v1 = [1], v2 = []
 *  Output: [1]
 *
 *  Example 3:
 *
 *  Input: v1 = [], v2 = [1]
 *  Output: [1]
 *
 *
 *  Constraints:
 *
 *  0 <= v1.length, v2.length <= 1000
 *  1 <= v1.length + v2.length <= 2000
 *  -2^31 <= v1[i], v2[i] <= 2^31 - 1
 *
 *  Follow up: what if you are given k vectors?
 */
public class ZigzagIterator {

    // V0
    // IDEA: queue of (still non-exhausted) iterators, rotate round-robin.
    //       This extends to k vectors for free.
    private final Queue<Iterator<Integer>> queue;

    /**
     * time = O(1)
     * space = O(1)   (iterators only, no copy of the input)
     */
    public ZigzagIterator(List<Integer> v1, List<Integer> v2) {
        this.queue = new LinkedList<>();
        if (v1 != null && !v1.isEmpty()) {
            this.queue.add(v1.iterator());
        }
        if (v2 != null && !v2.isEmpty()) {
            this.queue.add(v2.iterator());
        }
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public int next() {
        Iterator<Integer> cur = this.queue.poll();
        int val = cur.next();
        // put it back at the tail only if it still has elements
        if (cur.hasNext()) {
            this.queue.add(cur);
        }
        return val;
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean hasNext() {
        return !this.queue.isEmpty();
    }

    // V1
    // IDEA: flatten everything into one deque at construction time
    public static class ZigzagIterator2 {

        private final Deque<Integer> flat;

        /**
         * time = O(n + m)
         * space = O(n + m)
         */
        public ZigzagIterator2(List<Integer> v1, List<Integer> v2) {
            this.flat = new ArrayDeque<>();
            List<Integer> a = (v1 == null) ? new ArrayList<>() : v1;
            List<Integer> b = (v2 == null) ? new ArrayList<>() : v2;
            int i = 0;
            while (i < a.size() || i < b.size()) {
                if (i < a.size()) {
                    this.flat.add(a.get(i));
                }
                if (i < b.size()) {
                    this.flat.add(b.get(i));
                }
                i++;
            }
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int next() {
            return this.flat.poll();
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean hasNext() {
            return !this.flat.isEmpty();
        }
    }
}
