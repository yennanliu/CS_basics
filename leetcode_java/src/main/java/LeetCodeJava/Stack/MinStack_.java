package LeetCodeJava.Stack;

// https://leetcode.com/problems/min-stack/
/**
 * 155. Min Stack
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.
 *
 * Implement the MinStack class:
 *
 * MinStack() initializes the stack object.
 * void push(int val) pushes the element val onto the stack.
 * void pop() removes the element on the top of the stack.
 * int top() gets the top element of the stack.
 * int getMin() retrieves the minimum element in the stack.
 * You must implement a solution with O(1) time complexity for each function.
 *
 *
 *
 * Example 1:
 *
 * Input
 * ["MinStack","push","push","push","getMin","pop","top","getMin"]
 * [[],[-2],[0],[-3],[],[],[],[]]
 *
 * Output
 * [null,null,null,null,-3,null,0,-2]
 *
 * Explanation
 * MinStack minStack = new MinStack();
 * minStack.push(-2);
 * minStack.push(0);
 * minStack.push(-3);
 * minStack.getMin(); // return -3
 * minStack.pop();
 * minStack.top();    // return 0
 * minStack.getMin(); // return -2
 *
 *
 * Constraints:
 *
 * -231 <= val <= 231 - 1
 * Methods pop, top and getMin operations will always be called on non-empty stacks.
 * At most 3 * 104 calls will be made to push, pop, top, and getMin.
 *
 */

import java.util.*;

public class MinStack_ {

    // V0
    // TODO; implement

    // V0-1
    // IDEA: LIST (gpt) (not efficient)
    class MinStack_0_1 {
        private List<Integer> list; // Storage list

        public MinStack_0_1() {
            this.list = new ArrayList<>();
        }

        public void push(int val) {
            list.add(val); // Add to the end
        }

        public void pop() {
            if (!list.isEmpty()) {
                list.remove(list.size() - 1); // Remove last element
            }
        }

        public int top() {
            if (!list.isEmpty()) {
                return list.get(list.size() - 1); // Return last element
            }
            throw new EmptyStackException(); // Proper error handling
        }

        public int getMin() {
            if (list.isEmpty()) {
                throw new EmptyStackException(); // Prevent access on empty list
            }

            // Create a copy to avoid modifying original list
            List<Integer> tmp = new ArrayList<>(list);

            // Sort to get the minimum
            Collections.sort(tmp);

            return tmp.get(0); // Smallest element
        }

    }

    // V0-2
    // IDEA: stack + min stack (gpt)
    class MinStack_0_2 {
        private Stack<Integer> stack; // Stores all elements
        private Stack<Integer> minStack; // Stores min values

        public MinStack_0_2() {
            this.stack = new Stack<>();
            this.minStack = new Stack<>();
        }

        public void push(int val) {
            stack.push(val);
            // Push to minStack only if it's the first element or smaller than current min
            if (minStack.isEmpty() || val <= minStack.peek()) {
                minStack.push(val);
            }
        }

        public void pop() {
            if (stack.isEmpty())
                return;
            int removed = stack.pop();
            if (removed == minStack.peek()) {
                minStack.pop();
            }
        }

        public int top() {
            if (stack.isEmpty()) {
                System.out.println("Stack is empty");
                return -1;
            }
            return stack.peek();
        }

        public int getMin() {
            if (minStack.isEmpty()) {
                System.out.println("Stack is empty");
                return -1;
            }
            return minStack.peek();
        }
    }

    // V1
    // IDEA : PRIORITY QUEUE (for getMin) + STACK (for "top")
    // https://leetcode.com/problems/min-stack/solutions/1611233/java-stack-priorityqueue/
    class MinStack_1 {
        Stack<Integer> st;
        PriorityQueue<Integer> pq;
        public MinStack_1() {
            this.st = new Stack<>();
            this.pq = new PriorityQueue<Integer>();
        }

        public void push(int val) {
            this.st.push(val);
            this.pq.add(val);
        }

        public void pop() {
            this.pq.remove(this.st.pop());
        }

        public int top() {
            return this.st.peek();
        }

        public int getMin() {
            return this.pq.peek();
        }
    }

    // V1-1
    // IDEA : PRIORITY QUEUE
    // https://leetcode.com/problems/min-stack/solutions/1874692/simple-java-priorityqueue/
    class MinStack_1_1 {
        Stack<Integer> s = new Stack<>();
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        public MinStack_1_1() {

        }

        public void push(int val) {
            s.push(val);
            pq.add(val);
        }

        public void pop() {
            int temp = s.pop();
            pq.remove(temp);

        }

        public int top() {
            return s.peek();
        }

        public int getMin() {
            return pq.peek();
        }
    }

    // V2
    // IDEA : Stack of Value/ Minimum Pairs
    // https://leetcode.com/problems/min-stack/editorial/
    class MinStack_2 {

        private Stack<int[]> stack = new Stack<>();

        public MinStack_2() { }


        public void push(int x) {

            /* If the stack is empty, then the min value
             * must just be the first value we add. */
            if (stack.isEmpty()) {
                stack.push(new int[]{x, x});
                return;
            }

            int currentMin = stack.peek()[1];
            stack.push(new int[]{x, Math.min(x, currentMin)});
        }


        public void pop() {
            stack.pop();
        }


        public int top() {
            return stack.peek()[0];
        }


        public int getMin() {
            return stack.peek()[1];
        }
    }

    // V3
    // IDEA : Two Stacks
    // https://leetcode.com/problems/min-stack/editorial/
    class MinStack_3 {

        private Stack<Integer> stack = new Stack<>();
        private Stack<Integer> minStack = new Stack<>();


        public MinStack_3() { }


        public void push(int x) {
            stack.push(x);
            if (minStack.isEmpty() || x <= minStack.peek()) {
                minStack.push(x);
            }
        }


        public void pop() {
            if (stack.peek().equals(minStack.peek())) {
                minStack.pop();
            }
            stack.pop();
        }


        public int top() {
            return stack.peek();
        }


        public int getMin() {
            return minStack.peek();
        }
    }

    // V4
    // IDEA :  Improved Two Stacks
    // https://leetcode.com/problems/min-stack/editorial/
    class MinStack_4 {

        private Stack<Integer> stack = new Stack<>();
        private Stack<int[]> minStack = new Stack<>();


        public MinStack_4() { }


        public void push(int x) {

            // We always put the number onto the main stack.
            stack.push(x);

            // If the min stack is empty, or this number is smaller than
            // the top of the min stack, put it on with a count of 1.
            if (minStack.isEmpty() || x < minStack.peek()[0]) {
                minStack.push(new int[]{x, 1});
            }

            // Else if this number is equal to what's currently at the top
            // of the min stack, then increment the count at the top by 1.
            else if (x == minStack.peek()[0]) {
                minStack.peek()[1]++;
            }
        }


        public void pop() {

            // If the top of min stack is the same as the top of stack
            // then we need to decrement the count at the top by 1.
            if (stack.peek().equals(minStack.peek()[0])) {
                minStack.peek()[1]--;
            }

            // If the count at the top of min stack is now 0, then remove
            // that value as we're done with it.
            if (minStack.peek()[1] == 0) {
                minStack.pop();
            }

            // And like before, pop the top of the main stack.
            stack.pop();
        }


        public int top() {
            return stack.peek();
        }


        public int getMin() {
            return minStack.peek()[0];
        }
    }

}
