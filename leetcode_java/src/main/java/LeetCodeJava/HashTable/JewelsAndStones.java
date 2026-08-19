package LeetCodeJava.HashTable;

// https://leetcode.com/problems/jewels-and-stones/

import java.util.HashSet;
import java.util.Set;

/**
 *  771. Jewels and Stones
 *  Easy
 *
 *  You're given strings jewels representing the types of stones that are jewels,
 *  and stones representing the stones you have. Each character in stones is a type
 *  of stone you have. You want to know how many of the stones you have are also jewels.
 *
 *  Letters are case sensitive, so "a" is considered a different type of stone from "A".
 *
 *  Example 1:
 *  Input: jewels = "aA", stones = "aAAbbbb"
 *  Output: 3
 *
 *  Example 2:
 *  Input: jewels = "z", stones = "ZZ"
 *  Output: 0
 *
 *  Constraints:
 *  1 <= jewels.length, stones.length <= 50
 *  jewels and stones consist of only English letters.
 *  All the characters of jewels are unique.
 */
public class JewelsAndStones {

    // V0
    // IDEA: put jewel types in a HashSet, then count stones contained in it
    /**
     * time = O(n + m)
     * space = O(m)
     */
    public int numJewelsInStones(String jewels, String stones) {
        Set<Character> jewelSet = new HashSet<>();
        for (char c : jewels.toCharArray()) {
            jewelSet.add(c);
        }

        int res = 0;
        for (char c : stones.toCharArray()) {
            if (jewelSet.contains(c)) {
                res++;
            }
        }
        return res;
    }
}
