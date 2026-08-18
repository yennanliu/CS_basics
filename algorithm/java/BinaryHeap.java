import java.util.Arrays;

/**
 *  BINARY HEAP -- the array-backed max priority queue
 *
 *  A binary heap is a COMPLETE binary tree obeying the heap property:
 *  every parent is >= both of its children (max-heap). Because the tree
 *  is complete it needs no node objects -- it lives in an array, and
 *  index arithmetic IS the tree.
 *
 *  Storing the root at index 1 (leaving slot 0 unused) keeps the
 *  arithmetic clean:
 *
 *      parent of k : k / 2           index   1  2  3  4  5
 *      left   of k : k * 2           value   9  5  6  2  3
 *      right  of k : k * 2 + 1
 *
 *              9
 *            /   \          NOTE the heap property is only a
 *           5     6         PARENT/CHILD rule -- siblings are
 *          / \              unordered, so a heap is NOT a sorted
 *         2   3             array. It only guarantees a cheap maximum.
 *
 *  THE TWO MOVES
 *    swim(k)  a node is too BIG for its parent -> swap upward.
 *             Used by insert(): put the new key in the first free slot
 *             and let it rise.
 *    sink(k)  a node is too SMALL for its children -> swap down with
 *             the LARGER child. Used by delMax(): move the last key to
 *             the root and let it fall.
 *
 *  Both walk one root-to-leaf path of a complete tree, so both are
 *  O(log N). Compare UnorderedMaxPQ.java, where delMax is O(N).
 *
 *  Time  : insert O(log N), delMax O(log N), max O(1)
 *  Space : O(N)
 *
 *  Reference: https://www.coursera.org/learn/algorithms-part1/lecture/ZjoSM/binary-heaps
 */
public class BinaryHeap<Key extends Comparable<Key>> {

    private Key[] pq;   // heap-ordered complete binary tree in pq[1..n]; pq[0] unused
    private int n;      // number of keys

    @SuppressWarnings("unchecked")
    public BinaryHeap(int capacity) {
        pq = (Key[]) new Comparable[Math.max(capacity, 1) + 1];
        n = 0;
    }

    public BinaryHeap() {
        this(8);
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    /** The largest key, without removing it -- it is always at the root. */
    public Key max() {
        if (isEmpty()) {
            throw new IllegalStateException("priority queue is empty");
        }
        return pq[1];
    }

    /** Add a key at the end of the array, then let it swim up. */
    public void insert(Key x) {
        if (n == pq.length - 1) {
            resize(2 * pq.length);
        }
        pq[++n] = x;
        swim(n);
    }

    /** Remove and return the largest key: take the root, move the last key up, sink it. */
    public Key delMax() {
        if (isEmpty()) {
            throw new IllegalStateException("priority queue is empty");
        }
        Key max = pq[1];
        exchange(1, n--);      // the last key takes the root
        pq[n + 1] = null;      // avoid holding on to a dead reference
        sink(1);
        return max;
    }

    /** Move pq[k] up while it is larger than its parent. */
    private void swim(int k) {
        while (k > 1 && less(k / 2, k)) {
            exchange(k / 2, k);
            k = k / 2;
        }
    }

    /** Move pq[k] down while it is smaller than its larger child. */
    private void sink(int k) {
        while (2 * k <= n) {
            int child = 2 * k;
            // pick the LARGER child -- swapping with the smaller one
            // would leave the heap property broken
            if (child < n && less(child, child + 1)) {
                child++;
            }
            if (!less(k, child)) {
                break;            // heap property restored
            }
            exchange(k, child);
            k = child;
        }
    }

    private boolean less(int i, int j) {
        return pq[i].compareTo(pq[j]) < 0;
    }

    private void exchange(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        Key[] copy = (Key[]) new Comparable[capacity];
        System.arraycopy(pq, 1, copy, 1, n);
        pq = copy;
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOfRange(pq, 1, n + 1));
    }

    public static void main(String[] args) {
        BinaryHeap<Integer> heap = new BinaryHeap<>(10);
        assertThat(heap.isEmpty(), "a new heap is empty");

        for (int value : new int[] {3, 9, 5, 1, 6}) {
            heap.insert(value);
        }
        assertThat(heap.size() == 5, "five keys");
        assertThat(heap.max() == 9, "the root is the maximum");
        assertThat(heap.size() == 5, "max does not remove");

        // draining a max-heap yields descending order
        assertThat(heap.delMax() == 9, "9 first");
        assertThat(heap.delMax() == 6, "then 6");
        assertThat(heap.delMax() == 5, "then 5");
        assertThat(heap.delMax() == 3, "then 3");
        assertThat(heap.delMax() == 1, "then 1");
        assertThat(heap.isEmpty(), "drained");

        try {
            heap.delMax();
            assertThat(false, "expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // ok
        }

        // a node with only a LEFT child must not read past the end
        BinaryHeap<Integer> odd = new BinaryHeap<>(4);
        odd.insert(1);
        odd.insert(2);
        odd.insert(3);
        assertThat(odd.delMax() == 3 && odd.delMax() == 2 && odd.delMax() == 1, "odd sizes are safe");

        // duplicates, and growth past the initial capacity
        BinaryHeap<Integer> many = new BinaryHeap<>(1);
        int[] input = {5, 1, 5, 9, 2, 9, 0};
        for (int value : input) {
            many.insert(value);
        }
        int[] drained = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            drained[i] = many.delMax();
        }
        assertThat(Arrays.toString(drained).equals("[9, 9, 5, 5, 2, 1, 0]"), "sorted descending");

        BinaryHeap<String> words = new BinaryHeap<>();
        words.insert("pear");
        words.insert("apple");
        words.insert("zebra");
        assertThat(words.delMax().equals("zebra"), "any Comparable works");

        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
