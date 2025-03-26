package LeetCodeJava.Stack;

// https://leetcode.com/problems/implement-queue-using-stacks/description/

import java.util.Stack;

/**
 * 232. Implement Queue using Stacks
 * Solved
 * Easy
 * Topics
 * Companies
 * Implement a first in first out (FIFO) queue using only two stacks. The implemented queue should support all the functions of a normal queue (push, peek, pop, and empty).
 *
 * Implement the MyQueue class:
 *
 * void push(int x) Pushes element x to the back of the queue.
 * int pop() Removes the element from the front of the queue and returns it.
 * int peek() Returns the element at the front of the queue.
 * boolean empty() Returns true if the queue is empty, false otherwise.
 * Notes:
 *
 * You must use only standard operations of a stack, which means only push to top, peek/pop from top, size, and is empty operations are valid.
 * Depending on your language, the stack may not be supported natively. You may simulate a stack using a list or deque (double-ended queue) as long as you use only a stack's standard operations.
 *
 *
 * Example 1:
 *
 * Input
 * ["MyQueue", "push", "push", "peek", "pop", "empty"]
 * [[], [1], [2], [], [], []]
 * Output
 * [null, null, null, 1, 1, false]
 *
 * Explanation
 * MyQueue myQueue = new MyQueue();
 * myQueue.push(1); // queue is: [1]
 * myQueue.push(2); // queue is: [1, 2] (leftmost is front of the queue)
 * myQueue.peek(); // return 1
 * myQueue.pop(); // return 1, queue is [2]
 * myQueue.empty(); // return false
 *
 *
 * Constraints:
 *
 * 1 <= x <= 9
 * At most 100 calls will be made to push, pop, peek, and empty.
 * All the calls to pop and peek are valid.
 *
 *
 *
 *
 */
public class ImplementQueueUsingStacks {

    // V0
//    class MyQueue {
//
//        public MyQueue() {
//
//        }
//
//        public void push(int x) {
//
//        }
//
//        public int pop() {
//
//        }
//
//        public int peek() {
//
//        }
//
//        public boolean empty() {
//
//        }
//    }

    // V1

    // V2-1
    // https://leetcode.com/problems/implement-queue-using-stacks/editorial/
    // IDEA:  (Two Stacks) Push - O(n) per operation, Pop - O(1) per operation.

    // V2-2
    // https://leetcode.com/problems/implement-queue-using-stacks/editorial/
    // IDEA:  #2 (Two Stacks) Push - O(1) per operation, Pop - Amortized O(1) per operation.

    // V3
    // https://leetcode.com/problems/implement-queue-using-stacks/solutions/6579732/video-simple-solution-by-niits-sqaw/
    // IDEA: 2 stack
    class MyQueue_3{
        private Stack<Integer> input;
        private Stack<Integer> output;

        public MyQueue_3() {
            input = new Stack<>();
            output = new Stack<>();
        }

        public void push(int x) {
            input.push(x);
        }

        public int pop() {
            /**
             *  NOTE !!!
             *
             *  1)  before calling pop() directly,
             *      we firstly call `peak()`
             *      purpose:
             *        reset / reassign elements at `output` stack,
             *        so we can have the element in `queue ordering` in `output` stack
             *
             *  2) peak() return an integer, but it DOES NOT terminate the pop() execution
             *     since the `peek()` method is called and NOT assign its result to any object,
             *     then the `output.pop();` code is executed and return as result
             */
            peek();
            return output.pop();
        }

        public int peek() {
            if (output.isEmpty()) {
                while (!input.isEmpty()) {
                    output.push(input.pop());
                }
            }
            return output.peek();
        }

        public boolean empty() {
            return input.isEmpty() && output.isEmpty();
        }
    }

    // V4
    // https://leetcode.com/problems/implement-queue-using-stacks/solutions/6558280/best-simple-one-with-100-by-srihitha684-tvu2/
    // IDEA: 2 stack
    class MyQueue_4{
        Stack<Integer> st1;
        Stack<Integer> st2;

        public MyQueue_4() {
            st1 = new Stack<>();
            st2 = new Stack<>();
        }

        public void push(int x) {
            while(!st1.isEmpty()){
                int data = st1.pop();
                st2.push(data);
            }
            st1.push(x);
            while(!st2.isEmpty()){
                int data = st2.pop();
                st1.push(data);
            }
        }

        public int pop() {
            if(empty()) return -1;
            return st1.pop();

        }

        public int peek() {
            if(empty()) return -1;
            return st1.peek();

        }

        public boolean empty() {
            return st1.isEmpty();
        }
    }


}
