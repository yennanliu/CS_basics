package LeetCodeJava.Queue;

// https://leetcode.com/problems/design-circular-queue/description/

import java.util.Deque;
import java.util.LinkedList;

/**
 * 622. Design Circular Queue
 * Medium
 * Topics
 * Companies
 * Design your implementation of the circular queue. The circular queue is a linear data structure in which the operations are performed based on FIFO (First In First Out) principle, and the last position is connected back to the first position to make a circle. It is also called "Ring Buffer".
 *
 * One of the benefits of the circular queue is that we can make use of the spaces in front of the queue. In a normal queue, once the queue becomes full, we cannot insert the next element even if there is a space in front of the queue. But using the circular queue, we can use the space to store new values.
 *
 * Implement the MyCircularQueue class:
 *
 * MyCircularQueue(k) Initializes the object with the size of the queue to be k.
 * int Front() Gets the front item from the queue. If the queue is empty, return -1.
 * int Rear() Gets the last item from the queue. If the queue is empty, return -1.
 * boolean enQueue(int value) Inserts an element into the circular queue. Return true if the operation is successful.
 * boolean deQueue() Deletes an element from the circular queue. Return true if the operation is successful.
 * boolean isEmpty() Checks whether the circular queue is empty or not.
 * boolean isFull() Checks whether the circular queue is full or not.
 * You must solve the problem without using the built-in queue data structure in your programming language.
 *
 *
 *
 * Example 1:
 *
 * Input
 * ["MyCircularQueue", "enQueue", "enQueue", "enQueue", "enQueue", "Rear", "isFull", "deQueue", "enQueue", "Rear"]
 * [[3], [1], [2], [3], [4], [], [], [], [4], []]
 * Output
 * [null, true, true, true, false, 3, true, true, true, 4]
 *
 * Explanation
 * MyCircularQueue myCircularQueue = new MyCircularQueue(3);
 * myCircularQueue.enQueue(1); // return True
 * myCircularQueue.enQueue(2); // return True
 * myCircularQueue.enQueue(3); // return True
 * myCircularQueue.enQueue(4); // return False
 * myCircularQueue.Rear();     // return 3
 * myCircularQueue.isFull();   // return True
 * myCircularQueue.deQueue();  // return True
 * myCircularQueue.enQueue(4); // return True
 * myCircularQueue.Rear();     // return 4
 *
 *
 * Constraints:
 *
 * 1 <= k <= 1000
 * 0 <= value <= 1000
 * At most 3000 calls will be made to enQueue, deQueue, Front, Rear, isEmpty, and isFull.
 *
 */
public class DesignCircularQueue {

    /**
     * Your MyCircularQueue object will be instantiated and called as such:
     * MyCircularQueue obj = new MyCircularQueue(k);
     * boolean param_1 = obj.enQueue(value);
     * boolean param_2 = obj.deQueue();
     * int param_3 = obj.Front();
     * int param_4 = obj.Rear();
     * boolean param_5 = obj.isEmpty();
     * boolean param_6 = obj.isFull();
     */


    // V0
    // IDEA: array + head idx + elementCnt +`circular` handling (gemini)
    class MyCircularQueue_0 {
        // Underlying primitive array used to store our queue elements
        private final int[] queue;

        // The maximum capacity size limits of our circular queue
        private final int capacity;

        // Pointer index tracking the absolute front (head) of the queue
        private int head;

        // Scalar counter tracking exactly how many elements are currently active
        private int elementCnt;

        /**
         * Constructor: Initializes the object data structure with a fixed size k.
         */
        public MyCircularQueue_0(int k) {
            this.capacity = k;
            this.queue = new int[k]; // Allocate memory for the fixed array
            this.head = 0; // Start the read pointer at the first index
            this.elementCnt = 0; // Initially, the queue contains no elements
        }

        /**
         * Inserts an element into the circular queue. Return true if successful.
         */
        public boolean enQueue(int value) {
            // If the queue is already full, we cannot insert anymore elements
            if (isFull()) {
                return false;
            }

            /** NOTE !!!
             *
             *  how we get `to insert` index
             */
            // Calculate the next empty write slot (tail) dynamically.
            // The next spot is logically 'elementCnt' steps ahead of the head.
            // We use modulo (%) to gracefully wrap around back to 0 if it exceeds capacity.
            int tail = (head + elementCnt) % capacity;

            // Store the value into our computed circular target index
            queue[tail] = value;

            // Increment the total count of elements inside our queue
            elementCnt++;

            // Return true to indicate a successful enqueue operation
            return true;
        }

        /**
         * Deletes an element from the circular queue. Return true if successful.
         */
        public boolean deQueue() {
            // If the queue is empty, there is nothing left to remove
            if (isEmpty()) {
                return false;
            }

            /** NOTE !!!
             *
             *  how we get `new head` index
             */
            // Advance the head pointer forward by 1 to point to the next valid element.
            // We apply modulo (%) here too, so that if head steps off the end of the array,
            // it wraps back around to index 0 smoothly.
            head = (head + 1) % capacity;

            // Decrement our total active element count
            elementCnt--;

            // Return true to indicate a successful dequeue operation
            return true;
        }

        /**
         * Gets the front item from the queue. If empty, returns -1.
         */
        public int Front() {
            // Guard check: return -1 if there are no elements to inspect
            if (isEmpty()) {
                return -1;
            }
            // Return the item sitting exactly where the head pointer is looking
            return queue[head];
        }

        /**
         * Gets the last item from the queue. If empty, returns -1.
         */
        public int Rear() {
            // Guard check: return -1 if there are no elements to inspect
            if (isEmpty()) {
                return -1;
            }

            // Calculate the index of the absolute last element currently added.
            // The last item sits exactly 'elementCnt - 1' steps ahead of our head.
            // Modulo (%) prevents index out of bounds by handling structural wrap arounds.
            int tail = (head + elementCnt - 1) % capacity;

            // Return the item sitting at our calculated tail index
            return queue[tail];
        }

        /**
         * Checks whether the circular queue is empty or not.
         */
        public boolean isEmpty() {
            // The queue is empty if and only if our active counter reads exactly 0
            return elementCnt == 0;
        }

        /**
         * Checks whether the circular queue is full or not.
         */
        public boolean isFull() {
            // The queue is full if our active counter reaches our designated maximum capacity
            return elementCnt == capacity;
        }
    }




    // V0-1
    // IDEA: DEQUEUE
    /**
     * time = O(1)
     * space = O(k)
     */
    class MyCircularQueue {
        int capacity;
        Deque<Integer> dq;

        /**
         * time = O(1)
         * space = O(k)
         */
        public MyCircularQueue(int k) {
            this.capacity = k;
            this.dq = new LinkedList<>(); // ???
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean enQueue(int value) {
            if (this.isFull()) {
                return false;
            }
            this.dq.addLast(value);
            return true;

        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean deQueue() {
            if (this.isEmpty()) {
                return false;
            }
            /** NOTE !!!
             *
             *  we `remove` element from `FIRST`,
             *  so it ensures that there is always
             *  an element at that idx can be removed.
             */
            this.dq.pollFirst();
            return true;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int Front() {
            if (this.isEmpty()) {
                return -1;
            }
            return this.dq.getFirst(); // Queue: FIFO
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int Rear() {
            if (this.isEmpty()) {
                return -1;
            }
            return this.dq.getLast(); // Queue: FIFO
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean isEmpty() {
            return this.dq.isEmpty();
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean isFull() {
            return this.dq.size() == this.capacity;
        }

    }


    // V0-2
    // IDEA: START, END IDX + ARRAY (GPT)
    class MyCircularQueue_0_2 {

        int[] q;
        int size;
        int count;

        int front;
        int rear;

        public MyCircularQueue_0_2(int k) {
            q = new int[k];
            size = k;
            count = 0;

            front = 0;
            rear = -1;
        }

        public boolean enQueue(int value) {
            if (isFull()) {
                return false;
            }

            rear = (rear + 1) % size;
            q[rear] = value;
            count++;

            return true;
        }

        public boolean deQueue() {
            if (isEmpty()) {
                return false;
            }

            front = (front + 1) % size;
            count--;

            return true;
        }

        public int Front() {
            if (isEmpty()) {
                return -1;
            }

            return q[front];
        }

        public int Rear() {
            if (isEmpty()) {
                return -1;
            }

            return q[rear];
        }

        public boolean isEmpty() {
            return count == 0;
        }

        public boolean isFull() {
            return count == size;
        }
    }


    // V0-3
    // IDEA: START, END IDX + ARRAY (GEMINI)
    class MyCircularQueue_0_3 {
        private final int[] queue;
        private final int capacity;
        private int head;
        private int elementCnt;

        public MyCircularQueue_0_3(int k) {
            this.capacity = k;
            this.queue = new int[k];
            this.head = 0;
            this.elementCnt = 0;
        }

        public boolean enQueue(int value) {
            if (isFull()) {
                return false;
            }
            /** NOTE !!!
             *
             *  how we get `insertion` index
             */
            // Calculate `insertion` index:
            // (head + current count) wrapped around capacity
            int tail = (head + elementCnt) % capacity;
            queue[tail] = value;
            elementCnt++;
            return true;
        }

        public boolean deQueue() {
            if (isEmpty()) {
                return false;
            }
            // Advance head pointer circularly
            head = (head + 1) % capacity;
            elementCnt--;
            return true;
        }

        public int Front() {
            if (isEmpty()) {
                return -1;
            }
            return queue[head];
        }

        public int Rear() {
            if (isEmpty()) {
                return -1;
            }
            // Calculate the last element's index dynamically
            int tail = (head + elementCnt - 1) % capacity;
            return queue[tail];
        }

        public boolean isEmpty() {
            return elementCnt == 0;
        }

        public boolean isFull() {
            return elementCnt == capacity;
        }
    }



    // V1
    // https://youtu.be/aBbsfn863oA?si=zekB_25Mq5KHIxJl

    // V2-1
    // https://leetcode.com/problems/design-circular-queue/solutions/1141876/js-python-java-c-simple-array-solution-w-fbum/
    // IDEA: ARRAY
    class MyCircularQueue_2_1 {
        /**
         *
         * maxSize: The maximum capacity of the circular queue (the size of the underlying array).
         *
         * head: The index of the `front` element in the queue.
         *
         * tail: The index of the `last` element in the queue.
         *
         * data: An array that holds the elements of the queue.
         *
         */
        int maxSize = 0;
        int head = 0;
        int tail = -1; // NOTE !!! we init tail as -1
        int[] data;

        /**
         * time = O(1)
         * space = O(k)
         */
        public MyCircularQueue_2_1(int k) {
            data = new int[k];
            maxSize = k;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean enQueue(int val) {
            if (isFull())  // Check if the queue is full.
                return false;  // If the queue is full, return false.
            /**
             *  NOTE !!!  below op
             *
             *  1)  we do `tail + 1` first,
             *     then adjust the idx (per circular setting)
             *     -> e.g. (tail + 1) % maxSize
             *
             *
             *  2) Update the tail index: The tail index is incremented
             *    in a circular fashion using the formula (tail + 1) % maxSize.
             *    This ensures that when tail reaches the end of the array,
             *    it wraps back around to the beginning.
             *
             */
            tail = (tail + 1) % maxSize;  // Increment the tail index in a circular manner.
            data[tail] = val;  // Place the value at the tail position.
            return true;  // Successfully added the value.
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean deQueue() {
            if (isEmpty())  // Check if the queue is empty.
                return false;  // If the queue is empty, return false.
            /**
             *  NOTE !!! below
             *
             *    if `ONLY 1 element in queue` -> after deque, there is NO element in queue
             *     -> so we reset head, tail to their init value (head=0, tail=-1)
             *
             *
             *  1) `head == tail` :  If there is only one element in the queue.
             *
             *
             *  2) Handle single element case: If head == tail,
             *     it means there is only one element in the queue. In this case,
             *     both head and tail are reset to 0 and -1 respectively to indicate that
             *     the queue is now empty.
             *
             */
            if (head == tail) {  // If there is only one element in the queue.
                head = 0;  // Reset head and tail to indicate an empty queue.
                tail = -1;
            } else {
                /**
                 *  NOTE !!!
                 *
                 *  Update head:
                 *     If there are multiple elements,
                 *    -> the head index is incremented in a circular manner
                 *    -> using (head + 1) % maxSize.
                 */
                head = (head + 1) % maxSize;  // Increment head in a circular manner.
            }
            return true;  // Successfully removed the element.
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int Front() {
            /**
             * If the queue is empty (isEmpty() returns true), return -1 (to indicate no elements).
             *
             * Otherwise, return the element at the head index of the data array.
             *
             */
            return isEmpty() ? -1 : data[head];
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int Rear() {
            /**
             * If the queue is empty (isEmpty() returns true), return -1 (to indicate no elements).
             *
             * Otherwise, return the element at the tail index of the data array.
             *
             */
            return isEmpty() ? -1 : data[tail];
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean isEmpty() {
            /**
             * Action: Returns true if the queue is empty, otherwise false.
             *
             * Condition: The queue is considered empty when tail == -1, which happens when the queue is newly initialized or when all elements have been dequeued.
             */
            return tail == -1;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean isFull() {
            return !isEmpty() && (tail + 1) % maxSize == head;
        }
    }

    // V2-2
    // https://leetcode.com/problems/design-circular-queue/solutions/1141876/js-python-java-c-simple-array-solution-w-fbum/
    // IDEA: ListNode
    class ListNode {
        int val;
        ListNode next;

        /**
         * time = O(1)
         * space = O(1)
         */
        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    class MyCircularQueue_2_2 {
        int maxSize, size = 0;
        ListNode head = null, tail = null;

        /**
         * time = O(1)
         * space = O(k)
         */
        public MyCircularQueue_2_2(int k) {
            maxSize = k;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean enQueue(int val) {
            if (isFull())
                return false;
            ListNode newNode = new ListNode(val, null);
            if (isEmpty())
                head = tail = newNode;
            else {
                tail.next = newNode;
                tail = tail.next;
            }
            size++;
            return true;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean deQueue() {
            if (isEmpty())
                return false;
            head = head.next;
            size--;
            return true;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int Front() {
            return isEmpty() ? -1 : head.val;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int Rear() {
            return isEmpty() ? -1 : tail.val;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean isEmpty() {
            return size == 0;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean isFull() {
            return size == maxSize;
        }
    }

    // V3
    // https://leetcode.com/problems/design-circular-queue/solutions/2620288/leetcode-the-hard-way-explained-line-by-93bil/
    // IDEA: ARRAY
    // Time Complexity: O(1)
    // Space Complexity: O(N)
    class MyCircularQueue_3 {

        /**
         * time = O(1)
         * space = O(k)
         */
        public MyCircularQueue_3(int k) {
            // the queue holding the elements for the circular queue
            q = new int[k];
            // the number of elements in the circular queue
            cnt = 0;
            // queue size
            sz = k;
            // the idx of the head element
            headIdx = 0;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean enQueue(int value) {
            // handle full case
            if (isFull())
                return false;
            // set the value
            // Given an array of size of 4, we can find the position to be inserted using the formula
            // targetIdx = (headIdx + cnt) % sz
            // e.g. [1, 2, 3, _]
            // headIdx = 0, cnt = 3, sz = 4, targetIdx = (0 + 3) % 4 = 3
            // e.g. [_, 2, 3, 4]
            // headIdx = 1, cnt = 3, sz = 4, targetIdx = (1 + 3) % 4 = 0
            q[(headIdx + cnt) % sz] = value;
            // increase the number of elements by 1
            cnt += 1;
            return true;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean deQueue() {
            // handle empty case
            if (isEmpty())
                return false;
            // update the head index
            headIdx = (headIdx + 1) % sz;
            // decrease the number of elements by 1
            cnt -= 1;
            return true;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int Front() {
            // handle empty queue case
            if (isEmpty())
                return -1;
            // return the head element
            return q[headIdx];
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int Rear() {
            // handle empty queue case
            if (isEmpty())
                return -1;
            // Given an array of size of 4, we can find the tailIdx using the formula
            // tailIdx = (headIdx + cnt - 1) % sz
            // e.g. [0 1 2] 3
            // headIdx = 0, cnt = 3, sz = 4, tailIdx = (0 + 3 - 1) % 4 = 2
            // e.g. 0 [1 2 3]
            // headIdx = 1, cnt = 3, sz = 4, tailIdx = (1 + 3 - 1) % 4 = 3
            // e.g. 0] 1 [2 3
            // headIdx = 2, cnt = 3, sz = 4, tailIdx = (2 + 3 - 1) % 4 = 0
            return q[(headIdx + cnt - 1) % sz];
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean isEmpty() {
            // no element in the queue
            return cnt == 0;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean isFull() {
            // return true if the count is equal to the queue size
            // else return false
            return cnt == sz;
        }

        private int[] q;
        private int headIdx, cnt, sz;
    }

    // V4
    // https://leetcode.com/problems/design-circular-queue/submissions/1597388031/
    // IDEA: ARRAY
    class MyCircularQueue_4 {

        private int front;
        private int rear;
        private int[] arr;
        private int cap;

        private int next(int i) { // to get next idx after i in circular queue
            return (i + 1) % cap;
        }

        private int prev(int i) { // to get prev idx before i in circular queue
            return (i + cap - 1) % cap;
        }

        // rest is as simple as implmenting a normal queue using array.
        /**
         * time = O(1)
         * space = O(k)
         */
        public MyCircularQueue_4(int k) {
            arr = new int[k];
            cap = k;
            front = -1;
            rear = -1;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean enQueue(int value) {
            if (isFull())
                return false;
            if (front == -1) {
                front = 0;
                rear = 0;
                arr[rear] = value;
                return true;
            }
            rear = next(rear);
            arr[rear] = value;
            return true;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean deQueue() {
            if (isEmpty())
                return false;
            if (front == rear) {
                front = -1;
                rear = -1;
                return true;
            }
            front = next(front);
            return true;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int Front() {
            if (front == -1)
                return -1;
            return arr[front];
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public int Rear() {
            if (rear == -1)
                return -1;
            return arr[rear];
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean isEmpty() {
            return front == -1;
        }

        /**
         * time = O(1)
         * space = O(1)
         */
        public boolean isFull() {
            return front != -1 && next(rear) == front;
        }
    }





}
