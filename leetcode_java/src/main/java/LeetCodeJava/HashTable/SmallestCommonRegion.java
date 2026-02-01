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
    // IDEA: HASHMAP + SET (fixed by gpt)
    // TODO: validate
    /**

     * -> if use `Set` of ancestors from `region1`,
     *  how do we ensure that the result is the `smallest` region
     *   that contains both `region1` and `region2`?
     *
     *
     * ---
     *
     * ### ✅ **What “Smallest Region That Contains Both” Means**
     *
     * This is the **lowest common ancestor (LCA)** in a tree, where:
     *
     * * Each region has **exactly one parent** (tree, not DAG)
     * * The smallest common region is the **lowest (i.e., deepest)** region in the hierarchy that contains both `region1` and `region2`
     *
     * ---
     *
     * ### ✅ **Why the Set + Upward Traversal Works**
     *
     * Let’s walk through what the code does:
     *
     * #### Step 1: Build a Set of all ancestors of `region1`
     *
     * ```java
     * while (region1 != null) {
     *     ancestors.add(region1);
     *     region1 = map.get(region1);
     * }
     * ```
     *
     * This gives you a complete path from `region1` to root, e.g.:
     *
     * ```
     * Quebec → Canada → North America → Earth
     * ```
     *
     * #### Step 2: Walk from `region2` up toward the root
     *
     * ```java
     * while (region2 != null) {
     *     if (ancestors.contains(region2)) {
     *         return region2;
     *     }
     *     region2 = map.get(region2);
     * }
     * ```
     *
     * So for example, if `region2 = Texas`, this goes:
     *
     * ```
     * Texas → United States → North America → Earth
     * ```
     *
     * At each step, you’re checking: **is this region an ancestor of `region1`?**
     *
     * * The **first match** will be the **lowest (i.e., closest to leaves)** common ancestor.
     * * This is guaranteed because you're walking **bottom-up** from both `region1` and `region2`.
     *
     * ---
     *
     * ### 🔍 Example Walkthrough
     *
     * #### Tree:
     *
     * ```
     * Earth
     * ├── North America
     * │   ├── United States
     * │   │   ├── California
     * │   │   └── Texas
     * │   └── Canada
     * │       ├── Ontario
     * │       └── Quebec
     * └── South America
     * ```
     *
     * Input:
     *
     * * `region1 = Quebec`
     * * `region2 = Texas`
     *
     * Ancestor set from `region1`:
     *
     * ```
     * [Quebec, Canada, North America, Earth]
     * ```
     *
     * Now walk from `region2 = Texas`:
     *
     * * Is `Texas` in that set? ❌
     * * Is `United States` in that set? ❌
     * * Is `North America` in that set? ✅ → **this is the lowest common ancestor**
     *
     * ✔️ **Correct Answer: "North America"**
     *
     * ---
     *
     * ### ✅ Summary
     *
     * > Using a `Set` of ancestors from one node and walking up from the other **guarantees** the smallest region that contains both — because the first match is the **lowest** shared ancestor in the tree.
     *
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public String findSmallestRegion(List<List<String>> regions, String region1, String region2) {

        if (regions == null || regions.isEmpty()) {
            return null;
        }

        /**
         *  NOTE !!!
         *
         *  Map:  { child : parent }
         *
         */
        Map<String, String> map = new HashMap<>();
        for (List<String> r : regions) {
            String parent = r.get(0);
            for (int i = 1; i < r.size(); i++) {  // ✅ FIXED: loop over region list, not string length
                String child = r.get(i);
                map.put(child, parent);
            }
        }

        // Use Set instead of Queue for O(1) ancestor lookups
        Set<String> ancestors = new HashSet<>();
        while (region1 != null) {
            ancestors.add(region1);
            region1 = map.get(region1);
        }

        // Walk up from region2 and find the first common ancestor
        while (region2 != null) {
            if (ancestors.contains(region2)) {
                return region2;
            }
            region2 = map.get(region2);
        }

        return null; // No common ancestor found (unlikely if input is valid)
    }


    // V0-1
    // IDEA: HASHMAP (fixed by gpt)
    // TODO: validate
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
