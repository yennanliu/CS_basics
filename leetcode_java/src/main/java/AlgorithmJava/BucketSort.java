package AlgorithmJava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  BUCKET SORT
 *
 *  A DISTRIBUTION sort, not a comparison sort. Scatter the values into
 *  ordered buckets, sort each bucket, then read the buckets back in
 *  order.
 *
 *      input   0.42  0.32  0.75  0.12  0.78
 *
 *      bucket 0 [0.0, 0.2)   0.12
 *      bucket 1 [0.2, 0.4)   0.32
 *      bucket 2 [0.4, 0.6)   0.42
 *      bucket 3 [0.6, 0.8)   0.75  0.78     <- sorted individually
 *      bucket 4 [0.8, 1.0)
 *
 *      output  0.12  0.32  0.42  0.75  0.78
 *
 *  The buckets are ordered by construction, so the concatenation needs
 *  no merge step -- that is where the speed comes from.
 *
 *  THE PRECONDITIONS, both easy to miss:
 *    1) INPUT RANGE. `(int)(value * n)` only lands in 0..n-1 when every
 *       value is in [0, 1). Anything outside that range indexes out of
 *       bounds, so it is rejected here rather than crashing later.
 *    2) UNIFORM DISTRIBUTION. The O(N) claim assumes values spread
 *       evenly, giving O(1) items per bucket. Clustered input dumps
 *       everything into one bucket and degrades to whatever sorts the
 *       buckets -- O(N log N) here, O(N^2) with insertion sort.
 *
 *  Time  : Best / Average O(N + K), Worst O(N log N)
 *          (K = bucket count; worst = every value in one bucket)
 *  Space : O(N + K)
 *
 *  Reference: https://neetcode.io/courses/lessons/sorting-algorithms
 */
public class BucketSort {

    /**
     *  Sort arr ascending, in place.
     *
     *  @throws IllegalArgumentException if any value falls outside [0, 1)
     */
    public static void bucketSort(float[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }

        int n = arr.length;

        // 1) one bucket per element, so an even spread gives ~1 each
        List<List<Float>> buckets = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            buckets.add(new ArrayList<>());
        }

        // 2) scatter: bucket index is derived from the VALUE, not a comparison
        for (float value : arr) {
            if (value < 0.0f || value >= 1.0f) {
                throw new IllegalArgumentException(
                        "bucket sort here requires values in [0, 1), got " + value);
            }
            buckets.get((int) (value * n)).add(value);
        }

        // 3) sort each bucket on its own
        for (List<Float> bucket : buckets) {
            Collections.sort(bucket);
        }

        // 4) gather: buckets are already in order, so just concatenate
        int index = 0;
        for (List<Float> bucket : buckets) {
            for (float value : bucket) {
                arr[index++] = value;
            }
        }
    }

    public static void main(String[] args) {
        float[] mixed = {0.42f, 0.32f, 0.75f, 0.12f, 0.78f, 0.33f};
        bucketSort(mixed);
        assertThat(isSorted(mixed), "sorts a mixed array");
        assertThat(mixed[0] == 0.12f && mixed[5] == 0.78f, "smallest first, largest last");

        float[] empty = {};
        bucketSort(empty);
        assertThat(empty.length == 0, "empty array");

        float[] single = {0.5f};
        bucketSort(single);
        assertThat(single[0] == 0.5f, "single element");

        float[] sorted = {0.1f, 0.2f, 0.3f};
        bucketSort(sorted);
        assertThat(isSorted(sorted), "already sorted");

        float[] reversed = {0.9f, 0.5f, 0.1f};
        bucketSort(reversed);
        assertThat(isSorted(reversed), "reversed");

        float[] duplicates = {0.5f, 0.1f, 0.5f, 0.1f};
        bucketSort(duplicates);
        assertThat(Arrays.toString(duplicates).equals("[0.1, 0.1, 0.5, 0.5]"), "duplicates");

        // every value in one bucket: still correct, just no longer O(N)
        float[] clustered = {0.99f, 0.98f, 0.97f, 0.96f};
        bucketSort(clustered);
        assertThat(isSorted(clustered), "clustered input is still sorted correctly");

        // out-of-range input is rejected instead of indexing out of bounds
        try {
            bucketSort(new float[] {1.5f});
            assertThat(false, "expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // ok
        }
        try {
            bucketSort(new float[] {-0.5f});
            assertThat(false, "expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // ok
        }

        bucketSort(null);   // must not throw

        System.out.println(Arrays.toString(mixed));
        System.out.println("Success.");
    }

    private static boolean isSorted(float[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }
        return true;
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
