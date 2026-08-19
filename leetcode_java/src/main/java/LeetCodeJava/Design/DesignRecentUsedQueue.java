package LeetCodeJava.Design;

// https://leetcode.com/problems/design-most-recently-used-queue/

import java.util.ArrayList;
import java.util.List;

/**
 *  1756. Design Most Recently Used Queue
 *  Medium
 *
 *  Design a queue-like data structure that moves the most recently used element to the end
 *  of the queue.
 *
 *  Implement the MRUQueue class:
 *
 *   - MRUQueue(int n) constructs the MRUQueue with n elements: [1,2,3,...,n].
 *   - int fetch(int k) moves the kth element (1-indexed) to the end of the queue and
 *     returns it.
 *
 *  Example 1:
 *
 *  Input:
 *  ["MRUQueue", "fetch", "fetch", "fetch", "fetch"]
 *  [[8], [3], [5], [2], [8]]
 *  Output:
 *  [null, 3, 6, 2, 2]
 *
 *  Explanation:
 *  MRUQueue q = new MRUQueue(8); // [1,2,3,4,5,6,7,8]
 *  q.fetch(3); // -> [1,2,4,5,6,7,8,3], returns 3
 *  q.fetch(5); // -> [1,2,4,5,7,8,3,6], returns 6
 *  q.fetch(2); // -> [1,4,5,7,8,3,6,2], returns 2
 *  q.fetch(8); // returns 2 (already at the end)
 *
 *  Constraints:
 *
 *   1 <= n <= 2000
 *   1 <= k <= n
 *   At most 2000 calls will be made to fetch.
 */
public class DesignRecentUsedQueue {

    // V0
    // IDEA: ARRAY LIST - remove the (k-1)-th element and append it to the tail
    /**
     * time = O(n) per fetch
     * space = O(n)
     */
    private final List<Integer> queue;

    public DesignRecentUsedQueue(int n) {
        this.queue = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            queue.add(i);
        }
    }

    public int fetch(int k) {
        int val = queue.remove(k - 1);
        queue.add(val);
        return val;
    }
}
