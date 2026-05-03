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
