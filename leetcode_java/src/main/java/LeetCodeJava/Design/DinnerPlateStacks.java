package LeetCodeJava.Design;

// https://leetcode.com/problems/dinner-plate-stacks/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

/**
 *  1172. Dinner Plate Stacks
 *  Hard
 *
 *  You have an infinite number of stacks arranged in a row and numbered (left to right)
 *  from 0, each of the stacks has the same maximum capacity.
 *
 *  Implement the DinnerPlates class:
 *   - DinnerPlates(int capacity) Initializes the object with the maximum capacity.
 *   - void push(int val) Pushes val into the LEFTMOST stack with size < capacity.
 *   - int pop() Returns and removes the value at the top of the RIGHTMOST non-empty
 *     stack, or -1 if all the stacks are empty.
 *   - int popAtStack(int index) Returns and removes the value at the top of the stack
 *     with the given index, or -1 if that stack is empty.
 *
 *  Example 1:
 *    Input
 *      ["DinnerPlates","push","push","push","push","push","popAtStack","push","push",
 *       "popAtStack","popAtStack","pop","pop","pop","pop","pop"]
 *      [[2],[1],[2],[3],[4],[5],[0],[20],[21],[0],[2],[],[],[],[],[]]
 *    Output
 *      [null,null,null,null,null,null,2,null,null,20,21,5,4,3,1,-1]
 *
 *  Constraints:
 *    1 <= capacity <= 2 * 10^4
 *    1 <= val <= 2 * 10^4
 *    0 <= index <= 10^5
 *    At most 2 * 10^5 calls will be made to push, pop, and popAtStack.
 */
public class DinnerPlateStacks {

    // V0
    // IDEA: list of stacks + a MIN-HEAP of indexes that might be non-full.
    //       push  -> leftmost non-full stack = heap top (stale tops are dropped lazily)
    //       pop   -> trim trailing empty stacks, then popAtStack(last)
    //       popAtStack -> that stack became non-full, so push its index back on the heap
    /**
     * time = O(log n) amortized per op
     * space = O(n)
     */
    private final int capacity;
    private final List<Deque<Integer>> stacks;
    private final PriorityQueue<Integer> notFull; // indexes that MIGHT be non-full

    public DinnerPlateStacks(int capacity) {
        this.capacity = capacity;
        this.stacks = new ArrayList<>();
        this.notFull = new PriorityQueue<>();
    }

    /**
     * time = O(log n) amortized
     * space = O(1)
     */
    public void push(int val) {

        // drop stale heap tops (removed stacks / already-full stacks)
        while (!this.notFull.isEmpty()) {
            int i = this.notFull.peek();
            if (i < this.stacks.size() && this.stacks.get(i).size() < this.capacity) {
                break;
            }
            this.notFull.poll();
        }

        if (this.notFull.isEmpty()) {
            this.stacks.add(new ArrayDeque<Integer>());
            this.notFull.offer(this.stacks.size() - 1);
        }

        int idx = this.notFull.peek();
        this.stacks.get(idx).push(val);
        if (this.stacks.get(idx).size() == this.capacity) {
            this.notFull.poll();
        }
    }

    /**
     * time = O(1) amortized
     * space = O(1)
     */
    public int pop() {
        trimTail();
        if (this.stacks.isEmpty()) {
            return -1;
        }
        return popAtStack(this.stacks.size() - 1);
    }

    /**
     * time = O(log n)
     * space = O(1)
     */
    public int popAtStack(int index) {

        if (index < 0 || index >= this.stacks.size() || this.stacks.get(index).isEmpty()) {
            return -1;
        }

        int val = this.stacks.get(index).pop();
        // it is non-full now -> a candidate for the next push
        this.notFull.offer(index);

        trimTail();
        return val;
    }

    // drop trailing empty stacks so that `pop` always sees a real rightmost stack
    private void trimTail() {
        while (!this.stacks.isEmpty() && this.stacks.get(this.stacks.size() - 1).isEmpty()) {
            this.stacks.remove(this.stacks.size() - 1);
        }
    }
}
