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
