package LeetCodeJava.Design;

// https://leetcode.com/problems/design-circular-deque/description/

import java.util.ArrayDeque;
import java.util.Deque;
/**
 * 641. Design Circular Deque
 * Medium
 *
 * Design your implementation of the circular double-ended queue (deque).
 *
 * Implement the MyCircularDeque class:
 *
 * - MyCircularDeque(int k) Initializes the deque with a maximum size of k.
 * - boolean insertFront() Adds an item at the front of Deque. Returns true if the
 *   operation is successful, or false otherwise.
 * - boolean insertLast() Adds an item at the rear of Deque. Returns true if the
 *   operation is successful, or false otherwise.
 * - boolean deleteFront() Deletes an item from the front of Deque. Returns true if
 *   the operation is successful, or false otherwise.
 * - boolean deleteLast() Deletes an item from the rear of Deque. Returns true if
 *   the operation is successful, or false otherwise.
 * - int getFront() Returns the front item from the Deque. Returns -1 if the deque is empty.
 * - int getRear() Returns the last item from Deque. Returns -1 if the deque is empty.
 * - boolean isEmpty() Returns true if the deque is empty, or false otherwise.
 * - boolean isFull() Returns true if the deque is full, or false otherwise.
 *
 * Example 1:
 *
 * Input
 * ["MyCircularDeque", "insertLast", "insertLast", "insertFront", "insertFront", "getRear",
 *  "isFull", "deleteLast", "insertFront", "getFront"]
 * [[3], [1], [2], [3], [4], [], [], [], [4], []]
 * Output
 * [null, true, true, true, false, 2, true, true, true, 4]
 *
 * Explanation
 * MyCircularDeque myCircularDeque = new MyCircularDeque(3);
 * myCircularDeque.insertLast(1);  // return True
 * myCircularDeque.insertLast(2);  // return True
 * myCircularDeque.insertFront(3); // return True
 * myCircularDeque.insertFront(4); // return False, the queue is full.
 * myCircularDeque.getRear();      // return 2
 * myCircularDeque.isFull();       // return True
 * myCircularDeque.deleteLast();   // return True
 * myCircularDeque.insertFront(4); // return True
 * myCircularDeque.getFront();     // return 4
 *
 * Constraints:
 *
 * 1 <= k <= 1000
 * 0 <= value <= 1000
 * At most 2000 calls will be made to insertFront, insertLast, deleteFront,
 * deleteLast, getFront, getRear, isEmpty, isFull.
 *
 */
public class DesignCircularDeque {

    /**
     * Your MyCircularDeque object will be instantiated and called as such:
     * MyCircularDeque obj = new MyCircularDeque(k);
     * boolean param_1 = obj.insertFront(value);
     * boolean param_2 = obj.insertLast(value);
     * boolean param_3 = obj.deleteFront();
     * boolean param_4 = obj.deleteLast();
     * int param_5 = obj.getFront();
     * int param_6 = obj.getRear();
     * boolean param_7 = obj.isEmpty();
     * boolean param_8 = obj.isFull();
     */

    // V0
    // IDEA: FIXED ARRAY + (front, size) PAIR
    /**
     *   Track the index of the FRONT element and HOW MANY elements are stored.
     *   Everything else is modular arithmetic:
     *     - front element    -> q[front]
     *     - rear element     -> q[(front + size - 1) % capacity]
     *     - next rear slot   -> q[(front + size)     % capacity]
     *     - new front slot   -> q[(front - 1)        % capacity]
     *
     *   Storing `size` (rather than a rear pointer) removes the classic
     *   `full vs empty look identical` ambiguity, so no sacrificial slot is needed.
     *
     *   NOTE !!! java's `%` keeps the sign of the dividend, so `(front - 1) % capacity`
     *            would go NEGATIVE when front == 0 (python's `%` would not).
     *            -> we must write `(front - 1 + capacity) % capacity`.
     *
     *   time  = O(1) per operation
     *   space = O(k)
     */
    class MyCircularDeque {

        private int capacity;
        private int[] q;
        private int front;
        private int size;

        public MyCircularDeque(int k) {
            this.capacity = k;
            this.q = new int[k];
            this.front = 0;
            this.size = 0;
        }

        public boolean insertFront(int value) {
            if (isFull()) {
                return false;
            }
            // step the front pointer BACKWARDS (wraps to the end when front == 0)
            this.front = (this.front - 1 + capacity) % capacity;
            q[this.front] = value;
            this.size += 1;
            return true;
        }

        public boolean insertLast(int value) {
            if (isFull()) {
                return false;
            }
            q[(this.front + this.size) % capacity] = value;
            this.size += 1;
            return true;
        }

        public boolean deleteFront() {
            if (isEmpty()) {
                return false;
            }
            this.front = (this.front + 1) % capacity;
            this.size -= 1;
            return true;
        }

        public boolean deleteLast() {
            if (isEmpty()) {
                return false;
            }
            // the rear slot is DERIVED from size, so just shrinking is enough
            this.size -= 1;
            return true;
        }

        public int getFront() {
            return isEmpty() ? -1 : q[this.front];
        }

        public int getRear() {
            return isEmpty() ? -1 : q[(this.front + this.size - 1) % capacity];
        }

        public boolean isEmpty() {
            return this.size == 0;
        }

        public boolean isFull() {
            return this.size == this.capacity;
        }
    }


    // V1
    // IDEA: DOUBLY LINKED LIST
    /**
     *  Both ends are O(1) by construction, so there is no modular arithmetic and
     *  no `full vs empty` ambiguity at all -- the size counter alone enforces the
     *  capacity.
     *
     *  Costs one node object per element instead of a flat array.
     *
     *  time  = O(1) per operation
     *  space = O(k)
     */
    class MyCircularDeque_1 {

        private class DNode {
            int val;
            DNode prev;
            DNode next;

            DNode(int v) {
                this.val = v;
            }
        }

        private DNode head; // sentinel
        private DNode tail; // sentinel
        private int size;
        private int capacity;

        public MyCircularDeque_1(int k) {
            this.capacity = k;
            this.size = 0;
            this.head = new DNode(0);
            this.tail = new DNode(0);
            head.next = tail;
            tail.prev = head;
        }

        public boolean insertFront(int value) {
            if (isFull()) {
                return false;
            }
            link(head, new DNode(value), head.next);
            size += 1;
            return true;
        }

        public boolean insertLast(int value) {
            if (isFull()) {
                return false;
            }
            link(tail.prev, new DNode(value), tail);
            size += 1;
            return true;
        }

        public boolean deleteFront() {
            if (isEmpty()) {
                return false;
            }
            unlink(head.next);
            size -= 1;
            return true;
        }

        public boolean deleteLast() {
            if (isEmpty()) {
                return false;
            }
            unlink(tail.prev);
            size -= 1;
            return true;
        }

        public int getFront() {
            return isEmpty() ? -1 : head.next.val;
        }

        public int getRear() {
            return isEmpty() ? -1 : tail.prev.val;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean isFull() {
            return size == capacity;
        }

        private void link(DNode before, DNode node, DNode after) {
            before.next = node;
            node.prev = before;
            node.next = after;
            after.prev = node;
        }

        private void unlink(DNode node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    // V2
    // IDEA: RING BUFFER WITH A SACRIFICIAL SLOT (head / tail pointers, no size)
    /**
     *  The textbook circular buffer: allocate k + 1 slots and keep only two
     *  indices. `head == tail` then means EMPTY and `(tail + 1) % cap == head`
     *  means FULL -- the extra slot is what keeps those two states apart.
     *
     *  Trades one wasted slot for dropping the size counter, which is the classic
     *  embedded / lock-free formulation.
     *
     *  time  = O(1) per operation
     *  space = O(k)
     */
    class MyCircularDeque_2 {

        private int[] q;
        private int head;
        private int tail; // one PAST the last element
        private int cap;

        public MyCircularDeque_2(int k) {
            this.cap = k + 1; // the sacrificial slot
            this.q = new int[cap];
            this.head = 0;
            this.tail = 0;
        }

        public boolean insertFront(int value) {
            if (isFull()) {
                return false;
            }
            head = (head - 1 + cap) % cap;
            q[head] = value;
            return true;
        }

        public boolean insertLast(int value) {
            if (isFull()) {
                return false;
            }
            q[tail] = value;
            tail = (tail + 1) % cap;
            return true;
        }

        public boolean deleteFront() {
            if (isEmpty()) {
                return false;
            }
            head = (head + 1) % cap;
            return true;
        }

        public boolean deleteLast() {
            if (isEmpty()) {
                return false;
            }
            tail = (tail - 1 + cap) % cap;
            return true;
        }

        public int getFront() {
            return isEmpty() ? -1 : q[head];
        }

        public int getRear() {
            return isEmpty() ? -1 : q[(tail - 1 + cap) % cap];
        }

        public boolean isEmpty() {
            return head == tail;
        }

        public boolean isFull() {
            return (tail + 1) % cap == head;
        }
    }

    // V3
    // IDEA: DELEGATE TO java.util.ArrayDeque + an explicit capacity guard
    /**
     *  The JDK already has an amortised O(1) double-ended queue; the only thing
     *  the problem adds is the fixed capacity, so all this version does is guard
     *  every mutator with a size check.
     *
     *  The version you would actually write in production -- and the one that
     *  makes the other three easy to check.
     *
     *  time  = O(1) amortised per operation
     *  space = O(k)
     */
    class MyCircularDeque_3 {

        private Deque<Integer> dq;
        private int capacity;

        public MyCircularDeque_3(int k) {
            this.dq = new ArrayDeque<>(k);
            this.capacity = k;
        }

        public boolean insertFront(int value) {
            if (isFull()) {
                return false;
            }
            dq.addFirst(value);
            return true;
        }

        public boolean insertLast(int value) {
            if (isFull()) {
                return false;
            }
            dq.addLast(value);
            return true;
        }

        public boolean deleteFront() {
            if (isEmpty()) {
                return false;
            }
            dq.pollFirst();
            return true;
        }

        public boolean deleteLast() {
            if (isEmpty()) {
                return false;
            }
            dq.pollLast();
            return true;
        }

        public int getFront() {
            return isEmpty() ? -1 : dq.peekFirst();
        }

        public int getRear() {
            return isEmpty() ? -1 : dq.peekLast();
        }

        public boolean isEmpty() {
            return dq.isEmpty();
        }

        public boolean isFull() {
            return dq.size() == capacity;
        }
    }

}
