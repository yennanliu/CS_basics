package LeetCodeJava.DFS;

// https://leetcode.com/problems/pyramid-transition-matrix/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  756. Pyramid Transition Matrix
 *  Medium
 *
 *  You are stacking blocks to form a pyramid. Each block has a color, which is represented by
 *  a single letter. Each row of blocks contains one less block than the row beneath it and is
 *  centered on top.
 *
 *  To make the pyramid aesthetically pleasing, there are only specific triangular patterns that
 *  are allowed. A triangular pattern consists of a single block stacked on top of two blocks.
 *  The patterns are given as a list of three-letter strings allowed, where the first two
 *  characters of a pattern represent the left and right bottom blocks respectively, and the
 *  third character is the top block.
 *
 *  You start with a bottom row of blocks bottom, given as a single string. You are also given a
 *  list of allowed triangular patterns allowed. Return true if you can build the pyramid all the
 *  way to the top such that every triangular pattern in the pyramid is in allowed, or false
 *  otherwise.
 *
 *  Example 1:
 *  Input: bottom = "BCD", allowed = ["BCC","CDE","CEA","FFF"]
 *  Output: true
 *
 *  Example 2:
 *  Input: bottom = "AAAA", allowed = ["AAB","AAC","BCD","BBE","DEF"]
 *  Output: false
 *
 *  Constraints:
 *  2 <= bottom.length <= 6
 *  0 <= allowed.length <= 216
 *  allowed[i].length == 3
 *  The letters in all input strings are from the set {'A', 'B', 'C', 'D', 'E', 'F'}.
 *  All the values of allowed are unique.
 */
public class PyramidTransitionMatrix {

    // V0
    // IDEA: BACKTRACK level by level - build the next row char by char from the pair below,
    //       when the next row is complete recurse on it as the new bottom
    /**
     * time = O(a^b)   // a = max #tops per pair, b = bottom length
     * space = O(b^2)
     */
    public boolean pyramidTransition(String bottom, List<String> allowed) {
        Map<String, List<Character>> map = new HashMap<>();
        for (String s : allowed) {
            String key = s.substring(0, 2);
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<Character>());
            }
            map.get(key).add(s.charAt(2));
        }
        return helper(bottom, new StringBuilder(), map);
    }

    private boolean helper(String cur, StringBuilder above, Map<String, List<Character>> map) {
        // reached the apex
        if (cur.length() == 1) {
            return true;
        }
        // the row above is complete -> it becomes the new bottom
        if (above.length() == cur.length() - 1) {
            return helper(above.toString(), new StringBuilder(), map);
        }
        int pos = above.length();
        String pair = cur.substring(pos, pos + 2);
        List<Character> tops = map.get(pair);
        if (tops == null) {
            return false;
        }
        for (Character c : tops) {
            above.append(c);
            if (helper(cur, above, map)) {
                return true;
            }
            above.deleteCharAt(above.length() - 1);
        }
        return false;
    }
}
