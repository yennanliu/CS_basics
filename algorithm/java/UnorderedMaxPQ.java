import java.util.Arrays;

/**
 *  PRIORITY QUEUE -- unordered-array implementation
 *
 *  The BASELINE implementation, kept because it makes the case for the
 *  heap. Items go in unsorted, so insert is trivial; the whole cost
 *  moves into delMax(), which must scan everything to find the maximum.
 *
 *      insert 12, 1, 14, 7   ->  [12, 1, 14, 7]     (no ordering at all)
 *      delMax()              ->  scan all 4, take 14
 *      delMax()              ->  scan all 3, take 12
 *
 *  THE THREE DESIGNS, side by side:
 *
 *    backing store     insert      delMax      when it wins
 *    ---------------   --------    --------    -----------------------
 *    unordered array   O(1)        O(N)        many inserts, few deletes
 *    ordered array     O(N)        O(1)        few inserts, many deletes
 *    binary heap       O(log N)    O(log N)    balanced -- the default
 *
 *  Draining N items costs O(N^2) here versus O(N log N) with a heap,
 *  which is exactly why BinaryHeap.java exists.
 *
 *  Time  : insert O(1), delMax O(N), max O(N), isEmpty O(1)
 *  Space : O(N)
 *
 *  Reference: https://www.coursera.org/learn/algorithms-part1/lecture/A3kA3/apis-and-elementary-implementations
 */
public class UnorderedMaxPQ<Key extends Comparable<Key>> {

    private Key[] pq;   // items, in no particular order, in pq[0..n-1]
    private int n;      // number of items

    @SuppressWarnings("unchecked")
    public UnorderedMaxPQ(int capacity) {
        // generic arrays cannot be created directly in Java, so allocate
        // a Comparable[] and cast -- the standard workaround
        pq = (Key[]) new Comparable[Math.max(capacity, 1)];
        n = 0;
    }

    public UnorderedMaxPQ() {
        this(8);
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    /** Add an item -- O(1), because order does not matter. */
    public void insert(Key x) {
        if (n == pq.length) {
            resize(2 * pq.length);
        }
        pq[n++] = x;
    }

    /** The largest item, without removing it. */
    public Key max() {
        if (isEmpty()) {
            throw new IllegalStateException("priority queue is empty");
        }
        return pq[maxIndex()];
    }

    /**
     *  Remove and return the largest item.
     *
     *  Scan for the maximum (the O(N) part), swap it with the last
     *  item, then shrink -- so the hole is always at the end and no
     *  elements have to shift.
     */
    public Key delMax() {
        if (isEmpty()) {
            throw new IllegalStateException("priority queue is empty");
        }
        int max = maxIndex();
        exchange(max, n - 1);
        Key result = pq[--n];
        pq[n] = null;              // avoid holding on to a dead reference
        return result;
    }

    /** Index of the largest item -- a full scan. */
    private int maxIndex() {
        int max = 0;
        for (int i = 1; i < n; i++) {
            if (less(max, i)) {
                max = i;
            }
        }
        return max;
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
        System.arraycopy(pq, 0, copy, 0, n);
        pq = copy;
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(pq, n));
    }

    public static void main(String[] args) {
        UnorderedMaxPQ<Integer> pq = new UnorderedMaxPQ<>(4);
        assertThat(pq.isEmpty(), "a new queue is empty");

        pq.insert(12);
        pq.insert(1);
        pq.insert(14);
        pq.insert(7);
        assertThat(pq.size() == 4, "four items");
        assertThat(pq.toString().equals("[12, 1, 14, 7]"), "stored in insertion order -- UNSORTED");

        assertThat(pq.max() == 14, "max does not remove");
        assertThat(pq.size() == 4, "still four items");

        // draining yields descending order, one O(N) scan at a time
        assertThat(pq.delMax() == 14, "largest out first");
        assertThat(pq.delMax() == 12, "then 12");
        assertThat(pq.delMax() == 7, "then 7");
        assertThat(pq.delMax() == 1, "then 1");
        assertThat(pq.isEmpty(), "drained");

        try {
            pq.delMax();
            assertThat(false, "expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // ok
        }

        // it grows past its initial capacity
        UnorderedMaxPQ<String> words = new UnorderedMaxPQ<>(1);
        for (String word : new String[] {"pear", "apple", "zebra", "mango"}) {
            words.insert(word);
        }
        assertThat(words.size() == 4, "resized past the initial capacity");
        assertThat(words.delMax().equals("zebra"), "Comparable ordering, not just numbers");

        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
