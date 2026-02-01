package LeetCodeJava.Tree;

// https://leetcode.com/problems/range-sum-query-mutable/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 307. Range Sum Query - Mutable
 * Medium
 * Topics
 * Companies
 * Given an integer array nums, handle multiple queries of the following types:
 *
 * Update the value of an element in nums.
 * Calculate the sum of the elements of nums between indices left and right inclusive where left <= right.
 * Implement the NumArray class:
 *
 * NumArray(int[] nums) Initializes the object with the integer array nums.
 * void update(int index, int val) Updates the value of nums[index] to be val.
 * int sumRange(int left, int right) Returns the sum of the elements of nums between indices left and right inclusive (i.e. nums[left] + nums[left + 1] + ... + nums[right]).
 *
 *
 * Example 1:
 *
 * Input
 * ["NumArray", "sumRange", "update", "sumRange"]
 * [[[1, 3, 5]], [0, 2], [1, 2], [0, 2]]
 * Output
 * [null, 9, null, 8]
 *
 * Explanation
 * NumArray numArray = new NumArray([1, 3, 5]);
 * numArray.sumRange(0, 2); // return 1 + 3 + 5 = 9
 * numArray.update(1, 2);   // nums = [1, 2, 5]
 * numArray.sumRange(0, 2); // return 1 + 2 + 5 = 8
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 3 * 104
 * -100 <= nums[i] <= 100
 * 0 <= index < nums.length
 * -100 <= val <= 100
 * 0 <= left <= right < nums.length
 * At most 3 * 104 calls will be made to update and sumRange.
 *
 */
public class RangeSumQueryMutable {

    // V0
//    class NumArray {
//
//        public NumArray(int[] nums) {
//
//        }
//
//        public void update(int index, int val) {
//
//        }
//
//        public int sumRange(int left, int right) {
//
//        }
//    }

    // V0-1
    // TLE, TODO: optimize & fix
//    class NumArray {
//
//        List<Integer> preSum;
//        int[] nums;
//
//        public NumArray(int[] nums) {
//            this.nums = nums;
//            // pre-sum array
//            this.getPreSumArray(nums);
//        }
//
//        public void update(int index, int val) {
//            this.nums[index] = val;
//            // update preSum
//            this.getPreSumArray(nums);
//        }
//
//        public int sumRange(int left, int right) {
//
//            if (right < left) {
//                return -1; // or throw exception
//            }
//
//            return this.preSum.get(right + 1) - this.preSum.get(left);
//        }
//
//        private void getPreSumArray(int[] nums) {
//            this.preSum = new ArrayList<>();
//            this.preSum.add(0);
//            int cur = 0;
//            for (int i = 0; i < nums.length; i++) {
//                cur += nums[i];
//                this.preSum.add(cur);
//            }
//        }
//
//    }


    // V1

    // V2
    // https://leetcode.com/problems/range-sum-query-mutable/solutions/75753/java-using-binary-indexed-tree-with-clea-ts6e/
    // IDEA: BINARY INDEXED TREE
    public class NumArray_2 {
        /**
         * Binary Indexed Trees (BIT or Fenwick tree):
         * https://www.topcoder.com/community/data-science/data-science-
         * tutorials/binary-indexed-trees/
         *
         * Example: given an array a[0]...a[7], we use a array BIT[9] to
         * represent a tree, where index [2] is the parent of [1] and [3], [6]
         * is the parent of [5] and [7], [4] is the parent of [2] and [6], and
         * [8] is the parent of [4]. I.e.,
         *
         * BIT[] as a binary tree:
         * ______________*
         * ______*
         * __* __*
         * * * * *
         * indices: 0 1 2 3 4 5 6 7 8
         *
         * BIT[i] = ([i] is a left child) ? the partial sum from its left most
         * descendant to itself : the partial sum from its parent (exclusive) to
         * itself. (check the range of "__").
         *
         * Eg. BIT[1]=a[0], BIT[2]=a[1]+BIT[1]=a[1]+a[0], BIT[3]=a[2],
         * BIT[4]=a[3]+BIT[3]+BIT[2]=a[3]+a[2]+a[1]+a[0],
         * BIT[6]=a[5]+BIT[5]=a[5]+a[4],
         * BIT[8]=a[7]+BIT[7]+BIT[6]+BIT[4]=a[7]+a[6]+...+a[0], ...
         *
         * Thus, to update a[1]=BIT[2], we shall update BIT[2], BIT[4], BIT[8],
         * i.e., for current [i], the next update [j] is j=i+(i&-i) //double the
         * last 1-bit from [i].
         *
         * Similarly, to get the partial sum up to a[6]=BIT[7], we shall get the
         * sum of BIT[7], BIT[6], BIT[4], i.e., for current [i], the next
         * summand [j] is j=i-(i&-i) // delete the last 1-bit from [i].
         *
         * To obtain the original value of a[7] (corresponding to index [8] of
         * BIT), we have to subtract BIT[7], BIT[6], BIT[4] from BIT[8], i.e.,
         * starting from [idx-1], for current [i], the next subtrahend [j] is
         * j=i-(i&-i), up to j==idx-(idx&-idx) exclusive. (However, a quicker
         * way but using extra space is to store the original array.)
         */

        int[] nums;
        int[] BIT;
        int n;

        /**
         * time = O(N)
         * space = O(H)
         */
        public NumArray_2(int[] nums) {
            this.nums = nums;

            n = nums.length;
            BIT = new int[n + 1];
            for (int i = 0; i < n; i++)
                init(i, nums[i]);
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public void init(int i, int val) {
            i++;
            while (i <= n) {
                BIT[i] += val;
                i += (i & -i);
            }
        }

        void update(int i, int val) {
            int diff = val - nums[i];
            nums[i] = val;
            init(i, diff);
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public int getSum(int i) {
            int sum = 0;
            i++;
            while (i > 0) {
                sum += BIT[i];
                i -= (i & -i);
            }
            return sum;
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public int sumRange(int i, int j) {
            return getSum(j) - getSum(i - 1);
        }
    }

    // V3-1
    // https://leetcode.com/problems/range-sum-query-mutable/solutions/5511678/intuitive-and-optimized-approach-explain-ts4m/
    // IDEA :  Follow Up 315. Count of Smaller Numbers After Self
    public class NumArray_3_1 {

        class SegmentTreeNode {
            private int start, end;
            private SegmentTreeNode left, right;
            private int sum;

            /**
             * time = O(N)
             * space = O(H)
             */
            public SegmentTreeNode(int start, int end) {
                this.start = start;
                this.end = end;
                this.left = null;
                this.right = null;
                this.sum = 0;
            }
        }

        SegmentTreeNode root = null;

        /**
         * time = O(N)
         * space = O(H)
         */
        public NumArray_3_1(int[] nums) {
            root = buildTree(nums, 0, nums.length - 1);
        }

        // TC : O(n)
        private SegmentTreeNode buildTree(int[] nums, int start, int end) {
            if (start > end) {
                return null;
            } else {
                SegmentTreeNode ret = new SegmentTreeNode(start, end);
                if (start == end) {
                    ret.sum = nums[start]; // leaf nodes
                } else {
                    int mid = start + (end - start) / 2;
                    ret.left = buildTree(nums, start, mid);
                    ret.right = buildTree(nums, mid + 1, end);
                    ret.sum = ret.left.sum + ret.right.sum;
                }
                return ret;
            }
        }

        // TC : O(logn)
        void update(int i, int val) {
            updateHelper(root, i, val);
        }

        void updateHelper(SegmentTreeNode root, int pos, int val) {
            // found leaf node to be updated
            if (root.start == root.end) {
                root.sum = val;
            } else {
                // parent nodes across the path
                int mid = root.start + (root.end - root.start) / 2;
                if (pos <= mid) {

                    updateHelper(root.left, pos, val);
                } else {
                    updateHelper(root.right, pos, val);
                }
                root.sum = root.left.sum + root.right.sum;
            }
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public int sumRange(int i, int j) {
            return sumRangeHelper(root, i, j);
        }

        // TC : O(logn)
        /**
         * time = O(N)
         * space = O(H)
         */
        public int sumRangeHelper(SegmentTreeNode root, int start, int end) {
            // if you found out the node that matches your search return its value
            if (root.start == start && root.end == end) {
                return root.sum;
            } else {

                int mid = root.start + (root.end - root.start) / 2; // overflow conditions
                if (end <= mid) {
                    // move left
                    return sumRangeHelper(root.left, start, end);
                } else if (start >= mid + 1) {
                    // move right
                    return sumRangeHelper(root.right, start, end);
                } else {
                    // consider both nodes
                    return sumRangeHelper(root.left, start, mid) + sumRangeHelper(root.right, mid + 1, end);
                }
            }
        }
    }

    // V3-2
    // https://leetcode.com/problems/range-sum-query-mutable/solutions/5511678/intuitive-and-optimized-approach-explain-ts4m/
    class NumArray_3_2{
        private int[] segmentTree; // Array to store segment tree
        private int arrayLength; // Length of the input array

        // Constructor to initialize the segment tree with the given array
        /**
         * time = O(N)
         * space = O(H)
         */
        public NumArray_3_2(int[] nums) {
            this.arrayLength = nums.length; // Set the length of the input array
            if (arrayLength > 0) { // If array is not empty
                int height = (int) (Math.ceil(Math.log(arrayLength) / Math.log(2))); // Calculate height of the segment tree
                int maxSize = 2 * (int) Math.pow(2, height) - 1; // Maximum size of the segment tree
                segmentTree = new int[maxSize]; // Initialize the segment tree array
                buildSegmentTree(nums, 0, arrayLength - 1, 0); // Build the segment tree
            }
        }

        // Utility function to get the middle index of a range
        private int getMiddle(int start, int end) {
            return start + (end - start) / 2;
        }

        // Recursive function to build the segment tree
        private void buildSegmentTree(int[] arr, int segmentStart, int segmentEnd, int currentIndex) {
            // If the segment start is equal to the segment end, it's a leaf node
            if (segmentStart == segmentEnd) {
                segmentTree[currentIndex] = arr[segmentStart]; // Set the leaf node value
                return;
            }
            int mid = getMiddle(segmentStart, segmentEnd); // Get the middle index
            buildSegmentTree(arr, segmentStart, mid, currentIndex * 2 + 1); // Recursively build the left subtree
            buildSegmentTree(arr, mid + 1, segmentEnd, currentIndex * 2 + 2); // Recursively build the right subtree
            // Set the current node value as the sum of left and right children
            segmentTree[currentIndex] = segmentTree[currentIndex * 2 + 1] + segmentTree[currentIndex * 2 + 2];
        }

        // Function to update an element in the segment tree
        /**
         * time = O(N)
         * space = O(H)
         */
        public void update(int index, int newValue) {
            if (arrayLength == 0)
                return; // If the array is empty, do nothing
            int difference = newValue - getElement(0, arrayLength - 1, index, 0); // Calculate the difference
            updateSegmentTree(0, arrayLength - 1, index, difference, 0); // Update the segment tree
        }

        // Utility function to get the value of an element in the segment tree
        private int getElement(int segmentStart, int segmentEnd, int queryIndex, int currentIndex) {
            // If the segment start is equal to the segment end, it's a leaf node
            if (segmentStart == segmentEnd) {
                return segmentTree[currentIndex]; // Return the leaf node value
            }
            int mid = getMiddle(segmentStart, segmentEnd); // Get the middle index
            // Recursively get the element value from the left or right subtree
            if (queryIndex <= mid) {
                return getElement(segmentStart, mid, queryIndex, 2 * currentIndex + 1);
            } else {
                return getElement(mid + 1, segmentEnd, queryIndex, 2 * currentIndex + 2);
            }
        }

        // Recursive function to update the segment tree
        private void updateSegmentTree(int segmentStart, int segmentEnd, int index, int difference, int currentIndex) {
            // If the index is outside the segment range, return
            if (index < segmentStart || index > segmentEnd)
                return;
            segmentTree[currentIndex] += difference; // Update the current node value
            // If the segment start is not equal to the segment end, update the children
            if (segmentStart != segmentEnd) {
                int mid = getMiddle(segmentStart, segmentEnd); // Get the middle index
                updateSegmentTree(segmentStart, mid, index, difference, 2 * currentIndex + 1); // Update the left subtree
                updateSegmentTree(mid + 1, segmentEnd, index, difference, 2 * currentIndex + 2); // Update the right subtree
            }
        }

        // Function to get the sum of elements in the given range
        /**
         * time = O(N)
         * space = O(H)
         */
        public int sumRange(int left, int right) {
            if (arrayLength == 0)
                return 0; // If the array is empty, return 0
            return getSum(0, arrayLength - 1, left, right, 0); // Get the sum from the segment tree
        }

        // Recursive function to get the sum of elements in the given range
        private int getSum(int segmentStart, int segmentEnd, int queryStart, int queryEnd, int currentIndex) {
            // If the query range is within the segment range, return the current node value
            if (queryStart <= segmentStart && queryEnd >= segmentEnd) {
                return segmentTree[currentIndex];
            }
            // If the segment range is outside the query range, return 0
            if (segmentEnd < queryStart || segmentStart > queryEnd) {
                return 0;
            }
            int mid = getMiddle(segmentStart, segmentEnd); // Get the middle index
            // Recursively get the sum from the left and right subtrees
            return getSum(segmentStart, mid, queryStart, queryEnd, 2 * currentIndex + 1) +
                    getSum(mid + 1, segmentEnd, queryStart, queryEnd, 2 * currentIndex + 2);
        }
    }


    // V3-3
    // https://leetcode.com/problems/range-sum-query-mutable/solutions/5511678/intuitive-and-optimized-approach-explain-ts4m/
    class NumArray_3_3 {
        private SegmentTree root; // Root of the segment tree

        /**
         * time = O(N)
         * space = O(H)
         */
        public NumArray_3_3(int[] nums) {
            if (nums.length > 0) {
                root = buildSegmentTree(nums, 0, nums.length - 1); // Build the segment tree
            }
        }

        // Class representing a segment tree node
        class SegmentTree {
            SegmentTree left, right;
            int start, end, sum;

            SegmentTree(int start, int end) {
                this.start = start;
                this.end = end;
            }
        }

        // Function to build the segment tree
        private SegmentTree buildSegmentTree(int[] nums, int start, int end) {
            SegmentTree node = new SegmentTree(start, end);
            if (start == end) {
                node.sum = nums[start];
            } else {
                int mid = start + (end - start) / 2;
                node.left = buildSegmentTree(nums, start, mid);
                node.right = buildSegmentTree(nums, mid + 1, end);
                node.sum = node.left.sum + node.right.sum;
            }
            return node;
        }

        // Function to update an element in the segment tree
        /**
         * time = O(N)
         * space = O(H)
         */
        public void update(int index, int val) {
            update(root, index, val);
        }

        private void update(SegmentTree node, int index, int val) {
            if (node.start == node.end) {
                node.sum = val;
            } else {
                int mid = node.start + (node.end - node.start) / 2;
                if (index <= mid) {
                    update(node.left, index, val);
                } else {
                    update(node.right, index, val);
                }
                node.sum = node.left.sum + node.right.sum;
            }
        }

        // Function to get the sum of elements in the given range
        /**
         * time = O(N)
         * space = O(H)
         */
        public int sumRange(int left, int right) {
            return sumRange(root, left, right);
        }

        private int sumRange(SegmentTree node, int start, int end) {
            if (node.start == start && node.end == end) {
                return node.sum;
            }
            int mid = node.start + (node.end - node.start) / 2;
            if (end <= mid) {
                return sumRange(node.left, start, end);
            } else if (start > mid) {
                return sumRange(node.right, start, end);
            } else {
                return sumRange(node.left, start, mid) + sumRange(node.right, mid + 1, end);
            }
        }
    }

    // V4
    // https://leetcode.com/problems/range-sum-query-mutable/solutions/2357528/java-easy-solution-100-faster-code-by-sh-a2fd/
//    class NumArray {
//        static class BinaryIndexedTree {
//            int[] nums;
//            int[] BIT;
//            int n;
//
//            public BinaryIndexedTree(int[] nums) {
//                this.nums = nums;
//                this.n = nums.length;
//                BIT = new int[n + 1];
//                for (int i = 0; i < n; i++) {
//                    init(i, nums[i]);
//                }
//            }
//
//            void init(int i, int val) {
//                i++;
//                while (i <= n) {
//                    BIT[i] += val;
//                    i += (i & -i);
//                }
//            }
//
//            void update(int i, int val) {
//                int diff = val - nums[i];
//                nums[i] = val;
//                init(i, diff);
//            }
//
//            int getSum(int i) {
//                i++;
//                int ret = 0;
//                while (i > 0) {
//                    ret += BIT[i];
//                    i -= (i & -i);
//                }
//                return ret;
//            }
//        }
//
//        BinaryIndexedTree binaryIndexedTree;
//
//        public NumArray(int[] nums) {
//            binaryIndexedTree = new BinaryIndexedTree(nums);
//        }
//
//        public void update(int index, int val) {
//            binaryIndexedTree.update(index, val);
//        }
//
//        public int sumRange(int left, int right) {
//            return binaryIndexedTree.getSum(right) - binaryIndexedTree.getSum(left - 1);
//        }
//    }

}
