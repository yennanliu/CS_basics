package LeetCodeJava.Design;

// https://leetcode.com/problems/design-a-number-container-system/

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 *  2349. Design a Number Container System
 *  Medium
 *
 *  Design a number container system that can do the following:
 *   - Insert or Replace a number at the given index in the system.
 *   - Return the smallest index for the given number in the system.
 *
 *  Implement the NumberContainers class:
 *
 *   - NumberContainers() Initializes the number container system.
 *   - void change(int index, int number) Fills the container at index with the number.
 *     If there is already a number at that index, replace it.
 *   - int find(int number) Returns the smallest index for the given number, or -1 if
 *     there is no index that is filled by number in the system.
 *
 *  Example 1:
 *    Input
 *      ["NumberContainers","find","change","change","change","change","find","change","find"]
 *      [[],[10],[2,10],[1,10],[3,10],[5,10],[10],[1,20],[10]]
 *    Output
 *      [null,-1,null,null,null,null,1,null,2]
 *    Explanation
 *      find(10)     -> -1, nothing holds 10 yet
 *      after filling indices 2, 1, 3, 5 with 10, find(10) -> 1
 *      change(1, 20) overwrites index 1, so find(10) -> 2
 *
 *  Constraints:
 *    1 <= index, number <= 10^9
 *    At most 10^5 calls will be made in total to change and find.
 */
public class DesignANumberContainerSystem {

    // V0
    // IDEA: TWO MAPS -- index -> number, and number -> ORDERED SET of indices
    //
    //   idxToNum is the source of truth for what sits at an index.
    //   numToIdx keeps, per number, a TreeSet of the indices currently holding it,
    //   so find(number) is just first() on that set.
    //
    //   change() must UNREGISTER the index from its previous number first,
    //   otherwise a stale index would still answer find() for the old number.
    //   emptied sets are dropped so find() can rely on a null/empty check.
    /**
     * time = O(log N) per change / find
     * space = O(N)
     */
    private final Map<Integer, Integer> idxToNum = new HashMap<>();
    private final Map<Integer, TreeSet<Integer>> numToIdx = new HashMap<>();

    public DesignANumberContainerSystem() {
    }

    public void change(int index, int number) {
        Integer old = idxToNum.get(index);
        if (old != null) {
            TreeSet<Integer> prev = numToIdx.get(old);
            if (prev != null) {
                prev.remove(index);
                if (prev.isEmpty()) {
                    numToIdx.remove(old);
                }
            }
        }
        idxToNum.put(index, number);
        TreeSet<Integer> set = numToIdx.get(number);
        if (set == null) {
            set = new TreeSet<>();
            numToIdx.put(number, set);
        }
        set.add(index);
    }

    public int find(int number) {
        TreeSet<Integer> set = numToIdx.get(number);
        if (set == null || set.isEmpty()) {
            return -1;
        }
        return set.first();
    }
}
