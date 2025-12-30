package LeetCodeJava.Design;

// https://leetcode.com/problems/design-a-stack-with-increment-operation/description/

import java.util.*;

/**
 * 1381. Design a Stack With Increment Operation
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Design a stack that supports increment operations on its elements.
 *
 * Implement the CustomStack class:
 *
 * CustomStack(int maxSize) Initializes the object with maxSize which is the maximum number of elements in the stack.
 * void push(int x) Adds x to the top of the stack if the stack has not reached the maxSize.
 * int pop() Pops and returns the top of the stack or -1 if the stack is empty.
 * void inc(int k, int val) Increments the bottom k elements of the stack by val. If there are less than k elements in the stack, increment all the elements in the stack.
 *
 *
 * Example 1:
 *
 * Input
 * ["CustomStack","push","push","pop","push","push","push","increment","increment","pop","pop","pop","pop"]
 * [[3],[1],[2],[],[2],[3],[4],[5,100],[2,100],[],[],[],[]]
 * Output
 * [null,null,null,2,null,null,null,null,null,103,202,201,-1]
 * Explanation
 * CustomStack stk = new CustomStack(3); // Stack is Empty []
 * stk.push(1);                          // stack becomes [1]
 * stk.push(2);                          // stack becomes [1, 2]
 * stk.pop();                            // return 2 --> Return top of the stack 2, stack becomes [1]
 * stk.push(2);                          // stack becomes [1, 2]
 * stk.push(3);                          // stack becomes [1, 2, 3]
 * stk.push(4);                          // stack still [1, 2, 3], Do not add another elements as size is 4
 * stk.increment(5, 100);                // stack becomes [101, 102, 103]
 * stk.increment(2, 100);                // stack becomes [201, 202, 103]
 * stk.pop();                            // return 103 --> Return top of the stack 103, stack becomes [201, 202]
 * stk.pop();                            // return 202 --> Return top of the stack 202, stack becomes [201]
 * stk.pop();                            // return 201 --> Return top of the stack 201, stack becomes []
 * stk.pop();                            // return -1 --> Stack is empty return -1.
 *
 *
 * Constraints:
 *
 * 1 <= maxSize, x, k <= 1000
 * 0 <= val <= 100
 * At most 1000 calls will be made to each method of increment, push and pop each separately.
 *
 *
 */
public class DesignAStackWithIncrementOperation {

    /**
     * Your CustomStack object will be instantiated and called as such:
     * CustomStack obj = new CustomStack(maxSize);
     * obj.push(x);
     * int param_2 = obj.pop();
     * obj.increment(k,val);
     */


    // V0
//    class CustomStack {
//
//        public CustomStack(int maxSize) {
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
//        public void increment(int k, int val) {
//
//        }
//    }

    // V0-1
    // IDEA: ARRAY (fixed by gemini)
    /**  IDEA:
     *
     * Using an array is the intended way to solve this.
     * It allows you to access the bottom k
     * elements directly using indices 0 to k - 1
     *
     */
    class CustomStack_0_1 {
        private int[] stack;
        private int top; // Points to the next available position (size of stack)

        public CustomStack_0_1(int maxSize) {
            stack = new int[maxSize];
            top = 0;
        }

        public void push(int x) {
            // Only push if there is space; otherwise, do nothing
            if (top < stack.length) {
                stack[top] = x;
                top++;
            }
        }

        public int pop() {
            // If empty, return -1
            if (top == 0) {
                return -1;
            }
            // Decrement top and return the value
            top--;
            return stack[top];
        }

        public void increment(int k, int val) {
            // Increment the bottom k elements (index 0 up to k-1)
            // If the stack has fewer than k elements, increment all of them
            int limit = Math.min(k, top);
            for (int i = 0; i < limit; i++) {
                stack[i] += val;
            }
        }
    }


    // V0-2
    // IDEA: ARRAY (fixed by gemini)
    /**
     * Optimized "Lazy Propagation" Approach ($O(1)$ Increment)
     *
     *
     * If you want the most efficient solution
     * (often expected in interviews),
     * you can make increment run in O(1) time by storing
     * the increment values in a separate array and only applying
     * them during pop().
     *
     *
     */
    class CustomStack_0_2 {
        private int[] stack;
        private int[] lazyInc;
        private int top;

        public CustomStack_0_2(int maxSize) {
            stack = new int[maxSize];
            lazyInc = new int[maxSize];
            top = -1; // Using index for top
        }

        public void push(int x) {
            if (top < stack.length - 1) {
                top++;
                stack[top] = x;
                lazyInc[top] = 0;
            }
        }

        public int pop() {
            if (top == -1)
                return -1;

            // The real value is stack value + its lazy increment
            int res = stack[top] + lazyInc[top];

            // Pass the increment down to the element below (lazy propagation)
            if (top > 0) {
                lazyInc[top - 1] += lazyInc[top];
            }

            top--;
            return res;
        }

        public void increment(int k, int val) {
            // We only mark the (k-1)-th element.
            // Any pop above this index won't see this 'val' until it reaches this index.
            int idx = Math.min(k - 1, top);
            if (idx >= 0) {
                lazyInc[idx] += val;
            }
        }
    }



    // V0-3
    // IDEA: DEQUEUE (fixed by gpt)
    class CustomStack_0_3 {
        private int maxSize;
        private Deque<Integer> deque;

        public CustomStack_0_3(int maxSize) {
            this.maxSize = maxSize;
            this.deque = new ArrayDeque<>();
        }

        public void push(int x) {
            // Only push if stack hasn't reached maxSize
            if (deque.size() < maxSize) {
                deque.push(x); // Adds to the FRONT (Top)
            }
        }

        public int pop() {
            if (deque.isEmpty()) {
                return -1;
            }
            return deque.pop(); // Removes from the FRONT (Top)
        }

        public void increment(int k, int val) {
            // ArrayDeque doesn't allow indexed access, so we have to use an Iterator.
            // We use descendingIterator() because it starts from the BACK (Bottom).

            // However, ArrayDeque iterators don't allow modifying elements while iterating.
            // To stick with Deque and satisfy the "bottom k" rule efficiently,
            // it is actually better to use a LinkedList as the Deque implementation
            // or temporarily move elements.

            // Since we MUST stay with Deque, we'll use a temporary list to update.
            List<Integer> list = new ArrayList<>(deque);
            int n = list.size();

            // In a Deque push/pop stack, the BACK of the list is the BOTTOM of the stack.
            // If stack is [3, 2, 1] (Top is 3), list is [3, 2, 1]. Bottom is index n-1.
            int start = Math.max(0, n - k);
            for (int i = start; i < n; i++) {
                list.set(i, list.get(i) + val);
            }

            // Rebuild the deque
            deque.clear();
            for (int num : list) {
                deque.addLast(num);
            }
        }
    }



    // V1-1
    // IDEA: ARRAY
    // https://leetcode.com/problems/design-a-stack-with-increment-operation/editorial/
    class CustomStack_1_1 {

        // Array to store stack elements
        private int[] stackArray;
        // Index of the top element in the stack
        private int topIndex;

        public CustomStack_1_1(int maxSize) {
            stackArray = new int[maxSize];
            topIndex = -1;
        }

        public void push(int x) {
            if (topIndex < stackArray.length - 1) {
                stackArray[++topIndex] = x;
            }
        }

        public int pop() {
            if (topIndex >= 0) {
                return stackArray[topIndex--];
            }
            return -1; // Return -1 if the stack is empty
        }

        public void increment(int k, int val) {
            int limit = Math.min(k, topIndex + 1);
            for (int i = 0; i < limit; i++) {
                stackArray[i] += val;
            }
        }
    }

    // V1-2
    // IDEA: LINKED LIST
    // https://leetcode.com/problems/design-a-stack-with-increment-operation/editorial/
//    class CustomStack_1_2 {
//
//        private List<Integer> stack;
//        private int maxSize;
//
//        public CustomStack_1_2(int maxSize) {
//            // Initialize the stack as a LinkedList for efficient add/remove operations
//            stack = new LinkedList<>();
//            this.maxSize = maxSize;
//        }
//
//        public void push(int x) {
//            // Add the element to the top of the stack if it hasn't reached maxSize
//            if (stack.size() < maxSize) {
//                stack.addLast(x);
//            }
//        }
//
//        public int pop() {
//            // Return -1 if the stack is empty, otherwise remove and return the top element
//            if (stack.isEmpty())
//                return -1;
//            return stack.removeLast();
//        }
//
//        public void increment(int k, int val) {
//            // Create an iterator to traverse the stack from bottom to top
//            ListIterator<Integer> iterator = stack.listIterator();
//
//            // Increment the bottom k elements (or all elements if k > stack size)
//            while (iterator.hasNext() && k > 0) {
//                int current = iterator.next();
//                iterator.set(current + val);
//                k--;
//            }
//        }
//    }


    // V1-3
    // IDEA: Array using Lazy Propagation
    // https://leetcode.com/problems/design-a-stack-with-increment-operation/editorial/
    class CustomStack_1_3 {

        // Array to store stack elements
        private int[] stackArray;

        // Array to store increments for lazy propagation
        private int[] incrementArray;

        // Current top index of the stack
        private int topIndex;

        public CustomStack_1_3(int maxSize) {
            stackArray = new int[maxSize];
            incrementArray = new int[maxSize];
            topIndex = -1;
        }

        public void push(int x) {
            if (topIndex < stackArray.length - 1) {
                stackArray[++topIndex] = x;
            }
        }

        public int pop() {
            if (topIndex < 0) {
                return -1;
            }

            // Calculate the actual value with increment
            int result = stackArray[topIndex] + incrementArray[topIndex];

            // Propagate the increment to the element below
            if (topIndex > 0) {
                incrementArray[topIndex - 1] += incrementArray[topIndex];
            }

            // Reset the increment for this position
            incrementArray[topIndex] = 0;

            topIndex--;
            return result;
        }

        public void increment(int k, int val) {
            if (topIndex >= 0) {
                // Apply increment to the topmost element of the range
                int incrementIndex = Math.min(topIndex, k - 1);
                incrementArray[incrementIndex] += val;
            }
        }
    }


    // V2
    // https://leetcode.com/problems/design-a-stack-with-increment-operation/solutions/5849971/friendly-explained-any-language-beats-10-k5sj/
    class CustomStack_2 {
        int[] stack;
        int top = -1;

        public CustomStack_2(int maxSize) {
            this.stack = new int[maxSize];
        }

        public void push(int x) {
            if (top < this.stack.length - 1) {
                top++;
                this.stack[top] = x;
            }
        }

        public int pop() {
            if (top != -1) {
                return this.stack[top--];
            }
            return -1;
        }

        public void increment(int k, int val) {
            for (int i = 0; i < Math.min(k, top + 1); i++) {
                this.stack[i] += val;
            }
        }
    }


    // V3
    // https://leetcode.com/problems/design-a-stack-with-increment-operation/solutions/5849974/simple-and-easy-to-understand-beats-100-gybji/
    class CustomStack_3 {
        private int n;
        private Stack<Integer> stack;
        private List<Integer> inc;

        public CustomStack_3(int n) {
            this.n = n;
            this.stack = new Stack<>();
            this.inc = new ArrayList<>();
        }

        public void push(int x) {
            if (stack.size() < n) {
                stack.push(x);
                inc.add(0);
            }
        }

        public int pop() {
            if (stack.isEmpty())
                return -1;
            if (inc.size() > 1)
                inc.set(inc.size() - 2, inc.get(inc.size() - 2) + inc.get(inc.size() - 1));
            return stack.pop() + inc.remove(inc.size() - 1);
        }

        public void increment(int k, int val) {
            if (!stack.isEmpty()) {
                int index = Math.min(k, inc.size()) - 1;
                inc.set(index, inc.get(index) + val);
            }
        }
    }



}
