package LeetCodeJava.Design;

// https://leetcode.com/problems/insert-delete-getrandom-o1-duplicates-allowed/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 381. Insert Delete GetRandom O(1) - Duplicates allowed
 * Hard
 *
 * RandomizedCollection is a data structure that contains a collection of numbers, possibly
 * duplicates (i.e., a multiset). It should support inserting and removing specific elements
 * and also reporting a random element.
 *
 * Implement the RandomizedCollection class:
 *
 * - RandomizedCollection() Initializes the empty RandomizedCollection object.
 * - bool insert(int val) Inserts an item val into the multiset, even if the item is already
 *   present. Returns true if the item is not present, false otherwise.
 * - bool remove(int val) Removes an item val from the multiset if present. Returns true if
 *   the item is present, false otherwise. Note that if val has multiple occurrences in the
 *   multiset, we only remove one of them.
 * - int getRandom() Returns a random element from the current multiset of elements. The
 *   probability of each element being returned is linearly related to the number of the same
 *   values the multiset contains.
 *
 * You must implement the functions of the class such that each function works on average
 * O(1) time complexity.
 *
 * Note: The test cases are generated such that getRandom will only be called if there is
 * at least one item in the RandomizedCollection.
 *
 *
 * Example 1:
 *
 * Input
 * ["RandomizedCollection", "insert", "insert", "insert", "getRandom", "remove", "getRandom"]
 * [[], [1], [1], [2], [], [1], []]
 * Output
 * [null, true, false, true, 2, true, 1]
 *
 * Explanation
 * RandomizedCollection randomizedCollection = new RandomizedCollection();
 * randomizedCollection.insert(1);   // return true since the collection does not contain 1.
 * randomizedCollection.insert(1);   // return false since the collection contains 1.
 * randomizedCollection.insert(2);   // return true since the collection does not contain 2.
 * randomizedCollection.getRandom(); // return 1 with prob 2/3, or 2 with prob 1/3.
 * randomizedCollection.remove(1);   // return true since the collection contains 1.
 * randomizedCollection.getRandom(); // return 1 or 2, both equally likely.
 *
 *
 * Constraints:
 *
 * -2^31 <= val <= 2^31 - 1
 * At most 2 * 10^5 calls in total will be made to insert, remove, and getRandom.
 * There will be at least one element in the data structure when getRandom is called.
 *
 */
public class InsertDeleteGetRandomDuplicatesAllowed {

    /**
     * Your RandomizedCollection object will be instantiated and called as such:
     * RandomizedCollection obj = new RandomizedCollection();
     * boolean param_1 = obj.insert(val);
     * boolean param_2 = obj.remove(val);
     * int param_3 = obj.getRandom();
     */

    // V0
    // IDEA: FLAT LIST (for O(1) random) + HASHMAP val -> SET OF INDICES (for O(1) remove)
    /**
     *  getRandom needs a DENSE array to sample from, but remove() must be O(1), so we
     *  cannot SHIFT elements. Trick: SWAP the removed slot with the LAST element, then pop.
     *
     *  Because DUPLICATES are allowed, the map value is a SET of indices, not a single index.
     *
     *  NOTE !!! be CAREFUL with the ordering of the index-set updates below: they must
     *           stay correct even when the element being removed IS the last element
     *           (then val == last).
     *
     *  time  = O(1) average for insert / remove / getRandom
     *  space = O(n)
     */
    class RandomizedCollection {

        private List<Integer> vals;            // flat list of the values (O(1) sampling)
        private Map<Integer, Set<Integer>> idx; // val -> set of positions inside vals
        private Random rnd;

        public RandomizedCollection() {
            this.vals = new ArrayList<>();
            this.idx = new HashMap<>();
            this.rnd = new Random();
        }

        public boolean insert(int val) {
            Set<Integer> pos = idx.computeIfAbsent(val, k -> new HashSet<>());
            boolean wasAbsent = pos.isEmpty();
            pos.add(vals.size());
            vals.add(val);
            return wasAbsent;
        }

        public boolean remove(int val) {
            Set<Integer> pos = idx.get(val);
            if (pos == null || pos.isEmpty()) {
                return false;
            }

            int i = pos.iterator().next();          // ANY occurrence of val
            int lastIdx = vals.size() - 1;
            int last = vals.get(lastIdx);

            // move the LAST element into slot i
            vals.set(i, last);
            pos.remove(i);
            idx.computeIfAbsent(last, k -> new HashSet<>()).add(i);

            /** NOTE !!!
             *
             *  slot lastIdx disappears; do this AFTER the add above so the
             *  `val == last and i == lastIdx` case still ends up clean
             */
            idx.get(last).remove(lastIdx);

            vals.remove(lastIdx);
            return true;
        }

        public int getRandom() {
            return vals.get(rnd.nextInt(vals.size()));
        }
    }

}
