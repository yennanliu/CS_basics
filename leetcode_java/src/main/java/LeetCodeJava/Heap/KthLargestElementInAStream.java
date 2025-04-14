package LeetCodeJava.Heap;

// https://leetcode.com/problems/kth-largest-element-in-a-stream/
/**
 * 703. Kth Largest Element in a Stream
 * Solved
 * Easy
 * Topics
 * Companies
 * You are part of a university admissions office and need to keep track of the kth highest test score from applicants in real-time. This helps to determine cut-off marks for interviews and admissions dynamically as new applicants submit their scores.
 *
 * You are tasked to implement a class which, for a given integer k, maintains a stream of test scores and continuously returns the kth highest test score after a new score has been submitted. More specifically, we are looking for the kth highest score in the sorted list of all scores.
 *
 * Implement the KthLargest class:
 *
 * KthLargest(int k, int[] nums) Initializes the object with the integer k and the stream of test scores nums.
 * int add(int val) Adds a new test score val to the stream and returns the element representing the kth largest element in the pool of test scores so far.
 *
 *
 * Example 1:
 *
 * Input:
 * ["KthLargest", "add", "add", "add", "add", "add"]
 * [[3, [4, 5, 8, 2]], [3], [5], [10], [9], [4]]
 *
 * Output: [null, 4, 5, 5, 8, 8]
 *
 * Explanation:
 *
 * KthLargest kthLargest = new KthLargest(3, [4, 5, 8, 2]);
 * kthLargest.add(3); // return 4
 * kthLargest.add(5); // return 5
 * kthLargest.add(10); // return 5
 * kthLargest.add(9); // return 8
 * kthLargest.add(4); // return 8
 *
 * Example 2:
 *
 * Input:
 * ["KthLargest", "add", "add", "add", "add"]
 * [[4, [7, 7, 7, 7, 8, 3]], [2], [10], [9], [9]]
 *
 * Output: [null, 7, 7, 7, 8]
 *
 * Explanation:
 *
 * KthLargest kthLargest = new KthLargest(4, [7, 7, 7, 7, 8, 3]);
 * kthLargest.add(2); // return 7
 * kthLargest.add(10); // return 7
 * kthLargest.add(9); // return 7
 * kthLargest.add(9); // return 8
 *
 *
 * Constraints:
 *
 * 0 <= nums.length <= 104
 * 1 <= k <= nums.length + 1
 * -104 <= nums[i] <= 104
 * -104 <= val <= 104
 * At most 104 calls will be made to add.
 *
 */
import java.util.PriorityQueue;

public class KthLargestElementInAStream {

    // V0
    // IDEA : SMALL PRIORITY QUEUE
    //      -> NOTE !!!
    //      -> min-heap (min means that the heap will remove/find the smallest element,
    //      -> a max heap is the same thing but for the largest element)
    //
    //      -> so if we maintain a k size min-heap, when we get smallest, it is k largest element of the nums
    //      -> and we DON'T care rest of the element, so we can remove them
    //
    //      -> summary : maintain a k size min-heap, remove elements when there is a new num added and when size > k
    class KthLargest {

        // attr
        PriorityQueue<Integer> heap;
        int k;

        // constructor
        public KthLargest(int k, int[] nums) {

            this.k = k;
            /**
             * // NOTE !!! we use small PQ
             * e.g. : small -> big (increasing order)
             *
             * -> so, we pop elements when PQ size  > k
             * -> and we're sure that the top element is the `k th biggest element
             * -> e.g. (k-big, k-1 big, k-2 big, ...., 2 big, 1 big)
             */
            this.heap = new PriorityQueue<>();
            for (int x : nums){
                //this.heap.add(x); // this one is OK as well
                this.heap.offer(x);
            }

            // pop elements if heap size > k
            while(this.heap.size() > k){
                //this.heap.remove(); // this one is OK as well
                this.heap.poll();
            }
        }

        public int add(int val) {

            this.heap.offer(val);
            if (heap.size() > k){
                //this.heap.remove(); // this one is OK as well
                this.heap.poll();
            }

            return this.heap.peek();
        }
    }

    // V0-1
    // IDEA: SMALL PQ (fixed by gpt)
    class KthLargest_0_1 {
        int k;
        int[] nums;
        PriorityQueue<Integer> pq;

        public KthLargest_0_1(int k, int[] nums) {
            this.k = k;
            this.nums = nums; // ???
            /**
             * // NOTE !!! we use small PQ
             * e.g. : small -> big (increasing order)
             *
             * -> so, we pop elements when PQ size  > k
             * -> and we're sure that the top element is the `k th biggest element
             * -> e.g. (k-big, k-1 big, k-2 big, ...., 2 big, 1 big)
             */
            this.pq = new PriorityQueue<>();
            // NOTE !!! we also need to add eleemnts to PQ
            for (int x : nums) {
                pq.add(x);
            }
        }

        public int add(int val) {
            this.pq.add(val);
            // NOTE !!! need to remove elements when PQ size > k
            while (this.pq.size() > k) {
                this.pq.poll();
            }
            if (!this.pq.isEmpty()) {
                return this.pq.peek();
            }
            return -1;
        }
    }

    // V0-2
    // IDEA: MIN PQ (fixed by gpt)
    class KthLargest_0_2 {
        int k;
        PriorityQueue<Integer> pq;

        public KthLargest_0_2(int k, int[] nums) {
            this.k = k;
            this.pq = new PriorityQueue<>(); // Default is min-heap

            for (int num : nums) {
                add(num); // Use add() to maintain heap size correctly
            }
        }

        public int add(int val) {
            pq.add(val);

            if (pq.size() > k) {
                pq.poll(); // Remove smallest to maintain k largest
            }

            return pq.peek(); // k-th largest is always the smallest in the heap
        }
    }


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
