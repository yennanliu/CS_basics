package LeetCodeJava.Array;

// https://leetcode.com/problems/kth-largest-element-in-an-array/

import java.util.*;

public class KthLargestElementInAnArray {

    // V0
    // IDEA : PQ (priority queue)
    public int findKthLargest(int[] nums, int k) {
        if (nums.length == 1){
            if (k == 1){
                return nums[0];
            }
            return -1; // ?
        }

        // init
        // NOTE !!! init MAX PQ via Comparator.reverseOrder()
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

    // V0
    // IDEA : ARRAY + SORTING
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
    public int findKthLargest_2(int[] nums, int k) {
        Arrays.sort(nums);
        // Can't sort int[] in descending order in Java;
        // Sort ascending and then return the kth element from the end
        return nums[nums.length - k];
    }

    // V2
    // IDEA : Min-Heap
    // https://leetcode.com/problems/kth-largest-element-in-an-array/editorial/
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
