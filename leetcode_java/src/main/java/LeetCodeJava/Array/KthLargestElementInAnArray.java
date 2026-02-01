package LeetCodeJava.Array;

// https://leetcode.com/problems/kth-largest-element-in-an-array/
/**
 * 215. Kth Largest Element in an Array
 * Solved
 * N/A
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums and an integer k, return the kth largest element in the array.
 *
 * Note that it is the kth largest element in the sorted order, not the kth distinct element.
 *
 * Can you solve it without sorting?
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [3,2,1,5,6,4], k = 2
 * Output: 5
 * Example 2:
 *
 * Input: nums = [3,2,3,1,2,4,5,5,6], k = 4
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= k <= nums.length <= 105
 * -104 <= nums[i] <= 104
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 */
import java.util.*;

public class KthLargestElementInAnArray {

    // V0
    // IDEA: small PQ
    /**
     * time = O(N log K)
     * space = O(K)
     */
    public int findKthLargest(int[] nums, int k) {
        // edge
        if(nums == null || nums.length == 0){
            throw new RuntimeException("null array");
        }
        if(k > nums.length){
            throw new RuntimeException("k bigger than arr size");
        }
        if(nums.length == 1){
            if(k == 1){
                return nums[0];
            }
            throw new RuntimeException("not valid k");
        }

        // small PQ, so we can keep PQ size = k
        // and pop the `k+1` largest element
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        for(int x: nums){
            pq.add(x);
            while (pq.size() > k){
                pq.poll();
            }
        }

        if(!pq.isEmpty()){
            return pq.poll();
        }

        return -1;  // should NOT visit here
    }

    // V-0-0-1
    // IDEA: big PQ
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int findKthLargest_0_0_1(int[] nums, int k) {
        // edge
        if (nums == null || nums.length == 0) {
            //return -1; // ???
            throw new RuntimeException("null array");
        }
        if (nums.length == 1) {
            if (k == 1) {
                return nums[0]; // ?????
            }
           // return -1;
            throw new RuntimeException("null array");
        }
        // big PQ
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });

        for (int x : nums) {
            pq.add(x);
        }

        // pop
        int j = 0;
        int res = -1;
        while (j < k) {
            res = pq.poll();
            j += 1;
        }

        return res;
    }

    // V0-0-2
    // IDEA : PQ (priority queue)
    /**

     * time = O(N log N)

     * space = O(N)

     */
    public int findKthLargest_0_0_2(int[] nums, int k) {
        if (nums.length == 1){
            if (k == 1){
                return nums[0];
            }
            return -1; // ?
        }

        // init
        // NOTE !!! init small PQ via Comparator.reverseOrder()
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        for (int x : nums){
            pq.add(x);
        }
        //System.out.println("pq = " + pq);
        int cur = -1;
        while (k > 0){
            cur = pq.poll();
            k -= 1;
        }

        return cur;
    }

    // V0-1
    // IDEA : ARRAY + SORTING
    /**

     * time = O(N log N)

     * space = O(N)

     */
    public int findKthLargest_0_1(int[] nums, int k) {

        if (nums.length == 1 && k == 1){
            return nums[0];
        }
        Integer[] _nums = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++){
            _nums[i] = nums[i];
        }

        // reverse sort
        // https://stackoverflow.com/questions/1694751/java-array-sort-descending
        Arrays.sort(_nums, Collections.reverseOrder());

        for (int j = 0; j < _nums.length; j++){
            int cur = _nums[j];
            if (j == k-1){
                return cur;
            }
        }

        return -1;
    }

    // V1
    // IDEA : SORT
    // https://leetcode.com/problems/kth-largest-element-in-an-array/editorial/
    /**

     * time = O(N log N)

     * space = O(1)

     */
    public int findKthLargest_2(int[] nums, int k) {
        Arrays.sort(nums);
        // Can't sort int[] in descending order in Java;
        // Sort ascending and then return the kth element from the end
        return nums[nums.length - k];
    }

    // V2
    // IDEA : Min-Heap
    // https://leetcode.com/problems/kth-largest-element-in-an-array/editorial/
    /**

     * time = O(N log K)

     * space = O(K)

     */
    public int findKthLargest_3(int[] nums, int k) {
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        for (int num: nums) {
            heap.add(num);
            if (heap.size() > k) {
                heap.remove();
            }
        }

        return heap.peek();
    }

    // V3
    // IDEA : Quickselect
    // https://leetcode.com/problems/kth-largest-element-in-an-array/editorial/
    /**
     * time = O(N) average, O(N^2) worst
     * space = O(N)
     */
    public int findKthLargest_4(int[] nums, int k) {
        List<Integer> list = new ArrayList<>();
        for (int num: nums) {
            list.add(num);
        }

        return quickSelect(list, k);
    }

    public int quickSelect(List<Integer> nums, int k) {
        int pivotIndex = new Random().nextInt(nums.size());
        int pivot = nums.get(pivotIndex);

        List<Integer> left = new ArrayList<>();
        List<Integer> mid = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        for (int num: nums) {
            if (num > pivot) {
                left.add(num);
            } else if (num < pivot) {
                right.add(num);
            } else {
                mid.add(num);
            }
        }

        if (k <= left.size()) {
            return quickSelect(left, k);
        }

        if (left.size() + mid.size() < k) {
            return quickSelect(right, k - left.size() - mid.size());
        }

        return pivot;
    }

    // V4
    // IDEA : Counting Sort
    // https://leetcode.com/problems/kth-largest-element-in-an-array/editorial/
    /**

     * time = O(N + R)

     * space = O(R)

     */
    public int findKthLargest_5(int[] nums, int k) {
        int minValue = Integer.MAX_VALUE;
        int maxValue = Integer.MIN_VALUE;

        for (int num: nums) {
            minValue = Math.min(minValue, num);
            maxValue = Math.max(maxValue, num);
        }

        int[] count = new int[maxValue - minValue + 1];
        for (int num: nums) {
            count[num - minValue]++;
        }

        int remain = k;
        for (int num = count.length - 1; num >= 0; num--) {
            remain -= count[num];
            if (remain <= 0) {
                return num + minValue;
            }
        }

        return -1;
    }



}
