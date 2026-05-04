package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/max-chunks-to-make-sorted-ii/description/

import java.util.*;

/**
 * 768. Max Chunks To Make Sorted II
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an integer array arr.
 *
 * We split arr into some number of chunks (i.e., partitions), and individually sort each chunk. After concatenating them, the result should equal the sorted array.
 *
 * Return the largest number of chunks we can make to sort the array.
 *
 *
 *
 * Example 1:
 *
 * Input: arr = [5,4,3,2,1]
 * Output: 1
 * Explanation:
 * Splitting into two or more chunks will not return the required result.
 * For example, splitting into [5, 4], [3, 2, 1] will result in [4, 5, 1, 2, 3], which isn't sorted.
 * Example 2:
 *
 * Input: arr = [2,1,3,4,4]
 * Output: 4
 * Explanation:
 * We can split into two chunks, such as [2, 1], [3, 4, 4].
 * However, splitting into [2, 1], [3], [4], [4] is the highest number of chunks possible.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 2000
 * 0 <= arr[i] <= 108
 *
 *
 */
public class MaxChunksToMakeSorted2 {

    // V0
//    public int maxChunksToSorted(int[] arr) {
//
//    }


    // V0-1
    // IDEA: Prefix Sum Matching (GPT)
    public int maxChunksToSorted_0_1(int[] arr) {
        int[] sorted = arr.clone();
        Arrays.sort(sorted);

        int chunks = 0;
        long sum1 = 0, sum2 = 0;

        for (int i = 0; i < arr.length; i++) {
            sum1 += arr[i];
            sum2 += sorted[i];

            if (sum1 == sum2) {
                chunks++;
            }
        }

        return chunks;
    }


    // V0-2
    // IDEA: Prefix Sum Matching (GEMINI)
    /**
     *  ->
     *  The robust logic here is to compare the running
     *  maximum of the original array with
     *  the suffix minimum of the remaining array.
     *
     *
     */
    public int maxChunksToSorted_0_2(int[] arr) {
        int n = arr.length;
        if (n == 0)
            return 0;

        // minRight[i] stores the minimum value from arr[i] to arr[n-1]
        int[] minRight = new int[n];
        minRight[n - 1] = arr[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            minRight[i] = Math.min(minRight[i + 1], arr[i]);
        }

        int chunks = 0;
        int maxLeft = Integer.MIN_VALUE;

        // We iterate up to n-1. We can cut after index i if:
        // Max of everything to the left <= Min of everything to the right
        for (int i = 0; i < n - 1; i++) {
            maxLeft = Math.max(maxLeft, arr[i]);
            if (maxLeft <= minRight[i + 1]) {
                chunks++;
            }
        }

        // The last chunk always completes at the end of the array
        return chunks + 1;
    }





    // V1-1
    // IDEA: SLIDE WINDOW
    // https://leetcode.com/problems/max-chunks-to-make-sorted-ii/editorial/
    public int maxChunksToSorted_1_1(int[] arr) {
        Map<Integer, Integer> count = new HashMap();
        int ans = 0, nonzero = 0;

        int[] expect = arr.clone();
        Arrays.sort(expect);

        for (int i = 0; i < arr.length; ++i) {
            int x = arr[i], y = expect[i];

            count.put(x, count.getOrDefault(x, 0) + 1);
            if (count.get(x) == 0)
                nonzero--;
            if (count.get(x) == 1)
                nonzero++;

            count.put(y, count.getOrDefault(y, 0) - 1);
            if (count.get(y) == -1)
                nonzero++;
            if (count.get(y) == 0)
                nonzero--;

            if (nonzero == 0)
                ans++;
        }

        return ans;
    }



    // V1-2
    // IDEA: Sorted Count Pairs
    // https://leetcode.com/problems/max-chunks-to-make-sorted-ii/editorial/
    class Pair {
        int val, count;

        Pair(int v, int c) {
            val = v;
            count = c;
        }

        int compare(Pair that) {
            return this.val != that.val ? this.val - that.val : this.count - that.count;
        }
    }

    public int maxChunksToSorted_1_2(int[] arr) {
        Map<Integer, Integer> count = new HashMap();
        List<Pair> counted = new ArrayList();
        for (int x : arr) {
            count.put(x, count.getOrDefault(x, 0) + 1);
            counted.add(new Pair(x, count.get(x)));
        }

        List<Pair> expect = new ArrayList(counted);
        Collections.sort(expect, (a, b) -> a.compare(b));

        Pair cur = counted.get(0);
        int ans = 0;
        for (int i = 0; i < arr.length; ++i) {
            Pair X = counted.get(i), Y = expect.get(i);
            if (X.compare(cur) > 0)
                cur = X;
            if (cur.compare(Y) == 0)
                ans++;
        }

        return ans;
    }




    // V2





}
