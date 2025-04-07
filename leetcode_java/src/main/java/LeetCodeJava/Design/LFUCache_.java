package LeetCodeJava.Design;

// https://leetcode.com/problems/lfu-cache/

//import jdk.internal.net.http.common.Pair;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 460. LFU Cache
 * Solved
 * Hard
 * Topics
 * Companies
 * Design and implement a data structure for a Least Frequently Used (LFU) cache.
 *
 * Implement the LFUCache class:
 *
 * LFUCache(int capacity) Initializes the object with the capacity of the data structure.
 * int get(int key) Gets the value of the key if the key exists in the cache. Otherwise, returns -1.
 * void put(int key, int value) Update the value of the key if present, or inserts the key if not already present. When the cache reaches its capacity, it should invalidate and remove the least frequently used key before inserting a new item. For this problem, when there is a tie (i.e., two or more keys with the same frequency), the least recently used key would be invalidated.
 * To determine the least frequently used key, a use counter is maintained for each key in the cache. The key with the smallest use counter is the least frequently used key.
 *
 * When a key is first inserted into the cache, its use counter is set to 1 (due to the put operation). The use counter for a key in the cache is incremented either a get or put operation is called on it.
 *
 * The functions get and put must each run in O(1) average time complexity.
 *
 *
 *
 * Example 1:
 *
 * Input
 * ["LFUCache", "put", "put", "get", "put", "get", "get", "put", "get", "get", "get"]
 * [[2], [1, 1], [2, 2], [1], [3, 3], [2], [3], [4, 4], [1], [3], [4]]
 * Output
 * [null, null, null, 1, null, -1, 3, null, -1, 3, 4]
 *
 * Explanation
 * // cnt(x) = the use counter for key x
 * // cache=[] will show the last used order for tiebreakers (leftmost element is  most recent)
 * LFUCache lfu = new LFUCache(2);
 * lfu.put(1, 1);   // cache=[1,_], cnt(1)=1
 * lfu.put(2, 2);   // cache=[2,1], cnt(2)=1, cnt(1)=1
 * lfu.get(1);      // return 1
 *                  // cache=[1,2], cnt(2)=1, cnt(1)=2
 * lfu.put(3, 3);   // 2 is the LFU key because cnt(2)=1 is the smallest, invalidate 2.
 *                  // cache=[3,1], cnt(3)=1, cnt(1)=2
 * lfu.get(2);      // return -1 (not found)
 * lfu.get(3);      // return 3
 *                  // cache=[3,1], cnt(3)=2, cnt(1)=2
 * lfu.put(4, 4);   // Both 1 and 3 have the same cnt, but 1 is LRU, invalidate 1.
 *                  // cache=[4,3], cnt(4)=1, cnt(3)=2
 * lfu.get(1);      // return -1 (not found)
 * lfu.get(3);      // return 3
 *                  // cache=[3,4], cnt(4)=1, cnt(3)=3
 * lfu.get(4);      // return 4
 *                  // cache=[4,3], cnt(4)=2, cnt(3)=3
 *
 *
 * Constraints:
 *
 * 1 <= capacity <= 104
 * 0 <= key <= 105
 * 0 <= value <= 109
 * At most 2 * 105 calls will be made to get and put.
 *
 */
public class LFUCache_ {

    // V0
//    class LFUCache {
//
//        public LFUCache(int capacity) {
//
//        }
//
//        public int get(int key) {
//
//        }
//
//        public void put(int key, int value) {
//
//        }
//    }


    // V0-1
    class LFUCache_0_1 {
        int capacity;
        Map<Integer, Integer> kvMap; // Stores the key-value pairs
        Map<Integer, Integer> kFreqMap; // Stores the key-frequency pairs
        Map<Integer, LinkedHashSet<Integer>> freqMap; // Stores the keys by their frequency
        int minFreq;

        public LFUCache_0_1(int capacity) {
            this.capacity = capacity;
            this.kvMap = new HashMap<>();
            this.kFreqMap = new HashMap<>();
            this.freqMap = new HashMap<>();
            this.minFreq = 0;
        }

        public int get(int key) {
            if (!kvMap.containsKey(key)) {
                return -1;
            }
            // Get the current value and frequency of the key
            int val = kvMap.get(key);
            int freq = kFreqMap.get(key);

            // Remove the key from the current frequency set
            freqMap.get(freq).remove(key);

            // If the current frequency set is empty and it was the minimum frequency, update minFreq
            if (freqMap.get(freq).isEmpty()) {
                if (freq == minFreq) {
                    minFreq++;
                }
                freqMap.remove(freq);
            }

            // Increment the frequency of the key and add it to the new frequency set
            kFreqMap.put(key, freq + 1);
            /**
             *   1) computeIfAbsent(1, k -> new LinkedHashSet<>()):
             *
             *   -> This method checks whether the map already contains the key 1.
             *     If it doesn't, it computes a new value for this key using the provided lambda
             *     expression (k -> new LinkedHashSet<>()), which creates a new empty LinkedHashSet.
             *
             *   -> If the key 1 is not already present, it inserts a new LinkedHashSet
             *      as the value for this key.
             *
             *   -> If the key 1 already exists, it simply returns the existing value for
             *      that key (the LinkedHashSet associated with 1).
             *
             */
            freqMap.computeIfAbsent(freq + 1, k -> new LinkedHashSet<>()).add(key);

            return val;
        }

        public void put(int key, int value) {
            if (capacity <= 0)
                return;

            if (kvMap.containsKey(key)) {
                // Update the value of the key and its frequency
                kvMap.put(key, value);
                get(key); // Call get to update frequency
                return;
            }

            if (kvMap.size() >= capacity) {
                // Remove the least frequently used key
                LinkedHashSet<Integer> minFreqSet = freqMap.get(minFreq);
                Integer evictKey = minFreqSet.iterator().next();
                minFreqSet.remove(evictKey);
                kvMap.remove(evictKey);
                kFreqMap.remove(evictKey);
            }

            // Add the new key-value pair and set its frequency to 1
            kvMap.put(key, value);
            kFreqMap.put(key, 1);
            freqMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
            minFreq = 1; // Reset minFreq to 1 since we just added a new key with frequency 1
        }
    }


    // V1
    // https://www.youtube.com/watch?v=bLEIHn-DgoA


    // V2
    // https://leetcode.com/problems/lfu-cache/editorial/
    // IDEA: 2 HASHMAP
//    class LFUCache_2 {
//
//        // key: original key, value: frequency and original value.
//        private Map<Integer, Pair<Integer, Integer>> cache;
//        // key: frequency, value: All keys that have the same frequency.
//        private Map<Integer, LinkedHashSet<Integer>> frequencies;
//        private int minf;
//        private int capacity;
//
//        private void insert(int key, int frequency, int value) {
//            cache.put(key, new Pair<>(frequency, value));
//            frequencies.putIfAbsent(frequency, new LinkedHashSet<>());
//            frequencies.get(frequency).add(key);
//        }
//
//        public LFUCache_2(int capacity) {
//            cache = new HashMap<>();
//            frequencies = new HashMap<>();
//            minf = 0;
//            this.capacity = capacity;
//        }
//
//        public int get(int key) {
//            Pair<Integer, Integer> frequencyAndValue = cache.get(key);
//            if (frequencyAndValue == null) {
//                return -1;
//            }
//            final int frequency = frequencyAndValue.getKey();
//            final Set<Integer> keys = frequencies.get(frequency);
//            keys.remove(key);
//            if (keys.isEmpty()) {
//                frequencies.remove(frequency);
//
//                if (minf == frequency) {
//                    ++minf;
//                }
//            }
//            final int value = frequencyAndValue.getValue();
//            insert(key, frequency + 1, value);
//            return value;
//        }
//
//        public void put(int key, int value) {
//            if (capacity <= 0) {
//                return;
//            }
//            Pair<Integer, Integer> frequencyAndValue = cache.get(key);
//            if (frequencyAndValue != null) {
//                cache.put(key, new Pair<>(frequencyAndValue.getKey(), value));
//                get(key);
//                return;
//            }
//            if (capacity == cache.size()) {
//                final Set<Integer> keys = frequencies.get(minf);
//                final int keyToDelete = keys.iterator().next();
//                cache.remove(keyToDelete);
//                keys.remove(keyToDelete);
//                if (keys.isEmpty()) {
//                    frequencies.remove(minf);
//                }
//            }
//            minf = 1;
//            insert(key, 1, value);
//        }
//    }

    // V3
    // https://leetcode.com/problems/lfu-cache/solutions/3111521/o1-time-full-explanation-hashtable-c-jav-ar7e/
    class LFUCache_3 {
        public LFUCache_3(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            if (!keyToVal.containsKey(key))
                return -1;

            final int freq = keyToFreq.get(key);
            freqToLRUKeys.get(freq).remove(key);
            if (freq == minFreq && freqToLRUKeys.get(freq).isEmpty()) {
                freqToLRUKeys.remove(freq);
                ++minFreq;
            }

            // Increase key's freq by 1
            // Add this key to next freq's list
            putFreq(key, freq + 1);
            return keyToVal.get(key);
        }

        public void put(int key, int value) {
            if (capacity == 0)
                return;
            if (keyToVal.containsKey(key)) {
                keyToVal.put(key, value);
                get(key); // Update key's count
                return;
            }

            if (keyToVal.size() == capacity) {
                // Evict LRU key from the minFreq list
                final int keyToEvict = freqToLRUKeys.get(minFreq).iterator().next();
                freqToLRUKeys.get(minFreq).remove(keyToEvict);
                keyToVal.remove(keyToEvict);
            }

            minFreq = 1;
            putFreq(key, minFreq);    // Add new key and freq
            keyToVal.put(key, value); // Add new key and value
        }

        private int capacity;
        private int minFreq = 0;
        private Map<Integer, Integer> keyToVal = new HashMap<>();
        private Map<Integer, Integer> keyToFreq = new HashMap<>();
        private Map<Integer, LinkedHashSet<Integer>> freqToLRUKeys = new HashMap<>();

        private void putFreq(int key, int freq) {
            keyToFreq.put(key, freq);
            freqToLRUKeys.putIfAbsent(freq, new LinkedHashSet<>());
            freqToLRUKeys.get(freq).add(key);
        }
    }

    // V4
    // https://leetcode.com/problems/lfu-cache/solutions/3111462/95-faster-java-code-by-coding_menance-gli3/
    // IDEA: DoublyLinkedList
    class Node {
        int key;
        int val;
        Node next;
        Node prev;
        int freq = 1;

        Node(int k, int v) {
            key = k;
            val = v;
        }
    }

    class DoublyLinkedList {
        Node head;
        Node tail;

        DoublyLinkedList() {
            head = new Node(-1, -1);
            tail = new Node(-1, -1);
            head.next = tail;
            tail.prev = head;
        }

        void addNode(Node v) {
            Node next = head.next;
            head.next = v;
            v.prev = head;
            head.next = v;
            v.next = next;
            next.prev = v;
        }

        Node removeNode() {
            Node node = tail.prev;
            node.prev.next = tail;
            tail.prev = node.prev;
            return node;
        }

        Node removeNode(Node v) {
            Node prev = v.prev;
            Node next = v.next;
            prev.next = next;
            next.prev = prev;
            return v;
        }

        boolean isEmpty() {
            if (head.next == tail)
                return true;
            return false;
        }
    }

    class LFUCache_4 {
        HashMap<Integer, DoublyLinkedList> freqList = new HashMap<Integer, DoublyLinkedList>();
        HashMap<Integer, Node> lfuCache = new HashMap<Integer, Node>();
        int capacity;
        int minFreq;

        public LFUCache_4(int capacity) {
            this.capacity = capacity;
            minFreq = 1;
        }

        public int get(int key) {
            if (lfuCache.get(key) == null)
                return -1;
            Node v = lfuCache.get(key);
            freqList.get(v.freq).removeNode(v);
            if (freqList.get(v.freq).isEmpty()) {
                if (minFreq == v.freq) {
                    minFreq = v.freq + 1;
                }
            }
            v.freq += 1;
            if (freqList.get(v.freq) == null) {
                DoublyLinkedList d = new DoublyLinkedList();
                d.addNode(v);
                freqList.put(v.freq, d);
            } else {
                freqList.get(v.freq).addNode(v);
            }
            return v.val;
        }

        public void put(int key, int value) {
            if (capacity == 0)
                return;
            if (lfuCache.get(key) != null) {
                Node v = lfuCache.get(key);
                freqList.get(v.freq).removeNode(v);
                if (freqList.get(v.freq).isEmpty()) {
                    if (minFreq == v.freq)
                        minFreq = v.freq + 1;
                }
                v.freq += 1;
                if (freqList.get(v.freq) == null) {
                    DoublyLinkedList d = new DoublyLinkedList();
                    d.addNode(v);
                    freqList.put(v.freq, d);
                } else {
                    freqList.get(v.freq).addNode(v);
                }
                v.val = value;
            } else {
                if (lfuCache.size() == capacity) {
                    Node v = freqList.get(minFreq).removeNode();
                    lfuCache.remove(v.key);
                }
                Node newNode = new Node(key, value);
                lfuCache.put(key, newNode);
                if (freqList.get(1) != null) {
                    freqList.get(1).addNode(newNode);
                } else {
                    DoublyLinkedList d = new DoublyLinkedList();
                    d.addNode(newNode);
                    freqList.put(1, d);
                }
                minFreq = 1;
            }
        }
    }


}
