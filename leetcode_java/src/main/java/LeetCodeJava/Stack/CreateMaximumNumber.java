package LeetCodeJava.Stack;

// https://leetcode.com/problems/create-maximum-number/description/
// https://leetcode.cn/problems/create-maximum-number/description/

import java.util.Stack;

/**
 * 321. Create Maximum Number
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * You are given two integer arrays nums1 and nums2 of lengths m and n respectively. nums1 and nums2 represent the digits of two numbers. You are also given an integer k.
 *
 * Create the maximum number of length k <= m + n from digits of the two numbers. The relative order of the digits from the same array must be preserved.
 *
 * Return an array of the k digits representing the answer.
 *
 *
 *
 * Example 1:
 *
 * Input: nums1 = [3,4,6,5], nums2 = [9,1,2,5,8,3], k = 5
 * Output: [9,8,6,5,3]
 * Example 2:
 *
 * Input: nums1 = [6,7], nums2 = [6,0,4], k = 5
 * Output: [6,7,6,0,4]
 * Example 3:
 *
 * Input: nums1 = [3,9], nums2 = [8,9], k = 3
 * Output: [9,8,9]
 *
 *
 * Constraints:
 *
 * m == nums1.length
 * n == nums2.length
 * 1 <= m, n <= 500
 * 0 <= nums1[i], nums2[i] <= 9
 * 1 <= k <= m + n
 * nums1 and nums2 do not have leading zeros.
 *
 */
public class CreateMaximumNumber {

    // V0
//    public int[] maxNumber(int[] nums1, int[] nums2, int k) {
//
//    }

    // V1
    // https://leetcode.com/problems/create-maximum-number/solutions/1611691/java-easy-to-understand-monotonic-stack-43ijd/
    // IDEA:  monotonic STACK
    // monotonic decreasing stack same as leetcode 1673 , only > changed to <
    public int[] findLexMax(int[] nums, int k) {
        int n = nums.length;
        Stack<Integer> st = new Stack<>();
        int[] res = new int[k];

        /* we have to form a k digit number which is lexicographically greatest
         that gives some hint that we need to maintain a monotonic decreasing stack
         and rem=n-k means we will remove n-k smaller digits from stack
         lets say nums=[6,7,4,8,9] and k=3, we have to remove 5-3=2 digits from the stack,
         while we try to form monotonic decreasing satck
         o our max number will be [8,9]
         */
        int rem = n - k;
        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && st.peek() < nums[i] && rem > 0) {
                st.pop();
                rem--;
            }
            st.push(nums[i]);

        }

        /* if still some elements can be removed, the best option would be to remove the
        elements from the top of the stack, as we are mainting a monotonic decreasing satck
        so elemets at the top would be smaller
        take this example arr-[6,4,2] k=2 ans would be [6,4]
        */
        while (rem > 0) {
            st.pop();
            rem--;
        }
        for (int i = k - 1; i >= 0; i--) {
            res[i] = st.pop();
        }
        return res;
    }


    private static boolean findMax(int[] arr1, int[] arr2, int p1,int p2) {
        while (p1 < arr1.length && p2 < arr2.length) {
            if (arr1[p1] < arr2[p2]) {
                return false;
            } else if (arr1[p1] > arr2[p2]) {
                return true;
            } else {
                p1++;
                p2++;
            }
        }
        // when p1 is empty returns true, so that in merge function it picks up values of arr2
        // when p1==arr1.length, then returns false,so that we keep pn picking arr2 elements
        return p1!=arr1.length; // tricky
    }


    // think of the case [6,0,4] and [6,7], can t do simple logic like merge two sorted arrays
    // we need to decide which pointer to move forward based on the rest of the array,
    // if both elements are same compare the next element, and then decide
    private int[] merge_1(int[] nums1, int[] nums2, int k) {
        int[] res = new int[k];
        int resIndex = 0;
        int p1 = 0;
        int p2 = 0;

        while (resIndex<res.length) {
            res[resIndex++]=findMax(nums1,nums2,p1,p2)?nums1[p1++]:nums2[p2++];// tricky
        }

        return res;
    }

    public int[] maxNumber_1(int[] nums1, int[] nums2, int k) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        int[] maxRes = new int[k];
        for (int i = 0; i <= k; i++) {
            int j = k - i;
            if (i <= len1 && (k - i) <= len2) { // skip invalid cases, imp step!
                int[] maxLex1 = findLexMax(nums1, i);
                int[] maxLex2 = findLexMax(nums2, j);
                int[] res = merge_1(maxLex1, maxLex2, k);
                boolean compareRes = findMax(res, maxRes,0,0);
                if (compareRes) {
                    maxRes = res;
                }
            }
        }
        return maxRes;
    }

    // V2
    // IDEA: GREEDY
    // https://leetcode.com/problems/create-maximum-number/solutions/77285/share-my-greedy-solution-by-dietpepsi-jm2d/
    public int[] maxNumber_2(int[] nums1, int[] nums2, int k) {
        int n = nums1.length;
        int m = nums2.length;
        int[] ans = new int[k];
        for (int i = Math.max(0, k - m); i <= k && i <= n; ++i) {
            int[] candidate = merge(maxArray(nums1, i), maxArray(nums2, k - i), k);
            if (greater(candidate, 0, ans, 0))
                ans = candidate;
        }
        return ans;
    }

    private int[] merge(int[] nums1, int[] nums2, int k) {
        int[] ans = new int[k];
        for (int i = 0, j = 0, r = 0; r < k; ++r)
            ans[r] = greater(nums1, i, nums2, j) ? nums1[i++] : nums2[j++];
        return ans;
    }

    public boolean greater(int[] nums1, int i, int[] nums2, int j) {
        while (i < nums1.length && j < nums2.length && nums1[i] == nums2[j]) {
            i++;
            j++;
        }
        return j == nums2.length || (i < nums1.length && nums1[i] > nums2[j]);
    }

    public int[] maxArray(int[] nums, int k) {
        int n = nums.length;
        int[] ans = new int[k];
        for (int i = 0, j = 0; i < n; ++i) {
            while (n - i + j > k && j > 0 && ans[j - 1] < nums[i])
                j--;
            if (j < k)
                ans[j++] = nums[i];
        }
        return ans;
    }

}
