package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/merge-two-2d-arrays-by-summing-values/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 2570. Merge Two 2D Arrays by Summing Values
 * Easy
 *
 * You are given two 2D integer arrays nums1 and nums2.
 *
 * nums1[i] = [idi, vali] indicate that the number with the id idi has a value equal to vali.
 * nums2[i] = [idi, vali] indicate that the number with the id idi has a value equal to vali.
 *
 * Each array contains unique ids and is sorted in ascending order by id.
 *
 * Merge the two arrays into one array that is sorted in ascending order by id, respecting the following conditions:
 *
 * Only ids that appear in at least one of the two arrays should be included in the resulting array.
 * Each id should be included only once and its value should be the sum of the values of this id in the two arrays. If the id does not exist in one of the two arrays, then assume its value in that array to be 0.
 *
 * Return the resulting array. The returned array must be sorted in ascending order by id.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [[1,2],[2,3],[4,5]], nums2 = [[1,4],[3,2],[4,1]]
 * Output: [[1,6],[2,3],[3,2],[4,6]]
 * Explanation: The resulting array contains the following:
 * - id = 1, the value of this id is 2 + 4 = 6.
 * - id = 2, the value of this id is 3.
 * - id = 3, the value of this id is 2.
 * - id = 4, the value of this id is 5 + 1 = 6.
 *
 * Example 2:
 *
 * Input: nums1 = [[2,4],[3,6],[5,5]], nums2 = [[1,3],[4,3]]
 * Output: [[1,3],[2,4],[3,6],[4,3],[5,5]]
 * Explanation: There are no common ids, so we just include each id with its value in the resulting list.
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length, nums2.length <= 200
 * nums1[i].length == nums2[j].length == 2
 * 1 <= idi, vali <= 1000
 * Both arrays contain unique ids.
 * Both arrays are in strictly ascending order by id.
 *
 */
public class MergeTwo2DArraysBySummingValues {

    // V0
    // IDEA: TWO POINTERS — THE MERGE STEP OF MERGE SORT
    //  both inputs are sorted by id and ids are unique within each, so one linear merge pass
    //  is enough. compare the two front ids: emit the smaller one as is, or, when they are
    //  equal, emit the summed value and advance BOTH pointers (each id appears once).
    //  after the loop at most one array has leftovers; copy both tails, one is empty.
    /**
     * time = O(N + M)
     * space = O(1)   (besides the output)
     */
    public int[][] mergeArrays(int[][] nums1, int[][] nums2) {
        List<int[]> merged = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < nums1.length && j < nums2.length) {
            int id1 = nums1[i][0];
            int id2 = nums2[j][0];
            if (id1 < id2) {
                merged.add(new int[] {id1, nums1[i][1]});
                i++;
            } else if (id1 > id2) {
                merged.add(new int[] {id2, nums2[j][1]});
                j++;
            } else {
                // shared id: sum the two values
                merged.add(new int[] {id1, nums1[i][1] + nums2[j][1]});
                i++;
                j++;
            }
        }
        // only one of these tails is non-empty
        while (i < nums1.length) {
            merged.add(new int[] {nums1[i][0], nums1[i][1]});
            i++;
        }
        while (j < nums2.length) {
            merged.add(new int[] {nums2[j][0], nums2[j][1]});
            j++;
        }
        return merged.toArray(new int[0][]);
    }
}
