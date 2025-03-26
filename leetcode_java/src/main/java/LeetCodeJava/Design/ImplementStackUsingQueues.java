package LeetCodeJava.Design;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;


// https://leetcode.com/problems/implement-stack-using-queues/
/**
 *  225. Implement Stack using Queues
 * Solved
 * Easy
 * Topics
 * Companies
 * Implement a last-in-first-out (LIFO) stack using only two queues. The implemented stack should support all the functions of a normal stack (push, top, pop, and empty).
 *
 * Implement the MyStack class:
 *
 * void push(int x) Pushes element x to the top of the stack.
 * int pop() Removes the element on the top of the stack and returns it.
 * int top() Returns the element on the top of the stack.
 * boolean empty() Returns true if the stack is empty, false otherwise.
 * Notes:
 *
 * You must use only standard operations of a queue, which means that only push to back, peek/pop from front, size and is empty operations are valid.
 * Depending on your language, the queue may not be supported natively. You may simulate a queue using a list or deque (double-ended queue) as long as you use only a queue's standard operations.
 *
 *
 * Example 1:
 *
 * Input
 * ["MyStack", "push", "push", "top", "pop", "empty"]
 * [[], [1], [2], [], [], []]
 * Output
 * [null, null, null, 2, 2, false]
 *
 * Explanation
 * MyStack myStack = new MyStack();
 * myStack.push(1);
 * myStack.push(2);
 * myStack.top(); // return 2
 * myStack.pop(); // return 2
 * myStack.empty(); // return False
 *
 *
 * Constraints:
 *
 * 1 <= x <= 9
 * At most 100 calls will be made to push, pop, top, and empty.
 * All the calls to pop and top are valid.
 *
 *
 * Follow-up: Can you implement the stack using only one queue?
 *
 *
 */
public class ImplementStackUsingQueues {

    // V0
    // IDEA : DEQUEUE
    class MyStack {

        // attr
        Deque<Integer> q;
        int size;

        public MyStack() {
            this.q = new LinkedList<>();
            // this.size = 0;
        }

        public void push(int x) {
            this.q.addFirst(x);
            // this.size += 1;
        }

        public int pop() {
            if (!this.q.isEmpty()) {
                return this.q.pollFirst();
            }
            return 0; // ???
        }

        public int top() {
            return this.q.peekFirst(); // NOTE !!! here
        }

        public boolean empty() {
            return this.q.isEmpty();
        }
    }

    // V1
    // https://leetcode.com/problems/implement-stack-using-queues/solutions/62516/concise-1-queue-java-c-python/
    class MyStack_2 {

        private Queue<Integer> queue = new LinkedList<>();

        public void push(int x) {

            /**
             *  NOTE !!!
             *
             *   we still add element to queue first
             *
             */
            queue.add(x);
            /**
             *  NOTE !!!
             *
             *  below op:
             *
             *   Step 2: Reordering to simulate stack behavior
             *
             *     - After adding the new element, we want the new element to
             *       become the first element in the queue
             *       (because it should be the last one to be popped).
             *
             *     -  The for-loop begins by iterating over all elements
             *        except the one just added (i = 1 to i < queue.size()).
             *
             *     - Inside the loop, queue.remove() removes the front
             *       element of the queue (this is the oldest element in the queue),
             *       and queue.add() adds it back to the back of the queue.
             *
             *     - By repeating this process for all the elements in the queue
             *       (except the last added one), we effectively "rotate"
             *       the queue such that the most recently added element is moved
             *       to the front of the queue.
             *
             *
             *
             *   Example:
             *
             *
             *  Suppose you have the queue: [1, 2, 3] and you call push(4).
             *
             *  (NOTE !!! 1 is the head of queue, per [1,2,3] example )
             *
             * Initially, the queue will look like: [1, 2, 3, 4].
             *
             * After the for-loop runs:
             *
             * First, 1 is moved to the back, resulting in: [2, 3, 4, 1].
             *
             * Then, 2 is moved to the back, resulting in: [3, 4, 1, 2].
             *
             * Then, 3 is moved to the back, resulting in: [4, 1, 2, 3].
             *
             * Now, the most recent element (4) is at the front of the queue, simulating the stack behavior (LIFO).
             *
             *
             */
            for (int i=1; i < queue.size(); i++)
                queue.add(queue.remove());
        }

        public void pop() {
            queue.remove();
        }

        public int top() {
            return queue.peek();
        }

        public boolean empty() {
            return queue.isEmpty();
        }
    }

    // V2
    // https://leetcode.com/problems/implement-stack-using-queues/solutions/62519/only-push-is-o-n-others-are-o-1-using-one-queue-combination-of-two-shared-solutions/
    class MyStack_3
    {
        Queue<Integer> queue;

        public MyStack_3()
        {
            this.queue=new LinkedList<Integer>();
        }

        // Push element x onto stack.
        // NOTE this trick!!!
        public void push(int x)
        {
            queue.add(x);
            for(int i=0;i<queue.size()-1;i++)
            {
                queue.add(queue.poll());
            }
        }

        // Removes the element on top of the stack.
        public void pop()
        {
            queue.poll();
        }

        // Get the top element.
        public int top()
        {
            return queue.peek();
        }

        // Return whether the stack is empty.
        public boolean empty()
        {
            return queue.isEmpty();
        }
    }

    // V4
    // IDEA :  (Two Queues, push - O(1)O(1)O(1), pop O(n)O(n)O(n) )
    // https://leetcode.com/problems/implement-stack-using-queues/editorial/

}
