package LeetCodeJava.Design;

// https://leetcode.com/problems/implement-stack-using-queues/

import java.util.LinkedList;
import java.util.Queue;

public class ImplementStackUsingQueues {

    // V0
    // IDEA : QUEUE
//    class MyStack {
//
//        // attr
//        Queue<Integer> q1 = new LinkedList<>();
//        Queue<Integer> q2 = new LinkedList<>();
//
//        public MyStack() {
//        }
//
//        public void push(int x) {
//            this.q1.add(x);
//        }
//
//        public int pop() {
//            int x = Integer.parseInt(null);
//            Queue<Integer> _q1 = q1;
//            while (!this.q1.isEmpty()){
//                int cur = this.q1.remove();
//                x = cur;
//                this.q2.add(cur);
//            }
//
//            this.q1 = _q1;
//            return x;
//        }
//
//        public int top() {
//            int x = Integer.parseInt(null);
//            Queue<Integer> _q1 = q1;
//            while (!this.q1.isEmpty()){
//                int cur = this.q1.remove();
//                x = cur;
//                this.q2.add(cur);
//            }
//
//            return x;
//        }
//
//        public boolean empty() {
//            return this.q1.isEmpty();
//        }
//    }

    // V1
    // https://leetcode.com/problems/implement-stack-using-queues/solutions/62516/concise-1-queue-java-c-python/
    class MyStack_2 {

        private Queue<Integer> queue = new LinkedList<>();

        public void push(int x) {
            queue.add(x);
            for (int i=1; i<queue.size(); i++)
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
