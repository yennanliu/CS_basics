package LeetCodeJava.Heap;

// https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/description

import java.util.*;

/**
 *  1481. Least Number of Unique Integers after K Removals
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an array of integers arr and an integer k. Find the least number of unique integers after removing exactly k elements.
 *
 *
 *
 * Example 1:
 *
 * Input: arr = [5,5,4], k = 1
 * Output: 1
 * Explanation: Remove the single 4, only 5 is left.
 * Example 2:
 * Input: arr = [4,3,1,1,3,3,2], k = 3
 * Output: 2
 * Explanation: Remove 4, 2 and either one of the two 1s or three 3s. 1 and 3 will be left.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 10^5
 * 1 <= arr[i] <= 10^9
 * 0 <= k <= arr.length
 *
 */
public class LeastNumberOfUniqueIntegersAfterKRemovals {

    // V0
//    public int findLeastNumOfUniqueInts(int[] arr, int k) {
//
//    }

    // V0-1
    // IDEA: HASHMAP + PQ (fixed by gpt)
    public int findLeastNumOfUniqueInts_0_1(int[] arr, int k) {
        // edge
        if (arr == null || arr.length == 0) {
            return 0;
        }

        // Count frequencies
        Map<Integer, Integer> freq = new HashMap<>();
        for (int x : arr) {
            freq.put(x, freq.getOrDefault(x, 0) + 1);
        }

        // Min-heap by frequency
        PriorityQueue<Integer> pq = new PriorityQueue<>(
                (a, b) -> freq.get(a) - freq.get(b));

        // Add all unique keys
        pq.addAll(freq.keySet());

        // Consume removals
        while (k > 0 && !pq.isEmpty()) {
            int key = pq.poll();
            int cnt = freq.get(key);

            if (k >= cnt) {
                // Remove this number completely
                k -= cnt;
                freq.remove(key);
            } else {
                // Not enough k to erase this entire number
                break;
            }
        }

        return freq.size();
    }

    // V0-2
    // IDEA: MAP + PQ (fixed by gemini)
    public int findLeastNumOfUniqueInts_0_2(int[] arr, int k) {
        // Edge case: empty array
        if (arr == null || arr.length == 0) {
            return 0;
        }

        // 1. Calculate Frequencies: { value: count }
        Map<Integer, Integer> map = new HashMap<>();
        for (int x : arr) {
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        // If k is 0, the number of unique integers is simply the number of map entries.
        if (k == 0) {
            return map.keySet().size();
        }

        // 2. Build Min Heap: Stores the frequencies (counts) directly.
        // We only care about the frequencies, not the original integer values.
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        // Add all frequencies to the Min Heap.
        minHeap.addAll(map.values());

        // 3. Greedy Removal
        // Iterate while we have removals left and the heap is not empty.
        while (k > 0 && !minHeap.isEmpty()) {

            // Get the smallest frequency (the type to remove first).
            int currentFreq = minHeap.poll();

            // If we have enough removals (k) to remove ALL instances of this type:
            if (k >= currentFreq) {
                k -= currentFreq;
                // Since we successfully removed all instances, the number of unique integers
                // in the map is effectively reduced by 1, but we don't need to manually
                // update the map or its size variable. The answer is calculated at the end.
            } else {
                // If k < currentFreq, we cannot remove ALL instances of this type.
                // We stop here because all remaining types require more removals than we have.
                // We also need to re-add the current frequency back since it wasn't fully removed.
                minHeap.add(currentFreq);
                break;
            }
        }

        // 4. Final Answer
        // The number of remaining unique integers is simply the number of frequencies left in the heap.
        return minHeap.size();
    }

    // V1-1
    // IDEA: Sorting the Frequencies
    // https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/
    public int findLeastNumOfUniqueInts_1_1(int[] arr, int k) {
        // Map to track the frequencies of elements
        Map<Integer, Integer> map = new HashMap<>();
        for (int i : arr) {
            map.put(i, map.getOrDefault(i, 0) + 1);
        }

        // List to track all the frequencies
        List<Integer> frequencies = new ArrayList<>(map.values());

        // Sorting the frequencies
        Collections.sort(frequencies);

        // Tracking the number of elements removed
        int elementsRemoved = 0;

        for (int i = 0; i < frequencies.size(); i++) {
            // Removing frequencies[i] elements, which equates to
            // removing one unique element
            elementsRemoved += frequencies.get(i);

            // If the number of elements removed exceeds k, return
            // the remaining number of unique elements
            if (elementsRemoved > k) {
                return frequencies.size() - i;
            }
        }

        // We have removed all elements, so no unique integers remain
        // Return 0 in this case
        return 0;
    }


    // V1-2
    // IDEA: MIN HEAP
    // https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/
    public int findLeastNumOfUniqueInts_1_2(int[] arr, int k) {
        // Map to track the frequencies of elements
        Map<Integer, Integer> map = new HashMap<>();
        for (int i : arr) {
            map.put(i, map.getOrDefault(i, 0) + 1);
        }

        // Min heap to track all the frequencies
        PriorityQueue<Integer> frequencies = new PriorityQueue<>(map.values());

        // Tracking the number of elements removed
        int elementsRemoved = 0;

        // Traversing all frequencies
        while (!frequencies.isEmpty()) {
            // Removing the least frequent element
            elementsRemoved += frequencies.peek();

            // If the number of elements removed exceeds k, return
            // the remaining number of unique elements
            if (elementsRemoved > k) {
                return frequencies.size();
            }
            frequencies.poll();
        }

        // We have removed all elements, so no unique integers remain
        // Return 0 in this case
        return 0;
    }


    // V1-3
    // IDEA: Counting Sort
    // https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/
    public int findLeastNumOfUniqueInts_1_3(int[] arr, int k) {
        // Map to track the frequencies of elements
        Map<Integer, Integer> map = new HashMap<>();
        for (int i : arr) {
            map.put(i, map.getOrDefault(i, 0) + 1);
        }

        int n = arr.length;

        // Array to track the frequencies of frequencies
        // The maximum possible frequency of any element
        // will be n, so we'll initialize this array with size n + 1
        int[] countOfFrequencies = new int[n + 1];

        // Populating countOfFrequencies array
        for (int count : map.values()) {
            countOfFrequencies[count]++;
        }

        // Variable to track the remaining number of unique elements
        int remainingUniqueElements = map.size();

        // Traversing over all possible frequencies
        for (int i = 1; i <= n; i++) {
            // For each possible frequency i, we'd like to remove as
            // many elements with that frequency as possible.
            // k / i represents the number of maximum possible elements
            // we could remove with k elements left to remove.
            // countOfFrequencies[i] represents the actual number of elements
            // with frequency i.
            int numElementsToRemove = Math.min(k / i, countOfFrequencies[i]);

            // Removing maximum possible elements
            k -= (i * numElementsToRemove);

            // numElementsToRemove is the count of unique elements removed
            remainingUniqueElements -= numElementsToRemove;

            // If the number of elements that can be removed is less
            // than the current frequency, we won't be able to remove
            // any more elements with a higher frequency so we can return
            // the remaining number of unique elements
            if (k < i) {
                return remainingUniqueElements;
            }
        }

        // We have traversed all possible frequencies i.e.,
        // removed all elements. Returning 0 in this case.
        return 0;
    }


    // V2
    // https://leetcode.ca/2019-12-20-1481-Least-Number-of-Unique-Integers-after-K-Removals/
    public int findLeastNumOfUniqueInts_2(int[] arr, int k) {
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int x : arr) {
            cnt.merge(x, 1, Integer::sum);
        }
        List<Integer> nums = new ArrayList<>(cnt.values());
        Collections.sort(nums);
        for (int i = 0, m = nums.size(); i < m; ++i) {
            k -= nums.get(i);
            if (k < 0) {
                return m - i;
            }
        }
        return 0;
    }


}
