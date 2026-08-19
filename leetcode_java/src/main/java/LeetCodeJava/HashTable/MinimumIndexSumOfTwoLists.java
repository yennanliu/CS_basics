package LeetCodeJava.HashTable;

// https://leetcode.com/problems/minimum-index-sum-of-two-lists/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  599. Minimum Index Sum of Two Lists
 *  Easy
 *
 *  Given two arrays of strings list1 and list2, find the common strings with the
 *  least index sum.
 *
 *  A common string is a string that appeared in both list1 and list2.
 *  A common string with the least index sum is a common string such that if it
 *  appeared at list1[i] and list2[j] then i + j should be the minimum value among
 *  all the other common strings.
 *
 *  Return all the common strings with the least index sum. Return the answer in any order.
 *
 *  Example 1:
 *  Input: list1 = ["Shogun","Tapioca Express","Burger King","KFC"],
 *         list2 = ["Piatti","The Grill at Torrey Pines","Hungry Hunter Steakhouse","Shogun"]
 *  Output: ["Shogun"]
 *
 *  Example 2:
 *  Input: list1 = ["Shogun","Tapioca Express","Burger King","KFC"],
 *         list2 = ["KFC","Shogun","Burger King"]
 *  Output: ["Shogun"]
 *
 *  Constraints:
 *  1 <= list1.length, list2.length <= 1000
 *  1 <= list1[i].length, list2[i].length <= 30
 *  All the strings of list1 / list2 are unique.
 */
public class MinimumIndexSumOfTwoLists {

    // V0
    // IDEA: HASHMAP (string -> index in list1), scan list2 and keep the minimum index sum
    /**
     * time = O(m + n)
     * space = O(m)
     */
    public String[] findRestaurant(String[] list1, String[] list2) {
        Map<String, Integer> idx1 = new HashMap<>();
        for (int i = 0; i < list1.length; i++) {
            idx1.put(list1[i], i);
        }

        List<String> res = new ArrayList<>();
        int best = Integer.MAX_VALUE;
        for (int j = 0; j < list2.length; j++) {
            Integer i = idx1.get(list2[j]);
            if (i == null) {
                continue;
            }
            int sum = i + j;
            if (sum < best) {
                best = sum;
                res.clear();
                res.add(list2[j]);
            } else if (sum == best) {
                res.add(list2[j]);
            }
        }

        return res.toArray(new String[0]);
    }
}
