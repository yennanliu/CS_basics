package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/count-of-smaller-numbers-after-self/description/

import java.util.*;

public class countOfSmallerNumbersAfterSelf {

    // V0
    // TODO : implement

    // V1
    // IDEA : BINARY SEARCH (GPT)
    /**
     * 	1.	Sorted List:
     * 	    •	We maintain a sortedList that keeps track of the elements we’ve encountered so far, sorted in ascending order.
     *
     * 	2.	Binary Search for Insertion:
     * 	    •	For each element in the input array, starting from the end (rightmost element):
     * 	    •	We use binary search to find the correct position where the current element should be inserted into sortedList.
     * 	    •	The index at which we would insert the current element gives us the count of elements that are smaller than the current element and have been encountered so far.
     *
     * 	3.	Insert Element:
     * 	    •	After determining the insert position, we insert the current element into the sortedList at that position. This keeps the list sorted for subsequent operations.
     *
     * 	4.	Result Array:
     * 	    •	We store the count of smaller elements to the right in the result array, which is eventually returned as a list.
     *
     * 	5.	Time Complexity:
     * 	    •	The time complexity of this approach is O(n log n) where n is the length of the input array. The log n factor comes from the binary search, and we do this for each of the n elements.
     *
     */
    public List<Integer> countSmaller_1(int[] nums) {
        List<Integer> sortedList = new ArrayList<>();
        Integer[] result = new Integer[nums.length];

        for (int i = nums.length - 1; i >= 0; i--) {
            int currentNumber = nums[i];
            // Find the position where currentNumber should be inserted
            int insertPosition = findInsertPosition(sortedList, currentNumber);

            // The insert position gives the count of smaller elements to the right
            result[i] = insertPosition;

            // Insert currentNumber in the sorted list
            sortedList.add(insertPosition, currentNumber);
        }

        return Arrays.asList(result);
    }

    // Helper method to find the insert position using binary search
    /**
     *  The findInsertPosition method is designed to find the correct position in the sortedList
     *  where the target number should be inserted to maintain the list’s sorted order. (Ascending order!!!)
     *
     *  NOTE !!! -> This position also represents the number of elements in sortedList that are smaller than target.
     */
    private int findInsertPosition(List<Integer> sortedList, int target) {

        /**
         *  left: The starting index of the range we are considering. Initially set to 0.
         * 	right: The ending index of the range we are considering. Initially set to sortedList.size() (which is one past the last valid index).
         */
        int left = 0;
        int right = sortedList.size();

        /**
         *  Loop Termination:
         *   The loop continues until left and right converge on the same index.
         *   This index represents the correct position to insert target.
         */
        while (left < right) {
            int mid = left + (right - left) / 2;

            /**
             *  sortedList[mid] >= target
             *
             *   -> If the element at mid is greater than or equal to target,
             *     it means target should be inserted before mid (or at mid),
             *     so we move the right pointer to mid.
             */
            if (sortedList.get(mid) >= target) {
                right = mid;
            }

            /**
             *  sortedList[mid] < target
             *
             *   -> If the element at mid is less than target,
             *      it means target should be inserted after mid.
             *      Thus, we move the left pointer to mid + 1.
             */
            else {
                left = mid + 1;
            }
        }

        return left;
    }

    // V2
    // IDEA : Binary Index Tree (BIT)
    // https://leetcode.com/problems/count-of-smaller-numbers-after-self/solutions/76611/short-java-binary-index-tree-beat-97-33-with-detailed-explanation/
    // https://www.topcoder.com/thrive/articles/Binary%20Indexed%20Trees
    /**
     *  IDEA :
     *
     *  1, we should build an array with the length equals to the max element of the nums array as BIT.
     *  2, To avoid minus value in the array, we should first add the (min+1) for every elements
     *     (It may be out of range, where we can use long to build another array. But no such case in the test cases so far.)
     *   3, Using standard BIT operation to solve it.
     *
     */
    public List<Integer> countSmaller_2(int[] nums) {
        List<Integer> res = new LinkedList<Integer>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        // find min value and minus min by each elements, plus 1 to avoid 0 element
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            min = (nums[i] < min) ? nums[i]:min;
        }
        int[] nums2 = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            nums2[i] = nums[i] - min + 1;
            max = Math.max(nums2[i],max);
        }
        int[] tree = new int[max+1];
        for (int i = nums2.length-1; i >= 0; i--) {
            res.add(0,get(nums2[i]-1,tree));
            update(nums2[i],tree);
        }
        return res;
    }
    private int get(int i, int[] tree) {
        int num = 0;
        while (i > 0) {
            num +=tree[i];
            i -= i&(-i);
        }
        return num;
    }
    private void update(int i, int[] tree) {
        while (i < tree.length) {
            tree[i] ++;
            i += i & (-i);
        }
    }


    // V3
    // IDEA : BST
    // TODO : fix TLE
    // https://leetcode.com/problems/count-of-smaller-numbers-after-self/solutions/76587/easiest-java-solution/
    public List<Integer> countSmaller_3(int[] nums) {
        List<Integer> res = new ArrayList<>();
        if(nums == null || nums.length == 0) return res;
        TreeNode root = new TreeNode(nums[nums.length - 1]);
        res.add(0);
        for(int i = nums.length - 2; i >= 0; i--) {
            int count = insertNode(root, nums[i]);
            res.add(count);
        }
        Collections.reverse(res);
        return res;
    }

    public int insertNode(TreeNode root, int val) {
        int thisCount = 0;
        while(true) {
            if(val <= root.val) {
                root.count++;
                if(root.left == null) {
                    root.left = new TreeNode(val); break;
                } else {
                    root = root.left;
                }
            } else {
                thisCount += root.count;
                if(root.right == null) {
                    root.right = new TreeNode(val); break;
                } else {
                    root = root.right;
                }
            }
        }
        return thisCount;
    }
}

class TreeNode {
    TreeNode left;
    TreeNode right;
    int val;
    int count = 1;
    public TreeNode(int val) {
        this.val = val;
    }


    // V4
    // https://leetcode.com/problems/count-of-smaller-numbers-after-self/solutions/445769/merge-sort-clear-simple-explanation-with-examples-o-n-lg-n/
    //
    // Wrapper class for each and every value of the input array,
    // to store the original index position of each value, before we merge sort the array
    private class ArrayValWithOrigIdx {
        int val;
        int originalIdx;

        public ArrayValWithOrigIdx(int val, int originalIdx) {
            this.val = val;
            this.originalIdx = originalIdx;
        }
    }

    public List<Integer> countSmaller_4(int[] nums) {
        if (nums == null || nums.length == 0) return new LinkedList<Integer>();
        int n = nums.length;
        int[] result = new int[n];

        ArrayValWithOrigIdx[] newNums = new ArrayValWithOrigIdx[n];
        for (int i = 0; i < n; ++i) newNums[i] = new ArrayValWithOrigIdx(nums[i], i);

        mergeSortAndCount(newNums, 0, n - 1, result);

        // notice we don't care about the sorted array after merge sort finishes.
        // we only wanted the result counts, generated by running merge sort
        List<Integer> resultList = new LinkedList<Integer>();
        for (int i : result) resultList.add(i);
        return resultList;
    }

    private void mergeSortAndCount(ArrayValWithOrigIdx[] nums, int start, int end, int[] result) {
        if (start >= end) return;

        int mid = (start + end) / 2;
        mergeSortAndCount(nums, start, mid, result);
        mergeSortAndCount(nums, mid + 1, end, result);

        // left subarray start...mid
        // right subarray mid+1...end
        int leftPos = start;
        int rightPos = mid + 1;
        LinkedList<ArrayValWithOrigIdx> merged = new LinkedList<ArrayValWithOrigIdx>();
        int numElemsRightArrayLessThanLeftArray = 0;
        while (leftPos < mid + 1 && rightPos <= end) {
            if (nums[leftPos].val > nums[rightPos].val) {
                // this code block is exactly what the problem is asking us for:
                // a number from the right side of the original input array, is smaller
                // than a number from the left side
                //
                // within this code block,
                // nums[rightPos] is smaller than the start of the left sub-array.
                // Since left sub-array is already sorted,
                // nums[rightPos] must also be smaller than the entire remaining left sub-array
                ++numElemsRightArrayLessThanLeftArray;

                // continue with normal merge sort, merge
                merged.add(nums[rightPos]);
                ++rightPos;
            } else {
                // a number from left side of array, is smaller than a number from
                // right side of array
                result[nums[leftPos].originalIdx] += numElemsRightArrayLessThanLeftArray;

                // Continue with normal merge sort
                merged.add(nums[leftPos]);
                ++leftPos;
            }
        }

        // part of normal merge sort, if either left or right sub-array is not empty,
        // move all remaining elements into merged result
        while (leftPos < mid + 1) {
            result[nums[leftPos].originalIdx] += numElemsRightArrayLessThanLeftArray;

            merged.add(nums[leftPos]);
            ++leftPos;
        }
        while (rightPos <= end) {
            merged.add(nums[rightPos]);
            ++rightPos;
        }

        // part of normal merge sort
        // copy back merged result into array
        int pos = start;
        for (ArrayValWithOrigIdx m : merged) {
            nums[pos] = m;
            ++pos;
        }
    }

}
