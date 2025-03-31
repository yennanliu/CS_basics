package LeetCodeJava.Stack;

// https://leetcode.com/problems/maximum-frequency-stack/description/

import java.util.*;

/**
 * 895. Maximum Frequency Stack
 * Solved
 * Hard
 * Topics
 * Companies
 * Design a stack-like data structure to push elements to the stack and pop the most frequent element from the stack.
 *
 * Implement the FreqStack class:
 *
 * FreqStack() constructs an empty frequency stack.
 * void push(int val) pushes an integer val onto the top of the stack.
 * int pop() removes and returns the most frequent element in the stack.
 * If there is a tie for the most frequent element, the element closest to the stack's top is removed and returned.
 *
 *
 * Example 1:
 *
 * Input
 * ["FreqStack", "push", "push", "push", "push", "push", "push", "pop", "pop", "pop", "pop"]
 * [[], [5], [7], [5], [7], [4], [5], [], [], [], []]
 * Output
 * [null, null, null, null, null, null, null, 5, 7, 5, 4]
 *
 * Explanation
 * FreqStack freqStack = new FreqStack();
 * freqStack.push(5); // The stack is [5]
 * freqStack.push(7); // The stack is [5,7]
 * freqStack.push(5); // The stack is [5,7,5]
 * freqStack.push(7); // The stack is [5,7,5,7]
 * freqStack.push(4); // The stack is [5,7,5,7,4]
 * freqStack.push(5); // The stack is [5,7,5,7,4,5]
 * freqStack.pop();   // return 5, as 5 is the most frequent. The stack becomes [5,7,5,7,4].
 * freqStack.pop();   // return 7, as 5 and 7 is the most frequent, but 7 is closest to the top. The stack becomes [5,7,5,4].
 * freqStack.pop();   // return 5, as 5 is the most frequent. The stack becomes [5,7,4].
 * freqStack.pop();   // return 4, as 4, 5 and 7 is the most frequent, but 4 is closest to the top. The stack becomes [5,7].
 *
 *
 * Constraints:
 *
 * 0 <= val <= 109
 * At most 2 * 104 calls will be made to push and pop.
 * It is guaranteed that there will be at least one element in the stack before calling pop.
 *
 *
 */
public class MaximumFrequencyStack {

    /**
     * Your FreqStack object will be instantiated and called as such:
     * FreqStack obj = new FreqStack();
     * obj.push(val);
     * int param_2 = obj.pop();
     */
    // V0
//    class FreqStack {
//
//        public FreqStack() {
//
//        }
//
//        public void push(int val) {
//
//        }
//
//        public int pop() {
//
//        }
//    }


    // V1
    // https://www.youtube.com/watch?v=Z6idIicFDOE

    // V2
    // https://leetcode.com/problems/maximum-frequency-stack/solutions/1086543/js-python-java-c-frequency-map-stack-sol-o8kz/
    class FreqStack_2 {
        HashMap<Integer, Integer> fmap;
        List<Stack<Integer>> stack;

        public FreqStack_2() {
            fmap = new HashMap();
            stack = new ArrayList();
            stack.add(new Stack());
        }

        public void push(int x) {
            int freq = fmap.getOrDefault(x, 0) + 1;
            fmap.put(x, freq);
            if (freq == stack.size())
                stack.add(new Stack());
            stack.get(freq).add(x);
        }

        public int pop() {
            Stack<Integer> top = stack.get(stack.size() - 1);
            int x = top.pop();
            if (top.size() == 0)
                stack.remove(stack.size() - 1);
            fmap.put(x, fmap.get(x) - 1);
            return x;
        }
    }

    // V3
    // https://leetcode.com/problems/maximum-frequency-stack/solutions/1861901/java-stacks-explained-by-prashant404-cnz7/
    class FreqStack_3 {
        private final Map<Integer, Deque<Integer>> freqToStack = new HashMap<>();
        private final Map<Integer, Integer> numToMaxFreq = new HashMap<>();
        private int maxFreq;

        /**
         * Constructs an empty frequency stack
         */
        public FreqStack_3() {
        }

        /**
         * Pushes an integer val onto the top of the stack.
         *
         * @param val integer to be pushed
         */
        public void push(int val) {
            Integer freq = numToMaxFreq.compute(val, (k, v) -> v == null ? 1 : ++v);
            freqToStack.computeIfAbsent(freq, k -> new ArrayDeque<>())
                    .push(val);
            maxFreq = Math.max(maxFreq, freq);
        }

        /**
         * Removes and returns the most frequent element in the stack. If there is a tie for the most frequent element,
         * the element closest to the stack's top is removed and returned.
         *
         * @return The most frequent element in the stack
         */
        public int pop() {
            Deque<Integer> stack = freqToStack.get(maxFreq);
            Integer top = stack.pop();
            numToMaxFreq.compute(top, (k, v) -> --v);
            if (stack.isEmpty())
                maxFreq--;
            return top;
        }
    }

    // V4
    // https://leetcode.com/problems/maximum-frequency-stack/solutions/5172171/stack-implementation-problem-by-dixon_n-dq2i/
    class FreqStack_4 {
        HashMap<Integer, Integer> freq = new HashMap<>();
        HashMap<Integer, Stack<Integer>> m = new HashMap<>();
        int maxfreq = 0;

        public void push(int x) {
            int f = freq.getOrDefault(x, 0) + 1;
            freq.put(x, f);
            maxfreq = Math.max(maxfreq, f);
            if (!m.containsKey(f))
                m.put(f, new Stack<Integer>());
            m.get(f).push(x);// for the frequency f the elements are pushed into the stack. ie. For every Frequency Its own Key Exist
        }

        public int pop() {
            int x = m.get(maxfreq).pop();
            freq.put(x, maxfreq - 1);
            if (m.get(maxfreq).size() == 0)
                maxfreq--;
            return x;
        }
    }

}
