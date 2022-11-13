package dev;

// https://leetcode.com/explore/learn/card/heap/644/common-applications-of-heap/4022/
// https://leetcode.com/explore/learn/card/heap/644/common-applications-of-heap/4023/
// https://leetcode.com/explore/learn/card/heap/644/common-applications-of-heap/4024/
// https://leetcode.com/explore/learn/card/heap/644/common-applications-of-heap/4025/

// In Java, a Heap is represented by a Priority Queue

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Arrays;

public class HeapDemo1 {

    public static void main(String[] args) {

        /** Construct an empty Min Heap */
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        // Insert an element to the Min Heap
        minHeap.add(5);

        /** Construct an empty Max Heap */
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        // Insert an element to the Max Heap
        maxHeap.add(5);

        // Get top element from the Min Heap
        // i.e. the smallest element
        minHeap.peek();

        // Get top element from the Max Heap
        // i.e. the largest element
        maxHeap.peek();

        // Delete top element from the Min Heap
        minHeap.poll();

        // Delete top element from the Max Heap
        maxHeap.poll();

        // Construct a Heap with initial elements.
        // This process is named "Heapify".
        // The Heap is a Min Heap
        PriorityQueue<Integer> heapWithValues = new PriorityQueue<>(Arrays.asList(3, 1, 2));

        System.out.println(">>> heapWithValues = " + heapWithValues);
        System.out.println(">>> heapWithValues peek = " + heapWithValues.peek());
        System.out.println(">>> heapWithValues poll = " + heapWithValues.poll());
    }

}
