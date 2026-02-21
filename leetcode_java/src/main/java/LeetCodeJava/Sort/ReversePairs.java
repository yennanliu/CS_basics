package LeetCodeJava.Sort;

// https://leetcode.com/problems/reverse-pairs/description/
/**
 * 493. Reverse Pairs
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an integer array nums, return the number of reverse pairs in the array.
 *
 * A reverse pair is a pair (i, j) where:
 *
 * 0 <= i < j < nums.length and
 * nums[i] > 2 * nums[j].
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,2,3,1]
 * Output: 2
 * Explanation: The reverse pairs are:
 * (1, 4) --> nums[1] = 3, nums[4] = 1, 3 > 2 * 1
 * (3, 4) --> nums[3] = 3, nums[4] = 1, 3 > 2 * 1
 * Example 2:
 *
 * Input: nums = [2,4,3,5,1]
 * Output: 3
 * Explanation: The reverse pairs are:
 * (1, 4) --> nums[1] = 4, nums[4] = 1, 4 > 2 * 1
 * (2, 4) --> nums[2] = 3, nums[4] = 1, 3 > 2 * 1
 * (3, 4) --> nums[3] = 5, nums[4] = 1, 5 > 2 * 1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 5 * 104
 * -231 <= nums[i] <= 231 - 1
 *
 *
 */
public class ReversePairs {

    // V0
//    public int reversePairs(int[] nums) {
//
//    }

    // V1-1
    // IDEA: BRUTE FORCE (TLE)
    // https://leetcode.com/problems/reverse-pairs/editorial/
    public int reversePairs_1_1(int[] nums) {
        int n = nums.length;
        int count = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // cast to long to prevent integer overflow
                if ((long) nums[j] > 2L * nums[i]) {
                    count++;
                }
            }
        }

        return count;
    }


    // V1-2
    // IDEA:  Binary Search Tree (TLE)
    // https://leetcode.com/problems/reverse-pairs/editorial/

    static class Node {
        Node left, right;
        int val;
        int countGe; // count of nodes >= this value in this subtree

        Node(int val) {
            this.val = val;
            this.countGe = 1;
            this.left = null;
            this.right = null;
        }
    }

    private Node insert(Node head, int val) {
        if (head == null) {
            return new Node(val);
        } else if (val == head.val) {
            head.countGe++;
        } else if (val < head.val) {
            head.left = insert(head.left, val);
        } else {
            head.countGe++;
            head.right = insert(head.right, val);
        }
        return head;
    }

    private int search(Node head, long target) {
        if (head == null) {
            return 0;
        } else if (target == head.val) {
            return head.countGe;
        } else if (target < head.val) {
            return head.countGe + search(head.left, target);
        } else {
            return search(head.right, target);
        }
    }

    public int reversePairs_1_2(int[] nums) {
        Node head = null;
        int count = 0;

        for (int num : nums) {
            // use long to prevent overflow
            long target = 2L * num + 1;
            count += search(head, target);
            head = insert(head, num);
        }

        return count;
    }



    // V1-3
    // IDEA: BIT
    // https://leetcode.com/problems/reverse-pairs/editorial/


    // V1-4
    // IDEA: Modified Merge Sort
    // https://leetcode.com/problems/reverse-pairs/editorial/
    public int reversePairs_1_4(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;
        return mergeSortAndCount(nums, 0, nums.length - 1);
    }

    private int mergeSortAndCount(int[] A, int start, int end) {
        if (start >= end)
            return 0;

        int mid = start + (end - start) / 2;

        int count = mergeSortAndCount(A, start, mid)
                + mergeSortAndCount(A, mid + 1, end);

        // Count reverse pairs
        int j = mid + 1;
        for (int i = start; i <= mid; i++) {
            while (j <= end && (long) A[i] > 2L * A[j]) {
                j++;
            }
            count += j - (mid + 1);
        }

        merge(A, start, mid, end);

        return count;
    }

    private void merge(int[] A, int start, int mid, int end) {

        int n1 = mid - start + 1;
        int n2 = end - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++)
            L[i] = A[start + i];

        for (int j = 0; j < n2; j++)
            R[j] = A[mid + 1 + j];

        int i = 0, j = 0, k = start;

        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                A[k++] = L[i++];
            } else {
                A[k++] = R[j++];
            }
        }

        while (i < n1) {
            A[k++] = L[i++];
        }

        while (j < n2) {
            A[k++] = R[j++];
        }
    }



    // V2



}
