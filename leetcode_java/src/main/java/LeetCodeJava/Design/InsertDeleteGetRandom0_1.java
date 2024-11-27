package LeetCodeJava.Design;

// https://leetcode.com/problems/insert-delete-getrandom-o1/description/

import java.util.*;

/**
 * 380. Insert Delete GetRandom O(1)
 * Solved
 * Medium
 * Topics
 * Companies
 * Implement the RandomizedSet class:
 *
 * RandomizedSet() Initializes the RandomizedSet object.
 * bool insert(int val) Inserts an item val into the set if not present. Returns true if the item was not present, false otherwise.
 * bool remove(int val) Removes an item val from the set if present. Returns true if the item was present, false otherwise.
 * int getRandom() Returns a random element from the current set of elements (it's guaranteed that at least one element exists when this method is called). Each element must have the same probability of being returned.
 * You must implement the functions of the class such that each function works in average O(1) time complexity.
 *
 *
 *
 * Example 1:
 *
 * Input
 * ["RandomizedSet", "insert", "remove", "insert", "getRandom", "remove", "insert", "getRandom"]
 * [[], [1], [2], [2], [], [1], [2], []]
 * Output
 * [null, true, false, true, 2, true, false, 2]
 *
 * Explanation
 * RandomizedSet randomizedSet = new RandomizedSet();
 * randomizedSet.insert(1); // Inserts 1 to the set. Returns true as 1 was inserted successfully.
 * randomizedSet.remove(2); // Returns false as 2 does not exist in the set.
 * randomizedSet.insert(2); // Inserts 2 to the set, returns true. Set now contains [1,2].
 * randomizedSet.getRandom(); // getRandom() should return either 1 or 2 randomly.
 * randomizedSet.remove(1); // Removes 1 from the set, returns true. Set now contains [2].
 * randomizedSet.insert(2); // 2 was already in the set, so return false.
 * randomizedSet.getRandom(); // Since 2 is the only number in the set, getRandom() will always return 2.
 *
 *
 * Constraints:
 *
 * -231 <= val <= 231 - 1
 * At most 2 * 105 calls will be made to insert, remove, and getRandom.
 * There will be at least one element in the data structure when getRandom is called.
 *
 */
public class InsertDeleteGetRandom0_1 {

    /**
     * Your RandomizedSet object will be instantiated and called as such:
     * RandomizedSet obj = new RandomizedSet();
     * boolean param_1 = obj.insert(val);
     * boolean param_2 = obj.remove(val);
     * int param_3 = obj.getRandom();
     */

    // V0
    // TODO : implement
    class RandomizedSet {

        /** NOTE !!! we use map as storage structure */
        Map<Integer, Integer> map;
        int count;
        Random random;

        public RandomizedSet() {
            this.map = new HashMap<>();
            this.count = 0;
            this.random = new Random();
        }

        public boolean insert(int val) {
            if (!this.map.containsKey(val)){
                this.map.put(val, 1);
                this.count += 1;
                return true;
            }
            return false;
        }

        public boolean remove(int val) {
            if (this.map.containsKey(val)){
                this.map.remove(val);
                this.count -= 1;
                return true;
            }
            return false;
        }

        public int getRandom() {
            int randomIdx = random.nextInt(this.count);
            // java get hashMap key as array
            // https://stackoverflow.com/questions/16203880/get-array-of-maps-keys
            Integer[] keyArray = this.map.keySet().toArray(new Integer[this.map.keySet().size()]);
            return keyArray[randomIdx];
        }
    }

    // V1
    // IDEA : LIST + MAP (gpt)
    class RandomizedSet_1 {

        /** Storage structure for elements and their indices */
        private Map<Integer, Integer> map; // Maps value to its index in the list
        private List<Integer> list; // Stores elements for constant-time access
        private Random random;

        public RandomizedSet_1() {
            this.map = new HashMap<>();
            this.list = new ArrayList<>();
            this.random = new Random();
        }

        public boolean insert(int val) {
            if (!this.map.containsKey(val)) {
                this.list.add(val); // Add value to the list
                this.map.put(val, this.list.size() - 1); // Map value to its index in the list
                return true;
            }
            return false; // Value already exists
        }

        public boolean remove(int val) {
            if (this.map.containsKey(val)) {
                int idx = this.map.get(val); // Index of the value to remove
                int lastElement = this.list.get(this.list.size() - 1); // Last element in the list

                // Swap the last element with the element to remove
                this.list.set(idx, lastElement);
                this.map.put(lastElement, idx);

                // Remove the last element
                this.list.remove(this.list.size() - 1);
                this.map.remove(val);

                return true;
            }
            return false; // Value does not exist
        }

        public int getRandom() {
            int randomIdx = this.random.nextInt(this.list.size());
            return this.list.get(randomIdx); // Return a random element from the list
        }
    }

    // V2
}
