package LeetCodeJava.Heap;

// https://leetcode.com/problems/distant-barcodes/description/

import java.util.*;

/**
 * 1054. Distant Barcodes
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * In a warehouse, there is a row of barcodes, where the ith barcode is barcodes[i].
 *
 * Rearrange the barcodes so that no two adjacent barcodes are equal. You may return any answer, and it is guaranteed an answer exists.
 *
 *
 *
 * Example 1:
 *
 * Input: barcodes = [1,1,1,2,2,2]
 * Output: [2,1,2,1,2,1]
 * Example 2:
 *
 * Input: barcodes = [1,1,1,1,2,2,3,3]
 * Output: [1,3,1,3,1,2,1,2]
 *
 *
 * Constraints:
 *
 * 1 <= barcodes.length <= 10000
 * 1 <= barcodes[i] <= 10000
 */
public class DistantBarcodes {

    // V0
//    public int[] rearrangeBarcodes(int[] barcodes) {
//
//    }

    // V0-1
    // IDEA: PQ (fixed by gpt)
    public int[] rearrangeBarcodes_0_1(int[] barcodes) {
        if (barcodes == null || barcodes.length <= 1) return barcodes;

        // freq map
        Map<Integer, Integer> freq = new HashMap<>();
        for (int x : barcodes) {
            freq.put(x, freq.getOrDefault(x, 0) + 1);
        }

        // max-heap by frequency
        PriorityQueue<Integer> pq = new PriorityQueue<>(
                (a, b) -> freq.get(b) - freq.get(a)
        );

        // add all distinct keys
        pq.addAll(freq.keySet());

        int[] res = new int[barcodes.length];
        int prev = -1;

        for (int i = 0; i < res.length; i++) {

            int first = pq.poll();

            // case 1: OK to use `first`
            if (prev != first) {

                res[i] = first;
                prev = first;

                // update freq
                freq.put(first, freq.get(first) - 1);
                if (freq.get(first) == 0) freq.remove(first);
                else pq.add(first);
            }

            // case 2: conflict → need second element
            else {
                int second = pq.poll();     // must exist because input is valid

                res[i] = second;
                prev = second;

                // update freq
                freq.put(second, freq.get(second) - 1);
                if (freq.get(second) == 0) freq.remove(second);
                else pq.add(second);

                // push back first
                pq.add(first);
            }
        }

        return res;
    }

    // V0-2
    // IDEA: PQ (fixed by gemini)
    public int[] rearrangeBarcodes_0_2(int[] barcodes) {
        if (barcodes == null || barcodes.length <= 1) {
            return barcodes;
        }

        int n = barcodes.length;

        // 1. Build Frequency Map: { value : count }
        Map<Integer, Integer> map = new HashMap<>();
        for (int x : barcodes) {
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        // 2. Build Max Priority Queue (Max Heap)
        // Stores the barcode values (keys) and sorts based on the frequency stored in the map (value).
        // Since we are sorting based on an external map, we must ensure all keys are added
        // after the map is finalized.
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> map.get(b) - map.get(a));

        // Add all unique barcode values (map keys) to the PQ
        pq.addAll(map.keySet());

        // 3. Greedy Rearrangement
        int[] result = new int[n];

        // We need a variable to temporarily hold the item used in the previous step.
        // This is crucial for the "never adjacent" rule.
        Integer prev = null;

        // We use a temporary queue/list to hold the item that was just used
        // but needs to be re-added later, to maintain the structure.
        Queue<Integer> temp = new LinkedList<>();

        int index = 0;
        while (!pq.isEmpty()) {
            // Get the most frequent item (first)
            Integer current = pq.poll();

            // Place the current item in the result array
            result[index++] = current;

            // Reduce its count in the map
            map.put(current, map.get(current) - 1);

            // --- Logic for Re-insertion ---

            // Check if there was an item used in the previous step (held in prev)
            if (prev != null) {
                // If its count is still > 0, re-add it to the Max Heap.
                // It must be re-added now so it can be considered for the step after the current one.
                if (map.get(prev) > 0) {
                    pq.add(prev);
                }
            }

            // The item we just used (current) now becomes the "previous" item for the next iteration.
            // It must *not* be added to the PQ in this iteration.
            prev = current;
        }

        return result;
    }

    // V1
    // IDEA: SORT
    // https://leetcode.ca/2018-10-19-1054-Distant-Barcodes/
    public int[] rearrangeBarcodes_1(int[] barcodes) {
        int n = barcodes.length;
        Integer[] t = new Integer[n];
        int mx = 0;
        for (int i = 0; i < n; ++i) {
            t[i] = barcodes[i];
            mx = Math.max(mx, barcodes[i]);
        }
        int[] cnt = new int[mx + 1];
        for (int x : barcodes) {
            ++cnt[x];
        }
        Arrays.sort(t, (a, b) -> cnt[a] == cnt[b] ? a - b : cnt[b] - cnt[a]);
        int[] ans = new int[n];
        for (int k = 0, j = 0; k < 2; ++k) {
            for (int i = k; i < n; i += 2) {
                ans[i] = t[j++];
            }
        }
        return ans;
    }

    // V2
    // IDEA: PQ + HASHMAP
    // https://leetcode.com/problems/distant-barcodes/solutions/7184883/fast-and-clear-with-iterations-in-tables-78o8/
    public int[] rearrangeBarcodes_2(int[] barcodes) {
        int n = barcodes.length; // How many items total? Let's remember that.

        // 🧮 Step 1: Count those barcodes! (Like counting how many of each candy you have)
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int barcode : barcodes) {
            // For each barcode, add 1 to its count. If it's new, start counting from 0.
            freqMap.put(barcode, freqMap.getOrDefault(barcode, 0) + 1);
        }

        // 🥇 Step 2: Create a MAX HEAP (a special queue that always gives the most frequent item first)
        // We store pairs: [count, barcode_number]. The comparator (a, b) -> b[0] - a[0] sorts by count descending.
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[0] - a[0]);

        // Put each barcode and its count into our max heap
        for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
            maxHeap.offer(new int[]{entry.getValue(), entry.getKey()}); // [count, barcode]
        }

        // 🏗️ Step 3: Build the result array (our new, nicely arranged conveyor belt)
        int[] res = new int[n];
        int index = 0; // Start at the beginning of the new conveyor belt
        int[] prev = null; // This will remember the last pile we took from (so we don't use it twice in a row)

        // Keep going until we've placed all items
        while (!maxHeap.isEmpty()) {
            // Grab the pile with the most items currently
            int[] curr = maxHeap.poll();

            // Take one item from this pile and put it on the conveyor belt
            res[index++] = curr[1];
            curr[0]--; // One less item in this pile now

            // If we used a pile previously and it still has items left...
            if (prev != null && prev[0] > 0) {
                maxHeap.offer(prev); // ...put it back in the heap so we can use it later
            }

            // Remember this current pile as the "previous" one for the next iteration
            prev = curr;
        }

        return res; // 🎉 Here's your perfectly arranged conveyor belt!
    }


    // V3
    // IDEA: PQ
    // https://leetcode.com/problems/distant-barcodes/solutions/6954453/34-ms-beats-5284-with-indian-tadka-cpp-j-4ih9/
    public int[] rearrangeBarcodes_3(int[] barcodes) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int code : barcodes) {
            freq.put(code, freq.getOrDefault(code, 0) + 1);
        }

        // Max-heap based on frequency
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (a, b) -> b[0] - a[0]
        );

        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            pq.add(new int[]{entry.getValue(), entry.getKey()});
        }

        int n = barcodes.length;
        int[] res = new int[n];
        int i = 0;

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int count = curr[0], val = curr[1];
            while (count-- > 0) {
                if (i >= n) i = 1;
                res[i] = val;
                i += 2;
            }
        }

        return res;
    }



}
