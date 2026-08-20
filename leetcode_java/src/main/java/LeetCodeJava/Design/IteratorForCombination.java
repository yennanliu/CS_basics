package LeetCodeJava.Design;

// https://leetcode.com/problems/iterator-for-combination/

import java.util.ArrayList;
import java.util.List;

/**
 *  1286. Iterator for Combination
 *  Medium
 *
 *  Design the CombinationIterator class:
 *    CombinationIterator(String characters, int combinationLength) Initializes the
 *      object with a string characters of sorted distinct lowercase English letters
 *      and a number combinationLength as arguments.
 *    String next() Returns the next combination of length combinationLength in
 *      lexicographical order.
 *    boolean hasNext() Returns true if and only if there exists a next combination.
 *
 *  Example 1:
 *    Input
 *      ["CombinationIterator","next","hasNext","next","hasNext","next","hasNext"]
 *      [["abc", 2], [], [], [], [], [], []]
 *    Output
 *      [null, "ab", true, "ac", true, "bc", false]
 *
 *  Constraints:
 *    1 <= combinationLength <= characters.length <= 15
 *    All the characters of characters are unique.
 *    At most 10^4 calls will be made to next and hasNext.
 *    It is guaranteed that all calls of the function next are valid.
 */
public class IteratorForCombination {

    // V0
    // IDEA: PRE-COMPUTE ALL COMBINATIONS VIA BACKTRACK (DFS), THEN JUST WALK A CURSOR
    //
    //       characters.length <= 15, so there are at most C(15, 7) = 6435
    //       combinations -- cheap enough to generate them ALL up front. the DFS
    //       always extends with a LATER index, so they come out in lexicographical
    //       order for free and next() / hasNext() are a plain cursor over the list.
    /**
     * time = O(C(n, k) * k) for the constructor, O(1) for next / hasNext
     * space = O(C(n, k) * k)
     */
    private final List<String> combs;
    private int idx;

    public IteratorForCombination(String characters, int combinationLength) {
        this.combs = new ArrayList<>();
        this.idx = 0;
        build(characters, combinationLength, 0, new StringBuilder());
    }

    public String next() {
        return combs.get(idx++);
    }

    public boolean hasNext() {
        return idx < combs.size();
    }

    private void build(String chars, int k, int start, StringBuilder cur) {
        if (cur.length() == k) {
            combs.add(cur.toString());
            return;
        }
        for (int i = start; i < chars.length(); i++) {
            cur.append(chars.charAt(i));
            build(chars, k, i + 1, cur);
            cur.deleteCharAt(cur.length() - 1);
        }
    }
}
