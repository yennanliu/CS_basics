package LeetCodeJava.Heap;

// https://leetcode.com/problems/most-frequent-ids/description/
// https://leetcode.cn/problems/most-frequent-ids/

import DataStructure.Pair;

import java.util.*;

/**
 * 3092. Most Frequent IDs
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * The problem involves tracking the frequency of IDs in a collection that changes over time. You have two integer arrays, nums and freq, of equal length n. Each element in nums represents an ID, and the corresponding element in freq indicates how many times that ID should be added to or removed from the collection at each step.
 *
 * Addition of IDs: If freq[i] is positive, it means freq[i] IDs with the value nums[i] are added to the collection at step i.
 * Removal of IDs: If freq[i] is negative, it means -freq[i] IDs with the value nums[i] are removed from the collection at step i.
 * Return an array ans of length n, where ans[i] represents the count of the most frequent ID in the collection after the ith step. If the collection is empty at any step, ans[i] should be 0 for that step.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,2,1], freq = [3,2,-3,1]
 *
 * Output: [3,3,2,2]
 *
 * Explanation:
 *
 * After step 0, we have 3 IDs with the value of 2. So ans[0] = 3.
 * After step 1, we have 3 IDs with the value of 2 and 2 IDs with the value of 3. So ans[1] = 3.
 * After step 2, we have 2 IDs with the value of 3. So ans[2] = 2.
 * After step 3, we have 2 IDs with the value of 3 and 1 ID with the value of 1. So ans[3] = 2.
 *
 * Example 2:
 *
 * Input: nums = [5,5,3], freq = [2,-2,1]
 *
 * Output: [2,0,1]
 *
 * Explanation:
 *
 * After step 0, we have 2 IDs with the value of 5. So ans[0] = 2.
 * After step 1, there are no IDs. So ans[1] = 0.
 * After step 2, we have 1 ID with the value of 3. So ans[2] = 1.
 *
 *
 *
 * Constraints:
 *
 * 1 <= nums.length == freq.length <= 105
 * 1 <= nums[i] <= 105
 * -105 <= freq[i] <= 105
 * freq[i] != 0
 * The input is generated such that the occurrences of an ID will not be negative in any step.
 *
 */
public class MostFrequentIDs {

    // V0
//    public long[] mostFrequentIDs(int[] nums, int[] freq) {
//
//    }

    // V0-0-0-1
    // IDEA: PQ track freq from hashmap + hashmap (WRONG !!!)
    // TODO: double check and validate & compare other LC use same tricks
    /**  NOTE !!! below is WRONG
     *
     * ---
     *
     * Q: CAN't we use below approach?
     *
     * - using same map definition, but make PQ track the most freq val in hashmap ?
     *
     *
     * ---
     *
     *
     *
     * // map: { val : cnt }
     * Map<Integer, Integer> map = new HashMap<>();
     *
     * // max PQ ??
     * // [ cnt1, cnt2, .... ]
     * PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
     *     @Override
     *     public int compare(Integer o1, Integer o2) {
     *         //int diff = o2 - o1;
     *         int diff = map.get(o2) - map.get(o1); // ????
     *         return diff;
     *     }
     * });
     *
     *  Reason:
     *
     *  The short answer is:
     *
     *   -> NO !!!!, you cannot use that approach in Java.**
     *   While it looks logically sound on paper, it violates a
     *   fundamental rule of how the `PriorityQueue`
     *   (and most collection-based heaps) works in Java.
     *
     * ### 1. The "Broken Heap" Problem
     *
     * A `PriorityQueue` only sorts elements **once**,
     * at the moment they are inserted (`add` or `offer`).
     * It does **not** re-sort itself or re-adjust its internal
     * tree structure if the values inside your `Map` change later.
     *
     * **Here is what happens in memory:**
     *
     * 1. You add `ID: 1` (frequency 10) to the PQ. The PQ places it at the top because it's the largest.
     * 2. You update the Map so `ID: 1` now has a frequency of **2**.
     * 3. The PQ **does not know** the value changed. `ID: 1` stays at the top of the heap.
     * 4. When you call `pq.peek()`, it still gives you `ID: 1` as the "maximum," even though there might be another ID with a frequency of 5 that should now be the winner.
     *
     * ---
     *
     * ### 2. Why it leads to wrong answers
     *
     * To make a `PriorityQueue` recognize a change, you would have to:
     *
     * * `pq.remove(id);` ( - very slow)
     * * `pq.add(id);` ()
     *
     * Doing this inside your loop would turn your  algorithm into an  algorithm, which will **Time Limit Exceeded (TLE)** on LeetCode for .
     *
     * ---
     *
     * ### 3. The Solution: Lazy Update
     *
     * The "Lazy Update" pattern I showed previously is the standard way to fix this. Instead of trying to update the existing ID in the PQ, you **leave the old one there** and just add a **new** entry with the updated frequency.
     *
     * When you look at the top of the PQ:
     *
     * 1. You check: *"Is the frequency of this ID at the top of the heap the same as the current frequency in my Map?"*
     * 2. If **yes**: This is the true current maximum.
     * 3. If **no**: This is an "old version" (a ghost). Delete it (`poll`) and check the next one.
     *
     * ---
     *
     * ### 4. Comparison of Approaches
     *
     * | Approach | Logic | Complexity | Result |
     * | --- | --- | --- | --- |
     * | **Your Approach** | Comparator linked to Map |  | **Wrong Answer** (Heap becomes unsorted) |
     * | **Remove/Add** | Remove old ID, add new |  | **TLE** (Too slow) |
     * | **Lazy Removal** | Add new `{freq, id}` pair |  | **Accepted** (Fast & Correct) |
     *
     *
     */
    // NOTE !!! below is WRONG !!!!
//    public long[] mostFrequentIDs(int[] nums, int[] freq) {
//        int n = nums.length;
//
//        // map: { val : cnt }
//        Map<Integer, Integer> map = new HashMap<>();
//
//        // max PQ ??
//        // [ cnt1, cnt2, .... ]
//        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                //int diff = o2 - o1;
//                int diff = map.get(o2) - map.get(o1); // ????
//                return diff;
//            }
//        });
//        // ...
//    }


    // V0-0-1
    // IDEA: PQ (fixed by gemini)
    /**
     * Logic:
     * 1. Use a HashMap to store the actual current total frequency of each ID.
     * 2. Use a Max-Heap (PriorityQueue) to store pairs of {frequency, id}.
     * 3. When a frequency changes, push the NEW state into the PQ.
     * 4. Before getting the result, "Lazy Remove" stale entries from the top of the PQ.
     */
    public long[] mostFrequentIDs_0_0_1(int[] nums, int[] freq) {
        int n = nums.length;
        long[] res = new long[n];

        // Map: ID -> Current total frequency (must be Long)
        Map<Integer, Long> idToFreq = new HashMap<>();

        // PriorityQueue: {frequency, ID}
        // We use a custom comparator to sort by frequency descending.
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(b[0], a[0]));

        for (int i = 0; i < n; i++) {
            int id = nums[i];
            long delta = freq[i];

            // 1. Update the actual frequency in the Map
            long currentTotal = idToFreq.getOrDefault(id, 0L) + delta;
            idToFreq.put(id, currentTotal);

            // 2. Add the new state to the PQ (we don't remove the old one)
            pq.offer(new long[] { currentTotal, (long) id });

            // 3. Lazy Removal: If the top of the heap is outdated, throw it away.
            // An entry is stale if its frequency != current frequency in our Map.
            while (!pq.isEmpty() && pq.peek()[0] != idToFreq.get((int) pq.peek()[1])) {
                pq.poll();
            }

            // 4. The top of the heap is now guaranteed to be the current maximum.
            res[i] = pq.isEmpty() ? 0 : pq.peek()[0];
        }

        return res;
    }

    // V0-0-2
    // IDEA: PQ (fixed by gpt)
    public long[] mostFrequentIDs_0_0_2(int[] nums, int[] freq) {
        int n = nums.length;
        long[] res = new long[n];

        Map<Integer, Long> idToFreq = new HashMap<>();

        // max-heap by freq
        PriorityQueue<long[]> pq = new PriorityQueue<>(
                (a, b) -> Long.compare(b[0], a[0]) // b.freq - a.freq
        );
        // entry: [frequency, id]

        for (int i = 0; i < n; i++) {
            int id = nums[i];
            long delta = freq[i];

            long newFreq = idToFreq.getOrDefault(id, 0L) + delta;

            if (newFreq <= 0) {
                idToFreq.remove(id);
                // we don't push (0,id); just remove from map
            } else {
                idToFreq.put(id, newFreq);
                pq.offer(new long[] { newFreq, id }); // snapshot
            }

            // lazy cleanup: discard stale heap tops
            while (!pq.isEmpty()) {
                long topFreq = pq.peek()[0];
                int topId = (int) pq.peek()[1];
                long curFreq = idToFreq.getOrDefault(topId, 0L);

                if (curFreq == topFreq)
                    break; // valid top
                pq.poll(); // stale
            }

            res[i] = pq.isEmpty() ? 0L : pq.peek()[0];
        }

        return res;
    }



    // V0-1
    // IDEA: PQ (fixed by gemini)
    /**
     * Logic:
     * 1. Use a HashMap to store the actual current frequency of each ID.
     * 2. Use a PriorityQueue (Max-Heap) to store {frequency, ID}.
     * 3. For each update, update the Map and push the new frequency into the Heap.
     * 4. "Lazy Removal": Before taking the max from the Heap, check if the frequency
     * at the top matches the Map. If not, poll it and move to the next.
     * * Time Complexity: O(N log N)
     * Space Complexity: O(N)
     */
    public long[] mostFrequentIDs_0_1(int[] nums, int[] freq) {
        int n = nums.length;
        long[] res = new long[n];

        // Maps ID -> Current total frequency
        Map<Integer, Long> idToFreq = new HashMap<>();

        // Max-Heap stores: long[]{frequency, id}
        // Sorted descending by frequency (long[0])
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(b[0], a[0]));

        for (int i = 0; i < n; i++) {
            int id = nums[i];
            int fChange = freq[i];

            // 1. Update the true frequency in the Map
            long newFreq = idToFreq.getOrDefault(id, 0L) + fChange;
            idToFreq.put(id, newFreq);

            // 2. Push the updated state into the PriorityQueue
            pq.offer(new long[] { newFreq, (long) id });

            // 3. Lazy Removal: Clear the top of the heap until it's valid
            // Valid means the frequency in PQ matches the Map
            while (!pq.isEmpty() && pq.peek()[0] != idToFreq.get((int) pq.peek()[1])) {
                pq.poll();
            }

            // 4. The top of the heap is now the guaranteed maximum
            res[i] = pq.isEmpty() ? 0 : pq.peek()[0];
        }

        return res;
    }


    // V0-2
    // IDEA: HASHMAP + DOUBLE LOOP (TLE) (GPT)
    public long[] mostFrequentIDs_0_2(int[] nums, int[] freq) {
        long[] res = new long[nums.length];
        Map<Integer, Long> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            /** NOTE !!! below logic:
             *
             *  -> we calculate the updated cnt first,
             *     if the cnt <= 0,
             *     remove the key from map directly.
             *     else, put the updated val back to map
             */
            long newCnt = map.getOrDefault(nums[i], 0L) + freq[i];

            if (newCnt <= 0) {
                map.remove(nums[i]);
            } else {
                map.put(nums[i], newCnt);
            }

            res[i] = getMaxCnt(map);
        }

        return res;
    }

    private long getMaxCnt(Map<Integer, Long> map) {
        long max = 0;
        for (long v : map.values()) {
            max = Math.max(max, v);
        }
        return max;
    }

    // V0-3
    // IDEA: TREEMAP (gpt)
    public long[] mostFrequentIDs_0_3(int[] nums, int[] freq) {
        int n = nums.length;
        long[] res = new long[n];

        Map<Integer, Long> idToFreq = new HashMap<>();
        TreeMap<Long, Integer> freqCount = new TreeMap<>();

        for (int i = 0; i < n; i++) {
            int id = nums[i];
            long delta = freq[i];

            long oldFreq = idToFreq.getOrDefault(id, 0L);

            // remove old frequency
            if (oldFreq > 0) {
                freqCount.put(oldFreq, freqCount.get(oldFreq) - 1);
                if (freqCount.get(oldFreq) == 0) {
                    freqCount.remove(oldFreq);
                }
            }

            long newFreq = oldFreq + delta;

            if (newFreq > 0) {
                idToFreq.put(id, newFreq);
                freqCount.put(newFreq, freqCount.getOrDefault(newFreq, 0) + 1);
            } else {
                idToFreq.remove(id);
            }

            res[i] = freqCount.isEmpty() ? 0 : freqCount.lastKey();
        }

        return res;
    }


    // V1-1
    // IDEA: TREEMAP, HASHMAP, without max heap
    // https://leetcode.com/problems/most-frequent-ids/solutions/4916730/c-java-explained-using-heap-and-without-10on0/
    public long[] mostFrequentIDs_1_1(int[] nums, int[] freq) {
        long[] ans = new long[nums.length];
        HashMap<Integer, Long> idToFreqMap = new HashMap<>();
        TreeMap<Long, Integer> freqToIdCountMap = new TreeMap<>();
        for (int i = 0; i < nums.length; ++i) {
            if (idToFreqMap.getOrDefault(nums[i], 0L) != 0) {
                freqToIdCountMap.put(idToFreqMap.get(nums[i]), freqToIdCountMap.get(idToFreqMap.get(nums[i])) - 1);
                if (freqToIdCountMap.get(idToFreqMap.get(nums[i])) == 0)
                    freqToIdCountMap.remove(idToFreqMap.get(nums[i]));
            }
            idToFreqMap.put(nums[i], idToFreqMap.getOrDefault(nums[i], 0L) + freq[i]);
            freqToIdCountMap.put(idToFreqMap.get(nums[i]),
                    freqToIdCountMap.getOrDefault(idToFreqMap.get(nums[i]), 0) + 1);
            ans[i] = freqToIdCountMap.lastKey();
        }
        return ans;
    }

    // V1-2
    // IDEA: Using maxHeap
    // https://leetcode.com/problems/most-frequent-ids/solutions/4916730/c-java-explained-using-heap-and-without-10on0/
    public long[] mostFrequentIDs_1_2(int[] nums, int[] freq) {
        long[] ans = new long[nums.length];
        HashMap<Integer, Long> idToFreqMap = new HashMap<>();
        PriorityQueue<Pair<Long, Integer>> maxHeap = new PriorityQueue<>(
                (a, b) -> Long.compare(b.getKey(), a.getKey()));
        for (int i = 0; i < nums.length; ++i) {
            idToFreqMap.put(nums[i], idToFreqMap.getOrDefault(nums[i], 0L) + freq[i]);
            maxHeap.add(new Pair<>(idToFreqMap.get(nums[i]), nums[i]));
            while (idToFreqMap.get(maxHeap.peek().getValue()) != maxHeap.peek().getKey())
                maxHeap.poll();
            ans[i] = maxHeap.peek().getKey();
        }
        return ans;
    }


    // V2
    // IDEA: TREEMAP, HASHMAP
    // https://leetcode.com/problems/most-frequent-ids/solutions/4917471/clean-code-java-on-logn-by-navid-ox34/
    HashMap<Integer, Long> idToFreq = new HashMap<>();
    TreeMap<Long, Set<Integer>> freqToIds = new TreeMap<>();

    public long[] mostFrequentIDs_2(int[] nums, int[] freq) {
        int size = nums.length;
        long[] result = new long[size];
        for (int i = 0; i < size; i++) {
            int id = nums[i];
            long addedFreq = freq[i];
            long oldFreq = idToFreq.getOrDefault(id, 0L);
            long newFreq = addedFreq + oldFreq;
            updateIdToFreq(id, newFreq);
            updateFreqToIds(id, oldFreq, newFreq);
            result[i] = freqToIds.lastKey();
        }
        return result;
    }

    private void updateIdToFreq(int id, long newFreq) {
        idToFreq.put(id, newFreq);
    }

    private void updateFreqToIds(int id, long oldFreq, long newFreq) {
        if (oldFreq != 0) {
            freqToIds.get(oldFreq).remove(id);
            if (freqToIds.get(oldFreq).size() == 0)
                freqToIds.remove(oldFreq);
        }
        if (!freqToIds.containsKey(newFreq))
            freqToIds.put(newFreq, new HashSet<>());
        freqToIds.get(newFreq).add(id);
    }


    // V3
    // IDEA: PQ
    // https://leetcode.com/problems/most-frequent-ids/solutions/4917475/codestorywithmik-intuition-heap-map-c-ja-s2y7/
    public long[] mostFrequentIDs_3(int[] nums, int[] freq) {
        int n = nums.length;

        Map<Long, Long> mp = new HashMap<>(); //{ID -> freq}
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(b[0], a[0])); //{freq, ID}

        long[] result = new long[nums.length];

        for (int i = 0; i < n; i++) {
            long ID = nums[i];
            long f = freq[i];

            mp.put(ID, mp.getOrDefault(ID, 0L) + f);

            pq.offer(new long[] { mp.get(ID), ID });

            while (!pq.isEmpty() && mp.get(pq.peek()[1]) != pq.peek()[0]) {
                pq.poll();
            }

            result[i] = pq.peek()[0];
        }
        return result;
    }



}
