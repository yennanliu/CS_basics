package LeetCodeJava.Heap;

// https://leetcode.com/problems/most-frequent-ids/description/

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
