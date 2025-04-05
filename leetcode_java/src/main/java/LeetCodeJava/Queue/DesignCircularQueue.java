package LeetCodeJava.Queue;

// https://leetcode.com/problems/design-circular-queue/description/
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
//    class MyCircularQueue {
//
//        public MyCircularQueue(int k) {
//
//        }
//
//        public boolean enQueue(int value) {
//
//        }
//
//        public boolean deQueue() {
//
//        }
//
//        public int Front() {
//
//        }
//
//        public int Rear() {
//
//        }
//
//        public boolean isEmpty() {
//
//        }
//
//        public boolean isFull() {
//
//        }
//    }


    // V1
    // https://youtu.be/aBbsfn863oA?si=zekB_25Mq5KHIxJl

    // V2-1
    // https://leetcode.com/problems/design-circular-queue/solutions/1141876/js-python-java-c-simple-array-solution-w-fbum/
    // IDEA: ARRAY
    class MyCircularQueue_2_1 {
        int maxSize, head = 0, tail = -1;
        int[] data;

        public MyCircularQueue_2_1(int k) {
            data = new int[k];
            maxSize = k;
        }

        public boolean enQueue(int val) {
            if (isFull())
                return false;
            tail = (tail + 1) % maxSize;
            data[tail] = val;
            return true;
        }

        public boolean deQueue() {
            if (isEmpty())
                return false;
            if (head == tail) {
                head = 0;
                tail = -1;
            } else
                head = (head + 1) % maxSize;
            return true;
        }

        public int Front() {
            return isEmpty() ? -1 : data[head];
        }

        public int Rear() {
            return isEmpty() ? -1 : data[tail];
        }

        public boolean isEmpty() {
            return tail == -1;
        }

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

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    class MyCircularQueue_2_2 {
        int maxSize, size = 0;
        ListNode head = null, tail = null;

        public MyCircularQueue_2_2(int k) {
            maxSize = k;
        }

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

        public boolean deQueue() {
            if (isEmpty())
                return false;
            head = head.next;
            size--;
            return true;
        }

        public int Front() {
            return isEmpty() ? -1 : head.val;
        }

        public int Rear() {
            return isEmpty() ? -1 : tail.val;
        }

        public boolean isEmpty() {
            return size == 0;
        }

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

        public int Front() {
            // handle empty queue case
            if (isEmpty())
                return -1;
            // return the head element
            return q[headIdx];
        }

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

        public boolean isEmpty() {
            // no element in the queue
            return cnt == 0;
        }

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
        public MyCircularQueue_4(int k) {
            arr = new int[k];
            cap = k;
            front = -1;
            rear = -1;
        }

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

        public int Front() {
            if (front == -1)
                return -1;
            return arr[front];
        }

        public int Rear() {
            if (rear == -1)
                return -1;
            return arr[rear];
        }

        public boolean isEmpty() {
            return front == -1;
        }

        public boolean isFull() {
            return front != -1 && next(rear) == front;
        }
    }

}
