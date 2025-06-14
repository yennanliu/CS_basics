package LeetCodeJava.HashTable;

// https://leetcode.com/problems/smallest-common-region/description/
// https://leetcode.ca/all/1257.html

import java.util.*;

/**
 * 1257. Smallest Common Region
 * You are given some lists of regions where the first region of each list includes all other regions in that list.
 *
 * Naturally, if a region X contains another region Y then X is bigger than Y. Also by definition a region X contains itself.
 *
 * Given two regions region1, region2, find out the smallest region that contains both of them.
 *
 * If you are given regions r1, r2 and r3 such that r1 includes r3, it is guaranteed there is no r2 such that r2 includes r3.
 *
 * It's guaranteed the smallest region exists.
 *
 *
 *
 * Example 1:
 *
 * Input:
 * regions = [["Earth","North America","South America"],
 * ["North America","United States","Canada"],
 * ["United States","New York","Boston"],
 * ["Canada","Ontario","Quebec"],
 * ["South America","Brazil"]],
 * region1 = "Quebec",
 * region2 = "New York"
 * Output: "North America"
 *
 *
 * Constraints:
 *
 * 2 <= regions.length <= 10^4
 * region1 != region2
 * All strings consist of English letters and spaces with at most 20 letters.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Airbnb
 * Problem Solution
 * 1257-Smallest-Common-Region
 *
 *
 */
public class SmallestCommonRegion {

    // V0
//    public String findSmallestRegion_1(List<List<String>> regions, String region1, String region2) {
//
//    }

    // V0-1
    // IDEA: HASHMAP (fixed by gpt)
    // TODO: validate
    public String findSmallestRegion_0_1(List<List<String>> regions, String region1, String region2) {

        // Map each region to its parent
        /**
         *  NOTE !!!
         *
         *   map : {child : parent}
         *
         *   -> so the key is child, and the value is its parent
         *
         */
        Map<String, String> parentMap = new HashMap<>();

        for (List<String> regionList : regions) {
            String parent = regionList.get(0);
            for (int i = 1; i < regionList.size(); i++) {
                parentMap.put(regionList.get(i), parent);
            }
        }

        // Track ancestors of region1
        /**  NOTE !!!
         *
         *  we use `set` to track `parents` (ancestors)
         *  if exists, add it to set,
         *  and set `current region` as its `parent`
         *
         */
        Set<String> ancestors = new HashSet<>();
        while (region1 != null) {
            ancestors.add(region1);
            region1 = parentMap.get(region1);
        }

        // Traverse region2’s ancestors until we find one in region1’s ancestor set
        while (!ancestors.contains(region2)) {
            region2 = parentMap.get(region2);
        }

        return region2;
    }

    // V1
    // https://leetcode.ca/2019-05-10-1257-Smallest-Common-Region/
    public String findSmallestRegion_1(List<List<String>> regions, String region1, String region2) {
        Map<String, String> m = new HashMap<>();
        for (List<String> region : regions) {
            for (int i = 1; i < region.size(); ++i) {
                m.put(region.get(i), region.get(0));
            }
        }
        Set<String> s = new HashSet<>();
        while (m.containsKey(region1)) {
            s.add(region1);
            region1 = m.get(region1);
        }
        while (m.containsKey(region2)) {
            if (s.contains(region2)) {
                return region2;
            }
            region2 = m.get(region2);
        }
        return region1;
    }

    // V2

}
