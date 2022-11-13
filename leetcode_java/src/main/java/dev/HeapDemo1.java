package dev;

// https://leetcode.com/explore/learn/card/heap/644/common-applications-of-heap/4022/

// In Java, a Heap is represented by a Priority Queue
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Arrays;

public class HeapDemo1 {

    public static void main(String[] args) {

    /** Construct an empty Min Heap */
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    /** Construct an empty Max Heap */
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

    // Construct a Heap with initial elements.
    // This process is named "Heapify".
    // The Heap is a Min Heap
    PriorityQueue<Integer> heapWithValues= new PriorityQueue<>(Arrays.asList(3, 1, 2));

    System.out.println(">>> heapWithValues = " + heapWithValues);
    System.out.println(">>> heapWithValues peek = " + heapWithValues.peek());
    System.out.println(">>> heapWithValues poll = " + heapWithValues.poll());
    }

}
