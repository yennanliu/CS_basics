package LeetCodeJava.Heap;

// https://leetcode.com/problems/find-median-from-data-stream/?envType=list&envId=xoqag3yj

import java.util.*;

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
    // IDEA: SMALL, BIG PQ (fixed by gpt) + `rebalance` PQ
    class MedianFinder {

        /**
         * - small_pq: A max-heap (stores the `smaller` half of the numbers).
         *            The root will always be the largest number
         *            in the smaller half.
         *
         * - big_pq: A min-heap (stores the `larger` half of the numbers).
         *           The root will always be the smallest
         *           number in the larger half.
         *
         *
         *
         *  -> NOTE !!!
         *
         *   small_pq : MAX PQ
         *   big_pq : MIN PQ
         *
         */

        PriorityQueue<Integer> small_pq; // max-heap (stores smaller half)
        PriorityQueue<Integer> big_pq;   // min-heap (stores larger half)

        public MedianFinder() {
            // Initialize small_pq as a max-heap
            this.small_pq = new PriorityQueue<>(Comparator.reverseOrder());

            // Initialize big_pq as a min-heap (default behavior)
            this.big_pq = new PriorityQueue<>();
        }

        public void addNum(int num) {
            // Add the new number to the appropriate heap
            if (this.small_pq.isEmpty() || num <= this.small_pq.peek()) {
                this.small_pq.add(num);
            } else {
                this.big_pq.add(num);
            }


            /**
             *  NOTE !!!
             *
             *   The crucial part is to maintain the `balance`
             *   between the two heaps so that they
             *   `have roughly the SAME NUMBER of elements.`
             *
             *
             *   -> so in `rebalance` code below,
             *   -> we try to keep `size difference` <= 1
             *
             */
            // Rebalance the heaps to maintain a size difference of at most 1
            if (this.small_pq.size() > this.big_pq.size() + 1) {
                this.big_pq.add(this.small_pq.poll());
            } else if (this.big_pq.size() > this.small_pq.size() + 1) {
                this.small_pq.add(this.big_pq.poll());
            }
        }

        public double findMedian() {

            /**
             *  to find the median:
             *
             * - If the total number of elements is `odd`,
             *   the median is the root of the larger heap
             *   (which will have one more element).
             *
             *
             * - If the total number of elements is `even`,
             *   the median is the average of the roots of both heaps.
             *
             */
            int size = this.small_pq.size() + this.big_pq.size();

            if (size % 2 == 1) {
                // Odd number of elements, median is the top of the larger heap
                if (this.small_pq.size() > this.big_pq.size()) {
                    return this.small_pq.peek();
                } else {
                    return this.big_pq.peek();
                }
            } else {
                // Even number of elements, median is the average of the top of both heaps
                return (this.small_pq.peek() + this.big_pq.peek()) / 2.0;
            }
        }
    }


    // V0-1
    // IDEA: SORTING (TLE)
    class MedianFinder_0_1 {

        // attr
        List<Integer> collected;
        int cnt;

        public MedianFinder_0_1() {
            this.cnt = 0;
            this.collected = new ArrayList<>();
        }

        public void addNum(int num) {
            this.collected.add(num);
            // sort (increasing) (small -> big)
            Collections.sort(this.collected, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    int diff = o1 - o2;
                    return diff;
                }
            });
            this.cnt += 1;
        }

        public double findMedian() {
            if (this.cnt == 0 || this.collected == null) {
                return 0;
            }
            if (this.cnt == 1) {
                return this.collected.get(0);
            }

            /**
             * if cnt is odd, [1,2,3]
             * if cnt is even, [1,2,3,4]
             */
            // System.out.println(">>> this.cnt= " + this.cnt + ", this.collected = " +
            // this.collected);
            /**
             *  NOTE !!!
             *
             *   be careful on which val we use:
             *
             *    this.cnt VS array's leftmost idx
             *
             */
            int midIdx = -1;
            if (this.cnt % 2 == 0) {
                midIdx = this.cnt / 2;
                return (this.collected.get(midIdx) + this.collected.get(midIdx - 1)) / 2.0;
            } else {
                // midIdx = this.cnt / 2;
                return this.collected.get(this.cnt / 2);
            }
        }
    }

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
    /**
     * Time Complexity:
     *  - addNum(int num):
     *      The time complexity of adding an element is O(log N)
     *      due to the heap operations (insert and balance).
     *
     *  - findMedian():
     *   The time complexity is O(1) because the median is
     *   always the root of one or both heaps, which can be accessed in constant time.
     *
     */
    public class MedianFinder_1_2 {

        private Queue<Integer> smallHeap; // small elements - maxHeap
        private Queue<Integer> largeHeap; // large elements - minHeap

        public MedianFinder_1_2() {
            smallHeap = new PriorityQueue<>((a, b) -> b - a);
            largeHeap = new PriorityQueue<>((a, b) -> a - b);
        }

        /**
         *  Steps
         *
         *  step 1) First, the number is added to the smallHeap (max-heap).
         *
         *  step 2) Then, the method checks if the size of smallHeap exceeds
         *          the size of largeHeap by more than 1, or if the root of
         *          smallHeap (the largest element in the smaller half) is greater than
         *          the root of largeHeap (the smallest element in the larger half).
         *          If either condition is true, the root of smallHeap is moved to largeHeap.
         *
         *  step 3) If the size of largeHeap exceeds the size of smallHeap by more than 1,
         *          the root of largeHeap (the smallest element
         *          in the larger half) is moved to smallHeap.
         *
         *
         *  step 4) This balancing ensures that the two heaps are either of
         *          the same size or smallHeap has one extra element than largeHeap.
         *
         */
        public void addNum(int num) {
            smallHeap.add(num);
            if (smallHeap.size() - largeHeap.size() > 1 ||
                    !largeHeap.isEmpty() &&
                            smallHeap.peek() > largeHeap.peek()) {
                largeHeap.add(smallHeap.poll());
            }
            if (largeHeap.size() - smallHeap.size() > 1) {
                smallHeap.add(largeHeap.poll());
            }
        }

        /**
         *  Steps
         *
         *   This method returns the median of all the elements in the data structure.
         *
         * step 1) If both heaps are of the same size, the median
         *         is the average of the roots of both heaps
         *         (since the heaps are balanced).
         *
         * step 2) If smallHeap has more elements than largeHeap
         *         (i.e., smallHeap has one extra element),
         *         the median is the root of smallHeap.
         *
         * step 3) If largeHeap has more elements
         *        (which can happen, but it shouldn't happen frequently due to balancing),
         *        the median is the root of largeHeap.
         *
         */
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
