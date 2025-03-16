package AlgorithmJava;

// https://neetcode.io/courses/lessons/sorting-algorithms

import java.util.ArrayList;

/**
 *  Bucket sort is a sorting algorithm that distributes the elements
 *  of an array into several groups, called buckets.
 *  Each bucket is then sorted individually, either using a different sorting algorithm, or by recursively applying the bucket sorting algorithm.
 *
 *
 *   - Time complexity: O(N )
 *   - Space complexity: O(N + K)
 *
 *
 */
public class BucketSort {
    public static void bucketSort(float[] arr) {
        if (arr == null || arr.length <= 0)
            return;

        // Create buckets
        @SuppressWarnings("unchecked")
        ArrayList<Float>[] buckets = new ArrayList[arr.length];

        for (int i = 0; i < arr.length; i++)
            buckets[i] = new ArrayList<Float>();

        // Distribute input array values into buckets
        for (float item : arr) {
            int bucketIndex = (int) (item * arr.length);
            buckets[bucketIndex].add(item);
        }

        // Sort individual buckets
        for (ArrayList<Float> bucket : buckets) {
            Collections.sort(bucket);
        }

        // Concatenate all buckets into final array
        int index = 0;
        for (ArrayList<Float> bucket : buckets) {
            for (float item : bucket) {
                arr[index++] = item;
            }
        }
    }
}