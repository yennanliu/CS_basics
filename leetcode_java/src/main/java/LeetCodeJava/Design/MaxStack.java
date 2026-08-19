package LeetCodeJava.Design;

// https://leetcode.com/problems/max-stack/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  716. Max Stack
 *  Hard
 *
 *  Design a max stack data structure that supports the stack operations and supports
 *  finding the stack's maximum element.
 *
 *  Implement the MaxStack class:
 *   - MaxStack() Initializes the stack object.
 *   - void push(int x) Pushes element x onto the stack.
 *   - int pop() Removes the element on top of the stack and returns it.
 *   - int top() Gets the element on the top of the stack without removing it.
 *   - int peekMax() Retrieves the maximum element in the stack without removing it.
 *   - int popMax() Retrieves the maximum element in the stack and removes it.
 *     If there is more than one maximum element, only remove the top-most one.
 *
 *  Example 1:
 *    MaxStack stk = new MaxStack();
 *    stk.push(5); stk.push(1); stk.push(5);
 *    stk.top();     // 5
 *    stk.popMax();  // 5
 *    stk.top();     // 1
 *    stk.peekMax(); // 5
 *    stk.pop();     // 1
 *    stk.top();     // 5
 *
 *  Constraints:
 *    -10^7 <= x <= 10^7
 *    At most 10^4 calls will be made to push, pop, top, peekMax, and popMax.
 *    There will be at least one element in the stack when pop, top, peekMax, or
 *    popMax is called.
 */
public class MaxStack {

    // V0
    // IDEA: two stacks - the values, and a parallel "max so far" stack, so top /
    //       peekMax are O(1). popMax pops into a temp buffer until the max is on top,
    //       drops it, then pushes the buffer back (which fixes the max stack for free).
    /**
     * time = O(1) push/pop/top/peekMax, O(n) popMax
     * space = O(n)
     */
    private final Deque<Integer> stack;
    private final Deque<Integer> maxStack;

    public MaxStack() {
        this.stack = new ArrayDeque<>();
        this.maxStack = new ArrayDeque<>();
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public void push(int x) {
        int curMax = this.maxStack.isEmpty() ? x : Math.max(this.maxStack.peek(), x);
        this.stack.push(x);
        this.maxStack.push(curMax);
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public int pop() {
        this.maxStack.pop();
        return this.stack.pop();
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public int top() {
        return this.stack.peek();
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public int peekMax() {
        return this.maxStack.peek();
    }

    /**
     * time = O(n)
     * space = O(n)
     */
    public int popMax() {
        int max = peekMax();

        Deque<Integer> buffer = new ArrayDeque<>();
        while (top() != max) {
            buffer.push(pop());
        }
        pop(); // drop the top-most max

        while (!buffer.isEmpty()) {
            push(buffer.pop());
        }

        return max;
    }
}
