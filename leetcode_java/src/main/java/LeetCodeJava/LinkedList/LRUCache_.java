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
    // TODO : implement
//    class LRUCache {
//
//        public LRUCache(int capacity) {
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
