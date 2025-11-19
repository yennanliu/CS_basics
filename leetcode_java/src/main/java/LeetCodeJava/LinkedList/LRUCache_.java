package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/lru-cache/description/
/**
 * 146. LRU Cache
 * Solved
 * Medium
 * Topics
 * Companies
 * Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.
 *
 * Implement the LRUCache class:
 *
 * LRUCache(int capacity) Initialize the LRU cache with positive size capacity.
 * int get(int key) Return the value of the key if the key exists, otherwise return -1.
 * void put(int key, int value) Update the value of the key if the key exists. Otherwise, add the key-value pair to the cache. If the number of keys exceeds the capacity from this operation, evict the least recently used key.
 * The functions get and put must each run in O(1) average time complexity.
 *
 *
 *
 * Example 1:
 *
 * Input
 * ["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
 * [[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
 * Output
 * [null, null, null, 1, null, -1, null, -1, 3, 4]
 *
 * Explanation
 * LRUCache lRUCache = new LRUCache(2);
 * lRUCache.put(1, 1); // cache is {1=1}
 * lRUCache.put(2, 2); // cache is {1=1, 2=2}
 * lRUCache.get(1);    // return 1
 * lRUCache.put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
 * lRUCache.get(2);    // returns -1 (not found)
 * lRUCache.put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
 * lRUCache.get(1);    // return -1 (not found)
 * lRUCache.get(3);    // return 3
 * lRUCache.get(4);    // return 4
 *
 *
 * Constraints:
 *
 * 1 <= capacity <= 3000
 * 0 <= key <= 104
 * 0 <= value <= 105
 * At most 2 * 105 calls will be made to get and put.
 *
 *
 */

import java.util.*;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */

class ListNode {
    int key;
    int val;
    ListNode next;
    ListNode prev;

    public ListNode(int key, int val) {
        this.key = key;
        this.val = val;
    }
}

public class LRUCache_ {

    // V0
    // IDEA: HASHMAP + DEQUE (fixed by gpt)
    /**
     *  NOTE !!!
     *
     *   1) for dequeue, the `most RECENT` used element is at the `END`
     *       -> this.dq.addLast()
     *
     *   2) for dequeue, the `least` used element is at the `HEAD`
     *      -> this.dq.pollFirst()
     *
     */
    class LRUCache {
        int capacity;
        Map<Integer, Integer> map;
        Deque<Integer> dq;

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.map = new HashMap<>();
            this.dq = new LinkedList<>();
        }

        public int get(int key) {
            if (!this.map.containsKey(key)) {
                return -1;
            }
            // Get the value from the map
            int val = this.map.get(key);

            // Update the deque: remove and add the key to mark it as recently used
            this.dq.remove(key);
            // 1) for dequeue, the `most RECENT` used element is at the `END`
            this.dq.addLast(key);

            return val;
        }

        public void put(int key, int value) {
            if (this.map.containsKey(key)) {
                // If the key exists, update the value and move the key to the end
                this.map.put(key, value);
                // Remove the old key and re-add it as the most recently used
                this.dq.remove(key);
                // 1) for dequeue, the `most RECENT` used element is at the `END`
                this.dq.addLast(key);
            } else {
                // If the cache is full, remove the least recently used key
                if (this.dq.size() >= this.capacity) {
                    //2) for dequeue, the `least` used element is at the `HEAD`
                    int leastUsedKey = this.dq.pollFirst();
                    this.map.remove(leastUsedKey);
                }
                // Add the new key-value pair
                this.map.put(key, value);
                // 1) for dequeue, the `most RECENT` used element is at the `END`
                this.dq.addLast(key);
            }
        }
    }


    // V0-1
    // IDEA : LinkedHashMap (OrderedDict) (gpt)
    class LRUCache_0_1 {
        private int capacity;
        private LinkedHashMap<Integer, Integer> lruCache;

        public LRUCache_0_1(int capacity) {

            this.capacity = capacity;

            /**
             *  0.75f is the load factor.
             *
             *  By using 0.75f, you ensure a balance between space efficiency and performance in terms of access speed, which is particularly important for operations that are common in an LRU cache like this one.
             *
             *  The 0.75f load factor is a compromise that provides a good balance between memory usage and performance.
             *
             *  It ensures that the hash table will resize when it is 75% full, which helps maintain efficient access times without wasting too much memory.
             */
            this.lruCache = new LinkedHashMap<Integer, Integer>(capacity, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                    return size() > LRUCache_0_1.this.capacity;
                }
            };
        }

        public int get(int key) {
            if (!lruCache.containsKey(key)) {
                return -1;
            } else {
                return lruCache.get(key);
            }
        }

        public void put(int key, int value) {
            lruCache.put(key, value);
        }
    }

    // V0-2
    // IDEA : DOUBLE LINKED LIST (gpt)
    class LRUCache0_2 {
        private class Node {
            int key;
            int value;
            Node next;
            Node prev;

            Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }

        private int capacity;
        private HashMap<Integer, Node> cache;
        private Node head;
        private Node tail;

        public LRUCache0_2(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>(capacity);
            this.head = new Node(-1, -1);
            this.tail = new Node(-1, -1);
            head.next = tail;
            tail.prev = head;
        }

        public int get(int key) {
            if (!cache.containsKey(key)) {
                return -1;
            }
            Node node = cache.get(key);
            removeNode(node);
            addToHead(node);
            return node.value;
        }

        public void put(int key, int value) {
            if (cache.containsKey(key)) {
                Node node = cache.get(key);
                node.value = value;
                removeNode(node);
                addToHead(node);
            } else {
                if (cache.size() >= capacity) {
                    cache.remove(tail.prev.key);
                    removeNode(tail.prev);
                }
                Node newNode = new Node(key, value);
                cache.put(key, newNode);
                addToHead(newNode);
            }
        }

        private void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        private void addToHead(Node node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }
    }

    // V0-3
    // IDEA: LINED LIST, MAP  (fixed by gpt)
    class LRUCache_0_0_3 {

        class Node {
            int key, val;
            Node prev, next;

            Node(int k, int v) {
                key = k;
                val = v;
            }
        }

        private final int capacity;
        private Map<Integer, Node> map;
        private Node head, tail;

        public LRUCache_0_0_3(int capacity) {
            this.capacity = capacity;
            this.map = new HashMap<>();

            // dummy head & tail
            head = new Node(0, 0);
            tail = new Node(0, 0);
            head.next = tail;
            tail.prev = head;
        }

        public int get(int key) {
            if (!map.containsKey(key))
                return -1;

            Node node = map.get(key);
            remove(node);
            addFirst(node);

            return node.val;
        }

        public void put(int key, int value) {
            if (map.containsKey(key)) {
                Node node = map.get(key);
                node.val = value;

                remove(node);
                addFirst(node);
                return;
            }

            // new key
            if (map.size() == capacity) {
                // remove LRU = tail.prev
                Node lru = tail.prev;
                remove(lru);
                map.remove(lru.key);
            }

            Node newNode = new Node(key, value);
            addFirst(newNode);
            map.put(key, newNode);
        }

        // helper: remove node from list
        private void remove(Node n) {
            n.prev.next = n.next;
            n.next.prev = n.prev;
        }

        // helper: add to head (most recent)
        private void addFirst(Node n) {
            n.next = head.next;
            n.prev = head;
            head.next.prev = n;
            head.next = n;
        }
    }

    // V0-4
    // IDEA: LinkedHashMap (gemini)
    class LRUCache_0_4 {

        // Use LinkedHashMap which provides O(1) time complexity for put, get, and removal.
        // It maintains insertion order by default, but we will configure it for access order.
        private LinkedHashMap<Integer, Integer> cache;
        private int capacity;

        public LRUCache_0_4(int capacity) {
            this.capacity = capacity;

            // Initialize LinkedHashMap:
            // 1. initialCapacity: size of the map (can be anything reasonable)
            // 2. loadFactor: standard 0.75f
            // 3. accessOrder: TRUE means the linked list part is ordered by access (LRU),
            //    where accessed/modified entries move to the end (MRU side).
            this.cache = new LinkedHashMap<Integer, Integer>(capacity, 0.75f, true) {

                // Override this method to remove the oldest entry when the cache exceeds capacity.
                // This method is called after a put/putAll operation.
                @Override
                protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                    // Return true if the map size is greater than the specified capacity.
                    return size() > LRUCache_0_4.this.capacity;
                }
            };
        }

        public int get(int key) {
            // LinkedHashMap's get() method automatically moves the accessed entry
            // to the MRU end when accessOrder=true.
            return cache.getOrDefault(key, -1);
        }

        public void put(int key, int value) {
            // LinkedHashMap's put() method handles three things:
            // 1. If key exists, it updates the value AND moves the entry to the MRU end.
            // 2. If key doesn't exist, it adds the new entry to the MRU end.
            // 3. It calls removeEldestEntry() to handle capacity overflow.
            cache.put(key, value);
        }
    }


    // V1-1
    // https://youtu.be/7ABFKPK2hD4?feature=shared
    // https://neetcode.io/problems/lru-cache
    // IDEA: BRUTE FORCE
    public class LRUCache_1_1 {

        private ArrayList<int[]> cache;
        private int capacity;

        public LRUCache_1_1(int capacity) {
            this.cache = new ArrayList<>();
            this.capacity = capacity;
        }

        public int get(int key) {
            for (int i = 0; i < cache.size(); i++) {
                if (cache.get(i)[0] == key) {
                    int[] tmp = cache.remove(i);
                    cache.add(tmp);
                    return tmp[1];
                }
            }
            return -1;
        }

        public void put(int key, int value) {
            for (int i = 0; i < cache.size(); i++) {
                if (cache.get(i)[0] == key) {
                    int[] tmp = cache.remove(i);
                    tmp[1] = value;
                    cache.add(tmp);
                    return;
                }
            }

            if (capacity == cache.size()) {
                cache.remove(0);
            }

            cache.add(new int[]{key, value});
        }
    }

    // V1-2
    // https://youtu.be/7ABFKPK2hD4?feature=shared
    // https://neetcode.io/problems/lru-cache
    // IDEA:  Doubly Linked List
    public class Node {
        int key;
        int val;
        Node prev;
        Node next;

        public Node(int key, int val) {
            this.key = key;
            this.val = val;
            this.prev = null;
            this.next = null;
        }
    }

    public class LRUCache_1_2 {

        private int cap;
        private HashMap<Integer, Node> cache;
        private Node left;
        private Node right;

        public LRUCache_1_2(int capacity) {
            this.cap = capacity;
            this.cache = new HashMap<>();
            this.left = new Node(0, 0);
            this.right = new Node(0, 0);
            this.left.next = this.right;
            this.right.prev = this.left;
        }

        private void remove(Node node) {
            Node prev = node.prev;
            Node nxt = node.next;
            prev.next = nxt;
            nxt.prev = prev;
        }

        private void insert(Node node) {
            Node prev = this.right.prev;
            prev.next = node;
            node.prev = prev;
            node.next = this.right;
            this.right.prev = node;
        }

        public int get(int key) {
            if (cache.containsKey(key)) {
                Node node = cache.get(key);
                remove(node);
                insert(node);
                return node.val;
            }
            return -1;
        }

        public void put(int key, int value) {
            if (cache.containsKey(key)) {
                remove(cache.get(key));
            }
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            insert(newNode);

            if (cache.size() > cap) {
                Node lru = this.left.next;
                remove(lru);
                cache.remove(lru.key);
            }
        }
    }

    // V1-3
    // https://youtu.be/7ABFKPK2hD4?feature=shared
    // https://neetcode.io/problems/lru-cache
    // IDEA:   Built-In Data Structure
//    public class LRUCache_1_3 {
//        private final Map<Integer, Integer> cache;
//        private final int capacity;
//
//        public LRUCache_1_3(int capacity) {
//            this.capacity = capacity;
//            this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
//                protected boolean removeEldestEntry(Map.Entry eldest) {
//                    return size() > LRUCache_1_3.this.capacity;
//                }
//            };
//        }
//
//        public int get(int key) {
//            return cache.getOrDefault(key, -1);
//        }
//
//        public void put(int key, int value) {
//            cache.put(key, value);
//        }
//    }

    // V2
    // IDEA :  DOUBLE LINKED LIST
    // https://leetcode.com/problems/lru-cache/editorial/
    class LRUCache_2 {
        int capacity;
        Map<Integer, ListNode> dic;
        ListNode head;
        ListNode tail;

        public LRUCache_2(int capacity) {
            this.capacity = capacity;
            dic = new HashMap<>();
            head = new ListNode(-1, -1);
            tail = new ListNode(-1, -1);
            head.next = tail;
            tail.prev = head;
        }

        public int get(int key) {
            if (!dic.containsKey(key)) {
                return -1;
            }

            ListNode node = dic.get(key);
            remove(node);
            add(node);
            return node.val;
        }

        public void put(int key, int value) {
            if (dic.containsKey(key)) {
                ListNode oldNode = dic.get(key);
                remove(oldNode);
            }

            ListNode node = new ListNode(key, value);
            dic.put(key, node);
            add(node);

            if (dic.size() > capacity) {
                ListNode nodeToDelete = head.next;
                remove(nodeToDelete);
                dic.remove(nodeToDelete.key);
            }
        }

        public void add(ListNode node) {
            ListNode previousEnd = tail.prev;
            previousEnd.next = node;
            node.prev = previousEnd;
            node.next = tail;
            tail.prev = node;
        }

        public void remove(ListNode node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }


    // V3
    // IDEA : BUILD IN FUNC
    // https://leetcode.com/problems/lru-cache/editorial/
    class LRUCache_3 {
        int capacity;
        LinkedHashMap<Integer, Integer> dic;

        public LRUCache_3(int capacity) {
            this.capacity = capacity;
            dic = new LinkedHashMap<Integer, Integer>(5, 0.75f, true) {
                //@Override
                protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                    return size() > capacity;
                }
            };
        }

        public int get(int key) {
            return dic.getOrDefault(key, -1);
        }

        public void put(int key, int value) {
            dic.put(key, value);
        }

    }




}
