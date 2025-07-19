package dev;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class StackTest1 {
    public static void main(String[] args) {
//
//        // Create a Queue (LinkedList implements Queue)
//        Queue<Integer> queue = new LinkedList<>();
//
//        // Add elements to the Queue
//        queue.add(10);
//        queue.add(20);
//        queue.add(30);
//        queue.add(7);  // Adding element 7
//        queue.add(40);
//
//        System.out.println("Original queue: " + queue);
//
//        // Remove element 7 from the queue
//        boolean removed = queue.remove(7);  // Removes the first occurrence of 7
//
//        // Print the result of removal
//        System.out.println("Was element 7 removed? " + removed);  // true
//
//        // Print the modified queue
//        System.out.println("Queue after removal of 7: " + queue);
//
//        // Try to remove element 7 again
//        removed = queue.remove(7);  // Element 7 no longer exists in the queue
//
//        // Print the result of trying to remove 7 again
//        System.out.println("Was element 7 removed again? " + removed);  // false
//
//        System.out.println("queue: " + queue); // queue: [10, 20, 30, 40]
//
//        queue.remove();
//        System.out.println("queue: " + queue); // queue: [20, 30, 40]

        Stack<String> st = new Stack<>();
        st.push("a");
        st.push("b");
        st.push("c");

    // NOTE !!! loop over elements in stack
            for(String x: st){
                System.out.println(">>> x = " + x);
            }

    }

}
