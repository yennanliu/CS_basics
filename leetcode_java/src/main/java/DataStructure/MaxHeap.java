package DataStructure;

/**
 *  MAX HEAP -- array implementation from scratch
 *
 *  Scope: the mirror image of MinHeap -- identical code with every
 *         comparison flipped. In real Java code use
 *         `new PriorityQueue&lt;&gt;(Collections.reverseOrder())`; this
 *         exists to show what that does inside.
 *
 *  A binary heap is a COMPLETE binary tree, so it needs no node objects:
 *  the array index IS the tree. Storing the root at index 1 (leaving
 *  slot 0 unused) keeps the arithmetic clean:
 *
 *      parent of i : i / 2           index   1  2  3  4  5
 *      left   of i : i * 2           value   9  5  6  2  3
 *      right  of i : i * 2 + 1
 *
 *              9
 *            /   \           MAX-heap property:
 *           5     6          every parent >= both children.
 *          / \               Siblings are NOT ordered, so a heap is not
 *         2   3              a sorted array -- it only guarantees a
 *                            cheap MAXIMUM at the root.
 *
 *  THE TWO MOVES
 *    add():  put the new value in the first free slot, then SIFT UP --
 *            swap with the parent while it is LARGER.
 *    pop():  take the root, move the LAST element into the root, then
 *            SIFT DOWN -- swap with the LARGER child while it is
 *            smaller than one of them.
 *  Both walk one root-to-leaf path of a complete tree, so both are O(log N).
 *
 *  Time  : peek O(1), add O(log N), pop O(log N), size O(1)
 *  Space : O(N)
 *
 *  Reference: https://leetcode.com/explore/learn/card/heap/643/heap/4017/
 */
public class MaxHeap {

    /** The tree, 1-indexed: maxHeap[0] is deliberately unused. */
    int[] maxHeap;

    /** Capacity -- the number of elements this heap can hold. */
    int heapSize;

    /** How many elements are actually stored. */
    int realSize = 0;

    public MaxHeap(int heapSize) {
        this.heapSize = heapSize;
        // one extra slot because indexing starts at 1
        maxHeap = new int[heapSize + 1];
        maxHeap[0] = 0;   // never read
    }

    /** How many elements are stored. */
    public int size() {
        return realSize;
    }

    public boolean isEmpty() {
        return realSize == 0;
    }

    /** Add an element, then bubble it UP to its place. */
    public void add(int element) {
        if (realSize >= heapSize) {
            throw new IllegalStateException("heap is full (capacity=" + heapSize + ")");
        }

        realSize++;
        maxHeap[realSize] = element;

        int index = realSize;
        while (index > 1 && maxHeap[index] > maxHeap[index / 2]) {
            int parent = index / 2;
            swap(index, parent);
            index = parent;
        }
    }

    /** The maximum, in O(1). It is always at the root. */
    public int peek() {
        if (isEmpty()) {
            throw new IllegalStateException("heap is empty");
        }
        return maxHeap[1];
    }

    /** Remove and return the maximum, then bubble the new root DOWN. */
    public int pop() {
        if (isEmpty()) {
            throw new IllegalStateException("heap is empty");
        }

        int largest = maxHeap[1];
        maxHeap[1] = maxHeap[realSize];   // the last element takes the root
        realSize--;

        int index = 1;
        while (index * 2 <= realSize) {   // while the node has a left child
            int left = index * 2;
            int right = left + 1;

            // NOTE the `right <= realSize` guard. The right child may not
            // exist; without this we would read a slot past the end --
            // which still holds a stale value from an earlier pop and
            // silently corrupts the heap.
            int child = (right <= realSize && maxHeap[right] > maxHeap[left]) ? right : left;

            if (maxHeap[index] >= maxHeap[child]) {
                break;                    // heap property restored
            }
            swap(index, child);
            index = child;
        }

        return largest;
    }

    private void swap(int i, int j) {
        int temp = maxHeap[i];
        maxHeap[i] = maxHeap[j];
        maxHeap[j] = temp;
    }

    /** The array form, e.g. "[3,1,2]". Not sorted -- it is a heap. */
    @Override
    public String toString() {
        if (realSize == 0) {
            return "No element!";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 1; i <= realSize; i++) {
            sb.append(maxHeap[i]);
            if (i < realSize) {
                sb.append(',');
            }
        }
        return sb.append(']').toString();
    }

    public static void main(String[] args) {
        MaxHeap heap = new MaxHeap(5);
        assertThat(heap.isEmpty(), "a new heap is empty");
        assertThat(heap.toString().equals("No element!"), "empty display");

        heap.add(1);
        heap.add(2);
        heap.add(3);
        assertThat(heap.toString().equals("[3,1,2]"), "array form; the tree is 3 -> (1, 2)");
        assertThat(heap.size() == 3, "three elements");

        assertThat(heap.peek() == 3, "the root is the maximum");
        assertThat(heap.size() == 3, "peek does not remove");

        assertThat(heap.pop() == 3, "largest out first");
        assertThat(heap.pop() == 2, "then 2");
        assertThat(heap.pop() == 1, "then 1");
        assertThat(heap.isEmpty(), "drained");

        heap.add(4);
        heap.add(5);
        assertThat(heap.toString().equals("[5,4]"), "reusable after draining");

        // a node with only a LEFT child must not read past the end
        MaxHeap odd = new MaxHeap(5);
        odd.add(1);
        odd.add(2);
        odd.add(3);
        assertThat(odd.pop() == 3 && odd.pop() == 2 && odd.pop() == 1, "odd sizes are safe");

        // draining always yields reverse-sorted output
        MaxHeap many = new MaxHeap(8);
        for (int value : new int[] {8, 3, 5, 1, 7, 2}) {
            many.add(value);
        }
        StringBuilder drained = new StringBuilder();
        while (!many.isEmpty()) {
            drained.append(many.pop());
        }
        assertThat(drained.toString().equals("875321"), "drains in descending order");

        MaxHeap full = new MaxHeap(1);
        full.add(1);
        try {
            full.add(2);
            assertThat(false, "expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // ok
        }

        try {
            new MaxHeap(1).pop();
            assertThat(false, "expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // ok
        }

        System.out.println(heap);
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
