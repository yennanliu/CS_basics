package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/h-index-ii/

/**
 *  275. H-Index II
 *  Medium
 *
 *  Given an array of integers citations where citations[i] is the number of
 *  citations a researcher received for their ith paper and citations is sorted
 *  in non-decreasing order, return the researcher's h-index.
 *
 *  The h-index is defined as the maximum value of h such that the given
 *  researcher has published at least h papers that have each been cited at
 *  least h times.
 *
 *  You must write an algorithm that runs in logarithmic time.
 *
 *  Example 1:
 *
 *  Input: citations = [0,1,3,5,6]
 *  Output: 3
 *  Explanation: there are 3 papers with at least 3 citations each.
 *
 *  Example 2:
 *
 *  Input: citations = [1,2,100]
 *  Output: 2
 *
 *  Constraints:
 *
 *  n == citations.length
 *  1 <= n <= 10^5
 *  0 <= citations[i] <= 1000
 *  citations is sorted in ascending order.
 */
public class HIndexII {

    // V0
    // IDEA: binary search - at idx i there are (n - i) papers with citations >= citations[i],
    //       so we look for the smallest i with citations[i] >= n - i
    /**
     * time = O(log n)
     * space = O(1)
     */
    public int hIndex(int[] citations) {
        if (citations == null || citations.length == 0) {
            return 0;
        }
        int n = citations.length;
        int l = 0;
        int r = n - 1;
        int res = 0;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            int rem = n - mid; // how many papers have citations >= citations[mid]
            if (citations[mid] >= rem) {
                res = rem;
                r = mid - 1; // NOTE !!! try to make h bigger by moving left
            } else {
                l = mid + 1;
            }
        }
        return res;
    }

    // V1
    // IDEA: linear scan - walk left to right and stop at the first idx where
    //       citations[idx] >= n - idx (that (n - idx) is the h-index)
    /**
     * time = O(n)
     * space = O(1)
     */
    public int hIndex_1(int[] citations) {
        if (citations == null || citations.length == 0) {
            return 0;
        }
        int n = citations.length;
        for (int i = 0; i < n; i++) {
            if (citations[i] >= n - i) {
                return n - i;
            }
        }
        return 0;
    }

    // V2
    // IDEA: counting / bucket sort (the H-Index I trick) - ignores the fact that
    //       the input is sorted, so it also works on unsorted input
    /**
     * time = O(n)
     * space = O(n)
     */
    public int hIndex_2(int[] citations) {
        if (citations == null || citations.length == 0) {
            return 0;
        }
        int n = citations.length;
        // bucket[c] = how many papers have exactly c citations (everything >= n
        // is clamped into bucket[n], since the h-index can never exceed n)
        int[] bucket = new int[n + 1];
        for (int c : citations) {
            bucket[Math.min(c, n)]++;
        }
        int cnt = 0; // papers with citations >= h
        for (int h = n; h >= 0; h--) {
            cnt += bucket[h];
            if (cnt >= h) {
                return h;
            }
        }
        return 0;
    }
}
