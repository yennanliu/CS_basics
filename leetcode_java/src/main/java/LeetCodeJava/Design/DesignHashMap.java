package LeetCodeJava.Design;

// https://leetcode.com/problems/design-hashmap/description/

import java.util.Arrays;

/**
 *  706. Design HashMap
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Design a HashMap without using any built-in hash table libraries.
 *
 * Implement the MyHashMap class:
 *
 * MyHashMap() initializes the object with an empty map.
 * void put(int key, int value) inserts a (key, value) pair into the HashMap. If the key already exists in the map, update the corresponding value.
 * int get(int key) returns the value to which the specified key is mapped, or -1 if this map contains no mapping for the key.
 * void remove(key) removes the key and its corresponding value if the map contains the mapping for the key.
 *
 *
 * Example 1:
 *
 * Input
 * ["MyHashMap", "put", "put", "get", "get", "put", "get", "remove", "get"]
 * [[], [1, 1], [2, 2], [1], [3], [2, 1], [2], [2], [2]]
 * Output
 * [null, null, null, 1, -1, null, 1, null, -1]
 *
 * Explanation
 * MyHashMap myHashMap = new MyHashMap();
 * myHashMap.put(1, 1); // The map is now [[1,1]]
 * myHashMap.put(2, 2); // The map is now [[1,1], [2,2]]
 * myHashMap.get(1);    // return 1, The map is now [[1,1], [2,2]]
 * myHashMap.get(3);    // return -1 (i.e., not found), The map is now [[1,1], [2,2]]
 * myHashMap.put(2, 1); // The map is now [[1,1], [2,1]] (i.e., update the existing value)
 * myHashMap.get(2);    // return 1, The map is now [[1,1], [2,1]]
 * myHashMap.remove(2); // remove the mapping for 2, The map is now [[1,1]]
 * myHashMap.get(2);    // return -1 (i.e., not found), The map is now [[1,1]]
 *
 *
 * Constraints:
 *
 * 0 <= key, value <= 106
 * At most 104 calls will be made to put, get, and remove.
 *
 *
 */
public class DesignHashMap {

    // V0
//    class MyHashMap {
//
//        public MyHashMap() {
//
//        }
//
//        public void put(int key, int value) {
//
//        }
//
//        public int get(int key) {
//
//        }
//
//        public void remove(int key) {
//
//        }
//    }

    // V1-1
    // IDEA: ARRAY
    // https://leetcode.com/problems/design-hashmap/solutions/1097755/js-python-java-c-updated-hash-array-solu-7mhg/
    class MyHashMap_1_1 {
        int[] data;

        public MyHashMap_1_1() {
            data = new int[1000001];
            Arrays.fill(data, -1);
        }

        public void put(int key, int val) {
            data[key] = val;
        }

        public int get(int key) {
            return data[key];
        }

        public void remove(int key) {
            data[key] = -1;
        }
    }

    // V1-2
    // IDEA: HASH
    // https://leetcode.com/problems/design-hashmap/solutions/1097755/js-python-java-c-updated-hash-array-solu-7mhg/
    class ListNode {
        int key, val;
        ListNode next;

        public ListNode(int key, int val, ListNode next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    class MyHashMap_1_2 {
        static final int size = 19997;
        static final int mult = 12582917;
        ListNode[] data;

        public MyHashMap_1_2() {
            this.data = new ListNode[size];
        }

        private int hash(int key) {
            return (int) ((long) key * mult % size);
        }

        public void put(int key, int val) {
            remove(key);
            int h = hash(key);
            ListNode node = new ListNode(key, val, data[h]);
            data[h] = node;
        }

        public int get(int key) {
            int h = hash(key);
            ListNode node = data[h];
            for (; node != null; node = node.next)
                if (node.key == key)
                    return node.val;
            return -1;
        }

        public void remove(int key) {
            int h = hash(key);
            ListNode node = data[h];
            if (node == null)
                return;
            if (node.key == key)
                data[h] = node.next;
            else
                for (; node.next != null; node = node.next)
                    if (node.next.key == key) {
                        node.next = node.next.next;
                        return;
                    }
        }
    }


    // V2
    // IDEA: HASH
    // https://leetcode.com/problems/design-hashmap/solutions/6279872/video-how-we-think-about-a-solution-pyth-m16w/
    class Node {
        int key;
        int val;
        Node next;

        Node(int key, int val) {
            this.key = key;
            this.val = val;
            this.next = null;
        }
    }

    class MyHashMap_1_3 {

        private Node[] map;

        public MyHashMap_1_3() {
            map = new Node[1000];
            for (int i = 0; i < 1000; i++) {
                map[i] = new Node(-1, -1);
            }
        }

        public void put(int key, int value) {
            int hash = hash(key);
            Node cur = map[hash];

            while (cur.next != null) {
                if (cur.next.key == key) {
                    cur.next.val = value;
                    return;
                }
                cur = cur.next;
            }

            cur.next = new Node(key, value);
        }

        public int get(int key) {
            int hash = hash(key);
            Node cur = map[hash].next;

            while (cur != null) {
                if (cur.key == key)
                    return cur.val;
                cur = cur.next;
            }

            return -1;
        }

        public void remove(int key) {
            int hash = hash(key);
            Node cur = map[hash];

            while (cur.next != null) {
                if (cur.next.key == key) {
                    cur.next = cur.next.next;
                    return;
                }
                cur = cur.next;
            }
        }

        private int hash(int key) {
            return key % 1000;
        }
    }




}
