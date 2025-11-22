package LeetCodeJava.Design;

// https://leetcode.com/problems/design-hashset/description/

import java.util.Arrays;

/**
 *  705. Design HashSet
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Design a HashSet without using any built-in hash table libraries.
 *
 * Implement MyHashSet class:
 *
 * void add(key) Inserts the value key into the HashSet.
 * bool contains(key) Returns whether the value key exists in the HashSet or not.
 * void remove(key) Removes the value key in the HashSet. If key does not exist in the HashSet, do nothing.
 *
 *
 * Example 1:
 *
 * Input
 * ["MyHashSet", "add", "add", "contains", "contains", "add", "contains", "remove", "contains"]
 * [[], [1], [2], [1], [3], [2], [2], [2], [2]]
 * Output
 * [null, null, null, true, false, null, true, null, false]
 *
 * Explanation
 * MyHashSet myHashSet = new MyHashSet();
 * myHashSet.add(1);      // set = [1]
 * myHashSet.add(2);      // set = [1, 2]
 * myHashSet.contains(1); // return True
 * myHashSet.contains(3); // return False, (not found)
 * myHashSet.add(2);      // set = [1, 2]
 * myHashSet.contains(2); // return True
 * myHashSet.remove(2);   // set = [1]
 * myHashSet.contains(2); // return False, (already removed)
 *
 *
 * Constraints:
 *
 * 0 <= key <= 106
 * At most 104 calls will be made to add, remove, and contains.
 *
 *
 */
public class DesignHashSet {

    // V0
//    class MyHashSet {
//
//        public MyHashSet() {
//
//        }
//
//        public void add(int key) {
//
//        }
//
//        public void remove(int key) {
//
//        }
//
//        public boolean contains(int key) {
//
//        }
//    }

    /**
     * Your MyHashSet object will be instantiated and called as such:
     * MyHashSet obj = new MyHashSet();
     * obj.add(key);
     * obj.remove(key);
     * boolean param_3 = obj.contains(key);
     */

    // V1
    // https://leetcode.com/problems/design-hashset/solutions/6803076/cppjava-design-100-beats-easy-to-underst-diko/
    class MyHashSet_1 {

        private boolean[] storage;

        public MyHashSet_1() {
            storage = new boolean[1_000_001]; // Initialize array for keys in range [0, 1000000]
        }

        public void add(int key) {
            storage[key] = true; // Mark key as present
        }

        public void remove(int key) {
            storage[key] = false; // Mark key as absent
        }

        public boolean contains(int key) {
            return storage[key]; // Check if key is present
        }
    }


    // V2
    // https://leetcode.com/problems/design-hashset/solutions/3577264/python3-c-java-beats-97-by-abdullayev_ak-e83r/
    class MyHashSet_2 {
        private boolean[] mp;

        public MyHashSet_2() {
            mp = new boolean[1000001];
            Arrays.fill(mp, false);
        }

        public void add(int key) {
            mp[key] = true;
        }

        public void remove(int key) {
            mp[key] = false;
        }

        public boolean contains(int key) {
            return mp[key];
        }
    }


    // V3


}
