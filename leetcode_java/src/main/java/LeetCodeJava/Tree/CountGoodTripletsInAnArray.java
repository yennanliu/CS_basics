package LeetCodeJava.Tree;

// https://leetcode.com/problems/count-good-triplets-in-an-array/

/**
 *  2179. Count Good Triplets in an Array
 *  Hard
 *
 *  You are given two 0-indexed arrays nums1 and nums2 of length n, both of which
 *  are permutations of [0, 1, ..., n - 1].
 *
 *  A good triplet is a set of 3 distinct values which are present in increasing
 *  order by position both in nums1 and nums2. In other words, if we consider
 *  pos1v as the index of the value v in nums1 and pos2v as the index of the value
 *  v in nums2, then a good triplet will be a set (x, y, z) where
 *  0 <= x, y, z <= n - 1, such that pos1x < pos1y < pos1z and
 *  pos2x < pos2y < pos2z.
 *
 *  Return the total number of good triplets.
 *
 *  Example 1:
 *    Input: nums1 = [2,0,1,3], nums2 = [0,1,2,3]
 *    Output: 1
 *    Explanation: only the triplet (0,1,3) is increasing by position in both.
 *
 *  Example 2:
 *    Input: nums1 = [4,0,1,3,2], nums2 = [4,1,0,2,3]
 *    Output: 4
 *    Explanation: the 4 good triplets are (4,0,3), (4,0,2), (4,1,3), (4,1,2).
 *
 *  Constraints:
 *    n == nums1.length == nums2.length
 *    3 <= n <= 10^5
 *    0 <= nums1[i], nums2[i] <= n - 1
 *    nums1 and nums2 are permutations of [0, 1, ..., n - 1].
 */
public class CountGoodTripletsInAnArray {

    // V0
    // IDEA: BIT (FENWICK) over positions in nums2, count each value as the
    //       "MIDDLE" element of the triplet.
    //       map every value to its index in nums2: pos[v]. sweep nums1 left ->
    //       right, so everything already inserted is to the LEFT in nums1.
    //       fixing the current value as the middle y:
    //         left  = # inserted values with pos2 < pos2[y]        -> valid x
    //         right = # not-yet-inserted values with pos2 > pos2[y] -> valid z
    //       res += left * right.
    //       NOTE: right = (n - p) - (inserted - left), i.e. total values after
    //             y in nums2 minus the already inserted ones after y.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long goodTriplets(int[] nums1, int[] nums2) {
        int n = nums1.length;
        int[] pos = new int[n];
        for (int i = 0; i < n; i++) {
            pos[nums2[i]] = i + 1; // 1-indexed for the BIT
        }

        int[] tree = new int[n + 1];
        long res = 0;
        for (int k = 0; k < n; k++) {
            int p = pos[nums1[k]];
            long left = query(tree, p);
            long right = (n - p) - (k - left);
            res += left * right;
            update(tree, p, n);
        }
        return res;
    }

    private void update(int[] tree, int i, int n) {
        while (i <= n) {
            tree[i] += 1;
            i += i & (-i);
        }
    }

    private int query(int[] tree, int i) {
        int s = 0;
        while (i > 0) {
            s += tree[i];
            i -= i & (-i);
        }
        return s;
    }
}
