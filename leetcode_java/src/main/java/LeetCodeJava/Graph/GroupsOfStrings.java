package LeetCodeJava.Graph;

// https://leetcode.com/problems/groups-of-strings/description/

import java.util.*;

/**
 * 2157. Groups of Strings
 * Hard
 *
 * You are given a 0-indexed array of strings words. Each string consists of lowercase English letters only. No letter occurs more than once in any string of words.
 *
 * Two strings s1 and s2 are said to be connected if the set of letters of s2 can be obtained from the set of letters of s1 by any one of the following operations:
 *
 * Adding exactly one letter to the set of the letters of s1.
 * Deleting exactly one letter from the set of the letters of s1.
 * Replacing exactly one letter from the set of the letters of s1 with any letter, including itself.
 *
 * The array words can be divided into one or more non-intersecting groups. A string belongs to a group if any one of the following is true:
 *
 * It is connected to at least one other string of the group.
 * It is the only string present in the group.
 *
 * Note that the strings in words should be grouped in such a manner that a string belonging to a group cannot be connected to a string present in any other group. It can be proved that such an arrangement is always unique.
 *
 * Return an array ans of size 2 where:
 *
 * ans[0] is the maximum number of groups words can be divided into, and
 * ans[1] is the size of the largest group.
 *
 *
 * Example 1:
 *
 * Input: words = ["a","b","ab","cde"]
 * Output: [2,3]
 * Explanation:
 * - words[0] can be used to obtain words[1] (by replacing 'a' with 'b'), and words[2] (by adding 'b'). So words[0] is connected to words[1] and words[2].
 * - words[1] can be used to obtain words[0] (by replacing 'b' with 'a'), and words[2] (by adding 'a'). So words[1] is connected to words[0] and words[2].
 * - words[2] can be used to obtain words[0] (by deleting 'b'), and words[1] (by deleting 'a'). So words[2] is connected to words[0] and words[1].
 * - words[3] is not connected to any string in words.
 * Thus, words can be divided into 2 groups ["a","b","ab"] and ["cde"]. The size of the largest group is 3.
 *
 * Example 2:
 *
 * Input: words = ["a","ab","abc"]
 * Output: [1,3]
 * Explanation:
 * - words[0] is connected to words[1].
 * - words[1] is connected to words[0] and words[2].
 * - words[2] is connected to words[1].
 * Since all strings are connected to each other, they should be grouped together.
 * Thus, the size of the largest group is 3.
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 2 * 10^4
 * 1 <= words[i].length <= 26
 * words[i] consists of lowercase English letters only.
 * No letter occurs more than once in any string of words.
 *
 */
public class GroupsOfStrings {

    private static final int ALPHABET = 26;

    // V0
    // IDEA: EACH WORD IS A 26-BIT SET -> UNION-FIND OVER THE DISTINCT MASKS
    //       delete = clear one bit; add = the same edge seen from the other side;
    //       replace = clear bit i, set an unset bit j. Duplicate words share one node,
    //       weighted by how many times they occur.
    /**
     * time = O(N * 26^2 * alpha(N))
     * space = O(N)
     */
    public int[] groupStrings(String[] words) {
        // mask -> how many words have exactly that letter set
        Map<Integer, Integer> maskCount = new HashMap<>();
        for (String word : words) {
            int mask = maskOf(word);
            maskCount.put(mask, maskCount.getOrDefault(mask, 0) + 1);
        }

        // union-find keyed by mask; size counts words (with duplicates), not masks
        Map<Integer, Integer> parent = new HashMap<>();
        Map<Integer, Integer> size = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : maskCount.entrySet()) {
            parent.put(entry.getKey(), entry.getKey());
            size.put(entry.getKey(), entry.getValue());
        }

        for (int mask : maskCount.keySet()) {
            for (int i = 0; i < ALPHABET; i++) {
                // only letters present in this word can be deleted / replaced
                if ((mask & (1 << i)) == 0) {
                    continue;
                }
                // delete letter i (this edge also covers "add", which is its inverse)
                int deleted = mask ^ (1 << i);
                if (parent.containsKey(deleted)) {
                    union(parent, size, mask, deleted);
                }
                // replace letter i with any letter j not already in the word
                for (int j = 0; j < ALPHABET; j++) {
                    if ((mask & (1 << j)) != 0) {
                        continue;
                    }
                    int replaced = deleted | (1 << j);
                    if (parent.containsKey(replaced)) {
                        union(parent, size, mask, replaced);
                    }
                }
            }
        }

        // count the roots and the largest component
        int groups = 0;
        int largest = 0;
        for (int mask : maskCount.keySet()) {
            if (find(parent, mask) == mask) {
                groups++;
                largest = Math.max(largest, size.get(mask));
            }
        }
        return new int[] { groups, largest };
    }

    /** Bit i is set when letter ('a' + i) occurs in the word. */
    private int maskOf(String word) {
        int mask = 0;
        for (int i = 0; i < word.length(); i++) {
            mask |= 1 << (word.charAt(i) - 'a');
        }
        return mask;
    }

    /** Root of x, with path halving to keep the trees shallow. */
    private int find(Map<Integer, Integer> parent, int x) {
        while (parent.get(x) != x) {
            int grandParent = parent.get(parent.get(x));
            parent.put(x, grandParent);
            x = grandParent;
        }
        return x;
    }

    /** Merges the components of a and b, adding a's word count into b's root. */
    private void union(Map<Integer, Integer> parent, Map<Integer, Integer> size, int a, int b) {
        int rootA = find(parent, a);
        int rootB = find(parent, b);
        if (rootA == rootB) {
            return;
        }
        parent.put(rootA, rootB);
        size.put(rootB, size.get(rootB) + size.get(rootA));
    }
}
