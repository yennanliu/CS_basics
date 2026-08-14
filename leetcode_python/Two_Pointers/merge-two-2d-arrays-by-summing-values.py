"""

2570. Merge Two 2D Arrays by Summing Values
Easy

You are given two 2D integer arrays nums1 and nums2.

nums1[i] = [idi, vali] indicate that the number with the id idi has a value equal to vali.
nums2[i] = [idi, vali] indicate that the number with the id idi has a value equal to vali.

Each array contains unique ids and is sorted in ascending order by id.

Merge the two arrays into one array that is sorted in ascending order by id, respecting the following conditions:

Only ids that appear in at least one of the two arrays should be included in the resulting array.
Each id should be included only once and its value should be the sum of the values of this id in the two arrays. If the id does not exist in one of the two arrays, then assume its value in that array to be 0.

Return the resulting array. The returned array must be sorted in ascending order by id.


Example 1:

Input: nums1 = [[1,2],[2,3],[4,5]], nums2 = [[1,4],[3,2],[4,1]]
Output: [[1,6],[2,3],[3,2],[4,6]]
Explanation: The resulting array contains the following:
- id = 1, the value of this id is 2 + 4 = 6.
- id = 2, the value of this id is 3.
- id = 3, the value of this id is 2.
- id = 4, the value of this id is 5 + 1 = 6.

Example 2:

Input: nums1 = [[2,4],[3,6],[5,5]], nums2 = [[1,3],[4,3]]
Output: [[1,3],[2,4],[3,6],[4,3],[5,5]]
Explanation: There are no common ids, so we just include each id with its value in the resulting list.


Constraints:

1 <= nums1.length, nums2.length <= 200
nums1[i].length == nums2[j].length == 2
1 <= idi, vali <= 1000
Both arrays contain unique ids.
Both arrays are in strictly ascending order by id.

"""

# V0
# IDEA : TWO POINTERS (classic merge step of merge-sort)
#
#   Both inputs are already sorted by id and ids are unique WITHIN each array,
#   so a single linear merge pass is enough - no hash map, no re-sorting.
#
#   Walk pointers i, j over nums1, nums2 and at each step compare the two
#   front ids:
#     - id1 <  id2 -> only nums1 has it, emit as is, advance i
#     - id1 >  id2 -> only nums2 has it, emit as is, advance j
#     - id1 == id2 -> shared id, emit the SUM, advance BOTH
#
#   NOTE : the equal case must advance both pointers, otherwise the id gets
#          emitted twice (the problem requires each id exactly once).
#
#   NOTE : after the main loop exactly one array can still have leftovers -
#          append both tails unconditionally, one of them is empty.
#
# time = O(n + m), space = O(1) beyond the output
class Solution(object):
    def mergeArrays(self, nums1, nums2):
        res = []
        i, j = 0, 0
        n, m = len(nums1), len(nums2)
        while i < n and j < m:
            id1, v1 = nums1[i]
            id2, v2 = nums2[j]
            if id1 < id2:
                res.append([id1, v1])
                i += 1
            elif id1 > id2:
                res.append([id2, v2])
                j += 1
            else:
                res.append([id1, v1 + v2])
                i += 1
                j += 1
        # exactly one of these tails is non-empty
        while i < n:
            res.append([nums1[i][0], nums1[i][1]])
            i += 1
        while j < m:
            res.append([nums2[j][0], nums2[j][1]])
            j += 1
        return res
