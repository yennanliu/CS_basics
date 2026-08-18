import java.util.Arrays;

/**
 *  MERGE SORT -- top-down, with an auxiliary array
 *
 *  Divide and conquer:
 *    1) SPLIT the range in half
 *    2) sort each half recursively
 *    3) MERGE the two sorted halves back into place
 *
 *          [99, 44, 6, 2, 1]
 *         /                 \
 *    [99, 44]            [6, 2, 1]
 *      ...                  ...
 *         \                 /
 *        [1, 2, 6, 44, 99]
 *
 *  The merge is the whole algorithm. Two sorted runs combine in linear
 *  time by repeatedly taking the smaller of the two front values. There
 *  are log N levels and each level merges N items, hence O(N log N) --
 *  and unlike quicksort that bound holds in the WORST case too.
 *
 *  THE AUX ARRAY: allocate it ONCE in sort() and pass it down. Creating
 *  a new one inside merge() is the classic mistake -- it turns an O(N)
 *  allocation into O(N log N) of them and dominates the runtime.
 *
 *  Merge sort is STABLE, which is why it is the standard choice for
 *  sorting objects by a key. The price is the O(N) scratch space, which
 *  is also why quicksort usually wins on raw arrays.
 *
 *  Time  : O(N log N) in all cases
 *  Space : O(N) for the auxiliary array
 *
 *  Reference: https://www.coursera.org/learn/algorithms-part1/lecture/ARWDq/mergesort
 */
public class MergeSort {

    /** Sort a[] ascending, in place. */
    public static void sort(Comparable[] a) {
        if (a == null || a.length < 2) {
            return;
        }
        Comparable[] aux = new Comparable[a.length];   // allocated ONCE
        sort(a, aux, 0, a.length - 1);
    }

    /** Sort a[lo..hi]. */
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        if (lo >= hi) {
            return;                                     // 0 or 1 item is sorted
        }
        int mid = lo + (hi - lo) / 2;                   // not (lo + hi) / 2, which can overflow
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    /**
     *  Merge the sorted runs a[lo..mid] and a[mid+1..hi] back into
     *  a[lo..hi], using aux[] as scratch.
     */
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        assert isSorted(a, lo, mid);
        assert isSorted(a, mid + 1, hi);

        // copy the range out, then refill it in order
        System.arraycopy(a, lo, aux, lo, hi - lo + 1);

        int i = lo;          // cursor into the left run
        int j = mid + 1;     // cursor into the right run

        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];                    // left run exhausted
            } else if (j > hi) {
                a[k] = aux[i++];                    // right run exhausted
            } else if (less(aux[j], aux[i])) {
                a[k] = aux[j++];                    // right is strictly smaller
            } else {
                a[k] = aux[i++];                    // TIE goes LEFT -> stable
            }
        }

        assert isSorted(a, lo, hi);
    }

    @SuppressWarnings("unchecked")
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            if (less(a[i], a[i - 1])) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    public static void main(String[] args) {
        Integer[] mixed = {99, 44, 6, 2, 1, 5, 63, 87, 283, 4, 0};
        sort(mixed);
        assertThat(Arrays.toString(mixed).equals("[0, 1, 2, 4, 5, 6, 44, 63, 87, 99, 283]"),
                "sorts a mixed array");

        Integer[] empty = {};
        sort(empty);
        assertThat(empty.length == 0, "empty array");

        Integer[] single = {1};
        sort(single);
        assertThat(Arrays.toString(single).equals("[1]"), "single element");

        Integer[] sorted = {1, 2, 3};
        sort(sorted);
        assertThat(Arrays.toString(sorted).equals("[1, 2, 3]"), "already sorted");

        Integer[] reversed = {3, 2, 1};
        sort(reversed);
        assertThat(Arrays.toString(reversed).equals("[1, 2, 3]"), "reversed");

        Integer[] duplicates = {2, 1, 2, 1};
        sort(duplicates);
        assertThat(Arrays.toString(duplicates).equals("[1, 1, 2, 2]"), "duplicates");

        Integer[] negatives = {0, -3, 5, -1};
        sort(negatives);
        assertThat(Arrays.toString(negatives).equals("[-3, -1, 0, 5]"), "negatives");

        String[] words = {"pear", "apple", "zebra", "mango"};
        sort(words);
        assertThat(Arrays.toString(words).equals("[apple, mango, pear, zebra]"), "any Comparable");

        sort((Comparable[]) null);   // must not throw

        // stability: equal keys keep their input order
        Pair[] pairs = {new Pair(2, "a"), new Pair(1, "b"), new Pair(2, "c"), new Pair(1, "d")};
        sort(pairs);
        StringBuilder tags = new StringBuilder();
        for (Pair pair : pairs) {
            tags.append(pair.tag);
        }
        assertThat(tags.toString().equals("bdac"), "stable: b before d, a before c");

        System.out.println(Arrays.toString(mixed));
        System.out.println("Success.");
    }

    /** Sorts by key only, so equal keys let the demo observe stability. */
    private static class Pair implements Comparable<Pair> {
        final int key;
        final String tag;

        Pair(int key, String tag) {
            this.key = key;
            this.tag = tag;
        }

        @Override
        public int compareTo(Pair other) {
            return Integer.compare(key, other.key);
        }
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
