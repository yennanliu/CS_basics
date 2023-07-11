package LeetCodeJava.Stack;

// https://leetcode.com/problems/min-stack/

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

public class MinStack_ {

    // TODO: try if can use PriorityQueue
//    class MinStack {
//
//        private PriorityQueue<Integer> queue = new PriorityQueue<Integer>(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                // small queue
//                // https://blog.csdn.net/shinef/article/details/105935038
//                return o2- o1; //o1 - o2;
//            }
//        });
//
//        public MinStack() {
//
//        }
//
//        public void push(int val) {
//            this.queue.add(val);
//            Object[] array = this.queue.toArray();
//            System.out.println("--> push : array = " + Arrays.toString(array));
//        }
//
//        public void pop() {
//            Object[] array = this.queue.toArray();
//            System.out.println("--> pop : array = " + Arrays.toString(array));
//            this.queue.poll();
//        }
//
//        public int top() {
//            Object[] array = this.queue.toArray();
//            System.out.println("--> top : array = " + Arrays.toString(array));
//            int ans = this.queue.peek();
//            return ans;
//        }
//
//        public int getMin() {
//            Object[] array = this.queue.toArray();
//            System.out.println("--> getMin : array = " + Arrays.toString(array));
//            return (int) array[array.length-1];
//        }
//    }

    // V1
    // IDEA : PRIORITY QUEUE
    // https://leetcode.com/problems/min-stack/solutions/1611233/java-stack-priorityqueue/
    class MinStack {
        Stack<Integer> st;
        PriorityQueue<Integer> pq;
        public MinStack() {
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

    // V1'
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

    // V1
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

    // V2
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

    // V3
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
