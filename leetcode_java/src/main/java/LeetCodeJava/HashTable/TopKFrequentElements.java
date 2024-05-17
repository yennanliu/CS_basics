package LeetCodeJava.HashTable;

// https://leetcode.com/problems/top-k-frequent-elements/

import java.util.*;
import java.util.stream.Collectors;

public class TopKFrequentElements {

    // V0
    // IDEA : HASHMAP + ARRAY ORDERING
    public int[] topKFrequent(int[] nums, int k) {

        if (nums.equals(null) || nums.length == 0){
            return null;
        }

        HashMap<Integer, Integer> map = new HashMap<>();
        for (int x : nums){
            if (!map.containsKey(x)){
                map.put(x, 1);
            }else{
                int cur = map.get(x);
                map.put(x, cur+1);
            }
        }

        int _size = map.keySet().size();
        // tmp array save (k, count) from map
        // for ordering op below
        int[][] tmp = new int[_size][2];
        int z = 0;
        for (int j : map.keySet()){
            tmp[z][0] = j;
            tmp[z][1] = map.get(j);
            z += 1;
        }

        // order array
        /** NOTE !!! sort syntax here
         *
         * -> Integer.compare(-x[1], -y[1]) for reverse ordering (descending)
         */
        Arrays.sort(tmp, (x, y) -> Integer.compare(-x[1], -y[1]));
        // System.out.println(">>> ");
        // Arrays.stream(tmp).forEach(x -> System.out.println(x[0]));
        // System.out.println(">>> ");

        int[] res = new int[k];
        for (int i = 0 ; i < k; i++){
            res[i] = tmp[i][0];
        }

        return res;
    }

    // V0'
    // IDEA : PQ (priority queue)
    public int[] topKFrequent_0(int[] nums, int k) {

        // O(1) time
        if (k == nums.length) {
            return nums;
        }

        // Step 1. build hash map : character and how often it appears
        // O(N) time
        Map<Integer, Integer> count = new HashMap();
        for (int n: nums) {
            count.put(n, count.getOrDefault(n, 0) + 1);
        }

        /** NOTE !!! how to init PQ below
         *
         *
         *  Priority Queue with Custom Comparator based on a Map (count):
         *
         *   1) This priority queue uses a custom comparator that compares the elements
         *   based on their values in a map (count).
         *
         *   2) count.get(n1) retrieves the frequency of n1 from the map,
         *   and count.get(n2) retrieves the frequency of n2.
         *
         *  3)  The comparator orders the elements by their frequencies in ascending order.
         *  Elements with lower frequencies will
         *  have higher priority (i.e., they will be at the front of the queue).
         *
         *
         *  NOTE !!!
         *
         *   Queue<Integer> heap = new PriorityQueue<>((n1, n2) -> count.get(n1) - count.get(n2));
         *
         *   and
         *
         *   PriorityQueue<Integer> bigPQ = new PriorityQueue<>(Comparator.reverseOrder());
         *
         *   are DIFFERENT
         *
         */
        // init heap 'the less frequent element first'
        Queue<Integer> heap = new PriorityQueue<>((n1, n2) -> count.get(n1) - count.get(n2));

        /** NOTE !!! here */
        // Step 2. keep k top frequent elements in the heap
        // O(N log k) < O(N log N) time
        for (int n: count.keySet()) {
            heap.add(n);
            /** if size > k, remove smallest element (e.g. keep k top frequent elements in the heap) */
            if (heap.size() > k){
                heap.poll();
            }
        }

        // Step 3. build an output array
        // O(k log k) time
        int[] top = new int[k];
        for(int i = k - 1; i >= 0; --i) {
            top[i] = heap.poll();
        }
        return top;
    }

    // V0''
    // IDEA : HASH MAP + PQ (by GPT)
    public int[] topKFrequent_0_1(int[] nums, int k) {

        // Step 1. Count the frequency of each element
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int num : nums) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }

        /** NOTE !!! how to init PQ below */
        // Step 2. Use a Min-Heap (Priority Queue) to keep track of top K elements
        PriorityQueue<Map.Entry<Integer, Integer>> heap = new PriorityQueue<>(
                (a, b) -> a.getValue() - b.getValue()
        );

        // Step 3. Maintain the heap of size k
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            heap.add(entry);
            if (heap.size() > k) {
                heap.poll();  // Remove the element with the smallest frequency
            }
        }

        // Step 4. Extract the elements from the heap
        int[] topK = new int[k];
        for (int i = 0; i < k; i++) {
            topK[i] = heap.poll().getKey();
        }

        return topK;
    }

    // V1
    // IDEA : HEAP
    // https://leetcode.com/problems/top-k-frequent-elements/editorial/
    public int[] topKFrequent_2(int[] nums, int k) {
        // O(1) time
        if (k == nums.length) {
            return nums;
        }

        // 1. build hash map : character and how often it appears
        // O(N) time
        Map<Integer, Integer> count = new HashMap();
        for (int n: nums) {
            count.put(n, count.getOrDefault(n, 0) + 1);
        }

        // init heap 'the less frequent element first'
        Queue<Integer> heap = new PriorityQueue<>(
                (n1, n2) -> count.get(n1) - count.get(n2));

        // 2. keep k top frequent elements in the heap
        // O(N log k) < O(N log N) time
        for (int n: count.keySet()) {
            heap.add(n);
            if (heap.size() > k) heap.poll();
        }

        // 3. build an output array
        // O(k log k) time
        int[] top = new int[k];
        for(int i = k - 1; i >= 0; --i) {
            top[i] = heap.poll();
        }
        return top;
    }

    // V2
    // IDEA : Quickselect (Hoare's selection algorithm)
    // https://leetcode.com/problems/top-k-frequent-elements/editorial/
    int[] unique;
    Map<Integer, Integer> count;

    public void swap(int a, int b) {
        int tmp = unique[a];
        unique[a] = unique[b];
        unique[b] = tmp;
    }

    public int partition(int left, int right, int pivot_index) {
        int pivot_frequency = count.get(unique[pivot_index]);
        // 1. move pivot to end
        swap(pivot_index, right);
        int store_index = left;

        // 2. move all less frequent elements to the left
        for (int i = left; i <= right; i++) {
            if (count.get(unique[i]) < pivot_frequency) {
                swap(store_index, i);
                store_index++;
            }
        }

        // 3. move pivot to its final place
        swap(store_index, right);

        return store_index;
    }

    public void quickselect(int left, int right, int k_smallest) {
        /*
        Sort a list within left..right till kth less frequent element
        takes its place.
        */

        // base case: the list contains only one element
        if (left == right) return;

        // select a random pivot_index
        Random random_num = new Random();
        int pivot_index = left + random_num.nextInt(right - left);

        // find the pivot position in a sorted list
        pivot_index = partition(left, right, pivot_index);

        // if the pivot is in its final sorted position
        if (k_smallest == pivot_index) {
            return;
        } else if (k_smallest < pivot_index) {
            // go left
            quickselect(left, pivot_index - 1, k_smallest);
        } else {
            // go right
            quickselect(pivot_index + 1, right, k_smallest);
        }
    }

    public int[] topKFrequent_3(int[] nums, int k) {
        // build hash map : character and how often it appears
        count = new HashMap();
        for (int num: nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }

        // array of unique elements
        int n = count.size();
        unique = new int[n];
        int i = 0;
        for (int num: count.keySet()) {
            unique[i] = num;
            i++;
        }

        // kth top frequent element is (n - k)th less frequent.
        // Do a partial sort: from less frequent to the most frequent, till
        // (n - k)th less frequent element takes its place (n - k) in a sorted array.
        // All element on the left are less frequent.
        // All the elements on the right are more frequent.
        quickselect(0, n - 1, n - k);
        // Return top k frequent elements
        return Arrays.copyOfRange(unique, n - k, n);
    }

}