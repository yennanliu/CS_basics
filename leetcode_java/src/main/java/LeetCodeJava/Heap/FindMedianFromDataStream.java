package LeetCodeJava.Heap;

// https://leetcode.com/problems/find-median-from-data-stream/?envType=list&envId=xoqag3yj

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 295. Find Median from Data Stream
 * Hard
 * Topics
 * Companies
 * The median is the middle value in an ordered integer list. If the size of the list is even, there is no middle value, and the median is the mean of the two middle values.
 *
 * For example, for arr = [2,3,4], the median is 3.
 * For example, for arr = [2,3], the median is (2 + 3) / 2 = 2.5.
 * Implement the MedianFinder class:
 *
 * MedianFinder() initializes the MedianFinder object.
 * void addNum(int num) adds the integer num from the data stream to the data structure.
 * double findMedian() returns the median of all elements so far. Answers within 10-5 of the actual answer will be accepted.
 *
 *
 * Example 1:
 *
 * Input
 * ["MedianFinder", "addNum", "addNum", "findMedian", "addNum", "findMedian"]
 * [[], [1], [2], [], [3], []]
 * Output
 * [null, null, null, 1.5, null, 2.0]
 *
 * Explanation
 * MedianFinder medianFinder = new MedianFinder();
 * medianFinder.addNum(1);    // arr = [1]
 * medianFinder.addNum(2);    // arr = [1, 2]
 * medianFinder.findMedian(); // return 1.5 (i.e., (1 + 2) / 2)
 * medianFinder.addNum(3);    // arr[1, 2, 3]
 * medianFinder.findMedian(); // return 2.0
 *
 *
 * Constraints:
 *
 * -105 <= num <= 105
 * There will be at least one element in the data structure before calling findMedian.
 * At most 5 * 104 calls will be made to addNum and findMedian.
 *
 *
 * Follow up:
 *
 * If all integer numbers from the stream are in the range [0, 100], how would you optimize your solution?
 * If 99% of all integer numbers from the stream are in the range [0, 100], how would you optimize your solution?
 *
 *
 */

public class FindMedianFromDataStream {

    // V0
    // TODO : implement
//    class MedianFinder {
//
//        public MedianFinder() {
//
//        }
//
//        public void addNum(int num) {
//
//        }
//
//        public double findMedian() {
//            return 0.0;
//        }
//    }

    // V1-1
    // https://neetcode.io/problems/find-median-in-a-data-stream
    // IDEA: SORTING
    public class MedianFinder_1_1 {
        private ArrayList<Integer> data;

        public MedianFinder_1_1() {
            data = new ArrayList<>();
        }

        public void addNum(int num) {
            data.add(num);
        }

        public double findMedian() {
            Collections.sort(data);
            int n = data.size();
            if ((n & 1) == 1) {
                return data.get(n / 2);
            } else {
                return (data.get(n / 2) + data.get(n / 2 - 1)) / 2.0;
            }
        }
    }

    // V1-2
    // https://neetcode.io/problems/find-median-in-a-data-stream
    // IDEA: HEAP
    public class MedianFinder_1_2 {

        private Queue<Integer> smallHeap; //small elements - maxHeap
        private Queue<Integer> largeHeap; //large elements - minHeap

        public MedianFinder_1_2() {
            smallHeap = new PriorityQueue<>((a, b) -> b - a);
            largeHeap = new PriorityQueue<>((a, b) -> a - b);
        }

        public void addNum(int num) {
            smallHeap.add(num);
            if (
                    smallHeap.size() - largeHeap.size() > 1 ||
                            !largeHeap.isEmpty() &&
                                    smallHeap.peek() > largeHeap.peek()
            ) {
                largeHeap.add(smallHeap.poll());
            }
            if (largeHeap.size() - smallHeap.size() > 1) {
                smallHeap.add(largeHeap.poll());
            }
        }

        public double findMedian() {
            if (smallHeap.size() == largeHeap.size()) {
                return (double) (largeHeap.peek() + smallHeap.peek()) / 2;
            } else if (smallHeap.size() > largeHeap.size()) {
                return (double) smallHeap.peek();
            } else {
                return (double) largeHeap.peek();
            }
        }
    }

    // V-2
    // https://leetcode.com/problems/find-median-from-data-stream/solutions/74047/javapython-two-heap-solution-olog-n-add-gh8zw/
    // IDEA: HEAP
    class MedianFinder_2 {

        private PriorityQueue<Integer> small = new PriorityQueue<>(Collections.reverseOrder());
        private PriorityQueue<Integer> large = new PriorityQueue<>();
        private boolean even = true;

        public double MedianFinder_2() {
            if (even)
                return (small.peek() + large.peek()) / 2.0;
            else
                return small.peek();
        }

        public void addNum(int num) {
            if (even) {
                large.offer(num);
                small.offer(large.poll());
            } else {
                small.offer(num);
                large.offer(small.poll());
            }
            even = !even;
        }
    }

    // V3
    // https://leetcode.com/problems/find-median-from-data-stream/solutions/6446018/java-solutionfind-median-from-data-strea-mcjz/
    // IDEA: HEAP
    class MedianFinder_3 {

        PriorityQueue<Integer> maxHeap;
        PriorityQueue<Integer> minHeap;

        public MedianFinder_3() {
            maxHeap = new PriorityQueue<>(Collections.reverseOrder()); // . we need to store the elements in decreasing
            // order so we use reverse order.
            minHeap = new PriorityQueue<>();
        }

        public void addNum(int num) {
            maxHeap.offer(num);

            if (minHeap.size() > 0 && maxHeap.peek() > minHeap.peek()) {
                minHeap.offer(maxHeap.poll());
            }

            if (maxHeap.size() - minHeap.size() >= 2) {
                minHeap.offer(maxHeap.poll());
            } else if (minHeap.size() > maxHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
        }

        public double findMedian() {
            if (maxHeap.size() == minHeap.size()) {
                return (maxHeap.peek() + minHeap.peek()) / 2.0;
            } else {
                return maxHeap.peek();
            }
        }
    }


}
