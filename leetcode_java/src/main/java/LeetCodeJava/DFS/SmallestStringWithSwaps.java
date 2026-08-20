package LeetCodeJava.DFS;

// https://leetcode.com/problems/smallest-string-with-swaps/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  1202. Smallest String With Swaps
 *  Medium
 *
 *  You are given a string s, and an array of pairs of indices in the string
 *  pairs where pairs[i] = [a, b] indicates 2 indices (0-indexed) of the string.
 *
 *  You can swap the characters at any pair of indices in the given pairs any
 *  number of times.
 *
 *  Return the lexicographically smallest string that s can be changed to after
 *  using the swaps.
 *
 *  Example 1:
 *    Input: s = "dcab", pairs = [[0,3],[1,2]]
 *    Output: "bacd"
 *
 *  Example 2:
 *    Input: s = "dcab", pairs = [[0,3],[1,2],[0,2]]
 *    Output: "abcd"
 *
 *  Constraints:
 *    1 <= s.length <= 10^5
 *    0 <= pairs.length <= 10^5
 *    0 <= pairs[i][0], pairs[i][1] < s.length
 *    s only contains lower case English letters.
 */
public class SmallestStringWithSwaps {

    private int[] parent;

    // V0
    // IDEA: UNION FIND (connected components)
    //       if indices i and j are connected (directly or transitively through
    //       pairs) then the characters at those indices can be rearranged
    //       ARBITRARILY inside that component - the swaps generate the full
    //       permutation group.
    //       so: group the indices by component, sort each component's characters,
    //       and write them back into the component's indices in ascending order.
    //       counting sort over the 26 letters keeps each component linear.
    /**
     * time = O(26 * N + M)   // N = s.length, M = pairs.size()
     * space = O(N)
     */
    public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        int n = s.length();
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
        for (List<Integer> p : pairs) {
            int ra = find(p.get(0)), rb = find(p.get(1));
            if (ra != rb) {
                parent[ra] = rb;
            }
        }

        // root -> the indices it owns (already ascending, we scan i upwards)
        Map<Integer, List<Integer>> groups = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int r = find(i);
            List<Integer> g = groups.get(r);
            if (g == null) {
                g = new ArrayList<>();
                groups.put(r, g);
            }
            g.add(i);
        }

        char[] res = s.toCharArray();
        for (List<Integer> idxs : groups.values()) {
            int[] cnt = new int[26];
            for (int i : idxs) {
                cnt[s.charAt(i) - 'a']++;
            }
            int c = 0;
            for (int i : idxs) {
                while (cnt[c] == 0) {
                    c++;
                }
                cnt[c]--;
                res[i] = (char) ('a' + c);
            }
        }

        return new String(res);
    }

    private int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }
}
