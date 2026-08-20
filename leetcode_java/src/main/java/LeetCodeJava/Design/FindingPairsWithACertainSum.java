package LeetCodeJava.Design;

// https://leetcode.com/problems/finding-pairs-with-a-certain-sum/

import java.util.HashMap;
import java.util.Map;

/**
 *  1865. Finding Pairs With a Certain Sum
 *  Medium
 *
 *  You are given two integer arrays nums1 and nums2. You are tasked to implement a
 *  data structure that supports queries of two types:
 *    - Add a positive integer to an element of a given index in the array nums2.
 *    - Count the number of pairs (i, j) such that nums1[i] + nums2[j] equals a
 *      given value (0 <= i < nums1.length and 0 <= j < nums2.length).
 *
 *  Implement the FindSumPairs class:
 *    FindSumPairs(int[] nums1, int[] nums2) Initializes the object with the two
 *      integer arrays nums1 and nums2.
 *    void add(int index, int val) Adds val to nums2[index], i.e. nums2[index] += val.
 *    int count(int tot) Returns the number of pairs (i, j) such that
 *      nums1[i] + nums2[j] == tot.
 *
 *  Example 1:
 *    Input
 *      ["FindSumPairs","count","add","count","count","add","add","count"]
 *      [[[1,1,2,2,2,3],[1,4,5,2,5,4]],[7],[3,2],[8],[4],[0,1],[1,1],[7]]
 *    Output
 *      [null, 8, null, 2, 1, null, null, 11]
 *    Explanation
 *      count(7) -> 8
 *      add(3,2) -> nums2 = [1,4,5,4,5,4]
 *      count(8) -> 2 ; count(4) -> 1
 *      add(0,1), add(1,1) -> nums2 = [2,5,5,4,5,4]
 *      count(7) -> 11
 *
 *  Constraints:
 *    1 <= nums1.length <= 1000
 *    1 <= nums2.length <= 10^5
 *    1 <= nums1[i] <= 10^9
 *    1 <= nums2[i] <= 10^5
 *    0 <= index < nums2.length
 *    1 <= val <= 10^5
 *    1 <= tot <= 10^9
 *    At most 1000 calls are made to add and count each.
 */
public class FindingPairsWithACertainSum {

    // V0
    // IDEA: HASH COUNTER ON THE *BIG* ARRAY, LOOP OVER THE *SMALL* ONE
    //
    //       the asymmetry in the constraints is the whole trick:
    //         nums1 <= 1000    (small, never mutated)
    //         nums2 <= 10^5    (big, mutated by add)
    //       so keep a frequency map of nums2 (updated in O(1) on add), and answer
    //       count(tot) by scanning nums1 and summing freq2[tot - nums1[i]].
    //       that is O(|nums1|) = O(1000) per query -- fine for 1000 queries,
    //       whereas scanning the big array (or rebuilding a map of it) would not be.
    /**
     * time = O(n1 + n2) init, O(1) per add, O(n1) per count
     * space = O(n2)
     */
    private final int[] nums1;
    private final int[] nums2;
    private final Map<Integer, Integer> freq2;

    public FindingPairsWithACertainSum(int[] nums1, int[] nums2) {
        this.nums1 = nums1;
        this.nums2 = nums2;
        this.freq2 = new HashMap<>();
        for (int x : nums2) {
            bump(x, 1);
        }
    }

    public void add(int index, int val) {
        bump(nums2[index], -1);
        nums2[index] += val;
        bump(nums2[index], 1);
    }

    public int count(int tot) {
        int res = 0;
        for (int x : nums1) {
            Integer c = freq2.get(tot - x);
            if (c != null) {
                res += c;
            }
        }
        return res;
    }

    private void bump(int key, int d) {
        Integer old = freq2.get(key);
        int nv = (old == null ? 0 : old) + d;
        if (nv <= 0) {
            freq2.remove(key);
        } else {
            freq2.put(key, nv);
        }
    }
}
