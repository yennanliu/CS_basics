package LeetCodeJava.HashTable;

// https://leetcode.com/problems/find-anagram-mappings/

import java.util.HashMap;
import java.util.Map;

/**
 *  760. Find Anagram Mappings
 *  Easy
 *
 *  You are given two integer arrays nums1 and nums2 where nums2 is an anagram of nums1.
 *  Both arrays may contain duplicates.
 *
 *  Return an index mapping array mapping from nums1 to nums2 where mapping[i] = j
 *  means the ith element in nums1 appears in nums2 at index j.
 *  If there are multiple answers, return any of them.
 *
 *  Example 1:
 *  Input: nums1 = [12,28,46,32,50], nums2 = [50,12,32,46,28]
 *  Output: [1,4,3,2,0]
 *
 *  Example 2:
 *  Input: nums1 = [84,46], nums2 = [84,46]
 *  Output: [0,1]
 *
 *  Constraints:
 *  1 <= nums1.length <= 100
 *  nums2.length == nums1.length
 *  0 <= nums1[i], nums2[i] <= 10^5
 *  nums2 is an anagram of nums1.
 */
public class FindAnagramMappings {

    // V0
    // IDEA: HASHMAP value -> (an) index in nums2; duplicates may map to any matching index
    /**
     * time = O(n)
     * space = O(n)
     */
    public int[] anagramMappings(int[] nums1, int[] nums2) {
        Map<Integer, Integer> idx = new HashMap<>();
        for (int j = 0; j < nums2.length; j++) {
            idx.put(nums2[j], j);
        }

        int[] res = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            res[i] = idx.get(nums1[i]);
        }
        return res;
    }
}
