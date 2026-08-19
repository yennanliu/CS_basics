package LeetCodeJava.Design;

// https://leetcode.com/problems/design-front-middle-back-queue/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  1670. Design Front Middle Back Queue
 *  Medium
 *
 *  Design a queue that supports push and pop operations in the front, middle, and back.
 *
 *  Implement the FrontMiddleBack class:
 *
 *   - FrontMiddleBackQueue() Initializes the queue.
 *   - void pushFront(int val)  Adds val to the front of the queue.
 *   - void pushMiddle(int val) Adds val to the middle of the queue.
 *   - void pushBack(int val)   Adds val to the back of the queue.
 *   - int popFront()  Removes and returns the front element, or -1 if empty.
 *   - int popMiddle() Removes and returns the middle element, or -1 if empty.
 *   - int popBack()   Removes and returns the back element, or -1 if empty.
 *
 *  When there are two middle position choices, the operation is performed on the frontmost
 *  middle position. e.g. pushing 6 into the middle of [1,2,3,4,5] gives [1,2,6,3,4,5];
 *  popping the middle of [1,2,3,4,5,6] returns 3.
 *
 *  Example 1:
 *
 *  Input:
 *  ["FrontMiddleBackQueue","pushFront","pushBack","pushMiddle","pushMiddle","popFront",
 *   "popMiddle","popMiddle","popBack","popFront"]
 *  [[],[1],[2],[3],[4],[],[],[],[],[]]
 *  Output:
 *  [null,null,null,null,null,1,3,4,2,-1]
 *
 *  Constraints:
 *
 *   1 <= val <= 10^9
 *   At most 1000 calls will be made to the push/pop methods.
 */
public class DesignFrontMiddleBackQueue {

    // V0
    // IDEA: TWO DEQUES (left | right) with the invariant  left.size() == n / 2  (floor),
    //       so `right` always holds the extra element when n is odd.
    //         - insert index for pushMiddle is n / 2  -> always right.addFirst()
    //         - remove index for popMiddle is (n-1)/2 -> left.pollLast()  when n is even
    //                                                   right.pollFirst() when n is odd
    //       rebalance() restores the invariant after every operation.
    /**
     * time = O(1) per operation
     * space = O(n)
     */
    private final Deque<Integer> left;
    private final Deque<Integer> right;

    public DesignFrontMiddleBackQueue() {
        this.left = new ArrayDeque<>();
        this.right = new ArrayDeque<>();
    }

    public void pushFront(int val) {
        left.addFirst(val);
        rebalance();
    }

    public void pushMiddle(int val) {
        right.addFirst(val);
        rebalance();
    }

    public void pushBack(int val) {
        right.addLast(val);
        rebalance();
    }

    public int popFront() {
        if (size() == 0) {
            return -1;
        }
        int res = left.isEmpty() ? right.pollFirst() : left.pollFirst();
        rebalance();
        return res;
    }

    public int popMiddle() {
        int n = size();
        if (n == 0) {
            return -1;
        }
        int res = (n % 2 == 0) ? left.pollLast() : right.pollFirst();
        rebalance();
        return res;
    }

    public int popBack() {
        if (size() == 0) {
            return -1;
        }
        int res = right.pollLast();
        rebalance();
        return res;
    }

    private int size() {
        return left.size() + right.size();
    }

    private void rebalance() {
        int target = size() / 2;
        while (left.size() > target) {
            right.addFirst(left.pollLast());
        }
        while (left.size() < target) {
            left.addLast(right.pollFirst());
        }
    }
}
