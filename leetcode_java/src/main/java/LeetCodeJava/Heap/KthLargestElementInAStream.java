package LeetCodeJava.Heap;

// https://leetcode.com/problems/kth-largest-element-in-a-stream/

import java.util.PriorityQueue;

public class KthLargestElementInAStream {

    // V0

    // V1
    // IDEA : PRIORITY QUEUE
    // https://leetcode.com/problems/kth-largest-element-in-a-stream/editorial/
    // NOTE here :
    private static int k;
    private PriorityQueue<Integer> heap;

    // replace with name as KthLargest when run at LC
    // constructor
    public KthLargestElementInAStream(int k, int[] nums) {

        this.k = k;
        heap = new PriorityQueue<>();

        for (int num: nums) {
            // https://docs.oracle.com/javase/7/docs/api/java/util/PriorityQueue.html
            // offer : Inserts the specified element into this priority queue.
            heap.offer(num);
        }

        while (heap.size() > k) {
            // https://docs.oracle.com/javase/7/docs/api/java/util/PriorityQueue.html
            // poll : Retrieves and removes the head of this queue, or returns null if this queue is empty.
            heap.poll();
        }
    }

    public int add(int val) {
        heap.offer(val);
        if (heap.size() > k) {
            heap.poll();
        }

        return heap.peek();
    }

    // V2
    // https://leetcode.com/problems/kth-largest-element-in-a-stream/solutions/3553822/java-priorityqueue-7-lines-clean-code/
//    private PriorityQueue<Integer> heap = new PriorityQueue<>();
//    private int k;
//
//    public KthLargest(int k, int[] nums) {
//        this.k = k;
//        for (var n : nums) add(n);
//    }
//
//    public int add(int val) {
//        heap.offer(val);
//        if (heap.size() > k) heap.poll();
//        return heap.peek();
//    }
}
