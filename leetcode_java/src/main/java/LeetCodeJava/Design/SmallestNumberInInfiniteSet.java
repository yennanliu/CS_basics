package LeetCodeJava.Design;

// https://leetcode.com/problems/smallest-number-in-infinite-set/

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *  2336. Smallest Number in Infinite Set
 *  Medium
 *
 *  You have a set which contains all positive integers [1, 2, 3, 4, 5, ...].
 *
 *  Implement the SmallestInfiniteSet class:
 *
 *   - SmallestInfiniteSet() Initializes the SmallestInfiniteSet object to contain all
 *     positive integers.
 *   - int popSmallest() Removes and returns the smallest integer contained in the
 *     infinite set.
 *   - void addBack(int num) Adds a positive integer num back into the infinite set, if it
 *     is not already in the infinite set.
 *
 *  Example 1:
 *
 *  Input
 *  ["SmallestInfiniteSet", "addBack", "popSmallest", "popSmallest", "popSmallest",
 *   "addBack", "popSmallest", "popSmallest", "popSmallest"]
 *  [[], [2], [], [], [], [1], [], [], []]
 *  Output
 *  [null, null, 1, 2, 3, null, 1, 4, 5]
 *
 *  Explanation
 *  SmallestInfiniteSet s = new SmallestInfiniteSet();
 *  s.addBack(2);    // 2 is already in the set, so no change is made.
 *  s.popSmallest(); // return 1
 *  s.popSmallest(); // return 2
 *  s.popSmallest(); // return 3
 *  s.addBack(1);    // 1 is added back to the set.
 *  s.popSmallest(); // return 1
 *  s.popSmallest(); // return 4
 *  s.popSmallest(); // return 5
 *
 *  Constraints:
 *
 *   1 <= num <= 1000
 *   At most 1000 calls will be made in total to popSmallest and addBack.
 */
public class SmallestNumberInInfiniteSet {

    // V0
    // IDEA: A FRONTIER COUNTER FOR THE UNTOUCHED TAIL + A MIN-HEAP OF RETURNS
    //       the set is always "everything from `frontier` upward, plus whatever has been
    //       added back", so:
    //          frontier = the smallest number never yet popped
    //          heap     = the re-added numbers, all strictly below frontier
    //       popSmallest : if the heap has anything it holds the true minimum -> pop it;
    //                     otherwise hand out `frontier` and advance it.
    //       addBack     : only meaningful for a number below the frontier that is not
    //                     already back -> the `inHeap` set keeps it idempotent.
    /**
     * time = O(log N) per call
     * space = O(N)
     */
    private int frontier;
    private final PriorityQueue<Integer> heap;
    private final Set<Integer> inHeap;

    public SmallestNumberInInfiniteSet() {
        this.frontier = 1;
        this.heap = new PriorityQueue<>();
        this.inHeap = new HashSet<>();
    }

    public int popSmallest() {
        if (!heap.isEmpty()) {
            int res = heap.poll();
            inHeap.remove(res);
            return res;
        }
        return frontier++;
    }

    public void addBack(int num) {
        if (num >= frontier || inHeap.contains(num)) {
            return; // already in the set
        }
        heap.add(num);
        inHeap.add(num);
    }
}
